package com.gestorproyectos_v01.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.CalendarContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.gestorproyectos_v01.R;
import com.gestorproyectos_v01.activities.AddProyectoActivity;
import com.gestorproyectos_v01.adapters.CalendarioAdapter;
import com.gestorproyectos_v01.modelos.Eventos;
import com.gestorproyectos_v01.modelos.Proyecto;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import io.realm.Realm;
import io.realm.RealmAsyncTask;
import io.realm.RealmResults;

public class FragmentMenuCalendario extends Fragment {

    private Realm con;
    private RealmAsyncTask realmAsyncTask;
    private String usuario_log;
    private CalendarView calendarView;
    private Calendar calendar;
    private FloatingActionButton btn_add_event;
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private Date fechaSeleccionada;
    private String nombre_evento;
    private String dia_selecc = "00", mes_selecc = "EVENTO";
    CalendarioAdapter calendarioAdapter;
    List<Eventos> list;

    public FragmentMenuCalendario() {

    }
    public static FragmentMenuCalendario newInstance(String param1, String param2) {
        FragmentMenuCalendario fragment = new FragmentMenuCalendario();
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
        View root = inflater.inflate(R.layout.fragment_menu_calendario, container, false);

        calendarView = root.findViewById(R.id.calendar_view);
        calendar = Calendar.getInstance();
        btn_add_event = root.findViewById(R.id.btn_add_evento);

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int day) {
                setDate(day, month, year);
                fechaSeleccionada = calendar.getTime();
                dia_selecc = String.valueOf(day);
                mes_selecc = String.valueOf(month);
            }
        });

        btn_add_event.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buildAndShowInputDialogEvento();
            }
        });

        return root;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getAllEventos(view);
    }

    public void getAllEventos(View view){
        RealmResults<Eventos> results = con.where(Eventos.class).equalTo("usu_logueado", usuario_log).findAll();

        list = (List<Eventos>) results;

        recyclerView = view.findViewById(R.id.lista_eventos);
        layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        calendarioAdapter = new CalendarioAdapter(getContext(), results, dia_selecc, mes_selecc);

        recyclerView.setAdapter(calendarioAdapter);
        calendarioAdapter.notifyDataSetChanged();


        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    public void getDate(){
        long date = calendarView.getDate();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yy", Locale.getDefault());
        calendar.setTimeInMillis(date);

        String selectedDate = simpleDateFormat.format(calendar.getTime());
        Toast.makeText(getContext(), selectedDate, Toast.LENGTH_SHORT).show();

        return;
    }

    public void setDate(int day, int month, int year){
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, day);

        long milli = calendar.getTimeInMillis();
        calendarView.setDate(milli);

    }

    private void buildAndShowInputDialogEvento(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Añadir un evento");

        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        View dialogView = layoutInflater.inflate(R.layout.add_evento_dialog_view, null);
        EditText input_evento = (EditText) dialogView.findViewById(R.id.input_dialog_evento);

        builder.setView(dialogView);
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                nombre_evento = input_evento.getText().toString();
                if(nombre_evento == null || nombre_evento.isEmpty()){
                    Toast.makeText(getContext(), "El evento tiene que tener un nombre", Toast.LENGTH_SHORT).show();
                    dialogInterface.cancel();
                }else {
                    insertEvento(nombre_evento, dia_selecc, mes_selecc);
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

    private void insertEvento(String nombre_evento, String dia, String mes){
        String id = UUID.randomUUID().toString();

        realmAsyncTask = con.executeTransactionAsync(
                new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        Eventos evento = realm.createObject(Eventos.class, id);
                        evento.setNombre_evento(nombre_evento);
                        evento.setUsu_logueado(usuario_log);
                        evento.setFecha_fin(fechaSeleccionada);
                        evento.setDia_evento(dia);
                        evento.setMes_evento(mes);
                    }
                },

                new Realm.Transaction.OnSuccess() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(getContext(), "Evento creado", Toast.LENGTH_LONG).show();
                    }
                },

                new Realm.Transaction.OnError() {
                    @Override
                    public void onError(Throwable error) {
                        Toast.makeText(getContext(), "Error al crear", Toast.LENGTH_LONG).show();
                    }
                }

        );
    }

    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            int position = viewHolder.getAbsoluteAdapterPosition();
            Eventos evento = calendarioAdapter.getEventoAt(position);
            con.beginTransaction();
            evento.deleteFromRealm();
            con.commitTransaction();
            calendarioAdapter.notifyItemRemoved(position);
        }
    };

}