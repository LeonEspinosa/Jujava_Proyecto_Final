package com.example.myjavafx.view;

import com.example.myjavafx.controller.MainController;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import java.util.Arrays;

/**
 * Vista del Formulario (Alta y Modificación).
 * Delega la acción de "Guardar" al MainController.
 */
public class FormularioView {

    private final MainController controller;
    private Usuario usuarioActual;

    private ComboBox<String> cmbRol;
    private TextField txtDNI;
    private TextField txtNombre;
    private TextField txtApellido;
    private TextField txtInfoExtra;
    private Label lblInfoExtra;
    private VBox vbDatosEspecificos;
    private Button btnGuardar;

    public FormularioView(MainController controller) {
        this.controller = controller;
    }

    public VBox getView(Usuario usuario) {
        this.usuarioActual = usuario;
        boolean esModificacion = (usuario != null);

        String titulo = esModificacion ? "Formulario de Modificación" : "Formulario de Alta (Registro)";
        Label lblTitulo = new Label(titulo);
        lblTitulo.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        GridPane formGrid = createFormFields(esModificacion);
        HBox buttonBox = createButtons(esModificacion);

        VBox view = new VBox(20, lblTitulo, formGrid, buttonBox);
        view.setPadding(new Insets(20));
        view.setSpacing(15);
        return view;
    }

    private GridPane createFormFields(boolean esModificacion) {
        GridPane grid = new GridPane();
        grid.setVgap(10);
        grid.setHgap(10);
        grid.setPadding(new Insets(10));

        cmbRol = new ComboBox<>();
        cmbRol.getItems().addAll(Arrays.asList("Paciente", "Médico", "Administrativo"));
        cmbRol.setPromptText("Seleccione Rol");

        txtDNI = new TextField(); txtDNI.setPromptText("DNI");
        txtNombre = new TextField(); txtNombre.setPromptText("Nombre");
        txtApellido = new TextField(); txtApellido.setPromptText("Apellido");

        lblInfoExtra = new Label("Información Específica:");
        txtInfoExtra = new TextField();
        vbDatosEspecificos = new VBox(5, lblInfoExtra, txtInfoExtra);
        vbDatosEspecificos.setVisible(false);

        cmbRol.valueProperty().addListener((obs, oldVal, newVal) -> actualizarCamposDinamicos(newVal));

        grid.addRow(0, new Label("Rol:"), cmbRol);
        grid.addRow(1, new Label("DNI:"), txtDNI);
        grid.addRow(2, new Label("Nombre:"), txtNombre);
        grid.addRow(3, new Label("Apellido:"), txtApellido);
        grid.add(vbDatosEspecificos, 0, 4, 2, 1);

        if (esModificacion) {
            cargarDatosParaModificacion();
        }

        return grid;
    }

    private HBox createButtons(boolean esModificacion) {
        btnGuardar = new Button(esModificacion ? "Guardar Modificación" : "Registrar");
        Button btnCancelar = new Button("Cancelar");

        btnGuardar.setOnAction(e -> handleGuardar());
        btnCancelar.setOnAction(e -> controller.getMainView().cargarListado()); // Navega al listado

        HBox buttonBox = new HBox(10, btnGuardar, btnCancelar);
        return buttonBox;
    }

    private void actualizarCamposDinamicos(String rol) {
        if (rol == null) {
            vbDatosEspecificos.setVisible(false);
            return;
        }

        vbDatosEspecificos.setVisible(true);
        txtInfoExtra.setText("");

        if ("Médico".equals(rol)) {
            lblInfoExtra.setText("Matrícula del Médico:");
            txtInfoExtra.setPromptText("Ej. 1234");
        } else if ("Administrativo".equals(rol)) {
            lblInfoExtra.setText("Sector del Administrativo:");
            txtInfoExtra.setPromptText("Ej. Turnos");
        } else {
            vbDatosEspecificos.setVisible(false);
        }
    }

    private void cargarDatosParaModificacion() {
        if (usuarioActual == null) return;

        txtDNI.setText(usuarioActual.getDni());
        txtDNI.setEditable(false);
        txtDNI.setStyle("-fx-background-color: #eeeeee;");

        txtNombre.setText(usuarioActual.getNombre());
        txtApellido.setText(usuarioActual.getApellido());

        cmbRol.setValue(usuarioActual.getRol());
        txtInfoExtra.setText(usuarioActual.getInfoExtra());
    }

    private void handleGuardar() {
        String dni = txtDNI.getText();
        String nombre = txtNombre.getText();
        String apellido = txtApellido.getText();
        String rol = cmbRol.getValue();
        String infoExtra = vbDatosEspecificos.isVisible() ? txtInfoExtra.getText() : "";

        if (usuarioActual != null) {
            // Modificación
            usuarioActual.setNombre(nombre);
            usuarioActual.setApellido(apellido);
            usuarioActual.setRol(rol);
            usuarioActual.setInfoExtra(infoExtra);
            controller.guardarUsuario(usuarioActual, true);
        } else {
            // Registro
            Usuario nuevoUsuario = new Usuario(dni, nombre, apellido, rol, infoExtra);
            controller.guardarUsuario(nuevoUsuario, false);
        }
    }
}

