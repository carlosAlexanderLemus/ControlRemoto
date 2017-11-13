package lemus.com.bast_software.controlremoto.ConexionRed;

import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import lemus.com.bast_software.controlremoto.DispositivosIP;
import lemus.com.bast_software.controlremoto.R;

/**
 * Created by mekaku on 08/11/2017.
 */

// Clase escencial para la conexion
public class DispositivoConexion {
    // Informacion de la conexion actual en estatico
    private static DispositivosIP dispositivo_actual = null;

    private static boolean dispositivo_conectado = false;

    public static Conectar UltimaConexion;

    // Posibles errores
    public static final int SOLICITUD_ENVIADA = 0;
    public static final int DIRECCIONIP_VACIA = 1;
    public static final int ERROR_EN_EL_PUERTO = 2;
    public static final int LAS_CLAVES_NO_COINCIDEN = 3;
    public static final int UNA_CLAVE_ESTA_VACIA = 4;
    public static final int NO_SE_PUEDE_CONECTAR_POR_CONEXION_EXISTENTE = 5;
    public static final int INFORMACION_SASTIFACTORIA = 6;
    public static final int MODIFICACION_EXITOSA = 7;
    public static final int FALLO_AL_MODIFICAR = 8;

    // Establecemos la conexion actual
    public static void EstablecerDispositivoActual(DispositivosIP dispositivosIP)
    {
        // Establecemos el dispositivo actual
        dispositivo_actual = dispositivosIP.Clonar();
    }

    // Comprobar si hay dispositivo
    public static boolean HayConexionEstablecida()
    {
        // Hay dispositivo actual
        return dispositivo_actual != null;
    }

    // Obtener dispositivo
    public static DispositivosIP ObtenerDispositivoActual()
    {
        return dispositivo_actual;
    }

    // Actualizar dispositivo
    public static void ActualizarDispositivo(DispositivosIP dispositivosIP)
    {
        // Solo cuando ya exista alguna conexion
        if (HayConexionEstablecida())
        {
            dispositivo_actual.CopiarDatosDispositivoIP(dispositivosIP);
        }
    }

    public static void ConectarConElDispositivo(Context context, DispositivosIP dispositivosIP)
    {
        // Creamos el coso de cliente
        Clientes cliente = new Clientes(dispositivosIP.getIP(), dispositivosIP.getPuerto());

        // Vemos si las claves no son vacias
        if (!dispositivosIP.getClave().trim().equals(""))
            cliente.AñadirLinea(InformacionConexion.CABECERA_CLAVEPC, dispositivosIP.getClave().trim());

        // Comprobamos que el nombre no este vacio
        if (!dispositivosIP.getNombre().trim().equals(context.getResources().getString(R.string.text_name_device_default)))
            cliente.AñadirLinea(InformacionConexion.CABECERA_NOMBREPC, dispositivosIP.getNombre().trim());

        // En viamos el mensaje
        cliente.EnviarMensajeDeTexto(InformacionConexion.MOTIVOCONEXION_CONECTARSE);
    }

    // Creamos el objeto de conectar
    public static Conectar TratarConcexion(Context context, String ip, String puerto)
    {
        // Creamos el objeto de conexion
        Conectar conectar = new Conectar(context);

        // Establecemos la IP a usar
        conectar.IP = ip;

        // Puerto a usar
        conectar.puerto = puerto;

        // Devolvemos el objeto
        return conectar;
    }

    // Crear conexion
    public static Conectar EstablecerConecion(Context context, String ip, String puerto, String nombre, String password)
    {
        // Creamos la conexion
        // Creamos el objeto de conexion
        Conectar conectar = new Conectar(context);

        // Establecemos la IP a usar
        conectar.IP = ip;

        // Puerto a usar
        conectar.puerto = puerto;

        // Establecemos las password
        conectar.EstablecerClaves(password, password);

        // Establecer clave
        conectar.EstablecerNombre(nombre);

        // Devolvemos el objeto
        return conectar;
    }

