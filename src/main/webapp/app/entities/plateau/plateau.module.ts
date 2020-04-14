import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { PlateauFffSharedModule } from 'app/shared/shared.module';
import { PlateauComponent } from './plateau.component';
import { PlateauDetailComponent } from './plateau-detail.component';
import { PlateauUpdateComponent } from './plateau-update.component';
import { PlateauDeleteDialogComponent } from './plateau-delete-dialog.component';
import { plateauRoute } from './plateau.route';

@NgModule({
  imports: [PlateauFffSharedModule, RouterModule.forChild(plateauRoute)],
  declarations: [PlateauComponent, PlateauDetailComponent, PlateauUpdateComponent, PlateauDeleteDialogComponent],
  entryComponents: [PlateauDeleteDialogComponent]
})
export class PlateauFffPlateauModule {}
