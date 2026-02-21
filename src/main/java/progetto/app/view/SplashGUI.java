package progetto.app.view;

import javafx.animation.Animation;
import javafx.animation.ScaleTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.Duration;
import progetto.app.controller.AppController;
import progetto.app.controller.TypeWritingController;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller della schermata di avvio (splash screen).
 * <p>
 * Anima il logo con una transizione di scala ciclica e simula un testo
 * in stile "typewriter". Dopo 4 secondi reindirizza automaticamente
 * alla schermata di login e chiude la splash window.
 * </p>
 */
public class SplashGUI implements Initializable {
    /** Controller principale dell'applicazione. */
    AppController appController = AppController.getInstance();
    /** Logo mostrato nella splash screen. */
    @FXML
    private ImageView logo;
    /** Etichetta su cui viene animato il testo di caricamento. */
    @FXML
    private Label typeWriterText;

    /**
     * Inizializza la schermata di avvio: avvia l'animazione del logo e del testo,
     * poi naviga verso il login dopo 4 secondi su un thread separato.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        TypeWritingController animation = new TypeWritingController(typeWriterText, "Caricamento...", 100);
        typeWriterText.setOpacity(1);

        ScaleTransition pulse = new ScaleTransition(Duration.seconds(0.8), logo);
        pulse.setFromX(0.7);
        pulse.setFromY(0.7);
        pulse.setToX(1.0);
        pulse.setToY(1.0);
        pulse.setAutoReverse(true); // torna alla scala originale
        pulse.setCycleCount(Animation.INDEFINITE);
        pulse.play();
        animation.play();

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
