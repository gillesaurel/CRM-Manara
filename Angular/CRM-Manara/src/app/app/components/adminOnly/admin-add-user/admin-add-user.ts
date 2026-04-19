import { Component, OnInit } from '@angular/core';
import { AdminService } from '../../../services/admin-service';

@Component({
  selector: 'app-admin-add-user',
  standalone: false,
  templateUrl: './admin-add-user.html',
  styleUrl: './admin-add-user.css',
})
export class AdminAddUser implements OnInit {

  user: any = {
    prenom: '',
    nom: '',
    telephone: '',
    email: '',
    motDePasse: '',
    confirmMotDePasse: '',
    role: { id: null}
  };

  roles: any[] = []
  message: string = '';

  constructor(private adminService: AdminService) { }

  ngOnInit(): void {
    this.loadRoles();
  }

  loadRoles() {
    this.roles = [
      { id: 1, nom: 'ADMIN' },
      { id: 2, nom: 'ANIMATEUR' },
      { id: 3, nom: 'PARENT' }
    ];
  }

  submitForm() {
    if (this.user.motDePasse !== this.user.confirmMotDePasse) {
      this.message = 'Les mots de passe ne correspondent pas.';
      return;
    }

    this.adminService.saveUserByAdmin(this.user).subscribe({
      next: (response) => {
        this.message = 'Utilisateur ajouté avec succès !';
        this.resetForm();
      },
      error: (error) => {
        console.error(error);
        alert('Erreur lors de la création de l\'utilisateur.');
      }
    });
  }

  resetForm() {
    this.user = {
      prenom: '',
      nom: '',
      telephone: '',
      email: '',
      motDePasse: '',
      confirmMotDePasse: '',
      role: { id: null }
    };
  }

}
