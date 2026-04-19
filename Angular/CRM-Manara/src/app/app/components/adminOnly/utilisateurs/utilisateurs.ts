import { HttpErrorResponse } from '@angular/common/http';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { AdminService } from '../../../services/admin-service';

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
  private cdr: ChangeDetectorRef) { }

  ngOnInit(): void {
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
    if (!newPassword) {
      alert('Veuillez entrer un nouveau mot de passe.');
      return;
    }
    this.adminService.resetPassword(userId, newPassword).subscribe({
      next: () => {
        this.message = 'Mot de passe réinitialisé avec succès !';
      }
    });
  }

  deleteUser(id: number) {
    if (!confirm('Êtes-vous sûr de vouloir supprimer cet utilisateur ?')) {
      return;
    }
    this.adminService.deleteUser(id).subscribe({
      next: () => {
        this.message = 'Utilisateur supprimé avec succès !';
        this.loadUsers();
      },
    });
  }
}
