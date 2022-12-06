package br.com.victorhugoof.filmestexoit.service.impl;

import br.com.victorhugoof.filmestexoit.domain.MovieEntity;
import br.com.victorhugoof.filmestexoit.domain.ProducerEntity;
import br.com.victorhugoof.filmestexoit.domain.StudioEntity;
import br.com.victorhugoof.filmestexoit.model.Movie;
import br.com.victorhugoof.filmestexoit.model.PrizeInterval;
import br.com.victorhugoof.filmestexoit.model.ProducerPrizeInterval;
import br.com.victorhugoof.filmestexoit.repository.MovieRepository;
import br.com.victorhugoof.filmestexoit.service.MovieService;
import br.com.victorhugoof.filmestexoit.service.ProducerService;
import br.com.victorhugoof.filmestexoit.service.StudioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
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
    public List<Movie> list() {
        return movieRepository.findAll()
                .stream()
                .map(it -> Movie.builder()
                        .year(it.getYear())
                        .title(it.getTitle())
                        .studios(it.getStudios().stream().map(StudioEntity::getName).collect(Collectors.toSet()))
                        .producers(it.getProducers().stream().map(ProducerEntity::getName).collect(Collectors.toSet()))
                        .build())
                .toList();
    }

    @Transactional
    @Override
    public void saveAll(List<Movie> movies) {
        var studioCache = new HashMap<String, StudioEntity>();
        var producerCache = new HashMap<String, ProducerEntity>();

        var entities = movies.stream().map(it -> toEntity(it, studioCache, producerCache)).toList();
        movieRepository.saveAll(entities);
    }

    @Transactional(readOnly = true)
    @Override
    public PrizeInterval getPrizeInterval() {
        var winnerMovies = movieRepository.findAllByWinnerIsTrueOrderByYearAsc();

        var moviesByProducer = new HashMap<ProducerEntity, List<MovieEntity>>();
        winnerMovies.forEach(movie -> movie.getProducers().forEach(producer -> {
            if (!moviesByProducer.containsKey(producer)) {
                moviesByProducer.put(producer, new ArrayList<>());
            }
            moviesByProducer.get(producer).add(movie);
        }));

        var prizeIntervalsSet = new HashSet<ProducerPrizeInterval>();
        moviesByProducer.forEach((producer, movies) -> {
            var moviesSize = movies.size();
            for (int i = 0; i < moviesSize; i++) {
                var nextIndex = i + 1;

                if (nextIndex < moviesSize) {
                    var current = movies.get(i);
                    var next = movies.get(nextIndex);
                    prizeIntervalsSet.add(ProducerPrizeInterval.builder()
                            .producer(producer.getName())
                            .interval(Math.abs(next.getYear().getValue() - current.getYear().getValue()))
                            .previousWin(current.getYear())
                            .followingWin(next.getYear())
                            .build());
                }
            }
        });

        var minInterval = prizeIntervalsSet.stream().map(ProducerPrizeInterval::getInterval).min(Integer::compareTo).orElse(0);
        var maxInterval = prizeIntervalsSet.stream().map(ProducerPrizeInterval::getInterval).max(Integer::compareTo).orElse(0);

        return PrizeInterval.builder()
                .min(getIntervalList(prizeIntervalsSet, minInterval))
                .max(getIntervalList(prizeIntervalsSet, maxInterval))
                .build();
    }

    private List<ProducerPrizeInterval> getIntervalList(Set<ProducerPrizeInterval> itens, Integer interval) {
        var list = new ArrayList<>(itens.stream().filter(it -> it.getInterval().equals(interval)).toList());
        list.sort(Comparator.comparing(ProducerPrizeInterval::getPreviousWin));
        return list;
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
