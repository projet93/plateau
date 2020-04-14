package fr.formation.inti.repository.search;

import fr.formation.inti.domain.Referent;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Referent} entity.
 */
public interface ReferentSearchRepository extends ElasticsearchRepository<Referent, Long> {
}
