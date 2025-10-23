package com.example.myjavafx.controller;

import com.example.myjavafx.model.Usuario;
import com.example.myjavafx.view.AppMain;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import java.io.IOException;

/**
 * Controlador para main-view.fxml (La ventana principal).
 * Maneja la navegación principal cargando otras vistas FXML en el centro.
 */
public class MainViewController {

    @FXML
    private BorderPane contentArea; // El panel central donde se cargan las vistas

    // El "Controlador Lógico" que maneja los datos y el estado
    private MainController dataController;

    /**
     * Llamado automáticamente después de que el FXML es cargado.
     */
    @FXML
    private void initialize() {
        // 1. Inicializa el controlador de lógica/datos
        this.dataController = new MainController(this);

        // 2. Carga la vista de listado por defecto al iniciar
        handleListar();
    }

    // --- Métodos de Navegación (llamados desde los botones del menú) ---

    @FXML
    private void handleListar() {
        // Carga listado-view.fxml en el contentArea
        loadView("listado-view.fxml", null);
    }

    @FXML
    private void handleRegistro() {
        // Carga formulario-view.fxml en modo "Alta" (Usuario es null)
        loadView("formulario-view.fxml", null);
    }

    @FXML
    private void handleModificacion() {
        // Verifica si hay un usuario seleccionado en el controlador de datos
        Usuario seleccionado = dataController.getUsuarioSeleccionado();
        if (seleccionado == null) {
            dataController.showAlert(javafx.scene.control.Alert.AlertType.WARNING, "Advertencia", "Debe seleccionar un usuario en la vista de Listado para modificar.");
            return;
        }
        // Carga formulario-view.fxml en modo "Modificación" (pasando el usuario)
        loadView("formulario-view.fxml", seleccionado);
    }

    @FXML
    private void handleSalir() {
        Platform.exit();
    }


    /**
     * Método genérico para cargar vistas FXML en el panel central.
     * @param fxmlFile El nombre del archivo .fxml a cargar.
     * @param usuario El usuario (si es necesario para la vista, ej. Modificación).
     */
    private void loadView(String fxmlFile, Usuario usuario) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(AppMain.class.getResource("view/" + fxmlFile));

            Node view = loader.load();

            // Pasa el control (Inyección de Dependencia)
            if (fxmlFile.equals("listado-view.fxml")) {
                ListadoViewController controller = loader.getController();
                controller.init(dataController);
            } else if (fxmlFile.equals("formulario-view.fxml")) {
                FormularioViewController controller = loader.getController();
                // Pasamos ambos controladores: el de datos (lógica) y el de navegación (this)
                controller.initData(dataController, this, usuario);
            }

            contentArea.setCenter(view);

        } catch (IOException e) {
            e.printStackTrace();
            dataController.showAlert(javafx.scene.control.Alert.AlertType.ERROR, "Error", "No se pudo cargar la vista: " + fxmlFile);
        }
    }
}