    public static DispositivosIP ObtenerUltimoDispositivo(Context context)
    {
        // Obtenemos los dispositivos IP
        DispositivosIP dispositivosIP = null;

        try
        {
            // Obtenemos la base de datos
            String base = context.getResources().getString(R.string.base_de_datos);

            // Obtenemos los datos
            SQLDispositivos sqlDispositivos = new SQLDispositivos(context, base, null, 1);

            // Obtenemos la base de datos para escribir en ella
            SQLiteDatabase db = sqlDispositivos.getReadableDatabase();

            // Obtenemos los datos
            Cursor cursor = db.rawQuery(SQLDispositivos.SQL_SELECT_ULTIMA_IP, null);

            // Si hay datos
            if (cursor.moveToFirst())
            {
                // Obtenemos la informacion del primer resultado
                int _id = cursor.getInt(0);
                String _ip = cursor.getString(1);
                String _puerto = cursor.getString(2);
                String _nombre = cursor.getString(3);
                String _clave = cursor.getString(4);
                int _favoritos = cursor.getInt(5);
                int _frecuencia = cursor.getInt(6);

                dispositivosIP = new DispositivosIP(_id, _ip, Integer.parseInt(_puerto), _nombre, _clave, (_favoritos != 0), _frecuencia);
            }
        }
        catch (Exception ex)
        {
            Log.d("ErrorObtener", ex.getMessage());
        }

        // Devolvemos los dispositivos IP
        return dispositivosIP;
    }

    // Obtenemos todas las direcciones IP
    public static ArrayList<DispositivosIP> ObtenerTodasLasDireccionesIP(Context context)
    {
        // Obtenemos los dispositivos IP
        ArrayList<DispositivosIP> dispositivosIPs = new ArrayList<DispositivosIP>();

        try
        {
            // Obtenemos la base de datos
            String base = context.getResources().getString(R.string.base_de_datos);

            // Obtenemos los datos
            SQLDispositivos sqlDispositivos = new SQLDispositivos(context, base, null, 1);

            // Obtenemos la base de datos para escribir en ella
            SQLiteDatabase db = sqlDispositivos.getReadableDatabase();

            // Obtenemos los datos
            Cursor cursor = db.rawQuery(SQLDispositivos.SQL_SELECT_ALL_DEVICES, null);

            // Si hay datos
            if (cursor.moveToFirst())
            {
                do {
                    // Obtenemos la informacion
                    int _id = cursor.getInt(0);
                    String _ip = cursor.getString(1);
                    String _puerto = cursor.getString(2);
                    String _nombre = cursor.getString(3);
                    String _clave = cursor.getString(4);
                    int _favoritos = cursor.getInt(5);
                    int _frecuencia = cursor.getInt(6);

                    // Obtenemos todos los datos
                    dispositivosIPs.add(new DispositivosIP(_id, _ip, Integer.parseInt(_puerto), _nombre, _clave, (_favoritos != 0), _frecuencia));
                }
                while (cursor.moveToNext());// Nos movemos al siguientes
            }
        }
        catch (Exception ex)
        {
            Log.d("ErrorObtener", ex.getMessage());
        }

        // Devolvemos los dispositivos IP
        return dispositivosIPs;
    }

    public static ArrayList<DispositivosIP> ObtenerTodasLasDireccionesIPFavoritas(Context context)
    {
        // Obtenemos los dispositivos IP
        ArrayList<DispositivosIP> dispositivosIPs = new ArrayList<DispositivosIP>();

        try
        {
            // Obtenemos la base de datos
            String base = context.getResources().getString(R.string.base_de_datos);

            // Obtenemos los datos
            SQLDispositivos sqlDispositivos = new SQLDispositivos(context, base, null, 1);

            // Obtenemos la base de datos para escribir en ella
            SQLiteDatabase db = sqlDispositivos.getReadableDatabase();

            // Obtenemos los datos
            Cursor cursor = db.rawQuery(SQLDispositivos.SQL_SELECT_ALL_DEVICES_FAVORITES, null);

            // Si hay datos
            if (cursor.moveToFirst())
            {
                do {
                    // Obtenemos la informacion
                    int _id = cursor.getInt(0);
                    String _ip = cursor.getString(1);
                    String _puerto = cursor.getString(2);
                    String _nombre = cursor.getString(3);
                    String _clave = cursor.getString(4);
                    int _favoritos = cursor.getInt(5);
                    int _frecuencia = cursor.getInt(6);

                    // Obtenemos todos los datos
                    dispositivosIPs.add(new DispositivosIP(_id, _ip, Integer.parseInt(_puerto), _nombre, _clave, (_favoritos != 0), _frecuencia));
                }
                while (cursor.moveToNext());// Nos movemos al siguientes
            }
        }
        catch (Exception ex)
        {
            Log.d("ErrorObtener", ex.getMessage());
        }

        // Devolvemos los dispositivos IP
        return dispositivosIPs;
    }

