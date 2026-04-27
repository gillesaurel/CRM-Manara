import { Injectable } from '@angular/core';
import { CanActivate, Router, UrlTree } from '@angular/router';
import { AuthService } from '../services/auth-service';
import { map, Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class UserAuth implements CanActivate {

  constructor(
    private authService: AuthService,
    private router: Router
  ) {}

  canActivate(): Observable<boolean | UrlTree> {
    return this.authService.isLoggedIn().pipe(
      map(isAuth => {
        if (isAuth) {
          return true;
        }

        // Redirection 
        return this.router.createUrlTree(['/login']);
      })
    );
  }
}