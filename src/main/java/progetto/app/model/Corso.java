package progetto.app.model;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Modello che rappresenta un corso di cucina nel sistema.
 * <p>
 * Un corso è creato da uno {@link Chef} (identificato da {@code chefId})
 * e è suddiviso in più {@link Sessione}. I campi {@code frequenza} e
 * {@code numeroSessioni} descrivono la cadenza e la durata pianificata del
 * corso.
 * </p>
 */
public class Corso {
    /** Identificatore univoco del corso (0 se non ancora persistito). */
    private int id;
    /** Titolo del corso. */
    private String titolo;
    /** Categoria culinaria del corso. */
    private String categoria;
    /** Data di inizio del corso. */
    private LocalDate dataInizio;
    /** Cadenza delle sessioni (es. {@code Settimanale}, {@code Bisettimanale}). */
    private String frequenza;
    /** Numero totale di sessioni pianificate. */
    private int numeroSessioni;
    /** Identificatore dello {@link Chef} che ha creato il corso. */
    private int chefId;

    /**
     * Crea un corso senza ID (da usare prima della persistenza).
     *
     * @param titolo         titolo del corso
     * @param categoria      categoria culinaria
     * @param dataInizio     data di inizio
     * @param frequenza      cadenza delle sessioni
     * @param numeroSessioni numero di sessioni pianificate
     * @param chefId         ID dello chef proprietario del corso
     */
    public Corso(String titolo, String categoria, LocalDate dataInizio, String frequenza, int numeroSessioni,
            int chefId) {
        this.titolo = titolo;
        this.categoria = categoria;
        this.dataInizio = dataInizio;
        this.frequenza = frequenza;
        this.numeroSessioni = numeroSessioni;
        this.chefId = chefId;
    }

    /**
     * Crea un corso con ID (usato quando viene recuperato dal database).
     *
     * @param id             identificatore univoco del corso
     * @param titolo         titolo del corso
     * @param categoria      categoria culinaria
     * @param dataInizio     data di inizio
     * @param frequenza      cadenza delle sessioni
     * @param numeroSessioni numero di sessioni pianificate
     * @param chefId         ID dello chef proprietario del corso
     */
    public Corso(int id, String titolo, String categoria, LocalDate dataInizio, String frequenza, int numeroSessioni,
            int chefId) {
        this.id = id;
        this.titolo = titolo;
        this.categoria = categoria;
        this.dataInizio = dataInizio;
        this.frequenza = frequenza;
        this.numeroSessioni = numeroSessioni;
        this.chefId = chefId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitolo() {
        return titolo;
    }

    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public LocalDate getDataInizio() {
        return dataInizio;
    }

    public void setDataInizio(LocalDate dataInizio) {
        this.dataInizio = dataInizio;
    }

    public String getFrequenza() {
        return frequenza;
    }

    public void setFrequenza(String frequenza) {
        this.frequenza = frequenza;
    }

    public int getNumeroSessioni() {
        return numeroSessioni;
    }

    public void setNumeroSessioni(int numeroSessioni) {
        this.numeroSessioni = numeroSessioni;
    }

    public int getChefId() {
        return chefId;
    }

    public void setChefId(int chefId) {
        this.chefId = chefId;
    }

    @Override
    public String toString() {
        return "Corso{" +
                "id=" + id +
                ", titolo='" + titolo + '\'' +
                ", categoria='" + categoria + '\'' +
                ", dataInizio=" + dataInizio +
                ", frequenza='" + frequenza + '\'' +
                ", numeroSessioni=" + numeroSessioni +
                ", chefId=" + chefId +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Corso corso))
            return false;
        return id == corso.id && numeroSessioni == corso.numeroSessioni && chefId == corso.chefId
                && Objects.equals(titolo, corso.titolo) && Objects.equals(categoria, corso.categoria)
                && Objects.equals(dataInizio, corso.dataInizio) && Objects.equals(frequenza, corso.frequenza);
    }

}
