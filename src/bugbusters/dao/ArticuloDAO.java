package bugbusters.dao;

import bugbusters.modelo.Articulo;

/**
 * Interfaz DAO específica para la entidad Articulo.
 * Extiende GenericoDAO heredando las operaciones CRUD básicas.
 * La clave es el código del artículo (String).
 *
 * @author BugBusters
 * @version 1.0
 * @since 1.0
 */
public interface ArticuloDAO extends GenericoDAO<String, Articulo> {
    // Si en el futuro se necesitan métodos propios de Articulo se añaden aquí
}