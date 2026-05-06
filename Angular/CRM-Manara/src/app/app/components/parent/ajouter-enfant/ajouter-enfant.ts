import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { ParentService } from '../../../services/parent-service';

interface EnfantForm {
  prenom: string;
  nom: string;
  dateNaissance: string;
  allergies: string;
  notesMedicales: string;
}

@Component({
  selector: 'app-ajouter-enfant',
  standalone: false,
  templateUrl: './ajouter-enfant.html',
  styleUrls: ['./ajouter-enfant.css']
})
export class AjouterEnfantComponent {
  
  enfant: EnfantForm = {
    prenom: '',
    nom: '',
    dateNaissance: '',
    allergies: '',
    notesMedicales: ''
  };

  successMessage: string = '';
  errorMessage: string = '';
  ageError: string = '';  //Message d'erreur pour l'âge

  constructor(
    private parentService: ParentService,
    private router: Router
  ) {}

  /**
   * Calcule l'âge à partir d'une date de naissance
   * @param dateNaissance - La date de naissance au format YYYY-MM-DD
   * @returns L'âge en années (nombre entier)
   */
  calculerAge(dateNaissance: string): number {
    const aujourdhui = new Date();
    const naissance = new Date(dateNaissance);
    let age = aujourdhui.getFullYear() - naissance.getFullYear();
    const mois = aujourdhui.getMonth() - naissance.getMonth();
    // Si l'anniversaire n'est pas encore passé cette année, on enlève 1 an
    if (mois < 0 || (mois === 0 && aujourdhui.getDate() < naissance.getDate())) {
      age--;
    }
    return age;
  }

  onSubmit(): void {
    // ============================================
    // VÉRIFICATION DE L'ÂGE (3 ANS MINIMUM)
    // ============================================
    if (this.enfant.dateNaissance) {
      const age = this.calculerAge(this.enfant.dateNaissance);
      if (age < 3) {
        this.ageError = 'L\'enfant doit avoir au moins 3 ans pour être inscrit.';
        return;  // On arrête l'envoi du formulaire
      } else {
        this.ageError = '';  // Efface le message d'erreur si l'âge est valide
      }
    }

    // ============================================
    // FIN DE LA VÉRIFICATION
    // ============================================

    // Récupérer l'ID du parent connecté
    const parentId = this.getParentId();
    
    this.parentService.ajouterEnfant(parentId, this.enfant).subscribe({
      next: () => {
        this.successMessage = 'Enfant ajouté avec succès !';
        setTimeout(() => {
          // Force le rechargement complet de la page
          window.location.href = '/parent/enfants';
        }, 2000);
      },
      error: (err) => {
        this.errorMessage = err.error?.message || 'Erreur lors de l\'ajout de l\'enfant';
        console.error('Erreur:', err);
      }
    });
  }

  private getParentId(): number {
    const user = localStorage.getItem('user');
    if (user) {
      return JSON.parse(user).id;
    }
    return 0; // Valeur par défaut
  }
}