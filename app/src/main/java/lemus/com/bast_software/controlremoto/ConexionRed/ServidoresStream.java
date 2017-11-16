package lemus.com.bast_software.controlremoto.ConexionRed;

import android.app.Activity;
import android.util.Log;

import org.apache.commons.io.IOUtils;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * Created by mekaku on 15/11/2017.
 */

public class ServidoresStream {
    // Context de la app
    private Activity applicacion;
    // Socket del servidor
    private ServerSocket servidor;
    // Puerto del servidor
    private static final int puerto = 11000;
    // Hilo principal
    private Thread hiloDelSocket;
    // Actuador en caso de sertexto
    private ActuadorStream actuadorStream;

    // Inicializamos el servidor
    public ServidoresStream(Activity applicacion) {
        // Establecemos la aplicacion que se usara
        this.applicacion = applicacion;
    }

    // Obtenemos el puerto que usamos
    public static int obtenerPuerto()
    {
        // Obtenemos el puerto que usamos
        return ServidoresStream.puerto;
    }

    // Establecemos el actuador en el texto
    public void setStreamListener(ActuadorStream actuadorStream) {
        this.actuadorStream = actuadorStream;
    }

    public static String obtenerIpAddress(){
        String ip = "none";
        try {
            // Obtenemos todas las intrefaces de red
            Enumeration<NetworkInterface> enumNetworkInterfaces = NetworkInterface.getNetworkInterfaces();
            // Pasamos los elementos uno por uno
            while (enumNetworkInterfaces.hasMoreElements()) {
                // Obtenemos una interfaz
                NetworkInterface networkInterface = enumNetworkInterfaces.nextElement();
                // Obtenemos la direccion
                Enumeration<InetAddress> enumInetAddress = networkInterface.getInetAddresses();
                //Onteemos los elemeentos de la interfaz
                while (enumInetAddress.hasMoreElements()) {
                    // Obtenemos las direcciones
                    InetAddress inetAddress = enumInetAddress.nextElement();
                    // Comprobamos que sea el local
                    if (inetAddress.isSiteLocalAddress()) {
                        ip = inetAddress.getHostAddress();
                    }
                }
            }

        } catch (Exception e) {
            // Mostramos el mensaje de error
            e.printStackTrace();
        }
        // Devolvemos el IP
        return ip;
    }

    // Cerramos el servidor principal
    public void onDestroy() {
        // Si es server no esta vacio
        if (servidor != null) {
            try {
                // Cerramos el hilo
                hiloDelSocket.interrupt();
                // Cerramos el servidor
                servidor.close();
            } catch (Exception e) { e.printStackTrace(); }
        }
    }

    // Iniciar el servidor
    public void iniciarServidor()
    {
        // Iniciamos el servidor
        hiloDelSocket = new Thread(new ServidoresStream.ServidorHiloPrincipal());
        hiloDelSocket.start();
    }

    // Hilo del server principal
    private class ServidorHiloPrincipal extends Thread {
        // Corremos el hilo
        @Override
        public void run() {
            try {
                // creamos el server en un puerto en especifico
                servidor = new ServerSocket(puerto);
                // EL hijo se ejecutara siempre pero hasta que obtengamos algun socket
                while (true) {
                    // Creamos una lista
                    final byte[] archivos;
                    final int longitud;
                    // Bloqueamos la llamada hasta que se crea la conexion y regresar
                    // Obtenemos el socket del cliente
                    Socket socket = servidor.accept();

                    // Espero que funcione
                    // Informacion del socket
                    InputStream info = socket.getInputStream();

                    DataInputStream dataInputStream = new DataInputStream(info);

                    ByteArrayOutputStream datos = new ByteArrayOutputStream();

                    byte[] buffer = new byte[1000000];

                    int readBytes = -1;
                    while((readBytes = dataInputStream.read(buffer)) > 1){
                        datos.write(buffer,0,readBytes);
                    }

                    archivos = datos.toByteArray();
                    longitud = archivos.length;

                    //archivos = IOUtils.toByteArray(info);
                    //longitud = archivos.length;

                    // Mostramos la informacion del hilo principal
                    applicacion.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // Si posee texto
                            // Enviamos la informacion
                            if  (actuadorStream != null)
                            {
                                actuadorStream.RecibirInformacion(new ResultadoStream(longitud, archivos));
                            }
                        }
                    });
                }
            } catch (IOException e) {
                // Erro obtenifo
                Log.i("PutaCagadaLaVida: ", e.getMessage());
                e.printStackTrace();
            }
        }
    }
}
