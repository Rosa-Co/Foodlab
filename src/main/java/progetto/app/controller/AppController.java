package progetto.app.controller;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
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
import progetto.app.dto.*;
import progetto.app.exception.*;
import progetto.app.model.*;
import progetto.app.view.*;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.time.temporal.WeekFields.ISO;

/**
 * Controller principale, implementato come Singleton.
 * <p>
 * Centralizza tutta la logica applicativa: navigazione tra le viste JavaFX,
 * autenticazione (login/registrazione) di {@link Chef} e
 * {@link Allievo}, gestione dei corsi, sessioni, ricette
 * e notifiche. Funge da intermediario tra il layer di presentazione (GUI) e
 * il layer di accesso ai dati (DAO).
 * </p>
 *
 * <p>
 * Viene inizializzato una sola volta all'avvio dell'applicazione tramite
 * {@link #getInstance()} e mantiene lo stato dell'utente correntemente
 * autenticato.
 * </p>
 */
public class AppController {

    /**
     * Icona dell'applicazione caricata dal classpath, usata per decorare gli stage
     * JavaFX.
     */
    private static final Image APP_ICON = new Image(
            Objects.requireNonNull(Main.class.getResourceAsStream("/progetto/app/logo.png")));

    /**
     * L'utente attualmente autenticato (può essere un
     * {@link Chef} o un {@link Allievo}).
     */
    private User userLogged;

    /** Istanza Singleton del controller. */
    private static AppController instance; // Singleton per accesso globale

    /** Lo stage primario dell'applicazione JavaFX. */
    private Stage primaryStage;

    /** Cache delle viste FXML caricate, indicizzate per nome logico. */
    private final Map<String, Parent> views = new HashMap<>();

    /** Cache dei controller JavaFX associati alle viste caricate. */
    private final Map<String, Object> controllers = new HashMap<>();

    /** DAO per la gestione degli allievi. */
    private AllievoDAO allievoDAO = getAllievoDAO();

    /** DAO per la gestione degli chef. */
    private ChefDAO chefDAO = getChefDAO();

    /** DAO per la gestione dei corsi. */
    private CorsoDAO corsoDAO = getCorsoDAO();

    /** DAO per la gestione delle sessioni. */
    private SessioneDAO sessioneDAO = getSessioneDAO();

    /** DAO per la gestione delle ricette. */
    private RicettaDAO ricettaDAO = getRicettaDAO();

    /** DAO per la gestione delle notifiche. */
    private NotificaDAO notificaDAO = getNotificaDAO();

    /** DAO per la gestione delle statistiche chef. */
    private StatsDAO statsDAO = getStatsDAO();

    /** Espressione regolare per la validazione del formato email. */
    private static final String EMAIL_REGEX = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

    /** Pattern compilato per la validazione delle email. */
    private static final Pattern pattern = Pattern.compile(EMAIL_REGEX);

    /**
     * Costruttore privato. Utilizzare {@link #getInstance()} per ottenere
     * l'istanza.
     */
    private AppController() {
    }

    /**
     * Restituisce l'unica istanza di {@code AppController} (pattern Singleton).
     *
     * @return l'istanza singleton di {@code AppController}
     */
    public static AppController getInstance() {
        if (instance == null)
            instance = new AppController();
        return instance;
    }

    /**
     * Imposta lo stage primario dell'applicazione JavaFX.
     *
     * @param stage lo {@link Stage} principale dell'applicazione
     */
    public void setPrimaryStage(Stage stage) {
        this.primaryStage = stage;
    }

    /**
     * Restituisce il DAO per gli allievi, inizializzandolo lazily se necessario.
     *
     * @return l'istanza di {@link AllievoDAO}
     */
    public AllievoDAO getAllievoDAO() {
        if (this.allievoDAO == null)
            this.allievoDAO = new AllievoDAO_Postgree();
        return this.allievoDAO;
    }

    /**
     * Restituisce il DAO per gli chef, inizializzandolo lazily se necessario.
     *
     * @return l'istanza di {@link ChefDAO}
     */
    public ChefDAO getChefDAO() {
        if (this.chefDAO == null)
            this.chefDAO = new ChefDAO_Postgree();
        return this.chefDAO;
    }

    /**
     * Restituisce il DAO per i corsi, inizializzandolo lazily se necessario.
     *
     * @return l'istanza di {@link CorsoDAO}
     */
    public CorsoDAO getCorsoDAO() {
        if (this.corsoDAO == null)
            this.corsoDAO = new CorsoDAO_Postgree();
        return this.corsoDAO;
    }

    /**
     * Restituisce il DAO per le sessioni, inizializzandolo lazily se necessario.
     *
     * @return l'istanza di {@link SessioneDAO}
     */
    public SessioneDAO getSessioneDAO() {
        if (this.sessioneDAO == null)
            this.sessioneDAO = new SessioneDAO_Postgree();
        return this.sessioneDAO;
    }

