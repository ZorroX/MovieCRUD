package com.bebo.moviecrud.model.repositories;

import com.bebo.moviecrud.model.entities.Category;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends CrudRepository<Category, Long> {}
