<<<<<<< HEAD
package com.manara.portail.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "sessions_parent")
public class SessionParent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull(message = "L'heure de début est requise")
    @Column(name = "heure_debut", nullable = false)
    private LocalTime heureDebut;

    @NotNull(message = "L'heure de fin est requise")
    @Column(name = "heure_fin", nullable = false)
    private LocalTime heureFin;

    @NotNull(message = "La date est requise")
    @FutureOrPresent(message = "La date doit être dans le présent ou le futur")
    @Column(nullable = false)
    private LocalDate date;

    @NotNull(message = "La capacité maximale est requise")
    @Min(value = 1, message = "La capacité maximale doit être au moins 1")
    @Max(value = 100, message = "La capacité maximale ne peut pas dépasser 100")
    @Column(name = "capacite_max", nullable = false)
    private Integer capaciteMax;

    @NotBlank(message = "Le lieu est requis")
    @Size(max = 255, message = "Le lieu ne peut pas dépasser 255 caractères")
    @Column(nullable = false)
    private String lieu;

    @NotBlank(message = "Le statut est requis")
    @Column(length = 50, nullable = false)
    private String statut = "PLANIFIEE";

    @NotNull(message = "L'activité est requise")
    @ManyToOne
    @JoinColumn(name = "activite_id", nullable = false)
    private ActiviteParent activite;

    @ManyToOne
    @JoinColumn(name = "animateur_id")
    private Utilisateur animateur;

    @OneToMany(mappedBy = "session", cascade = CascadeType.ALL)
    private List<Presence> presences = new ArrayList<>();

    @OneToMany(mappedBy = "session", cascade = CascadeType.ALL)
    private List<InscriptionEnfant> inscriptions = new ArrayList<>();

    public SessionParent() {}

    public SessionParent(LocalTime heureDebut, LocalTime heureFin, LocalDate date,
                         Integer capaciteMax, String lieu, ActiviteParent activite) {
        this.heureDebut = heureDebut;
        this.heureFin = heureFin;
        this.date = date;
        this.capaciteMax = capaciteMax;
        this.lieu = lieu;
        this.activite = activite;
        this.statut = "PLANIFIEE";
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public LocalTime getHeureDebut() { return heureDebut; }
    public void setHeureDebut(LocalTime heureDebut) { this.heureDebut = heureDebut; }

    public LocalTime getHeureFin() { return heureFin; }
    public void setHeureFin(LocalTime heureFin) { this.heureFin = heureFin; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public Integer getCapaciteMax() { return capaciteMax; }
    public void setCapaciteMax(Integer capaciteMax) { this.capaciteMax = capaciteMax; }

    public String getLieu() { return lieu; }
    public void setLieu(String lieu) { this.lieu = lieu; }

    public String getStatut() { return statut; }
    public void setStatut(String statut) { this.statut = statut; }

    public ActiviteParent getActivite() { return activite; }
    public void setActivite(ActiviteParent activite) { this.activite = activite; }

    public Utilisateur getAnimateur() { return animateur; }
    public void setAnimateur(Utilisateur animateur) { this.animateur = animateur; }

    public List<Presence> getPresences() { return presences; }
    public void setPresences(List<Presence> presences) { this.presences = presences; }

    public List<InscriptionEnfant> getInscriptions() { return inscriptions; }
    public void setInscriptions(List<InscriptionEnfant> inscriptions) { this.inscriptions = inscriptions; }

    public int getNbInscrits() {
        return inscriptions != null ? (int) inscriptions.stream().filter(i -> !i.isEnAttente()).count() : 0;
    }
=======
package com.manara.portail.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "sessions_parent")
public class SessionParent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "heure_debut", nullable = false)
    private LocalTime heureDebut;

    @Column(name = "heure_fin", nullable = false)
    private LocalTime heureFin;


    @Column(nullable = false)
    private LocalDate date;


    @Column(name = "capacite_max", nullable = false)
    private Integer capaciteMax;

    @Column(nullable = false)
    private String lieu;

    @Column(length = 50, nullable = false)
    private String statut = "PLANIFIEE";

    @ManyToOne
    @JoinColumn(name = "activite_id", nullable = false)
    private ActiviteParent activite;

    @ManyToOne
    @JoinColumn(name = "animateur_id")
    private Utilisateur animateur;

    @OneToMany(mappedBy = "session", cascade = CascadeType.ALL)
    private List<InscriptionEnfant> inscriptions = new ArrayList<>();

    public SessionParent() {}

    public SessionParent(LocalTime heureDebut, LocalTime heureFin, LocalDate date,
                         Integer capaciteMax, String lieu, ActiviteParent activite) {
        this.heureDebut = heureDebut;
        this.heureFin = heureFin;
        this.date = date;
        this.capaciteMax = capaciteMax;
        this.lieu = lieu;
        this.activite = activite;
        this.statut = "PLANIFIEE";
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public LocalTime getHeureDebut() { return heureDebut; }
    public void setHeureDebut(LocalTime heureDebut) { this.heureDebut = heureDebut; }

    public LocalTime getHeureFin() { return heureFin; }
    public void setHeureFin(LocalTime heureFin) { this.heureFin = heureFin; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public Integer getCapaciteMax() { return capaciteMax; }
    public void setCapaciteMax(Integer capaciteMax) { this.capaciteMax = capaciteMax; }

    public String getLieu() { return lieu; }
    public void setLieu(String lieu) { this.lieu = lieu; }

    public String getStatut() { return statut; }
    public void setStatut(String statut) { this.statut = statut; }

    public ActiviteParent getActivite() { return activite; }
    public void setActivite(ActiviteParent activite) { this.activite = activite; }

    public Utilisateur getAnimateur() { return animateur; }
    public void setAnimateur(Utilisateur animateur) { this.animateur = animateur; }

    public List<InscriptionEnfant> getInscriptions() { return inscriptions; }
    public void setInscriptions(List<InscriptionEnfant> inscriptions) { this.inscriptions = inscriptions; }

    public int getNbInscrits() {
        return inscriptions != null ? (int) inscriptions.stream().filter(i -> !i.isEnAttente()).count() : 0;
    }
>>>>>>> temporary-brench-manu
}