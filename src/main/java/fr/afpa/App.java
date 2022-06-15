package fr.afpa;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

/**
 * Application permettant d'illustrer les failles liées aux injection SQL.
 */
public final class App {
    public static void main(String[] args) {
        System.out.println("Programme de test d'accès à une base de données");
        
        Scanner scanner = new Scanner(System.in);
		System.out.println("Login (adresse e-mail) : ");
		String email = scanner.nextLine();
		System.out.println("e-mail utilisateur : "+ email);

		System.out.println("Saisissez votre mot de passe pour obtenir des détails sur votre compte :");
		String password = scanner.nextLine();
		System.out.println("Mot de passe saisi : "+ password);
		printUserInformation(email, password);
        
        System.out.println("Fin du programme");
        scanner.close();
    }

    static void printUserInformation(String email, String password) {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection("jdbc:mariadb://localhost:3306/injection_test", "root", "mariaRoot");

            Statement statement = connection.createStatement();
            String query = "SELECT firstname, lastname, country, password FROM user WHERE email = '"+ email +"' AND password='"+ password +"'";

            System.out.println("(Debug) Requête qui sera exécutée : " + query);
            ResultSet resultSet = statement.executeQuery(query);
    
            int count = 0;
            if (resultSet != null) {
                while(resultSet.next()) {
                    System.out.println("Nom : " + resultSet.getString("firstname"));
                    System.out.println("Prénom : " + resultSet.getString("lastname"));
                    System.out.println("Pays : " + resultSet.getString("country"));
                    System.out.println("Mot de passe : " + resultSet.getString("password"));
                    count++;
                }
            }

            if (count == 0) {
                System.out.println("Aucune résultat retrouvé. Vérifiez votre identifiant ou votre mot de passe.");
            }

            // Ne pas oublier de fermer la connexion à la base de données en fin de programme
            connection.close();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }
}
