package bugbusters.dao;

import bugbusters.modelo.Articulo;
import bugbusters.modelo.Cliente;
import bugbusters.modelo.Pedido;
import bugbusters.util.ConexionBD;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementación MySQL del DAO para la entidad Pedido.
 * Realiza las operaciones contra la base de datos MySQL usando JDBC.
 * Usa PreparedStatement para evitar ataques SQL Injection.
 * Gestiona las relaciones con Cliente y Articulo mediante JOINs.
 *
 * @author BugBusters
 * @version 1.0
 * @since 1.0
 */
public class PedidoDAOMySQL implements PedidoDAO {

    /**
     * Inserta un pedido en la base de datos llamando al procedimiento almacenado
     * sp_insertar_pedido. El procedimiento gestiona la transacción internamente.
     * @param pedido Pedido a insertar
     */
    @Override
    public void insertar(Pedido pedido) {
        try (CallableStatement cs = ConexionBD.getConexion().prepareCall("{CALL sp_insertar_pedido(?,?,?,?)}")) {
            cs.setString(1, pedido.getCliente().getEmail());
            cs.setString(2, pedido.getArticulo().getCodigo());
            cs.setInt(3, pedido.getCantidad());
            cs.setTimestamp(4, Timestamp.valueOf(pedido.getFechaHora()));
            cs.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error al insertar pedido: " + e.getMessage());
        }
    }

    /**
     * Busca un pedido por su número.
     * Usa JOIN para recuperar los datos completos de cliente y artículo.
     * @param numeroPedido Número del pedido a buscar
     * @return El pedido si existe, null si no existe
     */
    @Override
    public Pedido buscar(Integer numeroPedido) {
        String sql = "SELECT p.numero_pedido, p.cantidad, p.fecha_hora, " +
                "c.email AS email_cliente, c.nombre, c.domicilio, c.nif, c.tipo, " +
                "a.codigo AS codigo_articulo, a.descripcion, a.precio_venta, a.gastos_envio, a.tiempo_preparacion " +
                "FROM pedidos p " +
                "JOIN clientes c ON p.email_cliente = c.email " +
                "JOIN articulos a ON p.codigo_articulo = a.codigo " +
                "WHERE p.numero_pedido = ?";
        try (PreparedStatement ps = ConexionBD.getConexion().prepareStatement(sql)) {
            ps.setInt(1, numeroPedido);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return construirPedido(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar pedido: " + e.getMessage());
        }
        return null;
    }

    /**
     * Devuelve todos los pedidos de la base de datos.
     * Usa JOIN para recuperar los datos completos de cliente y artículo.
     * @return Lista con todos los pedidos
     */
    @Override
    public List<Pedido> obtenerTodos() {
        return obtenerPedidosPorSQL(
                "SELECT p.numero_pedido, p.cantidad, p.fecha_hora, " +
                        "c.email AS email_cliente, c.nombre, c.domicilio, c.nif, c.tipo, " +
                        "a.codigo AS codigo_articulo, a.descripcion, a.precio_venta, a.gastos_envio, a.tiempo_preparacion " +
                        "FROM pedidos p " +
                        "JOIN clientes c ON p.email_cliente = c.email " +
                        "JOIN articulos a ON p.codigo_articulo = a.codigo"
        );
    }

    /**
     * Comprueba si existe un pedido con el número dado.
     * @param numeroPedido Número a comprobar
     * @return true si existe, false si no existe
     */
    @Override
    public boolean existe(Integer numeroPedido) {
        return buscar(numeroPedido) != null;
    }

    /**
     * Elimina un pedido de la base de datos llamando al procedimiento almacenado
     * sp_eliminar_pedido. El procedimiento gestiona la transacción internamente.
     * @param numeroPedido Número del pedido a eliminar
     */
    @Override
    public void eliminar(Integer numeroPedido) {
        try (CallableStatement cs = ConexionBD.getConexion().prepareCall("{CALL sp_eliminar_pedido(?)}")) {
            cs.setInt(1, numeroPedido);
            cs.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error al eliminar pedido: " + e.getMessage());
        }
    }

    /**
     * Devuelve todos los pedidos pendientes (aún cancelables).
     * @return Lista de pedidos pendientes
     */
    @Override
    public List<Pedido> obtenerPedidosPendientes() {
        List<Pedido> todos = obtenerTodos();
        List<Pedido> pendientes = new ArrayList<>();
        for (Pedido p : todos) {
            if (p.puedeCancelar()) {
                pendientes.add(p);
            }
        }
        return pendientes;
    }

    /**
     * Devuelve todos los pedidos enviados (no cancelables).
     * @return Lista de pedidos enviados
     */
    @Override
    public List<Pedido> obtenerPedidosEnviados() {
        List<Pedido> todos = obtenerTodos();
        List<Pedido> enviados = new ArrayList<>();
        for (Pedido p : todos) { // Recorre todos
            if (!p.puedeCancelar()) { // Si no puede cancelar es que está enviado
                enviados.add(p); // Devuelve solo los enviados
            }
        }
        return enviados;
    }

    /**
     * Devuelve el último número de pedido generado.
     * Necesario para asignar números secuenciales a nuevos pedidos.
     * @return Último número de pedido, 0 si no hay pedidos
     */
    @Override
    public int obtenerUltimoNumeroPedido() {
        String sql = "SELECT MAX(numero_pedido) FROM pedidos";
        try (PreparedStatement ps = ConexionBD.getConexion().prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener último número de pedido: " + e.getMessage());
        }
        return 0;
    }

    /**
     * Método privado auxiliar que construye un objeto Pedido a partir de un ResultSet.
     * Reconstruye también los objetos Cliente y Articulo asociados.
     * @param rs ResultSet con los datos del pedido
     * @return Pedido construido con sus objetos Cliente y Articulo
     * @throws SQLException Si hay error al leer el ResultSet
     */
    private Pedido construirPedido(ResultSet rs) throws SQLException {
        Cliente cliente = ClienteDAOMySQL.construirClienteDesdeJoin(rs);
        Articulo articulo = new Articulo(
                rs.getString("codigo_articulo"),
                rs.getString("descripcion"),
                rs.getDouble("precio_venta"),
                rs.getDouble("gastos_envio"),
                rs.getInt("tiempo_preparacion")
        );
        return new Pedido(
                rs.getInt("numero_pedido"),
                cliente,
                articulo,
                rs.getInt("cantidad"),
                rs.getTimestamp("fecha_hora").toLocalDateTime()
        );
    }

    /**
     * Método privado auxiliar que ejecuta una consulta SQL y devuelve una lista de pedidos.
     * Evita repetir código entre obtenerTodos(), obtenerPedidosPendientes() y obtenerPedidosEnviados().
     * @param sql Consulta SQL a ejecutar
     * @return Lista de pedidos resultado de la consulta
     */
    private List<Pedido> obtenerPedidosPorSQL(String sql) {
        List<Pedido> lista = new ArrayList<>();
        try (PreparedStatement ps = ConexionBD.getConexion().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(construirPedido(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener pedidos: " + e.getMessage());
        }
        return lista;
    }
}
