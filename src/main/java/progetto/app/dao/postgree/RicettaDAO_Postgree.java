package progetto.app.dao.postgree;

import progetto.app.dao.Interface.RicettaDAO;
import progetto.app.database.DatabaseConnection;
import progetto.app.exception.DAOException;
import progetto.app.exception.DuplicateRecipeException;
import progetto.app.model.Ricetta;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementazione PostgreSQL dell'interfaccia {@link RicettaDAO}.
 * <p>
 * Gestisce la persistenza delle entità {@link Ricetta} sulla tabella
 * {@code ricetta}
 * e le associazioni ricetta-sessione sulla tabella {@code sessione_ricetta},
 * utilizzando JDBC tramite {@link DatabaseConnection}.
 * </p>
 * <p>
 * Il metodo {@link #addRicetta(Ricetta)} usa la clausola PostgreSQL
 * {@code RETURNING id} per recuperare la chiave generata direttamente
 * nel {@link ResultSet}.
 * La violazione di unicità (codice SQL State {@code 23505}) viene intercettata
 * e
 * convertita in {@link DuplicateRecipeException}.
 * </p>
 */
public class RicettaDAO_Postgree implements RicettaDAO {

    /** Codice SQL State per la violazione di vincolo UNIQUE in PostgreSQL. */
    private static final String UNIQUE_VIOLATION = "23505";

    /**
     * {@inheritDoc}
     * <p>
     * Utilizza il metodo helper privato {@link #mapRowToRicetta(ResultSet)} per
     * mappare ciascuna riga del {@code ResultSet} in un'entità {@link Ricetta}.
     * </p>
     */
    @Override
    public List<Ricetta> getAllRicette() throws DAOException {
        List<Ricetta> ricette = new ArrayList<>();
        String sql = "SELECT * FROM ricetta";
        try (Connection con = DatabaseConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                ricette.add(mapRowToRicetta(rs));
            }
        } catch (SQLException e) {
            throw new DAOException("Impossibile recuperare le ricette", e);
        }
        return ricette;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Ricetta> getRicetteByChef(int chefId) throws DAOException {
        List<Ricetta> ricette = new ArrayList<>();
        String sql = "SELECT * FROM ricetta WHERE chef_id = ?";
        try (Connection con = DatabaseConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, chefId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ricette.add(mapRowToRicetta(rs));
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Impossibile recuperare le ricette dello chef", e);
        }
        return ricette;
    }

    /**
     * {@inheritDoc}
     * <p>
     * Usa la clausola PostgreSQL {@code RETURNING id} per ottenere l'ID generato
     * direttamente dal {@code ResultSet} della query, senza necessità di
     * {@code Statement.RETURN_GENERATED_KEYS}. L'ID viene anche scritto
     * nell'oggetto {@code ricetta} tramite {@link Ricetta#setId(int)}.
     * </p>
     */
    @Override
    public int addRicetta(Ricetta ricetta) throws DAOException, DuplicateRecipeException {
        String sql = "INSERT INTO ricetta (nome, descrizione, chef_id) VALUES (?, ?, ?) RETURNING id";
        try (Connection con = DatabaseConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, ricetta.getNome());
            ps.setString(2, ricetta.getDescrizione());
            ps.setInt(3, ricetta.getChefId());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int id = rs.getInt(1);
                    ricetta.setId(id);
                    return id;
                } else {
                    throw new DAOException("Creazione ricetta fallita, nessun ID ottenuto.");
                }
            }
        } catch (SQLException e) {
            if (UNIQUE_VIOLATION.equals(e.getSQLState())) {
                throw new DuplicateRecipeException(e.getMessage());
            }
            throw new DAOException("Impossibile aggiungere la ricetta", e);
        }
    }

    /**
     * {@inheritDoc}
     * <p>
     * Esegue un {@code INSERT} nella tabella di join {@code sessione_ricetta}
     * con la coppia ({@code sessioneId}, {@code ricettaId}).
     * </p>
     */
    @Override
    public void addRicettaSessione(int sessioneId, int ricettaId) throws DAOException {
        String sql = "INSERT INTO sessione_ricetta (sessione_id, ricetta_id) VALUES (?, ?)";
        try (Connection con = DatabaseConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, sessioneId);
            ps.setInt(2, ricettaId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("Impossibile associare la ricetta alla sessione", e);
        }
    }

    /**
     * Mappa una riga del {@link ResultSet} in un'entità {@link Ricetta}.
     *
     * @param rs il {@link ResultSet} posizionato sulla riga corrente
     * @return un'istanza di {@link Ricetta} popolata con i valori della riga
     * @throws SQLException in caso di errore nella lettura delle colonne
     */
    private Ricetta mapRowToRicetta(ResultSet rs) throws SQLException {
        return new Ricetta(
                rs.getInt("id"),
                rs.getString("nome"),
                rs.getString("descrizione"),
                rs.getInt("chef_id"));
    }
}
