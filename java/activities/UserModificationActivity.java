package com.gestorproyectos_v01.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.gestorproyectos_v01.BD.BaseDatos;
import com.gestorproyectos_v01.R;
import com.gestorproyectos_v01.modelos.Usuario;

import io.realm.Realm;

public class UserModificationActivity extends AppCompatActivity {

    private Usuario u;

    private EditText usuario;
    private EditText password;

    private Realm con;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_modification);

        //recibo los datos del usuario logueado
        String usuario_log = "";
        SharedPreferences preferences = getSharedPreferences("MIS_PREFERENCIAS", MODE_PRIVATE);
        if (preferences != null){
            usuario_log = preferences.getString("id_log", null);
        }

        con = Realm.getDefaultInstance();

        usuario = findViewById(R.id.usuario);
        password = findViewById(R.id.password);

        con.beginTransaction();
        u = con.where(Usuario.class).equalTo("id", usuario_log).findFirst();
        con.commitTransaction();
        usuario.setText(u.getNombre());
        password.setText(u.getPassword());

        Button btn_guardar = findViewById(R.id.btn_guardar);
        btn_guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(usuario.getText().toString() == null || usuario.getText().toString().isEmpty()){
                    Toast.makeText(UserModificationActivity.this, "El campo usuario no puede estar vacío", Toast.LENGTH_SHORT).show();
                    finish();
                } else if (password.getText().toString() == null || password.getText().toString().isEmpty()) {
                    Toast.makeText(UserModificationActivity.this, "El campo contraseña no puede estar vacío", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    guardar();
                }
            }
        });
    }

    public void guardar(){
        con.beginTransaction();
        u.setNombre(usuario.getText().toString());
        u.setPassword(password.getText().toString());
        con.copyToRealmOrUpdate(u);
        con.commitTransaction();
        finish();
    }
}