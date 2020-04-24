package fr.formation.inti.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;
import java.util.Objects;
import java.time.LocalDate;

import fr.formation.inti.domain.enumeration.Statut;

/**
 * A Plateau.
 */
@Entity
@Table(name = "plateau")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "plateau")
public class Plateau implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "date_debut", nullable = false)
    private LocalDate dateDebut;

    @Column(name = "date_fin")
    private LocalDate dateFin;

    @Column(name = "heure_debut")
    private String heureDebut;

    @Column(name = "heure_fin")
    private String heureFin;

    @Lob
    @Column(name = "programme")
    private byte[] programme;

    @Column(name = "programme_content_type")
    private String programmeContentType;

    @Column(name = "nombre_equipe_max")
    private Integer nombreEquipeMax;

    @Column(name = "nombre_equipe")
    private Integer nombreEquipe;

    @Enumerated(EnumType.STRING)
    @Column(name = "statut")
    private Statut statut;

    @Column(name = "valid")
    private Boolean valid;

    @Version
    @Column(name = "version")
    private Long version;

    @ManyToOne
    @JsonIgnoreProperties("plateaus")
    private Referent referent;

    @ManyToOne
    @JsonIgnoreProperties("plateaus")
    private User user;

    @ManyToOne
    @JsonIgnoreProperties("plateaus")
    private Stade stade;

    @ManyToOne
    @JsonIgnoreProperties("plateaus")
    private Categorie categorie;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDateDebut() {
        return dateDebut;
    }

    public Plateau dateDebut(LocalDate dateDebut) {
        this.dateDebut = dateDebut;
        return this;
    }

    public void setDateDebut(LocalDate dateDebut) {
        this.dateDebut = dateDebut;
    }

    public LocalDate getDateFin() {
        return dateFin;
    }

    public Plateau dateFin(LocalDate dateFin) {
        this.dateFin = dateFin;
        return this;
    }

    public void setDateFin(LocalDate dateFin) {
        this.dateFin = dateFin;
    }

    public String getHeureDebut() {
        return heureDebut;
    }

    public Plateau heureDebut(String heureDebut) {
        this.heureDebut = heureDebut;
        return this;
    }

    public void setHeureDebut(String heureDebut) {
        this.heureDebut = heureDebut;
    }

    public String getHeureFin() {
        return heureFin;
    }

    public Plateau heureFin(String heureFin) {
        this.heureFin = heureFin;
        return this;
    }

    public void setHeureFin(String heureFin) {
        this.heureFin = heureFin;
    }

    public byte[] getProgramme() {
        return programme;
    }

    public Plateau programme(byte[] programme) {
        this.programme = programme;
        return this;
    }

    public void setProgramme(byte[] programme) {
        this.programme = programme;
    }

    public String getProgrammeContentType() {
        return programmeContentType;
    }

    public Plateau programmeContentType(String programmeContentType) {
        this.programmeContentType = programmeContentType;
        return this;
    }

    public void setProgrammeContentType(String programmeContentType) {
        this.programmeContentType = programmeContentType;
    }

    public Integer getNombreEquipeMax() {
        return nombreEquipeMax;
    }

    public Plateau nombreEquipeMax(Integer nombreEquipeMax) {
        this.nombreEquipeMax = nombreEquipeMax;
        return this;
    }

    public void setNombreEquipeMax(Integer nombreEquipeMax) {
        this.nombreEquipeMax = nombreEquipeMax;
    }

    public Integer getNombreEquipe() {
        return nombreEquipe;
    }

    public Plateau nombreEquipe(Integer nombreEquipe) {
        this.nombreEquipe = nombreEquipe;
        return this;
    }

    public void setNombreEquipe(Integer nombreEquipe) {
        this.nombreEquipe = nombreEquipe;
    }

    public Statut getStatut() {
        return statut;
    }

    public Plateau statut(Statut statut) {
        this.statut = statut;
        return this;
    }

    public void setStatut(Statut statut) {
        this.statut = statut;
    }

    public Boolean isValid() {
        return valid;
    }

    public Plateau valid(Boolean valid) {
        this.valid = valid;
        return this;
    }

    public void setValid(Boolean valid) {
        this.valid = valid;
    }

    public Long getVersion() {
        return version;
    }

    public Plateau version(Long version) {
        this.version = version;
        return this;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public Referent getReferent() {
        return referent;
    }

    public Plateau referent(Referent referent) {
        this.referent = referent;
        return this;
    }

    public void setReferent(Referent referent) {
        this.referent = referent;
    }

    public User getUser() {
        return user;
    }

    public Plateau user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Stade getStade() {
        return stade;
    }

    public Plateau stade(Stade stade) {
        this.stade = stade;
        return this;
    }

    public void setStade(Stade stade) {
        this.stade = stade;
    }

    public Categorie getCategorie() {
        return categorie;
    }

    public Plateau categorie(Categorie categorie) {
        this.categorie = categorie;
        return this;
    }

    public void setCategorie(Categorie categorie) {
        this.categorie = categorie;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Plateau)) {
            return false;
        }
        return id != null && id.equals(((Plateau) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Plateau{" +
            "id=" + getId() +
            ", dateDebut='" + getDateDebut() + "'" +
            ", dateFin='" + getDateFin() + "'" +
            ", heureDebut='" + getHeureDebut() + "'" +
            ", heureFin='" + getHeureFin() + "'" +
            ", programme='" + getProgramme() + "'" +
            ", programmeContentType='" + getProgrammeContentType() + "'" +
            ", nombreEquipeMax=" + getNombreEquipeMax() +
            ", nombreEquipe=" + getNombreEquipe() +
            ", statut='" + getStatut() + "'" +
            ", valid='" + isValid() + "'" +
            ", version=" + getVersion() +
            "}";
    }
}
