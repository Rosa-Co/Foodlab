package progetto.app.dto;

import java.time.LocalDate;

/**
 * DTO che rappresenta un corso nel layer di presentazione.
 * <p>
 * Viene usato sia per la creazione di nuovi corsi (costruttore senza {@code id}
 * e senza {@code numeroSessioni}) sia per la visualizzazione di corsi esistenti
 * (costruttore completo). Il campo {@code id} è {@code 0} fino a quando il
 * corso non viene persistito nel database.
 * </p>
 */
public class CourseDTO {
    /** Identificatore univoco del corso (0 se non ancora persistito). */
    private int id;
    /** Titolo del corso. */
    private String titolo;
    /** Categoria culinaria del corso. */
    private String categoria;
    /** Data di inizio del corso. */
    private LocalDate dataInizio;
    /**
     * Frequenza delle sessioni (es. {@code Settimanale}, {@code Bisettimanale}).
     */
    private String frequenza;
    /** Numero totale di sessioni del corso. */
    private int numeroSessioni;

    /** Crea un {@code CourseDTO} con tutti i campi al valore di default. */
    public CourseDTO() {
    }

    /**
     * Crea un {@code CourseDTO} per la fase di creazione del corso
     * (senza ID e senza numero sessioni).
     *
     * @param titolo     titolo del corso
     * @param categoria  categoria culinaria
     * @param dataInizio data di inizio
     * @param frequenza  frequenza delle sessioni
     */
    public CourseDTO(String titolo, String categoria, LocalDate dataInizio, String frequenza) {
        this.titolo = titolo;
        this.categoria = categoria;
        this.dataInizio = dataInizio;
        this.frequenza = frequenza;
    }

    /**
     * Crea un {@code CourseDTO} completo, per la visualizzazione di un corso
     * esistente.
     *
     * @param id             identificatore del corso
     * @param titolo         titolo del corso
     * @param categoria      categoria culinaria
     * @param dataInizio     data di inizio
     * @param frequenza      frequenza delle sessioni
     * @param numeroSessioni numero totale di sessioni
     */
    public CourseDTO(int id, String titolo, String categoria, LocalDate dataInizio, String frequenza,
            int numeroSessioni) {
        this.id = id;
        this.titolo = titolo;
        this.categoria = categoria;
        this.dataInizio = dataInizio;
        this.frequenza = frequenza;
        this.numeroSessioni = numeroSessioni;
    }

    /** @return identificatore del corso */
    public int getId() {
        return id;
    }

    /** @param id identificatore del corso */
    public void setId(int id) {
        this.id = id;
    }

    /** @return titolo del corso */
    public String getTitolo() {
        return titolo;
    }

    /** @param titolo titolo del corso */
    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }

    /** @return categoria culinaria del corso */
    public String getCategoria() {
        return categoria;
    }

    /** @param categoria categoria culinaria del corso */
    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    /** @return data di inizio del corso */
    public LocalDate getDataInizio() {
        return dataInizio;
    }

    /** @param dataInizio data di inizio del corso */
    public void setDataInizio(LocalDate dataInizio) {
        this.dataInizio = dataInizio;
    }

    /** @return frequenza delle sessioni del corso */
    public String getFrequenza() {
        return frequenza;
    }

    /** @param frequenza frequenza delle sessioni del corso */
    public void setFrequenza(String frequenza) {
        this.frequenza = frequenza;
    }

    /** @return numero totale di sessioni del corso */
    public int getNumeroSessioni() {
        return numeroSessioni;
    }

    /** @param numeroSessioni numero totale di sessioni del corso */
    public void setNumeroSessioni(int numeroSessioni) {
        this.numeroSessioni = numeroSessioni;
    }
}
