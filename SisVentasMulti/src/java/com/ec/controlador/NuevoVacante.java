/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ec.controlador;

import com.ec.entidad.DetalleKardex;
import com.ec.entidad.Kardex;
import com.ec.entidad.Parametrizar;
import com.ec.entidad.Producto;
import com.ec.entidad.Tipoambiente;
import com.ec.entidad.Vacante;
import com.ec.seguridad.EnumSesion;
import com.ec.seguridad.UserCredential;
import com.ec.servicio.ServicioDetalleKardex;
import com.ec.servicio.ServicioKardex;
import com.ec.servicio.ServicioParametrizar;
import com.ec.servicio.ServicioProducto;
import com.ec.servicio.ServicioTipoAmbiente;
import com.ec.servicio.ServicioTipoKardex;
import com.ec.servicio.ServicioVacante;
import com.ec.untilitario.ArchivoUtils;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.image.AImage;
import org.zkoss.image.Image;
import org.zkoss.io.Files;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Fileupload;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

/**
 *
 * @author gato
 */
public class NuevoVacante {

    private Vacante vacante = new Vacante();
    ServicioVacante servicioVacante = new ServicioVacante();
    ServicioParametrizar servicioParametrizar = new ServicioParametrizar();
    private String accion = "create";
    private Parametrizar parametrizar = new Parametrizar();
    private Kardex kardex = new Kardex();
    @Wire
    Window windowCliente;
    @Wire
    Textbox txtIvaRec;

    private String conIva = "S";
    private String conICE = "N";
    private String esProducto = "P";
    UserCredential credential = new UserCredential();
    ServicioTipoAmbiente servicioTipoAmbiente = new ServicioTipoAmbiente();
    private Tipoambiente amb = new Tipoambiente();
    private String amRuc = "";
    private Boolean esUnProdcuto = Boolean.TRUE;

    //subir imagen
    private String filePath;
    byte[] buffer = new byte[1024 * 1024];
    private AImage fotoGeneral = null;

    @AfterCompose
    public void afterCompose(@ExecutionArgParam("valor") Vacante valor, @ContextParam(ContextType.VIEW) Component view) {
        Selectors.wireComponents(view, this, false);
        parametrizar = servicioParametrizar.FindALlParametrizar();
        if (valor != null) {
            this.vacante = valor;
            try {
                System.out.println("PATH" + parametrizar.getParServlet() + File.separator + "img" + File.separator + vacante.getVacNombreImagen());
                fotoGeneral = new AImage("fotoPedido", Imagen_A_Bytes(parametrizar.getParPathRecursos() + File.separator + "img" + File.separator + vacante.getVacNombreImagen()));
//                Imagen_A_Bytes(empresa.getIdUsuario().getUsuFoto());
            } catch (FileNotFoundException e) {
                System.out.println("error imagen " + e.getMessage());
            } catch (IOException ex) {
                Logger.getLogger(NuevoProducto.class.getName()).log(Level.SEVERE, null, ex);
            }

            accion = "update";
        } else {
            this.vacante = new Vacante();

            accion = "create";
        }

    }

    public NuevoVacante() {

        Session sess = Sessions.getCurrent();
        credential = (UserCredential) sess.getAttribute(EnumSesion.userCredential.getNombre());
//        amRuc = credential.getUsuarioSistema().getUsuRuc();
        amb = servicioTipoAmbiente.findALlTipoambientePorUsuario(credential.getUsuarioSistema());

    }

    @Command
    public void guardar() {
        if (vacante.getVacNombre() != null
                && vacante.getVacDescripcion() != null
                && vacante.getVacSueldo() != null) {
            vacante.setCodTipoambiente(amb);

            if (accion.equals("create")) {

                servicioVacante.crear(vacante);

                windowCliente.detach();
            } else {
                servicioVacante.modificar(vacante);

                windowCliente.detach();
            }

        } else {
            Messagebox.show("Verifique la informacion requerida", "Atención", Messagebox.OK, Messagebox.ERROR);
        }
    }

    public Boolean getEsUnProdcuto() {
        return esUnProdcuto;
    }

    public void setEsUnProdcuto(Boolean esUnProdcuto) {
        this.esUnProdcuto = esUnProdcuto;
    }

    @Command
    @NotifyChange({"fileContent", "empresa", "fotoGeneral"})
    public void subirImagen() throws InterruptedException, IOException {

        org.zkoss.util.media.Media media = Fileupload.get();
        if (media instanceof org.zkoss.image.Image) {
            String nombre = media.getName();
            Integer largo = ((Image) media).getWidth();
            Integer alto = ((Image) media).getHeight();

            if ((largo < 100 || largo > 1200) || (alto < 100 || alto > 1200)) {
                Clients.showNotification("El alto y ancho de la imagen debe ser entre 100px y 1200px (pixeles) ",
                        Clients.NOTIFICATION_TYPE_ERROR, null, "middle_center", 3000, true);
                return;
            }

            if (media.getByteData().length > 10 * 1024 * 1024) {
                Messagebox.show("El arhivo seleccionado sobrepasa el tamaño de 10Mb.\n Por favor seleccione un archivo más pequeño.", "Atención", Messagebox.OK, Messagebox.ERROR);

                return;
            }

            String reportFile = Executions.getCurrent().getDesktop().getWebApp()
                    .getRealPath("/reportes");
            filePath = parametrizar.getParPathRecursos() + File.separator + "img" + File.separator;

            File baseDir = new File(filePath);
            if (!baseDir.exists()) {
                baseDir.mkdirs();
            }
            Files.copy(new File(filePath + File.separator + media.getName().toLowerCase()),
                    media.getStreamData());

            vacante.setVacNombreImagen(media.getName().toLowerCase());
            vacante.setVacPathImagen(parametrizar.getParServlet() + "/img/" + media.getName().toLowerCase());
            System.out.println("PATH SUBIR " + filePath + File.separator + media.getName());
            fotoGeneral = new AImage("fotoPedido", Imagen_A_Bytes(filePath + File.separator + media.getName().toLowerCase()));

        }
    }

    public byte[] Imagen_A_Bytes(String pathImagen) throws FileNotFoundException {
        String reportPath = "";
        reportPath = pathImagen;
        File file = new File(reportPath);

        FileInputStream fis = new FileInputStream(file);
        //create FileInputStream which obtains input bytes from a file in a file system
        //FileInputStream is meant for reading streams of raw bytes such as image data. For reading streams of characters, consider using FileReader.

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buf = new byte[1024];
        try {
            for (int readNum; (readNum = fis.read(buf)) != -1;) {
                //Writes to this byte array output stream
                bos.write(buf, 0, readNum);
//                System.out.println("read " + readNum + " bytes,");
            }
        } catch (IOException ex) {
        }

        byte[] bytes = bos.toByteArray();
        return bytes;
    }

    public AImage getFotoGeneral() {
        return fotoGeneral;
    }

    public void setFotoGeneral(AImage fotoGeneral) {
        this.fotoGeneral = fotoGeneral;
    }

    public Vacante getVacante() {
        return vacante;
    }

    public void setVacante(Vacante vacante) {
        this.vacante = vacante;
    }

}
