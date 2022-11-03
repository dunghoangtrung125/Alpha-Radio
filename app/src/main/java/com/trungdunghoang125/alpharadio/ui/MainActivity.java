package com.trungdunghoang125.alpharadio.ui;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.trungdunghoang125.alpharadio.R;
import com.trungdunghoang125.alpharadio.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private BottomNavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        navigationView = binding.bottomNav;
        navigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.item_home_nav:
                        HomeFragment homeFragment = new HomeFragment();
                        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.fragment_container_view, homeFragment);
                        fragmentTransaction.commit();
                        return true;

                    case R.id.item_near_you_nav:
                        NearYouFragment nearYouFragment = new NearYouFragment();
                        FragmentTransaction fragmentTransaction1 = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction1.replace(R.id.fragment_container_view, nearYouFragment);
                        fragmentTransaction1.commit();
                        return true;

                    case R.id.item_search_nav:
                        SearchFragment searchFragment = new SearchFragment();
                        FragmentTransaction fragmentTransaction2 = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction2.replace(R.id.fragment_container_view, searchFragment);
                        fragmentTransaction2.commit();
                        return true;

                    case R.id.item_favorite_nav:
                        FavoriteFragment favoriteFragment = new FavoriteFragment();
                        FragmentTransaction fragmentTransaction3 = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction3.replace(R.id.fragment_container_view, favoriteFragment);
                        fragmentTransaction3.commit();
                        return true;
                }
                return false;
            }
        });
    }
}