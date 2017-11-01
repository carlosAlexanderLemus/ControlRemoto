package lemus.com.bast_software.controlremoto;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.util.zip.Inflater;


/**
 * A simple {@link Fragment} subclass.
 */
public class DeviceFragment extends Fragment {


    public DeviceFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_device, container, false);
        final LayoutInflater layoutInflater = getLayoutInflater(savedInstanceState);
        // Obtenemos la informacion
        BottomNavigationView bottomNavigationView = (BottomNavigationView)view.findViewById(R.id.bottom_device);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                // Obtenemos el ID
                int id = item.getItemId();

                // Comprobamos cual es el id de origen
                switch (id){
                    // En caso que tengamos que agregar un nuevo dispositivo
                    case R.id.item_bottom_add_device:
                        // Creamos el dialogo
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                        // Escogemos el layout
                        View view_layout = layoutInflater.inflate(R.layout.layout_add_device, null);

                        // Obtenemos la referencia a los botonre
                        Button btn_acepat = (Button)view_layout.findViewById(R.id.btn_conectar_dispositivo);
                        Button btn_cancelar = (Button)view_layout.findViewById(R.id.btn_cancelar_agregar_dispositivos);

                        // Lo establecemos como el layout
                        builder.setView(view_layout);

                        // Creamos el alerta
                        final AlertDialog alertDialog = builder.create();

                        // Acciones a realixar a seleccionar cualquis dispositivo
                        btn_acepat.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                // Quitamos el cuadro de dialogo
                                alertDialog.dismiss();
                            }
                        });

                        // Accion a realizar a cancelar
                        btn_cancelar.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // Quitamos el cuadro de dialogo
                                alertDialog.dismiss();
                            }
                        });

                        // Mostramos el dialogo
                        alertDialog.show();
                        break;
                }

                return true;
            }
        });
        return view;
    }

}
