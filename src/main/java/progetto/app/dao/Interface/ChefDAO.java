package progetto.app.dao.Interface;

import progetto.app.model.User;

import java.sql.SQLException;
import java.util.List;

public interface ChefDAO {

    void addChef(User user) throws SQLException;

    User getChefById(int id) throws SQLException;

    User getChefByEmail(String email) throws SQLException;

    List<User> getallChefs() throws SQLException; //devo gestire la flag

    void updateChef(User user) throws SQLException;

    void deleteChef(User user) throws SQLException;
}
