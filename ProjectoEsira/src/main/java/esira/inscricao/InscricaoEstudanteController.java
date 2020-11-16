/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package esira.inscricao;

import entidade.Arquivo;
import esira.domain.Arquivoestudante;
import esira.domain.Arquivoinscricao;
import esira.domain.Curso;
import esira.domain.Disciplina;
import esira.domain.Disciplinaanulada;
import esira.domain.Estudante;
import esira.domain.Faculdade;
import esira.domain.Funcionario;
import esira.domain.Grupo;
import esira.domain.Grupoinscricao;
import esira.domain.Inscricao;
import esira.domain.Inscricaodisciplina;
import esira.domain.InscricaodisciplinaPK;
import esira.domain.Matricula;
import esira.domain.PlanificacaoAnoLectivo;
import esira.domain.Planocurricular;
import esira.domain.Prescricao;
import esira.domain.Users;
import esira.domain.Validacaopendente;
import esira.domain.ValidacaopendentePK;
import esira.matricula.GuardarEstudanteController;
import esira.service.CRUDService;
import esira.service.UserAutentic;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import org.apache.commons.io.FilenameUtils;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.zkoss.util.media.AMedia;
import org.zkoss.util.media.Media;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.EventQueue;
import org.zkoss.zk.ui.event.EventQueues;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.event.InputEvent;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.Button;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Constraint;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Doublebox;
import org.zkoss.zul.Doublespinner;
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
import org.zkoss.zul.SimpleConstraint;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Tabpanel;
import org.zkoss.zul.Tabs;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;
import org.zkoss.zul.impl.InputElement;

/**
 *
 * @author Administrator
 */
public class InscricaoEstudanteController extends GenericForwardComposer {

    @WireVariable
    private CRUDService csimpm = (CRUDService) SpringUtil.getBean("CRUDService");
    private Window winAddInscricao, winInscricao, winAddInscricaoDisc, winInscricaoEst, mDialogMatricula, win, winInscEst, winInscEq;
    private Combobox combEstInsc, combSem, cbPeriodDisc2, cbPeriodDisc, cbcurso;
    private ListModel<Estudante> estudantes;
    private ListModel<Inscricao> inscric;
    private ListModel<Disciplina> disciplinas;
    private ListModel<Curso> listaCursoModel;
    private Label lbcurso, lbnivel, lbsexo, lbcred, lbtaxaInscricao, lbtaxaInscricaoE, lbano, lbNumero,
            lbtaxaMulta15dias, lbtaxaMulta30dias, lbtotal, lbsem;
    private Listbox lbdisceq;
    private Tabpanel tpanel1, tpanel2;
    private Tabs tabs;
    Textbox txProcurar, txProcNrmec, txnrTalao, est, tbimg4, txMotivo, txest, txMotEst, txMotDisc;
    Combobox cbdisciplina, combEstu, cbplano;
    private Checkbox chEst, chDisc;
    private Listbox lbestu, lbdisc;
    List listaI;
    Datebox dinsc;
    private static Media mediaCPI;
    private int sem = 0;
    private int indc = -1;
    private Intbox item, ibProcAno, ibano, ibano2, posc, posd;
    Map<String, Object> par = new HashMap<String, Object>();
    Map<String, Object> ent = new HashMap<String, Object>();
    Users usr = (Users) Sessions.getCurrent().getAttribute("user");
    private int ano;
    private String condn = "";
    Map<String, Object> condpar = new HashMap<String, Object>();
    private String pesq = null;
    private EventQueue eq;

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        Date dano = new Date();
        Calendar cal = new GregorianCalendar();
        cal.setTime(dano);
        ano = cal.get(Calendar.YEAR);
        if (ibano != null) {
            ibano.setValue(ano);
        }
        if (ibano2 != null) {
            ibano2.setValue(ano);
        }
        if (posc != null) {
            posc.setValue(0);
        }
    }

    public ListModel<Estudante> getEstInscModel() {
        List<Estudante> estu = null;
       // par.put("user", usr.getUtilizador());
        //Users u = csimpm.findEntByJPQuery("from Users u where u.utilizador = :user", par);
//        if (u != null) {
//            par.clear();
//        }
        //   par.put("ano", ano);
        Faculdade f = csimpm.get(Faculdade.class, usr.getFaculdade().getIdFaculdade());
        par.put("fac", f);
        estu = csimpm.findByJPQuery("from Estudante e where e.cursocurrente.faculdade = :fac", par);
        return new ListModelList<Estudante>(estu);
    }

    //Selecciona as disciplinas que o estudante ainda nao fez controlando as precedencias
    public void onSCombEstu() throws IOException {
        if (combEstu.getSelectedIndex() == posc.getValue() - 1) {

            onLoadComb();
            combEstu.open();
            combEstu.setText(pesq);
        } else {
            indc = combEstu.getSelectedIndex();
            if (combEstu.getSelectedItem() == null) {
                return;
            }
            Estudante e = (Estudante) combEstu.getSelectedItem().getValue();
            par.clear();
            par.put("e", e);
            List<Inscricao> li = csimpm.findByJPQuery("from Inscricao i where i.idEstudante = :e and i.dataConfirmacao is not null and i.estado is false", par);
            if (li != null && !li.isEmpty()) {
                final Iterator<Inscricao> items = new ArrayList(li).listIterator();
                Inscricao in;
                in = items.next();
                final Iterator<Arquivoinscricao> items2 = new ArrayList(in.getArquivoinscricaoList()).listIterator();
                Arquivoinscricao ai;
                while (items.hasNext()) {
                    ai = items2.next();
                    String dat = new SimpleDateFormat("dd_MM_yyyy_HH_mm_ss").format(in.getDataInscricao());
                    String nome = in.getIdEstudante().getNrEstudante() + ai.getNome() + dat;
                    File test = new File(UserAutentic.getPathR("4") + nome);
                    test.delete();
                }
                li.remove(in);
                csimpm.delete(in);
            }
            par.put("ano", ibano.getValue());
            par.put("sem", Short.parseShort(cbPeriodDisc.getSelectedItem().getValue().toString()));
            par.put("curso", e.getCursocurrente());
            par.put("planoc", e.getPlanoc());
            //Traser disciplinas que nao fez ou nao se inscreveu
            List<Disciplina> ldi = csimpm.findByJPQuery("from Disciplina d where d.curso = :curso and d.planoc = :planoc and d not in (select id.disciplina from"
                    + " Inscricaodisciplina id where id.inscricao.idEstudante = :e and id.estado is true and id.disciplinaActiva = 3"
                    + " and ((id.notaFinal is not null and id.notaFinal >= 10) or id.notaFinal is null or (id.notaFinal is not null and id.notaFinal <= 10"
                    + " and extract(year from id.inscricao.dataInscricao) = :ano and id.inscricao.semestre=:sem)))"
                    + " and not exists (select p from d.precedenciaList1 p where p.disciplina not in (select id.disciplina from Inscricaodisciplina"
                    + " id where id.inscricao.idEstudante = :e and id.estado is true and id.disciplinaActiva = 3"
                    + " and ((id.notaFinal is not null and id.notaFinal >= 10))))", par);
            lbdisc.setRows(1);
            lbdisc.setMultiple(false);
            lbdisc.setModel(new ListModelList<Disciplina>(ldi));
            lbdisc.setMultiple(true);
        }
    }

