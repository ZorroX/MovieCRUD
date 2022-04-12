package com.bebo.moviecrud;

import org.springframework.boot.SpringApplication;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;


@SpringBootApplication
@EnableAspectJAutoProxy(proxyTargetClass=true)
public class MovieCrudApplication {

	public static void main(String[] args) {
		SpringApplication.run(MovieCrudApplication.class, args);
	}

}
