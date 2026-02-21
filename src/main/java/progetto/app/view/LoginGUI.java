package progetto.app.view;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;
import org.kordamp.ikonli.javafx.FontIcon;
import progetto.app.controller.AppController;

/**
 * Controller della schermata di login e registrazione.
 * <p>
 * Gestisce due pannelli sovrapposti: {@code loginPane} (login) e
 * {@code registerPane} (registrazione). Solo uno è visibile per volta;
 * i metodi {@link #showRegisterForm()} e {@link #showLoginForm()} effettuano
 * lo scambio. Il pannello sinistro ({@code featuresPane}) mostra card
 * promozionali con icone FontAwesome.
 * </p>
 */
public class LoginGUI {

    /** Controller principale dell'applicazione. */
    AppController mainController = AppController.getInstance();

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button loginButton;

    @FXML
    private VBox featuresPane;

    @FXML
    private VBox loginPane;
    @FXML
    private VBox registerPane;
    @FXML
    private TextField registerNameField;
    @FXML
    private TextField registerSurnameField;
    @FXML
    private TextField registerUsernameField;
    @FXML
    private TextField registerEmailField;
    @FXML
    private PasswordField registerPasswordField;
    @FXML
    private PasswordField registerConfirmPasswordField;
    @FXML
    private CheckBox acceptTermsCheck;
    @FXML
    private RadioButton userTypeRadio;
    @FXML
    private RadioButton chefTypeRadio;
    @FXML
    private Button registerButton;

    /** Inizializza la view costruendo le card. */
    @FXML
    private void initialize() {
        setupFeatures();
    }

    /** Apre il dialogo dei termini di servizio. */
    @FXML
    private void showTerms() {
        mainController.showTermsOfService();
    }

    /**
     * Costruisce e popola programmaticamente il pannello delle feature con card
     * animate.
     */
    private void setupFeatures() {
        VBox headerBox = new VBox(8);
        headerBox.setAlignment(Pos.CENTER);
        headerBox.setPadding(new Insets(40, 30, 30, 30));

        Label mainTitle = new Label("Scopri FoodLab");
        mainTitle.setStyle("-fx-font-size: 32px; -fx-font-weight: bold;");

        Label subtitle = new Label("La tua piattaforma per condividere passione culinaria");
        subtitle.setStyle("-fx-font-size: 14px; -fx-opacity: 0.7; -fx-text-alignment: center;");
        subtitle.setWrapText(true);

        headerBox.getChildren().addAll(mainTitle, subtitle);

        // Features cards - usando gli enum di FontAwesome5
        Object[][] features = {
                { FontAwesomeSolid.UTENSILS, "Ricette Creative",
                        "Crea e condividi piatti unici e originali con la nostra community di food lover.", "#FF6B6B" },
                { FontAwesomeSolid.COFFEE, "Ingredienti Freschi",
                        "Scopri ingredienti freschi e stagionali consigliati dai migliori chef.", "#4ECDC4" },
                { FontAwesomeSolid.USERS, "Community Attiva",
                        "Connettiti con altri appassionati, la cucina è magica", "#FFE66D" }
        };

        VBox cardsContainer = new VBox(20);
        cardsContainer.setAlignment(Pos.TOP_CENTER);
        cardsContainer.setPadding(new Insets(0, 30, 30, 30));
        cardsContainer.setMaxWidth(450);

        for (Object[] f : features) {
            HBox card = createFeatureCard(f[0], (String) f[1], (String) f[2], (String) f[3]);
            cardsContainer.getChildren().add(card);
        }

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        featuresPane.getChildren().addAll(headerBox, cardsContainer, spacer);
    }

    /**
     * Crea una singola card feature con icona, titolo e descrizione.
     *
     * @param iconEnum    enum FontAwesome dell'icona
     * @param title       titolo della card
     * @param description testo descrittivo
     * @param accentColor colore esadecimale dell'accento
     * @return {@link HBox} pronto da aggiungere al layout
     */
    private HBox createFeatureCard(Object iconEnum, String title, String description, String accentColor) {
        HBox card = new HBox(15);
        card.setAlignment(Pos.CENTER_LEFT);
        card.setPadding(new Insets(20));
        card.setStyle(
                "-fx-background-color: -color-bg-default; " +
                        "-fx-background-radius: 12px; " +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 15, 0, 0, 3); " +
                        "-fx-cursor: hand;");

