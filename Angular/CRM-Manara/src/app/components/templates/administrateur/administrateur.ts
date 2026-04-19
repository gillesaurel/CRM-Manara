import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { AdminService } from '../../../services/admin-service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-administrateur',
  standalone: false,
  templateUrl: './administrateur.html',
  styleUrl: './administrateur.css',
})
export class Administrateur implements OnInit {
  activites: any[] = [];
  listeAttentesData: any[] = [];
  tauxRemplissage: number = 0;
  totalActivites: number = 0;
  activiteForm: any = {};
  isEditMode: boolean = false;
  showModal: boolean = false;
  emailPayload = {
    type: 'ALL',
    subject: 'Notification',
    message: ''
  };

  constructor(
    private adminService: AdminService,
    private cdr: ChangeDetectorRef,
    private router: Router,
  ) { }

  ngOnInit() {
    this.getAdminData();
    this.listeAttentes();
  }


  goToAddActivite(id?: number) {
    if (id) {
      this.router.navigate(['/adminOnly/ajouter-activite', id]);
    } else {
      this.router.navigate(['/adminOnly/ajouter-activite']);
    }
  }

  loadActiviteById(id: number) {
    this.adminService.getActiviteById(id).subscribe((data: any) => {
      this.activiteForm = data;
      this.isEditMode = true;
    });
  }


  getAdminData() {
    console.log("Appel de admin data");
    this.adminService.getAdminData().subscribe({
      next: (data: any) => {
        console.log('Données administrateur reçues :', data);
        this.activites = data.activites;
        this.tauxRemplissage = data.pourcentageInscrit;
        this.totalActivites = data.totalActivities;
        this.cdr.detectChanges();
      },
      error: (error) => {
        console.error('Erreur lors de la récupération des données administrateur :', error);
      },
    });
  }

  showActivites() {
    this.adminService.showActivites().subscribe({
      next: (data: any) => {
        this.activites = data.activites;
        this.tauxRemplissage = data.pourcentageInscrit;
        this.totalActivites = data.totalActivities;
        console.log('Activités trouvées :', this.activites);
      },
      error: (error) => {
        console.error('Erreur lors de la récupération des activités :', error);
      }
    });
  }

  saveOrUpdateActivite() {
    this.adminService.createActivite(this.activiteForm).subscribe({
      next: (response) => {
        alert('Activité enregistrée avec succès !');
        this.activiteForm = {};
        this.isEditMode = false;
        this.getAdminData();
        this.showActivites();
      }
    });
  }

  modifyActivite(id: number) {
    this.adminService.updateActivite(id).subscribe({
      next: (response) => {
        alert('Activité modifiée avec succès !');
        this.getAdminData();
        this.showActivites();
        this.activiteForm = {};
        this.isEditMode = true;
      }
    });
  }

  removeActivite(id: number) {
    if (!confirm('Êtes-vous sûr de vouloir supprimer cette activité ?')) {
      return;
    }
    this.adminService.removeActivite(id).subscribe({
      next: () => {
        alert('Activité supprimée avec succès !');
        this.activites = this.activites.filter((activite) => activite.id !== id);
        this.getAdminData();
        this.showActivites();
      }
    });
  }

  listeAttentes() {
    this.adminService.listeAttentes().subscribe({
      next: (data: any) => {
        console.log(data);
        this.listeAttentesData = data;
      }
    });
  }

  validerInscription(id: number) {
    this.adminService.validerInscription(id).subscribe({
      next: () => {
        alert('Inscription validée avec succès !');
        this.listeAttentes();
      }
    });
  }

  deleteInscription(id: number) {
    if (!confirm('Êtes-vous sûr de vouloir supprimer cette inscription ?')) {
      return;
    }
    this.adminService.deleteInscription(id).subscribe({
      next: () => {
        alert('Inscription supprimée avec succès !');
        this.listeAttentes();
      }
    });
  }

  saveUserByAdmin(user: any) {
    this.adminService.saveUserByAdmin(user).subscribe({
      next: (response) => {
        alert('Utilisateur enregistré avec succès !');
        this.getAdminData();
        this.showActivites();
      }
    });
  }

  deleteUser(id: number) {
    if (!confirm('Êtes-vous sûr de vouloir supprimer cet utilisateur ?')) {
      return;
    }
    this.adminService.deleteUser(id).subscribe({
      next: () => {
        alert('Utilisateur supprimé avec succès !');
        this.listeUsers();
      }
    });
  }

  listeUsers(keyword?: string, roleFilter?: string) {
    this.adminService.listeUsers(keyword, roleFilter).subscribe({
      next: (data: any) => {
        console.log('Liste des utilisateurs :', data);
      }
    });
  }

  resetPassword(userId: number, newPassword: string) {
    this.adminService.resetPassword(userId, newPassword).subscribe({
      next: () => {
        alert('Mot de passe réinitialisé avec succès !');
      }
    });
  }

  emailPage() {
    this.adminService.emailPage().subscribe({
      next: (data: any) => {
        console.log(data);
      }
    });
  }

  sendEmail(payload: any) {
    if (!payload.subject || !payload.message) {
      alert('Veuillez remplir tous les champs avant de réesayer.');
      return;
    }
    this.adminService.sendEmail(payload).subscribe({
      next: (response) => {
        alert('Email envoyé avec succès !');
      },
      error: (error) => {
        alert('Erreur lors de l\'envoi de l\'email : ' + error.message);
      },
    });
  }


}
