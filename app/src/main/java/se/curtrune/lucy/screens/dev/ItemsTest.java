package se.curtrune.lucy.screens.dev;

import static se.curtrune.lucy.util.Logger.log;

import android.content.Context;

import se.curtrune.lucy.classes.item.Item;
import se.curtrune.lucy.persist.ItemsWorker;

public class ItemsTest {

    public static void testDeleteTree(Context context){
        log("...testDeleteTree()");
    }
    public static void createTree(Context context){
        log("...createFamilyItems()");
        Item rootItem = new Item("rootItem");
        rootItem = ItemsWorker.insertChild(ItemsWorker.getDailyRoot(context), rootItem, context);
        Item child1 = new Item("child1");
        child1 = ItemsWorker.insertChild(rootItem, child1, context);
        Item child2 = new Item("child2");
        child2 = ItemsWorker.insertChild(rootItem, child2, context);
        Item grandchild1 = new Item("grandChild1");
        grandchild1 = ItemsWorker.insertChild(child1, grandchild1, context);
        Item grandChild2 = new Item("grandChild2");
        grandChild2 = ItemsWorker.insertChild(child1, grandChild2, context);
        log("rootItem id", rootItem.getID());
    }
    public static void deleteTree(Item parent, Context context){
        log("...deleteTree(Item)", parent.getHeading());
        ItemsWorker.deleteTree(parent, context);
    }
    public static void deleteTree(long parentID, Context context){
        log("ItemTest.deleteTree(long, Context)");
        Item item = ItemsWorker.selectItem(parentID, context);
        if( item == null){
            log(" no item with that id found ");
            return;
        }
        ItemsWorker.VERBOSE = true;
        ItemsWorker.deleteTree(item, context);
    }
}
