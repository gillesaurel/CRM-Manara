import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../../services/auth-service';

@Component({
  selector: 'app-main',
  standalone: false,
  templateUrl: './main.html',
  styleUrls: ['./main.css'],
})
export class Main implements OnInit {
  
  user: any = null;

  constructor(
    private authService: AuthService,
    private router: Router,
    private cdr: ChangeDetectorRef  
  ) {}

  ngOnInit(): void {
    // S'abonne aux changements d'utilisateur 
    this.authService.user$.subscribe(user => {
      console.log('Header: utilisateur mis à jour', user);
      this.user = user;
      this.cdr.detectChanges();
    });
    
    this.chargerUtilisateur();
    
    // Écoute les changements du localStorage
    window.addEventListener('storage', (event) => {
      if (event.key === 'user') {
        this.user = event.newValue ? JSON.parse(event.newValue) : null;
        this.cdr.detectChanges();  
      }
    });
    
    // Rafraîchissement périodique
    setInterval(() => {
      const currentUser = localStorage.getItem('user');
      if (currentUser) {
        const parsed = JSON.parse(currentUser);
        if (!this.user || JSON.stringify(this.user) !== JSON.stringify(parsed)) {
          console.log('Rafraîchissement périodique:', parsed);
          this.user = parsed;
          this.cdr.detectChanges();
        }
      } else if (this.user !== null) {
        this.user = null;
        this.cdr.detectChanges();
      }
    }, 500);
  }

  chargerUtilisateur(): void {
    const storedUser = localStorage.getItem('user');
    if (storedUser) {
      this.user = JSON.parse(storedUser);
      this.cdr.detectChanges();  
    }
    
    this.authService.getMe().subscribe({
      next: (data) => {
        this.user = data;
        if (data) {
          localStorage.setItem('user', JSON.stringify(data));
        }
        this.cdr.detectChanges();  
      },
      error: () => {
        this.user = null;
        localStorage.removeItem('user');
        this.cdr.detectChanges();  
      }
    });
  }

  isLoggedIn(): boolean {
    return this.user !== null;
  }

  isParent(): boolean {
    return this.user?.role?.nom === 'PARENT';
  }

  getUserName(): string {
    return this.user?.prenom || this.user?.email || 'Utilisateur';
  }

  logout(): void {
    this.authService.logout().subscribe({
      next: () => {
        this.user = null;
        localStorage.removeItem('user');
        this.cdr.detectChanges();  
        this.router.navigate(['/login']);
      },
      error: (err) => {
        console.error('Erreur lors de la déconnexion:', err);
      }
    });
  }
}