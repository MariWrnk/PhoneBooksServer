package springboot.PhoneBooks;

import java.util.*;

public class User {

    private final String id = UUID.randomUUID().toString();
    private String firstName;
    private String secondName;
    private final Map<String,PhoneBookEntry> phoneBook = new HashMap<>();

    User(String firstName, String secondName){
        this.firstName = firstName;
        this.secondName = secondName;
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

    public Map<String, PhoneBookEntry> getPhoneBook() {
        return phoneBook;
    }

    public String fullName(){
        return firstName + " " + secondName;
    }

    public void addPBEntry(PhoneBookEntry pbe){
        phoneBook.put(pbe.getId(), pbe);
    }

    public PhoneBookEntry getPBEntryById(String id){
        return phoneBook.get(id);
    }

    public PhoneBookEntry getPBEntryByPhoneNumber(String phoneNumber){
        phoneNumber = phoneNumber.replaceAll("[^0-9]", "");
        for(PhoneBookEntry pbe: phoneBook.values()){
            if(pbe.getPhoneNumber().equals(phoneNumber)){
                return pbe;
            }
        }
        return null;
    }

    public void deletePBEntry(String id){
        phoneBook.remove(id);
    }

    public void editPBEntry(String id, String firstName, String secondName, String phoneNumber){
        phoneBook.get(id).setFirstName(firstName);
        phoneBook.get(id).setSecondName(secondName);
        phoneBook.get(id).setPhoneNumber(phoneNumber);
    }

}
