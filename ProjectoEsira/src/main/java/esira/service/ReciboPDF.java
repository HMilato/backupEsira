/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package esira.service;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfWriter;
import esira.domain.Arquivoinscricao;
import esira.domain.Curso;
import esira.domain.Estudante;
import esira.domain.Inscricao;
import esira.domain.Inscricaodisciplina;
import esira.domain.Matricula;
import esira.domain.MatriculaPK;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.activation.DataSource;
import javax.mail.util.ByteArrayDataSource;
import javax.servlet.ServletContext;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import org.zkoss.util.media.AMedia;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zul.Iframe;
import org.zkoss.zul.Window;

/**
 *
 * @author Ussimane
 */
public class ReciboPDF {

    public DataSource reciboMatricula(String img, Matricula m,CRUDService csimp,ServletContext sct) throws DocumentException, BadElementException, IOException, JRException {
        Map<String, Object> par = new HashMap<String, Object>();
//        Document document = new Document();
// 
//        Image figura = Image.getInstance(img);
//
//        ByteArrayOutputStream stream = new ByteArrayOutputStream();
//        PdfWriter.getInstance(document, stream);
//
//        document.open();
//        document.add(figura);
//        document.addAuthor("UniLurio");
//        document.add(new Paragraph("Recibo de Matricula"));
//        document.add(new Paragraph(""));
//
//        BaseColor black = new BaseColor(1, 1, 1);
//        Font fontNormal = FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.NORMAL, black);
//        Font fontNegrito = FontFactory.getFont(FontFactory.COURIER, 10, Font.BOLD, black);
//
//        Paragraph texto = new Paragraph();
//        Phrase frase = new Phrase("Nome: ", fontNegrito);
//        par.clear();
//        par.put("mpk", m);
//        Estudante e = csimp.findEntByJPQuery("select m.estudante from Matricula m where m = :mpk", par);
//        frase.add(new Chunk(e.getNomeCompleto(), fontNormal));
//        texto.add(frase);
//        document.add(texto);
//
//        texto = new Paragraph();
//        frase = new Phrase("Curso: ", fontNegrito);
//        Curso c = csimp.findEntByJPQuery("select m.curso from Matricula m where m = :mpk", par);
//        frase.add(new Chunk(c.getDescricao(), fontNormal));
//        texto.add(frase);
//        document.add(texto);
//
//        texto = new Paragraph();
//        frase = new Phrase("Taxa de Matricula: ", fontNegrito);
//        frase.add(new Chunk(m.getValor().toString() + " ", fontNormal));if(m.getMulta()!=null){
//        frase.add(new Chunk("Multa: ", fontNegrito));
//        frase.add(new Chunk(m.getMulta().intValue() + "%", fontNormal));}
//        texto.add(frase);
//        document.add(texto);
//
//        texto = new Paragraph();
//        frase = new Phrase("Data de Matricula, ", fontNegrito);
//        frase.add(new Chunk(new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm").format(m.getDataMatricula()), fontNormal));
//        texto.add(frase);
//        document.add(texto);
//
////        document.close();
//        DataSource source = new ByteArrayDataSource(stream.toByteArray(), "application/pdf");   
//        return source;
        m = csimp.load(Matricula.class, m.getMatriculaPK());
        String path = sct.getRealPath("/relatorio");
        String path2 = sct.getRealPath("/img");
        List<Matricula> lm = new ArrayList<Matricula>();
        lm.add(m);
        JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(lm);//((ListModelList) lbinscdisc.getListModel()).getInnerList());
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("fac",m.getEstudante().getCursocurrente().getFaculdade().getDesricao());
        params.put("img", path2 + "/icoul.png");
        JasperPrint jasperPrint = JasperFillManager.fillReport(path + "/matricula.jasper", params, ds);
        JRPdfExporter exporter = new JRPdfExporter();
        ByteArrayOutputStream bytesOutputStream = new ByteArrayOutputStream();
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(bytesOutputStream);
        exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
        exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, bufferedOutputStream);
        exporter.exportReport();
        DataSource source = new ByteArrayDataSource(bytesOutputStream.toByteArray(), "application/pdf");
        return source;
    }

    public DataSource reciboInnscricao(String img, Inscricao i,List<Inscricaodisciplina> li,CRUDService csimp,ServletContext sct) throws DocumentException, BadElementException, IOException, JRException {
        Map<String, Object> par = new HashMap<String, Object>();
//        Document document = new Document();

        //   PdfWriter.getInstance(document, new FileOutputStream("C:\\PDF_DevMedia.pdf"));
//        Image figura = Image.getInstance(img);
//
//        ByteArrayOutputStream stream = new ByteArrayOutputStream();
//        PdfWriter.getInstance(document, stream);
//
//        document.open(); // adicionando um parágrafo no documento 
//        document.add(figura);
//        document.addAuthor("UniLurio");
//        document.add(new Paragraph("Recibo de Matricula"));
//        document.add(new Paragraph(""));
//
//        BaseColor black = new BaseColor(1, 1, 1);
//        Font fontNormal = FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.NORMAL, black);
//        Font fontNegrito = FontFactory.getFont(FontFactory.COURIER, 10, Font.BOLD, black);
//
//        Paragraph texto = new Paragraph();
//        Phrase frase = new Phrase("Nome: ", fontNegrito);
//        frase.add(new Chunk(i.getIdEstudante().getNomeCompleto(), fontNormal));
//        texto.add(frase);
//        document.add(texto);
//
//        texto = new Paragraph();
//        frase = new Phrase("Curso: ", fontNegrito);
//        frase.add(new Chunk(i.getIdEstudante().getCursocurrente().getDescricao(), fontNormal));
//        texto.add(frase);
//        document.add(texto);
//        texto = new Paragraph("Disciplinas:",fontNegrito);
//        document.add(texto);
//        texto = new Paragraph();
//        for(Inscricaodisciplina id : li){
//           document.add(new Paragraph(id.getDisciplina().getNome(),fontNegrito));
//        }
//
//
//        texto = new Paragraph();
//        frase = new Phrase("Taxa de Inscricao: ", fontNegrito);
//        frase.add(new Chunk(i.getTaxaInscricao().toString() + " ", fontNormal));
//        if(i.getMulta()!=null){
//        frase.add(new Chunk("Multa: ", fontNegrito));
//        frase.add(new Chunk(i.getMulta().intValue() + "%", fontNormal));}
//        texto.add(frase);
//        document.add(texto);
//
//        texto = new Paragraph();
//        frase = new Phrase("Data de Inscrição, ", fontNegrito);
//        frase.add(new Chunk(new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm").format(i.getDataInscricao()), fontNormal));
//        texto.add(frase);
//        document.add(texto);
//        ////
////        texto = new Paragraph();
////        frase = new Phrase("Data de Confirmação, ", fontNegrito);
////        frase.add(new Chunk(new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm").format(i.getDataConfirmacao()), fontNormal));
////        texto.add(frase);
////        document.add(texto);
//
//        document.close();
//        DataSource source = new ByteArrayDataSource(stream.toByteArray(), "application/pdf");
//        return source;
        i = csimp.load(Inscricao.class, i.getIdInscricao());
        par.clear();
        par.put("i", i);
        List<Inscricaodisciplina> lid = csimp.findByJPQuery("from Inscricaodisciplina id where id.inscricao = :i and id.estado is true", par);
        List<Arquivoinscricao> la = i.getArquivoinscricaoList();
        String path = sct.getRealPath("/relatorio");
        String path2 = sct.getRealPath("/img");
//        Window win = (Window) Executions.createComponents("/report.zul", null, null);
//        win.setTitle(i.getIdEstudante().getNomeCompleto());
        JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(lid);//((ListModelList) lbinscdisc.getListModel()).getInnerList());
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("img", path2 + "/icoul.png");
        params.put("fac", i.getIdEstudante().getCursocurrente().getFaculdade().getDesricao());
        if (la!=null&&!la.isEmpty()&&la.get(0).getDatadeposito() != null) {
            params.put("nrrec", la.get(0).getNrtalao());
            params.put("valrec", la.get(0).getValor());
            params.put("banco", la.get(0).getBanco());
        } else {
            params.put("nrrec", null);
            params.put("valrec", null);
            params.put("banco", null);
        }
        if (la!=null&&!la.isEmpty()&&la.get(0).getDatadeposito() != null) {
            params.put("data", new java.text.SimpleDateFormat("dd/MM/yyyy").format(la.get(0).getDatadeposito()));
        } else {
            params.put("data", null);
        }
        JasperPrint jasperPrint = JasperFillManager.fillReport(path + "/inscricao.jasper", params, ds);
        JRPdfExporter exporter = new JRPdfExporter();
        ByteArrayOutputStream bytesOutputStream = new ByteArrayOutputStream();
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(bytesOutputStream);
        exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
        exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, bufferedOutputStream);
        exporter.exportReport();
        DataSource source = new ByteArrayDataSource(bytesOutputStream.toByteArray(), "application/pdf");
        return source;
    }

