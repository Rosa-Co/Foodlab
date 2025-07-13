module progetto.app {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;

    opens progetto.app to javafx.fxml;
    exports progetto.app;
    exports progetto.app.controller;
    opens progetto.app.controller to javafx.fxml;
}