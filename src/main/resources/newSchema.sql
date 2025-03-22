CREATE TABLE budget(
   id INT AUTO_INCREMENT,
   descriptions TEXT,
   created_at DATETIME DEFAULT  CURRENT_TIMESTAMP,
   amount DECIMAL(10,2)   DEFAULT 0,
   id_customer int unsigned NOT NULL,
   id_user INT NOT NULL,
   PRIMARY KEY(id),
   FOREIGN KEY(id_customer) REFERENCES customer(customer_id),
   FOREIGN KEY(id_user) REFERENCES users(id)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE expense(
   id INT AUTO_INCREMENT,
   descriptions TEXT,
   amount DECIMAL(10,2)   DEFAULT 0,
   id_user INT,
   id_ticket int unsigned,
   id_lead int unsigned,
   PRIMARY KEY(id),
   FOREIGN KEY(id_user) REFERENCES users(id),
   FOREIGN KEY(id_ticket) REFERENCES trigger_ticket(ticket_id),
   FOREIGN KEY(id_lead) REFERENCES trigger_lead(lead_id)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE history_expense(
   id INT AUTO_INCREMENT,
   descriptions text,
   date_update DATETIME DEFAULT CURRENT_TIMESTAMP,
   amount DECIMAL(10,2)  ,
   id_expense int NOT NULL,
   id_user INT NOT NULL,
   PRIMARY KEY(id),
   FOREIGN KEY(id_expense) REFERENCES expense(id),
   FOREIGN KEY(id_user) REFERENCES users(id)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;