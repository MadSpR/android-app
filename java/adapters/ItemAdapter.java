package com.gestorproyectos_v01.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemHolders> {


    private Context context;
    private Realm realm;
    private RealmResults<Proyecto> realmResults;
    private LayoutInflater inflater;
    private ItemClickListener mItemClickListener;
    List<String> itemsList;

    public ItemAdapter(Context context, List<String> itemsList, ItemClickListener mItemClickListener) {
        this.context = context;
        this.itemsList = itemsList;
        this.inflater = LayoutInflater.from(context);
        this.mItemClickListener = mItemClickListener;
    }

    @NonNull
    @Override
    public ItemHolders onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View root = inflater.inflate(R.layout.item_general, parent, false);
        ItemHolders holders = new ItemHolders(root);

        return holders;
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolders holder, int position) {
        String itemName = itemsList.get(position).toString();
        holder.getItem(itemName, position);
        holder.itemView.setOnClickListener(view -> {
            mItemClickListener.onItemClick(itemsList.get(position));
        });
    }

    @Override
    public int getItemCount() {
        return itemsList.size();
    }


    public class ItemHolders extends RecyclerView.ViewHolder{

        private int position;
        private TextView item_name;

        public ItemHolders(@NonNull View itemView) {
            super(itemView);
            item_name = itemView.findViewById(R.id.tv_item_name);
        }

        public void getItem(String itemName, int position){
            this.position = position;

            item_name.setText(itemName);
        }

    }

    public String getItemAt(int position){
        return itemsList.get(position);
    }

    public interface ItemClickListener{
        void onItemClick(Object item);
    }
}
