package command;

import collectionManager.ProductCollection;
import common.Response;

/** Shuffle command - randomly shuffles the collection.
 *  Usage: shuffle */
public class ShuffleCommand implements Command {

    private final ProductCollection collection;

    public ShuffleCommand(ProductCollection collection) { this.collection = collection; }

    @Override
    public Response execute(Object payload) {
        if (collection.getProducts().isEmpty())
            return new Response(false, "Collection is empty.", collection.getSortedByName());
        collection.shuffle();
        return new Response(true, "Collection shuffled.", collection.getSortedByName());
    }

    @Override public String getName()        { return "shuffle"; }
    @Override public String getDescription() { return "shuffle - randomly shuffle the collection"; }
}
