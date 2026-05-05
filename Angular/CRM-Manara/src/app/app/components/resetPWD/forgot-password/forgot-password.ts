import { Component, ChangeDetectorRef } from '@angular/core';
import { AuthService } from '../../../services/auth-service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-forgot-password',
  standalone: false,
  templateUrl: './forgot-password.html',
  styleUrl: './forgot-password.css',
})
export class ForgotPassword {

  email: string = '';
  success: string = '';
  error: string = '';
  loading: boolean = false;

  constructor(
    private authService: AuthService,
    private cdr: ChangeDetectorRef,
    private router: Router
  ) { }

  submit() {
    if (!this.email) return;
    this.loading = true;
    this.error = '';

    this.authService.forgotPassword(this.email).subscribe({
      next: (res: any) => {
        this.success = res.message;
        this.loading = false;
        this.cdr.detectChanges();

        setTimeout(() => {
          this.success = '';
          this.router.navigate(['/login']);
        }, 5000);
      },
      error: (err) => {
        console.log('Erreur:', err);
        this.error = 'Une erreur est survenue. Veuillez réessayer.';
        this.loading = false;
        this.cdr.detectChanges();
      }
    });
  }
}