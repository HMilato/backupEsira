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
import org.zkoss.zk.ui.event.EventListener;
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
import org.zkoss.zul.Constraint;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Doublebox;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Iframe;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Menuitem;
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
public class PrescricaoController extends GenericForwardComposer {

    @WireVariable
    private CRUDService csimp = (CRUDService) SpringUtil.getBean("CRUDService");
    private Window winEstPrec, winAddPresc;
    private Combobox cbDiscP, cbcursoP, cbPeriodDisc, cbestudante, cbdisciplina;
    private Intbox anoi, idi, idd, ind, ibidestudante, litem;
    private Listbox lbinscdisc, cbinscricao;
    private Doublebox dbMedia;
    private Menuitem manoi;
    private Datebox dabdatai;
    private Button btv;
    Map<String, Object> parcond = new HashMap<String, Object>();
    Map<String, Object> par = new HashMap<String, Object>();
    String condp = "", condd = "", condc = "", conda = "";
    private Auxhead auxi;
    String conddis = "";
    Users usr = (Users) Sessions.getCurrent().getAttribute("user");
    private String condn = "";
    Map<String, Object> condpar = new HashMap<String, Object>();
    private String pesq = null;
    private int indc = -1;
    private Intbox posc;

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        if (posc != null) {
            posc.setValue(0);
        }
        condc = " and p.inscricaodisciplina.inscricao.idEstudante.cursocurrente.faculdade =:c ";
       // Users u = csimp.get(Users.class, usr.getUtilizador());
        Faculdade f = csimp.get(Faculdade.class, usr.getFaculdade().getIdFaculdade());
        parcond.put("c", f);
        setLB(0, 10);

    }

    public void onLoadi() {
        int i = lbinscdisc.getItemCount();
        setLB(i, i + 10);
    }

    public ListModel<Curso> getCursoModel() {
        par.clear();
        //Users u = csimp.get(Users.class, usr.getUtilizador());
        Faculdade f = csimp.get(Faculdade.class, usr.getFaculdade().getIdFaculdade());
        par.put("fac", f);
        List<Curso> lc = new ArrayList<Curso>();
        Curso c = new Curso();
        c.setDescricao("------- Curso -------");
        lc.add(c);
        List<Curso> lc2 = csimp.findByJPQuery("from Curso c where c.faculdade = :fac", par);
        lc.addAll(lc2);
        return new ListModelList<Curso>(lc);
    }

    public void onChanging$anoi(InputEvent event) {
        if (!event.getValue().equals("")) {
            conda = " and extract(year from p.inscricaodisciplina.inscricao.dataInscricao) = :a ";
            parcond.replace("a", Integer.parseInt(event.getValue()));
            setLB(0, 10);
        }
    }

    public void onSelect$cbPeriodDisc() {
        if (cbPeriodDisc.getSelectedItem() != null) {
            check(auxi);
            parcond.put("d", (Disciplina) cbPeriodDisc.getSelectedItem().getValue());
            conddis = " and p.inscricaodisciplina.disciplina = :d";
            setLB(0, 10);
        } else {
            parcond.remove("d");
            conddis = "";
        }
    }

    public void onSelect$cbcursoP() {
        if (cbcursoP.getSelectedItem() != null && ((Curso) cbcursoP.getSelectedItem().getValue()).getIdCurso() != null) {
            par.clear();
            par.put("c", (Curso) cbcursoP.getSelectedItem().getValue());
            if (parcond.containsKey("c")) {
                parcond.replace("c", (Curso) cbcursoP.getSelectedItem().getValue());
            } else {
                parcond.put("c", (Curso) cbcursoP.getSelectedItem().getValue());
            }
            condc = " and p.inscricaodisciplina.inscricao.idEstudante.cursocurrente = :c ";
            List<Disciplina> ld = new ArrayList<Disciplina>();
            Disciplina d = new Disciplina();
            d.setNome("-------Disciplina-------");
            ld.add(d);
            List<Disciplina> ld2 = csimp.findByJPQuery("from Disciplina o where o.curso = :c", par);
            ld.addAll(ld2);
            cbDiscP.setModel(new ListModelList<Disciplina>(ld));
            cbDiscP.setVisible(true);
        } else {
            condc = " and p.inscricaodisciplina.inscricao.idEstudante.cursocurrente.faculdade =:c ";
            //Users u = csimp.get(Users.class, usr.getUtilizador());
            Faculdade f = csimp.get(Faculdade.class, usr.getFaculdade().getIdFaculdade());
            if (parcond.containsKey("c")) {
                parcond.replace("c", f);
            } else {
                parcond.put("c", f);
            }
            condd = "";
            parcond.remove("d");
            cbDiscP.setVisible(false);

        }
        setLB(0, 10);
    }

    public void onSelect$cbDiscP() {
        if (cbDiscP.getSelectedItem() != null && ((Disciplina) cbDiscP.getSelectedItem().getValue()).getIdDisc() != null) {
            condd = "and p.inscricaodisciplina.disciplina = :d";
            if (parcond.containsKey("d")) {
                parcond.replace("d", (Disciplina) cbDiscP.getSelectedItem().getValue());
            } else {
                parcond.put("d", (Disciplina) cbDiscP.getSelectedItem().getValue());
            }
            parcond.remove("c");
            condc = "";
        } else {
            condd = "";
            parcond.remove("d");
            parcond.put("c", (Curso) cbcursoP.getSelectedItem().getValue());
            condc = " and p.inscricaodisciplina.inscricao.idEstudante.cursocurrente = :c ";
        }
        setLB(0, 10);
    }

    public void onClick$manoi(Event e) throws InterruptedException {
        anoi.setVisible(manoi.isChecked());
        if (manoi.isChecked()) {
            Date dano = new Date();
            Calendar cal = new GregorianCalendar();
            cal.setTime(dano);
            anoi.setValue(cal.get(Calendar.YEAR));
            conda = " and extract(year from p.inscricaodisciplina.inscricao.dataInscricao) = :a ";
            parcond.put("a", anoi.getValue());
        } else {
            conda = "";
            parcond.remove("a");
        }
        setLB(0, 10);
    }

    public void setLB(int i, int j) {
        if (j == 10) {
            lbinscdisc.setModel(new ListModelList<Prescricao>());
        }
//        List<Inscricaodisciplina> le = csimp.findByJPQuery("from Inscricaodisciplina id where 1=1 "+condd+conda+condc+" and "
//                + "id.inscricaodisciplinaPK in (select p.inscricaodisciplinaPK from Prescricao p)", parcond);
        List<Prescricao> le = csimp.findByJPQueryFilter("from  Prescricao p where 1=1 " + condd + conda + condc, parcond, i, j);
        //lbinscdisc.setModel(new ListModelList<Prescricao>(le));
        final Iterator<Prescricao> items = le.iterator();
        Prescricao p;
        while (items.hasNext()) {
            p = items.next();
//           par.clear();
//           par.put("mpk", m.getMatriculaPK());
//           m = csimpm.findEntByJPQuery("from Matricula m where m.matriculaPK =:mpk", par);
            ((ListModelList) lbinscdisc.getModel()).add(p);
            // }
        }
        if (le.size() < j) {
            btv.setVisible(false);
        } else {
            btv.setVisible(true);
        }
    }

