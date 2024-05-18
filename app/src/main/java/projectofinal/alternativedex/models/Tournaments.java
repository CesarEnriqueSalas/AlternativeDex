package projectofinal.alternativedex.models;

public class Tournaments {

    private String fecha, formato, lugarEvento, nombre, organizador, ubicacion, imagen;


    public Tournaments(){}

    public Tournaments(String fecha, String formato, String lugarEvento, String nombre, String organizador, String ubicacion, String imagen) {
        this.fecha = fecha;
        this.formato = formato;
        this.lugarEvento = lugarEvento;
        this.nombre = nombre;
        this.organizador = organizador;
        this.ubicacion = ubicacion;
        this.imagen = imagen;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getFormato() {
        return formato;
    }

    public void setFormato(String formato) {
        this.formato = formato;
    }

    public String getLugarEvento() {
        return lugarEvento;
    }

    public void setLugarEvento(String lugarEvento) {
        this.lugarEvento = lugarEvento;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getOrganizador() {
        return organizador;
    }

    public void setOrganizador(String organizador) {
        this.organizador = organizador;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }
}
