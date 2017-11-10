package lemus.com.bast_software.controlremoto;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
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
            DispositivosIP dispositivosIP = dispositivosIPs.get(getAdapterPosition());

            Toast.makeText(v.getContext(), "Modificar IP: " + dispositivosIP.getId(), Toast.LENGTH_SHORT).show();
        }

        return true;
    }
}
