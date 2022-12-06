package br.com.victorhugoof.filmestexoit.service;

import br.com.victorhugoof.filmestexoit.model.Movie;
import br.com.victorhugoof.filmestexoit.model.PrizeInterval;

import java.util.List;

public interface MovieService {

    List<Movie> list();

    void saveAll(List<Movie> movies);

    PrizeInterval getPrizeInterval();
}
