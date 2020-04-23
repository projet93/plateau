import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiDataUtils } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { Account } from 'app/core/user/account.model';
import { AccountService } from 'app/core/auth/account.service';
import { flatMap } from 'rxjs/operators';
import { IPlateau, Plateau } from 'app/shared/model/plateau.model';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { PlateauService } from './plateau.service';
import { PlateauDeleteDialogComponent } from './plateau-delete-dialog.component';
import { Statut } from 'app/shared/model/enumerations/statut.model';

@Component({
  selector: 'jhi-plateau',
  templateUrl: './plateau.component.html'
})
export class PlateauComponent implements OnInit, OnDestroy {
  plateaus?: IPlateau[];
  currentAccount: Account | null = null;
  eventSubscriber?: Subscription;
  currentSearch: string;
  totalItems = 0;
  itemsPerPage = ITEMS_PER_PAGE;
  page!: number;
  predicate!: string;
  ascending!: boolean;
  ngbPaginationPage = 1;

  constructor(
    protected plateauService: PlateauService,
    protected activatedRoute: ActivatedRoute,
    private accountService: AccountService,
    protected dataUtils: JhiDataUtils,
    protected router: Router,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal
  ) {
    this.currentSearch =
      this.activatedRoute.snapshot && this.activatedRoute.snapshot.queryParams['search']
        ? this.activatedRoute.snapshot.queryParams['search']
        : '';
  }

  loadPage(page?: number): void {
    const pageToLoad: number = page || this.page;

    if (this.currentSearch) {
      this.plateauService
        .search({
          page: pageToLoad - 1,
          query: this.currentSearch,
          size: this.itemsPerPage,
          sort: this.sort()
        })
        .subscribe(
          (res: HttpResponse<IPlateau[]>) => this.onSuccess(res.body, res.headers, pageToLoad),
          () => this.onError()
        );
      return;
    }

    this.plateauService
      .query({
        page: pageToLoad - 1,
        size: this.itemsPerPage,
        sort: this.sort()
      })
      .subscribe(
        (res: HttpResponse<IPlateau[]>) => this.onSuccess(res.body, res.headers, pageToLoad),
        () => this.onError()
      );
  }

  search(query: string): void {
    this.currentSearch = query;
    this.loadPage(1);
  }

  ngOnInit(): void {
    

    this.activatedRoute.data
    .pipe(
      flatMap(
        () => this.accountService.identity(),
        (data, account) => {
          this.page = data.pagingParams.page;
          this.ascending = data.pagingParams.ascending;
          this.predicate = data.pagingParams.predicate;
          this.ngbPaginationPage = data.pagingParams.page;
          this.currentAccount = account;
          this.loadPage();
        }
      )
    )
    .subscribe();
    this.registerChangeInPlateaus();

    
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IPlateau): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(contentType: string, base64String: string): void {
    return this.dataUtils.openFile(contentType, base64String);
  }

  registerChangeInPlateaus(): void {
    this.eventSubscriber = this.eventManager.subscribe('plateauListModification', () => this.loadPage());
  }

  delete(plateau: IPlateau): void {
    const modalRef = this.modalService.open(PlateauDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.plateau = plateau;
  }

  sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }
  canSinscrire(plateau: Plateau|any): boolean {
    if(plateau.statut === Statut.ENCOURS && plateau.valid){
      window.console.log(plateau.statut !== Statut.ENATTENTE);
      return true;
    }
      
    return false;
  }
  protected onSuccess(data: IPlateau[] | null, headers: HttpHeaders, page: number): void {
    this.totalItems = Number(headers.get('X-Total-Count'));
    this.page = page;
    this.ngbPaginationPage = this.page;
    this.router.navigate(['/plateau'], {
      queryParams: {
        page: this.page,
        size: this.itemsPerPage,
        search: this.currentSearch,
        sort: this.predicate + ',' + (this.ascending ? 'asc' : 'desc')
      }
    });
    this.plateaus = data || [];
  }

  protected onError(): void {
    this.ngbPaginationPage = this.page;
  }
  setActive(plateau: IPlateau, isActivated: boolean): void {
    this.plateauService.update({ ...plateau, valid: isActivated, statut: Statut.ENCOURS }).subscribe(() => this.loadPage());
  }
  inscription(plateau: IPlateau): void{
    const max : number | undefined = plateau.nombreEquipeMax  ;
    const nbr : number | undefined = plateau.nombreEquipe ;
    if(max !== undefined && nbr !== undefined)
    localStorage.setItem('maxEquipe', JSON.stringify(max - nbr));
    localStorage.setItem('id', JSON.stringify(plateau.id));
    localStorage.setItem('user', JSON.stringify(plateau.user?.id));
    this.router.navigate(['/inscription/new']);
    window.scrollTo(0, 0);
  }

  
}
