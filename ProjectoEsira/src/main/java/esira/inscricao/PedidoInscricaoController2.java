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
public class PedidoInscricaoController2 extends GenericForwardComposer {

    @WireVariable
    private CRUDService csimpm = (CRUDService) SpringUtil.getBean("CRUDService");
    private Window winInscricaoP, winAddInscricaoDisc, winInscricaoPEst, winAddInscricaop, winAddEqui, winAddInsc, winAddInscDisc, win, winAddMotivo;
    private Combobox combEstInsc, combSem;
    private ListModel<Estudante> estudantes;
    private ListModel<Inscricao> inscric;
    private ListModel<Disciplina> disciplinas;
    private ListModel<Curso> listaCursoModel;
    private Label lbcurso, lbnivel, lbsexo, lbcred, lbtaxaInscricao, lbtaxaInscricaoE, lbano, lbNumero,
            lbtaxaMulta15dias, lbtaxaMulta30dias, lbtotal, lbsem;
    private Listbox discSel, lbinscricaop, lbdiscInsc, lbimg, lbimg1;
    private Tab tabPagamentoTaxa, tabDadosInscricao;
    private Tabs tabs;
    private Row rwTaxanM, rwTaxaeM, rwTaxamulta15, rwTaxamulta30;
    private Button addInsc, btnCancDisc, btnAnular;
    Textbox txnrTalao, txx, tbimg4, txMotivo, txMotivoR, txdisc;
    Combobox cbCursoRel, cbDiscRel;
    List listaI;
    Datebox dinsc;
    private static Media mediaCPI;
    private int ano;
    private int sem = 0;
    private int n;
    private Intbox ibitem, ibidInsc, ibmulta, versao;
    Map<String, Object> par = new HashMap<String, Object>();
    Users usr = (Users) Sessions.getCurrent().getAttribute("user");
    private Hlayout ahead;
    String condfac = "", condnr = "", condnome = "", condgenero = "", condanoi = "", condano = "", condcurso = "";
    Textbox txProcurar, txProcNrmec;
    Map<String, Object> condpar = new HashMap<String, Object>();
    Combobox cbcurso;
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
        if (cbCursoRel != null && cbCursoRel.getItems() != null) {
            cbCursoRel.setSelectedIndex(-1);
        }
        if (cbDiscRel != null && cbDiscRel.getItems() != null) {
            cbDiscRel.setSelectedIndex(-1);
        }
        // if(winInscricaoP.isVisible()==false&&winAddInscricao!=null&&winAddInscricao.getParent().getId().equals("winInscricaoP")){
        // addInsc();
        //}
        setLB(0, 20);
    }
//    public void onModal$winAddInscDisc(){
//        Messagebox.show("fsdf"); 
//       win = winAddInscDisc;
//    }

    public void onSetQueuePI() {
        //Users u = csimpm.get(Users.class, usr.getUtilizador());
        eq = EventQueues.lookup("pinscD" + usr.getFaculdade().getIdFaculdade(), EventQueues.APPLICATION, true);
        eq.subscribe(getEvento());
        eq = EventQueues.lookup("rinscD" + usr.getFaculdade().getIdFaculdade(), EventQueues.APPLICATION, true);
        eq.subscribe(getEvento2());
        eq = EventQueues.lookup("rinscRD" + usr.getFaculdade().getIdFaculdade(), EventQueues.APPLICATION, true);
        eq.subscribe(getEvento2());
    }

    public EventListener getEvento() {
        return new EventListener() {
            @Override
            public void onEvent(Event event) throws Exception {
                Inscricao i = (Inscricao) event.getData();
                i = csimpm.load(Inscricao.class, i.getIdInscricao());
                if (i.getModoInscricao().intValue() < 3) {
                    //    Messagebox.show("1");
                    return;
                }
                // Users u = csimpm.get(Users.class, usr.getUtilizador());
                if (winInscricaoP.getParent() != null) {//&& u.getFaculdade().getIdFaculdade() == i.getIdEstudante().getCursocurrente().getFaculdade().getIdFaculdade()) {
                    //  Messagebox.show("2");
                    lbinscricaop.setRows(lbinscricaop.getItemCount() + 2);
                    ((ListModelList) lbinscricaop.getModel()).add(0, i);
                    // Messagebox.show("Entrou");
                }
            }

        };

    }

    public EventListener getEvento2() {
        return new EventListener() {
            @Override
            public void onEvent(Event event) throws Exception {
                Inscricao i = (Inscricao) event.getData();
                // i = csimpm.load(Inscricao.class, i.getIdInscricao());
                if (i.getModoInscricao().intValue() < 3) {
                    //    Messagebox.show("1");
                    return;
                }
                //  Users u = csimpm.get(Users.class, usr.getUtilizador());
                if (winInscricaoP.getParent() != null) {

//                   Long id = i.getIdInscricao();
//                    final Iterator<Listitem> items =  lbinscricaop.getItems().iterator();
//                    Inscricao in;
//                    while (items.hasNext()) {
//                        Listitem li = items.next();
//                        in = (Inscricao)li.getValue();
//                        if(in.getIdInscricao()==id)
//                        new Listbox().appendChild(li);
//                    }
                    //((ListModelList)lbinscricaop.getModel()).remove(i);
                    int ind = ((ListModelList) lbinscricaop.getModel()).indexOf(i);
                    // Messagebox.show(ind+"");
                    if (ind >= 0) {
                        ((ListModelList) lbinscricaop.getModel()).remove(ind);
                    }
                    //  lbinscricaop.invalidate();
                }
            }
        };
    }

    public void onModal$winInscricaoP() {
        Long ides = Long.parseLong(txx.getText());
        Estudante e = csimpm.get(Estudante.class, ides);
        List<Inscricao> li = null;
        par.clear();
        par.put("e", e);
        li = csimpm.findByJPQuery("from Inscricao i where i.idEstudante = :e and i.dataConfirmacao is null", par);
        lbinscricaop.setModel(new ListModelList<Inscricao>(li));
    }

    public void onClose$winInscricaoP(Event evt) {
        if (((Window) winInscricaoP.getParent()).inModal()) {
            winInscricaoP.getParent().detach();
        }
    }

