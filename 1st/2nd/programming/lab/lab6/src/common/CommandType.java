package common;

import java.io.Serializable;

/**
 * All command types shared between client and server.
 */
public enum CommandType implements Serializable {
    HELP,
    INFO,
    SHOW,
    ADD,
    UPDATE,
    REMOVE_BY_ID,
    CLEAR,
    REMOVE_LAST,
    ADD_IF_MIN,
    SHUFFLE,
    COUNT_BY_PRICE,
    PRINT_DESCENDING,
    PRINT_FIELD_DESCENDING_MANUFACTURER,
    EXECUTE_SCRIPT,
    CHECK_ID,
    SERVER_SAVE
}
