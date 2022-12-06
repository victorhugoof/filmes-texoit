package br.com.victorhugoof.filmestexoit.model;

import lombok.Builder;
import lombok.Getter;

import java.time.Year;

@Builder
@Getter
public class ProducerPrizeInterval {

    private String producer;
    private Integer interval;
    private Year previousWin;
    private Year followingWin;
}
