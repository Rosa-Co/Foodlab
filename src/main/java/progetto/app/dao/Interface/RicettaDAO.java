package progetto.app.dao.Interface;

import progetto.app.exception.DAOException;
import progetto.app.model.Ricetta;

import java.sql.SQLException;
import java.util.List;

public interface RicettaDAO {
    List<Ricetta> getAllRicette() throws DAOException;

    List<Ricetta> getRicetteByChef(int chefId) throws DAOException;

    int addRicetta(Ricetta ricetta) throws DAOException, SQLException;

    void addRicettaSessione(int sessioneId, int ricettaId) throws DAOException;
}
