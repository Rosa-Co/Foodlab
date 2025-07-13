package progetto.app.controller;

import javafx.animation.Animation;
import javafx.animation.ScaleTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.net.URL;
import java.util.ResourceBundle;

public class SplashController implements Initializable{
    @FXML
    private ImageView logo;
    @FXML
    private Label typeWriterText;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        TypeWritingController animation = new TypeWritingController(typeWriterText, "Caricamento...",100);
        typeWriterText.setTextFill(Color.DARKORANGE);
        typeWriterText.setPadding(new Insets(5));  // Spazio interno
        typeWriterText.setStyle(
                "-fx-background-color:white;"+
                        "-fx-border-color: black;" +       // Colore del bordo
                        "-fx-border-width: 1px;" +         // Spessore del bordo
                        "-fx-border-radius: 3px;" +        // Angoli arrotondati
                        "-fx-background-radius: 3px;"      // Arrotonda anche lo sfondo
        );
        typeWriterText.setOpacity(1);

        // === Pulse Animation ===
        ScaleTransition pulse = new ScaleTransition(Duration.seconds(0.8), logo);
        // Dal 100% all’110% di scala
        pulse.setFromX(0.7);
        pulse.setFromY(0.7);
        pulse.setToX(1.0);
        pulse.setToY(1.0);
        pulse.setAutoReverse(true);            // torna alla scala originale
        pulse.setCycleCount(Animation.INDEFINITE);
        pulse.play();
        animation.play();
        // ========================



        new Thread(() -> {
            try{
                Thread.sleep(4000);
            }catch (Exception e){
                e.printStackTrace();
            }
            Platform.runLater(()->{
                try{
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/progetto/app/hello-view.fxml")); //"parser" o "interprete"
                    Parent main = loader.load(); // legge il file FXML specificato nell'URL e crea l'albero degli oggetti JavaFX descritti nel file
                    Scene mainScene = new Scene(main);

                    Stage mainStage = new Stage(); // Crea un nuovo Stage per la finestra principale
                    mainStage.setScene(mainScene);
                    mainStage.setResizable(true); // Abilita il resize
                    mainStage.setFullScreen(true); // Imposta a fullscreen se necessario

                    Stage currentStage = (Stage) logo.getScene().getWindow();
                    currentStage.hide(); // Chiude la splash screen
                    mainStage.show();  // Mostra la finestra principale
                }catch (Exception e){
                    e.printStackTrace();
                }
            });
        }).start();
    }
}
