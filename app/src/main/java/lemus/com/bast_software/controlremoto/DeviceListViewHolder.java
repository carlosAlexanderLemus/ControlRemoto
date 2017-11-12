package lemus.com.bast_software.controlremoto;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

    }

    @Override
    public void onClick(View v) {
        // Obtenemos el dispositivo actual
        DispositivosIP dispositivosIP = dispositivosIPs.get(getAdapterPosition());

        // Si la vista es igual a la card
        if (v.equals(card_view)){
            // En caso que queramos seleccionar el dispositivo
            Context context = v.getContext();
            AlertDialog.Builder builder = new AlertDialog.Builder(context);

            // Obtenemos el layout inlfater
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View layout = inflater.inflate(R.layout.select_device_item_layout, null);

            // Obtenemos los texto
            TextView tv_name = (TextView)layout.findViewById(R.id.tv_selected_name_device);
            TextView tv_ip = (TextView)layout.findViewById(R.id.tv_selected_ip_device);

            tv_name.setText(dispositivosIP.getNombre());
            tv_ip.setText(dispositivosIP.getIP());

            builder.setView(layout);

            final AlertDialog dialog = builder.create();

            dialog.show();
        }
        else if (v.equals(iv_favorite))
        {
            // En caso que queramos modificar el estado de favoritismo
            if (dispositivosIP.isFavoritos())
                iv_favorite.setImageResource(android.R.drawable.btn_star_big_off);
            else
                iv_favorite.setImageResource(android.R.drawable.btn_star_big_on);

            dispositivosIP.setFavoritos(!dispositivosIP.isFavoritos());

            if (DispositivoConexion.ModificarDispositivoFavorito(v.getContext(), dispositivosIP.isFavoritos(), dispositivosIP.getId()))
            {
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
                // Mensaje sastifactorio
                Toast.makeText(v.getContext(), "Dispositivo modificado con exito", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public boolean onLongClick(View v) {
        if (v.equals(card_view)) {
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
                    // Obtenemos la posicion actual del item
                    int position = getAdapterPosition();
                    // Obtenemos el dispositivo actual
                    DispositivosIP dispositivosIP = dispositivosIPs.get(position);
                    // Si hemos podido eliminar con exito
                    if (DispositivoConexion.EliminarDispositivoPorID(v.getContext(), dispositivosIP.getId()))
                    {
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
                    int position = getAdapterPosition();
                    // Obtenemos el dispositivo actual
                    DispositivosIP dispositivosIP = dispositivosIPs.get(position);
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
                    String name = v.getContext().getResources().getString(R.string.ip_address_default);
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
                            // Modificaremos los datos

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

        return true;
    }
}
