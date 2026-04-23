import { Component, OnInit } from '@angular/core';
import { ParentService, Activite } from '../../../services/parent-service';

@Component({
  selector: 'app-activites-list',
  standalone: false,
  templateUrl: './activites-list.html',
  styleUrl: './activites-list.css',
})
export class ActivitesListComponent implements OnInit {
  
  activites: Activite[] = [];
  activitesFiltrees: Activite[] = [];
  searchTerm: string = '';
  ageFiltre: string = '';

  constructor(private parentService: ParentService) {}

  ngOnInit(): void {
    this.chargerActivites();
  }

  chargerActivites(): void {
    this.parentService.getActivites().subscribe({
      next: (data) => {
        this.activites = data;
        this.appliquerFiltres();
      },
      error: (err) => console.error('Erreur chargement activités:', err)
    });
  }

  appliquerFiltres(): void {
    this.activitesFiltrees = this.activites.filter(activite => {
      if (this.ageFiltre) {
        const [min, max] = this.ageFiltre.split('-').map(Number);
        if (activite.ageMin < min || activite.ageMax > max) {
          return false;
        }
      }
      
      if (this.searchTerm) {
        return activite.nom.toLowerCase().includes(this.searchTerm.toLowerCase()) ||
               activite.description.toLowerCase().includes(this.searchTerm.toLowerCase());
      }
      
      return true;
    });
  }

  filtrerParAge(event: Event): void {
    const select = event.target as HTMLSelectElement;
    this.ageFiltre = select.value;
    this.appliquerFiltres();
  }

  rechercher(event: Event): void {
    const input = event.target as HTMLInputElement;
    this.searchTerm = input.value;
    this.appliquerFiltres();
  }
}