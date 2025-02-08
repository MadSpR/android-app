package com.gestorproyectos_v01.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.gestorproyectos_v01.R;
import com.gestorproyectos_v01.adapters.DetalleItemAdapter;
import com.gestorproyectos_v01.modelos.Proyecto;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmQuery;

public class FragmentDetalleItem extends Fragment {

    private String idProy;
    private String item_name;
    private Proyecto proyecto;
    private Realm con;
    private RealmList<String> listaTemp;

    //para manejar el recycler view
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private DetalleItemAdapter detalleItemAdapter;

    private String addedItem;

    public FragmentDetalleItem() {
        // Required empty public constructor
    }
public static FragmentDetalleItem newInstance() {
        FragmentDetalleItem fragment = new FragmentDetalleItem();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        con = Realm.getDefaultInstance();

        //recuperar datos de otro fragmento
        Bundle datosRecuperados = getArguments();
        if(datosRecuperados == null){
            System.out.println("No hay datos para recuperar");
            return;
        }

        idProy = datosRecuperados.getString("id_proyecto");

        item_name = datosRecuperados.getString("item_name");

        //buscamos el proyecto con el id correspondiente al pulsado en la lista de proyectos
        RealmQuery<Proyecto> query = con.where(Proyecto.class).equalTo("id", idProy);
        proyecto = query.findFirst();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root;
        switch (item_name){
            case "Descripción":
                root = inflater.inflate(R.layout.item_detalle_descripcion, container, false);
                Button cancelar = root.findViewById(R.id.btn_cancelar_descripcion);
                Button guardar = root.findViewById(R.id.btn_guardar_descripcion);
                EditText edit_desc = root.findViewById(R.id.campo_descripcion);

                String descrip_inicial = proyecto.getDescripcion_proyecto();
                edit_desc.setText(descrip_inicial);

                cancelar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        getActivity().onBackPressed();
                    }
                });

                guardar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        con.beginTransaction();
                        proyecto.setDescripcion_proyecto(edit_desc.getText().toString());
                        con.insertOrUpdate(proyecto);
                        con.commitTransaction();
                        getActivity().onBackPressed();
                    }
                });

                break;
            case "Materiales":
                root = inflater.inflate(R.layout.item_detalle_materiales, container, false);
                //aqui añadir botones y funcionalidades de los botones
                Button añadir = root.findViewById(R.id.btn_add_material);
                Button cancelar_materiales = root.findViewById(R.id.btn_cancelar_materiales);


                añadir.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        buildAndShowInputDialogCheckItems();
                    }
                });

                cancelar_materiales.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        getActivity().onBackPressed();
                    }
                });

                break;
            case "Planos":
                root = inflater.inflate(R.layout.item_detalle_planos, container, false);
                Button añadir_plano = root.findViewById(R.id.btn_add_planos);
                Button volver_plano = root.findViewById(R.id.btn_volver_planos);

                volver_plano.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        getActivity().onBackPressed();
                    }
                });

                añadir_plano.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        buildAndShowInputDialogImagenes();
                    }
                });

                break;
            case "Enlaces":
                root = inflater.inflate(R.layout.item_detalle_enlaces, container, false);

                Button añadir_enlaces = root.findViewById(R.id.btn_add_enlaces);
                Button cancelar_enlaces = root.findViewById(R.id.btn_cancelar_enlaces);

                añadir_enlaces.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        buildAndShowInputDialogEnlaces();
                    }
                });

                cancelar_enlaces.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        getActivity().onBackPressed();
                    }
                });
                break;
            default:
                root = inflater.inflate(R.layout.fragment_detalle_item, container, false);
                break;
        }

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        switch (item_name){
            case "Materiales":
                getListaMateriales(view);
                break;
            case "Planos":
                getListaImagenes(view);
                break;
            case "Enlaces":
                getListaEnlaces(view);
                break;
        }
    }

    private void getListaMateriales(View view) {

        listaTemp = proyecto.getMateriales();

        recyclerView = view.findViewById(R.id.checkedText_recyclerview);
        layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        detalleItemAdapter = new DetalleItemAdapter(getContext(), listaTemp, idProy, 0);
        recyclerView.setAdapter(detalleItemAdapter);
        detalleItemAdapter.notifyDataSetChanged();

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

    }

    private void getListaEnlaces(View view) {
        listaTemp = proyecto.getEnlaces();

        recyclerView = view.findViewById(R.id.lista_enlaces);
        layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        detalleItemAdapter = new DetalleItemAdapter(getContext(), listaTemp, idProy, 1);
        recyclerView.setAdapter(detalleItemAdapter);
        detalleItemAdapter.notifyDataSetChanged();

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    private void getListaImagenes(View view) {

        listaTemp = proyecto.getUrl_imagenes();

        recyclerView = view.findViewById(R.id.images_recyclerview);
        layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        detalleItemAdapter = new DetalleItemAdapter(getContext(), listaTemp, idProy, 2);
        recyclerView.setAdapter(detalleItemAdapter);
        detalleItemAdapter.notifyDataSetChanged();

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

    }

    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            int position = viewHolder.getAbsoluteAdapterPosition();
            con.beginTransaction();
            listaTemp.remove(position);
            con.commitTransaction();
            detalleItemAdapter.notifyItemRemoved(position);

        }
    };

    private void buildAndShowInputDialogCheckItems(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Añadir un material");

        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        View dialogView = layoutInflater.inflate(R.layout.add_material_dialog_view, null);
        EditText input_material = dialogView.findViewById(R.id.input_dialog_material);

        builder.setView(dialogView);
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                addedItem = input_material.getText().toString();
                if(addedItem == null || addedItem.isEmpty()){
                    Toast.makeText(getContext(), "El campo no puede estar vacío", Toast.LENGTH_SHORT).show();
                    dialogInterface.cancel();
                } else{
                    con.beginTransaction();
                    listaTemp = proyecto.getMateriales();
                    listaTemp.add(addedItem);
                    con.insertOrUpdate(proyecto);
                    con.commitTransaction();
                    detalleItemAdapter.notifyItemInserted(listaTemp.size());
                }
            }
        });

        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });

        builder.show();
    }

    private void buildAndShowInputDialogEnlaces(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Añadir un enlace");

        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        View dialogView = layoutInflater.inflate(R.layout.add_enlace_dialog_view, null);
        EditText input_enlace = (EditText) dialogView.findViewById(R.id.input_dialog_enlaces);

        builder.setView(dialogView);
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                addedItem = input_enlace.getText().toString();

                if(addedItem == null || addedItem.isEmpty()){
                    Toast.makeText(getContext(), "El campo no puede estar vacío", Toast.LENGTH_SHORT).show();
                    dialogInterface.cancel();
                } else{
                    con.beginTransaction();
                    listaTemp = proyecto.getEnlaces();
                    listaTemp.add(addedItem);
                    con.insertOrUpdate(proyecto);
                    con.commitTransaction();
                }

            }
        });

        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });

        builder.show();

    }

    private void buildAndShowInputDialogImagenes(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Añadir una imagen");

        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        View dialogView = layoutInflater.inflate(R.layout.add_imagen_dialog_view, null);
        EditText input_imagen = (EditText) dialogView.findViewById(R.id.input_dialog_imagen);

        builder.setView(dialogView);
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                addedItem = input_imagen.getText().toString();

                if(addedItem == null || addedItem.isEmpty()){
                    Toast.makeText(getContext(), "El campo no puede estar vacío", Toast.LENGTH_SHORT).show();
                    dialogInterface.cancel();
                } else{
                    con.beginTransaction();
                    listaTemp = proyecto.getUrl_imagenes();
                    listaTemp.add(addedItem);
                    con.insertOrUpdate(proyecto);
                    con.commitTransaction();
                }
            }
        });

        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });

        builder.show();

    }

    @Override
    public void onResume() {
        super.onResume();
        if(detalleItemAdapter != null){
            detalleItemAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        con.close();
    }
}