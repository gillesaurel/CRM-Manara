import { Injectable } from '@angular/core';
import enfants from '../mock-data/enfants.json';

export interface Enfant {
  id: number;
  nom: string;
  prenom: string;
  notes_medicales: string | null;
  parent_id: number;
}

export interface Incident {
  id: number;
  enfantId: number;
  enfantNom: string;
  type: string;
  gravite: number;
  description: string;
  actionsPrises: string;
  dateHeure: string;
  statut: string;
}

@Injectable({ providedIn: 'root' })
export class IncidentService {
  private incidents: Incident[] = [];
  private nextId = 1;

  getEnfants(): Enfant[] {
    return enfants as Enfant[];
  }

  getIncidents(): Incident[] {
    return this.incidents;
  }

  ajouterIncident(incident: Omit<Incident, 'id' | 'dateHeure' | 'statut' | 'enfantNom'>): void {
    const enfant = (enfants as Enfant[]).find(e => e.id === incident.enfantId);
    this.incidents.unshift({
      ...incident,
      id: this.nextId++,
      enfantNom: enfant ? `${enfant.prenom} ${enfant.nom}` : 'Enfant inconnu',
      dateHeure: new Date().toISOString(),
      statut: 'EN_COURS',
    });
  }
}