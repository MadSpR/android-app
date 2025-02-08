package com.gestorproyectos_v01.adapters;

import android.content.Context;
import android.telecom.Call;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.gestorproyectos_v01.R;
import com.gestorproyectos_v01.modelos.Proyecto;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

public class ProyectoAdapter extends  RecyclerView.Adapter<ProyectoAdapter.Holders>{

    private Context context;
    private Realm realm;
    private RealmResults<Proyecto> realmResults;
    private LayoutInflater inflater;
    private ItemClickListener mItemClickListener;

    TextView txt_pendiente;

    public ProyectoAdapter(Context context, Realm realm, RealmResults<Proyecto> realmResults, ItemClickListener itemClickListener) {
        this.context = context;
        this.realm = realm;
        this.realmResults = realmResults;
        this.inflater = LayoutInflater.from(context);
        this.mItemClickListener = itemClickListener;
    }

    @Override
    public int getItemViewType(int position) {
        //https://www.youtube.com/watch?v=G2p7l3K1RLo recycler con multiple layouts || https://www.youtube.com/watch?v=_S3ZVL7Pmvo java
        //esto vale para listas no dinámicas pero si quiero añadir un determinado item??
        return super.getItemViewType(position);
    }

    @NonNull
    @Override
    public Holders onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.item_proyecto, parent, false);
        Holders holders = new Holders(view);

        return holders;
    }

    @Override
    public void onBindViewHolder(@NonNull Holders holder, int position) {

        Proyecto proyecto = realmResults.get(position);
        holder.getProyectos(proyecto, position);
        holder.itemView.setOnClickListener( view -> {
            mItemClickListener.onItemClick(realmResults.get(position));
        });
    }

    @Override
    public int getItemCount() {
        return realmResults.size();
    }

    public Proyecto getProyectoAt(int position){
        return realmResults.get(position);
    }

    public class Holders extends RecyclerView.ViewHolder{

        private int position;
        private TextView proy_name, proy_descrip;

        public Holders(@NonNull View itemView) {
            super(itemView);
            proy_name = itemView.findViewById(R.id.txt_nombre_proy);
            proy_descrip = itemView.findViewById(R.id.txt_descripcion_proy);
            txt_pendiente = itemView.findViewById(R.id.txt_is_pendiente);
        }

        public void getProyectos(Proyecto proyecto, int position){

            this.position = position;
            String nombre = proyecto.getNombre_proyecto();
            String descripcion = proyecto.getDescripcion_proyecto();

            proy_name.setText(nombre);
            proy_descrip.setText(descripcion);

            //para modificar si el proyecto está pendiente o no
            if(proyecto.isPendiente()){
                txt_pendiente.setText("Pendiente");
                txt_pendiente.setTextColor(ContextCompat.getColor(context, R.color.warning));
            } else {
                txt_pendiente.setText("Completado");
                txt_pendiente.setTextColor(ContextCompat.getColor(context, R.color.dark_green));
            }
        }
    }

    public interface ItemClickListener{
        void onItemClick(Proyecto proyecto);
    }

}
