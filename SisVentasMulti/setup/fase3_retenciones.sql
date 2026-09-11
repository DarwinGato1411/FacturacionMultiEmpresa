-- Cuentas de retenciones en la plantilla NIIF (cod_tipoambiente NULL)
BEGIN;

INSERT INTO public.cu_parametro (par_codigo, par_nombre, id_sub_cuenta, cod_tipoambiente)
SELECT v.codigo, v.nombre, s.id_sub_cuenta, NULL
FROM (VALUES
    ('RET_IVA_PAGAR', 'Retenciones de IVA por pagar (compras)', '2.01.04.002'),
    ('RET_RENTA_PAGAR', 'Retenciones de renta por pagar (compras)', '2.01.04.003'),
    ('RET_IVA_COBRAR', 'Retenciones de IVA a favor (ventas)', '1.01.05.002'),
    ('RET_RENTA_COBRAR', 'Retenciones de renta a favor (ventas)', '1.01.05.003')
) AS v(codigo, nombre, cuenta)
JOIN public.cu_sub_cuenta s ON s.subc_numero = v.cuenta AND s.cod_tipoambiente IS NULL
WHERE NOT EXISTS (
    SELECT 1 FROM public.cu_parametro p
    WHERE p.par_codigo = v.codigo AND p.cod_tipoambiente IS NULL
);

COMMIT;
