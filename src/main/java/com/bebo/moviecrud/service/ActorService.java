package com.bebo.moviecrud.service;

import java.sql.SQLException;
import java.util.List;

import com.bebo.moviecrud.model.entities.Actor;
import com.bebo.moviecrud.model.repositories.PersonRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ActorService {
    private final PersonRepository personRepository;
    @Autowired
    public ActorService(PersonRepository personRepository){
        this.personRepository=personRepository;
    }

    @Transactional(rollbackFor = { SQLException.class })
    public void bulkActors(List<Actor> actors) throws SQLException {
        try {
            for (Actor actor : actors) {
                personRepository.save(actor);  
            }

            
        } catch (Exception e) {
            throw new SQLException("Throwing exception for demoing rollback");

        }


    }
}
