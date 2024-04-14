package se.curtrune.lucy.classes.economy;


import java.time.LocalDate;


public class Transaction extends EcMoney{
/*    public static void main(String[] args) {
        Transaction transaction = PersistDB1.selectTransaction(1521);
        log(transaction);
        LocalDate lastDate = LocalDate.now();
        LocalDate firstDate = lastDate.minusDays(7);
        log("...is between", transaction.isBetween(firstDate, lastDate));
        log("...contains", transaction.contains(""));
        log("...isType", transaction.isType(Type.EVERYTHING));

    }*/
    public boolean isDate(LocalDate date){
        return getLocalDate().equals(date);
    }


}