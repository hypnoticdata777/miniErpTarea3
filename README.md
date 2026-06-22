# Mini ERP — Tarea 3
**Carlos Sanchez Gonzalez | Cuatrimestre 8**

Sistema de gestión de productos desarrollado en Java puro con conexión a MySQL.
Permite registrar productos y consultar el inventario desde la terminal (CLI).

---

## Tabla de contenido

1. [¿Qué es este proyecto?](#qué-es-este-proyecto)
2. [Alcance del MVP](#alcance-del-mvp)
3. [Arquitectura — "Las piezas del coche"](#arquitectura--las-piezas-del-coche)
4. [Estructura del proyecto](#estructura-del-proyecto)
5. [Cómo correrlo](#cómo-correrlo)
6. [Uso del sistema](#uso-del-sistema)
7. [Base de datos](#base-de-datos)
8. [Problemas conocidos](#problemas-conocidos)
9. [Próximas mejoras sugeridas](#próximas-mejoras-sugeridas)
10. [Pruebas](#pruebas)
11. [Tecnologías usadas](#tecnologías-usadas)

---

## ¿Qué es este proyecto?

Un **Mini ERP de consola** enfocado en el módulo de Productos. El usuario puede:

- **Registrar** un producto nuevo (nombre, precio, stock)
- **Listar** todos los productos almacenados en una tabla formateada
- **Persistir** los datos en una base de datos MySQL local (vía XAMPP)

El código aplica una arquitectura en capas estándar de Java empresarial, sin ningún framework externo, usando solo JDBC puro.

---

## Alcance del MVP

| Característica | Incluida | Notas |
|---|:---:|---|
| Crear producto | ✅ | Con validación de tipos y reglas de negocio |
| Listar productos | ✅ | Tabla formateada con total al pie |
| Editar producto | ❌ | Fuera del alcance actual |
| Eliminar producto | ❌ | Fuera del alcance actual |
| Buscar/filtrar | ❌ | Fuera del alcance actual |
| Autenticación | ❌ | Fuera del alcance actual |
| Interfaz gráfica | ❌ | Solo CLI (terminal) |
| Pruebas automatizadas | ❌ | No implementadas aún |
| Múltiples entidades | ❌ | Solo `Producto` |

**Entidad única:** `Producto` con campos `id`, `nombre`, `precio`, `stock`.

**Reglas de negocio validadas:**
- El nombre no puede estar vacío
- El precio debe ser mayor a 0
- El stock puede ser 0 pero no negativo

---

## Arquitectura — "Las piezas del coche"

El proyecto sigue una **arquitectura en 4 capas**. Cada capa es independiente y reutilizable: puedes copiar el patrón completo a cualquier proyecto Java+MySQL y solo cambiar los nombres de la entidad.

```
┌──────────────────────────────────────────────────────────┐
│  Main.java          ← Menú y bucle principal             │
│                       (orquestador, no tiene lógica)     │
└─────────────────────────┬────────────────────────────────┘
                          │ llama
┌─────────────────────────▼────────────────────────────────┐
│  [PIEZA 1] ProductoController.java  ← Capa de Presentación│
│                                                           │
│  • Lee lo que escribe el usuario (Scanner)               │
│  • Valida el TIPO del dato (número, entero…)             │
│  • Llama al Service y muestra el resultado               │
│  • NO conoce SQL, NO tiene reglas de negocio             │
└─────────────────────────┬────────────────────────────────┘
                          │ llama
┌─────────────────────────▼────────────────────────────────┐
│  [PIEZA 2] ProductoService.java  ← Capa de Negocio       │
│                                                           │
│  • Valida las REGLAS de negocio (precio > 0, etc.)       │
│  • Lanza IllegalArgumentException si algo no cumple      │
│  • NO sabe cómo se guarda el dato, NO imprime nada       │
└─────────────────────────┬────────────────────────────────┘
                          │ llama
┌─────────────────────────▼────────────────────────────────┐
│  [PIEZA 3] ProductoRepository.java  ← Capa de Datos      │
│                                                           │
│  • El ÚNICO lugar donde existe SQL en toda la app        │
│  • Usa PreparedStatement (protección contra SQL injection)│
│  • Devuelve objetos Java, nunca ResultSet crudos         │
│  • Cierra conexiones con try-with-resources              │
└─────────────────────────┬────────────────────────────────┘
                          │ llama
┌─────────────────────────▼────────────────────────────────┐
│  [PIEZA 4] DatabaseConnection.java  ← Fábrica de conexión│
│                                                           │
│  • Único responsable de crear la conexión JDBC           │
│  • Constructor privado (no se puede instanciar)          │
│  • Método estático getConnection() que cualquiera llama  │
└─────────────────────────┬────────────────────────────────┘
                          │
┌─────────────────────────▼────────────────────────────────┐
│  MySQL  (base de datos local vía XAMPP)                  │
└──────────────────────────────────────────────────────────┘
```

### Resumen de piezas reutilizables

| Pieza | Archivo | Patrón | Para qué sirve en otro proyecto |
|---|---|---|---|
| Modelo / Entidad | `Producto.java` | POJO / Entity | Copia, cambia nombre y campos; representa una fila de BD |
| Repositorio | `ProductoRepository.java` | DAO (Data Access Object) | Copia, cambia la entidad y el SQL; toda la lógica de BD va aquí |
| Servicio | `ProductoService.java` | Service / Facade | Copia, cambia la validación; toda la lógica de negocio va aquí |
| Controlador CLI | `ProductoController.java` | Controller / Presenter | Copia, cambia los campos que lees del Scanner |
| Conexión BD | `DatabaseConnection.java` | Static Factory | Reutilizable sin cambio en cualquier app Java+MySQL |
| Script SQL | `sql/schema.sql` | Migration script | Copia, cambia nombre de BD y tabla |
| Menú principal | `Main.java` | Orchestrator | Agrega más opciones al switch/if-else del menú |

---

## Estructura del proyecto

```
miniErpTarea3/
│
├── sql/
│   └── schema.sql              ← Script para crear la base de datos
│
├── src/main/java/com/minierp/
│   ├── Main.java               ← Punto de entrada y menú principal
│   ├── database/
│   │   └── DatabaseConnection.java   ← Fábrica de conexiones JDBC
│   └── producto/
│       ├── Producto.java             ← Modelo (campos de la tabla)
│       ├── ProductoRepository.java   ← SQL: guardar y consultar
│       ├── ProductoService.java      ← Validaciones de negocio
│       └── ProductoController.java   ← Interacción con el usuario
│
├── run.ps1                     ← Script para ejecutar en Windows (PowerShell)
└── pom.xml                     ← Configuración Maven (Java 17, mysql-connector)
```

---

## Cómo correrlo

### Paso 1 — Iniciar MySQL en XAMPP

1. Abre **XAMPP Control Panel**
2. Haz click en **Start** junto a **MySQL** (debe ponerse verde)
3. Opcional: inicia también Apache si quieres usar phpMyAdmin

> Si el puerto 3306 está ocupado, abre PowerShell como administrador y ejecuta `net stop mysql`, luego intenta desde XAMPP de nuevo.

### Paso 2 — Crear la base de datos (solo la primera vez)

Abre **phpMyAdmin** (`http://localhost/phpmyadmin`), ve a la pestaña **SQL** y ejecuta:

```sql
CREATE DATABASE IF NOT EXISTS mini_erp;
USE mini_erp;

CREATE TABLE IF NOT EXISTS producto (
    id      INT AUTO_INCREMENT PRIMARY KEY,
    nombre  VARCHAR(100) NOT NULL,
    precio  DECIMAL(10, 2) NOT NULL,
    stock   INT NOT NULL
);
```

### Paso 3 — Ejecutar la aplicación

Desde la carpeta raíz del proyecto en PowerShell:

```powershell
.\run.ps1
```

---

## Uso del sistema

### Registrar un producto

```
========================================
       MINI ERP - Carlos Sanchez G.
========================================
  Modulo: Productos
----------------------------------------
  1. Registrar Producto
  2. Mostrar Productos
  0. Salir
========================================
Seleccione una opcion: 1

--- Registrar Producto ---
Nombre: Arroz
Precio: 25.50
Stock: 100
  [OK] Producto registrado con ID: 1
```

- Si escribes letras donde va un número, el sistema te lo vuelve a pedir
- El nombre no puede quedar vacío
- El precio debe ser mayor a 0
- El stock puede ser 0 pero no negativo

### Listar productos

```
Seleccione una opcion: 2

--- Lista de Productos ---
+------+---------------------------+------------+--------+
| ID   | Nombre                    | Precio      | Stock  |
+------+---------------------------+------------+--------+
| 1    | Arroz                     | $25.50     | 100    |
| 2    | Frijoles                  | $30.00     | 50     |
+------+---------------------------+------------+--------+
  Total de productos: 2
```

---

## Base de datos

**Nombre:** `mini_erp`  
**Tabla:** `producto`

| Campo | Tipo SQL | Descripción |
|---|---|---|
| `id` | `INT AUTO_INCREMENT PK` | Identificador único, se asigna automáticamente |
| `nombre` | `VARCHAR(100) NOT NULL` | Nombre del producto (máx. 100 caracteres) |
| `precio` | `DECIMAL(10,2) NOT NULL` | Precio con hasta 2 decimales |
| `stock` | `INT NOT NULL` | Cantidad disponible en inventario |

**Configuración de conexión** (`DatabaseConnection.java`):

```java
URL      = "jdbc:mysql://localhost:3306/mini_erp"
USER     = "root"
PASSWORD = ""   // XAMPP no tiene contraseña por defecto
```

Si tu MySQL tiene contraseña, edita `PASSWORD` en ese archivo y recompila.

---

## Problemas conocidos

Estos son los defectos y deudas técnicas identificados en la versión actual:

| # | Problema | Archivo | Impacto |
|---|---|---|---|
| 1 | **Credenciales hardcodeadas** — `root` / password vacío están en el código fuente | `DatabaseConnection.java` | Seguridad: no se puede desplegar sin editar el archivo |
| 2 | **Versión de JDK inconsistente** — `pom.xml` apunta a Java 17 pero `run.ps1` usa JDK-24 | `pom.xml`, `run.ps1` | Puede fallar en máquinas con solo JDK 17 |
| 3 | **`double` para precios** — Java `double` tiene errores de punto flotante; la BD guarda `DECIMAL(10,2)` | `Producto.java` | `0.1 + 0.2 ≠ 0.3`; usar `BigDecimal` es lo correcto |
| 4 | **Sin CRUD completo** — no existe actualizar ni eliminar productos | Toda la app | El usuario no puede corregir un dato mal ingresado |
| 5 | **Acoplamiento fuerte** — `ProductoController` instancia `ProductoService` internamente; `ProductoService` instancia `ProductoRepository` | `.java` de cada capa | Imposible hacer pruebas unitarias sin tocar la BD real |
| 6 | **Presentación en el modelo** — `Producto.toString()` contiene lógica de formato de tabla | `Producto.java` | Viola principio de responsabilidad única (SRP) |
| 7 | **Sin pruebas automatizadas** — cero tests | — | No hay forma de verificar regresiones automáticamente |
| 8 | **`run.ps1` solo para Windows** — no hay script equivalente para Linux/Mac | `run.ps1` | No portable |
| 9 | **Sin paginación** — `obtenerTodos()` carga toda la tabla en memoria | `ProductoRepository.java` | Con miles de productos, la memoria se agota |
| 10 | **Sin conexión pooling** — cada operación abre y cierra una conexión nueva | `DatabaseConnection.java` | Lento e ineficiente bajo carga |

---

## Próximas mejoras sugeridas

Ordenadas de menor a mayor esfuerzo:

### Corto plazo (sin cambiar la arquitectura)

1. **Mover credenciales a un archivo `.properties`** — leer `db.url`, `db.user`, `db.password` desde `src/main/resources/config.properties` y agregar ese archivo al `.gitignore`. Es el cambio más urgente.

2. **Cambiar `double` por `BigDecimal`** en `Producto.java` y actualizar constructor, getter/setter, y el parse en `ProductoController`. Cuatro cambios de una línea.

3. **Agregar opción 3: Editar producto** — pedir el ID, cargar el producto, preguntar cuál campo cambiar, hacer `UPDATE` en la BD. Reutiliza exactamente el mismo patrón de las capas existentes.

4. **Agregar opción 4: Eliminar producto** — pedir el ID, confirmar, ejecutar `DELETE`. Misma estructura.

5. **Unificar versión de JDK** — en `run.ps1` cambiar la ruta a `jdk-17` (o la que esté instalada) para que coincida con `pom.xml`.

### Mediano plazo (mejoran la calidad del código)

6. **Inyección de dependencias manual** — en lugar de `new ProductoService()` dentro del Controller, recibirlo como parámetro en el constructor. Esto hace el código testeable sin BD real.

7. **Agregar pruebas unitarias con JUnit 5** — al menos para `ProductoService.validar()`: casos feliz, nombre vacío, precio negativo, stock negativo. No requieren BD (ver sección siguiente).

8. **Mover la lógica de formato de tabla** de `Producto.toString()` a un método `imprimirFila(Producto p)` dentro de `ProductoController`. El modelo solo debe ser un contenedor de datos.

9. **Script de ejecución multiplataforma** — reemplazar `run.ps1` con un `Makefile` que funcione en Linux/Mac/Windows.

### Largo plazo (mejoras de escala y funcionalidad)

10. **Búsqueda y filtrado** — `buscarPorNombre(String texto)` y `filtrarPorStock(int minimo)` en el Repository con cláusulas `WHERE` parametrizadas.

11. **Paginación** — `obtenerPagina(int pagina, int tamano)` en el Repository usando `LIMIT` y `OFFSET`.

12. **Segundo módulo: Ventas** — el patrón de 4 capas se replica exactamente. Crear `Venta.java`, `VentaRepository.java`, `VentaService.java`, `VentaController.java` y agregar la opción al menú en `Main.java`.

13. **Connection pooling** — agregar HikariCP (un JAR, cero configuración) para reutilizar conexiones en lugar de abrir una nueva por operación.

---

## Pruebas

### Estado actual

**No hay pruebas automatizadas.** Todas las validaciones son manuales (el desarrollador ejecuta el programa y prueba a mano).

### Plan de pruebas mínimo recomendado

Para agregar JUnit 5, añade esta dependencia en `pom.xml`:

```xml
<dependency>
    <groupId>org.junit.jupiter</groupId>
    <artifactId>junit-jupiter</artifactId>
    <version>5.10.2</version>
    <scope>test</scope>
</dependency>
```

Y crea el archivo `src/test/java/com/minierp/producto/ProductoServiceTest.java`:

```java
class ProductoServiceTest {

    private ProductoService service = new ProductoService();

    @Test
    void registrar_nombreVacio_lanzaExcepcion() {
        Producto p = new Producto("", 10.0, 5);
        assertThrows(IllegalArgumentException.class, () -> service.registrar(p));
    }

    @Test
    void registrar_precioNegativo_lanzaExcepcion() {
        Producto p = new Producto("Arroz", -1.0, 5);
        assertThrows(IllegalArgumentException.class, () -> service.registrar(p));
    }

    @Test
    void registrar_stockNegativo_lanzaExcepcion() {
        Producto p = new Producto("Arroz", 10.0, -1);
        assertThrows(IllegalArgumentException.class, () -> service.registrar(p));
    }

    @Test
    void registrar_stockCero_esValido() {
        // stock = 0 es permitido; solo falla si llega a la BD (que no tenemos aquí)
        Producto p = new Producto("Arroz", 10.0, 0);
        assertDoesNotThrow(() -> /* validar sin guardar */ {});
    }
}
```

> Nota: para probar `ProductoRepository` sin una BD real se necesitaría una BD embebida (H2) o un mock (Mockito). Eso corresponde a la siguiente iteración.

### Casos de prueba manuales actuales

| Caso | Entrada | Resultado esperado |
|---|---|---|
| Producto válido | nombre="Arroz", precio=25.5, stock=100 | `[OK] Producto registrado con ID: N` |
| Nombre vacío | nombre="", precio=10, stock=5 | `[Error de validacion] El nombre no puede estar vacío` |
| Precio = 0 | nombre="X", precio=0, stock=5 | `[Error de validacion] El precio debe ser mayor a 0` |
| Precio negativo | nombre="X", precio=-5, stock=5 | `[Error de validacion] El precio debe ser mayor a 0` |
| Stock negativo | nombre="X", precio=10, stock=-1 | `[Error de validacion] El stock no puede ser negativo` |
| Texto en precio | nombre="X", precio="abc" | `[!] Ingrese un numero valido para el precio.` (re-pregunta) |
| Sin productos | lista vacía en BD | `No hay productos registrados.` |

---

## Tecnologías usadas

| Herramienta | Versión | Rol |
|---|---|---|
| Java | 17 | Lenguaje principal |
| MySQL / MariaDB | Cualquiera vía XAMPP | Base de datos relacional |
| mysql-connector-j | 8.3.0 | Driver JDBC para conectar Java con MySQL |
| Maven | 3.x | Gestión de dependencias y compilación |
| JDK | 17+ | Compilación y ejecución |
| XAMPP | Cualquiera | Servidor local MySQL |
