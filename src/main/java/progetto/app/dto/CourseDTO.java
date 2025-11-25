package progetto.app.dto;

import java.time.LocalDate;

public class CourseDTO {
    private String titolo;
    private String categoria;
    private LocalDate dataInizio;
    private String frequenza;

    public CourseDTO(String titolo, String categoria, LocalDate dataInizio, String frequenza) {
        this.titolo = titolo;
        this.categoria = categoria;
        this.dataInizio = dataInizio;
        this.frequenza = frequenza;
    }

    public String getTitolo() {
        return titolo;
    }

    public String getCategoria() {
        return categoria;
    }

    public LocalDate getDataInizio() {
        return dataInizio;
    }

    public String getFrequenza() {
        return frequenza;
    }
}
