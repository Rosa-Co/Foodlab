package progetto.app.dao.Interface;

import progetto.app.exception.DAOException;
import progetto.app.model.Allievo;

import java.util.List;

public interface AllievoDAO {

    void addAllievo(Allievo allievo) throws DAOException;

    Allievo getAllievoById(int id) throws DAOException;

    Allievo getAllievoByEmail(String email) throws DAOException;

    Allievo getAllievoByUsername(String username) throws DAOException;

    List<Allievo> getAllAllievos() throws DAOException;

    void  updateAllievo(Allievo allievo) throws DAOException;

    void  deleteAllievo(Allievo allievo) throws DAOException;
}
