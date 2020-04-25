package fr.formation.inti.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.formation.inti.domain.Referent;
import fr.formation.inti.repository.ReferentRepository;
import fr.formation.inti.repository.search.ReferentSearchRepository;
import fr.formation.inti.service.ReferentService;

/**
 * Service Implementation for managing {@link Referent}.
 */
@Service
@Transactional
public class ReferentServiceImpl implements ReferentService {

    private final Logger log = LoggerFactory.getLogger(ReferentServiceImpl.class);

    @Autowired
    private ReferentRepository referentRepository;

    @Autowired
    private ReferentSearchRepository referentSearchRepository;

    public ReferentServiceImpl() {
     
    }

    /**
     * Save a referent.
     *
     * @param referent the entity to save.
     * @return the persisted entity.
     */
    @Override
    public Referent save(Referent referent) {
        log.debug("Request to save Referent : {}", referent);
        Referent result = referentRepository.save(referent);
        referentSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the referents.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Referent> findAll(Pageable pageable) {
        log.debug("Request to get all Referents");
        return referentRepository.findAll(pageable);
    }
    
    /**
     * Get all the referents.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Referent> findByUserIsCurrentUser(Pageable pageable) {
        log.debug("Request to get all Referents");
        return referentRepository.findByUserIsCurrentUser(pageable);
    }

    /**
     * Get one referent by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Referent> findOne(Long id) {
        log.debug("Request to get Referent : {}", id);
        return referentRepository.findById(id);
    }

    /**
     * Delete the referent by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Referent : {}", id);
        referentRepository.deleteById(id);
        referentSearchRepository.deleteById(id);
    }

    /**
     * Search for the referent corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Referent> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Referents for query {}", query);
        return referentSearchRepository.search(queryStringQuery(query), pageable);    }
}
