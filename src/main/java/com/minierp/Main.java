package com.minierp;

// ProductoController maneja todo lo relacionado a productos desde la vista
import com.minierp.producto.ProductoController;

import java.util.Scanner;

/**
 * Punto de entrada del programa.
 * Aqui empieza todo: se muestra el menu y se espera que el usuario
 * escriba una opcion. Segun lo que escriba, se llama al controlador
 * correspondiente.
 *
 * Flujo general del proyecto:
 *   Main  →  ProductoController  →  ProductoService  →  ProductoRepository  →  MySQL
 *
 * Main solo sabe mostrar el menu y delegar. No tiene logica de negocio.
 */
public class Main {

    public static void main(String[] args) {
        // try-with-resources: cierra el Scanner automaticamente al salir del bloque
        try (Scanner scanner = new Scanner(System.in)) {

            // El controlador recibe el scanner para poder pedirle datos al usuario
            ProductoController productoController = new ProductoController(scanner);

            // Mientras esta variable sea true, el menu sigue apareciendo
            boolean ejecutando = true;

            while (ejecutando) {
                mostrarMenu();
                System.out.print("Seleccione una opcion: ");

                // Leemos la opcion que escribio el usuario y quitamos espacios extras
                String opcion = scanner.nextLine().trim();

                // Dependiendo de la opcion, ejecutamos la accion correspondiente
                switch (opcion) {
                    case "1" -> productoController.registrarProducto();   // pide datos y guarda en BD
                    case "2" -> productoController.mostrarProductos();    // consulta y muestra la lista
                    case "0" -> {
                        System.out.println("\nSaliendo del sistema. Hasta luego.");
                        ejecutando = false; // esto rompe el bucle y termina el programa
                    }
                    default -> System.out.println("  [!] Opcion no valida. Intente de nuevo.");
                }
            }
        } // el scanner se cierra aqui automaticamente
    }

    /**
     * Imprime el menu principal en la terminal.
     * Es un metodo separado para mantener el main limpio y legible.
     */
    private static void mostrarMenu() {
        System.out.println("\n========================================");
        System.out.println("       MINI ERP - Carlos Sanchez G.    ");
        System.out.println("========================================");
        System.out.println("  Modulo: Productos");
        System.out.println("----------------------------------------");
        System.out.println("  1. Registrar Producto");
        System.out.println("  2. Mostrar Productos");
        System.out.println("  0. Salir");
        System.out.println("========================================");
    }
}
