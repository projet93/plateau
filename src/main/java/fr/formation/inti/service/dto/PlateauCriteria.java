package fr.formation.inti.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import fr.formation.inti.domain.enumeration.Statut;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import io.github.jhipster.service.filter.LocalDateFilter;

/**
 * Criteria class for the {@link fr.formation.inti.domain.Plateau} entity. This class is used
 * in {@link fr.formation.inti.web.rest.PlateauResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /plateaus?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class PlateauCriteria implements Serializable, Criteria {
    /**
     * Class for filtering Statut
     */
    public static class StatutFilter extends Filter<Statut> {

        public StatutFilter() {
        }

        public StatutFilter(StatutFilter filter) {
            super(filter);
        }

        @Override
        public StatutFilter copy() {
            return new StatutFilter(this);
        }

    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LocalDateFilter dateDebut;

    private LocalDateFilter dateFin;

    private StringFilter heureDebut;

    private StringFilter heureFin;

    private StringFilter adresse;

    private IntegerFilter nbrEquipe;

    private StatutFilter statut;

    private BooleanFilter valid;

    private LongFilter referentId;

    private LongFilter userId;

    public PlateauCriteria() {
    }

    public PlateauCriteria(PlateauCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.dateDebut = other.dateDebut == null ? null : other.dateDebut.copy();
        this.dateFin = other.dateFin == null ? null : other.dateFin.copy();
        this.heureDebut = other.heureDebut == null ? null : other.heureDebut.copy();
        this.heureFin = other.heureFin == null ? null : other.heureFin.copy();
        this.adresse = other.adresse == null ? null : other.adresse.copy();
        this.nbrEquipe = other.nbrEquipe == null ? null : other.nbrEquipe.copy();
        this.statut = other.statut == null ? null : other.statut.copy();
        this.valid = other.valid == null ? null : other.valid.copy();
        this.referentId = other.referentId == null ? null : other.referentId.copy();
        this.userId = other.userId == null ? null : other.userId.copy();
        this.userId = other.userId == null ? null : other.userId.copy();
    }

    @Override
    public PlateauCriteria copy() {
        return new PlateauCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public LocalDateFilter getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(LocalDateFilter dateDebut) {
        this.dateDebut = dateDebut;
    }

    public LocalDateFilter getDateFin() {
        return dateFin;
    }

    public void setDateFin(LocalDateFilter dateFin) {
        this.dateFin = dateFin;
    }

    public StringFilter getHeureDebut() {
        return heureDebut;
    }

    public void setHeureDebut(StringFilter heureDebut) {
        this.heureDebut = heureDebut;
    }

    public StringFilter getHeureFin() {
        return heureFin;
    }

    public void setHeureFin(StringFilter heureFin) {
        this.heureFin = heureFin;
    }

    public StringFilter getAdresse() {
        return adresse;
    }

    public void setAdresse(StringFilter adresse) {
        this.adresse = adresse;
    }

    public IntegerFilter getNbrEquipe() {
        return nbrEquipe;
    }

    public void setNbrEquipe(IntegerFilter nbrEquipe) {
        this.nbrEquipe = nbrEquipe;
    }

    public StatutFilter getStatut() {
        return statut;
    }

    public void setStatut(StatutFilter statut) {
        this.statut = statut;
    }

    public BooleanFilter getValid() {
        return valid;
    }

    public void setValid(BooleanFilter valid) {
        this.valid = valid;
    }

    public LongFilter getReferentId() {
        return referentId;
    }

    public void setReferentId(LongFilter referentId) {
        this.referentId = referentId;
    }

    public LongFilter getUserId() {
        return userId;
    }

    public void setUserId(LongFilter userId) {
        this.userId = userId;
    }

    
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final PlateauCriteria that = (PlateauCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(dateDebut, that.dateDebut) &&
            Objects.equals(dateFin, that.dateFin) &&
            Objects.equals(heureDebut, that.heureDebut) &&
            Objects.equals(heureFin, that.heureFin) &&
            Objects.equals(adresse, that.adresse) &&
            Objects.equals(nbrEquipe, that.nbrEquipe) &&
            Objects.equals(statut, that.statut) &&
            Objects.equals(valid, that.valid) &&
            Objects.equals(referentId, that.referentId) &&
            Objects.equals(userId, that.userId) &&
            Objects.equals(userId, that.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        dateDebut,
        dateFin,
        heureDebut,
        heureFin,
        adresse,
        nbrEquipe,
        statut,
        valid,
        referentId,
        userId,
        userId
        );
    }

    @Override
    public String toString() {
        return "PlateauCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (dateDebut != null ? "dateDebut=" + dateDebut + ", " : "") +
                (dateFin != null ? "dateFin=" + dateFin + ", " : "") +
                (heureDebut != null ? "heureDebut=" + heureDebut + ", " : "") +
                (heureFin != null ? "heureFin=" + heureFin + ", " : "") +
                (adresse != null ? "adresse=" + adresse + ", " : "") +
                (nbrEquipe != null ? "nbrEquipe=" + nbrEquipe + ", " : "") +
                (statut != null ? "statut=" + statut + ", " : "") +
                (valid != null ? "valid=" + valid + ", " : "") +
                (referentId != null ? "referentId=" + referentId + ", " : "") +
                (userId != null ? "userId=" + userId + ", " : "") +
                (userId != null ? "userId=" + userId + ", " : "") +
            "}";
    }

}