    // Modificamos el favoritismo
    public static boolean ModificarDispositivoFavorito(Context context, boolean favorito, int id)
    {
        boolean resultado = true;

        try
        {
            // Obtenemos la base de datos
            String base = context.getResources().getString(R.string.base_de_datos);

            // Obtenemos los datos
            SQLDispositivos sqlDispositivos = new SQLDispositivos(context, base, null, 1);

            // Obtenemos la base de datos para escribir en ella
            SQLiteDatabase db = sqlDispositivos.getWritableDatabase();

            // Por defecto esta apagada
            int star = 0;
            // En caso de que sea un favorito
            if (favorito) star = 1;

            // Ejecutamos la consulta
            db.execSQL(SQLDispositivos.ModificarDispositivoFavorito(star, id));

            // Cerramos la conexion
            db.close();
        }
        catch (Exception ex)
        {
            // Error
            Log.d("ModificarError", ex.getMessage());
            // Ha ocurrido algun error
            resultado = false;
        }

        return resultado;
    }

    public static boolean ModificarDispositivoPorId(Conectar conectar, int id)
    {
        boolean resultado = true;

        try
        {
            // Obtenemos la base de datos
            String base = conectar.getContext().getResources().getString(R.string.base_de_datos);

            // Obtenemos los datos
            SQLDispositivos sqlDispositivos = new SQLDispositivos(conectar.getContext(), base, null, 1);

            // Obtenemos la base de datos para escribir en ella
            SQLiteDatabase db = sqlDispositivos.getWritableDatabase();


            // Ejecutamos la consulta
            db.execSQL(SQLDispositivos.ModificarDispositivoPorID(conectar.getIP(), conectar.getPuerto(), conectar.getNombre(), conectar.getPassword_1(), id));

            // Cerramos la conexion
            db.close();
        }
        catch (Exception ex)
        {
            // Error
            Log.d("ModificarError", ex.getMessage());
            // Ha ocurrido algun error
            resultado = false;
        }

        return resultado;
    }

    // Eliminamos por id
    public static Boolean EliminarDispositivoPorID(Context context, int id)
    {
        boolean resultado = true;

        try
        {
            // Obtenemos la base de datos
            String base = context.getResources().getString(R.string.base_de_datos);

            // Obtenemos los datos
            SQLDispositivos sqlDispositivos = new SQLDispositivos(context, base, null, 1);

            // Obtenemos la base de datos para escribir en ella
            SQLiteDatabase db = sqlDispositivos.getWritableDatabase();

            // Ejecutamos la consulta
            db.execSQL(SQLDispositivos.EliminarDispositivoPorId(id));

            // Cerramos la conexion
            db.close();
        }
        catch (Exception ex)
        {
            // Error
            Log.d("EliminarError", ex.getMessage());
            // Ha ocurrido algun error
            resultado = false;
        }

        return resultado;
    }

    // Guardamos la direccion IP
    public static boolean GuardarDispositivo(Context context, Conectar conectar)
    {
        boolean resultado = true;

        try {
            // Obtenemos la base de datos
            String base = context.getResources().getString(R.string.base_de_datos);

            // Obtenemos los datos
            SQLDispositivos sqlDispositivos = new SQLDispositivos(context, base, null, 1);

            // Obtenemos la base de datos para escribir en ella
            SQLiteDatabase db = sqlDispositivos.getWritableDatabase();

            // Ejecutamos la consulta
            db.execSQL(SQLDispositivos.InsertarNuevoDispositivo(conectar.getIP(), conectar.getPuerto(), conectar.getNombre(), conectar.getPassword_1()));

            // Cerramos la conexion
            db.close();
        }
        catch (Exception ex)
        {
            Log.d("ErroDB", ex.getMessage());
            // Ha ocurrido algun error
            resultado = false;
        }

        return resultado;
    }

    // Clase especial para la conexion
    public static class Conectar
    {
        // Guardamos el contexto actual
        private Context context;

        // Direccion IP
        private String IP;

        // Puerto de conexion
        private String puerto;

        // Establecemos el nombre del dispositivo
        private String nombre;

        // Establecemos las dos claves
        private String password_1;
        private String password_2;

        private boolean envioMensaje;

        private boolean conectar_con_el_dispositivo;

