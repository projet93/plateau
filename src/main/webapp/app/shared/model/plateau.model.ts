import { Moment } from 'moment';
import { IUser } from 'app/core/user/user.model';
import { IReferent } from 'app/shared/model/referent.model';
import { Statut } from 'app/shared/model/enumerations/statut.model';

export interface IPlateau {
  id?: number;
  dateDebut?: Moment;
  dateFin?: Moment;
  heureDebut?: string;
  heureFin?: string;
  programmeContentType?: string;
  programme?: any;
  adresse?: string;
  nbrEquipe?: number;
  statut?: Statut;
  valid?: boolean;
  user?: IUser;
  referent?: IReferent;
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
    public adresse?: string,
    public nbrEquipe?: number,
    public statut?: Statut,
    public valid?: boolean,
    public user?: IUser,
    public referent?: IReferent
  ) {
    this.valid = this.valid || false;
  }
}
