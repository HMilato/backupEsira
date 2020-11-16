/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package esira.matricula;

import ExcelExport.BeanToExcel;
import entidade.Arquivo;
import esira.domain.Arquivoestudante;
import esira.domain.Arquivomatricula;
import esira.domain.Bolsa;
import esira.domain.Curso;
import esira.domain.Disciplinaanulada;
import esira.domain.Documento;
import esira.domain.Endereco;
import esira.domain.Enderecof;
import esira.domain.Estadocivil;
import esira.domain.Estudante;
import esira.domain.Faculdade;
import esira.domain.Ingressobolseiro;
import esira.domain.Ingressoexameadmissao;
import esira.domain.Ingressomudancauniversidade;
import esira.domain.Inscricao;
import esira.domain.Inscricaodisciplina;
import esira.domain.Matricula;
import esira.domain.MatriculaPK;
import esira.domain.Matriculaanulada;
import esira.domain.Operacaopedido;
import esira.domain.Pais;
import esira.domain.PlanificacaoAnoLectivo;
import esira.domain.Prescricao;
import esira.domain.Profissao;
import esira.domain.Provincia;
import esira.domain.Tipodocumento;
import esira.domain.Users;
import esira.domain.Viaingresso;
import static esira.matricula.ListaEstudantesMatriculadosController.ord;
import esira.service.CRUDService;
import esira.service.UserAutentic;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.apache.commons.io.FilenameUtils;
import org.zkoss.image.AImage;
import org.zkoss.image.Image;
import org.zkoss.lang.Strings;
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
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Div;
import org.zkoss.zul.Hlayout;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Paging;
import org.zkoss.zul.Radio;
import org.zkoss.zul.Row;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Vbox;
import org.zkoss.zul.Window;

/**
 *
 * @author DataCenter
 */
public class ListaEstudantesController extends GenericForwardComposer {

