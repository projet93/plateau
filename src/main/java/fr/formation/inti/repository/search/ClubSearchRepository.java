package fr.formation.inti.repository.search;

import fr.formation.inti.domain.Club;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Club} entity.
 */
public interface ClubSearchRepository extends ElasticsearchRepository<Club, Long> {
}
