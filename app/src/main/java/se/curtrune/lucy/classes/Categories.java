package se.curtrune.lucy.classes;

import static se.curtrune.lucy.util.Logger.log;

public class Categories {
    private String[] categories;
    public Categories(String[] categories){
        this.categories = categories;
    }
    public int  getIndex(String category){
        log("...getIndex(String)", category);
        if( category == null || category.isEmpty()){
            log("...missing category, returning index 0");
            return 0;
        }
        int index = 0;
        for( int i = 0; i < categories.length; i++){
            if(categories[i].equalsIgnoreCase(category)){
                return i;
            }
        }
        return 0; //should not happen, but if it does, default to the first category
    }

    public String[] getCategories() {
        return categories;
    }
}
