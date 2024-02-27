package se.curtrune.lucy.statistics;

import se.curtrune.lucy.classes.Listable;

public class TopTenListable implements Listable {
    @Override
    public String getHeading() {
        return null;
    }

    @Override
    public String getInfo() {
        return null;
    }

    @Override
    public boolean contains(String str) {
        return false;
    }

    @Override
    public long compare() {
        return 0;
    }
}
