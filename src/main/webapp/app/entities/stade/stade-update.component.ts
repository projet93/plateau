import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IStade, Stade } from 'app/shared/model/stade.model';
import { StadeService } from './stade.service';
import { IUser } from 'app/core/user/user.model';
import { UserService } from 'app/core/user/user.service';
import { IClub } from 'app/shared/model/club.model';
import { ClubService } from 'app/entities/club/club.service';

type SelectableEntity = IUser | IClub;

@Component({
  selector: 'jhi-stade-update',
  templateUrl: './stade-update.component.html'
})
export class StadeUpdateComponent implements OnInit {
  isSaving = false;
  users: IUser[] = [];
  clubs: IClub[] = [];

  editForm = this.fb.group({
    id: [],
    nom: [null, [Validators.required]],
    adresse: [],
    codePostal: [],
    ville: [],
    user: [],
    club: []
  });

  constructor(
    protected stadeService: StadeService,
    protected userService: UserService,
    protected clubService: ClubService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ stade }) => {
      this.updateForm(stade);

      this.userService.query().subscribe((res: HttpResponse<IUser[]>) => (this.users = res.body || []));

      this.clubService.query().subscribe((res: HttpResponse<IClub[]>) => (this.clubs = res.body || []));
    });
  }

  updateForm(stade: IStade): void {
    this.editForm.patchValue({
      id: stade.id,
      nom: stade.nom,
      adresse: stade.adresse,
      codePostal: stade.codePostal,
      ville: stade.ville,
      user: stade.user,
      club: stade.club
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const stade = this.createFromForm();
    if (stade.id !== undefined) {
      this.subscribeToSaveResponse(this.stadeService.update(stade));
    } else {
      this.subscribeToSaveResponse(this.stadeService.create(stade));
    }
  }

  private createFromForm(): IStade {
    return {
      ...new Stade(),
      id: this.editForm.get(['id'])!.value,
      nom: this.editForm.get(['nom'])!.value,
      adresse: this.editForm.get(['adresse'])!.value,
      codePostal: this.editForm.get(['codePostal'])!.value,
      ville: this.editForm.get(['ville'])!.value,
      user: this.editForm.get(['user'])!.value,
      club: this.editForm.get(['club'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IStade>>): void {
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
