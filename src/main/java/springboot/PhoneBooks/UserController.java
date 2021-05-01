package springboot.PhoneBooks;

import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
public class UserController {

    private final Map<String,User> users = new HashMap<>();

    void userExistingCheck(String id){
        if(!users.containsKey(id)){
            throw new UserNotFoundException("User with id " + id + "was not found.");
        }
    }

    void entryExistingCheck(String id, String pbeId){
        if(!users.get(id).getPhoneBook().containsKey(pbeId)){
            throw new UserNotFoundException("User with id " + id
                    + "doesn't have entry with id " + pbeId);
        }
    }

    @GetMapping("/users")
    Collection<User> allUsers() {
        return users.values();
    }

    @PostMapping("/users")
    String addUser(@RequestBody User newUser){
        users.put(newUser.getId(), newUser);
        return "User was added successfully!";
    }

    @GetMapping("/users/{id}")
    User getUserById(@PathVariable String id){
        userExistingCheck(id);
        return users.get(id);
    }

    @DeleteMapping("/users/{id}")
    String deleteUser(@PathVariable String id){
        userExistingCheck(id);
        users.remove(id);
        return "User was deleted successfully!";
    }

    @PutMapping("/users/{id}")
    User editUser(@PathVariable String id, @RequestBody User editUser){
        userExistingCheck(id);
        users.get(id).setFirstName(editUser.getFirstName());
        users.get(id).setSecondName(editUser.getSecondName());
        return users.get(id);
    }

    @GetMapping("/users/{id}/phonebook")
    public Collection<PhoneBookEntry> allPBEntries(@PathVariable String id) {
        userExistingCheck(id);
        return users.get(id).getPhoneBook().values();
    }

    @PostMapping("/users/{id}/phonebook")
    public String addPBEntry(@PathVariable String id, @RequestBody PhoneBookEntry pbe){
        userExistingCheck(id);
        users.get(id).addPBEntry(pbe);
        return "Phone book entry was added successfully!";
    }

    @GetMapping("/users/{id}/phonebook/{pbeId}")
    PhoneBookEntry getPBEntryById(@PathVariable String id, @PathVariable String pbeId){
        userExistingCheck(id);
        entryExistingCheck(id, pbeId);
        return users.get(id).getPBEntryById(pbeId);
    }

    @DeleteMapping("/users/{id}/phonebook/{pbeId}")
    String deletePBEntry(@PathVariable String id, @PathVariable String pbeId){
        userExistingCheck(id);
        entryExistingCheck(id, pbeId);
        users.get(id).deletePBEntry(pbeId);
        return "Phone book entry was deleted successfully!";
    }

    @PutMapping("/users/{id}/phonebook/{pbeId}")
    PhoneBookEntry editPBEntry(@PathVariable String id, @PathVariable String pbeId, @RequestBody PhoneBookEntry pbe){
        userExistingCheck(id);
        entryExistingCheck(id, pbeId);
        users.get(id).editPBEntry(pbeId, pbe.getFirstName(), pbe.getSecondName(), pbe.getPhoneNumber());
        return users.get(id).getPBEntryById(pbeId);
    }

    @GetMapping("/users/search")
    ArrayList<User> findUsersByName(@RequestParam(value = "name") String name){
        ArrayList<User> foundUsers = new ArrayList<>();
        for(User usr: users.values()){
            if(usr.fullName().contains(name)){
                foundUsers.add(usr);
            }
        }
        return foundUsers;
    }

    @GetMapping("users/{id}/phonebook/search")
    PhoneBookEntry findPBEntryByPhoneNumber(@PathVariable String id, @RequestParam(value = "phoneNumber") String phoneNumber){
        userExistingCheck(id);
        PhoneBookEntry foundPbe = users.get(id).getPBEntryByPhoneNumber(phoneNumber);
        if(foundPbe == null){
            throw new PBEntryNotFoundException("Requested phone number was not found!");
        }
        return foundPbe;
    }

}
