import { Component, OnInit } from '@angular/core';
import { AdminService } from '../../../services/admin-service';

@Component({
  selector: 'app-email',
  standalone: false,
  templateUrl: './email.html',
  styleUrl: './email.css',
})
export class Email implements OnInit {

  playload = {
    type: 'All',
    parentId: null,
    animateurId: null,
    subject: '',
    message: ''
  }

  parents: any[] = [];
  animateurs: any[] = [];
  message: string = '';

  constructor(private adminService: AdminService) { }

  ngOnInit(): void {
    this.loadUsers();
  }

  loadUsers() {
    this.adminService.emailPage().subscribe({
      next: (data: any) => {
        this.parents = data.parents;
        this.animateurs = data.animateurs;
      },
      error: (error) => {
        console.error('Erreur lors de la récupération des utilisateurs :', error);
      }
    });
  }

  sendEmail() {
    if (!this.playload.subject || !this.playload.message) {
      alert('Veuillez remplir tous les champs.');
      return;
    }
    this.adminService.sendEmail(this.playload).subscribe({
      next: () => {
        this.message = 'Email envoyé avec succès !';
        this.resetForm();
      },
      error: (error) => {
        console.error(error);
        alert('Erreur lors de l\'envoi de l\'email.');
      }
    });
  }

  resetForm() {
    this.playload = {
      type: 'All',
      parentId: null,
      animateurId: null,
      subject: '',
      message: ''
    };
  }

  onTypeChange() {
    if (this.playload.type === 'Parents') {
      this.playload.animateurId = null;
    } else if (this.playload.type === 'Animateurs') {
      this.playload.parentId = null;
    } else {
      this.playload.parentId = null;
      this.playload.animateurId = null;
    }
  } 
}

