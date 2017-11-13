package lemus.com.bast_software.controlremoto;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.List;

import lemus.com.bast_software.controlremoto.ConexionRed.DispositivoConexion;

/**
 * Created by mekaku on 08/11/2017.
 */

public class DispositivosIPItemAdapter extends RecyclerView.Adapter<DeviceListViewHolder> {
    private List<DispositivosIP> dispositivosIPs;
    private Context context;
    private int TipoDeAccion;

    public DispositivosIPItemAdapter(Context context, List<DispositivosIP> dispositivosIPs, int TipoDeAccion) {
        this.dispositivosIPs = dispositivosIPs;
        this.context = context;
        this.TipoDeAccion = TipoDeAccion;
    }

    @Override
    public DeviceListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_device_item, parent, false);
        // Al crear la lista
        return new DeviceListViewHolder(vista, dispositivosIPs, this);
    }

    @Override
    public void onBindViewHolder(DeviceListViewHolder holder, int position) {
        holder.EstablecerInformacion(position);

    }

    // Modificar ite
    public void ModificarItemConDispositivoIP(DispositivosIP dispositivosIP)
    {
        switch (TipoDeAccion)
        {
            // En caso que tratemos con los favoritos
            case DeviceListViewPageAdapter.LOS_DISPOSITIVOS_FAVORITOS:
                // Si poseemos datos
                if (dispositivosIPs.size() > 0)
                {
                    // Sabremos que si alguno concordo
                    boolean encontro_dispositivo_parecido = false;
                    boolean eliminar_dispositivo = false;
                    int indice_item = -1;

                    // Repasaremos la lista de los dispositvos
                    for (int i = 0; i < dispositivosIPs.size(); i++)
                    {
                        // Obtenemos el dispositivo
                        DispositivosIP device = dispositivosIPs.get(i);

                        // Comprobamos que las ip concuerden si es haci modificamos la ip
                        if (device.getId() == dispositivosIP.getId())
                        {
                            // Al encontrar el dispositivo
                            encontro_dispositivo_parecido = true;
                            // Ahora vemos si esta en favorito
                            if (dispositivosIP.isFavoritos())
                            {
                                // Cambiamos el favoritismo
                                device.CopiarDatosDispositivoIP(dispositivosIP);
                                // Notificamos el cambio
                                notifyItemChanged(i);
                            }
                            else {
                                // En caso de que sea negativo lo quitamos
                                eliminar_dispositivo = true;
                                indice_item = i;
                            }

                            // Rompemos el ciclo for
                            break;
                        }
                    }

                    // En caso de que no exista el dispositivo
                    if (!encontro_dispositivo_parecido)
                    {
                        // Añadimos aquel dispositivo IP
                        AñadirDispositivo(dispositivosIP.Clonar());
                    }
                    else if (eliminar_dispositivo)
                    {
                        // Como encontro datos y los tenemos que eliminar
                        dispositivosIPs.remove(indice_item);
                        notifyItemRemoved(indice_item);
                    }
                }
                else
                {
                    // Lo almacenamos en caso que sea un favorito
                    if (dispositivosIP.isFavoritos())
                    {
                        // Al no poseer datos tenemos que agregarlo
                        AñadirDispositivo(dispositivosIP.Clonar());
                    }
                }
                break;

            //
            case DeviceListViewPageAdapter.TODOS_LOS_DISPOSITIVOS:
                for (int i = 0; i < dispositivosIPs.size(); i++)
                {
                    // Obtenemos el dispositivo
                    DispositivosIP device = dispositivosIPs.get(i);

                    // Comprobamos que posean el mismo id
                    if (device.getId() == dispositivosIP.getId())
                    {
                        // Copiamos los datos
                        device.CopiarDatosDispositivoIP(dispositivosIP);
                        // Re cargamos
                        notifyItemChanged(i);
                        // Salimos del bucle
                        break;
                    }
                }
                break;
        }

    }

    public void AñadirDispositivo(DispositivosIP dispositivosIP)
    {
        // Añadimos un nuevo dispositivo
        dispositivosIPs.add(dispositivosIP);
        notifyItemInserted(dispositivosIPs.size() - 1);
    }

    @Override
    public int getItemCount() {
        return dispositivosIPs.size();
    }

    public int getTipoDeAccion() {
        return TipoDeAccion;
    }
}
