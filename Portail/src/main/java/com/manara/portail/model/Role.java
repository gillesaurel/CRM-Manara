package com.manara.portail.model;

import jakarta.persistence.*;

@Entity
@Table(name = "role")
public class  Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    String nom;
    String description;

    public Role() {
    }

    public Role( String nom, String description) {

        this.nom = nom;
        this.description = description;
    }

}
