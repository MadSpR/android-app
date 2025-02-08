package com.gestorproyectos_v01.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.gestorproyectos_v01.R;
import com.gestorproyectos_v01.modelos.Proyecto;

import java.util.UUID;

import io.realm.Realm;
import io.realm.RealmAsyncTask;

public class AddProyectoActivity extends AppCompatActivity {

    private EditText proy_nombre, proy_descripcion;
    private Realm myRealm;
    private RealmAsyncTask realmAsyncTask;
    private Button btn_add_proyecto;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_proyecto);

        proy_nombre = findViewById(R.id.proy_name_edit_text);
        proy_descripcion = findViewById(R.id.proy_descripcion_edit_text);
        btn_add_proyecto = findViewById(R.id.btn_add_proy);


        myRealm = Realm.getDefaultInstance();

    }

    private void insertRecords(){
        String id = UUID.randomUUID().toString();
        String nombre = proy_nombre.getText().toString();
        String descripcion = proy_descripcion.getText().toString();

        if(nombre.isEmpty()){
            Toast.makeText(AddProyectoActivity.this, "Introduce un nombre de proyecto", Toast.LENGTH_LONG).show();
            finish();
        } else{
            realmAsyncTask = myRealm.executeTransactionAsync(
                    new Realm.Transaction() {

                        @Override
                        public void execute(Realm realm) {
                            Proyecto proyecto = realm.createObject(Proyecto.class, id);
                            proyecto.setNombre_proyecto(nombre);
                            proyecto.setDescripcion_proyecto(descripcion);
                            proyecto.setPendiente(true);

                            //recibo los datos del usuario logueado
                            String usuario_log = "";
                            SharedPreferences preferences = getSharedPreferences("MIS_PREFERENCIAS", MODE_PRIVATE);
                            if (preferences != null){
                                usuario_log = preferences.getString("id_log", null);
                            }
                            proyecto.setUsu_logueado(usuario_log);
                        }
                    },
                    new Realm.Transaction.OnSuccess() {
                        @Override
                        public void onSuccess() {
                            Toast.makeText(AddProyectoActivity.this, "Proyecto creado", Toast.LENGTH_LONG).show();
                        }
                    },
                    new Realm.Transaction.OnError() {
                        @Override
                        public void onError(Throwable error) {
                            Toast.makeText(AddProyectoActivity.this, "Error", Toast.LENGTH_LONG).show();
                        }
                    }
            );
        }

    }

    public void addProyectos(View view){
        insertRecords();
        finish();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(realmAsyncTask != null && realmAsyncTask.isCancelled()){
            realmAsyncTask.cancel();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        myRealm.close();
    }
}