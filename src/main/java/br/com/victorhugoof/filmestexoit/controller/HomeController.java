package br.com.victorhugoof.filmestexoit.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/")
@RestController
public class HomeController {

    @GetMapping
    public String index() {
        return "OK";
    }
}
