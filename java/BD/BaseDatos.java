package com.gestorproyectos_v01.BD;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.preference.PreferenceManager;

import com.gestorproyectos_v01.modelos.Eventos;
import com.gestorproyectos_v01.modelos.Proyecto;
import com.gestorproyectos_v01.modelos.Usuario;

import io.realm.BuildConfig;
import io.realm.Realm;
import io.realm.RealmConfiguration;

public class BaseDatos extends Application{

    private static BaseDatos instance = new BaseDatos();
    public static BaseDatos getInstance() { return instance;}
    private Realm con;

    private Class[] models = {Usuario.class, Proyecto.class, Eventos.class};

    //devuelve un objeto realm con la conexión
    public Realm conectar(Context context){
        if(con == null){
            Realm.init(context);
            String nombre = "bbdd_proyectos_v01";

            RealmConfiguration config = new RealmConfiguration.Builder().name(nombre).build();
            Realm.setDefaultConfiguration(config);

            con = Realm.getInstance(config);
        }
        return con;
    }
}