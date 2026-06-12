package client;

import common.Request;
import common.Response;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * Manages non-blocking TCP connection to the server.
 * Handles connect, send, receive with retry logic.
 */
public class ServerConnection {

    private static final int BUFFER_SIZE = 65536;
    private static final int READ_TIMEOUT_MS = 5000;

    private final String host;
    private final int port;
    private final int maxRetries;
    private final int retryDelayMs;

    private SocketChannel channel;

    public ServerConnection(String host, int port, int maxRetries, int retryDelayMs) {
        this.host = host;
        this.port = port;
        this.maxRetries = maxRetries;
        this.retryDelayMs = retryDelayMs;
    }

    /** Connects to server with retry (maxRetries attempts). */
    public boolean connect() {
        for (int attempt = 1; attempt <= maxRetries; attempt++) {
            System.out.printf("Connecting to %s:%d (attempt %d/%d)...%n",
                    host, port, attempt, maxRetries);
            if (connectOnce()) return true;
            if (attempt < maxRetries) {
                System.out.printf("Retrying in %ds...%n", retryDelayMs / 1000);
                sleep(retryDelayMs);
            }
        }
        return false;
    }

    /** Single connection attempt. */
    private boolean connectOnce() {
        try {
            channel = SocketChannel.open();
            channel.configureBlocking(false);
            channel.connect(new InetSocketAddress(host, port));
            while (!channel.finishConnect()) sleep(100);
            System.out.println("Connected.");
            return true;
        } catch (IOException e) {
            System.out.println("Failed: " + e.getMessage());
            return false;
        }
    }

    /** Sends request and receives response with retry logic. */
    public Response sendAndReceive(Request request) throws IOException {
        for (int attempt = 1; attempt <= maxRetries; attempt++) {
            try {
                if (channel == null || !channel.isConnected()) {
                    System.out.println("Connecting (attempt " + attempt + "/" + maxRetries + ")...");
                    if (!connectOnce()) {
                        if (attempt < maxRetries) sleep(retryDelayMs);
                        continue;
                    }
                }

                send(request);
                Response response = receive();
                if (response != null) return response;

            } catch (IOException e) {
                System.out.println("Error (attempt " + attempt + "/" + maxRetries + "): " + e.getMessage());
                try { if (channel != null) channel.close(); } catch (IOException ignored) {}
                channel = null;
                if (attempt < maxRetries) sleep(retryDelayMs);
            }
        }
        throw new IOException("Server unavailable after " + maxRetries + " attempts.");
    }

    /** Serializes and sends request to server. */
    public void send(Request request) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (ObjectOutputStream oos = new ObjectOutputStream(baos)) {
            oos.writeObject(request);
        }
        channel.write(ByteBuffer.wrap(baos.toByteArray()));
    }

    /** Reads and deserializes response from server. */
    public Response receive() throws IOException {
        ByteBuffer buf = ByteBuffer.allocate(BUFFER_SIZE);
        long deadline = System.currentTimeMillis() + READ_TIMEOUT_MS;
        int read = 0;

        while (read <= 0 && System.currentTimeMillis() < deadline) {
            read = channel.read(buf);
            if (read == 0) sleep(50);
        }

        if (read <= 0) {
            System.out.println("Server did not respond in time.");
            return null;
        }

        buf.flip();
        byte[] data = new byte[buf.remaining()];
        buf.get(data);

        try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(data))) {
            return (Response) ois.readObject();
        } catch (ClassNotFoundException e) {
            System.out.println("Could not parse server response.");
            return null;
        }
    }

    /** Returns true if connected to server. */
    public boolean isConnected() {
        return channel != null && channel.isConnected();
    }

    /** Closes the connection. */
    public void close() {
        try { if (channel != null) channel.close(); }
        catch (IOException ignored) {}
    }

    private void sleep(long ms) {
        try { Thread.sleep(ms); }
        catch (InterruptedException e) { Thread.currentThread().interrupt(); }
    }
}