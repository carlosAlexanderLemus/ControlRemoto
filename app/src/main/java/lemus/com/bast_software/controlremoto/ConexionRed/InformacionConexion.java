package lemus.com.bast_software.controlremoto.ConexionRed;

/**
 * Created by mekaku on 06/11/2017.
 */

public class InformacionConexion {
    // Informacion de las conexiones
    public static final String TIPOCONEXION_TEXTO = "conexion de tipo texto";
    public static final String TIPOCONEXION_VIDEO = "conexion de tipo video";

    // Motivo de la conexion
    public static final String MOTIVOCONEXION_CONECTARSE = "conectar con el dispositivo";
    public static final String MOTIVOCONEXION_REPUESTAEXITOSA = "hay conexion con el dispositivo";
    public static final String MOTIVOCONEXION_REPUESTAFRACASO = "no puedo haber conexion con el dispositivo";
    public static final String MOTIVOCONEXION_REPUESTACONEXIONEXISTENTE = "ya hay dispositivos conectados";
    public static final String MOTIVOCONEXION_DESCONECTARSE = "quiero desconectarme ahora";
    public static final String MOTIVOCONEXION_DESCONEXION_EXISTOSA = "desconexion exitosa";
    public static final String MOTIVOCONEXION_SOLICITUD_VIDEO = "uso del video streaming";
    public static final String MOTIVOCONEXION_SOLICITUD_CANCELAR_VIDEO = "ya no quiero el video";

    // Tipo de texto
    public static final String CABECERA_NOMBREPC = "[NombrePC]";
    public static final String CABECERA_CLAVEPC = "[PasswordPC]";
}
