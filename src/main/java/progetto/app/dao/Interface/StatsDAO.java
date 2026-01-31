package progetto.app.dao.Interface;

import progetto.app.dto.ChefStatsDTO;
import progetto.app.exception.DAOException;

public interface StatsDAO {
    ChefStatsDTO getChefStats(int chefId) throws DAOException;
}
