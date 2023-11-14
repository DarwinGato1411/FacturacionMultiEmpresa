/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ec.controlador;

import com.ec.entidad.Producto;
import com.ec.entidad.Tipoambiente;
import com.ec.entidad.Vacante;
import com.ec.seguridad.EnumSesion;
import com.ec.seguridad.UserCredential;
import com.ec.servicio.ServicioGeneral;
import com.ec.servicio.ServicioKardex;
import com.ec.servicio.ServicioTipoAmbiente;
import com.ec.servicio.ServicioVacante;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.activation.MimetypesFileTypeMap;
import javax.mail.internet.ParseException;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.util.media.AMedia;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zul.Filedownload;

import java.io.File;
import java.io.FileInputStream;

/**
 *
 * @author gato
 */
public class AdmVacante {

    ServicioTipoAmbiente servicioTipoAmbiente = new ServicioTipoAmbiente();
    ServicioVacante servicioVacante = new ServicioVacante();
    ServicioKardex servicioKardex = new ServicioKardex();
    
    private List<Vacante> listaVacante = new ArrayList<Vacante>();

    
    private String buscarNombre = "";
    private String buscarCodigo = "";
    //reporte
    AMedia fileContent = null;
    Connection con = null;

    private static String PATH_BASE = "";
    private static String FOLDER_CODIGO_BARRAS = "";
    private static String PATH_CODIGO_BARRAS = "";

    private Integer cantidadCodBar = 1;
    UserCredential credential = new UserCredential();
    private String amRuc = "";
    private Tipoambiente amb = null;
    ServicioGeneral servicioGeneral = new ServicioGeneral();

    public AdmVacante() {
        Session sess = Sessions.getCurrent();
        credential = (UserCredential) sess.getAttribute(EnumSesion.userCredential.getNombre());
//        amRuc = credential.getUsuarioSistema().getUsuRuc();
        amb = servicioTipoAmbiente.findALlTipoambientePorUsuario(credential.getUsuarioSistema());
//         = servicioTipoAmbiente.FindALlTipoambiente();
        //OBTIENE LAS RUTAS DE ACCESO A LOS DIRECTORIOS DE LA TABLA TIPOAMBIENTE
        PATH_BASE = amb.getAmDirBaseArchivos() + File.separator
                    + amb.getAmDirXml();
        FOLDER_CODIGO_BARRAS = PATH_BASE + File.separator + "CODIGOBARRAS";

        File folderGen = new File(FOLDER_CODIGO_BARRAS);
        if (!folderGen.exists()) {
            folderGen.mkdirs();
        }
        findLikeNombre();
       
    }

   
  
    private void findLikeNombre() {
        listaVacante = servicioVacante.findVacante(buscarNombre, amb);
    }



    public List<Vacante> getListaVacante() {
        return listaVacante;
    }

    public void setListaVacante(List<Vacante> listaVacante) {
        this.listaVacante = listaVacante;
    }

    

    public String getBuscarNombre() {
        return buscarNombre;
    }

    public void setBuscarNombre(String buscarNombre) {
        this.buscarNombre = buscarNombre;
    }

   
    @Command
    @NotifyChange({"listaVacante", "buscarNombre"})
    public void buscarLikeNombre() {

        findLikeNombre();
      
    }

    

    @Command
    @NotifyChange({"listaVacante", "buscarNombre"})
    public void nuevo() {
        buscarNombre = "";
        org.zkoss.zul.Window window = (org.zkoss.zul.Window) Executions.createComponents(
                    "/nuevo/vacante.zul", null, null);
        window.doModal();
        findLikeNombre();
        
    }

    @Command
    @NotifyChange({"listaVacante", "buscarNombre"})
    public void actualizar(@BindingParam("valor") Vacante valor) {
        buscarNombre = "";
        final HashMap<String, Vacante> map = new HashMap<String, Vacante>();
        map.put("valor", valor);
        org.zkoss.zul.Window window = (org.zkoss.zul.Window) Executions.createComponents(
                    "/nuevo/vacante.zul", null, map);
        window.doModal();
        // findLikeNombre();
        // getProductosModel();
    }

   
    /*EXPORTAR EXCEL*/
    @Command
    public void exportListboxToExcel() throws Exception {
        try {
            File dosfile = new File(exportarExcel());
            if (dosfile.exists()) {
                FileInputStream inputStream = new FileInputStream(dosfile);
                Filedownload.save(inputStream, new MimetypesFileTypeMap().getContentType(dosfile), dosfile.getName());
            }
        } catch (FileNotFoundException e) {
            System.out.println("ERROR AL DESCARGAR EL ARCHIVO" + e.getMessage());
        }
    }

