import { HttpErrorResponse } from '@angular/common/http';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { AdminService } from '../../../services/admin-service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-utilisateurs',
  standalone: false,
  templateUrl: './utilisateurs.html',
  styleUrl: './utilisateurs.css',
})
export class Utilisateurs implements OnInit {

  keyword: string = '';
  parents: any[] = [];
  animateurs: any[] = [];
  enfants: any[] = [];

  message: string = '';
  error: string = '';

  constructor(
    private adminService: AdminService,
    private router: Router,
    private cdr: ChangeDetectorRef) { }

  ngOnInit(): void {
    const nav = this.router.getCurrentNavigation();
    this.message = nav?.extras?.state?.['message'] || '';
    this.loadUsers();
  }

  loadUsers() {
    this.adminService.listeUsers(this.keyword).subscribe({
      next: (data: any) => {
        this.parents = data.parents || [];
        this.animateurs = data.animateurs || [];
        this.enfants = data.enfant || [];
        this.cdr.detectChanges();
        console.log('Parents trouvées :', this.parents);
        console.log('Enfants trouvées :', this.enfants);
      },
      error: (error: HttpErrorResponse) => {
        console.error('Erreur complète :', error);
        console.error('Status :', error.status);
        console.error('Message :', error.message);
        console.error('Body :', error.error);
      }
    });
  }

  searchUsers() {
    this.loadUsers();
  }

  resetPassword(userId: number, newPassword: string) {
    if (!newPassword?.trim() || newPassword.length < 8) {
      alert('Mot de passe trop court (min 8 caractères).');
      return;
    } else {
      (!confirm('Le mot de passe sera réinitialisé.'))
    }

    console.log('Appel resetPassword envoyé');

    this.adminService.resetPassword(userId, newPassword).subscribe({
      next: (res: any) => {
        this.message = res?.message || res;
        this.loadUsers();
        setTimeout(() => {
          this.message = '';
          this.cdr.detectChanges();
        }, 3000);
      },

      error: (err) => {
        console.error(err);
        this.error = err.error?.message || err.error || 'Erreur';
        this.message = '';
      }
    });

  }

  deleteUser(id: number) {
    if (!confirm('Êtes-vous sûr de vouloir supprimer cet utilisateur ?')) return;

    this.message = '';
    this.error = '';

    this.adminService.deleteUser(id).subscribe({
      next: (res: any) => {
        this.message = res.message;

        this.loadUsers();

        setTimeout(() => {
          this.message = '';
          this.cdr.detectChanges();
        }, 3000);
      },
      error: (err) => {
        console.error(err);
        this.error = err.error?.message || 'Erreur de suppression';
      }
    });
  }
}
