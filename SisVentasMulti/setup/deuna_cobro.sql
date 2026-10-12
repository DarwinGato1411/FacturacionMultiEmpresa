-- Parametros de cobro Deuna (Payvalida) por empresa
BEGIN;

ALTER TABLE public.tipoambiente ADD COLUMN IF NOT EXISTS am_deuna_merchant character varying(80);
ALTER TABLE public.tipoambiente ADD COLUMN IF NOT EXISTS am_deuna_hash character varying(250);
ALTER TABLE public.tipoambiente ADD COLUMN IF NOT EXISTS am_deuna_caja character varying(40);
ALTER TABLE public.tipoambiente ADD COLUMN IF NOT EXISTS am_deuna_ambiente character varying(20) DEFAULT 'PRUEBAS';
ALTER TABLE public.tipoambiente ADD COLUMN IF NOT EXISTS am_deuna_email character varying(120);

COMMIT;
