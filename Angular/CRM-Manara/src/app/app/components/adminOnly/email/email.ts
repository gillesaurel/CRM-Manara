import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { HttpErrorResponse } from '@angular/common/http';
import { AdminService } from '../../../services/admin-service';

@Component({
  selector: 'app-email',
  standalone: false,
  templateUrl: './email.html',
  styleUrl: './email.css',
})
export class Email implements OnInit {

  dataSend = {
    type: 'ALL',
    parentId: null,
    animateurId: null,
    subject: '',
    message: ''
  }

  parents: any[] = [];
  animateurs: any[] = [];
  message: string = '';

  constructor(
    private adminService: AdminService, private cdr: ChangeDetectorRef) { }

  ngOnInit(): void {
    this.loadUsers();
  }

  loadUsers() {
    this.adminService.emailPage().subscribe({
      next: (data: any) => {
        this.parents = data.parents;
        this.animateurs = data.animateurs;
      },
      error: (error: unknown) => {
        console.error('Erreur lors de la récupération des utilisateurs :', error);
      }
    });
  }



  sendEmail(form: any) {
    if (form.invalid) {
      alert('Veuillez remplir tous les champs.');
      return;
    }

    const payload = {
      type: this.dataSend.type,
      parentId: this.dataSend.parentId,
      animateurId: this.dataSend.animateurId,
      subject: this.dataSend.subject?.trim(),
      message: this.dataSend.message?.trim()
    };

    console.log("PAYLOAD FINAL:", payload);

    this.adminService.sendEmail(payload).subscribe({
      next: (res: any) => {
        this.message = res?.message || 'Email envoyé avec succès !';
        this.cdr.detectChanges();
        setTimeout(() => {
          this.message = '';
          this.cdr.detectChanges();
        }, 3000);

        this.resetForm();
        form.resetForm();

      },
      error: (error: HttpErrorResponse) => {
        console.error(error);
        alert(error?.error?.message || 'Erreur lors de l\'envoi');
      }
    });
  }

  resetForm() {
    this.dataSend = {
      type: 'ALL',
      parentId: null,
      animateurId: null,
      subject: '',
      message: ''
    };
  }

  onTypeChange() {
    if (this.dataSend.type === 'PARENT_ALL') {
      this.dataSend.animateurId = null;
    } else if (this.dataSend.type === 'ANIMATEUR_ALL') {
      this.dataSend.parentId = null;
    } else if (this.dataSend.type === 'PARENT_ONE') {
      this.dataSend.animateurId = null;
    } else if (this.dataSend.type === 'ANIMATEUR_ONE') {
      this.dataSend.parentId = null;
    } else {
      this.dataSend.parentId = null;
      this.dataSend.animateurId = null;
    }
  }
}

