import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { Account } from 'app/core/user/account.model';
import { AccountService } from 'app/core/auth/account.service';

import { IInscription } from 'app/shared/model/inscription.model';
import { InscriptionService } from './inscription.service';
import { InscriptionDeleteDialogComponent } from './inscription-delete-dialog.component';
import { flatMap } from 'rxjs/operators';

@Component({
  selector: 'jhi-inscription',
  templateUrl: './inscription.component.html'
})
export class InscriptionComponent implements OnInit, OnDestroy {
  inscriptions?: IInscription[];
  eventSubscriber?: Subscription;
  currentSearch: string;
  currentAccount: Account | null = null;

  constructor(
    protected inscriptionService: InscriptionService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal,
    private accountService: AccountService,
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
    this.activatedRoute.data
    .pipe(
      flatMap(
        () => this.accountService.identity(),
        (data, account) => {
          this.currentAccount = account;
        }
      )
    ).subscribe();
    window.console.log('=========>',this.currentAccount);
    this.inscriptionService.queryByPlateau(localStorage['id']).subscribe((res: HttpResponse<IInscription[]>) => (this.inscriptions = res.body || []));
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
