
import Usuario;
import vistas.FormularioView;
import vistas.ListadoView;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Separator;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


public class AppMain extends Application {


    private Stage primaryStage;
    private BorderPane mainLayout;
    private ObservableList<Usuario> masterData; // Lista central de datos
    private Usuario usuarioSeleccionado; // Guarda la selección del Listado


    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Gestión de Turnos Médicos (Pure JavaFX)");


        // 1. Inicializar datos de ejemplo
        masterData = FXCollections.observableArrayList(
                new Usuario("11111111", "Juan", "Pérez", "Paciente", ""),
                new Usuario("22222222", "María", "Gómez", "Médico", "Matr. 1234"),
                new Usuario("33333333", "Carlos", "López", "Administrativo", "Sector Adm")
        );


        // 2. Crear layout principal (BorderPane)
        mainLayout = new BorderPane();
        mainLayout.setLeft(createMenu()); // Menú a la izquierda


        // 3. Cargar vista inicial (Listado)
        cargarListado();


        Scene scene = new Scene(mainLayout, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    // --- Creación del Menú Principal ---


    private VBox createMenu() {
        Button btnRegistro = new Button("Registro (Alta)");
        Button btnListar = new Button("Listar / Buscar");
        Button btnModificar = new Button("Modificación");
        Button btnSalir = new Button("Salir");


        // Asegura que los botones ocupen todo el ancho
        btnRegistro.setMaxWidth(Double.MAX_VALUE);
        btnListar.setMaxWidth(Double.MAX_VALUE);
        btnModificar.setMaxWidth(Double.MAX_VALUE);
        btnSalir.setMaxWidth(Double.MAX_VALUE);


        // Conexión de acciones a la navegación
        btnRegistro.setOnAction(e -> cargarFormulario(null));
        btnListar.setOnAction(e -> cargarListado());
        btnModificar.setOnAction(e -> handleCargarModificacion());
        btnSalir.setOnAction(e -> primaryStage.close());


        VBox menuBox = new VBox(10, btnRegistro, btnModificar, btnListar, new Separator(), btnSalir);
        menuBox.setPadding(new Insets(15));
        menuBox.setPrefWidth(150);
        menuBox.setStyle("-fx-background-color: #f4f4f4;");
        return menuBox;
    }


    // --- Métodos de Navegación ---


    public void cargarListado() {
        // La vista de listado se crea, se le pasan los datos y el control maestro
        ListadoView listado = new ListadoView(this, masterData);
        mainLayout.setCenter(listado.getView());
    }


    public void cargarFormulario(Usuario usuario) {
        FormularioView formulario = new FormularioView(this);
        // Se carga la vista, pasándole el usuario si es Modificación (o null si es Registro)
        mainLayout.setCenter(formulario.getView(usuario));
    }


    // --- Lógica de Modificación (Invocada desde el menú) ---


    private void handleCargarModificacion() {
        if (usuarioSeleccionado == null) {
            showAlert(Alert.AlertType.WARNING, "Advertencia", "Debe seleccionar un usuario en la vista de Listado para modificar.");
            return;
        }
        cargarFormulario(usuarioSeleccionado);
    }


    // --- Lógica de Gestión de Datos (llamada desde FormularioView) ---


    public void guardarUsuario(Usuario usuario, boolean esModificacion) {
        if (!esModificacion) {
            // Si es un nuevo usuario, lo añadimos a la lista central
            masterData.add(usuario);
            showAlert(Alert.AlertType.INFORMATION, "Éxito", "Usuario registrado correctamente.");
        } else {
            // Si es modificación, el objeto ya está en la lista y fue modificado por referencia
            showAlert(Alert.AlertType.INFORMATION, "Éxito", "Usuario modificado correctamente.");
        }


        // Finalizar y regresar al listado
        usuarioSeleccionado = null;
        cargarListado();
    }


    public void eliminarUsuario(Usuario usuario) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "¿Seguro que desea eliminar al usuario con DNI " + usuario.getDni() + "?", ButtonType.YES, ButtonType.NO);
        if (alert.showAndWait().orElse(ButtonType.NO) == ButtonType.YES) {
            masterData.remove(usuario);
            showAlert(Alert.AlertType.INFORMATION, "Eliminación", "Usuario eliminado correctamente.");
        }
    }


    // --- Métodos de Ayuda ---


    // Usado por ListadoView para guardar la fila seleccionada
    public void setUsuarioSeleccionado(Usuario usuario) {
        this.usuarioSeleccionado = usuario;
    }


    public void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }


    public static void main(String[] args) {
        launch(args);
    }
}

