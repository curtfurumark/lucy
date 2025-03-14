package se.curtrune.lucy.screens.dev;

import static se.curtrune.lucy.util.Logger.log;

import android.content.Context;

import se.curtrune.lucy.classes.item.Item;
import se.curtrune.lucy.classes.MedicineContent;
import se.curtrune.lucy.persist.ItemsWorker;

public class MedicineTest {
    public static void createMedicine(String name, String doctor, String dosage, String type, Context context){
        log("...createMedicine(String, String, String, String)");
        MedicineContent medicineContent = new MedicineContent();
        Item item = new Item(name);
        item.setContent(medicineContent);
        item = ItemsWorker.insert(item, context);

    }
}
