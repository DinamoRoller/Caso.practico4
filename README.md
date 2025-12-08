# Caso practico4
# Caso Práctico 4: Sistema Reactivo de Notificaciones (WebFlux + SSE)

Este proyecto implementa un sistema de notificaciones en tiempo real utilizando **Spring WebFlux** y **MongoDB Reactivo**. Permite a los usuarios recibir alertas instantáneas mediante **Server-Sent Events (SSE)** sin necesidad de recargar la página.

##  Integrantes
- María Díaz - Heredero López 152337
- Cintia Santillán García 154184
- Suren Hashemi Alam
## 🛠️ Tecnologías
* **Java 21** & **Spring Boot 3.2**
* **Spring WebFlux**: Para el manejo de flujos reactivos no bloqueantes.
* **MongoDB Reactive**: Persistencia de datos asíncrona.
* **Thymeleaf (Data-Driven)**: Renderizado de vistas con soporte para streams SSE.

##  Arquitectura de la Solución

La aplicación sigue una arquitectura en capas reactiva:

1.  **Modelo (`Notificacion`)**: Documento MongoDB que representa la alerta.
2.  **Repositorio (`NotificacionRepository`)**: Extiende de `ReactiveMongoRepository` para operaciones CRUD no bloqueantes.
3.  **Servicio (`NotificacionService`)**:
    * Utiliza un **`Sinks.Many` (Multicast)** para crear un "canal caliente" de datos.
    * Fusiona el historial de la base de datos (Cold Stream) con los eventos en vivo (Hot Stream) usando `Flux.concat`.
4.  **Controlador (`NotificacionController`)**:
    * Expone el endpoint `/notificaciones/{usuario}`.
    * Usa `ReactiveDataDriverContextVariable` para habilitar el modo **Data-Driven** de Thymeleaf, permitiendo que el HTML se actualice automáticamente fila a fila.

##  Cómo ejecutar

1.  Asegurar que MongoDB está corriendo en `localhost:27017`.
2.  Ejecutar la clase `NotificacionesApplication`.
3.  Abrir `http://localhost:8080/notificaciones/usuario1` en múltiples pestañas para probar la sincronización en tiempo real.
