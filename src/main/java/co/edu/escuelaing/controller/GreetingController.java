package co.edu.escuelaing.controller;

import co.edu.escuelaing.annotations.GetMapping;
import co.edu.escuelaing.annotations.RequestParam;
import co.edu.escuelaing.annotations.RestController;

import java.util.concurrent.atomic.AtomicLong;

@RestController
public class GreetingController {

    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    @GetMapping("/greeting")
    public String greeting(@RequestParam(value = "name", defaultValue = "World") String name) {
        return "Hola " + name;
    }
}
