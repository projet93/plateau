package fr.formation.inti.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.persistence.LockModeType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.formation.inti.domain.Inscription;
import fr.formation.inti.domain.Plateau;
import fr.formation.inti.domain.enumeration.Statut;
import fr.formation.inti.repository.InscriptionRepository;
import fr.formation.inti.repository.PlateauRepository;
import fr.formation.inti.repository.search.InscriptionSearchRepository;
import fr.formation.inti.service.InscriptionService;
import fr.formation.inti.web.rest.errors.BadRequestAlertException;

/**
 * Service Implementation for managing {@link Inscription}.
 */
@Service
@Transactional
public class InscriptionServiceImpl implements InscriptionService {

    private final Logger log = LoggerFactory.getLogger(InscriptionServiceImpl.class);

    private final InscriptionRepository inscriptionRepository;

    @Autowired
    private PlateauRepository plateauRepository;
    
    
    private final InscriptionSearchRepository inscriptionSearchRepository;

    public InscriptionServiceImpl(InscriptionRepository inscriptionRepository, InscriptionSearchRepository inscriptionSearchRepository) {
        this.inscriptionRepository = inscriptionRepository;
        this.inscriptionSearchRepository = inscriptionSearchRepository;
    }

    /**
     * Save a inscription.
     *
     * @param inscription the entity to save.
     * @return the persisted entity.
     */
    @Override
    @Transactional
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    public Inscription save(Inscription inscription) {
        log.debug("Request to save Inscription : {}", inscription);
        
        Plateau plateau = plateauRepository.findOneWithEagerRelationships(inscription.getPlateau().getId()).get();
        Integer nbrMax = plateau.getNombreEquipeMax();
        if(nbrMax < inscription.getNombreEquipe()) {
        	throw new BadRequestAlertException("impossible de faire l'inscription","inscription","");
        }
        if((nbrMax-inscription.getNombreEquipe()) == 0)
        	plateau.setStatut(Statut.CLOTURE);
        plateau.setNombreEquipeMax(nbrMax - inscription.getNombreEquipe());
        plateauRepository.save(plateau);
        inscription.setPlateau(plateau);
        Inscription result = inscriptionRepository.save(inscription);
        inscriptionSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the inscriptions.
     *
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public List<Inscription> findAll() {
        log.debug("Request to get all Inscriptions");
        return inscriptionRepository.findAll();
    }

    /**
     * Get one inscription by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Inscription> findOne(Long id) {
        log.debug("Request to get Inscription : {}", id);
        return inscriptionRepository.findById(id);
    }

    /**
     * Delete the inscription by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Inscription : {}", id);
        inscriptionRepository.deleteById(id);
        inscriptionSearchRepository.deleteById(id);
    }

    /**
     * Search for the inscription corresponding to the query.
     *
     * @param query the query of the search.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public List<Inscription> search(String query) {
        log.debug("Request to search Inscriptions for query {}", query);
        return StreamSupport
            .stream(inscriptionSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
