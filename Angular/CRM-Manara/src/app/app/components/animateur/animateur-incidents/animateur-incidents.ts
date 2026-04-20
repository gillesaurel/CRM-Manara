import { Component, OnInit } from '@angular/core';
import { IncidentService, Incident, Enfant } from '../../../services/incident-service';

@Component({
  selector: 'app-animateur-incidents',
  standalone: false,
  templateUrl: './animateur-incidents.html',
  styleUrl: './animateur-incidents.css',
})
export class AnimateurIncidents implements OnInit {
  enfants: Enfant[] = [];
  incidents: Incident[] = [];

  // Form fields
  enfantId: number | null = null;
  type: string = '';
  gravite: number | null = null;
  description: string = '';
  actionsPrises: string = '';

  message: string = '';
  messageType: 'success' | 'error' = 'success';

  constructor(private incidentService: IncidentService) {}

  ngOnInit(): void {
    this.enfants  = this.incidentService.getEnfants();
    this.incidents = this.incidentService.getIncidents();
  }

  soumettre(): void {
    if (!this.enfantId || !this.type || !this.gravite || !this.description.trim()) {
      this.message = 'Veuillez remplir tous les champs obligatoires.';
      this.messageType = 'error';
      return;
    }

    this.incidentService.ajouterIncident({
      enfantId:     this.enfantId,
      type:         this.type,
      gravite:      this.gravite,
      description:  this.description,
      actionsPrises: this.actionsPrises,
    });

    this.message     = 'Incident signalé avec succès.';
    this.messageType = 'success';
    this.incidents   = this.incidentService.getIncidents();

    // Reset form
    this.enfantId     = null;
    this.type         = '';
    this.gravite      = null;
    this.description  = '';
    this.actionsPrises = '';
  }

  graviteLabel(g: number): string {
    return ['', 'Mineur', 'Léger', 'Modéré', 'Grave', 'Très grave'][g] ?? '';
  }

  graviteColor(g: number): string {
    if (g >= 4) return 'var(--danger)';
    if (g === 3) return 'var(--warning)';
    return 'var(--present)';
  }

  formatDate(iso: string): string {
    return new Date(iso).toLocaleString('fr-CA', {
      day: '2-digit', month: '2-digit', year: 'numeric',
      hour: '2-digit', minute: '2-digit',
    });
  }
}