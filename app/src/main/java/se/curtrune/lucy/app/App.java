package se.curtrune.lucy.app;

import se.curtrune.lucy.classes.item.Item;
import se.curtrune.lucy.classes.Type;

public class App {
    public static Item getRootItem(String heading){
        Item item = new Item(heading);
        item.setType(Type.ROOT);
        return item;
    }
}
