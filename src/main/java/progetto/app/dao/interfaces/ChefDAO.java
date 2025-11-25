package progetto.app.dao.interfaces;

import progetto.app.exception.DAOException;
import progetto.app.model.Chef;

import java.util.List;

public interface ChefDAO {

    void addChef(Chef ch) throws DAOException;

    Chef getChefById(int id) throws DAOException;

    Chef getChefByEmail(String email) throws DAOException;

    Chef getChefByUsername(String username) throws DAOException;

    List<Chef> getallChefs() throws DAOException; // devo gestire la flag

    void updateChef(Chef ch) throws DAOException;

    void deleteChef(Chef ch) throws DAOException;
}
