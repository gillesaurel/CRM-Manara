import { Component, OnInit } from '@angular/core';
import { PresenceService, ActiviteGroup } from '../../../services/presence-service';
@Component({
  selector: 'app-animateur-presences',
  standalone: false,
  templateUrl: './animateur-presences.html',
  styleUrl: './animateur-presences.css',
})
export class AnimateurPresences implements OnInit {
  groups: ActiviteGroup[] = [];

  constructor(private presenceService: PresenceService) {}

  ngOnInit(): void {
    this.groups = this.presenceService.getPresencesParActivite();
  }
}