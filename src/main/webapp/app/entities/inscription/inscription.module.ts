import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { PlateauFffSharedModule } from 'app/shared/shared.module';
import { InscriptionDetailComponent } from './inscription-detail.component';
import { InscriptionUpdateComponent } from './inscription-update.component';
import { InscriptionDeleteDialogComponent } from './inscription-delete-dialog.component';
import { inscriptionRoute } from './inscription.route';

@NgModule({
  imports: [PlateauFffSharedModule, RouterModule.forChild(inscriptionRoute)],
  declarations: [ InscriptionDetailComponent, InscriptionUpdateComponent, InscriptionDeleteDialogComponent],
  entryComponents: [InscriptionDeleteDialogComponent]
})
export class PlateauFffInscriptionModule {}
