import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { AdminService } from '../../../services/admin-service';
import { ActivatedRoute, Router } from '@angular/router';

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
    role: { id: null }
  };

  roles: any[] = []
  message: string = '';

  constructor(
    private adminService: AdminService,
    private router: Router,
    private cdr: ChangeDetectorRef) { }

  ngOnInit(): void {
    this.loadRoles();
  }

  loadRoles() {
    this.roles = [
      { id: 1, nom: 'ADMIN' },
      { id: 2, nom: 'ANIMATEUR' },
      { id: 3, nom: 'PARENT' }
    ];
    this.cdr.detectChanges();
  }

  submitForm() {
    if (!this.user.motdepasse) {
      this.message = 'Mot de passe requis';
      return;
    }

    if (this.user.motdepasse !== this.user.confirmMotdepasse) {
      this.message = 'Les mots de passe ne correspondent pas.';
      return;
    }

    this.adminService.saveUserByAdmin(this.user).subscribe({
      next: () => {
        this.resetForm();

        this.router.navigate(['/adminOnly/utilisateurs'], {
          state: {
            message: 'Utilisateur créé avec succès !'
          }
        });
      },

      error: (err) => {
        this.message =
          err.error?.message || 'Erreur lors de la création';

        alert("Error appelé");
        setTimeout(() => {
          this.message = '';
        }, 2000);
      }
    });
  }

  resetForm() {
    this.user = {
      prenom: '',
      nom: '',
      telephone: '',
      email: '',
      motdepasse: '',
      confirmMotdepasse: '',
      role: { id: null }
    };
  }

}
