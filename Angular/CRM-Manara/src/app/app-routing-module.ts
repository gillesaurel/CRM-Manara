import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { Administrateur } from './app/components/administrateur/administrateur';
import { Activites } from './app/components/adminOnly/activites/activites';
import { Utilisateurs } from './app/components/adminOnly/utilisateurs/utilisateurs';
import { AdminAddUser } from './app/components/adminOnly/admin-add-user/admin-add-user';
import { Email } from './app/components/adminOnly/email/email';
import { Auth } from './app/components/auth/auth';
import { UserAuth } from './app/verifier/user-auth';
import { Accueil } from './app/components/accueil/accueil';
import { Header } from './app/components/header/header';
import { Footer } from './app/components/footer/footer';
import { AnimateurDashboard } from './app/components/animateur/animateur-dashboard/animateur-dashboard';
import { DashboardComponent } from './app/components/parent/dashboard/dashboard';
import { EnfantsListComponent } from './app/components/parent/enfants-list/enfants-list';
import { ModifierEnfantComponent } from './app/components/parent/modifier-enfant/modifier-enfant';
import { NotificationsComponent } from './app/components/parent/notifications/notifications';
import { PlanningComponent } from './app/components/parent/planning/planning';
import { ProfilComponent } from './app/components/parent/profil/profil';
import { SessionsListComponent } from './app/components/parent/sessions-list/sessions-list';
import { ActivitesListComponent } from './app/components/parent/activites-list/activites-list';
import { DetailsEnfantComponent } from './app/components/parent/details-enfant/details-enfant';
import { AjouterEnfantComponent } from './app/components/parent/ajouter-enfant/ajouter-enfant';
import { AnimateurHoraire } from './app/components/animateur/animateur-horaire/animateur-horaire';
import { AnimateurInscription } from './app/components/animateur/animateur-inscription/animateur-inscription';  // ← add this
import { AnimateurPresences } from './app/components/animateur/animateur-presences/animateur-presences';
import { AnimateurIncidents } from './app/components/animateur/animateur-incidents/animateur-incidents';

const routes: Routes = [
  // Routes publiques
  { path: '', redirectTo: '/home', pathMatch: 'full' },
  { path: 'home', component: Accueil },
  { path: 'header', component: Header },
  { path: 'footer', component: Footer },
  { path: 'login', component: Auth },

  // Routes Admin
  { path: 'admin', canActivate: [UserAuth], component: Administrateur },
  { path: 'adminOnly/activites', canActivate: [UserAuth], component: Activites },
  { path: 'adminOnly/utilisateurs', canActivate: [UserAuth], component: Utilisateurs },
  { path: 'adminOnly/adminAddUser', canActivate: [UserAuth], component: AdminAddUser },
  { path: 'adminOnly/email', canActivate: [UserAuth], component: Email },

// Routes Animateur
{ path: 'animateur/dashboard', canActivate: [UserAuth], component: AnimateurDashboard },
{ path: 'animateur/horaire', canActivate: [UserAuth], component: AnimateurHoraire },
{ path: 'animateur/presences', canActivate: [UserAuth], component: AnimateurPresences },
{ path: 'animateur/incidents', canActivate: [UserAuth], component: AnimateurIncidents },
{ path: 'animateur/inscriptions', canActivate: [UserAuth], component: AnimateurInscription },

  // ============================================
  // ROUTES PARENT
  // ============================================

  // Dashboard Parent
  { path: 'parent/dashboard', canActivate: [UserAuth], component: DashboardComponent },

  // Gestion des enfants
  { path: 'parent/enfants', canActivate: [UserAuth], component: EnfantsListComponent },
  { path: 'parent/enfants/ajouter', canActivate: [UserAuth], component: AjouterEnfantComponent },
  { path: 'parent/enfants/modifier/:id', canActivate: [UserAuth], component: ModifierEnfantComponent },
  { path: 'parent/enfants/:id', canActivate: [UserAuth], component: DetailsEnfantComponent },

  // Activités
  { path: 'parent/activites', canActivate: [UserAuth], component: ActivitesListComponent },
  { path: 'parent/activites/:id/sessions', canActivate: [UserAuth], component: SessionsListComponent },

  // Profil et planning
  { path: 'parent/profil', canActivate: [UserAuth], component: ProfilComponent },
  { path: 'parent/planning', canActivate: [UserAuth], component: PlanningComponent },
  { path: 'parent/notifications', canActivate: [UserAuth], component: NotificationsComponent },

  // Redirection par défaut si route non trouvée
  { path: '**', redirectTo: '/home' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
