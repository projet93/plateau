import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { JhiDataUtils } from 'ng-jhipster';

import { IPlateau } from 'app/shared/model/plateau.model';

@Component({
  selector: 'jhi-plateau-detail',
  templateUrl: './plateau-detail.component.html'
})
export class PlateauDetailComponent implements OnInit {
  plateau: IPlateau | null = null;

  constructor(protected dataUtils: JhiDataUtils, protected activatedRoute: ActivatedRoute) {
  }

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ plateau }) => (this.plateau = plateau));
    localStorage.setItem('id', JSON.stringify(this.plateau?.id));
    localStorage.setItem('user', JSON.stringify(this.plateau?.user?.id));

  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(contentType: string, base64String: string): void {
    this.dataUtils.openFile(contentType, base64String);
  }

  previousState(): void {
    window.history.back();
  }
}
