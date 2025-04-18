/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ec.controlador;

import com.ec.dao.DetalleFacturaDAO;
import com.ec.seguridad.EnumSesion;
import com.ec.seguridad.UserCredential;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Menu;
import org.zkoss.zul.Menuitem;

/**
 *
 * @author gato
 */
public class MenuOpciones extends SelectorComposer<Component> {

    @Wire("#menuEmitirfact")
    Menu menuEmitirfact;
    @Wire("#menuVentas")
    Menu menuVentas;
    @Wire("#menuCompras")
    Menu menuCompras;
    @Wire("#menuGuias")
    Menu menuGuias;
    @Wire("#menuKardex")
    Menu menuKardex;
    @Wire("#menuReportes")
    Menu menuReportes;

    @Wire("#btnAdministarVenta")
    Menuitem btnAdministarVenta;
    @Wire("#muenGestionUsu")
    Menuitem muenGestionUsu;
    @Wire("#muenGestionEmpresas")
    Menuitem muenGestionEmpresas;
    @Wire("#btnHisDeclaraciones")
    Menuitem btnHisDeclaraciones;
    @Wire("#btnGestionFactura")
    Menuitem btnGestionFactura;
    @Wire("#btnOrden")
    Menuitem btnOrden;
    @Wire("#btnlistaOrden")
    Menuitem btnlistaOrden;

    @Wire("#btnCodigoretencion")
    Menuitem btnCodigoretencion;
    @Wire("#btnFacturarPar")
    Menuitem btnFacturarPar;
    @Wire("#btnFacturar")
    Menuitem btnFacturar;
    @Wire("#btnCierreCajaUsu")
    Menuitem btnCierreCajaUsu;
    @Wire("#btnListarCierre")
    Menuitem btnListarCierre;

    UserCredential credential = new UserCredential();
    private String acceso = "";

