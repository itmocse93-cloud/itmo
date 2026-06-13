package util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Centralised logging facade (Log4J2 bonus task).
 */
public final class LoggerConfig {

    private static final Logger SERVER     = LogManager.getLogger("Server");
    private static final Logger CONNECTION = LogManager.getLogger("Connection");
    private static final Logger COMMAND    = LogManager.getLogger("Command");
    private static final Logger ERROR      = LogManager.getLogger("Error");

    private LoggerConfig() {}

    public static void logServerStart(int port, String file) {
        SERVER.info("STARTING  port={} file={}", port, file);
    }

    public static void logServerReady() {
        SERVER.info("READY  - waiting for connections");
    }

    public static void logServerShutdown() {
        SERVER.info("SHUTDOWN");
    }

    public static void logConnected(String addr) {
        CONNECTION.info("CONNECTED     {}", addr);
    }

    public static void logDisconnected(String addr) {
        CONNECTION.info("DISCONNECTED  {}", addr);
    }

    public static void logRequest(String addr, String cmd) {
        COMMAND.info("REQUEST   client={} cmd={}", addr, cmd);
    }

    public static void logCompleted(String cmd, long ms) {
        COMMAND.info("COMPLETED cmd={} {}ms", cmd, ms);
    }

    public static void logResponse(String addr, String cmd, boolean ok) {
        COMMAND.info("RESPONSE  client={} cmd={} ok={}", addr, cmd, ok);
    }

    public static void logSaved(String file, int count) {
        SERVER.info("SAVED  file={} count={}", file, count);
    }

    public static void logLoaded(String file, int count) {
        SERVER.info("LOADED file={} count={}", file, count);
    }

    public static void logError(String where, String msg, Exception e) {
        ERROR.error("ERROR [{}] {} - {}", where, msg, e.getMessage());
    }

    public static void logError(String where, String msg) {
        ERROR.error("ERROR [{}] {}", where, msg);
    }

    public static void logWarn(String where, String msg) {
        ERROR.warn("WARN  [{}] {}", where, msg);
    }
}