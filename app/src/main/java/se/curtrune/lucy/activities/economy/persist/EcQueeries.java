package se.curtrune.lucy.activities.economy.persist;

import java.time.LocalDate;
import java.util.Locale;

public class EcQueeries {
    public static final String CREATE_TABLE_TRANSACTIONS =
            "CREATE TABLE transactions " +
                    "(id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "description TEXT, " +
                    "date INTEGER, " +
                    "amount REAL )";
    public static final String CREATE_TABLE_ASSETS =
            "CREATE TABLE assets " +
                    "(id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "account TEXT, " +
                    "date INTEGER, " +
                    "amount REAL )";
    //TODO change drop to economy
    public static final String DROP_TABLE_TRANSACTIONS = "DROP TABLE IF EXISTS transactions";
    public static final String DROP_TABLE_ASSETS = "DROP TABLE  IF EXISTS assets";

    public static String selectTransactions() {
        return "SELECT * FROM transactions ORDER by date DESC";
    }

    public static String selectAssets() {
        return "SELECT * FROM assets ORDER BY date DESC";
    }

    public static String selectTransactions(LocalDate firstDate, LocalDate lastDate) {
        return String.format(Locale.getDefault(),"SELECT * FROM transactions WHERE date >= %d AND date <= lastDate ORDER BY date DESC");
    }
}
