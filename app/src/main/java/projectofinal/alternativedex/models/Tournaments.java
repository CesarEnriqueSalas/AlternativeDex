package projectofinal.alternativedex.models;

import android.content.Context;
import android.content.Intent;

import projectofinal.alternativedex.activities.TournamentsActivity;

public class Tournaments {

    private String fecha, formato, lugarEvento, nombre, organizador, ubicacion, imagen;


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

    public Intent getIntent(Context context){
        Intent intent = new Intent(context, TournamentsActivity.class);
        intent.putExtra("fecha", this.fecha);
        intent.putExtra("formato", this.formato);
        intent.putExtra("lugarEvento", this.lugarEvento);
        intent.putExtra("nombre", this.nombre);
        intent.putExtra("organizador", this.organizador);
        intent.putExtra("ubicacion", this.ubicacion);
        intent.putExtra("imagen", this.imagen);
        return intent;
    }
}
