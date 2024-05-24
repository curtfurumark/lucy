package se.curtrune.lucy.workers;

import se.curtrune.lucy.classes.Quote;
import se.curtrune.lucy.web.QuoteThread;

public class QuoteWorker {

    public interface GetQuoteCallback{
        void onQuote(Quote quote);
    }
    public static void getQuote(GetQuoteCallback callback){
        QuoteThread quoteThread = new QuoteThread();
        quoteThread.start();

    }
}
