package com.gestorproyectos_v01.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.gestorproyectos_v01.BD.BaseDatos;
import com.gestorproyectos_v01.R;
import com.gestorproyectos_v01.modelos.Usuario;

import java.util.Random;
import java.util.UUID;

import io.realm.Realm;
import io.realm.RealmAsyncTask;
import io.realm.RealmQuery;

public class UserRegisterActivity extends AppCompatActivity {

    private EditText usuario;
    private EditText password;
    private EditText password2;

    private RealmAsyncTask realmTask;
    private Realm con;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_register);

        usuario = findViewById(R.id.usuario_regis);
        password = findViewById(R.id.password_regis);
        password2 = findViewById(R.id.password_regis2);

        con = Realm.getDefaultInstance();

        Button btn_guardar = findViewById(R.id.btn_guardar_regis);
        btn_guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                guardar();
            }
        });
    }

    private void guardar() {

        if(!usuario.getText().toString().isEmpty()){
            if(password.getText().toString().equals(password2.getText().toString())){
                try{

                    String id = UUID.randomUUID().toString();
                    String nombre = usuario.getText().toString();
                    String pass = password.getText().toString();

                    Usuario existeUsu = con.where(Usuario.class).equalTo("nombre", nombre).findFirst();
                    String nombreUsuQuery = "";
                    if (existeUsu == null){
                        nombreUsuQuery = UUID.randomUUID().toString();
                    }else {
                        nombreUsuQuery = existeUsu.getNombre();
                    }

                    if (nombre.equals(nombreUsuQuery)){
                        Toast.makeText(this, "El usuario ya existe", Toast.LENGTH_SHORT).show();
                        finish();
                    }else {

                        realmTask = con.executeTransactionAsync(
                                new Realm.Transaction() {
                                    @Override
                                    public void execute(Realm realm) {
                                        Usuario usu = realm.createObject(Usuario.class, id);
                                        usu.setNombre(nombre);
                                        usu.setPassword(pass);
                                    }
                                },
                                new Realm.Transaction.OnSuccess() {
                                    @Override
                                    public void onSuccess() {
                                        Toast.makeText(UserRegisterActivity.this, "Usuario creado", Toast.LENGTH_SHORT).show();
                                    }
                                }, new Realm.Transaction.OnError() {
                                    @Override
                                    public void onError(Throwable error) {
                                        Toast.makeText(UserRegisterActivity.this, "Ha habido un error", Toast.LENGTH_LONG).show();
                                    }
                                });
                    }

                } finally {
                    finish();
                }
            } else{
                Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_LONG).show();
            }
        } else{
            Toast.makeText(this, "Introduce un nombre de usuario", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(realmTask != null && realmTask.isCancelled()){
            realmTask.cancel();
        }
    }
}