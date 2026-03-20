package bugbusters.dao;

import bugbusters.modelo.Pedido;
import java.util.List;

/**
 * Interfaz DAO específica para la entidad Pedido.
 * Extiende GenericoDAO heredando las operaciones básicas.
 * La clave es el número de pedido (Integer).
 *
 * @author BugBusters
 * @version 1.0
 * @since 1.0
 */
public interface PedidoDAO extends GenericoDAO<Integer, Pedido> {

    /**
     * Devuelve todos los pedidos pendientes (aún cancelables).
     * @return Lista de pedidos pendientes
     */
    List<Pedido> obtenerPedidosPendientes();

    /**
     * Devuelve todos los pedidos ya enviados (no cancelables).
     * @return Lista de pedidos enviados
     */
    List<Pedido> obtenerPedidosEnviados();

    /**
     * Devuelve el último número de pedido generado.
     * Necesario para asignar números secuenciales a nuevos pedidos.
     * @return Último número de pedido
     */
    int obtenerUltimoNumeroPedido();
}