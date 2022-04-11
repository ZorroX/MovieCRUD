package com.bebo.moviecrud.model.entities;

import java.sql.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "movies")
@Data
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long movieId;
    private String movieName;
    private String movieSinopsis;
    
    @OneToOne(optional = false)
    @JoinColumn(name = "director_id", referencedColumnName = "id")
    private Director director;

    @OneToOne(optional = false)
    @JoinColumn(name = "category_id")
    private Category category;

    private Date movieDate;
}
