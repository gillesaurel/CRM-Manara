import { Component, OnInit } from '@angular/core';
import { AdminService } from '../../../services/admin-service';

@Component({
  selector: 'app-liste-attente',
  standalone: false,
  templateUrl: './liste-attente.html',
  styleUrl: './liste-attente.css',
})
export class ListeAttente implements OnInit {

  inscriptions: any[] = [];
  stats: any = {};

  constructor(private adminService: AdminService) { }

  ngOnInit(): void {
    this.loadListeAttente();
  }

  loadListeAttente() {
    this.adminService.listeAttentes().subscribe({
      next: (data: any) => {
        this.inscriptions = data.inscriptions;
        this.stats = data.stats;
      },
      error: (error) => {
        console.error('Erreur lors de la récupération de la liste d\'attente :', error);
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
        this.loadListeAttente();
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
        this.loadListeAttente();
      },
      error: (error) => {
        console.error('Erreur lors de la suppression de l\'inscription :', error);
      }
    });
  }

  getInscriptionByActivite(nomActivite: string) {
    return this.inscriptions.filter((inscription) => inscription.activite.nom === nomActivite);
  }

  getActivites() {
    return Object.keys(this.stats);
  }

}

