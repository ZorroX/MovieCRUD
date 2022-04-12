package com.bebo.moviecrud.audit;


import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ControllerAspect {
    @Before(value = "execution(* org.springframework.data.repository.CrudRepository.*(..))")
	public void beforeAdvice(JoinPoint joinPoint) {
		System.out.println("DB Access from method: " + joinPoint.getSignature());
        
	}
}
