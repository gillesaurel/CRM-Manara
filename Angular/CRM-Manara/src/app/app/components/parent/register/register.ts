import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';
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
    private http: HttpClient,
    private router: Router
  ) {}

  onSubmit(): void {
    // Vérification côté frontend
    if (this.utilisateur.motdepasse !== this.utilisateur.confirmMotdepasse) {
      this.passwordMismatch = true;
      return;
    }
    this.passwordMismatch = false;

    // Données envoyées SANS confirmMotdepasse
    const data = {
      prenom: this.utilisateur.prenom,
      nom: this.utilisateur.nom,
      telephone: this.utilisateur.telephone,
      email: this.utilisateur.email,
      motdepasse: this.utilisateur.motdepasse,
      confirmMotdepasse: this.utilisateur.confirmMotdepasse
    };

    this.http.post('http://localhost:8080/api/register', data).subscribe({
      next: () => {
        this.successMessage = 'Inscription réussie ! Vous pouvez maintenant vous connecter.';
        this.errorMessage = '';

        setTimeout(() => this.router.navigate(['/login']), 2000);
      },
      error: (err: any) => {
        this.successMessage = '';
        this.errorMessage = 'Erreur lors de l\'inscription';

        // 🔍 Logs détaillés pour debug
        console.error("Status:", err.status);
        console.error("Error body:", err.error);
        console.error("Full error:", err);
      }
    });
  }
}