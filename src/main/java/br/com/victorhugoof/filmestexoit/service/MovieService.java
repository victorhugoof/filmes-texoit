package br.com.victorhugoof.filmestexoit.service;

import br.com.victorhugoof.filmestexoit.domain.MovieEntity;
import br.com.victorhugoof.filmestexoit.model.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MovieService {

    Page<Movie> list(Pageable pageable);

    List<MovieEntity> saveAll(List<Movie> movies);
}
