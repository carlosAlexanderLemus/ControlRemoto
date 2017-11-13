package lemus.com.bast_software.controlremoto.ConexionRed;

import android.app.Activity;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * Created by mekaku on 03/10/2017.
 * Clase orientada al server
 */

public class Servidores {
    // Context de la app
    private Activity applicacion;
    // Socket del servidor
    private ServerSocket servidor;
    // Puerto del servidor
    private static final int puerto = 11000;
    // Hilo principal
    private Thread hiloDelSocket;
    // Actuador en caso de sertexto
    private ActuadorDeTexto actuadorDeTexto;

    // Inicializamos el servidor
    public Servidores(Activity applicacion) {
        // Establecemos la aplicacion que se usara
        this.applicacion = applicacion;
    }

    // Obtenemos el puerto que usamos
    public static int obtenerPuerto()
    {
        // Obtenemos el puerto que usamos
        return Servidores.puerto;
    }

    // Establecemos el actuador en el texto
    public void establecerActuadorDeTexto(ActuadorDeTexto actuadorDeTexto) {
        this.actuadorDeTexto = actuadorDeTexto;
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
        hiloDelSocket = new Thread(new ServidorHiloPrincipal());
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
                    final List<String> archivos = new ArrayList<String>();
                    // Bloqueamos la llamada hasta que se crea la conexion y regresar
                    // Obtenemos el socket del cliente
                    Socket socket = servidor.accept();
                    // Informacion del socket
                    OutputStream outputStream = socket.getOutputStream();
                    InputStream info = socket.getInputStream();
                    // Mensaje
                    try
                    {
                        // Obtenemos el input
                        BufferedReader reader = new BufferedReader(new InputStreamReader(info));
                        String str = "";
                        if (info != null) {
                            while ((str = reader.readLine()) != null)
                            {
                                // Si es la etiqueta para finalizar el texto
                                if (str.trim().equals("<END>"))
                                    break;

                                // Iremos añadiendo el texto
                                archivos.add(str);
                            }
                        }
                    }
                    catch (Exception ex){ Log.i("HiloMierda: ", ex.getMessage()); }

                    // Enviamos informacion al socket
                    PrintStream printStream = new PrintStream(outputStream);
                    printStream.print("Paquete recibido");
                    printStream.close();
                    // Mostramos la informacion del hilo principal
                    applicacion.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // Si posee texto
                            if (archivos.size() > 0)
                            {
                                // Revisamos que sea el primero
                                switch (archivos.get(0))
                                {
                                    // Si es de tipo texto
                                    case InformacionConexion.TIPOCONEXION_TEXTO:
                                        String accion = archivos.get(1);
                                        // Removemos los archivos
                                        archivos.remove(0);
                                        archivos.remove(0);

                                        // El resultado
                                        ResultadoTexto resultadoTexto;

                                        // Si no posee lineas de codigos
                                        if (archivos.size() > 0)
                                            resultadoTexto = new ResultadoTexto(accion);
                                        else
                                            resultadoTexto = new ResultadoTexto(accion, archivos);

                                        // Si el actuador es nulo
                                        if (actuadorDeTexto != null)
                                            // El actuadoe
                                            actuadorDeTexto.RecibirMensaje(resultadoTexto);
                                        else
                                            Log.d("PutoActuador","No hay actuador");

                                        break;
                                }
                            }
                        }
                    });
                }
            } catch (IOException e) {
                // Erro obtenifo
                Log.i("PutaCagada: ", e.getMessage());
                e.printStackTrace();
            }
        }
    }
}
