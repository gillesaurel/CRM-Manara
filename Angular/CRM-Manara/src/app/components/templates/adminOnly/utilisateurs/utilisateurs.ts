import { Component, OnInit } from '@angular/core';
import { AdminService } from '../../../../services/admin-service';

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

  constructor(private adminService: AdminService) { }

  ngOnInit(): void {
    this.loadUsers();
  }

  loadUsers() {
    this.adminService.listeUsers(this.keyword).subscribe({
      next: (data: any) => {
        this.parents = data.parents || [];
        this.animateurs = data.animateurs || [];
        this.enfants = data.enfants || [];
      },
      error: (error) => {
        console.error('Erreur lors de la récupération des utilisateurs :', error);
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
