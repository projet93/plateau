import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption, SearchWithPagination } from 'app/shared/util/request-util';
import { IClub } from 'app/shared/model/club.model';

type EntityResponseType = HttpResponse<IClub>;
type EntityArrayResponseType = HttpResponse<IClub[]>;

@Injectable({ providedIn: 'root' })
export class ClubService {
  public resourceUrl = SERVER_API_URL + 'api/clubs';
  public resourceSearchUrl = SERVER_API_URL + 'api/_search/clubs';

  public resourceUrlUser = SERVER_API_URL + 'api/clubs/user';
  
  constructor(protected http: HttpClient) {}

  create(club: IClub): Observable<EntityResponseType> {
    return this.http.post<IClub>(this.resourceUrl, club, { observe: 'response' });
  }

  update(club: IClub): Observable<EntityResponseType> {
    return this.http.put<IClub>(this.resourceUrl, club, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IClub>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  findByUser(): Observable<EntityResponseType> {
    return this.http.get<IClub>(`${this.resourceUrlUser}`, { observe: 'response' });
  }
  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IClub[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IClub[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }
}
