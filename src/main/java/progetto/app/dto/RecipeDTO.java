package progetto.app.dto;

/**
 * DTO che rappresenta una ricetta nel layer di presentazione.
 * <p>
 * Dispone di un costruttore convenienza che accetta solo {@code id} e
 * {@code nome}
 * (delegando all'altro con descrizione vuota). Il metodo {@link #toString()}
 * restituisce il nome, permettendo di usare direttamente le istanze in
 * {@link javafx.scene.control.ComboBox} e simili controlli JavaFX.
 * </p>
 */
public class RecipeDTO {
    /** Identificatore univoco della ricetta. */
    private int id;
    /** Nome della ricetta. */
    private String nome;
    /** Descrizione testuale della ricetta. */
    private String descrizione;

    /** Crea un {@code RecipeDTO} con tutti i campi al valore di default. */
    public RecipeDTO() {
    }

    /**
     * Crea un {@code RecipeDTO} con solo ID e nome; la descrizione viene impostata
     * a stringa vuota.
     *
     * @param id   identificatore della ricetta
     * @param nome nome della ricetta
     */
    public RecipeDTO(int id, String nome) {
        this(id, nome, "");
    }

    /**
     * Crea un {@code RecipeDTO} completo.
     *
     * @param id          identificatore della ricetta
     * @param nome        nome della ricetta
     * @param descrizione descrizione testuale della ricetta
     */
    public RecipeDTO(int id, String nome, String descrizione) {
        this.id = id;
        this.nome = nome;
        this.descrizione = descrizione;
    }

    /** @return identificatore della ricetta */
    public int getId() {
        return id;
    }

    /** @param id identificatore della ricetta */
    public void setId(int id) {
        this.id = id;
    }

    /** @return nome della ricetta */
    public String getNome() {
        return nome;
    }

    /** @param nome nome della ricetta */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /** @return descrizione testuale della ricetta */
    public String getDescrizione() {
        return descrizione;
    }

    /** @param descrizione descrizione testuale della ricetta */
    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    /**
     * Restituisce il nome della ricetta.
     * <p>
     * Permette di usare questa istanza direttamente nei controlli JavaFX
     * (es. {@link javafx.scene.control.ComboBox}) senza un converter dedicato.
     * </p>
     *
     * @return il nome della ricetta
     */
    @Override
    public String toString() {
        return nome;
    }
}
