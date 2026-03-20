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
     * Inserta un artículo en la base de datos.
     * @param articulo Artículo a insertar
     */
    @Override
    public void insertar(Articulo articulo) {
        String sql = "INSERT INTO articulos (codigo, descripcion, precio_venta, gastos_envio, tiempo_preparacion) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = ConexionBD.getConexion().prepareStatement(sql)) {
            ps.setString(1, articulo.getCodigo());
            ps.setString(2, articulo.getDescripcion());
            ps.setDouble(3, articulo.getPrecioVenta());
            ps.setDouble(4, articulo.getGastosEnvio());
            ps.setInt(5, articulo.getTiempoPreparacionMin());
            ps.executeUpdate();
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
     * Elimina un artículo por su código.
     * @param codigo Código del artículo a eliminar
     */
    @Override
    public void eliminar(String codigo) {
        String sql = "DELETE FROM articulos WHERE codigo = ?";
        try (PreparedStatement ps = ConexionBD.getConexion().prepareStatement(sql)) {
            ps.setString(1, codigo);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error al eliminar artículo: " + e.getMessage());
        }
    }
}