package lemus.com.bast_software.controlremoto;

import android.content.Context;
import android.content.res.Resources;

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
    private Boolean favoritos;

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
}
