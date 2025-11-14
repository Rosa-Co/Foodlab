package progetto.app.dao.Interface;

import progetto.app.model.User;

import java.sql.SQLException;
import java.util.List;

public interface UserDAO {

    void  addUser(User user) throws SQLException;

    User getUserById(int id) throws SQLException;

    User getUserByEmail(String email) throws SQLException;

    List<User> getAllUsers() throws SQLException;

    void  updateUser(User user) throws SQLException;

    void  deleteUser(User user) throws SQLException;
}
