import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';  // ← Ajouter cet import
import { AuthService } from '../../../services/auth-service';

interface UtilisateurForm {
  prenom: string;
  nom: string;
  telephone: string;
  email: string;
  motdepasse: string;
  confirmMotdepasse: string;
}

@Component({
  selector: 'app-register',
  standalone: false,
  templateUrl: './register.html',
  styleUrls: ['./register.css']
})
export class Register {
  
  utilisateur: UtilisateurForm = {
    prenom: '',
    nom: '',
    telephone: '',
    email: '',
    motdepasse: '',
    confirmMotdepasse: ''
  };

  message: string = '';
  successMessage: string = '';
  errorMessage: string = '';
  passwordMismatch: boolean = false;

  constructor(
    private authService: AuthService,
    private http: HttpClient,  //
    private router: Router
  ) {}

  onSubmit(): void {
    if (this.utilisateur.motdepasse !== this.utilisateur.confirmMotdepasse) {
      this.passwordMismatch = true;
      return;
    }
    this.passwordMismatch = false;

    // Appel DIRECT vers l'API (sans passer par AuthService)
    this.http.post('http://localhost:8080/register/save', this.utilisateur).subscribe({
      next: () => {
        this.successMessage = 'Inscription réussie ! Vous pouvez maintenant vous connecter.';
        setTimeout(() => this.router.navigate(['/login']), 2000);
      },
      error: (err: any) => {
        this.errorMessage = 'Erreur lors de l\'inscription';
        console.error('Erreur:', err);
      }
    });
  }
}