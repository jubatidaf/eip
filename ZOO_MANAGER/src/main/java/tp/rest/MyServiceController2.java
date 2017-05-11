package tp.rest;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tp.model.*;

import java.util.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
/**
 * Created by root on 10/05/17.
 */
@RestController
@EnableAutoConfiguration
public class MyServiceController2 {

    public static void main(String[] args) throws Exception {
        System.getProperties().put( "server.port", "8087");
        SpringApplication.run(MyServiceController2.class, args);
    }

    private Center center;

    public MyServiceController2() {
        // Creates a new center
        center = new Center(new LinkedList<>(), new Position(49.30494d, 1.2170602d), "Biotropica");
        // And fill it with some animals
        Cage usa = new Cage(
                "usa",
                new Position(49.305d, 1.2157357d),
                25,
                new LinkedList<>(Arrays.asList(
                        new Animal("Ticjub", "usa", "Chipmunk", UUID.randomUUID()),
                        new Animal("Tac", "usa", "Chipmunk", UUID.randomUUID())
                ))
        );

        Cage amazon = new Cage(
                "amazon",
                new Position(49.305142d, 1.2154067d),
                15,
                new LinkedList<>(Arrays.asList(
                        new Animal("Canine", "amazon", "Piranha", UUID.randomUUID()),
                        new Animal("arsen", "amazon", "Piranha", UUID.randomUUID()),
                        new Animal("autre", "amazon", "Piranha", UUID.randomUUID()),
                        new Animal("makh", "amazon", "Piranha", UUID.randomUUID()),
                        new Animal("chris", "amazon", "Piranha", UUID.randomUUID())
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

    // /animals/findByName
    @RequestMapping(path = "/animals/findByName", method = POST, produces = APPLICATION_JSON_VALUE)
    public Animal getAnimalByName(@RequestBody String name) throws AnimalNotFoundException {
        return center.findAnimalByName(name);
    }

    // /animals/findByName
    @RequestMapping(path = "/cage/findByName", method = POST, produces = APPLICATION_JSON_VALUE)
    public Cage getCageByName(@RequestBody String name) throws CageNotFoundException {
        return center.findCageByName(name);
    }

}