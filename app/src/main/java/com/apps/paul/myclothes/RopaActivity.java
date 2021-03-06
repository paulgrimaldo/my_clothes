package com.apps.paul.myclothes;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.apps.paul.myclothes.Fragmentos.Ropa.RegistrarPrenda_Fragment;
import com.apps.paul.myclothes.Modelos.Ropa.Ropa;


public class RopaActivity extends AppCompatActivity implements RegistrarPrenda_Fragment.RegistrarFragment_Events {
    private static final int CAMERA_REQUEST = 1;
    Toolbar appbar;
    private DrawerLayout drawerLayout;
    private NavigationView navView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ropa);
        setTitle("My Clothes- Ropa");

        appbar = (Toolbar) findViewById(R.id.appbar);
        setSupportActionBar(appbar);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_nav_menu);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navView = (NavigationView) findViewById(R.id.navview);

        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_categorias:
                        Toast.makeText(getBaseContext(), "Categorias", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.menu_prendas:
                        Toast.makeText(getBaseContext(), "Prendas", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(getBaseContext(), RopaActivity.class);
                        startActivity(i);
                        break;
                    case R.id.menu_ropa_usada:
                        Toast.makeText(getBaseContext(), "Ropa usada", Toast.LENGTH_SHORT).show();

                        break;
                    case R.id.menu_contacos:
                        Toast.makeText(getBaseContext(), "Contactos", Toast.LENGTH_SHORT).show();

                        break;
                    case R.id.menu_favoritos:
                        Toast.makeText(getBaseContext(), "Favoritos", Toast.LENGTH_SHORT).show();

                        break;
                }
                drawerLayout.closeDrawers(); // cierro el cajon
                return true;
            }
        });
        ///cargar el fragment por default

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
            case R.id.menu_nuevo_ropa:
                registrarPrenda();
                return true;
            case R.id.action_settings:
                return true;
            case R.id.logout:
                return true;
        }

        return true;
    }

    private void registrarPrenda() {
        FragmentManager manager = getSupportFragmentManager();
        Fragment layout = manager.findFragmentByTag(RegistrarPrenda_Fragment.TAG);
        if (layout == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_frame_ropa, RegistrarPrenda_Fragment.newInstance(null), RegistrarPrenda_Fragment.TAG)
                    .addToBackStack(null)
                    .commit();
        } else {
            Toast.makeText(this, "Register form has been loaded", Toast.LENGTH_SHORT).show();
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ropa, menu);
        return true;
    }


    @Override
    public void registroFinalizado(Ropa ropa) {
        ///recargar el fragmento inicial
    }
}
