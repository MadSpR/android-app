package com.gestorproyectos_v01.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.gestorproyectos_v01.BD.BaseDatos;
import com.gestorproyectos_v01.R;
import com.gestorproyectos_v01.modelos.Usuario;

import io.realm.Realm;

public class LoginActivity extends AppCompatActivity {

    private TextView nombre;
    private TextView password;
    private Realm con;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        nombre = findViewById(R.id.editTextUsuario);
        password = findViewById(R.id.editTextPassword);

        con = BaseDatos.getInstance().conectar(getBaseContext());
        long numUsus = con.where(Usuario.class).count();

        if(numUsus == 0){
            //admin, admin
                Usuario u = new Usuario();
                u.setId("1");
                u.setNombre("admin");
                u.setPassword("admin");

                con.beginTransaction();
                con.copyToRealmOrUpdate(u);
                con.commitTransaction();
        }

        Button btn_login = findViewById(R.id.button_login);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });

        Button btn_registrar = findViewById(R.id.button_register);
        btn_registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registrarUsuario();
            }
        });


    }

    private void registrarUsuario() {
        Intent i = new Intent(this, UserRegisterActivity.class);
        startActivity(i);
    }

    public void login(){

        Usuario u = con.where(Usuario.class).equalTo("nombre", nombre.getText().toString()).findFirst();
        if( u == null){
            Toast.makeText(this, "Usuario no encontrado", Toast.LENGTH_SHORT).show();
        }else{
            if(u.getPassword().equals(password.getText().toString())){

                //paso los datos para usarlos en usermod
                SharedPreferences.Editor preferences = getSharedPreferences("MIS_PREFERENCIAS", MODE_PRIVATE).edit();
                preferences.putString("id_log", u.getId());
                preferences.apply();

                Intent intent = new Intent(this, MainMenuActivity.class);
                startActivity(intent);
                Toast.makeText(this, "Sesión iniciada", Toast.LENGTH_SHORT).show();
            } else{
                Toast.makeText(this, "Contraseña incorrecta", Toast.LENGTH_SHORT).show();
            }
        }

    }

}