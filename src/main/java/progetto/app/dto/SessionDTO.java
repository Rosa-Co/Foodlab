package progetto.app.dto;

import java.time.LocalDate;
import java.util.List;

public class SessionDTO {
    private int id;
    private int numeroSessione;
    private LocalDate dataSessione;
    private String modalita;
    private int durata;
    private String descrizione;
    private List<RecipeDTO> ricette;

    public SessionDTO() {
    }

    public SessionDTO(LocalDate dataSessione, String modalita, int durata, String descrizione,
            List<RecipeDTO> ricette) {
        this.dataSessione = dataSessione;
        this.modalita = modalita;
        this.durata = durata;
        this.descrizione = descrizione;
        this.ricette = ricette;
    }

    public SessionDTO(int id, int numeroSessione, LocalDate dataSessione, String modalita, int durata,
            String descrizione) {
        this.id = id;
        this.numeroSessione = numeroSessione;
        this.dataSessione = dataSessione;
        this.modalita = modalita;
        this.durata = durata;
        this.descrizione = descrizione;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNumeroSessione() {
        return numeroSessione;
    }

    public void setNumeroSessione(int numeroSessione) {
        this.numeroSessione = numeroSessione;
    }

    public LocalDate getDataSessione() {
        return dataSessione;
    }

    public void setDataSessione(LocalDate dataSessione) {
        this.dataSessione = dataSessione;
    }

    public String getModalita() {
        return modalita;
    }

    public void setModalita(String modalita) {
        this.modalita = modalita;
    }

    public int getDurata() {
        return durata;
    }

    public void setDurata(int durata) {
        this.durata = durata;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public List<RecipeDTO> getRicette() {
        return ricette;
    }

    public void setRicette(List<RecipeDTO> ricette) {
        this.ricette = ricette;
    }
}