//    public void onCreate$winInscricaoP(Event evt) {
//        if (winInscricaoP.inModal()) {
//            winInscricaoP.setMode("modal");
//            winInscricaoP.setTitle("Validação de Inscrições");
//            winInscricaoP.setClosable(true);
//            winInscricaoP.setVflex("1");
//            winInscricaoP.setHflex("1");
//            winInscricaoP.setWidth("80%");
//            // ahead.setVisible(false);
//            Long ides = Long.parseLong(txx.getText());
//            Estudante e = csimpm.get(Estudante.class, ides);
//            List<Inscricao> li = null;
//            par.clear();
//            par.put("e", e);
//            li = csimpm.findByJPQuery("from Inscricao i where i.idEstudante = :e and i.dataConfirmacao is null", par);
//            lbinscricaop.setModel(new ListModelList<Inscricao>(li));
//        }
//    }
    public ListModel<Estudante> getEstInscModel() {
        Date dano = new Date();
        Calendar cal = new GregorianCalendar();
        cal.setTime(dano);
        int ano = cal.get(Calendar.YEAR);
        par.clear();
        par.put("user", usr.getUtilizador());
       // Users u = csimpm.findEntByJPQuery("from Users u where u.utilizador = :user", par);
        Faculdade f = csimpm.get(Faculdade.class, usr.getFaculdade().getIdFaculdade());
        par.clear();
        par.put("ano", ano);
        par.put("fac", f);
        List<Estudante> estu = csimpm.findByJPQuery("from Estudante e where e.cursocurrente.faculdade = :fac and e.idEstudante in (select m.estudante from Matricula m where "
                + "extract(year from m.dataMatricula) = :ano and m.estudante not in (select ma.matriculaPK.idEstudante from Matriculaanulada ma where ma.matriculaPK.ano = :ano))", par);
        return new ListModelList<Estudante>(estu);
    }

    public void onValidar(ForwardEvent evt) throws FileNotFoundException, IOException {
        Button btn = (Button) evt.getOrigin().getTarget();
        Listitem litem = (Listitem) btn.getParent().getParent();
        Inscricao insc = (Inscricao) litem.getValue();
        insc = csimpm.load(Inscricao.class, insc.getIdInscricao());
        int index = litem.getIndex();
        int ve = -1;
        if (insc.getVersion() != null) {
            ve = insc.getVersion().intValue();
        }
        if (insc.getModoInscricao().intValue() == 8) {
            //Window w = (Window) Executions.createComponents("/inscricao/ValidarInscDisc.zul", winInscricaoP, null);
            winAddInscDisc.setParent(winInscricaoP);
            winAddInscDisc.doModal();
            Window w = winAddInscDisc;
            par.clear();
            par.put("insc", insc);
            Disciplina d = csimpm.findEntByJPQuery("select id.disciplina from Inscricaodisciplina id"
                    + " where id.inscricao = :insc", par);
            Calendar cal = new GregorianCalendar();
            cal.setTime(insc.getDataInscricao());
            int ano = cal.get(Calendar.YEAR);
            ((Intbox) w.getFellow("ibidInsci")).setValue(insc.getIdInscricao().intValue());
            ((Intbox) w.getFellow("ibitemi")).setValue(index);
            ((Textbox) w.getFellow("txtdisci")).setValue(d.getNome());
            ((Intbox) w.getFellow("versaoi")).setValue(ve);
            ((Label) w.getFellow("lbanoi")).setValue(ano + "");
            ((Label) w.getFellow("lbsemi")).setValue("Semestre " + insc.getSemestre().toString());
            ((Label) w.getFellow("lbcursoi")).setValue(d.getCurso().getDescricao());
            ((Label) w.getFellow("lbniveli")).setValue(d.getNivel() + "");
            ((Textbox) w.getFellow("txMoti")).setValue(insc.getGrupo().getDescricao());
            Listbox discs = ((Listbox) w.getFellow("discSeli"));
            limpaLB(discs);
            List<Disciplina> lds = null;
            List<Inscricao> li = insc.getGrupo().getInscricaoList();
            final Iterator<Inscricao> items = li.iterator();
            Inscricao i;
            while (items.hasNext()) {
                i = items.next();
                String nota = "";
                if (i.getInscricaodisciplinaList().get(0).getNotaFinal() != null) {
                    nota = i.getInscricaodisciplinaList().get(0).getNotaFinal() + "";
                }
                Listitem list = new Listitem(i.getIdEstudante().getNomeCompleto() + " - " + i.getIdEstudante().getNivelFrequencia() + "º Ano" + " Nota Final: " + nota, i);
                discs.appendChild(list);
            }
        } else if (insc.getModoInscricao().intValue() == 7) {
            // Window w = (Window) Executions.createComponents("/inscricao/ValidarEquiv.zul", winInscricaoP, null);
            winAddEqui.setParent(winInscricaoP);
            winAddEqui.doModal();
            Window w = winAddEqui;
            win = w;
            Estudante e = insc.getIdEstudante();
            Calendar cal = new GregorianCalendar();
            cal.setTime(insc.getDataInscricao());
            int ano = cal.get(Calendar.YEAR);
            ((Intbox) w.getFellow("ibidInsc")).setValue(insc.getIdInscricao().intValue());
            ((Intbox) w.getFellow("versao")).setValue(ve);
            ((Intbox) w.getFellow("ibitem")).setValue(index);
            ((Textbox) w.getFellow("txtestudante")).setValue(e.getNomeCompleto());
            ((Label) w.getFellow("lbcurso")).setValue(e.getCursocurrente().getDescricao());
            ((Label) w.getFellow("lbnivel")).setValue(e.getNivelFrequencia() + "");
            Listbox discs = ((Listbox) w.getFellow("discSel"));
            List<Inscricaodisciplina> lds = new ArrayList<Inscricaodisciplina>();
            List<Inscricaodisciplina> ld = new ArrayList<Inscricaodisciplina>();
            List<Inscricao> li = insc.getGrupo().getInscricaoList();
            final Iterator<Inscricao> items = li.iterator();
            Inscricao i;
            par.clear();
            par.put("insc", "");
            while (items.hasNext()) {
                i = items.next();
                par.replace("insc", i);
                ld = csimpm.findByJPQuery("from Inscricaodisciplina id"
                        + " where id.inscricao = :insc", par);
                lds.addAll(ld);
            }
            for (Inscricaodisciplina id : lds) {
                Disciplina di = id.getDisciplina();
                Listitem list = new Listitem(di.getNome() + " - " + di.getNivel() + "º Ano - " + di.getSemestre() + "º Sem - " + id.getNotaFinal() + " Valores", di);
                discs.appendChild(list);
            }
        } else if (insc.getModoInscricao().intValue() == 9) {
//            Window w = (Window) Executions.createComponents("/inscricao/ValidarInscEstu.zul", winInscricaoP, null);
            winAddInscricaop.setParent(winInscricaoP);
            winAddInscricaop.doModal();
            Window w = winAddInscricaop;
            Calendar cal = new GregorianCalendar();
            cal.setTime(insc.getDataInscricao());
            int ano = cal.get(Calendar.YEAR);
            ((Intbox) w.getFellow("ibidInscp")).setValue(insc.getIdInscricao().intValue());
            ((Intbox) w.getFellow("ibitemp")).setValue(index);
            ((Intbox) w.getFellow("versaop")).setValue(ve);
            ((Label) w.getFellow("lbanop")).setValue(ano + "");
            ((Label) w.getFellow("lbsemp")).setValue("Semestre " + insc.getSemestre().toString());
            ((Label) w.getFellow("lbnivelp")).setValue(insc.getIdEstudante().getNivelFrequencia() + "");
            ((Label) w.getFellow("lbcursop")).setValue(insc.getIdEstudante().getCursocurrente() + "");
            ((Textbox) w.getFellow("txMotp")).setValue(insc.getGrupo().getDescricao());
            if (insc.getIdEstudante().getMasculino()) {
                ((Label) w.getFellow("lbsexop")).setValue("Masculino");//csimpm.findByQuery("select s.descricao from fecn1.sexo s, fecn1.estudante e where s.id_sexo = e.sexo "
            } else {
                ((Label) w.getFellow("lbsexop")).setValue("Femenino");
            }
            sem = insc.getSemestre().intValue();
            // List<Disciplina> lds = null;
            List<Inscricaodisciplina> lids = null;
            par.clear();
            par.put("insc", insc);
//            lds = csimpm.findByJPQuery("select id.disciplina from Inscricaodisciplina id"
//                    + " where id.inscricao = :insc", par);
            lids = csimpm.findByJPQuery("from Inscricaodisciplina id"
                    + " where id.inscricao = :insc", par);
            int i = 0;
            for (Inscricaodisciplina d : lids) {
                i = i + d.getDisciplina().getCredito();
            }
            ((Label) w.getFellow("lbcredp")).setValue(i + "");
            Listbox discs = ((Listbox) w.getFellow("discSelp"));
            limpaLB(discs);
            for (Inscricaodisciplina d : lids) {
                // Listitem list = new Listitem(d.getNome() + " - " + d.getNivel() + "º Ano - " + d.getSemestre() + "º Sem - " + d.getCredito() + " Credito", d);
                Listitem list = new Listitem(d.getDisciplina().getNome() + " - " + d.getDisciplina().getNivel() + "º Ano - " + d.getDisciplina().getSemestre() + "º Sem - " + d.getDisciplina().getCredito() + "Nota Final: " + d.getNotaFinal() + " Credito", d);
////            list.setDisabled(true);
////            list.setCheckable(false);
////            discs.setMultiple(false);
                discs.appendChild(list);
//            discs.setMultiple(true);
            }
            //Recibos
//            List<Arquivoinscricao> lam3 = insc.getArquivoinscricaoList();
//            List<Arquivo> lm3 = new ArrayList<Arquivo>();
//            String dat = new SimpleDateFormat("dd_MM_yyyy_HH_mm_ss").format(insc.getDataInscricao());
//            for (Arquivoinscricao am3 : lam3) {
//                String sss = UserAutentic.getPathR("4") + insc.getIdEstudante().getNrEstudante() + am3.getNome() + dat;
//                //File f = new File(ss);
//                InputStream iss = new FileInputStream(sss);
//                Media pp = new AMedia(sss, FilenameUtils.getExtension(sss), URLConnection.guessContentTypeFromName(sss), iss);
//                Media mm = new AMedia(sss, FilenameUtils.getExtension(sss), URLConnection.guessContentTypeFromName(sss), pp.getByteData());
//                ((Textbox) w.getFellow("tbimg4")).setValue(mm.getName());
//                lm3.add(new Arquivo(am3.getNrtalao(), mm, mm.getName()));
//            }
            Estudante e = insc.getIdEstudante();
            List<Estudante> lc = new ArrayList<Estudante>();
            lc.add(e);
            Combobox cbe = (Combobox) w.getFellow("combEstInscp");
            cbe.setModel(new ListModelList<Estudante>(lc));
            cbe.setReadonly(true);
            cbe.setButtonVisible(false);
        } else {
            // Window w = (Window) Executions.createComponents("/inscricao/ValidarInsc.zul", winInscricaoP, null);
            winAddInsc.setParent(winInscricaoP);
            winAddInsc.doModal();
            Window w = winAddInsc;
            win = w;
            ((Tab) w.getFellow("tabDadosInscricao")).setSelected(true);
            Calendar cal = new GregorianCalendar();
            cal.setTime(insc.getDataInscricao());
            int ano = cal.get(Calendar.YEAR);
            ((Intbox) w.getFellow("ibidInscd")).setValue(insc.getIdInscricao().intValue());
            ((Intbox) w.getFellow("ibitemd")).setValue(index);
            ((Intbox) w.getFellow("versaod")).setValue(ve);
            ((Label) w.getFellow("lbanod")).setValue(ano + "");
            ((Label) w.getFellow("lbsemd")).setValue("Semestre " + insc.getSemestre().toString());
            ((Label) w.getFellow("lbniveld")).setValue(insc.getIdEstudante().getNivelFrequencia() + "");
            ((Label) w.getFellow("lbcursod")).setValue(insc.getIdEstudante().getCursocurrente() + "");
            if (insc.getModoInscricao().intValue() == 3) {
                ((Intbox) w.getFellow("ibmulta")).setValue(insc.getMulta().intValue());
                ((Label) w.getFellow("lbtotald")).setValue(insc.getTaxaInscricao() + "");
                ((Textbox) w.getFellow("txMotd")).setValue(insc.getObs());

            } else {
                ((Intbox) w.getFellow("ibmulta")).setValue(0);
                ((Label) w.getFellow("lbtotald")).setValue(0 + "");
                ((Textbox) w.getFellow("txMotd")).setValue(insc.getObs());
            }
            if (insc.getIdEstudante().getMasculino()) {
                ((Label) w.getFellow("lbsexod")).setValue("Masculino");//csimpm.findByQuery("select s.descricao from fecn1.sexo s, fecn1.estudante e where s.id_sexo = e.sexo "
            } else {
                ((Label) w.getFellow("lbsexod")).setValue("Femenino");
            }
            sem = insc.getSemestre().intValue();
            List<Disciplina> lds = null;
            par.clear();
            par.put("insc", insc);
            lds = csimpm.findByJPQuery("from Disciplina d where d in (select id.disciplina from Inscricaodisciplina id"
                    + " where id.inscricao = :insc)", par);
            int i = 0;
            for (Disciplina d : lds) {
                i = i + d.getCredito();
            }
            ((Label) w.getFellow("lbcredd")).setValue(i + "");
            Listbox discs = ((Listbox) w.getFellow("discSeld"));
            limpaLB(discs);
            for (Disciplina d : lds) {
                Listitem list = new Listitem(d.getNome() + " - " + d.getNivel() + "º Ano - " + d.getSemestre() + "º Sem - " + d.getCredito() + " Credito", d);
////            list.setDisabled(true);
////            list.setCheckable(false);
////            discs.setMultiple(false);
                discs.appendChild(list);
//            discs.setMultiple(true);
            }
            //Recibos
            ((Row) w.getFellow("rwRU")).setVisible(false);
            List<Arquivoinscricao> lam3 = insc.getArquivoinscricaoList();
            List<Arquivo> lm3 = new ArrayList<Arquivo>();
            List<Arquivo> lm4 = new ArrayList<Arquivo>();
            String recibousado = "";
            String dat = new SimpleDateFormat("dd_MM_yyyy_HH_mm_ss").format(insc.getDataInscricao());
            for (Arquivoinscricao am3 : lam3) {
                String sss = UserAutentic.getPathR("4") + insc.getIdEstudante().getNrEstudante() + am3.getNome() + dat;
                //File f = new File(ss);
                InputStream iss = new FileInputStream(sss);
                Media pp = new AMedia(sss, FilenameUtils.getExtension(sss), URLConnection.guessContentTypeFromName(sss), iss);
                Media mm = new AMedia(sss, FilenameUtils.getExtension(sss), URLConnection.guessContentTypeFromName(sss), pp.getByteData());
                ((Textbox) w.getFellow("tbimg4")).setValue(mm.getName());
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
            if (!recibousado.equals("")) {
                ((Row) w.getFellow("rwRU")).setVisible(true);
                ((Textbox) w.getFellow("txRU")).setText(recibousado);
            }
            ((Listbox) w.getFellow("lbimg")).setModel(new ListModelList(lm3));
            ((Listbox) w.getFellow("lbimgII")).setModel(new ListModelList(lm4));

            Estudante e = insc.getIdEstudante();
            List<Estudante> lc = new ArrayList<Estudante>();
            lc.add(e);
            Combobox cbe = (Combobox) w.getFellow("combEstInscd");
            cbe.setModel(new ListModelList<Estudante>(lc));
            cbe.setReadonly(true);
            cbe.setButtonVisible(false);
        }

    }

    public void limpForm() {
        //combEstInsc.setValue("Pesquise um Estudante...");
        lbcurso.setValue("");
        lbnivel.setValue("");
        lbsexo.setValue("");
        lbcred.setValue("");
        lbsem.setValue("");
//        discEst.setModel(new ListModelList<Disciplina>(new ArrayList<Disciplina>()));
//        discEstOp.setModel(new ListModelList<Disciplina>(new ArrayList<Disciplina>()));
        lbimg.setModel(new ListModelList<Arquivo>(new ArrayList<Arquivo>()));
        limpaLB(discSel);
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

    public ListModel<Inscricao> getListInscricaoModel() {
        List<Inscricao> li = null;
        par.clear();
        par.put("user", usr.getUtilizador());
        //Users u = csimpm.findEntByJPQuery("from Users u where u.utilizador = :user", par);
        Faculdade f = csimpm.get(Faculdade.class, usr.getFaculdade().getIdFaculdade());
        par.clear();
        par.put("fac", f);
        li = csimpm.findByJPQuery("from Inscricao i where i.idEstudante.cursocurrente.faculdade = :fac and i.dataConfirmacao is null", par);
        return new ListModelList<Inscricao>(li);
    }

    public void onBtncancelar() {
        // limpForm();
        win.detach();
        if (win.hasFellow("tabPagamentoTaxa")) {
            tabPagamentoTaxa.setSelected(false);
            tabDadosInscricao.setSelected(true);
        }
    }

    public ListModel<Estudante> getListEst() {
        Map<String, Object> par = new HashMap<String, Object>();
        Date dano = new Date();
        Calendar cal = new GregorianCalendar();
        cal.setTime(dano);
        int ano = cal.get(Calendar.YEAR);
        par.put("ano", ano);
        List<Estudante> estu = csimpm.findByJPQuery("from Estudante e where e.idEstudante in (select m.estudante from Matricula m where "
                + "extract(year from m.dataMatricula) = :ano and m.estudante not in (select ma.matriculaanuladaPK.idEstudante from Matriculaanulada ma where ma.matriculaanuladaPK.ano = :ano)) "
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

    public void onChange$combEstInsc(Event event) {
        if (combEstInsc.getSelectedItem() == null) {
            return;
        }
        Date dano = new Date();
        Calendar cal = new GregorianCalendar();
        cal.setTime(dano);
        int ano = cal.get(Calendar.YEAR);
        Estudante e = (Estudante) combEstInsc.getSelectedItem().getValue();
        e = csimpm.load(Estudante.class, e.getIdEstudante());
        Map<String, Object> par = new HashMap<String, Object>();
        Map<String, Object> ent = new HashMap<String, Object>();
        par.put("ide", e.getIdEstudante());
        lbcurso.setValue(e.getCursocurrente().getDescricao());//csimpm.findByQuery("select c.descricao from fecn1.curso c, fecn1.estudante e where c.id_curso = e.cursocurrente "
        // + "and e.id_estudante = :ide", null, par).get(0).toString());
        lbnivel.setValue(e.getNivelFrequencia() + "");
        if (e.getMasculino()) {
            lbsexo.setValue("Masculino");//csimpm.findByQuery("select s.descricao from fecn1.sexo s, fecn1.estudante e where s.id_sexo = e.sexo "
        }                //+ "and e.id_estudante = :ide", null, par).get(0).toString());
        if (!e.getMasculino()) {
            lbsexo.setValue("Femenino");
        }
        int nc = e.getNivelFrequencia();
        int c = 0;
        this.n = nc;
        //ultimo ano que fez inscricao
//        par.clear();
//        par.put("ide", e);
//        par.put("ano", ano);
//        List<Inscricao> li = csimpm.findByJPQuery("from Inscricao i where i.idEstudante = :ide"
//                + " and extract(year from i.dataInscricao)<:ano order by i.dataInscricao desc", par);
//        if (li != null && !li.isEmpty()) {
//            cal.setTime(li.get(0).getDataInscricao());
//            int d = cal.get(Calendar.YEAR);
//            par.clear();
//            par.put("ano", d);
//            par.put("ide", e.getIdEstudante());
//            c = Integer.parseInt(csimpm.findByQuery("select count(idi.id_inscricao) from fecn1.inscricaodisciplina idi, fecn1.inscricao i "
//                    + "where idi.estado is true and idi.nota_final < 10 and idi.disciplina_activa = 3 "
//                    + "and idi.id_inscricao = i.id_inscricao and extract(year from i.data_inscricao) = :ano "
//                    + "and i.id_estudante = :ide", null, par).get(0).toString());
//            if (c <= 2) {
//                nc = nc + 1;
//                this.n = nc;
//            }
//        }

//        par.clear();
//        par.put("semestre", sem);
//        par.put("curso", e.getCursocurrente());
//        par.put("nivel", nc);
//        par.put("ide", e.getIdEstudante());
//        ent.clear();
//        ent.put("d", Disciplina.class);
//        List<Disciplina> ldisc = csimpm.findByQuery("select d.* from fecn1.disciplina d "
//                + "where d.semestre = :semestre and d.curso = :curso and d.nivel "
//                + "< :nivel and d.caracter = 1 and d.id_disc not in (select idi.id_disciplina from fecn1.inscricaodisciplina idi, "
//                + "fecn1.inscricao i where idi.id_inscricao = i.id_inscricao and i.id_estudante = :ide) "
//                + "and d.id_disc not in (select p.id_disc from fecn1.precedencia p where "
//                + " p.precedencia not in (select idi.id_disciplina from fecn1.inscricaodisciplina idi, "
//                + "fecn1.inscricao i where idi.id_inscricao = i.id_inscricao and i.id_estudante = :ide "
//                + "and idi.disciplina_activa = 3 and idi.nota_final < 10)) and d.id_disc not in (select idi.id_disciplina from fecn1.inscricaodisciplina idi, "
//                + "fecn1.inscricao i where idi.id_inscricao = i.id_inscricao and i.id_estudante = :ide "
//                + "and idi.disciplina_activa = 3 and idi.nota_final is null)", ent, par);
//
//        List<Disciplina> ld1 = csimpm.findByQuery("select d.* from fecn1.disciplina d "
//                + "where d.semestre = :semestre and d.curso = :curso and d.nivel "
//                + "< :nivel and d.caracter = 1 and d.id_disc in (select idi.id_disciplina from fecn1.inscricaodisciplina idi, "
//                + "fecn1.inscricao i where idi.id_inscricao = i.id_inscricao and i.id_estudante = :ide and "
//                + "idi.disciplina_activa = 3 and idi.nota_final < 10) "
//                + "and d.id_disc not in (select p.id_disc from fecn1.precedencia p where "
//                + " p.precedencia not in (select idi.id_disciplina from fecn1.inscricaodisciplina idi, "
//                + "fecn1.inscricao i where idi.id_inscricao = i.id_inscricao and i.id_estudante = :ide "
//                + "and idi.disciplina_activa = 3 and idi.nota_final < 10)) and d.id_disc not in (select idi.id_disciplina from fecn1.inscricaodisciplina idi, "
//                + "fecn1.inscricao i where idi.id_inscricao = i.id_inscricao and i.id_estudante = :ide "
//                + "and idi.disciplina_activa = 3 and idi.nota_final is null)", ent, par);
//        ldisc.addAll(ld1);
//
//        List<Disciplina> ld2 = null;
//        if (c <= 2) {
//            ld2 = csimpm.findByQuery("select d.* from fecn1.disciplina d "
//                    + "where d.semestre = :semestre and d.curso = :curso and d.nivel = :nivel and d.caracter = 1 "
//                    + "and d.id_disc not in (select p.id_disc from fecn1.precedencia p where "
//                    + " p.precedencia not in (select idi.id_disciplina from fecn1.inscricaodisciplina idi, "
//                    + "fecn1.inscricao i where idi.id_inscricao = i.id_inscricao and i.id_estudante = :ide "
//                    + "and idi.disciplina_activa = 3 and idi.nota_final < 10)) and d.id_disc not in (select idi.id_disciplina from fecn1.inscricaodisciplina idi, "
//                    + "fecn1.inscricao i where idi.id_inscricao = i.id_inscricao and i.id_estudante = :ide "
//                    + "and idi.disciplina_activa = 3 and idi.nota_final is null)", ent, par);
//        } else {
//            ld2 = csimpm.findByQuery("select d.* from fecn1.disciplina d "
//                    + "where d.semestre = :semestre and d.curso = :curso and d.nivel= :nivel  and d.caracter = 1 "
//                    + "and d.nivel = :nivel and d.id_disc in (select idi.id_disciplina from fecn1.inscricaodisciplina idi, "
//                    + "fecn1.inscricao i where idi.id_inscricao = i.id_inscricao and i.id_estudante = :ide and "
//                    + "idi.disciplina_activa = 3 and idi.nota_final < 10) "
//                    + "and d.id_disc not in (select p.id_disc from fecn1.precedencia p where "
//                    + " p.precedencia not in (select idi.id_disciplina from fecn1.inscricaodisciplina idi, "
//                    + "fecn1.inscricao i where idi.id_inscricao = i.id_inscricao and i.id_estudante = :ide "
//                    + "and idi.disciplina_activa = 3 and idi.nota_final < 10)) and d.id_disc not in (select idi.id_disciplina from fecn1.inscricaodisciplina idi, "
//                    + "fecn1.inscricao i where idi.id_inscricao = i.id_inscricao and i.id_estudante = :ide "
//                    + "and idi.disciplina_activa = 3 and idi.nota_final is null)", ent, par);
//        }
//        ldisc.addAll(ld2);
//        discEst.setMultiple(false);
//        discEst.setModel(new ListModelList<Disciplina>(ldisc));
//        discEst.setMultiple(true);
//        limpaLB(discSel);
//        par.clear();
//        par.put("ide", e);
//        par.put("semestre", new Short(sem + ""));
//        par.put("ano", ano);
//        List<Disciplina> lds = csimpm.findByJPQuery("from Disciplina d where d in (select id.disciplina from Inscricaodisciplina"
//                + " id where id.inscricao.idEstudante = :ide and id.inscricao.semestre = :semestre and "
//                + "extract(year from id.inscricao.dataInscricao) = :ano)", par);
//        for (Disciplina d : lds) {
//            Listitem list = new Listitem(d.getNome() + " - " + d.getNivel() + "º Ano - " + d.getSemestre() + "º Sem - " + d.getCredito() + " Credito", d);
//            list.setDisabled(true);
//            list.setCheckable(false);
//            discSel.setMultiple(false);
//            discSel.appendChild(list);
//            discSel.setMultiple(true);
//        }
    }

    public void onClick$btnva() {
        int qda = 0;
        Boolean b = false;
        int a = 0;
        List<Integer> niv = new ArrayList<Integer>();
//        if (discEst.getSelectedItems().isEmpty() && discEstOp.getSelectedItems().isEmpty()) {
//            Clients.showNotification("Seleccione as disciplinas para a inscrição", "error", null, null, 0);
//            return;
//        }
//        if (Integer.parseInt(lbcred.getValue()) > 30) {
//            Clients.showNotification("Ultrapassou o limite de credito", "error", null, null, 0);
//            return;
//        }
//        for (Listitem ide : discEst.getSelectedItems()) {
//            int ni = ((Disciplina) ide.getValue()).getNivel();
//            if (ni < this.n) {
//                qda = qda + 1;
//            }
//            if (a == 0 && ni != this.n) {
//                a = ni;
//            }
//            if (ni != a && ni != this.n) {
//                b = true;
//            }
//        }
//        for (Listitem ide : discEstOp.getSelectedItems()) {
//            int ni = ((Disciplina) ide.getValue()).getNivel();
//            if (ni < this.n) {
//                qda = qda + 1;
//            }
//            if (a == 0 && ni != this.n) {
//                a = ni;
//            }
//            if (ni != a && ni != this.n) {
//                b = true;
//            }
//        }
        for (Listitem ide : discSel.getItems()) {
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
        if (qda > 2) {
            Clients.showNotification("Por favor, seleccione no maximo duas disciplinas dos niveis inferior", "error", null, null, 0);
            return;
        }
        if (b) {
            Clients.showNotification("Por favor, so podera se increver em dois niveis simultaneamente", "error", null, null, 0);
            return;
        }
//        if (discEst.getItemCount() > 0 && discEst.getSelectedCount() > 0) {
//            final Iterator<Listitem> items = new ArrayList(discEst.getSelectedItems()).listIterator();
//            Listitem li;
//            while (items.hasNext()) {
//                li = items.next();
//                discSel.setMultiple(false);
//                li.setLabel(" ");
////                Disciplina di = (Disciplina) li.getValue();
////                di = csimpm.load(Disciplina.class, di.getIdDisc());
////                if (di.getCaracter().getDescricao().equals("Obrigatorio")) {
////                    li.setLabel(" ");
////                }
//                discSel.appendChild(li);
//
//                discSel.setMultiple(true);
//            }
//        }
//        if (discEstOp.getItemCount() > 0 && discEstOp.getSelectedCount() > 0) {
//            final Iterator<Listitem> items = new ArrayList(discEstOp.getSelectedItems()).listIterator();
//            Listitem li;
//            while (items.hasNext()) {
//                li = items.next();
//                discSel.setMultiple(false);
//                discSel.appendChild(li);
//                discSel.setMultiple(true);
//            }
//        }
        discSel.clearSelection();
    }

    public void removerArquivo(List<Arquivoinscricao> la, List<Media> lm) {
        for (int i = 0; i < la.size(); i++) {
            la.remove(i);
            lm.remove(i);
        }
    }

    public void onBtnAvancar1() {
        if (combEstInsc.getSelectedItem() == null || discSel.getItemCount() == 0) {
            Clients.showNotification("Por favor, seleccione um estudante e as respectivas disciplinas", "error", null, null, 0);
            return;
        } else {
            tabDadosInscricao.setSelected(false);
            tabPagamentoTaxa.setSelected(true);
        }
        preenchTabPagementoTaxa();
    }

    public void onSwitchTab$tabPagamentoTaxa(ForwardEvent evt) {
        if (combEstInsc.getSelectedItem() == null || discSel.getItemCount() == 0) {
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
        Estudante e = (Estudante) combEstInsc.getSelectedItem().getValue();
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
            total = pal.getTaxaInscricaoEstrangeiro().floatValue() * discSel.getItemCount();
        } else {
            rwTaxanM.setVisible(true);
            lbtaxaInscricao.setValue(pal.getTaxaInscricaoNacional() + "");
            total = pal.getTaxaInscricaoNacional().floatValue() * discSel.getItemCount();
        }
        Inscricao i = csimpm.get(Inscricao.class, ibidInsc.getValue().longValue());
//        if (i.getMulta() != null && i.getMulta() > 0) {
//          //  rwTaxamulta30.setVisible(true);
//          //  lbtaxaMulta30dias.setValue(i.getMulta() + "");
//     //       total = total + (total * (i.getMulta() / 100));
//        }
//            else {
//            rwTaxamulta30.setVisible(false);
//        }
        // if(i.getMulta()!=null) ibmulta.setValue(i.getMulta().intValue());
        //   lbtotal.setValue(total + "");

    }

    public static void setMediaTaxa(Media media) {
        mediaCPI = media;
    }

    public void onVerDisc(ForwardEvent evt) throws Exception {
        winAddInscricaoDisc.setParent(winInscricaoP);
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

//    public void onClick$btnCancDisc() {
//        lbinscricaop.setModel(getListInscricaoModel());
//        winAddInscricaoDisc.detach();
//    }
    public void onAnular(final ForwardEvent evt) {
        Button btn = (Button) evt.getOrigin().getTarget();
        //Listitem litem = (Listitem) btn.getParent().getParent();
        Inscricaodisciplina id = (Inscricaodisciplina) ((Listitem) lbdiscInsc.getSelectedItems().toArray()[0]).getValue();
        id = csimpm.load(Inscricaodisciplina.class, id.getInscricaodisciplinaPK());
        Window win = (Window) Executions.createComponents("inscricao/AnularInscricao.zul", winAddInscricaoDisc, null);
        //((Intbox) win.getFellow("item")).setValue(litem.getIndex());
        ((Label) win.getFellow("lbnumero")).setValue(id.getInscricao().getIdEstudante().getNrEstudante());
        ((Label) win.getFellow("lbNome")).setValue(id.getInscricao().getIdEstudante().getNomeCompleto());
//        Messagebox.show("Pretende anular esta disciplina?", "Prompt", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION,
//                new EventListener() {
//                    @Override
//                    public void onEvent(Event evet) {
//                        switch (((Integer) evet.getData()).intValue()) {
//                            case Messagebox.YES:
//                                Button btn = (Button) evt.getOrigin().getTarget();
//                                Listitem litem = (Listitem) btn.getParent().getParent();
//                                Inscricaodisciplina id = (Inscricaodisciplina) litem.getValue();
//                                id.setEstado(false);
//                                ((Button) btn.getParent().getChildren().get(1)).setVisible(true);
//                                btn.setVisible(false);
//                                csimpm.update(id);
//                                Clients.showNotification(" Anulado com sucesso", null, null, null, 2000);
//                                break;
//                            case Messagebox.NO:
//                                return;
//                        }
//                    }
//                });
    }

    public void onClick$btnRegistar() throws IOException {
        Disciplinaanulada da = new Disciplinaanulada();
        da.setDataanulacao(new Date());
        da.setMotivo(txMotivo.getText());
        //tbimg2.getText();
        final Iterator<Listitem> items1 = new ArrayList(lbimg1.getItems()).listIterator();
        Media m2 = null;
        while (items1.hasNext()) {
            m2 = (Media) items1.next().getValue();
            da.setDoc(m2.getName());
        }
        csimpm.Save(da);
        if (m2 != null) {
            String nome = lbNumero.getValue() + da.getDataanulacao();
            File Test = new File(UserAutentic.getPathR("6") + nome);
            try {
                org.zkoss.io.Files.copy(Test, m2.getStreamData());
            } catch (IOException ex) {
                Logger.getLogger(GuardarEstudanteController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        final Iterator<Listitem> items2 = new ArrayList(((Listbox) win.getParent().getFellow("lbdiscInsc")).getSelectedItems()).listIterator();
        Inscricaodisciplina idis = null;
        while (items2.hasNext()) {
            Listitem li = items2.next();
            idis = (Inscricaodisciplina) li.getValue();
            idis.setEstado(false);
            idis.setAnulacao(da);
            csimpm.Save(idis);
        }
        win.detach();
    }

    public void onBtnCnf() {
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
                                    int v = versao.getValue().intValue();
                                    i = csimpm.getLocked(Inscricao.class, ibidInsc.getValue().longValue());
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
                                        win.detach();
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
                                    }
                                    Clients.showNotification(" Inscricao validada com Sucesso", null, null, null, 2000);

                                    //calcular o nivel de frequencia
                                    if (i.getModoInscricao().intValue() == 9) {
                                        par.clear();
                                        par.put("insc", i);
                                        List<Disciplina> ld = csimpm.findByJPQuery("select id.disciplina from Inscricaodisciplina id"
                                                + " where id.inscricao = :insc", par);
                                        int k = 0;
                                        Estudante e = i.getIdEstudante();
                                        final Iterator<Disciplina> items = ld.iterator();
                                        Disciplina ii;
                                        while (items.hasNext()) {
                                            ii = items.next();
                                            if (ld.get(k).getNivel() <= e.getNivelFrequencia()) {
                                                k++;
                                            }
                                        }
                                        if (k <= (ld.size() - 1)) {
                                            calcularNiveFreq(e);
                                        }
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
                                    eq = EventQueues.lookup("mmm" + f.getIdFaculdade(), EventQueues.APPLICATION, true);
                                    eq.publish(new Event("onRinscD", null, i));
                                    eq = EventQueues.lookup("mmmk" + i.getIdEstudante().getIdEstudante(), EventQueues.APPLICATION, true);
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
                                win.detach();
                                break;
                            case Messagebox.NO:
                                return;
                        }
                    }
                });

    }

//    public void calcularNiveFreq(Estudante e) {
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
//            int t1 = 0, t2 = 0;
//            if (co1 >= c1) {
//                t1 = co1 - c1;
//            }
//            if (co2 >= c2) {
//                t2 = co2 - c2;
//            }
//            if (t1 + t2 <= 2) {// so no primeiro semestre
//                nc2 = nc2 + 1;
//            }
//        }
//        if (nc2 > nc) {
//            e.setNivelFrequencia(nc2);
//            csimpm.updates(e);
//        }
//    }
    
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
                csimpm.updates(e);
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

    public void onRejeitar(ForwardEvent event) throws IOException {
        Window w = (Window) Executions.createComponents("/inscricao/MotivoInsc.zul", win, null);
        ((Textbox) w.getFellow("txMotivoR")).setText("Inscrição foi rejeitada.\n Dados estão incorrectos");
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
                                Long idi = ((Intbox) ((Window) winAddMotivo.getParent()).getFellow("ibidInsc")).getValue().longValue();
                                Inscricao i = null;
                                Users u = csimpm.get(Users.class, usr.getUtilizador());
                                Faculdade f = csimpm.get(Faculdade.class, usr.getFaculdade().getIdFaculdade());
                                Transaction t = csimpm.getTransacao();
                                try {
                                    t.begin();
                                    int v = ((Intbox) ((Window) winAddMotivo.getParent()).getFellow("versao")).getValue().intValue();
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
                                        // win.detach();
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
                                            csimpm.deletes(ii);
                                        }
                                    } else {
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
                                eq = EventQueues.lookup("rinscD" + f.getIdFaculdade(), EventQueues.APPLICATION, true);
                                eq.publish(new Event("onPedidoMatD", null, i));
                                eq = EventQueues.lookup("rinscD" + i.getIdEstudante().getIdEstudante(), EventQueues.APPLICATION, true);
                                eq.publish(new Event("onPedidoMatD", null, i));
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

    //Pesquisar estudante
    public ListModel<Curso> getListaCursoModel() {
        //Users u = csimpm.get(Users.class, usr.getUtilizador());
        Faculdade f = csimpm.get(Faculdade.class, usr.getFaculdade().getIdFaculdade());
        par.clear();
        par.put("fac", f);
        List<Curso> lc = csimpm.findByJPQuery("from Curso c where c.faculdade = :fac", par);
        return listaCursoModel = new ListModelList<Curso>(lc);
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
            if (condpar.containsKey("nr")) {
                condpar.remove("nr");
            }
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
            if (condpar.containsKey("nome")) {
                condpar.remove("nome");
            }
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
            if (condpar.containsKey("a")) {
                condpar.remove("a");
            }
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
            lbinscricaop.setModel(new ListModelList<Inscricao>());
        }
        List<Inscricao> li = null;
        par.clear();
        par.put("user", usr.getUtilizador());
        //Users u = csimpm.findEntByJPQuery("from Users u where u.utilizador = :user", par);
        Faculdade f = csimpm.get(Faculdade.class, usr.getFaculdade().getIdFaculdade());
        par.clear();
        par.put("fac",f);
        li = csimpm.findByJPQueryFilter("from Inscricao i where i.idEstudante.cursocurrente.faculdade = :fac and (i.modoInscricao>6 or i.modoInscricao=3) and i.dataConfirmacao is null order by i.dataInscricao asc", par, i, j);

        final Iterator<Inscricao> items = li.iterator();
        Inscricao insc;
        lbinscricaop.setRows(lbinscricaop.getItemCount() + li.size() + 1);
        while (items.hasNext()) {
            insc = items.next();
            ((ListModelList) lbinscricaop.getModel()).add(insc);
        }
        if (li.size() < j) {
            btv.setVisible(false);
        } else {
            btv.setVisible(true);
        }
    }

    public void onLoadi() {
        int i = lbinscricaop.getItemCount();
        setLB(i, i + 20);
    }

}