    /**
     * Restituisce il DAO per le ricette, inizializzandolo lazily se necessario.
     *
     * @return l'istanza di {@link RicettaDAO}
     */
    public RicettaDAO getRicettaDAO() {
        if (this.ricettaDAO == null)
            this.ricettaDAO = new RicettaDAO_Postgree();
        return this.ricettaDAO;
    }

    /**
     * Restituisce il DAO per le notifiche, inizializzandolo lazily se necessario.
     *
     * @return l'istanza di {@link NotificaDAO}
     */
    public NotificaDAO getNotificaDAO() {
        if (this.notificaDAO == null)
            this.notificaDAO = new NotificaDAO_Postgree();
        return this.notificaDAO;
    }

    /**
     * Restituisce il DAO per le statistiche, inizializzandolo lazily se necessario.
     *
     * @return l'istanza di {@link StatsDAO}
     */
    public StatsDAO getStatsDAO() {
        if (this.statsDAO == null)
            this.statsDAO = new StatsDAO_Postgree();
        return this.statsDAO;
    }

    /**
     * Recupera i dati statistici mensili dello chef correntemente autenticato.
     * <p>
     * Interroga il {@link StatsDAO} per ottenere informazioni aggregate sui corsi
     * tenuti (numero totale, sessioni online/in presenza, media/max/min ricette).
     * Restituisce un DTO vuoto se l'utente non è uno chef o si verifica un errore.
     * </p>
     *
     * @return un {@link ChefStatsDTO} con i dati statistici dello chef, mai
     *         {@code null}
     */
    public ChefStatsDTO getChefReportData() {
        if (userLogged != null && userLogged.isChef()) {
            try {
                return getStatsDAO().getChefStats(getCurrentChefId());
            } catch (DAOException e) {
                Platform.runLater(() -> new ErrorDialog("Errore Report",
                        "Impossibile recuperare i dati del report: " + e.getMessage()).show());
            }
        }
        return new ChefStatsDTO();
    }

    /**
     * Recupera tutte le ricette presenti nel sistema come lista di
     * {@link RecipeDTO}.
     * <p>
     * Utilizzato per popolare selettori o liste ricette globali (non filtrate per
     * chef).
     * In caso di errore DAO mostra un {@link progetto.app.dialog.ErrorDialog}.
     * </p>
     *
     * @return lista di {@link RecipeDTO} contenenti id e nome di ogni ricetta;
     *         lista vuota in caso di errore
     */
    public List<RecipeDTO> getAllRecipesDTO() {
        List<RecipeDTO> dtos = new ArrayList<>();
        try {
            List<Ricetta> recipes = getRicettaDAO().getAllRicette();
            for (Ricetta r : recipes) {
                dtos.add(new RecipeDTO(r.getId(), r.getNome()));
            }
        } catch (DAOException e) {
            e.printStackTrace();
            Platform.runLater(() -> new ErrorDialog("Errore Database", "Impossibile caricare le ricette.").show());
        }
        return dtos;
    }