//    public URL reciboInscricao(Inscricao i){
//    
//    }
//    
//     List<Media> lm2 = new ArrayList<Media>();
//        for (Arquivoestudante am2 : lam2) {
//            String sss = UserAutentic.getPathR(am2.getTipo().toString()) + estudante.getNrEstudante() + am2.getNomearquivo();
//            //File f = new File(ss);
//            InputStream iss = new FileInputStream(sss);
//            Media pp = new AMedia(sss, FilenameUtils.getExtension(sss), URLConnection.guessContentTypeFromName(sss), iss);
//            Media mm = new AMedia(am2.getNomearquivo(), FilenameUtils.getExtension(sss), URLConnection.guessContentTypeFromName(sss), pp.getByteData());
//            //Image ii = new AImage(sss);
//            //((org.zkoss.zul.Image) win.getFellow("picss").getChildren().get(0)).setContent(i);
//            ((Textbox) win.getFellow("tbimg3")).setValue(am2.getNomearquivo());
//            lm2.add(mm);
//            //   iss.close();
//        }
    
    public DataSource reciboMatricula1(String img, Matricula m ) throws DocumentException, BadElementException, IOException, JRException {
        Map<String, Object> par = new HashMap<String, Object>();
        Document document = new Document();
        esira.service.CRUDService csimp = (esira.service.CRUDService) org.zkoss.zkplus.spring.SpringUtil.getBean("CRUDService");
        m = csimp.get(Matricula.class, new MatriculaPK(new Long(217), 2017));
        Image figura = Image.getInstance(img+"/icoul.png");

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, stream);

        document.open();
        document.add(figura);
        document.addAuthor("UniLurio");
        document.add(new Paragraph("Recibo de Matricula"));
        document.add(new Paragraph(""));

        BaseColor black = new BaseColor(1, 1, 1);
        Font fontNormal = FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.NORMAL, black);
        Font fontNegrito = FontFactory.getFont(FontFactory.COURIER, 10, Font.BOLD, black);

        Paragraph texto = new Paragraph();
        Phrase frase = new Phrase("Nome: ", fontNegrito);
        par.clear();
        par.put("mpk", m);
        Estudante e = csimp.findEntByJPQuery("select m.estudante from Matricula m where m = :mpk", par);
        frase.add(new Chunk(e.getNomeCompleto(), fontNormal));
        texto.add(frase);
        document.add(texto);

        texto = new Paragraph();
        frase = new Phrase("Curso: ", fontNegrito);
        Curso c = csimp.findEntByJPQuery("select m.curso from Matricula m where m = :mpk", par);
        frase.add(new Chunk(c.getDescricao(), fontNormal));
        texto.add(frase);
        document.add(texto);

        texto = new Paragraph();
        frase = new Phrase("Taxa de Matricula: ", fontNegrito);
        frase.add(new Chunk(m.getValor().toString() + " ", fontNormal));if(m.getMulta()!=null){
        frase.add(new Chunk("Multa: ", fontNegrito));
        frase.add(new Chunk(m.getMulta().intValue() + "%", fontNormal));}
        texto.add(frase);
        document.add(texto);

        texto = new Paragraph();
        frase = new Phrase("Data de Matricula, ", fontNegrito);
        frase.add(new Chunk(new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm").format(m.getDataMatricula()), fontNormal));
        texto.add(frase);
        document.add(texto);

//        document.close();
        DataSource source = new ByteArrayDataSource(stream.toByteArray(), "application/pdf");   
        return source;
         
    }
}
