/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package esira.controller;

import ExcelExport.BeanToExcel;
import com.mchange.v1.db.sql.CBPUtils;
import esira.domain.Areacientifica;
import esira.domain.Caracter;
import esira.domain.Curso;
import esira.domain.Delegacao;
import esira.domain.Disciplina;
import esira.domain.Docente;
import esira.domain.Faculdade;
import esira.domain.Funcionario;
import esira.domain.Lecciona;
import esira.domain.LeccionaPK;
import esira.domain.Matricula;
import esira.domain.Periodo;
import esira.domain.Planocurricular;
import esira.domain.PlanocurricularPK;
import esira.domain.Precedencia;
import esira.domain.PrecedenciaPK;
import esira.domain.Prescricao;
import esira.domain.Users;
import esira.service.CRUDService;
import esira.service.UserAutentic;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.zkoss.lang.Strings;
import org.zkoss.web.servlet.dsp.action.Page;
import org.zkoss.zhtml.Textarea;
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
import org.zkoss.zkplus.hibernate.HibernateUtil;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.Button;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Constraint;
import org.zkoss.zul.Doublebox;
import org.zkoss.zul.FieldComparator;
import org.zkoss.zul.Hlayout;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Menuitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Paging;
import org.zkoss.zul.Row;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;
import org.zkoss.zul.event.PagingEvent;

/**
 *
 * @author user
 */
public class PlanoCurricularController extends GenericForwardComposer {

    @WireVariable
    private final CRUDService csimp = (CRUDService) SpringUtil.getBean("CRUDService");

    private static final long serialVersionUID = 43014628867656917L;

    private Window windowFaculdade, mDialogAddFaculdade, mDialogAddACient, windowACient, mDialogAddCurso, windowCurso,
            mDialogAddDiscip, windowDiscip, winDiscDetalhe, windowPrecedencia, winPrecDialog;
    private Textbox txtDescricao, txtLocalizacao, txtAbreviatura, txtAcient, txtCurso, txtAbrevCurso, txtCodCurso,
            txtAbrevDisc, txtNomeDisc, txtDpto, txtareaObjGeral, txtCod, txtnatraso, txtCodigo;
    //private Textarea txtareaObjGeral;
    private Combobox cbturno, cbplano, cbdirector, cbdelegacao, cbfaculdade, cbPeriodDisc, cbcaracter, cbareacient, cbPlanoP, cbCursoPrec, cbdiscipPre, cbSemPrec, cbprec;
    private Intbox fid, aid, cid, intSemestre, intDiscNivel, intcredito, did, txtPlano, txtPlanoD, ibPlanoP;
    private Listbox lbFaculdade, lbACient, lbCurso, lbDiscip, lbCursoPrec, lbprec;
    private Label validationFac, validationAcient, validationCurso, validationDisc;
    private Paging pagDisc;
    private Listheader lhnome;
    private String ord = " order by d.nivel, d.semestre";
    private String sql = "";
    // Doublebox d;
    Checkbox ch;
    Row rw;
    String cond;
    Users usr = (Users) Sessions.getCurrent().getAttribute("user");
    private Hlayout ahead;
    String condfac = "", condnr = "", condnome = "", condgenero = "", condanoi = "", condano = "", condcurso = "";
    Textbox txProcurar, txProcNrmec;
    Map<String, Object> par = new HashMap<String, Object>();
    Map<String, Object> condpar = new HashMap<String, Object>();
    Combobox cbcurso;
    private Intbox ibProcAno, iddisc, ibitem;
    private Button btv;
    Menuitem manoi;
    private Window windowDeleg, mDialogAddDeleg;
    private Button addDeleg;
    private Listbox lbDeleg;
    private Textbox txtDeleg, procdeleg;
    private Label validationDeleg;
    private Intbox deid;

    private Intbox poscf;
    private String pesqf = null, condnf = "";
    Map<String, Object> cbparf = new HashMap<String, Object>();
    private int indcf = -1;

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        if (lbDiscip != null) {
            addeventoOrd(lbDiscip, null, null);
            if (usr.getFaculdade().getLocalizacao() == null) {
                cbfaculdade.setVisible(true);
                cbfaculdade.setModel(getFaculdadeModel());
            }
            condpar.clear();
            condcurso = " and d.curso = :curso ";
            ListModel lm = cbcurso.getModel();
            if (lm != null && lm.getSize() > 0) {
                condpar.put("curso", (Curso) lm.getElementAt(0));
                List<Planocurricular> lp = csimp.findByJPQuery("from Planocurricular p where p.curso = :curso", condpar);
                ((Combobox)mDialogAddDiscip.getFellow("cbplano")).setModel(new ListModelList<Planocurricular>(lp));
                setLB(0, 20);
            }
        }
        if (lbCurso != null) {
            if (usr.getFaculdade().getLocalizacao() == null) {
                cbfaculdade.setVisible(true);
                cbfaculdade.setModel(getFaculdadeModel());
            }
        }
        if (poscf != null) {
            poscf.setValue(0);
        }
    }

    public ListModel<Funcionario> getFuncionarioModel() {
        List<Funcionario> lf = csimp.getAll(Funcionario.class);
        return new ListModelList<Funcionario>(lf);
    }

    public ListModel<Faculdade> getFaculdadeModel() {
        List<Faculdade> faculdades = csimp.getAll(Faculdade.class);
        return new ListModelList<Faculdade>(faculdades);
    }

    public void onClick$addFaculdade() {
        mDialogAddFaculdade.setParent(windowFaculdade);
        mDialogAddFaculdade.setTitle("Adicionar Faculdade");
        //  ((Row) mDialogAddUser.getFellow("row1user")).setVisible(true);
        ((Combobox) mDialogAddFaculdade.getFellow("cbdelegacao")).setValue("----Delegacao-----");
        ((Combobox) mDialogAddFaculdade.getFellow("cbdirector")).setValue("----Director-----");
        mDialogAddFaculdade.doModal();
    }

    public void onClick$cancelFaculdade(Event e) {
        clearFormAddFaculdade();
        addFaculdadeConstraint();
        mDialogAddFaculdade.detach();
    }

    public void onClick$saveFaculdade() {
        Faculdade f = new Faculdade();
        if (!(mDialogAddFaculdade.getTitle().charAt(0) == 'E')) {
            f.setCodigo(txtCodigo.getValue());
            f.setDesricao(txtDescricao.getValue());
            f.setLocalizacao(txtLocalizacao.getValue());
            f.setAbreviatura(txtAbreviatura.getValue());
            if (cbdelegacao.getSelectedItem() == null) {
                cbdelegacao.setText("");
                cbdelegacao.getText();
            }
            f.setIddelegacao(((Delegacao) cbdelegacao.getSelectedItem().getValue()));
            if (cbdirector.getSelectedItem() == null) {
                cbdirector.setText("");
                cbdirector.getText();
            }
            f.setDirector(((Funcionario) cbdirector.getSelectedItem().getValue()));

            if (!csimp.exist(f)) {
                csimp.Save(f);
                Clients.showNotification(" adicionado com sucesso", null, null, null, 2000);
            } else {
                validationFac.setValue(f.getDesricao() + " ja se encontra cadastrado no sistema");
                return;
            }
        } else {
            Map<String, Object> par = new HashMap<>();
            par.put("f", fid.getValue());
            // f = csimp.GetUniqueEntityByNamedQuery("Faculdade.findByIdFaculdade", fid.getValue());
            f = (Faculdade) csimp.findByJPQuery("from Faculdade f where f.idFaculdade = :f", par).get(0);
            f.setDesricao(txtDescricao.getValue());
            f.setLocalizacao(txtLocalizacao.getValue());
            f.setAbreviatura(txtAbreviatura.getValue());
            f.setCodigo(txtCodigo.getValue());
            if (cbdelegacao.getSelectedItem() == null) {
                cbdelegacao.setText("");
                cbdelegacao.getText();
            }
            f.setIddelegacao(((Delegacao) cbdelegacao.getSelectedItem().getValue()));
            if (cbdirector.getSelectedItem() == null) {
                cbdirector.setText("");
                cbdirector.getText();
            }
            f.setDirector(((Funcionario) cbdirector.getSelectedItem().getValue()));
            csimp.update(f);
            Clients.showNotification(f.getDesricao() + " modificado com sucesso", null, null, null, 2000);
        }
        lbFaculdade.setModel(getFaculdadeModel());
        clearFormAddFaculdade();
        addFaculdadeConstraint();
        mDialogAddFaculdade.detach();

    }

    public void onEditFaculdade(ForwardEvent evt) throws Exception {
        Button btn = (Button) evt.getOrigin().getTarget();
        Listitem litem = (Listitem) btn.getParent().getParent();
        Faculdade f = (Faculdade) litem.getValue();
        int d = f.getDirector().getIdFuncionario().intValue();
        int de = 0;
        if (f.getIddelegacao() != null) {
            de = f.getIddelegacao().getIddelegacao();
        } // se aceita campos nulos
        mDialogAddFaculdade.setParent(windowFaculdade);
        mDialogAddFaculdade.setTitle("Editar Faculdade");
        mDialogAddFaculdade.doModal();
        ((Intbox) mDialogAddFaculdade.getFellow("fid")).setValue(f.getIdFaculdade());
        ((Textbox) mDialogAddFaculdade.getFellow("txtDescricao")).setText(f.getDesricao());
        ((Textbox) mDialogAddFaculdade.getFellow("txtAbreviatura")).setText(f.getAbreviatura());
        ((Textbox) mDialogAddFaculdade.getFellow("txtLocalizacao")).setText(f.getLocalizacao());

//        Comboitem ci = new Comboitem();
//        for (Comboitem cit : cbdirector.getItems()) {
//            if (((Funcionario) cit.getValue()).getIdFuncionario() == f.getDirector().getIdFuncionario()) {
//        ((Combobox) mDialogAddFaculdade.getFellow("cbdirector")).setValue("------Director------");//setSelectedItem(cit);
//            }
//        }
        final Iterator<Comboitem> items = new ArrayList(((Combobox) mDialogAddFaculdade.getFellow("cbdirector")).getItems()).listIterator();
        Comboitem cit;
        while (items.hasNext()) {
            cit = items.next();
            if (((Funcionario) cit.getValue()).getIdFuncionario() == d) {
                ((Combobox) mDialogAddFaculdade.getFellow("cbdirector")).setSelectedItem(cit);
                break;
            }
        }
        ((Textbox) mDialogAddFaculdade.getFellow("txtCodigo")).setText(f.getCodigo());//na manutencao fica ultimo
        if (f.getIddelegacao() != null) {
            final Iterator<Comboitem> items1 = new ArrayList(((Combobox) mDialogAddFaculdade.getFellow("cbdelegacao")).getItems()).listIterator();
            Comboitem cit1;

            while (items1.hasNext()) {
                cit1 = items1.next();
                if (((Delegacao) cit1.getValue()).getIddelegacao() == de) {
                    ((Combobox) mDialogAddFaculdade.getFellow("cbdelegacao")).setSelectedItem(cit1);
                    break;
                }
            }
        }
    }

    public void onDeleteFaculdade(ForwardEvent evt) throws Exception {

        Button btn = (Button) evt.getOrigin().getTarget();
        Listitem litem = (Listitem) btn.getParent().getParent();

        Faculdade f = (Faculdade) litem.getValue();
        csimp.delete(f);
        lbFaculdade.setModel(getFaculdadeModel());
        Clients.showNotification(" apagado com sucesso", null, null, null, 2000);
    }

    private void clearFormAddFaculdade() {
        Constraint c = null;
        txtCodigo.setConstraint(c);
        txtDescricao.setConstraint(c);
        txtAbreviatura.setConstraint(c);
        cbdirector.setConstraint(c);
        txtDescricao.setValue("");
        txtAbreviatura.setValue("");
        cbdelegacao.setConstraint(c);
        //cbdirector.setValue("----Director-----");
    }

    private void addFaculdadeConstraint() {
        txtCodigo.setConstraint(" no Empty: Insira o Codigo!");
        txtDescricao.setConstraint(" no Empty: Insira o Nome da Faculdade!");
        txtAbreviatura.setConstraint(" no Empty: Insira uma abreviatura!");
        cbdirector.setConstraint(" no Empty: Seleccione um Funcionario!");
        cbdelegacao.setConstraint(" no Empty: Seleccione uma Delegacao!");
    }
