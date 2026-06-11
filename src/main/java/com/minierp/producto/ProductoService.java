package com.minierp.producto;

import java.util.List;

/**
 * Servicio de Producto: contiene las reglas de negocio.
 *
 * Se ubica entre el Controller (que interactua con el usuario)
 * y el Repository (que habla con la BD). Su trabajo es validar
 * que los datos sean correctos ANTES de intentar guardarlos.
 *
 * Si algo no cumple las reglas, lanza una excepcion con un mensaje
 * claro que el Controller puede mostrar al usuario.
 */
public class ProductoService {

    // El servicio usa el repositorio para acceder a la BD
    private final ProductoRepository repository;

    public ProductoService() {
        this.repository = new ProductoRepository();
    }

    /**
     * Valida el producto y luego lo manda a guardar.
     * Si la validacion falla, lanza IllegalArgumentException
     * y el registro NO se guarda en la BD.
     *
     * @param producto objeto a registrar
     */
    public void registrar(Producto producto) {
        validar(producto);           // primero verificamos que los datos sean validos
        repository.guardar(producto); // si paso la validacion, lo guardamos
    }

    /**
     * Delega directamente al repositorio sin validaciones adicionales,
     * ya que obtener datos no representa ningun riesgo.
     *
     * @return lista de todos los productos en la BD
     */
    public List<Producto> obtenerTodos() {
        return repository.obtenerTodos();
    }

    /**
     * Verifica que el producto cumpla las reglas minimas antes de guardarlo.
     * Es privado porque solo lo usa esta clase — es un detalle interno.
     *
     * @param producto objeto a validar
     * @throws IllegalArgumentException si algun campo es invalido
     */
    private void validar(Producto producto) {
        // El nombre no puede estar vacio ni ser solo espacios
        if (producto.getNombre() == null || producto.getNombre().isBlank()) {
            throw new IllegalArgumentException("El nombre del producto es obligatorio.");
        }
        // El precio debe ser un valor positivo real
        if (producto.getPrecio() <= 0) {
            throw new IllegalArgumentException("El precio debe ser mayor a 0.");
        }
        // El stock puede ser 0 (sin existencias) pero no negativo
        if (producto.getStock() < 0) {
            throw new IllegalArgumentException("El stock debe ser mayor o igual a 0.");
        }
    }
}
