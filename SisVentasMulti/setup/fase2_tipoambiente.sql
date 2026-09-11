-- Aislar contabilidad por empresa (tipoambiente)
BEGIN;

ALTER TABLE public.cu_parametro
    ADD COLUMN IF NOT EXISTS cod_tipoambiente integer
    REFERENCES public.tipoambiente (cod_tipoambiente);

ALTER TABLE public.cu_asiento
    ADD COLUMN IF NOT EXISTS cod_tipoambiente integer
    REFERENCES public.tipoambiente (cod_tipoambiente);

ALTER TABLE public.cu_parametro
    DROP CONSTRAINT IF EXISTS cu_parametro_par_codigo_key;

CREATE UNIQUE INDEX IF NOT EXISTS uq_cu_parametro_codigo_amb
    ON public.cu_parametro (par_codigo, COALESCE(cod_tipoambiente, 0));

CREATE INDEX IF NOT EXISTS idx_cu_asiento_ambiente_fecha
    ON public.cu_asiento (cod_tipoambiente, asi_fecha);

UPDATE public.cu_asiento a
SET cod_tipoambiente = f.cod_tipoambiente
FROM public.factura f
WHERE a.asi_origen = 'FACT'
  AND a.asi_id_documento = f.id_factura
  AND a.cod_tipoambiente IS NULL;

UPDATE public.cu_asiento a
SET cod_tipoambiente = c.cod_tipoambiente
FROM public.cabecera_compra c
WHERE a.asi_origen = 'COMP'
  AND a.asi_id_documento = c.id_cabecera
  AND a.cod_tipoambiente IS NULL;

COMMIT;
