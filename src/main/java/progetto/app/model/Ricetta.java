package progetto.app.model;

import java.util.Objects;

/**
 * Modello che rappresenta una ricetta creata da uno {@link Chef}.
 * <p>
 * Le ricette vengono associate alle sessioni in presenza tramite la
 * tabella di join {@code sessione_ricetta}.
 * </p>
 */
public class Ricetta {
    /** Identificatore univoco della ricetta (0 se non ancora persistita). */
    private int id;
    /** Nome della ricetta. */
    private String nome;
    /** Descrizione testuale della ricetta. */
    private String descrizione;
    /** ID dello {@link Chef} che ha creato la ricetta. */
    private int chefId;

    /**
     * Crea una ricetta senza ID (da usare prima della persistenza).
     *
     * @param nome        nome della ricetta
     * @param descrizione descrizione testuale
     * @param chefId      ID dello chef proprietario
     */
    public Ricetta(String nome, String descrizione, int chefId) {
        this.nome = nome;
        this.descrizione = descrizione;
        this.chefId = chefId;
    }

    /**
     * Crea una ricetta con ID (usato quando viene recuperata dal database).
     *
     * @param id          identificatore univoco
     * @param nome        nome della ricetta
     * @param descrizione descrizione testuale
     * @param chefId      ID dello chef proprietario
     */
    public Ricetta(int id, String nome, String descrizione, int chefId) {
        this.id = id;
        this.nome = nome;
        this.descrizione = descrizione;
        this.chefId = chefId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public int getChefId() {
        return chefId;
    }

    public void setChefId(int chefId) {
        this.chefId = chefId;
    }

    @Override
    public String toString() {
        return nome;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Ricetta ricetta))
            return false;
        return id == ricetta.id && chefId == ricetta.chefId && Objects.equals(nome, ricetta.nome)
                && Objects.equals(descrizione, ricetta.descrizione);
    }

}
