import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable, tap, switchMap, map, of } from 'rxjs';

export interface Utilisateur {
  email: string;
  prenom: string;
  nom: string;
  role: {
    nom: string;
  };
}

@Injectable({
  providedIn: 'root',
})
export class AuthService {

  private apiUrl = 'http://localhost:8080';
  private userSubject = new BehaviorSubject<Utilisateur | null>(null);
  user$ = this.userSubject.asObservable();

  constructor(private http: HttpClient) {
    this.loadUser();
  }

  private loadUser(): void {
    // 1. D'abord, lire depuis localStorage
    const storedUser = localStorage.getItem('user');
    if (storedUser) {
      try {
        const user = JSON.parse(storedUser);
        this.userSubject.next(user);
      } catch (e) {
        console.error('Erreur parsing user:', e);
      }
    }

    // 2. Ensuite, vérifier avec le serveur
    this.getMe().subscribe({
      next: (user) => {
        if (user) {
          this.userSubject.next(user);
          localStorage.setItem('user', JSON.stringify(user));
        }
      },
      error: () => {
        // Ne rien faire
      }
    });
  }

  getMe(): Observable<Utilisateur | null> {
    return this.http.get<Utilisateur | null>(`${this.apiUrl}/userConnecte`, {
      withCredentials: true
    }).pipe(
      map(user => user ?? null)
    );
  }

  login(data: any): Observable<Utilisateur | null> {
    const body = new URLSearchParams();
    body.set('username', data.email);
    body.set('password', data.password);

    return this.http.post(`${this.apiUrl}/login`, body.toString(), {
      headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
      withCredentials: true
    }).pipe(
      switchMap(() => this.getMe()),
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

  logout(): Observable<any> {
    return this.http.get(`${this.apiUrl}/logout`, {
      withCredentials: true
    }).pipe(
      tap(() => {
        this.userSubject.next(null);
        localStorage.removeItem('user');
      })
    );
  }

  isLoggedIn(): Observable<boolean> {
    return this.user$.pipe(
      map(user => !!user)
    );
  }

  updateUser(user: Utilisateur | null): void {
    this.userSubject.next(user);
  }

  get currentUser(): Utilisateur | null {
    return this.userSubject.value;
  }

  register(userData: any): Observable<any> {
    return this.http.post(`${this.apiUrl}/register/save`, userData);
  }

  forgotPassword(email: string) {
    return this.http.post(`${this.apiUrl}/auth/forgot-password`, { email },
      { withCredentials: true, responseType: 'json' });
  }

  resetPassword(token: string, nouveauMotDePasse: string) {
    return this.http.post(`${this.apiUrl}/auth/reset-password`, { token, nouveauMotDePasse });
  }
}