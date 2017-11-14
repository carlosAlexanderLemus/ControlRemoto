package lemus.com.bast_software.controlremoto;


import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import lemus.com.bast_software.controlremoto.ConexionRed.ActuadorDeTexto;
import lemus.com.bast_software.controlremoto.ConexionRed.Clientes;
import lemus.com.bast_software.controlremoto.ConexionRed.DispositivoConexion;
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

    // Adaptador
    private DeviceListViewPageAdapter deviceListViewPageAdapter;

    // Informacion de los elementos
    private TextView tv_ip_address_val;
    private TextView tv_puerto_val;
    private CheckBox cb_recordar_dispositivo;
    private ImageView iv_favority_state;
    private ImageView iv_disconect_device_selected;

    // Guardamos el dispositivo IP
    public DispositivosIP dispositivosIPNuevaConexion = null;

    public DeviceFragment() {
        // Required empty public constructor
    }


    public void Desconectar(DispositivosIP dispositivosIP)
    {
        // Establecemos el evento
        servidor.establecerActuadorDeTexto(new ActuadorDeTexto() {
            @Override
            public void RecibirMensaje(ResultadoTexto resultado) {
                // Probamos los distintos casos
                switch (resultado.TipoDeAccion())
                {
                    // En caso que la desconexion haya sido exitosa
                    case InformacionConexion.MOTIVOCONEXION_DESCONEXION_EXISTOSA:
                        // Nos desconectamos
                        DispositivoConexion.EliminarDispositivoActual();
                        // Borramos la informacion
                        ResetearInformacion();
                        Toast.makeText(getContext(), "Sea desconectado con exito", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });

        DispositivoConexion.SolicitarDesconexion(getContext(), dispositivosIP);
    }

    public void DesconectarConEliminacion(DispositivosIP dispositivosIP)
    {
        // btenemos el dispositivo
        final DispositivosIP dispositivosIP_eliminar = dispositivosIP.Clonar();
        // Establecemos el evento
        servidor.establecerActuadorDeTexto(new ActuadorDeTexto() {
            @Override
            public void RecibirMensaje(ResultadoTexto resultado) {
                // Probamos los distintos casos
                switch (resultado.TipoDeAccion())
                {
                    // En caso que la desconexion haya sido exitosa
                    case InformacionConexion.MOTIVOCONEXION_DESCONEXION_EXISTOSA:
                        // Eliminamos el item de la base
                        if (DispositivoConexion.EliminarDispositivoPorID(getContext(), dispositivosIP_eliminar.getId()))
                        {
                            // Si logramos la eliminacion borramos todos los item
                            EliminarDispositivo(dispositivosIP_eliminar);
                        }
                        // Nos desconectamos
                        DispositivoConexion.EliminarDispositivoActual();
                        // Borramos la informacion
                        ResetearInformacion();
                        Toast.makeText(getContext(), "Sea desconectado con exito", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });

        DispositivoConexion.SolicitarDesconexion(getContext(), dispositivosIP);
    }

    public void ActualizarTodosLosDispositivos(int tipo_accion, DispositivosIP dispositivosIP)
    {
        deviceListViewPageAdapter.ActualizarTodosLosDispositivos(tipo_accion, dispositivosIP);
        Log.d("TipoDeAccion", "Tipo: "+tipo_accion);
    }

    public void EliminarDispositivo(int tipo_accion, DispositivosIP dispositivosIP)
    {
        deviceListViewPageAdapter.EliminarDispositivo(tipo_accion, dispositivosIP);
        Log.d("TipoDeAccion", "Tipo: "+tipo_accion);
    }

    public void EliminarDispositivo(DispositivosIP dispositivosIP)
    {
        deviceListViewPageAdapter.EliminarDispositivo(dispositivosIP);
    }


    // Cambiamos el dispositivo actual
    public void EstablecerNuevaConexion(DispositivosIP dispositivosIP)
    {
        // Modificamos accion
        // Establecemos el evento
        servidor.establecerActuadorDeTexto(new ActuadorDeTexto() {
            @Override
            public void RecibirMensaje(ResultadoTexto resultado) {
                // Probamos los distintos casos
                switch (resultado.TipoDeAccion())
                {
                    // Conexion exitosa
                    case InformacionConexion.MOTIVOCONEXION_REPUESTAEXITOSA:
                        // Comprobamos que haya un dispositivo Actual
                        if (dispositivosIPNuevaConexion != null)
                        {
                            // Aumentamos la frecuencia por uno
                            dispositivosIPNuevaConexion.setFrecuenca(dispositivosIPNuevaConexion.getFrecuenca()+1);
                            // Cambiamos la frecuencia
                            if (!DispositivoConexion.ActualizarFrecuenciaDelDispositivo(getContext(), dispositivosIPNuevaConexion))
                                // Si cambiamos la frecuencia si no logramos modificarlo
                                dispositivosIPNuevaConexion.setFrecuenca(dispositivosIPNuevaConexion.getFrecuenca()-1);
                            else
                                // Notificamos a todos
                                deviceListViewPageAdapter.ActualizarDispositivosUsados();
                            // Guardamos la informacion del dispositivo actual
                            DispositivoConexion.EstablecerDispositivoActual(dispositivosIPNuevaConexion);
                            // Especificamos la informacion
                            EstablecerInformacionPorConexion();
                            // Reseteamos la IP
                            dispositivosIPNuevaConexion = null;
                            // Mensaje final
                            Toast.makeText(getContext(), "Dispositivo conectado con exito", Toast.LENGTH_SHORT).show();
                        }
                        break;

                    case  InformacionConexion.MOTIVOCONEXION_REPUESTAFRACASO:
                        // Obtenemos la repuesta
                        Toast.makeText(getContext(), "Fracaso de conexion", Toast.LENGTH_SHORT).show();
                        break;

                    case InformacionConexion.MOTIVOCONEXION_REPUESTACONEXIONEXISTENTE:
                        Toast.makeText(getContext(), "El dispositivo ya se encuentra conectado", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
        // Guardamos el dispositivo
        dispositivosIPNuevaConexion = dispositivosIP;
        // Establecemos una nueva conexion
        DispositivoConexion.ConectarConElDispositivo(getContext(), dispositivosIP.Clonar());
    }
    // Cambiar el valor de favoritsmos
    public void CambiarFavoritismosEnDispositivo(boolean estado)
    {
        // Solo se ejecutara en caso que ya haya conexion establecida
        if (DispositivoConexion.HayConexionEstablecida() && iv_favority_state != null)
        {
            // Cambiamos la estrella
            if (!estado)
                iv_favority_state.setImageResource(android.R.drawable.btn_star_big_off);
            else
                iv_favority_state.setImageResource(android.R.drawable.btn_star_big_on);
        }
    }

    private void ResetearInformacion()
    {
        // Estabelcemos la informacion
        if (tv_ip_address_val != null) {
            tv_ip_address_val.setText(getContext().getResources().getString(R.string.ip_address_default));
        }

        if (tv_puerto_val != null) {
            tv_puerto_val.setText(getContext().getResources().getString(R.string.port_default));
        }

        // Habilitamos el poder recordar el dispositivo
        if (cb_recordar_dispositivo != null) {
            cb_recordar_dispositivo.setEnabled(false);
            cb_recordar_dispositivo.setOnCheckedChangeListener(null);
            cb_recordar_dispositivo.setChecked(false);
        }

        // Cambiamos el icono de la imagen
        if (iv_favority_state != null) {
            // Cambiamos la imagen
            iv_favority_state.setImageResource(android.R.drawable.btn_star_big_off);
            // Cambiamos el favoritismo
            iv_favority_state.setOnClickListener(null);
        }

        // Desconectar dispositivo
        if (iv_disconect_device_selected != null)
        {
            // Desconectar
            iv_disconect_device_selected.setOnClickListener(null);
        }

    }

    // Establecemos la informacion
    private void EstablecerInformacionPorConexion()
    {
        // Si hay alguna conexion
        if (DispositivoConexion.HayConexionEstablecida())
        {
            // Obtenemos el dispositivo
            DispositivosIP dispositivoIp = DispositivoConexion.ObtenerDispositivoActual();

            // Estabelcemos la informacion
            if (tv_ip_address_val != null)
                tv_ip_address_val.setText(dispositivoIp.getIP());

            if (tv_puerto_val != null)
                tv_puerto_val.setText(""+dispositivoIp.getPuerto());

            // Habilitamos el poder recordar el dispositivo
            if (cb_recordar_dispositivo != null) {
                cb_recordar_dispositivo.setEnabled(true);

                // Ahora verificamos que sea el dispositvo a recordar
                cb_recordar_dispositivo.setChecked(DispositivoConexion.ComprobarExistenciaDeRecordadorioDelDispositivo(getContext(), dispositivoIp));

                cb_recordar_dispositivo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        // En caso que queramos recordar el dispositivo
                        if (isChecked){
                            // Recordamos el dispositivo actual
                            if (DispositivoConexion.RecordarElDispositivoActual(getContext()))
                                Toast.makeText(getContext(), "El dispositivo sera recordado", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            // Eliminamos el dispositivo
                            if (DispositivoConexion.EliminarRecordarDispositivo(getContext()))
                                Toast.makeText(getContext(), "No se recordara el dispositivo", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

            // Cambiamos el icono de la imagen
            if (iv_favority_state != null) {
                // Cambiamos la imagen
                if  (dispositivoIp.isFavoritos())
                    iv_favority_state.setImageResource(android.R.drawable.btn_star_big_on);
                else
                    iv_favority_state.setImageResource(android.R.drawable.btn_star_big_off);

                // Cambiamos el favoritismo
                iv_favority_state.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Si hay alguna conexion
                        if (DispositivoConexion.HayConexionEstablecida())
                        {
                            // Obtenemos el dispositivo
                            DispositivosIP dispositivosIP = DispositivoConexion.ObtenerDispositivoActual();

                            // Cambismoe el icono
                            if (dispositivosIP.isFavoritos())
                                iv_favority_state.setImageResource(android.R.drawable.btn_star_big_off);
                            else
                                iv_favority_state.setImageResource(android.R.drawable.btn_star_big_on);

                            // Cambiamos la informacion
                            dispositivosIP.setFavoritos(!dispositivosIP.isFavoritos());

                            // Modificamos el dispositivo
                            if (DispositivoConexion.ModificarDispositivoFavorito(getContext(), dispositivosIP.isFavoritos(), dispositivosIP.getId()))
                            {
                                // Modificamos a los item
                                deviceListViewPageAdapter.ModificarItemsDispositivos(dispositivosIP.Clonar());

                                // Mensaje sastifactorio
                                Toast.makeText(v.getContext(), "Dispositivo modificado con exito", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
            }

            // Desconectar dispositivo
            if (iv_disconect_device_selected != null)
            {
                // Desconectar
                iv_disconect_device_selected.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Primero creamos un cuadro de texto
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                        // Damos la informacion basica
                        builder.setMessage("¿Esta seguro que desea desconectarse?")
                                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        // Obtenemos la direccion IP
                                        DispositivosIP dispositivosIP = DispositivoConexion.ObtenerDispositivoActual().Clonar();
                                        // Ahora lo eliminamos
                                        Desconectar(dispositivosIP);
                                    }
                                })
                                .setNegativeButton("No", null)
                                .show();
                    }
                });
            }
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_device, container, false);
        final LayoutInflater layoutInflater = getLayoutInflater(savedInstanceState);

        // Establecemos el tab layout
        tabLayout = (TabLayout)view.findViewById(R.id.tab_menu_device);
        viewPager = (ViewPager)view.findViewById(R.id.view_menu_device);

        tv_ip_address_val = (TextView)view.findViewById(R.id.tv_ip_adress_val);
        tv_puerto_val = (TextView)view.findViewById(R.id.tv_port_val);
        cb_recordar_dispositivo = (CheckBox)view.findViewById(R.id.cb_recordar_dispositivo);
        iv_favority_state = (ImageView)view.findViewById(R.id.iv_favority_state);
        iv_disconect_device_selected = (ImageView)view.findViewById(R.id.iv_disconect_device_selected);
        // Iniciamos el dispositivo
        deviceListViewPageAdapter = new DeviceListViewPageAdapter(this);
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
                        final CheckBox cbx_recordar = (CheckBox)view_layout.findViewById(R.id.cb_guardar_dispositivo);
                        final CheckBox cbx_conectar = (CheckBox)view_layout.findViewById(R.id.cb_conectar_dispositivo);

                        // Lo establecemos como el layout
                        builder.setView(view_layout);

                        // Creamos el alerta
                        final AlertDialog alertDialog = builder.create();

                        // Establecemos el evento
                        servidor.establecerActuadorDeTexto(new ActuadorDeTexto() {
                            @Override
                            public void RecibirMensaje(ResultadoTexto resultado) {
                                Toast.makeText(getContext(), "Puta recibio el mensaje", Toast.LENGTH_SHORT).show();
                                // Probamos los distintos casos
                                switch (resultado.TipoDeAccion())
                                {
                                    // Conexion exitosa
                                    case InformacionConexion.MOTIVOCONEXION_REPUESTAEXITOSA:
                                        // Obtenemos la conexion
                                        DispositivoConexion.Conectar conectar = DispositivoConexion.UltimaConexion;

                                        Toast.makeText(getContext(), "Conexion exitosa", Toast.LENGTH_SHORT).show();

                                        // Si la conexion no es nula
                                        if (conectar != null)
                                        {
                                            // Si fuimos capaces de enviar el mensaje
                                            if (conectar.isEnvioMensaje())
                                            {
                                                // Si queremos  recordar el dispositivo
                                                if (cbx_recordar.isChecked()){
                                                    // Añadimos el item a la base de datos
                                                    if (DispositivoConexion.GuardarDispositivo(getContext(), conectar))
                                                    {
                                                        // Obtenemos el ultimo dispositivo
                                                        DispositivosIP dispositivosIP = DispositivoConexion.ObtenerUltimoDispositivo(getContext());

                                                        // Comprobamos que no haya error
                                                        if  (dispositivosIP != null)
                                                        {
                                                            // Obtenemos el fragment
                                                            DeviceListFragment deviceListFragment = deviceListViewPageAdapter.ObtenerFragmenDeseado(DeviceListViewPageAdapter.TODOS_LOS_DISPOSITIVOS);
                                                            // Dispositivo Alamacenado con exito
                                                            deviceListFragment.AñadirDispositivo(dispositivosIP);
                                                            // Guardamos la informacion del dispositivo actual
                                                            DispositivoConexion.EstablecerDispositivoActual(dispositivosIP);
                                                            // Especificamos la informacion
                                                            EstablecerInformacionPorConexion();
                                                            // Mostramos el mensaje
                                                            Toast.makeText(getContext(), "Dispositivo almacenado con exito", Toast.LENGTH_SHORT).show();
                                                        }
                                                        else
                                                            Toast.makeText(getContext(), "Error: no se puede recuperar los datos del dispositivo", Toast.LENGTH_SHORT).show();
                                                    }
                                                    else
                                                        Toast.makeText(getContext(), "Error: no se ha podido almacenar los datos", Toast.LENGTH_SHORT).show();

                                                }
                                            }
                                        }

                                        // Quitamos el cuadro de dialogo
                                        alertDialog.dismiss();
                                        break;

                                    case  InformacionConexion.MOTIVOCONEXION_REPUESTAFRACASO:
                                        // Obtenemos la repuesta
                                        Toast.makeText(getContext(), "Fracaso de conexion", Toast.LENGTH_SHORT).show();

                                        break;

                                    case InformacionConexion.MOTIVOCONEXION_REPUESTACONEXIONEXISTENTE:
                                        // Creamos el contructor
                                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                                        //Creamos el mensaje
                                        builder.setMessage("Ya hay una conexion existente con el dispositivo deseado, ¿Desea guartar siempre el dispositivo?")
                                                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        // Obtenemos la conexion
                                                        DispositivoConexion.Conectar conectar_item = DispositivoConexion.UltimaConexion;

                                                        // Si la conexion no es nula
                                                        if (conectar_item != null)
                                                        {
                                                            // Si fuimos capaces de enviar el mensaje
                                                            if (conectar_item.isEnvioMensaje())
                                                            {
                                                                // Si queremos  recordar el dispositivo
                                                                if (cbx_recordar.isChecked()){
                                                                    // Añadimos el item a la base de datos
                                                                    if (DispositivoConexion.GuardarDispositivo(getContext(), conectar_item))
                                                                    {
                                                                        // Obtenemos el ultimo dispositivo
                                                                        DispositivosIP dispositivosIP = DispositivoConexion.ObtenerUltimoDispositivo(getContext());

                                                                        // Comprobamos que no haya error
                                                                        if  (dispositivosIP != null)
                                                                        {
                                                                            // Obtenemos el fragment
                                                                            DeviceListFragment deviceListFragment = deviceListViewPageAdapter.ObtenerFragmenDeseado(DeviceListViewPageAdapter.TODOS_LOS_DISPOSITIVOS);
                                                                            // Dispositivo Alamacenado con exito
                                                                            deviceListFragment.AñadirDispositivo(dispositivosIP);
                                                                            // Mostramos el mensaje
                                                                            Toast.makeText(getContext(), "Dispositivo almacenado con exito", Toast.LENGTH_SHORT).show();
                                                                        }
                                                                        else
                                                                            Toast.makeText(getContext(), "Error: no se puede recuperar los datos del dispositivo", Toast.LENGTH_SHORT).show();
                                                                    }
                                                                    else
                                                                        Toast.makeText(getContext(), "Error: no se ha podido almacenar los datos", Toast.LENGTH_SHORT).show();

                                                                }
                                                            }
                                                        }
                                                    }
                                                })
                                                .setNegativeButton("No", null)
                                                .show();

                                        // Quitamos el cuadro de dialogo
                                        alertDialog.dismiss();
                                        break;
                                }
                            }
                        });

                        // Acciones a realixar a seleccionar cualquis dispositivo
                        btn_acepat.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // Comprobamos que no hay conexion
                                if (DispositivoConexion.HayConexionEstablecida() && cbx_conectar.isChecked())
                                {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                                    builder.setMessage("¿Desea desconectar el dispositivo y conectarlo con este?")
                                            .setTitle("Ya hay un dispositivo conectado")
                                            .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    // Valores del formulario
                                                    String IP_device = et_direccionIP.getText().toString();
                                                    String puerto_device = et_puerto.getText().toString();
                                                    String nombre_device = et_nombrePC.getText().toString();
                                                    String password1_device = et_passwordPC1.getText().toString();
                                                    String password2_device = et_passwordPC2.getText().toString();

                                                    boolean conectar_con_el_dispositivo = cbx_conectar.isChecked();
                                                    boolean recordar_dispositivo = cbx_recordar.isChecked();

                                                    // Conexion con el dispositivo
                                                    DispositivoConexion.Conectar conectar = DispositivoConexion.TratarConcexion(getContext(), IP_device, puerto_device);

                                                    // Establecemos el resto de la informacion
                                                    conectar.EstablecerNombre(nombre_device);
                                                    conectar.EstablecerClaves(password1_device, password2_device);
                                                    conectar.ComprobarConexion(conectar_con_el_dispositivo);

                                                    // Enviamos el mensaje
                                                    switch (conectar.ConectarConElDispositivo())
                                                    {
                                                        // En caso de que la informacion haya sido enviada con exito
                                                        case DispositivoConexion.DIRECCIONIP_VACIA:
                                                            Toast.makeText(getContext(), "Error: direccion IP vacia", Toast.LENGTH_SHORT).show();
                                                            break;

                                                        case DispositivoConexion.ERROR_EN_EL_PUERTO:
                                                            Toast.makeText(getContext(), "Error: el puerto ingresado es erroneo", Toast.LENGTH_SHORT).show();
                                                            break;

                                                        case DispositivoConexion.LAS_CLAVES_NO_COINCIDEN:
                                                            Toast.makeText(getContext(), "Error: las claves no concuerdan", Toast.LENGTH_SHORT).show();
                                                            break;

                                                        case DispositivoConexion.UNA_CLAVE_ESTA_VACIA:
                                                            Toast.makeText(getContext(), "Error: alguna clave esta vacia", Toast.LENGTH_SHORT).show();
                                                            break;

                                                        case DispositivoConexion.NO_SE_PUEDE_CONECTAR_POR_CONEXION_EXISTENTE:
                                                            Toast.makeText(getContext(), "Error: ya hay una conexion activa", Toast.LENGTH_SHORT).show();
                                                            break;

                                                        case DispositivoConexion.INFORMACION_SASTIFACTORIA:
                                                            if (recordar_dispositivo)
                                                            {
                                                                // Obtenemos la conexion
                                                                DispositivoConexion.Conectar conexion = DispositivoConexion.UltimaConexion;

                                                                // Si la conexion no es nula
                                                                if (conexion != null)
                                                                {
                                                                    // Añadimos el item a la base de datos
                                                                    if (DispositivoConexion.GuardarDispositivo(getContext(), conectar))
                                                                    {
                                                                        // Obtenemos el ultimo dispositivo
                                                                        DispositivosIP dispositivosIP = DispositivoConexion.ObtenerUltimoDispositivo(getContext());

                                                                        // Comprobamos que no haya error
                                                                        if  (dispositivosIP != null)
                                                                        {
                                                                            // Obtenemos el fragment
                                                                            DeviceListFragment deviceListFragment = deviceListViewPageAdapter.ObtenerFragmenDeseado(DeviceListViewPageAdapter.TODOS_LOS_DISPOSITIVOS);
                                                                            // Dispositivo Alamacenado con exito
                                                                            deviceListFragment.AñadirDispositivo(dispositivosIP);
                                                                            // Mostramos el mensaje
                                                                            Toast.makeText(getContext(), "Dispositivo almacenado con exito", Toast.LENGTH_SHORT).show();
                                                                        }
                                                                        else
                                                                            Toast.makeText(getContext(), "Error: no se puede recuperar los datos del dispositivo", Toast.LENGTH_SHORT).show();
                                                                    }
                                                                    else
                                                                        Toast.makeText(getContext(), "Error: no se ha podido almacenar los datos", Toast.LENGTH_SHORT).show();

                                                                    alertDialog.dismiss();
                                                                }
                                                            }
                                                            else
                                                                Toast.makeText(getContext(), "Error: no se hace nada con la informacion", Toast.LENGTH_SHORT).show();

                                                            break;
                                                    }
                                                }
                                            })
                                            .setNegativeButton("No", null)
                                            .show();
                                }
                                else
                                {
                                    // Valores del formulario
                                    String IP_device = et_direccionIP.getText().toString();
                                    String puerto_device = et_puerto.getText().toString();
                                    String nombre_device = et_nombrePC.getText().toString();
                                    String password1_device = et_passwordPC1.getText().toString();
                                    String password2_device = et_passwordPC2.getText().toString();

                                    boolean conectar_con_el_dispositivo = cbx_conectar.isChecked();
                                    boolean recordar_dispositivo = cbx_recordar.isChecked();

                                    // Conexion con el dispositivo
                                    DispositivoConexion.Conectar conectar = DispositivoConexion.TratarConcexion(getContext(), IP_device, puerto_device);

                                    // Establecemos el resto de la informacion
                                    conectar.EstablecerNombre(nombre_device);
                                    conectar.EstablecerClaves(password1_device, password2_device);
                                    conectar.ComprobarConexion(conectar_con_el_dispositivo);

                                    // Enviamos el mensaje
                                    switch (conectar.ConectarConElDispositivo())
                                    {
                                        // En caso de que la informacion haya sido enviada con exito
                                        case DispositivoConexion.DIRECCIONIP_VACIA:
                                            Toast.makeText(getContext(), "Error: direccion IP vacia", Toast.LENGTH_SHORT).show();
                                            break;

                                        case DispositivoConexion.ERROR_EN_EL_PUERTO:
                                            Toast.makeText(getContext(), "Error: el puerto ingresado es erroneo", Toast.LENGTH_SHORT).show();
                                            break;

                                        case DispositivoConexion.LAS_CLAVES_NO_COINCIDEN:
                                            Toast.makeText(getContext(), "Error: las claves no concuerdan", Toast.LENGTH_SHORT).show();
                                            break;

                                        case DispositivoConexion.UNA_CLAVE_ESTA_VACIA:
                                            Toast.makeText(getContext(), "Error: alguna clave esta vacia", Toast.LENGTH_SHORT).show();
                                            break;

                                        case DispositivoConexion.NO_SE_PUEDE_CONECTAR_POR_CONEXION_EXISTENTE:
                                            Toast.makeText(getContext(), "Error: ya hay una conexion activa", Toast.LENGTH_SHORT).show();
                                            break;

                                        case DispositivoConexion.INFORMACION_SASTIFACTORIA:
                                            if (recordar_dispositivo)
                                            {
                                                // Obtenemos la conexion
                                                DispositivoConexion.Conectar conexion = DispositivoConexion.UltimaConexion;

                                                // Si la conexion no es nula
                                                if (conexion != null)
                                                {
                                                    // Añadimos el item a la base de datos
                                                    if (DispositivoConexion.GuardarDispositivo(getContext(), conectar))
                                                    {
                                                        // Obtenemos el ultimo dispositivo
                                                        DispositivosIP dispositivosIP = DispositivoConexion.ObtenerUltimoDispositivo(getContext());

                                                        // Comprobamos que no haya error
                                                        if  (dispositivosIP != null)
                                                        {
                                                            // Obtenemos el fragment
                                                            DeviceListFragment deviceListFragment = deviceListViewPageAdapter.ObtenerFragmenDeseado(DeviceListViewPageAdapter.TODOS_LOS_DISPOSITIVOS);
                                                            // Dispositivo Alamacenado con exito
                                                            deviceListFragment.AñadirDispositivo(dispositivosIP);
                                                            // Mostramos el mensaje
                                                            Toast.makeText(getContext(), "Dispositivo almacenado con exito", Toast.LENGTH_SHORT).show();
                                                        }
                                                        else
                                                            Toast.makeText(getContext(), "Error: no se puede recuperar los datos del dispositivo", Toast.LENGTH_SHORT).show();
                                                    }
                                                    else
                                                        Toast.makeText(getContext(), "Error: no se ha podido almacenar los datos", Toast.LENGTH_SHORT).show();

                                                    alertDialog.dismiss();
                                                }
                                            }
                                            else
                                                Toast.makeText(getContext(), "Error: no se hace nada con la informacion", Toast.LENGTH_SHORT).show();

                                            break;
                                    }
                                }

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
