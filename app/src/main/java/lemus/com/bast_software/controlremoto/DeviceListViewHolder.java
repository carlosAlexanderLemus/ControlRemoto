package lemus.com.bast_software.controlremoto;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import lemus.com.bast_software.controlremoto.ConexionRed.DispositivoConexion;

/**
 * Created by mekaku on 08/11/2017.
 */

public class DeviceListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

    // Lista de dispositivo
    private List<DispositivosIP> dispositivosIPs;
    private View card_view;
    private ImageView iv_favorite;
    private DispositivosIPItemAdapter adapter;
    private DeviceListFragment deviceListFragment;

    public DeviceListViewHolder(View itemView, List<DispositivosIP> dispositivosIPs, DispositivosIPItemAdapter adapter) {
        super(itemView);
        // Guardamos la vista
        card_view = itemView;
        // Obtenemos la informacion
        itemView.setOnClickListener(this);
        itemView.setOnLongClickListener(this);

        this.dispositivosIPs = dispositivosIPs;
        this.adapter = adapter;
    }

    // Establecemos el device
    public void setDeviceListFragment(DeviceListFragment deviceListFragment) {
        this.deviceListFragment = deviceListFragment;
    }

    // Establecer la informacion
    public void EstablecerInformacion(int position)
    {
        // Obtenemos la referencia al objeto
        DispositivosIP dispositivosIP = dispositivosIPs.get(position);

        // Referencia a los edit text
        TextView tv_device_ip = (TextView)itemView.findViewById(R.id.tv_device_ip_item);
        TextView tv_device_name = (TextView)itemView.findViewById(R.id.tv_device_name_item);

        // Informacion de la image
        iv_favorite = (ImageView)itemView.findViewById(R.id.iv_favorite_item_device);

        iv_favorite.setOnClickListener(this);

        // Establecemos el nombre
        tv_device_ip.setText("IP: " + dispositivosIP.getIP());
        tv_device_name.setText(dispositivosIP.getNombre());

        // Sabremos que peto con la existencia
        if (dispositivosIP.getFavoritos())
            iv_favorite.setImageResource(android.R.drawable.btn_star_big_on);
        else
            iv_favorite.setImageResource(android.R.drawable.btn_star_big_off);

    }

    @Override
    public void onClick(View v) {
        // Obtenemos el dispositivo actual
        final DispositivosIP dispositivosIP = dispositivosIPs.get(getAdapterPosition());

        // Si la vista es igual a la card
        if (v.equals(card_view)){
            boolean poder_establecer_conexion = true;

            // Revisamos que haya alguna conexion previa
            if (DispositivoConexion.HayConexionEstablecida())
            {
                // Revisamos que la conexion no sea con esta IP
                if (DispositivoConexion.ObtenerDispositivoActual().getId() == dispositivosIP.getId())
                {
                    // Al ser lo mismo ya no puede establecer la conexion
                    poder_establecer_conexion = false;
                    // Mostramos el mensaje de conexion
                }
            }

            if (poder_establecer_conexion)
            {
                // En caso que queramos seleccionar el dispositivo
                Context context = v.getContext();
                AlertDialog.Builder builder = new AlertDialog.Builder(context);

                // Obtenemos el layout inlfater
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View layout = inflater.inflate(R.layout.select_device_item_layout, null);

                // Obtenemos los texto
                TextView tv_name = (TextView)layout.findViewById(R.id.tv_selected_name_device);
                TextView tv_ip = (TextView)layout.findViewById(R.id.tv_selected_ip_device);

                // Obtenemos los botones
                Button btn_conectar = (Button)layout.findViewById(R.id.btn_aceptar_conexion);
                Button btn_cancelar = (Button)layout.findViewById(R.id.btn_cancelar_conexion);

                tv_name.setText(dispositivosIP.getNombre());
                tv_ip.setText(dispositivosIP.getIP());

                builder.setView(layout);

                final AlertDialog dialog = builder.create();

                // Manejamos los botones
                btn_cancelar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Cerramos la conexion
                        dialog.dismiss();
                    }
                });

                btn_conectar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Comprobamos que haya una conexion previa
                        if (DispositivoConexion.HayConexionEstablecida())
                        {
                            // Al haber una conexion mostramos un mensaje
                            AlertDialog.Builder comprobar_conexion = new AlertDialog.Builder(v.getContext());
                            comprobar_conexion.setTitle("Ya existe una conexion previa")
                                    .setMessage("¿Desea establecer una conexion?")
                                    .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            // Quitamos la conexion previa y establecemos una nueva conexion
                                            deviceListFragment.ObtenerFragmentPadre().EstablecerNuevaConexion(dispositivosIP);
                                        }
                                    })
                                    .setNegativeButton("No", null)
                                    .show();
                        }
                        else
                        {
                            // Quitamos la conexion previa y establecemos una nueva conexion
                            deviceListFragment.ObtenerFragmentPadre().EstablecerNuevaConexion(dispositivosIP);
                        }

                        // Cerramos el dialofo
                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
            else
            {
                // En caso que queramos seleccionar el dispositivo
                Context context = v.getContext();
                AlertDialog.Builder builder = new AlertDialog.Builder(context);

                // Obtenemos el layout inlfater
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View layout = inflater.inflate(R.layout.layout_selected_item_device, null);

                // Obtenemos los texto
                TextView tv_name = (TextView)layout.findViewById(R.id.tv_selected_name_device);
                TextView tv_ip = (TextView)layout.findViewById(R.id.tv_selected_ip_device);

                // Obtenemos los botones
                Button btn_cancelar = (Button)layout.findViewById(R.id.btn_cancelar_desconexion);
                Button btn_desconectar = (Button)layout.findViewById(R.id.btn_desconectar);

                tv_name.setText(dispositivosIP.getNombre());
                tv_ip.setText(dispositivosIP.getIP());

                builder.setView(layout);

                final AlertDialog dialog = builder.create();

                btn_desconectar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Nos desconectamos
                        deviceListFragment.ObtenerFragmentPadre().Desconectar(dispositivosIP);
                       // Quitamos el dialogo
                       dialog.dismiss();
                    }
                });

                btn_cancelar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Quitamos el dialogo
                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        }
        else if (v.equals(iv_favorite))
        {
            dispositivosIP.setFavoritos(!dispositivosIP.isFavoritos());

            // Si modificamos el dispositivo
            if (DispositivoConexion.ModificarDispositivoFavorito(v.getContext(), dispositivosIP.isFavoritos(), dispositivosIP.getId()))
            {
                // en caso que sea el principal
                if (DispositivoConexion.HayConexionEstablecida())
                {
                    // En caso que sea el mismo id
                    if (DispositivoConexion.ObtenerDispositivoActual().getId() == dispositivosIP.getId())
                    {
                        // Actualizamos la informacion del dispositivo
                        DispositivoConexion.ActualizarDispositivo(dispositivosIP);
                        // Cambiamos la informacion del padre
                        deviceListFragment.ObtenerFragmentPadre().CambiarFavoritismosEnDispositivo(dispositivosIP.isFavoritos());
                    }
                }

                // En caso de que estemos en la opcion de fevoritos
                if (adapter.getTipoDeAccion() == DeviceListViewPageAdapter.LOS_DISPOSITIVOS_FAVORITOS)
                {
                    // Obtenemos la posicion actual del item
                    int position = getAdapterPosition();
                    // Eliminamos tanto de la lista como de
                    dispositivosIPs.remove(position);
                    // Notificamos del error
                    adapter.notifyItemRemoved(position);

                }

                // Actualizamos a los demas controladores
                deviceListFragment.ObtenerFragmentPadre().ActualizarTodosLosDispositivos(adapter.getTipoDeAccion(), dispositivosIP.Clonar());

                // Mensaje sastifactorio
                Toast.makeText(v.getContext(), "Dispositivo modificado con exito", Toast.LENGTH_SHORT).show();
            }
            else
                dispositivosIP.setFavoritos(!dispositivosIP.isFavoritos());

            // En caso que queramos modificar el estado de favoritismo
            if (!dispositivosIP.isFavoritos())
                iv_favorite.setImageResource(android.R.drawable.btn_star_big_off);
            else
                iv_favorite.setImageResource(android.R.drawable.btn_star_big_on);
        }
    }

    @Override
    public boolean onLongClick(View v) {
        if (v.equals(card_view)) {
            // Obtenemos la posicion actual del item
            final int position = getAdapterPosition();
            // Obtenemos el dispositivo actual
            final DispositivosIP dispositivosIP = dispositivosIPs.get(position);

            boolean modificar_o_eliminar = true;

            // Revisamos que haya alguna conexion previa
            if (DispositivoConexion.HayConexionEstablecida())
            {
                // Revisamos que la conexion no sea con esta IP
                if (DispositivoConexion.ObtenerDispositivoActual().getId() == dispositivosIP.getId())
                {
                    // Al ser lo mismo ya no puede establecer la conexion
                    modificar_o_eliminar = false;
                    // Mostramos el mensaje de conexion
                }
            }

            // Si podemos modificar o eliminar el dispositivo
            if (modificar_o_eliminar)
            {
                // Obtenemos el contexto
                Context context = v.getContext();
                // Obtenemos en menu PopUp
                final PopupWindow popupWindow = new PopupWindow(context);
                // Obtenemos el layout inflare
                LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                // Creamos la vista
                View view = inflater.inflate(R.layout.popup_device_action_layout, null);
                // Especificamos el dispositivo actual
                popupWindow.setFocusable(true);
                popupWindow.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
                popupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
                popupWindow.setContentView(view);
                // Obtenemos los botones
                Button btn_modificar = (Button)view.findViewById(R.id.btn_device_modifire);
                Button btn_eliminar = (Button)view.findViewById(R.id.btn_device_delete);
                // Eliminamos el elemento
                btn_eliminar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Si hemos podido eliminar con exito
                        if (DispositivoConexion.EliminarDispositivoPorID(v.getContext(), dispositivosIP.getId()))
                        {
                            // Eliminamos el dispositivo
                            deviceListFragment.ObtenerFragmentPadre().EliminarDispositivo(adapter.getTipoDeAccion(), dispositivosIP.Clonar());
                            // Eliminamos tanto de la lista como de
                            dispositivosIPs.remove(position);
                            // Notificamos del error
                            adapter.notifyItemRemoved(position);
                        }
                        else
                            Toast.makeText(v.getContext(), "No se ha podido eliminar el dispositivo", Toast.LENGTH_SHORT).show();

                        // quitamos el pop up
                        popupWindow.dismiss();
                    }
                });
                // En caso que queramos modificar
                btn_modificar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Obtenemos la posicion actual del item
                        final int position = getAdapterPosition();
                        // Obtenemos el dispositivo actual
                        final DispositivosIP dispositivosIP = dispositivosIPs.get(position);
                        // Creamos el boul
                        AlertDialog.Builder build = new AlertDialog.Builder(v.getContext());
                        // Obtenemos el layout inflare
                        LayoutInflater inflater = (LayoutInflater)v.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        // Especificamos el inflate
                        View view = inflater.inflate(R.layout.modifier_device_item_layout, null);
                        // Especificamos la union
                        build.setView(view);
                        // Creamos el alert dialog
                        final AlertDialog alert = build.create();
                        // EditText
                        final EditText et_name_item_modifire = (EditText)view.findViewById(R.id.et_name_device_modifier);
                        final EditText et_ip_item_modifire = (EditText)view.findViewById(R.id.et_ip_device_modifire);
                        final EditText et_port_item_modifire = (EditText)view.findViewById(R.id.et_port_device_modifire);
                        final EditText et_pass_item_modifire = (EditText)view.findViewById(R.id.et_password_device_modifire);
                        CheckBox cb_mostrar_clave_item = (CheckBox)view.findViewById(R.id.cb_ver_clave);
                        // Cambiar el comboboc
                        cb_mostrar_clave_item.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                if (isChecked)
                                    et_pass_item_modifire.setInputType(InputType.TYPE_CLASS_TEXT);
                                else
                                    et_pass_item_modifire.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                            }
                        });
                        // Los datos por defecto
                        String name = v.getContext().getResources().getString(R.string.text_name_device_default);
                        int port = Integer.parseInt(v.getContext().getResources().getString(R.string.port_default));
                        // Comprobamos que no sean iguales
                        if (!dispositivosIP.getNombre().equals(name))
                            et_name_item_modifire.setText(dispositivosIP.getNombre());
                        if (dispositivosIP.getPuerto() != port)
                            et_port_item_modifire.setText("" + dispositivosIP.getPuerto());
                        // Los valor que pueden ser nulo
                        et_ip_item_modifire.setText(dispositivosIP.getIP());
                        et_pass_item_modifire.setText(dispositivosIP.getClave());
                        // Boton de modificar
                        Button btn_modificar_item = (Button) view.findViewById(R.id.btn_acept_device_modifier);
                        // Boton eliminar
                        Button btn_cancelar_item = (Button)view.findViewById(R.id.btn_cancel_device_modifier);
                        // Modificamos los datos
                        btn_modificar_item.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // Obtenemos los datos
                                String ip_item = et_ip_item_modifire.getText().toString();
                                String name_item = et_name_item_modifire.getText().toString();
                                String port_item = et_port_item_modifire.getText().toString();
                                String pass_item = et_pass_item_modifire.getText().toString();

                                // Modificaremos los datos
                                DispositivoConexion.Conectar conectar = DispositivoConexion.EstablecerConecion(v.getContext(), ip_item, port_item, name_item, pass_item);

                                // Modificamos los datos
                                switch (conectar.ModificarDispositivoPorId(dispositivosIP.getId()))
                                {
                                    // Vemos todos los casos de errores
                                    case DispositivoConexion.MODIFICACION_EXITOSA:
                                        // Si se ha podido modificar
                                        dispositivosIP.Actualizar(conectar);
                                        adapter.notifyItemChanged(position, dispositivosIP);

                                        // Modificamos todos los otros item
                                        deviceListFragment.ObtenerFragmentPadre().ActualizarTodosLosDispositivos(adapter.getTipoDeAccion(), dispositivosIP.Clonar());

                                        Toast.makeText(v.getContext(), "Dispositivo actualizado", Toast.LENGTH_SHORT).show();
                                        alert.dismiss();
                                        break;
                                    case DispositivoConexion.DIRECCIONIP_VACIA:
                                        Toast.makeText(v.getContext(), "Error: direccion IP vacia", Toast.LENGTH_SHORT).show();
                                        break;
                                    case DispositivoConexion.ERROR_EN_EL_PUERTO:
                                        Toast.makeText(v.getContext(), "Error: inesperado en el puerto", Toast.LENGTH_SHORT).show();
                                        break;
                                    case DispositivoConexion.FALLO_AL_MODIFICAR:
                                        Toast.makeText(v.getContext(), "Error: inesperado al guardar la informacion", Toast.LENGTH_SHORT).show();
                                        break;
                                }
                            }
                        });
                        // Cancelamos la modificacion
                        btn_cancelar_item.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // Cerramos el dialogo
                                alert.dismiss();
                            }
                        });
                        // Ceramos el prop
                        popupWindow.dismiss();
                        // Iniciamos el alert
                        alert.show();
                    }
                });
                // Mostamos el elemento
                popupWindow.showAsDropDown(v, 0, 0);
            }
            else
            {
                // Opcion para desconectar el dispositivo
                // Obtenemos el contexto
                Context context = v.getContext();
                // Obtenemos en menu PopUp
                final PopupWindow popupWindow = new PopupWindow(context);
                // Obtenemos el layout inflare
                LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                // Creamos la vista
                View view = inflater.inflate(R.layout.layout_popup_device_selected_accion, null);
                // Especificamos el dispositivo actual
                popupWindow.setFocusable(true);
                popupWindow.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
                popupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
                popupWindow.setContentView(view);

                // Eliminamos y desconectamos
                Button btn_eliminar_desconectar = (Button)view.findViewById(R.id.btn_device_disconect_delete);

                // Quitamos el coso
                btn_eliminar_desconectar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        // Nos desconectamos
                        deviceListFragment.ObtenerFragmentPadre().DesconectarConEliminacion(dispositivosIP);
                        // Quitamos el coso
                        popupWindow.dismiss();
                    }
                });

                // Mostamos el elemento
                popupWindow.showAsDropDown(v, 0, 0);
            }
        }

        return true;
    }
}
