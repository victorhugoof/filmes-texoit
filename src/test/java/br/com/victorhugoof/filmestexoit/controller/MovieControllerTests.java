package br.com.victorhugoof.filmestexoit.controller;

import br.com.victorhugoof.filmestexoit.helper.ResourceReader;
import br.com.victorhugoof.filmestexoit.importer.MovieListCsvImporter;
import br.com.victorhugoof.filmestexoit.model.Movie;
import br.com.victorhugoof.filmestexoit.model.PrizeInterval;
import br.com.victorhugoof.filmestexoit.repository.MovieRepository;
import br.com.victorhugoof.filmestexoit.repository.ProducerRepository;
import br.com.victorhugoof.filmestexoit.repository.StudioRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.assertj.core.api.Assertions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.TestPropertySource;

import java.io.IOException;
import java.util.List;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("/tests.properties")
public class MovieControllerTests {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private MovieListCsvImporter movieListCsvImporter;

    @Autowired
    private ResourceReader resourceReader;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private ProducerRepository producerRepository;

    @Autowired
    private StudioRepository studioRepository;

    @AfterEach
    void afterEach() {
        movieRepository.deleteAll();
        producerRepository.deleteAll();
        studioRepository.deleteAll();
    }

    @Test
    void testList() {
        var response = testRestTemplate.exchange(
                "/movie",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Movie>>() {
                }
        );
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).isEmpty();
    }

    @Test
    void testListWithDefaults() throws IOException {
        movieListCsvImporter.importMovieListCsv("classpath:/movielist.csv");

        var response = testRestTemplate.exchange(
                "/movie",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Movie>>() {
                }
        );
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).hasSize(206);
    }

    @ParameterizedTest
    @CsvSource({
            "/prize-interval/movielist",
            "/prize-interval/movielist2",
            "/prize-interval/movielist3",
            "/prize-interval/movielist4",
    })
    void testPrizeInterval(String fileName) throws IOException {
        var inputResource = String.format("classpath:%s.csv", fileName);
        var outputResource = String.format("classpath:%s.json", fileName);

        movieListCsvImporter.importMovieListCsv(inputResource);

        var expectedOutput = objectMapper.readValue(resourceReader.getResourceContents(outputResource), PrizeInterval.class);
        var response = testRestTemplate.exchange(
                "/movie/prize-interval",
                HttpMethod.GET,
                null,
                PrizeInterval.class
        );
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).usingRecursiveComparison().isEqualTo(expectedOutput);
    }
}
