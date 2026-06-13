package command;

import common.CommandType;
import common.Request;
import common.Response;

import java.util.Map;

public class HelpCommand implements Command {

    private final Map<CommandType, Command> commands;

    public HelpCommand(Map<CommandType, Command> commands) {
        this.commands = commands;
    }

    @Override
    public Response execute(Request request) {
        StringBuilder sb = new StringBuilder("Available commands:\n");
        commands.values().stream()
                .filter(Command::isVisibleInHelp)
                .forEach(cmd -> sb.append(String.format("  %s%n",
                        cmd.getDescription())));

        sb.append(String.format("  %-1s - %s%n",
                "logout", "Log out and return to login screen"));
        sb.append(String.format("  %-1s - %s%n",
                "exit", "Exit the program"));


        return Response.ok(sb.toString().trim());

    }

    @Override public String getName() { return "help"; }

    @Override public String getDescription() { return "help - show this help message"; }
}
