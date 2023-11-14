/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ec.controlador;

import com.ec.entidad.Factura;
import com.ec.entidad.Postulaciones;
import com.ec.entidad.Tipoambiente;
import com.ec.seguridad.EnumSesion;
import com.ec.seguridad.UserCredential;
import com.ec.servicio.ServicioGeneral;
import com.ec.servicio.ServicioPostulaciones;
import com.ec.servicio.ServicioTipoAmbiente;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.util.media.AMedia;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;


/**
 *
 * @author gato
 */
public class AdmPostulaciones {

    ServicioTipoAmbiente servicioTipoAmbiente = new ServicioTipoAmbiente();
    ServicioPostulaciones servicio = new ServicioPostulaciones();
    
    
    private List<Postulaciones> listaDatos = new ArrayList<Postulaciones>();

    
    private String buscarNombre = "";    
    private Date inicio= new Date();
    private Date fin= new Date();
    //reporte
    AMedia fileContent = null;
    Connection con = null;


    UserCredential credential = new UserCredential();

    private Tipoambiente amb = null;
    ServicioGeneral servicioGeneral = new ServicioGeneral();

    public AdmPostulaciones() {
        Session sess = Sessions.getCurrent();
        credential = (UserCredential) sess.getAttribute(EnumSesion.userCredential.getNombre());
//        amRuc = credential.getUsuarioSistema().getUsuRuc();
        amb = servicioTipoAmbiente.findALlTipoambientePorUsuario(credential.getUsuarioSistema());
//         = servicioTipoAmbiente.FindALlTipoambiente();
        findLikeNombre();
       
    }

   
  
    private void findLikeNombre() {
        listaDatos = servicio.findPostulaciones(credential.getUsuarioSistema(),buscarNombre);
    }
    private void findFecha() {
        listaDatos = servicio.findPostulacionesFecha(credential.getUsuarioSistema(),inicio,fin);
    }




    

    public String getBuscarNombre() {
        return buscarNombre;
    }

    public void setBuscarNombre(String buscarNombre) {
        this.buscarNombre = buscarNombre;
    }

   
    @Command
    @NotifyChange({"listaDatos", "buscarNombre"})
    public void buscarLikeNombre() {

        findLikeNombre();
      
    }
    @Command
    @NotifyChange({"listaDatos", "buscarNombre"})
    public void aprobarReprobar(@BindingParam("valor") Postulaciones valor) {

      servicio.modificar(valor);
      
    }
    @Command
    @NotifyChange({"listaDatos", "inicio","fin"})
    public void buscarPorFecha() {

        findFecha();
      
    }

    public List<Postulaciones> getListaDatos() {
        return listaDatos;
    }

    public void setListaDatos(List<Postulaciones> listaDatos) {
        this.listaDatos = listaDatos;
    }


