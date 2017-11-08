package lemus.com.bast_software.controlremoto;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class DeviceListFragment extends Fragment {


    public DeviceListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_device_list, container, false);

        // Informacion
        ArrayList<DispositivosIP> dispositivosIPs = new ArrayList<DispositivosIP>();

        for (int i = 1; i <= 10; i++)
        {
            dispositivosIPs.add(new DispositivosIP(getContext(), i, "192.168.0."+i, i + 11000, (i % 2) == 0));
        }

        // Obtenemos el recicleview
        RecyclerView recyclerView = (RecyclerView)view.findViewById(R.id.recycle_contenedor);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerView.setAdapter(new DispositivosIPItemAdapter(dispositivosIPs));
        recyclerView.setLayoutManager(linearLayoutManager);

        return view;
    }

}
