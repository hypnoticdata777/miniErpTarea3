package com.minierp.producto;

/**
 * Clase modelo que representa un producto en el sistema.
 *
 * Un "modelo" es simplemente un objeto que guarda datos.
 * Esta clase refleja exactamente las columnas de la tabla `producto` en MySQL:
 *   id | nombre | precio | stock
 *
 * No tiene logica de negocio — solo guarda y expone sus datos
 * mediante getters y setters.
 */
public class Producto {

    private int    id;      // clave primaria en la BD, se asigna automaticamente
    private String nombre;
    private double precio;
    private int    stock;

    // Constructor vacio necesario para crear objetos sin datos iniciales
    public Producto() {}

    /**
     * Constructor para crear un producto nuevo (sin ID todavia).
     * Se usa al registrar: el ID lo asigna MySQL con AUTO_INCREMENT.
     */
    public Producto(String nombre, double precio, int stock) {
        this.nombre = nombre;
        this.precio = precio;
        this.stock  = stock;
    }

    /**
     * Constructor para crear un producto que ya existe en la BD.
     * Se usa al leer registros: MySQL ya tiene el ID guardado.
     */
    public Producto(int id, String nombre, double precio, int stock) {
        this.id     = id;
        this.nombre = nombre;
        this.precio = precio;
        this.stock  = stock;
    }

    // --- Getters y Setters ---
    // Permiten leer y modificar los campos privados desde otras clases

    public int getId()               { return id; }
    public void setId(int id)        { this.id = id; }

    public String getNombre()              { return nombre; }
    public void setNombre(String nombre)   { this.nombre = nombre; }

    public double getPrecio()              { return precio; }
    public void setPrecio(double precio)   { this.precio = precio; }

    public int getStock()              { return stock; }
    public void setStock(int stock)    { this.stock = stock; }

    /**
     * toString() define como se imprime el objeto con System.out.println(producto).
     * Usamos String.format para alinear los valores en columnas con ancho fijo,
     * de modo que la tabla quede bien formateada visualmente.
     *
     * Ejemplo de salida:
     *   | 1    | Chiles Serranos           | $20.00     | 5      |
     */
    @Override
    public String toString() {
        return String.format("| %-4d | %-25s | $%-10.2f | %-6d |",
                id, nombre, precio, stock);
    }
}
