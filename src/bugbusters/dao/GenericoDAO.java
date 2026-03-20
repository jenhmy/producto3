package bugbusters.dao;

import java.util.List;

/**
 * Interfaz genérica que define las operaciones  básicas para cualquier entidad.
 * Actúa como contrato base que deben extender todas las interfaces DAO específicas.
 * De esta forma se evita repetir los métodos comunes en cada interfaz.
 *
 * @param <K> Tipo de la clave identificadora (ej. String para email, Integer para ID)
 * @param <T> Tipo del objeto a gestionar (ej. Cliente, Articulo, Pedido)
 *
 * @author BugBusters
 * @version 1.0
 * @since 1.0
 */
public interface GenericoDAO<K, T> {

    /**
     * Inserta un objeto en la base de datos.
     * @param objeto Objeto a insertar
     */
    void insertar(T objeto);

    /**
     * Busca un objeto por su clave.
     * @param clave Clave identificadora del objeto
     * @return El objeto si existe, null si no existe
     */
    T buscar(K clave);

    /**
     * Devuelve todos los objetos almacenados.
     * @return Lista con todos los objetos
     */
    List<T> obtenerTodos();

    /**
     * Comprueba si existe un objeto con la clave dada.
     * @param clave Clave a comprobar
     * @return true si existe, false si no existe
     */
    boolean existe(K clave);

    /**
     * Elimina un objeto por su clave.
     * @param clave Clave del objeto a eliminar
     */
    void eliminar(K clave);
}