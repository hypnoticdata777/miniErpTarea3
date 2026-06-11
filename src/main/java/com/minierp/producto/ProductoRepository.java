package com.minierp.producto;

import com.minierp.database.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Repositorio de Producto: es la unica clase que habla directamente con MySQL.
 *
 * Su responsabilidad es traducir entre objetos Java y filas de la base de datos.
 * El resto del programa no sabe nada de SQL — solo llama metodos de esta clase.
 *
 * Patron usado: Repository (separa la logica de acceso a datos del resto).
 */
public class ProductoRepository {

    /**
     * Inserta un producto nuevo en la tabla `producto`.
     *
     * Usa PreparedStatement en lugar de concatenar strings en el SQL
     * para evitar SQL Injection (un problema de seguridad comun).
     *
     * Despues de insertar, recupera el ID que MySQL asigno automaticamente
     * y lo guarda en el objeto producto para que el llamador lo conozca.
     *
     * @param producto objeto con nombre, precio y stock a guardar
     */
    public void guardar(Producto producto) {
        // Los signos ? son parametros que se rellenan de forma segura con ps.setXxx()
        String sql = "INSERT INTO producto (nombre, precio, stock) VALUES (?, ?, ?)";

        // try-with-resources: cierra la conexion y el statement automaticamente
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            // Rellenamos cada ? en el orden en que aparecen en el SQL
            ps.setString(1, producto.getNombre());
            ps.setDouble(2, producto.getPrecio());
            ps.setInt(3, producto.getStock());

            // executeUpdate() ejecuta el INSERT y devuelve cuantas filas afecto
            ps.executeUpdate();

            // MySQL genera el ID automaticamente (AUTO_INCREMENT); lo recuperamos aqui
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    producto.setId(keys.getInt(1)); // guardamos el ID en el objeto
                }
            }

        } catch (SQLException e) {
            // Convertimos la excepcion de SQL a una de tiempo de ejecucion para
            // que el Service la pueda manejar sin declarar throws SQLException
            throw new RuntimeException("Error al guardar el producto: " + e.getMessage(), e);
        }
    }

    /**
     * Consulta todos los productos de la tabla y los devuelve como lista.
     *
     * ResultSet funciona como un cursor: empieza antes del primer registro
     * y con rs.next() avanzamos fila por fila leyendo los valores.
     *
     * @return lista con todos los productos; vacia si no hay ninguno
     */
    public List<Producto> obtenerTodos() {
        String sql = "SELECT id, nombre, precio, stock FROM producto ORDER BY id";
        List<Producto> lista = new ArrayList<>();

        try (Connection con = DatabaseConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            // Iteramos cada fila del resultado y creamos un objeto Producto por fila
            while (rs.next()) {
                lista.add(new Producto(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getDouble("precio"),
                        rs.getInt("stock")
                ));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener productos: " + e.getMessage(), e);
        }

        return lista;
    }
}
