package com.gestorproyectos_v01.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.gestorproyectos_v01.R;
import com.gestorproyectos_v01.modelos.Proyecto;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;

//ESTE ES EL ADAPTER DE LOS ITEMS DE LA VISTA DETALLE DE CADA PROYECTO
public class DetalleItemAdapter extends RecyclerView.Adapter<DetalleItemAdapterVH> {


    private Context context;
    private Realm realm;
    private Proyecto proyecto;
    private String idProy;
    private int item_type;

    private RealmList<String> items;
    private LayoutInflater inflater;

    public DetalleItemAdapter(Context context, RealmList<String> items, String idProy, int item_type){
        this.items = items;
        this.idProy = idProy;
        this.inflater = LayoutInflater.from(context);
        this.item_type = item_type;
        this.context = context;
    }

    @NonNull
    @Override
    public DetalleItemAdapterVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        switch (item_type){
            case 0:
                view = LayoutInflater.from(context)
                        .inflate(R.layout.item_checked_text_view, parent, false);

                return new DetalleItemAdapterVH(view, item_type).linkAdapter(this);
            case 1:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_enlace, parent, false);

                return new DetalleItemAdapterVH(view, item_type).linkAdapter(this);
            case 2:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_imagen, parent, false);

                return new DetalleItemAdapterVH(view, item_type).linkAdapter(this);
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull DetalleItemAdapterVH holder, int position) {

        switch (item_type){
            case 0:
                //TODO: con el debugger lPos=-1 con cada iteración
                holder.checkedText.setText(items.get(position));

                break;
            case 1:
                holder.editText.setText(items.get(position));
                break;
            case 2:
                String url_image = items.get(position);
                Glide.with(context).load(url_image).into(holder.imageView);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

}
class DetalleItemAdapterVH extends RecyclerView.ViewHolder{

    CheckBox checkedText;
    EditText editText;
    ImageView imageView;
    private DetalleItemAdapter detalleItemAdapter;
    public DetalleItemAdapterVH(@NonNull View itemView, int item_type) {
        super(itemView);
        switch (item_type){
            case 0:
                checkedText = itemView.findViewById(R.id.materiales_checkbox);
                checkedText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //checkedText.toggle();
                        if(checkedText.isChecked()){
                            checkedText.setChecked(false);
                        }else checkedText.setChecked(true);
                    }
                });

                break;
            case 1:
                editText = itemView.findViewById(R.id.etEnlace);
            break;
            case 2:
                imageView = itemView.findViewById(R.id.imagen);
                break;
        }

    }

    public DetalleItemAdapterVH linkAdapter(DetalleItemAdapter adapter){
        this.detalleItemAdapter = adapter;
        return this;
    }
}