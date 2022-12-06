package br.com.victorhugoof.filmestexoit.model;

import lombok.Builder;
import lombok.Getter;

import java.time.Year;
import java.util.Set;

@Builder
@Getter
public class Movie {

    private Year year;
    private String title;
    private Set<String> studios;
    private Set<String> producers;
    private boolean winner;
}
