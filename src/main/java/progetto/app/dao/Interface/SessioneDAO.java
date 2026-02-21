package progetto.app.dao.Interface;

import progetto.app.exception.DAOException;
import progetto.app.model.Sessione;

import java.util.List;

/**
 * Interfaccia DAO per la gestione delle sessioni nel sistema Foodlab.
 * <p>
 * Definisce le operazioni CRUD per le entità {@link Sessione}, che
 * rappresentano
 * i singoli appuntamenti di un {@link progetto.app.model.Corso}.
 * Ogni sessione ha una modalità ({@code Online} o {@code In Presenza}),
 * una data, una durata e una descrizione.
 * Le sessioni in presenza possono essere associate a ricette tramite
 * {@link RicettaDAO#addRicettaSessione(int, int)}.
 * </p>
 */
public interface SessioneDAO {

    /**
     * Inserisce una nuova sessione nel database.
     * <p>
     * Se l'inserimento ha successo, aggiorna l'ID dell'oggetto {@code sessione}
     * con la chiave generata dal database.
     * </p>
     *
     * @param sessione l'entità {@link Sessione} da persistere
     * @throws DAOException in caso di errore di accesso al database
     */
    void addSessione(Sessione sessione) throws DAOException;

    /**
     * Recupera tutte le sessioni associate a un corso, ordinate per numero di
     * sessione.
     *
     * @param corsoId l'ID del corso di cui recuperare le sessioni
     * @return lista di {@link Sessione} ordinate per {@code numero_sessione}; lista
     *         vuota se non ne esistono
     * @throws DAOException in caso di errore di accesso al database
     */
    List<Sessione> getSessioniByCorso(int corsoId) throws DAOException;

    /**
     * Elimina una sessione dal database tramite il suo ID.
     *
     * @param id l'identificatore numerico della sessione da eliminare
     * @throws DAOException in caso di errore di accesso al database
     */
    void deleteSessione(int id) throws DAOException;

    /**
     * Aggiorna i dati di una sessione esistente.
     * <p>
     * I campi aggiornabili sono: data della sessione, modalità, durata e
     * descrizione.
     * Il campo {@code numero_sessione} non viene modificato da questa operazione.
     * </p>
     *
     * @param sessione l'entità {@link Sessione} con i nuovi valori; l'ID viene
     *                 usato come chiave WHERE
     * @throws DAOException in caso di errore di accesso al database
     */
    void updateSessione(Sessione sessione) throws DAOException;
}