//    public ListModel<Disciplina> getDisciplinaModel() {
//        Map<String, Object> par = new HashMap<>();
//        par.put("idf", usr.getUtilizador());
//        Faculdade f = ((Users) csimpm.findByJPQuery("from Users u where u.utilizador = :idf", par).get(0)).getFaculdade();
//        par.clear();
//        par.put("idf", f);
//        List<Disciplina> disc = csimpm.findByJPQuery("from Disciplina d where d.curso.faculdade = :idf", par);
//        return new ListModelList<Disciplina>(disc);
//    }
    public ListModel<Curso> getListCursoModel() {
        //Users u = csimpm.get(Users.class, usr.getUtilizador());
        par.clear();
        Faculdade f = csimpm.get(Faculdade.class, usr.getFaculdade().getIdFaculdade());
        par.put("fac",f);
        List<Curso> lc = csimpm.findByJPQuery("from Curso c where c.faculdade = :fac", par);
        return new ListModelList<Curso>(lc);
    }
//    public ListModel<Curso> getListPlanoModel() {
//        Users u = csimpm.get(Users.class, usr.getUtilizador());
//        par.clear();
//        par.put("fac", u.getFaculdade());
//        List<Curso> lc = csimpm.findByJPQuery("from Curso c where c.faculdade = :fac", par);
//        return new ListModelList<Curso>(lc);
//    }

    public void onSelect$cbcurso() {
        if (cbcurso.getSelectedItem() != null) {
            par.clear();
            Curso cu = (Curso) cbcurso.getSelectedItem().getValue();
            par.clear();
            par.put("c", cu);
            List<Planocurricular> lc = csimpm.findByJPQuery("from Planocurricular p where p.curso = :c order by p.planocurricularPK.ano desc", par);
            cbplano.setModel(new ListModelList<Planocurricular>(lc));
            cbplano.setText(cu.getPlanoc() + "");
            par.clear();
            //Planocurricular pl = (Planocurricular) cbplano.getSelectedItem().getValue();
            par.put("c", cu);
            par.put("planoc", cu.getPlanoc());
            List<Disciplina> ld = csimpm.findByJPQuery("from Disciplina o where o.curso = :c and o.planoc = :planoc", par);
            cbdisciplina.setModel(new ListModelList<Disciplina>(ld));
            limpaLB(lbestu);
            Constraint c = cbdisciplina.getConstraint();
            cbdisciplina.setConstraint("");
            cbdisciplina.setText("");
            cbdisciplina.setConstraint(c);
        }
    }

    public void onSelect$cbplano() {
        if (cbplano.getSelectedItem() != null) {
            par.clear();
            Curso cu = (Curso) cbcurso.getSelectedItem().getValue();
            Planocurricular pl = (Planocurricular) cbplano.getSelectedItem().getValue();
            par.put("c", cu);
            par.put("planoc", pl.getPlanocurricularPK().getAno());
            List<Disciplina> ld = csimpm.findByJPQuery("from Disciplina o where o.curso = :c and o.planoc = :planoc", par);
            cbdisciplina.setModel(new ListModelList<Disciplina>(ld));
            limpaLB(lbestu);
            Constraint c = cbdisciplina.getConstraint();
            cbdisciplina.setConstraint("");
            cbdisciplina.setText("");
            cbdisciplina.setConstraint(c);
        }
    }

    public void onSelect$cbdisciplina() {
        setLBdisc(0, 20);
    }

    //Adicionar disciplinas para um estudante
    // @Transactional
    public void onSalvarEstu() {
        check(tpanel1);
        if (combEstu.getSelectedItem() == null) {
            //Clients.showNotification("Seleccione um Estudante", "error", null, null, 0);
            Clients.scrollIntoView(combEstu);
            combEstu.setText("");
            combEstu.getText();
            return;
        }
        if (lbdisc.getSelectedCount() == 0) {
            Clients.showNotification("Seleccione as Disciplinas", "error", null, null, 0);
            return;
        }
        Long estud = null;
        int idf = 0;
        String erro = null;
        Inscricao i = null;
        Transaction t = csimpm.getTransacao();
        try {
            t.begin();
            Grupoinscricao gi = new Grupoinscricao();
            int co = 0;
            gi.setDescricao(txMotEst.getValue());
            csimpm.Saves(gi);
            Calendar ca = Calendar.getInstance();
            Estudante e = (Estudante) combEstu.getSelectedItem().getValue();
            e = csimpm.load(Estudante.class, e.getIdEstudante());
            estud = e.getIdEstudante();
            int turma = e.getTurma();
            int turno = e.getTurno();
//            par.clear();    //Ver prescricao
//            par.put("ide", e);
//            Prescricao p = csimpm.findEntByJPQuery("from Prescricao p where p.inscricaodisciplina.inscricao.idEstudante = :ide"
//                    + " and p.estado is true", par);
//            if (p != null) {
//                t.rollback();
//                Clients.showNotification("O estudante prescreveu uma disciplina! So poderá se inscrever depois de regularizar", "error", null, null, 0);
//                return;
//            }
            ca.set(Calendar.YEAR, ibano.getValue());
            int a = ca.get(Calendar.YEAR);
            par.clear();
            par.put("e", e);
            par.put("a", a);
            Matricula mat = csimpm.findEntByJPQuery("from Matricula m where m.matriculaPK.ano = :a and m.estudante = :e", par);
            if (mat != null) {
                if (mat.getAnulada() == true) {
                    t.rollback();
                    Clients.showNotification("A Matricula do Estudante foi anulada!", "error", null, null, 0);
                    return;
                }
            } else {
                if (!chEst.isChecked()) {
                    t.rollback();
                    Clients.showNotification("O Estudante não esta matriculado!", "error", null, null, 0);
                    return;
                }
            }
            //ibano.getValue() < e.getAnoIngresso() || 
            if (ibano.getValue() > ano) {
                t.rollback();
                Clients.showNotification("Seleccione um Ano válido!", "error", null, null, 0);
                return;
            }
            i = new Inscricao();
            //  i.setDataConfirmacao(ca.getTime());
            ca.set(Calendar.YEAR, ibano.getValue());
            i.setDataInscricao(ca.getTime());
            i.setEstado(true);
            i.setGrupo(gi);
            Users u = csimpm.get(Users.class, usr.getUtilizador());
            Faculdade f = csimpm.get(Faculdade.class, usr.getFaculdade().getIdFaculdade());
            i.setFuncionario(u.getIdFuncionario());
            i.setIdEstudante(e);
            idf = f.getIdFaculdade();
            // i.setModoInscricao(new Short("6"));
            // if (co == 0) {
            i.setModoInscricao(new Short("9"));
            // }
            Short sem;
            if (cbPeriodDisc.getValue().equals("Semestre 1")) {
                sem = new Short("1");
            } else {
                sem = new Short("2");
            }
            i.setSemestre(sem);
            i.setTaxaInscricao(Float.NaN);//fazer um calculo automatico
            csimpm.Saves(i);
            //co++;
            float nota;
            Inscricaodisciplina id = null;
            final Iterator<Listitem> items = new ArrayList(lbdisc.getSelectedItems()).listIterator();
            while (items.hasNext()) {
                Listitem li = items.next();
                Double dob = ((Doublespinner) li.getChildren().get(1).getChildren().get(0)).getValue();

                Disciplina d = (Disciplina) li.getValue();
                id = new Inscricaodisciplina();
                id.setEstado(true);
                if (dob != null) {
                    id.setNotaFinal(dob.floatValue());
                }
                id.setDisciplinaActiva(new Short("3"));
                id.setInscricao(i);
                id.setTurma(turma);
                id.setTurno(turno);
                id.setDisciplina(d);
                id.setInscricaodisciplinaPK(new InscricaodisciplinaPK(i.getIdInscricao(), d.getIdDisc()));
                ultimaInscricao(d, e);
                id.setDisciplinaActiva(new Short("3"));
                csimpm.Saves(id);
//                if (dob != null) {
//                    verificarPrescricao(id);
//                }
            }
            Clients.showNotification("Inscricao efectuada com sucesso", null, null, null, 2000);
            chEst.setChecked(false);
            t.commit();
            while (!t.wasCommitted())
            ;
        } catch (RuntimeException e) {
            t.rollback();
//            
//            System.out.println("");System.out.println("");System.out.println("");System.out.println("");System.out.println("");System.out.println("");
//            System.out.println(e.getMessage());
//            System.out.println("");System.out.println("");System.out.println("");System.out.println("");System.out.println("");System.out.println("");
            erro = e.getMessage();
            Clients.showNotification("Erro", e.getMessage(), null, null, 2000);
        }
        if (erro != null) {
            Messagebox.show(erro);
        }
        String tipopedido = "g7";
        ValidacaopendentePK vpk = new ValidacaopendentePK(idf, tipopedido);
        Validacaopendente vp = csimpm.getLocked(Validacaopendente.class, vpk);
        if (vp != null) {
            if ((vp.getQtd() + 1) < 0) {
                vp.setQtd(1);
            } else {
                vp.setQtd(vp.getQtd() + 1);
            }
            csimpm.update(vp);
        } else {
            vp = new Validacaopendente();
            vp.setValidacaopendentePK(vpk);
           // Users u = csimpm.get(Users.class, usr.getUtilizador());
            Faculdade f = csimpm.get(Faculdade.class, usr.getFaculdade().getIdFaculdade());
            vp.setFaculdade(f);
            vp.setQtd(1);
            csimpm.Save(vp);
        }
        eq = EventQueues.lookup("valid" + idf, EventQueues.APPLICATION, true);
        eq.publish(new Event("onPedidoMatD", null, vp));
        limpaLB(lbdisc);
        lbdisc.setRows(2);
        Constraint c = combEstu.getConstraint();
        combEstu.setConstraint("");
        combEstu.setText("");
        combEstu.setConstraint(c);
        Clients.scrollIntoView(combEstu);
        eq = EventQueues.lookup("insc" + estud, EventQueues.APPLICATION, true);
        eq.publish(new Event("onPedidoMat", null, i));
        eq = EventQueues.lookup("insc" + idf, EventQueues.APPLICATION, true);
        eq.publish(new Event("onPedidoMat", null, i));
        eq = EventQueues.lookup("pinscD" + idf, EventQueues.APPLICATION, true);
        eq.publish(new Event("onPedidoMatD", null, i));
    }

    //Incluir estudantes numa disciplina
    // @Transactional
    public void onSalvarDiscip() {
        check(tpanel2);
        Transaction t = csimpm.getTransacao();
        // t.begin();
        List<Inscricao> lins = new ArrayList<Inscricao>();
        int idf = 0;
        Inscricao iii = null;
        try {
            t.begin();
            Calendar ca;

            if (lbestu.getSelectedCount() == 0) {
                t.rollback();
                Clients.showNotification("Seleccione os Estudantes", "error", null, null, 0);
                return;
            }
            if (ibano2.getValue() > ano) {
                t.rollback();
                Clients.showNotification("Seleccione um Ano válido!", "error", null, null, 0);
                return;
            }
            Disciplina d = (Disciplina) cbdisciplina.getSelectedItem().getValue();
            Grupoinscricao gi = new Grupoinscricao();
            int co = 0;
            gi.setNiveli(d.getNivel().shortValue());
            gi.setDescricao(txMotDisc.getValue());
            csimpm.Saves(gi);
            Users u = csimpm.get(Users.class, usr.getUtilizador());
            Faculdade f = csimpm.get(Faculdade.class, usr.getFaculdade().getIdFaculdade());
            idf = f.getIdFaculdade();
            for (Listitem l : lbestu.getSelectedItems()) {
                ca = Calendar.getInstance();
                Inscricao i = new Inscricao();
                Double dob = ((Doublespinner) l.getChildren().get(1).getChildren().get(0)).getValue();
                // i.setDataConfirmacao(ca.getTime());
                i.setGrupo(gi);
                Estudante e = (Estudante) l.getValue();
                e = csimpm.load(Estudante.class, e.getIdEstudante());
                int turma = e.getTurma();
                int turno = e.getTurno();
                /////////////////verificar estado das matriculas
//                par.clear();    //Ver prescricao
//                par.put("ide", e);
//                Prescricao p = csimpm.findEntByJPQuery("from Prescricao p where p.inscricaodisciplina.inscricao.idEstudante = :ide"
//                        + " and p.estado is true", par);
//                if (p != null) {
//                    t.rollback();
//                    Clients.showNotification("O estudante \"" + e.getNomeCompleto() + "\" prescreveu uma disciplina! So poderá se inscrever depois de regularizar", "error", null, null, 0);
//                    return;
//                }

                int a = Calendar.getInstance().get(Calendar.YEAR);
                par.clear();
                par.put("e", e);
                par.put("a", a);
                Matricula mat = csimpm.findEntByJPQuery("from Matricula m where m.matriculaPK.ano = :a and m.estudante = :e", par);
                if (mat != null) {
                    if (mat.getAnulada() == true) {
                        t.rollback();
                        Clients.showNotification("A Matricula do Estudante \"" + e.getNomeCompleto() + "\" foi anulada!", "error", null, null, 0);
                        return;
                    }
                } else {
                    if (!chDisc.isChecked()) {
                        t.rollback();
                        Clients.showNotification("O Estudante \"" + e.getNomeCompleto() + "\" não esta matriculado!", "error", null, null, 0);
                        return;
                    }
                }

                if (ibano.getValue() < e.getAnoIngresso()) {
                    //ca.set(Calendar.YEAR, e.getAnoIngresso());
                    t.rollback();
                    Clients.showNotification("O Ano seleccionado é inválido para o Estudante: " + e.getNomeCompleto() + "!", "error", null, null, 0);
                    return;
                } else {
                    ca.set(Calendar.YEAR, ibano2.getValue());
                }
                i.setDataInscricao(ca.getTime());
                i.setEstado(true);
                i.setFuncionario(u.getIdFuncionario());
                i.setIdEstudante(e);
                i.setModoInscricao(new Short("5"));
                if (co == 0) {
                    i.setModoInscricao(new Short("8"));
                    iii = i;
                }
                Short sem;
                if (cbPeriodDisc2.getValue().equals("Semestre 1")) {
                    sem = new Short("1");
                } else {
                    sem = new Short("2");
                }
                i.setSemestre(sem);
                i.setTaxaInscricao(Float.NaN);//fazer um calculo automatico
                csimpm.Saves(i);
                lins.add(i);
                co++;
                Inscricaodisciplina id = new Inscricaodisciplina();
                id.setEstado(true);
                id.setDisciplinaActiva(new Short("3"));
                id.setInscricao(i);
                if (dob != null) {
                    id.setNotaFinal(dob.floatValue());
                }
                id.setDisciplina(d);
                id.setTurma(turma);
                id.setTurno(turno);
                id.setInscricaodisciplinaPK(new InscricaodisciplinaPK(i.getIdInscricao(), d.getIdDisc()));
                ultimaInscricao(d, e);
                id.setDisciplinaActiva(new Short("3"));
                csimpm.Saves(id);
//                if (dob != null) {
//                    verificarPrescricao(id);
//                }

            }
            Clients.showNotification("Inscricao efectuada com sucesso", null, null, null, 2000);
//        lbestu.clearSelection();
//        cbdisciplina.setText("");
            chDisc.setChecked(false);
            t.commit();
            while (!t.wasCommitted())
            ;
        } catch (RuntimeException e) {
            t.rollback();
//            System.out.println("");System.out.println("");System.out.println("");System.out.println("");System.out.println("");System.out.println("");
//            System.out.println(e.getMessage());
//            System.out.println("");System.out.println("");System.out.println("");System.out.println("");System.out.println("");System.out.println("");
            Clients.showNotification("Erro", e.getMessage(), null, null, 2000);
        }
        String tipopedido = "g7";
        ValidacaopendentePK vpk = new ValidacaopendentePK(idf, tipopedido);
        Validacaopendente vp = csimpm.getLocked(Validacaopendente.class, vpk);
        if (vp != null) {
            if ((vp.getQtd() + 1) < 0) {
                vp.setQtd(1);
            } else {
                vp.setQtd(vp.getQtd() + 1);
            }
            csimpm.update(vp);
        } else {
            vp = new Validacaopendente();
            vp.setValidacaopendentePK(vpk);
            //Users u = csimpm.get(Users.class, usr.getUtilizador());
            Faculdade f = csimpm.get(Faculdade.class, usr.getFaculdade().getIdFaculdade());
            vp.setFaculdade(f);
            vp.setQtd(1);
            csimpm.Save(vp);
        }
        eq = EventQueues.lookup("valid" + idf, EventQueues.APPLICATION, true);
        eq.publish(new Event("onPedidoMatD", null, vp));
        limpaLB(lbestu);
        lbestu.setRows(2);
        Constraint c = cbdisciplina.getConstraint();
        cbdisciplina.setConstraint("");
        cbdisciplina.setText("");
        cbdisciplina.setConstraint(c);
        eq = EventQueues.lookup("insc" + idf, EventQueues.APPLICATION, true);
        eq.publish(new Event("onPedidoMat", null, iii));
        eq = EventQueues.lookup("pinscD" + idf, EventQueues.APPLICATION, true);
        eq.publish(new Event("onPedidoMatD", null, iii));
        for (Inscricao inscric : lins) {
            eq = EventQueues.lookup("insc" + inscric.getIdEstudante().getIdEstudante(), EventQueues.APPLICATION, true);
            eq.publish(new Event("onPedidoMat", null, inscric));
        }
    }

    public void onCanlar() {
        winInscEst.detach();
    }

    public void onSwitchTab$tabInscEstu(ForwardEvent evt) {
        final Tab tab = (Tab) evt.getOrigin().getTarget();
        tab.setSelected(true);

    }

    public void onSwitchTab$tabInscDisc(ForwardEvent evt) {
        final Tab tab = (Tab) evt.getOrigin().getTarget();
        tab.setSelected(true);

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

    private void checkIsValid(Component component) {
        if (component instanceof InputElement && (!((InputElement) component).isDisabled())) {
            if ((!((InputElement) component).isValid())) {
                Clients.scrollIntoView(component);
                ((InputElement) component).getText();
            }
            if (((component instanceof Combobox) && ((Combobox) component).getSelectedItem() == null)) {
                Clients.scrollIntoView(component);
                ((Combobox) component).setText("");
                ((Combobox) component).getValue();
            }
        }
    }

    public void onCreate$winInscEq(Event evt) throws IOException {
        long ides = Long.parseLong(est.getText());
        Estudante e = csimpm.get(Estudante.class, ides);
        txest.setValue(e.getNomeCompleto());
        Curso c = e.getCursocurrente();
        c = csimpm.load(Curso.class, c.getIdCurso());
        lbdisceq.setMultiple(false);
        lbdisceq.setModel(new ListModelList<Disciplina>(c.getDisciplinaList()));
        lbdisceq.setMultiple(true);
    }

    public void onCanlarEq() {
        winInscEq.getParent().detach();
        if (winInscEq.getParent().getId().equals(combSem)) {
            ((Combobox) winAddInscricao.getFellow("combEstInsc")).setText("");
        }
        if (winAddInscricao != null) {
            ((Label) winAddInscricao.getFellow("lbcurso")).setValue("");
            ((Label) winAddInscricao.getFellow("lbnivel")).setValue("");
            ((Label) winAddInscricao.getFellow("lbsexo")).setValue("");
            ((Label) winAddInscricao.getFellow("lbcred")).setValue("");
            Listbox lb = ((Listbox) winAddInscricao.getFellow("discSel"));
            limpaLB(lb);
        }
        winInscEq.detach();
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

    public void onSalvarEq() throws IOException {
        List<Inscricao> lins = new ArrayList<Inscricao>();
        int idf = 0;
        Inscricao iii = null;
        List<Integer> lintP = new ArrayList<Integer>();
        List<Integer> lintP2 = new ArrayList<Integer>();
        for (int h = 0; h < 8; h++) {
            lintP.add(0);
        }
        lintP2.clear();
        for (int h = 0; h < 8; h++) {
            lintP2.add(0);
        }
        Transaction t = csimpm.getTransacao();
        try {
            t.begin();
            long ides = Long.parseLong(est.getText());
            Estudante e = csimpm.get(Estudante.class, ides);
            if (e.getTransferido() != null && e.getTransferido().intValue() == 0) {
                e.setTransferido(new Short("1"));
            }
            if (e.getMudancac() != null && e.getMudancac().intValue() == 0) {
                e.setMudancac(new Short("1"));
            }
            //  csimpm.update(e);
            for (Listitem li : lbdisceq.getItems()) {
                checkLitem(li);
            }
            Grupoinscricao gi = new Grupoinscricao();
            gi.setDescricao("Equivalencia");
            gi.setNiveli(e.getNivelFrequencia().shortValue());
            csimpm.Save(gi);
            int co = 0;
            Calendar ca;
            Users u = csimpm.get(Users.class, usr.getUtilizador());
            Faculdade f = csimpm.get(Faculdade.class, usr.getFaculdade().getIdFaculdade());
            idf = f.getIdFaculdade();
            final Iterator<Listitem> items2 = new ArrayList(lbdisceq.getSelectedItems()).listIterator();
            Disciplina d = null;
            Listitem li;
            while (items2.hasNext()) {
                li = items2.next();
                d = (Disciplina) li.getValue();
                int ano = ((Intbox) li.getChildren().get(1).getChildren().get(0)).getValue();
                float nota = ((Doublespinner) li.getChildren().get(2).getChildren().get(0)).getValue().floatValue();
                if (nota < 10) {
                    t.rollback();
                    Clients.scrollIntoView(li.getChildren().get(2).getChildren().get(0));
                    Clients.showNotification("Introduza um valor maior ou igual a 10!", "error", null, null, 0);
                    return;
                }
                par.clear();
                par.put("ano", d.getNivel());
                par.put("sem", d.getSemestre().shortValue());
                par.put("e", e);
                ca = Calendar.getInstance();
                Inscricao i = csimpm.findEntByJPQuery("from Inscricao i where extract(year from i.dataInscricao) = :ano and"
                        + " i.semestre = :sem and i.idEstudante = :e", par);
                if (i == null) {
                    i = new Inscricao();
                    i.setSemestre(d.getSemestre().shortValue());
                    // i.setDataConfirmacao(ca.getTime());
                    ca.set(Calendar.YEAR, ano);
                    i.setDataInscricao(ca.getTime());
                    i.setEstado(true);
                    i.setFuncionario(u.getIdFuncionario());
                    i.setIdEstudante(e);
                    i.setModoInscricao(new Short("4"));
                    if (co == 0) {
                        i.setModoInscricao(new Short("7"));
                        iii = i;
                    }
                    i.setTaxaInscricao(Float.NaN);//fazer um calculo automatico
                    i.setGrupo(gi);
                    csimpm.Save(i);
                    lins.add(i);
                    co++;
                }
                Inscricaodisciplina id = new Inscricaodisciplina();
                id.setEstado(true);
                id.setDisciplinaActiva(new Short("3"));
                id.setInscricao(i);
                id.setDisciplina(d);
                id.setNotaFinal(nota);
                id.setInscricaodisciplinaPK(new InscricaodisciplinaPK(i.getIdInscricao(), d.getIdDisc()));
                csimpm.Save(id);
                if (d.getSemestre() == 1) {
                    int indice = d.getNivel();
                    lintP.set(indice - 1, lintP.get(indice - 1) + 1);
                } else {
                    int indice = d.getNivel();
                    lintP2.set(indice - 1, lintP2.get(indice - 1) + 1);
                }
            }
            int x = 0, cont = 0, cont2 = 0, anoing = 1;
            boolean sair = false;
            par.clear();
            par.put("c", e.getCursocurrente());
            List<Disciplina> ldisc = csimpm.findByJPQuery("from Disciplina d where d.curso = :c", par);
            for (int g = 0; g < 8 && sair == false; g++) {
                for (Disciplina dis : ldisc) {
                    if (dis.getSemestre() == 1 && dis.getNivel() == g + 1) {
                        cont = cont + 1;
                    }
                }
                for (Disciplina dis : ldisc) {
                    if (dis.getSemestre() == 2 && dis.getNivel() == g + 1) {
                        cont2 = cont2 + 1;
                    }
                }
                //           Messagebox.show(cont + "");
//                                      Messagebox.show(lintP.get(g)+"");
//                                        Messagebox.show("Total: "+(cont - lintP.get(g))+"  dados:"+" "+cont+" "+lintP.get(g));
                if (cont - lintP.get(g) > 2 || cont2 - lintP2.get(g) > 2) {
                    if (g > 0 && lintP.size() > 0 && lintP2.size() > 0) {
                        anoing = anoing + 1;
                    }
                    sair = true;
                } else {
                    if (g > 0) {
                        anoing = anoing + 1;
                    }
                }
                cont = 0;
                cont2 = 0;
            }
            e.setNivelFrequencia(anoing);
            csimpm.update(e);

            Clients.showNotification("Equivalência registada com sucesso", null, null, null, 2000);
            winInscEq.detach();
            t.commit();
            /// lbestu.clearSelection();
        } catch (Exception ex) {
            t.rollback();
            Clients.showNotification("Erro", "error", null, null, 2000);
        }
        eq = EventQueues.lookup("insc" + idf, EventQueues.APPLICATION, true);
        eq.publish(new Event("onPedidoMat", null, iii));
        eq = EventQueues.lookup("pinscD" + idf, EventQueues.APPLICATION, true);
        eq.publish(new Event("onPedidoMatD", null, iii));
        for (Inscricao inscric : lins) {
            eq = EventQueues.lookup("insc" + inscric.getIdEstudante().getIdEstudante(), EventQueues.APPLICATION, true);
            eq.publish(new Event("onPedidoMat", null, inscric));
        }
    }

    //validar campos de notas
    private void checkLitem(Component component) {
        checkIsValidLitem(component);
        if (component.isVisible()) {
            List<Component> children = component.getChildren();
            for (Component each : children) {
                checkLitem(each);
            }
        }
    }
    //validar campos de notas
    private void checkIsValidLitem(Component component) {
        if (component instanceof InputElement && !((InputElement) component).isDisabled()) {
            if ((!((InputElement) component).isDisabled()) && (!((InputElement) component).isValid())) {
                Clients.scrollIntoView(component);
                ((InputElement) component).getText();
            }
        }
    }

    public void onCombEstu(InputEvent evt) {
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
        combEstu.getItems().clear();
        setLBComb(0, 20);
    }

    public void onOpen$combEstu() {
//        if (posc.getValue() == 0) {
//            combEstu.getItems().clear();
//            setLBComb(0, 20);
//        }
        if (pesq == null) {
            setLBComb(0, 20);
            pesq = "";
        } else {
            if (combEstu.isOpen()) {
                combEstu.setText(pesq);
            } else {
                if (indc != -1) {
                    combEstu.getSelectedIndex();
                    combEstu.setSelectedIndex(indc);
                }
            }
        }
    }

    public void setLBComb(int i, int j) {
        if (j == 20) {
            combEstu.setModel(new ListModelList<Estudante>());
        }
        List<Estudante> li = null;
        //Users u = csimpm.get(Users.class, usr.getUtilizador());
        Faculdade f = csimpm.get(Faculdade.class, usr.getFaculdade().getIdFaculdade());
        // par.clear();
        condpar.put("fac", f);
        li = csimpm.findByJPQueryFilter("from Estudante e where e.cursocurrente.faculdade = :fac " + condn + " order by e.nomeCompleto", condpar, i, j);
        final Iterator<Estudante> items = li.iterator();
        Estudante e;
        //lbinscricao.setRows(lbinscricao.getItemCount() + li.size());
        if (j > 20) {
            ((ListModelList) combEstu.getModel()).remove(new Estudante());
            new Combobox().appendChild(combEstu.getItemAtIndex(posc.getValue() - 1));
        }
        while (items.hasNext()) {
            e = items.next();
            ((ListModelList) combEstu.getModel()).add(e);
        }
        if (li.size() == j) {
            Estudante es = new Estudante();
            es.setNomeCompleto("-------Ver Mais-------");
            ((ListModelList) combEstu.getModel()).add(es);
            posc.setValue(((ListModelList) combEstu.getModel()).size());
        }
    }

    public void onLoadComb() {
        int i = ((ListModelList) combEstu.getModel()).size() - 1;
        setLBComb(i, i + 20);
    }
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////   

//    public void onSelect$lbestu(){
//      if(lbestu.getSelectedItems().){//==posd.getValue()-1){
//         onLoaddisc(); lid.setc
//      }
//    }
    public void onSellbdisc(ForwardEvent e) {
//        final Iterator<Listitem> items = lbdisc.getItems().iterator();
        Listitem li = (Listitem) e.getOrigin().getTarget();

//        while (items.hasNext()) {
//            li = items.next();
        final Listitem lo = li;
        if (li.isSelected()) {
            ((Doublespinner) li.getChildren().get(1).getChildren().get(0)).setDisabled(false);
            ((Doublespinner) li.getChildren().get(1).getChildren().get(0)).setConstraint("min 0 max 20");
        } else {
            final Doublespinner dbs = ((Doublespinner) li.getChildren().get(1).getChildren().get(0));
            Constraint c = null;
            dbs.setConstraint(c);
//            if (dbs.getValue() != null) {
                Messagebox.show("Desseleccionar?", "Confirmação", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION,
                        new EventListener() {
                            @Override
                            public void onEvent(Event evet) {
                                switch (((Integer) evet.getData()).intValue()) {
                                    case Messagebox.YES:
                                        ((InputElement) lo.getChildren().get(1).getChildren().get(0)).setText("");
                                        dbs.setDisabled(true);
                                        break;
                                    case Messagebox.NO:
                                        dbs.setConstraint("min 0 max 20");
                                        lo.setSelected(true);
                                }
                            }
                        });
//            }
//            if (dbs.getValue() == null) {
//                ((InputElement) li.getChildren().get(1).getChildren().get(0)).setText("");
//                dbs.setDisabled(true);
//            }

        }
//        }
    }

    public void onEr(ForwardEvent e) {
//        Messagebox.show("dgdg");
//        final Doublespinner dbs = (Doublespinner) e.getOrigin().getTarget();
//        Listitem li = (Listitem) dbs.getParent().getParent();
//        if (!li.isSelected()) {
//            li.setSelected(true);
//            Constraint c = null;
//            dbs.setConstraint(c);
//            final Listitem lo = li;
////            if (dbs.getValue() != null) {
//                Messagebox.show("Desseleccionar?", "Confirmação", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION,
//                        new EventListener() {
//                            @Override
//                            public void onEvent(Event evet) {
//                                switch (((Integer) evet.getData()).intValue()) {
//                                    case Messagebox.YES:
//                                        ((InputElement) lo.getChildren().get(1).getChildren().get(0)).setText("");
//                                        dbs.setDisabled(true);
//                                        break;
//                                    case Messagebox.NO:
//                                        dbs.setConstraint("min 0 max 20; no Empty: Introduza a nota!");
//                                        lo.setSelected(true);
//                                }
//                            }
//                        });
////            }
//        }
    }

    public void onSellbestu(ForwardEvent e) {
        if (lbestu.hasFellow("lid")) {
            if (((Listitem) lbestu.getFellow("lid")).isSelected()) {
                onLoaddisc();
            } else {
//                final Iterator<Listitem> items = lbestu.getItems().iterator();
                Listitem li = (Listitem) e.getOrigin().getTarget();
//                while (items.hasNext()) {
//                    li = items.next();
                final Listitem lo = li;
                if (li.isSelected()) {
                    ((Doublespinner) li.getChildren().get(1).getChildren().get(0)).setDisabled(false);
                    ((Doublespinner) li.getChildren().get(1).getChildren().get(0)).setConstraint("min 0 max 20");
                } else {
                    if (li.getChildren().size() != 3) {
                        final Doublespinner dbs = ((Doublespinner) li.getChildren().get(1).getChildren().get(0));
                        Constraint c = null;
                        dbs.setConstraint(c);
                        if (true) {
                            Messagebox.show("Desseleccionar?", "Confirmação", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION,
                                    new EventListener() {
                                        @Override
                                        public void onEvent(Event evet) {
                                            switch (((Integer) evet.getData()).intValue()) {
                                                case Messagebox.YES:
                                                    ((InputElement) lo.getChildren().get(1).getChildren().get(0)).setText("");
                                                    dbs.setDisabled(true);
                                                    break;
                                                case Messagebox.NO:
                                                    dbs.setConstraint("min 0 max 20");
                                                    lo.setSelected(true);
                                            }
                                        }
                                    });
                        }
//                        Messagebox.show("sdfsdf");
//                        if (dbs.getValue() == null) {
//                            ((InputElement) li.getChildren().get(1).getChildren().get(0)).setText("");
//                            dbs.setDisabled(true);
//                        }
                    }
                }
//                }
            }
        } else {
//            final Iterator<Listitem> items = lbestu.getItems().iterator();
            Listitem li = (Listitem) e.getOrigin().getTarget();
//            while (items.hasNext()) {
//                li = items.next();
            final Listitem lo = li;
            if (li.isSelected()) {
                ((Doublespinner) li.getChildren().get(1).getChildren().get(0)).setDisabled(false);
                ((Doublespinner) li.getChildren().get(1).getChildren().get(0)).setConstraint("min 0 max 20");
            } else {
                final Doublespinner dbs = ((Doublespinner) li.getChildren().get(1).getChildren().get(0));
                Constraint c = null;
                dbs.setConstraint(c);
//                if (dbs.getValue() != null) {
                    Messagebox.show("Desseleccionar?", "Confirmação", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION,
                            new EventListener() {
                                @Override
                                public void onEvent(Event evet) {
                                    switch (((Integer) evet.getData()).intValue()) {
                                        case Messagebox.YES:

                                            ((InputElement) lo.getChildren().get(1).getChildren().get(0)).setText("");
                                            dbs.setDisabled(true);
                                            break;
                                        case Messagebox.NO:
                                            dbs.setConstraint("min 0 max 20");
                                            lo.setSelected(true);
                                    }
                                }
                            });
//                }
//                if (dbs.getValue() == null) {
//
//                    ((InputElement) li.getChildren().get(1).getChildren().get(0)).setText("");
//                    dbs.setDisabled(true);
//                }
            }
//            }
        }
    }

    public void onSellbdisceq() {
        final Iterator<Listitem> items = lbdisceq.getItems().iterator();
        Listitem li;
        while (items.hasNext()) {
            li = items.next();
            if (li.isSelected()) {
                ((Intbox) li.getChildren().get(1).getChildren().get(0)).setDisabled(false);
                ((Doublebox) li.getChildren().get(2).getChildren().get(0)).setDisabled(false);
                ((Intbox) li.getChildren().get(1).getChildren().get(0)).setConstraint(" no Empty: Introduza o Ano que realizou a disciplina!");
                ((Doublebox) li.getChildren().get(2).getChildren().get(0)).setConstraint(" no Empty: Introduza a Nota Final!");
            } else {
                Constraint c = null;
                ((Intbox) li.getChildren().get(1).getChildren().get(0)).setConstraint(c);
                ((Doublebox) li.getChildren().get(2).getChildren().get(0)).setConstraint(c);
                ((InputElement) li.getChildren().get(1).getChildren().get(0)).setText("");
                ((InputElement) li.getChildren().get(2).getChildren().get(0)).setText("");
                ((Intbox) li.getChildren().get(1).getChildren().get(0)).setDisabled(true);
                ((Doublebox) li.getChildren().get(2).getChildren().get(0)).setDisabled(true);
            }
        }
    }

    public void setLBdisc(int i, int j) {
        if (j == 20) {
            lbestu.setModel(new ListModelList<Estudante>());
        }
        List<Estudante> li = null;
        Disciplina d = (Disciplina) cbdisciplina.getSelectedItem().getValue();
        d = csimpm.load(Disciplina.class, d.getIdDisc());
        par.clear();
        par.put("ano", ibano2.getValue());
        par.put("sem", Short.parseShort(cbPeriodDisc2.getSelectedItem().getValue().toString()));
        par.put("d", d);
        par.put("cu", d.getCurso());
        li = csimpm.findByJPQueryFilter("from Estudante e where e.cursocurrente = :cu and"
                + " e not in (select id.inscricao.idEstudante from Inscricaodisciplina id where id.disciplina =:d and id.estado is true and id.disciplinaActiva = 3"
                + " and ((id.notaFinal is not null and id.notaFinal >= 10) or id.notaFinal is null or (id.notaFinal is not null and id.notaFinal <= 10"
                + " and extract(year from id.inscricao.dataInscricao) = :ano and id.inscricao.semestre=:sem)))"
                //trazer estudante que ainda nao fez a discip. (que nao tem nenhuma inscricao com nota >=10 ou com nota final = null )
                + " and not exists (select p from Precedencia p where p.disciplina1 =:d and p.disciplina not in (select id.disciplina from Inscricaodisciplina"
                + " id where id.inscricao.idEstudante = e and id.estado is true and id.disciplinaActiva = 3"
                + " and ((id.notaFinal is not null and id.notaFinal >= 10))))"
                //a disciplina nao tem precedencia ou tem precedencia com uma disciplna que ja foi feita
                + " order by e.anoIngresso asc,e.nomeCompleto asc", par, i, j);
        final Iterator<Estudante> items = li.iterator();
        Estudante e;
        lbestu.setRows(lbestu.getItemCount() + li.size() + 1);
        if (j > 20) {
            ((ListModelList) lbestu.getModel()).remove(new Estudante());
            // new Listbox().appendChild(lbestu.getItemAtIndex(posd.getValue()-1));
        }
        lbestu.setMultiple(false);
        while (items.hasNext()) {
            e = items.next();
            ((ListModelList) lbestu.getModel()).add(e);
        }
        lbestu.setMultiple(true);
        if (li.size() == j) {
            Estudante es = new Estudante();
            es.setNomeCompleto("-------Ver Mais-------");
            ((ListModelList) lbestu.getModel()).add(es);
            posd.setValue(((ListModelList) lbestu.getModel()).size());
        }
    }

    public void onLoaddisc() {
        int i = ((ListModelList) lbestu.getModel()).size() - 1;
        setLBdisc(i, i + 20);
    }

//    public void verificarPrescricao(Inscricaodisciplina i) {
//        if (i.getNotaFinal() < 10) {
//            //Verificar se tinha reprovado antes
//            par.clear();
//            par.put("d", i.getDisciplina());
//            par.put("e", i.getInscricao().getIdEstudante());
//            par.put("dat", i.getInscricao().getDataInscricao());
//            Inscricaodisciplina oldID = csimpm.findEntByJPQuery("select k from Inscricaodisciplina k where k.disciplina = :d and k.inscricao.dataInscricao in (select max(id.inscricao.dataInscricao) "
//                    + "from Inscricaodisciplina id where id.estado is true and id.disciplina =:d and id.inscricao.idEstudante = :e and id.disciplinaActiva = 2 and"
//                    + " id.inscricao.dataInscricao < :dat)", par);
////            Inscricaodisciplina oldID = csimp.findEntByJPQuery("from Inscricaodisciplina id where id.disciplina =:d and id.inscricao.idEstudante"
////                    + " = :e and id.disciplinaActiva = 2", par);
//            if (oldID != null && oldID.getNotaFinal() < 10 && oldID.getPrescricao() == null) {
//                InscricaodisciplinaPK prePK = new InscricaodisciplinaPK(i.getInscricao().getIdInscricao(), i.getDisciplina().getIdDisc());
//                Prescricao pre = new Prescricao();
//                pre.setInscricaodisciplinaPK(prePK);
//                pre.setEstado(true);
//                pre.setDatai(oldID.getInscricao().getDataInscricao());
//                csimpm.updates(pre);
//            }
//        }
//    }
    public void ultimaInscricao(Disciplina d, Estudante e) {
        par.clear();
        par.put("d", d);
        par.put("e", e);
        Inscricaodisciplina oldID = csimpm.findEntByJPQuery("from Inscricaodisciplina id where id.disciplina =:d and id.inscricao.idEstudante"
                + " = :e and id.disciplinaActiva = 3", par);
        if (oldID != null) {
            oldID.setDisciplinaActiva(new Short("2"));
            csimpm.updates(oldID);
        }
    }

    public void onChiba1() {
        limpaLB(lbdisc);
        lbdisc.setRows(2);
        Constraint c = combEstu.getConstraint();
        combEstu.setConstraint("");
        combEstu.setText("");
        combEstu.setConstraint(c);
    }

    public void onSelCbp1() {
        limpaLB(lbdisc);
        lbdisc.setRows(2);
        Constraint c = combEstu.getConstraint();
        combEstu.setConstraint("");
        combEstu.setText("");
        combEstu.setConstraint(c);
    }

    public void onChiba2() {
        limpaLB(lbestu);
        lbestu.setRows(2);
        Constraint c = cbdisciplina.getConstraint();
        cbdisciplina.setConstraint("");
        cbdisciplina.setText("");
        cbdisciplina.setConstraint(c);
    }

    public void onSelCbp2() {
        limpaLB(lbestu);
        lbestu.setRows(2);
        Constraint c = cbdisciplina.getConstraint();
        cbdisciplina.setConstraint("");
        cbdisciplina.setText("");
        cbdisciplina.setConstraint(c);
    }
}
