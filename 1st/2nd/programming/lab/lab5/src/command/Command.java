package command;

import collectionManager.ProductCollection;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;

/**
 * Abstract base class for all commands
 */
public abstract class Command {
    protected ProductCollection collection;
    protected InputStreamReader inputReader;
    protected BufferedReader reader;

    /**
     * Constructor for Command
     * @param collection product collection
     * @param inputReader input reader
     */
    public Command(ProductCollection collection, InputStreamReader inputReader) {
        this.collection = collection;
        this.inputReader = inputReader;
        this.reader = new BufferedReader(inputReader);
    }

    /**
     * Sets a new input reader (for script execution)
     * @param inputReader new input reader
     */
    public void setInputReader(InputStreamReader inputReader) {
        this.inputReader = inputReader;
        this.reader = new BufferedReader(inputReader);
    }

    /**
     * Executes the command
     * @param args command arguments
     */
    public abstract void execute(String[] args);
}