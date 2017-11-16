package lemus.com.bast_software.controlremoto.ConexionRed;

/**
 * Created by mekaku on 15/11/2017.
 */

public class ResultadoStream {
    private int lenght;
    private byte[] archivo;

    public ResultadoStream(int lenght, byte[] archivo) {
        this.lenght = lenght;
        this.archivo = archivo;
    }

    public int getLenght() {
        return lenght;
    }

    public byte[] getArchivo() {
        return archivo;
    }
}
