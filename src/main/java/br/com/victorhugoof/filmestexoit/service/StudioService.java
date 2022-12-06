package br.com.victorhugoof.filmestexoit.service;

import br.com.victorhugoof.filmestexoit.domain.StudioEntity;

public interface StudioService {

    StudioEntity findOrCreate(String studio);
}
