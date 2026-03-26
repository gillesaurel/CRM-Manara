use crm_manara;
create table role (
id int primary key,
nom varchar(100),
description varchar(100));
INSERT INTO role (id, nom, description) VALUES
(1, 'ADMINISTRATEUR', 'Accès haut'),
(2, 'ANIMATEUR', 'Accès moyen'),
(3, 'PARENT', 'Accès bas'),
(4, 'ENFANT', 'Accès nul');
