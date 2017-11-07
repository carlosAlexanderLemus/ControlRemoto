package lemus.com.bast_software.controlremoto.ConexionRed;

import java.util.List;

/**
 * Created by mekaku on 06/11/2017.
 */

public interface ActuadorDeTexto {
    // Recibimos mensaje
    public void RecibirMensaje(String tipoDeAccion, List<String> lineas);
}
