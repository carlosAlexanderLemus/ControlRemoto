package lemus.com.bast_software.controlremoto;

import android.app.FragmentManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Obtenemos las referencia de aquellos elementos del diseño
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        DrawerLayout drawerLayout = (DrawerLayout)findViewById(R.id.drawer_main);
        NavigationView navigationView = (NavigationView)findViewById(R.id.navigation_main);

        // Activamos el toolbar
        setSupportActionBar(toolbar);

        // Añadimos el drawe layout
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        // Añadimos el evento importante
        navigationView.setNavigationItemSelectedListener(this);

        // Obtenemos la instancia del fragment
        DeviceFragment devicefragment = new DeviceFragment();
        // Preparamos el contenedor
        getSupportFragmentManager().beginTransaction().add(R.id.contenedor_fragment, devicefragment);
    }


    @Override
    public void onBackPressed() {
        // AL preciones atras nos devolvemos al origen
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_main);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Aregamos los iconos
        getMenuInflater().inflate(R.menu.menu_toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Obtenemos el id del item seleccionado
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Obtemos el id del item
        int id = item.getItemId();

        // Comprobamos que elementos es
        switch (id)
        {
            case R.id.item_device:
                // Obtenemos la instancia del fragment
                DeviceFragment devicefragment = new DeviceFragment();
                // Fragmento
                FragmentTransaction fragmenttransaction = getSupportFragmentManager().beginTransaction();
                // Preparamos el contenedor
                fragmenttransaction.replace(R.id.contenedor_fragment, devicefragment);
                // Validamos el cambio
                fragmenttransaction.commit();
                break;
        }

        // Cerrramos el panel
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_main);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
