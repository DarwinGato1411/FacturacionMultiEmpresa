-- Estructura para descarga automática de comprobantes recibidos (SRI en línea).
-- PostgreSQL. También se aplica al abrir la pantalla (CREATE IF NOT EXISTS).

DO $$
BEGIN
    ALTER TABLE tipoambiente ADD COLUMN am_clave_sri_linea character varying(255);
EXCEPTION
    WHEN duplicate_column THEN NULL;
END $$;

CREATE TABLE IF NOT EXISTS sri_tipo_documento (
    std_codigo character varying(2) NOT NULL,
    std_nombre character varying(120) NOT NULL,
    std_nombre_portal character varying(200),
    std_codigo_portal integer,
    std_orden integer,
    std_activo boolean DEFAULT true,
    CONSTRAINT sri_tipo_documento_pkey PRIMARY KEY (std_codigo)
);

CREATE TABLE IF NOT EXISTS sri_descarga_lote (
    sdl_id serial NOT NULL,
    std_codigo character varying(2) NOT NULL,
    sdl_fecha_inicio date,
    sdl_fecha_fin date,
    sdl_fecha_ejecucion timestamp without time zone,
    sdl_origen character varying(20),
    sdl_estado character varying(20),
    sdl_mensaje text,
    sdl_ruta_archivo text,
    sdl_cantidad integer DEFAULT 0,
    sdl_nuevos integer DEFAULT 0,
    sdl_duplicados integer DEFAULT 0,
    sdl_usuario character varying(80),
    cod_tipoambiente integer,
    CONSTRAINT sri_descarga_lote_pkey PRIMARY KEY (sdl_id),
    CONSTRAINT sri_descarga_lote_tipo_fk FOREIGN KEY (std_codigo)
        REFERENCES sri_tipo_documento (std_codigo),
    CONSTRAINT sri_descarga_lote_ambiente_fk FOREIGN KEY (cod_tipoambiente)
        REFERENCES tipoambiente (cod_tipoambiente)
);

CREATE TABLE IF NOT EXISTS sri_comprobante_recibido (
    scr_id serial NOT NULL,
    std_codigo character varying(2) NOT NULL,
    sdl_id integer,
    scr_tipo_comprobante character varying(120),
    scr_serie character varying(40),
    scr_ruc_emisor character varying(20),
    scr_razon_social character varying(300),
    scr_fecha_emision date,
    scr_fecha_autorizacion date,
    scr_tipo_emision character varying(40),
    scr_identificacion_receptor character varying(20),
    scr_clave_acceso character varying(60),
    scr_autorizacion character varying(60),
    scr_total character varying(30),
    scr_clasificacion character varying(80),
    scr_estado_clasificacion character varying(30) DEFAULT 'PENDIENTE',
    scr_xml_descargado boolean DEFAULT false,
    scr_ruta_xml text,
    scr_fecha_registro timestamp without time zone,
    cod_tipoambiente integer,
    CONSTRAINT sri_comprobante_recibido_pkey PRIMARY KEY (scr_id),
    CONSTRAINT sri_comprobante_recibido_tipo_fk FOREIGN KEY (std_codigo)
        REFERENCES sri_tipo_documento (std_codigo),
    CONSTRAINT sri_comprobante_recibido_ambiente_fk FOREIGN KEY (cod_tipoambiente)
        REFERENCES tipoambiente (cod_tipoambiente)
);

CREATE UNIQUE INDEX IF NOT EXISTS sri_comprobante_recibido_amb_clave_uk
    ON sri_comprobante_recibido (cod_tipoambiente, scr_clave_acceso)
    WHERE scr_clave_acceso IS NOT NULL AND scr_clave_acceso <> '';

CREATE INDEX IF NOT EXISTS sri_comprobante_recibido_ambiente_idx
    ON sri_comprobante_recibido (cod_tipoambiente, std_codigo, scr_fecha_emision);

INSERT INTO sri_tipo_documento (std_codigo, std_nombre, std_nombre_portal, std_codigo_portal, std_orden, std_activo)
SELECT '01', 'Facturas', 'Factura', 1, 1, true
WHERE NOT EXISTS (SELECT 1 FROM sri_tipo_documento WHERE std_codigo = '01');

INSERT INTO sri_tipo_documento (std_codigo, std_nombre, std_nombre_portal, std_codigo_portal, std_orden, std_activo)
SELECT '03', 'Liquidaciones de compra', 'Liquidación de compra de bienes y prestación de servicios', 2, 2, true
WHERE NOT EXISTS (SELECT 1 FROM sri_tipo_documento WHERE std_codigo = '03');

INSERT INTO sri_tipo_documento (std_codigo, std_nombre, std_nombre_portal, std_codigo_portal, std_orden, std_activo)
SELECT '04', 'Notas de crédito', 'Notas de crédito', 3, 3, true
WHERE NOT EXISTS (SELECT 1 FROM sri_tipo_documento WHERE std_codigo = '04');

INSERT INTO sri_tipo_documento (std_codigo, std_nombre, std_nombre_portal, std_codigo_portal, std_orden, std_activo)
SELECT '05', 'Notas de débito', 'Notas de débito', 4, 4, true
WHERE NOT EXISTS (SELECT 1 FROM sri_tipo_documento WHERE std_codigo = '05');

INSERT INTO sri_tipo_documento (std_codigo, std_nombre, std_nombre_portal, std_codigo_portal, std_orden, std_activo)
SELECT '06', 'Guías de remisión', 'Guías de remisión', 6, 5, true
WHERE NOT EXISTS (SELECT 1 FROM sri_tipo_documento WHERE std_codigo = '06');

INSERT INTO sri_tipo_documento (std_codigo, std_nombre, std_nombre_portal, std_codigo_portal, std_orden, std_activo)
SELECT '07', 'Comprobantes de retención', 'Comprobante de retención', 7, 6, true
WHERE NOT EXISTS (SELECT 1 FROM sri_tipo_documento WHERE std_codigo = '07');
