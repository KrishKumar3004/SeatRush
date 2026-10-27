import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import { App } from './app';
describe('App',()=>{beforeEach(async()=>{await TestBed.configureTestingModule({imports:[App],providers:[provideHttpClient(),provideHttpClientTesting()]}).compileComponents()});it('renders the SeatRush experience',()=>{const fixture=TestBed.createComponent(App);fixture.detectChanges();expect(fixture.nativeElement.querySelector('.brand').textContent).toContain('SeatRush');expect(fixture.nativeElement.querySelector('h1').textContent).toContain('Neon Skyline')})});

