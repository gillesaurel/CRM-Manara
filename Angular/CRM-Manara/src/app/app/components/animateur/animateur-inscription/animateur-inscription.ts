import {  ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { AnimateurService } from '../../../services/animateur-service';

@Component({
  selector: 'app-animateur-inscription',
  standalone: false,
  templateUrl: './animateur-inscription.html',
  styleUrl: './animateur-inscription.css',
})
export class AnimateurInscription implements OnInit {
  groups: any[] = [];
  constructor(private animateurService: AnimateurService,
    private cdr: ChangeDetectorRef,
) {}

  ngOnInit(): void {
    this.GetAnimatorData()
  }
  GetAnimatorData() {
    console.log("Appel de animator data");
    this.animateurService.getInscriptionData().subscribe({
      next: (data: any) => {
        const raw = data.inscriptions;
        this.groups = Object.entries(raw).map(([activiteNom, inscriptions]) => ({
          activiteNom,
          inscriptions
        }));
        this.cdr.detectChanges();
      },
      error: (error: unknown) => {
        console.error('Erreur lors de la récupération des données animateurs :', error);
      },
    });
  }

}
