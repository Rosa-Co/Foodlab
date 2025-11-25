package progetto.app.controller;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.stage.Window;
import javafx.util.Duration;
import progetto.app.model.*;
import progetto.app.view.AddCourseDialogGUI;
import progetto.app.dto.CourseDTO;
import progetto.app.dto.RecipeDTO;
import progetto.app.dto.SessionDTO;

import java.util.*;
import java.io.IOException;

import progetto.app.Main;
import org.mindrot.jbcrypt.BCrypt;
import progetto.app.dao.Interface.ChefDAO;
import progetto.app.dao.Interface.AllievoDAO;
import progetto.app.dao.postgree.AllievoDAO_Postgree;
import progetto.app.dao.postgree.ChefDAO_Postgree;
import progetto.app.dao.Interface.CorsoDAO;
import progetto.app.dao.Interface.SessioneDAO;
import progetto.app.dao.Interface.RicettaDAO;
import progetto.app.dao.postgree.CorsoDAO_Postgree;
import progetto.app.dao.postgree.SessioneDAO_Postgree;
import progetto.app.dao.postgree.RicettaDAO_Postgree;
import progetto.app.dialog.ErrorDialog;
import progetto.app.dialog.TermsOfServiceDialog;
import progetto.app.dialog.WarningDialog;
import progetto.app.exception.*;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class AppController {
    private static final Image APP_ICON = new Image(
            Objects.requireNonNull(Main.class.getResourceAsStream("/progetto/app/logo.png")));

    private static AppController instance; // Singleton per accesso globale
    private Stage primaryStage;
    private final Map<String, Parent> views = new HashMap<>();
    private final Map<String, Object> controllers = new HashMap<>();
    private AllievoDAO allievoDAO = getAllievoDAO();
    private ChefDAO chefDAO = getChefDAO();
    private CorsoDAO corsoDAO = getCorsoDAO();
    private SessioneDAO sessioneDAO = getSessioneDAO();
    private RicettaDAO ricettaDAO = getRicettaDAO();
    private static final String EMAIL_REGEX = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

    private static final Pattern pattern = Pattern.compile(EMAIL_REGEX);

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

    public CorsoDAO getCorsoDAO() {
        if (this.corsoDAO == null)
            this.corsoDAO = new CorsoDAO_Postgree();
        return this.corsoDAO;
    }

    public SessioneDAO getSessioneDAO() {
        if (this.sessioneDAO == null)
            this.sessioneDAO = new SessioneDAO_Postgree();
        return this.sessioneDAO;
    }

    public RicettaDAO getRicettaDAO() {
        if (this.ricettaDAO == null)
            this.ricettaDAO = new RicettaDAO_Postgree();
        return this.ricettaDAO;
    }

    public List<RecipeDTO> getAllRecipesDTO() {
        List<RecipeDTO> dtos = new ArrayList<>();
        try {
            List<Ricetta> recipes = getRicettaDAO().getAllRicette();
            for (Ricetta r : recipes) {
                dtos.add(new RecipeDTO(r.getId(), r.getNome()));
            }
        } catch (DAOException e) {
            e.printStackTrace();
            new ErrorDialog("Errore Database", "Impossibile caricare le ricette.").show();
        }
        return dtos;
    }

    public boolean showCreateCourseDialog(Window owner) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/progetto/app/dialog/AddCourseDialog.fxml"));
            DialogPane dialogPane = loader.load();
            AddCourseDialogGUI controller = loader.getController();

            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setDialogPane(dialogPane);
            dialog.setTitle("Crea Nuovo Corso");
            dialog.initOwner(owner);

            Optional<ButtonType> result = dialog.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.FINISH) {
                CourseDTO courseDTO = controller.getCourseDTO();
                List<SessionDTO> sessionDTOs = controller.getSessionDTOs();
                return createCourse(courseDTO, sessionDTOs);
            }
        } catch (IOException e) {
            e.printStackTrace();
            new ErrorDialog("Errore Interfaccia", "Impossibile aprire il dialogo creazione corso.").show();
        }
        return false;
    }

    public boolean createCourse(CourseDTO courseDTO, List<SessionDTO> sessionDTOs) {
        try {
            int chefId = getCurrentChefId();

            Corso corso = new Corso(
                    courseDTO.getTitolo(),
                    courseDTO.getCategoria(),
                    courseDTO.getDataInizio(),
                    courseDTO.getFrequenza(),
                    sessionDTOs.size(),
                    chefId);

            getCorsoDAO().addCorso(corso); // Sets ID in corso object

            int sessionNum = 1;
            for (SessionDTO sDto : sessionDTOs) {
                Sessione sessione = new Sessione(
                        corso.getId(),
                        sessionNum++,
                        sDto.getData(),
                        sDto.getModalita(),
                        sDto.getDurata(),
                        sDto.getDescrizione());
                getSessioneDAO().addSessione(sessione);

                if ("In Presenza".equals(sDto.getModalita()) && sDto.getRicetta() != null) {
                    getRicettaDAO().addRicettaSessione(sessione.getId(), sDto.getRicetta().getId());
                }
            }
            return true;
        } catch (DAOException e) {
            new ErrorDialog("Errore Creazione Corso", "Impossibile creare il corso: " + e.getMessage()).show();
            return false;
        } catch (Exception e) {
            new ErrorDialog("Errore Inatteso", "Si è verificato un errore imprevisto: " + e.getMessage()).show();
            return false;
        }
    }

    public int getCurrentChefId() {
        // TODO: Implementare ancora
        return 1;
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

    public boolean login(String username, String password) {
        try {
            if (allievoDAO.getAllievoByUsername(username) != null) {
                return loginUser(username, password);
            }
        } catch (UserNotFoundException e) {
            try {
                if (chefDAO.getChefByUsername(username) != null) {
                    return loginChef(username, password);
                }
            } catch (ChefNotFoundException e1) {
                ErrorDialog errorDialog = new ErrorDialog(e.getMessage(), "Controlla i dati e riprova.");
                errorDialog.show();
                return false;
            }
        }
        return false;
    }

    public boolean registerUser(String username, String password, String name, String surname, String email) {
        if (searchChef(username, email))
            return false;

        String pswHashed = BCrypt.hashpw(password, BCrypt.gensalt(10));
        Allievo allievo = new Allievo(username, pswHashed, email, name, surname);

        try {
            allievoDAO.addAllievo(allievo);
            return true;
        } catch (DuplicateUserException e) {
            WarningDialog warn = new WarningDialog(e.getMessage(), "Passa alla schermata login.");
            warn.show();
            return false;
        } catch (DAOException e) {
            ErrorDialog errorDialog = new ErrorDialog("Errore durante la registrazione!", "Provare più tardi.");
            errorDialog.show();
            return false;
        }
    }

    public boolean loginUser(String username, String password) {
        try {
            Allievo allievo = allievoDAO.getAllievoByUsername(username);
            if (allievo == null)
                return false;
            String hashedPassword = allievo.getPassword();
            if (!BCrypt.checkpw(password, hashedPassword)) {
                ErrorDialog errorDialog = new ErrorDialog("Password errata.", "Riprova.");
                errorDialog.show();
                return false;
            }
            return true;
        } catch (DAOException e) {
            ErrorDialog errorDialog = new ErrorDialog("Errore in fase di login.", "Riprova.");
            errorDialog.show();
            return false;
        }
    }

    public boolean registerChef(String username, String password, String name, String surname, String email) {
        if (searchAllievo(username, email))
            return false;

        String pswHashed = BCrypt.hashpw(password, BCrypt.gensalt(10));
        Chef chef = new Chef(username, pswHashed, email, name, surname);

        try {
            chefDAO.addChef(chef);
            return true;
        } catch (DuplicateChefException e) {
            WarningDialog warn = new WarningDialog(e.getMessage(), "Passa alla schermata login.");
            warn.show();
            return false;
        } catch (DAOException e1) {
            ErrorDialog errorDialog = new ErrorDialog("Registrazione chef fallita!", "Provare più tardi.");
            errorDialog.show();
            return false;
        }
    }

    public boolean loginChef(String username, String password) {
        try {
            Chef chef = chefDAO.getChefByUsername(username);
            if (chef == null)
                return false;
            String hashedPassword = chef.getPassword();
            if (!BCrypt.checkpw(password, hashedPassword)) {
                ErrorDialog errorDialog = new ErrorDialog("Password errata", "Riprova");
                errorDialog.show();
                return false;
            }
            return true;
        } catch (DAOException e) {
            ErrorDialog errorDialog = new ErrorDialog(e.getMessage(), "Riprova");
            errorDialog.show();
            return false;
        }
    }

    public boolean searchAllievo(String username, String email) {
        try {
            if (allievoDAO.getAllievoByUsername(username) != null) {
                WarningDialog warningDialog = new WarningDialog("Account già esistente.",
                        "Proseguire sulla schermata di accesso.");
                warningDialog.show();
                return true;
            }
        } catch (UserNotFoundException e) {
            try {
                if (allievoDAO.getAllievoByEmail(email) != null) {
                    WarningDialog warningDialog = new WarningDialog("Account già esistente.",
                            "Proseguire sulla schermata di accesso.");
                    warningDialog.show();
                    return true;
                }
            } catch (UserNotFoundException e1) {
                return false;
            }
        }
        return false;
    }

    public boolean searchChef(String username, String email) {
        try {
            if (chefDAO.getChefByUsername(username) != null) {
                WarningDialog warningDialog = new WarningDialog("Account già esistente.",
                        "Proseguire sulla schermata di accesso.");
                warningDialog.show();
                return true;
            }
        } catch (ChefNotFoundException e) {
            try {
                if (chefDAO.getChefByEmail(email) != null) {
                    WarningDialog warningDialog = new WarningDialog("Account già esistente.",
                            "Proseguire sulla schermata di accesso.");
                    warningDialog.show();
                    return true;
                }
            } catch (ChefNotFoundException e1) {
                return false;
            }
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

    public boolean isLogged(String username) {
        try {
            return allievoDAO.getAllievoByUsername(username) != null;
        } catch (UserNotFoundException e) {
            try {
                return chefDAO.getChefByUsername(username) != null;
            } catch (ChefNotFoundException e1) {
                ErrorDialog errorDialog = new ErrorDialog(e.getMessage(), "Riprova.");
                errorDialog.show();
                return false;
            }
        }
    }

    public void showTermsOfService() {
        TermsOfServiceDialog tosDialog = new TermsOfServiceDialog();
        tosDialog.showDialog();
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
                    new KeyFrame(Duration.millis(350), e -> primaryStage.setX(originalX)));

            timeline.play();
        }
    }
}
