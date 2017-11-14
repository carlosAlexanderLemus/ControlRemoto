package lemus.com.bast_software.controlremoto;

import android.content.Context;
import android.content.res.Resources;

import lemus.com.bast_software.controlremoto.ConexionRed.DispositivoConexion;

/**
 * Created by mekaku on 07/11/2017.
 */

public class DispositivosIP {
    // Direccion IP
    private int id;
    private String IP;
    private int puerto;
    private String nombre;
    private String clave;
    private boolean favoritos;
    private int frecuenca;

    // Constructor
    public DispositivosIP(Context context,int id, String IP, int puerto, boolean favoritos)
    {
        // Informacion estandar
        this.id = id;
        this.IP = IP;
        this.puerto = puerto;
        this.favoritos = favoritos;
        // Informacion por defecto
        Resources resources = context.getResources();
        nombre = resources.getString(R.string.text_name_device_default);
        // Guardamos el nombre de la clave
        clave = "";
        frecuenca = 0;
    }

    public DispositivosIP Clonar()
    {
        // Devolvemos el nuevo dispositivo
        return new DispositivosIP(id, IP, puerto, nombre, clave, favoritos, frecuenca);
    }

    public void CopiarDatosDispositivoIP(DispositivosIP dispositivosIP)
    {
        // Informacion obtenida del dispositivo
        id = dispositivosIP.id;
        IP = dispositivosIP.IP;
        puerto = dispositivosIP.puerto;
        favoritos = dispositivosIP.favoritos;
        nombre = dispositivosIP.nombre;
        clave = dispositivosIP.clave;
        frecuenca = dispositivosIP.frecuenca;
    }

    public DispositivosIP(int id, String IP, int puerto, String nombre, String clave, boolean favoritos, int frecuenca)
    {
        // Informacion estandar
        this.id = id;
        this.IP = IP;
        this.puerto = puerto;
        this.favoritos = favoritos;
        this.nombre = nombre;
        this.clave = clave;
        this.frecuenca = frecuenca;
    }

    // Actualizar informacion
    public void Actualizar(DispositivoConexion.Conectar conectar)
    {
        // Actualizamos la informacion
        this.IP = conectar.getIP();
        this.nombre = conectar.getNombre();
        this.clave = conectar.getPassword_1();
        this.puerto = Integer.parseInt(conectar.getPuerto());
    }

    // Informacion importante
    public int getId() {
        return id;
    }

    public String getIP() {
        return IP;
    }

    public int getPuerto() {
        return puerto;
    }

    public String getNombre() {
        return nombre;
    }

    public String getClave() {
        return clave;
    }

    public Boolean getFavoritos() {
        return favoritos;
    }

    public boolean isFavoritos() {
        return favoritos;
    }

    public int getFrecuenca() {
        return frecuenca;
    }

    public void setFavoritos(boolean favoritos) {
        this.favoritos = favoritos;
    }

    public void setFrecuenca(int frecuenca) {
        this.frecuenca = frecuenca;
    }
}
