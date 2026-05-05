import { Component, OnInit } from '@angular/core';
import { AuthService } from '../../services/auth-service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-header',
  standalone: false,
  templateUrl: './header.html',
  styleUrl: './header.css',
})
export class Header implements OnInit {

  user: any = null;

  constructor(
    private authService: AuthService,
    private router: Router
  ) { }

  isDarkMode: boolean = false;

  toggleDarkMode() {
    this.isDarkMode = !this.isDarkMode;
    document.body.classList.toggle('dark-mode', this.isDarkMode);
    localStorage.setItem('darkMode', this.isDarkMode ? 'true' : 'false');
  }
  ngOnInit(): void {
    this.isDarkMode = localStorage.getItem('darkMode') === 'true';
    document.body.classList.toggle('dark-mode', this.isDarkMode);

    this.authService.getMe().subscribe({
      next: (data) => {
        this.user = data;
      },
      error: () => {
        this.user = null;
      }
    });
  }

  // Vérifie si l'utilisateur est connecté
  isLoggedIn(): boolean {
    return !!this.user;
  }

  // Retourne le nom affiché
  getUserName(): string {
    return this.user?.name || '';
  }

  logout(): void {
    const clearAndRedirect = () => {
      this.user = null;
      localStorage.removeItem('user');
      this.router.navigate(['/login']);
    };

    this.authService.logout().subscribe({
      next: clearAndRedirect,
      error: (err) => {
        console.error('Erreur lors de la déconnexion:', err);
        clearAndRedirect();
      }
    });
  }

  // Vérifie si l'utilisateur est un parent


  isParent(): boolean {
    const user = localStorage.getItem('user');

    if (!user) return false;

    try {
      const parsed = JSON.parse(user);

      if (!parsed?.role?.nom) return false;

      return parsed.role.nom === 'PARENT';
    } catch {
      return false;
    }

  }

}