package fr.formation.inti.service.impl;

import fr.formation.inti.service.CategorieService;
import fr.formation.inti.domain.Categorie;
import fr.formation.inti.repository.CategorieRepository;
import fr.formation.inti.repository.search.CategorieSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link Categorie}.
 */
@Service
@Transactional
public class CategorieServiceImpl implements CategorieService {

    private final Logger log = LoggerFactory.getLogger(CategorieServiceImpl.class);

    @Autowired
    private CategorieRepository categorieRepository;

    @Autowired
    private CategorieSearchRepository categorieSearchRepository;

    public CategorieServiceImpl() {
       
    }

    /**
     * Save a categorie.
     *
     * @param categorie the entity to save.
     * @return the persisted entity.
     */
    @Override
    public Categorie save(Categorie categorie) {
        log.debug("Request to save Categorie : {}", categorie);
        Categorie result = categorieRepository.save(categorie);
        categorieSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the categories.
     *
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public List<Categorie> findAll() {
        log.debug("Request to get all Categories");
        return categorieRepository.findAll();
    }

    
    @Override
    @Transactional(readOnly = true)
	public List<Categorie> findByUserIsCurrentUser() {
    	log.debug("Request to get all Categories by user");
		return categorieRepository.findByUserIsCurrentUser();
	}
    /**
     * Get one categorie by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Categorie> findOne(Long id) {
        log.debug("Request to get Categorie : {}", id);
        return categorieRepository.findById(id);
    }

    /**
     * Delete the categorie by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Categorie : {}", id);
        categorieRepository.deleteById(id);
        categorieSearchRepository.deleteById(id);
    }

    /**
     * Search for the categorie corresponding to the query.
     *
     * @param query the query of the search.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public List<Categorie> search(String query) {
        log.debug("Request to search Categories for query {}", query);
        return StreamSupport
            .stream(categorieSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

	
}
