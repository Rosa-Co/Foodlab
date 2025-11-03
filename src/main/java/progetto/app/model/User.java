package progetto.app.model;

public class User {
    private int id;
    private String username;
    private String email;
    private String name;
    private String surname;
    private Boolean isChef;

    public User(int id, String username, String email, String name, String surname, Boolean isChef) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.name = name;
        this.surname = surname;
        this.isChef = isChef;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public Boolean getIsChef() {
        return isChef;
    }

    public void setIsChef(Boolean isChef) {
        this.isChef = isChef;
    }
}
