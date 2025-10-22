module com.example.myjavafx {
    requires javafx.controls;
    requires javafx.fxml; // Aunque no usamos FXML, controlsfx puede requerirlo
    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;

    // Abrimos los paquetes de la vista y el modelo a JavaFX
    opens com.example.myjavafx.view to javafx.graphics;
    opens com.example.myjavafx.model to javafx.base;

    exports view;
    exports controller;
    exports model;
}
