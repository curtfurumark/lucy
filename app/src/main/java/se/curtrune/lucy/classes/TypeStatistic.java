package se.curtrune.lucy.classes;

import java.util.Locale;

import se.curtrune.lucy.util.Converter;

public class TypeStatistic implements Listable {
    private long secs;
    private Type type;

    public TypeStatistic(long secs, Type type) {
        this.secs = secs;
        this.type = type;
    }

    @Override
    public String toString() {
        return String.format(Locale.ENGLISH, "%s, %s", type.toString(), Converter.formatSecondsWithHours(secs));
    }

    public void add(long secs) {
        this.secs += secs;
    }

    public Type getType() {
        return type;
    }

    @Override
    public String getHeading() {
        return type.toString();
    }

    @Override
    public String getInfo() {
        return Converter.formatSecondsWithHours(secs);
    }

    @Override
    public boolean contains(String str) {
        return false;
    }

    @Override
    public long compare() {
        return secs;
    }

    @Override
    public long getID() {
        return 0;
    }
}
