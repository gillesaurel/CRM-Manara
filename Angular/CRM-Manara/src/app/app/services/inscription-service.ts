import { Injectable } from '@angular/core';
import inscriptions from '../mock-data/inscriptions.json';
import enfants     from '../mock-data/enfants.json';
import activites   from '../mock-data/activites-parent.json';

export interface Enfant {
  id: number;
  nom: string;
  prenom: string;
  date_naissance: string;
  notes_medicales: string | null;
  parent_id: number;
}

export interface Inscription {
  id: number;
  en_attente: boolean;
  nom_enfant: string;
  activites_id: number;
  enfant_id: number;
  session_id: number;
  utilisateur_id: number;
  enfant?: Enfant;
}

export interface GroupedInscriptions {
  activiteNom: string;
  inscriptions: Inscription[];
}

@Injectable({ providedIn: 'root' })
export class InscriptionService {

  /** Returns inscriptions grouped by activity name, with enfant data joined in */
  getInscriptionsParActivite(): GroupedInscriptions[] {
    // Join enfant data onto each inscription
    const enriched: Inscription[] = (inscriptions as Inscription[]).map(insc => ({
      ...insc,
      enfant: (enfants as Enfant[]).find(e => e.id === insc.enfant_id)
    }));

    // Group by activite nom
    const map = new Map<string, Inscription[]>();
    for (const insc of enriched) {
      const activite = (activites as any[]).find(a => a.id === insc.activites_id);
      const nom = activite?.nom ?? `Activité ${insc.activites_id}`;
      if (!map.has(nom)) map.set(nom, []);
      map.get(nom)!.push(insc);
    }

    return Array.from(map.entries()).map(([activiteNom, ins]) => ({ activiteNom, inscriptions: ins }));
  }
}