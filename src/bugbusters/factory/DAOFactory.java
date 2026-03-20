package bugbusters.factory;

import bugbusters.dao.ArticuloDAO;
import bugbusters.dao.ArticuloDAOMySQL;
import bugbusters.dao.ClienteDAO;
import bugbusters.dao.ClienteDAOMySQL;
import bugbusters.dao.PedidoDAO;
import bugbusters.dao.PedidoDAOMySQL;

/**
 * Clase Factory que centraliza la creación de los DAOs.
 * Implementa el patrón de diseño Factory para instanciar los DAOs correctos
 * sin que el resto del código sepa qué implementación se está usando.
 *
 * Gracias a esta clase, si en el futuro se cambia el motor de base de datos, solo hay que modificar esta clase.
 *
 * @author BugBusters
 * @version 1.0
 * @since 1.0
 */
public class DAOFactory {

    /**
     * Constructor privado para evitar instanciación.
     * Todos los métodos son estáticos, no tiene sentido crear objetos de esta clase.
     */
    private DAOFactory() {}

    /**
     * Devuelve la implementación MySQL del DAO de Articulo.
     * @return ArticuloDAO listo para usar
     */
    public static ArticuloDAO getArticuloDAO() {
        return new ArticuloDAOMySQL();
    }

    /**
     * Devuelve la implementación MySQL del DAO de Cliente.
     * @return ClienteDAO listo para usar
     */
    public static ClienteDAO getClienteDAO() {
        return new ClienteDAOMySQL();
    }

    /**
     * Devuelve la implementación MySQL del DAO de Pedido.
     * @return PedidoDAO listo para usar
     */
    public static PedidoDAO getPedidoDAO() {
        return new PedidoDAOMySQL();
    }
}
