

## Idea

My idea is to generate a CRUD for a Movie database so the first thing I want to design is the DB ERD:



![DER](https://i.ibb.co/r2hr9Xp/DER.png)

So I wanted to store Movies, with their directors and actors, also wanted to add a Catalog of categories so we can search by it.



So with these I know that the application should have 4 views to implement CRUD in each table.

## Spring Application

 I used Spring Initializer to generate a Maven based  project with the Spring Web Dependencies, I added Spring Web, and Spring Data for the Hibernate dependencies.
 ![enter image description here](https://i.ibb.co/ZLLWbK6/spring-Init.png)


So first I wanted to see if everything worked so I just created a WelcomeController  class 

    package com.bebo.moviecrud.controller;
    import org.springframework.stereotype.Controller;
    import org.springframework.ui.Model;
    import org.springframework.web.bind.annotation.GetMapping;
    import org.springframework.web.bind.annotation.RequestParam;
    @Controller
    public class WelcomeController {
    @GetMapping("/welcome")
    public String greeting(@RequestParam(name="name", required=false, defaultValue="World") String name, Model model) {
    model.addAttribute("name", name);
    return "greeting";
    }
    }

Its just a Simple controller that binds the /welcome path to resources/templates/welcome.html 

    <!DOCTYPE HTML>
    <html xmlns:th="http://www.thymeleaf.org">
    <head> 
    <title>Getting Started: Serving Web Content</title> 
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    </head>
    <body>
    <p th:text="'Welcome, ' + ${name} + '!'" />
    </body>
    </html>

I tried to run the app using ./mvnw spring-boot:run
but it fails with:

    Failed to configure a DataSource: 'url' attribute is not specified and no embedded datasource could be configured.
    
    Reason: Failed to determine a suitable driver class
    
    
    Action:
    
    Consider the following:
            If you want an embedded database (H2, HSQL or Derby), please put it on the classpath.
            If you have database settings to be loaded from a particular profile you may need to activate it (no profiles are currently active).

So I guess its because I added the Spring Data dependencies and its trying to connect to the database as default, I created my DB using MySQL so lets add the dependencies and the configurations files to do the connection:

First the MySQL Driver Dependency:

    <dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
    <scope>runtime</scope>
    </dependency>


Then the src/main/resources/application.properties

    spring.datasource.url=jdbc:mysql://localhost:3306/movies
    spring.datasource.username=moviesAdmin
    spring.datasource.password=Mexico123
    spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
    server.port=8081


I also changed the default port that Spring Boot uses to 8081

And with that I re run  ./mvnw spring-boot:run 

And now is working, good old Hello World !

![enter image description here](https://i.ibb.co/42pstsT/localhost.png)






So, I have already create my DB, can I automatically generate my Entities using Hibernate?

I remember it was a thing with Eclipse and eclipse link, but for this POC im using Visual Studio Code, so lets search if there is a tool for that… hmm , 10 minutes later I just find the plugin for Eclipse and IntelliJ, well I guess I have to generate them by hand, at least its not a big DB.

So lets start by the Category catalog, its just 2 fields

    package com.bebo.moviecrud.model.entities;
    
    import javax.persistence.Entity;
    import javax.persistence.GeneratedValue;
    import javax.persistence.GenerationType;
    import javax.persistence.Id;
    import javax.persistence.Table;
    
    import lombok.Data;
    
    @Entity
    @Table(name = "categories")
    @Data
    public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long categoryId;
    private String categoryName;

}


I also added loombok library to handle the getters and setters.

Its needs a Repository so:

    package com.bebo.moviecrud.model.repositories;
    
    import com.bebo.moviecrud.model.entities.Category;
    
    import org.springframework.data.repository.CrudRepository;
    import org.springframework.stereotype.Repository;
    
    @Repository
    public interface CategoryRepository extends CrudRepository<Category, Long> {}


Now the controller to handle the logic, to the Create, Read, Update and Delete

    package com.bebo.moviecrud.controller;
    
    import javax.validation.Valid;
    
    import com.bebo.moviecrud.model.entities.Category;
    import com.bebo.moviecrud.model.repositories.CategoryRepository;
    
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.stereotype.Controller;
    import org.springframework.ui.Model;
    import org.springframework.validation.BindingResult;
    import org.springframework.validation.annotation.Validated;
    import org.springframework.web.bind.annotation.GetMapping;
    import org.springframework.web.bind.annotation.PathVariable;
    import org.springframework.web.bind.annotation.PostMapping;
    
    @Controller
    public class CategoryController {
    private final CategoryRepository categoryRepository;
    
    @Autowired
    public CategoryController(CategoryRepository categoryRepository) {
    this.categoryRepository = categoryRepository;
    }
    
    @GetMapping("/addcat")
    public String showAddCategoryForm(Category category) {
    return "add-category";
    }
    @PostMapping("/addcategory")
    public String addCategory(@Valid Category category, BindingResult result, Model model) {
    if (result.hasErrors()) {
    return "add-category";
    }
    categoryRepository.save(category);
    return "redirect:/categories";
    }
    
    @GetMapping("/editcategory/{id}")
    public String showUpdateCategoryForm(@PathVariable("id") long id, Model model) {
    Category category = categoryRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid category Id:" + id));
    model.addAttribute("category", category);
    return "update-category";
    }
    
    @GetMapping("/deletecategory/{id}")
    public String deleteCategory(@PathVariable("id") long id, Model model) {
    Category category = categoryRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
    categoryRepository.delete(category);
    return "redirect:/categories";
    }
    
    @PostMapping("/updatecategory/{id}")
    public String updateCategory(@PathVariable("id") long id, @Valid Category category, BindingResult result, Model model) {
    
    category.setCategoryId(id);
    categoryRepository.save(category);
    
    return "redirect:/categories";
    }
    
    
    @GetMapping("/categories")
    public String showCategoryList(Model model) {
    model.addAttribute("categories", categoryRepository.findAll());
    return "categories";
    }
    }



With the controller created, I just need the views:

So categories.html, list all the categories and add the delete and edit butons to each, as well as an add button to crete new Categories

    <div th:switch="${categories}">
    <h2 th:case="null">No categories yet!</h2>
    <div th:case="*">
    <h2>Categories</h2>
    <table>
    <thead>
    <tr>
    <th>Category Name</th>
    <th>Edit</th>
    <th>Delete</th>
    </tr>
    </thead>
    <tbody>
    <tr th:each="category : ${categories}">
    <td th:text="${category.categoryName}"></td>
    <td><a th:href="@{/editcategory/{id}(id=${category.categoryId})}">Edit</a></td>
    <td><a th:href="@{/deletecategory/{id}(id=${category.categoryId})}">Delete</a></td>
    </tr>
    </tbody>
    </table>
    </div> 
    <p><a href="/addcat">Add a new category</a></p>
    </div>


The add-categories.html recive the input from the user to add a category:

    <form action="#" th:action="@{/addcategory}" th:object="${category}" method="post">
    <label for="name">Category Name</label>
    <input type="text" th:field="*{categoryName}" id="name" placeholder="name">
    <span th:if="${#fields.hasErrors('categoryName')}" th:errors="*{name}"></span>
    <input type="submit" value="Add Category"> 
    </form>

And last the update.category.html its very similar but handles the update-category

    <form action="#" 
    th:action="@{/updatecategory/{id}(id=${category.categoryId})}" 
    th:object="${category}" 
    method="post">
    <label for="name">Category Name</label>
    <input type="text" th:field="*{categoryName}" id="name" placeholder="Name">
    <span th:if="${#fields.hasErrors('categoryName')}" th:errors="*{categoryName}"></span>
    <input type="submit" value="Update Category"> 
    </form>


So with that I can show how to do a simple CRUD  with one entity, lets review the assigment:

•    Spring Transactions
•    Sprint AOP
•    Spring Web
•    Hibernate
o    Hibernate criteria
o    HQL
o    Hibernate mappings
o    Hibernate Inheritance mapping

This covers:
Spring Web, Hibernate and Hibernate Mappings

## Hibernate Inheritance

So next I will cover  Hibernate Inheritance, its a way to handle inhereitance in entities without interfiering wit the actual database, and my tables Directors and Employes are very similar, so I can create a Super class called Person and that Directors and Employees inherits from it.

Lets see if I can pull it off:

So I create the Person MappedSuperClass 

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

And then the Directors and the Actores entities are just empty classes that heredits from person, but with the Inheritance strategy TABLE_PER_CLASS to keep each table separately.

Actor.java

    package com.bebo.moviecrud.model.entities;
    
    import javax.persistence.Entity;
    import javax.persistence.*;
    
    @Entity
    @Table(name = "actors")
    @Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
    public class Actor extends Person {
    }

Director.java

    package com.bebo.moviecrud.model.entities;
    import javax.persistence.*;
    
    @Entity
    @Table(name = "directors")
    @Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
    
    public class Director extends Person {
    }


Ok so now we need a Repository and a Controller, but because I created a super class I can creat just PersonRepository and PersonController to handle the logic to both of these entities.

    PersonRepository.java
    
    package com.bebo.moviecrud.model.repositories;
    
    import com.bebo.moviecrud.model.entities.Person;
    
    import org.springframework.data.repository.CrudRepository;
    
    public interface PersonRepository extends CrudRepository<Person, Long> {}

## PersonController.java

Ok so very much like the CategoriesController, I inyect the Repository

    @Autowired
    public PersonController(PersonRepository personRepository) {
    this.personRepository = personRepository;
    }

and then try to get all the directors like this:

    @GetMapping("/directors")
    public String showDirectorsList(Model model) {
    model.addAttribute("directors", personRepository.findAll());
    return "directors";
    }

but wait, is Hibernate smart enough to give me the correct entity?

    context with path [] threw exception [Request processing failed; nested exception is org.springframework.dao.InvalidDataAccessApiUsageException: Not an entity: class com.bebo.moviecrud.model.entities.Person; nested exception is java.lang.IllegalArgumentException: Not an entity: class com.bebo.moviecrud.model.entities.Person] with root cause

I guess not, so Person is not an entity class and cant give me results, hmm so how about if I add a method in the controller to find all directors:

    @Query("from Director")
    List<Director> findAllDirectors();

So here im using HQL sintaxis, insetad of telling the tale name, im telling the entity name, lets change the in the controller to use this instead:

    @GetMapping("/directors")
    public String showDirectorsList(Model model) {
    model.addAttribute("directors", personRepository.findAllDirectors());
    return "directors";
    }


and with that and the corresponding html templates we have our directors showing up.

So for actors is a copy paste, so now I can finally put some Movies in .

So for Movies it went similar but I had to figure out how to get relationships

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



JoinColumn was doing some weird behavior, if I dont use it, even for category_id so, if the two collumns have the same name you still need to set the name attribute.

Also, because I'm using just one repository for Directors and Actors, Hibernate could not resolve the entities and its giving me Persons only, so in the end I created a DirectorsRepository just to get the correct table. So the View for add-movie finished like this:

    <form  action="#"  th:action="@{/addmovie}"  th:object="${movie}"  method="post">
    <label  for="name">Movie Name</label>
    <input  type="text"  th:field="*{movieName}"  id="name"  placeholder="name">
        <span  th:if="${#fields.hasErrors('movieName')}"  th:errors="*{name}"></span>
        <label  for="name">Movie Sinopsis</label>
        <input  type="text"  th:field="*{movieSinopsis}"  id="sinopsis"  placeholder="sinopsis">
        <span  th:if="${#fields.hasErrors('movieSinopsis')}"  th:errors="*{sinopsis}"></span>
        <label  for="name">Movie Category</label>
        <select  class="form-control"  th:field="*{category}">
        <option  value="0">select category</option>
        <option  th:each="category : ${categories}"  th:value="${category.categoryId}"  th:text="${category.categoryName}"></option>
        </select>
        <label  for="name">Movie Director</label>
        <select  class="form-control"  th:field="*{director}">
        <option  value="0">select director</option>
        <option  th:each="director : ${directors}"  th:value="${director.id}"  th:text="${director.name}"></option>
        </select>
        <label  for="name">Movie Release</label>
        <input  type="date"  th:field="*{movieDate}"  id="movieDate"  placeholder="movieDate">
        <span  th:if="${#fields.hasErrors('movieDate')}"  th:errors="*{movieDate}"></span>
        <input  type="submit"  value="Add Movie">
        </form>



## SPRING AOP
So with the basic functionality working i will be adding AOP,  Aspects are a way to crosscut an application and adding functionality without modifying the implemented code.
I will be adding an audit for DB access, so because all my methods are using the CrudRepository Interface i can set a pinpoint to all the methods in that class to log any acces to the DB.

    package  com.bebo.moviecrud.audit;
    import  org.aspectj.lang.JoinPoint;
    import  org.aspectj.lang.annotation.Aspect;
    import  org.aspectj.lang.annotation.Before;
    import  org.springframework.stereotype.Component;
     @Aspect
    @Component
    public  class  ControllerAspect {
    @Before(value = "execution(* org.springframework.data.repository.CrudRepository.*(..))")
    public  void  beforeAdvice(JoinPoint  joinPoint) {
    System.out.println("DB Access from method: " + joinPoint.getSignature());
    
    }
    
    }

So i set it to Before, so it actually an attempt to access, but we can set it to Before and also also there is a Trow to catch errors specifically.

And also i Need to add the net tag to my MovieCrudApplication class

@EnableAspectJAutoProxy(proxyTargetClass=true)


## SPRING TRANSACTIONS

  

For the Spring Transactions i was planning to do a Bulk insert of actors, but i was having troubles desingning a form that dinamically generate an actor form, so I did just a test method to demostrate the funcionality.

  

So First I tried to add the transactional method to the PersonController

  

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
and of course this is a very bad way to implement a Try but its just for the sake of the demonstration.

This was not rolling back, so I read that I needed another stereotype of @Component, the @Service, that its just a Component but for the Service logic, so I ended up creating a new Package with the service.



