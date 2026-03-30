USE crm_manara;

-- 1. Utilisateur (animateur) first
INSERT INTO utilisateur (id, email, motdepasse, nom, prenom, telephone, role_id)
VALUES (1, 'utilisateur@gmail.com', 'motdepasse', 'Nom', 'Prenom', '5555555555', 2),
 (2, 'parent@gmail.com', 'motdepasse', 'Nom', 'Prenom', '5555555555', 3);

-- 2. Activite
INSERT INTO activite (id, capacite, date_creation, date_debut, description, nom)
VALUES (1, 10, '2025-03-12 00:00:00', '2025-03-27 00:00:00', 'description', "nom de l'activité");

-- 3. Session (depends on activite + utilisateur)
INSERT INTO activites_parent (id, active, age_max, age_min, description, nom, prix)
VALUES (1, 1, 12, 3, 'description', 'nom activite', 0.00);

INSERT INTO sessions_parent (id, capacite_max, date, heure_debut, heure_fin, lieu, statut, activite_id, animateur_id)
VALUES (1, 10, '2025-03-27', '09:00:00', '18:00:00', 'MTL', 'ACTIVE', 1, 1),
	(2, 10, '2025-03-27', '09:00:00', '18:00:00', 'MTL', 'ACTIVE', 1, 1);
    
INSERT INTO parent values
(1, 2);

INSERT INTO enfant values
(1,2, "2000-01-01", 'nom', 'medical','prenom', 1),
(2,2, "2000-01-01", 'enfant 2', 'medical','prenom', 1);


INSERT INTO inscription_enfant values
(1, 1, 'nom enfant', 1, 1, 1, 2),
(2, 1, 'nom enfant', 1, 2, 1, 2);