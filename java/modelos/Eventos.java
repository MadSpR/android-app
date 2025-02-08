package com.gestorproyectos_v01.modelos;

import java.util.Date;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class Eventos extends RealmObject {

    @PrimaryKey
    private String id;
    @Required
    private String nombre_evento;
    private Date fecha_fin;
    private String usu_logueado;
    private String dia_evento, mes_evento;
    public Eventos(){

    }

    public Eventos(String id, String nombre_evento, Date fecha_fin) {
        this.id = id;
        this.nombre_evento = nombre_evento;
        this.fecha_fin = fecha_fin;
    }

    public Eventos(String id, String nombre_evento, Date fecha_fin, String usu_logueado) {
        this.id = id;
        this.nombre_evento = nombre_evento;
        this.fecha_fin = fecha_fin;
        this.usu_logueado = usu_logueado;
    }

    public Eventos(String id, String nombre_evento, Date fecha_fin, String usu_logueado, String dia_evento, String mes_evento) {
        this.id = id;
        this.nombre_evento = nombre_evento;
        this.fecha_fin = fecha_fin;
        this.usu_logueado = usu_logueado;
        this.dia_evento = dia_evento;
        this.mes_evento = mes_evento;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre_evento() {
        return nombre_evento;
    }

    public void setNombre_evento(String nombre_evento) {
        this.nombre_evento = nombre_evento;
    }

    public Date getFecha_fin() {
        return fecha_fin;
    }

    public void setFecha_fin(Date fecha_fin) {
        this.fecha_fin = fecha_fin;
    }

    public String getUsu_logueado() {
        return usu_logueado;
    }

    public void setUsu_logueado(String usu_logueado) {
        this.usu_logueado = usu_logueado;
    }

    public String getDia_evento() {
        return dia_evento;
    }

    public void setDia_evento(String dia_evento) {
        this.dia_evento = dia_evento;
    }

    public String getMes_evento() {
        return mes_evento;
    }

    public void setMes_evento(String mes_evento) {
        this.mes_evento = mes_evento;
    }

    @Override
    public String toString() {
        return "Eventos{" +
                "id='" + id + '\'' +
                ", nombre_evento='" + nombre_evento + '\'' +
                ", fecha_fin=" + fecha_fin +
                ", usu_logueado='" + usu_logueado + '\'' +
                ", dia_evento='" + dia_evento + '\'' +
                ", mes_evento='" + mes_evento + '\'' +
                '}';
    }
}
