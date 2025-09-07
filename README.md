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
- **Spring Cloud (Eureka, Gateway, WebFlux)**
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
   git clone https://github.com/RolyAri/inventory-management-microservices.git
   cd inventory-management-microservices
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

