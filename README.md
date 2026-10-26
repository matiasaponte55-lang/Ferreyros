# FERREYROS SERVTECH 2026 - REPOSITORIO BASE

Este repositorio contiene la estructura completa del proyecto para que cada integrante del equipo realice los commits en sus respectivas ramas y módulos correspondientes.

---

## Estructura del Proyecto

```
Ferreyros-Proyecto/
│
├── database/                                 <-- Base de datos (Matias Aponte)
│   ├── ddl/                                  <-- Scripts DDL de tablas y catálogos
│   ├── constraints/                          <-- Restricciones GiST y exclusion tstzrange
│   └── triggers/                             <-- Triggers de horómetro y auditoría
│
├── docs/                                     <-- Documentación y QA (Juan Zamata)
│   ├── openapi/                              <-- Especificaciones OpenAPI 3.0 / Swagger
│   └── diagrams/                             <-- Diagramas de secuencia y arquitectura
│
├── backend/                                  <-- Backend Spring Boot 3 (Ivan Mamani / Matias Aponte / Juan Zamata)
│   └── src/
│       ├── main/
│       │   ├── java/com/ferreyros/servtech/
│       │   │   ├── config/                   <-- Configuración CORS y seguridad
│       │   │   ├── controller/               <-- Controladores REST
│       │   │   ├── dto/                      <-- Data Transfer Objects (Login, Auth)
│       │   │   ├── exception/                <-- Manejadores globales de error (401, 404, 409)
│       │   │   ├── model/                    <-- Entidades JPA
│       │   │   ├── repository/               <-- Repositorios Spring Data JPA
│       │   │   ├── security/                 <-- Clases de seguridad y tokens JWT
│       │   │   ├── service/                  <-- Lógica de negocio (OTs, Dispatcher, Auth)
│       │   │   └── util/                     <-- Utilitarios (JwtUtil)
│       │   └── resources/                    <-- application.properties
│       └── test/java/com/ferreyros/servtech/
│           ├── controller/                   <-- Pruebas de integración REST (MockMvc)
│           ├── service/                      <-- Pruebas unitarias (Mockito)
│           └── validation/                   <-- Pruebas de validación GiST y triggers
│
└── frontend/                                 <-- Frontend SPA Vue 3 (Andy Manchego)
    ├── public/                               <-- Recursos estáticos e iconos
    └── src/
        ├── api/                              <-- Cliente Axios
        ├── assets/                           <-- Estilos e imágenes
        ├── components/                       <-- Componentes reutilizables
        ├── layouts/                          <-- Plantilla unificada (AppLayout.vue)
        ├── router/                           <-- Enrutador Vue Router y guards
        ├── stores/                           <-- Estado global Pinia (auth.js)
        └── views/                            <-- Vistas de la aplicación
```

---

## Ramas

### 1. Ivan Mamani (Backend) — 4 Ramas
| Rama Git | Módulo / Carpeta Destino | Descripción |
| :--- | :--- | :--- |
| `feature/spring-security-jwt` | `backend/.../security/`, `config/`, `util/`, `dto/` | Autenticación BCrypt + JWT y endpoints de login |
| `feature/rest-controllers-ot` | `backend/.../controller/`, `service/` | Endpoints REST para OTs, Maquinarias, Dashboard |
| `feature/dispatcher-assignment-logic` | `backend/.../service/`, `controller/` | Lógica de despacho y control de excepciones (HTTP 409) |
| `release/v1.0-entrega` | `backend/` completo | Entrega oficial v1.0 del servicio Backend |

---

### 2. Matias Aponte (Base de Datos & JPA) — 4 Ramas
| Rama Git | Módulo / Carpeta Destino | Descripción |
| :--- | :--- | :--- |
| `feature/ddl-tables-supabase` | `database/ddl/` | DDL de tablas maestras y extensiones en Supabase |
| `feature/gist-exclusion-constraint` | `database/constraints/` | Restricción matemática GiST anti-solapamiento |
| `feature/trigger-horometro-ascendente` | `database/triggers/` | Triggers de horómetro ascendente y auditoría forense |
| `feature/jpa-entities-mapping` | `backend/.../model/`, `repository/` | Mapeo de entidades JPA y repositorios Spring Data |

---

### 3. Andy Manchego (Frontend) — 5 Ramas
| Rama Git | Módulo / Carpeta Destino | Descripción |
| :--- | :--- | :--- |
| `feature/vue-vite-setup` | `frontend/` (configuración base) | Setup de Vite, Tailwind CSS y AppLayout.vue |
| `feature/pinia-auth-store` | `frontend/src/stores/`, `router/`, `views/LoginView.vue` | Store de Pinia, navegación protegida y Login |
| `feature/view-dispatcher-grid` | `frontend/src/views/DispatcherView.vue` | Vista de matriz de despacho y alerta GiST |
| `feature/view-kanban-board` | `frontend/src/views/KanbanView.vue`, `OrdenesView.vue` | Tablero Kanban y modal de retrabajo mandatorio |
| `feature/view-canvas-firma-ats` | `frontend/src/views/InspeccionView.vue`, `LiquidacionView.vue` | Tablet ATS, firma digital SHA-256 y Acta A4 |

---

### 4. Juan Zamata (QA & Docs) — 4 Ramas
| Rama Git | Módulo / Carpeta Destino | Descripción |
| :--- | :--- | :--- |
| `test/unit-services-mockito` | `backend/src/test/.../service/` | Pruebas unitarias JUnit 5 + Mockito |
| `test/integration-rest-endpoints` | `backend/src/test/.../controller/` | Pruebas de integración con MockMvc |
| `test/validation-gist-triggers` | `backend/src/test/.../validation/` | Validación de reglas GiST y triggers |
| `docs/openapi-swagger-specs` | `docs/openapi/` | Especificación técnica OpenAPI 3.0 YAML |

---

## Cómo Ejecutar el Proyecto (Paso a Paso)

### Requisitos Previos
* **Java JDK 17** o superior instalado.
* **Node.js 18** o superior (con npm) instalado.
* *Nota: No es necesario instalar PostgreSQL ni Maven de forma local. El backend ya incluye Maven Wrapper (`mvnw`) y se conecta automáticamente a la base de datos PostgreSQL alojada en Supabase Cloud.*

---

### Paso 1: Iniciar el Backend (Spring Boot 3)
Abre una terminal en la raíz del proyecto y ejecuta:
```bash
cd backend
./mvnw spring-boot:run
```
*(En Windows PowerShell usa `.\mvnw.cmd spring-boot:run`)*.

El backend iniciará en el puerto **`http://localhost:8080`**.

---

### Paso 2: Iniciar el Frontend (Vue 3 / Vite)
Abre una segunda terminal y ejecuta:
```bash
cd frontend
npm install
npm run dev
```

El frontend iniciará en **`http://localhost:5173`**.

---

### Cuentas Corporativas para Pruebas (RBAC)

Accede a [http://localhost:5173](http://localhost:5173) con cualquiera de estos usuarios:

| Rol | Correo Corporativo | Contraseña |
| :--- | :--- | :--- |
| **Supervisor de Despacho** | `supervisor@ferreyros.com.pe` | `Ferreyros2026!` |
| **Planificador MARC** | `planificador@ferreyros.com.pe` | `Ferreyros2026!` |
| **Técnico Mecánico** | `tecnico@ferreyros.com.pe` | `Ferreyros2026!` |
| **Administrador** | `admin@ferreyros.com.pe` | `Ferreyros2026!` |
