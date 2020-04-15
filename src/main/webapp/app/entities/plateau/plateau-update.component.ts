import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { JhiDataUtils, JhiFileLoadError, JhiEventManager, JhiEventWithContent } from 'ng-jhipster';

import { IPlateau, Plateau } from 'app/shared/model/plateau.model';
import { PlateauService } from './plateau.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { IUser } from 'app/core/user/user.model';
import { UserService } from 'app/core/user/user.service';
import { IReferent } from 'app/shared/model/referent.model';
import { ReferentService } from 'app/entities/referent/referent.service';

type SelectableEntity = IUser | IReferent;

@Component({
  selector: 'jhi-plateau-update',
  templateUrl: './plateau-update.component.html'
})
export class PlateauUpdateComponent implements OnInit {
  isSaving = false;
  users: IUser[] = [];
  referents: IReferent[] = [];
  dateDebutDp: any;
  dateFinDp: any;

  editForm = this.fb.group({
    id: [],
    dateDebut: [],
    dateFin: [],
    heureDebut: [],
    heureFin: [],
    programme: [],
    programmeContentType: [],
    adresse: [],
    nbrEquipe: [],
    statut: [],
    valid: [],
    user: [],
    referent: []
  });

  constructor(
    protected dataUtils: JhiDataUtils,
    protected eventManager: JhiEventManager,
    protected plateauService: PlateauService,
    protected userService: UserService,
    protected referentService: ReferentService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ plateau }) => {
      this.updateForm(plateau);

      this.userService.query().subscribe((res: HttpResponse<IUser[]>) => (this.users = res.body || []));

      this.referentService.query().subscribe((res: HttpResponse<IReferent[]>) => (this.referents = res.body || []));
    });
  }

  updateForm(plateau: IPlateau): void {
    this.editForm.patchValue({
      id: plateau.id,
      dateDebut: plateau.dateDebut,
      dateFin: plateau.dateFin,
      heureDebut: plateau.heureDebut,
      heureFin: plateau.heureFin,
      programme: plateau.programme,
      programmeContentType: plateau.programmeContentType,
      adresse: plateau.adresse,
      nbrEquipe: plateau.nbrEquipe,
      statut: plateau.statut,
      valid: plateau.valid,
      user: plateau.user,
      referent: plateau.referent
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(contentType: string, base64String: string): void {
    this.dataUtils.openFile(contentType, base64String);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe(null, (err: JhiFileLoadError) => {
      this.eventManager.broadcast(
        new JhiEventWithContent<AlertError>('plateauFffApp.error', { ...err, key: 'error.file.' + err.key })
      );
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const plateau = this.createFromForm();
    if (plateau.id !== undefined) {
      this.subscribeToSaveResponse(this.plateauService.update(plateau));
    } else {
      this.subscribeToSaveResponse(this.plateauService.create(plateau));
    }
  }

  private createFromForm(): IPlateau {
    return {
      ...new Plateau(),
      id: this.editForm.get(['id'])!.value,
      dateDebut: this.editForm.get(['dateDebut'])!.value,
      dateFin: this.editForm.get(['dateFin'])!.value,
      heureDebut: this.editForm.get(['heureDebut'])!.value,
      heureFin: this.editForm.get(['heureFin'])!.value,
      programmeContentType: this.editForm.get(['programmeContentType'])!.value,
      programme: this.editForm.get(['programme'])!.value,
      adresse: this.editForm.get(['adresse'])!.value,
      nbrEquipe: this.editForm.get(['nbrEquipe'])!.value,
      statut: this.editForm.get(['statut'])!.value,
      valid: this.editForm.get(['valid'])!.value,
      user: this.editForm.get(['user'])!.value,
      referent: this.editForm.get(['referent'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPlateau>>): void {
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
