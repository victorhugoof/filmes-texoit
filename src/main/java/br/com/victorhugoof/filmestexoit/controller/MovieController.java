package br.com.victorhugoof.filmestexoit.controller;

import br.com.victorhugoof.filmestexoit.model.Movie;
import br.com.victorhugoof.filmestexoit.model.PrizeInterval;
import br.com.victorhugoof.filmestexoit.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/movie")
@RestController
public class MovieController {

    @Autowired
    private MovieService movieService;

    @GetMapping
    public List<Movie> list() {
        return movieService.list();
    }

    @GetMapping("/prize-interval")
    public PrizeInterval getPrizeInterval() {
        return movieService.getPrizeInterval();
    }
}
