package com.bebo.moviecrud.controller;

import javax.validation.Valid;

import com.bebo.moviecrud.model.entities.Actor;
import com.bebo.moviecrud.model.entities.Director;
import com.bebo.moviecrud.model.repositories.PersonRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class PersonController {
private final PersonRepository personRepository;

@Autowired
public PersonController(PersonRepository personRepository) {
    this.personRepository = personRepository;
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

}
