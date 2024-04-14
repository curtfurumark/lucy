package se.curtrune.lucy.workers;

import static se.curtrune.lucy.util.Logger.log;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import se.curtrune.lucy.classes.economy.EcMoney;
import se.curtrune.lucy.classes.economy.Transaction;


public class TransactionWorker {
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


    public static  List<Transaction> filterTransactions(List<Transaction> transactions, String account, EcMoney.Type type){
        return transactions.stream().filter(transaction -> transaction.isAccount(account) && transaction.isType(type)).collect(Collectors.toList());
    }

    public static List<Transaction> filterTransactions(LocalDate date, List<Transaction> transactions) {
        return transactions.stream().filter(transaction -> transaction.isDate(date)).collect(Collectors.toList());
    }
}
