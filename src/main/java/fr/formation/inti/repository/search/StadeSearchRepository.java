package fr.formation.inti.repository.search;

import fr.formation.inti.domain.Stade;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Stade} entity.
 */
public interface StadeSearchRepository extends ElasticsearchRepository<Stade, Long> {
}
