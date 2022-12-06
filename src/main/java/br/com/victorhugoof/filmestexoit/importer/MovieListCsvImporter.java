package br.com.victorhugoof.filmestexoit.importer;

import br.com.victorhugoof.filmestexoit.helper.ResourceReader;
import br.com.victorhugoof.filmestexoit.parser.MovieCsvParser;
import br.com.victorhugoof.filmestexoit.service.MovieService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class MovieListCsvImporter {

    @Autowired
    private MovieService movieService;

    @Autowired
    private MovieCsvParser movieCsvParser;

    @Autowired
    private ResourceReader resourceReader;

    @Value("${filmes-texoit.movielist-csv-autoimport:true}")
    private boolean movieListCsvAutoimport;

    @Value("${filmes-texoit.movielist-csv-file:classpath:movielist.csv}")
    private String movieListCsvFile;

    @EventListener(ApplicationReadyEvent.class)
    public void autoImport() {
        if (movieListCsvAutoimport) {
            try {
                importMovieListCsv(movieListCsvFile);
            } catch (Exception e) {
                log.error("Error when importing {}: {}", movieListCsvFile, e.getMessage(), e);
            }
        }
    }

    public void importMovieListCsv(String filePath) throws IOException {
        log.info("Importing {}", filePath);
        var contents = resourceReader.getResourceContents(filePath);
        var movies = movieCsvParser.parseLines(contents, true);
        movieService.saveAll(movies);
        log.info("Imported {} movies successfully", movies.size());
    }
}