//.............................................................................................................................................

    public ListModel<Areacientifica> getAcientModel() {
        List<Areacientifica> la = csimp.getAll(Areacientifica.class);
        return new ListModelList<Areacientifica>(la);
    }

    public void onClick$addACient() {
        mDialogAddACient.setParent(windowACient);
        mDialogAddACient.setTitle("Adicionar Area cientifica");
        mDialogAddACient.doModal();
    }

    public void onClick$cancelACient(Event e) {
        clearFormAddACient();
        AddACientConstraint();
        mDialogAddACient.detach();
    }

    public void onClick$saveACient() {
        Areacientifica a = new Areacientifica();
        if (!(mDialogAddACient.getTitle().charAt(0) == 'E')) {
            a.setDescricao(txtAcient.getValue());

            if (!csimp.exist(a)) {
                csimp.Save(a);
                Clients.showNotification(" adicionado com sucesso", null, null, null, 2000);
            } else {
                Clients.showNotification(a.getDescricao() + " Ja se encontra cadastrado", Messagebox.EXCLAMATION, null, null, 2000);
                return;
            }
        } else {
            a = csimp.get(Areacientifica.class, aid.getValue().longValue());
            a.setDescricao(txtAcient.getValue());
            if (!csimp.exist(a)) {
                csimp.update(a);
                Clients.showNotification(" adicionado com sucesso", null, null, null, 2000);
            } else {
                Clients.alert("Ja se encontra cadastrado");

                return;
            }
            Clients.showNotification(a.getDescricao() + " modificado com sucesso", null, null, null, 2000);
        }
        lbACient.setModel(getAcientModel());
        clearFormAddACient();
        AddACientConstraint();

        mDialogAddACient.detach();

    }

    public void onEditACient(ForwardEvent evt) throws Exception {
        Button btn = (Button) evt.getOrigin().getTarget();
        Listitem litem = (Listitem) btn.getParent().getParent();
        Areacientifica a = (Areacientifica) litem.getValue();
        mDialogAddACient.setParent(windowACient);
        mDialogAddACient.setTitle("Editar Area cientifica");
        mDialogAddACient.doModal();
        ((Intbox) mDialogAddACient.getFellow("aid")).setValue(a.getIdarea().intValue());
        ((Textbox) mDialogAddACient.getFellow("txtAcient")).setText(a.getDescricao());
    }

    public void onDeleteACient(ForwardEvent evt) throws Exception {

        Button btn = (Button) evt.getOrigin().getTarget();
        Listitem litem = (Listitem) btn.getParent().getParent();

        Areacientifica a = (Areacientifica) litem.getValue();
        a = csimp.get(Areacientifica.class, a.getIdarea());
        csimp.delete(a);
        lbACient.setModel(getAcientModel());
        Clients.showNotification(" apagado com sucesso", null, null, null, 2000);
    }

    private void clearFormAddACient() {
        Constraint c = null;
        txtAcient.setConstraint(c);
        txtAcient.setValue("");
    }

    private void AddACientConstraint() {
        txtAcient.setConstraint(" no Empty: Insira uma area cientifica!");
    }

    //............................................................................................................
    public void onSelectcbfaculdadeCurso() {
        if (cbfaculdade.getSelectedItem() != null) {
            //cbcurso.setModel(new ListModelList<Curso>());
            Faculdade f = (Faculdade) cbfaculdade.getSelectedItem().getValue();
            f = csimp.get(Faculdade.class, f.getIdFaculdade());
            lbCurso.setModel(new ListModelList<Curso>(f.getCursoList()));
        }

    }

    public ListModel<Periodo> getturnoModel() {
        return new ListModelList<Periodo>(csimp.getAll(Periodo.class));
    }

    public ListModel<Curso> getCursoModel() {
        // return cursoModel;
//        Map<String, Object> par = new HashMap<>();
//        par.put("idf", usr.getUtilizador());
        //Faculdade f = ((Users) csimp.findByJPQuery("from Users u where u.utilizador = :idf", par).get(0)).getFaculdade();
        Faculdade f = csimp.get(Faculdade.class, usr.getFaculdade().getIdFaculdade());
        return new ListModelList<Curso>(f.getCursoList());
    }

    public void onClick$addCurso() {
        mDialogAddCurso.setParent(windowCurso);
        mDialogAddCurso.setTitle("Adicionar Curso");
        ((Combobox) mDialogAddCurso.getFellow("cbfaculdade")).setValue("----Faculdade----");
        ((Combobox) mDialogAddCurso.getFellow("cbturno")).setValue("------------ Periodo -----------");
        mDialogAddCurso.doModal();
    }

    public void onCancelCurso(Event e) {
        clearFormAddCurso();
        addCursoConstraint();
        mDialogAddCurso.detach();
    }

    public void onSaveCurso() {
        Curso c = new Curso();
        if (!(mDialogAddCurso.getTitle().charAt(0) == 'E')) {
            c.setCodigoCurso(txtCodCurso.getValue());
            c.setAbreviatura(txtAbrevCurso.getValue());
            c.setQtdSemestres(intSemestre.getValue());
            c.setDescricao(txtCurso.getValue());
            c.setPlanoc(txtPlano.getValue());
            c.setTurno(((Periodo) cbturno.getSelectedItem().getValue()));
            if (cbfaculdade.getSelectedItem() == null) {
                cbfaculdade.setText("");
                cbfaculdade.getText();
            }
            c.setFaculdade(((Faculdade) cbfaculdade.getSelectedItem().getValue()));
            if (!csimp.exist(c)) {
                csimp.Save(c);
//                par.clear();
//                par.put("c", c);
//                par.put("a", c.getPlanoc());
//                Planocurricular pc = csimp.findEntByJPQuery("from Planocurricular p where p.curso = :c and p.planocurricularPK.ano=:a", par);
//                if (pc == null) {
//                    pc = new Planocurricular(new PlanocurricularPK(c.getPlanoc(), c.getIdCurso()));
//                    pc.setCurso(c);
//                    csimp.Save(pc);
//                }
                Clients.showNotification(" adicionado com sucesso", null, null, null, 2000);
            } else {
                validationCurso.setValue(c.getDescricao() + " ja se encontra cadastrado no sistema");
                return;
            }
        } else {
            c = csimp.get(Curso.class, cid.getValue().longValue());
            int oldplano = c.getPlanoc();
            c.setCodigoCurso(txtCodCurso.getValue());
            c.setAbreviatura(txtAbrevCurso.getValue());
            c.setQtdSemestres(intSemestre.getValue());
            c.setDescricao(txtCurso.getValue());
            c.setPlanoc(txtPlano.getValue());
            if (cbfaculdade.getSelectedItem() == null) {
                cbfaculdade.setText("");
                cbfaculdade.getText();
            }
            c.setFaculdade(((Faculdade) cbfaculdade.getSelectedItem().getValue()));
            if (cbturno.getSelectedItem() == null) {
                cbturno.setText("");
                cbturno.getText();
            }
            c.setTurno(((Periodo) cbturno.getSelectedItem().getValue()));
            csimp.update(c);
//            par.clear();
//            par.put("c", c);
//            par.put("a", c.getPlanoc());
//            Planocurricular pc = csimp.findEntByJPQuery("from Planocurricular p where p.curso = :c and p.planocurricularPK.ano=:a", par);
//            if (pc == null) {
//                pc = new Planocurricular(new PlanocurricularPK(oldplano, c.getIdCurso()));
//                pc.setCurso(c);
//                csimp.Save(pc);
//            }
//            if (c.getPlanoc() != oldplano) {
//                par.clear();
//                par.put("c", c);
//                par.put("a", oldplano);
//                Disciplina d = csimp.findEntByJPQuery("from Disciplina d where d.curso = :c and d.planoc = :a", par);
//                if (d == null) {
//                    pc = csimp.get(Planocurricular.class, new PlanocurricularPK(c.getPlanoc(), c.getIdCurso()));
//                    if (pc != null) {
//                        csimp.delete(pc);
//                    }
//                }
//            }
            Clients.showNotification(c.getDescricao() + " modificado com sucesso", null, null, null, 2000);
        }
//        cursos = csimp.getAll(Curso.class);
//        cursoModel = new ListModelList<Curso>(cursos);
        lbCurso.setModel(getCursoModel());
        clearFormAddCurso();
        addCursoConstraint();

        mDialogAddCurso.detach();

    }

    public void onEditCurso(ForwardEvent evt) throws Exception {
        Button btn = (Button) evt.getOrigin().getTarget();
        Listitem litem = (Listitem) btn.getParent().getParent();
        Curso c = (Curso) litem.getValue();
        int turno = c.getTurno().getIdPeriodo();
        int f = c.getFaculdade().getIdFaculdade();
        String s = c.getFaculdade().getDesricao();
        mDialogAddCurso.setParent(windowCurso);
        mDialogAddCurso.setTitle("Editar Curso");
        mDialogAddCurso.doModal();
        ((Intbox) mDialogAddCurso.getFellow("cid")).setValue(c.getIdCurso().intValue());
        ((Textbox) mDialogAddCurso.getFellow("txtCodCurso")).setText(c.getCodigoCurso());
        ((Textbox) mDialogAddCurso.getFellow("txtCurso")).setText(c.getDescricao());
        ((Textbox) mDialogAddCurso.getFellow("txtAbrevCurso")).setText(c.getAbreviatura());
        ((Intbox) mDialogAddCurso.getFellow("intSemestre")).setValue(c.getQtdSemestres());
        ((Intbox) mDialogAddCurso.getFellow("txtPlano")).setValue(c.getPlanoc());
        // Comboitem ci = new Comboitem();
//        for (Comboitem cit : cbfaculdade.getItems()) {
//            if (((Faculdade) cit.getValue()).getIdFaculdade() == f) {
//                ((Combobox) mDialogAddCurso.getFellow("cbfaculdade")).setSelectedItem(cit);//.setValue("------Faculdade------");//
//                break;
//            }
//        }
        final Iterator<Comboitem> items = new ArrayList(((Combobox) mDialogAddCurso.getFellow("cbfaculdade")).getItems()).listIterator();
        Comboitem cit;
        while (items.hasNext()) {
            cit = items.next();
            if (((Faculdade) cit.getValue()).getIdFaculdade() == f) {
                ((Combobox) mDialogAddCurso.getFellow("cbfaculdade")).setSelectedItem(cit);//.setValue("------Faculdade------");//
                break;
            }
        }

        final Iterator<Comboitem> itemsturno = new ArrayList(((Combobox) mDialogAddCurso.getFellow("cbturno")).getItems()).listIterator();
        Comboitem citturno;
        while (itemsturno.hasNext()) {
            citturno = itemsturno.next();
            if (((Periodo) citturno.getValue()).getIdPeriodo() == turno) {
                ((Combobox) mDialogAddCurso.getFellow("cbturno")).setSelectedItem(citturno);
                break;
            }
        }
    }

    public void onDeleteCurso(ForwardEvent evt) throws Exception {

        Button btn = (Button) evt.getOrigin().getTarget();
        Listitem litem = (Listitem) btn.getParent().getParent();

        Curso c = (Curso) litem.getValue();
        csimp.delete(c);
//        cursos = csimp.getAll(Curso.class);
//        cursoModel = new ListModelList<Curso>(cursos);
        lbCurso.setModel(getCursoModel());
        Clients.showNotification(" apagado com sucesso", null, null, null, 2000);
    }

    private void clearFormAddCurso() {
        Constraint c = null;
        txtCurso.setConstraint(c);
        txtAbrevCurso.setConstraint(c);
        cbfaculdade.setConstraint(c);
        cbturno.setConstraint(c);
        intSemestre.setConstraint(c);
        txtCodCurso.setConstraint(c);
        txtPlano.setConstraint(c);
        txtCodCurso.setValue("");
        txtCurso.setValue("");
        txtAbrevCurso.setValue("");
        //  cbfaculdade.setValue("----Curso----");
        intSemestre.setValue(null);
    }

    private void addCursoConstraint() {
        txtCurso.setConstraint(" no Empty: Insira o nome do Curso!");
        txtAbrevCurso.setConstraint(" no Empty: Insira abreviatura do Curso!");
        cbfaculdade.setConstraint(" no Empty: Seleccione uma Faculdade!");
        cbturno.setConstraint(" no Empty: Seleccione uma Periodo!");
        intSemestre.setConstraint(" no Empty: Insira o total de semestres!");
        txtCodCurso.setConstraint(" no Empty: Insira o codigo do Curso!");
        txtPlano.setConstraint(" no Empty: Insira o ultimo Plano curricular (Ano)!");
    }
    //............................................................................

    public void onSelectcbfaculdadeDiscip() {
        if (cbfaculdade.getSelectedItem() != null) {
            //cbcurso.setModel(new ListModelList<Curso>());
            Faculdade f = (Faculdade) cbfaculdade.getSelectedItem().getValue();
            f = csimp.get(Faculdade.class, f.getIdFaculdade());
            List<Curso> lc = f.getCursoList();
            cbcurso.setModel(new ListModelList<Curso>(lc));
            condpar.clear();
            condcurso = " and d.curso = :curso ";
            if (lc != null && lc.size() > 0) {
                if (condpar.containsKey("curso")) {
                    condpar.replace("curso", (Curso) lc.get(0));
                } else {
                    condpar.put("curso", (Curso) lc.get(0));
                }
                setLB(0, 20);
            }
        }

    }

    public ListModel<Curso> getCursoDiscModel() {
        if (usr.getFaculdade().getLocalizacao() != null) {
            Faculdade f = csimp.get(Faculdade.class, usr.getFaculdade().getIdFaculdade());
            par.clear();
            par.put("fac", f);
            List<Curso> lp = csimp.findByJPQuery("from Curso c where c.faculdade = :fac", par);
            return new ListModelList<Curso>(lp);
        } else {
            List<Curso> lp = csimp.getAll(Curso.class);
            return new ListModelList<Curso>(lp);
        }
    }

    public void onSetPlano() {
        if (cbplano.getModel() == null) {
            par.clear();
            if (cbcurso.getSelectedItem() == null) {
                cbcurso.setText("");
                cbcurso.getValue();
            }
            par.put("c", ((Curso) cbcurso.getSelectedItem().getValue()));
            List<Planocurricular> lp = csimp.findByJPQuery("from Planocurricular p where p.curso = :c", par);
            cbplano.setModel(new ListModelList<Planocurricular>(lp));
        }
    }

    public void onSelcurso() {
        par.clear();
        par.put("c", ((Curso) cbcurso.getSelectedItem().getValue()));
        List<Planocurricular> lp = csimp.findByJPQuery("from Planocurricular p where p.curso = :c", par);
        ((Combobox)mDialogAddDiscip.getFellow("cbplano")).setModel(new ListModelList<Planocurricular>(lp));
    }

    public ListModel<Disciplina> getDiscipModel() {
        //return disciplinaModel;
//        Map<String, Object> par = new HashMap<>();
//        par.put("idf", usr.getUtilizador());
//        Faculdade f = ((Users) csimp.findByJPQuery("from Users u where u.utilizador = :idf", par).get(0)).getFaculdade();
        Faculdade f = csimp.get(Faculdade.class, usr.getFaculdade().getIdFaculdade());
        par.clear();
        par.put("idf", f);
        List<Disciplina> disc = csimp.findByJPQuery("from Disciplina d where d.curso.faculdade = :idf", par);
        return new ListModelList<Disciplina>(disc);
    }

    public ListModel<Caracter> getCaractModel() {
        List<Caracter> lc = csimp.getAll(Caracter.class);
        return new ListModelList<Caracter>(lc);
    }

    public void onAddDiscip() {
        mDialogAddDiscip.setParent(windowDiscip);
        mDialogAddDiscip.setTitle("Adicionar Disciplina");
        ((Combobox) mDialogAddDiscip.getFellow("cbPeriodDisc")).setValue("------Periodo------");
        ((Combobox) mDialogAddDiscip.getFellow("cbcaracter")).setValue("------Caracter------");
        ((Combobox) mDialogAddDiscip.getFellow("cbcurso")).setValue("------Curso------");
        //   ((Combobox) mDialogAddDiscip.getFellow("cbareacient")).setValue("------Area  Cientifica------");
        ((Combobox) mDialogAddDiscip.getFellow("cbplano")).setValue("------Plano Curricular------");
        mDialogAddDiscip.doModal();
    }

    public void onCancelDiscip(Event e) {
        clearFormAddDiscip();
        addDiscipConstraint();
        mDialogAddDiscip.detach();
    }

    public void onSaveDiscip() {
        Disciplina d = new Disciplina();
        if (!(mDialogAddDiscip.getTitle().charAt(0) == 'E')) {
            d.setCodigo(txtCod.getValue());
            d.setAbreviatura(txtAbrevDisc.getValue());
            d.setNome(txtNomeDisc.getValue());
            d.setNivel(intDiscNivel.getValue());
            d.setNatraso(txtnatraso.getValue());
            if (cbPeriodDisc.getSelectedItem() == null) {
                cbPeriodDisc.setText("");
                cbPeriodDisc.getText();
            }
            d.setSemestre(Integer.parseInt(cbPeriodDisc.getSelectedItem().getValue().toString()));
            d.setCredito(intcredito.getValue());
            if (cbplano.getSelectedItem() == null) {
                cbplano.setText("");
                cbplano.getText();
            }
            d.setPlanoc(((Planocurricular) cbplano.getSelectedItem().getValue()).getPlanocurricularPK().getAno());
            if (cbcurso.getSelectedItem() == null) {
                cbcurso.setText("");
                cbcurso.getText();
            }
            d.setCurso((Curso) cbcurso.getSelectedItem().getValue());
            d.setDepartamento(null);//txtDpto.getValue());
            if (cbcaracter.getSelectedItem() == null) {
                cbcaracter.setText("");
                cbcaracter.getText();
            }
            d.setCaracter((Caracter) cbcaracter.getSelectedItem().getValue());
            d.setObjcetivoGeral(null);//txtareaObjGeral.getValue());
//            if (cbareacient.getSelectedItem() == null) {
//                cbareacient.setText("");
//                cbareacient.getText();
//            }
            d.setAreaCientifica(null);//(Areacientifica) cbareacient.getSelectedItem().getValue());
            if (!csimp.exist(d)) {
                csimp.Save(d);
                par.clear();
//                par.put("c", d.getCurso());
//                int plano = d.getPlanoc();
//                par.put("a", plano);
//                Planocurricular pc = csimp.findEntByJPQuery("from Planocurricular p where p.curso = :c and p.planocurricularPK.ano=:a", par);
//                if (pc == null) {
//                    pc = new Planocurricular(new PlanocurricularPK(plano, d.getCurso().getIdCurso()));
//                    pc.setCurso(d.getCurso());
//                    csimp.Save(pc);
//                }
                Clients.showNotification(" adicionado com sucesso", null, null, null, 2000);
            } else {
                validationDisc.setValue(d.getNome() + " ja se encontra cadastrado no sistema");
                return;
            }
            clearFormAddDiscip();
            addDiscipConstraint();
            lbDiscip.setRows(lbDiscip.getRows() + 1);
            ((ListModelList) lbDiscip.getModel()).add(0, d);
            // lbDiscip.setModel(getDiscipModel());
        } else {
//            Map<String,Object> par = new HashMap<>();
//            par.put("d",did.getValue().longValue()); 
//            d = (Disciplina) csimp.findByJPQuery("from Disciplina d where d.idDisc = :d", par).get(0);
            d = csimp.get(Disciplina.class, did.getValue().longValue());
            int oldplano = d.getPlanoc();
            d.setCodigo(txtCod.getValue());
            d.setAbreviatura(txtAbrevDisc.getValue());
            d.setNome(txtNomeDisc.getValue());
            d.setNivel(intDiscNivel.getValue());
            d.setNatraso(txtnatraso.getValue());
            if (cbPeriodDisc.getSelectedItem() == null) {
                cbPeriodDisc.setText("");
                cbPeriodDisc.getText();
            }
            d.setSemestre(Integer.parseInt(cbPeriodDisc.getSelectedItem().getValue().toString()));
            d.setCredito(intcredito.getValue());
            if (cbplano.getSelectedItem() == null) {
                cbplano.setText("");
                cbplano.getText();
            }
            d.setPlanoc(((Planocurricular) cbplano.getSelectedItem().getValue()).getPlanocurricularPK().getAno());
            if (cbcurso.getSelectedItem() == null) {
                cbcurso.setText("");
                cbcurso.getText();
            }
            d.setCurso((Curso) cbcurso.getSelectedItem().getValue());
            //d.setDepartamento(txtDpto.getValue());
            if (cbcaracter.getSelectedItem() == null) {
                cbcaracter.setText("");
                cbcaracter.getText();
            }
            d.setCaracter((Caracter) cbcaracter.getSelectedItem().getValue());
            // d.setObjcetivoGeral(txtareaObjGeral.getValue());
//            if (cbareacient.getSelectedItem() == null) {
//                cbareacient.setText("");
//                cbareacient.getText();
//            }
            //d.setAreaCientifica((Areacientifica) cbareacient.getSelectedItem().getValue());
            csimp.update(d);
//            if (d.getPlanoc() != oldplano) {
//                par.clear();
//                par.put("c", d.getCurso());
//                par.put("a", oldplano);
//                Disciplina di = csimp.findEntByJPQuery("from Disciplina d where d.curso = :c and d.planoc = :a", par);
//                if (di == null) {
//                    Planocurricular pc = csimp.get(Planocurricular.class, new PlanocurricularPK(oldplano, d.getCurso().getIdCurso()));
//                    if (pc != null) {
//                        csimp.delete(pc);
//                    }
//                }
//                par.clear();
//                par.put("c", d.getCurso());
//                int plano = d.getPlanoc();
//                par.put("a", plano);
//                Planocurricular pc = csimp.findEntByJPQuery("from Planocurricular p where p.curso = :c and p.planocurricularPK.ano=:a", par);
//                if (pc == null) {
//                    pc = new Planocurricular(new PlanocurricularPK(plano, d.getCurso().getIdCurso()));
//                    pc.setCurso(d.getCurso());
//                    csimp.Save(pc);
//                }
//            }
            int index = ((ListModelList) lbDiscip.getModel()).indexOf(d);
            if (index >= 0) {
                ((ListModelList) lbDiscip.getModel()).set(index, d);
            }
            Clients.showNotification(d.getNome() + " modificado com sucesso", null, null, null, 2000);
            // lbDiscip.setModel(getDiscipModel());
            clearFormAddDiscip();
            addDiscipConstraint();
            mDialogAddDiscip.detach();
        }

    }

    public void onEditDiscip(ForwardEvent evt) throws Exception {
        Button btn = (Button) evt.getOrigin().getTarget();
        Listitem litem = (Listitem) btn.getParent().getParent();
        Disciplina d = (Disciplina) litem.getValue();
        d = csimp.load(Disciplina.class, d.getIdDisc());
        int s = d.getSemestre();
        int c = d.getCaracter().getIdCaracter().intValue();
        int cu = d.getCurso().getIdCurso().intValue();
        int a = 0;
//        if (d.getAreaCientifica() != null) {
//            a = d.getAreaCientifica().getIdarea().intValue();
//        }
        a = d.getPlanoc();

        mDialogAddDiscip.setParent(windowDiscip);
        mDialogAddDiscip.setTitle("Editar Disciplina");
        mDialogAddDiscip.doModal();
        ((Intbox) mDialogAddDiscip.getFellow("did")).setValue(d.getIdDisc().intValue());
        ((Textbox) mDialogAddDiscip.getFellow("txtAbrevDisc")).setText(d.getAbreviatura());
        ((Textbox) mDialogAddDiscip.getFellow("txtNomeDisc")).setText(d.getNome());
        ((Textbox) mDialogAddDiscip.getFellow("txtCod")).setText(d.getCodigo());
        ((Textbox) mDialogAddDiscip.getFellow("txtnatraso")).setText(d.getNatraso());
        ((Intbox) mDialogAddDiscip.getFellow("intDiscNivel")).setValue(d.getNivel());
        ((Intbox) mDialogAddDiscip.getFellow("intcredito")).setValue(d.getCredito());
        //  ((Textbox) mDialogAddDiscip.getFellow("txtDpto")).setText(d.getDepartamento());
        //  ((Intbox) mDialogAddDiscip.getFellow("txtPlanoD")).setValue(d.getPlanoc());
        // ((Textbox) mDialogAddDiscip.getFellow("txtareaObjGeral")).setText(d.getObjcetivoGeral());
        final Iterator<Comboitem> items = new ArrayList(((Combobox) mDialogAddDiscip.getFellow("cbPeriodDisc")).getItems()).listIterator();
        Comboitem cit;
        while (items.hasNext()) {
            cit = items.next();
            if (Integer.parseInt(cit.getValue().toString()) == s) {
                ((Combobox) mDialogAddDiscip.getFellow("cbPeriodDisc")).setSelectedItem(cit);
                break;
            }
        }
        final Iterator<Comboitem> items1 = new ArrayList(((Combobox) mDialogAddDiscip.getFellow("cbcaracter")).getItems()).listIterator();
        Comboitem cit1;
        while (items1.hasNext()) {
            cit1 = items1.next();
            if (((Caracter) cit1.getValue()).getIdCaracter() == c) {
                ((Combobox) mDialogAddDiscip.getFellow("cbcaracter")).setSelectedItem(cit1);
                break;
            }
        }
        final Iterator<Comboitem> items2 = new ArrayList(((Combobox) mDialogAddDiscip.getFellow("cbcurso")).getItems()).listIterator();
        Comboitem cit2;
        while (items2.hasNext()) {
            cit2 = items2.next();
            if (((Curso) cit2.getValue()).getIdCurso() == cu) {
                ((Combobox) mDialogAddDiscip.getFellow("cbcurso")).setSelectedItem(cit2);
            }
        }
        final Iterator<Comboitem> items3 = new ArrayList(((Combobox) mDialogAddDiscip.getFellow("cbplano")).getItems()).listIterator();
        Comboitem cit3;
        while (items3.hasNext()) {
            cit3 = items3.next();
            // Messagebox.show((((Planocurricular) cit3.getValue()) + ""));
            if ((((Planocurricular) cit3.getValue()).getPlanocurricularPK().getAno()) == a) {
                ((Combobox) mDialogAddDiscip.getFellow("cbplano")).setSelectedItem(cit3);
            }
        }

//        final Iterator<Comboitem> items3 = new ArrayList(((Combobox) mDialogAddDiscip.getFellow("cbareacient")).getItems()).listIterator();
//        Comboitem cit3;
//        while (items3.hasNext()) {
//            cit3 = items3.next();
//            if (((Areacientifica) cit3.getValue()).getIdarea() == a) {
//                ((Combobox) mDialogAddDiscip.getFellow("cbareacient")).setSelectedItem(cit3);
//            }
//        }
    }

    public void onDeleteDiscip(final ForwardEvent evt) throws Exception {
        Messagebox.show("Apagar?", "Conirmação", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION,
                new EventListener() {
                    @Override
                    public void onEvent(Event evet) {
                        switch (((Integer) evet.getData()).intValue()) {
                            case Messagebox.YES:
                                Button btn = (Button) evt.getOrigin().getTarget();
                                Listitem litem = (Listitem) btn.getParent().getParent();
                                Disciplina d = (Disciplina) litem.getValue();
                                //int oldplano = d.getPlanoc();
                                Curso cu = d.getCurso();
                                new Listbox().appendChild(litem);
                                csimp.delete(d);
//                                par.clear();
//                                par.put("c", cu);
//                                par.put("a", oldplano);
//                                Disciplina di = csimp.findEntByJPQuery("from Disciplina d where d.curso = :c and d.planoc = :a", par);
//                                if (di == null) {
//                                    Planocurricular pc = csimp.get(Planocurricular.class, new PlanocurricularPK(oldplano, cu.getIdCurso()));
//                                    if (pc != null) {
//                                        csimp.delete(pc);
//                                    }
//                                }
                                Clients.showNotification(" apagado com sucesso", null, null, null, 2000);
                                break;
                            case Messagebox.NO:
                                return;
                        }
                    }
                });
    }

    private void clearFormAddDiscip() {
        Constraint c = null;
        txtAbrevDisc.setConstraint(c);
        txtNomeDisc.setConstraint(c);
        intDiscNivel.setConstraint(c);
        cbPeriodDisc.setConstraint(c);
        intcredito.setConstraint(c);
        cbcurso.setConstraint(c);
        // txtDpto.setConstraint(c);
        // txtPlanoD.setConstraint(c);
        cbplano.setConstraint(c);
        //  cbareacient.setConstraint(c);
        // txtareaObjGeral.setConstraint(c);
        txtAbrevDisc.setValue(null);
        txtnatraso.setValue(null);
        txtNomeDisc.setValue(null);
        intDiscNivel.setValue(null);
        //cbPeriodDisc.setValue("----Periodo----");
        intcredito.setValue(null);
        //cbcurso.setValue("----Periodo----");
        // txtDpto.setValue(null);
        //cbareacient.setValue("---Area Cirntifica---");
        // txtareaObjGeral.setValue(null);
    }

    private void addDiscipConstraint() {
        txtAbrevDisc.setConstraint(" no Empty: Insira a abreviatura!");
        txtNomeDisc.setConstraint(" no Empty: Insira o nome da disciplina!");
        intDiscNivel.setConstraint(" no Empty: Insira o nivel da disciplina!");
        cbPeriodDisc.setConstraint(" no Empty: Insira o periodo semestral!");
        intcredito.setConstraint(" no Empty: Insira o credito da disciplina!");
        cbcurso.setConstraint(" no Empty: Seleccione um curso!");
        cbplano.setConstraint(" no Empty: Seleccione um plano!");
        //txtDpto.setConstraint(" no Empty: Insira um Departamento!");
        // txtPlanoD.setConstraint("no Empty: Insira o plano curricular (Ano)!");
        //cbareacient.setConstraint(" no Empty: Seleccione a Area Cientifica da disciplina!");
    }

    public void onDetal(ForwardEvent evt) throws Exception {
        Button btn = (Button) evt.getOrigin().getTarget();
        Listitem litem = (Listitem) btn.getParent().getParent();
        Disciplina d = (Disciplina) litem.getValue();
        d = csimp.load(Disciplina.class, d.getIdDisc());
        String c = d.getCaracter().getDescricao();
        String ac = "";
        if (d.getAreaCientifica() != null) {
            ac = d.getAreaCientifica().getDescricao();
        }
        String og = d.getObjcetivoGeral();
        winDiscDetalhe.setParent(windowDiscip);
        winDiscDetalhe.doModal();
        ((Textbox) winDiscDetalhe.getFellow("txtDCarater")).setText(c);
        ((Textbox) winDiscDetalhe.getFellow("txtDAreaCient")).setText(ac);
        ((Textbox) winDiscDetalhe.getFellow("txtDObjGeral")).setValue(og);
    }

    public void onClick$btnCancDetal() {
        winDiscDetalhe.detach();
    }
    //....................................................................................................................................................................................

    public void onChange$cbCursoPrec(Event event) {
        if (cbCursoPrec.getSelectedItem() != null) {
            Curso c = (Curso) cbCursoPrec.getSelectedItem().getValue();
            par.clear();
            par.put("c", c);
            List<Planocurricular> lc = csimp.findByJPQuery("from Planocurricular p where p.curso = :c order by p.planocurricularPK.ano desc", par);
            cbPlanoP.setModel(new ListModelList<Planocurricular>(lc));
            cbPlanoP.setText(c.getPlanoc() + "");
        }
        selCursoPrec();
    }

    public void onSelect$cbPlanoP() {
        selCursoPrec();
    }

    public void onChange$cbSemPrec(Event envent) {
        selCursoPrec();
    }

