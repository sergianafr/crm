# Utiliser une image de base avec Tomcat et Java 17
FROM tomcat:10.1-jdk17-temurin

# Supprimer les applications par défaut de Tomcat (optionnel)
RUN rm -rf /usr/local/tomcat/webapps/*

# Copier le fichier WAR dans le dossier webapps de Tomcat
COPY target/crm.war /usr/local/tomcat/webapps/ROOT.war

# Exposer le port 8080 (port par défaut de Tomcat)
EXPOSE 8080

# Commande pour démarrer Tomcat
CMD ["catalina.sh", "run"]