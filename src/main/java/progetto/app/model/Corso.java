package progetto.app.model;

import java.time.LocalDate;

public class Corso {
    private int id;
    private String titolo;
    private String categoria;
    private LocalDate dataInizio;
    private String frequenza;
    private int numeroSessioni;
    private int chefId;

    public Corso(String titolo, String categoria, LocalDate dataInizio, String frequenza, int numeroSessioni,
            int chefId) {
        this.titolo = titolo;
        this.categoria = categoria;
        this.dataInizio = dataInizio;
        this.frequenza = frequenza;
        this.numeroSessioni = numeroSessioni;
        this.chefId = chefId;
    }

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
}