//    public void onChange$ccc() {
//        Map<String,Object> par = new HashMap<>();
//        if (cbProcCurso.getSelectedItem()!=null) {
//            Curso curso ((Curso)cb.getSelectedItem().getValue());
//               par.put("curso", curso);
//               cond = cond + " and o.curso = :curso";
//        } else {
//             cond = "";
//             par.remove("curso");
//        }
//        if(!txProc.getText().equals("")){
//        String nome = txProc.getText();
//        par.put("nome", nome);
//        cond = cond + " and o.nome = :nome";}else{
//         
//        }
//        if(itProc.getValue != null){
//        int ano = itProc.getValue();
//        par.put("ano", ano);
//        cond = cond + "o.ano = :nome";
//        } else {
//           cond = "";
//           par.remove("ano");
//        }
//        Date datam = dabProc.getValue();
//        if(datam!=null){
//        par.put("ano", ano);
//        cond = cond + "o.ano = :nome";
//        }
//        initLB("from Disciplina o " +cond+" "+ ord, lbDiscip, pagDisc, Disciplina.class);
//    } 
    public void selCursoPrec() {
        if (cbCursoPrec.getSelectedItem() == null) {
            return;
        }
        Curso c = (Curso) cbCursoPrec.getSelectedItem().getValue();
        Map<String, Object> par = new HashMap<String, Object>();
        Map<String, Object> ent = new HashMap<String, Object>();
        par.put("cu", c);
        if (cbPlanoP.getSelectedItem() != null) {
            par.put("planoc", ((Planocurricular) cbPlanoP.getSelectedItem().getValue()).getPlanocurricularPK().getAno());
        } else {
            par.put("planoc", c.getPlanoc());// ent.put("d", Disciplina.class);
        }
        String sql = "from Disciplina d where d.curso = :cu and d.planoc = :planoc";
        if (cbSemPrec.getSelectedItem() != null) {
            int s = Integer.parseInt(cbSemPrec.getSelectedItem().getValue().toString());
            if (s != 0) {
                par.put("s", s);
                sql = sql + " and d.semestre = :s";
            }
        }
        List<Disciplina> ldisc = csimp.findByJPQuery(sql, par);
        lbCursoPrec.setModel(new ListModelList<Disciplina>());
        lbCursoPrec.setRows(lbCursoPrec.getItemCount() + ldisc.size() + 1);
//        lbCursoPrec.setModel(new ListModelList<Disciplina>(ldisc));
        final Iterator<Disciplina> items = ldisc.iterator();
        Disciplina e;
        while (items.hasNext()) {
            e = items.next();
            ((ListModelList) lbCursoPrec.getModel()).add(e);
        }
    }

