package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomnavigation.LabelVisibilityMode;
import com.google.android.material.navigation.NavigationBarView;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    Button button, button2, button3, button4;

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




        bottomNavigationView = findViewById(R.id.bottom_navigation);
        loadFragment(new FragmentHome()); // Default fragment

        bottomNavigationView.setItemActiveIndicatorEnabled(false);
        //bottomNavigationView.setLabelVisibilityMode(NavigationBarView.LABEL_VISIBILITY_UNLABELED);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment fragment = null;
            int id = item.getItemId();

            bottomNavigationView.post(() -> {
                final View view = bottomNavigationView.findViewById(item.getItemId());
                if (view != null) {
                    view.animate()
                            .scaleX(1.2f)
                            .scaleY(1.2f)
                            .setDuration(150)
                            .withEndAction(() -> {
                                view.animate().scaleX(1f).scaleY(1f).setDuration(150).start();
                            }).start();
                }
            });


            if (id == R.id.nav_home) {
                fragment = new FragmentHome();
            } else if (id == R.id.nav_profile) {
                fragment = new FragmentProfile();
            } else if (id == R.id.nav_add) {
                fragment = new FragmentAdd();
            } else if (id == R.id.nav_order) {
                fragment = new FragmentOrders();
            } else if (id == R.id.nav_wish) {
                fragment = new FragmentWishlist();
            }

            if (fragment != null) {
                loadFragment(fragment);
                return true;
            }

            return false;
        });



    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }

    private int dpToPx(int dp) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round((float) dp * density);
    }


}