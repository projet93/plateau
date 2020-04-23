import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IInscription, Inscription } from 'app/shared/model/inscription.model';
import { InscriptionService } from './inscription.service';
import { IPlateau, Plateau } from 'app/shared/model/plateau.model';
import { PlateauService } from 'app/entities/plateau/plateau.service';
import { IClub, Club } from 'app/shared/model/club.model';
import { ClubService } from 'app/entities/club/club.service';
import { IReferent } from 'app/shared/model/referent.model';
import { ReferentService } from 'app/entities/referent/referent.service';

type SelectableEntity = IPlateau | IClub | IReferent;

@Component({
  selector: 'jhi-inscription-update',
  templateUrl: './inscription-update.component.html'
})
export class InscriptionUpdateComponent implements OnInit {
  isSaving = false;
  plateaus: IPlateau[] = [];
  clubs: IClub[] = [];
  referents: IReferent[] = [];
  club?: IClub;

  editForm = this.fb.group({
    id: [],
    nombreEquipe: [null, [Validators.required,Validators.max(localStorage['maxEquipe'])]],
    plateau: [],
    club: [],
    referent: []
  });

  constructor(
    protected inscriptionService: InscriptionService,
    protected plateauService: PlateauService,
    protected clubService: ClubService,
    protected referentService: ReferentService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ inscription }) => {      
      this.updateForm(inscription);
      this.plateauService.query().subscribe((res: HttpResponse<IPlateau[]>) => (this.plateaus = res.body || []));     
      this.clubService.findByUser().subscribe((res: HttpResponse<IClub>) => (this.club = res.body || new Club()));
      this.referentService.query().subscribe((res: HttpResponse<IReferent[]>) => (this.referents = res.body || []));
    });
  }

  updateForm(inscription: IInscription): void {
    this.editForm.patchValue({
      id: inscription.id,
      nombreEquipe: inscription.nombreEquipe,
      plateau: this.getPlteau(localStorage['id']),
      club: this.club,
      referent: inscription.referent
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    window.console.log('=========================',this.club);  
    this.isSaving = true;
    const inscription = this.createFromForm();
    inscription.club = this.club;
    if (inscription.id !== undefined) {      
      this.subscribeToSaveResponse(this.inscriptionService.update(inscription));
    } else {
      
      this.subscribeToSaveResponse(this.inscriptionService.create(inscription));
    }
  }

  private createFromForm(): IInscription {
    return {
      ...new Inscription(),
      id: this.editForm.get(['id'])!.value,
      nombreEquipe: this.editForm.get(['nombreEquipe'])!.value,
      plateau: this.editForm.get(['plateau'])!.value,
      club: this.editForm.get(['club'])!.value,
      referent: this.editForm.get(['referent'])!.value
    };
  }
  private getPlteau(id : string) : IPlateau{
    const plateau = new Plateau();
    if(id){
      plateau.id =+ id;
    }
    return plateau;
  }
  protected subscribeToSaveResponse(result: Observable<HttpResponse<IInscription>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }

  trackById(index: number, item: SelectableEntity): any {
    return item.id;
  }
}
