package br.com.victorhugoof.filmestexoit.listener;

import br.com.victorhugoof.filmestexoit.parser.MovieCsvParser;
import br.com.victorhugoof.filmestexoit.service.MovieService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Slf4j
@Component
public class ApplicationReady implements ApplicationListener<ApplicationReadyEvent> {

    @Autowired
    private MovieService movieService;

    @Autowired
    private MovieCsvParser movieCsvParser;

    @Autowired
    private ResourceLoader resourceLoader;

    @Value("${filmes-texoit.movielist-csv-file}")
    private String movieListCsvLocation;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        importMovielistCsv();
    }

    private void importMovielistCsv() {
        log.info("Importing movielist.csv");

        try (var in = getResource(movieListCsvLocation)) {
            var contents = StreamUtils.copyToString(in, StandardCharsets.UTF_8);
            var movies = movieCsvParser.parseLines(contents, true);
            movieService.saveAll(movies);
        } catch (IOException e) {
            log.error("Error when importing movielist.csv: {}", e.getMessage(), e);
        }
    }

    private InputStream getResource(String location) throws IOException {
        try {
            return resourceLoader.getResource(movieListCsvLocation).getInputStream();
        } catch (FileNotFoundException fe) {
            log.debug(fe.getMessage(), fe);
        }

        return new FileInputStream(location);
    }
}
