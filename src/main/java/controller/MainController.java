package controller;

import model.Usuario;
import view.AppMain;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.Optional;

/**
 * Controlador de la Capa de Presentación.
 * Maneja el estado de la UI y la lógica de presentación.
 * Es el único que se comunica con las Vistas y (en el futuro) con la Capa de Negocio.
 */
public class MainController {

    private final AppMain mainView; // Referencia a la vista principal (AppMain) para navegación
    private final ObservableList<Usuario> masterData; // Lista maestra de datos
    private final FilteredList<Usuario> filteredData; // Lista filtrada para la tabla
    private Usuario usuarioSeleccionado; // Estado: Usuario seleccionado en la tabla

    public MainController(AppMain mainView) {
        this.mainView = mainView;

        // --- SIMULACIÓN DE CAPA DE DATOS ---
        // En una app real, esta lista vendría de la Capa de Negocio (Servicio).
        // Por ahora, la hardcodeamos aquí (fuera de la vista).
        this.masterData = FXCollections.observableArrayList(
                new Usuario("11111111", "Juan", "Pérez", "Paciente", ""),
                new Usuario("22222222", "María", "Gómez", "Médico", "Matr. 1234"),
                new Usuario("33333333", "Carlos", "López", "Administrativo", "Sector Adm")
        );
        // --- FIN SIMULACIÓN ---

        this.filteredData = new FilteredList<>(masterData, p -> true);
    }

    // --- Métodos llamados por las Vistas ---

    /**
     * Solicitado por ListadoView para poblar la tabla.
     */
    public FilteredList<Usuario> getFilteredData() {
        return filteredData;
    }

    /**
     * Solicitado por ListadoView cuando se selecciona una fila.
     */
    public void setUsuarioSeleccionado(Usuario usuario) {
        this.usuarioSeleccionado = usuario;
    }

    /**
     * Solicitado por el menú de AppMain (Modificar).
     */
    public void solicitarModificacion() {
        if (usuarioSeleccionado == null) {
            showAlert(Alert.AlertType.WARNING, "Advertencia", "Debe seleccionar un usuario en la vista de Listado para modificar.");
            return;
        }
        mainView.cargarFormulario(usuarioSeleccionado);
    }

    /**
     * Solicitado por ListadoView (Botón Buscar).
     */
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

    /**
     * Solicitado por FormularioView (Botón Guardar).
     */
    public void guardarUsuario(Usuario usuario, boolean esModificacion) {
        // --- LÓGICA DE NEGOCIO (SIMULADA) ---
        // Aquí iría la validación (ej. DNI duplicado, campos obligatorios).
        // En una app real:
        // 1. Validar campos
        // 2. if (esModificacion) { usuarioService.actualizar(usuario); }
        // 3. else { usuarioService.registrar(usuario); }

        if (!validarUsuario(usuario, esModificacion)) {
            return; // La validación falló, la alerta ya se mostró.
        }

        if (esModificacion) {
            // El objeto 'usuario' es una referencia al de masterData,
            // pero si viniera de la capa de negocio, haríamos:
            // masterData.set(masterData.indexOf(usuario), usuario);
            showAlert(Alert.AlertType.INFORMATION, "Éxito", "Usuario modificado correctamente.");
        } else {
            masterData.add(usuario);
            showAlert(Alert.AlertType.INFORMATION, "Éxito", "Usuario registrado correctamente.");
        }
        // --- FIN LÓGICA DE NEGOCIO (SIMULADA) ---

        // Navegamos de vuelta al listado
        this.usuarioSeleccionado = null;
        mainView.cargarListado();
    }

    /**
     * Solicitado por ListadoView (Botón Eliminar).
     */
    public void eliminarUsuarioSeleccionado() {
        if (usuarioSeleccionado == null) {
            showAlert(Alert.AlertType.WARNING, "Advertencia", "Debe seleccionar una fila para eliminar.");
            return;
        }

        // Pedimos confirmación (Lógica de Presentación)
        Optional<ButtonType> result = showConfirmation("¿Seguro que desea eliminar al usuario con DNI " + usuarioSeleccionado.getDni() + "?");

        if (result.isPresent() && result.get() == ButtonType.YES) {
            // --- LÓGICA DE NEGOCIO (SIMULADA) ---
            // En una app real: usuarioService.eliminar(usuarioSeleccionado.getDni());
            masterData.remove(usuarioSeleccionado);
            // --- FIN LÓGICA ---
            showAlert(Alert.AlertType.INFORMATION, "Eliminación", "Usuario eliminado correctamente.");
            usuarioSeleccionado = null;
        }
    }

    /**
     * Lógica de validación (Simulada - Debería estar en Capa de Negocio).
     */
    private boolean validarUsuario(Usuario u, boolean esModificacion) {
        if (u.getDni().trim().isEmpty() || u.getNombre().trim().isEmpty() || u.getRol() == null) {
            showAlert(Alert.AlertType.ERROR, "Error", "DNI, Nombre, y Rol son obligatorios.");
            return false;
        }
        if (("Médico".equals(u.getRol()) || "Administrativo".equals(u.getRol())) && u.getInfoExtra().trim().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "Rellene la información específica para el rol seleccionado.");
            return false;
        }

        // Validación de DNI duplicado (Solo en Alta)
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
}
