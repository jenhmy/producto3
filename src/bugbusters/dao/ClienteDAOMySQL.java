package bugbusters.dao;

import bugbusters.modelo.Cliente;
import bugbusters.modelo.ClienteEstandar;
import bugbusters.modelo.ClientePremium;
import bugbusters.util.ConexionBD;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementación MySQL del DAO para la entidad Cliente.
 * Realiza las operaciones contra la base de datos MySQL usando JDBC.
 * Usa PreparedStatement para evitar ataques SQL Injection.
 * Gestiona la herencia de Cliente (Estándar/Premium) mediante el campo tipo.
 *
 * @author BugBusters
 * @version 1.0
 * @since 1.0
 */
public class ClienteDAOMySQL implements ClienteDAO {

    /**
     * Inserta un cliente en la base de datos.
     * Guarda el tipo (ESTANDAR/PREMIUM) para poder reconstruir el objeto correcto al leer.
     * @param cliente Cliente a insertar
     */
    @Override
    public void insertar(Cliente cliente) {
        String sql = "INSERT INTO clientes (email, nombre, domicilio, nif, tipo) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = ConexionBD.getConexion().prepareStatement(sql)) {
            ps.setString(1, cliente.getEmail());
            ps.setString(2, cliente.getNombre());
            ps.setString(3, cliente.getDomicilio());
            ps.setString(4, cliente.getNif());
            ps.setString(5, cliente instanceof ClientePremium ? "PREMIUM" : "ESTANDAR");
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error al insertar cliente: " + e.getMessage());
        }
    }

    /**
     * Busca un cliente por su email.
     * Reconstruye el objeto ClienteEstandar o ClientePremium según el campo tipo.
     * @param email Email del cliente a buscar
     * @return El cliente si existe, null si no existe
     */
    @Override
    public Cliente buscar(String email) {
        String sql = "SELECT * FROM clientes WHERE email = ?";
        try (PreparedStatement ps = ConexionBD.getConexion().prepareStatement(sql)) {
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return construirClienteEstatico(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar cliente: " + e.getMessage());
        }
        return null;
    }

    /**
     * Devuelve todos los clientes de la base de datos.
     * @return Lista con todos los clientes
     */
    @Override
    public List<Cliente> obtenerTodos() {
        List<Cliente> lista = new ArrayList<>();
        String sql = "SELECT * FROM clientes";
        try (PreparedStatement ps = ConexionBD.getConexion().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(construirClienteEstatico(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener clientes: " + e.getMessage());
        }
        return lista;
    }

    /**
     * Comprueba si existe un cliente con el email dado.
     * @param email Email a comprobar
     * @return true si existe, false si no existe
     */
    @Override
    public boolean existe(String email) {
        return buscar(email) != null;
    }

    /**
     * Elimina un cliente por su email.
     * @param email Email del cliente a eliminar
     */
    @Override
    public void eliminar(String email) {
        String sql = "DELETE FROM clientes WHERE email = ?";
        try (PreparedStatement ps = ConexionBD.getConexion().prepareStatement(sql)) {
            ps.setString(1, email);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error al eliminar cliente: " + e.getMessage());
        }
    }

    /**
     * Devuelve todos los clientes de tipo Estándar.
     * @return Lista de clientes estándar
     */
    @Override
    public List<Cliente> obtenerClientesEstandar() {
        return obtenerClientesPorTipo("ESTANDAR");
    }

    /**
     * Devuelve todos los clientes de tipo Premium.
     * @return Lista de clientes premium
     */
    @Override
    public List<Cliente> obtenerClientesPremium() {
        return obtenerClientesPorTipo("PREMIUM");
    }

    /**
     * Método privado auxiliar que filtra clientes por tipo.
     * Evita repetir código entre obtenerClientesEstandar y obtenerClientesPremium.
     * @param tipo Tipo de cliente (ESTANDAR o PREMIUM)
     * @return Lista de clientes del tipo indicado
     */
    private List<Cliente> obtenerClientesPorTipo(String tipo) {
        List<Cliente> lista = new ArrayList<>();
        String sql = "SELECT * FROM clientes WHERE tipo = ?";
        try (PreparedStatement ps = ConexionBD.getConexion().prepareStatement(sql)) {
            ps.setString(1, tipo);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(construirClienteEstatico(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener clientes por tipo: " + e.getMessage());
        }
        return lista;
    }

    /**
     * Método privado auxiliar que construye un objeto Cliente a partir de un ResultSet.
     * Crea ClienteEstandar o ClientePremium según el campo tipo de la base de datos.
     * Evita repetir código en buscar() y obtenerTodos().
     * @param rs ResultSet con los datos del cliente
     * @return ClienteEstandar o ClientePremium según corresponda
     * @throws SQLException Si hay error al leer el ResultSet
     */
    public static Cliente construirClienteEstatico(ResultSet rs) throws SQLException {
        String email = rs.getString("email");
        String nombre = rs.getString("nombre");
        String domicilio = rs.getString("domicilio");
        String nif = rs.getString("nif");
        String tipo = rs.getString("tipo");

        if ("PREMIUM".equals(tipo)) {
            return new ClientePremium(email, nombre, domicilio, nif);
        } else {
            return new ClienteEstandar(email, nombre, domicilio, nif);
        }
    }
}