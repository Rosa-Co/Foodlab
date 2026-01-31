package progetto.app.view;

import javafx.animation.Animation;
import javafx.animation.ScaleTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import progetto.app.controller.AppController;
import progetto.app.controller.TypeWritingController;

import java.net.URL;
import java.util.ResourceBundle;

public class SplashGUI implements Initializable {
    AppController appController = AppController.getInstance();
    @FXML
    private ImageView logo;
    @FXML
    private Label typeWriterText;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        TypeWritingController animation = new TypeWritingController(typeWriterText, "Caricamento...", 100);
        // Removed manual ugly styling. Handled in FXML.

        // Ensure text is visible
        typeWriterText.setOpacity(1);

        // === Pulse Animation ===
        ScaleTransition pulse = new ScaleTransition(Duration.seconds(0.8), logo);
        // Dal 100% all’110% di scala
        pulse.setFromX(0.7);
        pulse.setFromY(0.7);
        pulse.setToX(1.0);
        pulse.setToY(1.0);
        pulse.setAutoReverse(true); // torna alla scala originale
        pulse.setCycleCount(Animation.INDEFINITE);
        pulse.play();
        animation.play();
        // ========================

        new Thread(() -> {
            try {
                Thread.sleep(4000);
            } catch (Exception e) {
                e.printStackTrace();
            }
            Platform.runLater(() -> {
                try {
                    Stage currentStage = (Stage) logo.getScene().getWindow();

                    appController.navigateToLogin();
                    appController.setPrimaryStageResizable(true);
                    currentStage.hide(); // Chiude la splash screen
                    appController.showPrimaryStage(); // Mostra la finestra principale
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }).start();
    }
}
