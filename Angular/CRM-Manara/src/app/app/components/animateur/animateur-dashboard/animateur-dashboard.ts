import { Component, OnInit, ChangeDetectorRef } from '@angular/core'; // 1. Import it
import { AnimateurService } from '../../../services/animateur-service';
import { Location } from '@angular/common';

@Component({
  selector: 'app-animateur-dashboard',
  standalone: false,
  templateUrl: './animateur-dashboard.html',
  styleUrl: './animateur-dashboard.css',
})
export class AnimateurDashboard implements OnInit {
  sessions: any[] = [];
  totalInscriptions: number = 0;
  sessionsAujourdHui: number = 0;
  incidents7j: number = 0;

  // 1. Inject ChangeDetectorRef
  constructor(
    private animateurService: AnimateurService,
    private cdr: ChangeDetectorRef ,
    private location: Location
  ) {}

  ngOnInit(): void {
    
    this.animateurService.getDashboardData().subscribe({
      next: (data: any) => {
        console.log("FULL DATA:", data.sessionsAujourdHui);
        this.sessions           = data.sessions           ?? [];
        this.totalInscriptions  = data.totalInscriptions  ?? 0;
        this.sessionsAujourdHui = data.sessionsAujourdHui ?? 0;
        this.incidents7j        = data.incidents7j        ?? 0;

        // 2. Add it here to notify Angular the state has changed
        this.cdr.markForCheck(); 
        console.log(this.sessions);

      },
      error: (err: any) => console.error('[Dashboard] load error', err),
    });
  }

  goBack(): void {
    this.location.back();
  }
}
