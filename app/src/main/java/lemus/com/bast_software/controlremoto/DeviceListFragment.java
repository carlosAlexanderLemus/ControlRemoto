package lemus.com.bast_software.controlremoto;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import lemus.com.bast_software.controlremoto.ConexionRed.DispositivoConexion;


/**
 * A simple {@link Fragment} subclass.
 */
public class DeviceListFragment extends Fragment {

    // Informacion
    public ArrayList<DispositivosIP> dispositivosIPs;
    private DispositivosIPItemAdapter dispositivosIPItemAdapter;

    private int TipoDeAccion = -1;

    public DeviceListFragment() {
        // Required empty public constructor
        // Creamos la lista
        dispositivosIPs = new ArrayList<DispositivosIP>();
    }

    public void establecerInformacion(int tipo_dispositivo) {
        // Required empty public constructor
        // Creamos la lista
        TipoDeAccion = tipo_dispositivo;
    }

    public void ModificarItemPorDispositivos(DispositivosIP dispositivosIP)
    {
        // Modificamos el adaptador
        dispositivosIPItemAdapter.ModificarItemConDispositivoIP(dispositivosIP);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_device_list, container, false);

        // Obtenemos el recicleview
        RecyclerView recyclerView = (RecyclerView)view.findViewById(R.id.recycle_contenedor);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        // Obtenemos todos los dispositivos
        switch (TipoDeAccion)
        {
            case DeviceListViewPageAdapter.TODOS_LOS_DISPOSITIVOS:
                dispositivosIPs = DispositivoConexion.ObtenerTodasLasDireccionesIP(getContext());
                break;
            case DeviceListViewPageAdapter.LOS_DISPOSITIVOS_FAVORITOS:
                dispositivosIPs = DispositivoConexion.ObtenerTodasLasDireccionesIPFavoritas(getContext());
                break;
        }

        // Creamos el adaptador pero con el tipo de accion indicado
        dispositivosIPItemAdapter = new DispositivosIPItemAdapter(getContext(), dispositivosIPs, TipoDeAccion);

        recyclerView.setAdapter(dispositivosIPItemAdapter);
        recyclerView.setLayoutManager(linearLayoutManager);

        return view;
    }

    // Añadimos los datos
    public void AñadirDispositivo(DispositivosIP dispositivosIP)
    {
        dispositivosIPItemAdapter.AñadirDispositivo(dispositivosIP);
    }
}
