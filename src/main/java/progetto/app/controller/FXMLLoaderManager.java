package progetto.app.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
//classe deprecata, sostituita da AppController, forse verrà eliminata in futuro.
public class FXMLLoaderManager {

    // Memorizza i controller già caricati per riutilizzarli

    // Cache per memorizzare FXML e controller già caricati
    private static final Map<String, CachedFXML> cache = new HashMap<>();
    // Classe interna per memorizzare root e controller insieme
    private static class CachedFXML {
        private Parent root;
        private Object controller;

        CachedFXML(Parent root, Object controller) {
            this.root = root;
            this.controller = controller;
        }

        public Parent getRoot() {
            return root;
        }

        public void setRoot(Parent root) {
            this.root = root;
        }

        public Object getController() {
            return controller;
        }

        public void setController(Object controller) {
            this.controller = controller;
        }
    }

    private FXMLLoaderManager(){};

    public static Parent loadFXML(String fxmlPath, boolean useCache) throws IOException {
        fxmlPath = formatPath(fxmlPath);

        // Se sta in cache, restituisci la versione cachata
        if (useCache && cache.containsKey(fxmlPath)) {
            return cache.get(fxmlPath).getRoot();
        }

        // Carica l'FXML
        FXMLLoader loader = new FXMLLoader(FXMLLoaderManager.class.getResource(fxmlPath));
        if (loader.getLocation() == null) {
            throw new IOException("Impossibile trovare il path del file: " + fxmlPath);
        }

        Parent root = loader.load();
        Object controller = loader.getController();

        // Salva nella cache se richiesto
        if (useCache) {
            cache.put(fxmlPath, new CachedFXML(root, controller));
        }

        return root;
    }

    // Carica FXML senza cache (sempre nuova istanza)
    public static Parent loadFXML(String fxmlPath) throws IOException {
        return loadFXML(fxmlPath, false);
    }

    // Ottieni il controller di un FXML già caricato dalla cache
    public static Object getController(String fxmlPath) {
        fxmlPath = formatPath(fxmlPath);
        CachedFXML cached = cache.get(fxmlPath);
        if (cached != null) {
            return cached.getController();
        }
        return null;
    }
    // Cambia scena su uno stage
    public static Scene setScene(Stage stage, String fxmlPath, String title) throws IOException {
        Parent root = loadFXML(fxmlPath);
        Scene scene = new Scene(root);
        stage.setTitle(title);
        stage.setScene(scene);
        return scene;
    }
    public static Scene setScene(Stage stage, String fxmlPath) throws IOException {
        Parent root = loadFXML(fxmlPath);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        return scene;
    }


    /* Funzioni deprecate per cambiare scena e mostrare lo stage, probabile vanno eliminate.
    public static Scene changeScene(Stage stage, String fxmlPath, String title) throws IOException{
        Scene newScene = setScene(stage,fxmlPath,title);
        stage.show();
        return newScene;
    }

    public static Scene changeScene(Stage stage, String fxmlPath) throws IOException{
        Scene newScene = setScene(stage,fxmlPath);
        stage.show();
        return newScene;
    }*/

    public static Stage changeStage(Stage currentStage, String fxmlPath, String title) throws IOException {
        // Carico il nuovo layout
        Parent root = loadFXML(fxmlPath,true);
        // 2. Ottengo lo stage corrente
        Stage newStage = new Stage();
        // 3. Imposto la nuova scena
        newStage.setScene(new Scene(root));
        newStage.setTitle(title);

        currentStage.close(); //gestisce automaticamente il dispatch
        newStage.show();

        return newStage;
    }

    // Opzionale: cambia root di un pannello già esistente (per esempio in BorderPane)
    public static void setRoot(Pane parentPane, String fxmlPath) throws IOException {
        Parent root = loadFXML(fxmlPath);
        parentPane.getChildren().setAll(root);
    }

    /**
     * Formatta il percorso FXML per assicurarsi che sia corretto.
     * @param fxmlPath Il percorso FXML da formattare
     * @return Il percorso FXML formattato correttamente.
     */
    private static String formatPath(String fxmlPath){
        // Assicura che il percorso inizi con /
        if (!fxmlPath.startsWith("/")) {
            fxmlPath = "/" + fxmlPath;
        }

        // Se il percorso non contiene il package, aggiungilo
        if (!fxmlPath.contains("/progetto/app/")) {
            fxmlPath = "/progetto/app" + fxmlPath;
        }

        // Assicura che il percorso finisca con .fxml
        if (!fxmlPath.endsWith(".fxml")) {
            fxmlPath += ".fxml";
        }

        return fxmlPath;
    }

    // Pulisce tutta la cache
    public static void clearCache() {
        cache.clear();
    }

    // Rimuove un FXML specifico dalla cache
    public static void removeFromCache(String fxmlPath) {
        fxmlPath = formatPath(fxmlPath);
        cache.remove(fxmlPath);
    }

    // Verifica se un FXML è in cache
    public static boolean isCached(String fxmlPath) {
        fxmlPath = formatPath(fxmlPath);
        return cache.containsKey(fxmlPath);
    }
}
