package progetto.app.dao.postgree;

import progetto.app.dao.Interface.ChefDAO;
import progetto.app.database.DatabaseConnection;
import progetto.app.exception.*;
import progetto.app.model.Chef;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ChefDAO_Postgree implements ChefDAO {

    private static final String UNIQUE_VIOLATION = "23505";

    public ChefDAO_Postgree() {}

    @Override
    public void addChef(Chef chef) throws DAOException{
        String checkSql = "SELECT COUNT(*) FROM chef WHERE LOWER(username) = LOWER(?) OR LOWER(email) = LOWER(?)";
        try(Connection con=DatabaseConnection.getConnection()) {
            try(PreparedStatement checkPs = con.prepareStatement(checkSql)) {
                checkPs.setString(1, chef.getUsername());
                checkPs.setString(2, chef.getEmail());
                ResultSet checkRs = checkPs.executeQuery();

                if(checkRs.next() && checkRs.getInt(1) > 0) {
                    throw new DuplicateChefException("Account già esistente.");
                }
            }
            String sql = "INSERT INTO chef (username,password,nome,cognome,email) VALUES (?,?,?,?,?)";
            try(PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, chef.getUsername());
                ps.setString(2, chef.getPassword());
                ps.setString(3, chef.getName());
                ps.setString(4, chef.getSurname());
                ps.setString(5, chef.getEmail());
                ps.executeUpdate();
                ResultSet rs = ps.getGeneratedKeys();
                if(rs.next()) {
                    int id = rs.getInt(1);
                    chef.setId(id);
                }
            }
        } catch(SQLException e) {
            if (UNIQUE_VIOLATION.equals(e.getSQLState())){
                throw new DuplicateChefException("Account già esistente.");
            }
            else {
                throw new DAOException("Impossibile aggiungere utente");
            }
        }
    }

    @Override
    public List<Chef> getallChefs() throws DAOException {
        List<Chef> chefs = new ArrayList<>();
        String sql = "SELECT * FROM chef";
        try(Connection con = DatabaseConnection.getConnection();PreparedStatement ps = con.prepareStatement(sql); //provo a eseguire la query
            ResultSet rs = ps.executeQuery()){ //e la metto in un resultset
            while(rs.next()){ //leggo finchè posso
                Chef ch= new Chef(rs.getInt("id"),rs.getString("username"),rs.getString("password"),rs.getString("email"),rs.getString("nome"),rs.getString("cognome"));
                chefs.add(ch);
            }
        } catch (SQLException e) {
            throw new DAOException("Impossibile ottenere gli chef", e);
        }
        return chefs;
    }

    public Chef getChefByUsername(String username) throws DAOException, ChefNotFoundException {
        String sql = "SELECT * FROM chef WHERE LOWER(username)=LOWER(?)";
        Chef ch = null;
        try(Connection con = DatabaseConnection.getConnection();PreparedStatement ps= con.prepareStatement(sql)){
            ps.setString(1,username);
            try(ResultSet rs=ps.executeQuery()){
                if(rs.next()){
                    ch= new Chef(rs.getInt("id"),rs.getString("username"),rs.getString("password"),rs.getString("email"),rs.getString("nome"),rs.getString("cognome"));
                }else{
                    throw new ChefNotFoundException("L'username non esiste");
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Errore durante la ricerca per username dello chef", e);
        }
        return ch;
    }

    @Override
    public Chef getChefByEmail(String email) throws DAOException,ChefNotFoundException{
        String sql = "SELECT * FROM chef WHERE LOWER(email) = LOWER(?)";
        Chef ch = null;
        try (Connection con = DatabaseConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    ch= new Chef(rs.getInt("id"),rs.getString("username"),rs.getString("password"),rs.getString("email"),rs.getString("nome"),rs.getString("cognome"));
                } else {
                    throw new ChefNotFoundException("L'email non esiste");
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Errore durante la ricerca per email dello chef", e);
        }
        return ch;
    }

    @Override
    public Chef getChefById(int id) throws DAOException, ChefNotFoundException {
        String sql = "SELECT * FROM chef WHERE id = ?";//devo aggiungere la cond?
        Chef ch = null;
        try (Connection con = DatabaseConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    ch= new Chef(rs.getInt("id"),rs.getString("username"),rs.getString("password"),rs.getString("email"),rs.getString("nome"),rs.getString("cognome"));
                } else {
                    throw new ChefNotFoundException("L'id non esiste");
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Errore durante la ricerca per id dello chef", e);
        }
        return ch;
    }

    @Override
    public void updateChef(Chef ch) throws DAOException {
        String sql = "UPDATE chef SET nome=?,cognome=?,email=?,username=? WHERE id=?";// devo aggiungere la cond?
        try (Connection con = DatabaseConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, ch.getName());
            ps.setString(2, ch.getSurname());
            ps.setString(3, ch.getEmail());
            ps.setString(4, ch.getUsername());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("Impossibile aggiornare utente", e);
        }
    }

    @Override
    public void deleteChef(Chef ch) throws DAOException {
        String sql = "DELETE FROM chef WHERE id=?";
        try (Connection con = DatabaseConnection.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, ch.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("Impossibile eliminare utente", e);
        }
    }
}
