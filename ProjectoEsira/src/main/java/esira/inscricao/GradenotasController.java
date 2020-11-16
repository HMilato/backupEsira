/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package esira.inscricao;

import ExcelExport.BeanToExcel;
import entidade.DisciplinaEstudante;
import esira.domain.Curso;
import esira.domain.Disciplina;
import esira.domain.Estudante;
import esira.domain.Faculdade;
import esira.domain.Inscricao;
import esira.domain.Inscricaodisciplina;
import esira.domain.Matriculaanulada;
import esira.domain.Planocurricular;
import esira.domain.Prescricao;
import esira.domain.Users;
import esira.service.CRUDService;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
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
import org.zkoss.util.media.AMedia;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.event.InputEvent;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.Auxhead;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Hlayout;
import org.zkoss.zul.Iframe;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Menuitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

/**
 *
 * @author Ussimane
 */
public class GradenotasController extends GenericForwardComposer {

    @WireVariable
    private CRUDService csimpm = (CRUDService) SpringUtil.getBean("CRUDService");
    private Window winGNotas, winDAtrasada;
    private Listbox lbgnotas;
    private int ano;
    Textbox txx;
    Map<String, Object> par = new HashMap<String, Object>();
    Users usr = (Users) Sessions.getCurrent().getAttribute("user");
    private Hlayout ahead;
    String condfac = "", condnr = "", condnome = "", condgenero = "", condanoi = "", condano = "", condcurso = "", condrep = " and id.notaFinal >=10 ";
    Textbox txProcurar, txProcNrmec;
    Map<String, Object> condpar = new HashMap<String, Object>();
    Combobox cbcurso;
    private Intbox ibProcAno;
    private Button btv;
    private int pos;
    private Menuitem mrep;

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        Date dano = new Date();
        Calendar cal = new GregorianCalendar();
        cal.setTime(dano);
        if (usr.getUestudante()) {
            btv.setVisible(false);
            ahead.setVisible(false);
            if (usr.getUtilizador() != null) {
                //txx.setValue(csimpm.get(Users.class, usr.getUtilizador()).getIdEstudante().getIdEstudante() + "");
            }
        }
        if (winGNotas != null) {
            // ibProcAno.setValue(cal.get(Calendar.YEAR));
            //cbcurso.setSelectedIndex(0);
            ano = cal.get(Calendar.YEAR);
            if (!usr.getUestudante()) {
                // Messagebox.show(ibProcAno.getValue()+" "+((Curso)cbcurso.getSelectedItem().getValue()).getDescricao());
                // lbgnotas.setModel(getListInscricaoModel());
                if(cbcurso.getModel()!=null&&cbcurso.getModel().getSize()>0){
                condpar.clear();
                pos = 0;
                condcurso = " and e.cursocurrente = :curso ";
                condpar.put("curso", (Curso) cbcurso.getModel().getElementAt(0));
                setLB(0, 20);}
            } else {
                if (!winGNotas.inModal() && usr.getUtilizador() != null) {
                    getListInscricaoModel2();
                }
            }
        }
        if (winDAtrasada != null) {
            getListInscricaoModel3();
        }

    }

    public ListModel<Curso> getListaCursoModel() {
       // Users u = csimpm.get(Users.class, usr.getUtilizador());
        par.clear();
        Faculdade f = csimpm.get(Faculdade.class, usr.getFaculdade().getIdFaculdade());
        par.put("fac", f);
        List<Curso> lc = csimpm.findByJPQuery("from Curso c where c.faculdade = :fac", par);
        return new ListModelList<Curso>(lc);
    }

    public ListModel<Estudante> getListInscricaoModel() {
        List<Estudante> li = null;
        par.clear();
        //par.put("curso", (Curso) cbcurso.getSelectedItem().getValue());e.cursocurrente = :curso and
        par.put("ano", ano);
        // li = csimpm.findByJPQuery("from Inscricao i where i.estado is true and i.dataConfirmacao is not null", null);
        li = csimpm.findByJPQuery("from Estudante e where 1=1 "
                + " and e.anoIngresso = :ano order by e.nomeCompleto asc", par);
        return new ListModelList<Estudante>(li);
    }

    public void getListInscricaoModel2() {
        par.clear();
        par.put("user", usr.getUtilizador());
        Users u = csimpm.get(Users.class, usr.getUtilizador());
        ListModelList lm = new ListModelList<>();
        List<DisciplinaEstudante> lde = new ArrayList<DisciplinaEstudante>();
        Curso cu = u.getIdEstudante().getCursocurrente();
        int pl = u.getIdEstudante().getPlanoc();
        par.clear();
        par.put("cu", cu);
        par.put("planoc", pl);
        List<Disciplina> ld = csimpm.findByJPQuery("from Disciplina d where d.curso = :cu and d.planoc = :planoc order by d.nivel, d.semestre", par);
        final Iterator<Disciplina> items2 = new ArrayList(ld).listIterator();
        par.clear();
        par.put("e", u.getIdEstudante());
        par.put("d", null);
        Disciplina d;
        while (items2.hasNext()) {
            d = items2.next();
            DisciplinaEstudante de = new DisciplinaEstudante();
            de.setDisciplina(d);
            par.replace("d", d);
            Inscricaodisciplina id = csimpm.findEntByJPQuery("from Inscricaodisciplina id where id.inscricao.idEstudante = :e"
                    + " and id.disciplinaActiva=3 and id.estado is not null and id.notaFinal >=10 and id.disciplina = :d order by"
                    + " id.inscricao.dataInscricao desc", par);
            de.setInscricaodisciplina(id);
            lde.add(de);
        }
        for (DisciplinaEstudante de : lde) {
            lm.add(de);
        }
        lbgnotas.setMultiple(false);
        lbgnotas.setModel(lm);
        lbgnotas.setMultiple(true);
        // return inscric = new ListModelList<Inscricao>(li);
    }

    public void getListInscricaoModel3() {
        par.clear();
        par.put("user", usr.getUtilizador());
        Users u = csimpm.get(Users.class, usr.getUtilizador());
        ListModelList lm = new ListModelList<>();
        List<DisciplinaEstudante> lde = new ArrayList<DisciplinaEstudante>();
        Curso cu = u.getIdEstudante().getCursocurrente();
        int pl = u.getIdEstudante().getPlanoc();
        par.clear();
        par.put("cu", cu);
        par.put("planoc", pl);
        List<Disciplina> ld = csimpm.findByJPQuery("from Disciplina d where d.curso = :cu and d.planoc = :planoc order by d.nivel, d.semestre", par);
        final Iterator<Disciplina> items2 = new ArrayList(ld).listIterator();
        par.clear();
        par.put("e", u.getIdEstudante());
        par.put("d", null);
        Disciplina d;
        while (items2.hasNext()) {
            d = items2.next();
            DisciplinaEstudante de = new DisciplinaEstudante();
            de.setDisciplina(d);
            par.replace("d", d);
            Inscricaodisciplina id = csimpm.findEntByJPQuery("from Inscricaodisciplina id where id.inscricao.idEstudante = :e"
                    + " and id.disciplinaActiva=3 and id.estado is not null and id.notaFinal >=10 and id.disciplina = :d order by"
                    + " id.inscricao.dataInscricao desc", par);
            if (id == null) {
                de.setInscricaodisciplina(id);
                lde.add(de);
            }
        }
        for (DisciplinaEstudante de : lde) {
            lm.add(de);
        }
        lbgnotas.setMultiple(false);
        lbgnotas.setModel(lm);
        lbgnotas.setMultiple(true);
        // return inscric = new ListModelList<Inscricao>(li);
    }

    public void onVerInscricao(ForwardEvent evt) {
        Button buton = (Button) evt.getOrigin().getTarget();
        Listitem item = (Listitem) buton.getParent().getParent();
        Estudante e = (Estudante) item.getValue();
        e = csimpm.load(Estudante.class, e.getIdEstudante());
        int ind = item.getIndex();
        Listbox l = item.getListbox();

//        List<Inscricaodisciplina> li = csimpm.findByJPQuery("from Inscricaodisciplina id where id.inscricao.idEstudante = :e"
//                + " and id.disciplinaActiva=3 and id.estado is not null", par);
        if (buton.getImage().equals("/icon/down.png")) {
            buton.setImage("/icon/chevrons.png");
            ListModelList lm = (ListModelList) l.getListModel();
            Object o = null;//lm.getElementAt(++ind);
            int k = 0;
            final Iterator<ListModelList> items = new ArrayList(lm.subList(++ind, lm.getSize())).listIterator();
            while (items.hasNext()) {

                o = items.next();
                if (o != null && !(o instanceof Estudante)) {
                    lm.remove(o);
                    k++;
                } else {
                    break;
                }
                ++ind;
            }
            int r = lbgnotas.getRows();
            lbgnotas.setRows(r - k);
        } else {
            buton.setImage("/icon/down.png");
            List<DisciplinaEstudante> lde = new ArrayList<DisciplinaEstudante>();
            Curso cu = e.getCursocurrente();
            par.clear();
            par.put("cu", cu);
            par.put("planoc", e.getPlanoc());
            List<Disciplina> ld = csimpm.findByJPQuery("from Disciplina d where d.curso = :cu and d.planoc = :planoc order by d.nivel, d.semestre", par);
            final Iterator<Disciplina> items2 = new ArrayList(ld).listIterator();
            par.clear();
            par.put("e", e);
            par.put("d", null);
            Disciplina d;
            while (items2.hasNext()) {
                d = items2.next();
                DisciplinaEstudante de;
                par.replace("d", d);
                List<Inscricaodisciplina> lid = csimpm.findByJPQuery("from Inscricaodisciplina id where id.inscricao.idEstudante = :e"
                        + " and id.estado is true and id.notaFinal is not null " + condrep + " and id.disciplina = :d order by"
                        + " id.inscricao.dataInscricao asc", par);
                for (Inscricaodisciplina id : lid) {
                    de = new DisciplinaEstudante();
                    de.setDisciplina(d);
                    de.setInscricaodisciplina(id);
                    lde.add(de);
                }
                if (lid.isEmpty()) {
                    de = new DisciplinaEstudante();
                    de.setDisciplina(d);
                    de.setInscricaodisciplina(null);
                    lde.add(de);
                }
            }
            int k = ((ListModelList) l.getListModel()).getSize() + 1;
            l.setRows(lde.size() + k);
            for (DisciplinaEstudante de : lde) {
                ++ind;
                de.setId(new Long(e.getIdEstudante()));
                l.setMultiple(false);
                // l.setSizedByContent(false);
                //  l.getListhead().setSizable(false);
                ((ListModelList) l.getListModel()).add(ind, de);
                // l.setSizedByContent(true);
                //   l.getListhead().setSizable(true);
                l.setMultiple(true);
            }
        }
    }

    public void onRelatorio() throws JRException, IOException {
        String path = winGNotas.getDesktop().getWebApp().getRealPath("/relatorio");
        String path2 = winGNotas.getDesktop().getWebApp().getRealPath("/img");
        Window win = (Window) Executions.createComponents("/report.zul", null, null);
        win.setTitle("Grade de Notas");
//        List<Matriculaanulada> lm = new ArrayList<Matriculaanulada>();
//        final Iterator<Listitem> items = new ArrayList(lbgnotas.getItems()).listIterator();
//        Listitem li;
//        while (items.hasNext()) {
//            li = items.next();
//            lm.add((Matriculaanulada) li.getValue());
//        }
        JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(((ListModelList) lbgnotas.getModel()).getInnerList());

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("img", path2 + "/icoul.png");
        JasperPrint jasperPrint = JasperFillManager.fillReport(path + "/gradenota.jasper", params, ds);
        JRPdfExporter exporter = new JRPdfExporter();

        ByteArrayOutputStream bytesOutputStream = new ByteArrayOutputStream();
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(bytesOutputStream);
        exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
        exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, bufferedOutputStream);
        exporter.exportReport();
        Iframe iframe = (Iframe) win.getFellow("report");
        InputStream mediais = new ByteArrayInputStream(bytesOutputStream.toByteArray());
        bytesOutputStream.close();

        AMedia amedia = new AMedia("Gradenota.pdf", "pdf", "application/pdf", mediais);

        iframe.setContent(amedia);

    }

    public void onExcelExport() throws ParseException {
        if (lbgnotas.getItemCount() == 0) {
            Clients.showNotification("Sem conteúdo", "warning", null, null, 3000);
            return;
        }
        BeanToExcel beanToExcel = new BeanToExcel();
        beanToExcel.setDataSheetName("Grade de Notas");
        beanToExcel.exportExcell(lbgnotas);
    }

    ////////////////////////////////////PESQUISAS//////////////////////////////////////////////////////
    public void onClick$mrep(Event e) throws InterruptedException {
        if (mrep.isChecked()) {
            condrep = " and (id.notaFinal >= 10 or id.notaFinal < 10) ";
        } else {
            condrep = " and id.notaFinal >= 10 ";
        }
        setLB(0, 20);
    }

    public void onChanging$txProcNrmec(InputEvent evt) {
        if (!evt.getValue().equals("") && evt.getValue().charAt(0) != '.') {
            condnr = " and e.nrEstudante = :nr ";
            if (condpar.containsKey("nr")) {
                condpar.replace("nr", evt.getValue());
            } else {
                condpar.put("nr", evt.getValue());
            }
        } else {
            condnr = "";
            if (!condpar.containsKey("nr")) {
                return;
            }
            condpar.remove("nr");
        }
        setLB(0, 20);
    }

    public void onChanging$txProcurar(InputEvent evt) {
        if (!evt.getValue().equals("") && evt.getValue().charAt(0) != '.') {
            condnome = " and lower(e.nomeCompleto) like :nome ";
            if (condpar.containsKey("nome")) {
                condpar.replace("nome", "%" + evt.getValue().toLowerCase() + "%");
            } else {
                condpar.put("nome", "%" + evt.getValue().toLowerCase() + "%");
            }
        } else {
            condnome = "";
            if (!condpar.containsKey("nome")) {
                return;
            }
            condpar.remove("nome");
        }
        setLB(0, 20);
    }

    public void onChanging$ibProcAno(InputEvent evt) {
        if (!evt.getValue().equals("") && evt.getValue().charAt(0) != '.') {
            condano = " and e.anoIngresso = :a ";
            if (condpar.containsKey("a")) {
                condpar.replace("a", Integer.parseInt(evt.getValue()));
            } else {
                condpar.put("a", Integer.parseInt(evt.getValue()));
            }
        } else {
            condano = "";
            if (!condpar.containsKey("a")) {
                return;
            }
            condpar.remove("a");
        }
        setLB(0, 20);
    }

    public void onChange$cbcurso() {
        // if (cbcurso.getSelectedIndex() != 0) {
        condcurso = " and e.cursocurrente = :curso ";
        if (condpar.containsKey("curso")) {
            condpar.replace("curso", (Curso) cbcurso.getSelectedItem().getValue());
        } else {
            condpar.put("curso", (Curso) cbcurso.getSelectedItem().getValue());
        }
        setLB(0, 20);
    }

    public void setLB(int i, int j) {
        if (j == 20) {
            lbgnotas.setModel(new ListModelList<Prescricao>());
        }
        List<Estudante> li = csimpm.findByJPQueryFilter("from Estudante e where 1=1" + condcurso + condnome + condnr + condano + " order by e.nomeCompleto asc", condpar, i, j);
        final Iterator<Estudante> items = li.iterator();
        Estudante e;
        lbgnotas.setRows(lbgnotas.getItemCount() + li.size());
        while (items.hasNext()) {
            e = items.next();
            ((ListModelList) lbgnotas.getModel()).add(e);
        }
        if (li.size() < j) {
            btv.setVisible(false);
        } else {
            pos = pos += li.size();
            btv.setVisible(true);
        }
    }

    public void onLoadi() {
        int i = pos;
        setLB(i, i + 20);
    }

}
