-- =========================================================================
-- FERREYROS SERVTECH 2026 - ESQUEMA CORE OPTIMIZADO PARA SUPABASE
-- Compatible con Spring Boot 3 + Vue.js 3 | Reglas GiST y RLS Activo
-- Rama: feature/ddl-tables-supabase | Integrante: Matias Aponte
-- =========================================================================

-- 1. EXTENSIONES REQUERIDAS
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
CREATE EXTENSION IF NOT EXISTS "btree_gist";

-- -------------------------------------------------------------------------
-- 2. TABLAS BASE Y CATÁLOGOS
-- -------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS sedes (
    id_sede SERIAL PRIMARY KEY,
    codigo_sede VARCHAR(20) UNIQUE NOT NULL,
    nombre VARCHAR(120) NOT NULL,
    tipo VARCHAR(30) NOT NULL CHECK (tipo IN ('SEDE_CENTRAL', 'SUCURSAL', 'OPERACION_MINERA', 'CRC')),
    region VARCHAR(50) NOT NULL,
    direccion VARCHAR(200),
    activo BOOLEAN DEFAULT TRUE,
    creado_en TIMESTAMPTZ DEFAULT NOW()
);

CREATE TABLE IF NOT EXISTS clientes (
    id_cliente SERIAL PRIMARY KEY,
    ruc VARCHAR(11) UNIQUE NOT NULL,
    razon_social VARCHAR(150) NOT NULL,
    contacto_nombre VARCHAR(100),
    contacto_telefono VARCHAR(20),
    contacto_email VARCHAR(100),
    activo BOOLEAN DEFAULT TRUE,
    creado_en TIMESTAMPTZ DEFAULT NOW()
);

CREATE TABLE IF NOT EXISTS usuarios (
    id_usuario UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    username VARCHAR(50) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    rol VARCHAR(30) NOT NULL CHECK (rol IN ('ROLE_ADMIN', 'ROLE_PLANIFICADOR', 'ROLE_SUPERVISOR', 'ROLE_TECNICO')),
    nombres VARCHAR(100) NOT NULL,
    apellidos VARCHAR(100) NOT NULL,
    dni VARCHAR(15) UNIQUE NOT NULL,
    email VARCHAR(100) UNIQUE,
    telefono VARCHAR(20),
    activo BOOLEAN DEFAULT TRUE,
    creado_en TIMESTAMPTZ DEFAULT NOW()
);

CREATE TABLE IF NOT EXISTS maquinarias (
    id_maquinaria SERIAL PRIMARY KEY,
    numero_serie_pin VARCHAR(50) UNIQUE NOT NULL,
    codigo_flota VARCHAR(30) NOT NULL,
    modelo_cat VARCHAR(50) NOT NULL,
    familia VARCHAR(50) NOT NULL CHECK (familia IN ('CAMION_MINERO', 'PALA_HIDRAULICA', 'TRACTOR_ORUGA', 'EXCAVADORA', 'CARGADOR_FRONTAL', 'PERFORADORA')),
    id_cliente INT NOT NULL REFERENCES clientes(id_cliente),
    id_sede_actual INT NOT NULL REFERENCES sedes(id_sede),
    horometro_acumulado NUMERIC(10,2) NOT NULL DEFAULT 0.00 CHECK (horometro_acumulado >= 0),
    contrato_marc VARCHAR(50) DEFAULT 'MARC_ORO',
    sla_garantizado NUMERIC(5,2) DEFAULT 92.00,
    estado_operativo VARCHAR(30) NOT NULL DEFAULT 'OPERATIVO' CHECK (estado_operativo IN ('OPERATIVO', 'EN_MANTENIMIENTO', 'PARADA_CRITICA_DOWN', 'STAND_BY')),
    telemetria_activa BOOLEAN DEFAULT TRUE,
    actualizado_en TIMESTAMPTZ DEFAULT NOW()
);

CREATE TABLE IF NOT EXISTS competencias (
    id_competencia SERIAL PRIMARY KEY,
    codigo_cat VARCHAR(50) UNIQUE NOT NULL,
    nombre VARCHAR(150) NOT NULL,
    categoria VARCHAR(50) NOT NULL CHECK (categoria IN ('MOTORES_DIESEL', 'SISTEMAS_HIDRAULICOS', 'TREN_DE_RODAJE', 'ELECTRICIDAD_ELECTRONICA', 'TRANSMISIONES'))
);

CREATE TABLE IF NOT EXISTS tecnicos (
    id_tecnico SERIAL PRIMARY KEY,
    id_usuario UUID UNIQUE NOT NULL REFERENCES usuarios(id_usuario) ON DELETE CASCADE,
    codigo_empleado VARCHAR(20) UNIQUE NOT NULL,
    nivel_certificacion VARCHAR(30) NOT NULL CHECK (nivel_certificacion IN ('Foundational', 'Level 1', 'Level 2', 'Specialist', 'Master CAT')),
    regimen_guardia VARCHAR(30) NOT NULL DEFAULT '14x7_DIURNO' CHECK (regimen_guardia IN ('14x7_DIURNO', '14x7_NOCTURNO', '5x2_TALLER')),
    horas_acumuladas_mes NUMERIC(5,2) DEFAULT 0.00 CHECK (horas_acumuladas_mes >= 0 AND horas_acumuladas_mes <= 192.00),
    estado_disponibilidad VARCHAR(30) DEFAULT 'DISPONIBLE' CHECK (estado_disponibilidad IN ('DISPONIBLE', 'ASIGNADO', 'EN_DESCANSO', 'LICENCIA')),
    creado_en TIMESTAMPTZ DEFAULT NOW()
);

CREATE TABLE IF NOT EXISTS ordenes_servicio (
    id_orden SERIAL PRIMARY KEY,
    codigo_ot VARCHAR(30) UNIQUE NOT NULL,
    id_maquinaria INT NOT NULL REFERENCES maquinarias(id_maquinaria),
    id_sede INT NOT NULL REFERENCES sedes(id_sede),
    id_creador UUID NOT NULL REFERENCES usuarios(id_usuario),
    tipo_mantenimiento VARCHAR(50) NOT NULL CHECK (tipo_mantenimiento IN ('PM1_250H', 'PM2_500H', 'PM3_1000H', 'PM4_2000H', 'CORRECTIVO', 'OVERHAUL', 'DIAGNOSTICO_FALLA')),
    prioridad VARCHAR(30) NOT NULL CHECK (prioridad IN ('BAJA', 'MEDIA', 'ALTA', 'CRITICA_DOWN_MACHINE')),
    estado VARCHAR(30) NOT NULL DEFAULT 'SOLICITADA' CHECK (estado IN ('SOLICITADA', 'ASIGNADA', 'EN_DIAGNOSTICO', 'EN_REPARACION', 'CONTROL_CALIDAD', 'CERRADA', 'LIQUIDADA')),
    horometro_registro NUMERIC(10,2) NOT NULL CHECK (horometro_registro >= 0),
    falla_reportada TEXT NOT NULL,
    fecha_solicitud TIMESTAMPTZ DEFAULT NOW(),
    fecha_cierre TIMESTAMPTZ
);
