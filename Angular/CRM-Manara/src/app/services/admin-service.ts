import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class AdminService {
  private apiUrl = 'http://localhost:8080/api/admin';

  constructor(private http: HttpClient) {}

  getAdminData() {
    return this.http.get(`${this.apiUrl}/administrateur`);
  }

  showActivites() {
    return this.http.get(`${this.apiUrl}/adminOnly/activites`);
  }

  saveOrUpdateActivite(activite: any) {
    if (activite.id) {
      return this.http.put(`${this.apiUrl}/activites/save/${activite.id}`, activite);
    } else {
      return this.http.post(`${this.apiUrl}/activites/save`, activite);
    }
  }

  modifyActivite(activite: any) {
    return this.http.put(`${this.apiUrl}/activites/edit/${activite.id}`, activite);
  }

  removeActivite(id: number) {
    return this.http.delete(`${this.apiUrl}/activites/delete/${id}`);
  }

  listeAttentes() {
    return this.http.get(`${this.apiUrl}/adminOnly/listeAttentes`);
  }

  validerInscription(id: number) {
    return this.http.post(`${this.apiUrl}/inscriptions/valider/${id}`, {});
  }

  deleteInscription(id: number) {
    return this.http.delete(`${this.apiUrl}/inscriptions/delete/${id}`);
  }

  saveUserByAdmin(user: any) {
    if (user.id) {
      return this.http.put(`${this.apiUrl}/save${user.id}`, user);
    } else {
      return this.http.post(`${this.apiUrl}/save`, user);
    }
  }

  deleteUser(id: number) {
    return this.http.delete(`${this.apiUrl}/utilisateur/delete/${id}`);
  }

  listeUsers(keyword?: string, roleFilter?: string) {
    let params = new HttpParams();
    if (keyword) {
      params = params.set('keyword', keyword);
    }
    if (roleFilter) {
      params = params.set('role', roleFilter);
    }
    return this.http.get(`${this.apiUrl}/adminOnly/listeUsers`, { params });
  }

  resetPassword(userId: number, newPassword: string) {
    let params = new HttpParams().set('newPassword', newPassword);
    return this.http.post(`${this.apiUrl}/reset-password`, {}, { params });
  }

  emailPage() {
    return this.http.get(`${this.apiUrl}/adminOnly/email`);
  }

  sendEmail(payload: any): Observable<any> {
    return this.http.post(`${this.apiUrl}/email/send`, payload);
  }

}
