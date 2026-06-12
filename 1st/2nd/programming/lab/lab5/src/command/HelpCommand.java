package command;

import collectionManager.ProductCollection;
import java.io.InputStreamReader;

/**
 * Command to display help information
 */
public class HelpCommand extends Command {
    public HelpCommand(ProductCollection collection, InputStreamReader inputReader) {
        super(collection, inputReader);
    }

    @Override
    public void execute(String[] args) {
        System.out.println("Available commands:");
        System.out.println("  help - display this help");
        System.out.println("  info - display collection information");
        System.out.println("  show - display all elements");
        System.out.println("  add - add new element");
        System.out.println("  update id - update element by id");
        System.out.println("  remove_by_id id - remove element by id");
        System.out.println("  clear - clear collection");
        System.out.println("  save - save collection to file");
        System.out.println("  execute_script file_name - execute script from file");
        System.out.println("  exit - exit program");
        System.out.println("  remove_last - remove last element");
        System.out.println("  add_if_min - add if element is minimum");
        System.out.println("  shuffle - shuffle collection randomly");
        System.out.println("  count_by_price price - count elements with price");
        System.out.println("  print_descending - print in descending order");
        System.out.println("  print_field_descending_manufacturer - print manufacturer names descending");
    }
}