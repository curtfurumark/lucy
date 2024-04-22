package se.curtrune.lucy.activities.economy.classes;

import android.content.Context;

import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

import se.curtrune.lucy.activities.economy.workers.TransactionWorker;

public class EconomyStats {
    private LocalDate firstDate;
    private LocalDate lastDate;
    private List<Transaction> transactions;

    public EconomyStats(LocalDate firstDate, LocalDate lastDate, Context context) {
        this.firstDate = firstDate;
        this.lastDate = lastDate;
        init(context);
    }
    private void init(Context context){
        transactions = TransactionWorker.selectTransactions(firstDate, lastDate, context);
    }
}
