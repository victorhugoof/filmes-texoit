package br.com.victorhugoof.filmestexoit.repository;

import br.com.victorhugoof.filmestexoit.domain.ProducerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProducerRepository extends JpaRepository<ProducerEntity, UUID> {

    Optional<ProducerEntity> findFirstByNameIgnoreCase(String name);
}
