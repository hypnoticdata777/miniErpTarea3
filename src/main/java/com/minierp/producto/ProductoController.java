package com.minierp.producto;

import java.util.List;
import java.util.Scanner;

/**
 * Controlador de Producto: maneja la interaccion con el usuario.
 *
 * Recibe lo que el usuario escribe, lo convierte en objetos,
 * llama al Service para procesarlos y muestra el resultado.
 *
 * No valida reglas de negocio (eso es trabajo del Service),
 * pero si valida que el usuario haya ingresado el tipo correcto
 * de dato (por ejemplo, que el precio sea un numero).
 */
public class ProductoController {

    private final ProductoService service;
    private final Scanner scanner;

    /**
     * Recibe el mismo Scanner que usa Main para que todos lean
     * del mismo flujo de entrada (la terminal del usuario).
     */
    public ProductoController(Scanner scanner) {
        this.service = new ProductoService();
        this.scanner = scanner;
    }

    /**
     * Guia al usuario paso a paso para registrar un producto nuevo.
     *
     * El precio y el stock usan bucles while(true) para seguir
     * preguntando hasta que el usuario escriba un valor numerico valido.
     * Esto evita que el programa se caiga con un error si el usuario
     * escribe letras donde se espera un numero.
     */
    public void registrarProducto() {
        System.out.println("\n--- Registrar Producto ---");

        // Leemos el nombre directamente (cualquier texto es valido aqui)
        System.out.print("Nombre: ");
        String nombre = scanner.nextLine().trim();

        // Bucle hasta obtener un numero decimal valido para el precio
        double precio = 0;
        while (true) {
            System.out.print("Precio: ");
            String entrada = scanner.nextLine().trim();
            try {
                precio = Double.parseDouble(entrada);
                break; // si llega aqui, el numero fue valido y salimos del bucle
            } catch (NumberFormatException e) {
                System.out.println("  [!] Ingrese un numero valido para el precio.");
            }
        }

        // Bucle hasta obtener un numero entero valido para el stock
        int stock = 0;
        while (true) {
            System.out.print("Stock: ");
            String entrada = scanner.nextLine().trim();
            try {
                stock = Integer.parseInt(entrada);
                break;
            } catch (NumberFormatException e) {
                System.out.println("  [!] Ingrese un numero entero valido para el stock.");
            }
        }

        // Creamos el objeto con los datos capturados (sin ID todavia)
        Producto producto = new Producto(nombre, precio, stock);

        try {
            // El service valida y guarda; si algo falla lanza una excepcion
            service.registrar(producto);
            // Si llega aqui, todo salio bien y el producto ya tiene ID de la BD
            System.out.println("  [OK] Producto registrado con ID: " + producto.getId());
        } catch (IllegalArgumentException e) {
            // Error de validacion: nombre vacio, precio negativo, etc.
            System.out.println("  [Error de validacion] " + e.getMessage());
        } catch (RuntimeException e) {
            // Error de base de datos u otro error inesperado
            System.out.println("  [Error] " + e.getMessage());
        }
    }

    /**
     * Consulta todos los productos en la BD y los muestra en una tabla.
     *
     * Si no hay productos, avisa al usuario.
     * Si hay productos, imprime encabezado + filas + total.
     */
    public void mostrarProductos() {
        System.out.println("\n--- Lista de Productos ---");

        try {
            List<Producto> productos = service.obtenerTodos();

            if (productos.isEmpty()) {
                System.out.println("  No hay productos registrados.");
                return; // salimos del metodo sin intentar imprimir la tabla
            }

            // Linea separadora que se reutiliza para el encabezado y el pie
            String linea = "+------+---------------------------+------------+--------+";
            System.out.println(linea);
            // %-4s = texto alineado a la izquierda en un campo de 4 caracteres
            System.out.printf("| %-4s | %-25s | %-11s | %-6s |%n",
                    "ID", "Nombre", "Precio", "Stock");
            System.out.println(linea);

            // Cada producto se imprime usando su metodo toString()
            for (Producto p : productos) {
                System.out.println(p);
            }

            System.out.println(linea);
            System.out.println("  Total de productos: " + productos.size());

        } catch (RuntimeException e) {
            System.out.println("  [Error] " + e.getMessage());
        }
    }
}
