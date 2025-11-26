package progetto.app.dao.Interface;

import progetto.app.exception.DAOException;
import progetto.app.model.Corso;
import java.util.List;

public interface CorsoDAO {
    void addCorso(Corso corso) throws DAOException;

    List<Corso> getAllCorsi() throws DAOException;

    List<Corso> getCorsiByChef(int chefId) throws DAOException;
}
