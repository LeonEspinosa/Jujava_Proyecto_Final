package com.example.myjavafx.view;

import com.example.myjavafx.controller.MainController;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * Vista del Listado (Tabla y Búsqueda).
 * Delega eventos al MainController.
 */
public class ListadoView {

    private final MainController controller;
    private TableView<Usuario> tblUsuarios;

    public ListadoView(MainController controller) {
        this.controller = controller;
        this.controller.setUsuarioSeleccionado(null);
    }

    public VBox getView() {
        HBox buscadorBox = createBuscador();
        tblUsuarios = createTable();
        VBox controlBox = createControlBox();

        VBox view = new VBox(10, buscadorBox, tblUsuarios, controlBox);
        view.setPadding(new Insets(15));
        return view;
    }

    private TableView<Usuario> createTable() {
        TableView<Usuario> table = new TableView<>();
        table.setItems(controller.getFilteredData());

        TableColumn<Usuario, String> colDni = new TableColumn<>("DNI");
        colDni.setCellValueFactory(cellData -> cellData.getValue().dniProperty());
        colDni.setPrefWidth(100);

        TableColumn<Usuario, String> colNombre = new TableColumn<>("Nombre");
        colNombre.setCellValueFactory(cellData -> cellData.getValue().nombreProperty());
        colNombre.setPrefWidth(150);

        TableColumn<Usuario, String> colApellido = new TableColumn<>("Apellido");
        colApellido.setCellValueFactory(cellData -> cellData.getValue().apellidoProperty());
        colApellido.setPrefWidth(150);

        TableColumn<Usuario, String> colRol = new TableColumn<>("Rol");
        colRol.setCellValueFactory(cellData -> cellData.getValue().rolProperty());
        colRol.setPrefWidth(120);

        table.getColumns().addAll(colDni, colNombre, colApellido, colRol);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        table.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSelection, newSelection) -> {
                    controller.setUsuarioSeleccionado(newSelection);
                });

        return table;
    }

    private HBox createBuscador() {
        TextField txtDni = new TextField();
        txtDni.setPromptText("Ingrese DNI a buscar");
        txtDni.setPrefWidth(200);

        Button btnBuscar = new Button("Buscar");

        btnBuscar.setOnAction(e -> controller.buscarUsuarioPorDNI(txtDni.getText()));
        txtDni.setOnAction(e -> controller.buscarUsuarioPorDNI(txtDni.getText()));

        HBox buscadorBox = new HBox(5, txtDni, btnBuscar);
        buscadorBox.setPadding(new Insets(0, 0, 10, 0));
        return buscadorBox;
    }

    private VBox createControlBox() {
        Button btnEliminar = new Button("Eliminar Usuario Seleccionado");
        btnEliminar.setOnAction(e -> controller.eliminarUsuarioSeleccionado());

        VBox controlBox = new VBox(btnEliminar);
        return controlBox;
    }
}

