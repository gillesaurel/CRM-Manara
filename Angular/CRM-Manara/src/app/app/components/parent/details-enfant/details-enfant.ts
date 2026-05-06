import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ParentService, Enfant, Inscription } from '../../../services/parent-service';

interface Presence {
  id: number;
  dateSaisie: string;
  present: boolean;
  session: {
    activite: { nom: string };
    date: string;
    heureDebut: string;
  };
}

@Component({
  selector: 'app-details-enfant',
  standalone: false,
  templateUrl: './details-enfant.html',
  styleUrls: ['./details-enfant.css']
})
export class DetailsEnfantComponent implements OnInit {
  
  enfant: Enfant | null = null;
  inscriptions: Inscription[] = [];
  presences: Presence[] = [];
  isLoading: boolean = true;

  constructor(
    private parentService: ParentService,
    private route: ActivatedRoute,
    private router: Router,
    private cdr: ChangeDetectorRef  // ← AJOUTER
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
    this.isLoading = true;
    
    this.parentService.getEnfantById(enfantId).subscribe({
      next: (data) => {
        this.enfant = data;
        this.chargerInscriptions(enfantId);
        this.chargerPresences(enfantId);
        this.isLoading = false;
        this.cdr.detectChanges();  // ← AJOUTER
      },
      error: (err) => {
        console.error('Erreur chargement enfant:', err);
        this.isLoading = false;
        this.cdr.detectChanges();  // ← AJOUTER
        this.router.navigate(['/parent/enfants']);
      }
    });
  }

  chargerInscriptions(enfantId: number): void {
    const parentId = this.getParentId();
    this.parentService.getInscriptionsParent(parentId).subscribe({
      next: (data: Inscription[]) => {
        this.inscriptions = data.filter(ins => ins.enfant?.id === enfantId);
        this.cdr.detectChanges();  // ← AJOUTER
      },
      error: (err) => console.error('Erreur chargement inscriptions:', err)
    });
  }

  chargerPresences(enfantId: number): void {
    this.presences = [];
    this.cdr.detectChanges();  // ← AJOUTER
  }

  private getParentId(): number {
    const user = localStorage.getItem('user');
    if (user) {
      return JSON.parse(user).id;
    }
    return 0;
  }
}