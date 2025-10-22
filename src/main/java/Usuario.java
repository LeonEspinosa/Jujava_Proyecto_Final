
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;


public class Usuario {


    private final StringProperty dni;
    private final StringProperty nombre;
    private final StringProperty apellido;
    private final StringProperty rol;
    private final StringProperty infoExtra; // Usado para Matrícula (Médico) o Sector (Admin)


    public Usuario(String dni, String nombre, String apellido, String rol, String infoExtra) {
        this.dni = new SimpleStringProperty(dni);
        this.nombre = new SimpleStringProperty(nombre);
        this.apellido = new SimpleStringProperty(apellido);
        this.rol = new SimpleStringProperty(rol);
        this.infoExtra = new SimpleStringProperty(infoExtra);
    }


    // Getters para las PROPIEDADES (necesarios para TableView)
    public StringProperty dniProperty() { return dni; }
    public StringProperty nombreProperty() { return nombre; }
    public StringProperty apellidoProperty() { return apellido; }
    public StringProperty rolProperty() { return rol; }
    public StringProperty infoExtraProperty() { return infoExtra; }


    // Getters para obtener el VALOR
    public String getDni() { return dni.get(); }
    public String getRol() { return rol.get(); }
    public String getInfoExtra() { return infoExtra.get(); }


    // Setters para la Modificación
    public void setNombre(String nombre) { this.nombre.set(nombre); }
    public void setApellido(String apellido) { this.apellido.set(apellido); }
    public void setRol(String rol) { this.rol.set(rol); }
    public void setInfoExtra(String infoExtra) { this.infoExtra.set(infoExtra); }
}

