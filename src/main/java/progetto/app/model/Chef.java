package progetto.app.model;

/**
 * Modello che rappresenta uno chef del sistema.
 * <p>
 * Estende {@link User} con {@link #isChef()} che restituisce sempre
 * {@code true}. Gli chef possono creare corsi, sessioni e notifiche.
 * </p>
 */
public class Chef extends User {

    /**
     * Crea uno chef senza ID (da usare prima della persistenza).
     *
     * @param username username
     * @param password hash BCrypt della password
     * @param email    indirizzo email
     * @param name     nome
     * @param surname  cognome
     */
    public Chef(String username, String password, String email, String name, String surname) {
        super(username, password, email, name, surname);
    }

    /**
     * Crea uno chef con ID (usato quando viene recuperato dal database).
     *
     * @param id       identificatore univoco
     * @param username username
     * @param password hash BCrypt della password
     * @param email    indirizzo email
     * @param name     nome
     * @param surname  cognome
     */
    public Chef(int id, String username, String password, String email, String name, String surname) {
        super(id, username, password, email, name, surname);
    }

    /** @return sempre {@code true} */
    @Override
    public boolean isChef() {
        return true;
    }
}
