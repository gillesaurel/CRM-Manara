import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthService } from '../../../services/auth-service';

@Component({
  selector: 'app-reset-password',
  standalone: false,
  templateUrl: './reset-password.html',
  styleUrl: './reset-password.css'
})
export class ResetPassword implements OnInit {

  token: string = '';
  nouveauMotDePasse: string = '';
  confirmation: string = '';
  success: string = '';
  error: string = '';
  loading: boolean = false;
  tokenInvalide: boolean = false;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private authService: AuthService
  ) {}

  ngOnInit() {
    this.token = this.route.snapshot.queryParamMap.get('token') || '';
    if (!this.token) {
      this.tokenInvalide = true;
    }
  }

  submit() {
    if (this.nouveauMotDePasse !== this.confirmation) {
      this.error = 'Les mots de passe ne correspondent pas.';
      return;
    }

    this.loading = true;
    this.error = '';

    this.authService.resetPassword(this.token, this.nouveauMotDePasse).subscribe({
      next: (res: any) => {
        this.success = res.message;
        this.loading = false;
        setTimeout(() => this.router.navigate(['/login']), 3000);
      },
      error: (err: any) => {
        this.error = err.error?.message || 'Une erreur est survenue.';
        this.loading = false;
        if (this.error.includes('expiré') || this.error.includes('invalide')) {
          this.tokenInvalide = true;
        }
      }
    });
  }
}