//    public ListModel<Inscricaodisciplina> getListInscDiscModel() {
//        parcond.clear();
//        parcond.put("a", anoi.getValue().intValue());
//        parcond.put("p", Short.parseShort(cbPeriodDisc.getSelectedItem().getValue().toString()));
//        parcond.put("d", ((Disciplina) cbDiscP.getSelectedItem().getValue()));
//        List<Inscricaodisciplina> le = csimp.findByJPQuery("from Inscricaodisciplina ie where 1=1 and ie.disciplina = :d and "
//                + "extract(year from ie.inscricao.dataInscricao) = :a and ie.inscricao.semestre = :p", parcond);
//        return new ListModelList<Inscricaodisciplina>(le);
//    }
    public void onEditPresc(final ForwardEvent evt) throws Exception {
        Messagebox.show("Regularizar a prescrição?", "Confirmação", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION,
                new EventListener() {
                    @Override
                    public void onEvent(Event evet) {
                        switch (((Integer) evet.getData()).intValue()) {
                            case Messagebox.YES:
                                Button btn = (Button) evt.getOrigin().getTarget();
                                Listitem litem = (Listitem) btn.getParent().getParent();
                                Prescricao p = (Prescricao) litem.getValue();
                                //     Inscricaodisciplina i = csimp.load(Inscricaodisciplina.class, p.getInscricaodisciplinaPK());
//        String n = f.getInscricao().getIdEstudante().getNomeCompleto();
//        Float m = f.getNotaFinal();
//        winAddPresc.setParent(winEstPrec);
//        winAddPresc.setTitle("Levantamento de Prescricao");
//        winAddPresc.doModal();
//        ((Intbox) winAddPresc.getFellow("ind")).setValue(lbinscdisc.getIndexOfItem(litem));
//        ((Intbox) winAddPresc.getFellow("idi")).setValue((int) f.getInscricaodisciplinaPK().getIdInscricao());
//        ((Intbox) winAddPresc.getFellow("idd")).setValue((int) f.getInscricaodisciplinaPK().getIdDisciplina());
//        ((Label) winAddPresc.getFellow("nome")).setValue(n);
                                p.setEstado(false);
                                csimp.update(p);
                                ((ListModelList) lbinscdisc.getModel()).set(litem.getIndex(), p);
                                Clients.showNotification(" Prescricao regularizada", null, null, null, 2000);
                                break;
                            case Messagebox.NO:
                                return;
                        }
                    }

                });
    }

    public void onAddPresc() {
//        Constraint k = null;
//        winAddPresc.setParent(winEstPrec);
//        winAddPresc.setTitle("Nova Prescrição");
//        winAddPresc.doModal();
////        Combobox cb = (Combobox) winAddPresc.getFellow("cbdisciplina");
////        Constraint c2 = cb.getConstraint();
////        cb.setConstraint(k);
////        cb.setText("");
////        cb.setConstraint(c2);
//        limpaLB((Listbox) winAddPresc.getFellow("cbinscricao"));//.setModel(new ListModelList<Inscricaodisciplina>());
//        ((Listbox) winAddPresc.getFellow("cbinscricao")).setRows(6);
//        Datebox db = ((Datebox) winAddPresc.getFellow("dabdatai"));
//        Constraint c = db.getConstraint();
//        db.setConstraint(k);
//        db.setValue(null);
//        db.setConstraint(c);
        Executions.createComponents("/inscricao/FormPrescricao.zul", winEstPrec, null);
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

    public void onSCbestudante() {
        if (cbestudante.getSelectedIndex() == posc.getValue() - 1) {
            onLoadComb();
            cbestudante.open();
            cbestudante.setText(pesq);
        } else {
            indc = cbestudante.getSelectedIndex();
            if (cbestudante.getSelectedItem() == null) {
                cbdisciplina.setModel(new ListModelList<Disciplina>());
                return;
            }
            Estudante e = (Estudante) cbestudante.getSelectedItem().getValue();
            e = csimp.load(Estudante.class, e.getIdEstudante());
            List<Disciplina> ldis = e.getCursocurrente().getDisciplinaList();
            cbdisciplina.setModel(new ListModelList<Disciplina>(ldis));
            Clients.scrollIntoView(cbdisciplina);
            cbdisciplina.open();
        }
//        else {
//            List<Disciplina> ldis = new ArrayList<Disciplina>();
//            cbdisciplina.setModel(new ListModelList<Disciplina>(ldis));
//        }
    }

    public void onSelect$cbdisciplina() {
        if (cbdisciplina.getSelectedItem() != null) {
            par.clear();
            par.put("d", (Disciplina) cbdisciplina.getSelectedItem().getValue());
            par.put("e", (Estudante) cbestudante.getSelectedItem().getValue());
            List<Inscricaodisciplina> li = csimp.findByJPQuery("from Inscricaodisciplina id where id.disciplina = :d and id.inscricao.idEstudante = :e"
                    + " and id.estado is true and id.notaFinal is not null and id.notaFinal < 10 and id.inscricaodisciplinaPK not in (select p.inscricaodisciplinaPK from Prescricao p)", par);
            cbinscricao.setModel(new ListModelList<Inscricaodisciplina>(li));
            Clients.scrollIntoView(cbinscricao);
            // cbinscricao.open();
            // ((Comboitem)cbinscricao.getItems().get(0)).getv
        }
    }
    
    public void onSelcbinsc(){
       Inscricaodisciplina i = (Inscricaodisciplina)cbinscricao.getSelectedItem().getValue();
       i=csimp.load(Inscricaodisciplina.class, i.getInscricaodisciplinaPK());
       dabdatai.setValue(i.getInscricao().getDataInscricao());
    }

    public void onSalvar() {
        if (cbinscricao.getSelectedCount() == 0) {
            Clients.showNotification(" Por favor seleccione uma inscrição!", "error", null, null, 0);
            return;
        }
        Inscricaodisciplina id = ((Inscricaodisciplina) cbinscricao.getSelectedItem().getValue());
        id = csimp.load(Inscricaodisciplina.class, id.getInscricaodisciplinaPK());
        Prescricao p = new Prescricao();
        p.setInscricaodisciplinaPK(id.getInscricaodisciplinaPK());
        p.setEstado(true);
        p.setInscricaodisciplina(id);
        Users u = csimp.get(Users.class, usr.getUtilizador());
        p.setOperador(u.getIdFuncionario());
        if (dabdatai.getValue().after(id.getInscricao().getDataInscricao())) {
            Clients.showNotification(" A data da Inscrição antecedente deve ser inferior que a data da última inscrição!", "error", null, null, 0);
            return;
        }
        p.setDatai(dabdatai.getValue());
        par.clear();
        par.put("pk", id.getInscricaodisciplinaPK());
        Prescricao pp = csimp.findEntByJPQuery("from Prescricao p where p.inscricaodisciplinaPK =:pk", par);
        if (pp == null) {
            csimp.Save(p);
        } else {
            Clients.showNotification(" Esta prescricao ja foi registada", "error", null, null, 0);
            return;
        }
        if (lbinscdisc.getModel() == null) {
            lbinscdisc.setModel(new ListModelList<Prescricao>());
        }
        ((ListModelList) lbinscdisc.getModel()).add(p);
        Clients.showNotification(" Adicionado com Sucesso", null, null, null, 0);
        winAddPresc.detach();
    }

    public void onEliminar(final ForwardEvent evt) throws Exception {
        Messagebox.show("Apagar?", "Prompt", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION,
                new EventListener() {
                    @Override
                    public void onEvent(Event evet) {
                        switch (((Integer) evet.getData()).intValue()) {
                            case Messagebox.YES:
                                Button btn = (Button) evt.getOrigin().getTarget();
                                Listitem litem = (Listitem) btn.getParent().getParent();
                                Prescricao p = (Prescricao) litem.getValue();
                                //         Inscricaodisciplina d = csimp.load(Inscricaodisciplina.class, p.getInscricaodisciplinaPK());
//                                Prescricao p = d.getPrescricao();
                                //       d.setPrescricao(null);
                                p.setInscricaodisciplina(null);
                                ((ListModelList) lbinscdisc.getModel()).remove(p);
                                new Listbox().appendChild(litem);
                                csimp.delete(p);
                                Clients.showNotification(" apagado com sucesso", null, null, null, 2000);
                                break;
                            case Messagebox.NO:
                                return;
                        }
                    }

                });
    }

    public void onCancelar() {
        winAddPresc.detach();
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
        }
    }

    public void onExcelExport() throws ParseException {
        if (lbinscdisc.getItemCount() == 0) {
            Clients.showNotification("Sem conteúdo", "warning", null, null, 3000);
            return;
        }
        BeanToExcel beanToExcel = new BeanToExcel();
        beanToExcel.setDataSheetName("Estudantes com Prescrição");
        beanToExcel.exportExcell(lbinscdisc);
    }

    public void onCbestudante(InputEvent evt) {
        indc = -1;
        if (!evt.getValue().equals("") && evt.getValue().charAt(0) != '.') {
            pesq = evt.getValue();
            condn = " and lower(e.nomeCompleto) like :nome ";
            if (condpar.containsKey("nome")) {
                condpar.replace("nome", "%" + evt.getValue().toLowerCase() + "%");
            } else {
                condpar.put("nome", "%" + evt.getValue().toLowerCase() + "%");
            }
        } else {
            if (pesq != null) {
                pesq = "";
            }
            condn = "";
            if (!condpar.containsKey("nome")) {
                return;
            }
            condpar.remove("nome");
        }
        cbestudante.getItems().clear();
        setLBComb(0, 20);
    }

    public void onOpen$cbestudante() {
        if (pesq == null) {
            setLBComb(0, 20);
            pesq = "";
        } else {
            if (cbestudante.isOpen()) {
                cbestudante.setText(pesq);
            } else {
                if (indc != -1) {
                    cbestudante.getSelectedIndex();
                    cbestudante.setSelectedIndex(indc);
                }
            }
        }
    }

    public void setLBComb(int i, int j) {
        if (j == 20) {
            cbestudante.setModel(new ListModelList<Estudante>());
        }
        List<Estudante> li = null;
        //Users u = csimp.get(Users.class, usr.getUtilizador());
        Faculdade f = csimp.get(Faculdade.class, usr.getFaculdade().getIdFaculdade());
        // par.clear();
        condpar.put("fac", f);
        li = csimp.findByJPQueryFilter("from Estudante e where e.cursocurrente.faculdade = :fac " + condn + " order by e.nomeCompleto", condpar, i, j);
        final Iterator<Estudante> items = li.iterator();
        Estudante e;
        //lbinscricao.setRows(lbinscricao.getItemCount() + li.size());
        if (j > 20) {
            ((ListModelList) cbestudante.getModel()).remove(new Estudante());
            new Combobox().appendChild(cbestudante.getItemAtIndex(posc.getValue() - 1));
        }
        while (items.hasNext()) {
            e = items.next();
            ((ListModelList) cbestudante.getModel()).add(e);
        }
        if (li.size() == j) {
            Estudante es = new Estudante();
            es.setNomeCompleto("-------Ver Mais-------");
            ((ListModelList) cbestudante.getModel()).add(es);
            posc.setValue(((ListModelList) cbestudante.getModel()).size());
        }
    }

    public void onLoadComb() {
        int i = ((ListModelList) cbestudante.getModel()).size() - 1;
        setLBComb(i, i + 20);
    }
}
