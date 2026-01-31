package progetto.app.model;

public class Chef extends User {

    public Chef(String username, String password, String email, String name, String surname) {
        super(username, password, email, name, surname);
    }

    public Chef(int id, String username, String password, String email, String name, String surname) {
        super(id, username, password, email, name, surname);
    }

    @Override
    public boolean isChef() {
        return true;
    }
}
