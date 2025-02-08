package com.gestorproyectos_v01.modelos;

import android.media.Image;
import android.os.Parcel;
import android.os.Parcelable;
import android.widget.CheckedTextView;

import androidx.annotation.NonNull;

import java.util.Date;
import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class Proyecto extends RealmObject {

    @PrimaryKey
    private String id;
    @Required
    private String nombre_proyecto;
    private String descripcion_proyecto;
    private boolean pendiente;
    //campos de detalle proyecto
    private RealmList<String> materiales;
    private RealmList<String> enlaces;
    private RealmList<String> url_imagenes;
    /* TODO: calendario con eventos de fin de proyecto
    private Date fecha_fin;
*/
    //prueba separar proys por usuario
    private String usu_logueado;

    public Proyecto(){

    }

    public Proyecto(String id, String nombre_proyecto, String descripcion_proyecto, boolean pendiente) {
        this.id = id;
        this.nombre_proyecto = nombre_proyecto;
        this.descripcion_proyecto = descripcion_proyecto;
        this.pendiente = pendiente;
    }

    public Proyecto(String nombre_proyecto, String desc, boolean pendiente) {
        this.nombre_proyecto = nombre_proyecto;
        this.descripcion_proyecto = desc;
        this.pendiente = pendiente;
    }


    //pruebas añadiendo items detalle version antes de pruebas: v07


    public Proyecto(String id, String nombre_proyecto, String descripcion_proyecto, RealmList<String> materiales, boolean pendiente) {
        this.id = id;
        this.nombre_proyecto = nombre_proyecto;
        this.descripcion_proyecto = descripcion_proyecto;
        this.materiales = materiales;
        this.pendiente = pendiente;
    }

    public Proyecto(String id, String nombre_proyecto, String descripcion_proyecto, boolean pendiente, RealmList<String> materiales, RealmList<String> enlaces, RealmList<String> url_imagenes) {
        this.id = id;
        this.nombre_proyecto = nombre_proyecto;
        this.descripcion_proyecto = descripcion_proyecto;
        this.pendiente = pendiente;
        this.materiales = materiales;
        this.enlaces = enlaces;
        this.url_imagenes = url_imagenes;
    }

    public Proyecto(String id, String nombre_proyecto, String descripcion_proyecto, boolean pendiente, RealmList<String> materiales, RealmList<String> enlaces, RealmList<String> url_imagenes, String usu_logueado) {
        this.id = id;
        this.nombre_proyecto = nombre_proyecto;
        this.descripcion_proyecto = descripcion_proyecto;
        this.pendiente = pendiente;
        this.materiales = materiales;
        this.enlaces = enlaces;
        this.url_imagenes = url_imagenes;
        this.usu_logueado = usu_logueado;
    }

    /**
     * GETTERS AND SETTERS
     */
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre_proyecto() {
        return nombre_proyecto;
    }

    public void setNombre_proyecto(String nombre_proyecto) {
        this.nombre_proyecto = nombre_proyecto;
    }

    public String getDescripcion_proyecto() {
        return descripcion_proyecto;
    }

    public void setDescripcion_proyecto(String descripcion_proyecto) {
        this.descripcion_proyecto = descripcion_proyecto;
    }

    public RealmList<String> getMateriales() {
        return materiales;
    }

    public void setMateriales(RealmList<String> materiales) {
        this.materiales = materiales;
    }

    public boolean isPendiente() {
        return pendiente;
    }

    public void setPendiente(boolean pendiente) {
        this.pendiente = pendiente;
    }

    public RealmList<String> getEnlaces() {
        return enlaces;
    }

    public void setEnlaces(RealmList<String> enlaces) {
        this.enlaces = enlaces;
    }

    public RealmList<String> getUrl_imagenes() {
        return url_imagenes;
    }

    public void setUrl_imagenes(RealmList<String> url_imagenes) {
        this.url_imagenes = url_imagenes;
    }

    public String getUsu_logueado() {
        return usu_logueado;
    }

    public void setUsu_logueado(String usu_logueado) {
        this.usu_logueado = usu_logueado;
    }

    @Override
    public String toString() {
        return "Proyecto{" +
                "id='" + id + '\'' +
                ", nombre_proyecto='" + nombre_proyecto + '\'' +
                ", descripcion_proyecto='" + descripcion_proyecto + '\'' +
                ", pendiente=" + pendiente +
                ", materiales=" + materiales +
                ", enlaces=" + enlaces +
                ", url_imagenes=" + url_imagenes +
                ", usu_logueado='" + usu_logueado + '\'' +
                '}';
    }
}
