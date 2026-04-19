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

  saveOrUpdateActivite(activite: any) {
  return this.http.post(`${this.apiUrl}/activites/save`, activite, {
    withCredentials: true
  });
}

  modifyActivite(id: number) {
    return this.http.get(`${this.apiUrl}/activites/edit/${id}`, {
      withCredentials: true
    });
  }

  removeActivite(id: number) {
  return this.http.delete(`${this.apiUrl}/activites/delete/${id}`, {
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
      params = params.set('role', roleFilter);
    }

    return this.http.get(`${this.apiUrl}/adminOnly/utilisateurs`, {
      params,
      withCredentials: true
    });
  }

  resetPassword(userId: number, newPassword: string) {
    let params = new HttpParams()
      .set('userId', userId)
      .set('newPassword', newPassword);

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

  sendEmail(payload: any): Observable<any> {
    return this.http.post(`${this.apiUrl}/email/send`, payload, {
      withCredentials: true
    });
  }

}
