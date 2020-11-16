/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package esira.inscricao;

import ExcelExport.BeanToExcel;
import entidade.Arquivo;
import esira.domain.Arquivoinscricao;
import esira.domain.Curso;
import esira.domain.Disciplina;
import esira.domain.Disciplinaanulada;
import esira.domain.Equivalencia;
import esira.domain.Estudante;
import esira.domain.Faculdade;
import esira.domain.Funcionario;
import esira.domain.Inscricao;
import esira.domain.Inscricaodisciplina;
import esira.domain.InscricaodisciplinaPK;
import esira.domain.Matricula;
import esira.domain.MatriculaPK;
import esira.domain.PlanificacaoAnoLectivo;
import esira.domain.Planocurricular;
import esira.domain.Prescricao;
import esira.domain.Users;
import esira.domain.Validacaopendente;
import esira.domain.ValidacaopendentePK;
import esira.matricula.GuardarEstudanteController;
import esira.service.CRUDService;
import esira.service.UserAutentic;
import esira.webapp.Item;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import org.apache.commons.io.FilenameUtils;
import org.springframework.security.core.context.SecurityContextHolder;
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
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Constraint;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Div;
import org.zkoss.zul.Doublebox;
import org.zkoss.zul.Hlayout;
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
import org.zkoss.zul.SimpleConstraint;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Tabs;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Vbox;
import org.zkoss.zul.Window;
import org.zkoss.zul.impl.InputElement;

/**
 *
 * @author Administrator
 */
public class InscricaoController extends GenericForwardComposer {

    private EventQueue eq;

    @WireVariable
    private CRUDService csimpm = (CRUDService) SpringUtil.getBean("CRUDService");
    private Window winAddInscricao, winInscricao, winAddInscricaoDisc, winAnularI, winanularmot, win;
    private Combobox combEstInsc, combSem;
    private ListModel<Estudante> estudantes;
    private ListModel<Inscricao> inscric;
    private ListModel<Disciplina> disciplinas;
    private ListModel<Curso> listaCursoModel;
    private Label lbcurso, lbnivel, lbsexo, lbcred, lbtaxaInscricao, lbtaxaInscricaoE, lbano, lbNumero,
            lbtaxaMulta15dias, lbtaxaMulta30dias, lbtotal, lbsem;
    private Listbox discEst, discEstOp, discSel, lbinscricao, lbdiscInsc, lbimg, lbimg1, lbin, lbimgII;
    private Tab tabPagamentoTaxa, tabDadosInscricao;
    private Tabs tabs;
    private Row rwTaxanM, rwTaxaeM, rwTaxamulta15, rwTaxamulta30;
    private Button addInsc, btnCancDisc, btnAnular, lbc, btnPInsc;
    Textbox txnrTalao, txx, tbimg4, txMotivo;
    Combobox cbCursoRel, cbDiscRel, cbfaculdade;
    List listaI;
    Datebox dinsc;
    private static Media mediaCPI;
    private int ano;
    private int sem = 0;
    private int n, novoNivel;
    //private int pos;

    private Intbox item, idest;
    Map<String, Object> par = new HashMap<String, Object>();
    Map<String, Object> ent = new HashMap<String, Object>();
    Users usr = (Users) Sessions.getCurrent().getAttribute("user");
    private Hlayout ahead;
    String condfac = "", condnr = "", condnome = "", condgenero = "", condanoi = "", condano = "", condcurso = "";
    Textbox txProcurar, txProcNrmec, txMot;
    Map<String, Object> condpar = new HashMap<String, Object>();
    Combobox cbcurso, cbPlanoD, cbcursoI, cbDisc;
    private Intbox ibProcAno;
    private Button btv;
    Menuitem manoi;
    private Div prazo;
    private Row prazoinsc;
    private Label lbprescricao;
    private Row rwmulta;
    private Intbox ibmulta, idcurso, idplano;
    Float totali;
    int indc = -1;
    private String pesq = null, condn = "";
    private Intbox posc;
    Map<String, Object> cbpar = new HashMap<String, Object>();

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        Date dano = new Date();
        Calendar cal = new GregorianCalendar();
        cal.setTime(dano);
        if (posc != null) {
            posc.setValue(0);
        }
        if (!usr.getUestudante()) {
            win = (Window) Executions.createComponents("/inscricao/EditarInsc.zul", null, null);
            win.setTitle("Editar Inscricao");
            win.detach();
        }
        if (usr.getUestudante()) {
            btv.setVisible(false);
            if (btnPInsc != null) {
                btnPInsc.setVisible(false);
            }
            ahead.setVisible(false);
            if (usr.getUtilizador() != null) {
                Users user = csimpm.get(Users.class, usr.getUtilizador());
                par.clear();
                par.put("ide", user.getIdEstudante());
                List<Disciplinaanulada> lda = csimpm.findByJPQuery("from Disciplinaanulada da where da.estado is false and da.dataconfirmacao is not null and "
                        + "da.idEstudante = :ide", par);
                if (!lda.isEmpty()) {
                    winanularmot.setVisible(true);
                }
                String title = "";
                for (Disciplinaanulada da : lda) {
                    title = title + "\n" + da.getMotivo();
                }
                winanularmot.setTitle(title);
                txx.setValue(user.getIdEstudante().getIdEstudante() + "");
            } else {
                btnAnular.setVisible(false);
            }
        }
        if (winInscricao != null) {
            ibProcAno.setValue(cal.get(Calendar.YEAR));
            cbcurso.setSelectedIndex(0);
            ano = cal.get(Calendar.YEAR);
//            if (cbCursoRel != null && cbCursoRel.getItems() != null) {
//                cbCursoRel.setSelectedIndex(-1);
//            }
//            if (cbDiscRel != null && cbDiscRel.getItems() != null) {
//                cbDiscRel.setSelectedIndex(-1);
//            }
            if (usr.getUtilizador() != null) {
                if (!usr.getUestudante()) {
                    if (!winInscricao.getParent().getId().equals("mDialogMatricula")) {
                    //    pos = 0;
                        condpar.clear();
                        //Users u = csimpm.get(Users.class, usr.getUtilizador());
                        Faculdade f = csimpm.get(Faculdade.class, usr.getFaculdade().getIdFaculdade());
                        if (f != null) {
                            condcurso = " and e.cursocurrente = :curso ";
                            ListModel lm = getListaCursoModel();
                            if (lm != null && lm.getSize() > 0) {
                                condpar.put("curso", (Curso) lm.getElementAt(0));
                                setLB(0, 10);
                            } else {
                                lbinscricao.setModel(new ListModelList<>());
                            }
                        } else {
                            cbfaculdade.setVisible(true);
                        }
                    } else {
                        btnAnular.setVisible(false);
                        btnPInsc.setVisible(false);
                        ahead.setVisible(false);
                        btv.setVisible(false);
                    }
                } else {
                    getListInscricaoModel2();
                }
            }
        }

        // if(mDialogMatricula.isVisible()==false&&winAddInscricao!=null&&winAddInscricao.getParent().getId().equals("mDialogMatricula")){
        // addInsc();
        //}
    }

    public void onSIndexCbcurso() {
        if (cbcurso.getModel() != null && cbcurso.getModel().getSize() > 0) {
            cbcurso.setSelectedIndex(0);
        }
    }

    public void onSetQueueInsc() {
        //Users u = csimpm.get(Users.class, usr.getUtilizador());
        if (!usr.getUestudante()) {
            // if (u.getFaculdade() != null) {
            eq = EventQueues.lookup("insc" + usr.getFaculdade().getIdFaculdade(), EventQueues.APPLICATION, true);
            eq.subscribe(getEventoInsc());
//            eq = EventQueues.lookup("pinscD" + u.getFaculdade().getIdFaculdade(), EventQueues.APPLICATION, true);
//            eq.subscribe(getEventoP());
            eq = EventQueues.lookup("rinscD" + usr.getFaculdade().getIdFaculdade(), EventQueues.APPLICATION, true);
            eq.subscribe(getEventoP());
            eq = EventQueues.lookup("rinscRD" + usr.getFaculdade().getIdFaculdade(), EventQueues.APPLICATION, true);
            eq.subscribe(getEventoRP());
            eq = EventQueues.lookup("rinscA" + usr.getFaculdade().getIdFaculdade(), EventQueues.APPLICATION, true);
            eq.subscribe(getEventoIA());
            eq = EventQueues.lookup("anula" + usr.getFaculdade().getIdFaculdade(), EventQueues.APPLICATION, true);
            eq.subscribe(getEventoAnul());
            // }
        } else if (usr.getUtilizador() != null) {
            Users u = csimpm.get(Users.class, usr.getUtilizador());
            eq = EventQueues.lookup("insc" + u.getIdEstudante().getIdEstudante(), EventQueues.APPLICATION, true);
            eq.subscribe(getEventoInscE());
//            eq = EventQueues.lookup("pinscD" + u.getIdEstudante().getIdEstudante(), EventQueues.APPLICATION, true);
//            eq.subscribe(getEventoPE());
            eq = EventQueues.lookup("rinscD" + u.getIdEstudante().getIdEstudante(), EventQueues.APPLICATION, true);
            eq.subscribe(getEventoPE());
            eq = EventQueues.lookup("rinscRD" + u.getIdEstudante().getIdEstudante(), EventQueues.APPLICATION, true);
            eq.subscribe(getEventoRPE());
            eq = EventQueues.lookup("rinscA" + u.getIdEstudante().getIdEstudante(), EventQueues.APPLICATION, true);
            eq.subscribe(getEventoIAE());
            eq = EventQueues.lookup("anula" + u.getIdEstudante().getIdEstudante(), EventQueues.APPLICATION, true);
            eq.subscribe(getEventoAnulE());
        }
    }

    public EventListener getEventoInscE() {
        return new EventListener() {
            @Override
            public void onEvent(Event event) throws Exception {
                Inscricao i = (Inscricao) event.getData();
                if (winInscricao.getParent() != null) {
                    i = csimpm.load(Inscricao.class, i.getIdInscricao());
                    lbinscricao.setRows(lbinscricao.getItemCount() + 2);
                    ((ListModelList) lbinscricao.getModel()).add(0, i);
                    par.clear();
                    par.put("i", i.getIdInscricao());
                    List<Inscricaodisciplina> li = csimpm.findByJPQuery("from Inscricaodisciplina id where id.inscricaodisciplinaPK.idInscricao = :i", par);
                    final Iterator<Inscricaodisciplina> items = new ArrayList(li).listIterator();
                    Inscricaodisciplina id;
                    int ind = 0;
                    while (items.hasNext()) {
                        ind = ind + 1;
                        id = items.next();
                        ((ListModelList) lbinscricao.getModel()).add(ind, id);
                    }
                }
            }
        };
    }

    public EventListener getEventoInsc() { //problemas quando o estudante nao existe
        return new EventListener() {
            @Override
            public void onEvent(Event event) throws Exception {
                Inscricao i = (Inscricao) event.getData();
                if (winInscricao.getParent() != null) {
                    i = csimpm.load(Inscricao.class, i.getIdInscricao());
                    Users u = csimpm.get(Users.class, usr.getUtilizador());
                    if (!usr.getUestudante()) {
                        int ind = ((ListModelList) lbinscricao.getModel()).indexOf(i.getIdEstudante());
                        if (ind >= 0) {
                            Listitem li = lbinscricao.getItemAtIndex(ind);
                            if (((Button) (li.getChildren().get(0).getChildren().get(0))).getImage().equals("/icon/down.png")) {
                                lbinscricao.setRows(lbinscricao.getItemCount() + 2);
                                ((ListModelList) lbinscricao.getModel()).add(ind + 1, i);
                                par.clear();
                                par.put("i", i.getIdInscricao());
                                List<Inscricaodisciplina> lid = csimpm.findByJPQuery("from Inscricaodisciplina id where id.inscricaodisciplinaPK.idInscricao = :i", par);
                                final Iterator<Inscricaodisciplina> items = new ArrayList(lid).listIterator();
                                Inscricaodisciplina id;
                                int indice = ind + 1;
                                while (items.hasNext()) {
                                    indice = indice + 1;
                                    id = items.next();
                                    ((ListModelList) lbinscricao.getModel()).add(indice, id);
                                }
                            }
                        }
                    }
                }
            }
        };
    }

    public EventListener getEventoPE() {
        return new EventListener() {
            @Override
            public void onEvent(Event event) throws Exception {
                Inscricao i = (Inscricao) event.getData();
                if (winInscricao.getParent() != null) {
                    i = csimpm.load(Inscricao.class, i.getIdInscricao());
                    int ind;
                    if (i.getGrupo() != null) {
                        for (Inscricao in : i.getGrupo().getInscricaoList()) {
                            ind = ((ListModelList) lbinscricao.getModel()).indexOf(in);
                            if (ind >= 0) {
                                ((ListModelList) lbinscricao.getModel()).set(ind, in);
                            }
                        }
                    } else {
                        ind = ((ListModelList) lbinscricao.getModel()).indexOf(i);
                        if (ind >= 0) {
                            ((ListModelList) lbinscricao.getModel()).set(ind, i);
                        }
                    }
                }
            }
        };
    }

    public EventListener getEventoRPE() {
        return new EventListener() {
            @Override
            public void onEvent(Event event) throws Exception {
//                Inscricao i = (Inscricao) event.getData();
//                if (winInscricao.getParent() != null) {
//                    
//                    int ind;
//                    ind = ((ListModelList) lbinscricao.getModel()).indexOf(i);Messagebox.show(ind+"");
//                    if (ind >= 0) {
//                        ((ListModelList) lbinscricao.getModel()).remove(ind);
//                    }
//                }
            }
        };
    }

    public EventListener getEventoP() {
        return new EventListener() {
            @Override
            public void onEvent(Event event) throws Exception {
                Inscricao i = (Inscricao) event.getData();
                if (winInscricao.getParent() != null) {
                    i = csimpm.load(Inscricao.class, i.getIdInscricao());
                    Users u = csimpm.get(Users.class, usr.getUtilizador());
                    int ind = ((ListModelList) lbinscricao.getModel()).indexOf(i.getIdEstudante());
                    if (ind >= 0) {
                        Listitem li = lbinscricao.getItemAtIndex(ind);
                        if (!((Button) (li.getChildren().get(0).getChildren().get(0))).getImage().equals("/icon/down.png")) {
                            return;
                        }
                        if (i.getGrupo() != null) {
                            for (Inscricao in : i.getGrupo().getInscricaoList()) {
                                ind = ((ListModelList) lbinscricao.getModel()).indexOf(in);
                                if (ind >= 0) {
                                    ((ListModelList) lbinscricao.getModel()).set(ind, in);
                                }
                            }
                        } else {
                            ind = ((ListModelList) lbinscricao.getModel()).indexOf(i);
                            if (ind >= 0) {
                                ((ListModelList) lbinscricao.getModel()).set(ind, i);
                            }
                        }
                    }
                }
            }
        };
    }

    public EventListener getEventoRP() {
        return new EventListener() {
            @Override
            public void onEvent(Event event) throws Exception {
                Inscricao i = (Inscricao) event.getData();
                int ind;
                ind = ((ListModelList) lbinscricao.getModel()).indexOf(i);
                if (ind >= 0) {
                    ((ListModelList) lbinscricao.getModel()).remove(ind);
                }
            }
        };
    }

    public EventListener getEventoIA() {
        return new EventListener() {
            @Override
            public void onEvent(Event event) throws Exception {
                Inscricao i = (Inscricao) event.getData();
                if (winInscricao.getParent() != null) {
                    i = csimpm.load(Inscricao.class, i.getIdInscricao());
                    Users u = csimpm.get(Users.class, usr.getUtilizador());
                    if (i.getGrupo() != null) {
                        for (Inscricao in : i.getGrupo().getInscricaoList()) {
                            par.clear();
                            par.put("i", i);
                            List<Inscricaodisciplina> li = csimpm.findByJPQuery("from Inscricaodisciplina id where id.inscricaodisciplinaPK.idInscricao = :i", par);
                            final Iterator<Inscricaodisciplina> items = new ArrayList(li).listIterator();
                            Inscricaodisciplina id;
                            while (items.hasNext()) {
                                id = items.next();
                                int index = ((ListModelList) lbinscricao.getModel()).indexOf(id);
                                if (index >= 0) {
                                    ((ListModelList) lbinscricao.getModel()).set(index, id);
                                }
                            }
                        }
                    } else {
                        int index = ((ListModelList) lbinscricao.getModel()).indexOf(i);
                        if (index >= 0) {
                            ((ListModelList) lbinscricao.getModel()).set(index, i);
                        }
                    }
                }
            }
        };
    }

    public EventListener getEventoIAE() {
        return new EventListener() {
            @Override
            public void onEvent(Event event) throws Exception {
                Inscricao i = (Inscricao) event.getData();
                if (winInscricao.getParent() != null) {
                    i = csimpm.load(Inscricao.class, i.getIdInscricao());
                    Users u = csimpm.get(Users.class, usr.getUtilizador());
                    int index = ((ListModelList) lbinscricao.getModel()).indexOf(i);
                    if (index >= 0) {
                        ((ListModelList) lbinscricao.getModel()).set(index, i);
                    }
                }
            }
        };
    }

    public EventListener getEventoAnul() {
        return new EventListener() {
            @Override
            public void onEvent(Event event) throws Exception {
                Disciplinaanulada da = (Disciplinaanulada) event.getData();
                // Messagebox.show("Entrou");
                if (winInscricao.getParent() != null) {
                    da = csimpm.load(Disciplinaanulada.class, da.getIdanulacao());
                    int ind = ((ListModelList) lbinscricao.getModel()).indexOf(da.getIdEstudante());
                    if (ind >= 0) {
                        Listitem li = lbinscricao.getItemAtIndex(ind);
                        if (!((Button) (li.getChildren().get(0).getChildren().get(0))).getImage().equals("/icon/down.png")) {
                            return;
                        }
                        final Iterator<Inscricaodisciplina> items = new ArrayList(da.getInscricaodisciplinaList()).listIterator();
                        Inscricaodisciplina id;
                        while (items.hasNext()) {
                            id = items.next();
                            int index = ((ListModelList) lbinscricao.getModel()).indexOf(id);
                            if (index >= 0) {
                                ((ListModelList) lbinscricao.getModel()).set(index, id);
                            }
                        }

                    }
                }
                Users user = csimpm.get(Users.class, usr.getUtilizador());
                par.clear();
                par.put("ide", user.getIdEstudante());
                List<Disciplinaanulada> lda = csimpm.findByJPQuery("from Disciplinaanulada da where da.estado is false and da.dataconfirmacao is not null and "
                        + "da.idEstudante = :ide", par);
                if (!lda.isEmpty()) {
                    winanularmot.setVisible(true);
                } else {
                    return;
                }
                String title = "";
                for (Disciplinaanulada dan : lda) {
                    title = title + "\n" + dan.getMotivo();
                }
                winanularmot.setTitle(title);
                txx.setValue(user.getIdEstudante().getIdEstudante() + "");
            }

        };

    }

    public EventListener getEventoAnulE() {
        return new EventListener() {
            @Override
            public void onEvent(Event event) throws Exception {
                Disciplinaanulada da = (Disciplinaanulada) event.getData();
                if (winInscricao.getParent() != null) {
                    da = csimpm.load(Disciplinaanulada.class, da.getIdanulacao());
                    final Iterator<Inscricaodisciplina> items = new ArrayList(da.getInscricaodisciplinaList()).listIterator();
                    Inscricaodisciplina id;
                    while (items.hasNext()) {
                        id = items.next();
                        int index = ((ListModelList) lbinscricao.getModel()).indexOf(id);
                        if (index >= 0) {
                            ((ListModelList) lbinscricao.getModel()).set(index, id);
                        }
                    }
                }
            }
        };
    }
