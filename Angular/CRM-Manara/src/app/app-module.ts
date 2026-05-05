import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common'; // ← add this
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
import { AnimateurInscription } from './app/components/animateur/animateur-inscription/animateur-inscription'; // ← add this
import { AnimateurPresences } from './app/components/animateur/animateur-presences/animateur-presences';
import { AnimateurIncidents } from './app/components/animateur/animateur-incidents/animateur-incidents';

import { ParentNav } from './app/components/fragments/parent/parent';
import { DashboardComponent } from './app/components/parent/dashboard/dashboard';
import { EnfantsListComponent } from './app/components/parent/enfants-list/enfants-list';
import { AjouterEnfantComponent } from './app/components/parent/ajouter-enfant/ajouter-enfant';
import { NotificationsComponent } from './app/components/parent/notifications/notifications';
import { PlanningComponent } from './app/components/parent/planning/planning';
import { ProfilComponent } from './app/components/parent/profil/profil';
import { ModifierEnfantComponent } from './app/components/parent/modifier-enfant/modifier-enfant';
import { DetailsEnfantComponent } from './app/components/parent/details-enfant/details-enfant';
import { ActivitesListComponent } from './app/components/parent/activites-list/activites-list';
import { SessionsListComponent } from './app/components/parent/sessions-list/sessions-list';
import { Register } from './app/components/parent/register/register';
import { ForgotPassword } from './app/components/resetPWD/forgot-password/forgot-password';
import { ResetPassword } from './app/components/resetPWD/reset-password/reset-password';

import { DatePipe } from '@angular/common';

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
    AnimateurInscription,
    AnimateurPresences,
    AnimateurIncidents,

    ParentNav,
    DashboardComponent,
    EnfantsListComponent,
    AjouterEnfantComponent,
    ModifierEnfantComponent,
    DetailsEnfantComponent,
    ActivitesListComponent,
    SessionsListComponent,
    ProfilComponent,
    PlanningComponent,
    NotificationsComponent,
    Register,
    ForgotPassword,
    ResetPassword,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    FormsModule,
    CommonModule,
    RouterModule,
  ],
  providers: [DatePipe],
  bootstrap: [App],
})
export class AppModule {}
