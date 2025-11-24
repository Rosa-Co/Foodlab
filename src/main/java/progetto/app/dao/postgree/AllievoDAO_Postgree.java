package progetto.app.dao.postgree;

import progetto.app.dao.Interface.AllievoDAO;
import progetto.app.database.DatabaseConnection;
import progetto.app.exception.DAOException;
import progetto.app.exception.DuplicateUserException;
import progetto.app.exception.UserNotFoundException;
import progetto.app.model.Allievo;
import progetto.app.model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AllievoDAO_Postgree implements AllievoDAO {

    public AllievoDAO_Postgree() {}

    @Override
    public void addAllievo(Allievo allievo) throws DAOException{

        String sql = "INSERT INTO allievo (username,password,nome,cognome,email) VALUES (?,?,?,?,?)";
        try(Connection con=DatabaseConnection.getConnection();PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, allievo.getUsername());
            ps.setString(2, allievo.getPassword());
            ps.setString(3, allievo.getName());
            ps.setString(4, allievo.getSurname());
            ps.setString(5, allievo.getEmail());
            ResultSet rs = ps.getGeneratedKeys();
            if(rs.next()) {
                int id = rs.getInt(1);
                allievo.setId(id);
            }
            ps.executeUpdate();
        } catch(SQLException e) {
            if ("23505".equals(e.getSQLState())){//!controlla if
                throw new DuplicateUserException("Account già esistente.");
            }
            else {
                throw new DAOException("Impossibile aggiungere utente");
            }
        }
    }

    @Override
    public List<Allievo> getAllAllievos() throws DAOException {
        List<Allievo> allievos = new ArrayList<>();
        String sql = "SELECT * FROM allievo";
        try(Connection con=DatabaseConnection.getConnection();PreparedStatement ps = con.prepareStatement(sql); //provo a eseguire la query
        ResultSet rs = ps.executeQuery()){ //e la metto in un resultset
            while(rs.next()){ //leggo finchè posso
                Allievo usr= new Allievo(rs.getString("username"),rs.getString("password"),rs.getString("email"),rs.getString("nome"),rs.getString("cognome"));
                allievos.add(usr);
            }
        } catch(SQLException e) {
            throw new DAOException("Impossibile ottenere gli utenti",e);
        }
        return allievos;
    }

    @Override
    public Allievo getAllievoByEmail(String email) throws DAOException, UserNotFoundException {
        String sql = "SELECT * FROM allievo WHERE LOWER(email) = LOWER(?)";
        Allievo allievo = null;
        try(Connection con=DatabaseConnection.getConnection();PreparedStatement ps = con.prepareStatement(sql)){
            ps.setString(1,email);
            try(ResultSet rs = ps.executeQuery()) {
                if(rs.next()) {
                    allievo = new Allievo(rs.getString("username"),rs.getString("password"),rs.getString("email"),rs.getString("nome"),rs.getString("cognome"));
                }
                else{
                    throw new UserNotFoundException("L'email non è corretta.");
                }
            }
        }catch(SQLException e) {
            throw new DAOException("Errore durante la ricerca per email dell'allievo",e);
        }
        return allievo;
    }

    @Override
    public Allievo getAllievoById(int id) throws DAOException,UserNotFoundException {
        String sql = "SELECT * FROM allievo WHERE id = ?";
        Allievo allievo = null;
        try(Connection con=DatabaseConnection.getConnection();PreparedStatement ps = con.prepareStatement(sql)){
            ps.setInt(1,id);
            try(ResultSet rs = ps.executeQuery()) {
                if(rs.next()) {
                    allievo = new Allievo(rs.getString("username"),rs.getString("password"),rs.getString("email"),rs.getString("nome"),rs.getString("cognome"));
                }
                else {
                    throw new UserNotFoundException("L'id non è corretto.");
                }
            }
            }catch(SQLException e) {
                throw new DAOException("Errore durante la ricerca per id dell'allievo",e);
            }
        return allievo;
    }

    public Allievo getAllievoByUsername(String username) throws UserNotFoundException {
        String sql = "SELECT * FROM allievo WHERE LOWER(username) = LOWER(?)";
        Allievo allievo = null;
        try(Connection con=DatabaseConnection.getConnection();PreparedStatement ps = con.prepareStatement(sql)){
            ps.setString(1,username);
            try(ResultSet rs = ps.executeQuery()) {
                if(rs.next()) {
                    allievo = new Allievo(rs.getString("username"),rs.getString("password"),rs.getString("email"),rs.getString("nome"),rs.getString("cognome"));
                }
                else{
                    throw new UserNotFoundException("L'username non è corretto.");
                }
            }
        }catch(SQLException e) {
            throw new DAOException("Errore durante la ricerca per username dell'allievo",e);
        }
        return allievo;

    }

    @Override
    public void updateAllievo(Allievo allievo) throws DAOException {
        String sql="UPDATE allievo SET nome=?,cognome=?,email=?,username=? WHERE id=?";
        try(Connection con=DatabaseConnection.getConnection();PreparedStatement ps = con.prepareStatement(sql)){
            ps.setString(1, allievo.getName());
            ps.setString(2, allievo.getSurname());
            ps.setString(3, allievo.getEmail());
            ps.setString(4, allievo.getUsername());
            ps.executeUpdate();
        } catch(SQLException e) {
            throw new DAOException("Impossibile aggiornare utente",e);
        }
    }

    @Override
    public void deleteAllievo(Allievo allievo) throws DAOException {
        String sql="DELETE FROM allievo WHERE id=?";
        try(Connection con=DatabaseConnection.getConnection();PreparedStatement ps = con.prepareStatement(sql)){
            ps.setInt(1, allievo.getId());
            ps.executeUpdate();
        } catch(SQLException e) {
            throw new DAOException("Impossibile eliminare utente",e);
        }
    }


}
