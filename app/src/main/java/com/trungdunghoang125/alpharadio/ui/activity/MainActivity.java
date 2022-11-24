package com.trungdunghoang125.alpharadio.ui.activity;

import static com.trungdunghoang125.alpharadio.service.RadioPlayerService.RADIO_LAST_PLAYED;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.trungdunghoang125.alpharadio.R;
import com.trungdunghoang125.alpharadio.databinding.ActivityMainBinding;
import com.trungdunghoang125.alpharadio.ui.fragment.FavoriteFragment;
import com.trungdunghoang125.alpharadio.ui.fragment.HomeFragment;
import com.trungdunghoang125.alpharadio.ui.fragment.NearYouFragment;
import com.trungdunghoang125.alpharadio.ui.fragment.SearchFragment;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private BottomNavigationView navigationView;
    public static boolean SHOW_MINI_PLAYER = false;

    private FragmentContainerView fragmentMiniPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        fragmentMiniPlayer = binding.fragmentContainerViewMiniPlayer;
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

    @Override
    protected void onResume() {
        super.onResume();
//        SharedPreferences preferences = getSharedPreferences(RADIO_LAST_PLAYED, MODE_PRIVATE);
//        String value = preferences.getString("Station", null);
//        if (value != null) {
//            fragmentMiniPlayer.setVisibility(View.VISIBLE);
//            SHOW_MINI_PLAYER = true;
//        } else {
//            fragmentMiniPlayer.setVisibility(View.GONE);
//            SHOW_MINI_PLAYER = false;
//        }
    }
}