package bugbusters.modelo;

import bugbusters.dao.ArticuloDAO;
import bugbusters.dao.ClienteDAO;
import bugbusters.dao.PedidoDAO;
import bugbusters.factory.DAOFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase que gestiona todos los datos de la aplicación.
 *
 * Actúa como repositorio central que coordina el acceso a los datos
 * mediante el patrón DAO. En lugar de almacenar los datos en memoria,
 * delega todas las operaciones en los DAOs correspondientes que
 * persisten la información en la base de datos MySQL.
 *
 * Los DAOs se obtienen a través de DAOFactory, por lo que esta clase
 * no conoce la implementación concreta (MySQL, Oracle, etc.)
 *
 * @author BugBusters
 * @version 1.0
 * @since 1.0
 */
public class Datos {

    /** DAO para la gestión de artículos */
    private ArticuloDAO articulos;

    /** DAO para la gestión de clientes */
    private ClienteDAO clientes;

    /** DAO para la gestión de pedidos */
    private PedidoDAO pedidos;

    /**
     * Constructor que inicializa los DAOs a través de la Factory.
     * No conoce la implementación concreta, solo las interfaces.
     */
    public Datos() {
        articulos = DAOFactory.getArticuloDAO();
        clientes = DAOFactory.getClienteDAO();
        pedidos = DAOFactory.getPedidoDAO();
    }

    /* =========================================================
       ================= GESTIÓN DE ARTÍCULOS ==================
       ========================================================= */

    /**
     * Añade un artículo a la base de datos.
     * @param articulo El artículo a añadir
     */
    public void anadirArticulo(Articulo articulo) {
        articulos.insertar(articulo);
    }

    /**
     * Busca un artículo por su código.
     * @param codigo Código del artículo a buscar
     * @return El artículo si existe, null si no existe
     */
    public Articulo buscarArticulo(String codigo) {
        return articulos.buscar(codigo);
    }

    /**
     * Comprueba si existe un artículo con un código concreto.
     * @param codigo Código a comprobar
     * @return true si existe, false si no existe
     */
    public boolean existeArticulo(String codigo) {
        return articulos.existe(codigo);
    }

    /**
     * Devuelve todos los artículos de la base de datos.
     * @return Lista con todos los artículos
     */
    public List<Articulo> obtenerTodosArticulos() {
        return articulos.obtenerTodos();
    }

    /* =========================================================
       ================= GESTIÓN DE PEDIDOS ===================
       ========================================================= */

    /**
     * Genera un número de pedido único consultando el último en la BD.
     * @return Número de pedido único
     */
    public int generarNumeroPedido() {
        return pedidos.obtenerUltimoNumeroPedido() + 1;
    }

    /**
     * Añade un pedido a la base de datos.
     * @param pedido El pedido a añadir
     */
    public void anadirPedido(Pedido pedido) {
        pedidos.insertar(pedido);
    }

    /**
     * Elimina un pedido de la base de datos.
     * @param numeroPedido Número del pedido a eliminar
     */
    public void eliminarPedido(int numeroPedido) {
        pedidos.eliminar(numeroPedido);
    }

    /**
     * Busca un pedido por su número.
     * @param numeroPedido Número del pedido a buscar
     * @return El pedido si existe, null si no existe
     */
    public Pedido buscarPedido(int numeroPedido) {
        return pedidos.buscar(numeroPedido);
    }

    /**
     * Devuelve la lista de pedidos pendientes (no enviados).
     * @return Lista de pedidos pendientes
     */
    public ArrayList<Pedido> getPedidosPendientes() {
        return new ArrayList<>(pedidos.obtenerPedidosPendientes());
    }

    /**
     * Devuelve la lista de pedidos enviados.
     * @return Lista de pedidos enviados
     */
    public List<Pedido> getPedidosEnviados() {
        return pedidos.obtenerPedidosEnviados();
    }

    /* =========================================================
      ================= GESTIÓN DE CLIENTES =====================
      ========================================================= */

    /**
     * Añade un cliente a la base de datos.
     * @param cliente El cliente a añadir
     */
    public void anadirCliente(Cliente cliente) {
        clientes.insertar(cliente);
    }

    /**
     * Comprueba si existe un cliente con un email concreto.
     * @param email Email a comprobar
     * @return true si existe, false si no existe
     */
    public boolean existeCliente(String email) {
        return clientes.existe(email);
    }

    /**
     * Busca un cliente por su email.
     * @param email Email del cliente a buscar
     * @return El cliente si existe, null si no existe
     */
    public Cliente buscarCliente(String email) {
        return clientes.buscar(email);
    }

    /**
     * Devuelve todos los clientes de la base de datos.
     * @return Lista con todos los clientes
     */
    public ArrayList<Cliente> obtenerTodosClientes() {
        return new ArrayList<>(clientes.obtenerTodos());
    }

    /**
     * Devuelve solo los clientes de tipo Estándar.
     * @return Lista de clientes estándar
     */
    public ArrayList<Cliente> obtenerClientesEstandar() {
        return new ArrayList<>(clientes.obtenerClientesEstandar());
    }

    /**
     * Devuelve solo los clientes de tipo Premium.
     * @return Lista de clientes premium
     */
    public ArrayList<Cliente> obtenerClientesPremium() {
        return new ArrayList<>(clientes.obtenerClientesPremium());
    }

    /**
     * Elimina un cliente de la base de datos por su email.
     * Devuelve false si el cliente no existe o si tiene pedidos asociados.
     * @param email Email del cliente a eliminar
     * @return true si se eliminó correctamente, false si no existía o tiene pedidos asociados
     */
    public boolean eliminarCliente(String email) {
        if (clientes.existe(email)) {
            try {
                clientes.eliminar(email);
                return true;
            } catch (RuntimeException e) {
                return false;
            }
        }
        return false;
    }
}