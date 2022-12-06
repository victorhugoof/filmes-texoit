package br.com.victorhugoof.filmestexoit.service.impl;

import br.com.victorhugoof.filmestexoit.domain.MovieEntity;
import br.com.victorhugoof.filmestexoit.domain.ProducerEntity;
import br.com.victorhugoof.filmestexoit.domain.StudioEntity;
import br.com.victorhugoof.filmestexoit.model.Movie;
import br.com.victorhugoof.filmestexoit.repository.MovieRepository;
import br.com.victorhugoof.filmestexoit.service.MovieService;
import br.com.victorhugoof.filmestexoit.service.ProducerService;
import br.com.victorhugoof.filmestexoit.service.StudioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class MovieServiceImpl implements MovieService {

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private StudioService studioService;

    @Autowired
    private ProducerService producerService;

    @Transactional(readOnly = true)
    @Override
    public Page<Movie> list(Pageable pageable) {
        return movieRepository.findAll(pageable)
                .map(it -> Movie.builder()
                        .year(it.getYear())
                        .title(it.getTitle())
                        .studios(it.getStudios().stream().map(StudioEntity::getName).collect(Collectors.toSet()))
                        .producers(it.getProducers().stream().map(ProducerEntity::getName).collect(Collectors.toSet()))
                        .build());
    }

    @Transactional
    @Override
    public List<MovieEntity> saveAll(List<Movie> movies) {
        var studioCache = new HashMap<String, StudioEntity>();
        var producerCache = new HashMap<String, ProducerEntity>();

        var entities = movies.stream().map(it -> toEntity(it, studioCache, producerCache)).toList();
        return movieRepository.saveAll(entities);
    }

    private MovieEntity toEntity(Movie movie, Map<String, StudioEntity> studioCache, Map<String, ProducerEntity> producerCache) {
        var entity = movieRepository.findFirstByTitleIgnoreCase(movie.getTitle()).orElseGet(MovieEntity::new);
        entity.setTitle(movie.getTitle());
        entity.setYear(movie.getYear());
        entity.setWinner(movie.isWinner());
        entity.setStudios(getFromCacheOrElse(movie.getStudios(), studioCache, studioService::findOrCreate));
        entity.setProducers(getFromCacheOrElse(movie.getProducers(), producerCache, producerService::findOrCreate));
        return entity;
    }

    private <K, T> Set<T> getFromCacheOrElse(Set<K> values, Map<K, T> cache, Function<K, T> orElse) {
        return values.stream()
                .map(it -> {
                    if (cache.containsKey(it)) {
                        return cache.get(it);
                    }

                    var res = orElse.apply(it);
                    cache.put(it, res);
                    return res;
                })
                .collect(Collectors.toSet());
    }
}
