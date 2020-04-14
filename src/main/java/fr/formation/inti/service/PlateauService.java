package fr.formation.inti.service;

import fr.formation.inti.domain.Plateau;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link Plateau}.
 */
public interface PlateauService {

    /**
     * Save a plateau.
     *
     * @param plateau the entity to save.
     * @return the persisted entity.
     */
    Plateau save(Plateau plateau);

    /**
     * Get all the plateaus.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Plateau> findAll(Pageable pageable);

    /**
     * Get the "id" plateau.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Plateau> findOne(Long id);

    /**
     * Delete the "id" plateau.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the plateau corresponding to the query.
     *
     * @param query the query of the search.
     * 
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Plateau> search(String query, Pageable pageable);
}
