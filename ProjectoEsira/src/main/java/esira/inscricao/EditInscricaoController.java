/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package esira.inscricao;

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
import org.hibernate.LockMode;
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
import org.zkoss.zul.Doublebox;
import org.zkoss.zul.Hlayout;
import org.zkoss.zul.Iframe;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
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
public class EditInscricaoController extends GenericForwardComposer {

    @WireVariable
    private CRUDService csimpm = (CRUDService) SpringUtil.getBean("CRUDService");
    private Window winAddInscricao, winInscricaoP, winAddInscricaoDisc, winInscricaoPEst, win, winEdInscricao, winAddMotivoI;
    private Combobox combEstInsc, combSem;
    private ListModel<Estudante> estudantes;
    private ListModel<Inscricao> inscric;
    private ListModel<Disciplina> disciplinas;
    private ListModel<Curso> listaCursoModel;
    private Label lbcurso, lbnivel, lbsexo, lbcred, lbtaxaInscricao, lbtaxaInscricaoE, lbano, lbNumero,
            lbtaxaMulta15dias, lbtaxaMulta30dias, lbtotal, lbsem;
    private Listbox discSel, lbinscricaop, lbdiscInsc, lbimg, lbimg1, lbimgII;
    private Tab tabPagamentoTaxa, tabDadosInscricao;
    private Tabs tabs;
    private Row rwTaxanM, rwTaxaeM, rwTaxamulta15, rwTaxamulta30;
    private Button addInsc, btnCancDisc, btnAnular;
    Textbox txnrTalao, txx, tbimg4, txMotivo, txMotivoR, txMot;
    Combobox cbCursoRel, cbDiscRel;
    List listaI;
    Datebox dinsc;
    private static Media mediaCPI;
    private int ano;
    private int sem = 0;
    private int n;
    private Intbox ibitem, ibidInsc, versao;
    Map<String, Object> par = new HashMap<String, Object>();
    Users usr = (Users) Sessions.getCurrent().getAttribute("user");
    private Auxhead ahead;
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
        //Users u = csimpm.get(Users.class, usr.getUtilizador());
        Faculdade f = csimpm.get(Faculdade.class, usr.getFaculdade().getIdFaculdade());
        condpar.clear();
        condpar.put("fac", f);
        // if(winInscricaoP.isVisible()==false&&winAddInscricao!=null&&winAddInscricao.getParent().getId().equals("winInscricaoP")){
        // addInsc();
        //}
        //      setLB(0, 20);
    }

    public void onSetQueue() {
        // Users u = csimpm.get(Users.class, usr.getUtilizador());
        eq = EventQueues.lookup("pinscD" + usr.getFaculdade().getIdFaculdade(), EventQueues.APPLICATION, true);
        eq.subscribe(getEvento());
        eq = EventQueues.lookup("rinscD" + usr.getFaculdade().getIdFaculdade(), EventQueues.APPLICATION, true);
        eq.subscribe(getEvento2());
    }

    public EventListener getEvento() {
        return new EventListener() {
            @Override
            public void onEvent(Event event) throws Exception {
                Inscricao i = (Inscricao) event.getData();
                i = csimpm.load(Inscricao.class, i.getIdInscricao());
                //  Users u = csimpm.get(Users.class, usr.getUtilizador());
                if (winInscricaoP.getParent() != null) {// && u.getFaculdade().getIdFaculdade() == i.getIdEstudante().getCursocurrente().getFaculdade().getIdFaculdade()) {
                    lbinscricaop.setRows(lbinscricaop.getItemCount() + 2);
                    //((ListModelList) lbinscricaop.getModel()).remove(i);
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
                // Users u = csimpm.get(Users.class, usr.getUtilizador());
                if (winInscricaoP.getParent() != null) {
                    ((ListModelList) lbinscricaop.getModel()).remove(i);
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

    public void onCreate$winInscricaoP(Event evt) {
        if (winInscricaoP.inModal()) {
            winInscricaoP.setMode("modal");
            winInscricaoP.setTitle("Validação de Inscrições");
            winInscricaoP.setClosable(true);
            winInscricaoP.setVflex("1");
            winInscricaoP.setHflex("1");
            winInscricaoP.setWidth("80%");
            ((Hlayout) winInscricaoP.getFellow("hlpinsc")).setVisible(false);
            //ahead.setVisible(false);
            Long ides = Long.parseLong(txx.getText());
            Estudante e = csimpm.get(Estudante.class, ides);
            List<Inscricao> li = null;
            par.clear();
            par.put("e", e);
            li = csimpm.findByJPQuery("from Inscricao i where i.idEstudante = :e and i.dataConfirmacao is null", par);
            lbinscricaop.setModel(new ListModelList<Inscricao>(li));
        }
    }

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
        if (insc.getModoInscricao().intValue() > 2) {
            Clients.showNotification("Esta Inscricao será validada pelo Director! Caso for aceite, poderá ser visualizada na Lista de Inscrições", "warning", null, null, 0);
            return;
        }
        winAddInscricao.setParent(winInscricaoP);
        winAddInscricao.doModal();
        ((Tab) winAddInscricao.getFellow("tabDadosInscricao")).setSelected(true);
        Calendar cal = new GregorianCalendar();
        cal.setTime(insc.getDataInscricao());
        int ano = cal.get(Calendar.YEAR);
        ((Intbox) winAddInscricao.getFellow("ibidInsc")).setValue(insc.getIdInscricao().intValue());
        ((Intbox) winAddInscricao.getFellow("ibitem")).setValue(litem.getIndex());
        int ve = -1;
        if (insc.getVersion() != null) {
            ve = insc.getVersion().intValue();
        }
        ((Intbox) winAddInscricao.getFellow("versao")).setValue(ve);
        ((Label) winAddInscricao.getFellow("lbano")).setValue(ano + "");
        ((Label) winAddInscricao.getFellow("lbsem")).setValue("Semestre " + insc.getSemestre().toString());
        ((Label) winAddInscricao.getFellow("lbnivel")).setValue(insc.getIdEstudante().getNivelFrequencia() + "");
        ((Label) winAddInscricao.getFellow("lbcurso")).setValue(insc.getIdEstudante().getCursocurrente() + "");
        if (insc.getIdEstudante().getMasculino()) {
            ((Label) winAddInscricao.getFellow("lbsexo")).setValue("Masculino");//csimpm.findByQuery("select s.descricao from fecn1.sexo s, fecn1.estudante e where s.id_sexo = e.sexo "
        } else {
            ((Label) winAddInscricao.getFellow("lbsexo")).setValue("Femenino");
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
        ((Label) winAddInscricao.getFellow("lbcred")).setValue(i + "");
        Listbox discs = ((Listbox) winAddInscricao.getFellow("discSel"));
        limpaLB(discs);
        for (Disciplina d : lds) {
            Listitem list = new Listitem(d.getNome() + " - " + d.getNivel() + "º Ano - " + d.getSemestre() + "º Sem - " + d.getCredito() + " Credito", d);
//            list.setDisabled(true);
//            list.setCheckable(false);
//            discs.setMultiple(false);
            discs.appendChild(list);
//            discs.setMultiple(true);
        }
        //Recibos
        ((Row) winAddInscricao.getFellow("rwRU")).setVisible(false);
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
            Media mm = new AMedia(am3.getNome(), FilenameUtils.getExtension(sss), URLConnection.guessContentTypeFromName(sss), pp.getByteData());
            ((Textbox) winAddInscricao.getFellow("tbimg4")).setValue(mm.getName());
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
            ((Row) winAddInscricao.getFellow("rwRU")).setVisible(true);
            ((Textbox) winAddInscricao.getFellow("txRU")).setText(recibousado);
        }
        ((Listbox) winAddInscricao.getFellow("lbimg")).setModel(new ListModelList(lm3));
        ((Listbox) winAddInscricao.getFellow("lbimgII")).setModel(new ListModelList(lm4));

        Estudante e = insc.getIdEstudante();
        List<Estudante> lc = new ArrayList<Estudante>();
        lc.add(e);
        Combobox cbe = (Combobox) winAddInscricao.getFellow("combEstInsc");
        cbe.setModel(new ListModelList<Estudante>(lc));
//       // cbe.setText("dsfsfsdfsf");
        //     cbe.setSelectedIndex(0);
        cbe.setReadonly(true);
        cbe.setButtonVisible(false);
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
        limpForm();
        winEdInscricao.detach();
        tabPagamentoTaxa.setSelected(false);
        tabDadosInscricao.setSelected(true);
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
        if (lbtotal.getValue().equals("")) {
            preenchTabPagementoTaxa();
        }
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
        int pais = (Integer) csimpm.findByQuery("select p.id_pais from pais p, estudante e "
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
        Date dtaxa1, dtaxa2, dtaxa3;
        if (lbsem.getValue().equals("Semestre 1")) {
            if (ano != e.getAnoIngresso()) {
                dtaxa1 = pal.getDataFinalMatricula();
                dtaxa2 = pal.getDataFimMatriculaE2();
                dtaxa3 = pal.getDm1();
            } else {
                dtaxa1 = pal.getDataFimIE1();
                dtaxa2 = pal.getDataFimIE2();
                dtaxa3 = pal.getDma1();
            }
            if (ano != e.getAnoIngresso()) {
                if (dano.after(dtaxa1) && dano.before(dtaxa2)) {
                    rwTaxamulta15.setVisible(true);
                    lbtaxaMulta15dias.setValue(pal.getPercentagemMultaInscricao15dias() + "");
                    total = total * (pal.getPercentagemMultaInscricao15dias() / 100);
                } else if (dano.after(dtaxa2) && dano.before(dtaxa3)) {
                    rwTaxamulta30.setVisible(true);
                    lbtaxaMulta30dias.setValue(pal.getPercentagemMultaInscricao30dias() + "");
                    total = total * (pal.getPercentagemMultaInscricao30dias() / 100);
                }
            }
        } else {
            dtaxa1 = pal.getDataFimE1();
            dtaxa2 = pal.getDataFimE2();
            dtaxa3 = pal.getDi1();
            if (dano.after(dtaxa1) && dano.before(dtaxa2)) {
                rwTaxamulta15.setVisible(true);
                lbtaxaMulta15dias.setValue(pal.getPercentagemMultaInscricao15dias() + "");
                total = total * (pal.getPercentagemMultaInscricao15dias() / 100);
            } else if (dano.after(dtaxa2) && dano.before(dtaxa3)) {
                rwTaxamulta30.setVisible(true);
                lbtaxaMulta30dias.setValue(pal.getPercentagemMultaInscricao30dias() + "");
                total = total * (pal.getPercentagemMultaInscricao30dias() / 100);
            }
        }
        lbtotal.setValue(total + "");

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
        List<Inscricaodisciplina> ldisc = csimpm.findByQuery("select id.* from inscricaodisciplina id "
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
        final Inscricao i = csimpm.get(Inscricao.class, ibidInsc.getValue().longValue());
        Estudante e = i.getIdEstudante();
        par.clear();
        Date datactual = new Date();
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(datactual);
        Messagebox.show("Pretende editar os dados?", "", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION,
                new EventListener() {
                    @Override
                    public void onEvent(Event evet) throws InterruptedException, IOException, DocumentException {
                        switch (((Integer) evet.getData()).intValue()) {
                            case Messagebox.YES:
                                // Transaction t = csimpm.getTransacao();
//                                try {
                                // t.begin();
                                int v = versao.getValue().intValue();
                                Inscricao i = csimpm.getLocked(Inscricao.class, ibidInsc.getValue().longValue());
                                if (v == -1 && i.getVersion() == null || v == i.getVersion().intValue()) {
                                    if (i.getVersion() == null) {
                                        i.setVersion(new Short("0"));
                                    } else {
                                        int ver = i.getVersion().intValue();
                                        ver = ver + 1;
                                        i.setVersion(new Short(ver + ""));
                                    }
                                } else {
                                    Clients.showNotification("Esta Inscrição foi Editada por um outro utilizador\n"
                                            + "Repita de novo", "warning", null, null, 0);
                                    //  t.rollback();
                                    winAddInscricao.detach();
                                    return;
                                }
                                List<Arquivoinscricao> larq = new ArrayList<Arquivoinscricao>();
                                List<Media> lmed = new ArrayList<Media>();
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
                                    arq3.setDataInscricao(i.getDataInscricao());
                                    arq3.setEstudante(i.getIdEstudante().getNomeCompleto());
                                    arq3.setValorcobrado(Float.parseFloat(lbtotal.getValue()));
                                    par.clear();
                                    par.put("nrt", arq3.getNrtalao());
                                    par.put("i", i);
                                    if (csimpm.findByJPQuery("from Arquivoinscricao a where a.idInscricao<>:i and a.nrtalao like :nrt", par).size() > 0) {
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
                                List<Arquivoinscricao> la = i.getArquivoinscricaoList();
                                final Iterator<Arquivoinscricao> items3 = new ArrayList(la).listIterator();
                                Arquivoinscricao ai;
                                while (items3.hasNext()) {
                                    ai = items3.next();
                                    i.getArquivoinscricaoList().remove(ai);
                                    String dat = new SimpleDateFormat("dd_MM_yyyy_HH_mm_ss").format(i.getDataInscricao());
                                    String nome = i.getIdEstudante().getNrEstudante() + ai.getNome() + dat;
                                    File test = null;
                                    try {
                                        test = new File(UserAutentic.getPathR("4") + nome);
                                    } catch (java.io.FileNotFoundException ex) {

                                    }
                                    if (test != null) {
                                        test.delete();
                                    }
                                    csimpm.delete(ai);
                                }

                                i.setObs(txMot.getValue());
                                i.setArquivoinscricaoList(null);
                                csimpm.update(i);
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
                                Clients.showNotification(" Inscricao editada com Sucesso", null, null, null, 2000);
                                // t.commit();
                                // while (!t.wasCommitted());
//                                } catch (RuntimeException e) {
//                                    //   t.rollback();
//                                    Clients.showNotification("Erro", e.getMessage(), null, null, 2000);
//                                }
                                winEdInscricao.detach();
                                break;

                            case Messagebox.NO:
                                return;
                        }
                    }
                });

    }

    public void onRejeitar(ForwardEvent event) throws IOException {
        winAddMotivoI.setParent(winAddInscricao);
        winAddMotivoI.doModal();
        ((Textbox) winAddMotivoI.getFellow("txMotivoR")).setText("A sua Inscrição foi rejeitada.\n Os seus dados estão incorrectos");
    }

    public void onBtnC() {
        winAddMotivoI.detach();
    }

    public void onBtnRjt() {
        Messagebox.show("Pretende Rejeitar este Pedido de Inscricao?", "", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION,
                new EventListener() {
                    @Override
                    public void onEvent(Event evet) throws InterruptedException, IOException, EmailException {
                        switch (((Integer) evet.getData()).intValue()) {
                            case Messagebox.YES:
                                Transaction t = csimpm.getTransacao();
                                try {
                                    t.begin();
                                    Long idi = ((Intbox) ((Window) winAddMotivoI.getParent()).getFellow("ibidInsc")).getValue().longValue();
                                    int v = ((Intbox) ((Window) winAddMotivoI.getParent()).getFellow("versao")).getValue().intValue();
                                    Inscricao i = csimpm.getLocked(Inscricao.class, idi);
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
                                        winAddInscricao.detach();
                                        winAddMotivoI.getParent().detach();
                                        winAddMotivoI.detach();
                                        return;
                                    }
                                    Users u = csimpm.get(Users.class, usr.getUtilizador());
                                    Faculdade f = csimpm.get(Faculdade.class, usr.getFaculdade().getIdFaculdade());
                                    i.setFuncionario(u.getIdFuncionario());
                                    i.setDataConfirmacao(new Date());
                                    i.setEstado(false);
                                    csimpm.updates(i);
//                                new Email().sendEmail(UserAutentic.getEmailP("email"),
//                                        i.getIdEstudante().getEmail(),
//                                        UserAutentic.getEmailP("user"),
//                                        UserAutentic.getEmailP("senha"));
                                    Motivoinsc m = new Motivoinsc();
                                    m.setIdInscricao(i.getIdInscricao());
                                    m.setMotivo(txMotivoR.getText());
                                    csimpm.updates(m);
                                    try {
                                        new Email().sendEmail(UserAutentic.getEmailP("email"),
                                                i.getIdEstudante().getEmail(), "Inscrição Rejeitada!", "Prezado " + i.getIdEstudante().getNomeCompleto() + ""
                                                + "\nA sua Inscricao foi rejeitada\n" + m.getMotivo() + "\n"
                                                + "\n\n----------------------------------------\n\n\n"
                                                + "Registo Acadêmico - UniLúrio-" + f.getDesricao(),
                                                UserAutentic.getEmailP("senha"));
                                    } catch (EmailException uh) {
                                        Clients.showNotification("O Envio de e-mail Falhou!", "warning", null, null, 0, true);
                                    }
                                    Clients.showNotification("Iscrição Rejeitada com Sucesso", null, null, null, 2000);
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
                                        eq.publish(new Event("onPedidoMatD", null, vp));
                                    }//
//                                ((ListModelList) ((Listbox) winAddMotivoI.getParent().getParent().getFellow("lbinscricaop")).getModel()).remove(i);
                                    eq = EventQueues.lookup("rinscD" + f.getIdFaculdade(), EventQueues.APPLICATION, true);
                                    eq.publish(new Event("onPedidoMatD", null, i));
                                    eq = EventQueues.lookup("rinscD" + i.getIdEstudante().getIdEstudante(), EventQueues.APPLICATION, true);
                                    eq.publish(new Event("onPedidoMatD", null, i));
                                    t.commit();
                                    while (!t.wasCommitted());
                                } catch (RuntimeException e) {
                                    t.rollback();
                                    Clients.showNotification("Erro", e.getMessage(), null, null, 2000);
                                }
//                                new Listbox().appendChild(((Listbox) winAddMotivoI.getParent().getParent().getFellow("lbinscricaop"))
//                                        .getItemAtIndex(((Intbox) winAddMotivoI.getParent().getFellow("ibitem")).getValue()));
                                winAddInscricao.detach();
                                winAddMotivoI.getParent().detach();
                                winAddMotivoI.detach();

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
        par.clear();
        Faculdade f = csimpm.get(Faculdade.class, usr.getFaculdade().getIdFaculdade());
        par.put("fac", f);
        List<Curso> lc = csimpm.findByJPQuery("from Curso c where c.faculdade = :fac", par);
        return listaCursoModel = new ListModelList<Curso>(lc);
    }

    //Ainda nao esta ser usado
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