    @WireVariable
    private CRUDService csimpm = (CRUDService) SpringUtil.getBean("CRUDService");
    Window winlestudantes, win, winAnulados, winanularmot, winTurma;
    Listbox lbEstudantes, lbAnulcao;
    Textbox txProcurar, txProcurarNrmec;
    Combobox cbcurso, cbTurno;
    ListModel<Curso> listaCursoModel;
    ListModel<Matricula> listEstudanteModel;
    List<Matricula> listaM;
    private Label lbmot, lba;
    private Vbox vb2, vb1, vb3, vb4;
    Intbox idl, ide, ibidEstudante, ibano;
    Map<String, Object> par = new HashMap<String, Object>();
    Users usr = (Users) Sessions.getCurrent().getAttribute("user");
    private Button btnMatric, btnAnular, btv;
    String condfac = "", condnr = "", condnome = "", condgenero = "", condanoi = "", condcurso = "";
    Map<String, Object> condpar = new HashMap<String, Object>();
    static String ord = "";
    private Intbox ibPAno, ibturma;
    private Hlayout ahead;
    private EventQueue eq;
    private Checkbox chturma;

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        win = (Window) Executions.createComponents("/matricula/Matricula.zul", null, null);
        win.setTitle("Renovar Matricula");
        win.detach();
        if (usr.getUestudante()) {
            if (lbEstudantes != null) {
                lbEstudantes.setVisible(false);
                ahead.setVisible(false);
                btv.setVisible(false);
            }
        } else {
            Date dano = new Date();
            Calendar cal = new GregorianCalendar();
            cal.setTime(dano);
            int ano = cal.get(Calendar.YEAR);
//        ibPAno.setValue(ano);
//        condpar.put("anoi", ano);
//        condanoi = " and m.matriculaPK.ano = :anoi";
            ListModel lm = getListaCursoModel();
            if (cbcurso != null&&lm!=null&&lm.getSize()>0) {
                cbcurso.setModel(getListaCursoModel());
                condpar.put("curso", lm.getElementAt(0));
                condcurso = " and m.curso = :curso";
                addeventoLB(lbEstudantes, null, Matricula.class);
            }
        }
    }
    
    public void onAfrender(){
     if(cbcurso.getModel()!=null&&cbcurso.getModel().getSize()>0)cbcurso.setSelectedIndex(0);
    }

    public void onSetQueueEstMat() {
        //Users u = csimpm.get(Users.class, usr.getUtilizador());
        if (!usr.getUestudante()) {
            eq = EventQueues.lookup("mat" + usr.getFaculdade().getIdFaculdade(), EventQueues.APPLICATION, true);
            eq.subscribe(getEventoP());
            eq = EventQueues.lookup("rmatD" + usr.getFaculdade().getIdFaculdade(), EventQueues.APPLICATION, true);
            eq.subscribe(getEventoP());
        } else {
            usr = csimpm.get(Users.class, usr.getUtilizador());
            eq = EventQueues.lookup("mat" + usr.getIdEstudante().getIdEstudante(), EventQueues.APPLICATION, true);
            eq.subscribe(getEventoMatE());
            eq = EventQueues.lookup("rmatD" + usr.getIdEstudante().getIdEstudante(), EventQueues.APPLICATION, true);
            eq.subscribe(getEventoPE());
            eq = EventQueues.lookup("rmatA" + usr.getIdEstudante().getIdEstudante(), EventQueues.APPLICATION, true);
            eq.subscribe(getEventoIAE());
            eq = EventQueues.lookup("anulaM" + usr.getIdEstudante().getIdEstudante(), EventQueues.APPLICATION, true);
            eq.subscribe(getEventoAnulE());
        }
    }

    public void onSetQueueEstMatAnul() {
       //Users u = csimpm.get(Users.class, usr.getUtilizador());
        eq = EventQueues.lookup("rmatA" + usr.getFaculdade().getIdFaculdade(), EventQueues.APPLICATION, true);
        eq.subscribe(getEventoAnulacao());
        eq = EventQueues.lookup("anulaM" + usr.getFaculdade().getIdFaculdade(), EventQueues.APPLICATION, true);
        eq.subscribe(getEventoAnulacao());
    }

    public EventListener getEventoMatE() {
        return new EventListener() {
            @Override
            public void onEvent(Event event) throws Exception {
                if (winlestudantes.getParent() != null) {
                    onCreate$vb4();
                }
            }
        };
    }

    public EventListener getEventoPE() {
        return new EventListener() {
            @Override
            public void onEvent(Event event) throws Exception {
                if (winlestudantes.getParent() != null) {
                    onCreate$vb4();
                }
            }
        };
    }

    public EventListener getEventoP() {
        return new EventListener() {
            @Override
            public void onEvent(Event event) throws Exception {
                Matricula m = (Matricula) event.getData();
//                par.clear();
//                par.put("mpk", m.getMatriculaPK());
                if (winlestudantes.getParent() != null) {
//                    m = csimpm.findEntByJPQuery("from Matricula m where m.matriculaPK = :mpk", par);
//                    Users u = csimpm.get(Users.class, usr.getUtilizador());
                    if (m.getEstado() == true && m.getConfirmacao() != null) {
                        int ind = ((ListModelList) lbEstudantes.getModel()).indexOf(m);
                        if (ind >= 0) {
                            // Messagebox.show(ind+"");
                            ((ListModelList) lbEstudantes.getModel()).remove(ind);
                        }
                    }
                }
            }
        };
    }

    public EventListener getEventoAnulacao() {
        return new EventListener() {
            @Override
            public void onEvent(Event event) throws Exception {
                Matriculaanulada m = (Matriculaanulada) event.getData();
                par.clear();
                par.put("mpk", m.getMatriculaPK());
                if (winAnulados.getParent() != null) {
                    Matriculaanulada ma = csimpm.findEntByJPQuery("from Matriculaanulada ma where ma.matriculaPK=:ide", par);
                    Users u = csimpm.get(Users.class, usr.getUtilizador());
                    if (m.getEstado() == true && m.getDataconfirmacao() != null) {
                        ((ListModelList) lbEstudantes.getModel()).add(0, m);
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
                if (winlestudantes.getParent() != null) {
                    onCreate$vb4();
                }
            }
        };
    }

    public EventListener getEventoAnulE() {
        return new EventListener() {
            @Override
            public void onEvent(Event event) throws Exception {
                Disciplinaanulada da = (Disciplinaanulada) event.getData();
                if (winlestudantes.getParent() != null) {
                    onCreate$vb4();
                }
            }
        };
    }

    public void onAnular(ForwardEvent event) throws Exception {
        if (usr.getUestudante() && winanularmot.isVisible()) {
            onClose$winanularmot(null);
            winanularmot.setVisible(false);
        }
        Date dano = new Date();
        Calendar cal = new GregorianCalendar();
        cal.setTime(dano);
        int ano = cal.get(Calendar.YEAR);
        Estudante e = csimpm.get(Users.class, usr.getUtilizador()).getIdEstudante();
        MatriculaPK matPK = new MatriculaPK(e.getIdEstudante(), ano);
        par.clear();
        par.put("matpk", matPK);
        Matricula todo = csimpm.findEntByJPQuery("from Matricula m where m.matriculaPK = :matpk", par);
        final HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("nome", todo.getEstudante().getNomeCompleto());
        map.put("cursoid", todo.getCurso().getIdCurso());
        map.put("cursonome", todo.getCurso().getDescricao());
        map.put("nrMec", todo.getEstudante().getNrEstudante());
        map.put("estudanteid", todo.getEstudante().getIdEstudante());
        map.put("ano", todo.getMatriculaPK().getAno());
        Window win = (Window) Executions.createComponents("/matricula/AnularMatricula.zul", winlestudantes, map);
        ((Intbox) win.getFellow("idlitem")).setValue(-1);
//         Listbox l = new Listbox();
//         l.appendChild(litem);
    }

    public ListModel<Matricula> getListEstudanteModel() {
        Calendar c = Calendar.getInstance();

        Map<String, Object> par = new HashMap<String, Object>();
        par.clear();
        par.put("user", usr.getUtilizador());
       // Users u = csimpm.findEntByJPQuery("from Users u where u.utilizador = :user", par);
        par.clear();
        Faculdade f = csimpm.get(Faculdade.class, usr.getFaculdade().getIdFaculdade());
        par.put("fac", f);
        par.put("ano", c.get(Calendar.YEAR));
        listaM = csimpm.findByJPQuery("select k from Matricula k where k.estudante.estado is true "
                + " k.dataMatricula in (select max(m.dataMatricula) from Matricula m "
                + "WHERE m.curso.faculdade = :fac and m.anulada is false and m.matriculaPK.idEstudante "
                + "NOT IN (SELECT m.matriculaPK.idEstudante FROM Matricula m WHERE m.matriculaPK.ano = :ano) group by"
                + " m.matriculaPK.idEstudante)", par);
        List<Matricula> lm = csimpm.findByJPQuery("from Matricula m "
                + "WHERE m.curso.faculdade = :fac and m.matriculaPK.ano =:ano and m.confirmacao is not null and m.estado is false", par);
        listaM.addAll(lm);
        return listEstudanteModel = new ListModelList<Matricula>(listaM);
    }

    public void onRenovar(ForwardEvent event) throws IOException {
        Button btn = (Button) event.getOrigin().getTarget();
        Listitem litem = (Listitem) btn.getParent().getParent();
        Matricula todo = (Matricula) litem.getValue();
        if (todo == null) {
            Clients.showNotification("A matricula nao pode ser renovada. \n Primeriro"
                    + "Provavelmente o estudante nao tem nenhuma nota", "warning", null, null, 0);
            return;
        }
        preMatricular(todo, litem.getIndex(), winlestudantes, litem.getListbox());
    }

    public void onRenovarAnulada(ForwardEvent event) throws IOException {
        Button btn = (Button) event.getOrigin().getTarget();
        Listitem litem = (Listitem) btn.getParent().getParent();
        Matricula todo = ((Matriculaanulada) litem.getValue()).getMatricula();
        preMatricular(todo, litem.getIndex(), winAnulados, litem.getListbox());
    }

    public void onMatric() throws IOException {
        Users user = csimpm.get(Users.class, usr.getUtilizador());
        par.clear();
        par.put("ide", user.getIdEstudante().getIdEstudante());
        Matricula ma = csimpm.findEntByJPQuery("select k from Matricula k where k.dataMatricula in (select max(m.dataMatricula) from Matricula m "
                + "WHERE m.matriculaPK.idEstudante =:ide) and k.matriculaPK.idEstudante = :ide", par);
        if (ma == null) {
            Clients.showNotification("A sua matricula nao pode ser renovada. \n"
                    + "Provavelmente o estudante nao tem nenhuma nota", "warning", null, null, 0);
            return;
        }
        preMatricular(ma, -1, winlestudantes, null);

    }

    public void preMatricular(Matricula todo, int index, Window windo, Listbox lb) throws IOException {
        Date dano = new Date();
        Calendar c = new GregorianCalendar();
        c.setTime(dano);
        int ano = c.get(Calendar.YEAR);
//        if (todo.getAnulada() && todo.getMatriculaPK().getAno() == ano) {
//            Clients.showNotification("Esta matricula pode ser renovada apartir do proximo Ano", "warning", null, null, 0);
//            return;
//        }
        if (usr.getUestudante() && vb4.isVisible() || !todo.getEstado()) {
            MatriculaPK mpk = new MatriculaPK(todo.getMatriculaPK().getIdEstudante(),
                    todo.getMatriculaPK().getAno());
            par.clear();
            par.put("ide", mpk);
            todo = csimpm.findEntByJPQuery("from Matricula m where m.matriculaPK=:ide", par);
            if (todo.getMatriculaPK().getAno() == ano) {
                dano = todo.getDataMatricula();
            }
        }

       Users up = csimpm.get(Users.class, usr.getUtilizador());
             par.clear();
            par.put("fac", up.getFaculdade());
           PlanificacaoAnoLectivo planificacaoAnoLectivo = csimpm.findEntByJPQuery("from PlanificacaoAnoLectivo p where p.faculdade = :fac", par);
        if (planificacaoAnoLectivo != null) {
//            if (dano.before(planificacaoAnoLectivo.getDataInicioMatricula())) {
//                Clients.showNotification("Fora da Epoca de Matricula", "warning", null, null, 0);
//                return;
//            }
            if ((dano.after(planificacaoAnoLectivo.getDataInicioMatricula()) && dano.before(planificacaoAnoLectivo.getDataFinalMatricula())) || (todo != null && todo.getPeriodo() != null && todo.getPeriodo().equals("NORMAL"))) {
                //  Messagebox.show("Periodo normal de Matricula");

                final HashMap<String, Object> map = new HashMap<String, Object>();
//                        map.put("matricula", todo);
//                        map.put("controler", "esira.matricula.RenovarMatriculaController");
                map.put("periodo", "NORMAL");
                // Window win = (Window) Executions.createComponents("/matricula/Matricula.zul", null, map);
                win.setParent(windo);
                win.doModal();
                ((Label) win.getFellow("taxamultas")).setValue("NORMAL");
                ((Label) win.getFellow("lbtaxaMatricula")).setValue(planificacaoAnoLectivo.getTaxaMatriculaNacional().toString());
                ((Label) win.getFellow("lbtaxaMatriculaE")).setValue(planificacaoAnoLectivo.getTaxaMatriculaEstrangeiro().toString());
                ((Row) win.getFellow("rwTaxamulta30")).setVisible(false);
                ((Row) win.getFellow("rwTaxamulta15")).setVisible(false);
                ((Div) win.getFellow("prazo")).setVisible(false);
                ((Row) win.getFellow("prazomat")).setVisible(false);
                ((Row) win.getFellow("rwmulta")).setVisible(false);
                renovar(todo, win, lb, index);
//                } else {
//                    if (dano.after(planificacaoAnoLectivo.getDataFinalMatricula())
//                            && c.getTime().before(planificacaoAnoLectivo.getDataFimMatriculaE2())) {
//
//                        Calendar c1 = Calendar.getInstance();
//                        c1.setTime(planificacaoAnoLectivo.getDataFinalMatricula());
//                        c1.add(Calendar.DAY_OF_MONTH, 15);
//                        if (c1.getTime().after(c.getTime())) {
            } else if ((dano.after(planificacaoAnoLectivo.getDataFinalMatricula()) && dano.before(planificacaoAnoLectivo.getDataFimMatriculaE2())) || (todo != null && todo.getPeriodo() != null && todo.getPeriodo().equals("M15"))) {

                // Messagebox.show("Periodo de Multa 15 dias = " + planificacaoAnoLectivo.getPercentagemMultaMatricula15dias());
                final HashMap<String, Object> map = new HashMap<String, Object>();
//                                map.put("matricula", todo);
//                                map.put("controler", "esira.matricula.RenovarMatriculaController");
                map.put("periodo", "M15");
                // Window win = (Window) Executions.createComponents("/matricula/Matricula.zul", null, map);
                win.setParent(windo);
                win.doModal();
                ((Label) win.getFellow("taxamultas")).setValue("M15");
                ((Label) win.getFellow("lbtaxaMatricula")).setValue(planificacaoAnoLectivo.getTaxaMatriculaNacional().toString());
                ((Label) win.getFellow("lbtaxaMatriculaE")).setValue(planificacaoAnoLectivo.getTaxaMatriculaEstrangeiro().toString());
                ((Row) win.getFellow("rwTaxamulta15")).setVisible(true);
                ((Row) win.getFellow("rwTaxamulta30")).setVisible(false);
                ((Div) win.getFellow("prazo")).setVisible(false);
                ((Row) win.getFellow("prazomat")).setVisible(false);
                ((Row) win.getFellow("rwmulta")).setVisible(false);
                renovar(todo, win, lb, index);
            } else if ((dano.after(planificacaoAnoLectivo.getDataFimMatriculaE2()) && dano.before(planificacaoAnoLectivo.getDm1())) || (todo != null && todo.getPeriodo() != null && todo.getPeriodo().equals("M30"))) {
                // Messagebox.show("Periodo de Multa 30 dias = " + planificacaoAnoLectivo.getPercentagemMultaMatricula30dias());

                final HashMap<String, Object> map = new HashMap<String, Object>();
                // map.put("matricula", todo);
                //  map.put("controler", "esira.matricula.RenovarMatriculaController");
                map.put("periodo", "M30");
                // Window win = (Window) Executions.createComponents("/matricula/Matricula.zul", null, map);
                win.setParent(windo);
                win.doModal();
                ((Label) win.getFellow("taxamultas")).setValue("M30");
                ((Label) win.getFellow("lbtaxaMatricula")).setValue(planificacaoAnoLectivo.getTaxaMatriculaNacional().toString());
                ((Label) win.getFellow("lbtaxaMatriculaE")).setValue(planificacaoAnoLectivo.getTaxaMatriculaEstrangeiro().toString());
                //    ((Label) win.getFellow("lbtaxaMuniversidade")).setValue(planificacaoAnoLectivo.getTaxaDeMudancaoTurno().toString());
                ((Row) win.getFellow("rwTaxamulta30")).setVisible(true);
                ((Row) win.getFellow("rwTaxamulta15")).setVisible(false);
                ((Div) win.getFellow("prazo")).setVisible(false);
                ((Row) win.getFellow("prazomat")).setVisible(false);
                ((Row) win.getFellow("rwmulta")).setVisible(false);
                renovar(todo, win, lb, index);
            } else {
                //  Messagebox.show("Periodo de Multa 30 dias = " + planificacaoAnoLectivo.getPercentagemMultaMatricula30dias());
                if (usr.getUestudante()) {
                    Clients.showNotification("Periodo de Matricula encerrado! Por favor contacte a Direção...", "warning", null, null, 0);
                    return;
                }
                final HashMap<String, Object> map = new HashMap<String, Object>();
                // map.put("matricula", todo);
                //  map.put("controler", "esira.matricula.RenovarMatriculaController");
                map.put("periodo", "M30");
                // Window win = (Window) Executions.createComponents("/matricula/Matricula.zul", null, map);
                win.setParent(windo);
                win.doModal();
                ((Label) win.getFellow("taxamultas")).setValue("M30");
                ((Label) win.getFellow("lbtaxaMatricula")).setValue(planificacaoAnoLectivo.getTaxaMatriculaNacional().toString());
                ((Label) win.getFellow("lbtaxaMatriculaE")).setValue(planificacaoAnoLectivo.getTaxaMatriculaEstrangeiro().toString());
                //    ((Label) win.getFellow("lbtaxaMuniversidade")).setValue(planificacaoAnoLectivo.getTaxaDeMudancaoTurno().toString());
                ((Row) win.getFellow("rwTaxamulta30")).setVisible(false);
                ((Row) win.getFellow("rwTaxamulta15")).setVisible(false);
                ((Div) win.getFellow("prazo")).setVisible(true);
                ((Row) win.getFellow("prazomat")).setVisible(true);
                ((Row) win.getFellow("rwmulta")).setVisible(true);
                ((Intbox) win.getFellow("ibmulta")).setValue(0);
                renovar(todo, win, lb, index);
            }
        }
    }

    public void renovar(Matricula mat, Window win, Listbox lb, int index) throws IOException {
        if (index != -1) {
            ((Intbox) win.getFellow("litem")).setValue(index);
            ((Label) win.getFellow("lbf")).setValue(lb.getId());
        }
        if (!mat.getEstado()) {
            win.setTitle("Editar Matricula");
        }
        ((Button) win.getFellow("btnsave")).setDisabled(false);
        ((Tab) win.getFellow("tabDadosPessoais")).setSelected(true);
        if (usr.getUestudante()) {
            ((Row) win.getFellow("rwnrm")).setVisible(false);
            ((Button) win.getFellow("btnsave")).setLabel("Enviar");
        } else {
            ((Row) win.getFellow("rwnrm")).setVisible(false);
        }
        Estudante estudante = mat.getEstudante();
        estudante = csimpm.load(Estudante.class, estudante.getIdEstudante());
       // par.clear();
       // par.put("ide", estudante);
       // Prescricao pre = csimpm.findEntByJPQuery("from Prescricao p where p.inscricaodisciplina.inscricao.idEstudante = :ide"
      //          + " and p.estado is true", par);
       // if (pre != null) {
       //     Clients.showNotification("O estudante prescreveu uma disciplina! So poderá Matricular depois de regularizar", "error", null, null, 0);
       //     win.detach();
       // }
        Bolsa bols = estudante.getBolsa();
        Long cursocurrente = estudante.getCursocurrente().getIdCurso();
        Long cursoingresso = estudante.getCursoingresso().getIdCurso();
        Estadocivil estadoc = estudante.getEstadoCivil();
        Pais escolap = estudante.getEscolaPais();
        Provincia escolaprov = estudante.getEscolaprovincia();
        int nacionalidade = estudante.getNacionalidade().getIdPais();
        Viaingresso ving = estudante.getViaIngresso();
        Provincia provnat = estudante.getProvincia();
        Endereco endereco = estudante.getEndereco();
        Enderecof enderecof = estudante.getEnderecof();
        Ingressoexameadmissao iex = estudante.getIngressoexameadmissao();
        Ingressomudancauniversidade imu = estudante.getIngressomudancauniversidade();
        Ingressobolseiro ibo = estudante.getIngressobolseiro();
        Documento doc = estudante.getDocumento();
        Profissao prof = estudante.getProfissao();

        ((Intbox) win.getFellow("ibidEstudante")).setValue(estudante.getIdEstudante().intValue());
        ((Intbox) win.getFellow("ibano")).setValue(mat.getMatriculaPK().getAno());
        ((Textbox) win.getFellow("tbnomeCompleto")).setValue(estudante.getNomeCompleto());
        ((Textbox) win.getFellow("tbapelido")).setValue(estudante.getApelido());
        final Iterator<Comboitem> itemscursocurrente = new ArrayList(((Combobox) win.getFellow("cbcursocurrente")).getItems()).listIterator();
        Comboitem citcursocurrente;
        while (itemscursocurrente.hasNext()) {
            citcursocurrente = itemscursocurrente.next();
            if (Objects.equals(((Curso) citcursocurrente.getValue()).getIdCurso(), cursocurrente)) {
                ((Combobox) win.getFellow("cbcursocurrente")).setSelectedItem(citcursocurrente);
                break;
            }
        }
        ((Datebox) win.getFellow("dabdataNascimento")).setValue(estudante.getDataNascimento());
        ((Combobox) win.getFellow("cbcursocurrente")).setReadonly(true);
        ((Combobox) win.getFellow("cbcursocurrente")).setButtonVisible(false);
        Combobox cbt = (Combobox) win.getFellow("cbTurno");
        Comboitem cbt1 = cbt.getItems().get(0);
        Comboitem cbt2 = cbt.getItems().get(1);
        if (estudante.getTurno() == 1) {
            cbt.setSelectedItem(cbt1);
        } else {
            cbt.setSelectedItem(cbt2);
        }
        if (!estudante.getMasculino()) {
            ((Radio) win.getFellow("rgsexo")).setChecked(false);
            ((Radio) win.getFellow("rgsm")).setChecked(true);
        } else {
            ((Radio) win.getFellow("rgsexo")).setChecked(true);
            ((Radio) win.getFellow("rgsm")).setChecked(false);
        }
        ((Textbox) win.getFellow("tbnomePai")).setValue(estudante.getNomePai());
        ((Textbox) win.getFellow("tbnomeMae")).setValue(estudante.getNomeMae());
        ((Textbox) win.getFellow("tblocalidade")).setValue(estudante.getLocalidade());
        ((Textbox) win.getFellow("tbdistrito")).setValue(estudante.getDistrito());
        ((Intbox) win.getFellow("ibanoTerMedio")).setValue(estudante.getAnoTerMedio());
        ((Textbox) win.getFellow("tbescola")).setValue(estudante.getEscola());
        //  ((Intbox) win.getFellow("ibanoAdmissao")).setValue(estudante.getAnoAdmissao());
        // ((Intbox) win.getFellow("ibnivelFrequencia")).setValue(estudante.getNivelFrequencia());
        //  ((Textbox) win.getFellow("tbpastaDocumento")).setValue(estudante.getPastaDocumento());
        ((Textbox) win.getFellow("tbnomeEncarregado")).setValue(estudante.getNomeEncarregado());
        ((Textbox) win.getFellow("tbcontactoEncarregado")).setValue(estudante.getContactoEncarregado());
        ((Textbox) win.getFellow("tbgrauParentesco")).setValue(estudante.getGrauParentesco());
        final Iterator<Comboitem> itemsnacionalidade = new ArrayList(((Combobox) win.getFellow("cbnacionalidade")).getItems()).listIterator();
        Comboitem citnacionalidade;
        while (itemsnacionalidade.hasNext()) {
            citnacionalidade = itemsnacionalidade.next();
            if (((Pais) citnacionalidade.getValue()).getIdPais() == nacionalidade) {
                ((Combobox) win.getFellow("cbnacionalidade")).setSelectedItem(citnacionalidade);
                break;
            }
        }
        ((Textbox) win.getFellow("tbemail")).setValue(estudante.getEmail());
        if (estudante.getPrimeiraUniversidade() != null) {
            ((Radio) win.getFellow("rgNuni")).setChecked(true);
            ((Radio) win.getFellow("rgSuni")).setChecked(false);
            ((Row) win.getFellow("rwPuni")).setVisible(true);
            ((Textbox) win.getFellow("tbprimeiraUniversidade")).setValue(estudante.getPrimeiraUniversidade());
        } else {
            ((Radio) win.getFellow("rgNuni")).setChecked(false);
            ((Row) win.getFellow("rwPuni")).setVisible(false);
            ((Radio) win.getFellow("rgSuni")).setChecked(true);
        }

        ((Textbox) win.getFellow("tbidioma")).setValue(estudante.getIdioma());

        if (estadoc != null) {
            int estadoCivil = estadoc.getIdEstado();
            final Iterator<Comboitem> itemsestadoCivil = new ArrayList(((Combobox) win.getFellow("cbestadoCivil")).getItems()).listIterator();
            Comboitem citestadoCivil;
            while (itemsestadoCivil.hasNext()) {
                citestadoCivil = itemsestadoCivil.next();
                if (((Estadocivil) citestadoCivil.getValue()).getIdEstado() == estadoCivil) {
                    ((Combobox) win.getFellow("cbestadoCivil")).setSelectedItem(citestadoCivil);
                    break;
                }
            }
        }
///// Endereco
        ((Textbox) win.getFellow("tbavenidaRua")).setValue(endereco.getAvenidaRua());
        ((Textbox) win.getFellow("tbbairro")).setValue(endereco.getBairro());
        ((Textbox) win.getFellow("tbtelefone")).setValue(endereco.getTelefone());
        final Iterator<Comboitem> itemsprovincia = new ArrayList(((Combobox) win.getFellow("cbprovinciaEndAL")).getItems()).listIterator();
        Comboitem citprovincia;
        while (itemsprovincia.hasNext()) {
            citprovincia = itemsprovincia.next();
            if (Objects.equals(((Provincia) citprovincia.getValue()).getIdProvincia(), endereco.getProvincia().getIdProvincia())) {
                ((Combobox) win.getFellow("cbprovinciaEndAL")).setSelectedItem(citprovincia);
                break;
            }
        }

/////Endereco Ferias
        ((Textbox) win.getFellow("tbavenidaRua2")).setValue(enderecof.getAvenidaRua());
        ((Textbox) win.getFellow("tbbairro2")).setValue(enderecof.getBairro());
        ((Textbox) win.getFellow("tbtelefone2")).setValue(enderecof.getTelefone());
        final Iterator<Comboitem> itemsprovincia2 = new ArrayList(((Combobox) win.getFellow("cbprovinciaEndPF")).getItems()).listIterator();
        Comboitem citprovincia2 = null;
        while (itemsprovincia2.hasNext()) {
            citprovincia2 = itemsprovincia2.next();
            if (Objects.equals(((Provincia) citprovincia2.getValue()).getIdProvincia(), enderecof.getProvincia().getIdProvincia())) {
                ((Combobox) win.getFellow("cbprovinciaEndPF")).setSelectedItem(citprovincia2);
                break;
            }
        }

        ((Intbox) win.getFellow("ibnotaAdmissao")).setValue(estudante.getNotaAdmissao());
        if (escolap != null) {
            int escolaPais = escolap.getIdPais();
            final Iterator<Comboitem> itemsescolaPais = new ArrayList(((Combobox) win.getFellow("cbescolaPais")).getItems()).listIterator();
            Comboitem citescolaPais;
            while (itemsescolaPais.hasNext()) {
                citescolaPais = itemsescolaPais.next();
                if (((Pais) citescolaPais.getValue()).getIdPais() == escolaPais) {
                    ((Combobox) win.getFellow("cbescolaPais")).setSelectedItem(citescolaPais);
                    break;
                }
            }
        }
        if (escolaprov != null) {
            Long escolaprovincia = escolaprov.getIdProvincia();
            final Iterator<Comboitem> itemsescolaProv = new ArrayList(((Combobox) win.getFellow("cbescolaProvincia")).getItems()).listIterator();
            Comboitem citescolaProv;
            while (itemsescolaProv.hasNext()) {
                citescolaProv = itemsescolaProv.next();
                if (Objects.equals(((Provincia) citescolaProv.getValue()).getIdProvincia(), escolaprovincia)) {
                    ((Combobox) win.getFellow("cbescolaProvincia")).setSelectedItem(citescolaProv);
                    break;
                }
            }
        }

        ///Provincia naturalidade
        if (provnat != null) {
            long pn = provnat.getIdProvincia();
            final Iterator<Comboitem> itemsprovnat = new ArrayList(((Combobox) win.getFellow("cbnaturalidade")).getItems()).listIterator();
            Comboitem cprov;
            while (itemsprovnat.hasNext()) {
                cprov = itemsprovnat.next();
                if (((Provincia) cprov.getValue()).getIdProvincia() == pn) {
                    ((Combobox) win.getFellow("cbnaturalidade")).setSelectedItem(cprov);
                    break;
                }
            }
            ((Combobox) win.getFellow("cbnaturalidade")).setReadonly(true);
            if (pn == 12) {
                ((Textbox) win.getFellow("tbnaturalidade")).setValue(estudante.getNaturalidade());
                ((Row) win.getFellow("rwnaturalidade")).setVisible(true);
            } else {
                ((Row) win.getFellow("rwnaturalidade")).setVisible(false);
            }
        }

        //// VIAS DE INGRESSO
        if (ving != null) {
            int viaIngresso = ving.getIdViaIngresso();
            final Iterator<Comboitem> itemsviaIngresso = new ArrayList(((Combobox) win.getFellow("cbviaIngresso")).getItems()).listIterator();
            Comboitem citviaIngresso;
            while (itemsviaIngresso.hasNext()) {
                citviaIngresso = itemsviaIngresso.next();
                if (((Viaingresso) citviaIngresso.getValue()).getIdViaIngresso() == viaIngresso) {
                    ((Combobox) win.getFellow("cbviaIngresso")).setSelectedItem(citviaIngresso);
                    break;
                }
            }
            ((Combobox) win.getFellow("cbviaIngresso")).setReadonly(true);
        }
        if (iex != null) {
            final Iterator<Comboitem> itemsprovinciaAdmissao = new ArrayList(((Combobox) win.getFellow("cbprovinciaAdmissao")).getItems()).listIterator();
            Comboitem citprovinciaAdmissao;
            while (itemsprovinciaAdmissao.hasNext()) {
                citprovinciaAdmissao = itemsprovinciaAdmissao.next();
                if (Objects.equals(((Provincia) citprovinciaAdmissao.getValue()).getIdProvincia(), iex.getProvinciaAdmissao().getIdProvincia())) {
                    ((Combobox) win.getFellow("cbprovinciaAdmissao")).setSelectedItem(citprovinciaAdmissao);
                    break;
                }
            }
            ((Row) win.getFellow("rwPexameA")).setVisible(true);
        } else if (imu != null) {
            ((Textbox) win.getFellow("tbnomeUniversidade")).setValue(imu.getNomeUniversidade());
            final Iterator<Comboitem> itemsTipou = new ArrayList(((Combobox) win.getFellow("cbtipouniversidade")).getItems()).listIterator();
            Comboitem citTipou;
            while (itemsTipou.hasNext()) {
                citTipou = itemsTipou.next();
                if (citTipou.getLabel().equals(imu.getTipouniversidade())) {
                    ((Combobox) win.getFellow("cbtipouniversidade")).setSelectedItem(citTipou);
                    break;
                }
            }
            final Iterator<Comboitem> itemspaisUniversidade = new ArrayList(((Combobox) win.getFellow("cbpaisUniversidade")).getItems()).listIterator();
            Comboitem citpaisUniversidade;
            while (itemspaisUniversidade.hasNext()) {
                citpaisUniversidade = itemspaisUniversidade.next();
                if (Objects.equals(((Pais) citpaisUniversidade.getValue()).getIdPais(), imu.getPaisUniversidade().getIdPais())) {
                    ((Combobox) win.getFellow("cbpaisUniversidade")).setSelectedItem(citpaisUniversidade);
                    break;
                }
            }
            ((Row) win.getFellow("rwUmudanca")).setVisible(true);
            ((Row) win.getFellow("rwTipoU")).setVisible(true);
            ((Row) win.getFellow("rwPaisUmudanca")).setVisible(true);
        } else {
            // if (estudante.getOutraViaIngresso() != null) {
            ((Textbox) win.getFellow("tboutraViaIngresso")).setValue(estudante.getOutraViaIngresso());
            ((Row) win.getFellow("rwoutraVia")).setVisible(true);

            //}
        }

        ((Intbox) win.getFellow("ibtamAgregadoFamiliar")).setValue(estudante.getTamAgregadoFamiliar());
//////bolsa
        if (bols != null) {
            Long bolsa = bols.getIdBolsa();
            final Iterator<Comboitem> itemsbolsa = new ArrayList(((Combobox) win.getFellow("cbbolsa")).getItems()).listIterator();
            Comboitem citbolsa;
            while (itemsbolsa.hasNext()) {
                citbolsa = itemsbolsa.next();
                if (Objects.equals(((Bolsa) citbolsa.getValue()).getIdBolsa(), bolsa)) {
                    ((Combobox) win.getFellow("cbbolsa")).setSelectedItem(citbolsa);
                    break;
                }
            }
        }
        if (ibo != null) {
            ((Datebox) win.getFellow("dabdataAprovacao")).setValue(ibo.getDataAprovacao());
            ((Textbox) win.getFellow("tbdoador")).setValue(ibo.getDoador());
            ((Row) win.getFellow("rwdataDoador")).setVisible(true);
            ((Row) win.getFellow("rwdataAprovacao")).setVisible(true);
        }
        // Messagebox.show(prof.getDescricaopr());
        if (prof != null) {
            ((Radio) win.getFellow("rgNP")).setChecked(false);
            ((Radio) win.getFellow("rgP")).setChecked(true);
            ((Textbox) win.getFellow("tbdescricaopr")).setValue(prof.getDescricaopr());
            ((Textbox) win.getFellow("tbavenidapr")).setValue(prof.getAvenidapr());
            ((Textbox) win.getFellow("tbcontactopr")).setValue(prof.getContactopr());
            ((Textbox) win.getFellow("tbbairropr")).setValue(prof.getBairropr());
//            ((Textbox) win.getFellow("tbdescricaopr")).setValue(prof.getDescricaopr());
            // ((Textbox) win.getFellow("tblocal")).setValue(prof.getLocal());
            final Iterator<Comboitem> itemsprovinciapr = new ArrayList(((Combobox) win.getFellow("cbprovinciapr")).getItems()).listIterator();
            Comboitem citprovinciapr;
            while (itemsprovinciapr.hasNext()) {
                citprovinciapr = itemsprovinciapr.next();
                if (Objects.equals(((Provincia) citprovinciapr.getValue()).getIdProvincia(), prof.getProvinciapr().getIdProvincia())) {
                    ((Combobox) win.getFellow("cbprovinciapr")).setSelectedItem(citprovinciapr);
                    break;
                }
            }
            ((Row) win.getFellow("rwProfissao")).setVisible(true);
        }

        par.clear();
        par.put("ide", estudante);
        Arquivoestudante am = csimpm.findEntByJPQuery("from Arquivoestudante am where"
                + " am.tipo=0 and am.idEstudante = :ide", par);
        if (am != null) {
            String ss = UserAutentic.getPathR(am.getTipo().toString()) + estudante.getNrEstudante() + am.getNomearquivo();
            //File f = new File(ss);
            InputStream is = new FileInputStream(ss);
            Media p = new AMedia(ss, FilenameUtils.getExtension(ss), URLConnection.guessContentTypeFromName(ss), is);
            Media m = new AMedia(am.getNomearquivo(), FilenameUtils.getExtension(ss), URLConnection.guessContentTypeFromName(ss), p.getByteData());
//        List<Media> lmed = new ArrayList<Media>();
//        lm.add(m);
            ((Listitem) win.getFellow("lif")).setValue(m);//.setModel(new ListModelList<Media>(lm));
//        Image i = new AImage(ss);
            Image ii = new AImage(ss);
            ((org.zkoss.zul.Image) win.getFellow("picss").getChildren().get(0)).setContent(ii);
            ((Textbox) win.getFellow("tbimg")).setValue(ii.getName());
        }
        /////Tipo documento
        ((Datebox) win.getFellow("dabdataEmissao")).setValue(doc.getDataEmissao());
        ((Textbox) win.getFellow("tblocalEmissao")).setValue(doc.getLocalEmissao());
        ((Textbox) win.getFellow("tbnrDocumento")).setValue(doc.getNrDocumento());
        final Iterator<Comboitem> itemstipo = new ArrayList(((Combobox) win.getFellow("cbtipo")).getItems()).listIterator();
        Comboitem cittipo;
        while (itemstipo.hasNext()) {
            cittipo = itemstipo.next();
            if (Objects.equals(((Tipodocumento) cittipo.getValue()).getIdTipo(), doc.getTipo().getIdTipo())) {
                ((Combobox) win.getFellow("cbtipo")).setSelectedItem(cittipo);
                break;
            }
        }
        //bilhete
        List<Arquivoestudante> lam = csimpm.findByJPQuery("from Arquivoestudante am where"
                + " am.tipo=1 and am.idEstudante = :ide", par);
        List<Media> lm = new ArrayList<>();
        for (Arquivoestudante am2 : lam) {
            String sss = UserAutentic.getPathR(am2.getTipo().toString()) + estudante.getNrEstudante() + am2.getNomearquivo();
            //File f = new File(ss);
            InputStream iss = new FileInputStream(sss);
            //  ByteOutputStream by= new 
            Media pp = new AMedia(sss, FilenameUtils.getExtension(sss), URLConnection.guessContentTypeFromName(sss), iss);
            Media mm = new AMedia(am2.getNomearquivo(), FilenameUtils.getExtension(sss), URLConnection.guessContentTypeFromName(sss), pp.getByteData());
//Media mmm = new

            //Image ii = new AImage(sss);
            //((org.zkoss.zul.Image) win.getFellow("picss").getChildren().get(0)).setContent(i);
            ((Textbox) win.getFellow("tbimg2")).setValue(am2.getNomearquivo());
            lm.add(mm);
            //  iss.close();
        }
        ((Listbox) win.getFellow("lbimg2")).setModel(new ListModelList(lm));
        //Certificado
        List<Arquivoestudante> lam2 = csimpm.findByJPQuery("from Arquivoestudante am where"
                + " am.tipo=2 and am.idEstudante = :ide", par);
        List<Media> lm2 = new ArrayList<>();
        for (Arquivoestudante am2 : lam2) {
            String sss = UserAutentic.getPathR(am2.getTipo().toString()) + estudante.getNrEstudante() + am2.getNomearquivo();
            //File f = new File(ss);
            InputStream iss = new FileInputStream(sss);
            Media pp = new AMedia(sss, FilenameUtils.getExtension(sss), URLConnection.guessContentTypeFromName(sss), iss);
            Media mm = new AMedia(am2.getNomearquivo(), FilenameUtils.getExtension(sss), URLConnection.guessContentTypeFromName(sss), pp.getByteData());
            //Image ii = new AImage(sss);
            //((org.zkoss.zul.Image) win.getFellow("picss").getChildren().get(0)).setContent(i);
            ((Textbox) win.getFellow("tbimg3")).setValue(am2.getNomearquivo());
            lm2.add(mm);
            //   iss.close();
        }
        ((Listbox) win.getFellow("lbimg1")).setModel(new ListModelList(lm2));
        ((Row) win.getFellow("rwRU")).setVisible(false);
        if (!mat.getEstado()) { //quando se esta a editar
            //recibos
            par.clear();
            par.put("mat", mat);
            List<Arquivomatricula> lam3 = csimpm.findByJPQuery("from Arquivomatricula am where"
                    + " am.matricula = :mat", par);
            List<Arquivo> lm3 = new ArrayList<>();
            List<Arquivo> lm4 = new ArrayList<>();
            for (Arquivomatricula am3 : lam3) {
                String sss = UserAutentic.getPathR("3") + estudante.getNrEstudante() + mat.getMatriculaPK().getAno() + am3.getNomearquivo();
                //File f = new File(ss);
                InputStream iss = new FileInputStream(sss);
                Media pp = new AMedia(sss, FilenameUtils.getExtension(sss), URLConnection.guessContentTypeFromName(sss), iss);
                Media mm = new AMedia(am3.getNomearquivo(), FilenameUtils.getExtension(sss), URLConnection.guessContentTypeFromName(sss), pp.getByteData());
                //Image ii = new AImage(sss);
                //((org.zkoss.zul.Image) win.getFellow("picss").getChildren().get(0)).setContent(i);
                ((Textbox) win.getFellow("tbimg4")).setValue(mm.getName());
                if (!am3.getNrtalao().equals("0")) {
                    lm3.add(new Arquivo(am3.getNrtalao(), mm, am3.getNomearquivo(), am3.getBanco(), am3.getValor(), am3.getEstudante(), am3.getDatadeposito()));
                } else {
                    lm4.add(new Arquivo(am3.getNrtalao(), mm, am3.getNomearquivo(), am3.getBanco(), am3.getValor(), am3.getEstudante(), am3.getDatadeposito()));
                }
            }
            ((Listbox) win.getFellow("lbimg")).setModel(new ListModelList(lm3));
            ((Listbox) win.getFellow("lbimgII")).setModel(new ListModelList(lm4));
        }
    }

//final Iterator<Comboitem> itemscursoingresso = new ArrayList(((Combobox) win.getFellow("cbcursoingresso")).getItems()).listIterator();
//        Comboitem citcursoingresso;
//        while (itemscursoingresso.hasNext()) {
//            citcursoingresso = itemscursoingresso.next();
//            if (((Curso) citcursoingresso.getValue()).getIdCurso() == cursoingresso) {
//                ((Combobox) win.getFellow("cbcursoingresso")).setSelectedItem(citcursoingresso);
//                break;
//            }
//        }
//
//}
    public void onCreate$vb4() {
        if (usr.getUestudante()) {
            Date datactual = new Date();
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(datactual);
            int anoActual = calendar.get(Calendar.YEAR);
            Users user = csimpm.get(Users.class, usr.getUtilizador());
            MatriculaPK mpk = new MatriculaPK(user.getIdEstudante().getIdEstudante(), anoActual);
            par.clear();
            par.put("mpk", mpk);
            Matricula m = csimpm.findEntByJPQuery("from Matricula m where m.matriculaPK = :mpk", par);
            if (m == null) {
                vb1.setVisible(true);
                btnMatric.setVisible(true);
            } else {
                if (m.getConfirmacao() == null) {
                    vb3.setVisible(true);
                    btnMatric.setVisible(false);
                } else {
                    if (!m.getEstado()) {
                        //ide.setValue(m.getEstudante().getIdEstudante().intValue());
                        ((Intbox) win.getFellow("ibidEstudante")).setValue(m.getEstudante().getIdEstudante().intValue());
                        ((Intbox) win.getFellow("ibano")).setValue(m.getMatriculaPK().getAno());
                        vb4.setVisible(true);
                        if (m.getMotivomat() != null) {
                            lbmot.setValue(m.getMotivomat().getMotivo());
                        }
                        btnMatric.setVisible(true);
                    } else {
                        vb2.setVisible(true);
                        btnMatric.setVisible(false);
                    }
                }
                if (m.getAnulada()) {
                    lba.setValue("A sua matricula esta anulada.");
                    btnAnular.setVisible(false);
                    btnMatric.setVisible(true);
                } else {
                    Matriculaanulada ma = m.getMatriculaanulada();
                    if (ma != null) {
                        if (ma.getDataconfirmacao() == null) {
                            lba.setValue("O Seu pedido para anular a Matricula foi enviado. \n Por favor aguarde a confirmação...");
                            btnAnular.setVisible(false);
                        } else if (!ma.getEstado()) {
                            winanularmot.setVisible(true);
                            winanularmot.setTitle(ma.getMotivo());
                        }
                    }
                }
            }
        }
    }

    public void onClose$winanularmot(Event evt) {
        Users user = csimpm.get(Users.class, usr.getUtilizador());
        par.clear();
        par.put("ide", user.getIdEstudante().getIdEstudante());
        Matricula ma = csimpm.findEntByJPQuery("select k from Matricula k where k.dataMatricula in (select max(m.dataMatricula) from Matricula m "
                + "WHERE m.matriculaPK.idEstudante =:ide) and k.matriculaPK.idEstudante = :ide", par);
        Matriculaanulada man = ma.getMatriculaanulada();
        ma.setMatriculaanulada(null);
        csimpm.delete(man);
    }

    public ListModel<Matriculaanulada> getListManuladaModel() {
        Calendar c = Calendar.getInstance();
        Map<String, Object> par = new HashMap<String, Object>();
        par.clear();
       // Users u = csimpm.get(Users.class, usr.getUtilizador());
        par.clear();
        Faculdade f = csimpm.get(Faculdade.class, usr.getFaculdade().getIdFaculdade());
        par.put("fac", f);
        List<Matriculaanulada> lma = csimpm.findByJPQuery("from Matriculaanulada ma where ma.dataconfirmacao is not null and ma.estado is true and"
                + " ma.matricula.curso.faculdade = :fac order by ma.dataAnulacao desc", par);
        return new ListModelList<>(lma);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////
    public ListModel<Curso> getListaCursoModel() {
       //Users u = csimpm.get(Users.class, usr.getUtilizador());
        par.clear();
        Faculdade f = csimpm.get(Faculdade.class, usr.getFaculdade().getIdFaculdade());
        par.put("fac", f);
        List<Curso> lc = csimpm.findByJPQuery("from Curso c where c.faculdade = :fac", par);
        return listaCursoModel = new ListModelList<Curso>(lc);
    }

    public void onChanging$txPNrmec(InputEvent evt) {
        if (!evt.getValue().equals("") && evt.getValue().charAt(0) != '.') {
            condnr = " and m.estudante.nrEstudante = :nr";
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
        addeventoLB(lbEstudantes, null, Matricula.class);
    }

    public void onChanging$txProcurar(InputEvent evt) {
        if (!evt.getValue().equals("") && evt.getValue().charAt(0) != '.') {
            condnome = " and lower(m.estudante.nomeCompleto) like :nome";
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
        addeventoLB(lbEstudantes, null, Matricula.class);
    }

    public void onChanging$ibPAno(InputEvent evt) {
        if (!evt.getValue().equals("") && evt.getValue().charAt(0) != '.') {
            condanoi = " and m.matriculaPK.ano = :anoi";
            condpar.put("anoi", Integer.parseInt(evt.getValue()));
        } else {
            condanoi = "";
            condpar.remove("anoi");
        }
        addeventoLB(lbEstudantes, null, Matricula.class);
    }

    public void onChange$cbcurso() {
        // if (cbcurso.getSelectedIndex() != 0) {
        condcurso = " and m.curso = :curso";
        condpar.replace("curso", (Curso) cbcurso.getSelectedItem().getValue());
//            condfac = "";
//            condpar.remove("fac");
//        } else {
//            condcurso = " and m.curso = :curso";
//            condpar.remove("curso");
//            //cbcurso.setVisible(false);
//        }
        addeventoLB(lbEstudantes, null, Matricula.class);
    }

    public void initLB(Listbox lb, Paging p, Class o) {
        //p.setTotalSize(csimpm.count(o));
        //final int PAGE_SIZE = p.getPageSize();
        setLBModel(lb, o, 0, 20);
    }

    public void setLBModel(Listbox lb, Class c, int o, int p) {
        Calendar ca = Calendar.getInstance();
        if (p == 20) {
            lb.setModel(new ListModelList<Matricula>());
        }
        listaM = new ArrayList<>();
        //while(listaM!=null){
        condpar.put("ano", ca.get(Calendar.YEAR));
        listaM = csimpm.findByJPQueryFilter("select k from Matricula k where k.estudante.estado is true and "
                + "k.dataMatricula in (select max(m.dataMatricula) from Matricula m "
                + "WHERE 1=1" + condnr + condnome + condcurso + " and m.anulada is false and"
                + " (m.matriculaPK.ano =:ano and m.confirmacao is not null and m.estado is false or"
                + " m.matriculaPK.idEstudante NOT IN (SELECT m.matriculaPK.idEstudante FROM Matricula m WHERE m.matriculaPK.ano = :ano)) group by"
                + " m.matriculaPK.idEstudante) order by k.estudante.nomeCompleto asc", condpar, o, p);
//        List<Matricula> lm = csimpm.findByJPQuery("from Matricula m "
//                + "WHERE 1=1" + condnr + condnome + condcurso + " and m.matriculaPK.ano =:ano and m.confirmacao is not null and m.estado is false", condpar);
//        listaM.addAll(lm);
        //List<Matricula> estudantes = csimpm.findByJPQuery("from Matricula m where 1=1 and m.estado is true and m.anulada is false" + condnome + condnr + condanoi + condcurso + ord, condpar);
        final Iterator<Matricula> items = listaM.iterator();
        Matricula m;
        while (items.hasNext()) {
            m = items.next();
//           par.clear();
//           par.put("mpk", m.getMatriculaPK());
//           m = csimpm.findEntByJPQuery("from Matricula m where m.matriculaPK =:mpk", par);
            ((ListModelList) lb.getModel()).add(m);
            // }
        }
        if (listaM.size() < p) {
            btv.setVisible(false);
        } else {
            btv.setVisible(true);
        }
        //lb.setModel(new ListModelList<Matricula>(listaM));
    }

    public void onLoadi() {
        int i = lbEstudantes.getItemCount();
        setLBModel(lbEstudantes, null, i, i + 20);
    }

    public void onSortEventListener(Event event, String sql, final Listbox lb, Paging p, final Class o) {
        // if(lb.getItemCount()==0)return;
        final Listheader lh = (Listheader) event.getTarget();
        final String sortDirection = lh.getSortDirection();

        if ("ascending".equals(sortDirection)) {
            ord = " order by m." + lh.getId() + " asc";
            initLB(lb, p, o);
            //p.setActivePage(0);
            // addeventoPagin(sql + ord, lb, p, o);
        } else if ("descending".equals(sortDirection) || "natural".equals(sortDirection) || Strings.isBlank(sortDirection)) {
            ord = " order by m." + lh.getId() + " desc";
            initLB(lb, p, o);
            // p.setActivePage(0);
            // addeventoPagin(sql + ord, lb, p, o);
        }
        //  Clients.evalJavaScript("scri");
    }

    public void addeventoOrd(final String sql, final Listbox lb, final Paging p, final Class o) {
        List<Listheader> list = lb.getListhead().getChildren();
        for (Object object : list) {
            if (object instanceof Listheader) {
                Listheader lheader = (Listheader) object;

                if (lheader.getSortAscending() != null || lheader.getSortDescending() != null) {

                    lheader.addEventListener("onSort", new EventListener() {
                        public void onEvent(Event event) {
                            onSortEventListener(event, sql, lb, p, o);
                        }
                    });
                }
            }
        }
    }

    public void addeventoLB(final Listbox lb, Paging p, final Class o) {
        initLB(lb, p, o);
        addeventoOrd("", lb, p, o);
        //addeventoPagin(sql, lb, p, o);
    }

    public void onExcelExportMatAnul() throws ParseException {
        if (lbAnulcao.getItemCount() == 0) {
            Clients.showNotification("Sem conteúdo", "warning", null, null, 3000);
            return;
        }
        BeanToExcel beanToExcel = new BeanToExcel();
        beanToExcel.setDataSheetName("Matriculas Anuladas");
        beanToExcel.exportExcell(lbAnulcao);
    }

    public void onGraduar(final ForwardEvent event) throws Exception {
        Listitem litem = (Listitem) event.getOrigin().getTarget().getParent().getParent().getParent();
        Matricula todo = (Matricula) litem.getValue();
//        todo = csimpm.get(Matricula.class, todo.getMatriculaPK());
        Estudante e = todo.getEstudante();

        if (e.getGraduado() != null && e.getGraduado() == true) {
            Messagebox.show("O Estudante ja pertence a lista dos graduados! /n"
                    + "Pretende retirar-lo da lista dos graduados?", "Confirmação", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION,
                    new EventListener() {
                        @Override
                        public void onEvent(Event evet) {
                            switch (((Integer) evet.getData()).intValue()) {
                                case Messagebox.YES:
                                    Listitem litem = (Listitem) event.getOrigin().getTarget().getParent().getParent().getParent();
                                    Matricula todo = (Matricula) litem.getValue();
                                    Estudante e = todo.getEstudante();
                                    e.setGraduado(false);
                                    e.setEstado(true);
                                    csimpm.update(e);
                                    // ((ListModelList) lbEstudantes.getModel()).remove(todo);
                                    Clients.showNotification(
                                            " O estudante ja pertence aos graduados", null, null, null, 1000);
                                    break;
                                case Messagebox.NO:
                                    return;
                            }
                        }
                    });
        } else {
            Messagebox.show("O Estudante sera movido para a lista dos graduados?", "Confirmação", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION,
                    new EventListener() {
                        @Override
                        public void onEvent(Event evet) {
                            switch (((Integer) evet.getData()).intValue()) {
                                case Messagebox.YES:
                                    Listitem litem = (Listitem) event.getOrigin().getTarget().getParent().getParent().getParent();
                                    Matricula todo = (Matricula) litem.getValue();
                                    Estudante e = todo.getEstudante();
                                    e.setGraduado(true);
                                    e.setEstado(false);
                                    csimpm.update(e);
                                    ((ListModelList) lbEstudantes.getModel()).remove(todo);
                                    Clients.showNotification(
                                            " O estudante ja pertence aos graduados", null, null, null, 1000);
                                    break;
                                case Messagebox.NO:
                                    return;
                            }
                        }
                    });
        }
    }

    public void onMudarTurma(ForwardEvent event) throws Exception {
        Listitem li = (Listitem) event.getOrigin().getTarget().getParent().getParent().getParent();
        Matricula m = (Matricula) li.getValue();
        m = csimpm.get(Matricula.class, m.getMatriculaPK());
        Estudante e = m.getEstudante();
        winTurma.setParent(winlestudantes);
        winTurma.setTitle("Mudar a turma do estudante");
        winTurma.doModal();
        ((Intbox) winTurma.getFellow("ide")).setValue(e.getIdEstudante().intValue());
        ((Label) winTurma.getFellow("nome")).setValue(e.getNomeCompleto());
        Combobox cbt = (Combobox) winTurma.getFellow("cbTurno");
        Comboitem cbt1 = cbt.getItems().get(0);
        Comboitem cbt2 = cbt.getItems().get(1);
//        Messagebox.show(f.getTurno()+"");
        //   Messagebox.show(cbt1.getValue().toString());
        if (e.getTurno() == 1) {
            cbt.setSelectedItem(cbt1);
            cbt.setText("Laboral");
        } else {
            cbt.setSelectedItem(cbt2);
            cbt.setText("Pos-Laboral");
        }
        ((Intbox) winTurma.getFellow("ibturma")).setValue(e.getTurma());
    }

    public void onSalvarTurma(ForwardEvent event) throws Exception {
        Messagebox.show("Mudar a turma do estudante?", "Confirmação", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION,
                new EventListener() {
                    @Override
                    public void onEvent(Event evet) {
                        switch (((Integer) evet.getData()).intValue()) {
                            case Messagebox.YES:
                                Estudante e = csimpm.get(Estudante.class, ide.getValue().longValue());
                                e.setTurma(ibturma.getValue());
                                e.setTurno(Integer.parseInt(cbTurno.getSelectedItem().getValue().toString()));
//        if()
                                csimpm.update(e);
                                Clients.showNotification(
                                        " Actualizado com sucesso", null, null, null, 1000);
                                break;
                            case Messagebox.NO:
                                return;
                        }
                    }
                });
    }

    public void onBtnCanc(ForwardEvent event) throws Exception {
        winTurma.detach();
    }
}
