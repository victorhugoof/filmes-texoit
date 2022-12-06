package br.com.victorhugoof.filmestexoit.repository;

import br.com.victorhugoof.filmestexoit.domain.MovieEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface MovieRepository extends JpaRepository<MovieEntity, UUID> {

    Optional<MovieEntity> findFirstByTitleIgnoreCase(String title);
}
