-- Parametros de ventas por tarifa IVA (plantilla NIIF, cod_tipoambiente NULL)
BEGIN;

INSERT INTO public.cu_parametro (par_codigo, par_nombre, id_sub_cuenta, cod_tipoambiente)
SELECT v.codigo, v.nombre, s.id_sub_cuenta, NULL
FROM (VALUES
    ('VENTAS_5', 'Ventas gravadas tarifa 5%', '4.01.01.002'),
    ('VENTAS_0', 'Ventas tarifa 0%', '4.01.01.003')
) AS v(codigo, nombre, cuenta)
JOIN public.cu_sub_cuenta s ON s.subc_numero = v.cuenta AND s.cod_tipoambiente IS NULL
WHERE NOT EXISTS (
    SELECT 1 FROM public.cu_parametro p
    WHERE p.par_codigo = v.codigo AND p.cod_tipoambiente IS NULL
);

COMMIT;
