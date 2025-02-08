package com.gestorproyectos_v01.modelos;

import javax.annotation.meta.Exclusive;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class Usuario extends RealmObject {

    @PrimaryKey
    private String id;
    @Required
    private String nombre;
    private String password;

    public Usuario (){

    }

    public Usuario(String id, String nombre, String password) {
        this.id = id;
        this.nombre = nombre;
        this.password = password;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
