import { Component, OnInit } from '@angular/core';
import { AdminService } from '../../../services/admin-service';

@Component({
  selector: 'app-liste-attente',
  standalone: false,
  templateUrl: './liste-attente.html',
  styleUrl: './liste-attente.css',
})
export class ListeAttente implements OnInit {
  activites: string[] = [];
  inscriptions: any[] = [];
  groupedInscriptions: Record<string, any[]> = {};
  stats: Record<string, number> = {};

  constructor(private adminService: AdminService) { }

  ngOnInit(): void {
    this.initData();
  }

  initData() {
    this.adminService.getActivites().subscribe({
      next: (data: any) => {

        let activitesArray: any[] = [];

        if (Array.isArray(data)) activitesArray = data;
        else if (Array.isArray(data?.activites)) activitesArray = data.activites;
        else if (Array.isArray(data?.data)) activitesArray = data.data;

        this.activites = [...new Set(
          activitesArray.map(a => a.nom?.trim())
        )];

        this.loadListeAttente();
      },
      error: (err: unknown) => console.error('Erreur activites', err)
    });
  }

  loadListeAttente() {
    this.adminService.listeAttentes().subscribe({
      next: (data: any) => {

        this.inscriptions = data?.inscriptions || [];

        this.groupedInscriptions = {};
        this.stats = {};

        this.activites.forEach(act => {
          this.groupedInscriptions[act] = [];
          this.stats[act] = 0;
        });

        this.inscriptions.forEach(ins => {

          const act = ins.activite?.nom?.trim();
          if (!act) return;

          if (!this.groupedInscriptions[act]) {
            this.groupedInscriptions[act] = [];
            this.activites.push(act);
          }

          this.groupedInscriptions[act].push(ins);
        });

        this.activites.forEach(act => {
          this.stats[act] = this.groupedInscriptions[act]?.length || 0;
        });

      },
      error: (err: unknown) => console.error('Erreur inscriptions', err)
    });
  }

  validerInscription(id: number) {
    if (!confirm('Êtes-vous sûr de vouloir valider cette inscription ?')) {
      return;
    }
    this.adminService.validerInscription(id).subscribe({
      next: () => {
        alert('Inscription validée avec succès !');
        this.loadListeAttente();
      },
      error: (error: unknown) => {
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
        this.loadListeAttente();
      },
      error: (error: unknown) => {
        console.error('Erreur lors de la suppression de l\'inscription :', error);
      }
    });
  }

}

