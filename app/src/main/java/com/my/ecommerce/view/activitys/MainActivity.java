package com.my.ecommerce.view.activitys;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.my.ecommerce.R;


public class MainActivity extends AppCompatActivity {
   private BottomNavigationView navBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        navBar=findViewById(R.id.bottom_navigation);
        NavController controller = Navigation.findNavController(this, R.id.NevHostFragment);
        setupBottomNavigationMenu(controller);
    }

    private void setupBottomNavigationMenu(NavController navController) {

        NavigationUI.setupWithNavController(navBar,navController);


    }
}