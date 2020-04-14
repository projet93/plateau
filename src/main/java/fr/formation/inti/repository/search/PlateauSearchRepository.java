package fr.formation.inti.repository.search;

import fr.formation.inti.domain.Plateau;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Plateau} entity.
 */
public interface PlateauSearchRepository extends ElasticsearchRepository<Plateau, Long> {
}
