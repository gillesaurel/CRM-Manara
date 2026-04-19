import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { AdminService } from '../../../../services/admin-service';

@Component({
  selector: 'app-activites',
  standalone: false,
  templateUrl: './activites.html',
  styleUrl: './activites.css',
})
export class Activites implements OnInit {

  activites: any[] = [];

  constructor(
    private adminService: AdminService,
    private cdr: ChangeDetectorRef) { }

  ngOnInit(): void {
    this.loadActivites();
  }

  loadActivites() {
    this.adminService.showActivites().subscribe({
      next: (data: any) => {
        this.activites = data.activites;
        console.log('Activités trouvées :', this.activites);
        this.cdr.detectChanges();
      },
      error: (error) => {
        console.error('Erreur lors de la récupération des activités :', error);
      }
    });
  }

  deleteActivite(id: number) {
    if (!confirm('Êtes-vous sûr de vouloir supprimer cette activité ?')) {
      return;
    }
    this.adminService.removeActivite(id).subscribe({
      next: () => {
        alert('Activité supprimée avec succès !');
        this.loadActivites();
      },
      error: (error) => {
        console.error('Erreur lors de la suppression de l\'activité :', error);
      }
    });
  }

  validerInscription(id: number) {
    if (!confirm('Êtes-vous sûr de vouloir valider cette inscription ?')) {
      return;
    }
    this.adminService.validerInscription(id).subscribe({
      next: () => {
        alert('Inscription validée avec succès !');
        this.loadActivites();
      },
      error: (error) => {
        console.error('Erreur lors de la validation de l\'inscription :', error);
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
        this.loadActivites();
      },
      error: (error) => {
        console.error('Erreur lors de la suppression de l\'inscription :', error);
      }
    });
  }
}
