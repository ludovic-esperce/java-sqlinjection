package fr.afpa;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class App {
    public static void main(String[] args) {
        System.out.println("Programme de test d'accès à une base de données");

        Connection connection = null;
        try {
            // TODO adapter nom d'utilisateur et mot de passe
            connection = DriverManager.getConnection("jdbc:mariadb://localhost:3306/papyrus", "root", "mariaRoot");

            // Des requêtes à foison !

            // Ne pas oublier de fermer la connexion à la base de données en fin de programme
            connection.close();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        System.out.println("Fin du programme");
    }
}
