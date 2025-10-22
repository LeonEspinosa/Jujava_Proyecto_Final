import app.AppMain;
import modelo.Usuario;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import java.util.Arrays;


public class FormularioView {


    private final AppMain mainApp;
    private Usuario usuarioActual;


    // Componentes del Formulario
    private ComboBox<String> cmbRol;
    private TextField txtDNI;
    private TextField txtNombre;
    private TextField txtApellido;
    private TextField txtInfoExtra;
    private VBox vbDatosEspecificos;
    private Button btnGuardar;


    public FormularioView(AppMain mainApp) {
        this.mainApp = mainApp;
    }


    public VBox getView(Usuario usuario) {
        this.usuarioActual = usuario;
        boolean esModificacion = (usuario != null);


        GridPane formGrid = createFormFields(esModificacion);
        HBox buttonBox = createButtons(esModificacion);


        VBox view = new VBox(20, formGrid, buttonBox);
        view.setPadding(new Insets(20));
        view.setSpacing(15);
        return view;
    }


    // --- Componentes y Diseño ---


    private GridPane createFormFields(boolean esModificacion) {
        GridPane grid = new GridPane();
        grid.setVgap(10);
        grid.setHgap(10);
        grid.setPadding(new Insets(10));


        // 1. ROL (El primero)
        cmbRol = new ComboBox<>();
        cmbRol.getItems().addAll(Arrays.asList("Paciente", "Médico", "Administrativo"));
        cmbRol.setPromptText("Seleccione Rol");


        // 2. Campos Fijos
        txtDNI = new TextField(); txtDNI.setPromptText("DNI");
        txtNombre = new TextField(); txtNombre.setPromptText("Nombre");
        txtApellido = new TextField(); txtApellido.setPromptText("Apellido");


        // 3. Campos Dinámicos
        txtInfoExtra = new TextField();
        vbDatosEspecificos = new VBox(5, new Label("Información Específica:"), txtInfoExtra);
        vbDatosEspecificos.setVisible(false);


        // Listener para la lógica de campos dinámicos
        cmbRol.valueProperty().addListener((obs, oldVal, newVal) -> actualizarCamposDinamicos(newVal));


        // Añadir al Grid
        grid.addRow(0, new Label("Rol:"), cmbRol);
        grid.addRow(1, new Label("DNI:"), txtDNI);
        grid.addRow(2, new Label("Nombre:"), txtNombre);
        grid.addRow(3, new Label("Apellido:"), txtApellido);
        grid.add(vbDatosEspecificos, 0, 4, 2, 1); // El VBox ocupa dos columnas


        // Cargar datos si es modificación
        if (esModificacion) {
            cargarDatosParaModificacion();
        }


        return grid;
    }


    private HBox createButtons(boolean esModificacion) {
        btnGuardar = new Button(esModificacion ? "Guardar Modificación" : "Registrar");
        Button btnCancelar = new Button("Cancelar");


        btnGuardar.setOnAction(e -> handleGuardar());
        btnCancelar.setOnAction(e -> mainApp.cargarListado());


        HBox buttonBox = new HBox(10, btnGuardar, btnCancelar);
        return buttonBox;
    }


    // --- Lógica Dinámica y de Estado ---


    private void actualizarCamposDinamicos(String rol) {
        if (rol == null) return;


        vbDatosEspecificos.setVisible(false);
        txtInfoExtra.setText("");


        if ("Médico".equals(rol)) {
            vbDatosEspecificos.setVisible(true);
            txtInfoExtra.setPromptText("Matrícula del Médico (Ej. 1234)");
        } else if ("Administrativo".equals(rol)) {
            vbDatosEspecificos.setVisible(true);
            txtInfoExtra.setPromptText("Sector del Administrativo (Ej. Turnos)");
        }
    }


    private void cargarDatosParaModificacion() {
        if (usuarioActual == null) return;


        txtDNI.setText(usuarioActual.getDni());
        txtDNI.setEditable(false); // El DNI no se debe modificar


        txtNombre.setText(usuarioActual.nombreProperty().get());
        txtApellido.setText(usuarioActual.apellidoProperty().get());


        // Esto dispara el listener y llama a actualizarCamposDinamicos()
        cmbRol.setValue(usuarioActual.getRol());
        txtInfoExtra.setText(usuarioActual.getInfoExtra());
    }


    private void handleGuardar() {
        if (!validarCampos()) {
            return;
        }


        String nombre = txtNombre.getText();
        String apellido = txtApellido.getText();
        String rol = cmbRol.getValue();
        String infoExtra = vbDatosEspecificos.isVisible() ? txtInfoExtra.getText() : "";


        if (usuarioActual != null) {
            // Modificación: Actualizamos las propiedades del objeto referenciado
            usuarioActual.setNombre(nombre);
            usuarioActual.setApellido(apellido);
            usuarioActual.setRol(rol);
            usuarioActual.setInfoExtra(infoExtra);
            mainApp.guardarUsuario(usuarioActual, true);
        } else {
            // Registro: Creamos nuevo objeto
            String dni = txtDNI.getText();
            Usuario nuevoUsuario = new Usuario(dni, nombre, apellido, rol, infoExtra);
            mainApp.guardarUsuario(nuevoUsuario, false);
        }
    }


    private boolean validarCampos() {
        if (txtDNI.getText().trim().isEmpty() || txtNombre.getText().trim().isEmpty() || cmbRol.getValue() == null) {
            mainApp.showAlert(Alert.AlertType.ERROR, "Error", "DNI, Nombre, y Rol son obligatorios.");
            return false;
        }
        if (vbDatosEspecificos.isVisible() && txtInfoExtra.getText().trim().isEmpty()) {
            mainApp.showAlert(Alert.AlertType.ERROR, "Error", "Rellene la información específica para el rol seleccionado.");
            return false;
        }
        return true;
    }
}

