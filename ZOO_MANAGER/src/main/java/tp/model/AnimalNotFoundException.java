package tp.model;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class AnimalNotFoundException extends Exception{
	public String getex(){
		return "no animal found";
	}
	public AnimalNotFoundException(){
		
	}
}
