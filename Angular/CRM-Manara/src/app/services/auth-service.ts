import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { map, Observable, BehaviorSubject, tap } from 'rxjs';

export interface Utilisateur {
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
  
  // Subject pour partager l'état de l'utilisateur
  private userSubject = new BehaviorSubject<Utilisateur | null>(null);
  user$ = this.userSubject.asObservable();

  constructor(private http: HttpClient) {}

  // Récupère l'utilisateur connecté
  getMe(): Observable<Utilisateur | null> {
    return this.http.get<Utilisateur | null>(`${this.apiUrl}/userConnecte`, {
      withCredentials: true
    }).pipe(
      tap(user => {
        this.userSubject.next(user);
        if (user) {
          localStorage.setItem('user', JSON.stringify(user));
        } else {
          localStorage.removeItem('user');
        }
      })
    );
  }

  // Vérifie si l'utilisateur est connecté
  isLoggedIn(): Observable<boolean> {
    return this.getMe().pipe(
      map(user => !!user)
    );
  }

  // Connexion
  login(data: any): Observable<any> {
    const body = new URLSearchParams();
    body.set('username', data.email);
    body.set('password', data.password);

    return this.http.post(`${this.apiUrl}/login`, body.toString(), {
      headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
      withCredentials: true
    });
  }

  // Déconnexion
  logout(): Observable<any> {
    return this.http.get(`${this.apiUrl}/deconnexion`, {
      withCredentials: true
    }).pipe(
      tap(() => {
        this.userSubject.next(null);
        localStorage.removeItem('user');
      })
    );
  }

  
  // ============================================
  
  updateUser(user: Utilisateur | null): void {
    this.userSubject.next(user);
    if (user) {
      localStorage.setItem('user', JSON.stringify(user));
    } else {
      localStorage.removeItem('user');
    }
  }
}