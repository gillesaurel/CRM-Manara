import { Injectable } from '@angular/core';
import { CanActivate, Router } from '@angular/router';
import { AuthService } from '../services/auth-service';
import { map } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class UserAuth implements CanActivate {

  constructor(private authService: AuthService, private router: Router) {}

  canActivate() {
    return this.authService.isLoggedIn().pipe(
      map(isAuth => {
        if (isAuth) return true;

        this.router.navigate(['/login']);
        return false;
      })
    );
  }
}