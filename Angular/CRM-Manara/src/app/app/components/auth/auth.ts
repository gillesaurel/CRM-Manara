import { Component, OnInit } from '@angular/core';
import { AuthService, Utilisateur } from '../../services/auth-service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-auth',
  standalone: false,
  templateUrl: './auth.html',
  styleUrl: './auth.css',
})
export class Auth implements OnInit {
  email: string = '';
  password: string = '';
  error: boolean = false;
  message: string = '';


  user: Utilisateur | null = null;

  constructor(
    private authService: AuthService,
    private rout: Router
  ) { }

  ngOnInit(): void {
    this.authService.getMe().subscribe({
      next: (data: Utilisateur | null) => {
        this.user = data;
      },
      error: () => {
        this.user = null;
      }
    });
  }

  login() {
  const data = {
    email: this.email,
    password: this.password
  };

  this.authService.login(data).subscribe({
    next: () => {
      
      setTimeout(() => {
        this.authService.getMe().subscribe({
          next: (user: Utilisateur | null) => {

            if (!user) {
              this.error = true;
              return;
            }

            const role = user.role.nom;

            if (role === 'ADMINISTRATEUR') {
              this.rout.navigate(['/admin']);
            } else if (role === 'PARENT') {
              this.rout.navigate(['/parent']);
            } else if (role === 'ANIMATEUR') {
              this.rout.navigate(['/animateur']);
            }
          },
          error: () => {
            this.error = true;
          }
        });
      }, 200);
    },
    error: () => {
      this.error = true;
    }
  });
}

  logout(): void {
    this.authService.logout().subscribe(() => {
      this.user = null;
      this.rout.navigate(['/login']);
    });
  }
}