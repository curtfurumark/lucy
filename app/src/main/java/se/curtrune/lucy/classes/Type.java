package se.curtrune.lucy.classes;

public enum Type {
    //ATTEMPT, EVENT, CHORE, SESSION_MUSIC, DEFAULT, ROOT, DEV, URL, APPOINTMENT, HOUSEHOLD, PENDING, SESSION, READ, WATCH;
    PENDING, MUSIC, ART, DEV, READ, WRITE, HEALTH, HOUSEHOLD, APPOINTMENT, WATCH, LEARN, SOCIAL, SORT, PLAN, WHATEVER;
    public static String[] toArray() {
        Type[] values = values();
        String[] strings = new String[values.length];
        for( int i = 0; i< values.length; i++){
            strings[i] = values[i].toString();
        }
        return strings;
    }
}
