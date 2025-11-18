package progetto.app.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.mindrot.jbcrypt.BCrypt;
import progetto.app.dao.Interface.ChefDAO;
import progetto.app.dao.Interface.AllievoDAO;
import progetto.app.dao.postgree.AllievoDAO_Postgree;
import progetto.app.dao.postgree.ChefDAO_Postgree;
import progetto.app.dialog.ErrorDialog;
import progetto.app.dialog.WarningDialog;
import progetto.app.exception.DAOException;
import progetto.app.model.Allievo;
import progetto.app.model.Chef;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class AppController {

    private static AppController instance;  // Singleton per accesso globale
    private Stage primaryStage;
    private final Map<String, Parent> views = new HashMap<>();
    private final Map<String, Object> controllers = new HashMap<>();
    private AllievoDAO allievoDAO = getAllievoDAO();
    private ChefDAO chefDAO = getChefDAO();
    private static final String EMAIL_REGEX =
            "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

    private static final Pattern pattern = Pattern.compile(EMAIL_REGEX);

    private AppController() {}

    public static AppController getInstance() {
        if (instance == null) instance = new AppController();
        return instance;
    }

    public void setPrimaryStage(Stage stage) {
        this.primaryStage = stage;
    }

    public AllievoDAO getAllievoDAO() {
        if(this.allievoDAO == null) this.allievoDAO = new AllievoDAO_Postgree();
        return this.allievoDAO;
    }
    public ChefDAO getChefDAO() {
        if(this.chefDAO == null) this.chefDAO = new ChefDAO_Postgree();
        return this.chefDAO;
    }

    /**
     * Formatta il percorso FXML per assicurarsi che sia corretto.
     * @param fxmlPath Il percorso FXML da formattare
     * @return Il percorso FXML formattato correttamente.
     */
    private static String formatFxmlPath(String fxmlPath){
        // Assicura che il percorso inizi con /
        if (!fxmlPath.startsWith("/")) {
            fxmlPath = "/" + fxmlPath;
        }

        // Se il percorso non contiene il package, aggiungilo
        if (!fxmlPath.contains("/progetto/app/")) {
            fxmlPath = "/progetto/app" + fxmlPath;
        }

        // Assicura che il percorso finisca con .fxml
        if (!fxmlPath.endsWith(".fxml")) {
            fxmlPath += ".fxml";
        }

        return fxmlPath;
    }

    /**
     * Carica una vista FXML e il suo controller in cache.
     * @param name Nome identificativo della vista
     * @param fxmlPath Percorso del file FXML
     */
    public void loadView(String name, String fxmlPath) {
        try {
            if (views.containsKey(name)) {
                throw new IllegalArgumentException("La vista con nome : '" + name + "' esiste già in cache.");
            }
            fxmlPath = formatFxmlPath(fxmlPath);
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            Object controller = loader.getController();
            views.put(name, root);
            controllers.put(name, controller);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Parent loadView(String fxmlPath) {
        Parent root = null;
        try {
            fxmlPath = formatFxmlPath(fxmlPath);
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return root;
    }

    public Scene createScene(String fxmlPath) {
        Scene newScene = null;
        try {
            newScene = new Scene(loadView(fxmlPath));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return newScene;
    }

    public void navigateToLogin() {
        navigateTo("login");
    }

    public void navigateToDashboard() {
        navigateTo("dashboard");
    }

    public void navigateToHome() {
        navigateTo("home");
    }

    private void navigateTo(String name) {
        Parent view = views.get(name);
        if (view != null && primaryStage != null) {
            primaryStage.setScene(new Scene(view));
            primaryStage.show();
        } else {
            System.err.println("View not found: " + name);
        }
    }
    //! fare il check!
    public Parent getView(String name){
        return views.get(name);
    }
    public Object getController(String name) {
        return controllers.get(name);
    }

    public void setPrimaryStageResizable(boolean value){
        primaryStage.setResizable(value);
    }
    public void showPrimaryStage(){
        primaryStage.show();
    }


    public boolean login(String username, String password) {
        return loginUser(username, password) || loginChef(username, password);
    }

    public boolean registerUser(String username, String password, String name, String surname, String email) {
        if(isRegistered(username, email)) return false;

        String pswHashed = BCrypt.hashpw(password, BCrypt.gensalt(10));
        Allievo allievo = new Allievo(username, pswHashed, email, name, surname);

        try {
            allievoDAO.addAllievo(allievo);
            return  true;
        } catch (DAOException e) {
            ErrorDialog errorDialog= new ErrorDialog("Registrazione utente fallita!","Provare più tardi.");
            errorDialog.show();
            return false;
        }
    }


    public boolean loginUser(String username, String password)  {
        try {
            Allievo allievo = allievoDAO.getAllievoByUsername(username);
            if (allievo == null) return false;
            String hashedPassword = allievo.getPassword();
            return BCrypt.checkpw(password, hashedPassword);

        } catch (Exception e) {
            ErrorDialog errorDialog= new ErrorDialog("Utente non trovato.","Controlla i campi e riprova.");
            errorDialog.show();
            return false;
        }
    }

    public boolean registerChef(String username, String password, String name, String surname, String email)  {
        if(isRegistered(username, email)) return false;

        String pswHashed = BCrypt.hashpw(password, BCrypt.gensalt(10));
        Chef chef = new Chef(username, pswHashed, email, name, surname);

        try {
            chefDAO.addChef(chef);
            return true;
        } catch (DAOException e) {
            ErrorDialog errorDialog= new ErrorDialog("Registrazione chef fallita!","Provare più tardi.");
            errorDialog.show();
            return false;
        }
    }

    public boolean loginChef(String username, String password)  {
        try {
            Chef chef = chefDAO.getChefByUsername(username);
            if (chef == null) return false;
            String hashedPassword = chef.getPassword();
            return BCrypt.checkpw(password, hashedPassword);

        } catch (Exception e) {
            ErrorDialog errorDialog= new ErrorDialog("Chef non trovato","Controlla i campi e riprova");
            errorDialog.show();
            return false;
        }
    }

    public boolean isRegistered(String username, String email){
        if(allievoDAO.getAllievoByEmail(email) != null || chefDAO.getChefByEmail(email) != null) {
            WarningDialog warningDialog= new WarningDialog("Account già esistente.","Proseguire sulla schermata di accesso.");
            warningDialog.show();
            return true;
        }
        else if(allievoDAO.getAllievoByUsername(username) != null || chefDAO.getChefByUsername(username) != null) {
            WarningDialog warningDialog= new WarningDialog("Account già esistente.","Proseguire sulla schermata di accesso.");
            warningDialog.show();
            return true;
        }
        return false;
    }

    public static boolean checkEmail(String email) {
        if (email == null) {
            return false;
        }
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
