import { Injectable } from '@angular/core';

import inscriptions from '../mock-data/inscriptions.json';
import enfants      from '../mock-data/enfants.json';
import sessions     from '../mock-data/sessions-parent.json';
import activites    from '../mock-data/activites-parent.json';

export interface EnfantPresence {
  nom: string;
  prenom: string;
  allergies: string | null;
}

export interface SessionGroup {
  sessionLabel: string;
  enfants: EnfantPresence[];
}

export interface ActiviteGroup {
  activiteNom: string;
  sessions: SessionGroup[];
}

@Injectable({ providedIn: 'root' })
export class PresenceService {

  getPresencesParActivite(): ActiviteGroup[] {
    // Only confirmed (not en_attente) inscriptions
    const confirmed = (inscriptions as any[]).filter(i => !i.en_attente);

    // Map: activiteNom → Map<sessionLabel, EnfantPresence[]>
    const activiteMap = new Map<string, Map<string, EnfantPresence[]>>();

    for (const insc of confirmed) {
      const activite = (activites as any[]).find(a => a.id === insc.activites_id);
      const session  = (sessions  as any[]).find(s => s.id === insc.session_id);
      const enfant   = (enfants   as any[]).find(e => e.id === insc.enfant_id);

      const activiteNom  = activite?.nom ?? `Activité ${insc.activites_id}`;
      const sessionLabel = session
        ? `${session.date} — ${session.heure_debut.slice(0, 5)} à ${session.heure_fin.slice(0, 5)} — ${session.lieu}`
        : `Session ${insc.session_id}`;

      if (!activiteMap.has(activiteNom)) activiteMap.set(activiteNom, new Map());
      const sessionMap = activiteMap.get(activiteNom)!;

      if (!sessionMap.has(sessionLabel)) sessionMap.set(sessionLabel, []);
      sessionMap.get(sessionLabel)!.push({
        nom:      enfant?.nom    ?? insc.nom_enfant,
        prenom:   enfant?.prenom ?? '',
        allergies: enfant?.notes_medicales ?? null,
      });
    }

    return Array.from(activiteMap.entries()).map(([activiteNom, sessionMap]) => ({
      activiteNom,
      sessions: Array.from(sessionMap.entries()).map(([sessionLabel, enf]) => ({
        sessionLabel,
        enfants: enf,
      })),
    }));
  }
}