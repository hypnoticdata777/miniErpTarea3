# Mini ERP — Tarea 3
**Carlos Sanchez Gonzalez | Cuatrimestre 8**

Sistema de gestión de productos desarrollado en Java con conexión a base de datos MySQL.
Permite registrar productos y consultar el inventario desde la terminal.

---

## Requisitos previos

Antes de correr el proyecto necesitas tener instalado:

| Herramienta | Versión usada | Para qué sirve |
|---|---|---|
| JDK | 17 o superior | Compilar y ejecutar Java |
| XAMPP | Cualquiera reciente | Proveer el servidor MySQL |

> El proyecto ya incluye el conector de MySQL (`mysql-connector-j`) descargado en la carpeta `.m2` local. No necesitas instalarlo manualmente.

---

## Estructura del proyecto

```
miniErp_carlosSanchezGonzalez_tarea3/
│
├── sql/
│   └── schema.sql              ← Script para crear la base de datos
│
├── src/main/java/com/minierp/
│   ├── Main.java               ← Punto de entrada, muestra el menú
│   ├── database/
│   │   └── DatabaseConnection.java   ← Maneja la conexión a MySQL
│   └── producto/
│       ├── Producto.java             ← Modelo (representa una fila de la BD)
│       ├── ProductoRepository.java   ← Acceso a datos (SQL)
│       ├── ProductoService.java      ← Reglas de negocio y validaciones
│       └── ProductoController.java   ← Interacción con el usuario
│
├── run.ps1                     ← Script para ejecutar fácilmente en Windows
└── pom.xml                     ← Configuración del proyecto Maven
```

**¿Cómo fluye el programa?**
```
Usuario escribe → Main → ProductoController → ProductoService → ProductoRepository → MySQL
                                          ← resultado ←
```

---

## Cómo correrlo

### Paso 1 — Iniciar MySQL en XAMPP

1. Abre **XAMPP Control Panel**
2. Haz click en **Start** junto a **MySQL** (debe ponerse verde)
3. Si Apache no está corriendo y quieres usar phpMyAdmin, inicia Apache también

> Si el puerto 3306 está ocupado por otro servicio de Windows, abre PowerShell como administrador y ejecuta `net stop mysql`, luego intenta iniciar desde XAMPP de nuevo.

### Paso 2 — Crear la base de datos (solo la primera vez)

1. Asegúrate de que **Apache** también esté iniciado en XAMPP (lo necesita phpMyAdmin)
2. Abre tu navegador y entra a `http://localhost/phpmyadmin`
3. En el menú superior haz click en la pestaña **SQL**
4. Copia y pega el contenido del archivo `sql/schema.sql` en el cuadro de texto:

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

5. Haz click en el botón **Continuar** (o **Go**) para ejecutarlo
6. En el panel izquierdo debería aparecer la base de datos `mini_erp` con la tabla `producto`

> Solo necesitas hacer esto una vez. Los datos que registres se conservan entre ejecuciones mientras MySQL esté corriendo.

### Paso 3 — Ejecutar la aplicación

Desde la terminal en la carpeta raíz del proyecto:

```powershell
.\run.ps1
```

Verás el menú principal:

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
Seleccione una opcion:
```

---

## Uso del sistema

### Registrar un producto
Selecciona la opción `1` e ingresa los datos cuando se soliciten:
```
--- Registrar Producto ---
Nombre: Arroz
Precio: 25.50
Stock: 100
  [OK] Producto registrado con ID: 1
```

- Si escribes letras donde va un número, el sistema te lo vuelve a pedir.
- El nombre no puede quedar vacío.
- El precio debe ser mayor a 0.
- El stock puede ser 0 pero no negativo.

### Consultar productos
Selecciona la opción `2` para ver todos los productos registrados:
```
--- Lista de Productos ---
+------+---------------------------+------------+--------+
| ID   | Nombre                    | Precio      | Stock  |
+------+---------------------------+------------+--------+
| 1    | Arroz                     | $25.50     | 100    |
| 2    | Frijoles                  | $30.00     | 50     |
+------+---------------------------+------------+--------+
  Total de productos: 2
```

### Salir
Selecciona la opción `0` para cerrar el programa correctamente.

---

## Base de datos

**Nombre:** `mini_erp`

**Tabla:** `producto`

| Campo | Tipo | Descripción |
|---|---|---|
| id | INT AUTO_INCREMENT | Identificador único, se asigna solo |
| nombre | VARCHAR(100) | Nombre del producto |
| precio | DECIMAL(10,2) | Precio con hasta 2 decimales |
| stock | INT | Cantidad disponible |

---

## Configuración de conexión

El archivo [DatabaseConnection.java](src/main/java/com/minierp/database/DatabaseConnection.java) contiene los datos de conexión:

```java
URL      = "jdbc:mysql://localhost:3306/mini_erp"
USER     = "root"
PASSWORD = ""   // XAMPP no tiene contraseña por defecto
```

Si tu MySQL tiene contraseña, edita la línea `PASSWORD` en ese archivo y vuelve a compilar.

---

## Tecnologías usadas

- **Java 17** — Lenguaje de programación
- **MySQL / MariaDB** (vía XAMPP) — Base de datos
- **mysql-connector-j 8.3.0** — Driver JDBC para conectar Java con MySQL
- **Maven** — Gestión de dependencias y compilación
