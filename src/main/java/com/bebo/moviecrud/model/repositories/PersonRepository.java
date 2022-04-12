package com.bebo.moviecrud.model.repositories;

import java.util.List;

import com.bebo.moviecrud.model.entities.Actor;
import com.bebo.moviecrud.model.entities.Director;
import com.bebo.moviecrud.model.entities.Person;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface PersonRepository extends CrudRepository<Person, Long> {
    @Query("from Director")
    List<Director> findAllDirectors();

    @Query("from Actor")
    List<Actor> findAllActors();

    @Query("from Actor where id=:i")
    Actor findActorById(long i);

    @Query("from Director where id=:i")
    Actor findDirectorById(long i);



}