//    public void onApagarPrec(final ForwardEvent evt) {
//        Messagebox.show("Pretende eliminar a precedencia?", "Confirmação", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION,
//                new EventListener() {
//                    @Override
//                    public void onEvent(Event evet) {
//                        switch (((Integer) evet.getData()).intValue()) {
//                            case Messagebox.YES:
//                                Combobox cb = (Combobox) evt.getOrigin().getTarget().getParent().getParent().getChildren().get(2).getChildren().get(0);
//                                // if (cb.getSelectedItem() != null) {
//                                Listitem litem = (Listitem) evt.getOrigin().getTarget().getParent().getParent();//cb.getParent().getParent();
//                                Disciplina d = (Disciplina) litem.getValue();
////                                    Precedencia p = new Precedencia();
////                                    p.setIdDisc(d.getIdDisc());
////                                    p.setDisciplina(d);
////                                    p.setPrecedencia((Disciplina) cb.getSelectedItem().getValue());
//                                Map<String, Object> par = new HashMap<String, Object>();
//                                par.put("d", d.getIdDisc());
//                                Precedencia pre = (Precedencia) csimp.findByJPQuery("from Precedencia p where p.idDisc = :d", par).get(0);
//                                csimp.delete(pre);
//                                Clients.showNotification(" Apagado com sucesso", null, null, null, 2000);
//                                //selCursoPrec();
////                                    List<Disciplina> ldis = new ArrayList<>();
////                                    Disciplina dis = new Disciplina();
////                                    dis.setNome("--------------------------");
////                                    ldis.add(dis);
////                                    cb.setModel(new ListModelList<>(ldis));
//                                cb.setText("");
//                                ((Button) evt.getOrigin().getTarget()).setVisible(false);
//                                ((Button) evt.getOrigin().getTarget().getParent().getChildren().get(0)).setVisible(true);
//                                ((Button) evt.getOrigin().getTarget().getParent().getChildren().get(1)).setVisible(false);
//                                //     }
//                                break;
//                            case Messagebox.NO:
//                                return;
//                        }
//                    }
//                });
//    }
    public void onAddPrec(ForwardEvent evt) {
        Button btn = (Button) evt.getOrigin().getTarget();
        Combobox cb = (Combobox) btn.getParent().getParent().getChildren().get(2).getChildren().get(0);
        Listitem litem = (Listitem) btn.getParent().getParent();
        Disciplina d = (Disciplina) litem.getValue();
        List<Disciplina> ldis = new ArrayList<>();
        Curso c = (Curso) cbCursoPrec.getSelectedItem().getValue();
        Map<String, Object> par = new HashMap<String, Object>();
        par.put("cu", c);
        ldis = csimp.findByJPQuery("from Disciplina d where d.curso = :cu", par);
        ldis.remove(d);
        cb.setModel(new ListModelList<Disciplina>(ldis));
        cb.open();
    }

    public void onEditPrec(ForwardEvent evt) {
        Button btn = (Button) evt.getOrigin().getTarget();
        Combobox cb = (Combobox) btn.getParent().getParent().getChildren().get(2).getChildren().get(0);
        Listitem litem = (Listitem) btn.getParent().getParent();
        Disciplina d = (Disciplina) litem.getValue();
        List<Disciplina> ldis = new ArrayList<>();
        Curso c = (Curso) cbCursoPrec.getSelectedItem().getValue();
        Map<String, Object> par = new HashMap<String, Object>();
        par.put("cu", c);
        ldis = csimp.findByJPQuery("from Disciplina d where d.curso = :cu", par);
        ldis.remove(d);
        cb.setModel(new ListModelList<Disciplina>(ldis));
        cb.open();
    }

