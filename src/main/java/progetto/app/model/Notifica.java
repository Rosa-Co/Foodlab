package progetto.app.model;

import java.util.Objects;

public class Notifica {
    private int idAvviso;
    private String titolo;
    private String contenuto;
    private int idChef;
    private Integer idCorso; // Nullable

    public Notifica(String titolo, String contenuto, int idChef, Integer idCorso) {
        this.titolo = titolo;
        this.contenuto = contenuto;
        this.idChef = idChef;
        this.idCorso = idCorso;
    }

    public Notifica(int idAvviso, String titolo, String contenuto, int idChef, Integer idCorso) {
        this.idAvviso = idAvviso;
        this.titolo = titolo;
        this.contenuto = contenuto;
        this.idChef = idChef;
        this.idCorso = idCorso;
    }

    public int getIdAvviso() {
        return idAvviso;
    }

    public void setIdAvviso(int idAvviso) {
        this.idAvviso = idAvviso;
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

    public int getIdChef() {
        return idChef;
    }

    public void setIdChef(int idChef) {
        this.idChef = idChef;
    }

    public Integer getIdCorso() {
        return idCorso;
    }

    public void setIdCorso(Integer idCorso) {
        this.idCorso = idCorso;
    }

    @Override
    public String toString() {
        return "Notifica{" +
                "idAvviso=" + idAvviso +
                ", titolo='" + titolo + '\'' +
                ", contenuto='" + contenuto + '\'' +
                ", idChef=" + idChef +
                ", idCorso=" + idCorso +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Notifica notifica)) return false;
        return idAvviso == notifica.idAvviso && idChef == notifica.idChef && Objects.equals(titolo, notifica.titolo) && Objects.equals(contenuto, notifica.contenuto) && Objects.equals(idCorso, notifica.idCorso);
    }
}
