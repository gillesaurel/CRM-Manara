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


const routes: Routes = [
  { path: '', redirectTo: '/home', pathMatch: 'full' },
  { path: 'home', component: Accueil },
  { path: 'login', component: Auth },
  { path: 'admin', canActivate:[UserAuth], component: Administrateur },
  { path: 'adminOnly/activites', canActivate:[UserAuth], component: Activites },
  { path: 'adminOnly/utilisateurs', canActivate:[UserAuth], component: Utilisateurs },
  { path: 'adminOnly/adminAddUser', canActivate:[UserAuth], component: AdminAddUser },
  { path: 'adminOnly/email', canActivate:[UserAuth], component: Email },
  { path: 'animateur/dashboard', canActivate:[UserAuth], component: Email }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
