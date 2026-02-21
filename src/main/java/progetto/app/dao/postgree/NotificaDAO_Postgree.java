package progetto.app.dao.postgree;

import progetto.app.dao.Interface.NotificaDAO;
import progetto.app.database.DatabaseConnection;
import progetto.app.exception.DAOException;
import progetto.app.model.Notifica;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementazione PostgreSQL dell'interfaccia {@link NotificaDAO}.
 * <p>
 * Gestisce la persistenza delle entità {@link Notifica} sulla tabella
 * {@code avvisi}
 * del database PostgreSQL, utilizzando JDBC tramite {@link DatabaseConnection}.
 * </p>
 * <p>
 * Il campo {@code id_corso} può essere {@code NULL} (notifica generica non
 * associata a nessun corso particolare); in questo caso viene usato
 * {@link PreparedStatement#setNull(int, int)} con tipo {@link Types#INTEGER}.
 * </p>
 */
public class NotificaDAO_Postgree implements NotificaDAO {

    /**
     * {@inheritDoc}
     * <p>
     * Le notifiche vengono recuperate ordinate per {@code id_avviso DESC},
     * quindi le più recenti appaiono per prime.
     * Il campo {@code id_corso} è letto via {@code rs.getObject()} per gestire
     * correttamente i valori {@code NULL} del database.
     * </p>
     */
    @Override
    public List<Notifica> getNotificheByChef(int chefId) throws DAOException {
        List<Notifica> notifiche = new ArrayList<>();
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

    /**
     * {@inheritDoc}
     * <p>
     * Se {@link Notifica#getIdCorso()} è non-null, il valore viene impostato come
     * intero; altrimenti il parametro viene impostato a {@code NULL} SQL.
     * In caso di successo aggiorna il campo {@code idAvviso} dell'oggetto
     * {@code notifica} con la chiave generata dal database.
     * </p>
     */
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
