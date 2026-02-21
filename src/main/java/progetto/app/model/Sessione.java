package progetto.app.model;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Modello che rappresenta una singola sessione di un {@link Corso}.
 * <p>
 * Ogni sessione appartiene a un corso specifico ({@code corsoId}) ed è
 * identificata da {@code numeroSessione} all'interno di quel corso.
 * Le sessioni in presenza ({@code modalita = "In Presenza"}) possono
 * avere ricette associate tramite la tabella {@code sessione_ricetta}.
 * </p>
 */
public class Sessione {
    /** Identificatore univoco della sessione (0 se non ancora persistita). */
    private int id;
    /** ID del {@link Corso} a cui appartiene questa sessione. */
    private int corsoId;
    /** Numero progressivo della sessione all'interno del corso. */
    private int numeroSessione;
    /** Data in cui si svolge la sessione. */
    private LocalDate dataSessione;
    /** Modalità di svolgimento ({@code Online} o {@code In Presenza}). */
    private String modalita;
    /** Durata della sessione in minuti. */
    private int durata;
    /** Descrizione del contenuto della sessione. */
    private String descrizione;

    /**
     * Crea una sessione senza ID (da usare prima della persistenza).
     *
     * @param corsoId        ID del corso a cui appartiene
     * @param numeroSessione numero progressivo della sessione
     * @param dataSessione   data della sessione
     * @param modalita       modalità ({@code Online} o {@code In Presenza})
     * @param durata         durata in minuti
     * @param descrizione    descrizione del contenuto
     */
    public Sessione(int corsoId, int numeroSessione, LocalDate dataSessione, String modalita, int durata,
            String descrizione) {
        this.corsoId = corsoId;
        this.numeroSessione = numeroSessione;
        this.dataSessione = dataSessione;
        this.modalita = modalita;
        this.durata = durata;
        this.descrizione = descrizione;
    }

    /**
     * Crea una sessione con ID (usato quando viene recuperata dal database).
     *
     * @param id             identificatore univoco
     * @param corsoId        ID del corso a cui appartiene
     * @param numeroSessione numero progressivo della sessione
     * @param dataSessione   data della sessione
     * @param modalita       modalità ({@code Online} o {@code In Presenza})
     * @param durata         durata in minuti
     * @param descrizione    descrizione del contenuto
     */
    public Sessione(int id, int corsoId, int numeroSessione, LocalDate dataSessione, String modalita, int durata,
            String descrizione) {
        this.id = id;
        this.corsoId = corsoId;
        this.numeroSessione = numeroSessione;
        this.dataSessione = dataSessione;
        this.modalita = modalita;
        this.durata = durata;
        this.descrizione = descrizione;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCorsoId() {
        return corsoId;
    }

    public void setCorsoId(int corsoId) {
        this.corsoId = corsoId;
    }

    public int getNumeroSessione() {
        return numeroSessione;
    }

    public void setNumeroSessione(int numeroSessione) {
        this.numeroSessione = numeroSessione;
    }

    public LocalDate getDataSessione() {
        return dataSessione;
    }

    public void setDataSessione(LocalDate dataSessione) {
        this.dataSessione = dataSessione;
    }

    public String getModalita() {
        return modalita;
    }

    public void setModalita(String modalita) {
        this.modalita = modalita;
    }

    public int getDurata() {
        return durata;
    }

    public void setDurata(int durata) {
        this.durata = durata;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    @Override
    public String toString() {
        return "Sessione{" +
                "id=" + id +
                ", corsoId=" + corsoId +
                ", numeroSessione=" + numeroSessione +
                ", dataSessione=" + dataSessione +
                ", modalita='" + modalita + '\'' +
                ", durata=" + durata +
                ", descrizione='" + descrizione + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Sessione sessione))
            return false;
        return id == sessione.id && corsoId == sessione.corsoId && numeroSessione == sessione.numeroSessione
                && durata == sessione.durata && Objects.equals(dataSessione, sessione.dataSessione)
                && Objects.equals(modalita, sessione.modalita) && Objects.equals(descrizione, sessione.descrizione);
    }
}