        // Contructor
        public Conectar(Context context)
        {
            // Estavlecemos el contexto
            this.context = context;

            // Informacion basica
            IP = "";
            puerto = "";

            // Establecemos el nombre del dispositivo
            Resources resources = context.getResources();
            nombre = resources.getString(R.string.text_name_device_default);

            // Establecemos las dos claves
            password_1 = "";
            password_2 = "";

            // Vemos la conexion
            conectar_con_el_dispositivo = false;

            // Valores por defecto
            envioMensaje = false;
        }

        // Establecemos el nombre
        public void EstablecerNombre(String nombre)
        {
            // Si el nombre que queremos usar no es igual a vacio
            if (!nombre.trim().equals(""))
                this.nombre = nombre;
        }

        // Establecemos las claves
        public void EstablecerClaves(String password_1, String password_2)
        {
            // Estbalcemos las claves
            this.password_1 = password_1;
            this.password_2 = password_2;
        }

        public void ComprobarConexion(boolean conectar_dispositivo)
        {
            this.conectar_con_el_dispositivo = conectar_dispositivo;
        }

        // EnviarMensaje
        public int ConectarConElDispositivo()
        {
            // Inicializamos los valores de conexion
            int mensajeError = 0;
            UltimaConexion = null;

            // Primero comprobamos que posea texto la IP
            if (!IP.trim().equals(""))
            {
                // En caso de que el puerto este vacio usamos el de defecto
                if (puerto.trim().equals(""))
                    puerto = context.getResources().getString(R.string.port_default);

                // Convertimos el puerto
                int puerto_int = Integer.parseInt(puerto);

                // Tiene que ser mayor a cero
                if (puerto_int > 0)
                {
                    // Saber si podemos parar a la sigiente fase
                    boolean claves_correctas = true;

                    // Comprobamos que las dos claves posean informacion
                    if (!password_1.trim().equals("") && !password_2.trim().equals(""))
                    {
                        // Si las claves no concuerda
                        if (!password_1.trim().equals(password_2.trim().toString()))
                        {
                            claves_correctas = false;
                            mensajeError = LAS_CLAVES_NO_COINCIDEN;
                        }
                    }
                    else if (password_1.trim().equals("") ^ password_2.trim().equals(""))
                    {
                        // En caso de que algunas
                        claves_correctas = false;
                        mensajeError = UNA_CLAVE_ESTA_VACIA;
                    }

                    // En caso de que las claves sean corectas
                    if (claves_correctas)
                    {
                        // Si conectamos con el dispositivo
                        if (conectar_con_el_dispositivo)
                        {
                            // En caso de que quiera conecat pero ya se encuentre algun dispositivo conectado
                            boolean conectar = !dispositivo_conectado;

                            // Si podemos conectarnos
                            if (conectar)
                            {
                                // Creamos el coso de cliente
                                Clientes cliente = new Clientes(IP, puerto_int);

                                // Vemos si las claves no son vacias
                                if (!password_1.trim().equals("") && !password_2.trim().equals(""))
                                    cliente.AñadirLinea(InformacionConexion.CABECERA_CLAVEPC, password_1.trim());

                                // Comprobamos que el nombre no este vacio
                                if (!nombre.trim().equals(context.getResources().getString(R.string.text_name_device_default)))
                                    cliente.AñadirLinea(InformacionConexion.CABECERA_NOMBREPC, nombre.trim());

                                // En viamos el mensaje
                                cliente.EnviarMensajeDeTexto(InformacionConexion.MOTIVOCONEXION_CONECTARSE);

                                // Guardamos la ultima conexion
                                UltimaConexion = this;
                                envioMensaje = true;
                            }
                            else
                                mensajeError = NO_SE_PUEDE_CONECTAR_POR_CONEXION_EXISTENTE;
                        }
                        else {
                            mensajeError = INFORMACION_SASTIFACTORIA;
                            // Guardamos la ultima conexion
                            UltimaConexion = this;
                            envioMensaje = false;
                        }

                    }
                }
                else
                    mensajeError = ERROR_EN_EL_PUERTO;
            }
            else
                mensajeError = DIRECCIONIP_VACIA;


            return mensajeError;
        }

