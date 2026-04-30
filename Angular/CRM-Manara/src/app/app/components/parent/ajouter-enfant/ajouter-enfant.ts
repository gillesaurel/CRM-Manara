
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

  constructor(
    private parentService: ParentService,
    private router: Router
  ) {}

  onSubmit(): void {
    // Récupérer l'ID du parent connecté (à adapter selon votre système d'auth)
    const parentId = this.getParentId();
    
    this.parentService.ajouterEnfant(parentId, this.enfant).subscribe({
      next: () => {
        this.successMessage = 'Enfant ajouté avec succès !';
        setTimeout(() => {
          //this.router.navigate(['/parent/enfants']);
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