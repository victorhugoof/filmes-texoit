package br.com.victorhugoof.filmestexoit.parser;

import br.com.victorhugoof.filmestexoit.model.Movie;
import org.springframework.stereotype.Component;

import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class MovieCsvParser {

    public List<Movie> parseLines(String csvLines, boolean firstLineIsHeader) {
        var lines = List.of(csvLines.split("\\r?\\n"));
        if (firstLineIsHeader) {
            lines = new ArrayList<>(lines);
            lines.remove(0);
        }

        return lines.stream()
                .map(this::parseLine)
                .toList();
    }

    public Movie parseLine(String line) {
        var cols = line.split(";", 5);
        if (cols.length != 5) {
            throw new IllegalStateException("Invalid layout");
        }

        var year = Year.parse(cols[0]);
        var title = cols[1];
        var studios = splitSet(cols[2]);
        var producers = splitSet(cols[3]);
        var winner = parseBoolean(cols[4]);

        return Movie.builder()
                .year(year)
                .title(title)
                .studios(studios)
                .producers(producers)
                .winner(winner)
                .build();
    }

    private boolean parseBoolean(String value) {
        return "yes".equalsIgnoreCase(value) || "true".equalsIgnoreCase(value);
    }

    private Set<String> splitSet(String line) {
        return Stream.of(line)
                .flatMap(it -> Stream.of(it.split(",")))
                .flatMap(it -> Stream.of(it.split("and")))
                .map(String::trim)
                .filter(it -> !it.isEmpty())
                .collect(Collectors.toSet());
    }
}
