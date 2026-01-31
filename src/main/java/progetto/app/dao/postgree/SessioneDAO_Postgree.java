package progetto.app.dao.postgree;

import progetto.app.dao.Interface.SessioneDAO;
import progetto.app.database.DatabaseConnection;
import progetto.app.exception.DAOException;
import progetto.app.model.Sessione;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SessioneDAO_Postgree implements SessioneDAO {

    @Override
    public void addSessione(Sessione sessione) throws DAOException {
        String sql = "INSERT INTO sessione (corso_id, numero_sessione, data_sessione, modalita, durata, descrizione) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection con = DatabaseConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, sessione.getCorsoId());
            ps.setInt(2, sessione.getNumeroSessione());
            ps.setDate(3, Date.valueOf(sessione.getDataSessione()));
            ps.setString(4, sessione.getModalita());
            ps.setInt(5, sessione.getDurata());
            ps.setString(6, sessione.getDescrizione());

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    sessione.setId(rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Impossibile aggiungere la sessione", e);
        }
    }

    @Override
    public List<Sessione> getSessioniByCorso(int corsoId) throws DAOException {
        List<Sessione> sessioni = new ArrayList<>();
        String sql = "SELECT * FROM sessione WHERE corso_id = ? ORDER BY numero_sessione";
        try (Connection con = DatabaseConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, corsoId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    sessioni.add(new Sessione(
                            rs.getInt("id"),
                            rs.getInt("corso_id"),
                            rs.getInt("numero_sessione"),
                            rs.getDate("data_sessione").toLocalDate(),
                            rs.getString("modalita"),
                            rs.getInt("durata"),
                            rs.getString("descrizione")));
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Impossibile recuperare le sessioni del corso", e);
        }
        return sessioni;
    }

    @Override
    public void deleteSessione(int id) throws DAOException {
        String sql = "DELETE FROM sessione WHERE id = ?";
        try (Connection con = DatabaseConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("Impossibile eliminare la sessione: " + e.getMessage(), e);
        }
    }

    @Override
    public void updateSessione(Sessione sessione) throws DAOException {
        String sql = "UPDATE sessione SET data_sessione=?, modalita=?, durata=?, descrizione=? WHERE id=?";
        try (Connection con = DatabaseConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(sessione.getDataSessione()));
            ps.setString(2, sessione.getModalita());
            ps.setInt(3, sessione.getDurata());
            ps.setString(4, sessione.getDescrizione());
            ps.setInt(5, sessione.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("Impossibile aggiornare la sessione: " + e.getMessage(), e);
        }
    }
}
