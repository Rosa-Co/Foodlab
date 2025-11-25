package progetto.app.dto;

import java.time.LocalDate;

public class SessionDTO {
    private LocalDate data;
    private String modalita;
    private int durata;
    private String descrizione;
    private RecipeDTO ricetta;

    public SessionDTO(LocalDate data, String modalita, int durata, String descrizione, RecipeDTO ricetta) {
        this.data = data;
        this.modalita = modalita;
        this.durata = durata;
        this.descrizione = descrizione;
        this.ricetta = ricetta;
    }

    public LocalDate getData() {
        return data;
    }

    public String getModalita() {
        return modalita;
    }

    public int getDurata() {
        return durata;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public RecipeDTO getRicetta() {
        return ricetta;
    }
}