//    public void onSelPrec(final ForwardEvent evt) {
//        final Combobox cb = (Combobox) evt.getOrigin().getTarget();
//        Messagebox.show("Pretende modificar?", "Confirmação", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION,
//                new EventListener() {
//                    @Override
//                    public void onEvent(Event evet) {
//                        switch (((Integer) evet.getData()).intValue()) {
//                            case Messagebox.YES:
//                                
//                                Listitem litem = (Listitem) cb.getParent().getParent();
//                                Disciplina d = (Disciplina) litem.getValue();
//                                d = csimp.load(Disciplina.class, d.getIdDisc());
//                                Precedencia p = new Precedencia();
//                                p.setIdDisc(d.getIdDisc());
//                                p.setDisciplina(d);
//                                p.setPrecedencia((Disciplina) cb.getSelectedItem().getValue());
//                                Map<String, Object> par = new HashMap<String, Object>();
//                                par.put("d", d.getIdDisc());
//                                if (d.getDisciplinaList() != null && d.getDisciplinaList().size() != 0) {
////                                    if (p.getPrecedencia().getIdDisc() != null) {
//                                    csimp.update(p);
//                                    Clients.showNotification(" Modificado com sucesso", null, null, null, 2000);
////                                    } else {
////                                        Precedencia pre = (Precedencia) csimp.findByJPQuery("from Precedencia p where p.idDisc = :d", par).get(0);
////                                        csimp.delete(pre);
////                                        Clients.showNotification(" Apagado com sucesso", null, null, null, 2000);
////                                    }
//                                } else {
//                                    if (p.getPrecedencia().getIdDisc() != null) {
//                                        csimp.Save(p);
//                                        Clients.showNotification(" Adicionado com sucesso", null, null, null, 2000);
//                                    }
//                                }
//                                ((Button) cb.getParent().getParent().getChildren().get(3).getChildren().get(0)).setVisible(false);
//                                ((Button) cb.getParent().getParent().getChildren().get(3).getChildren().get(1)).setVisible(true);
//                                ((Button) cb.getParent().getParent().getChildren().get(3).getChildren().get(2)).setVisible(true);
//                                //selCursoPrec();
//                                break;
//                            case Messagebox.NO:
//                                if (!((Button) cb.getParent().getParent().getChildren().get(3).getChildren().get(1)).isVisible()) {
//                                    cb.setText("");
//                                } else {
//                                    selCursoPrec();
//                                }
//                                return;
//                        }
//                    }
//                });
//    }
///////////////////////////////////// precedencia ///////////////////////////////////
    public void onOpen$cbprec() {
        if (cbprec.getModel() == null) {
            Disciplina d = csimp.get(Disciplina.class, iddisc.getValue().longValue());
            par.clear();
            par.put("cu", d.getCurso());
            par.put("planoc", ibPlanoP.getValue());
            List<Disciplina> ld = csimp.findByJPQuery("from Disciplina d where d.curso = :cu and d.planoc = :planoc", par);
            ld.remove(d);
            cbprec.setModel(new ListModelList<Disciplina>(ld));
        }
    }

    public void onBtnva() {
        if (cbprec.getSelectedItem() == null) {
            return;
        }
        Disciplina d = (Disciplina) cbprec.getSelectedItem().getValue();
        Disciplina di = new Disciplina(d.getIdDisc());
        if (!((ListModelList) lbprec.getListModel()).contains(di)) {
            ((ListModelList) lbprec.getListModel()).add(d);
        }
    }

    public void onBtnRegistar() {
        Disciplina d = csimp.get(Disciplina.class, iddisc.getValue().longValue());
        Iterator<Precedencia> items = new ArrayList(d.getPrecedenciaList1()).listIterator();
        Precedencia p;
        while (items.hasNext()) {
            p = items.next();
            d.getPrecedenciaList1().remove(p);
            csimp.delete(csimp.get(Precedencia.class, p.getPrecedenciaPK()));
        }
        Precedencia pe;
        Iterator<Listitem> items1 = lbprec.getItems().iterator();
        while (items1.hasNext()) {
            Disciplina di = (Disciplina) items1.next().getValue();
            pe = new Precedencia(new PrecedenciaPK(d.getIdDisc(), di.getIdDisc()));
            pe.setDisciplina(di);
            pe.setDisciplina1(d);
            d.getPrecedenciaList1().add(pe);
        }
        csimp.update(d);
        Clients.showNotification(" Actualizado com sucesso", null, null, null, 2000);
        ((ListModelList) lbCursoPrec.getModel()).set(ibitem.getValue(), d);
        winPrecDialog.detach();
    }

    public void onEditPrecDisc(ForwardEvent evt) {
        Button btn = (Button) evt.getOrigin().getTarget();
        Listitem litem = (Listitem) btn.getParent().getParent();
        Disciplina d = (Disciplina) litem.getValue();
        d = csimp.load(Disciplina.class, d.getIdDisc());
        Window w = (Window) Executions.createComponents("/planoc/formPreced.zul", windowPrecedencia, null);
        ((Intbox) w.getFellow("iddisc")).setValue(d.getIdDisc().intValue());
        ((Intbox) w.getFellow("ibitem")).setValue(litem.getIndex());
        ((Intbox) w.getFellow("ibPlanoP")).setValue(d.getPlanoc());
        ((Textbox) w.getFellow("lbNome")).setValue(d.getNome());
        Listbox docs = ((Listbox) w.getFellow("lbprec"));
        int sl = d.getPrecedenciaList1().size();
        if (sl > 0) {
            docs.setRows(sl + 1);
        }
        List<Precedencia> li = d.getPrecedenciaList1();
        final Iterator<Precedencia> items = li.iterator();
        Disciplina i;
        List<Disciplina> ld = new ArrayList<Disciplina>();
        while (items.hasNext()) {
            i = items.next().getDisciplina();
            ld.add(i);
        }
        docs.setModel(new ListModelList<Disciplina>(ld));
    }

    public void onCancAnul() {
        winPrecDialog.detach();
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////
    public ListModel<Curso> getListaCursoModel() {
        //Users u = csimp.get(Users.class, usr.getUtilizador());
        Faculdade f = csimp.get(Faculdade.class, usr.getFaculdade().getIdFaculdade());
        par.clear();
        par.put("fac", f);
        List<Curso> lc = csimp.findByJPQuery("from Curso c where c.faculdade = :fac", par);
        return new ListModelList<Curso>(lc);
    }

    public void onSortEventListener(Event event, final Listbox lb, Paging p, final Class o) {
        final Listheader lh = (Listheader) event.getTarget();
        final String sortDirection = lh.getSortDirection();

        if ("ascending".equals(sortDirection)) {
            ord = " order by lower(d." + lh.getId() + ") desc";
            //initLB(sql + "order by d." + lh.getId() + " asc", lb, p, o);
            //p.setActivePage(0);
            setLB(0, 20);
        } else if ("descending".equals(sortDirection) || "natural".equals(sortDirection) || Strings.isBlank(sortDirection)) {
            ord = " order by lower(d." + lh.getId() + ") asc";
            // initLB(sql + "order by d." + lh.getId() + " desc", lb, p, o);
            // p.setActivePage(0);
            setLB(0, 20);
        }
    }

    public void addeventoOrd(final Listbox lb, final Paging p, final Class o) {
        List<Listheader> list = lb.getListhead().getChildren();
        for (Object object : list) {
            if (object instanceof Listheader) {
                Listheader lheader = (Listheader) object;
                if (lheader.getSortAscending() != null || lheader.getSortDescending() != null) {
                    lheader.addEventListener("onSort", new EventListener() {
                        public void onEvent(Event event) {
                            onSortEventListener(event, lb, p, o);
                        }
                    });
                }
            }
        }
    }

    public void onSort(Listheader lh, final Listbox lb, Paging p, final Class o) {
        final String sortDirection = lh.getSortDirection();

        if ("ascending".equals(sortDirection)) {
            ord = " order by lower(d." + lh.getId() + ") asc";
//            initLB(sql + "order by d." + lh.getId() + " asc", lb, p, o);
//            p.setActivePage(0);
            setLB(0, 20);
        } else if ("descending".equals(sortDirection)) {
            ord = " order by lower(d." + lh.getId() + ") desc";
//            initLB(sql + "order by d." + lh.getId() + " desc", lb, p, o);
//            p.setActivePage(0);
            setLB(0, 20);
        }
    }

    public void onChanging$txProcNrmec(InputEvent evt) {
        if (!evt.getValue().equals("") && evt.getValue().charAt(0) != '.') {
            condnr = " and d.codigo = :nr ";
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
            condnome = " and lower(d.nome) like :nome ";
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

    public void onChange$cbcurso() {
        // if (cbcurso.getSelectedIndex() != 0) {
        condcurso = " and d.curso = :curso ";
        if (condpar.containsKey("curso")) {
            condpar.replace("curso", (Curso) cbcurso.getSelectedItem().getValue());
        } else {
            condpar.put("curso", (Curso) cbcurso.getSelectedItem().getValue());
        }
        setLB(0, 20);
        onSelcurso();
    }

    public void setLB(int i, int j) {
        if (j == 20) {
            lbDiscip.setModel(new ListModelList<Prescricao>());
        }
        List<Disciplina> li = csimp.findByJPQueryFilter("from Disciplina d where 1=1" + condnome + condnr + condcurso + ord, condpar, i, j);
        final Iterator<Disciplina> items = li.iterator();
        Disciplina e;
        lbDiscip.setRows(lbDiscip.getItemCount() + li.size());
        while (items.hasNext()) {
            e = items.next();
            ((ListModelList) lbDiscip.getModel()).add(e);
        }
        if (li.size() < j) {
            btv.setVisible(false);
        } else {
            btv.setVisible(true);
        }
    }

    public void onLoadi() {
        int i = lbDiscip.getItemCount();
        setLB(i, i + 20);
    }

    public void onExcelExportDisc() throws ParseException {
        if (lbDiscip.getItemCount() == 0) {
            Clients.showNotification("Sem conteúdo", "warning", null, null, 3000);
            return;
        }
        BeanToExcel beanToExcel = new BeanToExcel();
        beanToExcel.setDataSheetName("Disciplinas " + ((Curso) cbcurso.getSelectedItem().getValue()).getDescricao());
        beanToExcel.exportExcell(lbDiscip);
    }
///////////////////////////////// Delegacao //////////////////////////

    public void onSaveDeleg() {
        Delegacao d = new Delegacao();
        if (!(mDialogAddDeleg.getTitle().charAt(0) == 'E')) {
            d.setDelegacao(txtDeleg.getValue());

            if (!csimp.exist(d)) {
                csimp.Save(d);
                Clients.showNotification(" adicionado com sucesso", null, null, null, 2000);
            } else {
                Clients.showNotification(d.getDelegacao() + " Ja se encontra cadastrado", Messagebox.EXCLAMATION, null, null, 2000);
                return;
            }
        } else {
            d = csimp.get(Delegacao.class, deid.getValue());//Depende do tamanho (Se for long convertemos)
            d.setDelegacao(txtDeleg.getValue());
            // if (!csimp.exist(d)) {
            csimp.update(d);
            //    Clients.showNotification(" adicionado com sucesso", null, null, null, 2000);
//            } else {
//                Clients.alert("Ja se encontra cadastrado");
//
//                return;
//            }
            Clients.showNotification(d.getDelegacao() + " modificado com sucesso", null, null, null, 2000);
        }
        lbDeleg.setModel(getDelegModel());
        clearFormAddDeleg();
        AddDelegConstraint();

        mDialogAddDeleg.detach();
    }

    public void onEditDeleg(ForwardEvent evt) {
        Button btn = (Button) evt.getOrigin().getTarget();
        Listitem litem = (Listitem) btn.getParent().getParent();//depende da posicao?
        Delegacao d = (Delegacao) litem.getValue();
        mDialogAddDeleg.setParent(windowDeleg);
        mDialogAddDeleg.setTitle("Editar Delegacao");
        mDialogAddDeleg.doModal();
        ((Intbox) mDialogAddDeleg.getFellow("deid")).setValue(d.getIddelegacao().intValue());//(convertemos sempre para int)
        ((Textbox) mDialogAddDeleg.getFellow("txtDeleg")).setText(d.getDelegacao());
    }

    public void onDeleteDeleg(final ForwardEvent evt) {
        Messagebox.show("Apagar?", "Confirmação", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION,
                new EventListener() {
                    @Override
                    public void onEvent(Event evet) {
                        switch (((Integer) evet.getData()).intValue()) {
                            case Messagebox.YES:
                                Button btn = (Button) evt.getOrigin().getTarget();
                                Listitem litem = (Listitem) btn.getParent().getParent();
                                Delegacao d = (Delegacao) litem.getValue();
                                d = csimp.get(Delegacao.class, d.getIddelegacao());
                                int ind = ((ListModelList) lbDeleg.getModel()).indexOf(d);
                                csimp.delete(d);
                                if (ind >= 0) {
                                    ((ListModelList) lbDeleg.getModel()).remove(ind);
                                }
                                Clients.showNotification(" apagado com sucesso", null, null, null, 2000);
                                break;
                            case Messagebox.NO:
                                return;
                        }
                    }
                });
    }

    public void onCancelDeleg() {
        clearFormAddDeleg();
        AddDelegConstraint();
        mDialogAddDeleg.detach();
    }

    public ListModel<Delegacao> getDelegModel() {
        List<Delegacao> ld = csimp.getAll(Delegacao.class);
        return new ListModelList<Delegacao>(ld);
    }

    public void onClick$addDeleg() {
        mDialogAddDeleg.setParent(windowDeleg);
        mDialogAddDeleg.setTitle("Adicionar Delegacao");
        mDialogAddDeleg.doModal();
    }

    private void clearFormAddDeleg() {
        Constraint c = null;
        txtDeleg.setConstraint(c);
        txtDeleg.setValue("");
    }

    private void AddDelegConstraint() {
        txtDeleg.setConstraint(" no Empty: Insira uma Delegacao!");
    }

    /////////////////////////////////////COMBOBOX FUNCIONARIO//////////////////////////////////////////////////////////////
    public void onSCbfunc() {
        if (cbdirector.getSelectedIndex() == poscf.getValue() - 1) {
            onLoadCombf();
            cbdirector.open();
            cbdirector.setText(pesqf);
            return;
        }
        indcf = cbdirector.getSelectedIndex();
    }

    public void onCbfunc(InputEvent evt) {
        indcf = -1;
        if (!evt.getValue().equals("") && evt.getValue().charAt(0) != '.') {
            pesqf = evt.getValue();
            condnf = " and lower(f.nome) like :nome ";
            if (cbparf.containsKey("nome")) {
                cbparf.replace("nome", "%" + evt.getValue().toLowerCase() + "%");
            } else {
                cbparf.put("nome", "%" + evt.getValue().toLowerCase() + "%");
            }
        } else {
            if (pesqf != null) {
                pesqf = "";
            }
            condnf = "";
            if (!cbparf.containsKey("nome")) {
                return;
            }
            cbparf.remove("nome");
        }
        cbdirector.getItems().clear();
        setLBCombf(0, 20);
    }

    public void onOpen$cbdirector() {
//        if (poscf.getValue() == 0) {
//            cbdirector.getItems().clear();
//            setLBCombf(0, 20);
//        }
        if (pesqf == null) {
            setLBCombf(0, 20);
            pesqf = "";
        } else {
            if (cbdirector.isOpen()) {
                cbdirector.setText(pesqf);
            } else {
                if (indcf != -1) {
                    cbdirector.getSelectedIndex();
                    cbdirector.setSelectedIndex(indcf);
                }
            }
        }
    }

    public void setLBCombf(int i, int j) {
        if (j == 20) {
            cbdirector.setModel(new ListModelList<Funcionario>());
        }
        List<Funcionario> li = null;
        Users u = csimp.get(Users.class, usr.getUtilizador());
        // par.clear();
        Date dano = new Date();
        Calendar cal = new GregorianCalendar();
        cal.setTime(dano);
        int ano = cal.get(Calendar.YEAR);
        li = csimp.findByJPQueryFilter("from Funcionario f where 1=1 " + condnf + " order by f.nome", cbparf, i, j);
        final Iterator<Funcionario> items = li.iterator();
        Funcionario e;
        //lbinscricao.setRows(lbinscricao.getItemCount() + li.size());
        if (j > 20) {
            ((ListModelList) cbdirector.getModel()).remove(new Funcionario());
            new Combobox().appendChild(cbdirector.getItemAtIndex(poscf.getValue() - 1));
        }
        while (items.hasNext()) {
            e = items.next();
            ((ListModelList) cbdirector.getModel()).add(e);
        }
        if (li.size() == j) {
            Funcionario es = new Funcionario();
            es.setNome("-------Ver Mais-------");
            ((ListModelList) cbdirector.getModel()).add(es);
            poscf.setValue(((ListModelList) cbdirector.getModel()).size());
        }
    }

    public void onLoadCombf() {
        int i = ((ListModelList) cbdirector.getModel()).size() - 1;
        setLBCombf(i, i + 20);
    }

}
