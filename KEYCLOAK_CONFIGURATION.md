# 🔑 Keycloak Configuration Guide

Este documento explica cómo configurar **Keycloak** para integrarlo con el proyecto **Microservices with Spring Boot**.

---

## 1️⃣ Iniciar Keycloak
Levanta el contenedor de Keycloak con Docker Compose:
```bash
docker-compose up -d keycloak
```

Accede a la consola de administración:  
👉 [http://localhost:8181](http://localhost:8181)

Usuario/contraseña por defecto:
```
admin / admin
```

---

## 2️⃣ Crear un Realm
1. En el menú lateral izquierdo, selecciona **Add Realm**.  
2. Nombre: `microservices-realm`  
3. Guardar.

---

## 3️⃣ Crear un Client
1. Dentro del **Realm `microservices-realm`**, ir a **Clients → Create Client**. 
2. Client ID: `microservices_client` 
2. Name: `Microservices Client` 
3. Tipo de cliente: **OpenID Connect**.  
4. Configuración:  
   - **Client Authentication**: ✅ ON  
   - **Authorization**: ✅ ON  
   - **Standard Flow**: ✅ ON
   - **Direct Acces Grants**: ✅ ON
   - **OAuth 2.0 Device Authorization Grant**: ✅ ON
   - **Valid Redirect URIs**:  
     ```
     http://localhost:8080/*
     http://localhost:8080/login/oauth2/code/keycloak
     https://oauth.pstmn.io/v1/browser-callback
     ```
   - **Web Origins**: /*

![keycloak_client_configuration](https://github.com/user-attachments/assets/52cc6dd5-fe94-4bb7-be21-b15de6206d11)

5. Guarda y copia el **Client Secret** generado. Este debe coincidir con la propiedad en el API Gateway:
   
![keycloak_client_secret](https://github.com/user-attachments/assets/3dc545c3-b028-4340-b241-4fe6147c40bb)

```properties
spring.security.oauth2.client.registration.keycloak.client-secret=TU_CLIENT_SECRET
```



---

## 4️⃣ Crear Usuarios
1. Ve a **Users → Add User**.  
   - Username: `admin_user`  
   - Email, nombre, etc. opcionales.  
   - Habilitado: ✅ ON.  
2. En la pestaña **Credentials**, asigna una contraseña y marca **Temporary = OFF**.
3. Hacer el mismo paso para la creación del user `basic_user`
---

![keycloak_users_configuratio](https://github.com/user-attachments/assets/7baf4751-8e08-4586-8956-efe31823b54d)


## 5️⃣ Crear Roles
1. En el menú de **Roles**, crear roles como `USER`, `ADMIN`.

![keycloak_realm_roles_configuration](https://github.com/user-attachments/assets/72aa97a1-ede8-4cc6-9d4e-c1e0863fc177)
   
2. Asignar los roles a los usuarios creados:
   - `admin_user` → roles: **ADMIN** y **USER**  
   - `basic_user` → roles: **USER**

   ![keycloak_admin_user_configuration](https://github.com/user-attachments/assets/47b6d3d9-a579-4f34-b439-8521b676b7d7)
   ![keycloak_basic_user_configuration](https://github.com/user-attachments/assets/8580e13b-a49e-4122-8047-2ef724bbc201)
---


## 6️⃣ Probar el flujo OAuth2
1. Accede al API Gateway:
   ```
   http://localhost:8080/api/product
   ```
   El Gateway redirigirá automáticamente a Keycloak para autenticación.
2. Ingresa con el usuario `admin_user`(Acceso a todos los endpoints) o `basic_user`.  
3. Keycloak emitirá un **JWT token** y lo reenviará al API Gateway, que a su vez lo propagará a los microservicios gracias a:

```properties
spring.cloud.gateway.server.webflux.default-filters[1]=TokenRelay
```

---

✅ Con esto ya tendrás:  
- **Keycloak → emite tokens JWT**  
- **API Gateway → verifica tokens y los pasa a microservicios**  
- **Microservicios → protegidos con Spring Security + OAuth2 Resource Server**  