        // effetto Hover
        card.setOnMouseEntered(e -> card.setStyle(
                "-fx-background-color: -color-bg-default; " +
                        "-fx-background-radius: 12px; " +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 20, 0, 0, 5); " +
                        "-fx-cursor: hand; " +
                        "-fx-translate-y: -2px;"));

        card.setOnMouseExited(e -> card.setStyle(
                "-fx-background-color: -color-bg-default; " +
                        "-fx-background-radius: 12px; " +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 15, 0, 0, 3); " +
                        "-fx-cursor: hand;"));

        VBox iconContainer = new VBox();
        iconContainer.setAlignment(Pos.CENTER);
        iconContainer.setPrefSize(50, 50);
        iconContainer.setMinWidth(50);
        iconContainer.setMaxWidth(50);

        iconContainer.setStyle(
                "-fx-background-color: " + accentColor + "20; " +
                        "-fx-background-radius: 10px;");

        FontIcon icon = new FontIcon((FontAwesomeSolid) iconEnum);
        icon.setIconSize(28);
        icon.setIconColor(Color.valueOf(accentColor));
        iconContainer.getChildren().add(icon);

        VBox textBox = new VBox(5);
        textBox.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(textBox, Priority.ALWAYS);

        Label titleLabel = new Label(title);
        titleLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        Label descLabel = new Label(description);
        descLabel.setWrapText(true);
        descLabel.setStyle("-fx-font-size: 13px; -fx-opacity: 0.75;");

        textBox.getChildren().addAll(titleLabel, descLabel);

        card.getChildren().addAll(iconContainer, textBox);
        return card;
    }

    /**
     * Gestisce il click sul pulsante di login; mostra un avviso se i campi sono
     * vuoti.
     */
    @FXML
    private void handleLogin() {
        if (!usernameField.getText().isBlank() && !passwordField.getText().isBlank()) {
            mainController.login(usernameField.getText().strip(), passwordField.getText());
        } else {
            mainController.showWarningDialog("Attenzione!", "Riempire tutti i campi.");
        }
    }

    /**
     * Gestisce il click sul pulsante di registrazione, validando email, campi,
     * password e termini.
     */
    @FXML
    private void handleRegister() {
        if (!AppController.checkEmail(registerEmailField.getText().strip())) {
            mainController.showErrorDialog("Registrazione Fallita", "Formato email non valido");
            return;
        }
        if (!areAllFieldsFilled()) {
            mainController.showWarningDialog("Attenzione!", "Riempire tutti i campi.");
            return;
        }

        if (!AppController.doPasswordsMatch(registerPasswordField.getText(), registerConfirmPasswordField.getText())) {
            mainController.showWarningDialog("Attenzione!", "Le password non coincidono.");
            return;
        }

        if (!areTermsAccepted()) {
            mainController.showWarningDialog("Attenzione!", "Per proseguire, accettare termini e condizioni.");
            return;
        }

        boolean success = performRegistration();

    }

    /**
     * @return {@code true} se tutti i campi del form di registrazione sono
     *         compilati
     */
    private boolean areAllFieldsFilled() {
        return !registerNameField.getText().trim().isEmpty() &&
                !registerSurnameField.getText().trim().isEmpty() &&
                !registerUsernameField.getText().trim().isEmpty() &&
                !registerEmailField.getText().trim().isEmpty() &&
                !registerPasswordField.getText().trim().isEmpty() &&
                !registerConfirmPasswordField.getText().trim().isEmpty();
    }

    /**
     * @return {@code true} se la checkbox di accettazione dei termini è selezionata
     */
    private boolean areTermsAccepted() {
        return acceptTermsCheck.isSelected();
    }

    /**
     * Esegue la registrazione come allievo o come chef a seconda del radio button
     * selezionato.
     *
     * @return {@code true} se la registrazione ha avuto successo
     */
    private boolean performRegistration() {
        if (userTypeRadio.isSelected()) {
            return mainController.registerAllievo(
                    registerUsernameField.getText(),
                    registerPasswordField.getText(),
                    registerNameField.getText(),
                    registerSurnameField.getText(),
                    registerEmailField.getText());
        } else {
            return mainController.registerChef(
                    registerUsernameField.getText(),
                    registerPasswordField.getText(),
                    registerNameField.getText(),
                    registerSurnameField.getText(),
                    registerEmailField.getText());
        }
    }

    /** Nasconde il form di login e mostra il form di registrazione. */
    @FXML
    private void showRegisterForm() {
        clearRegisterFields();
        loginPane.setVisible(false);
        loginPane.setManaged(false);// così non occupa spazio nel layout.
        registerPane.setVisible(true);
        registerPane.setManaged(true);
    }

    /** Nasconde il form di registrazione e mostra il form di login. */
    @FXML
    private void showLoginForm() {
        clearLoginFields();
        registerPane.setVisible(false);
        registerPane.setManaged(false);
        loginPane.setVisible(true);
        loginPane.setManaged(true);
    }

    /**
     * Cancella tutti i campi del form di registrazione e deseleziona la checkbox
     * dei termini.
     */
    public void clearRegisterFields() {
        registerNameField.clear();
        registerSurnameField.clear();
        registerUsernameField.clear();
        registerEmailField.clear();
        registerPasswordField.clear();
        registerConfirmPasswordField.clear();
        acceptTermsCheck.setSelected(false);
    }

    /** Cancella i campi di username e password del form di login. */
    public void clearLoginFields() {
        usernameField.clear();
        passwordField.clear();
    }
}