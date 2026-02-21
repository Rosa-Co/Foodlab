package progetto.app.model;

import java.time.LocalDate;
import java.util.Objects;

public class Sessione {
    private int id;
    private int corsoId;
    private int numeroSessione;
    private LocalDate dataSessione;
    private String modalita;
    private int durata;
    private String descrizione;

    public Sessione(int corsoId, int numeroSessione, LocalDate dataSessione, String modalita, int durata,
            String descrizione) {
        this.corsoId = corsoId;
        this.numeroSessione = numeroSessione;
        this.dataSessione = dataSessione;
        this.modalita = modalita;
        this.durata = durata;
        this.descrizione = descrizione;
    }

    public Sessione(int id, int corsoId, int numeroSessione, LocalDate dataSessione, String modalita, int durata,
            String descrizione) {
        this.id = id;
        this.corsoId = corsoId;
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

    public int getCorsoId() {
        return corsoId;
    }

    public void setCorsoId(int corsoId) {
        this.corsoId = corsoId;
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

    @Override
    public String toString() {
        return "Sessione{" +
                "id=" + id +
                ", corsoId=" + corsoId +
                ", numeroSessione=" + numeroSessione +
                ", dataSessione=" + dataSessione +
                ", modalita='" + modalita + '\'' +
                ", durata=" + durata +
                ", descrizione='" + descrizione + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Sessione sessione)) return false;
        return id == sessione.id && corsoId == sessione.corsoId && numeroSessione == sessione.numeroSessione && durata == sessione.durata && Objects.equals(dataSessione, sessione.dataSessione) && Objects.equals(modalita, sessione.modalita) && Objects.equals(descrizione, sessione.descrizione);
    }
}
