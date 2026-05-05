import { Component, OnInit, ChangeDetectorRef } from '@angular/core'; // 1. Import CDR
import { AnimateurService } from '../../../services/animateur-service';
import { Location } from '@angular/common';

@Component({
  selector: 'app-animateur-incidents',
  standalone: false,
  templateUrl: './animateur-incidents.html',
  styleUrl: './animateur-incidents.css',
})
export class AnimateurIncidents implements OnInit {
  incidents:    any[] = [];
  enfants:      any[] = [];
  message:      string = '';
  messageType:  string = '';

  enfantId:     number | null = null;
  type:         string = '';
  description:  string = '';
  gravite:      number | null = null;
  actionsPrises: string = '';

  constructor(
    private animateurService: AnimateurService,
    private cdr: ChangeDetectorRef, // 2. Inject CDR
    private location: Location
  ) {}

  ngOnInit(): void {
    this.refreshData();
  }

  private refreshData(): void {
    this.animateurService.getIncidentData().subscribe({
      next: (data: any) => {
        this.incidents = data.incidents ?? [];
        this.enfants   = data.enfants   ?? [];
        this.cdr.detectChanges(); // 3. Force UI refresh after async load
      },
      error: (err: any) => console.error('[Incidents] load error', err),
    });
  }

  soumettre(): void {
    if (!this.enfantId || !this.type || !this.description || !this.gravite) {
      this.message     = 'Veuillez remplir tous les champs obligatoires.';
      this.messageType = 'error';
      return;
    }

    const payload = {
      enfantId:     this.enfantId,
      type:         this.type,
      description:  this.description,
      gravite:      this.gravite,
      actionsPrises: this.actionsPrises,
    };

    this.animateurService.saveIncident(payload).subscribe({
      next: (data: any) => {
        this.message     = data.message ?? 'Incident signalé avec succès.';
        this.messageType = 'success';
        this.resetForm();
        this.refreshData(); // Triggers detectChanges() via load
      },
      error: (err: any) => {
        this.message     = 'Erreur lors du signalement.';
        this.messageType = 'error';
        this.cdr.detectChanges(); // 4. Ensure error message appears
        console.error('[Incidents] save error', err);
      },
    });
  }

  // ... (graviteLabel, graviteColor, formatDate methods remain same)

  private resetForm(): void {
    this.enfantId      = null;
    this.type          = '';
    this.description   = '';
    this.gravite       = null;
    this.actionsPrises = '';
    this.cdr.detectChanges(); // 5. Ensure form clears immediately
  }

  graviteLabel(g: number): string {
    const labels: Record<number, string> = {
      1: 'Mineur',
      2: 'Faible',
      3: 'Modéré',
      4: 'Grave',
      5: 'Très grave',
    };
    return labels[g] ?? '';
  }

  // You likely also need this one for the template logic to be complete
  graviteColor(g: number): string {
    if (g <= 2) return '4px solid #28a745';
    if (g === 3) return '4px solid #fccd2c';
    return '4px solid #dc3545';
  }

  formatDate(dateHeure: string): string {
    if (!dateHeure) return '—';
    return new Date(dateHeure).toLocaleString('fr-CA');
  }

  goBack(): void {
    this.location.back();
  }
}
