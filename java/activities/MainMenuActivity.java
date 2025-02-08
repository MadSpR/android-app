package com.gestorproyectos_v01.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.gestorproyectos_v01.R;
import com.gestorproyectos_v01.fragments.FragmentMenuAjustes;
import com.gestorproyectos_v01.fragments.FragmentMenuCalendario;
import com.gestorproyectos_v01.fragments.FragmentMenuProyectos;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainMenuActivity extends AppCompatActivity {

    FragmentMenuProyectos fragmentMenuProyectos = new FragmentMenuProyectos();
    FragmentMenuCalendario fragmentMenuCalendario = new FragmentMenuCalendario();
    FragmentMenuAjustes fragmentMenuAjustes = new FragmentMenuAjustes();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        BottomNavigationView navigation = findViewById(R.id.bottom_navigation);
        navigation.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){
                    case R.id.fragmentProyectos:
                        loadFragment(fragmentMenuProyectos);
                        return true;

                    case R.id.fragmentCalendario:
                        loadFragment(fragmentMenuCalendario);
                        return true;

                    case R.id.fragmentAjustes:
                        loadFragment(fragmentMenuAjustes);
                        return true;
                }
                return false;
            }
        });

        loadFragment(fragmentMenuProyectos);

    }

    public void loadFragment(Fragment fragment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.commit();
    }

}