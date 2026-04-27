import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

// Interfaces pour typer les données
export interface Activite {
  id: number;
  nom: string;
  description: string;
  ageMin: number;
  ageMax: number;
  imageUrl?: string;
  sessions?: Session[];
}

export interface Session {
  id: number;
  date: string;
  heureDebut: string;
  heureFin: string;
  lieu: string;
  capaciteMax: number;
  nbInscrits: number;
  activite?: Activite;
}

export interface Enfant {
  id: number;
  nom: string;
  prenom: string;
  dateNaissance: string;
  allergies?: string;
  notesMedicales?: string;
  inscriptions?: any[];  
  presences?: any[];
}

export interface Inscription {
  id: number;
  enfant: Enfant;
  session: Session;
  statut: string;
  dateInscription: string;
}

@Injectable({
  providedIn: 'root',
})
export class ParentService {
  
  private apiUrl = 'http://localhost:8080/parent/api';

  constructor(private http: HttpClient) {}

  // ========== DASHBOARD ==========
  
  getDashboard(email: string): Observable<any> {
    return this.http.get(`${this.apiUrl}/dashboard/${email}`, {
      withCredentials: true
    });
  }

  // ========== ACTIVITÉS ==========
  
  getActivites(): Observable<Activite[]> {
    return this.http.get<Activite[]>(`${this.apiUrl}/activites`, {
      withCredentials: true
    });
  }

  getSessionsByActivite(activiteId: number): Observable<Session[]> {
    return this.http.get<Session[]>(`${this.apiUrl}/activites/${activiteId}/sessions`, {
      withCredentials: true
    });
  }

  // ========== ENFANTS ==========
  
  getEnfants(parentId: number): Observable<Enfant[]> {
    return this.http.get<Enfant[]>(`${this.apiUrl}/${parentId}/enfants`, {
      withCredentials: true
    });
  }

  getEnfantById(enfantId: number): Observable<Enfant> {
    return this.http.get<Enfant>(`${this.apiUrl}/enfants/${enfantId}`, {
      withCredentials: true
    });
  }

  ajouterEnfant(parentId: number, enfant: any): Observable<Enfant> {
    return this.http.post<Enfant>(`${this.apiUrl}/${parentId}/enfants`, enfant, {
      withCredentials: true
    });
  }

  modifierEnfant(enfantId: number, enfant: any): Observable<Enfant> {
    return this.http.put<Enfant>(`${this.apiUrl}/enfants/${enfantId}`, enfant, {
      withCredentials: true
    });
  }

  supprimerEnfant(enfantId: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/enfants/${enfantId}`, {
      withCredentials: true
    });
  }

  // ========== INSCRIPTIONS ==========
  
  inscrireEnfant(enfantId: number, sessionId: number): Observable<Inscription> {
    return this.http.post<Inscription>(`${this.apiUrl}/inscriptions`, { enfantId, sessionId }, {
      withCredentials: true
    });
  }

  getInscriptionsParent(parentId: number): Observable<Inscription[]> {
    return this.http.get<Inscription[]>(`${this.apiUrl}/${parentId}/inscriptions`, {
      withCredentials: true
    });
  }

  annulerInscription(inscriptionId: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/inscriptions/${inscriptionId}`, {
      withCredentials: true
    });
  }

  // ========== PLANNING ==========
  
  getPlanning(parentId: number): Observable<Session[]> {
    return this.http.get<Session[]>(`${this.apiUrl}/${parentId}/planning`, {
      withCredentials: true
    });
  }

  // ========== PROFIL ==========
  
  getProfil(email: string): Observable<any> {
    return this.http.get(`${this.apiUrl}/profil/${email}`, {
      withCredentials: true
    });
  }

  updateProfil(parentId: number, parentData: any): Observable<any> {
    return this.http.put(`${this.apiUrl}/profil/${parentId}`, parentData, {
      withCredentials: true
    });
  }
}