USE CRM_Manara;
CREATE TABLE utilisateur  (
    id int AUTO_INCREMENT PRIMARY KEY,
	role VARCHAR(100) NOT NULL,
    prenom VARCHAR(100) NOT NULL,
    nom VARCHAR(100) NOT NULL,
    telephone VARCHAR(20),
    email VARCHAR(150) UNIQUE NOT NULL,
    motdepasse VARCHAR(255) NOT NULL,
    photo VARCHAR(255)
);
