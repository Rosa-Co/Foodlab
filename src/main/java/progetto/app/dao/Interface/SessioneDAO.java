package progetto.app.dao.Interface;

import progetto.app.exception.DAOException;
import progetto.app.model.Sessione;
import java.util.List;

public interface SessioneDAO {
    void addSessione(Sessione sessione) throws DAOException;

    List<Sessione> getSessioniByCorso(int corsoId) throws DAOException;
}
