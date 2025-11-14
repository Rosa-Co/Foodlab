package progetto.app;

import atlantafx.base.theme.PrimerLight;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import progetto.app.controller.AppController;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        AppController appController = AppController.getInstance();
        appController.setPrimaryStage(stage);

        appController.loadView("login", "/progetto/app/login.fxml");
        appController.loadView("dashboard", "/progetto/app/Dashboard.fxml");
        appController.loadView("home", "/progetto/app/view/HomeView.fxml");
        appController.loadView("courses", "/progetto/app/view/CoursesView.fxml");
        appController.loadView("report", "/progetto/app/view/ReportView.fxml");


        Application.setUserAgentStylesheet(new PrimerLight().getUserAgentStylesheet());

        Stage splashStage = new Stage();
        splashStage.setResizable(false);
        splashStage.initStyle(StageStyle.TRANSPARENT);

        Scene splashScene = new Scene(appController.loadView("Splash.fxml"));
        splashScene.setFill(Color.TRANSPARENT);

        splashStage.setScene(splashScene);
        splashStage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}