import { IUser } from 'app/core/user/user.model';
import { IClub } from 'app/shared/model/club.model';
import { Section } from 'app/shared/model/enumerations/section.model';

export interface ICategorie {
  id?: number;
  section?: Section;
  descrition?: string;
  user?: IUser;
  clubs?: IClub[];
}

export class Categorie implements ICategorie {
  constructor(public id?: number, public section?: Section, public descrition?: string, public user?: IUser, public clubs?: IClub[]) {}
}
