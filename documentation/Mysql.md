DELIMITER //
CREATE FUNCTION calcul_remise(prix DECIMAL(10,2), client_id INT) 
RETURNS DECIMAL(10,2)
DETERMINISTIC
BEGIN
    DECLARE remise DECIMAL(10,2);
    SELECT reduction INTO remise FROM clients WHERE id = client_id;
    RETURN prix * (1 - IFNULL(remise, 0));
END //
DELIMITER ;

DELIMITER //
CREATE TRIGGER audit_client_update
AFTER UPDATE ON clients
FOR EACH ROW
BEGIN
    INSERT INTO audit_log (table_modifiee, action, ancienne_valeur, nouvelle_valeur)
    VALUES ('clients', 'UPDATE', CONCAT(OLD.id, '-', OLD.nom), CONCAT(NEW.id, '-', NEW.nom));
END //
DELIMITER ;

DELIMITER //
CREATE TRIGGER valider_commande
BEFORE INSERT ON commandes
FOR EACH ROW
BEGIN
    IF NEW.montant <= 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Montant invalide';
    END IF;
END //
DELIMITER ;