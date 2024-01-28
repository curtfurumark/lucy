package se.curtrune.lucy.classes;


import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

import se.curtrune.lucy.util.Converter;

public class Item implements Serializable , Listable {

    protected long id;
    protected long parent_id;

    protected String heading;
    protected String description;
    protected String comment;
    protected String tags;
    protected long created;
    protected long updated;
    protected long duration;
    protected long target_date;
    protected int target_time;
    protected int type;
    protected int state;
    protected  String category;
    protected int has_child;
    protected Item parent;
    protected List<Item> children;
    protected int days;
    protected int energy;




    public enum ItemClass{
        ITEM, PERIODITEM, APPOINTMENT,
    }
    protected ItemClass itemClass;

    public Item() {
        this.created = updated = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
        this.target_time =0;
        this.target_date = LocalDate.now().toEpochDay();
        this.duration = 0;
        this.days = 0;
        heading = comment = description = tags = "";
        state = State.TODO.ordinal();
        type = Type.PENDING.ordinal();
        has_child = 0;
        children = new ArrayList<>();
    }
    public Item(String heading){
        this();
        this.heading = heading;
    }
    public void addChild(Item item){
        children.add(0, item);
    }
    public long compare(){
        if( type == Type.APPOINTMENT.ordinal()){return target_date;}
        return (state == State.DONE.ordinal() ? Long.MIN_VALUE + updated: updated ) * -1;
    }

    public String getCategory() {
        return category;
    }
    public List<Item> getChildren(){
        return children;
    }
    public String getComment() {
        return comment;
    }
    public LocalDateTime getCreated() {
        return LocalDateTime.ofEpochSecond(created, 0, ZoneOffset.UTC);
    }
    public long getCreatedEpoch(){
        return created;
    }
    public int getDays(){
        return days;
    }
    public String getDescription() {
        return description;
    }
    public long getDuration(){
        return duration;
    }
    public int getEnergy(){
        return energy;
    }
    public String getHeading() {
        return heading;
    }
    @Override
    public long getID() {
        return id;
    }

    public String getInfo(){
        if(type == Type.APPOINTMENT.ordinal()){
           return String.format("%s %s", Converter.format(Converter.epochToDate(target_date)), Converter.epochTimeToFormattedString(target_time));
        }
        if( state == State.DONE.ordinal()){
            return String.format("%s %s", Converter.formatDateTimeUI(updated), Converter.formatSecondsWithHours(duration));
        }else if( state == State.INFINITE.ordinal()){
            return String.format("%s, %s", getState().toString(), Converter.epochToDate(target_date));
        }
        return String.format("%s", Converter.formatDateTimeUI(updated));
    }


    @Override
    public boolean contains(String str) {
        return (heading + description + comment + tags).contains(str);
    }
    public LocalDate getDateUpdated(){
        return getUpdated().toLocalDate();
    }

    public Item getParent(){
        return parent;
    }
    public long getParentId() {
        return parent_id;
    }


    public State getState() {
        return State.values()[state];
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
    public boolean hasChild(){
        return has_child == 1;
    }
    public boolean hasItemParent(){
        return parent != null;
    }
    public boolean isDone(){
        return state == State.DONE.ordinal();
    }
    public  boolean isInfinite(){
        return state == State.INFINITE.ordinal();
    }
    public boolean isRoot(){
        return parent_id < 1;
    }
    public boolean isWIP(){
        return state == State.WIP.ordinal();
    }
    public void setChildren(List<Item> children){
        for(Item item: children){
            item.setParent(this);
        }
        this.children = children;

    }
    public void setCategory(String category) {
        this.category = category;
    }
    public void setComment(String comment) {
        this.comment = comment;
    }
    public void setCreated(LocalDateTime created) {
        this.updated = created.toEpochSecond(ZoneOffset.UTC);
    }
    public void setCreated(long created){
        this.created = created;
    }
    public void setDays(int days){
        this.days = days;
    }
    public void setEnergy(int energy){
        this.energy = energy;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public void setDuration(long  duration){
        this.duration = duration;
    }
    public void setDuration(LocalDateTime now){
        duration  = now.toEpochSecond(ZoneOffset.UTC) - updated;

    }
    public void setHeading(String heading) {
        this.heading = heading;
    }

    public void setId(long id) {
        this.id = id;
    }
    public void setHasChild(boolean hasChild){
        this.has_child = hasChild? 1:0;
    }
    public void setParent(Item parent){
        this.parent_id = parent.getID();
        this.parent = parent;
    }
    public void setParentId(long parent_id) {
        this.parent_id = parent_id;
    }


    public void setState(int state){
        this.state = state;
    }
    public void setState(State state) {
        this.state = state != null ? state.ordinal(): State.PENDING.ordinal();
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

    @Override
    public String toString() {
        return String.format("%s (%d), parent: %d, has child: %b", heading,id, parent_id, hasChild());
    }
}
