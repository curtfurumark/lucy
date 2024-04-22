package se.curtrune.lucy.activities.economy.classes;


import java.io.Serializable;
import java.time.LocalDate;
import java.util.Locale;

import se.curtrune.lucy.classes.Listable;

public class Asset implements Listable, Serializable {
    private long id;
    private String account;

    private float amount;
    private long epochDay;


    @Override
    public boolean contains(String str) {
        return account.contains(str);
    }

    @Override
    public long compare() {
        return 0;
    }
    public String getAccount(){
        return account;
    }

    public LocalDate getDate(){
        return LocalDate.ofEpochDay(epochDay);
    }
    @Override
    public String getHeading() {
        return String.format(Locale.ENGLISH,"%s", account );
    }

    @Override
    public String getInfo() {
        return String.format(Locale.getDefault(), "%.2fkr %s", amount, getDate().toString());
    }

    public long getID() {
        return id;
    }

    public void setAccount(String account){
        this.account = account;
    }

    @Override
    public String toString() {
        return String.format(Locale.ENGLISH, "%.2f kr, %s %s", amount, account, getDate().toString());
    }


    public void setDate(LocalDate date){
        this.epochDay = date.toEpochDay();
    }
    public void setDateEpoch(long epochDay){
        this.epochDay = epochDay;
    }
    public void setAmount(float amount) {
        this.amount = amount;
    }
    public void setAmount(String amount) throws NumberFormatException{
        this.amount = Float.parseFloat(amount);
    }

    public float getAmount() {
        return amount;
    }
    public boolean isAccount(String account){
        return this.account.equalsIgnoreCase(account) || account.equalsIgnoreCase("all");
    }

    public void setID(long id){
        this.id = id;

    }
}
