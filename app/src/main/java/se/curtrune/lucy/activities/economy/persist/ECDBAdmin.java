package se.curtrune.lucy.activities.economy.persist;

import static se.curtrune.lucy.util.Logger.log;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import se.curtrune.lucy.activities.economy.classes.Asset;
import se.curtrune.lucy.activities.economy.classes.Transaction;
import se.curtrune.lucy.persist.SqliteLocalDB;

public class ECDBAdmin {
    public static void dropTables(Context context){
        log("ECDBAdmin.dropTables()");
        SqliteLocalDB db = new SqliteLocalDB(context);
        db.executeSQL(EcQueeries.DROP_TABLE_TRANSACTIONS);
        db.executeSQL(EcQueeries.DROP_TABLE_ASSETS);
    }
    public static void createEconomyTables(Context context) {
        log("DBAdmin.createEconomyTables()");
        SqliteLocalDB db = new SqliteLocalDB(context);
        db.executeSQL(EcQueeries.CREATE_TABLE_TRANSACTIONS);
        db.executeSQL(EcQueeries.CREATE_TABLE_ASSETS);
    }
    public static Asset getAsset(Cursor cursor) {
        Asset asset = new Asset();
        asset.setID(cursor.getLong(0));
        asset.setAccount(cursor.getString(1));
        asset.setDateEpoch(cursor.getLong(2));
        asset.setAmount(cursor.getFloat(3));
        return asset;
    }

    public static Transaction getTransaction(Cursor cursor) {
        Transaction transaction = new Transaction();
        transaction.setID(cursor.getLong(0));
        transaction.setDescription(cursor.getString(1));
        transaction.setDateEpoch(cursor.getLong(2));
        transaction.setAmount(cursor.getFloat(3));
        return  transaction;
    }


    public static ContentValues getContentValues(Asset asset) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("account", asset.getAccount());
        contentValues.put("amount", asset.getAmount());
        contentValues.put("date", asset.getDate().toEpochDay());
        return contentValues;
    }
}
