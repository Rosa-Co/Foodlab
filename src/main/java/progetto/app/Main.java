package progetto.app;

import atlantafx.base.theme.PrimerLight;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import progetto.app.controller.AppController;

import java.util.Objects;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws Exception {

        Application.setUserAgentStylesheet(new PrimerLight().getUserAgentStylesheet());

        AppController appController = AppController.getInstance();
        AppController.setAppIcon(stage);
        appController.setPrimaryStage(stage);

        // Imposta l'icona dell'applicazione
        Image icon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/progetto/app/logo.png")));
        stage.getIcons().add(icon);

        appController.loadView("login", "/progetto/app/login.fxml");
        appController.loadView("dashboard", "/progetto/app/Dashboard.fxml");
        appController.loadView("home", "/progetto/app/view/HomeView.fxml");
        appController.loadView("courses", "/progetto/app/view/CoursesView.fxml");
        appController.loadView("recipes", "/progetto/app/view/RecipesView.fxml");
        appController.loadView("notifications", "/progetto/app/view/NotificationsView.fxml");
        appController.loadView("report", "/progetto/app/view/ReportView.fxml");

        Stage splashStage = new Stage();
        AppController.setAppIcon(splashStage);
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