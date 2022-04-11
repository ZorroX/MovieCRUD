package com.bebo.moviecrud.controller;

import javax.validation.Valid;

import com.bebo.moviecrud.model.entities.Category;
import com.bebo.moviecrud.model.entities.Movie;
import com.bebo.moviecrud.model.repositories.CategoryRepository;
import com.bebo.moviecrud.model.repositories.DirectorRepository;
import com.bebo.moviecrud.model.repositories.MovieRepository;
import com.bebo.moviecrud.model.repositories.PersonRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class MovieController {
    private final MovieRepository movieRepository;
    private final CategoryRepository categoryRepository;
    private final PersonRepository personRepository;
    private final DirectorRepository directorRepository;

    @Autowired
    public MovieController( DirectorRepository directorRepository,
    MovieRepository movieRepository,
    CategoryRepository categoryRepository,
    PersonRepository personRepository) {
        this.movieRepository = movieRepository;
        this.categoryRepository = categoryRepository;
        this.personRepository = personRepository;
        this.directorRepository=directorRepository;
    }
    @GetMapping("/movies")
    public String showMoviesList(Model model) {
    model.addAttribute("movies", movieRepository.findAll());
    model.addAttribute("categories", categoryRepository.findAll());
    model.addAttribute("directors", personRepository.findAllDirectors());
    return "movies";
    }

    @GetMapping("/addmovieform")
    public String showAddMovieForm(Movie movie,Model model) {
        model.addAttribute("categories", categoryRepository.findAll());
        model.addAttribute("directors", directorRepository.findAll());
        return "add-movie";
    }

    @PostMapping("/addmovie")
    public String addMovie(@Valid Movie movie,Category category, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "add-movie";
        }
        
        movieRepository.save(movie);
        return "redirect:/movies";
    }

    @GetMapping("/editmovie/{id}")
    public String showUpdateMovieForm(@PathVariable("id") long id, Model model) {
        Movie movie = movieRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid category Id:" + id));
        model.addAttribute("movie", movie);
        
        return "update-movie";
    }

    @GetMapping("/deletemovie/{id}")
    public String deleteMovie(@PathVariable("id") long id, Model model) {
        Movie movie = movieRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
        movieRepository.delete(movie);
        
        return "redirect:/movies";
    }

    @PostMapping("/updatemovie/{id}")
    public String updateMoviey(@PathVariable("id") long id, @Valid Movie movie, BindingResult result, Model model) {

        movie.setMovieId(id);
        movieRepository.save(movie);

        return "redirect:/movies";
    }
}