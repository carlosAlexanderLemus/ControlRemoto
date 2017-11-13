package lemus.com.bast_software.controlremoto;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mekaku on 07/11/2017.
 */

public class DeviceListViewPageAdapter extends FragmentStatePagerAdapter {
    // Guardamos el dispositivo
    private DeviceFragment deviceFragment;
    private List<DeviceListFragment> deviceListFragments;

    // Constantes referente a las posicion
    public static final int TODOS_LOS_DISPOSITIVOS = 0;
    public static final int LOS_DISPOSITIVOS_RECIENTES = 1;
    public static final int LOS_DISPOSITIVOS_FAVORITOS = 2;

    public DeviceListViewPageAdapter(DeviceFragment deviceFragment) {
        // Usamos el fragment manager
        super(deviceFragment.getFragmentManager());

        // Establecemos el dispositivo
        this.deviceFragment = deviceFragment;

        deviceListFragments = new ArrayList<DeviceListFragment>();

        // Creamos el fragment
        DeviceListFragment todos_los_item = new DeviceListFragment();
        todos_los_item.establecerInformacion(TODOS_LOS_DISPOSITIVOS);

        DeviceListFragment los_mas_usados = new DeviceListFragment();

        DeviceListFragment todos_los_item_favoritos = new DeviceListFragment();
        todos_los_item_favoritos.establecerInformacion(LOS_DISPOSITIVOS_FAVORITOS);

        // Establecemos el device padre
        todos_los_item.EstablecerFragmentPadre(deviceFragment);
        los_mas_usados.EstablecerFragmentPadre(deviceFragment);
        todos_los_item_favoritos.EstablecerFragmentPadre(deviceFragment);

        // Obtenemos la lista de los datos
        // Todos los item
        deviceListFragments.add(todos_los_item);
        // Los recientes
        deviceListFragments.add(los_mas_usados);
        // Los favoritos
        deviceListFragments.add(todos_los_item_favoritos);
    }

    @Override
    public Fragment getItem(int position) {
        return deviceListFragments.get(position);
    }

    @Override
    public int getCount() {
        return 3;
    }

    // Obtenemos el fragment deseado
    public DeviceListFragment ObtenerFragmenDeseado(int position)
    {
        // En caso que deverdad este entre los limetes
        if (position >= 0 && position < 3)
        {
            return deviceListFragments.get(position);
        }
        else
            return null;
    }

    // Modificar dispositivos por ID
    public void ModificarItemsDispositivos(DispositivosIP dispositivosIP)
    {
        // Obtenemos todos los fragments para luego buscar es sus hijos
        for (DeviceListFragment device : deviceListFragments) {
            // Manejo de los errores
            try
            {
                // A cada fragment le modificamos el item
                device.ModificarItemPorDispositivos(dispositivosIP.Clonar());
            }
            catch (Exception ex)
            {
                Log.d("ErroCambiarValor", ex.getMessage());
            }
        }
    }
}
