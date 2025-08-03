package progetto.app.view;

import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

public class Login {

    @FXML
    private ImageView imageView;

    @FXML
    private AnchorPane anchorPane; // o altro layout contenitore

    public void initialize() {
        imageView.fitWidthProperty().bind(anchorPane.widthProperty());
        imageView.fitHeightProperty().bind(anchorPane.heightProperty());
    }

}
