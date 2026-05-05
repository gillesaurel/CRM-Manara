import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ParentService, Enfant } from '../../../services/parent-service';

@Component({
  selector: 'app-enfants-list',
  standalone: false,
  templateUrl: './enfants-list.html',
  styleUrls: ['./enfants-list.css']
})
export class EnfantsListComponent implements OnInit {
  
  enfants: Enfant[] = [];
  successMessage: string = '';
  errorMessage: string = '';

  constructor(
    private parentService: ParentService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.chargerEnfants();
  }

  chargerEnfants(): void {
    const parentId = this.getParentId();
    this.parentService.getEnfants(parentId).subscribe({
      next: (data) => {
        this.enfants = data;
      },
      error: (err) => {
        console.error('Erreur chargement enfants:', err);
        this.errorMessage = 'Erreur lors du chargement des enfants';
      }
    });
  }

  supprimerEnfant(enfantId: number): void {
    if (confirm('Êtes-vous sûr de vouloir supprimer cet enfant ?')) {
      this.parentService.supprimerEnfant(enfantId).subscribe({
        next: () => {
          this.successMessage = 'Enfant supprimé avec succès !';
          this.chargerEnfants();
          setTimeout(() => this.successMessage = '', 3000);
        },
        error: (err) => {
          console.error('Erreur suppression:', err);
          this.errorMessage = err.error?.message || 'Erreur lors de la suppression';
          setTimeout(() => this.errorMessage = '', 3000);
        }
      });
    }
  }

  private getParentId(): number {
    const user = localStorage.getItem('user');
    if (user) {
      return JSON.parse(user).id;
    }
    return 0;
  }
}