package se.curtrune.lucy.activities.economy.persist;

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
    public static final String DROP_TABLE_TRANSACTIONS = "DROP TABLE economy";
    public static final String DROP_TABLE_ASSETS = "DROP TABLE assets";

    public static String selectTransactions() {
        return "SELECT * FROM transactions ORDER by date DESC";
    }
}
