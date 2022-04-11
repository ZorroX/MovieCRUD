package com.bebo.moviecrud.model.entities;
import javax.persistence.*;

@Entity
@Table(name = "directors")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)

public class Director extends Person {
    
}
