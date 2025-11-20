package progetto.app.dao.postgree;

import progetto.app.dao.Interface.ChefDAO;
import progetto.app.database.DatabaseConnection;
import progetto.app.exception.DAOException;
import progetto.app.model.Chef;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ChefDAO_Postgree implements ChefDAO {

    public ChefDAO_Postgree() {}

    @Override
    public void addChef(Chef chef) throws DAOException{

        String sql = "INSERT INTO chef (username,password,nome,cognome,email) VALUES (?,?,?,?,?)";
        try(Connection con = DatabaseConnection.getConnection();PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1,chef.getUsername());
            ps.setString(2,chef.getPassword());
            ps.setString(3,chef.getName());
            ps.setString(4,chef.getSurname());
            ps.setString(5,chef.getEmail());
            ResultSet rs = ps.getGeneratedKeys();
            if(rs.next()) {
                int id = rs.getInt(1);
                chef.setId(id);
            }
            ps.executeUpdate();
        } catch(SQLException e) {
            throw new DAOException("Impossibile aggiungere utente",e);
        }
    }

    @Override
    public List<Chef> getallChefs() throws DAOException {
        List<Chef> chefs = new ArrayList<>();
        String sql = "SELECT * FROM chef";
        try(Connection con = DatabaseConnection.getConnection();PreparedStatement ps = con.prepareStatement(sql); //provo a eseguire la query
            ResultSet rs = ps.executeQuery()){ //e la metto in un resultset
            while(rs.next()){ //leggo finchè posso
                Chef ch= new Chef(rs.getString("username"),rs.getString("password"),rs.getString("nome"),rs.getString("cognome"),rs.getString("email"));
                chefs.add(ch);
            }
        } catch(SQLException e) {
            throw new DAOException("Impossibile ottenere gli utenti",e);
        }
        return chefs;
    }

    public Chef getChefByUsername(String username) throws DAOException{
        String sql = "SELECT * FROM chef WHERE LOWER(username)=LOWER(?)";
        Chef ch = null;
        try(Connection con = DatabaseConnection.getConnection();PreparedStatement ps= con.prepareStatement(sql)){
            ps.setString(1,username);
            try(ResultSet rs=ps.executeQuery()){
                if(rs.next()){
                    ch= new Chef(rs.getString("username"),rs.getString("password"),rs.getString("email"),rs.getString("nome"),rs.getString("cognome"));
                }
            }catch(SQLException e){
                throw new DAOException("Username non trovato",e);
            }
        }catch(SQLException e){
            throw new DAOException("Errore durante la ricerca per username dello chef",e);
        }
        return ch;
    }

    @Override
    public Chef getChefByEmail(String email) throws DAOException {
        String sql = "SELECT * FROM chef WHERE LOWER(email) = LOWER(?)";
        Chef ch = null;
        try(Connection con = DatabaseConnection.getConnection();PreparedStatement ps = con.prepareStatement(sql)){
            ps.setString(1,email);
            try(ResultSet rs = ps.executeQuery()) {
                if(rs.next()) {
                    ch= new Chef(rs.getString("username"),rs.getString("password"),rs.getString("email"),rs.getString("nome"),rs.getString("cognome"));
                }
            } catch(SQLException e) {
                throw new DAOException("Email non trovata");
            }
        }catch(SQLException e) {
            throw new DAOException("Errore durante la ricerca per email dello chef",e);
        }
        return ch;
    }


    @Override
    public Chef getChefById(int id) throws DAOException {
        String sql = "SELECT * FROM chef WHERE id = ?";//devo aggiungere la cond?
        Chef ch = null;
        try(Connection con = DatabaseConnection.getConnection();PreparedStatement ps = con.prepareStatement(sql)){
            ps.setInt(1,id);
            try(ResultSet rs = ps.executeQuery()) {
                if(rs.next()) {
                    ch= new Chef(rs.getString("username"),rs.getString("password"),rs.getString("email"),rs.getString("nome"),rs.getString("cognome"));                }
                } catch(SQLException e) {
                    throw new DAOException("Id non trovato",e);
                }
            }catch(SQLException e) {
                throw new DAOException("Errore durante la ricerca per id dello chef",e);
            }
        return ch;
    }

    @Override
    public void updateChef(Chef ch) throws DAOException {
        String sql="UPDATE chef SET nome=?,cognome=?,email=?,username=? WHERE id=?";//devo aggiungere la cond?
        try(Connection con = DatabaseConnection.getConnection();PreparedStatement ps = con.prepareStatement(sql)){
            ps.setString(1,ch.getName());
            ps.setString(2,ch.getSurname());
            ps.setString(3,ch.getEmail());
            ps.setString(4,ch.getUsername());
            ps.executeUpdate();
        } catch(SQLException e) {
            throw new DAOException("Impossibile aggiornare utente",e);
        }
    }

    @Override
    public void deleteChef(Chef ch) throws DAOException {
        String sql="DELETE FROM chef WHERE id=?";
        try(Connection con = DatabaseConnection.getConnection();PreparedStatement ps = con.prepareStatement(sql)){
            ps.setInt(1,ch.getId());
            ps.executeUpdate();
        } catch(SQLException e) {
            throw new DAOException("Impossibile eliminare utente",e);
        }
    }
}
