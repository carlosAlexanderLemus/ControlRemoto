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
