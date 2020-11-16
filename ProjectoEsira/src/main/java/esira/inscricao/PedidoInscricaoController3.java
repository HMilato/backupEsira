/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package esira.inscricao;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.DocumentException;
import entidade.Arquivo;
import esira.domain.Arquivoestudante;
import esira.domain.Arquivoinscricao;
import esira.domain.Arquivomatricula;
import esira.domain.Curso;
import esira.domain.Disciplina;
import esira.domain.Disciplinaanulada;
import esira.domain.Estudante;
import esira.domain.Faculdade;
import esira.domain.Funcionario;
import esira.domain.Grupo;
import esira.domain.Inscricao;
import esira.domain.Inscricaodisciplina;
import esira.domain.InscricaodisciplinaPK;
import esira.domain.Matricula;
import esira.domain.MatriculaPK;
import esira.domain.Motivoinsc;
import esira.domain.Notificacao;
import esira.domain.PlanificacaoAnoLectivo;
import esira.domain.Prescricao;
import esira.domain.Users;
import esira.domain.Validacaopendente;
import esira.domain.ValidacaopendentePK;
import esira.matricula.GuardarEstudanteController;
import esira.service.CRUDService;
import esira.service.Email;
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
import org.apache.commons.mail.EmailException;
import org.hibernate.Transaction;
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
import org.zkoss.zul.Auxhead;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Hlayout;
import org.zkoss.zul.Iframe;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Menuitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Row;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Tabs;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

/**
 *
 * @author Administrator
 */
public class PedidoInscricaoController3 extends GenericForwardComposer {

