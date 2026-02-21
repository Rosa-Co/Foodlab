package progetto.app.dao.postgree;

import progetto.app.dao.Interface.AllievoDAO;
import progetto.app.database.DatabaseConnection;
import progetto.app.exception.AllievoNotFoundException;
import progetto.app.exception.DAOException;
import progetto.app.exception.DuplicateAllievoException;
import progetto.app.model.Allievo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementazione PostgreSQL dell'interfaccia {@link AllievoDAO}.
 * <p>
 * Gestisce la persistenza delle entità {@link Allievo} sulla tabella
 * {@code allievo}
 * del database PostgreSQL, utilizzando JDBC tramite {@link DatabaseConnection}.
 * </p>
 * <p>
 * Le ricerche per username ed email sono case-insensitive grazie all'uso di
 * {@code LOWER()} nelle query SQL.
 * La violazione di unicità (codice SQL State {@code 23505}) viene intercettata
 * e
 * convertita in {@link DuplicateAllievoException}.
 * </p>
 */
public class AllievoDAO_Postgree implements AllievoDAO {

    /** Codice SQL State per la violazione di vincolo UNIQUE in PostgreSQL. */
    private static final String UNIQUE_VIOLATION = "23505";

    /**
     * Costruisce una nuova istanza di {@code AllievoDAO_Postgree}.
     */
    public AllievoDAO_Postgree() {
    }

