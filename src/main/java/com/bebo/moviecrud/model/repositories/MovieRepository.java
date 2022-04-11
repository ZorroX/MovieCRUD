package com.bebo.moviecrud.model.repositories;

import com.bebo.moviecrud.model.entities.Movie;

import org.springframework.data.repository.CrudRepository;

public interface MovieRepository extends CrudRepository<Movie, Long>{
    
}
