package springboot.PhoneBooks;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    private final User user = new User("Ivan", "Ivanov");
    private final User secondUser = new User("Ivan", "Fedorov");
    private final PhoneBookEntry pbe = new PhoneBookEntry("Peter", "Petrov", "3255516");
    private final ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private MockMvc mvc;

    @Autowired
    private UserController userController;

    @BeforeEach
    public void init(){
        userController.addUser(user);
        userController.addPBEntry(user.getId(), pbe);
    }

    @AfterEach
    public void close(){
        userController.deleteUser(user.getId());
    }

    @Test
    void allUsers() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/users")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].secondName", is(user.getSecondName())));
    }

    @Test
    void addUser() throws Exception {
        String req = mapper.writeValueAsString(secondUser);
        mvc.perform(MockMvcRequestBuilders.post("/users")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(req))
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo("User was added successfully!")));
        mvc.perform(MockMvcRequestBuilders.get("/users")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
        userController.deleteUser(secondUser.getId());
    }

    @Test
    void getUserById() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/users/" + user.getId())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.secondName", is(user.getSecondName())));
        mvc.perform(MockMvcRequestBuilders.get("/users/randomId")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteUser() throws Exception {
        mvc.perform(MockMvcRequestBuilders.delete("/users/" + user.getId())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo("User was deleted successfully!")));
        mvc.perform(MockMvcRequestBuilders.get("/users")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
        userController.addUser(user);
    }

    @Test
    void editUser() throws Exception {
        User editUser = new User("Fname", "Sname");
        String jsonRequest = mapper.writeValueAsString(editUser);
        mvc.perform(MockMvcRequestBuilders.put("/users/" + user.getId())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.secondName", is(editUser.getSecondName())));
    }

    @Test
    void allPBEntries() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/users/" + user.getId() + "/phonebook")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].secondName", is(pbe.getSecondName())))
                .andExpect(jsonPath("$[0].phoneNumber", is(pbe.getPhoneNumber())));
    }

    @Test
    void addPBEntry() throws Exception {
        PhoneBookEntry newPbe = new PhoneBookEntry("New", "Entry", "1234567");
        String jsonRequest = mapper.writeValueAsString(newPbe);
        mvc.perform(MockMvcRequestBuilders.post("/users/" + user.getId() + "/phonebook")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo("Phone book entry was added successfully!")));
        mvc.perform(MockMvcRequestBuilders.get("/users/" + user.getId() + "/phonebook")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    void testGetPBEntryById() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/users/" + user.getId() + "/phonebook/" + pbe.getId())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.secondName", is(pbe.getSecondName())))
                .andExpect(jsonPath("$.phoneNumber", is(pbe.getPhoneNumber())));
        mvc.perform(MockMvcRequestBuilders.get("/users/" + user.getId() + "/phonebook/randomId")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void deletePBEntry() throws Exception {
        mvc.perform(MockMvcRequestBuilders.delete("/users/" + user.getId() + "/phonebook/" + pbe.getId())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo("Phone book entry was deleted successfully!")));
        mvc.perform(MockMvcRequestBuilders.get("/users/" + user.getId() + "/phonebook")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void testEditPBEntry() throws Exception {
        PhoneBookEntry editPbe = new PhoneBookEntry("editedFname", "editedSname", "1234567");
        String jsonRequest = mapper.writeValueAsString(editPbe);
        mvc.perform(MockMvcRequestBuilders.put("/users/" + user.getId() + "/phonebook/" + pbe.getId())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.secondName", is(editPbe.getSecondName())))
                .andExpect(jsonPath("$.phoneNumber", is(editPbe.getPhoneNumber())));
    }

    @Test
    void findUsersByName() throws Exception {
        userController.addUser(secondUser);
        mvc.perform(MockMvcRequestBuilders.get("/users/search?name=Ivan")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
        mvc.perform(MockMvcRequestBuilders.get("/users/search?name=Ivanov")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
        userController.deleteUser(secondUser.getId());
    }

    @Test
    void findPBEntryByPhoneNumber() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/users/"+ user.getId()
                +"/phonebook/search?phoneNumber=" + pbe.getPhoneNumber())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.secondName", is(pbe.getSecondName())))
                .andExpect(jsonPath("$.phoneNumber", is(pbe.getPhoneNumber())));
        mvc.perform(MockMvcRequestBuilders.get("/users/"+ user.getId()
                +"/phonebook/search?phoneNumber=1111111")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}