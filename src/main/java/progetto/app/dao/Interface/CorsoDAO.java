package progetto.app.dao.Interface;

import progetto.app.exception.DAOException;
import progetto.app.exception.DuplicateCorsoException;
import progetto.app.model.Corso;

import java.util.List;

/**
 * Interfaccia DAO per la gestione dei corsi.
 * <p>
 * Definisce le operazioni di persistenza per le entità {@link Corso}.
 * Le implementazioni concrete (es.
 * {@link progetto.app.dao.postgree.CorsoDAO_Postgree})
 * mappano le entità sulla tabella {@code corso} del database PostgreSQL tramite
 * JDBC.
 * </p>
 */
public interface CorsoDAO {

    /**
     * Inserisce un nuovo corso nel database.
     * <p>
     * Se l'inserimento ha successo, aggiorna l'ID dell'oggetto {@code corso}
     * con la chiave generata dal database.
     * </p>
     *
     * @param corso l'entità {@link Corso} da persistere
     * @throws DAOException            in caso di errore generico di accesso al
     *                                 database
     * @throws DuplicateCorsoException se esiste già un corso omonimo nel sistema
     */
    void addCorso(Corso corso) throws DAOException, DuplicateCorsoException;

    /**
     * Recupera la lista di tutti i corsi presenti nel database.
     *
     * @return lista di {@link Corso}; lista vuota se non sono presenti corsi
     * @throws DAOException in caso di errore di accesso al database
     */
    List<Corso> getAllCorsi() throws DAOException;

    /**
     * Recupera la lista dei corsi appartenenti a uno chef specifico.
     * <p>
     * Il numero di sessioni restituito in ciascun {@link Corso} è calcolato
     * tramite sottoquery sul numero reale di sessioni esistenti nel database,
     * anziché sul valore memorizzato nella colonna {@code numero_sessioni}.
     * </p>
     *
     * @param chefId l'ID dello chef di cui recuperare i corsi
     * @return lista di {@link Corso} dello chef; lista vuota se lo chef non ha
     *         corsi
     * @throws DAOException in caso di errore di accesso al database
     */
    List<Corso> getCorsiByChef(int chefId) throws DAOException;
}
