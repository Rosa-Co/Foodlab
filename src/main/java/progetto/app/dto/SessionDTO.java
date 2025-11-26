package progetto.app.dto;

import java.time.LocalDate;
import java.util.List;

public class SessionDTO {
    private LocalDate data;
    private String modalita;
    private int durata;
    private String descrizione;
    private List<RecipeDTO> ricette;

    public SessionDTO(LocalDate data, String modalita, int durata, String descrizione,
            java.util.List<RecipeDTO> ricette) {
        this.data = data;
        this.modalita = modalita;
        this.durata = durata;
        this.descrizione = descrizione;
        this.ricette = ricette;
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

    public java.util.List<RecipeDTO> getRicette() {
        return ricette;
    }
}
