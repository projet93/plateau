import { IUser } from 'app/core/user/user.model';
import { IClub } from 'app/shared/model/club.model';

export interface IStade {
  id?: number;
  nom?: string;
  adresse?: string;
  codePostal?: string;
  ville?: string;
  user?: IUser;
  club?: IClub;
}

export class Stade implements IStade {
  constructor(
    public id?: number,
    public nom?: string,
    public adresse?: string,
    public codePostal?: string,
    public ville?: string,
    public user?: IUser,
    public club?: IClub
  ) {}
}
