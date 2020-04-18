import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { PlateauFffSharedModule } from 'app/shared/shared.module';
import { HOME_ROUTE } from './home.route';
import { HomeComponent } from './home.component';
import { AgmCoreModule } from '@agm/core';

@NgModule({
  imports: [PlateauFffSharedModule, RouterModule.forChild([HOME_ROUTE]),AgmCoreModule.forRoot({
    apiKey: 'AIzaSyDj-zgI5H5vSaR9NbLwk7BxCyPiCz3cCTs'
    })],
  declarations: [HomeComponent]
})
export class PlateauFffHomeModule {}
