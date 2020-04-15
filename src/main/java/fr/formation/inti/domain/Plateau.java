package fr.formation.inti.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

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

    @Column(name = "date_debut")
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

    @Column(name = "adresse")
    private String adresse;

    @Column(name = "nbr_equipe")
    private Integer nbrEquipe;

    @Enumerated(EnumType.STRING)
    @Column(name = "statut")
    private Statut statut;

    @Column(name = "valid")
    private Boolean valid;

    @ManyToOne
    @JsonIgnoreProperties("plateaus")
    private Referent referent;

    @ManyToOne
    @JsonIgnoreProperties("plateaus")
    private User user;

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

    public String getAdresse() {
        return adresse;
    }

    public Plateau adresse(String adresse) {
        this.adresse = adresse;
        return this;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public Integer getNbrEquipe() {
        return nbrEquipe;
    }

    public Plateau nbrEquipe(Integer nbrEquipe) {
        this.nbrEquipe = nbrEquipe;
        return this;
    }

    public void setNbrEquipe(Integer nbrEquipe) {
        this.nbrEquipe = nbrEquipe;
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
            ", adresse='" + getAdresse() + "'" +
            ", nbrEquipe=" + getNbrEquipe() +
            ", statut='" + getStatut() + "'" +
            ", valid='" + isValid() + "'" +
            "}";
    }
}
