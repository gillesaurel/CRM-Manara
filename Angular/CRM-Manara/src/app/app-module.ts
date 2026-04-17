import { NgModule, provideBrowserGlobalErrorListeners } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule } from '@angular/common/http';

import { AppRoutingModule } from './app-routing-module';
import { App } from './app';
import { Activites } from './components/templates/adminOnly/activites/activites';
import { Utilisateurs } from './components/templates/adminOnly/utilisateurs/utilisateurs';
import { Email } from './components/templates/adminOnly/email/email';
import { ListeAttente } from './components/templates/adminOnly/liste-attente/liste-attente';
import { AdminAddUser } from './components/templates/adminOnly/admin-add-user/admin-add-user';
import { Dashboard } from './components/templates/parent/dashboard/dashboard';
import { Administrateur } from './components/templates/administrateur/administrateur';
import { FormsModule } from '@angular/forms';
import { FragmentAdministrateur } from './components/templates/fragments/fragment-administrateur/fragment-administrateur';
import { Animateur } from './components/templates/fragments/animateur/animateur';
import { Main } from './components/templates/fragments/main/main';
import { Parent } from './components/templates/fragments/parent/parent';
import { Fragment } from './components/templates/fragment/fragment';
import { Footer } from './components/templates/fragments/footer/footer';
import { Auth } from './components/templates/auth/auth';

@NgModule({
  declarations: [
    App,
    Activites,
    Utilisateurs,
    Email,
    ListeAttente,
    AdminAddUser,
    Dashboard,
    Administrateur,
    FragmentAdministrateur,
    Animateur,
    Main,
    Parent,
    Fragment,
    Footer,
    Auth,
  ],
  imports: [BrowserModule, AppRoutingModule, HttpClientModule, FormsModule],
  providers: [provideBrowserGlobalErrorListeners()],
  bootstrap: [App],
})
export class AppModule {}
