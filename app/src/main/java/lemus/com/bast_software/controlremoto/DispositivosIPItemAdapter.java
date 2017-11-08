package lemus.com.bast_software.controlremoto;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by mekaku on 08/11/2017.
 */

public class DispositivosIPItemAdapter extends RecyclerView.Adapter<DeviceListViewHolder> {
    private List<DispositivosIP> dispositivosIPs;

    public DispositivosIPItemAdapter(List<DispositivosIP> dispositivosIPs) {
        this.dispositivosIPs = dispositivosIPs;
    }

    @Override
    public DeviceListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_device_item, parent, false);
        return new DeviceListViewHolder(vista, dispositivosIPs);
    }

    @Override
    public void onBindViewHolder(DeviceListViewHolder holder, int position) {
        holder.EstablecerInformacion(position);
    }

    @Override
    public int getItemCount() {
        return dispositivosIPs.size();
    }
}
