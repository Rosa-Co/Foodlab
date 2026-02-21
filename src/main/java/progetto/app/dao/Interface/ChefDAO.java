package progetto.app.dao.Interface;

import progetto.app.exception.DAOException;
import progetto.app.model.Chef;

import java.util.List;

/**
 * Interfaccia DAO per la gestione degli chef nel sistema Foodlab.
 * <p>
 * Definisce le operazioni CRUD (Create, Read, Update, Delete) per le entità
 * {@link Chef}, astraendo il meccanismo di persistenza sottostante.
 * Le implementazioni concrete (es.
 * {@link progetto.app.dao.postgree.ChefDAO_Postgree})
 * si occupano di interagire con il database PostgreSQL tramite JDBC.
 * </p>
 */
public interface ChefDAO {

    /**
     * Inserisce un nuovo chef nel database.
     * <p>
     * Prima della INSERT verifica che non esista già uno chef con lo stesso
     * username o la stessa email (confronto case-insensitive). In caso di duplicato
     * lancia {@link progetto.app.exception.DuplicateChefException}.
     * Se l'inserimento ha successo, aggiorna l'ID dell'oggetto {@code ch}
     * con la chiave generata dal database.
     * </p>
     *
     * @param ch l'entità {@link Chef} da persistere
     * @throws DAOException                                  in caso di errore di
     *                                                       accesso al database
     * @throws progetto.app.exception.DuplicateChefException se esiste già uno chef
     *                                                       con le stesse
     *                                                       credenziali
     */
    void addChef(Chef ch) throws DAOException;

    /**
     * Recupera uno chef tramite il suo ID univoco.
     *
     * @param id l'identificatore numerico dello chef
     * @return l'entità {@link Chef} corrispondente all'ID
     * @throws DAOException                                 in caso di errore di
     *                                                      accesso al database
     * @throws progetto.app.exception.ChefNotFoundException se nessuno chef
     *                                                      corrisponde all'ID
     *                                                      fornito
     */
    Chef getChefById(int id) throws DAOException;

    /**
     * Recupera uno chef tramite il suo indirizzo email (case-insensitive).
     *
     * @param email l'indirizzo email da cercare
     * @return l'entità {@link Chef} corrispondente all'email
     * @throws DAOException                                 in caso di errore di
     *                                                      accesso al database
     * @throws progetto.app.exception.ChefNotFoundException se nessuno chef
     *                                                      corrisponde all'email
     *                                                      fornita
     */
    Chef getChefByEmail(String email) throws DAOException;

    /**
     * Recupera uno chef tramite il suo username (case-insensitive).
     *
     * @param username lo username da cercare
     * @return l'entità {@link Chef} corrispondente allo username
     * @throws DAOException                                 in caso di errore di
     *                                                      accesso al database
     * @throws progetto.app.exception.ChefNotFoundException se nessuno chef
     *                                                      corrisponde allo
     *                                                      username fornito
     */
    Chef getChefByUsername(String username) throws DAOException;

    /**
     * Recupera la lista di tutti gli chef registrati nel sistema.
     *
     * @return lista di {@link Chef}; lista vuota se non sono presenti chef
     * @throws DAOException in caso di errore di accesso al database
     */
    List<Chef> getallChefs() throws DAOException; // devo gestire la flag

    /**
     * Aggiorna i dati anagrafici di uno chef esistente.
     *
     * @param ch l'entità {@link Chef} con i nuovi valori da persistere
     * @throws DAOException in caso di errore di accesso al database
     */
    void updateChef(Chef ch) throws DAOException;

    /**
     * Elimina uno chef dal database tramite il suo ID.
     *
     * @param ch l'entità {@link Chef} da eliminare (viene utilizzato il campo
     *           {@code id})
     * @throws DAOException in caso di errore di accesso al database
     */
    void deleteChef(Chef ch) throws DAOException;
}
