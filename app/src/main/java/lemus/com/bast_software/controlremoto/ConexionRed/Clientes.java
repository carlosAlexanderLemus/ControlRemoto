package lemus.com.bast_software.controlremoto.ConexionRed;

import android.os.StrictMode;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Clientes {
    // Campos ////////////
    private String ipAdress;
    private int port;
    private List<String> lineas;

    // Constructor ////////////
    public Clientes(String ipAdress, int port) {
        // Informacion
        this.ipAdress = ipAdress;
        this.port = port;
        // Inicializamos
        lineas = new ArrayList<String>();
    }

    // Añadimos lineas
    public void AñadirLinea(String linea)
    {
        // Comprobamos que no este repetida
        if (!lineas.contains(linea)){
            // Si no esta el elemento
            lineas.add(linea);
        }
    }

    // Añadimos lineas
    public void AñadirLinea(String cabezera, String linea)
    {
        // Comprobamos que no este repetida
        if (!lineas.contains(linea)){
            // Si no esta el elemento
            lineas.add(cabezera);
            lineas.add(linea);
        }
    }

    // Enviamos la informacion
    public void EnviarMensajeDeTexto(String motivo_conexion) {
        try
        {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitNetwork().build());
            Socket servidor = new Socket(ipAdress, port);

            PrintWriter salida = new PrintWriter(new OutputStreamWriter(servidor.getOutputStream()));

            // Es el titulo inicial
            salida.println(InformacionConexion.TIPOCONEXION_TEXTO);

            // MOTIVO DE LA CONEXION
            salida.println(motivo_conexion);

            // Informacion
            salida.println(Servidores.obtenerIpAddress());
            salida.println(Servidores.obtenerPuerto());

            // Si poseen lineas
            if (lineas.size() > 0)
            {
                for (String linea: lineas) {
                    salida.println(linea);
                }
            }

            salida.flush();

            BufferedReader entrada = new BufferedReader(new InputStreamReader(servidor.getInputStream()));
            servidor.close();
        }
        catch (Exception ex)
        {
            // Mostramos el mensaje de error
            Log.i("ErrorCliente", ex.getMessage() + " / " + ipAdress);
        }
    }
}
