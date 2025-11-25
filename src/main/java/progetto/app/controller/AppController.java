package progetto.app.controller;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;

import javafx.util.Duration;
import org.mindrot.jbcrypt.BCrypt;
import progetto.app.Main;
import progetto.app.dao.interfaces.ChefDAO;
import progetto.app.dao.interfaces.AllievoDAO;
import progetto.app.dao.postgree.AllievoDAO_Postgree;
import progetto.app.dao.postgree.ChefDAO_Postgree;
import progetto.app.dialog.TermsOfServiceDialog;
import progetto.app.exception.DAOException;
import progetto.app.model.Allievo;
import progetto.app.model.Chef;

public class AppController {
    private static final Image APP_ICON = new Image(
            Objects.requireNonNull(Main.class.getResourceAsStream("/progetto/app/logo.png")));

    // private static final Logger logger =
    // Logger.getLogger(AppController.class.getName());
    private static AppController instance; // Singleton per accesso globale
    private Stage primaryStage;
    private final Map<String, Parent> views = new HashMap<>();
    private final Map<String, Object> controllers = new HashMap<>();
    private AllievoDAO allievoDAO = getAllievoDAO();
    private ChefDAO chefDAO = getChefDAO();

    private AppController() {
    }

    public static AppController getInstance() {
        if (instance == null)
            instance = new AppController();
        return instance;
    }

    public void setPrimaryStage(Stage stage) {
        this.primaryStage = stage;
    }

    public AllievoDAO getAllievoDAO() {
        if (this.allievoDAO == null)
            this.allievoDAO = new AllievoDAO_Postgree();
        return this.allievoDAO;
    }

    public ChefDAO getChefDAO() {
        if (this.chefDAO == null)
            this.chefDAO = new ChefDAO_Postgree();
        return this.chefDAO;
    }

    /**
     * Formatta il percorso FXML per assicurarsi che sia corretto.
     * 
     * @param fxmlPath Il percorso FXML da formattare
     * @return Il percorso FXML formattato correttamente.
     */
    private static String formatFxmlPath(String fxmlPath) {
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
     * 
     * @param name     Nome identificativo della vista
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

    // ! fare il check!
    public Parent getView(String name) {
        return views.get(name);
    }

    public Object getController(String name) {
        return controllers.get(name);
    }

    public void setPrimaryStageResizable(boolean value) {
        primaryStage.setResizable(value);
    }

    public void showPrimaryStage() {
        primaryStage.show();
    }

    public void registerUser(String username, String password, String name, String surname, String email) {
        // controlla se l'utente esiste già
        if (allievoDAO.getAllievoByEmail(email) != null) {
            // ("User already exists");
        }
        // Hash della password
        String pswHashed = BCrypt.hashpw(password, BCrypt.gensalt(10));
        Allievo allievo = new Allievo(username, email, pswHashed, name, surname);

        try {
            allievoDAO.addAllievo(allievo);
        } catch (DAOException e) {
            e.printStackTrace(); // ! DA RIVEDERE
        }
    }

    public boolean login(String username, String password) {
        return loginUser(username, password) || loginChef(username, password);
    }

    public boolean loginUser(String username, String password) {
        /*
         * Allievo allievo = null;
         * try {
         * System.out.println("prima di get");
         * allievo = allievoDAO.getAllievoByUsername(username);
         * System.out.println("dopo di get");
         * }catch (Exception e){System.out.println(e); }
         * if(allievo == null) return false;
         * System.out.println(allievo);
         * System.out.println(allievo.getPassword());
         * System.out.println(password);
         * return BCrypt.checkpw(password, allievo.getPassword());
         */
        try {
            Allievo allievo = allievoDAO.getAllievoByUsername(username);
            if (allievo == null)
                return false;

            String hashedPassword = allievo.getPassword();

            // STAMPA QUESTI VALORI
            System.out.println("=== DEBUG LOGIN ===");
            System.out.println("username: " + username);
            System.out.println("Hash dal DB: [" + hashedPassword + "]");
            System.out.println("Lunghezza hash: " + hashedPassword.length());
            System.out.println(
                    "Primi 4 caratteri: [" + hashedPassword.substring(0, Math.min(4, hashedPassword.length())) + "]");
            System.out.println("Hash è null? " + (hashedPassword == null));
            System.out.println("Hash è vuoto? " + hashedPassword.isEmpty());

            return BCrypt.checkpw(password, hashedPassword);

        } catch (Exception e) {
            System.out.println("ERRORE: " + e.getClass().getName());
            System.out.println("MESSAGGIO: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public void registerChef(String username, String password, String name, String surname, String email) {
        if (chefDAO.getChefByEmail(email) != null) {
            // ("Chef already exists");
        }
        String pswHashed = BCrypt.hashpw(password, BCrypt.gensalt(10));
        Chef chef = new Chef(username, email, pswHashed, name, surname);

        try {
            chefDAO.addChef(chef);
        } catch (DAOException e) {
            e.printStackTrace(); // ! DA RIVEDERE
        }
    }

    public boolean loginChef(String username, String password) {
        Chef chef = getChefDAO().getChefByEmail(username);
        if (chef == null) {
            return false;
        }
        return BCrypt.checkpw(password, chef.getPassword());
    }

    /**
     * Imposta l'icona dell'applicazione per lo stage.
     * 
     * @param stage
     */
    public static void setAppIcon(Stage stage) {
        stage.getIcons().add(APP_ICON);
    }


    public void shakeWindow() {
        if (primaryStage != null) {
            double originalX = primaryStage.getX();

            Timeline timeline = new Timeline(
                    new KeyFrame(Duration.millis(0), e -> primaryStage.setX(originalX)),
                    new KeyFrame(Duration.millis(50), e -> primaryStage.setX(originalX - 10)),
                    new KeyFrame(Duration.millis(100), e -> primaryStage.setX(originalX + 10)),
                    new KeyFrame(Duration.millis(150), e -> primaryStage.setX(originalX - 10)),
                    new KeyFrame(Duration.millis(200), e -> primaryStage.setX(originalX + 10)),
                    new KeyFrame(Duration.millis(250), e -> primaryStage.setX(originalX - 5)),
                    new KeyFrame(Duration.millis(300), e -> primaryStage.setX(originalX + 5)),
                    new KeyFrame(Duration.millis(350), e -> primaryStage.setX(originalX))
            );

            timeline.play();
        }
    }

    public void showTermsOfService() {
        TermsOfServiceDialog tosDialog = new TermsOfServiceDialog();
        tosDialog.showDialog();
    }
}
