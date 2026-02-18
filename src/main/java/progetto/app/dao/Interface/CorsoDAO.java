package progetto.app.dao.Interface;

import progetto.app.exception.DAOException;
import progetto.app.exception.DuplicateCorsoException;
import progetto.app.model.Corso;
import java.util.List;

public interface CorsoDAO {
    void addCorso(Corso corso) throws DAOException, DuplicateCorsoException;

    List<Corso> getAllCorsi() throws DAOException;

    List<Corso> getCorsiByChef(int chefId) throws DAOException;
}
