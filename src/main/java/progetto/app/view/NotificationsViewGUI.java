package progetto.app.view;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import progetto.app.controller.AppController;
import progetto.app.dto.NotificationDTO;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Controller della schermata che mostra le notifiche inviate dallo chef.
 * <p>
 * Ogni notifica è mostrata come una card con titolo, destinatario e testo.
 * Il pulsante in alto apre il dialog per inviare una nuova notifica.
 * </p>
 */
public class NotificationsViewGUI implements Initializable {

    /** Contenitore verticale in cui vengono inserite le card delle notifiche. */
    @FXML
    private VBox notificationsContainer;
    /** Pulsante per aprire il dialog di invio di una nuova notifica. */
    @FXML
    private Button newNotificationButton;

    private final AppController appController = AppController.getInstance();

    /** Registra il listener sul pulsante di nuova notifica. */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (newNotificationButton != null) {
            newNotificationButton.setOnAction(e -> handleNewNotification());
        }
    }

    /**
     * Carica le notifiche su un thread in background e aggiorna la UI nel thread
     * JavaFX.
     */
    public void loadNotifications() {
        notificationsContainer.getChildren().clear();
        Label loadingLabel = new Label("Caricamento in corso...");
        loadingLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #95a5a6;");
        notificationsContainer.getChildren().add(loadingLabel);

        new Thread(() -> {
            List<NotificationDTO> notifications = appController.getNotificationsData();
            Platform.runLater(() -> populateNotifications(notifications));
        }).start();
    }

    /**
     * Popola il contenitore con le card delle notifiche, o mostra un placeholder
     * se non ce ne sono.
     *
     * @param notifications lista di {@link NotificationDTO} da mostrare
     */
    private void populateNotifications(List<NotificationDTO> notifications) {
        notificationsContainer.getChildren().clear();

        if (notifications.isEmpty()) {
            Label placeholder = new Label("Nessuna notifica inviata.");
            placeholder.setStyle("-fx-font-size: 14px;");
            notificationsContainer.getChildren().add(placeholder);
            return;
        }

        for (NotificationDTO notifica : notifications) {
            VBox card = createNotificationCard(notifica);
            notificationsContainer.getChildren().add(card);
        }
    }

    /**
     * Crea la card grafica per una singola notifica.
     *
     * @param notifica il {@link NotificationDTO} da rappresentare
     * @return il {@link VBox} della card
     */
    private VBox createNotificationCard(NotificationDTO notifica) {
        VBox card = new VBox(5);
        card.setStyle(
                "-fx-background-color: -color-bg-default; -fx-padding: 15; -fx-background-radius: 8; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 5, 0, 0, 2);");

        String titolo = notifica.getTitolo();
        String contenuto = notifica.getContenuto();
        String target = notifica.getTarget();

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

    /** Apre il dialog per creare una nuova notifica e poi ricarica l'elenco. */
    private void handleNewNotification() {
        appController.showCreateNotificationDialog(notificationsContainer.getScene().getWindow());
        loadNotifications(); // Ricarica dopo una potenziale aggiunta
    }
}
