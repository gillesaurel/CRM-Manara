import { Component, OnInit } from '@angular/core';
import sessions   from '../../../mock-data/sessions-parent.json';
import activites  from '../../../mock-data/activites-parent.json';
import inscriptions from '../../../mock-data/inscriptions.json';

interface Session {
  id: number;
  date: string;
  heure_debut: string;
  heure_fin: string;
  lieu: string;
  statut: string;
  activiteNom: string;
  nbInscrits: number;
}

@Component({
  selector: 'app-animateur-horaire',
  standalone: false,
  templateUrl: './animateur-horaire.html',
  styleUrl: './animateur-horaire.css',
})
export class AnimateurHoraire implements OnInit {
  sessions: Session[] = [];

  ngOnInit(): void {
    this.sessions = (sessions as any[]).map(sess => {
      const activite = (activites as any[]).find(a => a.id === sess.activite_id);
      const nbInscrits = (inscriptions as any[]).filter(i => i.session_id === sess.id && !i.en_attente).length;
      return {
        id:          sess.id,
        date:        sess.date,
        heure_debut: sess.heure_debut.slice(0, 5),
        heure_fin:   sess.heure_fin.slice(0, 5),
        lieu:        sess.lieu,
        statut:      sess.statut,
        activiteNom: activite?.nom ?? `Activité ${sess.activite_id}`,
        nbInscrits,
      };
    });
  }
}