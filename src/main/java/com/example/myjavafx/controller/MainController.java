package com.example.myjavafx.controller;

import com.example.myjavafx.model.Usuario;
import com.example.myjavafx.view.AppMain;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.Optional;

/**
 * Controlador de la Capa de Presentación.
 * Maneja el estado de la UI y la lógica de presentación.
 */
public class MainController {

    private AppMain mainView; // Referencia a la vista principal (AppMain) para navegación
    private final ObservableList<Usuario> masterData; // Lista maestra de datos
    private final FilteredList<Usuario> filteredData; // Lista filtrada para la tabla
    private Usuario usuarioSeleccionado; // Estado: Usuario seleccionado en la tabla

    public MainController(AppMain mainView) {
        this.mainView = mainView;

        // --- SIMULACIÓN DE CAPA DE DATOS ---
        // En una app real, esta lista vendría de la Capa de Negocio (Servicio).
        this.masterData = FXCollections.observableArrayList(
                new Usuario("11111111", "Juan", "Pérez", "Paciente", ""),
                new Usuario("22222222", "María", "Gómez", "Médico", "Matr. 1234"),
                new Usuario("33333333", "Carlos", "López", "Administrativo", "Sector Adm")
        );
        // --- FIN SIMULACIÓN ---

        this.filteredData = new FilteredList<>(masterData, p -> true);
    }

    // --- Métodos llamados por las Vistas ---

    public FilteredList<Usuario> getFilteredData() {
        return filteredData;
    }

    public void setUsuarioSeleccionado(Usuario usuario) {
        this.usuarioSeleccionado = usuario;
    }

    public void solicitarModificacion() {
        if (usuarioSeleccionado == null) {
            showAlert(Alert.AlertType.WARNING, "Advertencia", "Debe seleccionar un usuario en la vista de Listado para modificar.");
            return;
        }
        mainView.cargarFormulario(usuarioSeleccionado);
    }

    public void buscarUsuarioPorDNI(String dni) {
        String dniTrimmed = dni.trim();
        filteredData.setPredicate(usuario -> {
            if (dniTrimmed.isEmpty()) {
                return true; // Mostrar todo
            }
            return usuario.getDni().equalsIgnoreCase(dniTrimmed);
        });

        if (filteredData.isEmpty() && !dniTrimmed.isEmpty()) {
            showAlert(Alert.AlertType.INFORMATION, "Búsqueda", "No se encontró ningún usuario con ese DNI.");
        }
    }

    public void guardarUsuario(Usuario usuario, boolean esModificacion) {
        // --- LÓGICA DE NEGOCIO (SIMULADA) ---
        if (!validarUsuario(usuario, esModificacion)) {
            return; // La validación falló
        }

        if (esModificacion) {
            showAlert(Alert.AlertType.INFORMATION, "Éxito", "Usuario modificado correctamente.");
        } else {
            masterData.add(usuario);
            showAlert(Alert.AlertType.INFORMATION, "Éxito", "Usuario registrado correctamente.");
        }
        // --- FIN LÓGICA DE NEGOCIO (SIMULADA) ---

        this.usuarioSeleccionado = null;
        mainView.cargarListado();
    }

    public void eliminarUsuarioSeleccionado() {
        if (usuarioSeleccionado == null) {
            showAlert(Alert.AlertType.WARNING, "Advertencia", "Debe seleccionar una fila para eliminar.");
            return;
        }

        Optional<ButtonType> result = showConfirmation("¿Seguro que desea eliminar al usuario con DNI " + usuarioSeleccionado.getDni() + "?");

        if (result.isPresent() && result.get() == ButtonType.YES) {
            masterData.remove(usuarioSeleccionado);
            showAlert(Alert.AlertType.INFORMATION, "Eliminación", "Usuario eliminado correctamente.");
            usuarioSeleccionado = null;
        }
    }

    private boolean validarUsuario(Usuario u, boolean esModificacion) {
        if (u.getDni().trim().isEmpty() || u.getNombre().trim().isEmpty() || u.getRol() == null) {
            showAlert(Alert.AlertType.ERROR, "Error", "DNI, Nombre, y Rol son obligatorios.");
            return false;
        }
        if (("Médico".equals(u.getRol()) || "Administrativo".equals(u.getRol())) && u.getInfoExtra().trim().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "Rellene la información específica para el rol seleccionado.");
            return false;
        }

        if (!esModificacion) {
            boolean duplicado = masterData.stream().anyMatch(existente -> existente.getDni().equals(u.getDni()));
            if (duplicado) {
                showAlert(Alert.AlertType.ERROR, "Error", "El DNI ingresado ya existe.");
                return false;
            }
        }

        return true;
    }

    // --- Métodos de UI (Helpers) ---

    public void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public Optional<ButtonType> showConfirmation(String content) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, content, ButtonType.YES, ButtonType.NO);
        alert.setTitle("Confirmación");
        alert.setHeaderText(null);
        return alert.showAndWait();
    }

    // Getter para que FormularioView pueda navegar de vuelta
    public AppMain getMainView() {
        return mainView;
    }
}

