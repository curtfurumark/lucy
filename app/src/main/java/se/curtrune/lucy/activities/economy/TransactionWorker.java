package se.curtrune.lucy.activities.economy;

import static se.curtrune.lucy.util.Logger.log;

import android.content.Context;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import se.curtrune.lucy.activities.economy.classes.Asset;
import se.curtrune.lucy.activities.economy.classes.Transaction;
import se.curtrune.lucy.activities.economy.persist.EcQueeries;
import se.curtrune.lucy.persist.LocalDB;


public class TransactionWorker {

    public static Transaction insert(Transaction transaction, Context context){
        log("TransactionWorker.insert(Transaction, Context");
        LocalDB db = new LocalDB(context);
        return db.insert(transaction);
    }
    public static List<Asset> selectAssets(Context context) {
        log("TransactionWorker.selectAssets()");
        LocalDB db = new LocalDB(context);
        return db.selectAssets(EcQueeries.selectAssets());
    }
    public static List<Transaction> selectTransactions(Context context){
        log("...selectTransactions()");
        LocalDB db = new LocalDB(context);
        return db.selectTransactions(EcQueeries.selectTransactions());
    }

    public static List<Transaction> selectTransactions(LocalDate firstDate, LocalDate lastDate){
        log("...selectTransactions(LocalDate, LocalDate)");
        List<Transaction> transactions = new ArrayList<>();
        Transaction transaction = new Transaction();
        transaction.setAmount(42.01f);
        transaction.setDescription("douglas");
        transaction.setDate(LocalDate.now());
        transaction.setAccount("handelsbanken");
        transactions.add(transaction);
        return transactions;
    }



    public static  List<Transaction> filterTransactions(List<Transaction> transactions, String account, Transaction.Type type){
        return transactions.stream().filter(transaction -> transaction.isAccount(account) && transaction.isType(type)).collect(Collectors.toList());
    }

    public static List<Transaction> filterTransactions(LocalDate date, List<Transaction> transactions) {
        return transactions.stream().filter(transaction -> transaction.isDate(date)).collect(Collectors.toList());
    }


    public static Asset insert(Asset asset, Context context) {
        log("TransactionWorker.insert(Asset, Context)");
        LocalDB db = new LocalDB(context);
        return db.insert(asset);
    }
}
