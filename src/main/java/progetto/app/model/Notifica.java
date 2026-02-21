package progetto.app.model;

import java.util.Objects;

/**
 * Modello che rappresenta un avviso (notifica) inviato da uno {@link Chef}.
 * <p>
 * Una notifica può essere generica (non associata a nessun corso,
 * {@code idCorso == null}) oppure specifica per un corso
 * ({@code idCorso != null}).
 * </p>
 */
public class Notifica {
    /** Identificatore univoco della notifica (0 se non ancora persistita). */
    private int idAvviso;
    /** Titolo dell'avviso. */
    private String titolo;
    /** Testo del corpo dell'avviso. */
    private String contenuto;
    /** ID dello {@link Chef} che ha inviato la notifica. */
    private int idChef;
    /** ID del corso associato; {@code null} se la notifica è generica. */
    private Integer idCorso; // Nullable

    /**
     * Crea una notifica senza ID (da usare prima della persistenza).
     *
     * @param titolo    titolo dell'avviso
     * @param contenuto testo del corpo
     * @param idChef    ID dello chef mittente
     * @param idCorso   ID del corso destinatario, o {@code null} per avviso
     *                  generico
     */
    public Notifica(String titolo, String contenuto, int idChef, Integer idCorso) {
        this.titolo = titolo;
        this.contenuto = contenuto;
        this.idChef = idChef;
        this.idCorso = idCorso;
    }

    /**
     * Crea una notifica con ID (usato quando viene recuperata dal database).
     *
     * @param idAvviso  identificatore univoco
     * @param titolo    titolo dell'avviso
     * @param contenuto testo del corpo
     * @param idChef    ID dello chef mittente
     * @param idCorso   ID del corso destinatario, o {@code null} per avviso
     *                  generico
     */
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
        if (!(o instanceof Notifica notifica))
            return false;
        return idAvviso == notifica.idAvviso && idChef == notifica.idChef && Objects.equals(titolo, notifica.titolo)
                && Objects.equals(contenuto, notifica.contenuto) && Objects.equals(idCorso, notifica.idCorso);
    }
}
