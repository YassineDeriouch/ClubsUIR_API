package com.example.api;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
public class ApiApplication {

    public static void main(String[] args) {SpringApplication.run(ApiApplication.class, args);}
        @Bean
        public ModelMapper modelMapper() { // added model mapper
            return new ModelMapper();
    }
}
