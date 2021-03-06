package fr.formation.inti.service;

import java.util.List;

import javax.persistence.criteria.JoinType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// for static metamodels
import fr.formation.inti.domain.Categorie_;
import fr.formation.inti.domain.Plateau;
import fr.formation.inti.domain.Plateau_;
import fr.formation.inti.domain.Referent_;
import fr.formation.inti.domain.Stade_;
import fr.formation.inti.domain.User_;
import fr.formation.inti.repository.PlateauRepository;
import fr.formation.inti.service.dto.PlateauCriteria;
import io.github.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Plateau} entities in the database.
 * The main input is a {@link PlateauCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Plateau} or a {@link Page} of {@link Plateau} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PlateauQueryService extends QueryService<Plateau> {

    private final Logger log = LoggerFactory.getLogger(PlateauQueryService.class);

    private final PlateauRepository plateauRepository;

    

    public PlateauQueryService(PlateauRepository plateauRepository) {
        this.plateauRepository = plateauRepository;
    }

    /**
     * Return a {@link List} of {@link Plateau} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Plateau> findByCriteria(PlateauCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Plateau> specification = createSpecification(criteria);
        return plateauRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Plateau} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Plateau> findByCriteria(PlateauCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Plateau> specification = createSpecification(criteria);
        return plateauRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PlateauCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Plateau> specification = createSpecification(criteria);
        return plateauRepository.count(specification);
    }

    /**
     * Function to convert {@link PlateauCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Plateau> createSpecification(PlateauCriteria criteria) {
        Specification<Plateau> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Plateau_.id));
            }
            if (criteria.getDateDebut() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDateDebut(), Plateau_.dateDebut));
            }
            if (criteria.getDateFin() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDateFin(), Plateau_.dateFin));
            }
            if (criteria.getHeureDebut() != null) {
                specification = specification.and(buildStringSpecification(criteria.getHeureDebut(), Plateau_.heureDebut));
            }
            if (criteria.getHeureFin() != null) {
                specification = specification.and(buildStringSpecification(criteria.getHeureFin(), Plateau_.heureFin));
            }
            if (criteria.getNombreEquipeMax() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getNombreEquipeMax(), Plateau_.nombreEquipeMax));
            }
//            if (criteria.getNombreEquipe() != null) {
//                specification = specification.and(buildRangeSpecification(criteria.getNombreEquipe(), Plateau_.nombreEquipe));
//            }
            if (criteria.getStatut() != null) {
                specification = specification.and(buildSpecification(criteria.getStatut(), Plateau_.statut));
            }
            if (criteria.getValid() != null) {
                specification = specification.and(buildSpecification(criteria.getValid(), Plateau_.valid));
            }
            if (criteria.getReferentId() != null) {
                specification = specification.and(buildSpecification(criteria.getReferentId(),
                    root -> root.join(Plateau_.referent, JoinType.LEFT).get(Referent_.id)));
            }
            if (criteria.getUserId() != null) {
                specification = specification.and(buildSpecification(criteria.getUserId(),
                    root -> root.join(Plateau_.user, JoinType.LEFT).get(User_.id)));
            }
            if (criteria.getStadeId() != null) {
                specification = specification.and(buildSpecification(criteria.getStadeId(),
                    root -> root.join(Plateau_.stade, JoinType.LEFT).get(Stade_.id)));
            }
            if (criteria.getCategorieId() != null) {
                specification = specification.and(buildSpecification(criteria.getCategorieId(),
                    root -> root.join(Plateau_.categorie, JoinType.LEFT).get(Categorie_.id)));
            }
        }
        return specification;
    }
}
