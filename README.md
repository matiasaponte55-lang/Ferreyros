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

## 🚀 Flujo para Realizar los Commits

```bash
# 1. Crear la rama correspondiente
git checkout -b feature/nombre-de-la-rama

# 2. Agregar los archivos en la carpeta que te toca
git add .

# 3. Hacer el commit con mensaje descriptivo
git commit -m "feat(modulo): descripcion clara del cambio"

# 4. Subir la rama a GitHub
git push -u origin feature/nombre-de-la-rama
```
