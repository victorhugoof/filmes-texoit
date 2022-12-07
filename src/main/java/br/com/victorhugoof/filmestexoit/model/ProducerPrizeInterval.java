package br.com.victorhugoof.filmestexoit.model;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.time.Year;

@EqualsAndHashCode
@Builder
@Getter
public class ProducerPrizeInterval {

    private String producer;
    private Integer interval;
    private Year previousWin;
    private Year followingWin;
}
