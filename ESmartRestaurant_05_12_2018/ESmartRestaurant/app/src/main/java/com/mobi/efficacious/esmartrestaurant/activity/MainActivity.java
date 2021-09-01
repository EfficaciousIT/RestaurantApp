package com.mobi.efficacious.esmartrestaurant.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import com.mobi.efficacious.esmartrestaurant.R;
import com.mobi.efficacious.esmartrestaurant.fragment.Bill_Table;
import com.mobi.efficacious.esmartrestaurant.fragment.ExistingOrderedTableActivity;
import com.mobi.efficacious.esmartrestaurant.fragment.New_Dashboard;
import com.mobi.efficacious.esmartrestaurant.fragment.TakeAwayActivity;
import com.mobi.efficacious.esmartrestaurant.fragment.ViewCategoryActivity;
import com.mobi.efficacious.esmartrestaurant.fragment.main_fragment;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    String title;
    public static FragmentManager fragmentManager;
    private static final String PREFRENCES_NAME = "myprefrences";
    SharedPreferences settings;
    int k;
   public static ImageView cart_imgbtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cart_imgbtn=(ImageView)findViewById(R.id.cart_imgbtn);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        settings = getSharedPreferences(PREFRENCES_NAME, Context.MODE_PRIVATE);
        fragmentManager=getSupportFragmentManager();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);
        Calendar c = Calendar.getInstance();
        int timeOfDay = c.get(Calendar.HOUR_OF_DAY);

        if(timeOfDay >= 0 && timeOfDay < 12){
            title="Good Morning";
        }else if(timeOfDay >= 12 && timeOfDay < 16){
            title="Good Afternoon";
        }else if(timeOfDay >= 16 && timeOfDay < 21){
            title="Good Evening";
        }else if(timeOfDay >= 21 && timeOfDay < 24){
            title="Good Night";
        }
        main_fragment homeFragment = new main_fragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.content_main, homeFragment).commitAllowingStateLoss();
        getSupportActionBar().setTitle(title);
        cart_imgbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                title = "View Order";
                getSupportActionBar().setTitle(title);
                ExistingOrderedTableActivity existingOrderedTableActivity = new ExistingOrderedTableActivity();
                Bundle args = new Bundle();
                args.putString("pagename","ViewOrder");
                existingOrderedTableActivity.setArguments(args);
                getSupportFragmentManager().beginTransaction().replace(R.id.content_main, existingOrderedTableActivity).commitAllowingStateLoss();

            }
        });
    }

    @Override
    public void onBackPressed() {
        k++;
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            drawer.openDrawer(GravityCompat.START);
//            super.onBackPressed();
        }
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                k = 0;
            }
        }, 1000);
        if (k == 1) {
            Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setMessage("Are you sure want to Exit?").setPositiveButton("Yes", dialogClickListener)
                    .setNegativeButton("No", dialogClickListener).show();
        }

    }

    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case DialogInterface.BUTTON_POSITIVE:
                    finish();
                    break;
                case DialogInterface.BUTTON_NEGATIVE:

                    break;
            }
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            try
            {
                DialogInterface.OnClickListener dialogClickListenerr = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:
                                SharedPreferences.Editor editor_delete = settings.edit();
                                editor_delete.clear().commit();
                                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();
                                break;
                            case DialogInterface.BUTTON_NEGATIVE:

                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setMessage("Are you sure want to LogOut?").setPositiveButton("Yes", dialogClickListenerr)
                        .setNegativeButton("No", dialogClickListenerr).show();

            }catch (Exception ex)
            {

            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_Home) {
            title = "Home";
            main_fragment homeFragment = new main_fragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.content_main, homeFragment).commitAllowingStateLoss();
        } else if (id == R.id.nav_NewOrder) {
            title = "New Order";
            New_Dashboard new_dashboard = new New_Dashboard();
            getSupportFragmentManager().beginTransaction().replace(R.id.content_main, new_dashboard).commitAllowingStateLoss();
        } else if (id == R.id.nav_ExistingOrder) {
            title = "Existing Order";
            ExistingOrderedTableActivity existingOrderedTableActivity = new ExistingOrderedTableActivity();
            Bundle args = new Bundle();
            args.putString("pagename","ExistingOrder");
            existingOrderedTableActivity.setArguments(args);
            getSupportFragmentManager().beginTransaction().replace(R.id.content_main, existingOrderedTableActivity).commitAllowingStateLoss();

        } else if (id == R.id.nav_ViewOrder) {
            title = "View Order";
            ExistingOrderedTableActivity existingOrderedTableActivity = new ExistingOrderedTableActivity();
            Bundle args = new Bundle();
            args.putString("pagename","ViewOrder");
            existingOrderedTableActivity.setArguments(args);
            getSupportFragmentManager().beginTransaction().replace(R.id.content_main, existingOrderedTableActivity).commitAllowingStateLoss();

        } else if (id == R.id.nav_TakeAway) {
            title = "Take Away";
            TakeAwayActivity takeAwayActivity = new TakeAwayActivity();
            getSupportFragmentManager().beginTransaction().replace(R.id.content_main, takeAwayActivity).commitAllowingStateLoss();
        } else if (id == R.id.nav_Menu) {
            title = "Menu";
            ViewCategoryActivity viewCategoryActivity = new ViewCategoryActivity();
            getSupportFragmentManager().beginTransaction().replace(R.id.content_main, viewCategoryActivity).commitAllowingStateLoss();
//        } else if (id == R.id.nav_Profile) {
        }
        else if (id == R.id.nav_bill) {
            title = "View Bill";
            Bill_Table bill_table = new Bill_Table();
            getSupportFragmentManager().beginTransaction().replace(R.id.content_main, bill_table).commitAllowingStateLoss();
        } else if (id == R.id.nav_SignOut) {
            try
            {
                DialogInterface.OnClickListener dialogClickListenerr = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:
                                SharedPreferences.Editor editor_delete = settings.edit();
                                editor_delete.clear().commit();
                                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();
                                break;
                            case DialogInterface.BUTTON_NEGATIVE:

                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setMessage("Are you sure want to LogOut?").setPositiveButton("Yes", dialogClickListenerr)
                        .setNegativeButton("No", dialogClickListenerr).show();

            }catch (Exception ex)
            {

            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        getSupportActionBar().setTitle(title);
        return true;
    }
}
