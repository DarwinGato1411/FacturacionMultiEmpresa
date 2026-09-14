package com.ec.untilitario;

import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.Barcode128;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class RidePdfDesdeXml {

    private static final Color NAVY = new Color(18, 37, 54);
    private static final Color LINE = new Color(210, 218, 226);
    private static final Font F_TITLE = new Font(Font.HELVETICA, 11, Font.BOLD, NAVY);
    private static final Font F_H = new Font(Font.HELVETICA, 9, Font.BOLD, NAVY);
    private static final Font F_B = new Font(Font.HELVETICA, 8, Font.NORMAL, Color.DARK_GRAY);
    private static final Font F_S = new Font(Font.HELVETICA, 7, Font.NORMAL, Color.DARK_GRAY);
    private static final Font F_TH = new Font(Font.HELVETICA, 7, Font.BOLD, Color.WHITE);

    public static String leerXml(File archivo) throws Exception {
        FileInputStream in = new FileInputStream(archivo);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buf = new byte[4096];
        int n;
        while ((n = in.read(buf)) >= 0) {
            out.write(buf, 0, n);
        }
        in.close();
        byte[] datos = out.toByteArray();
        String utf8 = new String(datos, Charset.forName("UTF-8"));
        if (utf8.indexOf('\ufffd') >= 0) {
            utf8 = new String(datos, Charset.forName("ISO-8859-1"));
        }
        utf8 = utf8.trim();
        if (utf8.startsWith("\uFEFF")) {
            utf8 = utf8.substring(1);
        }
        if (utf8.startsWith("<![CDATA[") && utf8.endsWith("]]>")) {
            utf8 = utf8.substring(9, utf8.length() - 3).trim();
        }
        return utf8;
    }

    public static String prettyXml(String xml) {
        try {
            org.w3c.dom.Document doc = parse(xml);
            Transformer tf = TransformerFactory.newInstance().newTransformer();
            tf.setOutputProperty(OutputKeys.INDENT, "yes");
            tf.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            tf.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            StringWriter sw = new StringWriter();
            tf.transform(new DOMSource(doc), new StreamResult(sw));
            return sw.toString();
        } catch (Exception e) {
            return xml;
        }
    }

    public static byte[] generar(String xml) throws Exception {
        org.w3c.dom.Document doc = parse(xml);
        org.w3c.dom.Element root = doc.getDocumentElement();
        String numAut = texto(root, "numeroAutorizacion");
        String fecAut = texto(root, "fechaAutorizacion");
        org.w3c.dom.Element comp = raizComprobante(doc);
        org.w3c.dom.Element trib = hijo(comp, "infoTributaria");
        org.w3c.dom.Element info = primeraInfo(comp);

        String razon = texto(trib, "razonSocial");
        String comercial = texto(trib, "nombreComercial");
        String ruc = texto(trib, "ruc");
        String clave = texto(trib, "claveAcceso");
        if (clave.isEmpty()) {
            clave = numAut;
        }
        if (numAut.isEmpty()) {
            numAut = clave;
        }
        String estab = texto(trib, "estab");
        String pto = texto(trib, "ptoEmi");
        String sec = texto(trib, "secuencial");
        String dirMatriz = texto(trib, "dirMatriz");
        String dirEstab = texto(info, "dirEstablecimiento");
        String fecha = texto(info, "fechaEmision");
        String obligado = texto(info, "obligadoContabilidad");
        String rimpe = texto(trib, "contribuyenteRimpe");
        String tipoDoc = nombreTipo(texto(trib, "codDoc"), comp.getNodeName());

        String receptorNom = primerTexto(info, new String[]{
            "razonSocialComprador", "razonSocialProveedor", "razonSocialSujetoRetenido",
            "razonSocialDestinatario"
        });
        String receptorId = primerTexto(info, new String[]{
            "identificacionComprador", "identificacionProveedor", "identificacionSujetoRetenido",
            "identificacionDestinatario", "rucTransportista", "identificacionTransportista"
        });
        String receptorDir = primerTexto(info, new String[]{
            "direccionComprador", "direccionProveedor", "dirDestinatario", "dirEstablecimiento"
        });
        org.w3c.dom.Element dest = primerTag(comp, "destinatario");
        if (receptorNom.isEmpty() && dest != null) {
            receptorNom = texto(dest, "razonSocialDestinatario");
            receptorId = texto(dest, "identificacionDestinatario");
            receptorDir = texto(dest, "dirDestinatario");
        }

        Document pdf = new Document(PageSize.A4, 28, 28, 24, 24);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter writer = PdfWriter.getInstance(pdf, baos);
        pdf.open();

        PdfPTable cabeza = new PdfPTable(new float[]{58, 42});
        cabeza.setWidthPercentage(100);

        Paragraph izq = new Paragraph();
        izq.add(new Phrase(razon + "\n", F_TITLE));
        if (!comercial.isEmpty() && !comercial.equalsIgnoreCase(razon)) {
            izq.add(new Phrase(comercial + "\n", F_H));
        }
        izq.add(new Phrase("RUC: " + ruc + "\n", F_B));
        izq.add(new Phrase("Dir. matriz: " + dirMatriz + "\n", F_S));
        if (!dirEstab.isEmpty()) {
            izq.add(new Phrase("Dir. establecimiento: " + dirEstab + "\n", F_S));
        }
        if (!obligado.isEmpty()) {
            izq.add(new Phrase("Obligado a llevar contabilidad: " + obligado + "\n", F_S));
        }
        if (!rimpe.isEmpty()) {
            izq.add(new Phrase(rimpe + "\n", F_S));
        }
        PdfPCell cIzq = cell(izq, Rectangle.BOX);
        cIzq.setPadding(8);
        cabeza.addCell(cIzq);

        Paragraph der = new Paragraph();
        der.add(new Phrase("RIDE\n", F_TITLE));
        der.add(new Phrase(tipoDoc + "\n", F_H));
        der.add(new Phrase("No. " + estab + "-" + pto + "-" + sec + "\n", F_B));
        der.add(new Phrase("Fecha emisión: " + fecha + "\n", F_B));
        der.add(new Phrase("Núm. autorización:\n", F_S));
        der.add(new Phrase(numAut + "\n", F_S));
        if (!fecAut.isEmpty()) {
            der.add(new Phrase("Fecha autorización: " + fecAut + "\n", F_S));
        }
        PdfPCell cDer = cell(der, Rectangle.BOX);
        cDer.setPadding(8);
        cabeza.addCell(cDer);
        pdf.add(cabeza);

        if (clave != null && clave.length() >= 8) {
            try {
                Barcode128 bc = new Barcode128();
                bc.setCode(clave);
                bc.setCodeType(Barcode128.CODE128);
                bc.setBarHeight(28f);
                bc.setX(0.7f);
                bc.setFont(null);
                Image img = bc.createImageWithBarcode(writer.getDirectContent(), NAVY, NAVY);
                img.scaleToFit(520, 42);
                img.setAlignment(Image.ALIGN_CENTER);
                pdf.add(Chunk.NEWLINE);
                pdf.add(img);
                Paragraph cl = new Paragraph(clave, F_S);
                cl.setAlignment(Element.ALIGN_CENTER);
                pdf.add(cl);
            } catch (Exception ignored) {
            }
        }

        pdf.add(espacio(6));
        PdfPTable rec = new PdfPTable(1);
        rec.setWidthPercentage(100);
        Paragraph rp = new Paragraph();
        rp.add(new Phrase("Receptor\n", F_H));
        rp.add(new Phrase("Razón social: " + receptorNom + "\n", F_B));
        rp.add(new Phrase("Identificación: " + receptorId + "\n", F_B));
        if (!receptorDir.isEmpty() && !receptorDir.equals(dirEstab)) {
            rp.add(new Phrase("Dirección: " + receptorDir + "\n", F_S));
        }
        rec.addCell(cell(rp, Rectangle.BOX));
        pdf.add(rec);

        String extra = extraPorTipo(comp, info, dest);
        if (!extra.isEmpty()) {
            pdf.add(espacio(6));
            PdfPTable ex = new PdfPTable(1);
            ex.setWidthPercentage(100);
            Paragraph ep = new Paragraph();
            ep.add(new Phrase("Datos del comprobante\n", F_H));
            ep.add(new Phrase(extra, F_S));
            ex.addCell(cell(ep, Rectangle.BOX));
            pdf.add(ex);
        }

        List<String[]> filas = detalles(comp);
        if (!filas.isEmpty()) {
            pdf.add(espacio(6));
            boolean retencion = "comprobanteRetencion".equals(comp.getNodeName());
            PdfPTable det = retencion
                    ? new PdfPTable(new float[]{18, 22, 18, 14, 14, 14})
                    : new PdfPTable(new float[]{16, 36, 10, 12, 12, 14});
            det.setWidthPercentage(100);
            String[] heads = retencion
                    ? new String[]{"Código", "Impuesto", "Base", "%", "Valor", "Doc. sustento"}
                    : new String[]{"Código", "Descripción", "Cant.", "P. unit.", "Desc.", "Total"};
            for (int i = 0; i < heads.length; i++) {
                PdfPCell h = new PdfPCell(new Phrase(heads[i], F_TH));
                h.setBackgroundColor(NAVY);
                h.setPadding(4);
                h.setHorizontalAlignment(i >= 2 ? Element.ALIGN_RIGHT : Element.ALIGN_LEFT);
                det.addCell(h);
            }
            for (String[] f : filas) {
                for (int i = 0; i < 6; i++) {
                    PdfPCell d = new PdfPCell(new Phrase(f[i], F_S));
                    d.setPadding(3);
                    d.setBorderColor(LINE);
                    d.setHorizontalAlignment(i >= 2 ? Element.ALIGN_RIGHT : Element.ALIGN_LEFT);
                    det.addCell(d);
                }
            }
            pdf.add(det);
        }

        pdf.add(espacio(6));
        PdfPTable tot = new PdfPTable(new float[]{70, 30});
        tot.setWidthPercentage(100);
        tot.addCell(bordeCero(new Phrase(" ", F_S)));
        PdfPTable nums = new PdfPTable(2);
        addTot(nums, "Subtotal sin impuestos", texto(info, "totalSinImpuestos"));
        addTot(nums, "Descuento", texto(info, "totalDescuento"));
        NodeList imps = info == null ? null : info.getElementsByTagName("totalImpuesto");
        if (imps != null) {
            for (int i = 0; i < imps.getLength(); i++) {
                org.w3c.dom.Element ti = (org.w3c.dom.Element) imps.item(i);
                String tarifa = texto(ti, "tarifa");
                String valor = texto(ti, "valor");
                String nom = "IVA " + tarifa + "%";
                if ("3".equals(texto(ti, "codigo"))) {
                    nom = "ICE";
                }
                addTot(nums, nom, valor);
            }
        }
        addTot(nums, "Importe total", primerTexto(info, new String[]{
            "importeTotal", "valorModificacion", "valorTotal"
        }));
        if ("comprobanteRetencion".equals(local(comp.getNodeName()))) {
            addTot(nums, "Total retenido", sumarTags(comp, "valorRetenido"));
        }
        if (nums.getRows() != null && !nums.getRows().isEmpty()) {
            PdfPCell numsCell = new PdfPCell(nums);
            numsCell.setBorder(Rectangle.BOX);
            numsCell.setPadding(4);
            tot.addCell(numsCell);
            pdf.add(tot);
        }

        String infoAd = infosAdicionales(comp);
        if (!infoAd.isEmpty()) {
            pdf.add(espacio(6));
            Paragraph ad = new Paragraph("Información adicional\n", F_H);
            ad.add(new Phrase(infoAd, F_S));
            pdf.add(ad);
        }

        Paragraph pie = new Paragraph(
                "\nRepresentación impresa de documento electrónico autorizado por el SRI.", F_S);
        pie.setAlignment(Element.ALIGN_CENTER);
        pdf.add(pie);
        pdf.close();
        return baos.toByteArray();
    }

    private static org.w3c.dom.Document parse(String xml) throws Exception {
        DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
        f.setNamespaceAware(false);
        f.setExpandEntityReferences(false);
        try {
            f.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
        } catch (Exception ignored) {
        }
        return f.newDocumentBuilder().parse(new InputSource(new StringReader(xml)));
    }

    private static org.w3c.dom.Element raizComprobante(org.w3c.dom.Document doc) throws Exception {
        String[] tags = {"factura", "liquidacionCompra", "notaCredito", "notaDebito",
            "guiaRemision", "comprobanteRetencion"};
        org.w3c.dom.Element root = doc.getDocumentElement();
        String nombre = local(root.getNodeName());
        for (int i = 0; i < tags.length; i++) {
            if (tags[i].equalsIgnoreCase(nombre)) {
                return root;
            }
        }
        for (int i = 0; i < tags.length; i++) {
            NodeList nl = doc.getElementsByTagName(tags[i]);
            if (nl.getLength() > 0) {
                return (org.w3c.dom.Element) nl.item(0);
            }
        }
        NodeList c = doc.getElementsByTagName("comprobante");
        if (c.getLength() > 0) {
            String inner = c.item(0).getTextContent();
            if (inner != null && inner.contains("<")) {
                return parse(inner.trim()).getDocumentElement();
            }
        }
        return root;
    }

    private static org.w3c.dom.Element primeraInfo(org.w3c.dom.Element comp) {
        String[] tags = {"infoFactura", "infoLiquidacionCompra", "infoNotaCredito",
            "infoNotaDebito", "infoGuiaRemision", "infoCompRetencion"};
        for (int i = 0; i < tags.length; i++) {
            org.w3c.dom.Element e = hijo(comp, tags[i]);
            if (e != null) {
                return e;
            }
        }
        return comp;
    }

    private static List<String[]> detalles(org.w3c.dom.Element comp) {
        List<String[]> out = new ArrayList<String[]>();
        NodeList dets = comp.getElementsByTagName("detalle");
        for (int i = 0; i < dets.getLength(); i++) {
            org.w3c.dom.Element d = (org.w3c.dom.Element) dets.item(i);
            String[] f = new String[6];
            f[0] = primerTexto(d, new String[]{"codigoPrincipal", "codigoInterno", "codigoAdicional", "codigo"});
            f[1] = primerTexto(d, new String[]{"descripcion", "detalle"});
            f[2] = texto(d, "cantidad");
            f[3] = primerTexto(d, new String[]{"precioUnitario", "precio"});
            f[4] = texto(d, "descuento");
            f[5] = primerTexto(d, new String[]{"precioTotalSinImpuesto", "valor"});
            out.add(f);
        }
        NodeList imps = comp.getElementsByTagName("impuesto");
        if (out.isEmpty() && imps.getLength() > 0) {
            for (int i = 0; i < imps.getLength(); i++) {
                org.w3c.dom.Element d = (org.w3c.dom.Element) imps.item(i);
                String[] f = new String[6];
                f[0] = texto(d, "codigo");
                f[1] = nombreImpuesto(texto(d, "codigo"));
                f[2] = texto(d, "baseImponible");
                f[3] = texto(d, "porcentajeRetener");
                f[4] = texto(d, "valorRetenido");
                f[5] = texto(d, "numDocSustento");
                out.add(f);
            }
        }
        NodeList mots = comp.getElementsByTagName("motivo");
        if (out.isEmpty() && mots.getLength() > 0) {
            for (int i = 0; i < mots.getLength(); i++) {
                org.w3c.dom.Element d = (org.w3c.dom.Element) mots.item(i);
                String[] f = new String[6];
                f[0] = String.valueOf(i + 1);
                f[1] = primerTexto(d, new String[]{"razon", "motivo"});
                f[2] = "";
                f[3] = "";
                f[4] = "";
                f[5] = texto(d, "valor");
                if (!f[1].isEmpty() || !f[5].isEmpty()) {
                    out.add(f);
                }
            }
        }
        return out;
    }

    private static String extraPorTipo(org.w3c.dom.Element comp, org.w3c.dom.Element info,
            org.w3c.dom.Element dest) {
        StringBuilder sb = new StringBuilder();
        String tag = local(comp.getNodeName());
        if ("notaCredito".equals(tag) || "notaDebito".equals(tag)) {
            linea(sb, "Doc. modificado",
                    (texto(info, "codDocModificado") + " " + texto(info, "numDocModificado")).trim());
            linea(sb, "Fecha doc. sustento", texto(info, "fechaEmisionDocSustento"));
            linea(sb, "Motivo", primerTexto(info, new String[]{"motivo", "rise"}));
        }
        if ("guiaRemision".equals(tag)) {
            linea(sb, "Transportista", texto(info, "razonSocialTransportista"));
            linea(sb, "Ident. transportista", primerTexto(info, new String[]{
                "rucTransportista", "identificacionTransportista"
            }));
            linea(sb, "Placa", texto(info, "placa"));
            linea(sb, "Inicio transporte", texto(info, "fechaIniTransporte"));
            linea(sb, "Fin transporte", texto(info, "fechaFinTransporte"));
            linea(sb, "Dir. partida", texto(info, "dirPartida"));
            if (dest != null) {
                linea(sb, "Motivo traslado", texto(dest, "motivoTraslado"));
                linea(sb, "Doc. aduanero", texto(dest, "docAduaneroUnico"));
                linea(sb, "Ruta", texto(dest, "ruta"));
                linea(sb, "Cod. establecimiento destino", texto(dest, "codEstabDestino"));
            }
        }
        if ("comprobanteRetencion".equals(tag)) {
            linea(sb, "Periodo fiscal", texto(info, "periodoFiscal"));
        }
        if ("liquidacionCompra".equals(tag)) {
            linea(sb, "Proveedor", texto(info, "razonSocialProveedor"));
            linea(sb, "Identificación proveedor", texto(info, "identificacionProveedor"));
        }
        return sb.toString().trim();
    }

    private static void linea(StringBuilder sb, String etq, String val) {
        if (val == null || val.trim().isEmpty()) {
            return;
        }
        sb.append(etq).append(": ").append(val.trim()).append('\n');
    }

    private static org.w3c.dom.Element primerTag(org.w3c.dom.Element p, String tag) {
        if (p == null) {
            return null;
        }
        NodeList nl = p.getElementsByTagName(tag);
        return nl.getLength() > 0 ? (org.w3c.dom.Element) nl.item(0) : null;
    }

    private static String sumarTags(org.w3c.dom.Element p, String tag) {
        NodeList nl = p.getElementsByTagName(tag);
        double t = 0;
        boolean hay = false;
        for (int i = 0; i < nl.getLength(); i++) {
            String v = nl.item(i).getTextContent();
            if (v == null || v.trim().isEmpty()) {
                continue;
            }
            try {
                t += Double.parseDouble(v.trim().replace(",", "."));
                hay = true;
            } catch (NumberFormatException ignored) {
            }
        }
        return hay ? String.format(java.util.Locale.US, "%.2f", t) : "";
    }

    private static String infosAdicionales(org.w3c.dom.Element comp) {
        StringBuilder sb = new StringBuilder();
        NodeList nl = comp.getElementsByTagName("campoAdicional");
        for (int i = 0; i < nl.getLength(); i++) {
            org.w3c.dom.Element e = (org.w3c.dom.Element) nl.item(i);
            String nom = e.getAttribute("nombre");
            sb.append(nom).append(": ").append(e.getTextContent().trim()).append('\n');
        }
        return sb.toString().trim();
    }

    private static void addTot(PdfPTable t, String etq, String val) {
        if (val == null || val.trim().isEmpty()) {
            return;
        }
        PdfPCell a = new PdfPCell(new Phrase(etq, F_S));
        a.setBorder(Rectangle.NO_BORDER);
        a.setPadding(2);
        PdfPCell b = new PdfPCell(new Phrase(val, F_H));
        b.setBorder(Rectangle.NO_BORDER);
        b.setHorizontalAlignment(Element.ALIGN_RIGHT);
        b.setPadding(2);
        t.addCell(a);
        t.addCell(b);
    }

    private static PdfPCell cell(Phrase p, int border) {
        PdfPCell c = new PdfPCell(p);
        c.setBorder(border);
        c.setBorderColor(LINE);
        c.setPadding(6);
        return c;
    }

    private static PdfPCell bordeCero(Phrase p) {
        PdfPCell c = new PdfPCell(p);
        c.setBorder(Rectangle.NO_BORDER);
        return c;
    }

    private static Paragraph espacio(int pt) {
        Paragraph p = new Paragraph(" ");
        p.setSpacingBefore(pt);
        p.setSpacingAfter(0);
        p.setFont(new Font(Font.HELVETICA, 4));
        return p;
    }

    private static org.w3c.dom.Element hijo(org.w3c.dom.Element p, String tag) {
        if (p == null) {
            return null;
        }
        Node n = p.getFirstChild();
        while (n != null) {
            if (n.getNodeType() == Node.ELEMENT_NODE && tag.equals(local(n.getNodeName()))) {
                return (org.w3c.dom.Element) n;
            }
            n = n.getNextSibling();
        }
        NodeList nl = p.getElementsByTagName(tag);
        return nl.getLength() > 0 ? (org.w3c.dom.Element) nl.item(0) : null;
    }

    private static String texto(org.w3c.dom.Element p, String tag) {
        org.w3c.dom.Element e = hijo(p, tag);
        return e == null ? "" : e.getTextContent().trim();
    }

    private static String primerTexto(org.w3c.dom.Element p, String[] tags) {
        if (p == null) {
            return "";
        }
        for (int i = 0; i < tags.length; i++) {
            String v = texto(p, tags[i]);
            if (!v.isEmpty()) {
                return v;
            }
        }
        return "";
    }

    private static String local(String name) {
        int i = name.indexOf(':');
        return i >= 0 ? name.substring(i + 1) : name;
    }

    private static String nombreTipo(String cod, String tag) {
        if ("01".equals(cod) || "factura".equals(tag)) {
            return "FACTURA";
        }
        if ("03".equals(cod) || "liquidacionCompra".equals(tag)) {
            return "LIQUIDACIÓN DE COMPRA";
        }
        if ("04".equals(cod) || "notaCredito".equals(tag)) {
            return "NOTA DE CRÉDITO";
        }
        if ("05".equals(cod) || "notaDebito".equals(tag)) {
            return "NOTA DE DÉBITO";
        }
        if ("06".equals(cod) || "guiaRemision".equals(tag)) {
            return "GUÍA DE REMISIÓN";
        }
        if ("07".equals(cod) || "comprobanteRetencion".equals(tag)) {
            return "COMPROBANTE DE RETENCIÓN";
        }
        return "COMPROBANTE ELECTRÓNICO";
    }

    private static String nombreImpuesto(String codigo) {
        if ("1".equals(codigo)) {
            return "Renta";
        }
        if ("2".equals(codigo)) {
            return "IVA";
        }
        if ("6".equals(codigo)) {
            return "ISD";
        }
        return codigo;
    }

    public static File archivoXml(String ruta) {
        if (ruta == null || ruta.trim().isEmpty()) {
            return null;
        }
        File f = new File(ruta);
        return f.isFile() ? f : null;
    }

    public static String[] encabezadoExcelDetallado() {
        return new String[]{
            "Tipo", "RUC emisor", "Razón social", "Número", "Fecha emisión", "Clave acceso",
            "Ident. receptor", "Nombre receptor", "Importe",
            "Código", "Descripción", "Cantidad", "P. unitario", "Descuento", "Total línea"
        };
    }

    public static List<String[]> filasExcelDetallado(String xml) throws Exception {
        org.w3c.dom.Document doc = parse(xml);
        org.w3c.dom.Element comp = raizComprobante(doc);
        org.w3c.dom.Element trib = hijo(comp, "infoTributaria");
        org.w3c.dom.Element info = primeraInfo(comp);
        org.w3c.dom.Element dest = primerTag(comp, "destinatario");
        String receptorNom = primerTexto(info, new String[]{
            "razonSocialComprador", "razonSocialProveedor", "razonSocialSujetoRetenido",
            "razonSocialDestinatario"
        });
        String receptorId = primerTexto(info, new String[]{
            "identificacionComprador", "identificacionProveedor", "identificacionSujetoRetenido",
            "identificacionDestinatario"
        });
        if (receptorNom.isEmpty() && dest != null) {
            receptorNom = texto(dest, "razonSocialDestinatario");
            receptorId = texto(dest, "identificacionDestinatario");
        }
        String importe = primerTexto(info, new String[]{"importeTotal", "valorModificacion", "valorTotal"});
        if (importe.isEmpty() && "comprobanteRetencion".equals(local(comp.getNodeName()))) {
            importe = sumarTags(comp, "valorRetenido");
        }
        String[] cab = new String[]{
            nombreTipo(texto(trib, "codDoc"), comp.getNodeName()),
            texto(trib, "ruc"),
            texto(trib, "razonSocial"),
            texto(trib, "estab") + "-" + texto(trib, "ptoEmi") + "-" + texto(trib, "secuencial"),
            texto(info, "fechaEmision"),
            texto(trib, "claveAcceso"),
            receptorId,
            receptorNom,
            importe
        };
        List<String[]> dets = detalles(comp);
        List<String[]> out = new ArrayList<String[]>();
        if (dets.isEmpty()) {
            out.add(unir(cab, new String[]{"", "", "", "", "", ""}));
        } else {
            for (int i = 0; i < dets.size(); i++) {
                out.add(unir(cab, dets.get(i)));
            }
        }
        return out;
    }

    private static String[] unir(String[] a, String[] b) {
        String[] r = new String[a.length + b.length];
        System.arraycopy(a, 0, r, 0, a.length);
        System.arraycopy(b, 0, r, a.length, b.length);
        return r;
    }
}