        // EnviarMensaje
        public int ModificarDispositivoPorId(int id)
        {
            int mensajeError = 0;

            // Primero comprobamos que posea texto la IP
            if (!IP.trim().equals(""))
            {
                // En caso de que el puerto este vacio usamos el de defecto
                if (puerto.trim().equals(""))
                    puerto = context.getResources().getString(R.string.port_default);

                // Convertimos el puerto
                int puerto_int = Integer.parseInt(puerto);

                // Tiene que ser mayor a cero
                if (puerto_int > 0)
                {
                    // Saber si podemos parar a la sigiente fase
                    boolean claves_correctas = true;

                    // Comprobamos que las dos claves posean informacion
                    if (!password_1.trim().equals("") && !password_2.trim().equals(""))
                    {
                        // Si las claves no concuerda
                        if (!password_1.trim().equals(password_2.trim().toString()))
                        {
                            claves_correctas = false;
                            mensajeError = LAS_CLAVES_NO_COINCIDEN;
                        }
                    }
                    else if (password_1.trim().equals("") ^ password_2.trim().equals(""))
                    {
                        // En caso de que algunas
                        claves_correctas = false;
                        mensajeError = UNA_CLAVE_ESTA_VACIA;
                    }

                    // En caso de que las claves sean corectas
                    if (claves_correctas) {
                        if (DispositivoConexion.ModificarDispositivoPorId(this, id))
                            // Si lo logro modificar
                            mensajeError = MODIFICACION_EXITOSA;
                        else
                            mensajeError = FALLO_AL_MODIFICAR;

                    }
                }
                else
                    mensajeError = ERROR_EN_EL_PUERTO;
            }
            else
                mensajeError = DIRECCIONIP_VACIA;


            return mensajeError;
        }

        // Obtenemos los resultado
        public String getIP() {
            return IP;
        }

        public String getPuerto() {
            return puerto;
        }

        public String getNombre() {
            return nombre;
        }

        public String getPassword_1() {
            return password_1;
        }

        public String getPassword_2() {
            return password_2;
        }

        public boolean isEnvioMensaje() {
            return envioMensaje;
        }

        public boolean isConectar_con_el_dispositivo() {
            return conectar_con_el_dispositivo;
        }

        public Context getContext() {
            return context;
        }
    }

    private static class SQLDispositivos extends SQLiteOpenHelper
    {
        private static final String SQLTablaDispositivos = "CREATE TABLE dispositivos (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, ip VARCHAR(15) NOT NULL, puerto VARCHAR(10) NOT NULL, nombre VARCHAR(255), clave VARCHAR(255), favoritos BOOLEAN NOT NULL, frecuencia INT)";
        private static final String SQLEliminarTablaDispositivos = "DROP TABLE IF EXISTS dispositivos";
        public static final String SQL_NAME_TABLE = "dispositivos";
        public static final String SQL_SELECT_ALL_DEVICES = "SELECT id, ip, puerto, nombre, clave, favoritos, frecuencia FROM dispositivos";
        public static final String SQL_SELECT_ULTIMA_IP = "SELECT id, ip, puerto, nombre, clave, favoritos, frecuencia FROM dispositivos ORDER BY id DESC LIMIT 1";
        public static final String SQL_SELECT_ALL_DEVICES_FAVORITES = "SELECT id, ip, puerto, nombre, clave, favoritos, frecuencia FROM dispositivos WHERE favoritos = 1";

        public SQLDispositivos(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            // Creamos la tabla
            db.execSQL(SQLTablaDispositivos);
        }

        public static String InsertarNuevoDispositivo(String IP, String Puerto, String Nombre, String Clave)
        {
            return "INSERT INTO dispositivos (ip, puerto, nombre, clave, favoritos, frecuencia) VALUES ('" + IP + "', '" + Puerto +"','" + Nombre + "','" + Clave +"', 0, 0)";
        }

        public static String ModificarDispositivoFavorito(int favorito, int id)
        {
            return "UPDATE dispositivos SET favoritos = " + favorito + " WHERE id = " + id;
        }

        public static String EliminarDispositivoPorId(int id)
        {
            return "DELETE FROM dispositivos WHERE id=" + id;
        }

        public static String ModificarDispositivoPorID(String ip, String puerto, String nombre, String clave, int id)
        {
            return "UPDATE dispositivos SET ip = '" + ip + "', puerto = '" + puerto + "', nombre = '" + nombre + "', clave = '" + clave + "' WHERE id = " + id;
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // Eliminamos y volvemos a crear la tabla
            db.execSQL(SQLEliminarTablaDispositivos);
            db.execSQL(SQLTablaDispositivos);

        }
    }
}
