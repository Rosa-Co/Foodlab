package progetto.app.dao.postgree;

import progetto.app.dao.Interface.CorsoDAO;
import progetto.app.database.DatabaseConnection;
import progetto.app.exception.DAOException;
import progetto.app.exception.DuplicateCorsoException;
import progetto.app.model.Corso;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementazione PostgreSQL dell'interfaccia {@link CorsoDAO}.
 * <p>
 * Gestisce la persistenza delle entità {@link Corso} sulla tabella
 * {@code corso}
 * del database PostgreSQL, utilizzando JDBC tramite {@link DatabaseConnection}.
 * </p>
 * <p>
 * Il metodo {@link #getCorsiByChef(int)} calcola il numero reale di sessioni
 * tramite sottoquery sulla tabella {@code sessione}, ignorando il valore
 * memorizzato nella colonna {@code numero_sessioni} del corso.
 * </p>
 */
public class CorsoDAO_Postgree implements CorsoDAO {

    /** Codice SQL State per la violazione di vincolo UNIQUE in PostgreSQL. */
    private static final String UNIQUE_VIOLATION = "23505";

    /**
     * {@inheritDoc}
     * <p>
     * Esegue un {@code INSERT} con {@code RETURN_GENERATED_KEYS} per ottenere
     * l'ID assegnato dal database e aggiornarlo nell'oggetto {@code corso}.
     * La violazione UNIQUE (titolo duplicato) viene convertita in
     * {@link DuplicateCorsoException}.
     * </p>
     */
    @Override
    public void addCorso(Corso corso) throws DAOException, DuplicateCorsoException {
        String sql = "INSERT INTO corso (titolo, categoria, data_inizio, frequenza, numero_sessioni, chef_id) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection con = DatabaseConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, corso.getTitolo());
            ps.setString(2, corso.getCategoria());
            ps.setDate(3, Date.valueOf(corso.getDataInizio()));
            ps.setString(4, corso.getFrequenza());
            ps.setInt(5, corso.getNumeroSessioni());
            ps.setInt(6, corso.getChefId());

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    corso.setId(rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            if (e.getSQLState().equals(UNIQUE_VIOLATION)) {
                throw new DuplicateCorsoException("Il corso \"" + corso.getTitolo() + "\" esiste già.");
            }
            e.printStackTrace();
            throw new DAOException("Impossibile aggiungere il corso, riprova più tardi.", e);
        }
    }

    /**
     * {@inheritDoc}
     * <p>
     * Utilizza il metodo helper privato {@link #mapRowToCorso(ResultSet)} per
     * mappare ciascuna riga del {@code ResultSet} in un'entità {@link Corso}.
     * </p>
     */
    @Override
    public List<Corso> getAllCorsi() throws DAOException {
        List<Corso> corsi = new ArrayList<>();
        String sql = "SELECT * FROM corso";
        try (Connection con = DatabaseConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                corsi.add(mapRowToCorso(rs));
            }
        } catch (SQLException e) {
            throw new DAOException("Impossibile recuperare i corsi", e);
        }
        return corsi;
    }

    /**
     * {@inheritDoc}
     * <p>
     * La query calcola il numero di sessioni reali con una sottoquery
     * {@code (SELECT COUNT(*) FROM sessione WHERE corso_id = corso.id)},
     * pertanto il valore restituito potrebbe differire dal campo
     * {@code numero_sessioni}
     * memorizzato nella tabella {@code corso}.
     * </p>
     */
    @Override
    public List<Corso> getCorsiByChef(int chefId) throws DAOException {
        List<Corso> corsi = new ArrayList<>();
        String sql = "SELECT id, titolo, categoria, data_inizio, frequenza, (SELECT COUNT(*) FROM sessione WHERE corso_id = corso.id) as numero_sessioni, chef_id FROM corso WHERE chef_id = ?";
        try (Connection con = DatabaseConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, chefId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    corsi.add(mapRowToCorso(rs));
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Impossibile recuperare i corsi dello chef", e);
        }
        return corsi;
    }

    /**
     * Mappa una riga del {@link ResultSet} in un'entità {@link Corso}.
     *
     * @param rs il {@link ResultSet} posizionato sulla riga corrente
     * @return un'istanza di {@link Corso} popolata con i valori della riga
     * @throws SQLException in caso di errore nella lettura delle colonne
     */
    private Corso mapRowToCorso(ResultSet rs) throws SQLException {
        return new Corso(
                rs.getInt("id"),
                rs.getString("titolo"),
                rs.getString("categoria"),
                rs.getDate("data_inizio").toLocalDate(),
                rs.getString("frequenza"),
                rs.getInt("numero_sessioni"),
                rs.getInt("chef_id"));
    }

}
