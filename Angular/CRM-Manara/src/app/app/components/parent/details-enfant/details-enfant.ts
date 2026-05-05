import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ParentService, Enfant, Inscription } from '../../../services/parent-service';

interface Presence {
  id: number;
  dateSaisie: string;
  present: boolean;
  session: {
    activite: { nom: string };
    date: string;
    heureDebut: string;
  };
}

@Component({
  selector: 'app-details-enfant',
  standalone: false,
  templateUrl: './details-enfant.html',
  styleUrls: ['./details-enfant.css']
})
export class DetailsEnfantComponent implements OnInit {
  
  enfant: Enfant | null = null;
  inscriptions: Inscription[] = [];
  presences: Presence[] = [];
  isLoading: boolean = true;

  constructor(
    private parentService: ParentService,
    private route: ActivatedRoute,
    private router: Router,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');
    
    
    if (id) {
      this.chargerEnfant(+id);
    } else {
      
      this.router.navigate(['/parent/enfants']);
    }
  }

  chargerEnfant(enfantId: number): void {
    
    this.isLoading = true;
    
    this.parentService.getEnfantById(enfantId).subscribe({
      next: (data) => {
       
        this.enfant = data;
        
        this.chargerInscriptions(enfantId);
        this.chargerPresences(enfantId);
        
        this.isLoading = false;
        this.cdr.detectChanges();
        
      },
      error: (err) => {
       
        this.isLoading = false;
        this.cdr.detectChanges();
        this.router.navigate(['/parent/enfants']);
      }
    });
  }  

  chargerInscriptions(enfantId: number): void {
    const parentId = this.getParentId();
   
    
    this.parentService.getInscriptionsParent(parentId).subscribe({
      next: (data: Inscription[]) => {
       
        this.inscriptions = data.filter(ins => ins.enfant?.id === enfantId);
        
      },
      error: (err) => console.error('Erreur chargement inscriptions:', err)
    });
  }

  chargerPresences(enfantId: number): void {
   
    this.presences = [];
   
  }

  private getParentId(): number {
    const user = localStorage.getItem('user');
    
    
    if (user) {
      const parsed = JSON.parse(user);
      
      return parsed.id;
    }
    
    return 0;
  }
}