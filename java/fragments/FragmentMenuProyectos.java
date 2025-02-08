package com.gestorproyectos_v01.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gestorproyectos_v01.R;
import com.gestorproyectos_v01.activities.AddProyectoActivity;
import com.gestorproyectos_v01.adapters.ProyectoAdapter;
import com.gestorproyectos_v01.modelos.Proyecto;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class FragmentMenuProyectos extends Fragment {

    private Realm con;

    //para manejar el recycler view
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    private ProyectoAdapter proyectoAdapter;
    FloatingActionButton botonAdd;

    Button mostrarTodo, mostrarPendientes, mostrarCompletados;
    boolean todo, pendientes, completados;

    private String usuario_log;

    public FragmentMenuProyectos() {
        // Required empty public constructor
    }

    public static FragmentMenuProyectos newInstance() {
        FragmentMenuProyectos fragment = new FragmentMenuProyectos();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        con = Realm.getDefaultInstance();

        //recibo los datos del usuario logueado
        SharedPreferences preferences = getActivity().getSharedPreferences("MIS_PREFERENCIAS", getActivity().MODE_PRIVATE);
        if (preferences != null){
            usuario_log = preferences.getString("id_log", null);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_menu_proyectos, container, false);

        mostrarTodo = root.findViewById(R.id.btn_menu_todo);
        mostrarCompletados = root.findViewById(R.id.btn_menu_terminado);
        mostrarPendientes = root.findViewById(R.id.btn_menu_pendientes);
        botonAdd = root.findViewById(R.id.btn_nuevo);

        botonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AddProyectoActivity.class);
                startActivity(intent);
            }
        });

        //botones filtros
        mostrarTodo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                todo = true;
                pendientes = false;
                completados = false;
            }
        });

        mostrarPendientes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                todo = false;
                pendientes = true;
                completados = false;
                proyectoAdapter.notifyDataSetChanged();
            }
        });

        mostrarCompletados.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                todo = false;
                pendientes = false;
                completados = true;
            }
        });

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getAllProyectos(view);
    }

    @Override
    public void onResume() {
        super.onResume();
        if(proyectoAdapter != null){
            proyectoAdapter.notifyDataSetChanged();
        }
    }

    private void getAllProyectos(View view){
        RealmResults<Proyecto> results;
        //switch con si los btn filtro estan activos para que resuls almacene unos valores u otros
        if(todo){
            results = con.where(Proyecto.class).equalTo("usu_logueado", usuario_log).findAll();
        }else if(pendientes){
            results = con.where(Proyecto.class).equalTo("pendiente", true).equalTo("usu_logueado", usuario_log).findAll();
        } else if (completados){
            results = con.where(Proyecto.class).equalTo("pendiente", false).equalTo("usu_logueado", usuario_log).findAll();
        } else {
            results = con.where(Proyecto.class).equalTo("usu_logueado", usuario_log).findAll();
        }
        recyclerView = view.findViewById(R.id.menu_recyclerview);
        layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        proyectoAdapter = new ProyectoAdapter(getContext(), con, results, new ProyectoAdapter.ItemClickListener() {
            @Override
            public void onItemClick(Proyecto proyecto) {
                //enviar el objeto proyecto pulsado al fragment detalle
                Bundle datosAEnviar = new Bundle();
                datosAEnviar.putString("id_proyecto", proyecto.getId());
                getParentFragmentManager().setFragmentResult("key", datosAEnviar);
                //cambiar de fragment
                Fragment fragment = FragmentDetalleProyecto.newInstance();
                fragment.setArguments(datosAEnviar);
                fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.frame_container, fragment, null);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        recyclerView.setAdapter(proyectoAdapter);
        proyectoAdapter.notifyDataSetChanged();

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    //para borrar un proyecto
    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            //drag rows
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            int position = viewHolder.getAdapterPosition();
            Proyecto proyecto = proyectoAdapter.getProyectoAt(position);
            switch(direction){
                case ItemTouchHelper.LEFT:
                    try {
                        con.beginTransaction();
                        proyecto.deleteFromRealm();
                        con.commitTransaction();
                        proyectoAdapter.notifyItemRemoved(position);

                    } catch (IllegalStateException ex){
                        System.out.println(ex.getMessage());
                    }
                    break;
                    case ItemTouchHelper.RIGHT:
                        con.beginTransaction();
                        if(proyecto.isPendiente()){
                            proyecto.setPendiente(false);
                        }else proyecto.setPendiente(true);

                        con.commitTransaction();
                        proyectoAdapter.notifyItemChanged(position);
                        break;
            }
        }

        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

            new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    .addSwipeLeftBackgroundColor(ContextCompat.getColor(getContext(), R.color.dark_red))
                    .addSwipeLeftActionIcon(R.drawable.baseline_delete_white_24)
                    .addSwipeRightBackgroundColor(ContextCompat.getColor(getContext(), R.color.dark_green))
                    .addSwipeRightActionIcon(R.drawable.baseline_check_white_24)
                    .create()
                    .decorate();

            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        con.close();
    }
}