package progetto.app.dao.Interface;

import progetto.app.exception.DAOException;
import progetto.app.model.Notifica;

import java.util.List;

/**
 * Interfaccia DAO per la gestione delle notifiche (avvisi) nel sistema Foodlab.
 * <p>
 * Definisce le operazioni di lettura e scrittura per le entità
 * {@link Notifica},
 * che corrispondono alla tabella {@code avvisi} del database PostgreSQL.
 * Le notifiche possono essere indirizzate a tutti i corsi di uno chef
 * (quando {@code id_corso} è {@code NULL}) o a un corso specifico.
 * </p>
 */
public interface NotificaDAO {

    /**
     * Recupera tutte le notifiche inviate da uno chef, ordinate per data di
     * creazione
     * in ordine decrescente (le più recenti per prime).
     *
     * @param chefId l'ID dello chef di cui recuperare le notifiche
     * @return lista di {@link Notifica} dello chef; lista vuota se non ne esistono
     * @throws DAOException in caso di errore di accesso al database
     */
    List<Notifica> getNotificheByChef(int chefId) throws DAOException;

    /**
     * Inserisce una nuova notifica nel database.
     * <p>
     * Se la notifica non è associata a nessun corso specifico, il campo
     * {@code id_corso} viene impostato a {@code NULL}. In caso di inserimento
     * riuscito, aggiorna l'ID della notifica con la chiave generata dal database.
     * </p>
     *
     * @param notifica l'entità {@link Notifica} da persistere
     * @return l'ID generato dal database per la notifica appena inserita,
     *         o {@code -1} se non è stato possibile recuperarlo
     * @throws DAOException in caso di errore di accesso al database
     */
    int addNotifica(Notifica notifica) throws DAOException;
}
