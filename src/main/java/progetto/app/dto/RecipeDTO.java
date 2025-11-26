package progetto.app.dto;

public class RecipeDTO {
    private int id;
    private String nome;

    private String descrizione;
    private String categoria;

    public RecipeDTO(int id, String nome) {
        this(id, nome, "", "");
    }

    public RecipeDTO(int id, String nome, String descrizione, String categoria) {
        this.id = id;
        this.nome = nome;
        this.descrizione = descrizione;
        this.categoria = categoria;
    }

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public String getCategoria() {
        return categoria;
    }

    @Override
    public String toString() {
        return nome;
    }
}
