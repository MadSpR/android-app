package com.gestorproyectos_v01.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gestorproyectos_v01.R;
import com.gestorproyectos_v01.modelos.Eventos;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import io.realm.RealmResults;

public class CalendarioAdapter extends RecyclerView.Adapter<CalendarioAdapter.CalendarHolders>{

    private RealmResults<Eventos> realmResults;
    private LayoutInflater inflater;
    private String dia_selecc, mes_selecc;

    public CalendarioAdapter (Context context, RealmResults<Eventos> realmResults, String dia_selecc, String mes_selecc){
        this.realmResults = realmResults;
        this.dia_selecc = dia_selecc;
        this.mes_selecc = mes_selecc;
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public CalendarHolders onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_evento, parent, false);
        CalendarHolders holders = new CalendarHolders(view, dia_selecc, mes_selecc);

        return holders;
    }

    @Override
    public void onBindViewHolder(@NonNull CalendarHolders holder, int position) {
        Eventos evento = realmResults.get(position);
        holder.getEventos(evento, position);

    }

    @Override
    public int getItemCount() {
        return realmResults.size();
    }

    public Eventos getEventoAt(int position) {
        return realmResults.get(position);
    }

    public class CalendarHolders extends RecyclerView.ViewHolder{

        private int position;
        private TextView dia_evento, mes_evento, tv_evento;
        String dia_selecc, mes_selecc;

        public CalendarHolders(@NonNull View itemView, String dia_selecc, String mes_selecc) {
            super(itemView);
            dia_evento = itemView.findViewById(R.id.dia_evento);
            mes_evento = itemView.findViewById(R.id.mes_evento);
            tv_evento = itemView.findViewById(R.id.tv_evento);

            this.dia_selecc = dia_selecc;
            this.mes_selecc = mes_selecc;
        }

        public void getEventos(Eventos evento, int position){
            this.position = position;

            String mes = evento.getMes_evento();
            mes = nombreMes(mes);

            dia_evento.setText(evento.getDia_evento());
            mes_evento.setText(mes);
            tv_evento.setText(evento.getNombre_evento());
        }

    }

    private String nombreMes(String mes){

        switch (mes){
            case "0":
                mes = "Enero";
                break;
            case "1":
                mes = "Febrero";
                break;
            case "2":
                mes = "Marzo";
                break;
            case "3":
                mes = "Abril";
                break;
            case "4":
                mes = "Mayo";
                break;
            case "5":
                mes = "Junio";
                break;
            case "6":
                mes = "Julio";
                break;
            case "7":
                mes = "Agosto";
                break;
            case "8":
                mes = "Septiembre";
                break;
            case "9":
                mes = "Octubre";
                break;
            case "10":
                mes = "Noviembre";
                break;
            case "11":
                mes = "Diciembre";
                break;
        }
        return mes;
    }
}
