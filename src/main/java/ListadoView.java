import AppMain;
import Usuario;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;


public class ListadoView {


    private final AppMain mainApp;
    private final FilteredList<Usuario> filteredData;
    private Usuario usuarioSeleccionado;


    public ListadoView(AppMain mainApp, ObservableList<Usuario> masterData) {
        this.mainApp = mainApp;
        // La lista filtrada se basa en la lista central de AppMain
        this.filteredData = new FilteredList<>(masterData, p -> true);
        // Limpiamos la selección anterior al cargar la vista
        mainApp.setUsuarioSeleccionado(null);
    }


    public VBox getView() {
        TableView<Usuario> tblUsuarios = createTable();
        HBox buscadorBox = createBuscador();
        VBox controlBox = createControlBox();


        VBox view = new VBox(10, buscadorBox, tblUsuarios, controlBox);
        view.setPadding(new Insets(15));
        return view;
    }


    // --- Componentes ---


    private TableView<Usuario> createTable() {
        TableView<Usuario> table = new TableView<>();
        table.setItems(filteredData);


        // Mapeo de Columnas
        TableColumn<Usuario, String> colDni = new TableColumn<>("DNI");
        colDni.setCellValueFactory(cellData -> cellData.getValue().dniProperty());
        colDni.setPrefWidth(100);


        TableColumn<Usuario, String> colNombre = new TableColumn<>("Nombre");
        colNombre.setCellValueFactory(cellData -> cellData.getValue().nombreProperty());


        TableColumn<Usuario, String> colRol = new TableColumn<>("Rol");
        colRol.setCellValueFactory(cellData -> cellData.getValue().rolProperty());


        table.getColumns().addAll(colDni, colNombre, colRol);


        // Listener de Selección de Fila para Modificación/Eliminación
        table.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSelection, newSelection) -> {
                    usuarioSeleccionado = newSelection;
                    mainApp.setUsuarioSeleccionado(newSelection); // Guarda la selección en AppMain
                });


        return table;
    }


    private HBox createBuscador() {
        TextField txtDni = new TextField();
        txtDni.setPromptText("Ingrese DNI a buscar");
        txtDni.setPrefWidth(200);


        Button btnBuscar = new Button("Buscar");
        btnBuscar.setOnAction(e -> handleBuscar(txtDni.getText()));
        txtDni.setOnAction(e -> handleBuscar(txtDni.getText())); // Buscar al presionar ENTER


        HBox buscadorBox = new HBox(5, txtDni, btnBuscar);
        buscadorBox.setPadding(new Insets(0, 0, 10, 0));
        return buscadorBox;
    }


    private VBox createControlBox() {
        // Botón Eliminar estará al lado de la lista (debajo en el VBox)
        Button btnEliminar = new Button("Eliminar Usuario Seleccionado");
        btnEliminar.setOnAction(e -> handleEliminar());


        VBox controlBox = new VBox(btnEliminar);
        return controlBox;
    }


    // --- Lógica de Búsqueda ---


    private void handleBuscar(String dniBusqueda) {
        String dniTrimmed = dniBusqueda.trim();


        filteredData.setPredicate(usuario -> {
            if (dniTrimmed.isEmpty()) {
                return true; // Mostrar toda la lista si el campo está vacío
            }
            // Filtrar exactamente por DNI
            return usuario.getDni().equalsIgnoreCase(dniTrimmed);
        });


        if (filteredData.isEmpty() && !dniTrimmed.isEmpty()) {
            mainApp.showAlert(Alert.AlertType.INFORMATION, "Búsqueda", "No se encontró ningún usuario con ese DNI.");
        }
    }


    private void handleEliminar() {
        if (usuarioSeleccionado != null) {
            mainApp.eliminarUsuario(usuarioSeleccionado);
            usuarioSeleccionado = null;
        } else {
            mainApp.showAlert(Alert.AlertType.WARNING, "Advertencia", "Debe seleccionar una fila para eliminar.");
        }
    }
}

