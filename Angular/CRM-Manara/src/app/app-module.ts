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
  ],
  imports: [BrowserModule, AppRoutingModule, HttpClientModule, FormsModule],
  providers: [provideBrowserGlobalErrorListeners()],
  bootstrap: [App],
})
export class AppModule {}
