-- Suppression des tables si elles existent déjà (optionnel)
DROP TABLE IF EXISTS MESSAGES;
DROP TABLE IF EXISTS RENTALS;
DROP TABLE IF EXISTS USERS;

-- Création de la table USERS
CREATE TABLE USERS (
       id INTEGER PRIMARY KEY AUTO_INCREMENT,
       email VARCHAR(255) UNIQUE NOT NULL,
       name VARCHAR(255) NOT NULL,
       password VARCHAR(255) NOT NULL,
       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
       updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Création de la table RENTALS
CREATE TABLE RENTALS (
     id INTEGER PRIMARY KEY AUTO_INCREMENT,
     name VARCHAR(255) NOT NULL,
     surface NUMERIC NOT NULL,
     price NUMERIC NOT NULL,
     picture VARCHAR(255),
     description VARCHAR(2000),
     owner_id INTEGER NOT NULL,
     created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
     updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
     CONSTRAINT fk_rental_owner FOREIGN KEY (owner_id) REFERENCES USERS(id) ON DELETE CASCADE
);

-- Création de la table MESSAGES
CREATE TABLE MESSAGES (
      id INTEGER PRIMARY KEY AUTO_INCREMENT,
      rental_id INTEGER NOT NULL,
      user_id INTEGER NOT NULL,
      message VARCHAR(2000) NOT NULL,
      created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
      updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
      CONSTRAINT fk_message_user FOREIGN KEY (user_id) REFERENCES USERS(id) ON DELETE CASCADE,
      CONSTRAINT fk_message_rental FOREIGN KEY (rental_id) REFERENCES RENTALS(id) ON DELETE CASCADE
);

-- Insertion des utilisateurs
INSERT INTO USERS (email, name, password) VALUES
     ('alice@example.com', 'Alice Dupont', 'hashed_password_1'),
     ('bob@example.com', 'Bob Martin', 'hashed_password_2'),
     ('charlie@example.com', 'Charlie Lemoine', 'hashed_password_3');

-- Insertion des locations
INSERT INTO RENTALS (name, surface, price, picture, description, owner_id) VALUES
    ('Appartement cosy', 45.5, 750, 'appartement1.jpg', 'Petit appartement idéal pour un couple.', 1),
    ('Villa avec piscine', 120, 2200, 'villa1.jpg', 'Grande villa avec piscine et jardin.', 2),
    ('Studio en centre-ville', 30, 600, 'studio1.jpg', 'Studio meublé proche de toutes commodités.', 3);

-- Insertion des messages
INSERT INTO MESSAGES (rental_id, user_id, message) VALUES
    (1, 2, 'Bonjour, l\ appartement est-il toujours disponible ?'),
    (2, 1, 'Puis-je visiter la villa ce week-end ?'),
    (3, 3, 'Je suis intéressé par le studio, quelles sont les conditions ?');
