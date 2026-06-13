package common;

import java.io.Serializable;

public enum CommandType implements Serializable {
    // Auth commands
    REGISTER,
    LOGIN,

    // Collection commands
    ADD,
    ADD_IF_MIN,
    UPDATE,
    REMOVE_BY_ID,
    REMOVE_LAST,
    CLEAR,
    SHOW,
    INFO,
    SHUFFLE,
    COUNT_BY_PRICE,
    PRINT_DESCENDING,
    PRINT_FIELD_DESCENDING_MANUFACTURER,
    HELP,
    EXECUTE_SCRIPT,
    CHECK_ID,
    SERVER_SAVE,
    CHECK_OWNER
}
