package view;

import controller.MainController;
import model.Usuario;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import java.util.Arrays;

/**
 * Vista del Formulario (Alta y Modificación).
 * Es "tonta": solo dibuja la UI, recolecta datos y delega
 * la acción de "Guardar" al MainController.
 */
public class FormularioView {

    private final MainController controller;
    private Usuario usuarioActual; // El usuario que se está modificando (o null si es Alta)

    // Componentes del Formulario
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

    /**
     * Construye y devuelve el VBox principal de esta vista.
     */
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

    // --- Componentes y Diseño ---

    private GridPane createFormFields(boolean esModificacion) {
        GridPane grid = new GridPane();
        grid.setVgap(10);
        grid.setHgap(10);
        grid.setPadding(new Insets(10));

        // 1. ROL
        cmbRol = new ComboBox<>();
        cmbRol.getItems().addAll(Arrays.asList("Paciente", "Médico", "Administrativo"));
        cmbRol.setPromptText("Seleccione Rol");

        // 2. Campos Fijos
        txtDNI = new TextField(); txtDNI.setPromptText("DNI");
        txtNombre = new TextField(); txtNombre.setPromptText("Nombre");
        txtApellido = new TextField(); txtApellido.setPromptText("Apellido");

        // 3. Campos Dinámicos
        lblInfoExtra = new Label("Información Específica:");
        txtInfoExtra = new TextField();
        vbDatosEspecificos = new VBox(5, lblInfoExtra, txtInfoExtra);
        vbDatosEspecificos.setVisible(false); // Oculto por defecto

        // Listener para la lógica de campos dinámicos (esto es lógica de vista)
        cmbRol.valueProperty().addListener((obs, oldVal, newVal) -> actualizarCamposDinamicos(newVal));

        // Añadir al Grid
        grid.addRow(0, new Label("Rol:"), cmbRol);
        grid.addRow(1, new Label("DNI:"), txtDNI);
        grid.addRow(2, new Label("Nombre:"), txtNombre);
        grid.addRow(3, new Label("Apellido:"), txtApellido);
        grid.add(vbDatosEspecificos, 0, 4, 2, 1); // Ocupa dos columnas

        // Cargar datos si es modificación
        if (esModificacion) {
            cargarDatosParaModificacion();
        }

        return grid;
    }

    private HBox createButtons(boolean esModificacion) {
        btnGuardar = new Button(esModificacion ? "Guardar Modificación" : "Registrar");
        Button btnCancelar = new Button("Cancelar");

        // Delegación de eventos al controlador
        btnGuardar.setOnAction(e -> handleGuardar());
        btnCancelar.setOnAction(e -> controller.mainView.cargarListado()); // Navega al listado

        HBox buttonBox = new HBox(10, btnGuardar, btnCancelar);
        return buttonBox;
    }

    // --- Lógica de Estado de la Vista ---

    /**
     * Muestra u oculta campos según el Rol. Esto es lógica de presentación.
     */
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
            // Paciente
            vbDatosEspecificos.setVisible(false);
        }
    }

    /**
     * Rellena el formulario si estamos en modo Modificación.
     */
    private void cargarDatosParaModificacion() {
        if (usuarioActual == null) return;

        txtDNI.setText(usuarioActual.getDni());
        txtDNI.setEditable(false); // El DNI (PK) no se debe modificar
        txtDNI.setStyle("-fx-background-color: #eeeeee;"); // Feedback visual

        txtNombre.setText(usuarioActual.getNombre());
        txtApellido.setText(usuarioActual.getApellido());

        // Esto dispara el listener y llama a actualizarCamposDinamicos()
        cmbRol.setValue(usuarioActual.getRol());
        txtInfoExtra.setText(usuarioActual.getInfoExtra());
    }

    /**
     * Recolecta los datos y los envía al controlador para guardar.
     * La vista NO valida ni crea objetos de negocio.
     */
    private void handleGuardar() {
        String dni = txtDNI.getText();
        String nombre = txtNombre.getText();
        String apellido = txtApellido.getText();
        String rol = cmbRol.getValue();
        String infoExtra = vbDatosEspecificos.isVisible() ? txtInfoExtra.getText() : "";

        if (usuarioActual != null) {
            // Modificación: Actualizamos el objeto existente
            // (En una app pura, pasaríamos los datos crudos al controlador)
            usuarioActual.setNombre(nombre);
            usuarioActual.setApellido(apellido);
            usuarioActual.setRol(rol);
            usuarioActual.setInfoExtra(infoExtra);
            controller.guardarUsuario(usuarioActual, true);
        } else {
            // Registro: Creamos un DTO y lo pasamos al controlador
            Usuario nuevoUsuario = new Usuario(dni, nombre, apellido, rol, infoExtra);
            controller.guardarUsuario(nuevoUsuario, false);
        }
    }
}
