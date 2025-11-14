module progetto.app {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;
    requires atlantafx.base;
    requires org.kordamp.ikonli.fontawesome5;
    requires org.kordamp.ikonli.javafx;
    requires com.zaxxer.hikari;
    requires java.sql;
    requires org.postgresql.jdbc;
    requires org.controlsfx.controls;

    opens progetto.app to javafx.fxml;
    exports progetto.app;
    exports progetto.app.controller;
    opens progetto.app.controller to javafx.fxml;
    exports progetto.app.view;
    opens progetto.app.view to javafx.fxml;
}