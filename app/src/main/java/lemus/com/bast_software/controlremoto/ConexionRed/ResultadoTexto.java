package lemus.com.bast_software.controlremoto.ConexionRed;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mekaku on 07/11/2017.
 */

public class ResultadoTexto {
    // Informacion del tipo de accion a realizar
    private String tipoDeAccion;

    // Lineas de comando
    private List<String> lineas;

    // Constructor
    public ResultadoTexto(String tipoDeAccion){
        // Establecemos el tipo de accion
        this.tipoDeAccion = tipoDeAccion;

        // Lineas
        lineas = new ArrayList<String>();
    }

    // Constructor con la informacion de las lineas
    public ResultadoTexto(String tipoDeAccion, List<String> lineas){
        // Establecemos el tipo de accion
        this.tipoDeAccion = tipoDeAccion;

        // obtenemos las lineas
        this.lineas = lineas;
    }

    // Obtenemos la informacion de la accion
    public String TipoDeAccion(){
        return tipoDeAccion;
    }

}
