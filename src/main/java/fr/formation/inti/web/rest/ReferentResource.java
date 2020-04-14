package fr.formation.inti.web.rest;

import fr.formation.inti.domain.Referent;
import fr.formation.inti.service.ReferentService;
import fr.formation.inti.web.rest.errors.BadRequestAlertException;
import fr.formation.inti.service.dto.ReferentCriteria;
import fr.formation.inti.service.ReferentQueryService;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
 * REST controller for managing {@link fr.formation.inti.domain.Referent}.
 */
@RestController
@RequestMapping("/api")
public class ReferentResource {

    private final Logger log = LoggerFactory.getLogger(ReferentResource.class);

    private static final String ENTITY_NAME = "referent";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ReferentService referentService;

    private final ReferentQueryService referentQueryService;

    public ReferentResource(ReferentService referentService, ReferentQueryService referentQueryService) {
        this.referentService = referentService;
        this.referentQueryService = referentQueryService;
    }

    /**
     * {@code POST  /referents} : Create a new referent.
     *
     * @param referent the referent to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new referent, or with status {@code 400 (Bad Request)} if the referent has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/referents")
    public ResponseEntity<Referent> createReferent(@RequestBody Referent referent) throws URISyntaxException {
        log.debug("REST request to save Referent : {}", referent);
        if (referent.getId() != null) {
            throw new BadRequestAlertException("A new referent cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Referent result = referentService.save(referent);
        return ResponseEntity.created(new URI("/api/referents/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /referents} : Updates an existing referent.
     *
     * @param referent the referent to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated referent,
     * or with status {@code 400 (Bad Request)} if the referent is not valid,
     * or with status {@code 500 (Internal Server Error)} if the referent couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/referents")
    public ResponseEntity<Referent> updateReferent(@RequestBody Referent referent) throws URISyntaxException {
        log.debug("REST request to update Referent : {}", referent);
        if (referent.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Referent result = referentService.save(referent);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, referent.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /referents} : get all the referents.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of referents in body.
     */
    @GetMapping("/referents")
    public ResponseEntity<List<Referent>> getAllReferents(ReferentCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Referents by criteria: {}", criteria);
        Page<Referent> page = referentQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /referents/count} : count all the referents.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/referents/count")
    public ResponseEntity<Long> countReferents(ReferentCriteria criteria) {
        log.debug("REST request to count Referents by criteria: {}", criteria);
        return ResponseEntity.ok().body(referentQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /referents/:id} : get the "id" referent.
     *
     * @param id the id of the referent to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the referent, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/referents/{id}")
    public ResponseEntity<Referent> getReferent(@PathVariable Long id) {
        log.debug("REST request to get Referent : {}", id);
        Optional<Referent> referent = referentService.findOne(id);
        return ResponseUtil.wrapOrNotFound(referent);
    }

    /**
     * {@code DELETE  /referents/:id} : delete the "id" referent.
     *
     * @param id the id of the referent to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/referents/{id}")
    public ResponseEntity<Void> deleteReferent(@PathVariable Long id) {
        log.debug("REST request to delete Referent : {}", id);
        referentService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/referents?query=:query} : search for the referent corresponding
     * to the query.
     *
     * @param query the query of the referent search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/referents")
    public ResponseEntity<List<Referent>> searchReferents(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Referents for query {}", query);
        Page<Referent> page = referentService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
