package br.com.victorhugoof.filmestexoit.service.impl;

import br.com.victorhugoof.filmestexoit.domain.ProducerEntity;
import br.com.victorhugoof.filmestexoit.repository.ProducerRepository;
import br.com.victorhugoof.filmestexoit.service.ProducerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProducerServiceImpl implements ProducerService {

    @Autowired
    private ProducerRepository producerRepository;

    @Transactional
    @Override
    public ProducerEntity findOrCreate(String producer) {
        return producerRepository.findFirstByNameIgnoreCase(producer).orElseGet(() -> {
            var entity = new ProducerEntity();
            entity.setName(producer);
            return producerRepository.save(entity);
        });
    }
}
