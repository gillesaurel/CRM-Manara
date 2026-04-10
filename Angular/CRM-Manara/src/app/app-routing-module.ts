import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { Administrateur } from './components/templates/administrateur/administrateur';

const routes: Routes = [
  { path: '', redirectTo: '/utilisateurs', pathMatch: 'full' },
  { path: 'utilisateurs', component: Administrateur },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
