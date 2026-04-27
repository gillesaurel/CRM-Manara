import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ParentService } from '../../../services/parent-service';

interface ParentProfil {
  prenom: string;
  nom: string;
  email: string;
  telephone?: string;
}

@Component({
  selector: 'app-profil',
  standalone: false,
  templateUrl: './profil.html',
  styleUrls: ['./profil.css']
})
export class ProfilComponent implements OnInit {
  
  parent: ParentProfil | null = null;
  successMessage: string = '';
  errorMessage: string = '';
  isLoading: boolean = true;

  constructor(
    private parentService: ParentService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.chargerProfil();
  }

  chargerProfil(): void {
    // Utiliser les données du localStorage
    const user = localStorage.getItem('user');
    console.log('User from localStorage:', user);
    
    if (user) {
      try {
        const userData = JSON.parse(user);
        this.parent = {
          prenom: userData.prenom || '',
          nom: userData.nom || '',
          email: userData.email || '',
          telephone: userData.telephone || ''
        };
        console.log('Parent chargé depuis localStorage:', this.parent);
      } catch (e) {
        console.error('Erreur parsing:', e);
        this.errorMessage = 'Erreur lors du chargement du profil';
      }
    } else {
      this.errorMessage = 'Utilisateur non connecté';
    }
    
    this.isLoading = false;
  }

  onSubmit(): void {
    // Sauvegarder les modifications dans localStorage
    if (this.parent) {
      const user = localStorage.getItem('user');
      if (user) {
        const userData = JSON.parse(user);
        userData.prenom = this.parent.prenom;
        userData.nom = this.parent.nom;
        userData.telephone = this.parent.telephone;
        localStorage.setItem('user', JSON.stringify(userData));
        
        this.successMessage = 'Profil mis à jour avec succès !';
        setTimeout(() => this.successMessage = '', 3000);
      }
    }
    
    // Appel API en arrière-plan (optionnel)
    /*
    const parentId = this.getParentId();
    this.parentService.updateProfil(parentId, this.parent).subscribe(...);
    */
  }

  private getParentId(): number {
    const user = localStorage.getItem('user');
    if (user) {
      return JSON.parse(user).id;
    }
    return 0;
  }
}