package fr.formation.inti.service.impl;

import fr.formation.inti.service.StadeService;
import fr.formation.inti.domain.Stade;
import fr.formation.inti.repository.StadeRepository;
import fr.formation.inti.repository.search.StadeSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link Stade}.
 */
@Service
@Transactional
public class StadeServiceImpl implements StadeService {

    private final Logger log = LoggerFactory.getLogger(StadeServiceImpl.class);

    private final StadeRepository stadeRepository;

    private final StadeSearchRepository stadeSearchRepository;

    public StadeServiceImpl(StadeRepository stadeRepository, StadeSearchRepository stadeSearchRepository) {
        this.stadeRepository = stadeRepository;
        this.stadeSearchRepository = stadeSearchRepository;
    }

    /**
     * Save a stade.
     *
     * @param stade the entity to save.
     * @return the persisted entity.
     */
    @Override
    public Stade save(Stade stade) {
        log.debug("Request to save Stade : {}", stade);
        Stade result = stadeRepository.save(stade);
        stadeSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the stades.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Stade> findAll(Pageable pageable) {
        log.debug("Request to get all Stades");
        return stadeRepository.findAll(pageable);
    }
    /**
     * Get all the stades.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Stade> findByUserIsCurrentUser(Pageable pageable) {
        log.debug("Request to get all Stades");
        return stadeRepository.findByUserIsCurrentUser(pageable);
    }
    /**
     * Get one stade by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Stade> findOne(Long id) {
        log.debug("Request to get Stade : {}", id);
        return stadeRepository.findById(id);
    }

    /**
     * Delete the stade by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Stade : {}", id);
        stadeRepository.deleteById(id);
        stadeSearchRepository.deleteById(id);
    }

    /**
     * Search for the stade corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Stade> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Stades for query {}", query);
        return stadeSearchRepository.search(queryStringQuery(query), pageable);    }
}
