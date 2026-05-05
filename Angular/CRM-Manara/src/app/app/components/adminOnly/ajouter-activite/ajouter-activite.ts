import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { AdminService } from '../../../services/admin-service';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-ajouter-activite',
  standalone: false,
  templateUrl: './ajouter-activite.html',
  styleUrl: './ajouter-activite.css',
})
export class AjouterActivite implements OnInit {

  message: string = '';
  animateurs: any[] = [];

  activiteForm: any = {
    id: null,
    nom: '',
    description: '',
    capacite: 1,
    prix: 0,
    animateurId: null,
    seances: [{ dateHeure: '' }]
  };

  constructor(
    private adminService: AdminService,
    private router: Router,
    private cdr: ChangeDetectorRef,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    // Charger les animateurs
    this.adminService.getAnimateurs().subscribe({
      next: (data: any) => {
        this.animateurs = data;
      }
    });

    // Mode édition
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.adminService.getActiviteById(+id).subscribe((data: any) => {
        this.activiteForm = {
          id: data.id,
          nom: data.nom,
          description: data.description,
          capacite: data.capacite,
          prix: data.prix || 0,
          animateurId: data.animateur?.id || null,
          seances: data.seances?.length > 0
            ? data.seances.map((s: any) => ({
                dateHeure: new Date(s.dateHeure).toISOString().slice(0, 16)
              }))
            : [{ dateHeure: '' }]
        };
        this.cdr.detectChanges();
      });
    }
  }

  // Retourne l'animateur sélectionné pour le preview
  getSelectedAnimateur() {
    if (!this.activiteForm.animateurId) return null;
    return this.animateurs.find(a => a.id === this.activiteForm.animateurId);
  }

  // Ajouter une séance
  addSeance() {
    this.activiteForm.seances.push({ dateHeure: '' });
  }

  // Supprimer une séance
  removeSeance(index: number) {
    if (this.activiteForm.seances.length > 1) {
      this.activiteForm.seances.splice(index, 1);
    }
  }

  // Vérifie si la date d'une séance est invalide (avant la précédente)
  isDateInvalide(index: number): boolean {
    if (index === 0) return false;
    const current = this.activiteForm.seances[index].dateHeure;
    const previous = this.activiteForm.seances[index - 1].dateHeure;
    if (!current || !previous) return false;
    return new Date(current) <= new Date(previous);
  }

  // Vérifie s'il y a des erreurs de dates
  hasDateErrors(): boolean {
    return this.activiteForm.seances.some((_: any, i: number) => this.isDateInvalide(i));
  }

  onSubmit() {
    if (this.hasDateErrors()) {
      alert('Veuillez corriger les dates des séances.');
      return;
    }

    const payload = {
      id: this.activiteForm.id,
      nom: this.activiteForm.nom,
      description: this.activiteForm.description,
      capacite: this.activiteForm.capacite,
      prix: this.activiteForm.prix,
      animateurId: this.activiteForm.animateurId,
      dateDebut: this.activiteForm.seances[0]?.dateHeure || null,
      seances: this.activiteForm.seances
        .filter((s: any) => s.dateHeure)
        .map((s: any) => ({ dateHeure: s.dateHeure }))
    };

    const serviceCall = this.activiteForm.id
      ? this.adminService.updateActivite({ ...payload, id: this.activiteForm.id })
      : this.adminService.createActivite(payload);

    serviceCall.subscribe({
      next: () => {
        this.message = this.activiteForm.id
          ? 'Activité modifiée avec succès !'
          : 'Activité créée avec succès !';
        setTimeout(() => {
          this.message = '';
          this.router.navigate(['/admin']);
        }, 2000);
      },
      error: (err: unknown) => {
        console.error(err);
        this.message = "Une erreur est survenue lors de l'enregistrement.";
      }
    });
  }

  goBack() {
    this.router.navigate(['/admin']);
  }
}
