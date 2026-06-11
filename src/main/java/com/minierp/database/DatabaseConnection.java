package com.minierp.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Clase utilitaria que maneja la conexion a la base de datos MySQL.
 *
 * Solo tiene un metodo publico: getConnection().
 * Cualquier clase que necesite hablar con la BD lo llama asi:
 *
 *   Connection con = DatabaseConnection.getConnection();
 *
 * El constructor es privado porque no tiene sentido crear instancias
 * de esta clase — solo se usa el metodo estatico.
 */
public class DatabaseConnection {

    // URL de conexion: protocolo://host:puerto/nombre_de_la_base_de_datos
    private static final String URL      = "jdbc:mysql://localhost:3306/mini_erp";

    // Usuario de MySQL (XAMPP usa root por defecto)
    private static final String USER     = "root";

    // Contrasena de MySQL (XAMPP no tiene por defecto)
    private static final String PASSWORD = "";

    // Constructor privado: nadie puede hacer "new DatabaseConnection()"
    private DatabaseConnection() {}

    /**
     * Crea y devuelve una nueva conexion a la base de datos.
     *
     * Importante: quien llame este metodo es responsable de cerrar
     * la conexion cuando termine de usarla (con try-with-resources).
     *
     * @return una conexion activa a MySQL
     * @throws SQLException si no puede conectarse (por ejemplo, si MySQL no esta corriendo)
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
