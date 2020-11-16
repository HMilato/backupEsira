/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package esira.inscricao;

import ExcelExport.BeanToExcel;
import esira.domain.Curso;
import esira.domain.Disciplina;
import esira.domain.Estudante;
import esira.domain.Faculdade;
import esira.domain.Funcionario;
import esira.domain.Inscricao;
import esira.domain.Inscricaodisciplina;
import esira.domain.InscricaodisciplinaPK;
import esira.domain.Matricula;
import esira.domain.PlanificacaoAnoLectivo;
import esira.domain.Planocurricular;
import esira.domain.Prescricao;
import esira.service.CRUDService;
import esira.webapp.MainLayoutComposer;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import esira.domain.PrescricaoPK;
import esira.domain.Users;
import java.text.ParseException;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.zkoss.util.media.AMedia;
import org.zkoss.util.media.Media;
import org.zkoss.zk.au.out.AuDownload;
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
import org.zkoss.zul.A;
import org.zkoss.zul.Auxhead;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Doublebox;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Hlayout;
import org.zkoss.zul.Iframe;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Row;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Tabpanel;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;
import org.zkoss.zul.impl.InputElement;

/**
 *
 * @author Administrator
 */
public class InscricaoDiscController extends GenericForwardComposer {

    @WireVariable
    private CRUDService csimp = (CRUDService) SpringUtil.getBean("CRUDService");
    List<Curso> cursos = csimp.getAll(Curso.class);
    List<Disciplina> disciplinas = csimp.getAll(Disciplina.class);
    private ListModel<Disciplina> disciplinaModel = new ListModelList<Disciplina>(disciplinas);
    private Window winDiscInsc, winEditMedia;
    private Combobox cbDisc, cbcurso, cbPeriodDisc, cbTurno, cbPlanoD;
    private Intbox anoi, idi, idd, ind, ibturma;
    private Listbox lbinscdisc, lbinscdisc2;
    private Doublebox dbMedia;
    Map<String, Object> parcond = new HashMap<String, Object>();
    Map<String, Object> par = new HashMap<String, Object>();
    String condp = "", condd = "", condc = "", conda = "";
    Users usr = (Users) Sessions.getCurrent().getAttribute("user");
    private Hlayout hlayout;

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        Date dano = new Date();
        Calendar cal = new GregorianCalendar();
        cal.setTime(dano);
        anoi.setValue(cal.get(Calendar.YEAR));
        if (cbcurso.getModel() != null&&cbcurso.getModel().getSize()>0) {
            Curso c = (Curso) cbcurso.getModel().getElementAt(0);
            par.clear();
            par.put("c", c);
            par.put("planoc", c.getPlanoc());
            List<Disciplina> ld = csimp.findByJPQuery("from Disciplina d where d.curso = :c and d.planoc = :planoc", par);
            cbDisc.setModel(new ListModelList<Disciplina>(ld));
            par.clear();
            par.put("c", c);
            List<Planocurricular> lc = csimp.findByJPQuery("from Planocurricular p where p.curso = :c order by p.planocurricularPK.ano desc", par);
            cbPlanoD.setModel(new ListModelList<Planocurricular>(lc));
            cbPlanoD.setText(c.getPlanoc() + "");
        }
    }

