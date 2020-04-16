package fr.formation.inti.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;

import fr.formation.inti.PlateauFffApp;
import fr.formation.inti.domain.Plateau;
import fr.formation.inti.domain.Referent;
import fr.formation.inti.domain.User;
import fr.formation.inti.domain.enumeration.Statut;
import fr.formation.inti.repository.PlateauRepository;
import fr.formation.inti.repository.search.PlateauSearchRepository;
import fr.formation.inti.service.PlateauQueryService;
import fr.formation.inti.service.PlateauService;
/**
 * Integration tests for the {@link PlateauResource} REST controller.
 */
@SpringBootTest(classes = PlateauFffApp.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class PlateauResourceIT {

    private static final LocalDate DEFAULT_DATE_DEBUT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_DEBUT = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATE_DEBUT = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_DATE_FIN = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_FIN = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATE_FIN = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_HEURE_DEBUT = "AAAAAAAAAA";
    private static final String UPDATED_HEURE_DEBUT = "BBBBBBBBBB";

    private static final String DEFAULT_HEURE_FIN = "AAAAAAAAAA";
    private static final String UPDATED_HEURE_FIN = "BBBBBBBBBB";

    private static final byte[] DEFAULT_PROGRAMME = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_PROGRAMME = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_PROGRAMME_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_PROGRAMME_CONTENT_TYPE = "image/png";

    private static final String DEFAULT_ADRESSE = "AAAAAAAAAA";
    private static final String UPDATED_ADRESSE = "BBBBBBBBBB";

    private static final Integer DEFAULT_NBR_EQUIPE = 1;
    private static final Integer UPDATED_NBR_EQUIPE = 2;
    private static final Integer SMALLER_NBR_EQUIPE = 1 - 1;

    private static final Statut DEFAULT_STATUT = Statut.ENCOURS;
    private static final Statut UPDATED_STATUT = Statut.COMPLET;

    private static final Boolean DEFAULT_VALID = false;
    private static final Boolean UPDATED_VALID = true;

    @Autowired
    private PlateauRepository plateauRepository;

    @Mock
    private PlateauRepository plateauRepositoryMock;

    @Mock
    private PlateauService plateauServiceMock;

    @Autowired
    private PlateauService plateauService;

    /**
     * This repository is mocked in the fr.formation.inti.repository.search test package.
     *
     * @see fr.formation.inti.repository.search.PlateauSearchRepositoryMockConfiguration
     */
    @Autowired
    private PlateauSearchRepository mockPlateauSearchRepository;

    @Autowired
    private PlateauQueryService plateauQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPlateauMockMvc;

    private Plateau plateau;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Plateau createEntity(EntityManager em) {
        Plateau plateau = new Plateau()
            .dateDebut(DEFAULT_DATE_DEBUT)
            .dateFin(DEFAULT_DATE_FIN)
            .heureDebut(DEFAULT_HEURE_DEBUT)
            .heureFin(DEFAULT_HEURE_FIN)
            .programme(DEFAULT_PROGRAMME)
            .programmeContentType(DEFAULT_PROGRAMME_CONTENT_TYPE)
            .adresse(DEFAULT_ADRESSE)
            .nbrEquipe(DEFAULT_NBR_EQUIPE)
            .statut(DEFAULT_STATUT)
            .valid(DEFAULT_VALID);
        return plateau;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Plateau createUpdatedEntity(EntityManager em) {
        Plateau plateau = new Plateau()
            .dateDebut(UPDATED_DATE_DEBUT)
            .dateFin(UPDATED_DATE_FIN)
            .heureDebut(UPDATED_HEURE_DEBUT)
            .heureFin(UPDATED_HEURE_FIN)
            .programme(UPDATED_PROGRAMME)
            .programmeContentType(UPDATED_PROGRAMME_CONTENT_TYPE)
            .adresse(UPDATED_ADRESSE)
            .nbrEquipe(UPDATED_NBR_EQUIPE)
            .statut(UPDATED_STATUT)
            .valid(UPDATED_VALID);
        return plateau;
    }

    @BeforeEach
    public void initTest() {
        plateau = createEntity(em);
    }

    @Test
    @Transactional
    public void createPlateau() throws Exception {
        int databaseSizeBeforeCreate = plateauRepository.findAll().size();

        // Create the Plateau
        restPlateauMockMvc.perform(post("/api/plateaus")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(plateau)))
            .andExpect(status().isCreated());

        // Validate the Plateau in the database
        List<Plateau> plateauList = plateauRepository.findAll();
        assertThat(plateauList).hasSize(databaseSizeBeforeCreate + 1);
        Plateau testPlateau = plateauList.get(plateauList.size() - 1);
        assertThat(testPlateau.getDateDebut()).isEqualTo(DEFAULT_DATE_DEBUT);
        assertThat(testPlateau.getDateFin()).isEqualTo(DEFAULT_DATE_FIN);
        assertThat(testPlateau.getHeureDebut()).isEqualTo(DEFAULT_HEURE_DEBUT);
        assertThat(testPlateau.getHeureFin()).isEqualTo(DEFAULT_HEURE_FIN);
        assertThat(testPlateau.getProgramme()).isEqualTo(DEFAULT_PROGRAMME);
        assertThat(testPlateau.getProgrammeContentType()).isEqualTo(DEFAULT_PROGRAMME_CONTENT_TYPE);
        assertThat(testPlateau.getAdresse()).isEqualTo(DEFAULT_ADRESSE);
        assertThat(testPlateau.getNbrEquipe()).isEqualTo(DEFAULT_NBR_EQUIPE);
        assertThat(testPlateau.getStatut()).isEqualTo(DEFAULT_STATUT);
        assertThat(testPlateau.isValid()).isEqualTo(DEFAULT_VALID);

        // Validate the Plateau in Elasticsearch
        verify(mockPlateauSearchRepository, times(1)).save(testPlateau);
    }

    @Test
    @Transactional
    public void createPlateauWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = plateauRepository.findAll().size();

        // Create the Plateau with an existing ID
        plateau.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPlateauMockMvc.perform(post("/api/plateaus")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(plateau)))
            .andExpect(status().isBadRequest());

        // Validate the Plateau in the database
        List<Plateau> plateauList = plateauRepository.findAll();
        assertThat(plateauList).hasSize(databaseSizeBeforeCreate);

        // Validate the Plateau in Elasticsearch
        verify(mockPlateauSearchRepository, times(0)).save(plateau);
    }


    @Test
    @Transactional
    public void getAllPlateaus() throws Exception {
        // Initialize the database
        plateauRepository.saveAndFlush(plateau);

        // Get all the plateauList
        restPlateauMockMvc.perform(get("/api/plateaus?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(plateau.getId().intValue())))
            .andExpect(jsonPath("$.[*].dateDebut").value(hasItem(DEFAULT_DATE_DEBUT.toString())))
            .andExpect(jsonPath("$.[*].dateFin").value(hasItem(DEFAULT_DATE_FIN.toString())))
            .andExpect(jsonPath("$.[*].heureDebut").value(hasItem(DEFAULT_HEURE_DEBUT)))
            .andExpect(jsonPath("$.[*].heureFin").value(hasItem(DEFAULT_HEURE_FIN)))
            .andExpect(jsonPath("$.[*].programmeContentType").value(hasItem(DEFAULT_PROGRAMME_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].programme").value(hasItem(Base64Utils.encodeToString(DEFAULT_PROGRAMME))))
            .andExpect(jsonPath("$.[*].adresse").value(hasItem(DEFAULT_ADRESSE)))
            .andExpect(jsonPath("$.[*].nbrEquipe").value(hasItem(DEFAULT_NBR_EQUIPE)))
            .andExpect(jsonPath("$.[*].statut").value(hasItem(DEFAULT_STATUT.toString())))
            .andExpect(jsonPath("$.[*].valid").value(hasItem(DEFAULT_VALID.booleanValue())));
    }
    
    @SuppressWarnings({"unchecked"})
    public void getAllPlateausWithEagerRelationshipsIsEnabled() throws Exception {
        when(plateauServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPlateauMockMvc.perform(get("/api/plateaus?eagerload=true"))
            .andExpect(status().isOk());

        verify(plateauServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({"unchecked"})
    public void getAllPlateausWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(plateauServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPlateauMockMvc.perform(get("/api/plateaus?eagerload=true"))
            .andExpect(status().isOk());

        verify(plateauServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    public void getPlateau() throws Exception {
        // Initialize the database
        plateauRepository.saveAndFlush(plateau);

        // Get the plateau
        restPlateauMockMvc.perform(get("/api/plateaus/{id}", plateau.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(plateau.getId().intValue()))
            .andExpect(jsonPath("$.dateDebut").value(DEFAULT_DATE_DEBUT.toString()))
            .andExpect(jsonPath("$.dateFin").value(DEFAULT_DATE_FIN.toString()))
            .andExpect(jsonPath("$.heureDebut").value(DEFAULT_HEURE_DEBUT))
            .andExpect(jsonPath("$.heureFin").value(DEFAULT_HEURE_FIN))
            .andExpect(jsonPath("$.programmeContentType").value(DEFAULT_PROGRAMME_CONTENT_TYPE))
            .andExpect(jsonPath("$.programme").value(Base64Utils.encodeToString(DEFAULT_PROGRAMME)))
            .andExpect(jsonPath("$.adresse").value(DEFAULT_ADRESSE))
            .andExpect(jsonPath("$.nbrEquipe").value(DEFAULT_NBR_EQUIPE))
            .andExpect(jsonPath("$.statut").value(DEFAULT_STATUT.toString()))
            .andExpect(jsonPath("$.valid").value(DEFAULT_VALID.booleanValue()));
    }


    @Test
    @Transactional
    public void getPlateausByIdFiltering() throws Exception {
        // Initialize the database
        plateauRepository.saveAndFlush(plateau);

        Long id = plateau.getId();

        defaultPlateauShouldBeFound("id.equals=" + id);
        defaultPlateauShouldNotBeFound("id.notEquals=" + id);

        defaultPlateauShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultPlateauShouldNotBeFound("id.greaterThan=" + id);

        defaultPlateauShouldBeFound("id.lessThanOrEqual=" + id);
        defaultPlateauShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllPlateausByDateDebutIsEqualToSomething() throws Exception {
        // Initialize the database
        plateauRepository.saveAndFlush(plateau);

        // Get all the plateauList where dateDebut equals to DEFAULT_DATE_DEBUT
        defaultPlateauShouldBeFound("dateDebut.equals=" + DEFAULT_DATE_DEBUT);

        // Get all the plateauList where dateDebut equals to UPDATED_DATE_DEBUT
        defaultPlateauShouldNotBeFound("dateDebut.equals=" + UPDATED_DATE_DEBUT);
    }

    @Test
    @Transactional
    public void getAllPlateausByDateDebutIsNotEqualToSomething() throws Exception {
        // Initialize the database
        plateauRepository.saveAndFlush(plateau);

        // Get all the plateauList where dateDebut not equals to DEFAULT_DATE_DEBUT
        defaultPlateauShouldNotBeFound("dateDebut.notEquals=" + DEFAULT_DATE_DEBUT);

        // Get all the plateauList where dateDebut not equals to UPDATED_DATE_DEBUT
        defaultPlateauShouldBeFound("dateDebut.notEquals=" + UPDATED_DATE_DEBUT);
    }

    @Test
    @Transactional
    public void getAllPlateausByDateDebutIsInShouldWork() throws Exception {
        // Initialize the database
        plateauRepository.saveAndFlush(plateau);

        // Get all the plateauList where dateDebut in DEFAULT_DATE_DEBUT or UPDATED_DATE_DEBUT
        defaultPlateauShouldBeFound("dateDebut.in=" + DEFAULT_DATE_DEBUT + "," + UPDATED_DATE_DEBUT);

        // Get all the plateauList where dateDebut equals to UPDATED_DATE_DEBUT
        defaultPlateauShouldNotBeFound("dateDebut.in=" + UPDATED_DATE_DEBUT);
    }

    @Test
    @Transactional
    public void getAllPlateausByDateDebutIsNullOrNotNull() throws Exception {
        // Initialize the database
        plateauRepository.saveAndFlush(plateau);

        // Get all the plateauList where dateDebut is not null
        defaultPlateauShouldBeFound("dateDebut.specified=true");

        // Get all the plateauList where dateDebut is null
        defaultPlateauShouldNotBeFound("dateDebut.specified=false");
    }

    @Test
    @Transactional
    public void getAllPlateausByDateDebutIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        plateauRepository.saveAndFlush(plateau);

        // Get all the plateauList where dateDebut is greater than or equal to DEFAULT_DATE_DEBUT
        defaultPlateauShouldBeFound("dateDebut.greaterThanOrEqual=" + DEFAULT_DATE_DEBUT);

        // Get all the plateauList where dateDebut is greater than or equal to UPDATED_DATE_DEBUT
        defaultPlateauShouldNotBeFound("dateDebut.greaterThanOrEqual=" + UPDATED_DATE_DEBUT);
    }

    @Test
    @Transactional
    public void getAllPlateausByDateDebutIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        plateauRepository.saveAndFlush(plateau);

        // Get all the plateauList where dateDebut is less than or equal to DEFAULT_DATE_DEBUT
        defaultPlateauShouldBeFound("dateDebut.lessThanOrEqual=" + DEFAULT_DATE_DEBUT);

        // Get all the plateauList where dateDebut is less than or equal to SMALLER_DATE_DEBUT
        defaultPlateauShouldNotBeFound("dateDebut.lessThanOrEqual=" + SMALLER_DATE_DEBUT);
    }

    @Test
    @Transactional
    public void getAllPlateausByDateDebutIsLessThanSomething() throws Exception {
        // Initialize the database
        plateauRepository.saveAndFlush(plateau);

        // Get all the plateauList where dateDebut is less than DEFAULT_DATE_DEBUT
        defaultPlateauShouldNotBeFound("dateDebut.lessThan=" + DEFAULT_DATE_DEBUT);

        // Get all the plateauList where dateDebut is less than UPDATED_DATE_DEBUT
        defaultPlateauShouldBeFound("dateDebut.lessThan=" + UPDATED_DATE_DEBUT);
    }

    @Test
    @Transactional
    public void getAllPlateausByDateDebutIsGreaterThanSomething() throws Exception {
        // Initialize the database
        plateauRepository.saveAndFlush(plateau);

        // Get all the plateauList where dateDebut is greater than DEFAULT_DATE_DEBUT
        defaultPlateauShouldNotBeFound("dateDebut.greaterThan=" + DEFAULT_DATE_DEBUT);

        // Get all the plateauList where dateDebut is greater than SMALLER_DATE_DEBUT
        defaultPlateauShouldBeFound("dateDebut.greaterThan=" + SMALLER_DATE_DEBUT);
    }


    @Test
    @Transactional
    public void getAllPlateausByDateFinIsEqualToSomething() throws Exception {
        // Initialize the database
        plateauRepository.saveAndFlush(plateau);

        // Get all the plateauList where dateFin equals to DEFAULT_DATE_FIN
        defaultPlateauShouldBeFound("dateFin.equals=" + DEFAULT_DATE_FIN);

        // Get all the plateauList where dateFin equals to UPDATED_DATE_FIN
        defaultPlateauShouldNotBeFound("dateFin.equals=" + UPDATED_DATE_FIN);
    }

    @Test
    @Transactional
    public void getAllPlateausByDateFinIsNotEqualToSomething() throws Exception {
        // Initialize the database
        plateauRepository.saveAndFlush(plateau);

        // Get all the plateauList where dateFin not equals to DEFAULT_DATE_FIN
        defaultPlateauShouldNotBeFound("dateFin.notEquals=" + DEFAULT_DATE_FIN);

        // Get all the plateauList where dateFin not equals to UPDATED_DATE_FIN
        defaultPlateauShouldBeFound("dateFin.notEquals=" + UPDATED_DATE_FIN);
    }

    @Test
    @Transactional
    public void getAllPlateausByDateFinIsInShouldWork() throws Exception {
        // Initialize the database
        plateauRepository.saveAndFlush(plateau);

        // Get all the plateauList where dateFin in DEFAULT_DATE_FIN or UPDATED_DATE_FIN
        defaultPlateauShouldBeFound("dateFin.in=" + DEFAULT_DATE_FIN + "," + UPDATED_DATE_FIN);

        // Get all the plateauList where dateFin equals to UPDATED_DATE_FIN
        defaultPlateauShouldNotBeFound("dateFin.in=" + UPDATED_DATE_FIN);
    }

    @Test
    @Transactional
    public void getAllPlateausByDateFinIsNullOrNotNull() throws Exception {
        // Initialize the database
        plateauRepository.saveAndFlush(plateau);

        // Get all the plateauList where dateFin is not null
        defaultPlateauShouldBeFound("dateFin.specified=true");

        // Get all the plateauList where dateFin is null
        defaultPlateauShouldNotBeFound("dateFin.specified=false");
    }

    @Test
    @Transactional
    public void getAllPlateausByDateFinIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        plateauRepository.saveAndFlush(plateau);

        // Get all the plateauList where dateFin is greater than or equal to DEFAULT_DATE_FIN
        defaultPlateauShouldBeFound("dateFin.greaterThanOrEqual=" + DEFAULT_DATE_FIN);

        // Get all the plateauList where dateFin is greater than or equal to UPDATED_DATE_FIN
        defaultPlateauShouldNotBeFound("dateFin.greaterThanOrEqual=" + UPDATED_DATE_FIN);
    }

    @Test
    @Transactional
    public void getAllPlateausByDateFinIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        plateauRepository.saveAndFlush(plateau);

        // Get all the plateauList where dateFin is less than or equal to DEFAULT_DATE_FIN
        defaultPlateauShouldBeFound("dateFin.lessThanOrEqual=" + DEFAULT_DATE_FIN);

        // Get all the plateauList where dateFin is less than or equal to SMALLER_DATE_FIN
        defaultPlateauShouldNotBeFound("dateFin.lessThanOrEqual=" + SMALLER_DATE_FIN);
    }

    @Test
    @Transactional
    public void getAllPlateausByDateFinIsLessThanSomething() throws Exception {
        // Initialize the database
        plateauRepository.saveAndFlush(plateau);

        // Get all the plateauList where dateFin is less than DEFAULT_DATE_FIN
        defaultPlateauShouldNotBeFound("dateFin.lessThan=" + DEFAULT_DATE_FIN);

        // Get all the plateauList where dateFin is less than UPDATED_DATE_FIN
        defaultPlateauShouldBeFound("dateFin.lessThan=" + UPDATED_DATE_FIN);
    }

    @Test
    @Transactional
    public void getAllPlateausByDateFinIsGreaterThanSomething() throws Exception {
        // Initialize the database
        plateauRepository.saveAndFlush(plateau);

        // Get all the plateauList where dateFin is greater than DEFAULT_DATE_FIN
        defaultPlateauShouldNotBeFound("dateFin.greaterThan=" + DEFAULT_DATE_FIN);

        // Get all the plateauList where dateFin is greater than SMALLER_DATE_FIN
        defaultPlateauShouldBeFound("dateFin.greaterThan=" + SMALLER_DATE_FIN);
    }


    @Test
    @Transactional
    public void getAllPlateausByHeureDebutIsEqualToSomething() throws Exception {
        // Initialize the database
        plateauRepository.saveAndFlush(plateau);

        // Get all the plateauList where heureDebut equals to DEFAULT_HEURE_DEBUT
        defaultPlateauShouldBeFound("heureDebut.equals=" + DEFAULT_HEURE_DEBUT);

        // Get all the plateauList where heureDebut equals to UPDATED_HEURE_DEBUT
        defaultPlateauShouldNotBeFound("heureDebut.equals=" + UPDATED_HEURE_DEBUT);
    }

    @Test
    @Transactional
    public void getAllPlateausByHeureDebutIsNotEqualToSomething() throws Exception {
        // Initialize the database
        plateauRepository.saveAndFlush(plateau);

        // Get all the plateauList where heureDebut not equals to DEFAULT_HEURE_DEBUT
        defaultPlateauShouldNotBeFound("heureDebut.notEquals=" + DEFAULT_HEURE_DEBUT);

        // Get all the plateauList where heureDebut not equals to UPDATED_HEURE_DEBUT
        defaultPlateauShouldBeFound("heureDebut.notEquals=" + UPDATED_HEURE_DEBUT);
    }

    @Test
    @Transactional
    public void getAllPlateausByHeureDebutIsInShouldWork() throws Exception {
        // Initialize the database
        plateauRepository.saveAndFlush(plateau);

        // Get all the plateauList where heureDebut in DEFAULT_HEURE_DEBUT or UPDATED_HEURE_DEBUT
        defaultPlateauShouldBeFound("heureDebut.in=" + DEFAULT_HEURE_DEBUT + "," + UPDATED_HEURE_DEBUT);

        // Get all the plateauList where heureDebut equals to UPDATED_HEURE_DEBUT
        defaultPlateauShouldNotBeFound("heureDebut.in=" + UPDATED_HEURE_DEBUT);
    }

    @Test
    @Transactional
    public void getAllPlateausByHeureDebutIsNullOrNotNull() throws Exception {
        // Initialize the database
        plateauRepository.saveAndFlush(plateau);

        // Get all the plateauList where heureDebut is not null
        defaultPlateauShouldBeFound("heureDebut.specified=true");

        // Get all the plateauList where heureDebut is null
        defaultPlateauShouldNotBeFound("heureDebut.specified=false");
    }
                @Test
    @Transactional
    public void getAllPlateausByHeureDebutContainsSomething() throws Exception {
        // Initialize the database
        plateauRepository.saveAndFlush(plateau);

        // Get all the plateauList where heureDebut contains DEFAULT_HEURE_DEBUT
        defaultPlateauShouldBeFound("heureDebut.contains=" + DEFAULT_HEURE_DEBUT);

        // Get all the plateauList where heureDebut contains UPDATED_HEURE_DEBUT
        defaultPlateauShouldNotBeFound("heureDebut.contains=" + UPDATED_HEURE_DEBUT);
    }

    @Test
    @Transactional
    public void getAllPlateausByHeureDebutNotContainsSomething() throws Exception {
        // Initialize the database
        plateauRepository.saveAndFlush(plateau);

        // Get all the plateauList where heureDebut does not contain DEFAULT_HEURE_DEBUT
        defaultPlateauShouldNotBeFound("heureDebut.doesNotContain=" + DEFAULT_HEURE_DEBUT);

        // Get all the plateauList where heureDebut does not contain UPDATED_HEURE_DEBUT
        defaultPlateauShouldBeFound("heureDebut.doesNotContain=" + UPDATED_HEURE_DEBUT);
    }


    @Test
    @Transactional
    public void getAllPlateausByHeureFinIsEqualToSomething() throws Exception {
        // Initialize the database
        plateauRepository.saveAndFlush(plateau);

        // Get all the plateauList where heureFin equals to DEFAULT_HEURE_FIN
        defaultPlateauShouldBeFound("heureFin.equals=" + DEFAULT_HEURE_FIN);

        // Get all the plateauList where heureFin equals to UPDATED_HEURE_FIN
        defaultPlateauShouldNotBeFound("heureFin.equals=" + UPDATED_HEURE_FIN);
    }

    @Test
    @Transactional
    public void getAllPlateausByHeureFinIsNotEqualToSomething() throws Exception {
        // Initialize the database
        plateauRepository.saveAndFlush(plateau);

        // Get all the plateauList where heureFin not equals to DEFAULT_HEURE_FIN
        defaultPlateauShouldNotBeFound("heureFin.notEquals=" + DEFAULT_HEURE_FIN);

        // Get all the plateauList where heureFin not equals to UPDATED_HEURE_FIN
        defaultPlateauShouldBeFound("heureFin.notEquals=" + UPDATED_HEURE_FIN);
    }

    @Test
    @Transactional
    public void getAllPlateausByHeureFinIsInShouldWork() throws Exception {
        // Initialize the database
        plateauRepository.saveAndFlush(plateau);

        // Get all the plateauList where heureFin in DEFAULT_HEURE_FIN or UPDATED_HEURE_FIN
        defaultPlateauShouldBeFound("heureFin.in=" + DEFAULT_HEURE_FIN + "," + UPDATED_HEURE_FIN);

        // Get all the plateauList where heureFin equals to UPDATED_HEURE_FIN
        defaultPlateauShouldNotBeFound("heureFin.in=" + UPDATED_HEURE_FIN);
    }

    @Test
    @Transactional
    public void getAllPlateausByHeureFinIsNullOrNotNull() throws Exception {
        // Initialize the database
        plateauRepository.saveAndFlush(plateau);

        // Get all the plateauList where heureFin is not null
        defaultPlateauShouldBeFound("heureFin.specified=true");

        // Get all the plateauList where heureFin is null
        defaultPlateauShouldNotBeFound("heureFin.specified=false");
    }
                @Test
    @Transactional
    public void getAllPlateausByHeureFinContainsSomething() throws Exception {
        // Initialize the database
        plateauRepository.saveAndFlush(plateau);

        // Get all the plateauList where heureFin contains DEFAULT_HEURE_FIN
        defaultPlateauShouldBeFound("heureFin.contains=" + DEFAULT_HEURE_FIN);

        // Get all the plateauList where heureFin contains UPDATED_HEURE_FIN
        defaultPlateauShouldNotBeFound("heureFin.contains=" + UPDATED_HEURE_FIN);
    }

    @Test
    @Transactional
    public void getAllPlateausByHeureFinNotContainsSomething() throws Exception {
        // Initialize the database
        plateauRepository.saveAndFlush(plateau);

        // Get all the plateauList where heureFin does not contain DEFAULT_HEURE_FIN
        defaultPlateauShouldNotBeFound("heureFin.doesNotContain=" + DEFAULT_HEURE_FIN);

        // Get all the plateauList where heureFin does not contain UPDATED_HEURE_FIN
        defaultPlateauShouldBeFound("heureFin.doesNotContain=" + UPDATED_HEURE_FIN);
    }


    @Test
    @Transactional
    public void getAllPlateausByAdresseIsEqualToSomething() throws Exception {
        // Initialize the database
        plateauRepository.saveAndFlush(plateau);

        // Get all the plateauList where adresse equals to DEFAULT_ADRESSE
        defaultPlateauShouldBeFound("adresse.equals=" + DEFAULT_ADRESSE);

        // Get all the plateauList where adresse equals to UPDATED_ADRESSE
        defaultPlateauShouldNotBeFound("adresse.equals=" + UPDATED_ADRESSE);
    }

    @Test
    @Transactional
    public void getAllPlateausByAdresseIsNotEqualToSomething() throws Exception {
        // Initialize the database
        plateauRepository.saveAndFlush(plateau);

        // Get all the plateauList where adresse not equals to DEFAULT_ADRESSE
        defaultPlateauShouldNotBeFound("adresse.notEquals=" + DEFAULT_ADRESSE);

        // Get all the plateauList where adresse not equals to UPDATED_ADRESSE
        defaultPlateauShouldBeFound("adresse.notEquals=" + UPDATED_ADRESSE);
    }

    @Test
    @Transactional
    public void getAllPlateausByAdresseIsInShouldWork() throws Exception {
        // Initialize the database
        plateauRepository.saveAndFlush(plateau);

        // Get all the plateauList where adresse in DEFAULT_ADRESSE or UPDATED_ADRESSE
        defaultPlateauShouldBeFound("adresse.in=" + DEFAULT_ADRESSE + "," + UPDATED_ADRESSE);

        // Get all the plateauList where adresse equals to UPDATED_ADRESSE
        defaultPlateauShouldNotBeFound("adresse.in=" + UPDATED_ADRESSE);
    }

    @Test
    @Transactional
    public void getAllPlateausByAdresseIsNullOrNotNull() throws Exception {
        // Initialize the database
        plateauRepository.saveAndFlush(plateau);

        // Get all the plateauList where adresse is not null
        defaultPlateauShouldBeFound("adresse.specified=true");

        // Get all the plateauList where adresse is null
        defaultPlateauShouldNotBeFound("adresse.specified=false");
    }
                @Test
    @Transactional
    public void getAllPlateausByAdresseContainsSomething() throws Exception {
        // Initialize the database
        plateauRepository.saveAndFlush(plateau);

        // Get all the plateauList where adresse contains DEFAULT_ADRESSE
        defaultPlateauShouldBeFound("adresse.contains=" + DEFAULT_ADRESSE);

        // Get all the plateauList where adresse contains UPDATED_ADRESSE
        defaultPlateauShouldNotBeFound("adresse.contains=" + UPDATED_ADRESSE);
    }

    @Test
    @Transactional
    public void getAllPlateausByAdresseNotContainsSomething() throws Exception {
        // Initialize the database
        plateauRepository.saveAndFlush(plateau);

        // Get all the plateauList where adresse does not contain DEFAULT_ADRESSE
        defaultPlateauShouldNotBeFound("adresse.doesNotContain=" + DEFAULT_ADRESSE);

        // Get all the plateauList where adresse does not contain UPDATED_ADRESSE
        defaultPlateauShouldBeFound("adresse.doesNotContain=" + UPDATED_ADRESSE);
    }


    @Test
    @Transactional
    public void getAllPlateausByNbrEquipeIsEqualToSomething() throws Exception {
        // Initialize the database
        plateauRepository.saveAndFlush(plateau);

        // Get all the plateauList where nbrEquipe equals to DEFAULT_NBR_EQUIPE
        defaultPlateauShouldBeFound("nbrEquipe.equals=" + DEFAULT_NBR_EQUIPE);

        // Get all the plateauList where nbrEquipe equals to UPDATED_NBR_EQUIPE
        defaultPlateauShouldNotBeFound("nbrEquipe.equals=" + UPDATED_NBR_EQUIPE);
    }

    @Test
    @Transactional
    public void getAllPlateausByNbrEquipeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        plateauRepository.saveAndFlush(plateau);

        // Get all the plateauList where nbrEquipe not equals to DEFAULT_NBR_EQUIPE
        defaultPlateauShouldNotBeFound("nbrEquipe.notEquals=" + DEFAULT_NBR_EQUIPE);

        // Get all the plateauList where nbrEquipe not equals to UPDATED_NBR_EQUIPE
        defaultPlateauShouldBeFound("nbrEquipe.notEquals=" + UPDATED_NBR_EQUIPE);
    }

    @Test
    @Transactional
    public void getAllPlateausByNbrEquipeIsInShouldWork() throws Exception {
        // Initialize the database
        plateauRepository.saveAndFlush(plateau);

        // Get all the plateauList where nbrEquipe in DEFAULT_NBR_EQUIPE or UPDATED_NBR_EQUIPE
        defaultPlateauShouldBeFound("nbrEquipe.in=" + DEFAULT_NBR_EQUIPE + "," + UPDATED_NBR_EQUIPE);

        // Get all the plateauList where nbrEquipe equals to UPDATED_NBR_EQUIPE
        defaultPlateauShouldNotBeFound("nbrEquipe.in=" + UPDATED_NBR_EQUIPE);
    }

    @Test
    @Transactional
    public void getAllPlateausByNbrEquipeIsNullOrNotNull() throws Exception {
        // Initialize the database
        plateauRepository.saveAndFlush(plateau);

        // Get all the plateauList where nbrEquipe is not null
        defaultPlateauShouldBeFound("nbrEquipe.specified=true");

        // Get all the plateauList where nbrEquipe is null
        defaultPlateauShouldNotBeFound("nbrEquipe.specified=false");
    }

    @Test
    @Transactional
    public void getAllPlateausByNbrEquipeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        plateauRepository.saveAndFlush(plateau);

        // Get all the plateauList where nbrEquipe is greater than or equal to DEFAULT_NBR_EQUIPE
        defaultPlateauShouldBeFound("nbrEquipe.greaterThanOrEqual=" + DEFAULT_NBR_EQUIPE);

        // Get all the plateauList where nbrEquipe is greater than or equal to UPDATED_NBR_EQUIPE
        defaultPlateauShouldNotBeFound("nbrEquipe.greaterThanOrEqual=" + UPDATED_NBR_EQUIPE);
    }

    @Test
    @Transactional
    public void getAllPlateausByNbrEquipeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        plateauRepository.saveAndFlush(plateau);

        // Get all the plateauList where nbrEquipe is less than or equal to DEFAULT_NBR_EQUIPE
        defaultPlateauShouldBeFound("nbrEquipe.lessThanOrEqual=" + DEFAULT_NBR_EQUIPE);

        // Get all the plateauList where nbrEquipe is less than or equal to SMALLER_NBR_EQUIPE
        defaultPlateauShouldNotBeFound("nbrEquipe.lessThanOrEqual=" + SMALLER_NBR_EQUIPE);
    }

    @Test
    @Transactional
    public void getAllPlateausByNbrEquipeIsLessThanSomething() throws Exception {
        // Initialize the database
        plateauRepository.saveAndFlush(plateau);

        // Get all the plateauList where nbrEquipe is less than DEFAULT_NBR_EQUIPE
        defaultPlateauShouldNotBeFound("nbrEquipe.lessThan=" + DEFAULT_NBR_EQUIPE);

        // Get all the plateauList where nbrEquipe is less than UPDATED_NBR_EQUIPE
        defaultPlateauShouldBeFound("nbrEquipe.lessThan=" + UPDATED_NBR_EQUIPE);
    }

    @Test
    @Transactional
    public void getAllPlateausByNbrEquipeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        plateauRepository.saveAndFlush(plateau);

        // Get all the plateauList where nbrEquipe is greater than DEFAULT_NBR_EQUIPE
        defaultPlateauShouldNotBeFound("nbrEquipe.greaterThan=" + DEFAULT_NBR_EQUIPE);

        // Get all the plateauList where nbrEquipe is greater than SMALLER_NBR_EQUIPE
        defaultPlateauShouldBeFound("nbrEquipe.greaterThan=" + SMALLER_NBR_EQUIPE);
    }


    @Test
    @Transactional
    public void getAllPlateausByStatutIsEqualToSomething() throws Exception {
        // Initialize the database
        plateauRepository.saveAndFlush(plateau);

        // Get all the plateauList where statut equals to DEFAULT_STATUT
        defaultPlateauShouldBeFound("statut.equals=" + DEFAULT_STATUT);

        // Get all the plateauList where statut equals to UPDATED_STATUT
        defaultPlateauShouldNotBeFound("statut.equals=" + UPDATED_STATUT);
    }

    @Test
    @Transactional
    public void getAllPlateausByStatutIsNotEqualToSomething() throws Exception {
        // Initialize the database
        plateauRepository.saveAndFlush(plateau);

        // Get all the plateauList where statut not equals to DEFAULT_STATUT
        defaultPlateauShouldNotBeFound("statut.notEquals=" + DEFAULT_STATUT);

        // Get all the plateauList where statut not equals to UPDATED_STATUT
        defaultPlateauShouldBeFound("statut.notEquals=" + UPDATED_STATUT);
    }

    @Test
    @Transactional
    public void getAllPlateausByStatutIsInShouldWork() throws Exception {
        // Initialize the database
        plateauRepository.saveAndFlush(plateau);

        // Get all the plateauList where statut in DEFAULT_STATUT or UPDATED_STATUT
        defaultPlateauShouldBeFound("statut.in=" + DEFAULT_STATUT + "," + UPDATED_STATUT);

        // Get all the plateauList where statut equals to UPDATED_STATUT
        defaultPlateauShouldNotBeFound("statut.in=" + UPDATED_STATUT);
    }

    @Test
    @Transactional
    public void getAllPlateausByStatutIsNullOrNotNull() throws Exception {
        // Initialize the database
        plateauRepository.saveAndFlush(plateau);

        // Get all the plateauList where statut is not null
        defaultPlateauShouldBeFound("statut.specified=true");

        // Get all the plateauList where statut is null
        defaultPlateauShouldNotBeFound("statut.specified=false");
    }

    @Test
    @Transactional
    public void getAllPlateausByValidIsEqualToSomething() throws Exception {
        // Initialize the database
        plateauRepository.saveAndFlush(plateau);

        // Get all the plateauList where valid equals to DEFAULT_VALID
        defaultPlateauShouldBeFound("valid.equals=" + DEFAULT_VALID);

        // Get all the plateauList where valid equals to UPDATED_VALID
        defaultPlateauShouldNotBeFound("valid.equals=" + UPDATED_VALID);
    }

    @Test
    @Transactional
    public void getAllPlateausByValidIsNotEqualToSomething() throws Exception {
        // Initialize the database
        plateauRepository.saveAndFlush(plateau);

        // Get all the plateauList where valid not equals to DEFAULT_VALID
        defaultPlateauShouldNotBeFound("valid.notEquals=" + DEFAULT_VALID);

        // Get all the plateauList where valid not equals to UPDATED_VALID
        defaultPlateauShouldBeFound("valid.notEquals=" + UPDATED_VALID);
    }

    @Test
    @Transactional
    public void getAllPlateausByValidIsInShouldWork() throws Exception {
        // Initialize the database
        plateauRepository.saveAndFlush(plateau);

        // Get all the plateauList where valid in DEFAULT_VALID or UPDATED_VALID
        defaultPlateauShouldBeFound("valid.in=" + DEFAULT_VALID + "," + UPDATED_VALID);

        // Get all the plateauList where valid equals to UPDATED_VALID
        defaultPlateauShouldNotBeFound("valid.in=" + UPDATED_VALID);
    }

    @Test
    @Transactional
    public void getAllPlateausByValidIsNullOrNotNull() throws Exception {
        // Initialize the database
        plateauRepository.saveAndFlush(plateau);

        // Get all the plateauList where valid is not null
        defaultPlateauShouldBeFound("valid.specified=true");

        // Get all the plateauList where valid is null
        defaultPlateauShouldNotBeFound("valid.specified=false");
    }

    @Test
    @Transactional
    public void getAllPlateausByReferentIsEqualToSomething() throws Exception {
        // Initialize the database
        plateauRepository.saveAndFlush(plateau);
        Referent referent = ReferentResourceIT.createEntity(em);
        em.persist(referent);
        em.flush();
        plateau.setReferent(referent);
        plateauRepository.saveAndFlush(plateau);
        Long referentId = referent.getId();

        // Get all the plateauList where referent equals to referentId
        defaultPlateauShouldBeFound("referentId.equals=" + referentId);

        // Get all the plateauList where referent equals to referentId + 1
        defaultPlateauShouldNotBeFound("referentId.equals=" + (referentId + 1));
    }


    
    @Test
    @Transactional
    public void getAllPlateausByUserIsEqualToSomething() throws Exception {
        // Initialize the database
        plateauRepository.saveAndFlush(plateau);
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        plateau.addUser(user);
        plateauRepository.saveAndFlush(plateau);
        Long userId = user.getId();

        // Get all the plateauList where user equals to userId
        defaultPlateauShouldBeFound("userId.equals=" + userId);

        // Get all the plateauList where user equals to userId + 1
        defaultPlateauShouldNotBeFound("userId.equals=" + (userId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPlateauShouldBeFound(String filter) throws Exception {
        restPlateauMockMvc.perform(get("/api/plateaus?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(plateau.getId().intValue())))
            .andExpect(jsonPath("$.[*].dateDebut").value(hasItem(DEFAULT_DATE_DEBUT.toString())))
            .andExpect(jsonPath("$.[*].dateFin").value(hasItem(DEFAULT_DATE_FIN.toString())))
            .andExpect(jsonPath("$.[*].heureDebut").value(hasItem(DEFAULT_HEURE_DEBUT)))
            .andExpect(jsonPath("$.[*].heureFin").value(hasItem(DEFAULT_HEURE_FIN)))
            .andExpect(jsonPath("$.[*].programmeContentType").value(hasItem(DEFAULT_PROGRAMME_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].programme").value(hasItem(Base64Utils.encodeToString(DEFAULT_PROGRAMME))))
            .andExpect(jsonPath("$.[*].adresse").value(hasItem(DEFAULT_ADRESSE)))
            .andExpect(jsonPath("$.[*].nbrEquipe").value(hasItem(DEFAULT_NBR_EQUIPE)))
            .andExpect(jsonPath("$.[*].statut").value(hasItem(DEFAULT_STATUT.toString())))
            .andExpect(jsonPath("$.[*].valid").value(hasItem(DEFAULT_VALID.booleanValue())));

        // Check, that the count call also returns 1
        restPlateauMockMvc.perform(get("/api/plateaus/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPlateauShouldNotBeFound(String filter) throws Exception {
        restPlateauMockMvc.perform(get("/api/plateaus?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPlateauMockMvc.perform(get("/api/plateaus/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingPlateau() throws Exception {
        // Get the plateau
        restPlateauMockMvc.perform(get("/api/plateaus/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePlateau() throws Exception {
        // Initialize the database
        plateauService.save(plateau);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockPlateauSearchRepository);

        int databaseSizeBeforeUpdate = plateauRepository.findAll().size();

        // Update the plateau
        Plateau updatedPlateau = plateauRepository.findById(plateau.getId()).get();
        // Disconnect from session so that the updates on updatedPlateau are not directly saved in db
        em.detach(updatedPlateau);
        updatedPlateau
            .dateDebut(UPDATED_DATE_DEBUT)
            .dateFin(UPDATED_DATE_FIN)
            .heureDebut(UPDATED_HEURE_DEBUT)
            .heureFin(UPDATED_HEURE_FIN)
            .programme(UPDATED_PROGRAMME)
            .programmeContentType(UPDATED_PROGRAMME_CONTENT_TYPE)
            .adresse(UPDATED_ADRESSE)
            .nbrEquipe(UPDATED_NBR_EQUIPE)
            .statut(UPDATED_STATUT)
            .valid(UPDATED_VALID);

        restPlateauMockMvc.perform(put("/api/plateaus")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedPlateau)))
            .andExpect(status().isOk());

        // Validate the Plateau in the database
        List<Plateau> plateauList = plateauRepository.findAll();
        assertThat(plateauList).hasSize(databaseSizeBeforeUpdate);
        Plateau testPlateau = plateauList.get(plateauList.size() - 1);
        assertThat(testPlateau.getDateDebut()).isEqualTo(UPDATED_DATE_DEBUT);
        assertThat(testPlateau.getDateFin()).isEqualTo(UPDATED_DATE_FIN);
        assertThat(testPlateau.getHeureDebut()).isEqualTo(UPDATED_HEURE_DEBUT);
        assertThat(testPlateau.getHeureFin()).isEqualTo(UPDATED_HEURE_FIN);
        assertThat(testPlateau.getProgramme()).isEqualTo(UPDATED_PROGRAMME);
        assertThat(testPlateau.getProgrammeContentType()).isEqualTo(UPDATED_PROGRAMME_CONTENT_TYPE);
        assertThat(testPlateau.getAdresse()).isEqualTo(UPDATED_ADRESSE);
        assertThat(testPlateau.getNbrEquipe()).isEqualTo(UPDATED_NBR_EQUIPE);
        assertThat(testPlateau.getStatut()).isEqualTo(UPDATED_STATUT);
        assertThat(testPlateau.isValid()).isEqualTo(UPDATED_VALID);

        // Validate the Plateau in Elasticsearch
        verify(mockPlateauSearchRepository, times(1)).save(testPlateau);
    }

    @Test
    @Transactional
    public void updateNonExistingPlateau() throws Exception {
        int databaseSizeBeforeUpdate = plateauRepository.findAll().size();

        // Create the Plateau

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPlateauMockMvc.perform(put("/api/plateaus")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(plateau)))
            .andExpect(status().isBadRequest());

        // Validate the Plateau in the database
        List<Plateau> plateauList = plateauRepository.findAll();
        assertThat(plateauList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Plateau in Elasticsearch
        verify(mockPlateauSearchRepository, times(0)).save(plateau);
    }

    @Test
    @Transactional
    public void deletePlateau() throws Exception {
        // Initialize the database
        plateauService.save(plateau);

        int databaseSizeBeforeDelete = plateauRepository.findAll().size();

        // Delete the plateau
        restPlateauMockMvc.perform(delete("/api/plateaus/{id}", plateau.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Plateau> plateauList = plateauRepository.findAll();
        assertThat(plateauList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Plateau in Elasticsearch
        verify(mockPlateauSearchRepository, times(1)).deleteById(plateau.getId());
    }

    @Test
    @Transactional
    public void searchPlateau() throws Exception {
        // Initialize the database
        plateauService.save(plateau);
        when(mockPlateauSearchRepository.search(queryStringQuery("id:" + plateau.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(plateau), PageRequest.of(0, 1), 1));
        // Search the plateau
        restPlateauMockMvc.perform(get("/api/_search/plateaus?query=id:" + plateau.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(plateau.getId().intValue())))
            .andExpect(jsonPath("$.[*].dateDebut").value(hasItem(DEFAULT_DATE_DEBUT.toString())))
            .andExpect(jsonPath("$.[*].dateFin").value(hasItem(DEFAULT_DATE_FIN.toString())))
            .andExpect(jsonPath("$.[*].heureDebut").value(hasItem(DEFAULT_HEURE_DEBUT)))
            .andExpect(jsonPath("$.[*].heureFin").value(hasItem(DEFAULT_HEURE_FIN)))
            .andExpect(jsonPath("$.[*].programmeContentType").value(hasItem(DEFAULT_PROGRAMME_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].programme").value(hasItem(Base64Utils.encodeToString(DEFAULT_PROGRAMME))))
            .andExpect(jsonPath("$.[*].adresse").value(hasItem(DEFAULT_ADRESSE)))
            .andExpect(jsonPath("$.[*].nbrEquipe").value(hasItem(DEFAULT_NBR_EQUIPE)))
            .andExpect(jsonPath("$.[*].statut").value(hasItem(DEFAULT_STATUT.toString())))
            .andExpect(jsonPath("$.[*].valid").value(hasItem(DEFAULT_VALID.booleanValue())));
    }
}
