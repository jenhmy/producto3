package bugbusters.dao;

import bugbusters.modelo.Cliente;
import java.util.List;

/**
 * Interfaz DAO específica para la entidad Cliente.
 * Extiende GenericoDAO heredando las operaciones CRUD básicas.
 * La clave es el email del cliente (String).
 *
 * @author BugBusters
 * @version 1.0
 * @since 1.0
 */
public interface ClienteDAO extends GenericoDAO<String, Cliente> {

    /**
     * Devuelve todos los clientes de tipo Estándar.
     * @return Lista de clientes estándar
     */
    List<Cliente> obtenerClientesEstandar();

    /**
     * Devuelve todos los clientes de tipo Premium.
     * @return Lista de clientes premium
     */
    List<Cliente> obtenerClientesPremium();
}