    private String exportarExcel() throws FileNotFoundException, IOException, ParseException {
        String directorioReportes = Executions.getCurrent().getDesktop().getWebApp().getRealPath("/reportes");

        Date date = new Date();
        SimpleDateFormat fhora = new SimpleDateFormat("HH:mm");
        SimpleDateFormat sm = new SimpleDateFormat("yyy-MM-dd");
        String strDate = sm.format(date);

        String pathSalida = directorioReportes + File.separator + "vacante.xls";
        System.out.println("Direccion del reporte  " + pathSalida);
        try {
            int j = 1;
            File archivoXLS = new File(pathSalida);
            if (archivoXLS.exists()) {
                archivoXLS.delete();
            }
            archivoXLS.createNewFile();
            FileOutputStream archivo = new FileOutputStream(archivoXLS);
            HSSFWorkbook wb = new HSSFWorkbook();
            HSSFSheet s = wb.createSheet("Autorizadas");

            HSSFFont fuente = wb.createFont();
            fuente.setBoldweight((short) 700);
            HSSFCellStyle estiloCelda = wb.createCellStyle();
            estiloCelda.setWrapText(true);
            estiloCelda.setAlignment((short) 2);
            estiloCelda.setFont(fuente);

            HSSFCellStyle estiloCeldaInterna = wb.createCellStyle();
            estiloCeldaInterna.setWrapText(true);
            estiloCeldaInterna.setAlignment((short) 5);
            estiloCeldaInterna.setFont(fuente);

            HSSFCellStyle estiloCelda1 = wb.createCellStyle();
            estiloCelda1.setWrapText(true);
            estiloCelda1.setFont(fuente);

            HSSFRow r = null;

            HSSFCell c = null;
            r = s.createRow(0);

            HSSFCell chfe = r.createCell(0);
            chfe.setCellValue(new HSSFRichTextString("Nomvre"));
            chfe.setCellStyle(estiloCelda);

            HSSFCell ch1 = r.createCell(j++);
            ch1.setCellValue(new HSSFRichTextString("Descripcion"));
            ch1.setCellStyle(estiloCelda);

            HSSFCell ch2 = r.createCell(j++);
            ch2.setCellValue(new HSSFRichTextString("Fecha inicio"));
            ch2.setCellStyle(estiloCelda);

            HSSFCell ch21 = r.createCell(j++);
            ch21.setCellValue(new HSSFRichTextString("Fecha fin"));
            ch21.setCellStyle(estiloCelda);

            HSSFCell ch3 = r.createCell(j++);
            ch3.setCellValue(new HSSFRichTextString("Sueldo"));
            ch3.setCellStyle(estiloCelda);

           

            int rownum = 1;
            int i = 0;

            for (Vacante item : listaVacante) {
                i = 0;

                r = s.createRow(rownum);

                HSSFCell cf = r.createCell(i++);
                cf.setCellValue(new HSSFRichTextString(item.getVacNombre().toString()));

                HSSFCell c0 = r.createCell(i++);
                c0.setCellValue(new HSSFRichTextString(item.getVacDescripcion()));

                HSSFCell c1 = r.createCell(i++);
                c1.setCellValue(new HSSFRichTextString(sm.format(item.getVacFechaInicio())));

                HSSFCell c11 = r.createCell(i++);
                c11.setCellValue(new HSSFRichTextString(sm.format(item.getVacFechaFin())));

                HSSFCell c2 = r.createCell(i++);
                c2.setCellValue(new HSSFRichTextString(item.getVacSueldo().toString()));

             
                rownum += 1;

            }
            for (int k = 0; k <= listaVacante.size(); k++) {
                s.autoSizeColumn(k);
            }
            wb.write(archivo);
            archivo.close();

        } catch (IOException e) {
            System.out.println("error " + e.getMessage());
        }
        return pathSalida;

    }

    
}
