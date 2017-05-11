package tp.rest;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.*;

import ch.qos.logback.core.joran.conditional.ThenAction;
import tp.model.*;

import static org.springframework.http.MediaType.*;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
@EnableAutoConfiguration
public class MyServiceController {

    public static void main(String[] args) throws Exception {
    	System.getProperties().put( "server.port", "8025");
         SpringApplication.run(MyServiceController.class, args);
    }

    private Center center;

    public MyServiceController() {
        // Creates a new center
        center = new Center(new LinkedList<>(), new Position(49.30494d, 1.2170602d), "Biotropica");
        // And fill it with some animals
        Cage usa = new Cage(
                "usa",
                new Position(49.305d, 1.2157357d),
                25,
                new LinkedList<>(Arrays.asList(
                        new Animal("Tic", "usa", "Chipmunk", UUID.randomUUID()),
                        new Animal("Tac", "usa", "Chipmunk", UUID.randomUUID())
                ))
        );

        Cage amazon = new Cage(
                "amazon",
                new Position(49.305142d, 1.2154067d),
                15,
                new LinkedList<>(Arrays.asList(
                        new Animal("Canine", "amazon", "Piranha", UUID.randomUUID()),
                        new Animal("Incisive", "amazon", "Piranha", UUID.randomUUID()),
                        new Animal("Molaire", "amazon", "Piranha", UUID.randomUUID()),
                        new Animal("De lait", "amazon", "Piranha", UUID.randomUUID())
                ))
        );

        center.getCages().addAll(Arrays.asList(usa, amazon));
    }

    // -----------------------------------------------------------------------------------------------------------------
    // /animals
    @RequestMapping(path = "/animals", method = GET, produces = APPLICATION_JSON_VALUE)
    public Center getAnimals(){
        return center;
    }

    @RequestMapping(path = "/animals", method = POST, produces = APPLICATION_JSON_VALUE)
    public Center addAnimal(@RequestBody Animal animal) throws CageNotFoundException {
        boolean success = this.center.getCages()
                .stream()
                .filter(cage -> cage.getName().equals(animal.getCage()))
                .findFirst()
                .orElseThrow(CageNotFoundException::new)
                .getResidents()
                .add(animal);
        if(success)return this.center;
        throw new IllegalStateException("Failing to add the animal while the input was valid and it's cage was existing is not suppose to happen.");
    }

    // -----------------------------------------------------------------------------------------------------------------
    // /animals/{id}
    @RequestMapping(path = "/animals/{id}", method = GET, produces = APPLICATION_JSON_VALUE)
    public Animal getAnimalById(@PathVariable UUID id) throws AnimalNotFoundException {
        return center.findAnimalById(id);
    }

    // /animals/findByName/{name]
    @RequestMapping(path = "/animals/findByName/{name}", method = GET, produces = APPLICATION_JSON_VALUE)
    public Animal getAnimalByName(@PathVariable String name, HttpServletResponse re) throws AnimalNotFoundException {
    	 if (re.getStatus()==404){
         	Animal animal=new Animal();
         	return animal;
         }else{
        return center.findAnimalByName(name);
        }
    }
    


    /// /animals/findByName
    @RequestMapping(path = "/animals/findByName", method = POST, produces = APPLICATION_JSON_VALUE)
    public Animal getAnimalByName(@RequestBody String name) throws AnimalNotFoundException {
        return center.findAnimalByName(name);
    }

    // /cage/findByName
    @RequestMapping(path = "/cage/findByName", method = POST, produces = APPLICATION_JSON_VALUE)
    public Cage getCageByName(@RequestBody String name) throws CageNotFoundException {
        return center.findCageByName(name);
    }
    
}
