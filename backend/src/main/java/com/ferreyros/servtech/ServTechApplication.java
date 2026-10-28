package com.ferreyros.servtech;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

/* SISTEMA WEB SERVTECH 2026 - FERREYROS S.A.*/
@SpringBootApplication
@EnableCaching
public class ServTechApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServTechApplication.class, args);
        System.out.println("=================================================");
        System.out.println(" Ferreyros ServTech 2026 - Backend Iniciado OK   ");
        System.out.println(" Puerto: http://localhost:8080                   ");
        System.out.println(" Conectado a PostgreSQL (Supabase Cloud)         ");
        System.out.println("=================================================");
    }
}
