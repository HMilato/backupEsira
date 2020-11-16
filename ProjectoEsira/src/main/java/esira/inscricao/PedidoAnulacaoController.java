/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package esira.inscricao;

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
import esira.domain.PlanificacaoAnoLectivo;
import esira.domain.Prescricao;
import esira.domain.Users;
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
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Iframe;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
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
public class PedidoAnulacaoController extends GenericForwardComposer {

    @WireVariable
    private CRUDService csimpm = (CRUDService) SpringUtil.getBean("CRUDService");
    private Window winVAnulacao, winPAnularInsc, winVAnulacaoDisc, winPAnularInscEst, win, winAddMotivo;
    private Combobox combEstInsc, combSem;
    private ListModel<Estudante> estudantes;
    private ListModel<Inscricao> inscric;
    private ListModel<Disciplina> disciplinas;
    private ListModel<Curso> listaCursoModel;
    private Label lbcurso, lbnivel, lbsexo, lbcred, lbtaxaInscricao, lbtaxaInscricaoE, lbano, lbNumero,
            lbtaxaMulta15dias, lbtaxaMulta30dias, lbtotal, lbsem;
    private Listbox discSel, lbpanularInsc, lbdiscInsc, lbimg, lbimg1, lbd;
    private Tab tabPagamentoTaxa, tabDadosInscricao;
    private Tabs tabs;
    private Row rwTaxanM, rwTaxaeM, rwTaxamulta15, rwTaxamulta30;
    private Button addInsc, btnCancDisc, btnAnular;
    Textbox txProcurar, txProcurarNrmec, txnrTalao, txx, tbimg4, txMotivo, txMotivoR;
    Combobox cbcurso, cbCursoRel, cbDiscRel;
    List listaI;
    Datebox dinsc;
    private static Media mediaCPI;
    private int ano;
    private int sem = 0;
    private int n;
    private Intbox ibitem, ibidInsc, idanul, ida, idest, idlitem;
    Map<String, Object> par = new HashMap<String, Object>();
    Users usr = (Users) Sessions.getCurrent().getAttribute("user");
    private EventQueue eq;

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
    }

    public void onSetQueueA() {
        //Users u = csimpm.get(Users.class, usr.getUtilizador());
        eq = EventQueues.lookup("pinscA" + usr.getFaculdade().getIdFaculdade(), EventQueues.APPLICATION, true);
        eq.subscribe(getEventoA());
        eq = EventQueues.lookup("rinscA" + usr.getFaculdade().getIdFaculdade(), EventQueues.APPLICATION, true);
        eq.subscribe(getEventoRA());
    }

    public EventListener getEventoA() {
        return new EventListener() {
            @Override
            public void onEvent(Event event) throws Exception {
                Inscricao i = (Inscricao) event.getData();
                i = csimpm.load(Inscricao.class, i.getIdInscricao());
                Users u = csimpm.get(Users.class, usr.getUtilizador());
                if (winPAnularInsc.getParent() != null) {// && u.getFaculdade().getIdFaculdade() == i.getIdEstudante().getCursocurrente().getFaculdade().getIdFaculdade()) {
                    lbpanularInsc.setRows(lbpanularInsc.getItemCount() + 2);
                    ((ListModelList) lbpanularInsc.getModel()).remove(i);
                    ((ListModelList) lbpanularInsc.getModel()).add(0, i);
                    // Messagebox.show("Entrou");
                }
            }
        };
    }

    public EventListener getEventoRA() {
        return new EventListener() {
            @Override
            public void onEvent(Event event) throws Exception {
                Inscricao i = (Inscricao) event.getData();
                i = csimpm.load(Inscricao.class, i.getIdInscricao());
                Users u = csimpm.get(Users.class, usr.getUtilizador());
                if (winPAnularInsc.getParent() != null) {
                    ((ListModelList) lbpanularInsc.getModel()).remove(i);
                }
            }
        };
    }

    public ListModel<Disciplinaanulada> getPedidoModel() {
        //Users u = csimpm.get(Users.class, usr.getUtilizador());
        par.clear();
        Faculdade f = csimpm.get(Faculdade.class, usr.getFaculdade().getIdFaculdade());
        par.put("fac", f);
        List<Disciplinaanulada> lda = csimpm.findByJPQuery("from Disciplinaanulada da where da.dataconfirmacao is null and da.idEstudante.cursocurrente.faculdade = :fac", par);
        return new ListModelList<Disciplinaanulada>(lda);
    }

    public void onValidar(ForwardEvent evt) throws FileNotFoundException, IOException {
        Button btn = (Button) evt.getOrigin().getTarget();
        Listitem litem = (Listitem) btn.getParent().getParent();
        Disciplinaanulada da = (Disciplinaanulada) litem.getValue();
        da = csimpm.load(Disciplinaanulada.class, da.getIdanulacao());
        winVAnulacao.setParent(winPAnularInsc);
        winVAnulacao.doModal();
        ((Label) winVAnulacao.getFellow("lbNumero")).setValue(da.getIdEstudante().getNrEstudante());
        ((Label) winVAnulacao.getFellow("lbNome")).setValue(da.getIdEstudante().getNomeCompleto());
        ((Intbox) winVAnulacao.getFellow("idest")).setValue(da.getIdEstudante().getIdEstudante().intValue());
        ((Intbox) winVAnulacao.getFellow("idanul")).setValue(da.getIdanulacao());
        ((Intbox) winVAnulacao.getFellow("idlitem")).setValue(litem.getIndex());
        ((Textbox) winVAnulacao.getFellow("txMotivo")).setValue(da.getMotivo());
        Listbox lbo = ((Listbox) winVAnulacao.getFellow("lbd"));
        for (Inscricaodisciplina id : da.getInscricaodisciplinaList()) {
            Disciplina d = id.getDisciplina();
            lbo.appendChild(new Listitem(d.getNome() + " - " + d.getNivel() + "º Ano - " + d.getSemestre() + "º Sem - " + d.getCredito() + " Credito", d));
        }
    }

    public void onVerRequerimento(Event evt) throws FileNotFoundException, IOException {
        Disciplinaanulada da = csimpm.load(Disciplinaanulada.class, idanul.getValue());
        if (da.getDoc() == null) {
            Clients.showNotification("Não existe documento", "warning", null, null, 2000);
            return;
        }
        String nre = da.getIdEstudante().getNrEstudante();
        String dat = new SimpleDateFormat("dd_MM_yyyy_HH_mm_ss").format(da.getDataanulacao());
        String sss = UserAutentic.getPathR("6") + nre + dat;
        //File f = new File(ss);
        InputStream iss = new FileInputStream(sss);
        Media pp = new AMedia(sss, FilenameUtils.getExtension(sss), URLConnection.guessContentTypeFromName(sss), iss);
        Media mm = new AMedia(sss, FilenameUtils.getExtension(sss), URLConnection.guessContentTypeFromName(sss), pp.getByteData());
        Window win = (Window) Executions.createComponents("/report.zul", null, null);
        win.setTitle("Requerimento/Anulação de Inscrição");
        Iframe iframe = (Iframe) win.getFellow("report");
        iframe.setContent(mm);
    }

    public void onClick$btnCnf() {
        Messagebox.show("Pretende validar este Pedido de Anulação de Inscrição?", "", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION,
                new EventListener() {
                    @Override
                    public void onEvent(Event evet) throws InterruptedException {
                        switch (((Integer) evet.getData()).intValue()) {
                            case Messagebox.YES:
                                Disciplinaanulada da = csimpm.load(Disciplinaanulada.class, idanul.getValue());
                                da.setEstado(true);
                                da.setDataconfirmacao(new Date());
                                Users u = csimpm.get(Users.class, usr.getUtilizador());
                                Faculdade f = csimpm.get(Faculdade.class, usr.getFaculdade().getIdFaculdade());
                                da.setFuncionario(u.getIdFuncionario());
                                final Iterator<Inscricaodisciplina> items2 = da.getInscricaodisciplinaList().listIterator();
                                Inscricaodisciplina idis = null;
                                while (items2.hasNext()) {
                                    idis = (Inscricaodisciplina) items2.next();
                                    idis.setEstado(false);
                                    idis.setAnulacao(da);
                                    csimpm.update(idis);
                                }
                                Clients.showNotification("A Anulação de Inscrição foi validada com Sucesso", null, null, null, 2000);
//                                new Listbox().appendChild(((Listbox) winVAnulacao.getParent().getFellow("lbpanularInsc"))
//                                        .getItemAtIndex(idlitem.getValue()));
                                eq = EventQueues.lookup("rinscA" + f.getIdFaculdade(), EventQueues.APPLICATION, true);
                                eq.publish(new Event("onPedidoMatD", null, da));
                                eq = EventQueues.lookup("rinscA" + da.getIdEstudante().getIdEstudante(), EventQueues.APPLICATION, true);
                                eq.publish(new Event("onPedidoMatD", null, da));
                                winVAnulacao.detach();
                                break;
                            case Messagebox.NO:
                                return;
                        }
                    }
                });

    }

    public void onRejeitar(ForwardEvent event) throws IOException {
        winAddMotivo.setParent(winVAnulacao);
        winAddMotivo.doModal();
        ((Intbox) winAddMotivo.getFellow("ibidInsc")).setValue(idanul.getValue());
        ((Textbox) winAddMotivo.getFellow("txMotivoR")).setText("O Seu Pedido de Anulação de Inscrição foi rejeitado.\n Os seus dados estão incorrectos");

    }

    public void onBtnC() {
        winAddMotivo.detach();
    }

    public void onBtncancelar() {
        winVAnulacao.detach();
    }

    public void onBtnRjt() {
        Messagebox.show("Pretende Rejeitar este Pedido de Anulação de Inscrição?", "", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION,
                new EventListener() {
                    @Override
                    public void onEvent(Event evet) throws InterruptedException, IOException, EmailException {
                        switch (((Integer) evet.getData()).intValue()) {
                            case Messagebox.YES:
                                Disciplinaanulada da = csimpm.get(Disciplinaanulada.class, ibidInsc.getValue());
                                Users u = csimpm.get(Users.class, usr.getUtilizador());
                                Faculdade f = csimpm.get(Faculdade.class, usr.getFaculdade().getIdFaculdade());
                                da.setFuncionario(u.getIdFuncionario());
                                da.setDataconfirmacao(new Date());
                                da.setEstado(false);
                                da.setMotivo(txMotivoR.getValue());
                                csimpm.update(da);
                                Clients.showNotification("Pedido de Anulação de Inscrição Rejeitado com Sucesso", null, null, null, 2000);
//                                new Listbox().appendChild(((Listbox) winAddMotivo.getParent().getParent().getFellow("lbpanularInsc"))
//                                        .getItemAtIndex(((Intbox) winAddMotivo.getParent().getFellow("idlitem")).getValue()));
                                eq = EventQueues.lookup("rinscA" + f.getIdFaculdade(), EventQueues.APPLICATION, true);
                                eq.publish(new Event("onPedidoMatD", null, da));
                                eq = EventQueues.lookup("rinscA" + da.getIdEstudante().getIdEstudante(), EventQueues.APPLICATION, true);
                                eq.publish(new Event("onPedidoMatD", null, da));
                                winAddMotivo.getParent().detach();
                                break;
                            case Messagebox.NO:
                                return;
                        }
                    }
                });
    }

}
