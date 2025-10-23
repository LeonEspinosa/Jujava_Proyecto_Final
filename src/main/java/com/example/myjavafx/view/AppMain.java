package com.example.myjavafx.view;

import com.example.myjavafx.controller.MainController;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Separator;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Vista Principal (Clase Application).
 * Construye la ventana principal (Menú y área de contenido).
 * Delega toda la lógica y navegación al MainController.
 */
public class AppMain extends Application {

    private Stage primaryStage;
    private BorderPane mainLayout;
    private MainController controller; // Referencia al controlador

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Gestión de Turnos (Capa Presentación)");

        // 1. Crear el controlador
        this.controller = new MainController(this);

        // 2. Crear layout principal
        mainLayout = new BorderPane();
        mainLayout.setLeft(createMenu()); // Menú a la izquierda

        // 3. Cargar vista inicial
        cargarListado();

        Scene scene = new Scene(mainLayout, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private VBox createMenu() {
        Button btnRegistro = new Button("Registro (Alta)");
        Button btnModificar = new Button("Modificación");
        Button btnListar = new Button("Listar / Buscar");
        Button btnSalir = new Button("Salir");

        btnRegistro.setMaxWidth(Double.MAX_VALUE);
        btnModificar.setMaxWidth(Double.MAX_VALUE);
        btnListar.setMaxWidth(Double.MAX_VALUE);
        btnSalir.setMaxWidth(Double.MAX_VALUE);

        // --- Delegación al Controlador ---
        btnRegistro.setOnAction(e -> cargarFormulario(null));
        btnListar.setOnAction(e -> cargarListado());
        btnModificar.setOnAction(e -> controller.solicitarModificacion());
        btnSalir.setOnAction(e -> primaryStage.close());
        // --- Fin Delegación ---

        VBox menuBox = new VBox(10, btnRegistro, btnModificar, btnListar, new Separator(), btnSalir);
        menuBox.setPadding(new Insets(15));
        menuBox.setPrefWidth(160);
        menuBox.setStyle("-fx-background-color: #f4f4f4;");
        return menuBox;
    }

    // --- Métodos de Navegación (Llamados por el Controlador) ---

    public void cargarListado() {
        ListadoView listado = new ListadoView(controller);
        mainLayout.setCenter(listado.getView());
    }

    public void cargarFormulario(Usuario usuario) {
        FormularioView formulario = new FormularioView(controller);
        mainLayout.setCenter(formulario.getView(usuario));
    }
}

