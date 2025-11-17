# AeroReserva ✈️
![Java CI](https://img.shields.io/badge/Java-17-blue?logo=java&logoColor=white)
![Build](https://img.shields.io/badge/build-passing-brightgreen)

AeroReserva es un proyecto académico de una plataforma de reserva de vuelos. Es una aplicación de consola construida en Java, enfocada en una arquitectura de 5 capas (Vistas, Servicios, Entidades, Persistencia, Main) y principios de POO.

El sistema persiste los datos manualmente usando `org.json` y utiliza Inyección de Dependencias para gestionar los servicios (Gestores).

---

## ✨ Características Principales

### 👤 Para Clientes (Pasajeros)
* **Gestión de Cuentas:** Registro (con validación de email/contraseña), Login y Perfil de Usuario (modificar datos, email y contraseña).
* **Búsqueda Avanzada:** Soporta búsquedas de "Solo Ida" y "Ida y Vuelta".
* **Lógica de Itinerarios:** El sistema genera automáticamente itinerarios, incluyendo **vuelos directos** y **vuelos con 1 escala**.
* **Reservas Complejas:** Permite la selección de asientos de un mapa (`Map`) y agregado de múltiples piezas de equipaje por tipo.
* **Precios Dinámicos:** El costo final se calcula basado en el precio base, la clase (`BUSINESS`/`ECONOMY`), si el `CarryOn` es gratis en ese vuelo, y las tarifas de equipaje específicas de la `Aerolinea`.
* **Gestión de Reservas:** Permite ver, cancelar (reserva completa o un solo pasajero) y modificar reservas existentes (cambiar asiento, agregar equipaje, cambiar clase).
* **Programa de Lealtad:** Acumulación de millas por cada compra.

### 🛠️ Para Administradores
* **Gestión Maestra (ABMCL):** Control total de Alta, Baja, Modificación y Listado para `Aeropuertos`, `Aviones`, `Aerolineas`, `Vuelos` y `Usuarios`.
* **Gestión de Flota:** Capacidad de crear `Aerolineas` (definiendo sus precios de equipaje) y asignar `Aviones` a sus flotas.
* **Validación de Vuelos:** El sistema **impide** crear un vuelo si el `Avion` seleccionado no pertenece a la `flota` de la `Aerolinea` seleccionada.
* **Protección de Datos:** El sistema **impide** modificar un `Vuelo` si este ya tiene reservas vendidas.

---

## 🛠️ Stack Tecnológico
* **Lenguaje:** Java 17+
* **Persistencia:** `org.json` (Serialización/Deserialización manual)
* **Arquitectura:** Inyección de Dependencias (manual) y patrón `toJSON()` en entidades.

---

