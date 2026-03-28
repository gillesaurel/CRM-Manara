USE crm_manara;

-- 1. Utilisateurs
INSERT INTO utilisateur (id, email, motdepasse, nom, prenom, telephone, role_id)
VALUES 
  (1, 'sarah.leblanc@manara.com', 'motdepasse', 'Leblanc', 'Sarah', '5141234567', 2),
  (2, 'marc.tremblay@gmail.com', 'motdepasse', 'Tremblay', 'Marc', '4389876543', 3),
  (3, 'julie.gagnon@gmail.com', 'motdepasse', 'Gagnon', 'Julie', '5147654321', 3),
  (4, 'kevin.roy@manara.com', 'motdepasse', 'Roy', 'Kevin', '4381112222', 2);

-- 2. Activite (admin)
INSERT INTO activite (id, capacite, date_creation, date_debut, description, nom)
VALUES 
  (1, 20, '2025-01-10 08:00:00', '2025-04-01 08:00:00', 'Activité sportive pour enfants', 'Soccer jeunesse'),
  (2, 15, '2025-01-15 08:00:00', '2025-04-05 08:00:00', 'Cours de peinture et dessin', 'Atelier arts plastiques');

-- 3. Activites parent
INSERT INTO activites_parent (id, active, age_max, age_min, description, nom, prix)
VALUES 
  (1, 1, 12, 5, 'Initiation au soccer pour les jeunes de 5 à 12 ans', 'Soccer jeunesse', 25.00),
  (2, 1, 10, 4, 'Atelier créatif de peinture et dessin', 'Atelier arts plastiques', 15.00),
  (3, 1, 8, 3, 'Éveil musical avec instruments adaptés aux enfants', 'Éveil musical', 20.00);

-- 4. Sessions parent
INSERT INTO sessions_parent (id, capacite_max, date, heure_debut, heure_fin, lieu, statut, activite_id, animateur_id)
VALUES 
  (1, 15, '2026-04-05', '09:00:00', '11:00:00', 'Centre sportif Rosemont', 'PLANIFIEE', 1, 1),
  (2, 15, '2026-04-12', '09:00:00', '11:00:00', 'Centre sportif Rosemont', 'PLANIFIEE', 1, 1),
  (3, 10, '2026-04-07', '13:00:00', '15:00:00', 'Salle polyvalente A', 'PLANIFIEE', 2, 4),
  (4, 10, '2026-04-14', '13:00:00', '15:00:00', 'Salle polyvalente A', 'PLANIFIEE', 2, 4),
  (5, 12, '2026-04-09', '10:00:00', '12:00:00', 'Studio musical Manara', 'PLANIFIEE', 3, 1);

-- 5. Parents
INSERT INTO parent VALUES
  (1, 2),
  (2, 3);

-- 6. Enfants
INSERT INTO enfant VALUES
  (1, NULL, '2018-06-15', 'Tremblay', NULL, 'Luca', 1),
  (2, NULL, '2020-03-22', 'Tremblay', 'Allergie aux arachides', 'Emma', 1),
  (3, NULL, '2019-11-08', 'Gagnon', NULL, 'Noah', 2),
  (4, NULL, '2021-01-30', 'Gagnon', NULL, 'Léa', 2);

-- 7. Inscriptions
INSERT INTO inscription_enfant (id, en_attente, nom_enfant, activite_id, enfant_id, session_id, utilisateur_id)
VALUES
  (1, 0, 'Luca Tremblay',  1, 1, 1, 2),
  (2, 0, 'Emma Tremblay',  1, 2, 2, 2),
  (3, 1, 'Noah Gagnon',    2, 3, 3, 3),
  (4, 0, 'Léa Gagnon',     2, 4, 4, 3),
  (5, 0, 'Luca Tremblay',  1, 1, 5, 2);