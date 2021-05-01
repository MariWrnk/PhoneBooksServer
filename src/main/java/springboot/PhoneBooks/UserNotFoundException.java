package springboot.PhoneBooks;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Requested user was not found.")
public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException(String ex){
        super(ex);
    }
}
