package com.example.locatex;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.locatex.Fragment.HomeFragment;
import com.example.locatex.Fragment.MapFragment;
import com.example.locatex.Fragment.MeFragment;
import com.example.locatex.GoogleMap.MapActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

public class HomepageActivity extends AppCompatActivity implements
        BottomNavigationView.OnNavigationItemSelectedListener {

    boolean doubleTap = false;

    SharedPreferences Preferences;
    SharedPreferences.Editor editor;

    BottomNavigationView HomeBottomNV;
    DrawerLayout drawerLayout;
    ImageView ivmenubtn;

    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
        ivmenubtn = findViewById(R.id.ivmenubtn);
        drawerLayout = findViewById(R.id.drawerlayout);
        navigationView = findViewById(R.id.navigation_view);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);


        ivmenubtn.setOnClickListener(v -> {
            drawerLayout.openDrawer(GravityCompat.START);
        });

        navigationView.setNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.menuHomeMyProfile)
            {
                Intent i = new Intent(HomepageActivity.this,MyProfileActivity.class);
                startActivity(i);
            }
            else if (item.getItemId() == R.id.menuHomeSetting)
            {
                Intent i = new Intent(HomepageActivity.this,SettingActivity.class);
                startActivity(i);
                Toast.makeText(HomepageActivity.this, "Settings Click", Toast.LENGTH_SHORT).show();
            }
            else if (item.getItemId() == R.id.menuHomeContactUs)
            {
                Intent i = new Intent(HomepageActivity.this,ContactUsActivity.class);
                startActivity(i);
                Toast.makeText(HomepageActivity.this, "Contact Us Click", Toast.LENGTH_SHORT).show();
            }
            else if (item.getItemId() == R.id.menuHomeAboutUs)
            {
                Intent i = new Intent(HomepageActivity.this,AboutUsActivity.class);
                startActivity(i);
                Toast.makeText(HomepageActivity.this, "About Us Click", Toast.LENGTH_SHORT).show();
            }
            else if (item.getItemId() == R.id.menuHomeLogout)
            {
                logout();
            }
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;

        });
        HomeBottomNV = findViewById(R.id.HomeBottomNV);
        HomeBottomNV.setOnNavigationItemSelectedListener(this);
        HomeBottomNV.setSelectedItemId(R.id.bottomMenuHomeHome);
        ivmenubtn = findViewById(R.id.ivmenubtn);
        drawerLayout = findViewById(R.id.drawerlayout);
        navigationView = findViewById(R.id.navigation_view);

        ivmenubtn.setOnClickListener(v -> {
            drawerLayout.openDrawer(GravityCompat.START);
        });

        Preferences = PreferenceManager.getDefaultSharedPreferences(HomepageActivity.this);
        editor = Preferences.edit();

        boolean isFirstTime = Preferences.getBoolean("isFirstTime",true);
        if (isFirstTime)
        {
            Wellcome();
        }
    }


    private void Wellcome()
    {
        AlertDialog.Builder ald = new AlertDialog.Builder(HomepageActivity.this);
        ald.setTitle("LocateX App");
        ald.setMessage("Welcome to LocateX App");
        ald.setPositiveButton("Thank You", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        }).show().create();

        editor.putBoolean("isFirstTime",false).commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.home_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId()==R.id.menuHomeMyProfile)
        {
            Intent I = new Intent(HomepageActivity.this,MyProfileActivity.class);
            startActivity(I);
        }

        else if (item.getItemId()==R.id.menuHomeSetting)
        {
            Intent I = new Intent(HomepageActivity.this, SettingActivity.class);
            startActivity(I);
        }

        else if (item.getItemId()==R.id.menuHomeContactUs)
        {
            Intent I = new Intent(HomepageActivity.this,ContactUsActivity.class);
            startActivity(I);
        }

        else if (item.getItemId()==R.id.menuHomeAboutUs)
        {
            Intent I = new Intent(HomepageActivity.this, AboutUsActivity.class);
            startActivity(I);
        }

        else if (item.getItemId()==R.id.menuHomeLogout)
        {
            logout();
        }
        return true;
    }

    private void logout()
    {
        AlertDialog.Builder ald = new AlertDialog.Builder(HomepageActivity.this);
        ald.setTitle("Logout");
        ald.setMessage("Are you sure you want to logout ?");
        ald.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.cancel();
            }
        });

        ald.setNegativeButton("Logout", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                Intent I = new Intent(HomepageActivity.this,LoginActivity2.class);
                editor.putBoolean("isLogin",false).commit();
                startActivity(I);
                finishAffinity();
            }
        }).show().create();
    }


    @SuppressLint({"MissingSuperCall", "GestureBackNavigation"})
    @Override
    public void onBackPressed()
    {
        if (doubleTap) {
            finishAffinity();
        }
        else
        {
            Toast.makeText(HomepageActivity.this, "Double tap to exit app", Toast.LENGTH_SHORT).show();
            doubleTap = true;
            Handler H = new Handler();
            H.postDelayed(new Runnable() {
                @Override
                public void run() {
                    doubleTap = false;
                }
            },2000);
        }
    }

    HomeFragment homeFragment = new HomeFragment();

    MapFragment mapFragment = new MapFragment();
    MeFragment meFragment = new MeFragment();
    @SuppressLint("CommitTransaction")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.bottomMenuHomeHome)
        {
            getSupportFragmentManager().beginTransaction().
                    replace(R.id.HomeFrameLayout,homeFragment).commit();
        }
        else if (item.getItemId() == R.id.bottomMeuHomeMap)
        {
            getSupportFragmentManager().beginTransaction().
                    replace(R.id.HomeFrameLayout,mapFragment).commit();
            Intent I = new Intent(HomepageActivity.this, MapActivity.class);
            startActivity(I);
        }
        else if (item.getItemId() == R.id.bottomMenuHomeMe)
        {
         getSupportFragmentManager().beginTransaction().
                 replace(R.id.HomeFrameLayout,meFragment).commit();
        }

        return true;
    }
}