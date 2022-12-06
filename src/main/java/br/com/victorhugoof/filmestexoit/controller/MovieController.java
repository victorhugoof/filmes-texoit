package br.com.victorhugoof.filmestexoit.controller;

import br.com.victorhugoof.filmestexoit.model.Movie;
import br.com.victorhugoof.filmestexoit.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/movie")
@RestController
public class MovieController {

    @Autowired
    private MovieService movieService;

    @GetMapping
    public Page<Movie> list(Pageable pageable) {
        return movieService.list(pageable);
    }
}
