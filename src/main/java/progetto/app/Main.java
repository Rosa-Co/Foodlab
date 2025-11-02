package progetto.app;

import atlantafx.base.theme.PrimerLight;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import progetto.app.controller.AppController;
import progetto.app.controller.FXMLLoaderManager;
import progetto.app.database.DatabaseConnection;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        AppController appController = AppController.getInstance();
        appController.setPrimaryStage(stage);
        appController.loadView("dashboard", "/progetto/app/dashboard.fxml");
        appController.loadView("login", "/progetto/app/login.fxml");

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

        Stage splashStage = new Stage();
        splashStage.setResizable(false);
        splashStage.initStyle(StageStyle.TRANSPARENT);

        Scene splashScene = new Scene(appController.loadView("Splash.fxml"));
        splashScene.setFill(Color.TRANSPARENT); //Setta la scena trasparente

        splashStage.setScene(splashScene);
        splashStage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}