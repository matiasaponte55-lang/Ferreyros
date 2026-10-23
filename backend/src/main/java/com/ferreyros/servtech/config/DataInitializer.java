package com.ferreyros.servtech.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * Inicializador del sistema Ferreyros ServTech 2026.
 * Mantiene la base de datos totalmente limpia y sin datos precargados.
 * Las cuentas oficiales residen en Supabase y los datos se ingresan
 * exclusivamente a través de la interfaz web durante la operación.
 */
@Component
@Order(2)
public class DataInitializer implements CommandLineRunner {

    @Override
    public void run(String... args) {
        System.out.println("=================================================");
        System.out.println(" Ferreyros ServTech 2026 - Entorno Limpio Listo ");
        System.out.println(" Base de datos conectada sin datos precargados   ");
        System.out.println("=================================================");
    }
}
