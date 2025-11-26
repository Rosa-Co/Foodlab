package progetto.app.model;

public class Allievo extends User {
    public Allievo(String username, String password, String email, String name, String surname) {
        super(username,password,email,name,surname);
    }

    public Allievo(int id, String username, String password, String email, String name, String surname) {
        super(id,username,password,email,name,surname);
    }
}
