package progetto.app.model;

public class Ricetta {
    private int id;
    private String nome;
    private String descrizione;
    private String categoria;
    private int chefId; // ? chef should create the recipe indipendently from the course.

    public Ricetta(String nome, String descrizione, String categoria, int chefId) {
        this.nome = nome;
        this.descrizione = descrizione;
        this.categoria = categoria;
        this.chefId = chefId;
    }

    public Ricetta(int id, String nome, String descrizione, String categoria, int chefId) {
        this.id = id;
        this.nome = nome;
        this.descrizione = descrizione;
        this.categoria = categoria;
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

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public int getChefId() {
        return chefId;
    }

    public void setChefId(int chefId) {
        this.chefId = chefId;
    }

    @Override
    public String toString() {
        return nome; // Useful for ComboBox display
    }
}
