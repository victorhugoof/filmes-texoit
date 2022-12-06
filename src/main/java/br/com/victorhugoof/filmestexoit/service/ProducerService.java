package br.com.victorhugoof.filmestexoit.service;

import br.com.victorhugoof.filmestexoit.domain.ProducerEntity;

public interface ProducerService {

    ProducerEntity findOrCreate(String producer);
}
