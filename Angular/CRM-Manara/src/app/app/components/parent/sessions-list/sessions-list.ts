import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ParentService, Activite, Session, Enfant } from '../../../services/parent-service';

@Component({
  selector: 'app-sessions-list',
  standalone: false,
  templateUrl: './sessions-list.html',
  styleUrls: ['./sessions-list.css']
})
export class SessionsListComponent implements OnInit {
  
  activite: Activite | null = null;
  sessions: Session[] = [];
  enfants: Enfant[] = [];
  selectedEnfantIds: { [key: number]: number } = {};
  successMessage: string = '';
  errorMessage: string = '';

  constructor(
    private parentService: ParentService,
    private route: ActivatedRoute,
    private router: Router
  ) {}

  ngOnInit(): void {
    const activiteId = this.route.snapshot.paramMap.get('id');
    if (activiteId) {
      this.chargerActivite(+activiteId);
      this.chargerSessions(+activiteId);
      this.chargerEnfants();
    }
  }

  chargerActivite(activiteId: number): void {
    // Récupérer l'activité depuis la liste ou via un appel API
    this.parentService.getActivites().subscribe({
      next: (data) => {
        this.activite = data.find(a => a.id === activiteId) || null;
      },
      error: (err) => console.error('Erreur chargement activité:', err)
    });
  }

  chargerSessions(activiteId: number): void {
    this.parentService.getSessionsByActivite(activiteId).subscribe({
      next: (data) => {
        this.sessions = data;
      },
      error: (err) => console.error('Erreur chargement sessions:', err)
    });
  }

  chargerEnfants(): void {
    const parentId = this.getParentId();
    this.parentService.getEnfants(parentId).subscribe({
      next: (data) => {
        this.enfants = data;
      },
      error: (err) => console.error('Erreur chargement enfants:', err)
    });
  }

  inscrireEnfant(sessionId: number): void {
    const enfantId = this.selectedEnfantIds[sessionId];
    
    if (!enfantId) {
      this.errorMessage = 'Veuillez sélectionner un enfant';
      setTimeout(() => this.errorMessage = '', 3000);
      return;
    }

    this.parentService.inscrireEnfant(enfantId, sessionId).subscribe({
      next: () => {
        this.successMessage = 'Inscription réussie !';
        setTimeout(() => {
          this.successMessage = '';
          this.router.navigate(['/parent/inscriptions']);
        }, 2000);
      },
      error: (err) => {
        this.errorMessage = err.error?.message || 'Erreur lors de l\'inscription';
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
}