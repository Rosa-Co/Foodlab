package progetto.app.dto;

public class NotificationDTO {
    private String titolo;
    private String contenuto;
    private String target; // e.g. "Tutti i corsi" or "Corso: Java Basics"
    private Integer corsoId; // Optional, for creation

    public NotificationDTO() {
    }

    // Constructor for View (Display)
    public NotificationDTO(String titolo, String contenuto, String target) {
        this.titolo = titolo;
        this.contenuto = contenuto;
        this.target = target;
    }

    // Constructor for Creation
    public NotificationDTO(String titolo, String contenuto, Integer corsoId) {
        this.titolo = titolo;
        this.contenuto = contenuto;
        this.corsoId = corsoId;
    }

    public String getTitolo() {
        return titolo;
    }

    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }

    public String getContenuto() {
        return contenuto;
    }

    public void setContenuto(String contenuto) {
        this.contenuto = contenuto;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public Integer getCorsoId() {
        return corsoId;
    }

    public void setCorsoId(Integer corsoId) {
        this.corsoId = corsoId;
    }
}
