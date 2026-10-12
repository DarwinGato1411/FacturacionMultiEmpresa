package com.ec.untilitario;

import com.ec.entidad.Tipoambiente;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfWriter;
import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Date;

/** Ticket PDF 80 mm para cobro Deuna. */
public class TicketDeunaPdf {

    private static final Color NAVY = new Color(18, 37, 54);
    private static final Font F_EMP = new Font(Font.HELVETICA, 9, Font.BOLD, NAVY);
    private static final Font F_N = new Font(Font.HELVETICA, 8, Font.NORMAL, NAVY);
    private static final Font F_T = new Font(Font.HELVETICA, 14, Font.BOLD, NAVY);

    public static byte[] generar(Tipoambiente amb, ResultadoQrDeuna r) throws Exception {
        float ancho = 226.77f;
        Rectangle hoja = new Rectangle(ancho, 420f);
        Document doc = new Document(hoja, 10, 10, 12, 12);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter.getInstance(doc, baos);
        doc.open();

        String empresa = "";
        String ruc = "";
        String caja = "";
        if (amb != null) {
            empresa = texto(amb.getAmNombreComercial());
            if (empresa.isEmpty()) {
                empresa = texto(amb.getAmRazonSocial());
            }
            ruc = texto(amb.getAmRuc());
            caja = texto(amb.getAmDeunaCaja());
        }
        String monto = "0.00";
        String orden = "";
        if (r != null) {
            BigDecimal m = r.getMonto() == null ? BigDecimal.ZERO : r.getMonto().setScale(2, RoundingMode.HALF_UP);
            monto = m.toPlainString();
            orden = texto(r.getPoId());
        }
        String fecha = new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date());

        doc.add(centro(empresa, F_EMP));
        doc.add(centro("RUC " + ruc, F_N));
        doc.add(centro("Cobro Deuna", F_N));
        doc.add(centro("$ " + monto, F_T));

        if (r != null && r.getImagen() != null) {
            Image qr = Image.getInstance(r.getImagen());
            qr.scaleToFit(164f, 164f);
            qr.setAlignment(Element.ALIGN_CENTER);
            doc.add(qr);
        }

        doc.add(centro("Orden " + orden, F_N));
        if (!caja.isEmpty()) {
            doc.add(centro("Caja " + caja, F_N));
        }
        doc.add(centro(fecha, F_N));
        doc.add(centro("Escanee con la app Deuna", F_N));

        doc.close();
        return baos.toByteArray();
    }

    private static Paragraph centro(String texto, Font font) {
        Paragraph p = new Paragraph(texto == null ? "" : texto, font);
        p.setAlignment(Element.ALIGN_CENTER);
        p.setSpacingAfter(3f);
        return p;
    }

    private static String texto(String s) {
        return s == null ? "" : s.trim();
    }
}
