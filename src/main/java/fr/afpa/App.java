package fr.afpa;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

/**
 * Application permettant d'illustrer les failles liées aux injection SQL.
 */
public final class App {
    private static Connection connection;

    public static void main(String[] args) {
        establishConnection();

        System.out.println("Programme de test d'accès à une base de données");

        Scanner scanner = new Scanner(System.in);
        System.out.println("Login (adresse e-mail) : ");
        String email = scanner.nextLine();
        System.out.println("-> e-mail utilisateur saisi : " + email);

        System.out.println("Saisissez votre mot de passe pour obtenir des détails sur votre compte :");
        String password = scanner.nextLine();
        System.out.println("-> Mot de passe saisi : " + password);
        
        printUserInformation(email, password);

        System.out.println("Fin du programme");
        // fermeture du scanner pour éviter les fuites mémoire
        scanner.close();

        // Ne pas oublier de fermer la connexion à la base de données en fin de programme
        try {
            connection.close();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    private static void establishConnection() {
        try {
            App.connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/injection_test", "postgres",
                    "postgrespostgres");
        } catch (SQLException e) {
            System.err.println("Erreur lors de la connexion à la base de données.");
            System.err.println(e.getMessage());
        }
    }

    /**
     * Affiche les informations d'un utilisateur retrouvé en BDD via son email et
     * son mot de passe.
     */
    private static void printUserInformation(String email, String password) {
        try {
            Statement statement = connection.createStatement();
            String query = "SELECT firstname, lastname, country, \"password\" FROM \"user\" WHERE email = '" + email
                    + "' AND password='" + password + "'";

            System.out.println("-> (Debug) Requête qui sera exécutée : " + query);
            ResultSet resultSet = statement.executeQuery(query);

            int count = 0;
            if (resultSet != null) {
                while (resultSet.next()) {
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

        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * Affiche les informations utlisateur via l'utilisation d'un appel de fonction PostgreSQL
     * 
     * TODO : ajouter des paramètres -> email + password
     */
    public static void printUserInformationByFunctionCall() {
        try {

            PreparedStatement preparedStatement = connection.prepareStatement("select * from find_user_info(?, ?)");
            // TODO préparation de la requête avec les variables attendues

            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                // TODO affichage des informations utilisateur
            }

        } catch (SQLException sqlException) {
            System.err.println(sqlException.getMessage());
        }
    }
}
