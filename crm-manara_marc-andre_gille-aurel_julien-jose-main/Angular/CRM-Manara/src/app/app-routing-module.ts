import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { Administrateur } from './components/administrateur/administrateur';
import { Activites } from './components/adminOnly/activites/activites';
import { Utilisateurs } from './components/adminOnly/utilisateurs/utilisateurs';
import { AdminAddUser } from './components/adminOnly/admin-add-user/admin-add-user';
import { Email } from './components/adminOnly/email/email';
import { Auth } from './components/auth/auth';
import { UserAuth } from './verifier/user-auth';
import { Accueil } from './components/accueil/accueil';


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
