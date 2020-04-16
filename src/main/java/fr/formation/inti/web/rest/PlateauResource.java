package fr.formation.inti.web.rest;

import fr.formation.inti.domain.Plateau;
import fr.formation.inti.domain.Stade;
import fr.formation.inti.domain.enumeration.Statut;
import fr.formation.inti.repository.UserRepository;
import fr.formation.inti.security.AuthoritiesConstants;
import fr.formation.inti.security.SecurityUtils;
import fr.formation.inti.service.PlateauService;
import fr.formation.inti.web.rest.errors.BadRequestAlertException;
import fr.formation.inti.service.dto.PlateauCriteria;
import fr.formation.inti.service.PlateauQueryService;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing {@link fr.formation.inti.domain.Plateau}.
 */
@RestController
@RequestMapping("/api")
public class PlateauResource {

    private final Logger log = LoggerFactory.getLogger(PlateauResource.class);

    private static final String ENTITY_NAME = "plateau";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    @Autowired
    private UserRepository userRepository;
    
    private final PlateauService plateauService;

    private final PlateauQueryService plateauQueryService;

    public PlateauResource(PlateauService plateauService, PlateauQueryService plateauQueryService) {
        this.plateauService = plateauService;
        this.plateauQueryService = plateauQueryService;
    }

    /**
     * {@code POST  /plateaus} : Create a new plateau.
     *
     * @param plateau the plateau to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new plateau, or with status {@code 400 (Bad Request)} if the plateau has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/plateaus")
    public ResponseEntity<Plateau> createPlateau(@RequestBody Plateau plateau) throws URISyntaxException {
        log.debug("REST request to save Plateau : {}", plateau);
        if (plateau.getId() != null) {
            throw new BadRequestAlertException("A new plateau cannot already have an ID", ENTITY_NAME, "idexists");
        }
        if (!SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.ADMIN)) {
            log.debug("No user passed in, using current user: {}", SecurityUtils.getCurrentUserLogin());
            plateau.setUser(userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin().orElse(null)).orElse(null));
        }
        plateau.setStatut(Statut.ENCOURS);
        Plateau result = plateauService.save(plateau);
        return ResponseEntity.created(new URI("/api/plateaus/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /plateaus} : Updates an existing plateau.
     *
     * @param plateau the plateau to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated plateau,
     * or with status {@code 400 (Bad Request)} if the plateau is not valid,
     * or with status {@code 500 (Internal Server Error)} if the plateau couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/plateaus")
    public ResponseEntity<Plateau> updatePlateau(@RequestBody Plateau plateau) throws URISyntaxException {
        log.debug("REST request to update Plateau : {}", plateau);
        if (plateau.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Plateau result = plateauService.save(plateau);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, plateau.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /plateaus} : get all the plateaus if valid is true
     *  and get All our plateaus 
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of plateaus in body.
     */
    @GetMapping("/plateaus")
    public ResponseEntity<List<Plateau>> getAllPlateaus(PlateauCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Plateaus by criteria: {}", criteria);
        Page<Plateau> page;
        if (!SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.ADMIN)) {
        	page = plateauService.findByUserIsCurrentUser(pageable);
        }else {
        	page = plateauQueryService.findByCriteria(criteria, pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /plateaus/count} : count all the plateaus.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/plateaus/count")
    public ResponseEntity<Long> countPlateaus(PlateauCriteria criteria) {
        log.debug("REST request to count Plateaus by criteria: {}", criteria);
        return ResponseEntity.ok().body(plateauQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /plateaus/:id} : get the "id" plateau.
     *
     * @param id the id of the plateau to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the plateau, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/plateaus/{id}")
    public ResponseEntity<Plateau> getPlateau(@PathVariable Long id) {
        log.debug("REST request to get Plateau : {}", id);
        Optional<Plateau> plateau = plateauService.findOne(id);
        return ResponseUtil.wrapOrNotFound(plateau);
    }

    /**
     * {@code DELETE  /plateaus/:id} : delete the "id" plateau.
     *
     * @param id the id of the plateau to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/plateaus/{id}")
    public ResponseEntity<Void> deletePlateau(@PathVariable Long id) {
        log.debug("REST request to delete Plateau : {}", id);
        plateauService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/plateaus?query=:query} : search for the plateau corresponding
     * to the query.
     *
     * @param query the query of the plateau search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/plateaus")
    public ResponseEntity<List<Plateau>> searchPlateaus(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Plateaus for query {}", query);
        Page<Plateau> page = plateauService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
