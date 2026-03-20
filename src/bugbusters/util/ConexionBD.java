package bugbusters.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Clase utilitaria para gestionar la conexión a la base de datos MySQL.
 * Implementa el patrón Singleton para garantizar una única conexión activa.
 */
public class ConexionDB {

    private static final String HOST = "TU_HOST_AIVEN";
    private static final int PORT = TU_PUERTO_AIVEN;
    private static final String DATABASE = "bugbusters";
    private static final String USER = "TU_USUARIO_AIVEN";
    private static final String PASSWORD = "TU_PASSWORD_AIVEN";
    private static final String SSL_CA = "C:/ruta/a/tu/ca.pem";

    private static Connection conexion = null;

    private ConexionDB() {} // Constructor privado (Singleton)

    public static Connection getConexion() throws SQLException {
        if (conexion == null || conexion.isClosed()) {
            String url = String.format(
                    "jdbc:mysql://%s:%d/%s?sslMode=VERIFY_CA&trustCertificateKeyStoreUrl=file:%s&trustCertificateKeyStorePassword=''",
                    HOST, PORT, DATABASE, SSL_CA
            );
            conexion = DriverManager.getConnection(url, USER, PASSWORD);
        }
        return conexion;
    }

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