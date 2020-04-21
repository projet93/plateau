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
import { IReferent } from 'app/shared/model/referent.model';
import { ReferentService } from 'app/entities/referent/referent.service';
import { IUser } from 'app/core/user/user.model';
import { UserService } from 'app/core/user/user.service';
import { IStade } from 'app/shared/model/stade.model';
import { StadeService } from 'app/entities/stade/stade.service';

type SelectableEntity = IReferent | IUser | IStade;

@Component({
  selector: 'jhi-plateau-update',
  templateUrl: './plateau-update.component.html'
})
export class PlateauUpdateComponent implements OnInit {
  isSaving = false;
  referents: IReferent[] = [];
  users: IUser[] = [];
  stades: IStade[] = [];
  dateDebutDp: any;
  dateFinDp: any;

  editForm = this.fb.group({
    id: [],
    dateDebut: [null, [Validators.required]],
    dateFin: [],
    heureDebut: [],
    heureFin: [],
    programme: [],
    programmeContentType: [],
    nombreEquipeMax: [],
    statut: [],
    valid: [],
    referent: [],
    user: [],
    stade: []
  });

  constructor(
    protected dataUtils: JhiDataUtils,
    protected eventManager: JhiEventManager,
    protected plateauService: PlateauService,
    protected referentService: ReferentService,
    protected userService: UserService,
    protected stadeService: StadeService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ plateau }) => {
      this.updateForm(plateau);

      this.referentService.query().subscribe((res: HttpResponse<IReferent[]>) => (this.referents = res.body || []));

      this.userService.query().subscribe((res: HttpResponse<IUser[]>) => (this.users = res.body || []));

      this.stadeService.query().subscribe((res: HttpResponse<IStade[]>) => (this.stades = res.body || []));
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
      nombreEquipeMax: plateau.nombreEquipeMax,
      statut: plateau.statut,
      valid: plateau.valid,
      referent: plateau.referent,
      user: plateau.user,
      stade: plateau.stade
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
      nombreEquipeMax: this.editForm.get(['nombreEquipeMax'])!.value,
      statut: this.editForm.get(['statut'])!.value,
      valid: this.editForm.get(['valid'])!.value,
      referent: this.editForm.get(['referent'])!.value,
      user: this.editForm.get(['user'])!.value,
      stade: this.editForm.get(['stade'])!.value
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
