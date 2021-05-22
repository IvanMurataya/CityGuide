package com.example.cityguide.User;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.cityguide.Common.LoginSignup.RetailerStarUpScreen;
import com.example.cityguide.HelperClasses.HomeAdapter.CategoriesAdapter;
import com.example.cityguide.HelperClasses.HomeAdapter.FeaturedAdapter;
import com.example.cityguide.HelperClasses.HomeAdapter.FeaturedHelperClass;
import com.example.cityguide.HelperClasses.HomeAdapter.MostViewAdapter;
import com.example.cityguide.R;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

public class UserDashboard extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    static final float END_SCALE = 0.7f;
    RecyclerView featuredRecycler, mostViewedRecyler, categoriesRecycler;
    RecyclerView.Adapter adapter, adapter2, adapter3;
    ImageView menuIcon;
    LinearLayout contentView;

    //Drawer Manu
    DrawerLayout drawerLayout;
    NavigationView navigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_user_dashboard);

        //Hooks
        featuredRecycler = findViewById(R.id.featured_recycler);
        mostViewedRecyler = findViewById(R.id.featured_recycler2);
        categoriesRecycler = findViewById(R.id.featured_recycler3);
        menuIcon = findViewById(R.id.menu_icon);
        contentView = findViewById(R.id.content);
        // Menu Hooks
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);


        navigationDrawer();


        //Recycler View Function Calls
        featuredRecycler();
        mostViewedRecycler();
        categoriesRecycler();
    }

    // Navigation Drawer Functions
    //when button is pressed open menu
    private void navigationDrawer() {

        // Navigation Drawer
        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_home);

        menuIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawerLayout.isDrawerVisible(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    drawerLayout.openDrawer(GravityCompat.START);
                }
            }
        });

        animateNavigationDrawer();
    }
    private void animateNavigationDrawer() {
        //Add any color or remove it to use the default one!
        //To make it transparent use Color.Transparent in side setScrimColor();
        //drawerLayout.setScrimColor(Color.TRANSPARENT);
        drawerLayout.setScrimColor(getResources().getColor(R.color.colorPrimary));
        drawerLayout.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

                // Scale the View based on current slide offset
                final float diffScaledOffset = slideOffset * (1 - END_SCALE);
                final float offsetScale = 1 - diffScaledOffset;
                contentView.setScaleX(offsetScale);
                contentView.setScaleY(offsetScale);

                // Translate the View, accounting for the scaled width
                final float xOffset = drawerView.getWidth() * slideOffset;
                final float xOffsetDiff = contentView.getWidth() * diffScaledOffset / 2;
                final float xTranslation = xOffset - xOffsetDiff;
                contentView.setTranslationX(xTranslation);
            }
        });

    }



    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerVisible(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }else{
            super.onBackPressed();
        }

    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_all_categories:
                Intent intent = new Intent(getApplicationContext(),AllCategories.class);
                startActivity(intent);
                break;

        }
        return true; //Some item will be selected
    }


    public void callRetailerScreens(View view){
        startActivity(new Intent(getApplicationContext(), RetailerStarUpScreen.class));
    }


    // Recicle elements
    private void featuredRecycler() {
        featuredRecycler.setHasFixedSize(true);
        featuredRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        ArrayList<FeaturedHelperClass> featuredLocations = new ArrayList<>();

        featuredLocations.add(new FeaturedHelperClass(R.drawable.orderingcar, "McDonalds", "fsdfas fdsa fdsa "));
        featuredLocations.add(new FeaturedHelperClass(R.drawable.carrental, "Enderobe", "fsdfas fdsa fdsa "));
        featuredLocations.add(new FeaturedHelperClass(R.drawable.clip_1655, "Sweet and bakers", "fsdfas fdsa fdsa "));
        adapter = new FeaturedAdapter(featuredLocations);
        featuredRecycler.setAdapter(adapter);

    }

    private void mostViewedRecycler() {
        mostViewedRecyler.setHasFixedSize(true);
        mostViewedRecyler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        ArrayList<FeaturedHelperClass> featuredLocations = new ArrayList<>();

        featuredLocations.add(new FeaturedHelperClass(R.drawable.orderingcar, "Play", "fsdfas fdsa fdsa f dsahilkfds ahkfjdsa hjfkdsafs gre"));
        featuredLocations.add(new FeaturedHelperClass(R.drawable.carrental, "Gym", "fsdfas fdsa fdsa  jklf kldsaj fklsa j klfñdsa"));
        featuredLocations.add(new FeaturedHelperClass(R.drawable.clip_1655, "Expand", "fsdfas fdsa fdsa fdsafdsaf jglfdsjgfklds jfkldsa l jfkldsa"));

        adapter2 = new MostViewAdapter(featuredLocations);
        mostViewedRecyler.setAdapter(adapter2);

    }

    private void categoriesRecycler() {
        categoriesRecycler.setHasFixedSize(true);
        categoriesRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        ArrayList<FeaturedHelperClass> featuredLocations = new ArrayList<>();

        featuredLocations.add(new FeaturedHelperClass(R.drawable.orderingcar, "Drive"));
        featuredLocations.add(new FeaturedHelperClass(R.drawable.carrental, "Helping"));
        featuredLocations.add(new FeaturedHelperClass(R.drawable.clip_1655, "Visit"));

        adapter3 = new CategoriesAdapter(featuredLocations);
        categoriesRecycler.setAdapter(adapter3);

    }


}