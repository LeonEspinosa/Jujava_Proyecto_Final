public class Medico extends Usuario {
    String matricula;
    String especialidad;
    public Medico() {}
    public Medico(String nombre, String apellido, int dni, char genero, int telefono, String matricula, String especialidad) {
        super(nombre, apellido, dni, genero, telefono);
        this.matricula = matricula;
        this.especialidad = especialidad;
    }
    public String getMatricula() {
        return matricula;
    }
    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }
    public String getEspecialidad() {
        return especialidad;
    }
    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }
    @Override
    public String toString() {
        return "Medico [nombre=" + getNombre() + ", apellido=" + getApellido() +
                ", dni=" + getDni() + ", genero=" + getGenero() +
                ", telefono=" + getTelefono() + ", matricula=" + matricula +
                ", especialidad=" + especialidad + "]";
    }
    public void registrarMedico(Medico m){
        if(gestor != null && gestor.validarDNIUnico(String.valueOf(m.getDni()))){
            gestor.medicos.add(m);
            HelperCSV.guardarMedicos(gestor.getMedicos());
        }
    }
    public void modificarMedico(int DNI, String nombre, String apellido, int telefono, String matricula, String especialidad){
        if(gestor != null) {
            Usuario usuario = gestor.buscarMedicoPorDNI(DNI);
            if (usuario instanceof Medico medico) {
                medico.setNombre(nombre);
                medico.setApellido(apellido);
                medico.setTelefono(telefono);
                medico.setMatricula(matricula);
                medico.setEspecialidad(especialidad);
                HelperCSV.guardarMedicos(gestor.getMedicos());
            }
        }
    }
}

