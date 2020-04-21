import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IInscription } from 'app/shared/model/inscription.model';
import { InscriptionService } from './inscription.service';
import { InscriptionDeleteDialogComponent } from './inscription-delete-dialog.component';

@Component({
  selector: 'jhi-inscription',
  templateUrl: './inscription.component.html'
})
export class InscriptionComponent implements OnInit, OnDestroy {
  inscriptions?: IInscription[];
  eventSubscriber?: Subscription;
  currentSearch: string;

  constructor(
    protected inscriptionService: InscriptionService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal,
    protected activatedRoute: ActivatedRoute
  ) {
    this.currentSearch =
      this.activatedRoute.snapshot && this.activatedRoute.snapshot.queryParams['search']
        ? this.activatedRoute.snapshot.queryParams['search']
        : '';
  }

  loadAll(): void {
    if (this.currentSearch) {
      this.inscriptionService
        .search({
          query: this.currentSearch
        })
        .subscribe((res: HttpResponse<IInscription[]>) => (this.inscriptions = res.body || []));
      return;
    }

    this.inscriptionService.query().subscribe((res: HttpResponse<IInscription[]>) => (this.inscriptions = res.body || []));
  }

  search(query: string): void {
    this.currentSearch = query;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInInscriptions();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IInscription): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInInscriptions(): void {
    this.eventSubscriber = this.eventManager.subscribe('inscriptionListModification', () => this.loadAll());
  }

  delete(inscription: IInscription): void {
    const modalRef = this.modalService.open(InscriptionDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.inscription = inscription;
  }
}
