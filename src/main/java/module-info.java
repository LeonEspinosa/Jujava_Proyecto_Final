module com.example.myjavafx {
    requires javafx.controls;
    // Quitamos fxml si no lo usamos, pero lo dejamos por si acaso (controlsfx a veces lo necesita)
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;

    // Abrimos los paquetes al framework JavaFX
    opens com.example.myjavafx.view to javafx.graphics;
    opens com.example.myjavafx.model to javafx.base;
    opens com.example.myjavafx.controller to javafx.base; // Abrimos controller si es necesario
    opens com.example.myjavafx to javafx.graphics, javafx.fxml;

    // Exportamos los paquetes que deben ser públicos
    exports com.example.myjavafx;
    exports com.example.myjavafx.view;
    exports com.example.myjavafx.controller;
    exports com.example.myjavafx.model;
}

