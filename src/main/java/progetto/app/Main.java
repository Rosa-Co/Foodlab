package progetto.app;

import atlantafx.base.theme.PrimerLight;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import progetto.app.controller.FXMLLoaderManager;
import progetto.app.database.DatabaseConnection;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        /*Parent root = FXMLLoader.load(getClass().getResource("Splash.fxml"));
        Scene scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT); //Setta la scena trasparente
        stage.initStyle(StageStyle.TRANSPARENT); // Rimuove i pulsanti dalla splash screen e rende trasparente lo stage
        stage.setScene(scene);
        stage.setResizable(true);
        stage.setTitle("FoodLab");
        stage.show();*/
        // Applicazione tema Atlantafx
        Application.setUserAgentStylesheet(new PrimerLight().getUserAgentStylesheet());
        //preparazione per la splashscreen
        stage.initStyle(StageStyle.TRANSPARENT);
        Scene newScene = FXMLLoaderManager.setScene(stage,"Splash.fxml", "FoodLab");
        newScene.setFill(Color.TRANSPARENT); //Setta la scena trasparente
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}