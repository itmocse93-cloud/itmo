package command;

import common.Request;
import common.Response;

public interface Command {
    Response execute(Request request);
    String getName();
    String getDescription();
    default boolean isVisibleInHelp() { return true; }
}
