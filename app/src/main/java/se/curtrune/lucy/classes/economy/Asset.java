package se.curtrune.lucy.classes.economy;


import java.io.Serializable;
import java.time.LocalDate;
import java.util.Locale;

import se.curtrune.lucy.classes.Listable;

public class Asset implements Listable, Serializable {
    private long id;
    private String account;

    private float amount;
    private String date;


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

    public String getDate(){
        return date;
    }
    @Override
    public String getHeading() {
        return String.format(Locale.ENGLISH,"%s", account );
    }

    @Override
    public String getInfo() {
        return  date;
    }

    public long getID() {
        return id;
    }

    public void setAccount(String account){
        this.account = account;
    }

    @Override
    public String toString() {
        return String.format(Locale.ENGLISH, "%.2f kr, %s %s", amount, account, date);
    }

    public void setDate(String date) {
        this.date = date;
    }
    public void setDate(LocalDate date){
        this.date = date.toString();

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
