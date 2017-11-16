package lemus.com.bast_software.controlremoto;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import lemus.com.bast_software.controlremoto.ConexionRed.ActuadorStream;
import lemus.com.bast_software.controlremoto.ConexionRed.DispositivoConexion;
import lemus.com.bast_software.controlremoto.ConexionRed.ResultadoStream;
import lemus.com.bast_software.controlremoto.ConexionRed.ServidoresStream;


/**
 * A simple {@link Fragment} subclass.
 */
public class PlayComputerStreamFragment extends Fragment {

    // Servidores
    private ServidoresStream servidoresStream = null;

    // Bitmap
    private ImageView iv_video_stream;

    public PlayComputerStreamFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_play_computer_stream, container, false);

        // Obtenemos la imagen
        iv_video_stream = (ImageView)view.findViewById(R.id.iv_video_stream);

        // Comprobamos que no haya alguna conexion
        if (!DispositivoConexion.HayConexionEstablecida()){
            // Mostramos un mensaje de error
            AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
            builder.setTitle("Error inesperado")
                    .setMessage("Lo sentimos pero parece que usted no posee algun dispositivo en conexion")
                    .setPositiveButton("OK", null)
                    .show();

        }
        else{
            // En caso que si estemos conectados
            servidoresStream = new ServidoresStream(getActivity());

            // Ejecutamos la informacion
            servidoresStream.setStreamListener(new ActuadorStream() {
                @Override
                public void RecibirInformacion(ResultadoStream resultados) {
                    // Obtenemos el bitmap
                    try {
                        //Toast.makeText(getContext(), "Info: " + resultados.getLenght(), Toast.LENGTH_SHORT).show();
                        Bitmap bitmap = BitmapFactory.decodeByteArray(resultados.getArchivo(), 0, resultados.getLenght());
                        iv_video_stream.setImageBitmap(bitmap);
                    }
                    catch (Exception ex)
                    {
                        Log.d("Informacion", ex.getMessage());
                    }
                }
            });

            servidoresStream.iniciarServidor();

            // Enviamos el mensaje piquiendo lo que seria el video
            DispositivoConexion.SolicitarVideoAlDispositivoActual(view.getContext());

        }

        // Devolvemos la vista
        return view;
    }

    @Override
    public void onDestroy() {
        // Cancelams el servidor
        servidoresStream.onDestroy();
        // Solicitamos el coso
        DispositivoConexion.SolicitarCancelacionDelVideoAlDispositivoActual(getContext());
        // Por defecto
        super.onDestroy();
    }
}
