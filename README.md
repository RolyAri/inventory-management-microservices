# Microservices with Spring Boot  

Este proyecto implementa una arquitectura de microservicios usando **Spring Boot**, desplegados con **Docker Compose**, que simula un sistema de e-commerce con autenticación, catálogo de productos, órdenes, inventario y notificaciones.  

## 📌 Arquitectura  

![microservices](https://github.com/user-attachments/assets/356143d1-6245-4824-bf97-df24cd8d7270)

### Componentes principales  
- **Discovery Server (Eureka)** → Registro y descubrimiento de microservicios.  
- **API Gateway (Spring Cloud Gateway)** → Entrada única al sistema, con **balanceo de carga** automático hacia los microservicios registrados en Eureka.
- **OAuth2 Server (Keycloak + PostgreSQL)** → Autenticación y autorización.  
- **Product Microservice (Spring Boot + PostgreSQL)** → Gestión de productos.  
- **Orders Microservice (Spring Boot + MySQL)** → Gestión de órdenes, resiliencia con **Resilience4j**.  
- **Inventory Microservice (Spring Boot + PostgreSQL)** → Control de inventario.  
- **Notification Microservice (Spring Boot + Kafka)** → Manejo de notificaciones asincrónicas.  

## 🛠️ Tecnologías usadas  

- **Java 17**  
- **Spring Boot 3**
- **Spring Boot Webflux**
- **Spring Cloud (Eureka, Gateway, Load Balancer)**
- **Spring Boot Actuator** (health, métricas y monitoreo)    
- **Keycloak (OAuth2, JWT)**  
- **PostgreSQL / MySQL**  
- **Kafka + Zookeeper**  
- **Docker & Docker Compose**  
- **Resilience4j (Circuit Breaker)**  

## 🐳 Docker Compose  

El archivo `docker-compose.yml` levanta:  

- **Bases de datos**:  
  - PostgreSQL para Inventario (`5431`), Productos (`5433`), y Keycloak (`5434`).  
  - MySQL para Órdenes (`3307`).  
- **Keycloak** en el puerto `8181`.  
- **Kafka + Zookeeper** en `9092` y `2181`.  

Ejemplo de ejecución:  

```bash
docker-compose up -d
```

Verifica que los contenedores estén corriendo:  

```bash
docker ps
```

## 🔑 Keycloak  

- URL: [http://localhost:8181](http://localhost:8181)  
- Usuario admin: `admin`  
- Password: `admin`  

> Debes configurar un **realm** y un **cliente** en Keycloak para proteger los microservicios vía OAuth2.

👉 [Guía completa de configuración de Keycloak](./KEYCLOAK_CONFIGURATION.md)  

## 🌍 Accesos principales  

- **Eureka Server** → [http://localhost:8761](http://localhost:8761)  
- **API Gateway** → [http://localhost:8081](http://localhost:8081)  
- **Keycloak** → [http://localhost:8181](http://localhost:8181)  
- **Kafka Broker** → `localhost:9092`  

![eureka](https://github.com/user-attachments/assets/20502732-62e2-4a07-8fbf-cad9c75276e3)
![keycloak8181](https://github.com/user-attachments/assets/7242d940-e527-4216-a85a-f951a11f7f86)

## 🚀 Cómo ejecutar  

1. Clonar repositorio:  
   ```bash
   git clone https://github.com/RolyAri/microservices-architecture.git
   cd microservices-architecture
   ```

2. Levantar infraestructura (DBs, Keycloak, Kafka):  
   ```bash
   docker-compose up -d
   ```

3. Levantar cada microservicio (desde su carpeta):  
   ```bash
   mvn spring-boot:run
   ```

4. Revisar el registro en **Eureka** y acceder a los endpoints vía **API Gateway**.

## 🔑 Autenticación con Keycloak en Postman  

Para consumir los microservicios necesitas un **token JWT** emitido por Keycloak.  

1. Abre **Postman** → crea una nueva colección o request.  
2. En la pestaña **Authorization** selecciona:  
   - **Auth Type**: `OAuth 2.0`
   - **Token Name**: `kc_token` (puede ser cualquier nombre)  
   - **Grant Type**: `Authorization Code`  
   - **Callback URL**: `http://localhost:8080/login/oauth2/code/keycloak`  
   - **Auth URL**: `http://localhost:8181/realms/microservices-realm/protocol/openid-connect/auth`  
   - **Access Token URL**: `http://localhost:8181/realms/microservices-realm/protocol/openid-connect/token`  
   - **Client ID**: `microservices_client`  
   - **Client Secret**: `TU_SECRET`  
   - **Scope**: `openid`
  
![postman_auth_config](https://github.com/user-attachments/assets/de4721d5-0fca-46ff-94fa-42c2d5c7a482)

3. Haz clic en **Get New Access Token** → Postman abrirá Keycloak para login.
   ![get_new_Access_token](https://github.com/user-attachments/assets/0ace6e04-7257-4224-854f-adf47549308d)

5. Ingresa con el usuario creado en Keycloak (`admin_user` o `basic_user`).  
6. Se generará un **token** y se guardará en Postman.
  ![use_token](https://github.com/user-attachments/assets/cfbc8a00-6c8c-4180-80b6-569a7c28640a)

👉 Desde ahí ya puedes invocar cualquier endpoint protegido con:  

⚠️ **Nota:** Antes de autenticarte en Postman, prueba primero un endpoint `GET` y autenticate con keycloak en el navegador (ejemplo: [http://localhost:8080/api/product](http://localhost:8080/api/product)).  
Esto asegura que las **cookies de sesión de Keycloak** se sincronicen entre el navegador y Postman, evitando errores al solicitar el token.

![sync_cookies](https://github.com/user-attachments/assets/595b549e-3fd1-48d2-a767-2f4fc495ddff)

![Test_api_chrome](https://github.com/user-attachments/assets/b57ce8c0-fd00-44f9-9264-862fede84658)

![response_Test_api](https://github.com/user-attachments/assets/d63f26cc-a9cb-49fe-90c6-36c92d642094)

## 📬 Postman Collection  

Para probar los endpoints de los microservicios, puedes importar la siguiente colección en **Postman**:  

👉 [Descargar colección de Postman](./MICROSERVICIOS.postman_collection.json)  

1. Abre Postman.  
2. Ve a **Importar** → selecciona el archivo `MICROSERVICIOS.postman_collection.json`.  
3. Ejecuta las requests usando los endpoints expuestos en el **API Gateway**.

## 📬 Endpoints

### Orders Service
- GET `/api/order` → Listar todas las órdenes  
- POST `/api/order` → Crear una nueva orden  
  ```json
  {
    "orderItems": [
      {
        "sku": "000001",
        "price": 13500,
        "quantity": 1
      }
    ]
  }
  ```

### Inventory Service
- GET `/api/inventory/{sku}` → Obtener stock por producto  
  Ejemplo: `/api/inventory/000004`

### Products Service
- GET `/api/product` → Listar todos los productos  
- POST `/api/product` → Crear un nuevo producto  
  ```json
  {
    "sku": "000001",
    "name": "Laptop Lenovo IdeaPad 3",
    "description": "Laptop de 15.6 pulgadas con procesador AMD Ryzen 5, 8GB de RAM y 512GB SSD. Ideal para trabajo y estudio.",
    "price": 13500,
    "status": true
  }
  ```

---

⚠️ Nota: Todos los endpoints requieren autenticación vía **Keycloak (OAuth2/JWT)**.  
La colección de Postman ya incluye la configuración con:  
- clientId: `microservices_client`  
- clientSecret: `TU_SECRET`  
- authUrl: `http://localhost:8181/realms/microservices-realm/protocol/openid-connect/auth`  
- tokenUrl: `http://localhost:8181/realms/microservices-realm/protocol/openid-connect/token`  

