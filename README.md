# **ms-springcloud-gateway**

## **Descripción General**
El microservicio **ms-springcloud-gateway** actúa como el punto de entrada central para todas las solicitudes externas en la arquitectura basada en microservicios de la aplicación **Relatos de Papel**. Su función principal es enrutar dinámicamente las solicitudes a los microservicios correspondientes mediante **Eureka**, gestionar políticas de reintento y manejar fallos a través del patrón de **Circuit Breaker**.

---

## **Configuración del Gateway**

- **Descubrimiento dinámico de servicios:** Utiliza **Eureka** para detectar automáticamente los microservicios registrados, eliminando la necesidad de definir rutas estáticas.
- **Circuit Breaker:** Implementa un mecanismo de protección que redirige a controladores de fallback cuando un microservicio no responde o está fuera de servicio.
- **CORS global:** Permite solicitudes desde diferentes orígenes según la configuración del proyecto.
- **Rutas definidas:** Se establecen rutas específicas para cada microservicio (por ejemplo, `ms-books-catalogue` y `ms-books-payments`) y una ruta genérica para manejar los casos no configurados.

---

## **Rutas Configuradas**

### **1. Ruta: `ms-books-catalogue`**
- **URI:** `lb://ms-books-catalogue`
- **Rutas accesibles:** Endpoints relacionados con el catálogo de libros, como el acceso a la API de libros, documentación Swagger y health check.
- **Circuit Breaker:** Si el servicio no responde, redirige al controlador de fallback `/fallback/books` mostrando un mensaje de error al usuario.

### **2. Ruta: `ms-books-payments`**
- **URI:** `lb://ms-books-payments`
- **Rutas accesibles:** Endpoints relacionados con la gestión de pagos de libros.
- **Circuit Breaker:** Si el servicio no está disponible, redirige a `/fallback/payments` mostrando un mensaje informativo.

### **3. Ruta genérica para casos no configurados**
- **Ruta predeterminada:** Se activa si una solicitud no coincide con ninguna de las rutas configuradas.
- **Fallback genérico:** Muestra un mensaje de error indicando que el recurso no fue encontrado.

---

## **Componentes Importantes**

### **1. FallbackController**
El **FallbackController** maneja las respuestas personalizadas cuando un microservicio no está disponible. Cada vez que el **Circuit Breaker** detecta una falla, redirige la solicitud al controlador de fallback, proporcionando una respuesta clara y específica al cliente.

- **/fallback/payments:** Mensaje cuando el servicio de pagos no responde.
- **/fallback/books:** Mensaje cuando el servicio de libros no está disponible.
- **/fallback/not-found:** Mensaje genérico para rutas no configuradas.

### **2. PostFilter (Filtro Global)**
El filtro global se encarga de interceptar solicitudes POST y validar si se está intentando cambiar el método a través del encabezado **`X-Http-Method-Override`**. Si se detecta que el método POST está siendo sobrescrito con un método no permitido, el filtro bloquea la solicitud y devuelve un mensaje de error.

- **Objetivo:** Garantizar la seguridad al evitar que se modifiquen dinámicamente las solicitudes POST hacia otros métodos no autorizados.
- **Orden:** Tiene una alta prioridad para asegurar que se ejecute antes de otras operaciones de filtrado.

---

## **Pasos para Subir el Proyecto conhttps://github.com/Angelica-Quevedo-unir/ms-springcloud-gateway Spring Boot**

1. **Clonar el repositorio del proyecto**
   ```bash
   git clone https://github.com/Angelica-Quevedo-unir/ms-springcloud-gateway
   ```

2. **Configurar el archivo `application.yml`**  
   Verifica que los valores en el archivo `application.yml` sean correctos, especialmente la URL de Eureka y las rutas definidas.

3. **Construir el proyecto**  
   Compila y construye el proyecto con Maven:
   ```bash
   mvn clean package
   ```

4. **Ejecutar el proyecto**  
   Inicia el microservicio con:
   ```bash
   mvn spring-boot:run
   ```

5. **Verificar el gateway**  
   Accede a los endpoints configurados a través del puerto especificado en el archivo de configuración.

---

## **Comportamiento del Circuit Breaker**
El **Circuit Breaker** monitorea las solicitudes a los microservicios y redirige automáticamente a los controladores de fallback cuando detecta fallos persistentes, como tiempos de espera o errores en el servicio. Esto garantiza la continuidad del sistema y evita sobrecargas.

- **Cuando falla `ms-books-catalogue`:** Muestra un mensaje al usuario informando que el servicio de libros no está disponible.
- **Cuando falla `ms-books-payments`:** Informa al usuario que el servicio de pagos no está disponible.
- **Fallback genérico:** Se activa cuando la solicitud no coincide con ninguna ruta configurada, devolviendo un mensaje de error predeterminado.

Con esta configuración, el gateway gestiona eficazmente las solicitudes y mantiene la resiliencia de la aplicación en caso de fallos.
```