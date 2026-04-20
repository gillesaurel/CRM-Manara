import { Component, OnInit } from '@angular/core';
import { InscriptionService, GroupedInscriptions } from '../../../services/inscription-service';

@Component({
  selector: 'app-animateur-inscription',
  standalone: false,
  templateUrl: './animateur-inscription.html',
  styleUrl: './animateur-inscription.css',
})
export class AnimateurInscription implements OnInit {
  groups: GroupedInscriptions[] = [];

  constructor(private inscriptionService: InscriptionService) {}

  ngOnInit(): void {
    this.groups = this.inscriptionService.getInscriptionsParActivite();
  }
}