package lemus.com.bast_software.controlremoto;


import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

import lemus.com.bast_software.controlremoto.ConexionRed.ActuadorDeTexto;
import lemus.com.bast_software.controlremoto.ConexionRed.Clientes;
import lemus.com.bast_software.controlremoto.ConexionRed.InformacionConexion;
import lemus.com.bast_software.controlremoto.ConexionRed.ResultadoTexto;
import lemus.com.bast_software.controlremoto.ConexionRed.Servidores;


/**
 * A simple {@link Fragment} subclass.
 */
public class DeviceFragment extends Fragment{

    // Informacion del servidor
    Servidores servidor;

    // Manejo del tab
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private DeviceListViewPageAdapter deviceListViewPageAdapter;

    public DeviceFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_device, container, false);
        final LayoutInflater layoutInflater = getLayoutInflater(savedInstanceState);

        // Establecemos el tab layout
        tabLayout = (TabLayout)view.findViewById(R.id.tab_menu_device);
        viewPager = (ViewPager)view.findViewById(R.id.view_menu_device);

        deviceListViewPageAdapter = new DeviceListViewPageAdapter(getFragmentManager());
        viewPager.setAdapter(deviceListViewPageAdapter);

        // Opciones a escoger
        final TabLayout.Tab tab_opcion_all_device = tabLayout.newTab();
        final TabLayout.Tab tab_opcion_most_use_device = tabLayout.newTab();
        final TabLayout.Tab tab_opcion_star_device = tabLayout.newTab();

        // Establecemos los iconos
        tab_opcion_all_device.setIcon(R.mipmap.ic_all_device_list_select);
        tab_opcion_most_use_device.setIcon(R.drawable.ic_more_used_device);
        tab_opcion_star_device.setIcon(R.drawable.ic_favorites_device);

        // Añadimos la tab
        tabLayout.addTab(tab_opcion_all_device, 0);
        tabLayout.addTab(tab_opcion_most_use_device, 1);
        tabLayout.addTab(tab_opcion_star_device, 2);

        tabLayout.setTabTextColors(ContextCompat.getColorStateList(getContext(), R.color.tab_selector));
        tabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(getContext(), R.color.colorAcentTab));

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));


        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener(){

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position){
                    case 0:
                        tab_opcion_all_device.setIcon(R.mipmap.ic_all_device_list_select);
                        tab_opcion_most_use_device.setIcon(R.drawable.ic_more_used_device);
                        tab_opcion_star_device.setIcon(R.drawable.ic_favorites_device);
                        break;

                    case 1:
                        tab_opcion_all_device.setIcon(R.drawable.ic_all_device_list);
                        tab_opcion_most_use_device.setIcon(R.mipmap.ic_more_used_device_select);
                        tab_opcion_star_device.setIcon(R.drawable.ic_favorites_device);
                        break;

                    case  2:
                        tab_opcion_all_device.setIcon(R.drawable.ic_all_device_list);
                        tab_opcion_most_use_device.setIcon(R.drawable.ic_more_used_device);
                        tab_opcion_star_device.setIcon(R.mipmap.ic_favorites_device_select);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        // Inicializamos el servidor
        servidor = new Servidores(getActivity());

        // Iniciamos el servidor
        servidor.iniciarServidor();

        // Obtenemos la informacion
        BottomNavigationView bottomNavigationView = (BottomNavigationView)view.findViewById(R.id.bottom_device);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                // Obtenemos el ID
                int id = item.getItemId();

                // Comprobamos cual es el id de origen
                switch (id){
                    // En caso que tengamos que agregar un nuevo dispositivo
                    case R.id.item_bottom_add_device:
                        // Creamos el dialogo
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                        // Escogemos el layout
                        View view_layout = layoutInflater.inflate(R.layout.layout_add_device, null);

                        // Obtenemos la referencia a los botonre
                        Button btn_acepat = (Button)view_layout.findViewById(R.id.btn_conectar_dispositivo);
                        Button btn_cancelar = (Button)view_layout.findViewById(R.id.btn_cancelar_agregar_dispositivos);

                        // Los textboxt del layout
                        final EditText et_direccionIP = (EditText)view_layout.findViewById(R.id.et_ip_device);
                        final EditText et_puerto = (EditText)view_layout.findViewById(R.id.et_port_device);

                        final EditText et_nombrePC = (EditText)view_layout.findViewById(R.id.et_name_device);
                        final EditText et_passwordPC1 = (EditText)view_layout.findViewById(R.id.et_pass_device);
                        final EditText et_passwordPC2 = (EditText)view_layout.findViewById(R.id.et_pass_confirmate_device);

                        // Lo establecemos como el layout
                        builder.setView(view_layout);

                        // Creamos el alerta
                        final AlertDialog alertDialog = builder.create();

                        // Establecemos el evento
                        servidor.establecerActuadorDeTexto(new ActuadorDeTexto() {
                            @Override
                            public void RecibirMensaje(ResultadoTexto resultado) {
                                // Probamos los distintos casos
                                switch (resultado.TipoDeAccion())
                                {
                                    // Conexion exitosa
                                    case InformacionConexion.MOTIVOCONEXION_REPUESTAEXITOSA:
                                        // Obtenemos la repuesta
                                        Toast.makeText(getContext(), "Conexion exitosa", Toast.LENGTH_SHORT).show();
                                        // Quitamos el cuadro de dialogo
                                        alertDialog.dismiss();
                                        break;

                                    case  InformacionConexion.MOTIVOCONEXION_REPUESTAFRACASO:
                                        // Obtenemos la repuesta
                                        Toast.makeText(getContext(), "Fracaso de conexion", Toast.LENGTH_SHORT).show();

                                        break;

                                    case InformacionConexion.MOTIVOCONEXION_REPUESTACONEXIONEXISTENTE:
                                        // Repuesta ante una conexion ya existente
                                        Toast.makeText(getContext(), "Ya hay algun dispositivo conectado", Toast.LENGTH_SHORT).show();

                                        break;
                                }
                            }
                        });

                        // Acciones a realixar a seleccionar cualquis dispositivo
                        btn_acepat.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                            // Obtenemos los recursos
                            Resources resources = getResources();

                            // Obtenemos el puerto por defecto
                            String puerto = resources.getString(R.string.port_default);

                            // Si el EditText no esta vacio podemos hacer la conexion
                            if (!et_direccionIP.getText().toString().equals("")) {

                                // Saber si enviamos el mensaje
                                boolean lasClavesConcuerdan = true;

                                // Saber si usaremos el puerto por defecto
                                if (!et_puerto.getText().toString().equals("")) puerto = et_puerto.getText().toString();

                                // Convertimos el puerto a un entero
                                int puerto_final = Integer.parseInt(puerto);

                                // Creamos el coso de cliente
                                Clientes cliente = new Clientes(et_direccionIP.getText().toString(), puerto_final);

                                // Si posee nombre
                                if (!et_nombrePC.getText().toString().equals(""))
                                    cliente.AñadirLinea(InformacionConexion.CABECERA_NOMBREPC, et_nombrePC.getText().toString());

                                // Si las dos claves concuerdan
                                if (!et_passwordPC1.getText().toString().equals("") || !et_passwordPC2.getText().toString().equals(""))
                                {
                                    // Comprobamos que sean la misma clave
                                    if (et_passwordPC1.getText().toString().equals(et_passwordPC2.getText().toString()))
                                    {
                                        // Añadimos la iformacion de la clave
                                        cliente.AñadirLinea(InformacionConexion.CABECERA_CLAVEPC, et_passwordPC1.getText().toString());
                                    }
                                    else
                                        lasClavesConcuerdan = false;
                                }

                                if (lasClavesConcuerdan)
                                    // En viamos el mensaje
                                    cliente.EnviarMensajeDeTexto(InformacionConexion.MOTIVOCONEXION_CONECTARSE);
                                else
                                    Toast.makeText(getContext(), "Error: las claves no concuerdan", Toast.LENGTH_SHORT).show();
                            }
                            else
                                Toast.makeText(getContext(), "Error: No hay direccion IP", Toast.LENGTH_SHORT).show();
                            }
                        });

                        // Accion a realizar a cancelar
                        btn_cancelar.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // Quitamos el cuadro de dialogo
                                alertDialog.dismiss();
                            }
                        });

                        // Mostramos el dialogo
                        alertDialog.show();
                        break;
                }

                return true;
            }
        });
        return view;
    }


    @Override
    public void onDestroy() {
        // Accion para cerrar el servidor
        servidor.onDestroy();
        // Por defecto
        super.onDestroy();
    }
}
