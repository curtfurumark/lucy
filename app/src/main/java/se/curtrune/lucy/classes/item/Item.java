package se.curtrune.lucy.classes.item;


import static se.curtrune.lucy.util.Logger.log;

import androidx.annotation.NonNull;

import com.google.gson.Gson;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import se.curtrune.lucy.classes.Contact;
import se.curtrune.lucy.classes.Content;
import se.curtrune.lucy.classes.ItemDuration;
import se.curtrune.lucy.classes.Listable;
import se.curtrune.lucy.classes.Mental;
import se.curtrune.lucy.classes.Notification;
import se.curtrune.lucy.classes.Reward;
import se.curtrune.lucy.classes.State;
import se.curtrune.lucy.classes.Type;
import se.curtrune.lucy.util.Converter;

public class Item implements Serializable , Listable {

    protected long id;
    protected long parent_id;
    protected long repeatID;
    protected String heading;
    protected String description;
    protected String comment;
    protected Contact contact;
    protected Content content;
    protected String tags;
    protected long created;
    protected long updated;
    protected long duration;
    protected boolean syncWithGoogle;

    protected long target_date;
    protected int target_time;
    protected int type;
    protected int state;
    protected  String category;
    protected int has_child;
    protected boolean isCalenderItem;
    protected Item parent;
    protected List<Item> children;
    protected Repeat repeat;
    //protected MentalStats estimate;
    protected Notification notification;
    protected ItemDuration itemDuration;
    protected int energy;
    protected int anxiety;
    protected int stress;
    protected  int mood;
    protected  int color = -1;
    //protected MediaContent content;
    public static boolean VERBOSE = false;
    protected Reward reward;
    protected int priority;
    /**
     * 0 = not a template in anyway
     * templateRoot, has templateChildren
     * templateGenerated...
     */
    protected int templateType;

    public Item() {
        this.created = updated = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
        this.target_time =0;
        this.target_date = 0;
        this.duration = 0;
        category = heading = comment = description = tags = "";
        state = State.TODO.ordinal();
        type = Type.NODE.ordinal();
        has_child = 0;
        children = new ArrayList<>();
    }
    public Item(String heading){
        this();
        this.heading = heading;
    }

    /**
     * when a template spawns a child
     * @param other, the template item
     */
    public Item(Item other){
        this();
        this.type = other.getType().ordinal();
        this.heading = other.getHeading();
        this.duration = other.getDuration();
        this.category = other.getCategory();
        this.tags = other.getTags();
        this.target_time = LocalTime.now().toSecondOfDay();
        this.target_date = LocalDate.now().toEpochDay();
        //this.estimate = item.getEstimate();
        this.repeatID = other.repeatID;
        this.color = other.getColor();
    }
    public long compare(){
        if( type == Type.APPOINTMENT.ordinal()){return target_date;}
        return (state == State.DONE.ordinal() ? Long.MIN_VALUE + updated: updated ) * -1;
    }
    public long compareTargetDate(){
        return target_date * -1;
    }

    public long compareTargetTime(){
        return target_time;
    }
    @Override
    public boolean contains(String str) {
        return (heading + description + comment + tags + category).contains(str);
    }
    public int getAnxiety(){
        return anxiety;
    }
    public String getCategory() {
        return category;
    }
    public int getColor(){
        return color;
    }
    public Content getContent(){
        return content;
    }
    public String getComment() {
        return comment;
    }
    public Contact getContact(){
        return contact;
    }
    public LocalDateTime getCreated() {
        return LocalDateTime.ofEpochSecond(created, 0, ZoneOffset.UTC);
    }
    public long getCreatedEpoch(){
        return created;
    }

    public String getDescription() {
        return description;
    }
    public long getDuration(){
        return duration;
    }
    public int getEnergy(){
        //return mental != null? mental.getEnergy():0;
        return energy;
    }
/*    public MentalStats getEstimate(){
        return estimate;
    }*/
    public String getHeading() {
        return heading;
    }
    public long getID() {
        return id;
    }

    public String getInfo(){
        if(type == Type.APPOINTMENT.ordinal()){
           return String.format("%s %s", Converter.format(Converter.epochToDate(target_date)), Converter.epochTimeToFormattedString(target_time));
        }
        return String.format(Locale.getDefault(), "%s", getTargetDate().toString());
    }

