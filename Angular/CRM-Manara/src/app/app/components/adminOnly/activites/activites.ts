import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { AdminService } from '../../../services/admin-service';
import { Router } from '@angular/router';

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
    private cdr: ChangeDetectorRef,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.loadActivites();
  }

  loadActivites() {
    this.adminService.showActivites().subscribe({
      next: (data: any) => {
        this.activites = data.activites;
        this.cdr.detectChanges();
      },
      error: (error: unknown) => {
        console.error('Erreur lors de la récupération des activités :', error);
      }
    });
  }

  editActivite(id: number) {
    this.router.navigate(['/adminOnly/ajouter-activite', id]);
  }

  deleteActivite(id: number) {
    if (!confirm('Êtes-vous sûr de vouloir supprimer cette activité ?')) return;
    this.adminService.removeActivite(id).subscribe({
      next: () => {
        alert('Activité supprimée avec succès !');
        this.loadActivites();
      }
    });
  }

  validerInscription(id: number) {
    if (!confirm('Êtes-vous sûr de vouloir valider cette inscription ?')) return;
    this.adminService.validerInscription(id).subscribe({
      next: () => {
        alert('Inscription validée avec succès !');
        this.loadActivites();
      }
    });
  }

  deleteInscription(id: number) {
    if (!confirm('Êtes-vous sûr de vouloir supprimer cette inscription ?')) return;
    this.adminService.deleteInscription(id).subscribe({
      next: () => {
        alert('Inscription supprimée avec succès !');
        this.loadActivites();
      }
    });
  }
}