package progetto.app.model;

/**
 * Modello che rappresenta un allievo (studente) del sistema.
 * <p>
 * Estende {@link User} con {@link #isChef()} che restituisce sempre
 * {@code false}.
 * </p>
 */
public class Allievo extends User {

    /**
     * Crea un allievo senza ID (da usare prima della persistenza).
     *
     * @param username username
     * @param password hash BCrypt della password
     * @param email    indirizzo email
     * @param name     nome
     * @param surname  cognome
     */
    public Allievo(String username, String password, String email, String name, String surname) {
        super(username, password, email, name, surname);
    }

    /**
     * Crea un allievo con ID (usato quando viene recuperato dal database).
     *
     * @param id       identificatore univoco
     * @param username username
     * @param password hash BCrypt della password
     * @param email    indirizzo email
     * @param name     nome
     * @param surname  cognome
     */
    public Allievo(int id, String username, String password, String email, String name, String surname) {
        super(id, username, password, email, name, surname);
    }

    /** @return sempre {@code false} */
    @Override
    public boolean isChef() {
        return false;
    }
}
