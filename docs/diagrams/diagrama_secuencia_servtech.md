# Diagramas de Secuencia y Arquitectura Operativa - ServTech 2026
**Autor:** Draacoo12 - Juan Zamata (QA & Arquitectura de Software)

---

## 1. Diagrama de Secuencia: Validación de Horómetro Ascendente (RF-02 / RF-03)

```mermaid
sequenceDiagram
    autonumber
    actor Supervisor as Supervisor / Planificador
    participant View as NuevaOrdenView (Vue 3)
    participant Ctrl as OrdenServicioController
    participant Svc as OrdenServicioService
    participant Repo as MaquinariaRepository
    participant DB as PostgreSQL (Supabase)

    Supervisor->>View: Ingresa PIN y Horómetro de Entrada (ej: 1,450 hrs)
    View->>View: Bloqueo de caracteres no numéricos (@keydown)
    View->>Ctrl: POST /api/v1/ordenes-servicio (NuevaOrdenRequest)
    Ctrl->>Svc: crearOrden(request)
    Svc->>Repo: findById(idMaquinaria)
    Repo->>DB: SELECT * FROM maquinarias WHERE id = ?
    DB-->>Repo: Maquinaria (Horómetro acumulado histórico: 1,400 hrs)
    Repo-->>Svc: Objeto Maquinaria

    alt Horómetro nuevo < Horómetro histórico
        Svc-->>Ctrl: Lanza HorometroInvalidoException (RF-03)
        Ctrl-->>View: HTTP 400 Bad Request (JSON de Error Estructurado)
        View-->>Supervisor: Muestra alerta roja en pantalla
    else Horómetro nuevo >= Horómetro histórico
        Svc->>Repo: save(Maquinaria actualizada con 1,450 hrs)
        Svc->>DB: INSERT INTO ordenes_servicio (...)
        Svc->>DB: INSERT INTO auditoria_estados_ot (RF-14)
        DB-->>Svc: Orden guardada exitosamente
        Svc-->>Ctrl: OrdenServicio creada
        Ctrl-->>View: HTTP 201 Created
        View-->>Supervisor: Mensaje de confirmación y redirección
    end
```

---

## 2. Diagrama de Secuencia: Asignación de Franjas Horarias con Control GiST (CU-03 / RF-04)

```mermaid
sequenceDiagram
    autonumber
    actor JefeTaller as Supervisor de Despacho
    participant Disp as DispatcherView (Vue 3)
    participant Ctrl as AsignacionController
    participant Svc as AsignacionService
    participant DB as PostgreSQL (Índice GiST btree_gist)

    JefeTaller->>Disp: Selecciona Técnico y Franja (09:00 - 11:00)
    Disp->>Ctrl: POST /api/v1/asignaciones (idOrden, idTecnico, horaInicio, horaFin)
    Ctrl->>Svc: asignarCuadrilla(request)
    Svc->>DB: SELECT * FROM asignaciones WHERE id_tecnico = ? AND (rango && tstzrange)
    
    alt Existe solapamiento temporal
        DB-->>Svc: Retorna asignación conflictiva previa
        Svc-->>Ctrl: Lanza ConflictoHorarioException (Código 23P01)
        Ctrl-->>Disp: HTTP 409 Conflict
        Disp-->>JefeTaller: Muestra banner de advertencia GiST con código OT conflictiva
    else Franja Horaria Disponible
        Svc->>DB: INSERT INTO asignaciones (id_orden, id_tecnico, tstzrange, ...)
        DB-->>Svc: Confirmación de inserción
        Svc-->>Ctrl: Asignacion creada
        Ctrl-->>Disp: HTTP 201 Created
        Disp-->>JefeTaller: Actualiza cuadrícula de despacho con recuadro amarillo
    end
```

---

## 3. Diagrama de Secuencia: Checklist Digital ATS y Sellado Criptográfico SHA-256 (RF-08 / RF-09)

```mermaid
sequenceDiagram
    autonumber
    actor Tecnico as Técnico Mecánico (Tablet)
    participant Insp as InspeccionView (Vue 3 Canvas)
    participant Ctrl as InspeccionController
    participant Svc as InspeccionService
    participant DB as PostgreSQL (Supabase)

    Tecnico->>Insp: Marca verificación LOTO y PETAR aprobados
    Tecnico->>Insp: Evalúa subsistemas CAT (Motor, Hidráulico, Transmisión, Cabina)
    Tecnico->>Insp: Dibuja rúbrica táctil en lienzo HTML5 Canvas
    Tecnico->>Insp: Presiona "Sellar y Registrar ATS Digital"
    Insp->>Insp: Genera DataURL Base64 de la rúbrica
    Insp->>Insp: Calcula Hash SHA-256 (Web Crypto API)
    Insp->>Ctrl: POST /api/v1/inspecciones/{id}/firmar (firmaBase64, hashSha256)
    Ctrl->>Svc: firmarInspeccion(id, request)
    Svc->>DB: INSERT INTO inspecciones_ats (hash_sha256, firma_digital_base64, loto, petar)
    DB-->>Svc: Expediente técnico sellado
    Svc-->>Ctrl: Inspección confirmada
    Ctrl-->>Insp: HTTP 200 OK
    Insp-->>Tecnico: Muestra candado verde con código criptográfico SHA-256
```
