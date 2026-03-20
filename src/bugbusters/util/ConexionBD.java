package bugbusters.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Clase para gestionar la conexión a la base de datos MySQL. Cualquier otra clase del proyecto que necesite hablar
 * con la base de datos la llama.
 *
 * Implementa el patrón Singleton para garantizar una única instancia
 * de conexión activa durante toda la ejecución de la aplicación.
 * Las credenciales se leen del archivo .env para no exponerlas en el código público.
 *
 * @author BugBusters
 * @version 1.0
 * @since 1.0
 */
public class ConexionBD {

    /** Única instancia de la conexión (patrón Singleton) */
    private static Connection conexion = null;

    /** Constructor privado para evitar instanciación externa (patrón Singleton) */
    private ConexionBD() {}

    /**
     * Lee las variables del archivo .env y las devuelve en un mapa.
     *
     * Se usa HashMap para guardar las variables del .env mientras se lee el archivo, ya que HashMap almacena
     * pares "clave-valor" con claves únicas, lo que encaja perfectamente con el formato del .env
     *
     * @return Mapa con las variables de entorno del archivo .env
     */
    private static Map<String, String> cargarEnv() {
        Map<String, String> env = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(".env"))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                // Ignorar líneas vacías y comentarios
                if (linea.isBlank() || linea.startsWith("#")) continue;
                String[] partes = linea.split("=", 2);
                if (partes.length == 2) {
                    env.put(partes[0].trim(), partes[1].trim());
                }
            }
        } catch (IOException e) {
            System.err.println("Error al leer el archivo .env: " + e.getMessage());
        }
        return env;
    }

    /**
     * Devuelve la conexión activa a la base de datos.
     * Si no existe o está cerrada, crea una nueva.
     *
     * @return Objeto Connection activo
     * @throws SQLException Si hay un error al establecer la conexión
     */
    public static Connection getConexion() throws SQLException {
        if (conexion == null || conexion.isClosed()) {
            Map<String, String> env = cargarEnv();

            String host = env.get("DB_HOST");
            String port = env.get("DB_PORT");
            String database = env.get("DB_DATABASE");
            String user = env.get("DB_USER");
            String password = env.get("DB_PASSWORD");
            String sslCa = env.get("DB_SSL_CA");

            String url = String.format(
                    "jdbc:mysql://%s:%s/%s?sslMode=VERIFY_CA&trustCertificateKeyStoreUrl=file:%s&trustCertificateKeyStorePassword=''",
                    host, port, database, sslCa
            );

            conexion = DriverManager.getConnection(url, user, password);
        }
        return conexion;
    }

    /**
     * Cierra la conexión activa a la base de datos.
     */
    public static void cerrarConexion() {
        if (conexion != null) {
            try {
                conexion.close();
                conexion = null;
            } catch (SQLException e) {
                System.err.println("Error al cerrar la conexión: " + e.getMessage());
            }
        }
    }
}