    /**
     * Recupera i corsi dell'utente chef autenticato come lista di
     * {@link CourseDTO}.
     * <p>
     * Ogni {@link CourseDTO} contiene id, titolo, categoria, data di inizio,
     * frequenza e numero di sessioni del corso.
     * Restituisce una lista vuota se l'utente non è uno chef o si verifica un
     * errore.
     * </p>
     *
     * @return lista di {@link CourseDTO} dei corsi dello chef; lista vuota se non
     *         applicabile
     */
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
                Platform.runLater(() -> new ErrorDialog("Errore di Caricamento",
                        "Impossibile caricare i corsi: " + e.getMessage()).show());
            }
        }
        return new ArrayList<>();
    }

    /* ------------------- RECIPES ------------------- */

    /**
     * Recupera le ricette dello chef autenticato come lista di {@link RecipeDTO}.
     * <p>
     * A differenza di {@link #getAllRecipesDTO()}, filtra le ricette per lo chef
     * correntemente autenticato e include anche la descrizione nel DTO.
     * </p>
     *
     * @return lista di {@link RecipeDTO} delle ricette dello chef; lista vuota se
     *         non applicabile
     */
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
                Platform.runLater(() -> new ErrorDialog("Errore di Caricamento",
                        "Impossibile caricare le ricette: " + e.getMessage()).show());
            }
        }
        return new ArrayList<>();
    }

    /**
     * Crea una nuova ricetta per lo chef autenticato a partire da un
     * {@link RecipeDTO}.
     * <p>
     * Valida che nome e descrizione abbiano almeno 5 caratteri, poi persiste
     * la nuova {@link progetto.app.model.Ricetta} tramite il DAO.
     * Mostra un {@link progetto.app.dialog.ErrorDialog} per ogni tipo di errore.
     * </p>
     *
     * @param recipeDTO il DTO contenente nome e descrizione della nuova ricetta
     * @return {@code true} se la ricetta è stata creata con successo, {@code false}
     *         altrimenti
     */
    public boolean createRecipe(RecipeDTO recipeDTO) {
        if (userLogged != null && userLogged.isChef()) {
            try {
                if (recipeDTO.getDescrizione().length() < 5 || recipeDTO.getNome().length() < 5) {
                    throw new LengthException(
                            "La descrizione o il nome della ricetta non possono essere inferiori a 5 caratteri.");
                }
                Ricetta newRecipe = new Ricetta(recipeDTO.getNome(), recipeDTO.getDescrizione(), getCurrentChefId());
                ricettaDAO.addRicetta(newRecipe);
                return true;
            } catch (LengthException e) {
                new ErrorDialog("Errore di Creazione",
                        "Il nome e la descrizione della ricetta devono essere lunghe almeno 5 caratteri.").show();
                return false;
            } catch (DuplicateRecipeException e) {
                new ErrorDialog("Errore di Creazione", "Non sono ammesse ricette con lo stesso nome.").show();
                return false;
            } catch (DAOException e) {
                new ErrorDialog("Errore di Creazione", "Impossibile creare la ricetta: " + e.getMessage()).show();
                return false;
            } catch (SQLException e) {
                new ErrorDialog("Errore di Creazione", "Errore imprevisto: " + e.getMessage()).show();
                return false;
            }
        }
        return false;
    }

    /**
     * Apre il dialog per la creazione di un nuovo corso e, se confermato, esegue la
     * creazione.
     * <p>
     * Mostra {@link progetto.app.view.AddCourseDialogGUI} e, se l'utente conferma,
     * chiama {@link #createCourse(CourseDTO, List)} con i dati inseriti.
     * </p>
     *
     * @param owner la finestra proprietaria del dialog
     * @return {@code true} se il corso è stato creato con successo, {@code false}
     *         altrimenti
     */
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

    /**
     * Apre il dialog per la creazione di una nuova ricetta e, se confermato, esegue
     * la creazione.
     * <p>
     * Mostra {@link progetto.app.view.AddRecipeDialogGUI} e, se l'utente conferma,
     * chiama {@link #createRecipe(RecipeDTO)} con i dati inseriti.
     * </p>
     *
     * @param owner la finestra proprietaria del dialog
     * @return {@code true} se la ricetta è stata creata con successo, {@code false}
     *         altrimenti
     */
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

    /**
     * Crea un nuovo corso con le relative sessioni e, per le sessioni in presenza,
     * le ricette.
     * <p>
     * Esegue le validazioni tramite {@link #checkDtos}, {@link #checkDates} e
     * {@link #checkFrequency}, quindi persiste il {@link progetto.app.model.Corso},
     * le {@link progetto.app.model.Sessione} e le eventuali
     * {@link progetto.app.model.Ricetta} associate.
     * Le ricette nuove (id == 0) vengono create al volo.
     * </p>
     *
     * @param courseDTO   il DTO con i dati del corso (titolo, categoria, data,
     *                    frequenza)
     * @param sessionDTOs la lista dei DTO delle sessioni da creare
     * @return {@code true} se il corso è stato creato con successo, {@code false}
     *         altrimenti
     */
    public boolean createCourse(CourseDTO courseDTO, List<SessionDTO> sessionDTOs) {
        try {
            int chefId = getCurrentChefId();
            checkDtos(courseDTO, sessionDTOs);
            checkDates(courseDTO, sessionDTOs);
            checkFrequency(courseDTO, sessionDTOs);
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
                            Ricetta newRicetta = new Ricetta(rDto.getNome(), rDto.getDescrizione(),
                                    chefId);
                            recipeId = ricettaDAO.addRicetta(newRicetta); // returns id
                        }
                        ricettaDAO.addRicettaSessione(sessione.getId(), recipeId);
                    }
                }
            }
            return true;
        } catch (IllegalArgumentException e) {
            new ErrorDialog("Errore Campi Vuoti", e.getMessage()).show();
            return false;
        } catch (FrequencyException e) {
            new ErrorDialog("Errore Frequenza", e.getMessage()).show();
            return false;
        } catch (CourseCreationException e) {
            new ErrorDialog("Errore Creazione Corso", e.getMessage()).show();
            return false;
        } catch (DAOException e) {
            new ErrorDialog("Errore", e.getMessage()).show();
            return false;
        } catch (Exception e) {
            new ErrorDialog("Errore Inatteso", "Si è verificato un errore imprevisto: " + e.getMessage()).show();
            return false;
        }
    }

    /**
     * Verifica che le date di tutte le sessioni non siano antecedenti alla data di
     * inizio del corso.
     *
     * @param courseDTO   il DTO del corso contenente la data di inizio
     * @param sessionDTOs la lista dei DTO delle sessioni da verificare
     * @throws CourseCreationException se almeno una sessione ha data antecedente
     *                                 all'inizio del corso
     */
    public void checkDates(CourseDTO courseDTO, List<SessionDTO> sessionDTOs) throws CourseCreationException {
        for (int i = 0; i < sessionDTOs.size(); i++) {
            if (courseDTO.getDataInizio().isAfter(sessionDTOs.get(i).getDataSessione())) {
                throw new CourseCreationException("La data di una sessione è antecedente all'inizio del corso");
            }
        }
    }

    /**
     * Valida che i DTO del corso e delle sessioni non siano nulli o privi di dati
     * obbligatori.
     *
     * @param courseDTO   il DTO del corso da validare
     * @param sessionDTOs la lista dei DTO delle sessioni; deve contenere almeno un
     *                    elemento
     * @throws IllegalArgumentException se il corso è null, la lista sessioni è
     *                                  vuota,
     *                                  o mancano campi obbligatori nel corso
     */
    public void checkDtos(CourseDTO courseDTO, List<SessionDTO> sessionDTOs) throws IllegalArgumentException {
        if (courseDTO == null) {
            throw new IllegalArgumentException("Riempire tutti i campi");
        }
        if (sessionDTOs == null || sessionDTOs.isEmpty()) {
            throw new IllegalArgumentException("Riempire ALMENO una sessione o riempirne tutti i campi");
        }
        if (courseDTO.getTitolo() == null || courseDTO.getTitolo().isBlank()
                || courseDTO.getCategoria() == null || courseDTO.getCategoria().isBlank()
                || courseDTO.getDataInizio() == null
                || courseDTO.getFrequenza() == null || courseDTO.getFrequenza().isBlank()) {
            throw new IllegalArgumentException("Riempire tutti i campi del corso");
        }
    }

    /**
     * Verifica che le date delle sessioni rispettino la frequenza dichiarata per il
     * corso.
     * <p>
     * Gestisce le frequenze: {@code Mensile}, {@code Settimanale},
     * {@code Bisettimanale},
     * {@code Trisettimanale}, delegando ai metodi privati specifici.
     * </p>
     *
     * @param courseDTO   il DTO del corso contenente la frequenza da rispettare
     * @param sessionDTOs la lista dei DTO delle sessioni da verificare
     * @throws FrequencyException se le sessioni non rispettano la frequenza
     *                            dichiarata
     */
    public void checkFrequency(CourseDTO courseDTO, List<SessionDTO> sessionDTOs) throws FrequencyException {
        String frequenza = courseDTO.getFrequenza();

        switch (frequenza) {
            case "Mensile":
                checkMonthlyFrequency(sessionDTOs);
                break;
            case "Settimanale":
                checkWeeklyFrequency(sessionDTOs, 1);
                break;
            case "Bisettimanale":
                checkWeeklyFrequency(sessionDTOs, 2);
                break;
            case "Trisettimanale":
                checkWeeklyFrequency(sessionDTOs, 3);
                break;
        }
    }

    /**
     * Verifica che non ci sia più di una sessione per mese (frequenza mensile).
     *
     * @param sessionDTOs la lista dei DTO delle sessioni
     * @throws FrequencyException se un mese contiene più di una sessione
     */
    private void checkMonthlyFrequency(List<SessionDTO> sessionDTOs) throws FrequencyException {
        Map<String, Integer> sessioniPerMese = new HashMap<>();
        for (int i = 0; i < sessionDTOs.size(); i++) {
            LocalDate data = sessionDTOs.get(i).getDataSessione();
            String chiave = data.getYear() + "-" + data.getMonthValue();
            sessioniPerMese.put(chiave, sessioniPerMese.getOrDefault(chiave, 0) + 1);
            if (sessioniPerMese.get(chiave) > 1) {
                throw new FrequencyException(
                        "Sessione " + (i + 1) + ": la frequenza mensile permette solo 1 sessione al mese ("
                                + data.getMonth() + " " + data.getYear() + ")");
            }
        }
    }

    /**
     * Verifica che il numero di sessioni per settimana non superi il massimo
     * consentito.
     * <p>
     * Utilizzata per frequenze settimanale (max 1), bisettimanale (max 2) e
     * trisettimanale (max 3).
     * </p>
     *
     * @param sessionDTOs     la lista dei DTO delle sessioni
     * @param maxPerSettimana il numero massimo di sessioni consentite per settimana
     * @throws FrequencyException se una settimana contiene più sessioni del
     *                            consentito
     */
    private void checkWeeklyFrequency(List<SessionDTO> sessionDTOs, int maxPerSettimana) throws FrequencyException {
        Map<String, Integer> sessioniPerSettimana = new HashMap<>();
        for (int i = 0; i < sessionDTOs.size(); i++) {
            LocalDate data = sessionDTOs.get(i).getDataSessione();
            int settimana = data.get(ISO.weekOfWeekBasedYear());
            String chiave = data.getYear() + "-W" + settimana;
            sessioniPerSettimana.put(chiave, sessioniPerSettimana.getOrDefault(chiave, 0) + 1);
            if (sessioniPerSettimana.get(chiave) > maxPerSettimana) {
                throw new FrequencyException("Sessione " + (i + 1) + ": la frequenza permette massimo "
                        + maxPerSettimana + " sessione/i a settimana (settimana " + settimana + ")");
            }
        }
    }

    /**
     * Restituisce l'ID dello chef correntemente autenticato.
     *
     * @return l'ID dello chef loggato
     * @throws NullPointerException se nessun utente è autenticato
     */
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

    /**
     * Recupera le sessioni associate a un corso specifico come lista di
     * {@link SessionDTO}.
     *
     * @param corsoId l'ID del corso di cui recuperare le sessioni
     * @return lista di {@link SessionDTO} con i dati delle sessioni; lista vuota in
     *         caso di errore
     */
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

    /**
     * Elimina una sessione dal sistema tramite il suo ID.
     *
     * @param sessionId l'ID della sessione da eliminare
     * @return {@code true} se l'eliminazione ha avuto successo, {@code false}
     *         altrimenti
     */
    public boolean deleteSession(int sessionId) {
        try {
            sessioneDAO.deleteSessione(sessionId);
            return true;
        } catch (DAOException e) {
            new ErrorDialog("Errore Eliminazione", "Impossibile eliminare la sessione: " + e.getMessage()).show();
            return false;
        }
    }

    /**
     * Aggiorna i dati di una sessione esistente.
     * <p>
     * Crea un oggetto {@link progetto.app.model.Sessione} temporaneo con i nuovi
     * dati del DTO e lo persiste tramite il DAO.
     * </p>
     *
     * @param sessionId  l'ID della sessione da aggiornare
     * @param sessionDTO il DTO contenente i nuovi valori (data, modalità, durata,
     *                   descrizione)
     * @return {@code true} se l'aggiornamento ha avuto successo, {@code false}
     *         altrimenti
     */
    public boolean updateSession(int sessionId, SessionDTO sessionDTO) {
        try {
            SessioneDAO dao = getSessioneDAO();

            Sessione s = new Sessione(sessionId, 0, 0,
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

    /**
     * Apre il dialog dei dettagli di un corso e, se sono state apportate modifiche,
     * ricarica la lista corsi nella vista.
     *
     * @param owner       la finestra proprietaria del dialog
     * @param corsoId     l'ID del corso di cui visualizzare i dettagli
     * @param corsoTitolo il titolo del corso, mostrato nell'intestazione del dialog
     */
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

    /**
     * Recupera le notifiche inviate dallo chef autenticato come lista di
     * {@link NotificationDTO}.
     * <p>
     * Risolve il titolo del corso destinatario della notifica a partire dalla mappa
     * dei corsi dello chef. Se la notifica non è associata a nessun corso
     * specifico,
     * il target viene impostato a {@code "Tutti i corsi"}.
     * </p>
     *
     * @return lista di {@link NotificationDTO} con titolo, contenuto e target;
     *         lista vuota in caso di errore
     */
    public List<NotificationDTO> getNotificationsData() {
        List<NotificationDTO> result = new ArrayList<>();

        try {
            List<Corso> corsi = corsoDAO.getCorsiByChef(userLogged.getId());
            Map<Integer, String> corsiMap = new HashMap<>();
            for (Corso c : corsi) {
                corsiMap.put(c.getId(), c.getTitolo());
            }

            List<Notifica> notifiche = notificaDAO.getNotificheByChef(userLogged.getId());
            for (Notifica n : notifiche) {
                String target = "Tutti i corsi";
                if (n.getIdCorso() != null && n.getIdCorso() != 0) {
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
            Platform.runLater(() -> new ErrorDialog("Errore", "Impossibile recuperare le notifiche.").show());
        }
        return result;
    }

    /**
     * Crea e persiste una nuova notifica per lo chef autenticato.
     * <p>
     * La notifica può essere indirizzata a tutti i corsi (se {@code corsoId} è
     * null)
     * o a un corso specifico.
     * </p>
     *
     * @param notificationDTO il DTO contenente titolo, contenuto e l'eventuale ID
     *                        del corso di destinazione
     * @return {@code true} se la notifica è stata creata con successo,
     *         {@code false} altrimenti
     */
    public boolean createNotification(NotificationDTO notificationDTO) {
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
            new ErrorDialog("Errore", "Impossibile creare la notifica.").show();
            return false;
        }
    }

    /**
     * Restituisce una lista semplificata dei corsi dello chef autenticato,
     * contenente solo id e titolo di ciascun corso.
     * <p>
     * Utilizzato tipicamente per popolare i selettori di corsi nei dialog
     * di creazione notifiche.
     * </p>
     *
     * @return lista di {@link CourseDTO} con id e titolo; lista vuota in caso di
     *         errore
     */
    public List<CourseDTO> getSimpleCoursesData() {
        List<CourseDTO> result = new ArrayList<>();
        try {
            List<Corso> corsi = corsoDAO.getCorsiByChef(userLogged.getId());

            for (Corso c : corsi) {
                CourseDTO dto = new CourseDTO();
                dto.setId(c.getId());
                dto.setTitolo(c.getTitolo());
                result.add(dto);
            }
        } catch (Exception e) {
            e.printStackTrace();
            new ErrorDialog("Errore", "Impossibile recuperare i corsi.").show();
        }
        return result;
    }

    /**
     * Apre il dialog di creazione notifica e, se confermato, persiste la notifica.
     *
     * @param owner la finestra proprietaria del dialog
     */
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

    /**
     * Carica una vista FXML senza aggiungerla alla cache interna.
     * <p>
     * Utile per caricare viste temporanee o dialog che non devono essere
     * riutilizzati tramite navigazione.
     * </p>
     *
     * @param fxmlPath il percorso del file FXML da caricare
     * @return il nodo radice {@link Parent} della vista caricata, o {@code null} in
     *         caso di errore
     */
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

    /**
     * Tenta di rimuovere i controller delle viste figlie dalla memoria.
     * Al prossimo login, verranno re-istanzati da zero, assicurandosi
     * che non rimangano dati del vecchio utente.
     */
    private void clearControllersData() {
        controllers.remove("dashboard");
        controllers.remove("courses");
        controllers.remove("recipes");
        controllers.remove("notifications");
        controllers.remove("report");
        views.remove("dashboard");
        views.remove("courses");
        views.remove("recipes");
        views.remove("notifications");
        views.remove("report");
    }

    /**
     * Effettua il logout dell'utente corrente.
     * <p>
     * Azzera l'utente autenticato, distrugge la cache delle view dinamiche
     * per il vecchio utente e reindirizza alla schermata di accesso.
     * </p>
     */
    public void logout() {
        this.userLogged = null;

        clearControllersData();

        LoginGUI loginGUI = (LoginGUI) getController("login");
        if (loginGUI != null) {
            loginGUI.clearLoginFields();
            loginGUI.clearRegisterFields();
            navigateToLogin();
        }
    }

    /**
     * Naviga alla schermata di login.
     */
    public void navigateToLogin() {
        navigateTo("login");
    }

    /**
     * Naviga alla dashboard principale.
     */
    public void navigateToDashboard() {
        navigateTo("dashboard");
    }

    /**
     * Naviga alla vista identificata dal nome logico, impostando la scena sullo
     * stage primario.
     *
     * @param name il nome logico della vista nella cache {@link #views}
     */
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

    /**
     * Naviga alla schermata segnaposto per funzionalità non ancora implementate.
     */
    public void navigateToNotImplemented() {
        navigateTo("notImplemented");
    }

    /**
     * Restituisce il nodo radice della vista identificata dal nome logico.
     *
     * @param name il nome logico della vista
     * @return il {@link Parent} della vista, o {@code null} se non presente in
     *         cache
     */
    public Parent getView(String name) {
        return views.get(name);
    }

    /**
     * Restituisce il controller JavaFX associato alla vista identificata dal nome
     * logico.
     *
     * @param name il nome logico della vista
     * @return il controller, o {@code null} se non presente in cache
     */
    public Object getController(String name) {
        return controllers.get(name);
    }

    /**
     * Imposta se lo stage primario può essere ridimensionato dall'utente.
     *
     * @param value {@code true} per rendere lo stage ridimensionabile,
     *              {@code false} per bloccarne le dimensioni
     */
    public void setPrimaryStageResizable(boolean value) {
        primaryStage.setResizable(value);
    }

    /**
     * Rende visibile lo stage primario dell'applicazione.
     */
    public void showPrimaryStage() {
        primaryStage.show();
    }

    /**
     * Tenta il login di un utente (allievo o chef) con le credenziali fornite.
     * <p>
     * Cerca prima tra gli allievi; se non trovato, cerca tra gli chef.
     * In caso di successo aggiorna la dashboard con lo username e naviga alla vista
     * appropriata.
     * </p>
     *
     * @param username lo username inserito dall'utente
     * @param password la password inserita dall'utente
     * @return {@code true} se il login ha avuto successo, {@code false} altrimenti
     */
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
                showErrorDialog(e1.getMessage(), "controlla i dati e riprova");
            }
        }
        return false;
    }

    /**
     * Registra un nuovo allievo nel sistema.
     * <p>
     * Verifica che non esista già uno chef con stesse credenziali, effettua l'hash
     * della password con BCrypt e persiste il nuovo
     * {@link progetto.app.model.Allievo}.
     * </p>
     *
     * @param username il nome utente scelto
     * @param password la password in chiaro (verrà hashata con BCrypt)
     * @param name     il nome dell'allievo
     * @param surname  il cognome dell'allievo
     * @param email    l'indirizzo email dell'allievo
     * @return {@code true} se la registrazione ha avuto successo, {@code false}
     *         altrimenti
     */
    public boolean registerAllievo(String username, String password, String name, String surname, String email) {
        if (searchChef(username, email)) {
            showWarningDialog("Account già esistente.", "Proseguire sulla schermata di accesso.");
            return false;
        }

        String pswHashed = BCrypt.hashpw(password, BCrypt.gensalt(10));
        Allievo allievo = new Allievo(username, pswHashed, email, name, surname);

        try {
            allievoDAO.addAllievo(allievo);
            navigateToNotImplemented();
            return true;
        } catch (DuplicateAllievoException e) {
            showWarningDialog(e.getMessage(), "Passa alla schermata login.");
            return false;
        } catch (DAOException e) {
            showErrorDialog("Errore durante la registrazione!", "Provare più tardi.");
            return false;
        }
    }

    /**
     * Effettua il login di un allievo verificando la password con BCrypt.
     *
     * @param username lo username dell'allievo
     * @param password la password in chiaro da verificare
     * @return {@code true} se il login ha avuto successo
     * @throws AllievoNotFoundException se l'allievo non viene trovato nel DB
     */
    private boolean loginAllievo(String username, String password) throws AllievoNotFoundException {
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
            navigateToNotImplemented();
            return true;
        } catch (DAOException e) {
            showErrorDialog("Errore in fase di login.", "Riprova.");
            return false;
        }
    }

    /**
     * Registra un nuovo chef nel sistema.
     * <p>
     * Verifica che non esista già un allievo con stesse credenziali, effettua
     * l'hash
     * della password con BCrypt e persiste il nuovo
     * {@link progetto.app.model.Chef}.
     * </p>
     *
     * @param username il nome utente scelto
     * @param password la password in chiaro (verrà hashata con BCrypt)
     * @param name     il nome dello chef
     * @param surname  il cognome dello chef
     * @param email    l'indirizzo email dello chef
     * @return {@code true} se la registrazione ha avuto successo, {@code false}
     *         altrimenti
     */
    public boolean registerChef(String username, String password, String name, String surname, String email) {
        if (searchAllievo(username, email)) {
            showWarningDialog("Account già esistente.", "Proseguire sulla schermata di accesso.");
            return false;
        }

        String pswHashed = BCrypt.hashpw(password, BCrypt.gensalt(10));
        Chef chef = new Chef(username, pswHashed, email, name, surname);

        try {
            chefDAO.addChef(chef);
            this.userLogged = chef;

            // Inizializza le GUI dinamicamente per questo utente
            loadAppViews();

            DashboardGUI controller = (DashboardGUI) controllers.get("dashboard");
            controller.updateUsername(username);

            navigateToDashboard();
            return true;
        } catch (DuplicateChefException e) {
            showWarningDialog(e.getMessage(), "Passa alla schermata login.");
            return false;
        } catch (DAOException e1) {
            showErrorDialog("Password errata", "Riprova");
            return false;
        }
    }

    /**
     * Carica in memoria i file FXML salvandone i controlli legati alla dashboard e
     * ai sotto-menu (corsi, ricette, notifiche, report).
     */
    private void loadAppViews() {
        loadView("dashboard", "/progetto/app/Dashboard.fxml");
        loadView("courses", "/progetto/app/view/CoursesView.fxml");
        loadView("recipes", "/progetto/app/view/RecipesView.fxml");
        loadView("notifications", "/progetto/app/view/NotificationsView.fxml");
        loadView("report", "/progetto/app/view/ReportView.fxml");
    }

    /**
     * Effettua il login di uno chef verificando la password con BCrypt.
     *
     * @param username lo username dello chef
     * @param password la password in chiaro da verificare
     * @return {@code true} se il login ha avuto successo
     * @throws ChefNotFoundException se lo chef non viene trovato nel DB
     */
    private boolean loginChef(String username, String password) throws ChefNotFoundException {
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

            // Inizializza le GUI dinamicamente per questo utente
            loadAppViews();

            DashboardGUI controller = (DashboardGUI) controllers.get("dashboard");
            if (controller != null) {
                controller.updateUsername(username);
            }

            navigateToDashboard();
            return true;
        } catch (DAOException e) {
            showErrorDialog(e.getMessage(), "Riprova");
            return false;
        }
    }

    /**
     * Verifica se esiste un allievo con lo username o l'email forniti.
     *
     * @param username lo username da cercare
     * @param email    l'email da cercare come fallback
     * @return {@code true} se esiste almeno un allievo con username o email
     *         corrispondente
     */
    public boolean searchAllievo(String username, String email) {
        try {
            if (searchAllievoByUsername(username)) {
                return true;
            }
        } catch (AllievoNotFoundException e) {
            try {
                if (searchAllievoByEmail(email)) {
                    return true;
                }
            } catch (AllievoNotFoundException e1) {
                return false;
            }
        }
        return false;
    }

    /**
     * Verifica se esiste uno chef con lo username o l'email forniti.
     *
     * @param username lo username da cercare
     * @param email    l'email da cercare come fallback
     * @return {@code true} se esiste almeno uno chef con username o email
     *         corrispondente
     */
    public boolean searchChef(String username, String email) {
        try {
            if (searchChefByUsername(username)) {
                return true;
            }
        } catch (ChefNotFoundException e) {
            try {
                if (searchChefByEmail(email)) {
                    return true;
                }
            } catch (ChefNotFoundException e1) {
                return false;
            }
        }
        return false;
    }

    /**
     * Verifica se una stringa email rispetta il formato valido tramite regex.
     *
     * @param email la stringa da validare
     * @return {@code true} se l'email è nel formato corretto, {@code false}
     *         altrimenti
     */
    public static boolean checkEmail(String email) {
        if (email == null) {
            return false;
        }
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    /**
     * Confronta due stringhe di password per verificarne l'uguaglianza.
     *
     * @param password        la password originale
     * @param confirmPassword la password di conferma
     * @return {@code true} se le due password sono identiche
     */
    public static boolean doPasswordsMatch(String password, String confirmPassword) {
        return password.equals(confirmPassword);
    }

    /**
     * Verifica se esiste un allievo con lo username specificato.
     *
     * @param username lo username da cercare
     * @return {@code true} se l'allievo esiste
     * @throws AllievoNotFoundException se nessun allievo corrisponde allo username
     */
    public boolean searchAllievoByUsername(String username) throws AllievoNotFoundException {
        return allievoDAO.getAllievoByUsername(username) != null;
    }

    /**
     * Verifica se esiste un allievo con l'email specificata.
     *
     * @param email l'email da cercare
     * @return {@code true} se l'allievo esiste
     * @throws AllievoNotFoundException se nessun allievo corrisponde all'email
     */
    public boolean searchAllievoByEmail(String email) throws AllievoNotFoundException {
        return allievoDAO.getAllievoByEmail(email) != null;
    }

    /**
     * Verifica se esiste uno chef con lo username specificato.
     *
     * @param username lo username da cercare
     * @return {@code true} se lo chef esiste
     * @throws ChefNotFoundException se nessuno chef corrisponde allo username
     */
    public boolean searchChefByUsername(String username) throws ChefNotFoundException {
        return chefDAO.getChefByUsername(username) != null;
    }

    /**
     * Verifica se esiste uno chef con l'email specificata.
     *
     * @param email l'email da cercare
     * @return {@code true} se lo chef esiste
     * @throws ChefNotFoundException se nessuno chef corrisponde all'email
     */
    public boolean searchChefByEmail(String email) throws ChefNotFoundException {
        return chefDAO.getChefByEmail(email) != null;
    }

    /**
     * Mostra un dialog di errore e anima lo stage con un effetto di scuotimento.
     *
     * @param title   il titolo del dialog di errore
     * @param message il messaggio descrittivo dell'errore
     */
    public void showErrorDialog(String title, String message) {
        ErrorDialog errorDialog = new ErrorDialog(title, message);
        shakeWindow();
        errorDialog.show();
    }

    /**
     * Mostra un dialog di avviso e anima lo stage con un effetto di scuotimento.
     *
     * @param title   il titolo del dialog di avviso
     * @param message il messaggio descrittivo dell'avviso
     */
    public void showWarningDialog(String title, String message) {
        WarningDialog warningDialog = new WarningDialog(title, message);
        shakeWindow();
        warningDialog.show();
    }

    /**
     * Mostra il dialog dei Termini di Servizio.
     */
    public void showTermsOfService() {
        TermsOfServiceDialog tosDialog = new TermsOfServiceDialog();
        tosDialog.showDialog();
    }

    /**
     * Imposta l'icona dell'applicazione ({@link #APP_ICON}) per lo {@link Stage}
     * indicato.
     *
     * @param stage lo stage a cui aggiungere l'icona
     */
    public static void setAppIcon(Stage stage) {
        stage.getIcons().add(APP_ICON);
    }

    /**
     * Anima lo stage primario con un effetto di scuotimento orizzontale.
     * <p>
     * Viene tipicamente invocato prima di mostrare un dialog di errore o avviso
     * per attirare l'attenzione dell'utente.
     * </p>
     */
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

    /**
     * Restituisce lo username dell'utente correntemente autenticato.
     *
     * @return lo username dell'utente loggato, o {@code null} se nessun utente è
     *         autenticato
     */
    public String getLoggedUsername() {
        return userLogged != null ? userLogged.getUsername() : null;
    }

}
