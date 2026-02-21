package progetto.app.dao.Interface;

import progetto.app.exception.DAOException;
import progetto.app.model.Allievo;

import java.util.List;

/**
 * Interfaccia DAO per la gestione degli allievi nel sistema Foodlab.
 * <p>
 * Definisce le operazioni CRUD (Create, Read, Update, Delete) per le entità
 * {@link Allievo}, astraendo il meccanismo di persistenza sottostante.
 * Le implementazioni concrete (es.
 * {@link progetto.app.dao.postgree.AllievoDAO_Postgree})
 * si occupano di interagire con il database PostgreSQL tramite JDBC.
 * </p>
 */
public interface AllievoDAO {

    /**
     * Inserisce un nuovo allievo nel database.
     * <p>
     * Prima della INSERT verifica che non esista già un allievo con lo stesso
     * username o la stessa email (confronto case-insensitive). In caso di duplicato
     * lancia {@link progetto.app.exception.DuplicateAllievoException}.
     * Se l'inserimento ha successo, aggiorna l'ID dell'oggetto {@code allievo}
     * con la chiave generata dal database.
     * </p>
     *
     * @param allievo l'entità {@link Allievo} da persistere
     * @throws DAOException                                     in caso di errore di
     *                                                          accesso al database
     * @throws progetto.app.exception.DuplicateAllievoException se esiste già un
     *                                                          allievo con le
     *                                                          stesse credenziali
     */
    void addAllievo(Allievo allievo) throws DAOException;

    /**
     * Recupera un allievo tramite il suo ID univoco.
     *
     * @param id l'identificatore numerico dell'allievo
     * @return l'entità {@link Allievo} corrispondente all'ID
     * @throws DAOException                                    in caso di errore di
     *                                                         accesso al database
     * @throws progetto.app.exception.AllievoNotFoundException se nessun allievo
     *                                                         corrisponde all'ID
     *                                                         fornito
     */
    Allievo getAllievoById(int id) throws DAOException;

    /**
     * Recupera un allievo tramite il suo indirizzo email (case-insensitive).
     *
     * @param email l'indirizzo email da cercare
     * @return l'entità {@link Allievo} corrispondente all'email
     * @throws DAOException                                    in caso di errore di
     *                                                         accesso al database
     * @throws progetto.app.exception.AllievoNotFoundException se nessun allievo
     *                                                         corrisponde all'email
     *                                                         fornita
     */
    Allievo getAllievoByEmail(String email) throws DAOException;

    /**
     * Recupera un allievo tramite il suo username (case-insensitive).
     *
     * @param username lo username da cercare
     * @return l'entità {@link Allievo} corrispondente allo username
     * @throws DAOException                                    in caso di errore di
     *                                                         accesso al database
     * @throws progetto.app.exception.AllievoNotFoundException se nessun allievo
     *                                                         corrisponde allo
     *                                                         username fornito
     */
    Allievo getAllievoByUsername(String username) throws DAOException;

    /**
     * Recupera la lista di tutti gli allievi registrati nel sistema.
     *
     * @return lista di {@link Allievo}; lista vuota se non sono presenti allievi
     * @throws DAOException in caso di errore di accesso al database
     */
    List<Allievo> getAllAllievos() throws DAOException;

    /**
     * Aggiorna i dati anagrafici di un allievo esistente.
     *
     * @param allievo l'entità {@link Allievo} con i nuovi valori da persistere
     * @throws DAOException in caso di errore di accesso al database
     */
    void updateAllievo(Allievo allievo) throws DAOException;

    /**
     * Elimina un allievo dal database tramite il suo ID.
     *
     * @param allievo l'entità {@link Allievo} da eliminare (viene utilizzato il
     *                campo {@code id})
     * @throws DAOException in caso di errore di accesso al database
     */
    void deleteAllievo(Allievo allievo) throws DAOException;
}
