import { Moment } from 'moment';
import { IReferent } from 'app/shared/model/referent.model';
import { IUser } from 'app/core/user/user.model';
import { IStade } from 'app/shared/model/stade.model';
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
  statut?: Statut;
  valid?: boolean;
  referent?: IReferent;
  user?: IUser;
  stade?: IStade;
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
    public statut?: Statut,
    public valid?: boolean,
    public referent?: IReferent,
    public user?: IUser,
    public stade?: IStade
  ) {
    this.valid = this.valid || false;
  }
}
