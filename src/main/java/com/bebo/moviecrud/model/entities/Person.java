package com.bebo.moviecrud.model.entities;



import java.sql.Date;

import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import lombok.Data;

@MappedSuperclass
@Data
public class Person {
    @Id
    private long id;
    private String name;
    private String gender;
    private Date birthdate;
}