    /**
     * {@inheritDoc}
     * <p>
     * Verifica prima dell'INSERT che non esista un allievo con lo stesso username
     * o email (case-insensitive). Se la verifica fallisce viene lanciata
     * {@link DuplicateAllievoException}. In caso di successo, il campo {@code id}
     * dell'oggetto {@code allievo} viene aggiornato con la chiave generata dal DB.
     * </p>
     */
    @Override
    public void addAllievo(Allievo allievo) throws DAOException, DuplicateAllievoException {
        String checkSql = "SELECT COUNT(*) FROM allievo WHERE LOWER(username) = LOWER(?) OR LOWER(email) = LOWER(?)";
        try (Connection con = DatabaseConnection.getConnection()) {
            try (PreparedStatement checkPs = con.prepareStatement(checkSql)) {
                checkPs.setString(1, allievo.getUsername());
                checkPs.setString(2, allievo.getEmail());
                ResultSet checkRs = checkPs.executeQuery();

                if (checkRs.next() && checkRs.getInt(1) > 0) {
                    throw new DuplicateAllievoException("Account già esistente.");
                }
            }
            String sql = "INSERT INTO allievo (username,password,nome,cognome,email) VALUES (?,?,?,?,?)";
            try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, allievo.getUsername());
                ps.setString(2, allievo.getPassword());
                ps.setString(3, allievo.getName());
                ps.setString(4, allievo.getSurname());
                ps.setString(5, allievo.getEmail());
                ps.executeUpdate();
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    int id = rs.getInt(1);
                    allievo.setId(id);
                }
            }
        } catch (SQLException e) {
            if (UNIQUE_VIOLATION.equals(e.getSQLState())) {
                throw new DuplicateAllievoException("Account già esistente.");
            } else {
                throw new DAOException("Impossibile aggiungere utente");
            }
        }
    }

    /**
     * {@inheritDoc}
     * <p>
     * Esegue una {@code SELECT * FROM allievo} e mappa ogni riga in un oggetto
     * {@link Allievo}.
     * </p>
     */
    @Override
    public List<Allievo> getAllAllievos() throws DAOException {
        List<Allievo> allievos = new ArrayList<>();
        String sql = "SELECT * FROM allievo";
        try (Connection con = DatabaseConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql); // provo a eseguire la query
                ResultSet rs = ps.executeQuery()) { // e la metto in un resultset
            while (rs.next()) { // leggo finchè posso
                Allievo usr = new Allievo(rs.getInt("id"), rs.getString("username"), rs.getString("password"),
                        rs.getString("email"), rs.getString("nome"), rs.getString("cognome"));
                allievos.add(usr);
            }
        } catch (SQLException e) {
            throw new DAOException("Impossibile ottenere gli utenti", e);
        }
        return allievos;
    }

    /**
     * {@inheritDoc}
     * <p>
     * La ricerca è case-insensitive grazie a {@code LOWER(email) = LOWER(?)}.
     * Se nessun allievo corrisponde, viene lanciata
     * {@link AllievoNotFoundException}.
     * </p>
     */
    @Override
    public Allievo getAllievoByEmail(String email) throws DAOException, AllievoNotFoundException {
        String sql = "SELECT * FROM allievo WHERE LOWER(email) = LOWER(?)";
        Allievo allievo;
        try (Connection con = DatabaseConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    allievo = new Allievo(rs.getInt("id"), rs.getString("username"), rs.getString("password"),
                            rs.getString("email"), rs.getString("nome"), rs.getString("cognome"));
                } else {
                    throw new AllievoNotFoundException("L'email non esiste.");
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Errore durante la ricerca per email dell'allievo", e);
        }
        return allievo;
    }

    /**
     * {@inheritDoc}
     * <p>
     * La ricerca è case-insensitive grazie a {@code LOWER(username) = LOWER(?)}.
     * Se nessun allievo corrisponde, viene lanciata
     * {@link AllievoNotFoundException}.
     * </p>
     */
    @Override
    public Allievo getAllievoById(int id) throws DAOException, AllievoNotFoundException {
        String sql = "SELECT * FROM allievo WHERE id = ?";
        Allievo allievo;
        try (Connection con = DatabaseConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    allievo = new Allievo(rs.getInt("id"), rs.getString("username"), rs.getString("password"),
                            rs.getString("email"), rs.getString("nome"), rs.getString("cognome"));
                } else {
                    throw new AllievoNotFoundException("L'id non esiste.");
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Errore durante la ricerca per id dell'allievo", e);
        }
        return allievo;
    }

    /**
     * {@inheritDoc}
     * <p>
     * La ricerca è case-insensitive grazie a {@code LOWER(username) = LOWER(?)}.
     * Se nessun allievo corrisponde, viene lanciata
     * {@link AllievoNotFoundException}.
     * </p>
     */
    public Allievo getAllievoByUsername(String username) throws DAOException, AllievoNotFoundException {
        String sql = "SELECT * FROM allievo WHERE LOWER(username) = LOWER(?)";
        Allievo allievo;
        try (Connection con = DatabaseConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    allievo = new Allievo(rs.getInt("id"), rs.getString("username"), rs.getString("password"),
                            rs.getString("email"), rs.getString("nome"), rs.getString("cognome"));
                } else {
                    throw new AllievoNotFoundException("L'username non esiste.");
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Errore durante la ricerca per username dell'allievo", e);
        }
        return allievo;

    }

    /**
     * {@inheritDoc}
     * <p>
     * Aggiorna i campi {@code nome}, {@code cognome}, {@code email} e
     * {@code username}
     * dell'allievo identificato dal campo {@code id} dell'oggetto passato.
     * </p>
     */
    @Override
    public void updateAllievo(Allievo allievo) throws DAOException {
        String sql = "UPDATE allievo SET nome=?,cognome=?,email=?,username=? WHERE id=?";
        try (Connection con = DatabaseConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, allievo.getName());
            ps.setString(2, allievo.getSurname());
            ps.setString(3, allievo.getEmail());
            ps.setString(4, allievo.getUsername());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("Impossibile aggiornare utente", e);
        }
    }

    /**
     * {@inheritDoc}
     * <p>
     * Esegue un {@code DELETE FROM allievo WHERE id = ?} utilizzando l'ID
     * dell'oggetto {@code allievo} passato come parametro.
     * </p>
     */
    @Override
    public void deleteAllievo(Allievo allievo) throws DAOException {
        String sql = "DELETE FROM allievo WHERE id=?";
        try (Connection con = DatabaseConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, allievo.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("Impossibile eliminare utente", e);
        }
    }

}
