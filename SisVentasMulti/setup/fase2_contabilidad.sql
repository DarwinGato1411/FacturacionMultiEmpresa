-- Fase 2: parametrización de cuentas y libro diario
BEGIN;

CREATE TABLE IF NOT EXISTS public.cu_parametro (
    id_parametro serial PRIMARY KEY,
    par_codigo character varying(40) NOT NULL UNIQUE,
    par_nombre character varying(150) NOT NULL,
    id_sub_cuenta integer REFERENCES public.cu_sub_cuenta(id_sub_cuenta)
);

CREATE TABLE IF NOT EXISTS public.cu_asiento (
    id_asiento serial PRIMARY KEY,
    asi_fecha date,
    asi_numero integer,
    asi_glosa character varying(250),
    asi_origen character varying(20),
    asi_id_documento integer,
    asi_tipo_doc character varying(20),
    asi_estado character varying(20) DEFAULT 'REG'
);

CREATE TABLE IF NOT EXISTS public.cu_asiento_detalle (
    id_detalle serial PRIMARY KEY,
    id_asiento integer NOT NULL REFERENCES public.cu_asiento(id_asiento) ON DELETE CASCADE,
    id_sub_cuenta integer REFERENCES public.cu_sub_cuenta(id_sub_cuenta),
    det_debe numeric(14,4) DEFAULT 0,
    det_haber numeric(14,4) DEFAULT 0,
    det_glosa character varying(250)
);

CREATE INDEX IF NOT EXISTS idx_cu_asiento_origen_doc ON public.cu_asiento (asi_origen, asi_id_documento);
CREATE INDEX IF NOT EXISTS idx_cu_asiento_fecha ON public.cu_asiento (asi_fecha);

INSERT INTO public.cu_parametro (par_codigo, par_nombre, id_sub_cuenta)
SELECT v.codigo, v.nombre, s.id_sub_cuenta
FROM (VALUES
    ('CAJA', 'Caja / bancos (cobro de contado)', '1.01.01.001'),
    ('CXC', 'Cuentas por cobrar clientes', '1.01.02.001'),
    ('IVA_CREDITO', 'Credito tributario IVA (compras)', '1.01.05.001'),
    ('INVENTARIO', 'Inventario de mercaderias', '1.01.04.001'),
    ('PROVEEDORES', 'Cuentas por pagar proveedores', '2.01.01.001'),
    ('IVA_VENTAS', 'IVA por pagar (ventas)', '2.01.04.001'),
    ('VENTAS', 'Ventas de bienes', '4.01.01.001'),
    ('SERVICIOS', 'Ventas de servicios', '4.01.02.001'),
    ('CMV', 'Costo de mercaderias vendidas', '5.01.01.001'),
    ('COMPRAS', 'Compras / inventario (ingreso mercaderia)', '1.01.04.001')
) AS v(codigo, nombre, cuenta)
JOIN public.cu_sub_cuenta s ON s.subc_numero = v.cuenta
WHERE NOT EXISTS (SELECT 1 FROM public.cu_parametro p WHERE p.par_codigo = v.codigo);

COMMIT;
