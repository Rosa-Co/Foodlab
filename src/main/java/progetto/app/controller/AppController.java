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
import progetto.app.dto.CourseWithSessionsDTO;
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
import progetto.app.model.Allievo;
import progetto.app.model.Chef;
import progetto.app.model.User;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class AppController {
    private static final Image APP_ICON = new Image(
            Objects.requireNonNull(Main.class.getResourceAsStream("/progetto/app/logo.png")));

    private User userLogged;
    private static AppController instance;  // Singleton per accesso globale
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
        Optional<CourseWithSessionsDTO> result;
        try {
            result = AddCourseDialogGUI.showDialog(owner);
        } catch (IOException e) {
            new ErrorDialog("Errore grafico", "Impossibile caricare l'interfaccia.").show();
            return false;
        } catch (NullPointerException e) {
            new ErrorDialog("Errore", "Si è verificato un errore nel caricamento dei dati.").show();
            return false;
        }

        if (result.isPresent()) {
            CourseWithSessionsDTO data = result.get();
            return createCourse(data.getCourse(), data.getSessions());
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

                if ("In Presenza".equals(sDto.getModalita()) && sDto.getRicette() != null) {
                    for (RecipeDTO rDto : sDto.getRicette()) {
                        int recipeId = rDto.getId();
                        if (recipeId == 0) {
                            // Create new recipe
                            Ricetta newRicetta = new Ricetta(rDto.getNome(), rDto.getDescrizione(), "Personalizzata",
                                    chefId);
                            recipeId = getRicettaDAO().addRicetta(newRicetta);
                        }
                        getRicettaDAO().addRicettaSessione(sessione.getId(), recipeId);
                    }
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
        try{
           if(allievoDAO.getAllievoByUsername(username) != null){
               return loginUser(username, password);
           }
        }catch (UserNotFoundException e){
            try{
                if(chefDAO.getChefByUsername(username) != null){
                    return loginChef(username, password);
                }
            }catch (ChefNotFoundException e1){
                showErrorDialog(e.getMessage(),"controlla i dati e riprova");
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
            showWarningDialog(e.getMessage(),"Passa alla schermata login.");
            return false;
        } catch (DAOException e){
            showErrorDialog("Errore durante la registrazione!","Provare più tardi.");
            return false;
        }
    }

    public boolean loginUser(String username, String password) {
        try {
            Allievo allievo = allievoDAO.getAllievoByUsername(username);
            if (allievo == null) return false;
            this.userLogged = allievo;
            String hashedPassword = allievo.getPassword();
            if(!BCrypt.checkpw(password, hashedPassword)){
                showErrorDialog("Password errata.","Riprova.");
                return false;
            }
            return true;
        } catch (DAOException e) {
            showErrorDialog("Errore in fase di login.","Riprova.");
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
        } catch(DuplicateChefException e){
            showWarningDialog(e.getMessage(),"Passa alla schermata login.");
            return false;
        } catch (DAOException e1) {
            showErrorDialog("Password errata","Riprova");
            return false;
        }
    }

    public boolean loginChef(String username, String password) throws UserNotFoundException{
        try {
            Chef chef = chefDAO.getChefByUsername(username);
            if (chef == null) return false;
            this.userLogged=chef;
            String hashedPassword = chef.getPassword();
            if(!BCrypt.checkpw(password, hashedPassword)){
                showErrorDialog("Password errata","Riprova");
                return false;
            }
            return true;
        } catch (DAOException e) {
            showErrorDialog(e.getMessage(),"Riprova");
            return false;
        }
    }

    public boolean searchAllievo(String username, String email) {
        try {
            if(searchAllievoByUsername(username)) return true;
        }catch (UserNotFoundException e) {
            try{
                if(searchAllievoByEmail(email)) return true;
            }catch (UserNotFoundException e1) {
                return false;
            }
        }
        return false;
    }

    public boolean searchChef(String username, String email) {
        try {
            if(searchChefByUsername(username)) return true;
        }catch (ChefNotFoundException e) {
            try{
                if(searchChefByEmail(email)) return true;
            }catch (ChefNotFoundException e1) {
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

    public static boolean doPasswordsMatch(String password, String confirmPassword) {
        return password.equals(confirmPassword);
    }

    public boolean searchAllievoByUsername(String username) throws UserNotFoundException {
        if (allievoDAO.getAllievoByUsername(username) != null) {
            showWarningDialog("Account già esistente.", "Proseguire sulla schermata di accesso.");
            return true;
        }
        return false;
    }
    public boolean searchAllievoByEmail(String email) throws UserNotFoundException {
        if (allievoDAO.getAllievoByEmail(email) != null) {
            showWarningDialog("Account già esistente.", "Proseguire sulla schermata di accesso.");
            return true;
        }
        return false;
    }
    public boolean searchChefByUsername(String username) throws UserNotFoundException {
        if (chefDAO.getChefByUsername(username) != null) {
            showWarningDialog("Account già esistente.", "Proseguire sulla schermata di accesso.");
            return true;
        }
        return false;
    }
    public boolean searchChefByEmail(String email)throws UserNotFoundException {
        if (chefDAO.getChefByEmail(email) != null) {
            showWarningDialog("Account già esistente.", "Proseguire sulla schermata di accesso.");
            return true;
        }
        return false;
    }

    public void showErrorDialog(String title, String message) {
        ErrorDialog errorDialog = new ErrorDialog(title, message);
        errorDialog.show();
    }

    public void showWarningDialog(String title, String message) {
        WarningDialog warningDialog = new WarningDialog(title, message);
        warningDialog.show();
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
