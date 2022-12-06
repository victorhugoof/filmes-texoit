package br.com.victorhugoof.filmestexoit.model;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class PrizeInterval {

    private List<ProducerPrizeInterval> min;
    private List<ProducerPrizeInterval> max;
}
