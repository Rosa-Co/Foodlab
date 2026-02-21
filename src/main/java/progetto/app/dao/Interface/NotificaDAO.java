package progetto.app.dao.Interface;

import progetto.app.exception.DAOException;
import progetto.app.model.Notifica;

import java.util.List;

public interface NotificaDAO {
    List<Notifica> getNotificheByChef(int chefId) throws DAOException;

    int addNotifica(Notifica notifica) throws DAOException;
}
