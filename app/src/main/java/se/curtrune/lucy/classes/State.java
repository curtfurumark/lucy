package se.curtrune.lucy.classes;

public enum State {
    //TODO, DONE, WIP, ABORTED, PENDING, INDETERMINATE
    DONE, WIP, TODO, PENDING, FAILED, INFINITE, ABORTED, ARCHIVE, INFINITYTREE;

    @Deprecated
    public static String[] toArray() {
        State[] values = values();
        String[] strings= new String[values.length];
        for( int i = 0; i< values.length; i++){
            strings[i] = values[i].toString();
        }
        return strings;
    }
}
