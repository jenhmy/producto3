package bugbusters.dao;

import bugbusters.modelo.Articulo;
import bugbusters.util.ConexionBD;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementación MySQL del DAO para la entidad Articulo.
 * Realiza las operaciones CRUD contra la base de datos MySQL usando JDBC.
 * Usa PreparedStatement para evitar ataques SQL Injection.
 *
 * @author BugBusters
 * @version 1.0
 * @since 1.0
 */
public class ArticuloDAOMySQL implements ArticuloDAO {

    /**
     * Inserta un artículo en la base de datos llamando al procedimiento almacenado
     * sp_insertar_articulo. El procedimiento gestiona la transacción internamente.
     * @param articulo Artículo a insertar
     */
    @Override
    public void insertar(Articulo articulo) {
        try (CallableStatement cs = ConexionBD.getConexion().prepareCall("{CALL sp_insertar_articulo(?,?,?,?,?)}")) {
            cs.setString(1, articulo.getCodigo());
            cs.setString(2, articulo.getDescripcion());
            cs.setDouble(3, articulo.getPrecioVenta());
            cs.setDouble(4, articulo.getGastosEnvio());
            cs.setInt(5, articulo.getTiempoPreparacionMin());
            cs.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error al insertar artículo: " + e.getMessage());
        }
    }

    /**
     * Busca un artículo por su código.
     * @param codigo Código del artículo a buscar
     * @return El artículo si existe, null si no existe
     */
    @Override
    public Articulo buscar(String codigo) {
        String sql = "SELECT * FROM articulos WHERE codigo = ?";
        try (PreparedStatement ps = ConexionBD.getConexion().prepareStatement(sql)) {
            ps.setString(1, codigo);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Articulo(
                        rs.getString("codigo"),
                        rs.getString("descripcion"),
                        rs.getDouble("precio_venta"),
                        rs.getDouble("gastos_envio"),
                        rs.getInt("tiempo_preparacion")
                );
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar artículo: " + e.getMessage());
        }
        return null;
    }

    /**
     * Devuelve todos los artículos de la base de datos.
     * @return Lista con todos los artículos
     */
    @Override
    public List<Articulo> obtenerTodos() {
        List<Articulo> lista = new ArrayList<>();
        String sql = "SELECT * FROM articulos";
        try (PreparedStatement ps = ConexionBD.getConexion().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(new Articulo(
                        rs.getString("codigo"),
                        rs.getString("descripcion"),
                        rs.getDouble("precio_venta"),
                        rs.getDouble("gastos_envio"),
                        rs.getInt("tiempo_preparacion")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener artículos: " + e.getMessage());
        }
        return lista;
    }

    /**
     * Comprueba si existe un artículo con el código dado.
     * @param codigo Código a comprobar
     * @return true si existe, false si no existe
     */
    @Override
    public boolean existe(String codigo) {
        return buscar(codigo) != null;
    }

    /**
     * Elimina un artículo de la base de datos llamando al procedimiento almacenado
     * sp_eliminar_articulo. El procedimiento gestiona la transacción internamente.
     * @param codigo Código del artículo a eliminar
     */
    @Override
    public void eliminar(String codigo) {
        try (CallableStatement cs = ConexionBD.getConexion().prepareCall("{CALL sp_eliminar_articulo(?)}")) {
            cs.setString(1, codigo);
            cs.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error al eliminar artículo: " + e.getMessage());
        }
    }
}