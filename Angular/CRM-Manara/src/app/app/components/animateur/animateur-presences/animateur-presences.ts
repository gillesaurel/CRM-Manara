import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { AnimateurService } from '../../../services/animateur-service';

@Component({
  selector: 'app-animateur-presences',
  standalone: false,
  templateUrl: './animateur-presences.html',
  styleUrl: './animateur-presences.css',
})
export class AnimateurPresences implements OnInit {
  groups: { activiteNom: string; sessions: { sessionLabel: string; enfants: any[] }[] }[] = [];

  constructor(
    private animateurService: AnimateurService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.loadPresences();
  }

  loadPresences() {
    this.animateurService.getPresenceData().subscribe({
      next: (data: any) => {
        const raw = data.presences;
        this.groups = Object.entries(raw).map(([activiteNom, sessions]) => ({
          activiteNom,
          sessions: sessions as { sessionLabel: string; enfants: any[] }[]
        }));
        console.log('Présences chargées :', this.groups);
        this.cdr.detectChanges();
      },
      error: (error: unknown) => {
        console.error('Erreur lors de la récupération des présences :', error);
      }
    });
  }
}
