package se.curtrune.lucy.activities.economy.classes;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Locale;

import se.curtrune.lucy.classes.Listable;


public class Transaction implements Serializable, Listable {
    private long id;
    private float amount;
    private String type;
    private String description;
    protected long date;
    protected String account;
    public enum Type{
        PENDING, RUNNING, MONTHLY, HEALTH, CHILDREN, INCOME, UNSPECIFIED, OTHER,EVERYTHING
    }
    //TODO modify values in table to accord with these
    private static final String[] accounts = {"bank norwegian", "cash", "hb allkonto", "hb sparkonto", "avanza", "sbab", "all", "PENDING"};
/*    public enum Account{
        PENDING, ALLKONTO,SPARKONTO, AVANZA, ALL;
    }*/

    //public

    @Override
    public long compare() {
        return date;
    }

    @Override
    public boolean contains(String str) {
        return description.contains(str);
    }
    public String getAccount(){
        return  account;
    }
    public  int getAccountOrdinal(){
        return getAccountOrdinal(account);
    }
    public static  String[] getAccounts(){
        return accounts;
    }
    public static int getAccountOrdinal(String account){
        int ordinal = 0;
        for( String acc: getAccounts()){
            if( acc.equalsIgnoreCase(account)){
                return ordinal;
            }
            ordinal++;
        }
        return -1;
    }
    public float getAmount() {
        return amount;
    }
    public LocalDate getDate(){
        return LocalDate.ofEpochDay(date);
    }
    public String getDescription(){
        return description;
    }
    @Override
    public String getHeading() {
        return description;
    }

    public long getID() {
        return id;
    }
    @Override
    public String getInfo() {
        return String.format("%.2fkr, %s", amount, date);
    }


    public Type getType() {
        return Type.valueOf(type.toUpperCase());
    }
    public boolean isAccount(String account){
        return this.account.equalsIgnoreCase(account) || account.equalsIgnoreCase("all");
    }
    public boolean isBetween(LocalDate firstDate, LocalDate lastDate){
        return date >= firstDate.toEpochDay() && date <= lastDate.toEpochDay();
    }
    public boolean isDate(LocalDate date){
        return getDate().equals(date);
    }
    public boolean isType(Type type){
        return getType().equals(type) || type.equals(Type.EVERYTHING);
    }
    public void setAccount(String account){
        this.account = account;
    }
    public void setAmount(float amount){
        this.amount = amount;
    }
    public void setAmount(String amount) throws NumberFormatException{
        this.amount = Float.parseFloat(amount);
    }
    public void setDate(LocalDate date){
        this.date = date.toEpochDay();
    }
    public void setDateEpoch(long epoch){
        this.date = epoch;
    }

    public void setDescription(String description){
        this.description = description;
    }
    public void setID(long id){
        this.id = id;
    }
    public void setType(Type type){
        this.type = type.toString();
    }
    public void setType(String type){
        this.type = Type.valueOf(type).toString();
    }

    @Override
    public String toString() {
        return String.format(Locale.ENGLISH, "%s, %s, %.2fkr", date, description, amount);
    }
}
