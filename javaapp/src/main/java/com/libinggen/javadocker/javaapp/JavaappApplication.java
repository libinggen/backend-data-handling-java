package com.libinggen.javadocker.javaapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages = {"com.libinggen.javadocker.javaapp"})
@SpringBootApplication
public class JavaappApplication {

	public static void main(String[] args) {
		SpringApplication.run(JavaappApplication.class, args);
	}

}
