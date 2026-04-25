import { ChangeDetectorRef, Component, OnInit  } from '@angular/core';
import { AnimateurService } from    '../../../services/animateur-service';
import { InscriptionService } from  '../../../services/inscription-service';
import { AuthService } from         '../../../../services/auth-service';

@Component({
  selector: 'app-animateur-dashboard',
  standalone: false,
  templateUrl: './animateur-dashboard.html',
  styleUrl: './animateur-dashboard.css',
})
export class AnimateurDashboard {
  constructor(
    private animateurService: AnimateurService,
    private inscriptionService: InscriptionService,
    private authService: AuthService,
  ) {}

  ngOnInit(): void {
    this.getInscriptionCount();
  }

  getInscriptionCount() {
    console.log(this.authService.getMe());
    //this.inscriptionService.getInscriptionsByAnimateur();
  }
}


