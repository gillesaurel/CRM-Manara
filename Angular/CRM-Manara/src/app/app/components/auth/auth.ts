import { Component } from '@angular/core';
import { AuthService, Utilisateur } from '../../services/auth-service';
import { Router } from '@angular/router';

@Component({
   standalone: false,
  selector: 'app-auth',
  templateUrl: './auth.html',
  styleUrl: './auth.css',
})
export class Auth {

  email: string = '';
  password: string = '';
  error: boolean = false;
  message: string = '';
  user$;

  constructor(
    private authService: AuthService,
    private router: Router
  ) {
    
    this.user$ = this.authService.user$;
  }

  login() {
  const data = {
    email: this.email,
    password: this.password
  };

  this.authService.login(data).subscribe({
    next: (user: Utilisateur | null) => {
      if (!user) {
        this.error = true;
        return;
      }

      this.error = false;

      this.authService.updateUser(user);

      const routes: any = {
        ADMINISTRATEUR: '/admin',
        PARENT: '/parent/dashboard',
        ANIMATEUR: '/animateur/dashboard'
      };

      if (user.role.nom === 'PARENT') {
        window.location.href = '/parent/dashboard';
      } else {
        this.router.navigate([routes[user.role.nom]]);
      }
    },
    error: () => {
      this.error = true;
    }
  });
}

  logout(): void {
    this.authService.logout().subscribe({
      next: () => {
        this.router.navigate(['/login']);
      }
    });
  }
}