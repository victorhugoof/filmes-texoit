package br.com.victorhugoof.filmestexoit.repository;

import br.com.victorhugoof.filmestexoit.domain.StudioEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface StudioRepository extends JpaRepository<StudioEntity, UUID> {

    Optional<StudioEntity> findFirstByNameIgnoreCase(String name);
}
