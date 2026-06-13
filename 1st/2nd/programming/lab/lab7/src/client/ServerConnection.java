package client;

import common.Request;
import common.Response;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class ServerConnection {

    private final String host;
    private final int port;
    private SocketChannel channel;
    private static final int MAX_RETRIES = 5;
    private static final long RETRY_DELAY_MS = 2000;

    public ServerConnection(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void connect() throws IOException {
        for (int attempt = 1; attempt <= MAX_RETRIES; attempt++) {
            if (attempt > 1) {
                System.out.printf("Connecting to %s:%d (attempt %d/%d)...%n",
                        host, port, attempt, MAX_RETRIES);
            }
            try {
                channel = SocketChannel.open();
                channel.configureBlocking(true);
                channel.connect(new InetSocketAddress(host, port));
                if (attempt > 1) {
                    System.out.println("Connected.");
                }
                return;
            } catch (IOException e) {
                if (attempt < MAX_RETRIES) {
                    System.out.printf("Retrying in %ds...%n", RETRY_DELAY_MS / 1000);
                    try { Thread.sleep(RETRY_DELAY_MS); } catch (InterruptedException ignored) {}
                } else {
                    throw new IOException("Could not connect after " + MAX_RETRIES + " attempts.");
                }
            }
        }
    }

    public void send(Request request) throws IOException {
        // Reconnect if needed
        if (channel == null || !channel.isConnected()) connect();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (ObjectOutputStream oos = new ObjectOutputStream(baos)) {
            oos.writeObject(request);
        }
        byte[] bytes = baos.toByteArray();
        ByteBuffer buf = ByteBuffer.allocate(4 + bytes.length);
        buf.putInt(bytes.length);
        buf.put(bytes);
        buf.flip();
        while (buf.hasRemaining()) channel.write(buf);
    }

    public Response receive() throws IOException {
        ByteBuffer lenBuf = ByteBuffer.allocate(4);
        long start = System.currentTimeMillis();
        while (lenBuf.hasRemaining()) {
            if (System.currentTimeMillis() - start > 5000) throw new IOException("Server did not respond in time.");
            channel.read(lenBuf);
        }
        lenBuf.flip();
        int length = lenBuf.getInt();

        ByteBuffer dataBuf = ByteBuffer.allocate(length);
        while (dataBuf.hasRemaining()) channel.read(dataBuf);
        dataBuf.flip();
        byte[] bytes = new byte[length];
        dataBuf.get(bytes);

        try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(bytes))) {
            return (Response) ois.readObject();
        } catch (ClassNotFoundException e) {
            throw new IOException("Cannot deserialize response", e);
        } finally {
            // Close and nullify so next request reconnects
            try { channel.close(); } catch (IOException ignored) {}
            channel = null;
        }
    }

    public Response sendAndReceive(Request request) throws IOException {
        send(request);
        return receive();
    }

    public boolean isConnected() {
        return channel != null && channel.isConnected();
    }

    public void close() {
        if (channel != null) {
            try { channel.close(); } catch (IOException ignored) {}
        }
    }
}
