-- Plan NIIF por empresa (tipoambiente). Las filas actuales quedan como plantilla (NULL).
BEGIN;

ALTER TABLE public.cu_clase
    ADD COLUMN IF NOT EXISTS cod_tipoambiente integer
    REFERENCES public.tipoambiente (cod_tipoambiente);
ALTER TABLE public.cu_grupo
    ADD COLUMN IF NOT EXISTS cod_tipoambiente integer
    REFERENCES public.tipoambiente (cod_tipoambiente);
ALTER TABLE public.cu_cuenta
    ADD COLUMN IF NOT EXISTS cod_tipoambiente integer
    REFERENCES public.tipoambiente (cod_tipoambiente);
ALTER TABLE public.cu_sub_cuenta
    ADD COLUMN IF NOT EXISTS cod_tipoambiente integer
    REFERENCES public.tipoambiente (cod_tipoambiente);

CREATE INDEX IF NOT EXISTS idx_cu_clase_ambiente ON public.cu_clase (cod_tipoambiente);
CREATE INDEX IF NOT EXISTS idx_cu_grupo_ambiente ON public.cu_grupo (cod_tipoambiente);
CREATE INDEX IF NOT EXISTS idx_cu_cuenta_ambiente ON public.cu_cuenta (cod_tipoambiente);
CREATE INDEX IF NOT EXISTS idx_cu_sub_cuenta_ambiente ON public.cu_sub_cuenta (cod_tipoambiente);
CREATE INDEX IF NOT EXISTS idx_cu_sub_cuenta_num_amb ON public.cu_sub_cuenta (subc_numero, cod_tipoambiente);

CREATE UNIQUE INDEX IF NOT EXISTS uq_cu_clase_num_amb
    ON public.cu_clase (clas_numero, COALESCE(cod_tipoambiente, 0));
CREATE UNIQUE INDEX IF NOT EXISTS uq_cu_grupo_num_amb
    ON public.cu_grupo (grup_numero, COALESCE(cod_tipoambiente, 0));
CREATE UNIQUE INDEX IF NOT EXISTS uq_cu_cuenta_num_amb
    ON public.cu_cuenta (grup_numero, COALESCE(cod_tipoambiente, 0));
CREATE UNIQUE INDEX IF NOT EXISTS uq_cu_sub_num_amb
    ON public.cu_sub_cuenta (subc_numero, COALESCE(cod_tipoambiente, 0));

COMMIT;
