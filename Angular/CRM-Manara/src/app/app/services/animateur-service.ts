import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class AnimateurService {
  private apiUrl = 'http://localhost:8080/animateur';

  constructor(private http: HttpClient) {}

  getInscriptionData(): Observable<any> {
    return this.http.get(`${this.apiUrl}/inscriptions`, { withCredentials: true });
  }

  getPresenceData(): Observable<any> {
    return this.http.get(`${this.apiUrl}/presences`, { withCredentials: true });
  }

  getHoraireData(): Observable<any> {
    return this.http.get(`${this.apiUrl}/horaire`, { withCredentials: true });
  }

  getIncidentData(): Observable<any> {
    return this.http.get(`${this.apiUrl}/incidents`, { withCredentials: true });
  }

  saveIncident(payload: any): Observable<any> {
    const params = new HttpParams()
      .set('enfantId', payload.enfantId)
      .set('type', payload.type)
      .set('gravite', payload.gravite)
      .set('description', payload.description)
      .set('actionsPrises', payload.actionsPrises ?? '');

    return this.http.post(`${this.apiUrl}/incidents/save`, params, { withCredentials: true });
  }
}
