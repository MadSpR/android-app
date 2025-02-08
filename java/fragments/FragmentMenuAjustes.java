package com.gestorproyectos_v01.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.gestorproyectos_v01.R;
import com.gestorproyectos_v01.activities.UserModificationActivity;
import com.gestorproyectos_v01.activities.UserRegisterActivity;


public class FragmentMenuAjustes extends Fragment implements AdapterView.OnItemClickListener{

    private ListView listView;
    private SearchView searchView;
    ArrayAdapter<String> adapter;
    String[] data = {"Editar usuario"};


    public FragmentMenuAjustes() {
        // Required empty public constructor
    }

    public static FragmentMenuAjustes newInstance(String param1, String param2) {
        FragmentMenuAjustes fragment = new FragmentMenuAjustes();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_menu_ajustes, container, false);
        listView = (ListView) view.findViewById(R.id.listView);
        adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, data);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);

        return view;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        switch (position){
            case 0:
                Intent in = new Intent(getActivity(), UserModificationActivity.class);
                startActivity(in);
                break;
        }
    }
}