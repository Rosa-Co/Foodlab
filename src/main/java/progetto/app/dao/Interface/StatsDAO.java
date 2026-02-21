package progetto.app.dao.Interface;

import progetto.app.dto.ChefStatsDTO;
import progetto.app.exception.DAOException;

/**
 * Interfaccia DAO per il recupero delle statistiche aggregate nel sistema
 * Foodlab.
 * <p>
 * Fornisce metodi di sola lettura che aggregano dati da più tabelle del
 * database
 * (corsi, sessioni, ricette) per produrre report riepilogativi.
 * Le implementazioni concrete (es.
 * {@link progetto.app.dao.postgree.StatsDAO_Postgree})
 * eseguono query SQL complesse tramite JDBC su PostgreSQL.
 * </p>
 */
public interface StatsDAO {

    /**
     * Calcola e restituisce le statistiche aggregate per uno chef specifico.
     * <p>
     * Le statistiche comprendono:
     * <ul>
     * <li>Numero totale di corsi dello chef</li>
     * <li>Numero di sessioni online</li>
     * <li>Numero di sessioni in presenza</li>
     * <li>Media, massimo e minimo del numero di ricette per sessione in
     * presenza</li>
     * </ul>
     * </p>
     *
     * @param chefId l'ID dello chef di cui calcolare le statistiche
     * @return un {@link ChefStatsDTO} popolato con i dati aggregati
     * @throws DAOException in caso di errore di accesso al database
     */
    ChefStatsDTO getChefStats(int chefId) throws DAOException;
}
