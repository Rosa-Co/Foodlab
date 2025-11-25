package progetto.app.dao.Interface;

import progetto.app.exception.DAOException;
import progetto.app.model.Ricetta;
import java.util.List;

public interface RicettaDAO {
    List<Ricetta> getAllRicette() throws DAOException;

    List<Ricetta> getRicetteByChef(int chefId) throws DAOException;

    void addRicettaSessione(int sessioneId, int ricettaId) throws DAOException;
}
