package progetto.app.dao.postgree;

import progetto.app.dao.Interface.NotificaDAO;
import progetto.app.database.DatabaseConnection;
import progetto.app.exception.DAOException;
import progetto.app.model.Notifica;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NotificaDAO_Postgree implements NotificaDAO {

    @Override
    public List<Notifica> getNotificheByChef(int chefId) throws DAOException {
        List<Notifica> notifiche = new ArrayList<>();
        // Updated query for real schema
        String sql = "SELECT * FROM avvisi WHERE id_chef = ? ORDER BY id_avviso DESC";

        try (Connection con = DatabaseConnection.getConnection();
                PreparedStatement st = con.prepareStatement(sql)) {

            st.setInt(1, chefId);
            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    int idAvviso = rs.getInt("id_avviso");
                    String titolo = rs.getString("titolo");
                    String contenuto = rs.getString("contenuto");
                    int idChef = rs.getInt("id_chef");
                    Integer idCorso = (Integer) rs.getObject("id_corso");

                    notifiche.add(new Notifica(idAvviso, titolo, contenuto, idChef, idCorso));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DAOException("Errore nel recupero delle notifiche: " + e.getMessage());
        }
        return notifiche;
    }

    @Override
    public int addNotifica(Notifica notifica) throws DAOException {
        String sql = "INSERT INTO avvisi (titolo, contenuto, id_chef, id_corso) VALUES (?, ?, ?, ?)";
        int generatedId = -1;

        try (Connection con = DatabaseConnection.getConnection();
                PreparedStatement st = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            st.setString(1, notifica.getTitolo());
            st.setString(2, notifica.getContenuto());
            st.setInt(3, notifica.getIdChef());

            if (notifica.getIdCorso() != null) {
                st.setInt(4, notifica.getIdCorso());
            } else {
                st.setNull(4, Types.INTEGER);
            }

            st.executeUpdate();

            try (ResultSet generatedKeys = st.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    generatedId = generatedKeys.getInt(1);
                    notifica.setIdAvviso(generatedId);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DAOException("Errore nell'inserimento della notifica: " + e.getMessage());
        }
        return generatedId;
    }
}
