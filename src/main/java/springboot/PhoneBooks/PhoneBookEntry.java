package springboot.PhoneBooks;

import java.util.UUID;

public class PhoneBookEntry {

    private final String id = UUID.randomUUID().toString();
    private String firstName;
    private String secondName;
    private String phoneNumber;

    PhoneBookEntry(String firstName, String secondName, String phoneNumber){
        this.firstName = firstName;
        this.secondName = secondName;
        this.phoneNumber = phoneNumber.replaceAll("[^0-9]", "");
    }

    public String getId() {
        return id;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public String getSecondName() {
        return secondName;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
}
