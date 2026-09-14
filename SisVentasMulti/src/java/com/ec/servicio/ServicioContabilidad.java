package com.ec.servicio;

import com.ec.entidad.CabeceraCompra;
import com.ec.entidad.DetalleRetencionCompra;
import com.ec.entidad.Factura;
import com.ec.entidad.FormaPago;
import com.ec.entidad.NotaCreditoDebito;
import com.ec.entidad.RetencionCompra;
import com.ec.entidad.RetencionVenta;
import com.ec.entidad.Tipoambiente;
import com.ec.entidad.partida.CuAsiento;
import com.ec.entidad.partida.CuAsientoDetalle;
import com.ec.entidad.partida.CuParametro;
import com.ec.entidad.partida.CuSubCuenta;
import com.ec.untilitario.ModeloLibroMayor;
import com.ec.untilitario.ModeloLibroMayorMov;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;

public class ServicioContabilidad {

    private EntityManager em;

    public List<CuParametro> findAllParametros(Tipoambiente ambiente) {
        List<CuParametro> lista = new ArrayList<CuParametro>();
        if (ambiente == null) {
            return lista;
        }
        try {
            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
            clonarPlantillaSiFalta(em, ambiente);
            Query query = em.createNamedQuery("CuParametro.findByAmbiente");
            query.setParameter("ambiente", ambiente);
            lista = query.getResultList();
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Error parametros " + e.getMessage());
            if (em != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
        } finally {
            if (em != null) {
                em.close();
            }
        }
        return lista;
    }

    public void modificarParametro(CuParametro valor, Tipoambiente ambiente) {
        try {
            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
            valor.setCodTipoambiente(ambiente);
            if (valor.getCodigoCuenta() != null && valor.getCodigoCuenta().trim().length() > 0) {
                Query q = em.createQuery("SELECT c FROM CuSubCuenta c WHERE c.subcNumero = :n AND c.codTipoambiente = :ambiente");
                q.setParameter("n", valor.getCodigoCuenta().trim());
                q.setParameter("ambiente", ambiente);
                List list = q.getResultList();
                if (list.isEmpty()) {
                    throw new RuntimeException("No existe la cuenta " + valor.getCodigoCuenta());
                }
                valor.setIdSubCuenta((CuSubCuenta) list.get(0));
            }
            em.merge(valor);
            em.getTransaction().commit();
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<CuAsiento> findAsientos(Date inicio, Date fin, Tipoambiente ambiente) {
        List<CuAsiento> lista = new ArrayList<CuAsiento>();
        if (ambiente == null) {
            return lista;
        }
        try {
            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
            Query query = em.createNamedQuery("CuAsiento.findBetweenFecha");
            query.setParameter("ambiente", ambiente);
            query.setParameter("inicio", inicio);
            query.setParameter("fin", fin);
            lista = query.getResultList();
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Error asientos " + e.getMessage());
        } finally {
            if (em != null) {
                em.close();
            }
        }
        return lista;
    }

    public List<ModeloLibroMayor> findMayor(Date inicio, Date fin, Tipoambiente ambiente, String filtroCuenta) {
        List<ModeloLibroMayor> resultado = new ArrayList<ModeloLibroMayor>();
        if (ambiente == null || inicio == null || fin == null) {
            return resultado;
        }
        String filtro = filtroCuenta != null ? filtroCuenta.trim() : "";
        EntityManager local = null;
        try {
            local = HelperPersistencia.getEMF();
            local.getTransaction().begin();
            Query qApertura = local.createQuery(
                    "SELECT s.subcNumero, s.subcNombre, SUM(d.detDebe), SUM(d.detHaber) "
                    + "FROM CuAsientoDetalle d JOIN d.idAsiento a JOIN d.idSubCuenta s "
                    + "WHERE a.codTipoambiente = :ambiente AND a.asiFecha < :inicio "
                    + "GROUP BY s.subcNumero, s.subcNombre");
            qApertura.setParameter("ambiente", ambiente);
            qApertura.setParameter("inicio", inicio);
            java.util.Map<String, Object[]> mapa = new java.util.LinkedHashMap<String, Object[]>();
            List aper = qApertura.getResultList();
            for (int i = 0; i < aper.size(); i++) {
                Object[] row = (Object[]) aper.get(i);
                String codigo = (String) row[0];
                if (!pasaFiltro(codigo, filtro)) {
                    continue;
                }
                mapa.put(codigo, new Object[]{row[1], nvl(toBig(row[2])), nvl(toBig(row[3])),
                    BigDecimal.ZERO, BigDecimal.ZERO, new ArrayList<ModeloLibroMayorMov>()});
            }
            Query qMov = local.createQuery(
                    "SELECT d FROM CuAsientoDetalle d JOIN FETCH d.idAsiento a JOIN FETCH d.idSubCuenta s "
                    + "WHERE a.codTipoambiente = :ambiente AND a.asiFecha BETWEEN :inicio AND :fin "
                    + "ORDER BY s.subcNumero, a.asiFecha, a.asiNumero, d.idDetalle");
            qMov.setParameter("ambiente", ambiente);
            qMov.setParameter("inicio", inicio);
            qMov.setParameter("fin", fin);
            List movs = qMov.getResultList();
            for (int i = 0; i < movs.size(); i++) {
                CuAsientoDetalle d = (CuAsientoDetalle) movs.get(i);
                if (d.getIdSubCuenta() == null || d.getIdAsiento() == null) {
                    continue;
                }
                String codigo = d.getIdSubCuenta().getSubcNumero();
                if (!pasaFiltro(codigo, filtro)) {
                    continue;
                }
                Object[] acc = mapa.get(codigo);
                if (acc == null) {
                    acc = new Object[]{d.getIdSubCuenta().getSubcNombre(), BigDecimal.ZERO, BigDecimal.ZERO,
                        BigDecimal.ZERO, BigDecimal.ZERO, new ArrayList<ModeloLibroMayorMov>()};
                    mapa.put(codigo, acc);
                }
                BigDecimal debe = nvl(d.getDetDebe());
                BigDecimal haber = nvl(d.getDetHaber());
                acc[3] = ((BigDecimal) acc[3]).add(debe);
                acc[4] = ((BigDecimal) acc[4]).add(haber);
                ModeloLibroMayorMov linea = new ModeloLibroMayorMov();
                linea.setFecha(d.getIdAsiento().getAsiFecha());
                linea.setNumero(d.getIdAsiento().getAsiNumero());
                linea.setOrigen(d.getIdAsiento().getAsiOrigen());
                linea.setGlosa(d.getDetGlosa() != null ? d.getDetGlosa() : d.getIdAsiento().getAsiGlosa());
                linea.setDebe(debe);
                linea.setHaber(haber);
                ((List) acc[5]).add(linea);
            }
            for (java.util.Map.Entry<String, Object[]> e : mapa.entrySet()) {
                String codigo = e.getKey();
                Object[] acc = e.getValue();
                BigDecimal debeAnt = (BigDecimal) acc[1];
                BigDecimal haberAnt = (BigDecimal) acc[2];
                BigDecimal debePer = (BigDecimal) acc[3];
                BigDecimal haberPer = (BigDecimal) acc[4];
                BigDecimal saldoIni = saldoNaturaleza(codigo, debeAnt, haberAnt);
                BigDecimal saldoFin = saldoNaturaleza(codigo, debeAnt.add(debePer), haberAnt.add(haberPer));
                ModeloLibroMayor m = new ModeloLibroMayor();
                m.setCodigo(codigo);
                m.setNombre((String) acc[0]);
                m.setSaldoInicial(saldoIni);
                m.setDebe(debePer);
                m.setHaber(haberPer);
                m.setSaldoFinal(saldoFin);
                aplicarLadosSaldo(m);
                List lineas = (List) acc[5];
                List<ModeloLibroMayorMov> det = new ArrayList<ModeloLibroMayorMov>();
                ModeloLibroMayorMov ant = new ModeloLibroMayorMov();
                ant.setGlosa("Saldo anterior");
                ant.setSaldo(saldoIni);
                det.add(ant);
                BigDecimal acumDebe = debeAnt;
                BigDecimal acumHaber = haberAnt;
                for (int i = 0; i < lineas.size(); i++) {
                    ModeloLibroMayorMov lin = (ModeloLibroMayorMov) lineas.get(i);
                    acumDebe = acumDebe.add(nvl(lin.getDebe()));
                    acumHaber = acumHaber.add(nvl(lin.getHaber()));
                    lin.setSaldo(saldoNaturaleza(codigo, acumDebe, acumHaber));
                    det.add(lin);
                }
                m.setMovimientos(det);
                if (saldoIni.compareTo(BigDecimal.ZERO) != 0
                        || debePer.compareTo(BigDecimal.ZERO) != 0
                        || haberPer.compareTo(BigDecimal.ZERO) != 0) {
                    resultado.add(m);
                }
            }
            local.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Error libro mayor " + e.getMessage());
            e.printStackTrace();
            if (local != null && local.getTransaction().isActive()) {
                local.getTransaction().rollback();
            }
        } finally {
            if (local != null) {
                local.close();
            }
        }
        return resultado;
    }

    private void aplicarLadosSaldo(ModeloLibroMayor m) {
        BigDecimal s = nvl(m.getSaldoFinal());
        boolean acreedora = false;
        if (m.getCodigo() != null && m.getCodigo().length() > 0) {
            char c = m.getCodigo().charAt(0);
            acreedora = c == '2' || c == '3' || c == '4';
        }
        if (acreedora) {
            if (s.compareTo(BigDecimal.ZERO) >= 0) {
                m.setSaldoAcreedor(s);
                m.setSaldoDeudor(BigDecimal.ZERO);
            } else {
                m.setSaldoDeudor(s.negate());
                m.setSaldoAcreedor(BigDecimal.ZERO);
            }
        } else if (s.compareTo(BigDecimal.ZERO) >= 0) {
            m.setSaldoDeudor(s);
            m.setSaldoAcreedor(BigDecimal.ZERO);
        } else {
            m.setSaldoAcreedor(s.negate());
            m.setSaldoDeudor(BigDecimal.ZERO);
        }
    }

    private boolean pasaFiltro(String codigo, String filtro) {
        if (filtro == null || filtro.length() == 0) {
            return true;
        }
        return codigo != null && codigo.indexOf(filtro) >= 0;
    }

    private BigDecimal saldoNaturaleza(String codigo, BigDecimal debe, BigDecimal haber) {
        BigDecimal d = nvl(debe);
        BigDecimal h = nvl(haber);
        if (codigo != null && codigo.length() > 0) {
            char c = codigo.charAt(0);
            if (c == '2' || c == '3' || c == '4') {
                return h.subtract(d);
            }
        }
        return d.subtract(h);
    }

    public List<CuAsientoDetalle> findDetalle(CuAsiento asiento, Tipoambiente ambiente) {
        List<CuAsientoDetalle> lista = new ArrayList<CuAsientoDetalle>();
        if (asiento == null || ambiente == null) {
            return lista;
        }
        try {
            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
            Query check = em.createQuery("SELECT c FROM CuAsiento c WHERE c.idAsiento = :id AND c.codTipoambiente = :ambiente");
            check.setParameter("id", asiento.getIdAsiento());
            check.setParameter("ambiente", ambiente);
            if (check.getResultList().isEmpty()) {
                em.getTransaction().commit();
                return lista;
            }
            Query query = em.createQuery("SELECT d FROM CuAsientoDetalle d WHERE d.idAsiento = :a ORDER BY d.idDetalle");
            query.setParameter("a", asiento);
            lista = query.getResultList();
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Error detalle asiento " + e.getMessage());
        } finally {
            if (em != null) {
                em.close();
            }
        }
        return lista;
    }

    public void asentarFactura(Factura factura) {
        if (factura == null || factura.getIdFactura() == null) {
            return;
        }
        if (factura.getFacTipo() != null && !"FACT".equals(factura.getFacTipo()) && !"NTV".equals(factura.getFacTipo())) {
            return;
        }
        Tipoambiente ambiente = factura.getCod_tipoambiente();
        if (ambiente == null) {
            return;
        }
        String origen = "NTV".equals(factura.getFacTipo()) ? "NTV" : "FACT";
        eliminarAsiento(origen, factura.getIdFactura(), ambiente);
        BigDecimal total = nvl(factura.getFacTotal());
        BigDecimal base5 = nvl(factura.getFacSubt5());
        BigDecimal base15 = nvl(factura.getFacSubt15());
        BigDecimal base0 = nvl(factura.getFacTotalBaseCero());
        BigDecimal gravada = nvl(factura.getFacTotalBaseGravaba());
        if (base15.compareTo(BigDecimal.ZERO) == 0) {
            BigDecimal restoGravada = gravada.subtract(base5);
            if (restoGravada.compareTo(BigDecimal.ZERO) > 0) {
                base15 = restoGravada;
            }
        }
        base15 = base15.add(nvl(factura.getFacSubt13())).add(nvl(factura.getFacSubt14()));
        BigDecimal iva = nvl(factura.getFacIva())
                .add(nvl(factura.getFacIva5()))
                .add(nvl(factura.getFacIva13()))
                .add(nvl(factura.getFacIva14()))
                .add(nvl(factura.getFacIva15()));
        if (base5.add(base15).add(base0).compareTo(BigDecimal.ZERO) == 0) {
            BigDecimal venta = total.subtract(iva);
            if (venta.compareTo(BigDecimal.ZERO) < 0) {
                venta = nvl(factura.getFacSubtotal());
            }
            if (nvl(factura.getFacIva5()).compareTo(BigDecimal.ZERO) > 0) {
                base5 = venta;
            } else if (base0.compareTo(BigDecimal.ZERO) == 0 && iva.compareTo(BigDecimal.ZERO) == 0) {
                base0 = venta;
            } else {
                base15 = venta;
            }
        }
        String cobro = esContado(factura.getIdFormaPago()) ? "CAJA" : "CXC";
        String glosa;
        if ("NTV".equals(origen)) {
            glosa = "Nota de venta " + (factura.getFacNumNotaVenta() != null ? factura.getFacNumNotaVenta() : factura.getIdFactura());
        } else {
            glosa = "Factura " + (factura.getFacNumeroText() != null ? factura.getFacNumeroText() : factura.getFacNumero());
        }
        List<CuAsientoDetalle> lineas = new ArrayList<CuAsientoDetalle>();
        agregarLinea(lineas, ambiente, cobro, total, BigDecimal.ZERO, glosa);
        agregarLinea(lineas, ambiente, "VENTAS", BigDecimal.ZERO, base15, glosa);
        agregarLinea(lineas, ambiente, "VENTAS_5", BigDecimal.ZERO, base5, glosa);
        agregarLinea(lineas, ambiente, "VENTAS_0", BigDecimal.ZERO, base0, glosa);
        agregarLinea(lineas, ambiente, "IVA_VENTAS", BigDecimal.ZERO, iva, glosa);
        grabarAsiento(ambiente, origen, factura.getIdFactura(), factura.getFacFecha(), glosa,
                lineas.toArray(new CuAsientoDetalle[lineas.size()]));
    }

    public void asentarNotaCredito(NotaCreditoDebito nota) {
        if (nota == null || nota.getIdNota() == null) {
            return;
        }
        Tipoambiente ambiente = ambienteDeNota(nota);
        if (ambiente == null) {
            return;
        }
        eliminarAsiento("NCRE", nota.getIdNota(), ambiente);
        BigDecimal total = nvl(nota.getFacTotal());
        BigDecimal base5 = nvl(nota.getFacSubt5());
        BigDecimal base15 = nvl(nota.getFacSubt15());
        BigDecimal base0 = nvl(nota.getFacTotalBaseCero());
        BigDecimal gravada = nvl(nota.getFacTotalBaseGravaba());
        if (base15.compareTo(BigDecimal.ZERO) == 0) {
            BigDecimal restoGravada = gravada.subtract(base5);
            if (restoGravada.compareTo(BigDecimal.ZERO) > 0) {
                base15 = restoGravada;
            }
        }
        BigDecimal iva = nvl(nota.getFacIva()).add(nvl(nota.getFacIva5())).add(nvl(nota.getFacIva15()));
        if (base5.add(base15).add(base0).compareTo(BigDecimal.ZERO) == 0) {
            BigDecimal venta = total.subtract(iva);
            if (venta.compareTo(BigDecimal.ZERO) < 0) {
                venta = nvl(nota.getFacSubtotal());
            }
            if (nvl(nota.getFacIva5()).compareTo(BigDecimal.ZERO) > 0) {
                base5 = venta;
            } else {
                base15 = venta;
            }
        }
        String cobro = "CXC";
        if (nota.getIdFactura() != null && esContado(nota.getIdFactura().getIdFormaPago())) {
            cobro = "CAJA";
        }
        String glosa = "Nota de credito " + (nota.getFacNumeroText() != null ? nota.getFacNumeroText() : nota.getFacNumero());
        List<CuAsientoDetalle> lineas = new ArrayList<CuAsientoDetalle>();
        agregarLinea(lineas, ambiente, "VENTAS", base15, BigDecimal.ZERO, glosa);
        agregarLinea(lineas, ambiente, "VENTAS_5", base5, BigDecimal.ZERO, glosa);
        agregarLinea(lineas, ambiente, "VENTAS_0", base0, BigDecimal.ZERO, glosa);
        agregarLinea(lineas, ambiente, "IVA_VENTAS", iva, BigDecimal.ZERO, glosa);
        agregarLinea(lineas, ambiente, cobro, BigDecimal.ZERO, total, glosa);
        grabarAsiento(ambiente, "NCRE", nota.getIdNota(), nota.getFacFecha(), glosa,
                lineas.toArray(new CuAsientoDetalle[lineas.size()]));
    }

    public void asentarAnulacionFactura(Factura factura) {
        if (factura == null || factura.getIdFactura() == null) {
            return;
        }
        if (factura.getFacTipo() != null && !"FACT".equals(factura.getFacTipo()) && !"NTV".equals(factura.getFacTipo())) {
            return;
        }
        Tipoambiente ambiente = factura.getCod_tipoambiente();
        if (ambiente == null) {
            return;
        }
        if (existe("ANUL", factura.getIdFactura(), ambiente)) {
            return;
        }
        String origenOrig = "NTV".equals(factura.getFacTipo()) ? "NTV" : "FACT";
        if (!existe(origenOrig, factura.getIdFactura(), ambiente)) {
            return;
        }
        BigDecimal total = nvl(factura.getFacTotal());
        BigDecimal base5 = nvl(factura.getFacSubt5());
        BigDecimal base15 = nvl(factura.getFacSubt15());
        BigDecimal base0 = nvl(factura.getFacTotalBaseCero());
        BigDecimal gravada = nvl(factura.getFacTotalBaseGravaba());
        if (base15.compareTo(BigDecimal.ZERO) == 0) {
            BigDecimal restoGravada = gravada.subtract(base5);
            if (restoGravada.compareTo(BigDecimal.ZERO) > 0) {
                base15 = restoGravada;
            }
        }
        base15 = base15.add(nvl(factura.getFacSubt13())).add(nvl(factura.getFacSubt14()));
        BigDecimal iva = nvl(factura.getFacIva())
                .add(nvl(factura.getFacIva5()))
                .add(nvl(factura.getFacIva13()))
                .add(nvl(factura.getFacIva14()))
                .add(nvl(factura.getFacIva15()));
        if (base5.add(base15).add(base0).compareTo(BigDecimal.ZERO) == 0) {
            BigDecimal venta = total.subtract(iva);
            if (venta.compareTo(BigDecimal.ZERO) < 0) {
                venta = nvl(factura.getFacSubtotal());
            }
            if (nvl(factura.getFacIva5()).compareTo(BigDecimal.ZERO) > 0) {
                base5 = venta;
            } else {
                base15 = venta;
            }
        }
        String cobro = esContado(factura.getIdFormaPago()) ? "CAJA" : "CXC";
        String glosa = ("NTV".equals(origenOrig) ? "Anulacion nota de venta " : "Anulacion factura ")
                + (factura.getFacNumeroText() != null ? factura.getFacNumeroText() : factura.getIdFactura());
        List<CuAsientoDetalle> lineas = new ArrayList<CuAsientoDetalle>();
        agregarLinea(lineas, ambiente, "VENTAS", base15, BigDecimal.ZERO, glosa);
        agregarLinea(lineas, ambiente, "VENTAS_5", base5, BigDecimal.ZERO, glosa);
        agregarLinea(lineas, ambiente, "VENTAS_0", base0, BigDecimal.ZERO, glosa);
        agregarLinea(lineas, ambiente, "IVA_VENTAS", iva, BigDecimal.ZERO, glosa);
        agregarLinea(lineas, ambiente, cobro, BigDecimal.ZERO, total, glosa);
        grabarAsiento(ambiente, "ANUL", factura.getIdFactura(), new Date(), glosa,
                lineas.toArray(new CuAsientoDetalle[lineas.size()]));
    }

    public void guardarAsientoManual(Tipoambiente ambiente, Date fecha, String glosa, List<CuAsientoDetalle> lineas) {
        if (ambiente == null) {
            throw new RuntimeException("No hay empresa activa.");
        }
        if (lineas == null || lineas.isEmpty()) {
            throw new RuntimeException("Ingrese al menos dos lineas.");
        }
        BigDecimal debe = BigDecimal.ZERO;
        BigDecimal haber = BigDecimal.ZERO;
        int validas = 0;
        for (int i = 0; i < lineas.size(); i++) {
            CuAsientoDetalle d = lineas.get(i);
            if (d == null || d.getIdSubCuenta() == null) {
                continue;
            }
            debe = debe.add(nvl(d.getDetDebe()));
            haber = haber.add(nvl(d.getDetHaber()));
            validas++;
        }
        if (validas < 2) {
            throw new RuntimeException("El asiento necesita al menos dos cuentas.");
        }
        if (debe.compareTo(haber) != 0) {
            throw new RuntimeException("El debe y el haber no cuadran.");
        }
        grabarAsiento(ambiente, "MAN", null, fecha, glosa != null && glosa.trim().length() > 0 ? glosa : "Asiento manual",
                lineas.toArray(new CuAsientoDetalle[lineas.size()]));
    }

    public CuSubCuenta buscarSubcuenta(String numero, Tipoambiente ambiente) {
        return subcuentaEmpresaLookup(numero, ambiente);
    }

    private Tipoambiente ambienteDeNota(NotaCreditoDebito nota) {
        if (nota.getIdFactura() != null && nota.getIdFactura().getCod_tipoambiente() != null) {
            return nota.getIdFactura().getCod_tipoambiente();
        }
        if (nota.getCodTipoambiente() == null) {
            return null;
        }
        EntityManager local = null;
        try {
            local = HelperPersistencia.getEMF();
            local.getTransaction().begin();
            Tipoambiente amb = local.find(Tipoambiente.class, nota.getCodTipoambiente());
            local.getTransaction().commit();
            return amb;
        } catch (Exception e) {
            return null;
        } finally {
            if (local != null) {
                local.close();
            }
        }
    }

    private CuSubCuenta subcuentaEmpresaLookup(String numero, Tipoambiente ambiente) {
        EntityManager local = null;
        try {
            local = HelperPersistencia.getEMF();
            local.getTransaction().begin();
            clonarPlantillaSiFalta(local, ambiente);
            CuSubCuenta c = subcuentaEmpresa(local, numero, ambiente);
            local.getTransaction().commit();
            return c;
        } catch (Exception e) {
            return null;
        } finally {
            if (local != null) {
                local.close();
            }
        }
    }

    public void asentarCompra(CabeceraCompra compra) {
        if (compra == null || compra.getIdCabecera() == null) {
            return;
        }
        Tipoambiente ambiente = compra.getCodTipoambiente();
        if (ambiente == null) {
            return;
        }
        if (existe("COMP", compra.getIdCabecera(), ambiente)) {
            return;
        }
        BigDecimal total = nvl(compra.getCabTotal());
        BigDecimal iva = nvl(compra.getCabIva());
        BigDecimal neto = nvl(compra.getCabSubTotal());
        if (neto.compareTo(BigDecimal.ZERO) == 0) {
            neto = total.subtract(iva);
        }
        Date fecha = compra.getCabFechaEmision() != null ? compra.getCabFechaEmision() : compra.getCabFecha();
        String glosa = "Compra " + (compra.getCabNumFactura() != null ? compra.getCabNumFactura() : compra.getIdCabecera());
        grabarAsiento(ambiente, "COMP", compra.getIdCabecera(), fecha, glosa,
                linea(ambiente, "COMPRAS", neto, BigDecimal.ZERO, glosa),
                linea(ambiente, "IVA_CREDITO", iva, BigDecimal.ZERO, glosa),
                linea(ambiente, "PROVEEDORES", BigDecimal.ZERO, total, glosa));
    }

    public void asentarRetencionCompra(RetencionCompra retencion) {
        if (retencion == null || retencion.getRcoCodigo() == null) {
            return;
        }
        Tipoambiente ambiente = retencion.getCodTipoambiente();
        if (ambiente == null && retencion.getIdCabecera() != null) {
            ambiente = retencion.getIdCabecera().getCodTipoambiente();
        }
        if (ambiente == null) {
            return;
        }
        if (existe("RCOM", retencion.getRcoCodigo(), ambiente)) {
            return;
        }
        BigDecimal renta = BigDecimal.ZERO;
        BigDecimal iva = BigDecimal.ZERO;
        EntityManager local = null;
        try {
            local = HelperPersistencia.getEMF();
            local.getTransaction().begin();
            Query q = local.createQuery("SELECT d FROM DetalleRetencionCompra d WHERE d.rcoCodigo = :r");
            q.setParameter("r", retencion);
            List dets = q.getResultList();
            local.getTransaction().commit();
            for (int i = 0; i < dets.size(); i++) {
                DetalleRetencionCompra d = (DetalleRetencionCompra) dets.get(i);
                BigDecimal valor = nvl(d.getDrcValorRetenido());
                String cod = d.getDrcCodImpuestoAsignado() != null ? d.getDrcCodImpuestoAsignado() : "";
                String tipo = d.getDrcTipoRegistro() != null ? d.getDrcTipoRegistro().toUpperCase() : "";
                if ("1".equals(cod) || "R".equals(tipo) || tipo.indexOf("RENTA") >= 0) {
                    renta = renta.add(valor);
                } else {
                    iva = iva.add(valor);
                }
            }
        } catch (Exception e) {
            System.out.println("Error leer detalle retencion " + e.getMessage());
            return;
        } finally {
            if (local != null) {
                local.close();
            }
        }
        if (iva.compareTo(BigDecimal.ZERO) == 0) {
            iva = nvl(retencion.getRcoValorRetencionIva()).subtract(renta);
            if (iva.compareTo(BigDecimal.ZERO) < 0) {
                iva = BigDecimal.ZERO;
            }
        }
        BigDecimal total = renta.add(iva);
        if (total.compareTo(BigDecimal.ZERO) == 0) {
            return;
        }
        String glosa = "Retencion compra " + (retencion.getRcoSecuencialText() != null
                ? retencion.getRcoSecuencialText() : retencion.getRcoSecuencial());
        grabarAsiento(ambiente, "RCOM", retencion.getRcoCodigo(), retencion.getRcoFecha(), glosa,
                linea(ambiente, "PROVEEDORES", total, BigDecimal.ZERO, glosa),
                linea(ambiente, "RET_RENTA_PAGAR", BigDecimal.ZERO, renta, glosa),
                linea(ambiente, "RET_IVA_PAGAR", BigDecimal.ZERO, iva, glosa));
    }

    public void asentarRetencionVenta(RetencionVenta retencion) {
        if (retencion == null || retencion.getRveCodigo() == null) {
            return;
        }
        Factura fac = retencion.getIdFactura();
        Tipoambiente ambiente = fac != null ? fac.getCod_tipoambiente() : null;
        if (ambiente == null) {
            return;
        }
        if (existe("RVEN", retencion.getRveCodigo(), ambiente)) {
            return;
        }
        BigDecimal iva = nvl(retencion.getRveValorRetencionIva100())
                .add(nvl(retencion.getRveValorRetencionIva30()))
                .add(nvl(retencion.getRveValorRetencionIva70()));
        BigDecimal renta = nvl(retencion.getRveValorRetencionRenta());
        BigDecimal total = iva.add(renta);
        if (total.compareTo(BigDecimal.ZERO) == 0) {
            return;
        }
        String glosa = "Retencion venta " + (retencion.getRveSecuencial() != null ? retencion.getRveSecuencial() : retencion.getRveCodigo());
        grabarAsiento(ambiente, "RVEN", retencion.getRveCodigo(), retencion.getRveFecha(), glosa,
                linea(ambiente, "RET_IVA_COBRAR", iva, BigDecimal.ZERO, glosa),
                linea(ambiente, "RET_RENTA_COBRAR", renta, BigDecimal.ZERO, glosa),
                linea(ambiente, "CXC", BigDecimal.ZERO, total, glosa));
    }

    private boolean existe(String origen, Integer idDocumento, Tipoambiente ambiente) {
        boolean hay = false;
        EntityManager local = null;
        try {
            local = HelperPersistencia.getEMF();
            local.getTransaction().begin();
            Query query = local.createNamedQuery("CuAsiento.findByOrigenDocumento");
            query.setParameter("ambiente", ambiente);
            query.setParameter("origen", origen);
            query.setParameter("idDocumento", idDocumento);
            hay = !query.getResultList().isEmpty();
            local.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Error existe asiento " + e.getMessage());
        } finally {
            if (local != null) {
                local.close();
            }
        }
        return hay;
    }

    private void grabarAsiento(Tipoambiente ambiente, String origen, Integer idDoc, Date fecha, String glosa, CuAsientoDetalle... lineas) {
        EntityManager local = null;
        try {
            local = HelperPersistencia.getEMF();
            local.getTransaction().begin();
            Query max = local.createQuery("SELECT COALESCE(MAX(c.asiNumero),0) FROM CuAsiento c WHERE c.codTipoambiente = :ambiente");
            max.setParameter("ambiente", ambiente);
            Integer numero = ((Number) max.getSingleResult()).intValue() + 1;
            CuAsiento asiento = new CuAsiento();
            asiento.setCodTipoambiente(ambiente);
            asiento.setAsiFecha(fecha != null ? fecha : new Date());
            asiento.setAsiNumero(numero);
            asiento.setAsiGlosa(glosa);
            asiento.setAsiOrigen(origen);
            asiento.setAsiIdDocumento(idDoc);
            asiento.setAsiTipoDoc(origen);
            asiento.setAsiEstado("REG");
            local.persist(asiento);
            local.flush();
            for (int i = 0; i < lineas.length; i++) {
                CuAsientoDetalle d = lineas[i];
                if (d == null || d.getIdSubCuenta() == null) {
                    continue;
                }
                if (nvl(d.getDetDebe()).compareTo(BigDecimal.ZERO) == 0
                        && nvl(d.getDetHaber()).compareTo(BigDecimal.ZERO) == 0) {
                    continue;
                }
                d.setIdAsiento(asiento);
                local.persist(d);
            }
            local.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Error grabar asiento " + e.getMessage());
            e.printStackTrace();
            if (local != null && local.getTransaction().isActive()) {
                local.getTransaction().rollback();
            }
        } finally {
            if (local != null) {
                local.close();
            }
        }
    }

    private void agregarLinea(List<CuAsientoDetalle> lineas, Tipoambiente ambiente, String codigoParametro,
            BigDecimal debe, BigDecimal haber, String glosa) {
        if (nvl(debe).compareTo(BigDecimal.ZERO) == 0 && nvl(haber).compareTo(BigDecimal.ZERO) == 0) {
            return;
        }
        CuAsientoDetalle d = linea(ambiente, codigoParametro, debe, haber, glosa);
        if (d != null) {
            lineas.add(d);
        }
    }

    private void eliminarAsiento(String origen, Integer idDocumento, Tipoambiente ambiente) {
        if (ambiente == null || origen == null || idDocumento == null) {
            return;
        }
        EntityManager local = null;
        try {
            local = HelperPersistencia.getEMF();
            local.getTransaction().begin();
            local.createNativeQuery(
                    "DELETE FROM public.cu_asiento_detalle d USING public.cu_asiento a "
                    + "WHERE d.id_asiento = a.id_asiento AND a.asi_origen = ?1 "
                    + "AND a.asi_id_documento = ?2 AND a.cod_tipoambiente = ?3")
                    .setParameter(1, origen)
                    .setParameter(2, idDocumento)
                    .setParameter(3, ambiente.getCodTipoambiente())
                    .executeUpdate();
            local.createNativeQuery(
                    "DELETE FROM public.cu_asiento a "
                    + "WHERE a.asi_origen = ?1 AND a.asi_id_documento = ?2 AND a.cod_tipoambiente = ?3")
                    .setParameter(1, origen)
                    .setParameter(2, idDocumento)
                    .setParameter(3, ambiente.getCodTipoambiente())
                    .executeUpdate();
            local.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Error eliminar asiento " + e.getMessage());
            if (local != null && local.getTransaction().isActive()) {
                local.getTransaction().rollback();
            }
        } finally {
            if (local != null) {
                local.close();
            }
        }
    }

    private void asegurarParametroPlantilla(EntityManager local, String codigo, String nombre, String numeroCuenta) {
        Query q = local.createQuery("SELECT p FROM CuParametro p WHERE p.parCodigo = :c AND p.codTipoambiente IS NULL");
        q.setParameter("c", codigo);
        if (!q.getResultList().isEmpty()) {
            return;
        }
        CuParametro p = new CuParametro();
        p.setParCodigo(codigo);
        p.setParNombre(nombre);
        Query sc = local.createQuery("SELECT c FROM CuSubCuenta c WHERE c.subcNumero = :n AND c.codTipoambiente IS NULL");
        sc.setParameter("n", numeroCuenta);
        List cuentas = sc.getResultList();
        if (!cuentas.isEmpty()) {
            p.setIdSubCuenta((CuSubCuenta) cuentas.get(0));
        }
        local.persist(p);
        local.flush();
    }

    private CuAsientoDetalle linea(Tipoambiente ambiente, String codigoParametro, BigDecimal debe, BigDecimal haber, String glosa) {
        CuSubCuenta cuenta = cuentaDe(codigoParametro, ambiente);
        if (cuenta == null) {
            System.out.println("Parametro contable sin cuenta: " + codigoParametro);
            return null;
        }
        CuAsientoDetalle d = new CuAsientoDetalle();
        d.setIdSubCuenta(cuenta);
        d.setDetDebe(nvl(debe));
        d.setDetHaber(nvl(haber));
        d.setDetGlosa(glosa);
        return d;
    }

    private CuSubCuenta cuentaDe(String codigoParametro, Tipoambiente ambiente) {
        EntityManager local = null;
        try {
            local = HelperPersistencia.getEMF();
            local.getTransaction().begin();
            clonarPlantillaSiFalta(local, ambiente);
            Query query = local.createQuery("SELECT p FROM CuParametro p LEFT JOIN FETCH p.idSubCuenta WHERE p.parCodigo = :parCodigo AND p.codTipoambiente = :ambiente");
            query.setParameter("parCodigo", codigoParametro);
            query.setParameter("ambiente", ambiente);
            List list = query.getResultList();
            local.getTransaction().commit();
            if (list.isEmpty()) {
                return null;
            }
            CuParametro p = (CuParametro) list.get(0);
            return p.getIdSubCuenta();
        } catch (Exception e) {
            System.out.println("Error cuentaDe " + e.getMessage());
            return null;
        } finally {
            if (local != null) {
                local.close();
            }
        }
    }

    private void clonarPlantillaSiFalta(EntityManager local, Tipoambiente ambiente) {
        new ServicioPlanCuentas().clonarPlantilla(local, ambiente);
        asegurarParametroPlantilla(local, "VENTAS_5", "Ventas gravadas tarifa 5%", "4.01.01.002");
        asegurarParametroPlantilla(local, "VENTAS_0", "Ventas tarifa 0%", "4.01.01.003");
        Query plantilla = local.createNamedQuery("CuParametro.findPlantilla");
        List list = plantilla.getResultList();
        Query existentes = local.createQuery("SELECT p.parCodigo FROM CuParametro p WHERE p.codTipoambiente = :ambiente");
        existentes.setParameter("ambiente", ambiente);
        java.util.HashSet<String> hay = new java.util.HashSet<String>();
        List codes = existentes.getResultList();
        for (int i = 0; i < codes.size(); i++) {
            if (codes.get(i) != null) {
                hay.add(codes.get(i).toString());
            }
        }
        for (int i = 0; i < list.size(); i++) {
            CuParametro origen = (CuParametro) list.get(i);
            if (origen.getParCodigo() != null && hay.contains(origen.getParCodigo())) {
                continue;
            }
            CuParametro copia = new CuParametro();
            copia.setParCodigo(origen.getParCodigo());
            copia.setParNombre(origen.getParNombre());
            copia.setCodTipoambiente(ambiente);
            String numero = origen.getIdSubCuenta() != null ? origen.getIdSubCuenta().getSubcNumero() : null;
            copia.setIdSubCuenta(subcuentaEmpresa(local, numero, ambiente));
            local.persist(copia);
        }
        local.flush();
        remapearCuentasEmpresa(local, ambiente);
    }

    private CuSubCuenta subcuentaEmpresa(EntityManager local, String numero, Tipoambiente ambiente) {
        if (numero == null || numero.trim().length() == 0) {
            return null;
        }
        Query q = local.createQuery("SELECT c FROM CuSubCuenta c WHERE c.subcNumero = :n AND c.codTipoambiente = :ambiente");
        q.setParameter("n", numero.trim());
        q.setParameter("ambiente", ambiente);
        List list = q.getResultList();
        if (list.isEmpty()) {
            return null;
        }
        return (CuSubCuenta) list.get(0);
    }

    private void remapearCuentasEmpresa(EntityManager local, Tipoambiente ambiente) {
        Query params = local.createQuery("SELECT p FROM CuParametro p LEFT JOIN FETCH p.idSubCuenta WHERE p.codTipoambiente = :ambiente");
        params.setParameter("ambiente", ambiente);
        List list = params.getResultList();
        for (int i = 0; i < list.size(); i++) {
            CuParametro p = (CuParametro) list.get(i);
            if (p.getIdSubCuenta() == null) {
                continue;
            }
            Tipoambiente ambCuenta = p.getIdSubCuenta().getCodTipoambiente();
            if (ambCuenta != null && ambiente.getCodTipoambiente().equals(ambCuenta.getCodTipoambiente())) {
                continue;
            }
            CuSubCuenta emp = subcuentaEmpresa(local, p.getIdSubCuenta().getSubcNumero(), ambiente);
            if (emp != null) {
                p.setIdSubCuenta(emp);
            }
        }
        local.createNativeQuery(
                "UPDATE public.cu_asiento_detalle d SET id_sub_cuenta = s2.id_sub_cuenta "
                + "FROM public.cu_asiento a, public.cu_sub_cuenta s1, public.cu_sub_cuenta s2 "
                + "WHERE d.id_asiento = a.id_asiento AND d.id_sub_cuenta = s1.id_sub_cuenta "
                + "AND s2.subc_numero = s1.subc_numero AND s2.cod_tipoambiente = a.cod_tipoambiente "
                + "AND a.cod_tipoambiente = ?1 "
                + "AND (s1.cod_tipoambiente IS NULL OR s1.cod_tipoambiente <> a.cod_tipoambiente)")
                .setParameter(1, ambiente.getCodTipoambiente())
                .executeUpdate();
    }

    private boolean esContado(FormaPago forma) {
        if (forma == null) {
            return false;
        }
        String nom = forma.getForNombre() != null ? forma.getForNombre().toUpperCase() : "";
        String cod = forma.getForCodigo() != null ? forma.getForCodigo() : "";
        return nom.indexOf("EFECTIVO") >= 0 || nom.indexOf("CONTADO") >= 0 || nom.indexOf("CAJA") >= 0
                || "01".equals(cod);
    }

    private BigDecimal toBig(Object v) {
        if (v == null) {
            return BigDecimal.ZERO;
        }
        if (v instanceof BigDecimal) {
            return (BigDecimal) v;
        }
        return new BigDecimal(v.toString());
    }

    private BigDecimal nvl(BigDecimal v) {
        return v == null ? BigDecimal.ZERO : v;
    }
}
