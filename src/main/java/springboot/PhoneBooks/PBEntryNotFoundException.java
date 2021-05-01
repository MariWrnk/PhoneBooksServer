package springboot.PhoneBooks;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Requested entry was not found.")
public class PBEntryNotFoundException extends RuntimeException{
    public PBEntryNotFoundException(String ex){
        super(ex);
    }
}
