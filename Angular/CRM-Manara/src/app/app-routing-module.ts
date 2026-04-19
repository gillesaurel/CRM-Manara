import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { Administrateur } from './components/templates/administrateur/administrateur';
import { Activites } from './components/templates/adminOnly/activites/activites';
import { Utilisateurs } from './components/templates/adminOnly/utilisateurs/utilisateurs';
import { AdminAddUser } from './components/templates/adminOnly/admin-add-user/admin-add-user';
import { Email } from './components/templates/adminOnly/email/email';
import { Auth } from './components/templates/auth/auth';
import { UserAuth } from './verifier/user-auth';
import { AjouterActivite } from './components/templates/adminOnly/ajouter-activite/ajouter-activite';
import { ListeAttente } from './components/templates/adminOnly/liste-attente/liste-attente';

const routes: Routes = [
  { path: '', redirectTo: '/login', pathMatch: 'full' },
  { path: 'login', component: Auth },
  { path: 'admin', canActivate: [UserAuth], component: Administrateur },
  { path: 'adminOnly/activites', canActivate: [UserAuth], component: Activites },
  { path: 'adminOnly/utilisateurs', canActivate: [UserAuth], component: Utilisateurs },
  { path: 'adminOnly/adminAddUser', canActivate: [UserAuth], component: AdminAddUser },
  { path: 'adminOnly/email', canActivate: [UserAuth], component: Email },
  { path: 'adminOnly/ajouter-activite', canActivate: [UserAuth], component: AjouterActivite },
  { path: 'adminOnly/ajouter-activite/:id', canActivate: [UserAuth], component: AjouterActivite },
  { path: 'adminOnly/listeAttente', canActivate: [UserAuth], component: ListeAttente },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
