import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IReferent, Referent } from 'app/shared/model/referent.model';
import { ReferentService } from './referent.service';
import { ReferentComponent } from './referent.component';
import { ReferentDetailComponent } from './referent-detail.component';
import { ReferentUpdateComponent } from './referent-update.component';

@Injectable({ providedIn: 'root' })
export class ReferentResolve implements Resolve<IReferent> {
  constructor(private service: ReferentService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IReferent> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((referent: HttpResponse<Referent>) => {
          if (referent.body) {
            return of(referent.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Referent());
  }
}

export const referentRoute: Routes = [
  {
    path: '',
    component: ReferentComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: [Authority.USER],
      defaultSort: 'id,asc',
      pageTitle: 'plateauFffApp.referent.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: ReferentDetailComponent,
    resolve: {
      referent: ReferentResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'plateauFffApp.referent.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: ReferentUpdateComponent,
    resolve: {
      referent: ReferentResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'plateauFffApp.referent.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: ReferentUpdateComponent,
    resolve: {
      referent: ReferentResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'plateauFffApp.referent.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];