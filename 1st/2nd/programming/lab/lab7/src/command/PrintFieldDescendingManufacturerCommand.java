package command;

import collectionManager.ProductCollection;
import common.Request;
import common.Response;

import java.util.List;

public class PrintFieldDescendingManufacturerCommand implements Command {

    private final ProductCollection collection;

    public PrintFieldDescendingManufacturerCommand(ProductCollection collection) {
        this.collection = collection;
    }

    @Override
    public Response execute(Request request) {
        List<String> names = collection.getManufacturerNamesDescending();
        if (names.isEmpty()) {
            return Response.ok("No manufacturers found in the collection.");
        }
        return Response.ok(String.join("\n", names));
    }

    @Override public String getName() { return "print_field_descending_manufacturer"; }

    @Override public String getDescription() { return "print_field_descending_manufacturer - print manufacturer names in descending order"; }
}