//    public ListModel<Disciplina> getDiscModel() {
//        if(cbcurso.getSelectedItem()==null)
//            return new ListModelList<Disciplina>(new ArrayList<Disciplina>());
//        return new ListModelList<Disciplina>(((Curso)cbcurso.getSelectedItem().getValue()).getDisciplinaList());
//    }
    public ListModel<Curso> getCursoModel() {
       // Users u = csimp.get(Users.class, usr.getUtilizador());
        Faculdade f = csimp.get(Faculdade.class, usr.getFaculdade().getIdFaculdade());
        par.clear();
        par.put("fac", f);
        List<Curso> lc = csimp.findByJPQuery("from Curso c where c.faculdade = :fac", par);
        return new ListModelList<Curso>(lc);
    }

    public void onChanging$anoi(InputEvent evt) {
        if (!evt.getValue().equals("")) {
            //check(hlayout);
            anoi.setValue(Integer.parseInt(evt.getValue()));
            setLB();
        }
    }

    public void onSelect$cbPeriodDisc() {
        if (cbPeriodDisc.getSelectedItem() != null) {
            //    check(hlayout);
            setLB();
        }
    }

    public void onSelect$cbcurso() {
        if (cbcurso.getSelectedItem() != null) {
            Curso c = (Curso) cbcurso.getSelectedItem().getValue();
            par.clear();
            par.put("c", c);
            List<Planocurricular> lc = csimp.findByJPQuery("from Planocurricular p where p.curso = :c order by p.planocurricularPK.ano desc", par);
            cbPlanoD.setModel(new ListModelList<Planocurricular>(lc));

            par.clear();
            par.put("c", c);
            if (cbPlanoD.getSelectedItem() != null) {
                par.put("planoc", ((Planocurricular) cbPlanoD.getSelectedItem().getValue()).getPlanocurricularPK().getAno());
            } else {
                par.put("planoc", c.getPlanoc());// ent.put("d", Disciplina.class);
            }
            List<Disciplina> ld = csimp.findByJPQuery("from Disciplina d where d.curso = :c and d.planoc = :planoc", par);
            cbDisc.setModel(new ListModelList<Disciplina>(ld));
            // cbDisc.setSelectedIndex(0);
            check(hlayout);
            //setLB();
        }
    }

    public void onSelect$cbPlanoD() {
        if (cbPlanoD.getSelectedItem() != null) {
            par.clear();
            Curso c = (Curso) cbcurso.getSelectedItem().getValue();
            Planocurricular pl = (Planocurricular) cbPlanoD.getSelectedItem().getValue();
            par.put("c", c);
            par.put("planoc", pl.getPlanocurricularPK().getAno());
            List<Disciplina> ld = csimp.findByJPQuery("from Disciplina d where d.curso = :c and d.planoc = :planoc", par);
            cbDisc.setModel(new ListModelList<Disciplina>(ld));
            // cbDisc.setSelectedIndex(0);
            check(hlayout);
        }
    }

    public void onSelect$cbDisc() {
        if (cbDisc.getSelectedItem() != null) {
            check(hlayout);
            setLB();
        }
    }

    public void setLB() {
        limpaLB(lbinscdisc);
        limpaLB(lbinscdisc2);
        parcond.clear();
        parcond.put("a", anoi.getValue());
        parcond.put("p", Short.parseShort(cbPeriodDisc.getSelectedItem().getValue().toString()));
        if (cbDisc.getSelectedItem() != null) {
            parcond.put("d", ((Disciplina) cbDisc.getSelectedItem().getValue()));
            List<Inscricaodisciplina> le = csimp.findByJPQuery("from Inscricaodisciplina ie where ie.disciplina = :d and "
                    + "extract(year from ie.inscricao.dataInscricao) = :a and ie.inscricao.semestre = :p and ie.estado is true order by ie.inscricao.idEstudante.nomeCompleto", parcond);
            lbinscdisc.setModel(new ListModelList<Inscricaodisciplina>(le));
            lbinscdisc2.setModel(new ListModelList<Inscricaodisciplina>(le));
        }
    }

    public ListModel<Inscricaodisciplina> getListInscDiscModel() {
        parcond.clear();
        parcond.put("a", anoi.getValue().intValue());
        parcond.put("p", Short.parseShort(cbPeriodDisc.getSelectedItem().getValue().toString()));
        parcond.put("d", ((Disciplina) cbDisc.getSelectedItem().getValue()));
        List<Inscricaodisciplina> le = csimp.findByJPQuery("from Inscricaodisciplina ie where 1=1 and ie.disciplina = :d and "
                + "extract(year from ie.inscricao.dataInscricao) = :a and ie.inscricao.semestre = :p", parcond);
        return new ListModelList<Inscricaodisciplina>(le);
    }

    public void onEditDisc(ForwardEvent evt) throws Exception {
        Button btn = (Button) evt.getOrigin().getTarget();
        Listitem litem = (Listitem) btn.getParent().getParent();
        Inscricaodisciplina f = (Inscricaodisciplina) litem.getValue();
        f = csimp.load(Inscricaodisciplina.class, f.getInscricaodisciplinaPK());
        String n = f.getInscricao().getIdEstudante().getNomeCompleto();
        Float m = f.getNotaFinal();
        winEditMedia.setParent(winDiscInsc);
        winEditMedia.setTitle("Editar Media Final");
        winEditMedia.doModal();
        ((Intbox) winEditMedia.getFellow("ind")).setValue(lbinscdisc.getIndexOfItem(litem));
        ((Intbox) winEditMedia.getFellow("idi")).setValue((int) f.getInscricaodisciplinaPK().getIdInscricao());
        ((Intbox) winEditMedia.getFellow("idd")).setValue((int) f.getInscricaodisciplinaPK().getIdDisciplina());
        ((Label) winEditMedia.getFellow("nome")).setValue(n);
        Combobox cbt = (Combobox) winEditMedia.getFellow("cbTurno");
        Comboitem cbt1 = cbt.getItems().get(0);
        Comboitem cbt2 = cbt.getItems().get(1);
//        Messagebox.show(f.getTurno()+"");
        //   Messagebox.show(cbt1.getValue().toString());
        if (f.getTurno() == 1) {
            cbt.setSelectedItem(cbt1);
            cbt.setText("Laboral");
        } else {
            cbt.setSelectedItem(cbt2);
            cbt.setText("Pos-Laboral");
        }
        ((Intbox) winEditMedia.getFellow("ibturma")).setValue(f.getTurma());
        if (m != null) {
            ((Doublebox) winEditMedia.getFellow("dbMedia")).setValue(m.doubleValue());
        }
    }

    public void onBtnSave() {
        par.clear();
        par.put("iePK", new InscricaodisciplinaPK(idi.getValue().longValue(), idd.getValue().longValue()));
        Inscricaodisciplina i = (Inscricaodisciplina) csimp.findByJPQuery("from Inscricaodisciplina ie where ie.inscricaodisciplinaPK = :iePK", par).get(0);
        if (dbMedia.getValue() != null) {
            i.setNotaFinal(dbMedia.getValue().floatValue());
            i.setTurno(Integer.parseInt(cbTurno.getSelectedItem().getValue().toString()));
            i.setTurma(ibturma.getValue());
        } else {
            i.setNotaFinal(null);
            i.setTurno(Integer.parseInt(cbTurno.getSelectedItem().getValue().toString()));
            i.setTurma(ibturma.getValue());
            csimp.update(i);
            if (i.getPrescricao() != null) {
                par.clear();
                par.put("id", i.getInscricaodisciplinaPK());
                Prescricao p = csimp.findEntByJPQuery("from Prescricao p where p.inscricaodisciplinaPK = :id", par);
                i.setPrescricao(null);
                csimp.delete(p);
            }
            Clients.showNotification(" Sem nota!", "warning", null, null, 2000);
            ((Label) lbinscdisc.getItemAtIndex(ind.getValue()).getChildren().get(2).getChildren().get(0)).setValue("--- ");
            winEditMedia.detach();
            return;
        }
        csimp.update(i);
        if (i.getNotaFinal() < 10) {
            //Verificar se tinha reprovado antes
            par.clear();
            par.put("d", i.getDisciplina());
            par.put("e", i.getInscricao().getIdEstudante());
            par.put("dat", i.getInscricao().getDataInscricao());
            Inscricaodisciplina oldID = csimp.findEntByJPQuery("select k from Inscricaodisciplina k where k.disciplina = :d and k.inscricao.dataInscricao in (select max(id.inscricao.dataInscricao) "
                    + "from Inscricaodisciplina id where id.estado is true and id.disciplina =:d and id.inscricao.idEstudante = :e and id.disciplinaActiva = 2 and"
                    + " id.inscricao.dataInscricao < :dat)", par);
//            Inscricaodisciplina oldID = csimp.findEntByJPQuery("from Inscricaodisciplina id where id.disciplina =:d and id.inscricao.idEstudante"
//                    + " = :e and id.disciplinaActiva = 2", par);
            if (oldID != null && oldID.getNotaFinal() < 10 && oldID.getPrescricao() == null) {
                InscricaodisciplinaPK prePK = new InscricaodisciplinaPK(i.getInscricao().getIdInscricao(), i.getDisciplina().getIdDisc());
                Prescricao pre = new Prescricao();
                pre.setInscricaodisciplinaPK(prePK);
                pre.setEstado(true);
                pre.setDatai(oldID.getInscricao().getDataInscricao());
                csimp.update(pre);
                Clients.showNotification(" Foi registado uma prescricao para o " + i.getInscricao().getIdEstudante().getNomeCompleto() + "!", "warning", null, null, 0);
            }
        } else {
            if (i.getPrescricao() != null) {
                par.clear();
                par.put("id", i.getInscricaodisciplinaPK());
                Prescricao p = csimp.findEntByJPQuery("from Prescricao p where p.inscricaodisciplinaPK = :id", par);
                i.setPrescricao(null);
                csimp.delete(p);
            }
        }
        Clients.showNotification(" Actualizado com sucesso", null, null, null, 2000);
        ((Label) lbinscdisc.getItemAtIndex(ind.getValue()).getChildren().get(2).getChildren().get(0)).setValue(dbMedia.getValue().floatValue() + "");
        winEditMedia.detach();

    }

    public void onBtnCanc() {
        winEditMedia.detach();
    }

    private void check(Component component) {
        checkIsValid(component);

        if (component.isVisible()) {
            List<Component> children = component.getChildren();
            for (Component each : children) {
                check(each);
            }
        }
    }

    private void checkParent(Component component) {
        if (component instanceof Tabpanel) {
            ((Tabpanel) component).getTabbox().setSelectedPanel((Tabpanel) component);
        } else {
            checkParent(component.getParent());
        }
    }

    private void checkIsValid(Component component) {
        if (component instanceof InputElement) {
            if ((!((InputElement) component).isValid())) {
                // Force show errorMessage
                if (!(((InputElement) component)).isVisible()) {
                    checkParent(component.getParent());
                }
                Clients.scrollIntoView(component);
                ((InputElement) component).setFocus(true);
                return;
            }
            if (((component instanceof Combobox) && ((Combobox) component).getSelectedItem() == null)) {
                if (!(((InputElement) component)).isVisible()) {
                    checkParent(component.getParent());
                }
                Clients.scrollIntoView(component);
                ((Combobox) component).open();
                return;
            }
        }
    }

    public void onRelatorio() throws JRException, IOException {
        if (lbinscdisc.getItemCount() == 0 && lbinscdisc2.getItemCount() == 0) {
            Clients.showNotification("Sem conteúdo", "warning", null, null, 3000);
            return;
        }
        check(hlayout);
        String path = winDiscInsc.getDesktop().getWebApp().getRealPath("/relatorio");
        String path2 = winDiscInsc.getDesktop().getWebApp().getRealPath("/img");
        Window win = (Window) Executions.createComponents("/report.zul", null, null);
        win.setTitle("Estudantes Inscritos na Disciplina");
        List<Inscricaodisciplina> lm = new ArrayList<Inscricaodisciplina>();
        Inscricaodisciplina id = new Inscricaodisciplina();
        List<Listitem> lit = new ArrayList<Listitem>();
        lit.addAll(lbinscdisc.getItems());
        lit.addAll(lbinscdisc2.getItems());
        final Iterator<Listitem> items = new ArrayList(lit).listIterator();

        Listitem li;
        while (items.hasNext()) {
            li = items.next();
            id = (Inscricaodisciplina) li.getValue();
            id = csimp.load(Inscricaodisciplina.class, id.getInscricaodisciplinaPK());
            Inscricao i = id.getInscricao();
            i = csimp.load(Inscricao.class, i.getIdInscricao());
            if (i.getEstado()) {
                Estudante e = i.getIdEstudante();
                e = csimp.load(Estudante.class, e.getIdEstudante());
                i.setIdEstudante(e);
                id.setInscricao(i);
                lm.add(id);
            }
        }
//        Inscricaodisciplina id=new Inscricaodisciplina();
//        if (lbinscdisc.getItemCount() > 0) {
//            id = ((Inscricaodisciplina) lbinscdisc.getItems().get(0).getValue());
//            id = csimp.load(Inscricaodisciplina.class, id.getInscricaodisciplinaPK());
//        }
        JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(lm);//((ListModelList) lbinscdisc.getListModel()).getInnerList());

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("img", path2 + "/icoul.png");
        Curso c = (Curso) cbcurso.getSelectedItem().getValue();
        params.put("curso", c.getDescricao());
        params.put("discip", ((Disciplina) cbDisc.getSelectedItem().getValue()).getNome());
        params.put("anol", anoi.getValue());
        c = csimp.load(Curso.class, c.getIdCurso());
        params.put("fac", c.getFaculdade().getDesricao());
        params.put("localiz", c.getFaculdade().getLocalizacao());
//         String jrxmlFile = path + "/inscricaodisciplina.jrxml";
//            String jasperDestination = path + "/inscricaodisciplina.jasper";
//            JasperCompileManager.compileReportToFile(jrxmlFile, jasperDestination);
        JasperPrint jasperPrint = JasperFillManager.fillReport(path + "/inscricaodisciplina.jasper", params, ds);
        JRPdfExporter exporter = new JRPdfExporter();

        ByteArrayOutputStream bytesOutputStream = new ByteArrayOutputStream();
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(bytesOutputStream);
        exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
        exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, bufferedOutputStream);
        exporter.exportReport();
        Iframe iframe = (Iframe) win.getFellow("report");
        InputStream mediais = new ByteArrayInputStream(bytesOutputStream.toByteArray());
        bytesOutputStream.close();

        AMedia amedia = new AMedia("inscricaodisciplina.pdf", "pdf", "application/pdf", mediais);

        iframe.setContent(amedia);

    }

    public void onExcelExport() throws ParseException {
        if (lbinscdisc.getItemCount() == 0) {
            Clients.showNotification("Sem conteúdo", "warning", null, null, 3000);
            return;
        }
        BeanToExcel beanToExcel = new BeanToExcel();
        beanToExcel.setDataSheetName(cbcurso.getValue() + " - Lista de estuudante");
        beanToExcel.exportExcell(lbinscdisc);
    }

    public void limpaLB(Listbox lb) {
        if (lb.getItemCount() > 0) {
            final Iterator<Listitem> items = new ArrayList(lb.getItems()).listIterator();
            Listitem li;
            Listbox l = new Listbox();
            while (items.hasNext()) {
                li = items.next();
                l.appendChild(li);
            }
        }
    }
}
