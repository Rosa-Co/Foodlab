package progetto.app.model;

import java.util.Objects;

public class Ricetta {
    private int id;
    private String nome;
    private String descrizione;
    private int chefId;

    public Ricetta(String nome, String descrizione, int chefId) {
        this.nome = nome;
        this.descrizione = descrizione;
        this.chefId = chefId;
    }

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
        if (!(o instanceof Ricetta ricetta)) return false;
        return id == ricetta.id && chefId == ricetta.chefId && Objects.equals(nome, ricetta.nome) && Objects.equals(descrizione, ricetta.descrizione);
    }

}
