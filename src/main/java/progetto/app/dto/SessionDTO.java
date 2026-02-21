package progetto.app.dto;

import java.time.LocalDate;
import java.util.List;

/**
 * DTO che rappresenta una sessione nel layer di presentazione.
 * <p>
 * Ha due costruttori distinti:
 * <ul>
 * <li>Il costruttore senza {@code id} e {@code numeroSessione} viene usato
 * durante la creazione di un nuovo corso, quando questi valori non sono
 * ancora disponibili.</li>
 * <li>Il costruttore completo viene usato quando la sessione esiste già nel
 * database (es. per la visualizzazione o modifica).</li>
 * </ul>
 * Il campo {@code ricette} è presente solo nel primo costruttore, poiché la
 * lista di ricette viene associata alla sessione al momento della creazione.
 * </p>
 */
public class SessionDTO {
    /** Identificatore univoco della sessione (0 se non ancora persistita). */
    private int id;
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
     * Lista delle ricette associate (solo per sessioni in presenza, durante la
     * creazione).
     */
    private List<RecipeDTO> ricette;

    /** Crea una {@code SessionDTO} con tutti i campi al valore di default. */
    public SessionDTO() {
    }

    /**
     * Crea un {@code SessionDTO} per la fase di creazione di una sessione
     * (senza ID e senza numero sessione progressivo).
     *
     * @param dataSessione data della sessione
     * @param modalita     modalità ({@code Online} o {@code In Presenza})
     * @param durata       durata in minuti
     * @param descrizione  descrizione del contenuto
     * @param ricette      lista di ricette associate (può essere vuota)
     */
    public SessionDTO(LocalDate dataSessione, String modalita, int durata, String descrizione,
            List<RecipeDTO> ricette) {
        this.dataSessione = dataSessione;
        this.modalita = modalita;
        this.durata = durata;
        this.descrizione = descrizione;
        this.ricette = ricette;
    }

    /**
     * Crea un {@code SessionDTO} completo per una sessione già persistita.
     *
     * @param id             identificatore della sessione
     * @param numeroSessione numero progressivo della sessione nel corso
     * @param dataSessione   data della sessione
     * @param modalita       modalità ({@code Online} o {@code In Presenza})
     * @param durata         durata in minuti
     * @param descrizione    descrizione del contenuto
     */
    public SessionDTO(int id, int numeroSessione, LocalDate dataSessione, String modalita, int durata,
            String descrizione) {
        this.id = id;
        this.numeroSessione = numeroSessione;
        this.dataSessione = dataSessione;
        this.modalita = modalita;
        this.durata = durata;
        this.descrizione = descrizione;
    }

    /** @return identificatore della sessione */
    public int getId() {
        return id;
    }

    /** @param id identificatore della sessione */
    public void setId(int id) {
        this.id = id;
    }

    /** @return numero progressivo della sessione nel corso */
    public int getNumeroSessione() {
        return numeroSessione;
    }

    /** @param numeroSessione numero progressivo della sessione nel corso */
    public void setNumeroSessione(int numeroSessione) {
        this.numeroSessione = numeroSessione;
    }

    /** @return data in cui si svolge la sessione */
    public LocalDate getDataSessione() {
        return dataSessione;
    }

    /** @param dataSessione data in cui si svolge la sessione */
    public void setDataSessione(LocalDate dataSessione) {
        this.dataSessione = dataSessione;
    }

    /** @return modalità di svolgimento della sessione */
    public String getModalita() {
        return modalita;
    }

    /** @param modalita modalità di svolgimento della sessione */
    public void setModalita(String modalita) {
        this.modalita = modalita;
    }

    /** @return durata della sessione in minuti */
    public int getDurata() {
        return durata;
    }

    /** @param durata durata della sessione in minuti */
    public void setDurata(int durata) {
        this.durata = durata;
    }

    /** @return descrizione del contenuto della sessione */
    public String getDescrizione() {
        return descrizione;
    }

    /** @param descrizione descrizione del contenuto della sessione */
    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    /** @return lista delle ricette associate alla sessione */
    public List<RecipeDTO> getRicette() {
        return ricette;
    }

    /** @param ricette lista delle ricette associate alla sessione */
    public void setRicette(List<RecipeDTO> ricette) {
        this.ricette = ricette;
    }
}
