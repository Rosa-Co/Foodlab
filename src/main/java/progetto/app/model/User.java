package progetto.app.model;

import java.util.Objects;

/**
 * Classe base astratta che rappresenta un utente del sistema.
 * <p>
 * Contiene i dati anagrafici comuni a tutte le tipologie di utente
 * ({@link Chef} e {@link Allievo}). La distinzione tra le due tipologie
 * è demandata al metodo astratto {@link #isChef()}, che le sottoclassi
 * implementano restituendo {@code true} o {@code false}.
 * </p>
 * <p>
 * La password memorizzata è sempre l'hash BCrypt, non la password in chiaro.
 * </p>
 */
public abstract class User {
    /** Identificatore univoco dell'utente (0 se non ancora persistito). */
    private int id;
    /** Username univoco scelto dall'utente. */
    private String username;
    /** Indirizzo email univoco dell'utente. */
    private String email;
    /** Nome dell'utente. */
    private String name;
    /** Cognome dell'utente. */
    private String surname;
    /** Hash BCrypt della password dell'utente. */
    private String password;

    /**
     * Crea un utente senza ID (da usare prima della persistenza).
     *
     * @param username username scelto dall'utente
     * @param password hash BCrypt della password
     * @param email    indirizzo email
     * @param name     nome
     * @param surname  cognome
     */
    public User(String username, String password, String email, String name, String surname) {
        this.email = email;
        this.username = username;
        this.surname = surname;
        this.name = name;
        this.password = password;
    }

    /**
     * Crea un utente con ID (usato quando viene recuperato dal database).
     *
     * @param id       identificatore univoco dell'utente
     * @param username username scelto dall'utente
     * @param password hash BCrypt della password
     * @param email    indirizzo email
     * @param name     nome
     * @param surname  cognome
     */
    public User(int id, String username, String password, String email, String name, String surname) {
        this.id = id;
        this.email = email;
        this.username = username;
        this.surname = surname;
        this.name = name;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Indica se l'utente è uno chef.
     *
     * @return {@code true} se l'utente è uno {@link Chef}, {@code false} se è un
     *         {@link Allievo}
     */
    public abstract boolean isChef();

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof User user))
            return false;
        return id == user.id && Objects.equals(username, user.username) && Objects.equals(email, user.email)
                && Objects.equals(name, user.name) && Objects.equals(surname, user.surname)
                && Objects.equals(password, user.password);
    }
}
