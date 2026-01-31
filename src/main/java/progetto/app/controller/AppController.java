package progetto.app.controller;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Duration;
import org.mindrot.jbcrypt.BCrypt;
import progetto.app.Main;
import progetto.app.dao.Interface.*;
import progetto.app.dao.postgree.*;
import progetto.app.dialog.ErrorDialog;
import progetto.app.dialog.TermsOfServiceDialog;
import progetto.app.dialog.WarningDialog;
import progetto.app.dto.ChefStatsDTO;
import progetto.app.dto.CourseDTO;
import progetto.app.dto.CourseWithSessionsDTO;
import progetto.app.dto.NotificationDTO;
import progetto.app.dto.RecipeDTO;
import progetto.app.dto.SessionDTO;
import progetto.app.exception.*;
import progetto.app.model.*;
import progetto.app.view.AddCourseDialogGUI;
import progetto.app.view.AddNotificationDialogGUI;
import progetto.app.view.AddRecipeDialogGUI;
import progetto.app.view.CourseDetailsDialogGUI;
import progetto.app.view.CoursesViewGUI;
import progetto.app.view.LoginGUI;

import java.io.IOException;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AppController {
    private static final Image APP_ICON = new Image(
            Objects.requireNonNull(Main.class.getResourceAsStream("/progetto/app/logo.png")));

    private User userLogged;
    private static AppController instance; // Singleton per accesso globale
    private Stage primaryStage;
    private final Map<String, Parent> views = new HashMap<>();
    private final Map<String, Object> controllers = new HashMap<>();
    private AllievoDAO allievoDAO = getAllievoDAO();
    private ChefDAO chefDAO = getChefDAO();
    private CorsoDAO corsoDAO = getCorsoDAO();
    private SessioneDAO sessioneDAO = getSessioneDAO();
    private RicettaDAO ricettaDAO = getRicettaDAO();
    private NotificaDAO notificaDAO = getNotificaDAO();
    private StatsDAO statsDAO = getStatsDAO();
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

    public NotificaDAO getNotificaDAO() {
        if (this.notificaDAO == null)
            this.notificaDAO = new NotificaDAO_Postgree();
        return this.notificaDAO;
    }

    public StatsDAO getStatsDAO() {
        if (this.statsDAO == null)
            this.statsDAO = new StatsDAO_Postgree();
        return this.statsDAO;
    }

    public ChefStatsDTO getChefReportData() {
        if (userLogged != null && userLogged.isChef()) {
            try {
                return statsDAO.getChefStats(getCurrentChefId());
            } catch (DAOException e) {
                new ErrorDialog("Errore Report", "Impossibile recuperare i dati del report: " + e.getMessage()).show();
            }
        }
        return new ChefStatsDTO();
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

    public List<CourseDTO> getCoursesData() {
        if (userLogged != null && userLogged.isChef()) {
            try {
                List<Corso> courses = corsoDAO.getCorsiByChef(getCurrentChefId());
                List<CourseDTO> coursesData = new ArrayList<>();
                for (Corso c : courses) {
                    coursesData.add(new CourseDTO(
                            c.getId(),
                            c.getTitolo(),
                            c.getCategoria(),
                            c.getDataInizio(),
                            c.getFrequenza(),
                            c.getNumeroSessioni()));
                }
                return coursesData;
            } catch (DAOException e) {
                new ErrorDialog("Errore di Caricamento", "Impossibile caricare i corsi: " + e.getMessage()).show();
            }
        }
        return new ArrayList<>();
    }

    /* ------------------- RECIPES ------------------- */

    public List<RecipeDTO> getRecipesData() {
        if (userLogged != null && userLogged.isChef()) {
            try {
                List<Ricetta> recipes = ricettaDAO.getRicetteByChef(getCurrentChefId());
                List<RecipeDTO> recipesData = new ArrayList<>();
                for (Ricetta r : recipes) {
                    recipesData.add(new RecipeDTO(
                            r.getId(),
                            r.getNome(),
                            r.getDescrizione()));
                }
                return recipesData;
            } catch (DAOException e) {
                new ErrorDialog("Errore di Caricamento", "Impossibile caricare le ricette: " + e.getMessage()).show();
            }
        }
        return new ArrayList<>();
    }

    public boolean createRecipe(RecipeDTO recipeDTO) {
        if (userLogged != null && userLogged.isChef()) {
            try {
                Ricetta newRecipe = new Ricetta(recipeDTO.getNome(), recipeDTO.getDescrizione(), getCurrentChefId());
                ricettaDAO.addRicetta(newRecipe);
                return true;
            } catch (DAOException e) {
                new ErrorDialog("Errore di Creazione", "Impossibile creare la ricetta: " + e.getMessage()).show();
                return false;
            }
        }
        return false;
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

    public boolean showCreateRecipeDialog(Window owner) {
        try {
            Optional<RecipeDTO> result = AddRecipeDialogGUI.showDialog(owner);
            if (result.isPresent()) {
                return createRecipe(result.get());
            }
        } catch (IOException e) {
            e.printStackTrace();
            new ErrorDialog("Errore grafico", "Impossibile caricare l'interfaccia.").show();
        } catch (Exception e) {
            e.printStackTrace();
            new ErrorDialog("Errore", "Si è verificato un errore: " + e.getMessage()).show();
        }
        return false;
    }

    public boolean createCourse(CourseDTO courseDTO, List<SessionDTO> sessionDTOs) {
        try {
            int chefId = getCurrentChefId();
            System.out.println("[DEBUG : ] Creating course for chef ID: " + chefId + " " + this.userLogged.getName());

            Corso corso = new Corso(
                    courseDTO.getTitolo(),
                    courseDTO.getCategoria(),
                    courseDTO.getDataInizio(),
                    courseDTO.getFrequenza(),
                    sessionDTOs.size(),
                    chefId);

            corsoDAO.addCorso(corso);

            int sessionNum = 1;
            for (SessionDTO sDto : sessionDTOs) {
                Sessione sessione = new Sessione(
                        corso.getId(),
                        sessionNum++,
                        sDto.getDataSessione(),
                        sDto.getModalita(),
                        sDto.getDurata(),
                        sDto.getDescrizione());
                sessioneDAO.addSessione(sessione);

                if ("In Presenza".equals(sDto.getModalita()) && sDto.getRicette() != null) {
                    for (RecipeDTO rDto : sDto.getRicette()) {
                        int recipeId = rDto.getId();
                        if (recipeId == 0) {
                            // Create new recipe
                            Ricetta newRicetta = new Ricetta(rDto.getNome(), rDto.getDescrizione(),
                                    chefId);
                            recipeId = ricettaDAO.addRicetta(newRicetta); // returns id
                        }
                        ricettaDAO.addRicettaSessione(sessione.getId(), recipeId);
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
        return userLogged.getId();
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

    // --- GESTIONE SESSIONI ---

    public List<SessionDTO> getSessioniByCorso(int corsoId) {
        List<SessionDTO> result = new ArrayList<>();
        try {
            List<Sessione> sessioni = sessioneDAO.getSessioniByCorso(corsoId);
            for (Sessione s : sessioni) {
                result.add(new SessionDTO(
                        s.getId(),
                        s.getNumeroSessione(),
                        s.getDataSessione(),
                        s.getModalita(),
                        s.getDurata(),
                        s.getDescrizione()));
            }
        } catch (DAOException e) {
            new ErrorDialog("Errore", "Impossibile recuperare le sessioni: " + e.getMessage()).show();
        }
        return result;
    }

    public boolean deleteSession(int sessionId) {
        try {
            sessioneDAO.deleteSessione(sessionId);
            return true;
        } catch (DAOException e) {
            new ErrorDialog("Errore Eliminazione", "Impossibile eliminare la sessione: " + e.getMessage()).show();
            return false;
        }
    }

    public boolean updateSession(int sessionId, SessionDTO sessionDTO) {
        try {
            SessioneDAO dao = getSessioneDAO();

            int dummyCorsoId = 0;
            int dummyNumSessione = 0;

            Sessione s = new Sessione(sessionId, dummyCorsoId, dummyNumSessione,
                    sessionDTO.getDataSessione(),
                    sessionDTO.getModalita(),
                    sessionDTO.getDurata(),
                    sessionDTO.getDescrizione());
            dao.updateSessione(s);
            return true;
        } catch (DAOException e) {
            new ErrorDialog("Errore Aggiornamento", "Impossibile aggiornare la sessione: " + e.getMessage()).show();
            return false;
        }
    }

    public void showCourseDetailsDialog(Window owner, int corsoId, String corsoTitolo) {
        try {
            boolean changed = CourseDetailsDialogGUI.showDialog(owner, corsoId, corsoTitolo);
            if (changed) {
                CoursesViewGUI coursesController = (CoursesViewGUI) controllers.get("courses");
                if (coursesController != null) {
                    coursesController.loadCourses();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            new ErrorDialog("Errore", "Impossibile aprire i dettagli del corso.").show();
        }
    }

    // --- NOTIFICHE ---

    // --- NOTIFICHE ---

    // --- NOTIFICHE ---

    public List<NotificationDTO> getNotificationsData() {
        if (userLogged == null || !userLogged.isChef())
            return new ArrayList<>();

        List<NotificationDTO> result = new ArrayList<>();

        try {
            // Load all courses for the chef to map ID -> Title
            List<Corso> corsi = corsoDAO.getCorsiByChef(userLogged.getId());
            Map<Integer, String> corsiMap = new HashMap<>();
            for (Corso c : corsi) {
                corsiMap.put(c.getId(), c.getTitolo());
            }

            List<Notifica> notifiche = notificaDAO.getNotificheByChef(userLogged.getId());
            for (Notifica n : notifiche) {
                String target = "Tutti i corsi";
                if (n.getIdCorso() != null && n.getIdCorso() != 0) {
                    // Check if map contains the ID, otherwise fallback to ID
                    if (corsiMap.containsKey(n.getIdCorso())) {
                        target = "Corso: " + corsiMap.get(n.getIdCorso());
                    } else {
                        target = "Corso ID: " + n.getIdCorso();
                    }
                }
                result.add(new NotificationDTO(n.getTitolo(), n.getContenuto(), target));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public boolean createNotification(NotificationDTO notificationDTO) {
        if (userLogged == null)
            return false;

        String titolo = notificationDTO.getTitolo();
        String contenuto = notificationDTO.getContenuto();
        Integer corsoId = notificationDTO.getCorsoId();

        Notifica notifica = new Notifica(titolo, contenuto, userLogged.getId(),
                corsoId);

        try {
            notificaDAO.addNotifica(notifica);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<CourseDTO> getSimpleCoursesData() {
        if (userLogged == null || !userLogged.isChef())
            return new ArrayList<>();

        List<CourseDTO> result = new ArrayList<>();
        try {
            List<Corso> corsi = corsoDAO.getCorsiByChef(userLogged.getId());

            for (Corso c : corsi) {
                // Populate minimal info
                CourseDTO dto = new CourseDTO();
                dto.setId(c.getId());
                dto.setTitolo(c.getTitolo());
                result.add(dto);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public void showCreateNotificationDialog(Window owner) {
        try {
            Optional<NotificationDTO> result = AddNotificationDialogGUI.showDialog(owner);
            if (result.isPresent()) {
                createNotification(result.get());
            }
        } catch (Exception e) {
            e.printStackTrace();
            new ErrorDialog("Errore", "Impossibile aprire il dialogo notifica.").show();
        }
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

    public void logout() {
        this.userLogged = null;
        LoginGUI loginGUI = (LoginGUI) getController("login");
        loginGUI.clearLoginFields();
        loginGUI.clearRegisterFields();
        navigateToLogin();
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
            if (view.getScene() != null) {
                primaryStage.setScene(view.getScene());
            } else {
                primaryStage.setScene(new Scene(view));
            }
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
                return loginAllievo(username, password);
            }
        } catch (AllievoNotFoundException e) {
            try {
                if (chefDAO.getChefByUsername(username) != null) {
                    return loginChef(username, password);
                }
            } catch (ChefNotFoundException e1) {
                showErrorDialog(e.getMessage(), "controlla i dati e riprova");
                return false;
            }
        }
        return false;
    }

    public boolean registerAllievo(String username, String password, String name, String surname, String email) {
        if (searchChef(username, email))
            return false;

        String pswHashed = BCrypt.hashpw(password, BCrypt.gensalt(10));
        Allievo allievo = new Allievo(username, pswHashed, email, name, surname);

        try {
            allievoDAO.addAllievo(allievo);
            return true;
        } catch (DuplicateAllievoException e) {
            showWarningDialog(e.getMessage(), "Passa alla schermata login.");
            return false;
        } catch (DAOException e) {
            showErrorDialog("Errore durante la registrazione!", "Provare più tardi.");
            return false;
        }
    }

    public boolean loginAllievo(String username, String password) throws AllievoNotFoundException {
        try {
            Allievo allievo = allievoDAO.getAllievoByUsername(username);
            if (allievo == null)
                return false;
            this.userLogged = allievo;
            String hashedPassword = allievo.getPassword();
            if (!BCrypt.checkpw(password, hashedPassword)) {
                showErrorDialog("Password errata.", "Riprova.");
                return false;
            }
            return true;
        } catch (DAOException e) {
            showErrorDialog("Errore in fase di login.", "Riprova.");
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
            showWarningDialog(e.getMessage(), "Passa alla schermata login.");
            return false;
        } catch (DAOException e1) {
            showErrorDialog("Password errata", "Riprova");
            return false;
        }
    }

    public boolean loginChef(String username, String password) throws ChefNotFoundException {
        try {
            Chef chef = chefDAO.getChefByUsername(username);
            if (chef == null)
                return false;
            this.userLogged = chef;
            String hashedPassword = chef.getPassword();
            if (!BCrypt.checkpw(password, hashedPassword)) {
                showErrorDialog("Password errata", "Riprova");
                return false;
            }
            return true;
        } catch (DAOException e) {
            showErrorDialog(e.getMessage(), "Riprova");
            return false;
        }
    }

    public boolean searchAllievo(String username, String email) {
        try {
            if (searchAllievoByUsername(username))
                return true;
        } catch (AllievoNotFoundException e) {
            try {
                if (searchAllievoByEmail(email))
                    return true;
            } catch (AllievoNotFoundException e1) {
                return false;
            }
        }
        return false;
    }

    public boolean searchChef(String username, String email) {
        try {
            if (searchChefByUsername(username))
                return true;
        } catch (ChefNotFoundException e) {
            try {
                if (searchChefByEmail(email))
                    return true;
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

    public static boolean doPasswordsMatch(String password, String confirmPassword) {
        return password.equals(confirmPassword);
    }

    public boolean searchAllievoByUsername(String username) throws AllievoNotFoundException {
        if (allievoDAO.getAllievoByUsername(username) != null) {
            showWarningDialog("Account già esistente.", "Proseguire sulla schermata di accesso.");
            return true;
        }
        return false;
    }

    public boolean searchAllievoByEmail(String email) throws AllievoNotFoundException {
        if (allievoDAO.getAllievoByEmail(email) != null) {
            showWarningDialog("Account già esistente.", "Proseguire sulla schermata di accesso.");
            return true;
        }
        return false;
    }

    public boolean searchChefByUsername(String username) throws ChefNotFoundException {
        if (chefDAO.getChefByUsername(username) != null) {
            showWarningDialog("Account già esistente.", "Proseguire sulla schermata di accesso.");
            return true;
        }
        return false;
    }

    public boolean searchChefByEmail(String email) throws ChefNotFoundException {
        if (chefDAO.getChefByEmail(email) != null) {
            showWarningDialog("Account già esistente.", "Proseguire sulla schermata di accesso.");
            return true;
        }
        return false;
    }

    public void showErrorDialog(String title, String message) {
        ErrorDialog errorDialog = new ErrorDialog(title, message);
        shakeWindow();
        errorDialog.show();
    }

    public void showWarningDialog(String title, String message) {
        WarningDialog warningDialog = new WarningDialog(title, message);
        shakeWindow();
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
