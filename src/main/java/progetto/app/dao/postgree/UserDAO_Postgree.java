package progetto.app.dao.postgree;

import progetto.app.dao.Interface.UserDAO;
import progetto.app.model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO_Postgree implements UserDAO {

    private Connection con;

    public UserDAO_Postgree(Connection con) {
        this.con = con;
    }

    @Override
    public void addUser(User user) throws SQLException{

        String sql = "INSERT INTO user (username,password,nome,cognome,email) VALUES (?,?,?,?,?,?)";
        try(PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {;
            ps.setString(1,user.getUsername());
            ps.setString(2,user.getPassword());
            ps.setString(3,user.getName());
            ps.setString(4,user.getSurname());
            ps.setString(5,user.getEmail());
            ResultSet rs = ps.getGeneratedKeys();
            if(rs.next()) {
                int id = rs.getInt(1);
                user.setId(id);
            }
            ps.executeUpdate();
        }
    }

    @Override
    public List<User> getAllUsers() throws SQLException {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM user"; //query per selezionare tutti gli users
        try(PreparedStatement ps = con.prepareStatement(sql); //provo a eseguire la query
        ResultSet rs = ps.executeQuery()){ //e la metto in un resultset
            while(rs.next()){ //leggo finchè posso
                User usr= new User(rs.getString("username"),rs.getString("password"),rs.getString("nome"),rs.getString("cognome"),rs.getString("email"));
                users.add(usr);
            }
        }
        return users;
    }

    @Override
    public User getUserByEmail(String email) throws SQLException {
        String sql = "SELECT * FROM user WHERE email = ?";
        User user = null;
        try(PreparedStatement ps = con.prepareStatement(sql)){
            ps.setString(1,email);
            try(ResultSet rs = ps.executeQuery()) {
                if(rs.next()) {
                    user= new User(rs.getString("username"),rs.getString("password"),rs.getString("nome"),rs.getString("cognome"),rs.getString("email"));
                }
            }
        }
        return user;
    }

    @Override
    public User getUserById(int id) throws SQLException {
        String sql = "SELECT * FROM user WHERE id = ?";
        User user = null;
        try(PreparedStatement ps = con.prepareStatement(sql)){
            ps.setInt(1,id);
            try(ResultSet rs = ps.executeQuery()) {
                if(rs.next()) {
                    user= new User(rs.getString("username"),rs.getString("password"),rs.getString("nome"),rs.getString("cognome"),rs.getString("email"));
                }
            }
        }
        return user;
    }

    @Override
    public void updateUser(User user) throws SQLException {
        String sql="UPDATE user SET nome=?,cognome=?,email=?,username=? WHERE id=?";
        try(PreparedStatement ps = con.prepareStatement(sql)){
            ps.setString(1,user.getName());
            ps.setString(2,user.getSurname());
            ps.setString(3,user.getEmail());
            ps.setString(4,user.getUsername());
            ps.executeUpdate();
        }
    }

    @Override
    public void deleteUser(User user) throws SQLException {
        String sql="DELETE FROM user WHERE id=?";
        try(PreparedStatement ps = con.prepareStatement(sql)){
            ps.setInt(1,user.getId());
            ps.executeUpdate();
        }
    }


}
