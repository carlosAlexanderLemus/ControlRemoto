package lemus.com.bast_software.controlremoto;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by mekaku on 08/11/2017.
 */

public class DeviceListViewHolder extends RecyclerView.ViewHolder {

    // Lista de dispositivo
    private List<DispositivosIP> dispositivosIPs;

    public DeviceListViewHolder(View itemView, List<DispositivosIP> dispositivosIPs) {
        super(itemView);

        this.dispositivosIPs = dispositivosIPs;
    }

    // Establecer la informacion
    public void EstablecerInformacion(int position)
    {
        // Obtenemos la referencia al objeto
        DispositivosIP dispositivosIP = dispositivosIPs.get(position);

        // Referencia a los edit text
        TextView tv_device_ip = (TextView)itemView.findViewById(R.id.tv_device_ip_item);
        TextView tv_device_name = (TextView)itemView.findViewById(R.id.tv_device_name_item);

        // Establecemos el nombre
        tv_device_ip.setText("IP: " + dispositivosIP.getIP());
        tv_device_name.setText(dispositivosIP.getNombre());

        // Sabremos que peto con la existencia
        if (dispositivosIP.getFavoritos())
        {
            // En caso que sea un favorito
            ImageView iv_star = (ImageView)itemView.findViewById(R.id.iv_favorite_item_device);
            iv_star.setImageResource(android.R.drawable.btn_star_big_on);
        }
    }
}
