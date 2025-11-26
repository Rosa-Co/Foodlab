package progetto.app.dao.postgree;

import progetto.app.dao.Interface.RicettaDAO;
import progetto.app.database.DatabaseConnection;
import progetto.app.exception.DAOException;
import progetto.app.model.Ricetta;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RicettaDAO_Postgree implements RicettaDAO {

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

    @Override
    public int addRicetta(Ricetta ricetta) throws DAOException {
        String sql = "INSERT INTO ricetta (nome, descrizione, categoria, chef_id) VALUES (?, ?, ?, ?) RETURNING id";
        try (Connection con = DatabaseConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, ricetta.getNome());
            ps.setString(2, ricetta.getDescrizione());
            ps.setString(3, ricetta.getCategoria());
            ps.setInt(4, ricetta.getChefId());
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
            throw new DAOException("Impossibile aggiungere la ricetta", e);
        }
    }

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

    private Ricetta mapRowToRicetta(ResultSet rs) throws SQLException {
        return new Ricetta(
                rs.getInt("id"),
                rs.getString("nome"),
                rs.getString("descrizione"),
                rs.getString("categoria"),
                rs.getInt("chef_id"));
    }
}
