package progetto.app.dao.postgree;

import progetto.app.dao.Interface.CorsoDAO;
import progetto.app.database.DatabaseConnection;
import progetto.app.exception.DAOException;
import progetto.app.model.Corso;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CorsoDAO_Postgree implements CorsoDAO {

    @Override
    public void addCorso(Corso corso) throws DAOException {
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
            e.printStackTrace();
            throw new DAOException("Impossibile aggiungere il corso", e);
        }
    }

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

    @Override
    public List<Corso> getCorsiByChef(int chefId) throws DAOException {
        List<Corso> corsi = new ArrayList<>();
        String sql = "SELECT * FROM corso WHERE chef_id = ?";
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
