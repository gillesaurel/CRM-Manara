import { NgModule, provideBrowserGlobalErrorListeners } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule } from '@angular/common/http';

import { AppRoutingModule } from './app-routing-module';
import { App } from './app';
import { Activites } from './components/adminOnly/activites/activites';
import { Utilisateurs } from './components/adminOnly/utilisateurs/utilisateurs';
import { Email } from './components/adminOnly/email/email';
import { ListeAttente } from './components/adminOnly/liste-attente/liste-attente';
import { AdminAddUser } from './components/adminOnly/admin-add-user/admin-add-user';
import { Dashboard } from './components/parent/dashboard/dashboard';
import { Administrateur } from './components/administrateur/administrateur';
import { FormsModule } from '@angular/forms';
import { FragmentAdministrateur } from './components/fragments/fragment-administrateur/fragment-administrateur';
import { Animateur } from './components/fragments/animateur/animateur';
import { Main } from './components/fragments/main/main';
import { Parent } from './components/fragments/parent/parent';
import { Fragment } from './components/fragment/fragment';
import { Footer } from './components/fragments/footer/footer';
import { Auth } from './components/auth/auth';

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
