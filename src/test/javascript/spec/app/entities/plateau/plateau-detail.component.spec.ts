import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';
import { JhiDataUtils } from 'ng-jhipster';

import { PlateauFffTestModule } from '../../../test.module';
import { PlateauDetailComponent } from 'app/entities/plateau/plateau-detail.component';
import { Plateau } from 'app/shared/model/plateau.model';

describe('Component Tests', () => {
  describe('Plateau Management Detail Component', () => {
    let comp: PlateauDetailComponent;
    let fixture: ComponentFixture<PlateauDetailComponent>;
    let dataUtils: JhiDataUtils;
    const route = ({ data: of({ plateau: new Plateau(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PlateauFffTestModule],
        declarations: [PlateauDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(PlateauDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(PlateauDetailComponent);
      comp = fixture.componentInstance;
      dataUtils = fixture.debugElement.injector.get(JhiDataUtils);
    });

    describe('OnInit', () => {
      it('Should load plateau on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.plateau).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });

    describe('byteSize', () => {
      it('Should call byteSize from JhiDataUtils', () => {
        // GIVEN
        spyOn(dataUtils, 'byteSize');
        const fakeBase64 = 'fake base64';

        // WHEN
        comp.byteSize(fakeBase64);

        // THEN
        expect(dataUtils.byteSize).toBeCalledWith(fakeBase64);
      });
    });

    describe('openFile', () => {
      it('Should call openFile from JhiDataUtils', () => {
        // GIVEN
        spyOn(dataUtils, 'openFile');
        const fakeContentType = 'fake content type';
        const fakeBase64 = 'fake base64';

        // WHEN
        comp.openFile(fakeContentType, fakeBase64);

        // THEN
        expect(dataUtils.openFile).toBeCalledWith(fakeContentType, fakeBase64);
      });
    });
  });
});
