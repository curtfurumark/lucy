package se.curtrune.lucy.classes;

import static se.curtrune.lucy.util.Logger.log;

import android.content.Context;

import java.util.Random;

import se.curtrune.lucy.R;

public class Quotes {
    public static String getRandomQuote(Context context) {
        String[] quotes = context.getResources().getStringArray(R.array.quotes);
        Random random = new Random();
        int index = random.nextInt(quotes.length - 1);
        log("Quotes.getRandomQuote()");
        return quotes[index];
    }
}
