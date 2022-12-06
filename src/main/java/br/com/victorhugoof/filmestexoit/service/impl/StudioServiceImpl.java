package br.com.victorhugoof.filmestexoit.service.impl;

import br.com.victorhugoof.filmestexoit.domain.StudioEntity;
import br.com.victorhugoof.filmestexoit.repository.StudioRepository;
import br.com.victorhugoof.filmestexoit.service.StudioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StudioServiceImpl implements StudioService {

    @Autowired
    private StudioRepository studioRepository;

    @Transactional
    @Override
    public StudioEntity findOrCreate(String studio) {
        return studioRepository.findFirstByNameIgnoreCase(studio).orElseGet(() -> {
            var entity = new StudioEntity();
            entity.setName(studio);
            return studioRepository.save(entity);
        });
    }
}
