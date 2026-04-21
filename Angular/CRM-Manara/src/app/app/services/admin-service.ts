import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class AdminService {
  private apiUrl = 'http://localhost:8080/admin';

  constructor(private http: HttpClient) { }

  getAdminData() {
    return this.http.get(`${this.apiUrl}/administrateur`, {
      withCredentials: true
    });
  }

  showActivites() {
    return this.http.get(`${this.apiUrl}/adminOnly/activites`, {
      withCredentials: true
    });
  }

  createActivite(activite: any) {
    return this.http.post(`${this.apiUrl}/activities/save`, activite, {
      withCredentials: true
    });
  }

  updateActivite(activite: any) {
    return this.http.put(`${this.apiUrl}/activities/update/${activite.id}`, activite, {
      withCredentials: true
    });
  }

  removeActivite(id: number) {
    return this.http.delete(`${this.apiUrl}/activities/delete/${id}`, {
      withCredentials: true
    });
  }

  listeAttentes() {
    return this.http.get(`${this.apiUrl}/adminOnly/listeAttentes`, {
      withCredentials: true
    });
  }

  validerInscription(id: number) {
    return this.http.post(`${this.apiUrl}/inscriptions/valider/${id}`, {}, {
      withCredentials: true
    });
  }

  deleteInscription(id: number) {
    return this.http.delete(`${this.apiUrl}/inscriptions/delete/${id}`, {
      withCredentials: true
    });
  }

  saveUserByAdmin(user: any) {
    if (user.id) {
      return this.http.put(`${this.apiUrl}/save/${user.id}`, user, {
        withCredentials: true
      });
    } else {
      return this.http.post(`${this.apiUrl}/save`, user, {
        withCredentials: true
      });
    }
  }

  deleteUser(id: number) {
    return this.http.delete(`${this.apiUrl}/utilisateur/delete/${id}`, {
      withCredentials: true
    });
  }

  listeUsers(keyword?: string, roleFilter?: string) {
    let params = new HttpParams();

    if (keyword) {
      params = params.set('keyword', keyword);
    }

    if (roleFilter) {
      params = params.set('roleFilter', roleFilter);
    }

    return this.http.get(`${this.apiUrl}/adminOnly/utilisateurs`, {
      params,
      withCredentials: true
    });
  }

  resetPassword(userId: number, newPassword: string) {
    let params = new HttpParams()
      .set('userId', userId)
      .set('password', newPassword);

    return this.http.post(`${this.apiUrl}/reset-password`, {}, {
      params,
      withCredentials: true
    });
  }

  emailPage() {
    return this.http.get(`${this.apiUrl}/adminOnly/email`, {
      withCredentials: true
    });
  }

  sendEmail(payload: any): Observable<{ success: boolean; message: string }> {
    return this.http.post<{ success: boolean; message: string }>(
      `${this.apiUrl}/email/send`,
      payload, { withCredentials: true }
    );
  }

  getActiviteById(id: number) {
    return this.http.get(`${this.apiUrl}/activities/edit/${id}`, {
      withCredentials: true
    });
  }

  getActivites(): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/adminOnly/activites`, {
      withCredentials: true
    });
  }

}
