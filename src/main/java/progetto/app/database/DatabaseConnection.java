package progetto.app.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Gestisce le connessioni al database PostgreSQL.
 *
 * Questa classe fornisce metodi statici per ottenere connessioni
 * al database utilizzando le credenziali.
 * Ogni connessione viene aperta e deve essere chiusa manualmente
 * dal chiamante usando try-with-resources o finally.
 * 
 * @see Connection
 * @see SQLException
 */
public class DatabaseConnection {

    // Configurazione database
    private static final String URL = "jdbc:postgresql://ep-spring-dust-adb2oyjm-pooler.c-2.us-east-1.aws.neon.tech/neondb?sslmode=require&channel_binding=require";
    private static final String USER = "neondb_owner";
    private static final String PASSWORD = "npg_3QxTHSCYk1wi";
    private static Connection connection = null;

    /** Costruttore privato per impedire istanziazione */
    private DatabaseConnection() {
    }

    /**
     * Ottiene una nuova connessione al database PostgreSQL.
     * Questo metodo crea una nuova connessione fisica al database.
     * La connessione deve essere chiusa dal chiamante per evitare memory leak.
     * 
     * @return una nuova connessione al database, mai {@code null}
     * @throws SQLException se la connessione fallisce o le credenziali non sono
     *                      configurate correttamente
     * @see #testConnection()
     */

    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
        }
        return connection;
    }

    /**
     * Testa se la connessione al database funziona correttamente.
     * Questo metodo apre una connessione temporanea, verifica che sia
     * valida usando {@link Connection#isValid(int)}, e la chiude automaticamente.
     * È utile per verificare la configurazione all'avvio dell'applicazione.
     * 
     * @return {@code true} se la connessione è riuscita, {@code false} altrimenti
     * @see #getConnection()
     */
    public static boolean testConnection() {
        try {
            connection = getConnection();
            return connection != null && connection.isValid(2);
        } catch (SQLException e) {
            System.err.println("Test connessione fallito: " + e.getMessage());
            return false;
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                    System.out.println("Connessione chiusa");
                } catch (SQLException e) {
                    System.err.println("Errore chiusura connessione: " + e.getMessage());
                }
            }
        }
    }

}