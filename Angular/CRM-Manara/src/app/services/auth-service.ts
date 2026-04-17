import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { map, Observable } from 'rxjs';

export interface Utilisateur{
  email: string;
  prenom: string;
  nom: string;
  role: {
    nom: string;
  }
}

@Injectable({
  providedIn: 'root',
})


export class AuthService {
  private apiUrl = 'http://localhost:8080';


  constructor(private http: HttpClient) {}

  getMe() {
  return this.http.get<any>(`${this.apiUrl}/userConnecte`, {
    withCredentials: true
  });
}

  isLoggedIn(): Observable<boolean> {
    return this.getMe().pipe(
      map(user => !!user)
    );
  }

  login(data: any): Observable<any> {
  const body = new URLSearchParams();
  body.set('username', data.email);
  body.set('password', data.password);

  return this.http.post(`${this.apiUrl}/login`, body.toString(), {
    headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
    withCredentials: true
  });
}

  logout(): Observable<any> {
    return this.http.get(`${this.apiUrl}/deconnexion`, {
      withCredentials: true
    });
  }

}
