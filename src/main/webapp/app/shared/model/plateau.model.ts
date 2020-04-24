import { Moment } from 'moment';
import { IReferent } from 'app/shared/model/referent.model';
import { IUser } from 'app/core/user/user.model';
import { IStade } from 'app/shared/model/stade.model';
import { ICategorie } from 'app/shared/model/categorie.model';
import { Statut } from 'app/shared/model/enumerations/statut.model';

export interface IPlateau {
  id?: number;
  dateDebut?: Moment;
  dateFin?: Moment;
  heureDebut?: string;
  heureFin?: string;
  programmeContentType?: string;
  programme?: any;
  nombreEquipeMax?: number;
  nombreEquipe?: number;
  statut?: Statut;
  valid?: boolean;
  version?: number;
  referent?: IReferent;
  user?: IUser;
  stade?: IStade;
  categorie?: ICategorie;
}

export class Plateau implements IPlateau {
  constructor(
    public id?: number,
    public dateDebut?: Moment,
    public dateFin?: Moment,
    public heureDebut?: string,
    public heureFin?: string,
    public programmeContentType?: string,
    public programme?: any,
    public nombreEquipeMax?: number,
    public nombreEquipe?: number,
    public statut?: Statut,
    public valid?: boolean,
    public version?: number,
    public referent?: IReferent,
    public user?: IUser,
    public stade?: IStade,
    public categorie?: ICategorie
  ) {
    this.valid = this.valid || false;
  }
}
