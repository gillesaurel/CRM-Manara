import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { AnimateurService } from '../../../services/animateur-service';
import { Location } from '@angular/common';

@Component({
  selector: 'app-animateur-horaire',
  standalone: false,
  templateUrl: './animateur-horaire.html',
  styleUrl: './animateur-horaire.css',
})
export class AnimateurHoraire implements OnInit {
  sessions: any[] = [];

  constructor(
    private animateurService: AnimateurService,
    private cdr: ChangeDetectorRef,
    private location: Location
  ) {}

  ngOnInit(): void {
    this.loadSessions();
  }

  loadSessions() {
    this.animateurService.getHoraireData().subscribe({
      next: (data: any) => {
        console.log('Raw data:', JSON.stringify(data.sessions[0]));
        this.sessions = data.sessions.map((sess: any) => ({
          id: sess.id,
          date: sess.date,
          heure_debut: sess.heureDebut?.slice(0, 5),
          heure_fin: sess.heureFin?.slice(0, 5),
          lieu: sess.lieu,
          statut: sess.statut,
          activiteNom: sess.activite?.nom ?? 'Activité inconnue',
        }));
        this.cdr.detectChanges();
      },
      error: (error: unknown) => {
        console.error('Erreur lors du chargement des sessions :', error);
      }
    });
  }

  goBack(): void {
    this.location.back();
  }
}
