package server;

import command.CommandDispatcher;
import common.Request;
import common.Response;
import util.LoggerConfig;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * Handles one client request cycle.
 *
 * Module 2 - deserialize the incoming request.
 * Module 3 - dispatch to the appropriate Command.
 * Module 4 - serialize and write the response.
 */
public class RequestHandler {

    private static final int BUFFER_SIZE = 65536;

    private final CommandDispatcher dispatcher;

    public RequestHandler(CommandDispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }

    /**
     * @return false when the client has disconnected and the channel should be closed
     */
    public boolean handle(SocketChannel channel) {
        String addr = remoteAddress(channel);
        try {
            byte[] raw = readRaw(channel);
            if (raw == null) {
                LoggerConfig.logDisconnected(addr);
                return false;
            }

            // Module 2 - deserialize
            Request request = deserialize(raw);
            LoggerConfig.logRequest(addr, request.getCommandType().toString());

            // Module 3 - execute
            long     start    = System.currentTimeMillis();
            Response response = dispatcher.execute(request);
            LoggerConfig.logCompleted(request.getCommandType().toString(),
                    System.currentTimeMillis() - start);

            // Module 4 - send
            writeRaw(channel, serialize(response));
            LoggerConfig.logResponse(addr, request.getCommandType().toString(), response.isSuccess());
            return true;

        } catch (ClassNotFoundException e) {
            LoggerConfig.logError("RequestHandler", "Unknown class in request", e);
            trySendError(channel, "Invalid request format.");
            return true;
        } catch (IOException e) {
            LoggerConfig.logWarn("RequestHandler", "IO error: " + e.getMessage());
            return false;
        }
    }


    private byte[] readRaw(SocketChannel channel) throws IOException {
        ByteBuffer buf  = ByteBuffer.allocate(BUFFER_SIZE);
        int        read = channel.read(buf);
        if (read == -1) return null;
        buf.flip();
        byte[] data = new byte[buf.remaining()];
        buf.get(data);
        return data;
    }

    private Request deserialize(byte[] data) throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(data))) {
            return (Request) ois.readObject();
        }
    }

    private byte[] serialize(Response response) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (ObjectOutputStream oos = new ObjectOutputStream(baos)) {
            oos.writeObject(response);
        }
        return baos.toByteArray();
    }

    private void writeRaw(SocketChannel channel, byte[] data) throws IOException {
        channel.write(ByteBuffer.wrap(data));
    }

    private void trySendError(SocketChannel channel, String message) {
        try { writeRaw(channel, serialize(new Response(false, message))); }
        catch (IOException ignored) {}
    }

    private String remoteAddress(SocketChannel channel) {
        try { return channel.getRemoteAddress().toString(); }
        catch (IOException e) { return "unknown"; }
    }
}
