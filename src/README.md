# Sistema de Pedidos — Food Store

Aplicación de consola en **Java** para la gestión integral de un comercio tipo *food store*: administración de categorías, productos, usuarios y pedidos, con control de stock, estados, formas de pago y un menú interactivo.

> **Desarrollado por:** Alexander Sequera y Alejandro Ruiz Díaz

---

## 📋 Tabla de contenidos

- [Descripción general](#-descripción-general)
- [Funcionalidades](#-funcionalidades)
- [Arquitectura del proyecto](#-arquitectura-del-proyecto)
- [Estructura de carpetas](#-estructura-de-carpetas)
- [Modelo de dominio](#-modelo-de-dominio)
- [Requisitos](#-requisitos)
- [Cómo compilar y ejecutar](#-cómo-compilar-y-ejecutar)
- [Ejemplo de uso](#-ejemplo-de-uso)
- [Manejo de errores](#-manejo-de-errores)
- [Decisiones de diseño](#-decisiones-de-diseño)

---

## 📌 Descripción general

El programa simula el back-office de una tienda de comidas. Al iniciarlo se muestra un menú principal desde el cual el usuario puede gestionar las cuatro entidades del sistema. Cada módulo cuenta con sus propias operaciones CRUD (crear, listar, editar, eliminar) y reglas de negocio (validaciones de stock, duplicados, datos obligatorios, etc.).

La persistencia es **en memoria** (estructuras `ArrayList`): los datos viven mientras dura la ejecución del programa. Se utiliza **borrado lógico** (`eliminado = true`) en lugar de eliminar registros físicamente, lo que permite mantener integridad referencial entre pedidos y productos/usuarios ya dados de baja.

---

## ✨ Funcionalidades

### Categorías
- Crear, listar (todas / activas), editar y eliminar categorías.
- Validación de nombre único y datos obligatorios.

### Productos
- Crear, listar, editar y eliminar productos.
- Listado filtrado: por categoría, con stock disponible, activos.
- Asociación obligatoria a una categoría existente y no eliminada.
- Control de precio, stock, disponibilidad e imagen.

### Usuarios
- Alta, modificación, listado y baja de usuarios.
- Roles disponibles: `ADMIN` y `USUARIO`.
- Validación de mail único.

### Pedidos
- Creación de pedidos asociados a un usuario, con uno o más ítems.
- Descuento automático de stock al confirmar el pedido.
- Cálculo automático del total (interfaz `Calculable`).
- Actualización de estado (`PENDIENTE`, `CONFIRMADO`, `TERMINADO`, `CANCELADO`).
- Selección de forma de pago (`EFECTIVO`, `TARJETA`, `TRANSFERENCIA`).
- Eliminación lógica de pedidos.

---

## 🏗 Arquitectura del proyecto

El código sigue una separación por capas inspirada en MVC adaptado a una app de consola:

```
┌──────────────────┐
│       UI         │  ← Menús, interacción con el usuario (Scanner)
├──────────────────┤
│    Services      │  ← Lógica de negocio y validaciones
├──────────────────┤
│    Entities      │  ← Modelo de dominio (POJOs con herencia)
├──────────────────┤
│ Enums / Excepts. │  ← Estados, roles, errores personalizados
├──────────────────┤
│      Utils       │  ← Helpers, mensajes y funciones reutilizables
└──────────────────┘
```

Cada capa depende únicamente de las capas inferiores, lo que facilita el mantenimiento y la extensión del sistema.

---

## 📁 Estructura de carpetas

```
src/
├── Main.java                         # Punto de entrada del programa
│
├── entities/                         # Modelo de dominio
│   ├── Base.java                     # Clase abstracta base (id, eliminado, createdAt)
│   ├── Calculable.java               # Interfaz para entidades con total calculable
│   ├── Categoria.java
│   ├── Producto.java
│   ├── Usuario.java
│   ├── Pedido.java
│   └── DetallePedido.java
│
├── enums/                            # Tipos enumerados
│   ├── Estado.java                   # PENDIENTE | CONFIRMADO | TERMINADO | CANCELADO
│   ├── FormaPago.java                # EFECTIVO | TARJETA | TRANSFERENCIA
│   └── Rol.java                      # ADMIN | USUARIO
│
├── exception/                        # Excepciones personalizadas (runtime)
│   ├── DatoDuplicadaException.java
│   ├── DatoInexistenteException.java
│   ├── DatoInvalidoException.java
│   └── StockInsuficienteException.java
│
├── services/                         # Lógica de negocio
│   ├── CategoriaServices.java
│   ├── ProductoServices.java
│   ├── UsuarioServices.java
│   ├── PedidosServices.java
│   └── ItemPedido.java               # DTO para crear pedidos
│
├── ui/                               # Menús de consola
│   ├── MenuPrincipal.java
│   ├── MenuCategoria.java
│   ├── MenuProducto.java
│   ├── MenuUsuarios.java
│   └── MenuPedidos.java
│
└── utils/                            # Utilidades y mensajes
    ├── UtilsGeneral.java
    ├── UtilsMenu.java
    ├── UtilsListar.java
    ├── MensajesGenerales.java
    ├── MensajesCategoria.java
    ├── MensajesProducto.java
    ├── MensajesUsuarios.java
    └── MensajesPedidos.java
```

---

## 🧱 Modelo de dominio

Todas las entidades heredan de la clase abstracta **`Base`**, que centraliza tres atributos comunes:

| Atributo    | Tipo            | Descripción                              |
|-------------|-----------------|------------------------------------------|
| `id`        | `Long`          | Identificador único auto-incremental     |
| `eliminado` | `boolean`       | Marca de borrado lógico                  |
| `createdAt` | `LocalDateTime` | Fecha y hora de creación                 |

### Relaciones principales

```
Categoria 1 ──── * Producto
Usuario   1 ──── * Pedido    1 ──── * DetallePedido * ──── 1 Producto
```

- Un **Producto** pertenece a una **Categoría**.
- Un **Pedido** pertenece a un **Usuario** y contiene una lista de **DetallePedido**.
- Cada **DetallePedido** referencia un **Producto** y guarda cantidad y subtotal.
- `Pedido` implementa la interfaz `Calculable`, que define `calcularTotal()`.

---

## ⚙ Requisitos

- **JDK 8** o superior (se usan `LocalDate`, `LocalDateTime` y otras APIs modernas).
- Cualquier IDE compatible con Java (IntelliJ IDEA, Eclipse, VS Code con Extension Pack for Java) o terminal con `javac` y `java` en el `PATH`.

---

## 🚀 Cómo compilar y ejecutar

### Desde la terminal (PowerShell en Windows)

Desde la raíz del proyecto (la carpeta que contiene `src/`):

```powershell
# 1. Crear carpeta de salida
mkdir out

# 2. Compilar todos los archivos .java
javac -d out (Get-ChildItem -Recurse src -Filter *.java | ForEach-Object { $_.FullName })

# 3. Ejecutar
java -cp out Main
```

### Desde la terminal (Linux / macOS)

```bash
mkdir -p out
find src -name "*.java" | xargs javac -d out
java -cp out Main
```

### Desde un IDE

1. Abrir el proyecto.
2. Marcar la carpeta `src` como *Sources Root* (si el IDE no lo detecta automáticamente).
3. Ejecutar la clase `Main`.

---

## 💡 Ejemplo de uso

Al iniciar el programa aparece el menú principal:

```
=====================================
|| SISTEMA DE PEDIDOS (FOOD STORE) ||
=====================================
|| 1. Categorias                   ||
|| 2. Productos                    ||
|| 3. Usuarios                     ||
|| 4. Pedidos                      ||
|| 0. Salir                        ||
=====================================
Seleccione una opcion:
```

**Flujo típico para crear un pedido:**

1. Crear al menos una **Categoría** (opción 1 → 1).
2. Crear uno o más **Productos** asignándolos a esa categoría (opción 2 → 1).
3. Crear un **Usuario** (opción 3 → 1).
4. Ir a **Pedidos** (opción 4 → 2), seleccionar el usuario, agregar ítems, elegir forma de pago.
5. El sistema valida stock, calcula el total y descuenta automáticamente las unidades del inventario.

---

## ⚠ Manejo de errores

El sistema define cuatro excepciones personalizadas que extienden `RuntimeException`:

| Excepción                       | Cuándo se lanza                                              |
|---------------------------------|--------------------------------------------------------------|
| `DatoInvalidoException`         | Datos nulos, vacíos o fuera de rango                         |
| `DatoDuplicadaException`        | Intento de crear un registro con un nombre/mail ya existente |
| `DatoInexistenteException`      | Búsqueda por ID que no encuentra un registro activo          |
| `StockInsuficienteException`    | Pedido con cantidad mayor al stock disponible                |

Las excepciones se capturan en la capa de UI para mostrar mensajes amigables sin interrumpir la ejecución.

---

## 🧠 Decisiones de diseño

- **Herencia con `Base`**: evita repetir id, fecha de creación y flag de borrado en cada entidad.
- **Borrado lógico**: preserva integridad histórica. Los pedidos pasados siguen mostrando productos aunque hoy estén dados de baja.
- **Interfaz `Calculable`**: pensada para futuras entidades que necesiten un total (ej. facturas, presupuestos).
- **Separación de mensajes en `utils/Mensajes*`**: facilita internacionalización y mantenimiento de textos.
- **DTO `ItemPedido`**: desacopla el formato de entrada (id de producto + cantidad) de la entidad `DetallePedido`, que ya requiere un objeto `Producto` resuelto.
- **Inyección de dependencias por constructor**: los servicios reciben sus dependencias en lugar de instanciarlas (`PedidosServices` recibe `UsuarioServices` y `ProductoServices`).

---

## 📄 Licencia

Proyecto académico desarrollado con fines educativos.

---

**Autores:** Alexander Sequera · Alejandro Ruiz Díaz
