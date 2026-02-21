package progetto.app.dao.Interface;

import progetto.app.exception.DAOException;
import progetto.app.model.Ricetta;

import java.sql.SQLException;
import java.util.List;

/**
 * Interfaccia DAO per la gestione delle ricette nel sistema Foodlab.
 * <p>
 * Definisce le operazioni di lettura, scrittura e associazione per le entità
 * {@link Ricetta}. Le ricette sono collegate agli chef che le creano
 * e possono essere associate alle sessioni in presenza tramite la tabella
 * di join {@code sessione_ricetta}.
 * Le implementazioni concrete (es.
 * {@link progetto.app.dao.postgree.RicettaDAO_Postgree})
 * interagiscono con il database PostgreSQL tramite JDBC.
 * </p>
 */
public interface RicettaDAO {

    /**
     * Recupera tutte le ricette presenti nel database.
     *
     * @return lista di {@link Ricetta}; lista vuota se non ne esistono
     * @throws DAOException in caso di errore di accesso al database
     */
    List<Ricetta> getAllRicette() throws DAOException;

    /**
     * Recupera la lista delle ricette create da uno chef specifico.
     *
     * @param chefId l'ID dello chef di cui recuperare le ricette
     * @return lista di {@link Ricetta} dello chef; lista vuota se non ne esistono
     * @throws DAOException in caso di errore di accesso al database
     */
    List<Ricetta> getRicetteByChef(int chefId) throws DAOException;

    /**
     * Inserisce una nuova ricetta nel database.
     * <p>
     * In caso di inserimento riuscito, aggiorna l'ID dell'oggetto {@code ricetta}
     * con la chiave generata dal database e la restituisce come valore di ritorno.
     * </p>
     *
     * @param ricetta l'entità {@link Ricetta} da persistere
     * @return l'ID generato dal database per la ricetta appena inserita
     * @throws DAOException                                    in caso di errore
     *                                                         generico di accesso
     *                                                         al database
     * @throws progetto.app.exception.DuplicateRecipeException se esiste già una
     *                                                         ricetta con lo stesso
     *                                                         nome per lo stesso
     *                                                         chef
     * @throws SQLException                                    in caso di errore SQL
     *                                                         non gestito
     *                                                         esplicitamente
     */
    int addRicetta(Ricetta ricetta) throws DAOException, SQLException;

    /**
     * Crea l'associazione tra una sessione e una ricetta nella tabella
     * {@code sessione_ricetta}.
     * <p>
     * Utilizzato durante la creazione di una sessione in presenza per collegare
     * le ricette che verranno realizzate durante quella sessione.
     * </p>
     *
     * @param sessioneId l'ID della sessione a cui associare la ricetta
     * @param ricettaId  l'ID della ricetta da associare
     * @throws DAOException in caso di errore di accesso al database
     */
    void addRicettaSessione(int sessioneId, int ricettaId) throws DAOException;
}
