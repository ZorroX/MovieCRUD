package com.bebo.moviecrud.model.entities;

import javax.persistence.Entity;
import javax.persistence.*;

@Entity
@Table(name = "actors")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class Actor extends Person {
    
}
