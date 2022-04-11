package com.bebo.moviecrud.model.repositories;

import java.util.List;


import com.bebo.moviecrud.model.entities.Director;


import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface DirectorRepository extends CrudRepository<Director, Long> {
    @Query("from Director")
    List<Director> findAllDirectors();


}