    public MenuOpciones() {
        Session sess = Sessions.getCurrent();
        UserCredential cre = (UserCredential) sess.getAttribute(EnumSesion.userCredential.getNombre());
        credential = cre;
    }

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        if (credential.getUsuarioSistema() != null) {

            if (credential.getUsuarioSistema().getUsuNivel() == 1) {
                menuEmitirfact.setVisible(Boolean.FALSE);
                menuVentas.setVisible(Boolean.FALSE);
                menuCompras.setVisible(Boolean.FALSE);
                menuGuias.setVisible(Boolean.FALSE);
                menuKardex.setVisible(Boolean.FALSE);
                menuReportes.setVisible(Boolean.FALSE);
                btnAdministarVenta.setVisible(Boolean.FALSE);
                btnHisDeclaraciones.setVisible(Boolean.FALSE);

            } else {
                muenGestionUsu.setVisible(Boolean.FALSE);
                muenGestionEmpresas.setVisible(Boolean.FALSE);
                btnGestionFactura.setVisible(Boolean.FALSE);
                if (credential.getTipoambiente().getAmParqueadero()) {
                    btnFacturar.setVisible(Boolean.FALSE);
                    btnFacturarPar.setVisible(Boolean.TRUE);
                } else {
                    btnFacturar.setVisible(Boolean.TRUE);
                    btnFacturarPar.setVisible(Boolean.FALSE);
                }

                if (credential.getTipoambiente().getAmCierreCaja()) {
                    btnCierreCajaUsu.setVisible(Boolean.TRUE);
                    btnListarCierre.setVisible(Boolean.TRUE);
                } else {
                    btnCierreCajaUsu.setVisible(Boolean.FALSE);
                    btnListarCierre.setVisible(Boolean.FALSE);
                }
            }
        }
    }

    @Listen("onClick = #buttonConsultar")
    public void buttonConsultar() {
        Executions.sendRedirect("/consultas.zul");
    }

    @Listen("onClick = #btnFacturar")
    public void doFacturar() {
        Executions.sendRedirect("/venta/facturar.zul");
    }

    @Listen("onClick = #btnFacturarPar")
    public void btnFacturarPar() {
        Executions.sendRedirect("/venta/facturarpar.zul");
    }

    @Listen("onClick = #btnKardex")
    public void btnKardex() {
        Executions.sendRedirect("/administrar/kardex.zul");
    }

    @Listen("onClick = #btnSinFact")
    public void btnSinFactura() {
        Executions.sendRedirect("/venta/ventadiaria.zul");
    }

    @Listen("onClick = #btnAdministarVenta")
    public void doAdministrarVenta() {
        Executions.sendRedirect("/venta/administrar.zul");
    }

    @Listen("onClick = #btnHisDeclaraciones")
    public void doAdministrarHistoDeclaraciones() {
        Executions.sendRedirect("/reportevistas/histodeclaraciones.zul");
    }

    @Listen("onClick = #btnConsulta")
    public void doConsultas() {
        Executions.sendRedirect("/compra/consultas.zul");
    }

    @Listen("onClick = #btnReporte")
    public void doReportes() {
        Executions.sendRedirect("/venta/listanotaventa.zul");
    }

    @Listen("onClick = #btnNotaVentas")
    public void doAdministrarNotaVenta() {
        Executions.sendRedirect("/venta/listafacturas.zul");
    }

    @Listen("onClick = #btnNotaVenta")
    public void btnNotaVenta() {
        Executions.sendRedirect("/venta/listanotaventa.zul");
    }

    @Listen("onClick = #btnGuiaRemision")
    public void btnGuiaRemision() {
        Executions.sendRedirect("/venta/listaguia.zul");
    }

    @Listen("onClick = #btnCierreCaja")
    public void doCierreCaja() {
        Executions.sendRedirect("/venta/cierrecaja.zul");
    }

    @Listen("onClick = #btnProformasEmitidas")
    public void doListaProformas() {
        Executions.sendRedirect("/venta/listacotizaciones.zul");
    }

    @Listen("onClick = #btnIngresoCompras")
    public void doIngresoCompras() {
        Executions.sendRedirect("/compra/compras.zul");
    }

    @Listen("onClick = #btnListaComprasSRI")
    public void btnListaComprasSRI() {
        Executions.sendRedirect("/compra/listacomprassri.zul");
    }

    @Listen("onClick = #btnListaCompras")
    public void doInventario() {
        Executions.sendRedirect("/compra/listacompras.zul");
    }

    @Listen("onClick = #btnListaVentaDiaria")
    public void doListaVentaDiaria() {
        Executions.sendRedirect("/administrar/listaventadiaria.zul");
    }

    @Listen("onClick = #btnSistema")
    public void doAdministrarSistema() {
        Executions.sendRedirect("/administrar/administrarusuarios.zul");
    }

    @Listen("onClick = #btnPedidos")
    public void doRealizarPedidos() {
        Executions.sendRedirect("/administrar/pedidos.zul");
    }

    @Listen("onClick = #btnMailMasivo")
    public void mailMasivo() {
        Executions.sendRedirect("/administrar/publicidad.zul");
    }

    @Listen("onClick = #btnContabilidad")
    public void btnContabilidad() {
        Executions.sendRedirect("/contabilidad/ventasats.zul");
    }

    @Listen("onClick = #btnContabilidadATS")
    public void btnContabilidadATS() {
        Executions.sendRedirect("/contabilidad/ats.zul");
    }

    @Listen("onClick = #btnRetenciones")
    public void btnRetenciones() {
        Executions.sendRedirect("/compra/listaretencion.zul");
    }

    @Listen("onClick = #btnBalanceUtilidad")
    public void btnBalanceUtilidad() {
        Executions.sendRedirect("/contabilidad/compraventa.zul");
    }

    @Listen("onClick = #btnRetencionesCasillero")
    public void btnRetencionesCasillero() {
        Executions.sendRedirect("/compra/listaretencioncasillero.zul");
    }

    @Listen("onClick = #btnFactAutori")
    public void btnFactAutori() {
        Executions.sendRedirect("/venta/facturasautorizadas.zul");
    }

    @Listen("onClick = #btnNotaCredito")
    public void btnNotaCredito() {
        Executions.sendRedirect("/venta/listanc.zul");
    }

    @Listen("onClick = #btnEmitirGuiaRemision")
    public void btnEmitirGuiaRemision() {
        Executions.sendRedirect("/venta/guia.zul");
    }

    @Listen("onClick = #btnVentaRuta")
    public void btnVentaRuta() {
        Executions.sendRedirect("/venta/listaventaruta.zul");
    }

    @Listen("onClick = #btnRepVentCant")
    public void btnRepVentCant() {
        Executions.sendRedirect("/contabilidad/cantproducto.zul");
    }

    @Listen("onClick = #btnAjuste")
    public void btnAjuste() {
        Executions.sendRedirect("/administrar/ajuste.zul");
    }

    @Listen("onClick = #btnAcumuladoventas")
    public void btnAcumuladoventas() {
        Executions.sendRedirect("/reportevistas/ventaaniomes.zul");
    }

    @Listen("onClick = #btnRotacionProducto")
    public void btnRotacionProducto() {
        Executions.sendRedirect("/reportevistas/rotacion.zul");
    }

    @Listen("onClick = #btnKardexPorProd")
    public void btnKardexPorProd() {
        Executions.sendRedirect("/reportevistas/kardexproductos.zul");
    }

    @Listen("onClick = #btnListarCierre")
    public void btnListarCierre() {
        Executions.sendRedirect("/reportevistas/listacierrecaja.zul");
    }

    @Listen("onClick = #btnPreciPromCompra")
    public void btnPreciPromCompra() {
        Executions.sendRedirect("/reportevistas/listacomprapromedio.zul");
    }

    @Listen("onClick = #btnPreciPromVenta")
    public void btnPreciPromVenta() {
        Executions.sendRedirect("/reportevistas/listaventapromedio.zul");
    }

    @Listen("onClick = #btnOrdenTrab")
    public void btnOrdenTrab() {
        Executions.sendRedirect("/venta/ordentrabajo.zul");
    }

    @Listen("onClick = #btnListaOrden")
    public void btnListaOrden() {
        Executions.sendRedirect("/venta/listaorden.zul");
    }

    @Listen("onClick = #btnfactCobra")
    public void btnfactCobra() {
        Executions.sendRedirect("/venta/facturasporcobrar.zul");
    }

    @Listen("onClick = #btnOrden")
    public void btnOrden() {
        Executions.sendRedirect("/venta/ordentrabajo.zul");
    }

    @Listen("onClick = #btnlistaOrden")
    public void btnlistaOrden() {
        Executions.sendRedirect("/venta/listaorden.zul");
    }

    @Listen("onClick = #btnCodigoretencion")
    public void btnCodigoretencion() {
        Executions.sendRedirect("/administrar/codigoretenciones.zul");
    }

    @Command
    public void facturar(@BindingParam("valor") DetalleFacturaDAO valor) {
        Executions.sendRedirect("/venta/facturar.zul");
    }

    @Listen("onClick = #btnCierreCajaUsu")
    public void btnCierreCaja() {
//        if (credential.getUsuarioSistema().getUsuNivel() != 1) {
        org.zkoss.zul.Window window = (org.zkoss.zul.Window) Executions.createComponents(
                "/nuevo/cierrecaja.zul", null, null);
        window.doModal();
//        } else {
//            Clients.showNotification("El usuario administrador no puede cerrar una caja",
//                    Clients.NOTIFICATION_TYPE_INFO, null, "middle_center", 5000, true);
//        }

    }

    @Command
    public void nuevoProducto() {

        org.zkoss.zul.Window window = (org.zkoss.zul.Window) Executions.createComponents(
                "/nuevo/producto.zul", null, null);
        window.doModal();

    }

    @Command
    public void nuevoCliente() {

        org.zkoss.zul.Window window = (org.zkoss.zul.Window) Executions.createComponents(
                "/nuevo/cliente.zul", null, null);
        window.doModal();

    }

    public UserCredential getCredential() {
        return credential;
    }

    public void setCredential(UserCredential credential) {
        this.credential = credential;
    }

    public String getAcceso() {
        return acceso;
    }

    public void setAcceso(String acceso) {
        this.acceso = acceso;
    }

    /*gestion administrativa*/
    @Listen("onClick = #muenGestionUsu")
    public void muenGestionUsu() {
        Executions.sendRedirect("/administrar/gestionusuarios.zul");
    }

    @Listen("onClick = #muenGestionEmpresas")
    public void muenGestionEmpresas() {
        Executions.sendRedirect("/administrar/empresas.zul");
    }

    @Listen("onClick = #btnGestionFactura")
    public void btnGestionFactura() {
        Executions.sendRedirect("/administrar/gestionfactura.zul");
    }
}
