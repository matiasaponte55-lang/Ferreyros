-- =========================================================================
-- REGLA DE EXCLUSIÓN TEMPORAL CON ÍNDICE GiST (ANTI-SOLAPAMIENTO RF-04)
-- Cumplimiento de especificación IEEE 830: Exclusión matemática tstzrange &&
-- Rama: feature/gist-exclusion-constraint | Integrante: Matias Aponte
-- =========================================================================

-- Requerimiento: La extensión btree_gist debe estar habilitada
CREATE EXTENSION IF NOT EXISTS "btree_gist";

-- Creación de la tabla de asignaciones horarias con restricción GiST
CREATE TABLE IF NOT EXISTS asignaciones (
    id_asignacion SERIAL PRIMARY KEY,
    id_orden INT NOT NULL REFERENCES ordenes_servicio(id_orden) ON DELETE CASCADE,
    id_tecnico INT NOT NULL REFERENCES tecnicos(id_tecnico) ON DELETE RESTRICT,
    periodo_asignacion TSTZRANGE NOT NULL,
    horas_programadas NUMERIC(4,2) GENERATED ALWAYS AS (
        EXTRACT(EPOCH FROM (UPPER(periodo_asignacion) - LOWER(periodo_asignacion))) / 3600.0
    ) STORED,
    rol_cuadrilla VARCHAR(30) DEFAULT 'LIDER_TECNICO' CHECK (rol_cuadrilla IN ('LIDER_TECNICO', 'APOYO', 'ESPECIALISTA')),
    creado_en TIMESTAMPTZ DEFAULT NOW(),

    -- REGLA MATEMÁTICA ANTI-SOLAPAMIENTO (RF-04 / Código PostgreSQL 23P01)
    -- Si el mismo técnico es asignado en un intervalo que intersecte (&&), la BD aborta la transacción
    CONSTRAINT exclusion_solapamiento_tecnico 
        EXCLUDE USING gist (
            id_tecnico WITH =,
            periodo_asignacion WITH &&
        )
);

-- Índices B-Tree complementarios para optimizar consultas del Dispatcher
CREATE INDEX IF NOT EXISTS idx_asignaciones_orden ON asignaciones(id_orden);
CREATE INDEX IF NOT EXISTS idx_asignaciones_tecnico ON asignaciones(id_tecnico);
