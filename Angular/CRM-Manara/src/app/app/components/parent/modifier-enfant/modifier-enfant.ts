import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ParentService, Enfant } from '../../../services/parent-service';

@Component({
  selector: 'app-modifier-enfant',
  standalone: false,
  templateUrl: './modifier-enfant.html',
  styleUrls: ['./modifier-enfant.css']
})
export class ModifierEnfantComponent implements OnInit {
  
  enfant: Enfant = {
    id: 0,
    prenom: '',
    nom: '',
    dateNaissance: '',
    allergies: '',
    notesMedicales: ''
  };
  
  successMessage: string = '';
  errorMessage: string = '';
  isLoading: boolean = true;

  constructor(
    private parentService: ParentService,
    private route: ActivatedRoute,
    private router: Router
  ) {}

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.chargerEnfant(+id);
    } else {
      this.router.navigate(['/parent/enfants']);
    }
  }

  chargerEnfant(enfantId: number): void {
    this.parentService.getEnfantById(enfantId).subscribe({
      next: (data) => {
        this.enfant = data;
        this.isLoading = false;
      },
      error: (err) => {
        console.error('Erreur chargement enfant:', err);
        this.errorMessage = 'Erreur lors du chargement de l\'enfant';
        this.isLoading = false;
      }
    });
  }

  onSubmit(): void {
    if (!this.enfant.id) return;

    this.parentService.modifierEnfant(this.enfant.id, this.enfant).subscribe({
      next: () => {
        this.successMessage = 'Enfant modifié avec succès !';
        setTimeout(() => {
          this.router.navigate(['/parent/enfants']);
        }, 2000);
      },
      error: (err) => {
        this.errorMessage = err.error?.message || 'Erreur lors de la modification';
        console.error('Erreur:', err);
      }
    });
  }
}