    public LocalDate getDateUpdated(){
        return getUpdated().toLocalDate();
    }
    public long getEstimatedDuration(){
/*        if( hasEstimate()){
            return estimate.getDuration();
        }*/
        //TODO, implement some sort of duration worker getInstances children add duration divide by number of instances
        return 0;
    }
    public ItemDuration getItemDuration() {
        return itemDuration;
    }
    public int getMood(){
        return mood;
    }
    public Mental getMental(){
        return new Mental(anxiety, energy, mood, stress);
    }
    public Notification getNotification(){
        return this.notification;
    }

    public Item getParent(){
        return parent;
    }
    public long getParentId() {
        return parent_id;
    }
    public int getPriority(){
        return priority;
    }

    public Repeat getRepeat(){
        return repeat;
    }
    public Reward getReward(){
        return reward;
    }
    public long getRepeatID(){
        return repeatID;
    }
    public State getState() {
        return State.values()[state];
    }
    public int getStress(){
        return stress;
    }
    public String getTags() {
        return tags;
    }
    public LocalDate getTargetDate() {
        return LocalDate.ofEpochDay(target_date);
    }
    public long getTargetDateEpochDay(){
        return target_date;
    }
    public int getTargetTimeSecondOfDay() {
        return target_time;
    }
    public LocalTime getTargetTime() {
        return LocalTime.ofSecondOfDay(target_time);
    }
    public LocalTime getTimeUpdated(){
        return getUpdated().toLocalTime();
    }
    public Type getType() {
        return Type.values()[type];
    }
    public LocalDateTime getUpdated() {
        return LocalDateTime.ofEpochSecond(updated, 0, ZoneOffset.UTC);
    }
    public long getUpdatedEpoch(){
        return updated;
    }
    public boolean hasCategory(){
        return ! this.category.isEmpty();
    }
    public boolean hasChild(){
        return has_child == 1;
    }
    public boolean hasColor() {
        return color != -1 && color != 0;
    }
    public boolean hasEstimate(){
        //return estimate != null;
        return false;
    }
    public boolean hasItemParent(){
        return parent != null;
    }
    public boolean hasRepeat(){
        return repeat != null;
    }
    public boolean hasNotification(){return notification != null;}
    public boolean hasReward(){return reward != null;}
    public boolean hasTags(){
        return !this.tags.isEmpty();
    }
    public boolean isCalenderItem(){
        return isCalenderItem;
    }
    public boolean isSyncWithGoogle(){
        return syncWithGoogle;
    }
    public boolean isCategory(String category){
        if( this.category == null){
            log("Item this.category == null, returning false");
            return false;
        }
        return this.category.equalsIgnoreCase(category);
    }

    /**
     * checks if this item has targetTime
     * @param date the date to check
     * @return true if date equals this items targetDate
     */
    public boolean isDate(LocalDate date){
        return this.target_date == date.toEpochDay();
    }
    public boolean isDateHour(LocalDate date, LocalTime time){
        return isDate(date) && isTargetTimeHour(time);
    }
    public boolean isDone(){
        return state == State.DONE.ordinal();
    }

    public boolean isPrioritized(){
        return priority == 1;
    }

    public boolean isRoot(){
        return parent_id < 1;
    }
    public boolean isState(State state){
        return this.state == state.ordinal();
    }
    public boolean isTargetTimeHour(LocalTime time){
        return getTargetTime().getHour() == time.getHour();
    }
    public boolean isTemplate(){
        return templateType != 0;
    }
    public boolean isUpdated(LocalDate date){
        LocalDateTime localDateTime = date.atStartOfDay();
        long epoch = localDateTime.toEpochSecond(ZoneOffset.UTC);
        return updated >= epoch && updated <= epoch + (3600 * 24);
    }

    public void setAnxiety(int anxiety){
        this.anxiety = anxiety;
    }
    public void setCategory(String category) {
        //log("Item.setCategory(String)", category);
        if( category != null) {
            this.category = category;
        }
    }
    public void setColor(int color){
        this.color = color;
    }
    public void setContent(Content content){
        this.content = content;
    }
    public void setComment(String comment) {
        this.comment = comment;
    }
    public void setContact(Contact contact){
        this.contact = contact;
    }
    public void setCreated(LocalDateTime created) {
        this.updated = created.toEpochSecond(ZoneOffset.UTC);
    }
    public void setCreated(long created){
        this.created = created;
    }

