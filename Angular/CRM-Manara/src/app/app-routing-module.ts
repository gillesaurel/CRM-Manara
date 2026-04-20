import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { Administrateur } from './app/components/administrateur/administrateur';
import { Activites } from './app/components/adminOnly/activites/activites';
import { Utilisateurs } from './app/components/adminOnly/utilisateurs/utilisateurs';
import { AdminAddUser } from './app/components/adminOnly/admin-add-user/admin-add-user';
import { AjouterActivite } from './app/components/adminOnly/ajouter-activite/ajouter-activite';
import { ListeAttente } from './app/components/adminOnly/liste-attente/liste-attente';

import { AnimateurDashboard } from './app/components/animateur/animateur-dashboard/animateur-dashboard';
import { AnimateurInscription } from './app/components/animateur/animateur-inscription/animateur-inscription';

import { AnimateurHoraire } from './app/components/animateur/animateur-horaire/animateur-horaire';
import { AnimateurPresences } from './app/components/animateur/animateur-presences/animateur-presences';
import { AnimateurIncidents } from './app/components/animateur/animateur-incidents/animateur-incidents';


import { Email } from './app/components/adminOnly/email/email';
import { Auth } from './app/components/auth/auth';
import { UserAuth } from './app/verifier/user-auth';
import { Accueil } from './app/components/accueil/accueil';
import { Header } from './app/components/header/header';
import { Footer } from './app/components/footer/footer';

const routes: Routes = [
  { path: '', redirectTo: '/home', pathMatch: 'full' },
  { path: 'home', component: Accueil },
  { path: 'header', component: Header },
  { path: 'footer', component: Footer },
  { path: 'login', component: Auth },
  { path: 'admin', canActivate:[UserAuth], component: Administrateur },
  { path: 'adminOnly/activites', canActivate:[UserAuth], component: Activites },
  { path: 'adminOnly/utilisateurs', canActivate:[UserAuth], component: Utilisateurs },
  { path: 'adminOnly/adminAddUser', canActivate:[UserAuth], component: AdminAddUser },
  { path: 'adminOnly/listeAttente', canActivate:[UserAuth], component: ListeAttente },
  { path: 'adminOnly/email', canActivate:[UserAuth], component: Email },
  { path: 'adminOnly/ajouter-activite', canActivate:[UserAuth], component: AjouterActivite },
  { path: 'adminOnly/ajouter-activite/:id', canActivate:[UserAuth], component: AjouterActivite },
  { path: 'animateur/dashboard',  component: AnimateurDashboard },
  { path: 'animateur/inscription',  component: AnimateurInscription },
  { path: 'animateur/horaire',  component: AnimateurHoraire },
  { path: 'animateur/presences',  component: AnimateurPresences },
  { path: 'animateur/incidents',  component: AnimateurIncidents },



];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
