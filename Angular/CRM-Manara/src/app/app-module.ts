import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { AppRoutingModule } from './app-routing-module';
import { App } from './app';
import { Administrateur } from './app/components/administrateur/administrateur';
import { Activites } from './app/components/adminOnly/activites/activites';
import { Utilisateurs } from './app/components/adminOnly/utilisateurs/utilisateurs';
import { AdminAddUser } from './app/components/adminOnly/admin-add-user/admin-add-user';
import { Email } from './app/components/adminOnly/email/email';
import { Auth } from './app/components/auth/auth';
import { Accueil } from './app/components/accueil/accueil';
import { Main } from './app/components/fragments/main/main';
import { Header } from './app/components/header/header';
import { Footer } from './app/components/footer/footer';
import { FragmentAdministrateur } from './app/components/fragments/fragment-administrateur/fragment-administrateur';
import { ListeAttente } from './app/components/adminOnly/liste-attente/liste-attente';
import { AjouterActivite } from './app/components/adminOnly/ajouter-activite/ajouter-activite';
import { AnimateurDashboard } from './app/components/animateur/animateur-dashboard/animateur-dashboard';
import { AnimateurHoraire } from './app/components/animateur/animateur-horaire/animateur-horaire';

@NgModule({
  declarations: [
    App,
    Administrateur,
    Activites,
    Utilisateurs,
    AdminAddUser,
    Email,
    Auth,
    Accueil,
    Main,
    Footer,
    Header,
    FragmentAdministrateur,
    ListeAttente,
    AjouterActivite,
    AnimateurDashboard,
    AnimateurHoraire,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    FormsModule,
    RouterModule,
  ],
  bootstrap: [App],
})
export class AppModule {}