    /*EXPORTAR EXCEL*/
//    @Command
//    public void exportListboxToExcel() throws Exception {
//        try {
//            File dosfile = new File(exportarExcel());
//            if (dosfile.exists()) {
//                FileInputStream inputStream = new FileInputStream(dosfile);
//                Filedownload.save(inputStream, new MimetypesFileTypeMap().getContentType(dosfile), dosfile.getName());
//            }
//        } catch (FileNotFoundException e) {
//            System.out.println("ERROR AL DESCARGAR EL ARCHIVO" + e.getMessage());
//        }
//    }
//
//    private String exportarExcel() throws FileNotFoundException, IOException, ParseException {
//        String directorioReportes = Executions.getCurrent().getDesktop().getWebApp().getRealPath("/reportes");
//
//        Date date = new Date();
//        SimpleDateFormat fhora = new SimpleDateFormat("HH:mm");
//        SimpleDateFormat sm = new SimpleDateFormat("yyy-MM-dd");
//        String strDate = sm.format(date);
//
//        String pathSalida = directorioReportes + File.separator + "vacante.xls";
//        System.out.println("Direccion del reporte  " + pathSalida);
//        try {
//            int j = 1;
//            File archivoXLS = new File(pathSalida);
//            if (archivoXLS.exists()) {
//                archivoXLS.delete();
//            }
//            archivoXLS.createNewFile();
//            FileOutputStream archivo = new FileOutputStream(archivoXLS);
//            HSSFWorkbook wb = new HSSFWorkbook();
//            HSSFSheet s = wb.createSheet("Autorizadas");
//
//            HSSFFont fuente = wb.createFont();
//            fuente.setBoldweight((short) 700);
//            HSSFCellStyle estiloCelda = wb.createCellStyle();
//            estiloCelda.setWrapText(true);
//            estiloCelda.setAlignment((short) 2);
//            estiloCelda.setFont(fuente);
//
//            HSSFCellStyle estiloCeldaInterna = wb.createCellStyle();
//            estiloCeldaInterna.setWrapText(true);
//            estiloCeldaInterna.setAlignment((short) 5);
//            estiloCeldaInterna.setFont(fuente);
//
//            HSSFCellStyle estiloCelda1 = wb.createCellStyle();
//            estiloCelda1.setWrapText(true);
//            estiloCelda1.setFont(fuente);
//
//            HSSFRow r = null;
//
//            HSSFCell c = null;
//            r = s.createRow(0);
//
//            HSSFCell chfe = r.createCell(0);
//            chfe.setCellValue(new HSSFRichTextString("Nomvre"));
//            chfe.setCellStyle(estiloCelda);
//
//            HSSFCell ch1 = r.createCell(j++);
//            ch1.setCellValue(new HSSFRichTextString("Descripcion"));
//            ch1.setCellStyle(estiloCelda);
//
//            HSSFCell ch2 = r.createCell(j++);
//            ch2.setCellValue(new HSSFRichTextString("Fecha inicio"));
//            ch2.setCellStyle(estiloCelda);
//
//            HSSFCell ch21 = r.createCell(j++);
//            ch21.setCellValue(new HSSFRichTextString("Fecha fin"));
//            ch21.setCellStyle(estiloCelda);
//
//            HSSFCell ch3 = r.createCell(j++);
//            ch3.setCellValue(new HSSFRichTextString("Sueldo"));
//            ch3.setCellStyle(estiloCelda);
//
//
//
//            int rownum = 1;
//            int i = 0;
//
//            for (Vacante item : listaVacante) {
//                i = 0;
//
//                r = s.createRow(rownum);
//
//                HSSFCell cf = r.createCell(i++);
//                cf.setCellValue(new HSSFRichTextString(item.getVacNombre().toString()));
//
//                HSSFCell c0 = r.createCell(i++);
//                c0.setCellValue(new HSSFRichTextString(item.getVacDescripcion()));
//
//                HSSFCell c1 = r.createCell(i++);
//                c1.setCellValue(new HSSFRichTextString(sm.format(item.getVacFechaInicio())));
//
//                HSSFCell c11 = r.createCell(i++);
//                c11.setCellValue(new HSSFRichTextString(sm.format(item.getVacFechaFin())));
//
//                HSSFCell c2 = r.createCell(i++);
//                c2.setCellValue(new HSSFRichTextString(item.getVacSueldo().toString()));
//
//             
//                rownum += 1;
//
//            }
//            for (int k = 0; k <= listaVacante.size(); k++) {
//                s.autoSizeColumn(k);
//            }
//            wb.write(archivo);
//            archivo.close();
//
//        } catch (IOException e) {
//            System.out.println("error " + e.getMessage());
//        }
//        return pathSalida;
//
//    }
 
    public Date getInicio() {
        return inicio;
    }

    public void setInicio(Date inicio) {
        this.inicio = inicio;
    }

    public Date getFin() {
        return fin;
    }

    public void setFin(Date fin) {
        this.fin = fin;
    }

    
    
    
    
}
