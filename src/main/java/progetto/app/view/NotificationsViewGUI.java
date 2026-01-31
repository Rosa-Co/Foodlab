package progetto.app.view;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import progetto.app.controller.AppController;

import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class NotificationsViewGUI implements Initializable {

    @FXML
    private VBox notificationsContainer;
    @FXML
    private Button newNotificationButton;

    private final AppController appController = AppController.getInstance();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (newNotificationButton != null) {
            newNotificationButton.setOnAction(e -> handleNewNotification());
        }
    }

    public void loadNotifications() {
        notificationsContainer.getChildren().clear();
        Label loadingLabel = new Label("Caricamento in corso...");
        loadingLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #95a5a6;");
        notificationsContainer.getChildren().add(loadingLabel);

        new Thread(() -> {
            List<Map<String, Object>> notifications = appController.getNotificationsData();
            Platform.runLater(() -> populateNotifications(notifications));
        }).start();
    }

    private void populateNotifications(List<Map<String, Object>> notifications) {
        notificationsContainer.getChildren().clear();

        if (notifications.isEmpty()) {
            Label placeholder = new Label("Nessuna notifica inviata.");
            placeholder.setStyle("-fx-text-fill: -color-text-subtle; -fx-font-size: 14px;");
            notificationsContainer.getChildren().add(placeholder);
            return;
        }

        for (Map<String, Object> notifica : notifications) {
            VBox card = createNotificationCard(notifica);
            notificationsContainer.getChildren().add(card);
        }
    }

    private VBox createNotificationCard(Map<String, Object> notifica) {
        VBox card = new VBox(5);
        card.setStyle(
                "-fx-background-color: -color-bg-default; -fx-padding: 15; -fx-background-radius: 8; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 5, 0, 0, 2);");

        String titolo = (String) notifica.get("titolo");
        String contenuto = (String) notifica.get("contenuto");
        String target = (String) notifica.get("target");

        Label titleLabel = new Label(titolo);
        titleLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 16px;");

        Label targetLabel = new Label("A: " + target);
        targetLabel.setStyle("-fx-text-fill: -color-accent-fg; -fx-font-size: 12px;");

        Label contentLabel = new Label(contenuto);
        contentLabel.setWrapText(true);
        contentLabel.setStyle("-fx-font-size: 14px;");

        card.getChildren().addAll(titleLabel, targetLabel, contentLabel);

        return card;
    }

    private void handleNewNotification() {
        appController.showCreateNotificationDialog(notificationsContainer.getScene().getWindow());
        loadNotifications(); // Reload after potential add
    }
}