    @WireVariable
    private CRUDService csimpm = (CRUDService) SpringUtil.getBean("CRUDService");
    private Window winAddInscricaop, winAddMotivo;
    private Combobox combEstInscp, combSemp;
    private ListModel<Estudante> estudantes;
    private ListModel<Inscricao> inscric;
    private ListModel<Disciplina> disciplinas;
    private ListModel<Curso> listaCursoModel;
    private Label lbcursop, lbnivelp, lbsexop, lbcredp, lbtaxaInscricaop, lbtaxaInscricaoE, lbanop, lbNumero,
            lbtaxaMulta15dias, lbtaxaMulta30dias, lbtotal, lbsemp;
    private Listbox discSelp, lbinscricaop, lbdiscInsc, lbimg, lbimg1;
    private Tab tabPagamentoTaxa, tabDadosInscricao;
    private Tabs tabs;
    private Row rwTaxanM, rwTaxaeM, rwTaxamulta15, rwTaxamulta30;
    private Button addInsc, btnCancDisc, btnAnular;
    Textbox txnrTalao, txx, tbimg4, txMotivop, txMotivoR, txdiscp;
    List listaI;
    Datebox dinscp;
    private static Media mediaCPI;
    private int ano;
    private int sem = 0;
    private int n;
    private Intbox ibitemp, ibidInscp, ibmulta, versaop;
    Map<String, Object> par = new HashMap<String, Object>();
    Users usr = (Users) Sessions.getCurrent().getAttribute("user");
    private Hlayout ahead;
    String condfac = "", condnr = "", condnome = "", condgenero = "", condanoi = "", condano = "", condcurso = "";
    Map<String, Object> condpar = new HashMap<String, Object>();
    Combobox cbcursop;
    private Intbox ibProcAno;
    private Button btv;
    Menuitem manoi;
    private EventQueue eq;

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        Date dano = new Date();
        Calendar cal = new GregorianCalendar();
        cal.setTime(dano);
        ano = cal.get(Calendar.YEAR);

    }

    public void limpForm() {
        //combEstInsc.setValue("Pesquise um Estudante...");
        lbcursop.setValue("");
        lbnivelp.setValue("");
        lbsexop.setValue("");
        lbcredp.setValue("");
        lbsemp.setValue("");
//        discEst.setModel(new ListModelList<Disciplina>(new ArrayList<Disciplina>()));
//        discEstOp.setModel(new ListModelList<Disciplina>(new ArrayList<Disciplina>()));
        //lbimg.setModel(new ListModelList<Arquivo>(new ArrayList<Arquivo>()));
        limpaLB(discSelp);
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

    public void onBtncancelarp() {
        // limpForm();
        winAddInscricaop.detach();
    }

    public void onChange$combEstInsc(Event event) {
        if (combEstInscp.getSelectedItem() == null) {
            return;
        }
        Date dano = new Date();
        Calendar cal = new GregorianCalendar();
        cal.setTime(dano);
        int ano = cal.get(Calendar.YEAR);
        Estudante e = (Estudante) combEstInscp.getSelectedItem().getValue();
        e = csimpm.load(Estudante.class, e.getIdEstudante());
        Map<String, Object> par = new HashMap<String, Object>();
        Map<String, Object> ent = new HashMap<String, Object>();
        par.put("ide", e.getIdEstudante());
        lbcursop.setValue(e.getCursocurrente().getDescricao());//csimpm.findByQuery("select c.descricao from fecn1.curso c, fecn1.estudante e where c.id_curso = e.cursocurrente "
        // + "and e.id_estudante = :ide", null, par).get(0).toString());
        lbnivelp.setValue(e.getNivelFrequencia() + "");
        if (e.getMasculino()) {
            lbsexop.setValue("Masculino");//csimpm.findByQuery("select s.descricao from fecn1.sexo s, fecn1.estudante e where s.id_sexo = e.sexo "
        }                //+ "and e.id_estudante = :ide", null, par).get(0).toString());
        if (!e.getMasculino()) {
            lbsexop.setValue("Femenino");
        }
        int nc = e.getNivelFrequencia();
        int c = 0;
        this.n = nc;
    }

    public void onBtnAvancar1() {
        if (combEstInscp.getSelectedItem() == null || discSelp.getItemCount() == 0) {
            Clients.showNotification("Por favor, seleccione um estudante e as respectivas disciplinas", "error", null, null, 0);
            return;
        } else {
            tabDadosInscricao.setSelected(false);
            tabPagamentoTaxa.setSelected(true);
        }
        preenchTabPagementoTaxa();
    }

    public void onSwitchTab$tabPagamentoTaxa(ForwardEvent evt) {
        if (combEstInscp.getSelectedItem() == null || discSelp.getItemCount() == 0) {
            Clients.showNotification("Por favor, seleccione um estudante e as respectivas disciplinas", "error", null, null, 0);
//            tabPagamentoTaxa.setSelected(false);
//            tabDadosInscricao.setSelected(true);
            return;
        }
        final Tab tab = (Tab) evt.getOrigin().getTarget();
        tab.setSelected(true);
        // if (lbtotal.getValue().equals("")) {
        preenchTabPagementoTaxa();
        // }
    }

    public void onSwitchTab$tabDadosInscricao(ForwardEvent evt) {
        final Tab tab = (Tab) evt.getOrigin().getTarget();
        tab.setSelected(true);
    }

    public void preenchTabPagementoTaxa() {
        Date dano = new Date();
        Calendar cal = new GregorianCalendar();
        cal.setTime(dano);
        Estudante e = (Estudante) combEstInscp.getSelectedItem().getValue();
        e = csimpm.load(Estudante.class, e.getIdEstudante());
        Map<String, Object> par = new HashMap<String, Object>();
        Map<String, Object> ent = new HashMap<String, Object>();
        par.put("ide", e.getIdEstudante());
        int pais = (Integer) csimpm.findByQuery("select p.id_pais from fecn1.pais p, fecn1.estudante e "
                + "where p.id_pais = e.nacionalidade and e.id_estudante = :ide "
                + "and e.id_estudante = :ide", null, par).get(0);
//        par.clear();
//        par.put("ano", ano);
//        ent.put("p", PlanificacaoAnoLectivo.class);
        Float total = null;
        PlanificacaoAnoLectivo pal = csimpm.findEntByJPQuery("from PlanificacaoAnoLectivo", null);
        if (pais != 1) {
            rwTaxaeM.setVisible(true);
            lbtaxaInscricaoE.setValue(pal.getTaxaInscricaoEstrangeiro() + "");
            total = pal.getTaxaInscricaoEstrangeiro().floatValue() * discSelp.getItemCount();
        } else {
            rwTaxanM.setVisible(true);
            lbtaxaInscricaop.setValue(pal.getTaxaInscricaoNacional() + "");
            total = pal.getTaxaInscricaoNacional().floatValue() * discSelp.getItemCount();
        }
        Inscricao i = csimpm.get(Inscricao.class, ibidInscp.getValue().longValue());
    }

    public static void setMediaTaxa(Media media) {
        mediaCPI = media;
    }

//    public void onVerDisc(ForwardEvent evt) throws Exception {
//        winAddInscricaoDisc.setParent(winInscricaoP);
//        winAddInscricaoDisc.doModal();
//        Button btn = (Button) evt.getOrigin().getTarget();
//        Listitem litem = (Listitem) btn.getParent().getParent();
//        Inscricao i = (Inscricao) litem.getValue();
//        Map<String, Object> par = new HashMap<String, Object>();
//        Map<String, Object> ent = new HashMap<String, Object>();
//        ent.put("id", Inscricaodisciplina.class);
//        par.put("insc", i.getIdInscricao());
//        ((Listbox) winAddInscricaoDisc.getFellow("lbdiscInsc")).setMultiple(false);
//        List<Inscricaodisciplina> ldisc = csimpm.findByQuery("select id.* from fecn1.inscricaodisciplina id "
//                + "where id.id_inscricao = :insc", ent, par);
//        ((Listbox) winAddInscricaoDisc.getFellow("lbdiscInsc")).setModel(new ListModelList<Inscricaodisciplina>(ldisc));
//        ((Listbox) winAddInscricaoDisc.getFellow("lbdiscInsc")).setMultiple(true);
//    }
    public void onBtnCnfp() {
//        Inscricao i = csimpm.get(Inscricao.class, ibidInsc.getValue().longValue());
//        Estudante e = i.getIdEstudante();
//        par.clear();
//        Date datactual = new Date();
//        Calendar calendar = new GregorianCalendar();
//        calendar.setTime(datactual);
//        int anoActual = calendar.get(Calendar.YEAR);
//        MatriculaPK mpk = new MatriculaPK(e.getIdEstudante(), anoActual);
//        par.clear();
//        par.put("ide", mpk);
//        Matricula m = (Matricula) csimpm.findEntByJPQuery("from Matricula m where m.matriculaPK=:ide", par);
//        if (m != null && (m.getConfirmacao() == null || m.getEstado() == false)) {
//            Clients.showNotification(" Antes de validar uma inscriçaão, é necessário validar a matricula", "warning", null, null, 0);
//            return;
//        }
        Messagebox.show("Pretende validar este Pedido de Inscricao?", "", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION,
                new EventListener() {
                    @Override
                    public void onEvent(Event evet) throws InterruptedException, DocumentException, BadElementException, IOException {
                        switch (((Integer) evet.getData()).intValue()) {
                            case Messagebox.YES:
                                Inscricao i = null;
                                Users u = csimpm.get(Users.class, usr.getUtilizador());
                                Faculdade f = csimpm.get(Faculdade.class, usr.getFaculdade().getIdFaculdade());
                                Transaction t = csimpm.getTransacao();
                                try {
                                    t.begin();
                                    int v = versaop.getValue().intValue();
                                    i = csimpm.getLocked(Inscricao.class, ibidInscp.getValue().longValue());
                                    if (v == -1 && i.getVersion() == null || v == i.getVersion().intValue()) {
                                        if (i.getVersion() == null) {
                                            i.setVersion(new Short("0"));
                                        } else {
                                            int ver = i.getVersion().intValue();
                                            ver = ver + 1;
                                            i.setVersion(new Short(ver + ""));
                                        }
                                    } else {
                                        Clients.showNotification("Esta Inscrição ja foi Validada por outro utilizador", "warning", null, null, 0);
                                        t.rollback();
                                        winAddInscricaop.detach();
                                        return;
                                    }
                                    if (i.getGrupo() != null) {
                                        List<Inscricao> li = i.getGrupo().getInscricaoList();
                                        int nivel = 0;
                                        boolean b = false;
                                        if (i.getModoInscricao().intValue() == 8) {
                                            nivel = i.getGrupo().getNiveli();
                                            b = true;
                                        }
                                        final Iterator<Inscricao> items = li.iterator();
                                        Inscricao ii;
                                        while (items.hasNext()) {
                                            ii = items.next();
                                            Estudante e = ii.getIdEstudante();
                                            Calendar cal = Calendar.getInstance();
                                            cal.setTime(ii.getDataInscricao());
                                            int an = cal.get(Calendar.YEAR);
                                            MatriculaPK mpk = new MatriculaPK(e.getIdEstudante(), an);
                                            par.clear();
                                            par.put("mpk", mpk);
                                            if (ii.getModoInscricao().intValue() == 7) {
                                                if (e.getTransferido() != null) {
                                                    e.setTransferido(new Short("2"));
                                                }
                                                if (e.getMudancac() != null) {
                                                    e.setMudancac(new Short("2"));
                                                }
                                            }
                                            Matricula m = csimpm.findEntByJPQuery("from Matricula m where m.matriculaPK = :mpk", par);
                                            if (m == null) {
                                                m = new Matricula(mpk);
                                                m.setEstado(true);
                                                m.setEstudante(e);
                                                m.setAnulada(false);
                                                m.setCurso(e.getCursocurrente());
                                                m.setValor(Float.NaN);
                                                m.setModoMatricula(3);
                                                m.setDataMatricula(cal.getTime());
                                                m.setConfirmacao(new Date());
                                                m.setFuncionario(u.getIdFuncionario());
                                                csimpm.Saves(m);
                                            }
                                            ii.setFuncionario(u.getIdFuncionario());
                                            ii.setDataConfirmacao(new Date());
                                            ii.setEstado(true);
                                            //calcular o nivel de frequencia
                                            if (b && (nivel > e.getNivelFrequencia())) {
                                                calcularNiveFreq(e);
                                            }
                                            csimpm.updates(ii);
                                            par.clear();
                                            par.put("insc", ii);
                                            Inscricaodisciplina ld = csimpm.findEntByJPQuery("from Inscricaodisciplina id"
                                                    + " where id.inscricao = :insc", par);
                                            if (ld.getNotaFinal() != null && ld.getNotaFinal() < 10) {
                                                verificarPrescricao(ld);
                                            }
                                        }
                                    } else {
                                        if (i.getModoInscricao().intValue() == 9) {
                                            Estudante e = i.getIdEstudante();
                                            Calendar cal = Calendar.getInstance();
                                            cal.setTime(i.getDataInscricao());
                                            int an = cal.get(Calendar.YEAR);
                                            MatriculaPK mpk = new MatriculaPK(e.getIdEstudante(), an);
                                            par.clear();
                                            par.put("mpk", mpk);
                                            Matricula m = csimpm.findEntByJPQuery("from Matricula m where m.matriculaPK = :mpk", par);
                                            if (m == null) {
                                                m = new Matricula(mpk);
                                                m.setEstado(true);
                                                m.setEstudante(e);
                                                m.setAnulada(false);
                                                m.setCurso(e.getCursocurrente());
                                                m.setValor(Float.NaN);
                                                m.setModoMatricula(3);
                                                m.setDataMatricula(cal.getTime());
                                                m.setConfirmacao(new Date());
                                                m.setFuncionario(u.getIdFuncionario());
                                                csimpm.Saves(m);
                                            }
                                        }
                                        i.setFuncionario(u.getIdFuncionario());
                                        i.setDataConfirmacao(new Date());
                                        i.setEstado(true);
                                        csimpm.updates(i);
                                        par.clear();
                                        par.put("insc", i);
                                        List<Inscricaodisciplina> idis = csimpm.findByJPQuery("from Inscricaodisciplina id"
                                                + " where id.inscricao = :insc", par);
                                        final Iterator<Inscricaodisciplina> items = idis.iterator();
                                        Inscricaodisciplina idisc;
                                        while (items.hasNext()) {
                                            idisc = items.next();
                                            if (idisc.getNotaFinal() != null && idisc.getNotaFinal() < 10) {
                                                verificarPrescricao(idisc);
                                            }
                                        }
                                    }
                                    Clients.showNotification(" Inscricao validada com Sucesso", null, null, null, 2000);
                                   

                                //calcular o nivel de frequencia
                                if (i.getModoInscricao().intValue() == 9) {
//                                    par.clear();
//                                    par.put("insc", i);
//                                    List<Disciplina> ld = csimpm.findByJPQuery("select id.disciplina from Inscricaodisciplina id"
//                                            + " where id.inscricao = :insc", par);
//                                    int k = 0;
                                    Estudante e = i.getIdEstudante();
//                                    final Iterator<Disciplina> items = ld.iterator();
//                                    Disciplina ii;
//                                    while (items.hasNext()) {
//                                        ii = items.next();
//                                        if (ld.get(k).getNivel() <= e.getNivelFrequencia()) {
//                                            k++;
//                                        }
//                                    }
//                                    if (k <= (ld.size() - 1)) {
                                    calcularNiveFreq(e);
//                                    }
                                }
//                                try {
//                                    String path = winInscricaoP.getDesktop().getWebApp().getRealPath("/img");
//                                    new Email().sendEmailReciboInsc(UserAutentic.getEmailP("email"),
//                                            i.getIdEstudante().getEmail(), "Validação de Inscrição", "Prezado " + i.getIdEstudante().getNomeCompleto() + ""
//                                            + "\nA sua Inscrição foi validada com Sucesso\n\n----------------------------------------"
//                                            + "Registo Acadêmico - UniLúrio-" + u.getFaculdade().getDesricao(),
//                                            UserAutentic.getEmailP("senha"), path + "/icoul.png", i, i.getInscricaodisciplinaList());
//                                } catch (EmailException uh) {
//                                    Clients.showNotification("O Envio de e-mail Falhou!", "warning", null, null, 0, true);
//                                }
                                ///////////
                                if (i.getModoInscricao().intValue() != 0) {
                                    String tipopedido = "";
                                    if (i.getModoInscricao().intValue() == 1) {
                                        tipopedido = "g4";
                                    } else {
                                        tipopedido = "g7";
                                    }
                                    ValidacaopendentePK vpk = new ValidacaopendentePK(f.getIdFaculdade(), tipopedido);
                                    Validacaopendente vp = csimpm.getLocked(Validacaopendente.class, vpk);
                                    if (vp != null) {
                                        if ((vp.getQtd() - 1) < 0) {
                                            vp.setQtd(0);
                                        } else {
                                            vp.setQtd(vp.getQtd() - 1);
                                        }
                                        csimpm.updates(vp);
                                    } else {
                                        vp = new Validacaopendente();
                                        vp.setValidacaopendentePK(vpk);
                                        vp.setFaculdade(f);
                                        vp.setQtd(0);
                                        csimpm.Saves(vp);
                                    }
                                    eq = EventQueues.lookup("valid" + f.getIdFaculdade(), EventQueues.APPLICATION, true);
                                    eq.publish(new Event("onValidIns", null, vp));
                                }//
                                eq = EventQueues.lookup("rinscD" + f.getIdFaculdade(), EventQueues.APPLICATION, true);
                                eq.publish(new Event("onRinscD", null, i));
                                eq = EventQueues.lookup("rinscD" + i.getIdEstudante().getIdEstudante(), EventQueues.APPLICATION, true);
                                eq.publish(new Event("onRinscEstud", null, i));
                                //  ((ListModelList) ((Listbox) winAddInscricao.getParent().getFellow("lbinscricaop")).getModel()).remove(i);
//                                Listitem li = ((Listbox) winAddInscricao.getParent().getFellow("lbinscricaop")).getItemAtIndex(ibitem.getValue());
//                                   if(li!=null) new Listbox().appendChild(li);
                                 t.commit();
                                    while (!t.wasCommitted())
            ;
                                } catch (RuntimeException e) {
                                    t.rollback();
                                    Clients.showNotification("Erro", e.getMessage(), null, null, 2000);
                                    return;
                                }
                                winAddInscricaop.detach();
                                break;
                            case Messagebox.NO:
                                return;
                        }
                    }
                });

    }

    public void calcularNiveFreq(Estudante e) {
        int nc = e.getNivelFrequencia();
        int nc2 = nc;
        int c1 = 0;
        int c2 = 0;
        do {
            c1 = 0;
            c2 = 0;
            par.clear();
            par.put("e", e);
            par.put("planoc", e.getPlanoc());
            par.put("anoi", nc);
            //par.put("niv", "%" + e.getAnoIngresso() + "%");
            c1 = csimpm.findByJPQuery("from Inscricaodisciplina id where id.inscricao.idEstudante = :e and "
                    + "id.disciplina.nivel = :anoi and id.disciplina.planoc = :planoc and id.disciplina.semestre = 1 and (id.disciplinaActiva = 3"
                    + " and id.estado is true and ((id.notaFinal is not null and id.notaFinal >= 10)))", par).size();// or id.notaFinal is null
            c2 = csimpm.findByJPQuery("from Inscricaodisciplina id where id.inscricao.idEstudante = :e and "
                    + "id.disciplina.nivel = :anoi and id.disciplina.planoc = :planoc and id.disciplina.semestre = 2 and (id.disciplinaActiva = 3"
                    + " and id.estado is true and ((id.notaFinal is not null and id.notaFinal >= 10)))", par).size();// or id.notaFinal is null
            par.clear();
            par.put("curso", e.getCursocurrente());
            par.put("nivel", nc);
            par.put("planoc", e.getPlanoc());
            par.put("niv", "%" + e.getAnoIngresso() + "%");
            int co1 = csimpm.findByJPQuery("from Disciplina d where d.curso=:curso and d.planoc = :planoc and d.semestre = 1 and"
                    + " d.nivel =:nivel and ((d.natraso is not null and d.natraso not like :niv) or d.natraso is null)", par).size();

            int co2 = csimpm.findByJPQuery("from Disciplina d where d.curso=:curso and d.planoc = :planoc and d.semestre = 2 and"
                    + " d.nivel =:nivel and ((d.natraso is not null and d.natraso not like :niv) or d.natraso is null)", par).size();
            if (c1 != 0 || c2 != 0) {
                if (nc2 != nc) {
                    nc2 = nc2 + 1;
                }
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
                    //      Messagebox.show("entrou");
                } else {
                    e.setNivelFrequencia(nc2);
                    csimpm.updates(e);
                    return;
                }
            }
        } while (c2 != 0);
        //if (nc2 > nc) {
        e.setNivelFrequencia(nc2);
        csimpm.updates(e);
        //}
    }

    public void onRejeitarp(ForwardEvent event) throws IOException {
        final HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("controler", "esira.inscricao.PedidoInscricaoController3");
        Window w = (Window) Executions.createComponents("/inscricao/MotivoInsc.zul", winAddInscricaop, map);
        ((Textbox) w.getFellow("txMotivoR")).setText("Inscrição foi rejeitada.\n Os dados estão incorrectos");
    }

    public void onBtnC() {
        winAddMotivo.detach();
    }

    public void onBtnRjt() {
        Messagebox.show("Pretende Rejeitar este Pedido de Inscricao?", "", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION,
                new EventListener() {
                    @Override
                    public void onEvent(Event evet) throws InterruptedException, IOException, EmailException {
                        switch (((Integer) evet.getData()).intValue()) {
                            case Messagebox.YES:
                                Long idi = ((Intbox) ((Window) winAddMotivo.getParent()).getFellow("ibidInscp")).getValue().longValue();
                                Inscricao i = null;
                                Users u = csimpm.get(Users.class, usr.getUtilizador());
                                Faculdade f = csimpm.get(Faculdade.class, usr.getFaculdade().getIdFaculdade());
                                Transaction t = csimpm.getTransacao();
                                try {
                                    t.begin();
                                    int v = ((Intbox) ((Window) winAddMotivo.getParent()).getFellow("versaop")).getValue().intValue();
                                    i = csimpm.getLocked(Inscricao.class, idi);
                                    if (v == -1 && i.getVersion() == null || v == i.getVersion().intValue()) {
                                        if (i.getVersion() == null) {
                                            i.setVersion(new Short("0"));
                                        } else {
                                            int ver = i.getVersion().intValue();
                                            ver = ver + 1;
                                            i.setVersion(new Short(ver + ""));
                                        }
                                    } else {
                                        Clients.showNotification("Esta Inscrição ja foi Validada por outro utilizador", "warning", null, null, 0);
                                        t.rollback();
                                        // winAddInscricaop.detach();
                                        winAddMotivo.getParent().detach();
                                        winAddMotivo.detach();
                                        return;
                                    }
                                    Estudante e = i.getIdEstudante();
                                    if (i.getModoInscricao().intValue() == 7) {
                                        if (e.getTransferido() != null && e.getTransferido().intValue() == 1) {
                                            e.setTransferido(new Short("0"));
                                        }
                                        if (e.getMudancac() != null && e.getMudancac().intValue() == 1) {
                                            e.setMudancac(new Short("0"));
                                        }
                                        csimpm.updates(e);
                                    }
                                    Notificacao n = new Notificacao(new Date());
                                    n.setMsg(txMotivoR.getText());
                                    n.setIdFuncionario(i.getFuncionario());
                                    csimpm.Saves(n);
                                    eq = EventQueues.lookup("rinscD" + f.getIdFaculdade(), EventQueues.APPLICATION, true);
                                    eq.publish(new Event("onPedidoMatD", null, i));
                                    eq = EventQueues.lookup("rinscD" + i.getIdEstudante().getIdEstudante(), EventQueues.APPLICATION, true);
                                    eq.publish(new Event("onPedidoMatD", null, i));
                                    if (i.getGrupo() != null) {
                                        List<Inscricao> li;
                                        if (i.getModoInscricao().intValue() == 7) {
                                            par.clear();
                                            par.put("e", e);
                                            par.put("d", i.getDataInscricao());
                                            li = csimpm.findByJPQuery("from Inscricao i where i.idEstudante = :e and i.dataInscricao >= :d", par);
                                            e.setNivelFrequencia(i.getGrupo().getNiveli().intValue());
                                        } else {
                                            li = i.getGrupo().getInscricaoList();
                                        }
                                        final Iterator<Inscricao> items = li.iterator();
                                        Inscricao ii;
                                        while (items.hasNext()) {
                                            ii = items.next();
                                            par.clear();
                                            par.put("insc", ii);
                                            Inscricaodisciplina ld = csimpm.findEntByJPQuery("from Inscricaodisciplina id"
                                                    + " where id.inscricao = :insc", par);
                                            ultimaInscricao(ld);
                                            csimpm.deletes(ii);
                                        }
                                    } else {
                                        par.clear();
                                        par.put("insc", i);
                                        List<Inscricaodisciplina> idis = csimpm.findByJPQuery("from Inscricaodisciplina id"
                                                + " where id.inscricao = :insc", par);
                                        final Iterator<Inscricaodisciplina> items = idis.iterator();
                                        Inscricaodisciplina idisc;
                                        while (items.hasNext()) {
                                            idisc = items.next();
                                            ultimaInscricao(idisc);
                                        }
                                        csimpm.deletes(i);
                                    }
                                    Clients.showNotification("Iscrição Rejeitada com Sucesso", null, null, null, 2000);
                                    t.commit();
                                    while (!t.wasCommitted())
            ;
                                } catch (RuntimeException e) {
                                    t.rollback();
                                    Clients.showNotification("Erro", e.getMessage(), null, null, 2000);
                                    return;
                                }
                                if (i.getModoInscricao().intValue() != 0) {
                                    String tipopedido = "";
                                    if (i.getModoInscricao().intValue() == 1) {
                                        tipopedido = "g4";
                                    } else {
                                        tipopedido = "g7";
                                    }
                                    ValidacaopendentePK vpk = new ValidacaopendentePK(f.getIdFaculdade(), tipopedido);
                                    Validacaopendente vp = csimpm.getLocked(Validacaopendente.class, vpk);
                                    if (vp != null) {
                                        if ((vp.getQtd() - 1) < 0) {
                                            vp.setQtd(0);
                                        } else {
                                            vp.setQtd(vp.getQtd() - 1);
                                        }
                                        csimpm.updates(vp);
                                    } else {
                                        vp = new Validacaopendente();
                                        vp.setValidacaopendentePK(vpk);
                                        vp.setFaculdade(f);
                                        vp.setQtd(0);
                                        csimpm.Saves(vp);
                                    }
                                    eq = EventQueues.lookup("valid" + f.getIdFaculdade(), EventQueues.APPLICATION, true);
                                    eq.publish(new Event("onPedidoMatD", null, vp));
                                }//

                                if (i.getModoInscricao().intValue() == 7) {
                                    Listbox lb = (Listbox) winAddMotivo.getParent().getParent().getFellow("lbinscricaop");
                                    limpaLB(lb);
                                } else {
                                    //  ((ListModelList) ((Listbox) winAddMotivo.getParent().getParent().getFellow("lbinscricaop")).getModel()).remove(i);
//                                    new Listbox().appendChild(((Listbox) winAddMotivo.getParent().getParent().getFellow("lbinscricaop"))
//                                            .getItemAtIndex(((Intbox) winAddMotivo.getParent().getFellow("ibitem")).getValue()));
                                }
                                // winAddInscricao.detach();
                                winAddMotivo.getParent().detach();
                                winAddMotivo.detach();
                                break;

                            case Messagebox.NO:
                                return;
                        }
                    }
                });
    }

    //Pesquisar estudante
    public ListModel<Curso> getListaCursoModel() {
        //Users u = csimpm.get(Users.class, usr.getUtilizador());
        par.clear();
        Faculdade f = csimpm.get(Faculdade.class, usr.getFaculdade().getIdFaculdade());
        par.put("fac", f);
        List<Curso> lc = csimpm.findByJPQuery("from Curso c where c.faculdade = :fac", par);
        return listaCursoModel = new ListModelList<Curso>(lc);
    }

    public void verificarPrescricao(Inscricaodisciplina i) {
        if (i.getNotaFinal() < 10) {
            //Verificar se tinha reprovado antes
            par.clear();
            par.put("d", i.getDisciplina());
            par.put("e", i.getInscricao().getIdEstudante());
            par.put("dat", i.getInscricao().getDataInscricao());
            Inscricaodisciplina oldID = csimpm.findEntByJPQuery("select k from Inscricaodisciplina k where k.disciplina = :d and k.inscricao.dataInscricao in (select max(id.inscricao.dataInscricao) "
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
                csimpm.updates(pre);
            }
        }
    }

    public void ultimaInscricao(Inscricaodisciplina i) {
        par.clear();
        par.put("d", i.getDisciplina());
        par.put("e", i.getInscricao().getIdEstudante());
        par.put("dat", i.getInscricao().getDataInscricao());
        Inscricaodisciplina oldID = csimpm.findEntByJPQuery("select k from Inscricaodisciplina k where k.disciplina = :d and k.inscricao.dataInscricao in (select max(id.inscricao.dataInscricao) "
                + "from Inscricaodisciplina id where id.estado is true and id.disciplina =:d and id.inscricao.idEstudante = :e and id.disciplinaActiva = 2 and"
                + " id.inscricao.dataInscricao < :dat)", par);
        if (oldID != null) {
            oldID.setDisciplinaActiva(new Short("3"));
            csimpm.updates(oldID);
        }
    }
}
