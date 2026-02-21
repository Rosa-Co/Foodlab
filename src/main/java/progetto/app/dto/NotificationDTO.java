package progetto.app.dto;

/**
 * DTO che rappresenta una notifica (avviso) nel layer di presentazione.
 * <p>
 * Ha due costruttori distinti, uno per la visualizzazione (con {@code target}
 * come stringa leggibile, es. {@code "Tutti i corsi"}) e uno per la creazione
 * (con {@code corsoId} opzionale). Il campo {@code corsoId} è {@code null}
 * quando la notifica non è associata a nessun corso specifico.
 * </p>
 */
public class NotificationDTO {
    /** Titolo dell'avviso. */
    private String titolo;
    /** Testo del corpo dell'avviso. */
    private String contenuto;
    /**
     * Descrizione testuale del destinatario (es. {@code "Tutti i corsi"} o il nome
     * del corso).
     */
    private String target; // e.g. "Tutti i corsi" or "Corso: Java Basics"
    /** ID del corso associato; {@code null} se la notifica è generica. */
    private Integer corsoId; // Optional, for creation

    /** Crea un {@code NotificationDTO} con tutti i campi a {@code null}. */
    public NotificationDTO() {
    }

    /**
     * Crea un {@code NotificationDTO} per la visualizzazione di una notifica
     * esistente.
     *
     * @param titolo    titolo dell'avviso
     * @param contenuto testo del corpo dell'avviso
     * @param target    stringa descrittiva del destinatario
     */
    // Constructor for View (Display)
    public NotificationDTO(String titolo, String contenuto, String target) {
        this.titolo = titolo;
        this.contenuto = contenuto;
        this.target = target;
    }

    /**
     * Crea un {@code NotificationDTO} per la creazione di una nuova notifica.
     *
     * @param titolo    titolo dell'avviso
     * @param contenuto testo del corpo dell'avviso
     * @param corsoId   ID del corso destinatario, o {@code null} per notifica
     *                  generica
     */
    // Constructor for Creation
    public NotificationDTO(String titolo, String contenuto, Integer corsoId) {
        this.titolo = titolo;
        this.contenuto = contenuto;
        this.corsoId = corsoId;
    }

    /** @return titolo dell'avviso */
    public String getTitolo() {
        return titolo;
    }

    /** @param titolo titolo dell'avviso */
    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }

    /** @return testo del corpo dell'avviso */
    public String getContenuto() {
        return contenuto;
    }

    /** @param contenuto testo del corpo dell'avviso */
    public void setContenuto(String contenuto) {
        this.contenuto = contenuto;
    }

    /** @return stringa descrittiva del destinatario della notifica */
    public String getTarget() {
        return target;
    }

    /** @param target stringa descrittiva del destinatario della notifica */
    public void setTarget(String target) {
        this.target = target;
    }

    /** @return ID del corso associato, o {@code null} se la notifica è generica */
    public Integer getCorsoId() {
        return corsoId;
    }

    /**
     * @param corsoId ID del corso associato, o {@code null} per notifica generica
     */
    public void setCorsoId(Integer corsoId) {
        this.corsoId = corsoId;
    }
}
