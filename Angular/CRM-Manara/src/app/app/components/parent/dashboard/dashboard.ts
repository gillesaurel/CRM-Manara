import { Component, OnInit } from '@angular/core';

import { ParentService, Enfant, Inscription } from '../../../services/parent-service';

interface Parent {
  utilisateur: {
    prenom: string;
    nom: string;
    email: string;
  };
}


interface Communication {
  id: number;
  sujet: string;
  message: string;
  canal: string;
  dateEnvoi: string;
  lu: boolean;
}

interface Activite {
  date: string;
  heureDebut: string;
  heureFin: string;
  lieu: string;
  statut: string;
  activite: { nom: string };
}

@Component({
  selector: 'app-dashboard',
  standalone: false,
  templateUrl: './dashboard.html',
  styleUrls: ['./dashboard.css']
})
export class DashboardComponent implements OnInit {
  
  parent: Parent | null = null;
  enfants: Enfant[] = [];
  inscriptions: Inscription[] = [];
  prochainesActivites: Activite[] = [];
  communications: Communication[] = [];
  messagesNonLus: number = 0;

  constructor(private parentService: ParentService) {}

  ngOnInit(): void {
    this.chargerDashboard();
  }

  chargerDashboard(): void {
    const parentId = this.getParentId();
    const email = this.getUserEmail();
    
  if (email) {
  this.parentService.getDashboard(email).subscribe({
    next: (data: any) => {
      this.parent = data.parent || data;
      this.enfants = data.enfants || [];
      this.inscriptions = data.inscriptions || [];
      this.messagesNonLus = data.messagesNonLus || 0;
      this.communications = data.communications || [];
      this.prochainesActivites = data.prochainesActivites || [];
    },
    error: (err) => console.error('Erreur chargement dashboard:', err)
  });
}

    if (parentId) {
      this.parentService.getEnfants(parentId).subscribe({
        next: (data) => this.enfants = data,
        error: (err) => console.error('Erreur chargement enfants:', err)
      });

      this.parentService.getInscriptionsParent(parentId).subscribe({
        next: (data) => this.inscriptions = data,
        error: (err) => console.error('Erreur chargement inscriptions:', err)
      });
    }
  }

  private getParentId(): number {
    const user = localStorage.getItem('user');
    if (user) {
      return JSON.parse(user).id;
    }
    return 0;
  }

  private getUserEmail(): string {
    const user = localStorage.getItem('user');
    if (user) {
      return JSON.parse(user).email;
    }
    return '';
  }
}