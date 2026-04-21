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
  activites: any[] = [];
  message: string = '';
  activiteForm: any = {
    id: null,
    nom: '',
    dateDebut: '',
    capacite: 1,
    description: ''
  };
  isEditMode: boolean = false;

  constructor(
    private adminService: AdminService,
    private router: Router,
    private cdr: ChangeDetectorRef,
    private route: ActivatedRoute
  ) { }

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');
    const d = new Date(this.activiteForm.dateDebut);

    if (id) {
      this.adminService.getActiviteById(+id).subscribe((data: any) => {

        const formattedDate = data.dateDebut
          ? new Date(data.dateDebut).toISOString().slice(0, 16)
          : '';

        this.activiteForm = {
          id: data.id,
          nom: data.nom,
          dateDebut: formattedDate,
          capacite: data.capacite,
          description: data.description
        };

        this.isEditMode = true;

        this.cdr.detectChanges();
      });
    }
  }

  onSubmit() {
    const serviceCall = this.activiteForm.id
      ? this.adminService.updateActivite(this.activiteForm)
      : this.adminService.createActivite(this.activiteForm);

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



  editActivite(activite: any) {
    this.activiteForm = { ...activite };
    this.isEditMode = true;
  }

  goBack() {
    this.router.navigate(['/admin']);
  }
}