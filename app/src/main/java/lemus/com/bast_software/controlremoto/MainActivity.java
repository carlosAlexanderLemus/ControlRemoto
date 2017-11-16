package lemus.com.bast_software.controlremoto;

import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import lemus.com.bast_software.controlremoto.ConexionRed.ActuadorDeTexto;
import lemus.com.bast_software.controlremoto.ConexionRed.DispositivoConexion;
import lemus.com.bast_software.controlremoto.ConexionRed.InformacionConexion;
import lemus.com.bast_software.controlremoto.ConexionRed.ResultadoTexto;
import lemus.com.bast_software.controlremoto.ConexionRed.Servidores;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    // Informacion sobre el menu precionado
    private int id_option_used = -1;

    // Metodo para la conexion
    private void ConectarConElDispositivo(final DispositivosIP dispositivoIp, final ProgressDialog progress)
    {
        final Servidores servidor = new Servidores(this);
        servidor.establecerActuadorDeTexto(new ActuadorDeTexto() {
            @Override
            public void RecibirMensaje(ResultadoTexto resultado) {
                // Obtenemos todos los posibles resultados
                switch (resultado.TipoDeAccion())
                {
                    case InformacionConexion.MOTIVOCONEXION_REPUESTAEXITOSA:
                        DispositivoConexion.EstablecerDispositivoActual(dispositivoIp);
                        Toast.makeText(MainActivity.this, "Conexion exitosa", Toast.LENGTH_SHORT).show();
                        break;

                    default:
                        Toast.makeText(MainActivity.this, "No se ha podido establecer la conexion", Toast.LENGTH_SHORT).show();
                        break;
                }
                // De cualquier caso lo quitamos
                progress.dismiss();
                // Esperemos que no haya error :"v
                servidor.onDestroy();
            }
        });
        servidor.iniciarServidor();
        // Enviamos mensaje
        DispositivoConexion.ConectarConElDispositivo(this, dispositivoIp);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // En caso que tengamos algun dispositivo a almacenar
        if (DispositivoConexion.HayDispositivoGuardado(this))
        {
            // Obtenemos el dispositivo actual
            final DispositivosIP dispositivosIP = DispositivoConexion.ObtenerDispositivoARecordar(this);

            // Comprobamos que no sea nulo
            if (dispositivosIP != null)
            {
                // Preguntamoa que si quera conectar
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                View layout = getLayoutInflater().inflate(R.layout.select_device_item_layout, null);

                // Obtenemos los txt
                final TextView tv_name = (TextView)layout.findViewById(R.id.tv_selected_name_device);
                final TextView tv_ip = (TextView)layout.findViewById(R.id.tv_selected_ip_device);

                // Botones
                Button btn_aceptar = (Button)layout.findViewById(R.id.btn_aceptar_conexion);
                Button btn_cancelar = (Button)layout.findViewById(R.id.btn_cancelar_conexion);

                // Ingresamos los valores
                tv_name.setText(dispositivosIP.getNombre());
                tv_ip.setText(dispositivosIP.getIP());

                // Creamos el dialogo
                builder.setView(layout);
                final AlertDialog dialog = builder.create();

                // Manejamos los botones
                btn_aceptar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Iniciamos el progress bar
                        final ProgressDialog progress = new ProgressDialog(MainActivity.this);
                        progress.setCancelable(false);
                        progress.setTitle("Conexion con el servidor");
                        progress.setMessage("Esperando repuesta...");
                        progress.setMax(100);
                        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                        progress.setCanceledOnTouchOutside(false);

                        progress.show();
                        dialog.dismiss();

                        // Activamos la conexion
                        ConectarConElDispositivo(dispositivosIP.Clonar(), progress);
                    }
                });

                btn_cancelar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Quitamos el dialogo
                        dialog.dismiss();
                    }
                });

                // Mostramos el dialog
                dialog.show();
            }
        }

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

        // Comprobamos que el item no este usado
        if  (id != id_option_used) {
            // Guardamos el id actual
            id_option_used = id;

            // Comprobamos que elementos es
            switch (id) {
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

                case R.id.item_play_video:
                    PlayComputerStreamFragment playComputerStreamFragment = new PlayComputerStreamFragment();
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.contenedor_fragment, playComputerStreamFragment);
                    fragmentTransaction.commit();
                    break;
            }
        }

        // Cerrramos el panel
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_main);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
