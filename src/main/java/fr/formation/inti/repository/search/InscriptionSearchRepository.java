package fr.formation.inti.repository.search;

import fr.formation.inti.domain.Inscription;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Inscription} entity.
 */
public interface InscriptionSearchRepository extends ElasticsearchRepository<Inscription, Long> {
}
