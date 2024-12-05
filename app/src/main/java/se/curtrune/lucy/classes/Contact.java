package se.curtrune.lucy.classes;

public class Contact implements Content{
    private String displayName;
    private String email;
    private String phoneNumber;
    private long id;
    public boolean contains(String string){
        return (displayName + email + phoneNumber).toLowerCase().contains(string.toLowerCase());
    }

    public String getDisplayName() {
        return displayName;
    }
    public boolean hasEmailAddress() {
        return !email.isEmpty();
    }

    public boolean hasPhoneNumber() {
        return !phoneNumber.isEmpty();
    }
    public String getEmail() {
        return email;
    }

    public long getId(){
        return id;
    }
    public String getPhoneNumber() {
        return phoneNumber;
    }
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setId(long id){
        this.id = id;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public String toString() {
        return "Contact{" +
                "displayName='" + displayName + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", id=" + id +
                '}';
    }



}
