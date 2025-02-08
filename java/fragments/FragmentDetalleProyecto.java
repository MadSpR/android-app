package com.gestorproyectos_v01.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.gestorproyectos_v01.R;
import com.gestorproyectos_v01.adapters.ItemAdapter;
import com.gestorproyectos_v01.adapters.ProyectoAdapter;
import com.gestorproyectos_v01.modelos.Proyecto;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmQuery;

public class FragmentDetalleProyecto extends Fragment {

    private Realm con;
    private Proyecto proyecto;
    private String idProy;
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private FragmentTransaction fragmentTransaction;
    private ItemAdapter itemAdapter;
    ArrayList<String> itemsList;
    private TextView tv_nombre_proy;

    //tutorial multiple recyclerview in one: https://www.youtube.com/watch?v=kQsn1r2fzfw

    public FragmentDetalleProyecto() {
        // Required empty public constructor
    }

    public static FragmentDetalleProyecto newInstance() {
        FragmentDetalleProyecto fragment = new FragmentDetalleProyecto();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        con = Realm.getDefaultInstance();
        this.itemsList = instanceList();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_detalle_proyecto, container, false);
        tv_nombre_proy = root.findViewById(R.id.title_proy);


        return root;
    }

    public ArrayList<String> instanceList(){
        ArrayList<String> lista = new ArrayList<>();
        lista.add(0, "Descripción" );
        lista.add(1, "Materiales");
        lista.add(2, "Planos");
        lista.add(3, "Enlaces");
        return lista;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //recuperar datos de otro fragmento
        Bundle datosRecuperados = getArguments();
        if(datosRecuperados == null){
            System.out.println("No hay datos para recuperar");
            return;
        }

        String id_proyecto = datosRecuperados.getString("id_proyecto");
        idProy = id_proyecto;

        getDatosProyecto(view);
    }

    private void getDatosProyecto(View view) {

        //buscamos el proyecto con el id correspondiente al pulsado en la lista de proyectos
        RealmQuery<Proyecto> query = con.where(Proyecto.class).equalTo("id", idProy);
        proyecto = query.findFirst();
        TextView prueba = view.findViewById(R.id.title_proy);
        prueba.setText(proyecto.getNombre_proyecto());

        recyclerView = view.findViewById(R.id.lista_items);
        layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        itemAdapter = new ItemAdapter(getContext(), itemsList, new ItemAdapter.ItemClickListener() {
            @Override
            public void onItemClick(Object item) {
                Bundle datosAEnviar2 = new Bundle();
                datosAEnviar2.putString("id_proyecto", proyecto.getId());
                datosAEnviar2.putString("item_name", item.toString());
                getParentFragmentManager().setFragmentResult("key2", datosAEnviar2);

                //cambiar de fragment a la vista detalle de los items
                Fragment fragment = FragmentDetalleItem.newInstance();
                fragment.setArguments(datosAEnviar2);
                fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.frame_container, fragment, null);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        recyclerView.setAdapter(itemAdapter);

    }

    @Override
    public void onResume() {
        super.onResume();
        if(itemAdapter != null){
            itemAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        con.close();
    }
}