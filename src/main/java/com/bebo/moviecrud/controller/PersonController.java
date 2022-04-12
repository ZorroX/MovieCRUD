package com.bebo.moviecrud.controller;

import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import com.bebo.moviecrud.model.entities.Actor;
import com.bebo.moviecrud.model.entities.Director;
import com.bebo.moviecrud.model.entities.Person;
import com.bebo.moviecrud.model.repositories.PersonRepository;
import com.bebo.moviecrud.service.ActorService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class PersonController {
private final PersonRepository personRepository;
private final ActorService actorService;


@Autowired
public PersonController(PersonRepository personRepository,ActorService actorService) {
    this.personRepository = personRepository;
    this.actorService=actorService;
}

@GetMapping("/directors")
public String showDirectorsList(Model model) {
model.addAttribute("directors", personRepository.findAllDirectors());
return "directors";
}
@GetMapping("/actors")
public String showActorsList(Model model) {
model.addAttribute("actors", personRepository.findAllActors());
return "actors";
}
@GetMapping("/addactorform")
public String showAddActorForm(Actor actor) {
    return "add-actor";
}
@GetMapping("/adddirectorform")
public String showAddDirectorForm(Director director) {
    return "add-director";
}
@PostMapping("/addactor")
    public String addActor(@Valid Actor actor, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "add-actor";
        }
        
        personRepository.save(actor);
        return "redirect:/actors";
    }
    @PostMapping("/adddirector")
    public String addDirector(@Valid Director director, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "add-director";
        }
        
        personRepository.save(director);
        return "redirect:/directors";
    }

    @GetMapping("/editactor/{id}")
    public String showUpdateActorForm(@PathVariable("id") long id, Model model) {
        
        Actor actor = personRepository.findActorById(id);
        model.addAttribute("actor", actor);
        
        return "update-actor";
    }
    @GetMapping("/editdirector/{id}")
    public String showUpdateDirectorForm(@PathVariable("id") long id, Model model) {
        
        Director director = (Director) personRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid category Id:" + id));
        model.addAttribute("director", director);
        
        return "update-director";
    }

    @PostMapping("/updateactor/{id}")
    public String updateCategory(@PathVariable("id") long id, @Valid Actor actor, BindingResult result, Model model) {

        actor.setId(id);
        personRepository.save(actor);

        return "redirect:/actors";
    }

    @PostMapping("/updatedirector/{id}")
    public String updateCategory(@PathVariable("id") long id, @Valid Director director, BindingResult result, Model model) {

        director.setId(id);
        personRepository.save(director);

        return "redirect:/directors";
    }

    @GetMapping("/deleteactor/{id}")
    public String deleteActor(@PathVariable("id") long id, Model model) {
        Person person = personRepository.findActorById(id);
        personRepository.delete(person);
        
        return "redirect:/actors";
    }

    @GetMapping("/deletedirector/{id}")
    public String deleteDirector(@PathVariable("id") long id, Model model) {
        Person person = personRepository.findDirectorById(id);
        personRepository.delete(person);
        
        return "redirect:/directors";
    }

    @GetMapping("/bulkactorstest")
    public String bulkActorsTest() {
    List<Actor> actors = new ArrayList<Actor>();
    Actor actor1= new Actor();
    Actor actor2= new Actor();
    Actor actor3= new Actor();
        actor1.setId(90);
        actor2.setId(91);
        actor3.setId(92);
        actor1.setName("Will Smith");
        actor1.setGender("male");     
        actor1.setBirthdate( Date.valueOf("2015-03-31"));
        actors.add(actor1);
        actor2.setName("Bruce Willis");
        actor2.setGender("male");     
        actor2.setBirthdate( Date.valueOf("2015-03-31"));
        actors.add(actor2);
        actor3.setName("Sharon Stone");
        actor3.setGender("female");     
        actor3.setBirthdate( Date.valueOf("2015-03-31"));
        actors.add(actor3);
        try {
            actorService.bulkActors(actors);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return "redirect:/actors";
    }

    

}
