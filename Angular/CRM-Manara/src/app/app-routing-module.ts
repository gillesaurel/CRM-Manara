import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { Administrateur } from './components/templates/administrateur/administrateur';
import { Activites } from './components/templates/adminOnly/activites/activites';
import { Utilisateurs } from './components/templates/adminOnly/utilisateurs/utilisateurs';
import { AdminAddUser } from './components/templates/adminOnly/admin-add-user/admin-add-user';
import { Email } from './components/templates/adminOnly/email/email';


const routes: Routes = [
  { path: '', redirectTo: '/administrateur', pathMatch: 'full' },
  { path: 'utilisateurs', component: Administrateur },
  { path: 'adminOnly/activites', component: Activites },
  { path: 'adminOnly/utilisateurs', component: Utilisateurs },
  { path: 'adminOnly/adminAddUser', component: AdminAddUser },
  { path: 'adminOnly/email', component: Email }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
