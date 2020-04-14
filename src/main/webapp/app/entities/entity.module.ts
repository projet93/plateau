import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'club',
        loadChildren: () => import('./club/club.module').then(m => m.PlateauFffClubModule)
      },
      {
        path: 'stade',
        loadChildren: () => import('./stade/stade.module').then(m => m.PlateauFffStadeModule)
      },
      {
        path: 'categorie',
        loadChildren: () => import('./categorie/categorie.module').then(m => m.PlateauFffCategorieModule)
      },
      {
        path: 'referent',
        loadChildren: () => import('./referent/referent.module').then(m => m.PlateauFffReferentModule)
      },
      {
        path: 'plateau',
        loadChildren: () => import('./plateau/plateau.module').then(m => m.PlateauFffPlateauModule)
      }
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ])
  ]
})
export class PlateauFffEntityModule {}
