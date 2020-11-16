/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package esira.matricula;

import esira.domain.Curso;
import esira.domain.Estudante;
import esira.domain.Matricula;
import esira.domain.Matriculaanulada;
import esira.domain.MatriculaanuladaPK;
import esira.service.CRUDService;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import org.zkoss.bind.annotation.Init;
import org.zkoss.util.media.AMedia;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.InputEvent;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Iframe;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

/**
 *
 * @author DataCenter
 */
public class ListaMAnuladasController extends GenericForwardComposer {
    
    @WireVariable
    private CRUDService csimpm = (CRUDService) SpringUtil.getBean("CRUDService");
    Window winAnulados;
    private Listbox lbAnulcao;
    private Textbox txProcuraNome, txProcuraNrmec;
    private Combobox cbcurso;
    private ListModel<Curso> cursoModel;
    private ListModel<Matriculaanulada> listManuladaModel;
    List listaM;
    
    
    @Init
    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        
        

    }

    public ListModel<Curso> getCursoModel() {
        return cursoModel =new ListModelList<Curso>(csimpm.getAll(Curso.class));
    }

    public ListModel<Matriculaanulada> getListManuladaModel() {
        listaM=csimpm.findByJPQuery("from Matriculaanulada", null);
        
        return listManuladaModel=new ListModelList<Matriculaanulada>(listaM);
    }

    public void onChanging$txProcuraNome(InputEvent event) throws Exception {
        List<Matriculaanulada> l = new ArrayList<Matriculaanulada>(listaM);

        List<Matriculaanulada> tempList = new ArrayList<Matriculaanulada>();

        if (cbcurso.getSelectedItem() == null) {
            if (txProcuraNrmec.getValue().isEmpty()) {
                for (Matriculaanulada m : l) {
                    if (m.getMatricula().getEstudante().getNomeCompleto().regionMatches(true, 0, event.getValue(), 0, event.getValue().length())) {
                        tempList.add(m);
                    }
                }
            } else {
                for (Matriculaanulada m : l) {
                    if (m.getMatricula().getEstudante().getNomeCompleto().regionMatches(true, 0, event.getValue(), 0, event.getValue().length())
                            && m.getMatricula().getEstudante().getNrEstudante().regionMatches(true, 0, txProcuraNrmec.getValue(), 0, txProcuraNrmec.getValue().length())) {
                        tempList.add(m);
                    }
                }
            }

        } else {
            if (txProcuraNrmec.getValue().isEmpty()) {
                for (Matriculaanulada m : l) {
                    if (m.getMatricula().getEstudante().getNomeCompleto().regionMatches(true, 0, event.getValue(), 0, event.getValue().length())
                            && m.getMatricula().getEstudante().getCursocurrente() == cbcurso.getSelectedItem().getValue()) {
                        tempList.add(m);
                    }
                }
            } else {
                for (Matriculaanulada m : l) {
                    if (m.getMatricula().getEstudante().getNomeCompleto().regionMatches(true, 0, event.getValue(), 0, event.getValue().length())
                            && m.getMatricula().getEstudante().getNrEstudante().regionMatches(true, 0, txProcuraNrmec.getValue(), 0, txProcuraNrmec.getValue().length())
                            && m.getMatricula().getEstudante().getCursocurrente() == cbcurso.getSelectedItem().getValue()) {
                        tempList.add(m);
                    }
                }
            }


        }
        ListModelList<Matriculaanulada> model = new ListModelList<Matriculaanulada>(tempList);
        lbAnulcao.setModel(model);

    }

    public void onChanging$txProcuraNrmec(InputEvent event) throws Exception {
        List<Matriculaanulada> l = new ArrayList<Matriculaanulada>(listaM);

        List<Matriculaanulada> tempList = new ArrayList<Matriculaanulada>();

        if (cbcurso.getSelectedItem() == null) {
            if (txProcuraNome.getValue().isEmpty()) {
                for (Matriculaanulada m : l) {
                    if (m.getMatricula().getEstudante().getNrEstudante().regionMatches(true, 0, event.getValue(), 0, event.getValue().length())) {
                        tempList.add(m);
                    }
                }
            } else {
                for (Matriculaanulada m : l) {
                    if (m.getMatricula().getEstudante().getNrEstudante().regionMatches(true, 0, event.getValue(), 0, event.getValue().length())
                            && m.getMatricula().getEstudante().getNomeCompleto().regionMatches(true, 0, txProcuraNome.getValue(), 0, txProcuraNome.getValue().length())) {
                        tempList.add(m);
                    }
                }
            }
        } else {
            if (txProcuraNome.getValue().isEmpty()) {
                for (Matriculaanulada m : l) {
                    if (m.getMatricula().getEstudante().getNrEstudante().regionMatches(true, 0, event.getValue(), 0, event.getValue().length())
                            && m.getMatricula().getEstudante().getCursocurrente() == cbcurso.getSelectedItem().getValue()) {
                        tempList.add(m);
                    }
                }
            } else {
                for (Matriculaanulada m : l) {
                    if (m.getMatricula().getEstudante().getNrEstudante().regionMatches(true, 0, event.getValue(), 0, event.getValue().length())
                            && m.getMatricula().getEstudante().getNomeCompleto().regionMatches(true, 0, txProcuraNome.getValue(), 0, txProcuraNome.getValue().length())
                            && m.getMatricula().getEstudante().getCursocurrente() == cbcurso.getSelectedItem().getValue()) {
                        tempList.add(m);
                    }
                }
            }
        }
        ListModelList<Matriculaanulada> model = new ListModelList<Matriculaanulada>(tempList);
        lbAnulcao.setModel(model);

    }

    public void onSelect$cbcurso() throws Exception {
        List<Matriculaanulada> l = new ArrayList<Matriculaanulada>(listaM);

        List<Matriculaanulada> tempList = new ArrayList<Matriculaanulada>();


        if (txProcuraNome.getValue().isEmpty()) {
            if (txProcuraNrmec.getValue().isEmpty()) {
                for (Matriculaanulada m : l) {
                    if (m.getMatricula().getEstudante().getCursocurrente() == cbcurso.getSelectedItem().getValue()) {
                        tempList.add(m);
                    }
                }
            } else {
                for (Matriculaanulada m : l) {
                    if (m.getMatricula().getEstudante().getNrEstudante().regionMatches(true, 0, txProcuraNrmec.getValue(), 0, txProcuraNrmec.getValue().length())
                            && m.getMatricula().getEstudante().getCursocurrente() == cbcurso.getSelectedItem().getValue()) {
                        tempList.add(m);
                    }
                }
            }
        } else {
            if (txProcuraNrmec.getValue().isEmpty()) {
                for (Matriculaanulada m : l) {
                    if (m.getMatricula().getEstudante().getCursocurrente() == cbcurso.getSelectedItem().getValue()
                            && m.getMatricula().getEstudante().getNomeCompleto().regionMatches(true, 0, txProcuraNome.getValue(), 0, txProcuraNome.getValue().length())) {
                        tempList.add(m);
                    }
                }
            } else {
                for (Matriculaanulada m : l) {
                    if (m.getMatricula().getEstudante().getNrEstudante().regionMatches(true, 0, txProcuraNrmec.getValue(), 0, txProcuraNrmec.getValue().length())
                            && m.getMatricula().getEstudante().getCursocurrente() == cbcurso.getSelectedItem().getValue()
                            && m.getMatricula().getEstudante().getNomeCompleto().regionMatches(true, 0, txProcuraNome.getValue(), 0, txProcuraNome.getValue().length())) {
                        tempList.add(m);
                    }
                }
            }
        }

        ListModelList<Matriculaanulada> model = new ListModelList<Matriculaanulada>(tempList);
        lbAnulcao.setModel(model);


    }
    
    public void onRelatorio() throws JRException, IOException {
        String path = winAnulados.getDesktop().getWebApp().getRealPath("/relatorio");
        String path2 = winAnulados.getDesktop().getWebApp().getRealPath("/img");
        Window win = (Window) Executions.createComponents("/report.zul", null, null);
        win.setTitle("Estudantes com Matricula Anulada");
//        List<Matriculaanulada> lm = new ArrayList<Matriculaanulada>();
//        final Iterator<Listitem> items = new ArrayList(lbAnulcao.getItems()).listIterator();
//        Listitem li;
//        while (items.hasNext()) {
//            li = items.next();
//            lm.add((Matriculaanulada) li.getValue());
//        }
        JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(((ListModelList)lbAnulcao.getModel()).getInnerList());

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("img", path2 + "/icoul.png");
        JasperPrint jasperPrint = JasperFillManager.fillReport(path + "/matanulada.jasper", params, ds);
        JRPdfExporter exporter = new JRPdfExporter();

        ByteArrayOutputStream bytesOutputStream = new ByteArrayOutputStream();
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(bytesOutputStream);
        exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
        exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, bufferedOutputStream);
        exporter.exportReport();
        Iframe iframe = (Iframe) win.getFellow("report");
        InputStream mediais = new ByteArrayInputStream(bytesOutputStream.toByteArray());
        bytesOutputStream.close();

        AMedia amedia = new AMedia("inscricaoestudante.pdf", "pdf", "application/pdf", mediais);

        iframe.setContent(amedia);

    }

}
