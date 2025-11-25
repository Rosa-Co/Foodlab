package progetto.app.view;

import atlantafx.base.controls.Notification;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;
import progetto.app.controller.AppController;
import progetto.app.dialog.ErrorDialog;
import progetto.app.dialog.InfoDialog;
import progetto.app.dialog.WarningDialog;

import static progetto.app.controller.AppController.checkEmail;

public class LoginGUI {

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

    @FXML
    private void initialize() {
        setupFeatures();
    }

    @FXML
    private void showTerms() {
        mainController.showTermsOfService();
    }

    private void setupFeatures() {
        // Header section
        VBox headerBox = new VBox(8);
        headerBox.setAlignment(Pos.CENTER);
        headerBox.setPadding(new Insets(40, 30, 30, 30));

        Label mainTitle = new Label("Scopri FoodHub");
        mainTitle.setStyle("-fx-font-size: 32px; -fx-font-weight: bold;");

        Label subtitle = new Label("La tua piattaforma per condividere passione culinaria");
        subtitle.setStyle("-fx-font-size: 14px; -fx-opacity: 0.7; -fx-text-alignment: center;");
        subtitle.setWrapText(true);
        // subtitle.setMaxWidth(400);

        headerBox.getChildren().addAll(mainTitle, subtitle);

        // Features cards - usando gli enum di FontAwesome5
        Object[][] features = {
                { FontAwesomeSolid.UTENSILS, "Ricette Creative",
                        "Crea e condividi piatti unici e originali con la nostra community di food lover.", "#FF6B6B" },
                { FontAwesomeSolid.COFFEE, "Ingredienti Freschi",
                        "Scopri ingredienti freschi e stagionali consigliati dai migliori chef.", "#4ECDC4" },
                { FontAwesomeSolid.USERS, "Community Attiva",
                        "Connettiti con altri appassionati, commenta e valuta le ricette.", "#FFE66D" }
        };

        VBox cardsContainer = new VBox(20);
        cardsContainer.setAlignment(Pos.TOP_CENTER);
        cardsContainer.setPadding(new Insets(0, 30, 30, 30));
        cardsContainer.setMaxWidth(450);

        for (Object[] f : features) {
            HBox card = createFeatureCard(f[0], (String) f[1], (String) f[2], (String) f[3]);
            cardsContainer.getChildren().add(card);
        }

        // Add spacing
        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        featuresPane.getChildren().addAll(headerBox, cardsContainer, spacer);
    }

    private HBox createFeatureCard(Object iconEnum, String title, String description, String accentColor) {
        HBox card = new HBox(15);
        card.setAlignment(Pos.CENTER_LEFT);
        card.setPadding(new Insets(20));
        card.setStyle(
                "-fx-background-color: -color-bg-default; " +
                        "-fx-background-radius: 12px; " +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 15, 0, 0, 3); " +
                        "-fx-cursor: hand;");

        // Hover effect
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

        // Icon container
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
        icon.setIconColor(javafx.scene.paint.Color.valueOf(accentColor));
        iconContainer.getChildren().add(icon);

        // Text content
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

    @FXML
    private void handleLogin() {
        if (!usernameField.getText().isBlank() && !passwordField.getText().isBlank()) {
            if (mainController.login(usernameField.getText().strip(), passwordField.getText())) {
                mainController.navigateToDashboard();
            }
        } else {
            WarningDialog warningDialog = new WarningDialog("Attenzione!", "Riempire tutti i campi.");
            warningDialog.show();
        }
    }

    @FXML
    private void handleRegister() {
        if (!checkEmail(registerEmailField.getText().strip())) {
            ErrorDialog err = new ErrorDialog("Registrazione Fallita", "Formato email non valido");
            err.show();
            return;
        }
        if (!areAllFieldsFilled()) {
            WarningDialog warn = new WarningDialog("Attenzione!", "Riempire tutti i campi.");
            warn.show();
            return;
        }

        if (!doPasswordsMatch()) {
            WarningDialog warn = new WarningDialog("Attenzione!", "Le password non coincidono.");
            warn.show();
            return;
        }

        if (!areTermsAccepted()) {
            WarningDialog warn = new WarningDialog("Attenzione!", "Per proseguire, accettare termini e condizioni.");
            warn.show();
            return;
        }

        boolean registrationSuccessful = performRegistration();

        if (registrationSuccessful) {
            mainController.navigateToDashboard();
        }
    }

    private boolean areAllFieldsFilled() {
        return !registerNameField.getText().trim().isEmpty() &&
                !registerSurnameField.getText().trim().isEmpty() &&
                !registerUsernameField.getText().trim().isEmpty() &&
                !registerEmailField.getText().trim().isEmpty() &&
                !registerPasswordField.getText().trim().isEmpty() &&
                !registerConfirmPasswordField.getText().trim().isEmpty();
    }

    private boolean doPasswordsMatch() {
        return registerPasswordField.getText().equals(registerConfirmPasswordField.getText());
    }

    private boolean areTermsAccepted() {
        return acceptTermsCheck.isSelected();
    }

    private boolean performRegistration() {
        if (userTypeRadio.isSelected()) {
            return mainController.registerUser(
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

    @FXML
    private void showRegisterForm() {
        loginPane.setVisible(false);
        loginPane.setManaged(false); // così non occupa spazio nel layout.
        registerPane.setVisible(true);
        registerPane.setManaged(true);
    }

    @FXML
    private void showLoginForm() {
        registerPane.setVisible(false);
        registerPane.setManaged(false);
        loginPane.setVisible(true);
        loginPane.setManaged(true);
    }

}