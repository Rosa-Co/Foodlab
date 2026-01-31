package progetto.app.view;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import progetto.app.controller.AppController;

import java.util.List;
import progetto.app.dto.RecipeDTO;

public class RecipesViewGUI {

    @FXML
    private Button createRecipeButton;
    @FXML
    private VBox recipesContainer;

    private final AppController appController = AppController.getInstance();

    @FXML
    public void initialize() {
        if (createRecipeButton != null) {
            createRecipeButton.setOnAction(e -> openCreateRecipeDialog());
        }
    }

    public void loadRecipes() {
        recipesContainer.getChildren().clear();
        Label loadingLabel = new Label("Caricamento in corso...");
        loadingLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #95a5a6;");
        recipesContainer.getChildren().add(loadingLabel);

        new Thread(() -> {
            List<RecipeDTO> recipes = appController.getRecipesData();

            Platform.runLater(() -> {
                recipesContainer.getChildren().clear();
                if (recipes.isEmpty()) {
                    Label placeholder = new Label("Nessuna ricetta presente.");
                    placeholder.setStyle("-fx-font-size: 16px; -fx-text-fill: -color-fg-muted;");
                    recipesContainer.getChildren().add(placeholder);
                } else {
                    for (RecipeDTO recipe : recipes) {
                        recipesContainer.getChildren().add(createRecipeCard(recipe));
                    }
                }
            });
        }).start();
    }

    private Node createRecipeCard(RecipeDTO recipe) {
        VBox card = new VBox(5);
        card.setStyle(
                "-fx-background-color: white; -fx-padding: 12; -fx-background-radius: 8; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.08), 5, 0, 0, 2);");

        // Header: Name + Category
        HBox header = new HBox(10);
        header.setAlignment(Pos.CENTER_LEFT);

        Label name = new Label(recipe.getNome());
        name.setStyle("-fx-font-weight: bold; -fx-font-size: 15px; -fx-text-fill: #2c3e50;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        header.getChildren().addAll(name, spacer);

        // Description
        Label desc = new Label(recipe.getDescrizione());
        desc.setWrapText(true);
        desc.setStyle("-fx-text-fill: #7f8c8d; -fx-font-size: 12px;");

        card.getChildren().addAll(header, desc);
        return card;
    }

    private void openCreateRecipeDialog() {
        boolean success = appController.showCreateRecipeDialog(createRecipeButton.getScene().getWindow());
        if (success) {
            loadRecipes();
        }
    }
}
