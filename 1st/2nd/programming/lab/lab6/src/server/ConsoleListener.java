package server;

import command.CommandDispatcher;

import java.util.Scanner;
import java.util.NoSuchElementException;

/**
 * Reads server-console commands.
 * Commands: save - saves collection to file
 */
public class ConsoleListener implements Runnable {

    private final CommandDispatcher dispatcher;

    public ConsoleListener(CommandDispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }

    @Override
    public void run() {
        Scanner sc = new Scanner(System.in);
        while (true) {
            try {
                if (!sc.hasNextLine()) break;
                String line = sc.nextLine().trim().toLowerCase();
                if (line.equals("save")) {
                    System.out.println(dispatcher.save().getMessage());
                } else if (!line.isEmpty()) {
                    System.out.println("Server console commands: save");
                }
            } catch (NoSuchElementException e) {
                System.out.println("\nServer shutting down...");
                System.exit(0);
                break;
            }
        }
        sc.close();
    }
}