    public void setEnergy(int energy){
        this.energy = energy;
    }

    /**
     * TODO, use this one as extra, ie link if type is content/file
     * or maybe not
     * @param description
     */
    public void setDescription(String description) {
        this.description = description;
    }
    public void setDuration(long  duration){
        this.duration = duration;
    }
    @Deprecated
    public void setEstimatedDuration(long seconds) {

    }
    public void setItemDuration(ItemDuration itemDuration){
        this.itemDuration = itemDuration;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }

    public void setID(long id){
        this.id = id;
    }
    public void setNotification(Notification notification){
        this.notification = notification;
    }

    public void setId(long id) {
        this.id = id;
    }
    public void setIsAppointment(boolean isAppointment){
        this.type = isAppointment ? Type.APPOINTMENT.ordinal(): Type.NODE.ordinal();
    }
    public void setIsCalenderItem(boolean calenderItem){
        this.isCalenderItem = calenderItem;
    }
    public void setIsDone(boolean isDone){
        this.state = isDone ? State.DONE.ordinal(): State.TODO.ordinal();
    }
    public void setHasChild(boolean hasChild){
        this.has_child = hasChild? 1:0;
    }
    public void setIsTemplate(boolean isTemplate){
        this.templateType = isTemplate ? 1:0;
    }
    public void setMood(int mood){
        this.mood = mood;
    }
    public void setParent(Item parent){
        this.parent_id = parent.getID();
        this.parent = parent;
    }
    public void setParentId(long parent_id) {
        this.parent_id = parent_id;
    }

    public void setRepeat(Repeat repeat){
        this.repeat = repeat;
    }
    public void setRepeatID(long id){
        this.repeatID = id;
    }

    public void setPriority(int priority){
        this.priority = priority;
    }
    public void setReward(Reward reward){
        this.reward = reward;
    }
    public void setReward(String json){
        this.reward = new Gson().fromJson(json, Reward.class);
    }

    public void setStress(int stress){
        this.stress = stress;
    }
    public void setState(int state){
        this.state = state;
    }
    public void setState(State state) {
        this.state = state.ordinal();
    }
    public void setSyncWithGoogle(boolean syncWithGoogle){
        this.syncWithGoogle = syncWithGoogle;
    }
    public void setTargetDate(LocalDate localDate) {
        this.target_date = localDate != null? localDate.toEpochDay() : 0;
    }
    public void setTargetDate(long target_date) {
        this.target_date = target_date;
    }
    public void setTags(String tags) {
        this.tags = tags;
    }
    public void setTargetTime(int target_time) {
        this.target_time = target_time;
    }
    public void setType(Type type) {
        this.type = type.ordinal();
    }
    public void setType(int type){
        this.type = type;
    }
    public void setTargetTime(LocalTime localTime) {
        this.target_time = localTime != null ?localTime.toSecondOfDay(): 0;
    }
    public void setUpdated(LocalDateTime updated) {
        this.updated = updated.toEpochSecond(ZoneOffset.UTC);
    }
    public void setUpdated(long updated){
        this.updated = updated;
    }

    @NonNull
    @Override
    public String toString() {
        return String.format(Locale.getDefault(), "%s, time: %s, date: :%s", getHeading(), getTargetTime().toString(), getTargetDate().toString());
        //return String.format(Locale.getDefault(), "%s, %s,  d:%s", getHeading(), getTargetTime().toString(), Converter.formatSecondsWithHours(duration));
    }
    /**
     * sets the next targetDate,
     * to be called when item is done
     */
    public void updateTargetDate(){
        log("...updateTargetDate()");
        if( hasRepeat()){
            //TODO, repeater.get next date?
            //target_date = repeat.getNextDate().toEpochDay();
        }
    }
    public boolean isAppointment() {
        return type == Type.APPOINTMENT.ordinal();
    }

    public void setMental(Mental mental) {
        energy = mental.energy;
        mood = mental.mood;
        anxiety = mental.anxiety;
        stress = mental.stress;
    }


}
