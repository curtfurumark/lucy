package se.curtrune.lucy.web;

import se.curtrune.lucy.classes.Quote;

public class QuoteThread extends Thread{
    private static final String URL = "https://curt.furumark.se/lucinda/quotes";
    public interface Callback{
        void onQuote(Quote quote);
    }
    @Override
    public void run() {
        HTTPBasic.get(URL);
    }
}