//    public void onModal$winInscricao() {
//        Long ides = Long.parseLong(txx.getText());
//        Estudante e = csimpm.get(Estudante.class, ides);
//        //lbinscricao.setModel(new ListModelList<Inscricao>(e.getInscricaoList()));
//        List<Inscricao> li = e.getInscricaoList();
//        ListModelList lm = new ListModelList<>();
//        for (Inscricao insc : li) {
//            lm.add(insc);
//            for (Inscricaodisciplina id : insc.getInscricaodisciplinaList()) {
//                lm.add(id);
//            }
//        }
//        lbinscricao.setMultiple(false);
//        lbinscricao.setModel(lm);
//        lbinscricao.setMultiple(true);
//    }

    public void onCreate$winInscricao(Event evt) {
        if (winInscricao.inModal()) {
            winInscricao.setMode("modal");
            winInscricao.setTitle("Inscrição");
            winInscricao.setClosable(true);
            winInscricao.setContentStyle("overflow:auto");
            winInscricao.setSizable(true);
            // winInscricao.setVflex("1");
            // winInscricao.setHflex("1");
            winInscricao.setWidth("80%");
            if (usr.getUtilizador() == null) {
                Long ides = Long.parseLong(txx.getText());
                Estudante e = csimpm.get(Estudante.class, ides);
                List<Inscricao> li = e.getInscricaoList();
                ListModelList lm = new ListModelList<>();
                for (Inscricao insc : li) {
                    lm.add(insc);
                    for (Inscricaodisciplina id : insc.getInscricaodisciplinaList()) {
                        lm.add(id);
                    }
                }
                lbinscricao.setMultiple(false);
                lbinscricao.setModel(lm);
                lbinscricao.setMultiple(true);
            }
        }
    }

    public void onClose$winInscricao(Event evt) {
        if (((Window) winInscricao.getParent()).inModal()) {
            winInscricao.getParent().detach();
        }
    }

    public ListModel<Estudante> getEstInscModel() {
        Date dano = new Date();
        Calendar cal = new GregorianCalendar();
        cal.setTime(dano);
        int ano = cal.get(Calendar.YEAR);
        par.clear();
        List<Estudante> estu = new ArrayList<Estudante>();
        if (usr.getUtilizador() != null) {
//            par.put("user", usr.getUtilizador());
//            Users u = csimpm.findEntByJPQuery("from Users u where u.utilizador = :user", par);
//            if (u != null) {
//                par.clear();
//            }
            Faculdade f = csimpm.get(Faculdade.class, usr.getFaculdade().getIdFaculdade());
            par.put("ano", ano);
            par.put("fac", f);
            estu = csimpm.findByJPQuery("from Estudante e where e.cursocurrente.faculdade = :fac and e.idEstudante in (select m.estudante from Matricula m where "
                    + "m.matriculaPK.ano = :ano and m.estudante not in (select ma.matriculaPK.idEstudante from Matriculaanulada ma where ma.matriculaPK.ano = :ano))", par);
        }
        return new ListModelList<Estudante>(estu);
    }

    public void onCreate$combEstInsc(Event evt) throws IOException {
        if (winInscricao.inModal()) {
            //combEstInsc.setSelectedIndex(1);
//            combEstInsc.setSelectedItem(combEstInsc.getItems().get(1));
            long ides = Long.parseLong(txx.getText());

            if (usr.getUtilizador() == null) {
                Estudante e = csimpm.get(Estudante.class, ides);
                combEstInsc.setModel(new ListModelList<Inscricao>(new ArrayList<Inscricao>()));
                combEstInsc.setText(e.getNomeCompleto());
            } else {
                Estudante e = csimpm.get(Estudante.class, ides);
                List<Estudante> le = new ArrayList<Estudante>();
                le.add(e);
                combEstInsc.setModel(new ListModelList<Estudante>(le));
            }
            combEstInsc.setReadonly(true);
//            combEstInsc.removeAttribute("forward");
//           if(combEstInsc.removeForward("onChanging", combEstInsc, "onCombEstInsc")){
//              Messagebox.show("removeu");
//           }else{
//        Messagebox.show("nao removeu");
//        }
            combEstInsc.setAutodrop(false);
            combEstInsc.setAutocomplete(false);
            combEstInsc.setButtonVisible(false);
            combEstInsc.setReadonly(true);
            //onClick$addInsc();
            //onChange$combEstInsc(null);
        }
        if (usr.getUtilizador() != null && usr.getUestudante()) {
            combEstInsc.setAutodrop(false);
            combEstInsc.setAutocomplete(false);
            combEstInsc.setButtonVisible(false);
            combEstInsc.setReadonly(true);
        }
//        
    }

    public void oncreate$txx(Event evt) throws IOException {
        if (winInscricao.inModal()) {
            onAddInsc();
        }
    }

    public void onAddInsc() throws IOException {//Esta funcao pode trazer apenas o semestre ano.. ou pode seleccionar tudo
        if (usr.getUestudante() && usr.getUtilizador() == null) {
            ((Row) winAddInscricao.getFellow("prazoinsc")).setVisible(false);
        }
        Long ides = null;
        Estudante e = null;
        // if (usr.getUestudante()) {
        if (usr.getUestudante() || (!usr.getUestudante() && winInscricao.inModal())) { //para novo ingresso (utilizador null)
            ides = Long.parseLong(txx.getText());
//         else if (usr.getUestudante()) {
            e = csimpm.get(Estudante.class, ides);
        }
//         else if (usr.getUestudante()) {
//            //e = csimpm.get(Users.class, usr.getUtilizador()).getIdEstudante();
//            e = (Estudante) combEstInsc.getSelectedItem().getValue();
//        }
        if (usr.getUestudante() || (!usr.getUestudante() && winInscricao.inModal())) {
            Date dano = new Date();
            Calendar cal = new GregorianCalendar();
            cal.setTime(dano);
            int ano = cal.get(Calendar.YEAR);
            par.clear();
            MatriculaPK mpk = new MatriculaPK(e.getIdEstudante(), ano);
            par.put("mpk", mpk);
            Matricula m = csimpm.findEntByJPQuery("from Matricula m where m.matriculaPK = :mpk", par);
            if (m == null) {
                Clients.showNotification("O Estudante não esta Matriculado", "error", null, null, 0);
                //limpaLB(discSel);
                winAddInscricao.detach();
                return;
            }
            //  par.clear();    //Ver prescricao
            //  par.put("ide", e);
            //  Prescricao p = csimpm.findEntByJPQuery("from Prescricao p where p.inscricaodisciplina.inscricao.idEstudante = :ide"
            //          + " and p.estado is true", par);
            //  if (p != null) {
            //      Clients.showNotification("O estudante prescreveu uma disciplina! So poderá se inscrever depois de regularizar", "error", null, null, 0);
            //limpaLB(discSel);
            //      winAddInscricao.detach();
            //     return;
            // }
            par.clear();   //Limpar inscricoes rejeitadas
            par.put("est", e);
            List<Inscricao> li = csimpm.findByJPQuery("from Inscricao i where i.idEstudante = :est and i.dataConfirmacao is not null and i.estado is false", par);
            if (li != null && !li.isEmpty()) {
                final Iterator<Inscricao> items = new ArrayList(li).listIterator();
                Inscricao in;
                while (items.hasNext()) {
                    in = items.next();
                    par.clear();
                    par.put("i", in.getIdInscricao());
                    // Messagebox.show(in.getIdInscricao()+"");
                    List<Arquivoinscricao> la = in.getArquivoinscricaoList();//csimpm.findByJPQuery("from Arquivoinscricao ai where ai.idInscricao.idInscricao = :i", par);
                    // Messagebox.show(la.size()+"");
                    final Iterator<Arquivoinscricao> items2 = new ArrayList(la).listIterator();
                    Arquivoinscricao ai;
                    while (items2.hasNext()) {
                        ai = items2.next();
                        String dat = new SimpleDateFormat("dd_MM_yyyy_HH_mm_ss").format(in.getDataInscricao());
                        String nome = in.getIdEstudante().getNrEstudante() + ai.getNome() + dat;
                        File test = new File(UserAutentic.getPathR("4") + nome);
                        test.delete();
                    }
                    li.remove(in);
                    csimpm.delete(in);
                }
            }
        }
        winAddInscricao.setParent(winInscricao);
        winAddInscricao.doModal();
        Date dano = new Date();
        Calendar cal = new GregorianCalendar();
        cal.setTime(dano);
        int ano = cal.get(Calendar.YEAR);
        ((Label) winAddInscricao.getFellow("lbano")).setValue(ano + "");
        Map<String, Object> par = new HashMap<String, Object>();
        Map<String, Object> ent = new HashMap<String, Object>();
        PlanificacaoAnoLectivo pal1 = csimpm.findEntByJPQuery("from PlanificacaoAnoLectivo", null);
        if (pal1 != null || pal1.getAnolectivo() == ano) {
            Date d = new Date();
            Date di = pal1.getDatainicioInscricao2();
            // Date df = pal1.getDi1();
            if (d.compareTo(di) >= 0) {
                ((Label) winAddInscricao.getFellow("lbsem")).setValue("Semestre 2");
                sem = 2;
            }
            // di = pal1.getDataInicioMatricula();
            // df = pal1.getDma1();
            if (d.compareTo(di) < 0) {
                ((Label) winAddInscricao.getFellow("lbsem")).setValue("Semestre 1");
                sem = 1;
            }
//            if (sem == 0) {
//                Boolean b = true;
//                if (!usr.getUestudante()) {
//                    ((Div) winAddInscricao.getFellow("prazo")).setVisible(true);
//                    ((Row) winAddInscricao.getFellow("rwmulta")).setVisible(false);
//                    ((Intbox) winAddInscricao.getFellow("ibmulta")).setValue(0);
//                    if (d.after(pal1.getDatainicioInscricao2())) {
//                        ((Label) winAddInscricao.getFellow("lbsem")).setValue("Semestre 2");
//                    } else {
//                        ((Label) winAddInscricao.getFellow("lbsem")).setValue("Semestre 1");
//                    }
//                } else {
//                    winAddInscricao.detach();
//                    Clients.showNotification("Esta fora do prazo de inscrição. Por favor contacte a Direcção", "error", null, null, 0);
//                }
//            }
        } else {
            winAddInscricao.detach();
            Clients.showNotification("Nao existe um plano para o Ano Lectivo!", "error", null, null, 0);
        }

        if (usr.getUestudante() || (!usr.getUestudante() && winInscricao.inModal())) {
//            if (winInscricao.inModal()) {
//                ides = Long.parseLong(txx.getText());
//                e = csimpm.get(Estudante.class, ides);
//            } else {
//                e = csimpm.get(Users.class, usr.getUtilizador()).getIdEstudante();
//                ides = e.getIdEstudante();
//            }
//            if ((e.getTransferido() != null && e.getTransferido().intValue() == 0)
//                    || (e.getMudancac() != null && e.getMudancac().intValue() == 0)) {
////                    e.getIngressomudancauniversidade() != null&&e.getTransferido()==null
////                    || e.getCursocurrente().getIdCurso() != e.getCursoingresso().getIdCurso()) {
//
//                par.clear();
//                par.put("e", e);
//                Inscricaodisciplina id = csimpm.findEntByJPQuery("from Inscricaodisciplina id where id.inscricao.idEstudante = :e"
//                        + " and id.disciplinaActiva = 3 and id.estado is true", par);
//                if (id == null) {
//                    Clients.showNotification("Antes de se inscrever, faça equivalência das suas disciplinas."
//                            + "Entre em contacto com a Direção da sua Faculdade", "warning", null, null, 0);
//                    winAddInscricao.detach();
//                    return;
//                }
//
//            }
            ((Combobox) winAddInscricao.getFellow("combEstInsc")).setText(e.getNomeCompleto());
//            ((Combobox) winAddInscricao.getFellow("combEstInsc")).setReadonly(true);
//            Combobox co = ((Combobox) winAddInscricao.getFellow("combEstInsc"));
//            co.removeAttribute("forward");
//                    if(co.removeForward("onChanging", co, "onCombEstInsc")){
//              Messagebox.show("removeu");
//           }else{
//        Messagebox.show("nao removeu");
//        }
            // ((Combobox) winAddInscricao.getFellow("combEstInsc")).setButtonVisible(false);
            buscarDisciplinas(e, ((Listbox) winAddInscricao.getFellow("discEst")),
                    ((Listbox) winAddInscricao.getFellow("discSel")), ((Label) winAddInscricao.getFellow("lbnivel")),
                    ((Label) winAddInscricao.getFellow("lbsexo")), ((Label) winAddInscricao.getFellow("lbcurso")));
        }

    }

    public void limpForm() {
        combEstInsc.setText("");
        lbcurso.setValue("");
        lbnivel.setValue("");
        lbsexo.setValue("");
        lbcred.setValue("");
        limpar(tbimg4);
        //lbsem.setValue("");
        discEst.setModel(new ListModelList<Disciplina>(new ArrayList<Disciplina>()));
        discEstOp.setModel(new ListModelList<Disciplina>(new ArrayList<Disciplina>()));
        lbimg.setModel(new ListModelList<Arquivo>(new ArrayList<Arquivo>()));
        limpaLB(discSel);
        if (winInscricao.inModal()) {
            return;
        }
        //    combEstInsc.setValue("Pesquise um Estudante...");
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

    public ListModel<Curso> getListaCursoModel() {
        if (usr.getUtilizador() == null) {
            return null;
        }
        //Users u = csimpm.get(Users.class, usr.getUtilizador());
        Faculdade f = csimpm.get(Faculdade.class, usr.getFaculdade().getIdFaculdade());
        par.clear();
        if (f != null) {
            par.put("fac", f);
            List<Curso> lc = csimpm.findByJPQuery("from Curso c where c.faculdade = :fac", par);
            return listaCursoModel = new ListModelList<Curso>(lc);
        } else {
            return null;
        }
    }

    public ListModel<Faculdade> getListaFacModel() {
        return new ListModelList<Faculdade>(csimpm.getAll(Faculdade.class));
    }

    public void onSelectcbfaculdade() {
        if (cbfaculdade.getSelectedItem() != null) {
            cbcurso.setModel(new ListModelList<Curso>());
            cbcurso.setText("-----Curso------");
            condfac = " and e.cursocurrente.faculdade = :fac";
            Faculdade f = (Faculdade) cbfaculdade.getSelectedItem().getValue();
//            if (condpar.containsKey("fac")) {
//                condpar.replace("fac", f);
//            } else {
//                condpar.put("fac", f);
//            }
            par.clear();
            par.put("f", f);
            condcurso = "";
            if (condpar.containsKey("curso")) {
                condpar.remove("curso");
            }
            List<Curso> lc2 = csimpm.findByJPQuery("from Curso c where c.faculdade = :f", par);
            if (lc2 != null && lc2.size() > 0) {
                cbcurso.setModel(new ListModelList<Curso>(lc2));
                setLB(0, 20);
            } else {
                lbinscricao.setModel(new ListModelList<>());
            }
        }

    }

    public ListModel<Estudante> getListInscricaoModel() {
        List<Estudante> li = null;
        par.clear();
        //par.put("curso", (Curso) cbcurso.getSelectedItem().getValue());e.cursocurrente = :curso and
        par.put("ano", ano);
        // li = csimpm.findByJPQuery("from Inscricao i where i.estado is true and i.dataConfirmacao is not null", null);
        li = csimpm.findByJPQuery("from Estudante e where e.idEstudante in (select i.idEstudante from Inscricao"
                + " i where extract(year from i.dataInscricao) = :ano and i.dataConfirmacao is not null and i.estado is true)", par);
        return new ListModelList<Estudante>(li);
    }

    //quando o utilizador for um estudante usa esta lista(usado no forward)
    public void getListInscricaoModel2() {
        List<Inscricao> li = null;
        Users u = csimpm.get(Users.class, usr.getUtilizador());
        par.clear();
        par.put("e", u.getIdEstudante());
        li = csimpm.findByJPQuery("from Inscricao i where i.idEstudante = :e order by i.dataInscricao asc", par);
        ListModelList lm = new ListModelList<>();
        for (int i = li.size() - 1; i >= 0; i--) {
            Inscricao insc = li.get(i);
            lm.add(insc);
            for (Inscricaodisciplina id : insc.getInscricaodisciplinaList()) {
                lm.add(id);
            }
        }
        lbinscricao.setMultiple(false);
        lbinscricao.setRows(lbinscricao.getItemCount() + li.size());
        lbinscricao.setModel(lm);
        lbinscricao.setMultiple(true);
        // return inscric = new ListModelList<Inscricao>(li);
    }

    public void onBtncancelar() {
        limpForm();
        if (!usr.getUestudante() && !((Window) winAddInscricao.getParent()).inModal()) {
            setLB(0, 20);// lbinscricao.setModel(getListInscricaoModel());
        }
//        else {
//            if (!((Window) winAddInscricao.getParent()).inModal()) {
//                //lbinscricao.setModel(
//                getListInscricaoModel2();
//            }
//        }
        winAddInscricao.detach();
//        if (winInscricao.inModal()) {
//            return;
//        }
        tabPagamentoTaxa.setSelected(false);
        tabDadosInscricao.setSelected(true);
    }

//    public void onClick$btnCancelar1() {
//        limpForm();
//        if (!usr.getUestudante() && !((Window) winAddInscricao.getParent()).inModal()) {
//            setLB(0, 20);// lbinscricao.setModel(getListInscricaoModel());
//        }
////        else {
////            if (!((Window) winAddInscricao.getParent()).inModal()) {
////                getListInscricaoModel2();
////            }
////        }
//        winAddInscricao.detach();
//        tabPagamentoTaxa.setSelected(false);
//        tabDadosInscricao.setSelected(true);
//    }
    public ListModel<Estudante> getListEst() {
        Map<String, Object> par = new HashMap<String, Object>();
        Date dano = new Date();
        Calendar cal = new GregorianCalendar();
        cal.setTime(dano);
        int ano = cal.get(Calendar.YEAR);
        par.put("ano", ano);
        List<Estudante> estu = csimpm.findByJPQuery("from Estudante e where e.idEstudante in (select m.estudante from Matricula m where "
                + "m.matriculaPK.ano = :ano and m.estudante not in (select ma.matriculaanuladaPK.idEstudante from Matriculaanulada ma where ma.matriculaanuladaPK.ano = :ano)) "
                + " and e.idEstudante not in (select i.estudante from Inscricao i where extract(yeaer from i.dataInscricao) = :ano)", par);
//        
        return estudantes = new ListModelList<>(estu);
    }
//    public ListModel<Disciplina> getdiscEst() {
//    List<Disciplina> ldisc = csimpm.findByQuery("select d.* from Disciplina d "
//            + "where d.semestre = :semestre and d.curso = :curso and d.nivel = :nivel", ent, par);
//        ldisc.addAll(((Estudante)csimpm.GetUniqueEntityByNamedQuery("Inscricao.findByIdEstudante", 
//                ((Estudante)combEstInsc.getSelectedItem().getValue()).getIdEstudante())).getInscricaoList().get(0).getInscricaodisciplinaList().get(0).);
//        return disciplinas = new ListModelList<Disciplina>(ldisc);
//    }

    public void onSCombEstInsc(Event event) throws IOException {
//        if (combEstInsc.getSelectedItem() == null) {
//            return;
//        }
        if (combEstInsc.getSelectedIndex() == posc.getValue() - 1) {
            onLoadComb();
            combEstInsc.open();
            combEstInsc.setText(pesq);
            return;
        }
        indc = combEstInsc.getSelectedIndex();
        Estudante e = null;
        if (usr.getUtilizador() == null) {
            e = csimpm.get(Estudante.class, Long.parseLong(txx.getText()));
        } else {
            if (combEstInsc.getSelectedItem() == null) {
                return;
            }
            e = (Estudante) combEstInsc.getSelectedItem().getValue();
            e = csimpm.load(Estudante.class, e.getIdEstudante());
        }
        if (!usr.getUestudante() && !winInscricao.inModal()) {
            MatriculaPK mpk = new MatriculaPK(e.getIdEstudante(), ano);
            par.clear();
            par.put("ide", mpk);
            Matricula mat = csimpm.findEntByJPQuery("from Matricula m where m.matriculaPK=:ide", par);
            if (mat.getConfirmacao() == null || mat.getEstado() == false) {
                Clients.showNotification("A matricula do Estudante ainda não esta confirmada!", "error", null, null, 0);
                limpaLB(discEstOp);
                limpaLB(discSel);
                return;
            }
            // par.clear();
            //  par.put("ide", e);
            //  Prescricao p = csimpm.findEntByJPQuery("from Prescricao p where p.inscricaodisciplina.inscricao.idEstudante = :ide"
            //          + " and p.estado is true", par);
            //  if (p != null) {
            //      Clients.showNotification("O estudante prescreveu uma disciplina! So poderá se inscrever depois de regularizar", "error", null, null, 0);
            //     limpaLB(discEstOp);
            //     limpaLB(discSel);
            //      return;
            //  }
        }
        par.clear();
        par.put("e", e);
        List<Inscricao> li = csimpm.findByJPQuery("from Inscricao i where i.idEstudante = :e and i.dataConfirmacao is not null and i.estado is false", par);
        if (li != null && !li.isEmpty()) {
            final Iterator<Inscricao> items = new ArrayList(li).listIterator();
            Inscricao in;
            while (items.hasNext()) {
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
        }
        //}
//        if ((e.getTransferido() != null && e.getTransferido().intValue() == 0) || (e.getMudancac() != null && e.getMudancac().intValue() == 0)) {
//            if (!usr.getUestudante()) {
//                Window win = (Window) Executions.createComponents("/inscricao/inscricaoequivalencia.zul", winAddInscricao, null);
//                ((Textbox) win.getFellow("est")).setText(e.getIdEstudante() + "");
//                ((Button) win.getFellow("btnCnf")).setVisible(false);
//                ((Button) win.getFellow("btnRj")).setVisible(false);
//                win.doModal();
//                combEstInsc.setText("");
//                return;
//            } else {
//                winAddInscricao.detach();
//                Clients.showNotification("Antes de se inscrever, faça equivalencia das suas disciplinas."
//                        + "Entre em contacto com a Direção da sua Faculdade actual", "warning", null, null, 0);
//                return;
//            }
//        }
        buscarDisciplinas(e, discEst, discSel, lbnivel, lbsexo, lbcurso);

    }

    public void buscarDisciplinas(Estudante e, Listbox discEst, Listbox discSel, Label lbnivel, Label lbsexo, Label lbcurso) {
        //ultimo ano que fez inscricao
        Listbox discEstOp = (Listbox) winAddInscricao.getFellow("discEstOp");
        Date dano = new Date();
        Calendar cal = new GregorianCalendar();
        cal.setTime(dano);
        int ano = cal.get(Calendar.YEAR);
        int nc = e.getNivelFrequencia();
        int c = 0;
        this.n = nc;
        Listitem l = new Listitem();
        //l.seth
        e = csimpm.load(Estudante.class, e.getIdEstudante());
        par.clear();
        par.put("fac", e.getCursocurrente().getFaculdade());
        PlanificacaoAnoLectivo pal = csimpm.findEntByJPQuery("from PlanificacaoAnoLectivo p where p.faculdade = :fac", par);
        prazo = (Div) winAddInscricao.getFellow("prazo");
        prazoinsc = (Row) winAddInscricao.getFellow("prazoinsc");
        rwmulta = (Row) winAddInscricao.getFellow("rwmulta");
        ibmulta = (Intbox) winAddInscricao.getFellow("ibmulta");
        lbsem = (Label) winAddInscricao.getFellow("lbsem");
        ((Row) winAddInscricao.getFellow("rwRU")).setVisible(false);
        Date dtaxa1, dtaxa2, dtaxa3;
       // int ms = 0;
        // ms = e.getMatriculaList().size();
        if (lbsem.getValue().equals("Semestre 1")) {
            prazo.setVisible(false);
            sem = 1;
            if (ano != e.getAnoIngresso()) {
                dtaxa1 = pal.getDataInicioMatricula();
                dtaxa2 = pal.getDataFimMatriculaE2();
                dtaxa3 = pal.getDm1();
            } else {
                dtaxa1 = pal.getDatainicioInscricao();
                dtaxa2 = pal.getDataFimIE1();
                dtaxa3 = pal.getDma1();
            }
            // Messagebox.show(dtaxa2+"");
            if (dano.before(dtaxa1)) {
                //      Messagebox.show("sem 1");
                Clients.showNotification("Por favor aguarde o periodo de Inscrição!", "warning", null, null, 0);
                limpaLB(discEstOp);
                limpaLB(discSel);
                winAddInscricao.detach();
            } else if ((ano == e.getAnoIngresso() && dano.after(dtaxa2)) || (ano != e.getAnoIngresso() && dano.after(dtaxa3))) {
                if (!usr.getUestudante()) {
                    if (ano != e.getAnoIngresso()) {//if (ms > 1) {
                        Clients.showNotification("O periodo de Inscricao para os antigos Estudantes esta encerrado! Esta Inscrição será submetida ao Director", "warning", null, null, 0);
                    } else {
                        Clients.showNotification("O periodo de Inscricao para novos Ingressos esta encerrado! Esta Inscrição será submetida ao Director", "warning", null, null, 0);
                    }
                    prazo.setVisible(true);
                    prazoinsc.setVisible(true);
                    rwmulta.setVisible(true);
                    ibmulta.setValue(0);
                } else {
                    Clients.showNotification("O periodo de Inscrição incerrado, Contacte a direção!", "warning", null, null, 0);
                    winAddInscricao.detach();
                }
            }
        } else {
            prazo.setVisible(false);
            sem = 2;
            dtaxa1 = pal.getDatainicioInscricao2();
            dtaxa2 = pal.getDataFimE2();
            dtaxa3 = pal.getDi1();
            if (dano.before(dtaxa1)) {
                Clients.showNotification("Por favor aguarde o periodo de Inscrição!", "warning", null, null, 0);
                limpaLB(discEstOp);
                limpaLB(discSel);
                winAddInscricao.detach();
            } else if (dano.after(dtaxa3)) {
                if (!usr.getUestudante()) {
                    Clients.showNotification("O periodo de Inscrição esta encerrado! Esta Inscrição será submetida ao Director", "warning", null, null, 0);
                    prazo.setVisible(true);
                    prazoinsc.setVisible(true);
                    rwmulta.setVisible(true);
                    ibmulta.setValue(0);
                } else {
                    Clients.showNotification("O periodo de Inscrição incerrado, Contacte a direção!", "warning", null, null, 0);
                    winAddInscricao.detach();
                }
            }
        }
        ((Intbox) winAddInscricao.getFellow("idcurso")).setValue(e.getCursocurrente().getIdCurso().intValue());
        ((Intbox) winAddInscricao.getFellow("idplano")).setValue(e.getPlanoc());
        //mostrar a lista de prescricoes
//        if (lbsem.getValue().equals("Semestre 1")) {
//            par.clear();
//            par.put("ano", ano - 1);
//            par.put("ide", e);
//            List<Prescricao> p1 = csimpm.findByJPQuery("from Prescricao p where p.inscricaodisciplina.inscricao.idEstudante = :ide"
//                    + " and extract(year from p.inscricaodisciplina.inscricao.dataInscricao)=:ano", par);
//            String lab = "";
//            for (Prescricao pe : p1) {
//                lab = lab + (ano - 1) + " " + pe.getInscricaodisciplina().getDisciplina().getNome() + "\n";
//            }
//            ((Label) winAddInscricao.getFellow("lbprescricao")).setValue(lab);
//        } else {
//            String lab = "";
//            par.clear();
//            par.put("ano", ano);
//            List<Prescricao> p1 = csimpm.findByJPQuery("from Prescricao p where p.inscricaodisciplina.inscricao.idEstudante = :ide"
//                    + " and extract(year from p.inscricaodisciplina.inscricao.dataInscricao)=:ano", par);
//            for (Prescricao pe : p1) {
//                lab = lab + (ano - 1) + " " + pe.getInscricaodisciplina().getDisciplina().getNome() + "\n";
//            }
//            par.clear();
//            par.put("ano", ano - 1);
//            List<Prescricao> p2 = csimpm.findByJPQuery("from Prescricao p where p.inscricaodisciplina.inscricao.idEstudante = :ide"
//                    + " and extract(year from p.inscricaodisciplina.inscricao.dataInscricao)=:ano"
//                    + " and p.inscricaodisciplina.inscricao.semestre=2", par);
//            for (Prescricao pe : p2) {
//                lab = lab + (ano - 1) + " " + pe.getInscricaodisciplina().getDisciplina().getNome() + "\n";
//            }
//            ((Label) winAddInscricao.getFellow("lbprescricao")).setValue(lab);
//        }
        par.put("ide", e.getIdEstudante());
        lbcurso.setValue(e.getCursocurrente().getDescricao());//csimpm.findByQuery("select c.descricao from fecn1.curso c, fecn1.estudante e where c.id_curso = e.cursocurrente "
        // + "and e.id_estudante = :ide", null, par).get(0).toString());
        lbnivel.setValue(e.getNivelFrequencia() + "");
        if (e.getMasculino()) {
            lbsexo.setValue("Masculino");//csimpm.findByQuery("select s.descricao from fecn1.sexo s, fecn1.estudante e where s.id_sexo = e.sexo "
        }                //+ "and e.id_estudante = :ide", null, par).get(0).toString());
        if (!e.getMasculino()) {
            lbsexo.setValue("Feminino");
        }

        par.clear();
        // int anoi = (e.getAnoIngresso() + e.getNivelFrequencia()) - 1;
        par.put("nivel", e.getNivelFrequencia());
        par.put("curso", e.getCursocurrente());
        par.put("niv", "%" + e.getAnoIngresso() + "%");
        par.put("planoc", e.getPlanoc());
        int nn = nc + 1;
        int c1 = 0;
        int c2 = 0;

        int co1 = csimpm.findByJPQuery("from Disciplina d where d.curso=:curso and d.planoc = :planoc and d.semestre = 1 and"
                + " d.nivel =:nivel and ((d.natraso is not null and d.natraso not like :niv) or d.natraso is null)", par).size();

        int co2 = csimpm.findByJPQuery("from Disciplina d where d.curso=:curso and d.planoc = :planoc and d.semestre = 2 and"
                + " d.nivel =:nivel and ((d.natraso is not null and d.natraso not like :niv) or d.natraso is null)", par).size();
        par.clear();
        par.put("e", e);
        par.put("anoi", e.getNivelFrequencia());
        par.put("planoc", e.getPlanoc());
        par.put("ano", ano);
        // par.put("niv", "%"+e.getAnoIngresso() + "%");

        c1 = csimpm.findByJPQuery("from Inscricaodisciplina id where id.inscricao.idEstudante = :e and "
                + "id.disciplina.nivel = :anoi and id.disciplina.planoc = :planoc and id.disciplina.semestre = 1 and (id.disciplinaActiva = 3"
                + " and id.estado is true and ((id.notaFinal is not null and id.notaFinal >= 10) or (id.notaFinal is null and extract(year from id.inscricao.dataInscricao) < :ano)))", par).size();
        c2 = csimpm.findByJPQuery("from Inscricaodisciplina id where id.inscricao.idEstudante = :e and "
                + "id.disciplina.nivel = :anoi and id.disciplina.planoc = :planoc and id.disciplina.semestre = 2 and (id.disciplinaActiva = 3"
                + " and id.estado is true and ((id.notaFinal is not null and id.notaFinal >= 10) or (id.notaFinal is null and extract(year from id.inscricao.dataInscricao) < :ano)))", par).size();
        //  if (sem == 1) {
        //   if (((co1 - c1) <= 2) && ((co2 - c2) <= 2)) {// so no primeiro semestre
        // Messagebox.show(co1+" - "+c1+"+) + ("+co2+" - "+c2);
        int t1 = 0, t2 = 0;
        if (co1 >= c1) {
            t1 = co1 - c1;
        }
        if (co2 >= c2) {
            t2 = co2 - c2;
        }
        //   if (((co1 == 0 && co2 != 0) || (co1 == 0 && co2 == 0)) && 
        if (t1 + t2 <= 2) {// so no primeiro semestre
            nc = nc + 1;
            this.n = nc;
            //  Messagebox.show("dxfsdf");
        }
        //    }
        // if (true) {
        //  Messagebox.show(this.n+"  "+e.getNivelFrequencia());
        if (this.n == e.getNivelFrequencia()) {//"sem == 1 &&" nao entra semestre porque oaluno pode aparecer no 2o sem
            final Long es = e.getIdEstudante();
            final int npl = e.getCursocurrente().getPlanoc();
            if (e.getPlanoc() < npl
                    && e.getNivelFrequencia() <= ((ano - npl) + 1)) {
                Messagebox.show("O estudante encontra-se no nivel de transicao para o novo curriculo.\n "
                        + "Portanto, devera transitar para o novo curriculo!\n Continuar?: ", "", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION,
                        new EventListener() {
                            @Override
                            public void onEvent(Event evet) throws InterruptedException {
                                switch (((Integer) evet.getData()).intValue()) {
                                    case Messagebox.YES:
                                        Estudante estud = csimpm.get(Estudante.class, es);

                                        par.clear();   //Limpar inscricoes rejeitadas
                                        par.put("est", estud);
                                        List<Inscricao> li = csimpm.findByJPQuery("from Inscricao i where i.idEstudante = :est and i.modoInscricao=7", par);
                                        if (li != null && !li.isEmpty()) {
                                            final Iterator<Inscricao> items = new ArrayList(li).listIterator();
                                            Inscricao in;
                                            while (items.hasNext()) {
                                                in = items.next();
//                    par.clear();
//                    par.put("i", in.getIdInscricao());
//                    // Messagebox.show(in.getIdInscricao()+"");
//                    List<Arquivoinscricao> la = in.getArquivoinscricaoList();//csimpm.findByJPQuery("from Arquivoinscricao ai where ai.idInscricao.idInscricao = :i", par);
//                    // Messagebox.show(la.size()+"");
//                    final Iterator<Arquivoinscricao> items2 = new ArrayList(la).listIterator();
//                    Arquivoinscricao ai;
//                    while (items2.hasNext()) {
//                        ai = items2.next();
//                        String dat = new SimpleDateFormat("dd_MM_yyyy_HH_mm_ss").format(in.getDataInscricao());
//                        String nome = in.getIdEstudante().getNrEstudante() + ai.getNome() + dat;
//                        File test = new File(UserAutentic.getPathR("4") + nome);
//                        test.delete();
//                    }
                                                li.remove(in);
                                                csimpm.delete(in);
                                            }
                                        }
                                        Long iec = estud.getCursocurrente().getIdCurso();
                                        par.clear();
                                        par.put("e", estud);
                                        par.put("planoc", estud.getPlanoc());
                                        //nao deve converter se tiver uma disciplina sem nota.................................
                                        List<Inscricaodisciplina> ldinscricao = csimpm.findByJPQuery("from Inscricaodisciplina id where id.inscricao.idEstudante = :e and "
                                                + "id.disciplina.planoc = :planoc and (id.disciplinaActiva = 3"
                                                + " and id.estado is true and (id.notaFinal is not null and id.notaFinal >= 10))", par);
                                        par.clear();
                                        par.put("curso", estud.getCursocurrente());
                                        par.put("planoc", npl);
                                        List<Disciplina> ldnovo = csimpm.findByJPQuery("from Disciplina d where d.curso=:curso and d.planoc = :planoc", par);
                                        Disciplina di = null;
                                        final Iterator<Disciplina> items = new ArrayList(ldnovo).listIterator();
                                        while (items.hasNext()) {
                                            di = items.next();
                                            int contador = 0;
                                            float soma = 0;
                                            boolean b1 = false;
                                            boolean b2 = true;
                                            //  List<Equivalencia>  lequiv=csimpm.findByJPQuery("", condpar)
                                            Inscricaodisciplina iddi = null;//ultima   inscricaodaequivalencia
                                            final Iterator<Equivalencia> items2 = new ArrayList(di.getEquivalenciaList1()).listIterator();
                                            Equivalencia eq = null;
                                            while (items2.hasNext()) {
                                                eq = items2.next();
                                                b1 = false;
                                                if (eq.getDisciplina().getCurso().getIdCurso() == iec) {
                                                    for (Inscricaodisciplina in : ldinscricao) {
                                                        if (in.getDisciplina().getIdDisc() == eq.getDisciplina().getIdDisc()) {
                                                            soma = soma + in.getNotaFinal();
                                                            contador = contador + 1;
                                                            b1 = true;
                                                            iddi = in;
                                                        }
                                                    }
                                                    if (b1 == false) {
                                                        b2 = false;
                                                    }
                                                }
                                            }
                                            if (b1 == true && b2 == true) {
                                                Inscricao oi = iddi.getInscricao();
                                                Inscricao ieq = new Inscricao();
                                                Calendar ca = new GregorianCalendar();
                                                ca.setTime(oi.getDataInscricao());
                                                int ms = ca.get(Calendar.MILLISECOND);
                                                ca.add(Calendar.MILLISECOND, ms + 2);
                                                ieq.setEmailenviado(true);
                                                ieq.setDataInscricao(ca.getTime());
                                                ieq.setModoInscricao(new Short("7"));
                                                ieq.setEstado(true);
                                                ieq.setDataConfirmacao(oi.getDataConfirmacao());
                                                ieq.setFuncionario(oi.getFuncionario());
                                                ieq.setTaxaInscricao(Float.NaN);
                                                ieq.setSemestre(di.getSemestre().shortValue());
                                                ieq.setIdEstudante(estud);
                                                csimpm.Save(ieq);
                                                Inscricaodisciplina id = new Inscricaodisciplina();
                                                id.setEstado(true);
                                                id.setDisciplinaActiva(new Short("3"));
                                                id.setInscricao(ieq);
                                                BigDecimal bd = new BigDecimal(soma / contador).setScale(0, RoundingMode.HALF_UP);
                                                id.setNotaFinal(bd.floatValue());
                                                id.setDisciplina(di);
                                                id.setTurma(iddi.getTurma());
                                                id.setTurno(iddi.getTurno());
                                                id.setInscricaodisciplinaPK(new InscricaodisciplinaPK(ieq.getIdInscricao(), di.getIdDisc()));
                                                csimpm.Saves(id);

                                            }
                                        }
                                        estud.setPlanoc(npl);
                                        csimpm.update(estud);
                                        // refre(estud);
                                        Clients.showNotification(" A sua transicao para o novo curriculo foi realizada com sucessos\nPortanto, ja podera efectuar uma nova inscricao", null, null, null, 0);
//                                        limpForm();
//                                        winAddInscricao.detach();
                                        break;
                                    case Messagebox.NO:
                                        //   winAddInscricao.detach();
                                        if (combEstInsc != null) {
                                            onBtncancelar();//pra fechar o winAddInscricao se estiver aberto
                                        }
                                        return;
                                }
                            }
                        });
                if (combEstInsc == null) {
                    winAddInscricao.detach();
                }
                return;
            }

        }

        par.clear();
        par.put("semestre", sem);
        par.put("curso", e.getCursocurrente());
        par.put("nivel", e.getNivelFrequencia());//nc);
        par.put("ide", e.getIdEstudante());
        par.put("planoc", e.getPlanoc());
        ent.clear();
        ent.put("d", Disciplina.class);
        //DISCIPLINAS INSCRITAS/FEITAS SAO AQUELAS QUE TEM NOTA OU NAO, COM ESTADO TRUE
        // ATRASADAS  que nao fez mas e nao esta tenha precedencia com disciplinas que nunca fiz
        List<Disciplina> ldisc = csimpm.findByQuery("select d.* from disciplina d "
                + "where d.semestre = :semestre and d.curso = :curso and d.planoc = :planoc and d.nivel "
                + "< :nivel and d.caracter = 1 and d.id_disc not in (select idi.id_disciplina from inscricaodisciplina idi, "
                + "inscricao i where idi.id_inscricao = i.id_inscricao and i.id_estudante = :ide and idi.disciplina_activa = 3 "
                + "and idi.estado is true and ((idi.nota_final is not null and idi.nota_final >=10) or idi.nota_final is null)) "
                + "and d.id_disc not in (select p.id_disc from precedencia p where "
                + " p.precedencia not in (select idi.id_disciplina from inscricaodisciplina idi, "
                + "inscricao i where idi.id_inscricao = i.id_inscricao and i.id_estudante = :ide "
                + "and idi.estado is true and idi.disciplina_activa = 3 and idi.nota_final is not null and idi.nota_final >=10))", ent, par);
        List<Disciplina> ld2 = null;
        //edit
        //ATRASADA do meu nivel que reprovei ou nao fiz mas que nao tenho precedencia
        ld2 = csimpm.findByQuery("select d.* from disciplina d "
                + "where d.semestre = :semestre and d.curso = :curso and d.planoc = :planoc and d.nivel "
                + "= :nivel and d.caracter = 1 and d.id_disc not in (select idi.id_disciplina from inscricaodisciplina idi, "
                + "inscricao i where idi.id_inscricao = i.id_inscricao and i.id_estudante = :ide and idi.disciplina_activa = 3 "
                + "and idi.estado is true and ((idi.nota_final is not null and idi.nota_final >=10) or idi.nota_final is null)) "
                + "and d.id_disc not in (select p.id_disc from precedencia p where "
                + " p.precedencia not in (select idi.id_disciplina from inscricaodisciplina idi, "
                + "inscricao i where idi.id_inscricao = i.id_inscricao and i.id_estudante = :ide "
                + "and idi.estado is true and idi.disciplina_activa = 3 and idi.nota_final is not null and idi.nota_final >=10))", ent, par);
        ldisc.addAll(ld2);
        if (t1 + t2 <= 2) {
            //Do meu novo nivel que nao tenha precedencia (que reprovei ou nao fiz) .. porque o plano pode mudar e uma disc. sair de 1 para 3 ano
            par.replace("nivel", this.n);
            ld2 = csimpm.findByQuery("select d.* from disciplina d "
                    + "where d.semestre = :semestre and d.curso = :curso and d.planoc = :planoc and d.nivel "
                    + "= :nivel and d.caracter = 1 and d.id_disc not in (select idi.id_disciplina from inscricaodisciplina idi, "
                    + "inscricao i where idi.id_inscricao = i.id_inscricao and i.id_estudante = :ide and idi.disciplina_activa = 3 "
                    + "and idi.estado is true and ((idi.nota_final is not null and idi.nota_final >=10) or idi.nota_final is null)) "
                    + "and d.id_disc not in (select p.id_disc from precedencia p where "
                    + " p.precedencia not in (select idi.id_disciplina from inscricaodisciplina idi, "
                    + "inscricao i where idi.id_inscricao = i.id_inscricao and i.id_estudante = :ide "
                    + "and idi.estado is true and idi.disciplina_activa = 3 and idi.nota_final is not null and idi.nota_final >=10))", ent, par);
            ldisc.addAll(ld2);
        }

        // if (ldisc.size() == 0 && nc < nn && ((c1 != 0 && sem == 1) || (c2 != 0 && sem == 2))) { //se nao tenho disciplinas atrazadas para alem do meu nivel reprovado, posso fazer algumas disciplinas
        //do novo ano
        par.clear();  //Disciplinas qua ja escreveu no semestre actual;
        par.put("ide", e);
        par.put("semestre", new Short(sem + ""));
        par.put("curso", e.getCursocurrente());
        par.put("planoc", e.getPlanoc());
        par.put("ano", ano);
        List<Disciplina> lds = csimpm.findByJPQuery("from Disciplina d where d.curso=:curso and d.planoc = :planoc and d in (select id.disciplina from Inscricaodisciplina"
                + " id where id.inscricao.idEstudante = :ide and id.estado is true and id.inscricao.semestre = :semestre and "
                + "extract(year from id.inscricao.dataInscricao) = :ano)", par);
        ldisc.removeAll(lds); //retirar as disciplinas que ja se escreveu no semestre actual
        List<Disciplina> ldsi = csimpm.findByJPQuery("from Disciplina d where ((d.curso=:curso and d.planoc != :planoc) or d.curso!=:curso) and d in (select id.disciplina from Inscricaodisciplina"
                + " id where id.inscricao.idEstudante = :ide and id.estado is true and id.inscricao.semestre = :semestre and "
                + "extract(year from id.inscricao.dataInscricao) = :ano)", par);

        ///disciplinas em atraso color o meu maior nivel
        int nivelactual;
        t1 = 0;
        t2 = 0;
        if (co1 >= c1) {
            t1 = co1 - c1;
        }
        if (co2 >= c2) {
            t2 = co2 - c2;
        }
        if ((sem == 1 && (t1 + t2 <= 2))) {
            nivelactual = nn;
        } else {
            nivelactual = this.n;
        }
        for (Disciplina d : ldisc) {
            if (d.getNatraso() != null && d.getNatraso().contains(e.getAnoIngresso() + "")) {
                d.setNivel(nivelactual);
            }
        }

        discEst.setMultiple(false);
        discEst.setModel(new ListModelList<Disciplina>(ldisc));
        discEst.setMultiple(true);

        limpaLB(discSel);
        for (Disciplina d : lds) {
            d = csimpm.get(Disciplina.class, d.getIdDisc());
            if (d.getNatraso() != null && d.getNatraso().contains(e.getAnoIngresso() + "")) {
                d.setNivel(nivelactual);
            }
            Listitem list = new Listitem();
            Listcell lc = new Listcell();
            Label le = new Label(d.getNome() + " - " + d.getNivel() + "º Ano - " + d.getSemestre() + "º Sem - " + d.getCredito() + " Credito " + d.getCurso().getAbreviatura() + "-" + d.getPlanoc());
            le.setParent(lc);
            lc.setParent(list);
            list.setValue(d);
            list.setLabel(" ");
            list.setDisabled(true);
            list.setCheckable(false);
            discSel.setMultiple(false);
            discSel.appendChild(list);
            discSel.setMultiple(true);
        }
        for (Disciplina d : ldsi) {
            d = csimpm.get(Disciplina.class, d.getIdDisc());
//            if (d.getNatraso() != null && d.getNatraso().contains(e.getAnoIngresso() + "")) {
//                d.setNivel(nivelactual);
//            }
            Listitem list = new Listitem(d.getNome() + " - " + d.getNivel() + "º Ano - " + d.getSemestre() + "º Sem - " + d.getCredito() + " Credito " + d.getCurso().getAbreviatura() + "-" + d.getPlanoc(), d);
            list.setDisabled(true);
            list.setCheckable(false);
            discSel.setMultiple(false);
            discSel.appendChild(list);
            discSel.setMultiple(true);
        }
        int i = 0;
        if (discSel.getItemCount() > 0) {
            for (Listitem ide : discSel.getItems()) {
                if (!ide.getLabel().isEmpty()) {
                    i = i + ((Disciplina) ide.getValue()).getCredito();
                }
            }
        }
        ((Label) winAddInscricao.getFellow("lbcred")).setValue(i + "");
    }

    public boolean menorLevel(int n) {//verificar se existem 3 nivel abaixo do level
        for (Listitem ide : discEst.getItems()) {
            int ni = ((Disciplina) ide.getValue()).getNivel();
            if (ni < n) {
                if (ide.isSelected()) {  //se existe alguem abaixo de mim que nao esta seleccionado
                    return true;
                }
            }
        }
        return false;
    }

    public boolean nivelMenor(int n) {
        for (Listitem ide : discEst.getItems()) {
            int ni = ((Disciplina) ide.getValue()).getNivel();
            if (ni < n) {
                if (ni < this.n) {
                    if (!ide.isSelected()) {
                        if (menorLevel(ni)) {// se retornar true, entao existem menor seleccionado dos niveis balanceados
                            return true;    // salvo se abaixo de mim existir alguem que esteja seleccionado
                        }
                        return false;
                    }
                } else {
                    if (!ide.isSelected()) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public void onBtnva() {
        //REGRAS
        //  se o nivel for maior ou igual ao level, procura saber se nao existe outro mais menor que
        //nao esta seleccionado = seleccione as disciplinas atrazadas  //isto deveria ser processado uma vez

        //se o nivel e menor que level, se esta completo=seleccione no maximo duas disc de um nivel inferior //isto deveria ser processado uma vez
        int qda = 0;
        Boolean b = false;
        int a = 0;
        int d = 0;
        int c = 0;
        Boolean nmenor1 = false;
        Boolean nmenor2 = false;
        List<Integer> niv = new ArrayList<Integer>();
        if (discEst.getSelectedItems().isEmpty() && discEstOp.getSelectedItems().isEmpty()) {
            Clients.showNotification("Seleccione as disciplinas para a inscrição", "error", null, null, 0);
            return;
        }
        if (Integer.parseInt(lbcred.getValue()) > 48) {
            Clients.showNotification("Ultrapassou o limite de credito", "error", null, null, 0);
            return;
        }
        for (Listitem ide : discEst.getSelectedItems()) {
            int ni = ((Disciplina) ide.getValue()).getNivel();
            if (ni > this.n) {//nmenor1 == false && 
                nmenor1 = nivelMenor(ni);
                if (!nmenor1) {
                    Clients.showNotification("Por favor, Primeiro Seleccione as disciplinas dos Níveis em atraso", "error", null, null, 0);
                    return;
                }
            }
            if (ni == this.n) {//nmenor2 == false && 
                nmenor2 = nivelMenor(ni);
                if (!nmenor2) {
                    Clients.showNotification("Por favor, Primeiro Seleccione as disciplinas dos Níveis em atraso", "error", null, null, 0);
                    return;
                }
            }
            if (ni < this.n) {
                qda = qda + 1;
            }
            if (a == 0 && d == 0 && c == 0) {
                a = ni;
            }
            if (a != 0 && c == 0 && ni != a) {
                c = ni;
            }
            if (a != 0 && c != 0 && ni != a && ni != c) {
                b = true;
            }
        }
        for (Listitem ide : discEstOp.getSelectedItems()) {
            int ni = ((Disciplina) ide.getValue()).getNivel();
            if (ni < this.n) {
                qda = qda + 1;
            }
            if (a == 0 && ni != this.n) {
                a = ni;
            }
            if (ni != a && ni != this.n) {
                b = true;
            }
        }
        for (Listitem ide : discSel.getItems()) {
            int ni = ((Disciplina) ide.getValue()).getNivel();
            if (ni < this.n) {
                qda = qda + 1;
            }
            if (a == 0 && d == 0 && c == 0) {
                a = ni;
            }
            if (a != 0 && c == 0 && ni != a) {
                c = ni;
            }
            if (a != 0 && c != 0 && ni != a && ni != c) {
                b = true;
            }
        }
        if (qda > 2) {
            Clients.showNotification("Por favor, seleccione no maximo duas disciplinas dos niveis inferiores", "error", null, null, 0);
            return;
        }
        if (b) {
            Clients.showNotification("Por favor, so podera se inscrever em dois niveis simultaneamente", "error", null, null, 0);
            return;
        }
        if (discEst.getItemCount() > 0 && discEst.getSelectedCount() > 0) {
            final Iterator<Listitem> items = new ArrayList(discEst.getSelectedItems()).listIterator();
            Listitem li;
            while (items.hasNext()) {
                li = items.next();
                discSel.setMultiple(false);
                li.setLabel(" ");
//                Disciplina di = (Disciplina) li.getValue();
//                di = csimpm.load(Disciplina.class, di.getIdDisc());
//                if (di.getCaracter().getDescricao().equals("Obrigatorio")) {
//                    li.setLabel(" ");
//                }
                discSel.appendChild(li);

                discSel.setMultiple(true);
            }
        }
        if (discEstOp.getItemCount() > 0 && discEstOp.getSelectedCount() > 0) {
            final Iterator<Listitem> items = new ArrayList(discEstOp.getSelectedItems()).listIterator();
            Listitem li;
            while (items.hasNext()) {
                li = items.next();
                discSel.setMultiple(false);
                discSel.appendChild(li);
                discSel.setMultiple(true);
            }
        }
        discSel.clearSelection();
    }

    public boolean maiorNivelOp(int n) {
        for (Listitem ide : discSel.getItems()) {
            int ni = ((Disciplina) ide.getValue()).getNivel();
            if (ni < n) {
                if (ni < this.n) {
                    if (!ide.isSelected()) {
                        if (!menorLevel(ni)) {// se retornar true, entao existem menor seleccionado dos niveis balanceados
                            return false;
                        }
                    }
                } else {
                    if (!ide.isSelected()) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public boolean maiorNivelOb(int n) {
        final Iterator<Listitem> items = new ArrayList(discSel.getItems()).listIterator();
        Listitem ide;
        while (items.hasNext()) {
            ide = items.next();
            if (!ide.getLabel().isEmpty()) {
                int ni = ((Disciplina) ide.getValue()).getNivel();
                if (ni > n && ni >= this.n && ide.isCheckable()) {                        ///TODOS DOS NIVEIS MAIORES DEVERAO SAIR exo do levelcepto que esta abaix
                    discEst.setMultiple(false);
                    discEst.appendChild(ide);
                    discEst.setMultiple(true);
                }
                if (ni == n && ni < this.n && (!ide.isSelected()) && ide.isCheckable()) {   //MEU IRMAO ABAIXO DO LEVEL QUE NAO ESTA SELECCIONADO(se nao estiver desabilitado) TAMBEM DEVERA SAIR
                    discEst.setMultiple(false);
                    discEst.appendChild(ide);
                    discEst.setMultiple(true);
                }
            }
        }
        return true;
    }

    public void onBtnvem() {
        if (discSel.getSelectedItems().isEmpty()) {
            Clients.showNotification("Seleccione as disciplinas para restaurar", "error", null, null, 0);
            return;
        }
        final Iterator<Listitem> items = new ArrayList(discSel.getSelectedItems()).listIterator();
        Listitem li;
        while (items.hasNext()) {
            li = items.next();
            if (!li.getLabel().isEmpty()) {
                int ni = ((Disciplina) li.getValue()).getNivel();
                maiorNivelOb(ni);
                discEst.setMultiple(false);
                discEst.appendChild(li);
                discEst.setMultiple(true);
            } else {
                // discEstOp.setMultiple(false);
                // discEstOp.appendChild(li);
                // discEstOp.setMultiple(true);
                new Listbox().appendChild(li);
            }
        }
        int i = 0;
        if (discSel.getItemCount() > 0) {
            for (Listitem ide : discSel.getItems()) {
                i = i + ((Disciplina) ide.getValue()).getCredito();
            }
        }
        lbcred.setValue(i + "");
        discEst.clearSelection();
        discSel.clearSelection();
    }

    public void onSelect$discEst() {
        int i = 0;
        int c = 0;
        lbcred.setStyle("color:black");
        discEst.renderAll();
        for (Listitem ide : discEst.getSelectedItems()) {
            i = i + ((Disciplina) ide.getValue()).getCredito();
        }
        if (discEstOp.getItemCount() > 0 && discEst.getSelectedCount() != discEst.getItemCount()) {
            discEstOp.clearSelection();
        } else {
            for (Listitem ide : discEstOp.getSelectedItems()) {
                i = i + ((Disciplina) ide.getValue()).getCredito();
            }
        }
        if (discSel.getItemCount() > 0) {
            for (Listitem ide : discSel.getItems()) {
                i = i + ((Disciplina) ide.getValue()).getCredito();
            }
        }
        lbcred.setValue(i + "");
        if (i > 48) {
            lbcred.setStyle("color:#dc291e");
        }
    }

    public void onSelect$discEstOp() {
        int i = 0;
        lbcred.setStyle("color:black");
        discEstOp.renderAll();
        for (Listitem ide : discEst.getSelectedItems()) {
            i = i + ((Disciplina) ide.getValue()).getCredito();
        }
        if (discEst.getItemCount() > 0 && discEst.getSelectedCount() != discEst.getItemCount()) {
            discEstOp.clearSelection();
            Clients.showNotification("Primeiro seleccione todas disciplinas prioritárias", "error", null, null, 0);
        } else {
            for (Listitem ide : discEstOp.getSelectedItems()) {
                i = i + ((Disciplina) ide.getValue()).getCredito();
            }
        }
        if (discSel.getItemCount() > 0) {
            for (Listitem ide : discSel.getItems()) {
                i = i + ((Disciplina) ide.getValue()).getCredito();
            }
        }
        lbcred.setValue(i + "");
        if (i > 48) {
            lbcred.setStyle("color:#dc291e");
        }
    }

    public void removerArquivo(List<Arquivoinscricao> la, List<Media> lm) {
        for (int i = 0; i < la.size(); i++) {
            la.remove(i);
            lm.remove(i);
        }
    }

    public void onBtnsave() throws IOException {
        int fac = 0;
        int turno = 0, turma = 0;
        Faculdade f = null;
        Map<String, Object> par = new HashMap<String, Object>();
        Map<String, Object> ent = new HashMap<String, Object>();
        List<Arquivoinscricao> larq = new ArrayList<Arquivoinscricao>();
        List<Media> lmed = new ArrayList<Media>();
        String s = SecurityContextHolder.getContext().getAuthentication().getName();
        par.put("util", s);
        ent.put("f", Funcionario.class);
        Estudante e = null;
        if (combEstInsc.isButtonVisible()) {
            e = (Estudante) combEstInsc.getSelectedItem().getValue();
            e = csimpm.load(Estudante.class, e.getIdEstudante());
            f = e.getCursocurrente().getFaculdade();
            fac = f.getIdFaculdade();
        } else {
            e = csimpm.get(Estudante.class, Long.parseLong(txx.getText()));
            f = e.getCursocurrente().getFaculdade();
            fac = f.getIdFaculdade();
        }
        Inscricaodisciplina id;
        Inscricao i = new Inscricao();
        List<Inscricaodisciplina> lid = new ArrayList<Inscricaodisciplina>();
        Date datai = new Date();
        i.setDataInscricao(datai);
        if (!usr.getUestudante()) {
            if (prazo.isVisible()) {
                i.setEstado(false);
                i.setModoInscricao(new Short("3")); //pedido para director
                i.setObs(txMot.getText());
            } else {
                i.setEstado(true);
                i.setModoInscricao(new Short("0"));
                i.setDataConfirmacao(new Date());
            }
            Users u = csimpm.get(Users.class, usr.getUtilizador());
            i.setFuncionario(u.getIdFuncionario());
        } else {
            i.setEstado(false);
            i.setModoInscricao(new Short("1")); //pedido para funcionario
        }
        turma = e.getTurma();
        turno = e.getTurno();
        i.setIdEstudante(e);
        if (((Label) winAddInscricao.getFellow("lbsem")).getValue().equals("Semestre 2")) {
            i.setSemestre(new Short("2"));
        } else {
            i.setSemestre(new Short("1"));
        }
        i.setTaxaInscricao(Float.parseFloat(lbtotal.getValue()));
        if (ibmulta.getValue() != null && ibmulta.getValue() != 0) {
            i.setMulta(ibmulta.getValue().floatValue());
        } else {
            i.setMulta(new Double(0.0).floatValue());//Float.parseFloat("0"));
        }
        // i.setIdInscricao(3); 
        String est = e.getNomeCompleto();
        //Verificar se ja fez pagamento numa outra inscricao do semestre currente
//        par.put("e", e);
//        par.put("a", ano);
//        par.put("semestre", i.getSemestre());
//         if (csimpm.findByJPQuery("from Inscricao i where i.idEstudante = :e and"
//         + " extract(year from i.dataInscricao) = :a and i.semestre = :sem and i.valor", par).size() > 0) {
//         }
        tbimg4.getText();
        final Iterator<Listitem> items1 = new ArrayList(lbimg.getItems()).listIterator();
        Media m2 = null;
        removerArquivo(larq, lmed);
        boolean ru = false;
        while (items1.hasNext()) {
            Listitem li = items1.next();
            m2 = ((Arquivo) li.getValue()).getMedia();
            Arquivoinscricao arq3 = new Arquivoinscricao();
            arq3.setNome(m2.getName());
            arq3.setNrtalao(((Textbox) li.getChildren().get(0).getChildren().get(0)).getText());
            arq3.setBanco(((Textbox) li.getChildren().get(1).getChildren().get(0)).getText());
            arq3.setValor((((Doublebox) li.getChildren().get(2).getChildren().get(0)).getValue()).floatValue());
            arq3.setDatadeposito(((Datebox) li.getChildren().get(3).getChildren().get(0)).getValue());
            arq3.setDataInscricao(datai);
            arq3.setEstudante(est);
            arq3.setValorcobrado(Float.parseFloat(lbtotal.getValue()));
            par.clear();
            par.put("nrt", arq3.getNrtalao());
            if (csimpm.findByJPQuery("from Arquivoinscricao a where a.nrtalao like :nrt", par).size() > 0) {
                Clients.showNotification("O nr de recibo ja Existe", "warning", null, null, 0);
                ru = true;
                //return;
            }
            arq3.setRecibousado(ru);
            larq.add(arq3);
            lmed.add(m2);
        }
        Iterator<Listitem> items2 = new ArrayList(lbimgII.getItems()).listIterator();
        m2 = null;
        while (items2.hasNext()) {
            Listitem li = items2.next();
            m2 = ((Arquivo) li.getValue()).getMedia();
            Arquivoinscricao arq3 = new Arquivoinscricao();
            arq3.setNome(m2.getName());
            arq3.setRecibousado(false);
            arq3.setNrtalao("0");
            larq.add(arq3);
            lmed.add(m2);
        }
        csimpm.Save(i);
        String dat = new SimpleDateFormat("dd_MM_yyyy_HH_mm_ss").format(i.getDataInscricao());
        int ii = 0;
        for (Arquivoinscricao arq : larq) {
            arq.setIdInscricao(i);
            arq.setIdarquivoi(i.getIdInscricao());
            csimpm.Save(arq);
            String nome = i.getIdEstudante().getNrEstudante() + arq.getNome() + dat;
            File Test = new File(UserAutentic.getPathR("4") + nome);
            try {
                org.zkoss.io.Files.copy(Test, lmed.get(ii).getStreamData());
            } catch (IOException ex) {
                Logger.getLogger(GuardarEstudanteController.class.getName()).log(Level.SEVERE, null, ex);
            }
            ii++;
        }
        List<Inscricaodisciplina> ld = new ArrayList<Inscricaodisciplina>();
        for (Listitem ide : discSel.getItems()) {
            if (!ide.isDisabled()) {
                id = new Inscricaodisciplina();
                Disciplina d = (Disciplina) ide.getValue();
                id.setDisciplina(d);
                id.setInscricao(i);
                //  if (i.getModoInscricao().intValue() == 0) {
                par.clear();
                par.put("d", d);
                par.put("e", e);
                Inscricaodisciplina oldID = csimpm.findEntByJPQuery("from Inscricaodisciplina id where id.disciplina =:d and id.inscricao.idEstudante"
                        + " = :e and id.disciplinaActiva = 3", par);
                if (oldID != null) {
                    //if (oldID.getPrescricao() == null) {
                    oldID.setDisciplinaActiva(new Short("2"));
                    csimpm.update(oldID);
//                    } else {
//                        oldID.setDisciplinaActiva(new Short("1"));
//                        csimpm.update(oldID);
//                        oldID = csimpm.findEntByJPQuery("from Inscricaodisciplina id where id.disciplina =:d and id.inscricao.idEstudante"
//                                + " = :e and id.disciplinaActiva = 2", par);
//                        oldID.setDisciplinaActiva(new Short("1"));
//                        csimpm.update(oldID);
//                    }
                }
                // }
                id.setDisciplinaActiva(new Short("3"));
                id.setEstado(true);
                id.setTurma(turma);
                id.setTurno(turno);
                //id.setInscricao(i);
                id.setInscricaodisciplinaPK(new InscricaodisciplinaPK(i.getIdInscricao(), d.getIdDisc()));
                // lid.add(id);
                csimpm.Save(id);
                ld.add(id);
            }
        }
        limpForm();
        tabPagamentoTaxa.setSelected(false);
        tabDadosInscricao.setSelected(true);
        ////////////////////////////////////////////Envio do evento///////////////////////////////////////////////////////////////

        if (!usr.getUestudante()) {
            if (((Window) winAddInscricao.getParent()).inModal()) {
                i = csimpm.load(Inscricao.class, i.getIdInscricao());
                Listbox lb = ((Listbox) ((Window) winAddInscricao.getParent()).getFellow("lbinscricao"));
                if (lb.getListModel() == null) {
                    lb.setModel(new ListModelList<>());
                }
                lb.setMultiple(false);
                ((ListModelList) lb.getModel()).add(0, i);
                int iii = 1;
                Inscricaodisciplina idisc;
                final Iterator<Inscricaodisciplina> itemsi2 = ld.listIterator();
                while (itemsi2.hasNext()) {
                    idisc = itemsi2.next();
                    ((ListModelList) lb.getModel()).add(iii, idisc);
                    iii++;
                }
                lb.setMultiple(true);
            }
            Clients.showNotification("Inscrição efectuada com sucesso", null, null, null, 2000);
            calcularNiveFreq(e);//calcular nivel de frequencia, apartir do nivel em que estou
        } else {
            if (((Window) winAddInscricao.getParent()).inModal()) {
                Clients.showNotification("O seu pedido de Inscrição foi efectuada com sucesso, Aguarde a confirmação via e-mail", null, null, null, 2000);
            } else {
                Clients.showNotification("O seu pedido de Inscrição foi efectuada com sucesso", null, null, null, 2000);
            }

            i = csimpm.load(Inscricao.class, i.getIdInscricao());
            Listbox lb = ((Listbox) ((Window) winAddInscricao.getParent()).getFellow("lbinscricao"));
            if (lb.getListModel() == null) {
                lb.setModel(new ListModelList<>());
            }
            lb.setMultiple(false);
            ((ListModelList) lb.getModel()).add(0, i);
            int iii = 1;
            Inscricaodisciplina idisc;
            final Iterator<Inscricaodisciplina> itemsi2 = ld.listIterator();
            while (itemsi2.hasNext()) {
                idisc = itemsi2.next();
                ((ListModelList) lb.getModel()).add(iii, idisc);
                iii++;
            }
            lb.setMultiple(true);
            winAddInscricao.detach();
            //return;
        }
//        limpForm();
//        tabPagamentoTaxa.setSelected(false);
//        tabDadosInscricao.setSelected(true);
//        if (((Window) winAddInscricao.getParent()).inModal()) {
////            Estudante est = i.getIdEstudante();
////            est = csimpm.load(Estudante.class, est.getIdEstudante());
////            ((Listbox) ((Window) winAddInscricao.getParent()).getFellow("lbinscricao")).setModel(new ListModelList<Inscricao>(est.getInscricaoList()));
//            winAddInscricao.detach();
//        }
//        if (usr.getUestudante() && usr.getUtilizador() != null) {
//            Listbox lb = ((Listbox) ((Window) winAddInscricao.getParent()).getFellow("lbinscricao"));
//            if (lb.getListModel() == null) {
//                lb.setModel(new ListModelList<>());
//            }
//            lb.setMultiple(false);
//            ((ListModelList) lb.getModel()).add(0, i);
//            int iii = 1;
//            Messagebox.show(i.getInscricaodisciplinaList().get(0).getNotaFinal()+"");
////            Inscricaodisciplina idisc;
////            final Iterator<Inscricaodisciplina> itemsi2 = i.getInscricaodisciplinaList().listIterator();
////            while (itemsi2.hasNext()) {
////                idisc = itemsi2.next();
////                ((ListModelList) lb.getModel()).add(iii, idisc);
////                iii++;
////            }
//            lb.setMultiple(true);
//            winAddInscricao.detach();
//        }
        if (i.getModoInscricao().intValue() != 0) {
            String tipopedido = "";
            if (i.getModoInscricao().intValue() == 1) {
                tipopedido = "g4";
            } else {
                tipopedido = "g7";
            }
            ValidacaopendentePK vpk = new ValidacaopendentePK(fac, tipopedido);
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
                vp.setFaculdade(f);
                vp.setQtd(1);
                csimpm.Save(vp);
            }
            eq = EventQueues.lookup("valid" + fac, EventQueues.APPLICATION, true);
            eq.publish(new Event("onPedidoMatD", null, vp));
        }
        eq = EventQueues.lookup("insc" + fac, EventQueues.APPLICATION, true);
        eq.publish(new Event("onPedidoMat", null, i));
        eq = EventQueues.lookup("insc" + e.getIdEstudante(), EventQueues.APPLICATION, true);
        eq.publish(new Event("onPedidoMat", null, i));
        if (i.getModoInscricao().intValue() != 0) {
            eq = EventQueues.lookup("pinscD" + fac, EventQueues.APPLICATION, true);
            eq.publish(new Event("onPedidoMatD", null, i));
        }

        if ((!usr.getUestudante()) && ((Window) winAddInscricao.getParent()).inModal()) {
//            Estudante est = i.getIdEstudante();
//            est = csimpm.load(Estudante.class, est.getIdEstudante());
//            ((Listbox) ((Window) winAddInscricao.getParent()).getFellow("lbinscricao")).setModel(new ListModelList<Inscricao>(est.getInscricaoList()));
            winAddInscricao.detach();
        }
    }

    public void calcularNiveFreq(Estudante e) {
        par.clear();
        // int anoi = (e.getAnoIngresso() + e.getNivelFrequencia()) - 1;
        int nactual = e.getNivelFrequencia();
        par.put("nivel", e.getNivelFrequencia());
        par.put("curso", e.getCursocurrente());
        par.put("niv", "%" + e.getAnoIngresso() + "%");
        par.put("planoc", e.getPlanoc());
        //     int nn = nc + 1;
        int c1 = 0;
        int c2 = 0;

        int co1 = csimpm.findByJPQuery("from Disciplina d where d.curso=:curso and d.planoc = :planoc and d.semestre = 1 and"
                + " d.nivel =:nivel and ((d.natraso is not null and d.natraso not like :niv) or d.natraso is null)", par).size();

        int co2 = csimpm.findByJPQuery("from Disciplina d where d.curso=:curso and d.planoc = :planoc and d.semestre = 2 and"
                + " d.nivel =:nivel and ((d.natraso is not null and d.natraso not like :niv) or d.natraso is null)", par).size();
        par.clear();
        par.put("e", e);
        par.put("anoi", e.getNivelFrequencia());
        par.put("planoc", e.getPlanoc());
        par.put("ano", ano);
        // par.put("niv", "%"+e.getAnoIngresso() + "%");

        c1 = csimpm.findByJPQuery("from Inscricaodisciplina id where id.inscricao.idEstudante = :e and "
                + "id.disciplina.nivel = :anoi and id.disciplina.planoc = :planoc and id.disciplina.semestre = 1 and (id.disciplinaActiva = 3"
                + " and id.estado is true and ((id.notaFinal is not null and id.notaFinal >= 10) or (id.notaFinal is null and extract(year from id.inscricao.dataInscricao) < :ano)))", par).size();
        c2 = csimpm.findByJPQuery("from Inscricaodisciplina id where id.inscricao.idEstudante = :e and "
                + "id.disciplina.nivel = :anoi and id.disciplina.planoc = :planoc and id.disciplina.semestre = 2 and (id.disciplinaActiva = 3"
                + " and id.estado is true and ((id.notaFinal is not null and id.notaFinal >= 10) or (id.notaFinal is null and extract(year from id.inscricao.dataInscricao) < :ano)))", par).size();
        //  if (sem == 1) {
        //   if (((co1 - c1) <= 2) && ((co2 - c2) <= 2)) {// so no primeiro semestre
        //       Messagebox.show(co1+" - "+c1+"+) + ("+co2+" - "+c2);
        int t1 = 0, t2 = 0;
        if (co1 >= c1) {
            t1 = co1 - c1;
        }
        if (co2 >= c2) {
            t2 = co2 - c2;
        }
        if ((t1 + t2 <= 2)) {// (co1 == 0 || co1 == 0 && co2 == 0) && na primeira inscricao do ano// so no primeiro semestre
            nactual = nactual + 1;
            int nc = nactual;//e.getNivelFrequencia();
            int nc2 = nc;
            c1 = 0;
            c2 = 0;
            par.clear();
            par.put("curso", e.getCursocurrente());
            par.put("nivel", nc);
            par.put("planoc", e.getPlanoc());
            par.put("niv", "%" + e.getAnoIngresso() + "%");
//            co1 = csimpm.findByJPQuery("from Disciplina d where d.curso=:curso and d.planoc = :planoc and d.semestre = 1 and"
//                    + " d.nivel =:nivel and ((d.natraso is not null and d.natraso not like :niv) or d.natraso is null)", par).size();
//
//            co2 = csimpm.findByJPQuery("from Disciplina d where d.curso=:curso and d.planoc = :planoc and d.semestre = 2 and"
//                    + " d.nivel =:nivel and ((d.natraso is not null and d.natraso not like :niv) or d.natraso is null)", par).size();
            par.clear();
            par.put("e", e);
            par.put("planoc", e.getPlanoc());
            par.put("anoi", nc);
            par.put("ano", ano);
            //par.put("niv", "%"+e.getAnoIngresso() + "%");
            c1 = csimpm.findByJPQuery("from Inscricaodisciplina id where id.inscricao.idEstudante = :e and "
                    + "id.disciplina.nivel = :anoi and id.disciplina.planoc = :planoc and id.disciplina.semestre = 1 and (id.disciplinaActiva = 3"
                    + " and id.estado is true and extract(year from id.inscricao.dataInscricao) = :ano)", par).size();//and ((id.notaFinal is not null and id.notaFinal >= 10)) or id.notaFinal is null
            c2 = csimpm.findByJPQuery("from Inscricaodisciplina id where id.inscricao.idEstudante = :e and "
                    + "id.disciplina.nivel = :anoi and id.disciplina.planoc = :planoc and id.disciplina.semestre = 2 and (id.disciplinaActiva = 3"
                    + " and id.estado is true and extract(year from id.inscricao.dataInscricao) = :ano)", par).size();//  and ((id.notaFinal is not null and id.notaFinal >= 10))or id.notaFinal is null
            //  Messagebox.show("1");
            if (c1 != 0 || c2 != 0) {
                //  nc2 = nc2 + 1;
                e.setNivelFrequencia(nc2);
                //   Messagebox.show("2");
                csimpm.update(e);
            }
//            if (nc2 > nc) {
//                
//            }
        }
        ///
//        int nc = e.getNivelFrequencia();
//        int nc2 = nc;
//        int c1 = 0;
//        int c2 = 0;
//        par.clear();
//        par.put("curso", e.getCursocurrente());
//        par.put("nivel", e.getNivelFrequencia());
//        par.put("planoc", e.getPlanoc());
//        par.put("niv", "%" + e.getAnoIngresso() + "%");
//        int co1 = csimpm.findByJPQuery("from Disciplina d where d.curso=:curso and d.planoc = :planoc and d.semestre = 1 and"
//                + " d.nivel =:nivel and ((d.natraso is not null and d.natraso not like :niv) or d.natraso is null)", par).size();
//
//        int co2 = csimpm.findByJPQuery("from Disciplina d where d.curso=:curso and d.planoc = :planoc and d.semestre = 2 and"
//                + " d.nivel =:nivel and ((d.natraso is not null and d.natraso not like :niv) or d.natraso is null)", par).size();
//        par.clear();
//        par.put("e", e);
//        par.put("planoc", e.getPlanoc());
//        par.put("anoi", e.getNivelFrequencia());
//        //par.put("niv", "%"+e.getAnoIngresso() + "%");
//        c1 = csimpm.findByJPQuery("from Inscricaodisciplina id where id.inscricao.idEstudante = :e and "
//                + "id.disciplina.nivel = :anoi and id.disciplina.planoc = :planoc and id.disciplina.semestre = 1 and (id.disciplinaActiva = 3"
//                + " and id.estado is true and ((id.notaFinal is not null and id.notaFinal >= 10)))", par).size();// or id.notaFinal is null
//        c2 = csimpm.findByJPQuery("from Inscricaodisciplina id where id.inscricao.idEstudante = :e and "
//                + "id.disciplina.nivel = :anoi and id.disciplina.planoc = :planoc and id.disciplina.semestre = 2 and (id.disciplinaActiva = 3"
//                + " and id.estado is true and ((id.notaFinal is not null and id.notaFinal >= 10)))", par).size();// or id.notaFinal is null
//        if (c2 != 0) {
//            if ((co1 - c1) + (co2 - c2) <= 2) {// so no primeiro semestre
//                nc2 = nc2 + 1;
//            }
//        }
//        if (nc2 > nc) {
//            e.setNivelFrequencia(nc2);
//            csimpm.update(e);
//        }
    }

    public void onBtnAvancar1() {
        if ((combEstInsc.getSelectedItem() == null && combEstInsc.isButtonVisible()) || discSel.getItemCount() == 0) {
            Clients.showNotification("Por favor, seleccione um estudante e as respectivas disciplinas", "error", null, null, 0);
            return;
        }
        boolean b = false;
        for (Listitem ide : discSel.getItems()) {
            if (!ide.isDisabled()) {
                b = true;
                break;
            }
        }
        if (!b) {
            Clients.showNotification("Por favor, seleccione um estudante e as respectivas disciplinas", "error", null, null, 0);
            return;
        }
        tabDadosInscricao.setSelected(false);
        tabPagamentoTaxa.setSelected(true);
        preenchTabPagementoTaxa();
    }

    public void onSwitchTab$tabPagamentoTaxa(ForwardEvent evt) {
        if ((combEstInsc.getSelectedItem() == null && combEstInsc.isButtonVisible()) || discSel.getItemCount() == 0) {
            Clients.showNotification("Por favor, seleccione um estudante e as respectivas disciplinas", "error", null, null, 0);
//            tabPagamentoTaxa.setSelected(false);
//            tabDadosInscricao.setSelected(true);
            return;
        }
        boolean b = false;
        for (Listitem ide : discSel.getItems()) {
            if (!ide.isDisabled()) {
                b = true;
                break;
            }
        }
        if (!b) {
            Clients.showNotification("Por favor, seleccione um estudante e as respectivas disciplinas", "error", null, null, 0);
            return;
        }
        final Tab tab = (Tab) evt.getOrigin().getTarget();
        tab.setSelected(true);
        preenchTabPagementoTaxa();
    }

    public void onSwitchTab$tabDadosInscricao(ForwardEvent evt) {
        final Tab tab = (Tab) evt.getOrigin().getTarget();
        tab.setSelected(true);
    }

    public void preenchTabPagementoTaxa() {
        Date dano = new Date();
        Calendar cal = new GregorianCalendar();
        cal.setTime(dano);
        Estudante e = null;
        if (combEstInsc.isButtonVisible()) {
            e = (Estudante) combEstInsc.getSelectedItem().getValue();
            e = csimpm.load(Estudante.class, e.getIdEstudante());
        } else {
            e = csimpm.get(Estudante.class, Long.parseLong(txx.getText()));
        }
        Map<String, Object> par = new HashMap<String, Object>();
        Map<String, Object> ent = new HashMap<String, Object>();
        int pais = e.getNacionalidade().getIdPais();
        par.clear();
        par.put("ano", ano);
        ent.put("p", PlanificacaoAnoLectivo.class);
        Float total = null;
        PlanificacaoAnoLectivo pal = csimpm.findEntByJPQuery("from PlanificacaoAnoLectivo", null);

        //mostrar a lista de prescricoes
        if (lbsem.getValue().equals("Semestre 1")) {
            par.clear();
            par.put("ano", ano - 1);
            par.put("ide", e);
            List<Prescricao> p1 = csimpm.findByJPQuery("from Prescricao p where p.inscricaodisciplina.inscricao.idEstudante = :ide"
                    + " and extract(year from p.inscricaodisciplina.inscricao.dataInscricao)=:ano", par);
            String lab = "";
            for (Prescricao pe : p1) {
                lab = lab + (ano - 1) + "- " + pe.getInscricaodisciplina().getInscricao().getSemestre() + " Sem- " + pe.getInscricaodisciplina().getDisciplina().getNome() + ";\n";
            }
            ((Label) winAddInscricao.getFellow("lbprescricao")).setValue(lab);
        } else {
            String lab = "";
            par.clear();
            par.put("ide", e);
            par.put("ano", ano);
            List<Prescricao> p1 = csimpm.findByJPQuery("from Prescricao p where p.inscricaodisciplina.inscricao.idEstudante = :ide"
                    + " and extract(year from p.inscricaodisciplina.inscricao.dataInscricao)=:ano", par);
            for (Prescricao pe : p1) {
                lab = lab + ano + "- " + pe.getInscricaodisciplina().getInscricao().getSemestre() + " Sem- " + pe.getInscricaodisciplina().getDisciplina().getNome() + ";\n";
            }
            par.clear();
            par.put("ano", ano - 1);
            par.put("ide", e);
            List<Prescricao> p2 = csimpm.findByJPQuery("from Prescricao p where p.inscricaodisciplina.inscricao.idEstudante = :ide"
                    + " and extract(year from p.inscricaodisciplina.inscricao.dataInscricao)=:ano"
                    + " and p.inscricaodisciplina.inscricao.semestre=2", par);
            for (Prescricao pe : p2) {
                lab = lab + (ano - 1) + "- " + pe.getInscricaodisciplina().getInscricao().getSemestre() + " Sem- " + pe.getInscricaodisciplina().getDisciplina().getNome() + ";\n";
            }
            ((Label) winAddInscricao.getFellow("lbprescricao")).setValue(lab);
        }
        if (pais != 122) {
            rwTaxaeM.setVisible(true);
            lbtaxaInscricaoE.setValue(pal.getTaxaInscricaoEstrangeiro() + "");
            total = pal.getTaxaInscricaoEstrangeiro().floatValue();// * discSel.getItemCount()-discDesabilitadas;
        } else {
            rwTaxanM.setVisible(true);
            lbtaxaInscricao.setValue(pal.getTaxaInscricaoNacional() + "");
            total = pal.getTaxaInscricaoNacional().floatValue();// * discSel.getItemCount()-discDesabilitadas;
        }
        Date dtaxa1, dtaxa2, dtaxa3;
        if (lbsem.getValue().equals("Semestre 1")) {
            if (ano != e.getAnoIngresso()) {//if (e.getMatriculaList().size() > 1) {
                dtaxa1 = pal.getDataFinalMatricula();
                dtaxa2 = pal.getDataFimMatriculaE2();
                dtaxa3 = pal.getDm1();
            } else {
                dtaxa1 = pal.getDataFimIE1();
                dtaxa2 = pal.getDataFimIE2();
                dtaxa3 = pal.getDma1();
            }
            if (ano != e.getAnoIngresso()) {
                if (dtaxa2 != null && dano.after(dtaxa1) && dano.before(dtaxa2)) {
                    rwTaxamulta15.setVisible(true);
                    lbtaxaMulta15dias.setValue(pal.getPercentagemMultaInscricao15dias() + "");
                    ibmulta.setValue(pal.getPercentagemMultaInscricao15dias().intValue());
                    if (pal.getPercentagemMultaInscricao15dias() > 0) {
                        total = total + (total * (pal.getPercentagemMultaInscricao15dias() / 100));
                    }
                } else if (dtaxa3 != null && dano.after(dtaxa2) && dano.before(dtaxa3)) {
                    rwTaxamulta30.setVisible(true);
                    lbtaxaMulta30dias.setValue(pal.getPercentagemMultaInscricao30dias() + "");
                    ibmulta.setValue(pal.getPercentagemMultaInscricao30dias().intValue());
                    if (pal.getPercentagemMultaInscricao30dias() > 0) {
                        total = total + (total * (pal.getPercentagemMultaInscricao30dias() / 100));
                    }
                }
            }
        } else {
            dtaxa1 = pal.getDataFimE1();
            dtaxa2 = pal.getDataFimE2();
            dtaxa3 = pal.getDi1();
            if (dano.after(dtaxa1) && dano.before(dtaxa2)) {
                rwTaxamulta15.setVisible(true);
                lbtaxaMulta15dias.setValue(pal.getPercentagemMultaInscricao15dias() + "");
                ibmulta.setValue(pal.getPercentagemMultaInscricao15dias().intValue());
                if (pal.getPercentagemMultaInscricao15dias() > 0) {
                    total = total + (total * (pal.getPercentagemMultaInscricao15dias() / 100));
                }
            } else if (dano.after(dtaxa2) && dano.before(dtaxa3)) {
                rwTaxamulta30.setVisible(true);
                lbtaxaMulta30dias.setValue(pal.getPercentagemMultaInscricao30dias() + "");
                ibmulta.setValue(pal.getPercentagemMultaInscricao30dias().intValue());
                if (pal.getPercentagemMultaInscricao30dias() > 0) {
                    total = total + (total * (pal.getPercentagemMultaInscricao30dias() / 100));
                }
            }
        }
        if (prazo.isVisible()) {
            rwTaxamulta15.setVisible(false);
            rwTaxamulta30.setVisible(false);
            ibmulta.setValue(0);
            totali = total;
            // totali = Float.parseFloat(lbtotal.getValue());
        } else {
            rwmulta.setVisible(false);
            prazoinsc.setVisible(false);
        }
        lbtotal.setValue(total + "");
    }

    public void onChanging$ibmulta(InputEvent evt) {
        if (!evt.getValue().equals("")) {
            if (evt.getValue().equals("0")) {
                Float t = totali;
                lbtotal.setValue(t + "");
            } else {
                Float t = totali + (totali * (Integer.parseInt(evt.getValue()) / 100));
                lbtotal.setValue(t + "");
                // Messagebox.show(t+"");}
            }
        }
    }

    public static void setMediaTaxa(Media media) {
        mediaCPI = media;
    }

    public void onVerDisc(ForwardEvent evt) throws Exception {
        winAddInscricaoDisc.setParent(winInscricao);
        winAddInscricaoDisc.doModal();
        Button btn = (Button) evt.getOrigin().getTarget();
        Listitem litem = (Listitem) btn.getParent().getParent();
        Inscricao i = (Inscricao) litem.getValue();
        Map<String, Object> par = new HashMap<String, Object>();
        Map<String, Object> ent = new HashMap<String, Object>();
        ent.put("id", Inscricaodisciplina.class);
        par.put("insc", i.getIdInscricao());
        ((Listbox) winAddInscricaoDisc.getFellow("lbdiscInsc")).setMultiple(false);
        List<Inscricaodisciplina> ldisc = csimpm.findByQuery("select id.* from fecn1.inscricaodisciplina id "
                + "where id.id_inscricao = :insc", ent, par);
        ((Listbox) winAddInscricaoDisc.getFellow("lbdiscInsc")).setModel(new ListModelList<Inscricaodisciplina>(ldisc));
        ((Listbox) winAddInscricaoDisc.getFellow("lbdiscInsc")).setMultiple(true);
    }

    public void onClick$btnCancDisc() { //nao existe
        if (!usr.getUestudante()) {
            lbinscricao.setModel(getListInscricaoModel());
        } else {
            if (!winInscricao.inModal()) {
                getListInscricaoModel2();
            }
        }
        winAddInscricaoDisc.detach();
    }

//    public void onSelect$lbinscricao() {
//        int s = lbinscricao.getItemCount();
//        lbinscricao.clearSelection();
////        if (lbinscricao.getSelectedCount() == s) {
////            if (!winInscricao.inModal() && usr.getUestudante()) {
////                getListInscricaoModel2();
////            } else {
////                // lbinscricao.setModel(getListInscricaoModel());
////                setLB(0, 20);
////            }
////        }
//    }
    public void onAnular(final ForwardEvent evt) {
//        Button btn = (Button) evt.getOrigin().getTarget();
//        Listitem litem = (Listitem) btn.getParent().getParent();
        if (lbinscricao.getSelectedCount() == 0) {
            Clients.showNotification("Selecione Disciplinas de um único estudante", "error", null, null, 3000);
            return;
        }
        limpaLB(lbin);
        final Iterator<Listitem> itemsi2 = new ArrayList(lbinscricao.getItems()).listIterator();
        while (itemsi2.hasNext()) {
            Listitem li = itemsi2.next();
            if (li.isSelected()) {
                lbin.appendChild(li);
            }
        }
        Inscricaodisciplina id = null;
        final Iterator<Listitem> itemsi = new ArrayList(lbin.getItems()).listIterator();
        Inscricaodisciplina idis = null;
        while (itemsi.hasNext()) {
            Listitem li = itemsi.next();
            Inscricaodisciplina idiscip = (Inscricaodisciplina) li.getValue();
            if (idiscip.getAnulacao() != null || (usr.getUestudante() && (!idiscip.getInscricao().getEstado()))) {
                Clients.showNotification("Selecionou uma Inscricao que esta anulada ou esta aguardando confirmação...\n Tente de novo!", "error", null, null, 0);
                if (usr.getUestudante()) {
                    getListInscricaoModel2();
                } else {
                    // lbinscricao.setModel(getListInscricaoModel());
                    setLB(0, 20);
                }
                return;
            }
            Date d = ((Inscricaodisciplina) li.getValue()).getInscricao().getDataInscricao();
            int s = ((Inscricaodisciplina) li.getValue()).getInscricao().getSemestre().intValue();
            PlanificacaoAnoLectivo pa = csimpm.findEntByJPQuery("from PlanificacaoAnoLectivo", null);
            if (usr.getUestudante() && (s == 1 && d.before(pa.getDataInicioMatricula()) || (s == 2 && d.before(pa.getDatainicioInscricao2())))) {
                Clients.showNotification("Não se pode anular Inscrições dos Semestres anteriores...\n Tente de novo!", "error", null, null, 0);
                if (usr.getUestudante()) {
                    getListInscricaoModel2();
                } else {
                    //lbinscricao.setModel(getListInscricaoModel());
                    setLB(0, 20);
                }
                return;
            }
            if (id == null) {
                id = (Inscricaodisciplina) li.getValue();
            }
            if (id.getInscricao().getIdEstudante() != ((Inscricaodisciplina) li.getValue()).getInscricao().getIdEstudante()) {
                Clients.showNotification("So poderá anular Inscrições de um único estudante", "error", null, null, 0);
                if (usr.getUestudante()) {
                    getListInscricaoModel2();
                } else {
                    // lbinscricao.setModel(getListInscricaoModel());
                    setLB(0, 20);
                }
                return;
            }
        }
        id = csimpm.load(Inscricaodisciplina.class, id.getInscricaodisciplinaPK());
        Window win = (Window) Executions.createComponents("inscricao/AnularInscricao.zul", winInscricao, null);
        //((Intbox) win.getFellow("item")).setValue(litem.getIndex());
        ((Label) win.getFellow("lbNumero")).setValue(id.getInscricao().getIdEstudante().getNrEstudante());
        ((Label) win.getFellow("lbNome")).setValue(id.getInscricao().getIdEstudante().getNomeCompleto());
        ((Intbox) win.getFellow("idest")).setValue(id.getInscricao().getIdEstudante().getIdEstudante().intValue());
    }

    public void onRegistarAnulacao() throws IOException {
        final Disciplinaanulada da = new Disciplinaanulada();
        da.setDataanulacao(new Date());
        da.setMotivo(txMotivo.getText());
        Messagebox.show("Pretende anular estas disciplinas?", "Atenção", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION,
                new EventListener() {
                    @Override
                    public void onEvent(Event evet) throws IOException {
                        switch (((Integer) evet.getData()).intValue()) {
                            case Messagebox.YES:
                                Listbox lb = (Listbox) ((Window) winAnularI.getParent()).getFellow("lbin");
                                Inscricaodisciplina id = null;
                                Users u = null;
                                Date dano = new Date();
                                Calendar c = new GregorianCalendar();
                                c.setTime(dano);
                                int ano = c.get(Calendar.YEAR);
                                //Antes de anular uma inscricao, primeiro detectar o semestre para fazer a contagem do tempo;
                                PlanificacaoAnoLectivo pa = csimpm.findEntByJPQuery("from PlanificacaoAnoLectivo", null);
                                Date dfi = null;
//                                if (dano.compareTo(pa.getDatainicioInscricao2()) < 0) {
//                                    dfi = pa.getDataFimE1();
//                                } else {
//                                    dfi = pa.getDataFimE2();
//                                }
//                                
//                                for (Listitem li : lb.getItems()) {
//                                    id = (Inscricaodisciplina) li.getValue();
                                dfi = pa.getSem1i();
                                GregorianCalendar cal = new GregorianCalendar();
                                cal.setTime(dfi);
                                cal.add(Calendar.DAY_OF_YEAR, 24);
                                Date dinsc = cal.getTime();

//                                }
                                final Iterator<Listitem> items1 = new ArrayList(lbimg1.getItems()).listIterator();
                                Media m2 = null;
                                while (items1.hasNext()) {
                                    m2 = (Media) items1.next().getValue();
                                    da.setDoc(m2.getName());
                                }
                                da.setIdEstudante(csimpm.get(Estudante.class, idest.getValue().longValue()));
                                da.setDataanulacao(dano);
                                if (usr.getUestudante()) {
                                    if (dano.after(dinsc)) {
                                        Clients.showNotification("So podera anular uma disciplina dentro de 4 semanas após o inicio de aulas", "warning", null, null, 0);
                                        return;
                                    }
                                    if (!lbc.isDisabled()) {
                                        Clients.showNotification("Por favor anexar o seu Requerimento", "error", null, null, 0);
                                        return;
                                    }
                                }
                                if (!usr.getUestudante()) {
                                    da.setEstado(true);
                                    da.setDataconfirmacao(dano);
                                    u = csimpm.get(Users.class, usr.getUtilizador());
                                    da.setFuncionario(u.getIdFuncionario());
                                }
                                csimpm.Save(da);
                                if (m2 != null) {
                                    String dat = new SimpleDateFormat("dd_MM_yyyy_HH_mm_ss").format(da.getDataanulacao());
                                    String nome = lbNumero.getValue() + dat;
                                    File Test = new File(UserAutentic.getPathR("6") + nome);
                                    try {
                                        org.zkoss.io.Files.copy(Test, m2.getStreamData());
                                    } catch (IOException ex) {
                                        Logger.getLogger(GuardarEstudanteController.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                }
                                final Iterator<Listitem> items2 = new ArrayList(lb.getItems()).listIterator();
                                Inscricaodisciplina idis = null;
                                while (items2.hasNext()) {
                                    Listitem li = items2.next();
                                    idis = (Inscricaodisciplina) li.getValue();
                                    if (!usr.getUestudante()) {
                                        idis.setEstado(false);
                                    }
                                    idis.setAnulacao(da);
                                    csimpm.update(idis);
                                }
                                lb.clearSelection();
                                Faculdade f = csimpm.get(Faculdade.class, usr.getFaculdade().getIdFaculdade());
                                if (usr.getUestudante()) {
                                    Clients.showNotification("Pedido de Anulação enviado com sucesso", null, null, null, 2000);
                                    // getListInscricaoModel2();
                                    eq = EventQueues.lookup("pinscA" + f.getIdFaculdade(), EventQueues.APPLICATION, true);
                                    eq.publish(new Event("onPedidoMatD", null, da));
//                                    eq = EventQueues.lookup("pinscA" + da.getIdEstudante().getIdEstudante(), EventQueues.APPLICATION, true);
//                                    eq.publish(new Event("onPedidoMatD", null, da));
                                } else {
                                    Clients.showNotification("Inscrição Anulada com sucesso", null, null, null, 2000);
                                }
                                eq = EventQueues.lookup("anula" + f.getIdFaculdade(), EventQueues.APPLICATION, true);
                                eq.publish(new Event("onPedidoMat", null, da));
                                eq = EventQueues.lookup("anula" + da.getIdEstudante().getIdEstudante(), EventQueues.APPLICATION, true);
                                eq.publish(new Event("onPedidoMat", null, da));
                                winAnularI.detach();
                                break;
                            case Messagebox.NO:
                                return;
                        }
                    }
                });
    }

    public void onCancAnul() {
        winAnularI.detach();
    }

    public void onSelect$lbdiscInsc() {
        if (lbdiscInsc.getSelectedCount() > 0) {
            btnAnular.setDisabled(false);
        } else {
            btnAnular.setDisabled(true);
        }
    }

    public void onActivar(ForwardEvent evt) {
        Button btn = (Button) evt.getOrigin().getTarget();
        Listitem litem = (Listitem) btn.getParent().getParent();
        Inscricaodisciplina id = (Inscricaodisciplina) litem.getValue();
        id.setEstado(true);
        ((Button) btn.getParent().getChildren().get(0)).setVisible(true);
        btn.setVisible(false);
        csimpm.update(id);
    }

    public ListModel<Disciplina> getDiscRelModel() {
        return new ListModelList<Disciplina>(csimpm.getAll(Disciplina.class));
    }

    public ListModel<Curso> getCursoRelModel() {
        return new ListModelList<Curso>(csimpm.getAll(Curso.class));
    }

//    public void onChange$dinsc() {
//        lbinscricao.setModel(new ListModelList<Estudante>(getLiEI()));
//    }
//
//    public void onChange$cbDiscRel() {
//        lbinscricao.setModel(new ListModelList<Estudante>(getLiEI()));
//    }
//
//    public void onChange$cbCursoRel() {
//        cbDiscRel.setModel(new ListModelList<Disciplina>(((Curso) cbCursoRel.getSelectedItem().getValue()).getDisciplinaList()));
//        lbinscricao.setModel(new ListModelList<Estudante>(getLiEI()));
//    }
//
//    public ListModel<Estudante> getInscricaoEstudanteModel() {
//
//        return new ListModelList<Estudante>(getLiEI());
//    }
//
//    public List<Estudante> getLiEI() {
//        Map<String, Object> par = new HashMap<String, Object>();
//        Map<String, Object> ent = new HashMap<String, Object>();
//        ent.put("e", Estudante.class);
////        if (cbDiscRel.getSelectedItem() != null) {
////            par.put("disc", ((Disciplina) cbDiscRel.getSelectedItem().getValue()).getIdDisc());
////        } else {
//        par.put("disc", 20);
////        }
//        par.put("curso", 1);
////        Calendar cal = new GregorianCalendar();
////        cal.setTime(dinsc.getValue());
//        par.put("ano", 2014); //cal.get(Calendar.YEAR));
//        List<Estudante> le = csimpm.findByQuery("select e.* from fecn1.estudante e, fecn1.inscricao i, fecn1.inscricaodisciplina id, fecn1.disciplina d, fecn1.curso c"
//                + " where e.id_estudante = i.id_estudante and i.id_inscricao = id.id_inscricao and"
//                + " id.id_disciplina = d.id_disc and d.id_disc = :disc and d.curso = :curso and extract(year from i.data_inscricao) = :ano", ent, par);
//        return le;
//    }
    public void onModal$winAddInscricaoDisc() {
        btnAnular.setVisible(false);
    }

    private void limpar(Component component) {
        limparComp(component);

        if (component.isVisible()) {
            List<Component> children = component.getChildren();
            for (Component each : children) {
                limpar(each);
            }
        }
    }

    public void limparComp(Component component) {
        Constraint co = null;
        if (component instanceof InputElement) {
            Constraint c = ((InputElement) component).getConstraint();
            if (c != null) {
                SimpleConstraint sc = (SimpleConstraint) c;
                String s = sc.getClientConstraint();
                ((InputElement) component).setConstraint(co);
                ((InputElement) component).setText("");
                ((InputElement) component).setConstraint(s.substring(1, s.length() - 1));
            } else {
                ((InputElement) component).setText("");
            }
        }
    }

    public void onVerMotivo(ForwardEvent evt) {
        Inscricao i = (Inscricao) ((Listitem) evt.getOrigin().getTarget().getParent().getParent()).getValue();
        Window w = new Window();
        Textbox t = new Textbox("");
        if (i.getMotivoinsc() != null) {
            t.setText(i.getMotivoinsc().getMotivo());
        }
        t.setRows(10);
        t.setParent(w);
        w.setTitle("Motivo da Rejeição");
        w.setClosable(true);
        w.setHflex("min");
        w.setVflex("min");
        w.setWidth("60px");
        w.setParent(winInscricao);
        w.doModal();
    }

    public void onVerMotivoAnu(ForwardEvent evt) {
        Inscricaodisciplina i = (Inscricaodisciplina) ((Listitem) evt.getOrigin().getTarget().getParent().getParent()).getValue();
        i = csimpm.load(Inscricaodisciplina.class, i.getInscricaodisciplinaPK());
        Window w = new Window();
        w.setAttribute("apply", "esira.inscricao.InscricaoController");
        Vbox vb = new Vbox();
        vb.setParent(w);
        Textbox t = new Textbox("");
        if (i.getAnulacao() != null) {
            t.setText(i.getAnulacao().getMotivo());
        }
        t.setRows(10);
        t.setParent(vb);
        Intbox it = new Intbox();
        it.setVisible(false);
        it.setValue(i.getAnulacao().getIdanulacao().intValue());
        it.setParent(vb);
        Button b = new Button("Ver Requerimento");
        b.setId("mm");
        //b.addForward("onClick",  b, "onVerRequerimento");
        b.addEventListener("onClick", new EventListener() {
            public void onEvent(Event evt) throws IOException {
                onVerRequerimento(evt);
            }
        });
        b.setAttribute("apply", "esira.inscricao.InscricaoController");
        b.setParent(vb);
        w.setTitle("Motivo da Anulação");
        w.setId("winx");
        w.setClosable(true);
        w.setHflex("min");
        w.setVflex("min");
        //  Executions.createComponents(w.getPage().getRequestPath(), winInscricao, null);
        w.setParent(winInscricao);
        w.doModal();
    }

    public void onClick$mm() {
        Messagebox.show("dfd");
    }

    public void onVerRequerimento(Event evt) throws FileNotFoundException, IOException {
        Intbox it = (Intbox) evt.getTarget().getParent().getChildren().get(1);
        Disciplinaanulada da = csimpm.load(Disciplinaanulada.class, it.getValue());
        if (da.getDoc() == null) {
            Clients.showNotification("Não existe documento", "warning", null, null, 2000);
            return;
        }
        String nre = da.getInscricaodisciplinaList().get(0).getInscricao().getIdEstudante().getNrEstudante();
        String dat = new SimpleDateFormat("dd_MM_yyyy_HH_mm_ss").format(da.getDataanulacao());
        String sss = UserAutentic.getPathR("6") + nre + dat;
        //File f = new File(ss);
        InputStream iss = new FileInputStream(sss);
        Media pp = new AMedia(sss, FilenameUtils.getExtension(sss), URLConnection.guessContentTypeFromName(sss), iss);
        Media mm = new AMedia(sss, FilenameUtils.getExtension(sss), URLConnection.guessContentTypeFromName(sss), pp.getByteData());
        Window win = (Window) Executions.createComponents("/report.zul", null, null);
        win.setTitle("Requerimento/Anulação");
        Iframe iframe = (Iframe) win.getFellow("report");
//        org.zkoss.util.media.Media m = (org.zkoss.util.media.Media) ((Listitem) self.getParent().getParent()).getValue();
//        org.zkoss.util.media.Media amedia = new org.zkoss.util.media.AMedia(m.getName(), m.getFormat(), m.getContentType(), m.getStreamData());
        iframe.setContent(mm);
    }

    public void onVerInscricao(ForwardEvent evt) {
        Button buton = (Button) evt.getOrigin().getTarget();
        Listitem item = (Listitem) buton.getParent().getParent();
        Estudante e = (Estudante) item.getValue();
        int ind = item.getIndex();
        Listbox l = item.getListbox();
        par.clear();
        par.put("e", e);
        String co = "";
        if (ibProcAno.isVisible()) {
            par.put("ano", ibProcAno.getValue());
            co = " and extract(year from i.dataInscricao) = :ano";
        }
        if (buton.getImage().equals("/icon/down.png")) {
            Executions.createComponents("componente/buton.zul", null, null);
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
            }
            int r = lbinscricao.getRows();
            lbinscricao.setRows(r - k);
        } else {
            buton.setImage("/icon/down.png");
            List<Inscricao> li = csimpm.findByJPQuery("from Inscricao i where i.idEstudante = :e " + co + " order by i.dataInscricao desc", par);
            final Iterator<Inscricao> items = li.iterator();
            Inscricao insc;
            while (items.hasNext()) {
                insc = items.next();
                ((ListModelList) l.getListModel()).add(++ind, insc);
                // insc = csimpm.load(Inscricao.class, insc.getIdInscricao());
                int k = ((ListModelList) l.getListModel()).getSize() + 1;
//                for(int j=0;j<k;j++){
//                 //  l.setRenderdefer(j);
                l.setRows(insc.getInscricaodisciplinaList().size() + k);
//                }
                final Iterator<Inscricaodisciplina> iter = insc.getInscricaodisciplinaList().iterator();
                Inscricaodisciplina id;
                while (iter.hasNext()) {
                    id = iter.next();
                    // id = csimpm.load(Inscricaodisciplina.class, id.getInscricaodisciplinaPK());
                    l.setMultiple(false);
                    ((ListModelList) l.getListModel()).add(++ind, id);
                    l.setMultiple(true);
                }
            }
        }
        lbinscricao.clearSelection();
    }

    public void onPInsc(ForwardEvent evt) {
//        Window win = (Window) Executions.createComponents("/inscricao/inscricaoequivalencia.zul", winAddInscricao, null);
//        ((Textbox) win.getFellow("est")).setText(694 + "");
//        win.doModal();

        Executions.createComponents("inscricao/inscricaoestudante.zul", winInscricao, null);
    }

    public void onClose$winanularmot(Event evt) {
        Users user = csimpm.get(Users.class, usr.getUtilizador());
        par.clear();
        par.put("ide", user.getIdEstudante());
        List<Disciplinaanulada> lda = csimpm.findByJPQuery("from Disciplinaanulada da where da.estado is false and da.dataconfirmacao is not null and "
                + "da.idEstudante = :ide", par);
        final Iterator<Disciplinaanulada> items = lda.listIterator();
        Disciplinaanulada da = null;
        while (items.hasNext()) {
            da = (Disciplinaanulada) items.next();
            final Iterator<Inscricaodisciplina> items2 = da.getInscricaodisciplinaList().listIterator();
            Inscricaodisciplina idis = null;
            while (items2.hasNext()) {
                idis = (Inscricaodisciplina) items2.next();
                idis.setAnulacao(null);
                csimpm.update(idis);
            }
        }
    }

    ////////////////////////////////////PESQUISAS//////////////////////////////////////////////////////
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

    public void onClick$manoi(Event e) throws InterruptedException {
        ibProcAno.setVisible(manoi.isChecked());
        if (manoi.isChecked()) {
            Date dano = new Date();
            Calendar cal = new GregorianCalendar();
            cal.setTime(dano);
            ibProcAno.setValue(cal.get(Calendar.YEAR));
            condano = " and extract(year from i.dataInscricao) = :a ";
            condpar.put("a", ibProcAno.getValue());
        } else {
            condano = "";
            condpar.remove("a");
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
            condano = " and extract(year from i.dataInscricao) = :a ";
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
        if (i == 0) {
            lbinscricao.setModel(new ListModelList<Prescricao>());
        }
        List<Estudante> li = csimpm.findByJPQueryFilter("from Estudante e where 1=1" + condcurso + condnome + condnr + " and e.idEstudante in (select i.idEstudante from Inscricao"
                + " i where 1=1 " + condano + " and i.dataConfirmacao is not null and i.estado is true) order by e.nomeCompleto asc", condpar, i, j);
        final Iterator<Estudante> items = li.iterator();
        Estudante e;
        lbinscricao.setRows(lbinscricao.getItemCount() + li.size());
        while (items.hasNext()) {
            e = items.next();
            ((ListModelList) lbinscricao.getModel()).add(e);
        }
        if (li.size() < j) {
            btv.setVisible(false);
        } else {
//            pos = pos += li.size();
            btv.setVisible(true);
        }
    }

    public void onLoadi() {
        int i = lbinscricao.getItemCount();//pos;
        setLB(i, i + 20);
    }

    /////////////////////////////////////COMBOBOX PESQUISA//////////////////////////////////////////////////////////////
    public void onCombEstInsc(InputEvent evt) {
        indc = -1;
        if (!evt.getValue().equals("") && evt.getValue().charAt(0) != '.') {
            pesq = evt.getValue();
            condn = " and lower(e.nomeCompleto) like :nome ";
            if (cbpar.containsKey("nome")) {
                cbpar.replace("nome", "%" + evt.getValue().toLowerCase() + "%");
            } else {
                cbpar.put("nome", "%" + evt.getValue().toLowerCase() + "%");
            }
        } else {
            if (pesq != null) {
                pesq = "";
            }
            condn = "";
            if (!cbpar.containsKey("nome")) {
                return;
            }
            cbpar.remove("nome");
        }
        combEstInsc.getItems().clear();
        setLBComb(0, 20);
    }

    public void onOpen$combEstInsc() {

        if (pesq == null) {
            //   Messagebox.show("esta");
            //  combEstInsc.getItems().clear();
            setLBComb(0, 20);
            pesq = "";
        } else {
            //  Messagebox.show("nao esta");
            if (combEstInsc.isOpen()) {
                combEstInsc.setText(pesq);
            } else {
                if (indc != -1) {
                    combEstInsc.getSelectedIndex();
                    combEstInsc.setSelectedIndex(indc);
                }
            }
        }
    }

    public void setLBComb(int i, int j) {
        if (j == 20) {
            combEstInsc.setModel(new ListModelList<Estudante>());
        }
        List<Estudante> li = null;
        // Users u = csimpm.get(Users.class, usr.getUtilizador());
        Faculdade f = csimpm.get(Faculdade.class, usr.getFaculdade().getIdFaculdade());
        // par.clear();
        Date dano = new Date();
        Calendar cal = new GregorianCalendar();
        cal.setTime(dano);
        int ano = cal.get(Calendar.YEAR);
        cbpar.put("ano", ano);
        cbpar.put("fac", f);
        li = csimpm.findByJPQueryFilter("from Estudante e where e.cursocurrente.faculdade = :fac " + condn + " and e.idEstudante in (select m.estudante from Matricula m where "
                + "m.matriculaPK.ano = :ano and m.estudante not in (select ma.matriculaPK.idEstudante from Matriculaanulada ma where ma.matriculaPK.ano = :ano)) order by e.nomeCompleto", cbpar, i, j);
        final Iterator<Estudante> items = li.iterator();
        Estudante e;
        //lbinscricao.setRows(lbinscricao.getItemCount() + li.size());
        if (j > 20) {
            ((ListModelList) combEstInsc.getModel()).remove(new Estudante());
            new Combobox().appendChild(combEstInsc.getItemAtIndex(posc.getValue() - 1));
        }
        while (items.hasNext()) {
            e = items.next();
            ((ListModelList) combEstInsc.getModel()).add(e);
        }
        if (li.size() == j) {
            Estudante es = new Estudante();
            es.setNomeCompleto("-------Ver Mais-------");
            ((ListModelList) combEstInsc.getModel()).add(es);
            posc.setValue(((ListModelList) combEstInsc.getModel()).size());
        }
    }

    public void onLoadComb() {
        int i = ((ListModelList) combEstInsc.getModel()).size() - 1;
        setLBComb(i, i + 20);
    }

    public void onExcelExport() throws ParseException {
        if (lbinscricao.getItemCount() == 0) {
            Clients.showNotification("Sem conteúdo", "warning", null, null, 3000);
            return;
        }
        BeanToExcel beanToExcel = new BeanToExcel();
        beanToExcel.setDataSheetName("Grade de Notas");
        beanToExcel.exportExcell(lbinscricao);
    }

    public void onRefresh() {
        par.clear();
        par.put("nome", combEstInsc.getText());
        Estudante e = csimpm.findEntByJPQuery("from Estudante e where e.nomeCompleto = :nome", par);
        refre(e);

    }

    public void refre(Estudante e) {
        if (e != null) {
            int nc = 1;
            int nc2 = 0;
            int c1 = 0;
            int c2 = 1;
            int co1 = 0;
            int co2 = 0;
            Date dano = new Date();
            Calendar cal = new GregorianCalendar();
            cal.setTime(dano);
            int ano = cal.get(Calendar.YEAR);
            boolean bool = false;
            do {
                c1 = 0;
                c2 = 0;
                par.clear();
                par.put("e", e);
                par.put("planoc", e.getPlanoc());
                par.put("anoi", nc);
                par.put("ano", ano);
                // par.put("niv", "%"+e.getAnoIngresso() + "%");
                c1 = csimpm.findByJPQuery("from Inscricaodisciplina id where id.inscricao.idEstudante = :e and "
                        + "id.disciplina.nivel = :anoi and id.disciplina.planoc = :planoc and id.disciplina.semestre = 1 and (id.disciplinaActiva = 3"
                        + " and id.estado is true and ((id.notaFinal is not null and id.notaFinal >= 10) ) and extract(year from id.inscricao.dataInscricao) < :ano)", par).size();//or (id.notaFinal is null and extract(year from id.inscricao.dataInscricao) < :ano)
                c2 = csimpm.findByJPQuery("from Inscricaodisciplina id where id.inscricao.idEstudante = :e and "
                        + "id.disciplina.nivel = :anoi and id.disciplina.planoc = :planoc and id.disciplina.semestre = 2 and (id.disciplinaActiva = 3"
                        + " and id.estado is true and ((id.notaFinal is not null and id.notaFinal >= 10) ) and extract(year from id.inscricao.dataInscricao) < :ano)", par).size();//or (id.notaFinal is null and extract(year from id.inscricao.dataInscricao) < :ano)
                par.clear();
                par.put("curso", e.getCursocurrente());
                par.put("planoc", e.getPlanoc());
                par.put("nivel", nc);
                par.put("niv", "%" + e.getAnoIngresso() + "%");
                co1 = csimpm.findByJPQuery("from Disciplina d where d.curso=:curso and d.planoc = :planoc and d.semestre = 1 and"
                        + " d.nivel =:nivel and ((d.natraso is not null and d.natraso not like :niv) or d.natraso is null)", par).size();

                co2 = csimpm.findByJPQuery("from Disciplina d where d.curso=:curso and d.planoc = :planoc and d.semestre = 2 and"
                        + " d.nivel =:nivel and ((d.natraso is not null and d.natraso not like :niv) or d.natraso is null)", par).size();

                //Messagebox.show("c1 e co1:" + c1 + "|" + co1 + " c2 e co2:" + c2 + "|" + co2 + " nc2:" + nc2);
                //if(nc2==0)c2=1;
//                if (c1 != 0 || c2 != 0) {
//                    nc2 = nc2 + 1;
//                }
//                if (c2 != 0) {
//                    if ((co1 - c1) + (co2 - c2) <= 2) {// so no primeiro semestre
//                        nc = nc + 1;
//                        //  Messagebox.show("entrou");
//                    } else {
//                        if (nc2 != 0) {
//                            e.setNivelFrequencia(nc2);
//                        } else {
//                            e.setNivelFrequencia(1);
//                        }
//                        csimpm.update(e);
//                        if (combEstInsc != null) {
//                            onBtncancelar();//pra fechar o winAddInscricao se estiver aberto
//                        }
//                        return;
//                    }
//                }
//            } while (c2 != 0);
//            if (nc2 != 0) {
//                e.setNivelFrequencia(nc2);
//            } else {
//                e.setNivelFrequencia(1);
//            }
//            csimpm.update(e);
//            if (combEstInsc != null) {
//                onBtncancelar();//pra fechar o winAddInscricao se estiver aberto
//            }
                if (c1 != 0 || c2 != 0) {
                    nc2 = nc2 + 1;
                }
                if (c2 != 0) {
                    int t1 = 0, t2 = 0;
                    if (co1 >= c1) {
                        t1 = co1 - c1;
                    }
                    if (co2 >= c2) {
                        t2 = co2 - c2;
                    }
                    if (t1 + t2 <= 2) {// so no primeiro semestre
                        nc = nc + 1;
                        //  Messagebox.show("entrou");
                    } else {
                        if (nc2 != 0) {
                            e.setNivelFrequencia(nc2);
                        } else {
                            e.setNivelFrequencia(1);
                        }
                        csimpm.update(e);
                        calcularNiveFreq(e);
                        bool = true;
                        if (combEstInsc != null) {
                            onBtncancelar();//pra fechar o winAddInscricao se estiver aberto
                        }
                        return;
                    }
                }
                if (bool) {
                    break;
                }
            } while (c2 != 0);
            bool = false;
            e.setNivelFrequencia(nc2);
            csimpm.update(e);
            calcularNiveFreq(e);
            if (combEstInsc != null) {
                onBtncancelar();//pra fechar o winAddInscricao se estiver aberto
            }
        }
    }

    public void onEditar(ForwardEvent evt) throws FileNotFoundException, IOException {
        Button btn = (Button) evt.getOrigin().getTarget();
        Listitem litem = (Listitem) btn.getParent().getParent();
        Inscricao insc = (Inscricao) litem.getValue();
        insc = csimpm.load(Inscricao.class, insc.getIdInscricao());
//        LockMode l = csimpm.bloqueado(insc);
//        Clients.showNotification(l.toString(), "warning", null, null, 0, true);
//        if (l.compareTo(LockMode.PESSIMISTIC_WRITE) == 0) {
//            Clients.showNotification("Esta Inscrição esta sendo Validada por outro utilizador", "warning", null, null, 0);
//            return;
//        } else {
//            //insc = csimpm.getLocked(Inscricao.class,insc.getIdInscricao());
//            csimpm.lock(insc);
//            // while(!t.wasCommitted()){}
//        }
//        l = csimpm.bloqueado(insc);
//        Clients.showNotification(l.toString(), "warning", null, null, 0, true);
//        insc = csimpm.load(Inscricao.class, insc.getIdInscricao());
//        if (insc.getModoInscricao().intValue() > 2) {
//            Clients.showNotification("Esta Inscricao será validada pelo Director! Caso for aceite, poderá ser visualizada na Lista de Inscrições", "warning", null, null, 0);
//            return;
//        }
        win.setParent(winInscricao);
        win.doModal();
        ((Tab) win.getFellow("tabDadosInscricao")).setSelected(true);
        Calendar cal = new GregorianCalendar();
        cal.setTime(insc.getDataInscricao());
        int ano = cal.get(Calendar.YEAR);
        ((Intbox) win.getFellow("ibidInsc")).setValue(insc.getIdInscricao().intValue());
        ((Intbox) win.getFellow("ibitem")).setValue(litem.getIndex());
        int ve = -1;
        if (insc.getVersion() != null) {
            ve = insc.getVersion().intValue();
        }
        ((Intbox) win.getFellow("versao")).setValue(ve);
        ((Label) win.getFellow("lbano")).setValue(ano + "");
        ((Label) win.getFellow("lbsem")).setValue("Semestre " + insc.getSemestre().toString());
        ((Label) win.getFellow("lbnivel")).setValue(insc.getIdEstudante().getNivelFrequencia() + "");
        ((Label) win.getFellow("lbcurso")).setValue(insc.getIdEstudante().getCursocurrente() + "");
        if (insc.getIdEstudante().getMasculino()) {
            ((Label) win.getFellow("lbsexo")).setValue("Masculino");//csimpm.findByQuery("select s.descricao from fecn1.sexo s, fecn1.estudante e where s.id_sexo = e.sexo "
        } else {
            ((Label) win.getFellow("lbsexo")).setValue("Femenino");
        }
        sem = insc.getSemestre().intValue();
        par.clear();
        par.put("insc", insc.getIdInscricao());
        List<Disciplina> lds = csimpm.findByJPQuery("from Disciplina d where d in (select id.disciplina from Inscricaodisciplina id"
                + " where id.inscricaodisciplinaPK.idInscricao = :insc)", par);
        int i = 0;
        for (Disciplina d : lds) {
            i = i + d.getCredito();
        }
        ((Label) win.getFellow("lbcred")).setValue(i + "");
        Listbox discs = ((Listbox) win.getFellow("discSel"));
        limpaLB(discs);
        for (Disciplina d : lds) {
            Listitem list = new Listitem(d.getNome() + " - " + d.getNivel() + "º Ano - " + d.getSemestre() + "º Sem - " + d.getCredito() + " Credito", d);
//            list.setDisabled(true);
//            list.setCheckable(false);
//            discs.setMultiple(false);
            discs.appendChild(list);
//            discs.setMultiple(true);
        }
        Estudante e = insc.getIdEstudante();
        List<Estudante> lc = new ArrayList<Estudante>();
        lc.add(e);
        Combobox cbe = (Combobox) win.getFellow("combEstInsc");
        cbe.setModel(new ListModelList<Estudante>(lc));
//       // cbe.setText("dsfsfsdfsf");
        //     cbe.setSelectedIndex(0);
//        cbe.setReadonly(true);
//        cbe.removeAttribute("forward");
//        if(cbe.removeForward("onChanging", cbe, "onCombEstInsc")){
//              Messagebox.show("removeu");
//           }else{
//        Messagebox.show("nao removeu");
//        }
//        cbe.setButtonVisible(false);

        //Recibos
        ((Row) win.getFellow("rwRU")).setVisible(false);
        List<Arquivoinscricao> lam3 = insc.getArquivoinscricaoList();
        List<Arquivo> lm3 = new ArrayList<Arquivo>();
        List<Arquivo> lm4 = new ArrayList<Arquivo>();
        String recibousado = "";
        String dat = new SimpleDateFormat("dd_MM_yyyy_HH_mm_ss").format(insc.getDataInscricao());
        try {
            for (Arquivoinscricao am3 : lam3) {
                String sss = UserAutentic.getPathR("4") + insc.getIdEstudante().getNrEstudante() + am3.getNome() + dat;
                //File f = new File(ss);

                Media pp = null;
                Media mm = null;
                try {
                    InputStream iss = new FileInputStream(sss);
                    pp = new AMedia(sss, FilenameUtils.getExtension(sss), URLConnection.guessContentTypeFromName(sss), iss);
                    mm = new AMedia(am3.getNome(), FilenameUtils.getExtension(sss), URLConnection.guessContentTypeFromName(sss), pp.getByteData());
                } catch (java.io.FileNotFoundException ex) {
                } finally {
                    if (mm != null) {
                        ((Textbox) win.getFellow("tbimg4")).setValue(mm.getName());
                        if (am3.getRecibousado() != null && am3.getRecibousado() == true) {
                            par.clear();
                            par.put("nrt", am3.getNrtalao());
                            List<Arquivoinscricao> lamat = csimpm.findByJPQuery("from Arquivoinscricao a where a.nrtalao like :nrt", par);
                            String rs = "Recibo Nr.: " + am3.getNrtalao() + " foi usado por:", rs2 = "";
                            for (Arquivoinscricao ama : lamat) {
                                rs2 = rs2 + "\nEstudante: " + ama.getEstudante() + "\n"
                                        + "Taxa Matricula: " + ama.getValorcobrado() + " Data Matricula: " + ama.getDataInscricao();
                            }
                            recibousado = recibousado + "\n" + rs + "\n" + rs2;
                        }

                        if (!am3.getNrtalao().equals("0")) {
                            lm3.add(new Arquivo(am3.getNrtalao(), mm, mm.getName(), am3.getBanco(), am3.getValor(), am3.getEstudante(), am3.getDatadeposito()));
                        } else {
                            lm4.add(new Arquivo(am3.getNrtalao(), mm, mm.getName(), am3.getBanco(), am3.getValor(), am3.getEstudante(), am3.getDatadeposito()));
                        }
                    }
                }
            }
        } finally {
            ((Textbox) win.getFellow("txMot")).setText(insc.getObs());
            if (!recibousado.equals("")) {
                ((Row) win.getFellow("rwRU")).setVisible(true);
                ((Textbox) win.getFellow("txRU")).setText(recibousado);
            }
            ((Listbox) win.getFellow("lbimg")).setModel(new ListModelList(lm3));
            ((Listbox) win.getFellow("lbimgII")).setModel(new ListModelList(lm4));

        }
    }

    public void onVerInfo(ForwardEvent evt) throws JRException, IOException {
        Button btn = (Button) evt.getOrigin().getTarget();
        Listitem litem = (Listitem) btn.getParent().getParent();
        Inscricao i = (Inscricao) litem.getValue();
        i = csimpm.load(Inscricao.class, i.getIdInscricao());
        par.clear();
        par.put("i", i);
        List<Inscricaodisciplina> lid = csimpm.findByJPQuery("from Inscricaodisciplina id where id.inscricao = :i and id.estado is true", par);
        List<Arquivoinscricao> la = i.getArquivoinscricaoList();
        String path = winInscricao.getDesktop().getWebApp().getRealPath("/relatorio");
        String path2 = winInscricao.getDesktop().getWebApp().getRealPath("/img");
        Window win = (Window) Executions.createComponents("/report.zul", null, null);
        win.setTitle(i.getIdEstudante().getNomeCompleto());
//        Inscricaodisciplina id=new Inscricaodisciplina();
//        if (lbinscdisc.getItemCount() > 0) {
//            id = ((Inscricaodisciplina) lbinscdisc.getItems().get(0).getValue());
//            id = csimp.load(Inscricaodisciplina.class, id.getInscricaodisciplinaPK());
//        }
//        List<Inscricao> li = new ArrayList<Inscricao>();
//        li.add(i);
        JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(lid);//((ListModelList) lbinscdisc.getListModel()).getInnerList());
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("img", path2 + "/icoul.png");
        params.put("fac", i.getIdEstudante().getCursocurrente().getFaculdade().getDesricao());
        if (la != null && !la.isEmpty() && la.get(0).getDatadeposito() != null) {
            params.put("nrrec", la.get(0).getNrtalao());
            params.put("valrec", la.get(0).getValor());
            params.put("banco", la.get(0).getBanco());
        } else {
            params.put("nrrec", null);
            params.put("valrec", null);
            params.put("banco", null);
        }
        if (la != null && !la.isEmpty() && la.get(0).getDatadeposito() != null) {
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
        Iframe iframe = (Iframe) win.getFellow("report");
        InputStream mediais = new ByteArrayInputStream(bytesOutputStream.toByteArray());
        bytesOutputStream.close();

        AMedia amedia = new AMedia(i.getIdEstudante().getNomeCompleto() + ".pdf", "pdf", "application/pdf", mediais);

        iframe.setContent(amedia);
    }

    public void onVerObs(ForwardEvent evt) {
        Inscricao i = (Inscricao) ((Listitem) evt.getOrigin().getTarget().getParent().getParent()).getValue();
        i = csimpm.load(Inscricao.class, i.getIdInscricao());
        Window w = new Window();
        w.setAttribute("apply", "esira.inscricao.InscricaoController");
        Vbox vb = new Vbox();
        vb.setParent(w);
        Textbox t = new Textbox("");
        if (i.getObs() != null) {
            t.setText(i.getObs());
        }
        t.setRows(10);
        t.setParent(vb);
        Intbox it = new Intbox();
        Intbox it2 = new Intbox();
        it.setVisible(false);
        it2.setVisible(false);
        it.setValue(i.getIdInscricao().intValue());
        it.setParent(vb);
        it2.setParent(vb);
        Button b = new Button("", "/icones/OK.png");
        b.setId("mm");
        //b.addForward("onClick",  b, "onVerRequerimento");
        b.addEventListener("onClick", new EventListener() {
            public void onEvent(Event evt) throws IOException {
                onSalvar(evt);
            }
        });
        b.setAttribute("apply", "esira.inscricao.InscricaoController");
        b.setParent(vb);
        w.setTitle("Observações");
        w.setId("winx");
        w.setClosable(true);
        w.setHflex("min");
        w.setVflex("min");
        //  Executions.createComponents(w.getPage().getRequestPath(), winInscricao, null);
        w.setParent(winInscricao);
        w.doModal();
    }

    public void onSalvar(final Event evt) throws FileNotFoundException, IOException {
        Intbox it = (Intbox) evt.getTarget().getParent().getChildren().get(1);
        final Inscricao m = csimpm.load(Inscricao.class, it.getValue().longValue());
        Textbox obs = (Textbox) evt.getTarget().getParent().getChildren().get(0);
        m.setObs(obs.getValue());
        Messagebox.show("Actualizar?", "Atenção", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION,
                new EventListener() {
                    @Override
                    public void onEvent(Event evet) throws IOException {
                        switch (((Integer) evet.getData()).intValue()) {
                            case Messagebox.YES:
                                csimpm.update(m);
                                ((Window) evt.getTarget().getParent().getParent()).detach();
                                break;
                            case Messagebox.NO:
                                return;
                        }
                    }
                });
    }

    public ListModel<Curso> getCursoModel() {
        // Users u = csimp.get(Users.class, usr.getUtilizador());
        Faculdade f = null;
        List<Curso> lc = null;
        if (usr.getUestudante() && usr.getFaculdade() != null) {
            f = csimpm.get(Faculdade.class, usr.getFaculdade().getIdFaculdade());
            par.clear();
            par.put("fac", f);
            lc = csimpm.findByJPQuery("from Curso c where c.faculdade = :fac", par);
        } else {
            lc = csimpm.findByJPQuery("from Curso c", par);
        }
        return new ListModelList<Curso>(lc);
    }

    public void onSelectcbcursoI() {
        if (cbcursoI.getSelectedItem() != null) {
            Curso c = (Curso) cbcursoI.getSelectedItem().getValue();
            List<Planocurricular> lc = null;
            if (idcurso.getValue() == null) {
                Clients.showNotification("Seleccione um estudante", "error", null, null, 2000);
                cbcursoI.setSelectedIndex(-1);
                return;
            }
            if (c.getIdCurso() == idcurso.getValue().longValue()) {
                par.clear();
                par.put("c", c);
                par.put("planoc", idplano.getValue());
                // Messagebox.show(1+"");
                lc = csimpm.findByJPQuery("from Planocurricular p where p.curso = :c and p.planocurricularPK.ano>:planoc order by p.planocurricularPK.ano desc", par);
            } else { // Messagebox.show(2+"");
                par.clear();
                par.put("c", c);
                lc = csimpm.findByJPQuery("from Planocurricular p where p.curso = :c order by p.planocurricularPK.ano desc", par);
            }

            cbPlanoD.setSelectedIndex(-1);
            cbDisc.setSelectedIndex(-1);
            cbPlanoD.setValue("-Plano curric.-");
            cbDisc.setValue("-Disciplina-");
            cbPlanoD.setModel(new ListModelList<Planocurricular>(lc));

            cbDisc.setModel(null);
//            if (cbPlanoD.getSelectedItem() != null) {
//                par.put("planoc", ((Planocurricular) cbPlanoD.getSelectedItem().getValue()).getPlanocurricularPK().getAno());
//            } else {
//                par.put("planoc", c.getPlanoc());// ent.put("d", Disciplina.class);
//            }
//            List<Disciplina> ld = csimpm.findByJPQuery("from Disciplina d where d.curso = :c and d.planoc = :planoc", par);
//            cbDisc.setModel(new ListModelList<Disciplina>(ld));
        }
    }

    public void onSelectcbPlanoD() {
        if (cbPlanoD.getSelectedItem() != null) {
            par.clear();
            Curso c = (Curso) cbcursoI.getSelectedItem().getValue();
            Planocurricular pl = (Planocurricular) cbPlanoD.getSelectedItem().getValue();
            par.put("c", c);
            par.put("planoc", pl.getPlanocurricularPK().getAno());
            List<Disciplina> ld = csimpm.findByJPQuery("from Disciplina d where d.curso = :c and d.planoc = :planoc", par);
            cbDisc.setModel(new ListModelList<Disciplina>(ld));
        }
    }

    public void onSelectcbDisc() {
        if (cbDisc.getSelectedItem() != null) {
            boolean cond = true;
            Disciplina d = (Disciplina) cbDisc.getSelectedItem().getValue();
            d = csimpm.get(Disciplina.class, d.getIdDisc());
            if (discSel.getItems() != null) {
                Iterator<Listitem> li = discSel.getItems().iterator();
                while (li.hasNext()) {
                    Listitem lit = li.next();
                    // Messagebox.show(((Disciplina) lit.getValue()).getIdDisc()+" "+d.getIdDisc());
                    if (Objects.equals(((Disciplina) lit.getValue()).getIdDisc(), d.getIdDisc())) {
                        cond = false;
                    }
                }
            }
            if (cond) {
                Listitem li = new Listitem();
                Listcell lc = new Listcell();
                Label le = new Label();
                le.setValue(d.getNome() + " - " + d.getNivel() + "º Ano" + " - " + d.getSemestre() + "º Sem-" + d.getCredito() + " Credito - " + d.getCurso().getAbreviatura() + "-" + d.getPlanoc());
                le.setStyle("color: #008080");
                le.setParent(lc);
                lc.setParent(li);
                li.setValue(d);
//                if(discSel.getModel()==null){
//                   discSel.setModel(new ListModelList<>());
//                }
                discSel.setMultiple(false);
                // ((ListModelList)discSel.getModel()).add(d);
                discSel.appendChild(li);
                discSel.setMultiple(true);
                Clients.showNotification("Adicionado a selecao", null, null, null, 3000);
            }
            cbDisc.setSelectedIndex(-1);
            cbDisc.setValue("-Disciplina-");
        }
    }
}
