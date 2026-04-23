import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ParentService } from '../../../services/parent-service';

interface ParentProfil {
  prenom: string;
  nom: string;
  email: string;
  telephone?: string;
}

interface PasswordData {
  ancienMotDePasse: string;
  nouveauMotDePasse: string;
  confirmMotDePasse: string;
}

@Component({
  selector: 'app-profil',
  standalone: false,
  templateUrl: './profil.html',
  styleUrls: ['./profil.css']
})
export class ProfilComponent implements OnInit {
  
  parent: ParentProfil = {
    prenom: '',
    nom: '',
    email: '',
    telephone: ''
  };
  
  passwordData: PasswordData = {
    ancienMotDePasse: '',
    nouveauMotDePasse: '',
    confirmMotDePasse: ''
  };
  
  successMessage: string = '';
  errorMessage: string = '';
  passwordMismatch: boolean = false;
  isLoading: boolean = true;

  constructor(
    private parentService: ParentService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.chargerProfil();
  }

  chargerProfil(): void {
    const email = this.getUserEmail();
    this.parentService.getProfil(email).subscribe({
      next: (data: any) => {
        this.parent = {
          prenom: data.utilisateur?.prenom || data.prenom,
          nom: data.utilisateur?.nom || data.nom,
          email: data.utilisateur?.email || data.email,
          telephone: data.utilisateur?.telephone || data.telephone
        };
        this.isLoading = false;
      },
      error: (err) => {
        console.error('Erreur chargement profil:', err);
        this.errorMessage = 'Erreur lors du chargement du profil';
        this.isLoading = false;
      }
    });
  }

  onSubmit(): void {
    const parentId = this.getParentId();
    this.parentService.updateProfil(parentId, this.parent).subscribe({
      next: () => {
        this.successMessage = 'Profil mis à jour avec succès !';
        setTimeout(() => this.successMessage = '', 3000);
      },
      error: (err) => {
        this.errorMessage = err.error?.message || 'Erreur lors de la mise à jour';
        console.error('Erreur:', err);
        setTimeout(() => this.errorMessage = '', 3000);
      }
    });
  }


  private getParentId(): number {
    const user = localStorage.getItem('user');
    if (user) {
      return JSON.parse(user).id;
    }
    return 0;
  }

  private getUserEmail(): string {
    const user = localStorage.getItem('user');
    if (user) {
      return JSON.parse(user).email;
    }
    return '';
  }
}