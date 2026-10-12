-- Cierre de ejercicio y parametros de resultados
BEGIN;

CREATE TABLE IF NOT EXISTS public.cu_ejercicio (
    id_ejercicio serial PRIMARY KEY,
    cod_tipoambiente integer NOT NULL REFERENCES public.tipoambiente(cod_tipoambiente),
    eje_anio integer NOT NULL,
    eje_estado character varying(20) NOT NULL DEFAULT 'ABIERTO',
    eje_fecha_cierre date,
    id_asiento integer,
    UNIQUE (cod_tipoambiente, eje_anio)
);

CREATE INDEX IF NOT EXISTS idx_cu_ejercicio_ambiente
    ON public.cu_ejercicio (cod_tipoambiente, eje_anio);

INSERT INTO public.cu_parametro (par_codigo, par_nombre, id_sub_cuenta, cod_tipoambiente)
SELECT v.codigo, v.nombre, s.id_sub_cuenta, NULL
FROM (VALUES
    ('RESULTADOS', 'Resultados acumulados (utilidad del ejercicio)', '3.03.01.001'),
    ('PERDIDAS_ACUM', 'Perdidas acumuladas', '3.03.01.002')
) AS v(codigo, nombre, cuenta)
JOIN public.cu_sub_cuenta s ON s.subc_numero = v.cuenta AND s.cod_tipoambiente IS NULL
WHERE NOT EXISTS (
    SELECT 1 FROM public.cu_parametro p
    WHERE p.par_codigo = v.codigo AND p.cod_tipoambiente IS NULL
);

COMMIT;
