/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package esira.matricula;

import ExcelExport.BeanToExcel;
import com.itextpdf.text.DocumentException;
import entidade.Arquivo;
import esira.domain.Arquivoestudante;
import esira.domain.Arquivomatricula;
import esira.domain.Bolsa;
import esira.domain.Curso;
import esira.domain.Disciplinaanulada;
import esira.domain.Documento;
import esira.domain.Endereco;
import esira.domain.Enderecof;
import esira.domain.Especial;
import esira.domain.Estadocivil;
import esira.domain.Estudante;
import esira.domain.Faculdade;
import esira.domain.Funcionario;
import esira.domain.Ingressobolseiro;
import esira.domain.Ingressoexameadmissao;
import esira.domain.Ingressomudancauniversidade;
import esira.domain.Ingressotransferencia;
import esira.domain.Inscricao;
import esira.domain.Inscricaodisciplina;
import esira.domain.Listaadmissao;
import esira.domain.Matricula;
import esira.domain.MatriculaPK;
import esira.domain.Motivomat;
import esira.domain.Notificacao;
import esira.domain.Pais;
import esira.domain.PlanificacaoAnoLectivo;
import esira.domain.Planocurricular;
import esira.domain.Prescricao;
import esira.domain.Profissao;
import esira.domain.Provincia;
import esira.domain.Tipodocumento;
import esira.domain.Users;
import esira.domain.Validacaopendente;
import esira.domain.ValidacaopendentePK;
import esira.domain.Viaingresso;
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
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.mail.EmailException;
import org.zkoss.image.AImage;
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
import org.zkoss.zul.Iframe;
import org.zkoss.zul.Image;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Menuitem;
import org.zkoss.zul.Menupopup;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Paging;
import org.zkoss.zul.Radio;
import org.zkoss.zul.Row;
import org.zkoss.zul.Script;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Vbox;
import org.zkoss.zul.Window;
import org.zkoss.zul.event.PagingEvent;

/**
 *
 * @author Tafula
 */
public class ListaEstudantesMatriculadosController extends GenericForwardComposer {

    @WireVariable
    private CRUDService csimpm = (CRUDService) SpringUtil.getBean("CRUDService");
    private Listbox lbEstudantes;
    private ListModel<Matricula> listEstudanteModel;
    private ListModel<Curso> listaCursoModel;
    Window winlestudantes, mDialogMatricula, win, winTurma, winPlano, winMot;
    private Intbox ibPAno;
    private Textbox txAno, txMotivo;
    Button btnProcurar;
    // List<Estudante> userList = new ArrayList<Estudante>(csimpm.getAll(Matricula.class));
    List listaM;
    Map<String, Object> par = new HashMap<String, Object>();
    private Paging pagEstudante;
    private Listheader lhnome;
    private Intbox litem;
    private Label lb;
    static String ord = "";
    Script scri;
    private List<Estudante> estudantes = null;
    Users usr = (Users) Sessions.getCurrent().getAttribute("user");
    private int n;
    private int pos;
    private Intbox item, idest, oldcurso, oldplano;
    Map<String, Object> ent = new HashMap<String, Object>();
    private Hlayout ahead;
    String condfac = "", condnr = "", condnome = "", condgenero = "", condanoi = "", condano = "", condcurso = "";
    Textbox txProcurar, txProcNrmec;
    Map<String, Object> condpar = new HashMap<String, Object>();
    Combobox cbcurso, cbcurso2, cbTurno, cbplano;
    private Intbox ibProcAno, anoi, ide, ibturma, ibidest, anomat;
    private Button btv;
    private Checkbox chturma;
    Menuitem manoi;
    private EventQueue eq;
    private Intbox ibnivel, oldnivel;
    private Window winNivel;

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        Date dano = new Date();
        Calendar cal = new GregorianCalendar();
        cal.setTime(dano);
        int ano = cal.get(Calendar.YEAR);
        win = (Window) Executions.createComponents("/matricula/MatriculaEdit.zul", null, null);
        win.setTitle("Editar Matricula");
        win.detach();
        condpar.clear();
        ibProcAno.setValue(ano);
        condpar.put("a", ano);
        condano = " and m.matriculaPK.ano = :a";
        condcurso = " and m.estudante.cursocurrente = :curso ";
        if (cbcurso.getModel().getSize() > 0) {
            condpar.put("curso", (Curso) cbcurso.getModel().getElementAt(0));
            setLB(0, 20);
        }
    }

    public void onSIndexCbcurso() {
        if (cbcurso.getModel() != null && cbcurso.getModel().getSize() > 0) {
            cbcurso.setSelectedIndex(0);
        }
    }

    public void onCreatCbcurso2() {
        if (cbcurso2.getModel() != null && cbcurso2.getModel().getSize() > 0) {
            cbcurso2.setSelectedIndex(0);
        }
    }

    public void onSetQueueEstMat() {
        //Users u = csimpm.get(Users.class, usr.getUtilizador());
        if (!usr.getUestudante()) {
            eq = EventQueues.lookup("mat" + usr.getFaculdade().getIdFaculdade(), EventQueues.APPLICATION, true);
            eq.subscribe(getEventoMat());
            eq = EventQueues.lookup("rmatD" + usr.getFaculdade().getIdFaculdade(), EventQueues.APPLICATION, true);
            eq.subscribe(getEventoMat());
            eq = EventQueues.lookup("rmatA" + usr.getFaculdade().getIdFaculdade(), EventQueues.APPLICATION, true);
            eq.subscribe(getEventoAnul());
            eq = EventQueues.lookup("anulaM" + usr.getFaculdade().getIdFaculdade(), EventQueues.APPLICATION, true);
            eq.subscribe(getEventoAnul());
        }
//        else {
//            eq = EventQueues.lookup("mat" + u.getIdEstudante().getIdEstudante(), EventQueues.APPLICATION, true);
//            eq.subscribe(getEventoMatE());
//            eq = EventQueues.lookup("rmatD" + u.getIdEstudante().getIdEstudante(), EventQueues.APPLICATION, true);
//            eq.subscribe(getEventoPE());
//            eq = EventQueues.lookup("rmatA" + u.getIdEstudante().getIdEstudante(), EventQueues.APPLICATION, true);
//            eq.subscribe(getEventoIAE());
//            eq = EventQueues.lookup("anulaM" + u.getIdEstudante().getIdEstudante(), EventQueues.APPLICATION, true);
//            eq.subscribe(getEventoAnulE());
//        }
    }

    public EventListener getEventoMat() { //problemas quando o estudante nao existe
        return new EventListener() {
            @Override
            public void onEvent(Event event) throws Exception {
                Matricula m = (Matricula) event.getData();
                par.clear();
                par.put("mpk", m.getMatriculaPK());
                if (winlestudantes.getParent() != null) {
                    m = csimpm.findEntByJPQuery("from Matricula m where m.matriculaPK = :mpk", par);
                    Users u = csimpm.get(Users.class, usr.getUtilizador());
                    if (m.getEstado() == true && m.getConfirmacao() != null) {
                        ((ListModelList) lbEstudantes.getModel()).add(0, m);
                    }
                }
            }
        };
    }

    public EventListener getEventoP() {
        return new EventListener() {
            @Override
            public void onEvent(Event event) throws Exception {
                Matricula m = (Matricula) event.getData();
                par.clear();
                par.put("mpk", m.getMatriculaPK());
                if (winlestudantes.getParent() != null) {
                    m = csimpm.findEntByJPQuery("from Matricula m where m.matriculaPK = :mpk", par);
                    Users u = csimpm.get(Users.class, usr.getUtilizador());
                    if (m.getEstado() == true && m.getConfirmacao() != null) {
                        int ind = ((ListModelList) lbEstudantes.getModel()).indexOf(m);
                        if (ind >= 0) {
                            ((ListModelList) lbEstudantes.getModel()).set(ind, m);
                        }
                    }
                }
            }
        };
    }

    public EventListener getEventoAnul() {
        return new EventListener() {
            @Override
            public void onEvent(Event event) throws Exception {
                Matricula m = (Matricula) event.getData();
                par.clear();
                par.put("mpk", m.getMatriculaPK());
                if (winlestudantes.getParent() != null) {
                    m = csimpm.findEntByJPQuery("from Matricula m where m.matriculaPK = :mpk", par);
                    Users u = csimpm.get(Users.class, usr.getUtilizador());
                    if (m.getAnulada()) {
                        int ind = ((ListModelList) lbEstudantes.getModel()).indexOf(m);
                        if (ind >= 0) {
                            ((ListModelList) lbEstudantes.getModel()).remove(m);
                        }
                    }
                }
            }
        };
    }

//    public ListModel<Matricula> getListEstudanteModel() {
//        par.clear();
//        par.put("user", usr.getUtilizador());
//        Users u = csimpm.findEntByJPQuery("from Users u where u.utilizador = :user", par);
//        par.clear();
//        par.put("fac", u.getFaculdade());
//        List<Curso> lc = csimpm.findByJPQuery("from Curso c where c.faculdade =:fac", par);
//        par.put("curso", lc.get(0));
//        listaM = csimpm.findByJPQuery("from Matricula m where m.curso.faculdade = :fac and m.curso=:curso and m.confirmacao is not null and m.estado is true and m.matriculaPK not in (select ma.matriculaPK from "
//                + "Matriculaanulada ma)", par);
//        return listEstudanteModel = new ListModelList<Matricula>(listaM);
//    }
    public ListModel<Curso> getListaCursoModel() {
        //Users u = csimpm.get(Users.class, usr.getUtilizador());
        List<Curso> lc = null;
        par.clear();
        if (usr.getFaculdade().getLocalizacao() == null) {
            lc = csimpm.getAll(Curso.class);
        } else {
            Faculdade f = csimpm.get(Faculdade.class, usr.getFaculdade().getIdFaculdade());
            par.put("fac", f);
            lc = csimpm.findByJPQuery("from Curso c where c.faculdade = :fac", par);
        }
        return listaCursoModel = new ListModelList<Curso>(lc);
    }

    public ListModel<Curso> getCursocModel() {
        //Users u = csimpm.get(Users.class, usr.getUtilizador());
        par.clear();
        Faculdade f = csimpm.get(Faculdade.class, usr.getFaculdade().getIdFaculdade());
        par.put("fac", f);
        List<Curso> lc = csimpm.findByJPQuery("from Curso c where c.faculdade = :fac", par);
        return listaCursoModel = new ListModelList<Curso>(lc);
    }

//    public void onInvalidar(final ForwardEvent event) throws Exception {
//        Messagebox.show("Invalidar?", "Confirmação", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION,
//                new EventListener() {
//                    @Override
//                    public void onEvent(Event evet) {
//                        switch (((Integer) evet.getData()).intValue()) {
//                            case Messagebox.YES:
//                                Listitem litem = (Listitem) event.getOrigin().getTarget().getParent().getParent().getParent();
//                                Matricula todo = (Matricula) litem.getValue();
//                                if (todo.getPeriodo() == null) {
//                                    ((ListModelList) lbEstudantes.getModel()).remove(todo);
//                                    csimpm.delete(todo);
//                                    Clients.showNotification(
//                                            " Matricula Rejeitada", null, null, null, 1000);
//                                } else {
//                                    todo.setEstado(false);
//                                    csimpm.update(todo);
//                                    ((ListModelList) lbEstudantes.getModel()).remove(todo);
//                                    Clients.showNotification(
//                                            " Matricula Rejeitada", null, null, null, 1000);
//                                }
//                                break;
//                            case Messagebox.NO:
//                                return;
//                        }
//                    }
//                });
//    }
    public void onInvalidar(ForwardEvent event) throws IOException {
        Listitem litem = (Listitem) event.getOrigin().getTarget().getParent().getParent().getParent();
        Matricula todo = (Matricula) litem.getValue();
        ((Intbox) winMot.getFellow("ibidest")).setValue(new Long(todo.getMatriculaPK().getIdEstudante()).intValue());
        ((Intbox) winMot.getFellow("anomat")).setValue(todo.getMatriculaPK().getAno());
        winMot.setParent(winlestudantes);
        winMot.setTitle("Mudar a turma do estudante");
        winMot.doModal();
        ((Textbox) winMot.getFellow("txMotivo")).setText("A sua matricula foi rejeitada.\n Os seus dados estão incorrectos");
    }

    public void onFecharmot() {
        winMot.detach();
    }

    public void onRejeitarMat() {
        Messagebox.show("Invalidar?", "Confirmação", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION,
                new EventListener() {
                    @Override
                    public void onEvent(Event evet) throws InterruptedException, IOException, EmailException {
                        switch (((Integer) evet.getData()).intValue()) {
                            case Messagebox.YES:
                                final MatriculaPK mpk = new MatriculaPK(ibidest.getValue().longValue(), anomat.getValue());
                                par.clear();
                                par.put("ide", mpk);
                                final Users u = csimpm.get(Users.class, usr.getUtilizador());
//                                Faculdade f = csimpm.get(Faculdade.class, usr.getFaculdade().getIdFaculdade());
                                final Matricula mat = (Matricula) csimpm.findEntByJPQuery("from Matricula m where m.matriculaPK=:ide", par);
//                                if (tipov.getValue() == 2) {
//                                    Notificacao n = new Notificacao(new Date());
//                                    n.setMsg(txMotivoR.getText());
//                                    n.setIdFuncionario(mat.getFuncionario());
//                                    csimpm.Save(n);
//                                    csimpm.delete(mat);
//                                } else {
                                mat.setFuncionario(u.getIdFuncionario());
                                mat.setConfirmacao(new Date());
                                mat.setEmailenviado(false);
                                mat.setEstado(false);
                                csimpm.update(mat);
                                Motivomat mm = new Motivomat();
                                mm.setMatriculaPK(mat.getMatriculaPK());
                                mm.setMotivo(txMotivo.getText());
                                mm.setMatricula(mat);//antes nao era necessario?
                                csimpm.Save(mm);
//                                }
                                ((ListModelList) lbEstudantes.getModel()).remove(mat);
                                Clients.showNotification(" Matricula Rejeitada com Sucesso", null, null, null, 2000);
                                //Substituido com eventquee
//                                new Listbox().appendChild(((Listbox) winAddMotivo.getParent().getParent().getFellow("lbEstudantes"))
//                                        .getItemAtIndex(litem.getValue()));

                                Messagebox.show("Enviar email ao estudante?", "", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION,
                                        new EventListener() {
                                            @Override
                                            public void onEvent(Event evet) throws InterruptedException, IOException, EmailException, DocumentException {
                                                switch (((Integer) evet.getData()).intValue()) {
                                                    case Messagebox.YES:
                                                        par.clear();
                                par.put("ide", mpk);
                               final Matricula mat1 = (Matricula) csimpm.findEntByJPQuery("from Matricula m where m.matriculaPK=:ide", par);
                                                        Users uu = csimpm.get(Users.class, u.getUtilizador());
                                                        Funcionario f = csimpm.get(Funcionario.class, uu.getIdFuncionario().getIdFuncionario());
                                                        Clients.evalJavaScript("var xhttp = new XMLHttpRequest();\n"
                                                                + "xhttp = new XMLHttpRequest();"
                                                                + "xhttp.open('POST', 'EnviarEmailRecibo?tipo=2&id1=" + mat1.getEstudante().getIdEstudante().intValue()
                                                                + "&id2=" + mat1.getMatriculaPK().getAno() + "&idf=" + f.getIdFuncionario().intValue() + "&ido=" + 0 + "', true);\n"
                                                                + " xhttp.send();");
                                                        break;
                                                    case Messagebox.NO:
                                                }
                                            }
                                        });
                                winMot.detach();
                                break;
                            case Messagebox.NO:
                                return;
                        }
                    }
                });
    }

    public void onAnular(ForwardEvent event) throws Exception {
        Listitem litem = (Listitem) event.getOrigin().getTarget().getParent().getParent().getParent();
        Matricula todo = (Matricula) litem.getValue();
        if (todo.getMatriculaanulada() != null && todo.getAnulada() == false) {
            Clients.showNotification("Este Estudante tem um pedido de Anulação de Matrícula pendente!", "warning", null, null, 0);
        }
        final HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("nome", todo.getEstudante().getNomeCompleto());
        map.put("cursoid", todo.getCurso().getIdCurso());
        map.put("cursonome", todo.getCurso().getDescricao());
        map.put("nrMec", todo.getEstudante().getNrEstudante());
        map.put("estudanteid", todo.getEstudante().getIdEstudante());
        map.put("ano", todo.getMatriculaPK().getAno());
        Window win = (Window) Executions.createComponents("/matricula/AnularMatricula.zul", winlestudantes, map);
        ((Intbox) win.getFellow("idlitem")).setValue(litem.getIndex());
//         Listbox l = new Listbox();
//         l.appendChild(litem);
    }

    public void onGraduar(final ForwardEvent event) throws Exception {
        Listitem litem = (Listitem) event.getOrigin().getTarget().getParent().getParent().getParent();
        Matricula todo = (Matricula) litem.getValue();
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
                                    //  ((ListModelList) lbEstudantes.getModel()).remove(todo);
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
                                    //  ((ListModelList) lbEstudantes.getModel()).remove(todo);
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

    public void setCombobox(Combobox cb, int id) {

    }

    public void onEdit(ForwardEvent event) {

        Button btn = (Button) event.getOrigin().getTarget();
        Listitem litem = (Listitem) btn.getParent().getParent();
        Matricula todo = (Matricula) litem.getValue();

        final HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("matricula", todo);
        Executions.createComponents("/matricula/EditarMatricula.zul", null, map);
    }

//    public void addeventoPagin(final String sql, final Listbox lb, Paging p, final Class o) {
//        final int PAGE_SIZE = p.getPageSize();
//        initLB(sql, lb, p, o);
//        p.addEventListener("onPaging", new EventListener() {
//            @Override
//            public void onEvent(Event event) {
//                PagingEvent pe = (PagingEvent) event;
//                int pgno = pe.getActivePage();
//                int ofs = pgno * PAGE_SIZE;
//                // Redraw current paging
//                setLBModel(sql, lb, o, ofs, PAGE_SIZE);
//            }
//        });
//    }
    public void onRelatorio() throws JRException, IOException {
        String path = winlestudantes.getDesktop().getWebApp().getRealPath("/relatorio");
        String path2 = winlestudantes.getDesktop().getWebApp().getRealPath("/img");
        Window win = (Window) Executions.createComponents("/report.zul", null, null);
        win.setTitle("Estudantes Matriculados");
        List<Matricula> lm = new ArrayList<Matricula>();
        final Iterator<Listitem> items = new ArrayList(lbEstudantes.getItems()).listIterator();
        Listitem li;
        while (items.hasNext()) {
            li = items.next();
            lm.add((Matricula) li.getValue());
        }
        JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(lm);

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("img", path2 + "/icoul.png");
        params.put("curso", "Engenharia Informatica");
        params.put("anol", ibPAno.getValue());
        JasperPrint jasperPrint = JasperFillManager.fillReport(path + "/estmatriculados.jasper", params, ds);
        JRPdfExporter exporter = new JRPdfExporter();

        ByteArrayOutputStream bytesOutputStream = new ByteArrayOutputStream();
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(bytesOutputStream);
        exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
        exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, bufferedOutputStream);
        exporter.exportReport();
        Iframe iframe = (Iframe) win.getFellow("report");
        InputStream mediais = new ByteArrayInputStream(bytesOutputStream.toByteArray());
        bytesOutputStream.close();

        AMedia amedia = new AMedia("inscricaoestudante.pdf", "pdf", "application/pdf", mediais);

        iframe.setContent(amedia);

    }

    ////////////////////////////////////PESQUISAS//////////////////////////////////////////////////////
    public void onChanging$txProcNrmec(InputEvent evt) {
        if (!evt.getValue().equals("") && evt.getValue().charAt(0) != '.') {
            condnr = " and m.estudante.nrEstudante = :nr ";
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
        anoi.setVisible(manoi.isChecked());
        if (manoi.isChecked()) {
            Date dano = new Date();
            Calendar cal = new GregorianCalendar();
            cal.setTime(dano);
            anoi.setValue(cal.get(Calendar.YEAR));
            condanoi = " and m.estudante.anoIngresso = :ai ";
            condpar.put("ai", anoi.getValue());
        } else {
            condanoi = "";
            condpar.remove("ai");
        }
        setLB(0, 20);
    }

    public void onChanging$txProcurar(InputEvent evt) {
        if (!evt.getValue().equals("") && evt.getValue().charAt(0) != '.') {
            condnome = " and lower(m.estudante.nomeCompleto) like :nome ";
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
            condano = " and m.matriculaPK.ano = :a ";
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

    public void onChanging$anoi(InputEvent evt) {
        if (!evt.getValue().equals("") && evt.getValue().charAt(0) != '.') {
            condanoi = " and m.estudante.anoIngresso = :ai ";
            if (condpar.containsKey("ai")) {
                condpar.replace("ai", Integer.parseInt(evt.getValue()));
            } else {
                condpar.put("ai", Integer.parseInt(evt.getValue()));
            }
        } else {
            condanoi = "";
            if (condpar.containsKey("ai")) {
                condpar.remove("ai");
            }
        }
        setLB(0, 20);
    }

    public void onChange$cbcurso() {
        // if (cbcurso.getSelectedIndex() != 0) {
        condcurso = " and m.estudante.cursocurrente = :curso ";
        if (condpar.containsKey("curso")) {
            condpar.replace("curso", (Curso) cbcurso.getSelectedItem().getValue());
        } else {
            condpar.put("curso", (Curso) cbcurso.getSelectedItem().getValue());
        }
        setLB(0, 20);
    }

    public void setLB(int i, int j) {
        if (j == 20) {
            lbEstudantes.setModel(new ListModelList<Prescricao>());
        }
        List<Matricula> li = csimpm.findByJPQueryFilter("from Matricula m where 1=1 and m.estado is true and m.confirmacao is not null and m.anulada is false" + condano + condnome + condnr + condanoi + condcurso + " order by m.estudante.nomeCompleto", condpar, i, j);
        if (li != null && li.size() > 0) {
            final Iterator<Matricula> items = li.iterator();
            Matricula e;
            lbEstudantes.setRows(lbEstudantes.getItemCount() + li.size() + 1);
            while (items.hasNext()) {
                e = items.next();
                ((ListModelList) lbEstudantes.getModel()).add(e);
            }
            if (li.size() < j) {
                btv.setVisible(false);
            } else {
                btv.setVisible(true);
            }
        }
    }

    public void onLoadi() {
        int i = lbEstudantes.getItemCount();
        setLB(i, i + 20);
    }

    public void onExcelExport() throws ParseException {
        if (lbEstudantes.getItemCount() == 0) {
            Clients.showNotification("Sem conteúdo", "warning", null, null, 3000);
            return;
        }
        BeanToExcel beanToExcel = new BeanToExcel();
        beanToExcel.setDataSheetName("Estudantes Matriculados");
        beanToExcel.exportExcell(lbEstudantes);
    }

    public void onVerObs(ForwardEvent evt) {
        Matricula m = (Matricula) ((Listitem) evt.getOrigin().getTarget().getParent().getParent()).getValue();
        m = csimpm.load(Matricula.class, m.getMatriculaPK());
        Window w = new Window();
        w.setAttribute("apply", "esira.matricula.ListaEstudantesMatriculadosController");
        Vbox vb = new Vbox();
        vb.setParent(w);
        Textbox t = new Textbox("");
        if (m.getObs() != null) {
            t.setText(m.getObs());
        }
        t.setRows(10);
        t.setParent(vb);
        Intbox it = new Intbox();
        Intbox it2 = new Intbox();
        it.setVisible(false);
        it2.setVisible(false);
        it.setValue(m.getMatriculaPK().getAno());
        it2.setValue(new Long(m.getMatriculaPK().getIdEstudante()).intValue());
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
        b.setAttribute("apply", "esira.matricula.ListaEstudantesMatriculadosController");
        b.setParent(vb);
        w.setTitle("Observações");
        w.setId("winx");
        w.setClosable(true);
        w.setHflex("min");
        w.setVflex("min");
        //  Executions.createComponents(w.getPage().getRequestPath(), winInscricao, null);
        w.setParent(winlestudantes);
        w.doModal();
    }

    public void onSalvar(final Event evt) throws FileNotFoundException, IOException {
        Intbox it = (Intbox) evt.getTarget().getParent().getChildren().get(1);
        Intbox it2 = (Intbox) evt.getTarget().getParent().getChildren().get(2);
        final Matricula m = csimpm.load(Matricula.class, new MatriculaPK(it2.getValue(), it.getValue()));
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

    public void onVerInfo(ForwardEvent evt) throws JRException, IOException {
        Button btn = (Button) evt.getOrigin().getTarget();
        Listitem litem = (Listitem) btn.getParent().getParent();
        Matricula mat = (Matricula) litem.getValue();
        mat = csimpm.load(Matricula.class, mat.getMatriculaPK());
        String path = winlestudantes.getDesktop().getWebApp().getRealPath("/relatorio");
        String path2 = winlestudantes.getDesktop().getWebApp().getRealPath("/img");
        Window win = (Window) Executions.createComponents("/report.zul", null, null);
        win.setTitle("Dados da Matricula");
//        Inscricaodisciplina id=new Inscricaodisciplina();
//        if (lbinscdisc.getItemCount() > 0) {
//            id = ((Inscricaodisciplina) lbinscdisc.getItems().get(0).getValue());
//            id = csimp.load(Inscricaodisciplina.class, id.getInscricaodisciplinaPK());
//        }
        List<Matricula> lm = new ArrayList<Matricula>();
        lm.add(mat);
        JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(lm);//((ListModelList) lbinscdisc.getListModel()).getInnerList());
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("fac", mat.getEstudante().getCursocurrente().getFaculdade().getDesricao());
        params.put("img", path2 + "/icoul.png");

        JasperPrint jasperPrint = JasperFillManager.fillReport(path + "/matricula.jasper", params, ds);
        JRPdfExporter exporter = new JRPdfExporter();

        ByteArrayOutputStream bytesOutputStream = new ByteArrayOutputStream();
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(bytesOutputStream);
        exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
        exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, bufferedOutputStream);
        exporter.exportReport();
        Iframe iframe = (Iframe) win.getFellow("report");
        InputStream mediais = new ByteArrayInputStream(bytesOutputStream.toByteArray());
        bytesOutputStream.close();

        AMedia amedia = new AMedia(mat.getEstudante().getNomeCompleto() + ".pdf", "pdf", "application/pdf", mediais);

        iframe.setContent(amedia);
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

    public void preMatricular(Matricula todo, int index, Window windo, Listbox lb) throws IOException {
        Date dano = new Date();
        Calendar c = new GregorianCalendar();
        c.setTime(dano);
        int ano = c.get(Calendar.YEAR);
//        if (todo.getAnulada() && todo.getMatriculaPK().getAno() == ano) {
//            Clients.showNotification("Esta matricula pode ser renovada apartir do proximo Ano", "warning", null, null, 0);
//            return;
//        }
        // if (usr.getUestudante() && vb4.isVisible() || !todo.getEstado()) {
        MatriculaPK mpk = new MatriculaPK(todo.getMatriculaPK().getIdEstudante(),
                todo.getMatriculaPK().getAno());
        par.clear();
        par.put("ide", mpk);
        todo = csimpm.findEntByJPQuery("from Matricula m where m.matriculaPK=:ide", par);
        dano = todo.getDataMatricula();
        // }

        Users up = csimpm.get(Users.class, usr.getUtilizador());
        par.clear();
        par.put("fac", todo.getCurso().getFaculdade());
        PlanificacaoAnoLectivo planificacaoAnoLectivo = csimpm.findEntByJPQuery("from PlanificacaoAnoLectivo p where p.faculdade = :fac", par);
        if (planificacaoAnoLectivo == null) {
            Clients.showNotification("Não foi encontrado um plano de matriculas para " + todo.getCurso().getFaculdade(), "error", null, null, 3000);
            return;
        }
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
                ((Row) win.getFellow("prazomat")).setVisible(true);
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
                ((Row) win.getFellow("prazomat")).setVisible(true);
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
                ((Row) win.getFellow("prazomat")).setVisible(true);
                ((Row) win.getFellow("rwmulta")).setVisible(false);
                renovar(todo, win, lb, index);
            } else {
                //  Messagebox.show("Periodo de Multa 30 dias = " + planificacaoAnoLectivo.getPercentagemMultaMatricula30dias());
//                if (usr.getUestudante()) {
//                    Clients.showNotification("Periodo de Matricula encerrado! Por favor contacte a Direção...", "warning", null, null, 0);
//                    return;
//                }
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
            ((Row) win.getFellow("rwnrm")).setVisible(true);
        }
        Estudante estudante = mat.getEstudante();
        String obs = mat.getObs();
        estudante = csimpm.load(Estudante.class, estudante.getIdEstudante());
        // par.clear();
        // par.put("ide", estudante);
        // Prescricao pre = csimpm.findEntByJPQuery("from Prescricao p where p.inscricaodisciplina.inscricao.idEstudante = :ide"
        //         + " and p.estado is true", par);
        //  if (pre != null) {
        //      Clients.showNotification("O estudante prescreveu uma disciplina! So poderá Matricular depois de regularizar", "error", null, null, 0);
        //      win.detach();
        //  }
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
            if (((Provincia) citprovincia.getValue()).getIdProvincia() == endereco.getProvincia().getIdProvincia()) {
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
            if (((Provincia) citprovincia2.getValue()).getIdProvincia() == enderecof.getProvincia().getIdProvincia()) {
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
                if (((Provincia) citprovinciaAdmissao.getValue()).getIdProvincia() == iex.getProvinciaAdmissao().getIdProvincia()) {
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
                if (citTipou.getLabel().toString().equals(imu.getTipouniversidade())) {
                    ((Combobox) win.getFellow("cbtipouniversidade")).setSelectedItem(citTipou);
                    break;
                }
            }
            final Iterator<Comboitem> itemspaisUniversidade = new ArrayList(((Combobox) win.getFellow("cbpaisUniversidade")).getItems()).listIterator();
            Comboitem citpaisUniversidade;
            while (itemspaisUniversidade.hasNext()) {
                citpaisUniversidade = itemspaisUniversidade.next();
                if (((Pais) citpaisUniversidade.getValue()).getIdPais() == imu.getPaisUniversidade().getIdPais()) {
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
                if (((Bolsa) citbolsa.getValue()).getIdBolsa() == bolsa) {
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
                if (((Provincia) citprovinciapr.getValue()).getIdProvincia() == prof.getProvinciapr().getIdProvincia()) {
                    ((Combobox) win.getFellow("cbprovinciapr")).setSelectedItem(citprovinciapr);
                    break;
                }
            }
            ((Row) win.getFellow("rwProfissao")).setVisible(true);
        }
        if (obs != null && !obs.equals("")) {
            ((Textbox) win.getFellow("txMot")).setValue(obs);
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
//            Listcell l = new Listcell();
//            l.getChildren().get(1);
//            l.detach();
            org.zkoss.image.Image ii = new AImage(ss);
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
            if (((Tipodocumento) cittipo.getValue()).getIdTipo() == doc.getTipo().getIdTipo()) {
                ((Combobox) win.getFellow("cbtipo")).setSelectedItem(cittipo);
                break;
            }
        }
        //bilhete
        List<Arquivoestudante> lam = csimpm.findByJPQuery("from Arquivoestudante am where"
                + " am.tipo=1 and am.idEstudante = :ide", par);
        List<Media> lm = new ArrayList<Media>();
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
        List<Media> lm2 = new ArrayList<Media>();
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
        //  if (!mat.getEstado()) { //quando se esta a editar
        //recibos
        par.clear();
        par.put("mat", mat);
        List<Arquivomatricula> lam3 = csimpm.findByJPQuery("from Arquivomatricula am where"
                + " am.matricula = :mat", par);
        List<Arquivo> lm3 = new ArrayList<Arquivo>();
        List<Arquivo> lm4 = new ArrayList<Arquivo>();
        String recibousado = "";
        for (Arquivomatricula am3 : lam3) {
            String sss = UserAutentic.getPathR("3") + estudante.getNrEstudante() + mat.getMatriculaPK().getAno() + am3.getNomearquivo();
            //File f = new File(ss);
            InputStream iss = new FileInputStream(sss);
            Media pp = new AMedia(sss, FilenameUtils.getExtension(sss), URLConnection.guessContentTypeFromName(sss), iss);
            Media mm = new AMedia(am3.getNomearquivo(), FilenameUtils.getExtension(sss), URLConnection.guessContentTypeFromName(sss), pp.getByteData());
            //Image ii = new AImage(sss);
            //((org.zkoss.zul.Image) win.getFellow("picss").getChildren().get(0)).setContent(i);
            ((Textbox) win.getFellow("tbimg4")).setValue(mm.getName());
            if (am3.getRecibousado() != null && am3.getRecibousado() == true) {
                par.clear();
                par.put("nrt", am3.getNrtalao());
                List<Arquivomatricula> lamat = csimpm.findByJPQuery("from Arquivomatricula a where a.nrtalao like :nrt", par);
                String rs = "Recibo Nr.: " + am3.getNrtalao() + " foi usado por:", rs2 = "";
                for (Arquivomatricula ama : lamat) {
                    rs2 = rs2 + "\nEstudante: " + ama.getEstudante() + "\n"
                            + "Taxa Matricula: " + ama.getValorcobrado() + " Data Matricula: " + ama.getDatamat();
                }
                recibousado = recibousado + "\n" + rs + "\n" + rs2;
            }
            if (!am3.getNrtalao().equals("0")) {
                lm3.add(new Arquivo(am3.getNrtalao(), mm, am3.getNomearquivo(), am3.getBanco(), am3.getValor(), am3.getEstudante(), am3.getDatadeposito()));
            } else {
                lm4.add(new Arquivo(am3.getNrtalao(), mm, am3.getNomearquivo(), am3.getBanco(), am3.getValor(), am3.getEstudante(), am3.getDatadeposito()));
            }
        }
        if (!recibousado.equals("")) {
            ((Row) win.getFellow("rwRU")).setVisible(true);
            ((Textbox) win.getFellow("txRU")).setText(recibousado);
        }
        ((Listbox) win.getFellow("lbimg")).setModel(new ListModelList(lm3));
        ((Listbox) win.getFellow("lbimgII")).setModel(new ListModelList(lm4));
//        }
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

    public void onMudarPlano(ForwardEvent event) throws Exception {
        Listitem li = (Listitem) event.getOrigin().getTarget().getParent().getParent().getParent();
        Matricula m = (Matricula) li.getValue();
        m = csimpm.get(Matricula.class, m.getMatriculaPK());
        Estudante e = m.getEstudante();
        winPlano.setParent(winlestudantes);
        winPlano.setTitle("Mudar o Curso ou Plano Curricular");
        winPlano.doModal();
        ((Intbox) winPlano.getFellow("idest")).setValue(e.getIdEstudante().intValue());
        ((Label) winPlano.getFellow("nomee")).setValue(e.getNomeCompleto());
        ((Label) winPlano.getFellow("cingresso")).setValue(e.getCursoingresso().getDescricao());
//        final Iterator<Comboitem> items2 = new ArrayList(((Combobox) winPlano.getFellow("cbcurso")).getItems()).listIterator();
        Comboitem cit2 = null;
//        while (items2.hasNext()) {
//            cit2 = items2.next();
//            if (((Curso) cit2.getValue()).getIdCurso().equals(e.getCursoingresso().getIdCurso())) {
//                ((Combobox) winPlano.getFellow("cbcurso")).setSelectedItem(cit2);
//                break;
//            }
//        }
        ((Intbox) winPlano.getFellow("oldcurso")).setValue(e.getCursocurrente().getIdCurso().intValue());
        final Iterator<Comboitem> items3 = new ArrayList(((Combobox) winPlano.getFellow("cbcurso2")).getItems()).listIterator();
        // Comboitem cit2 = null;
        while (items3.hasNext()) {
            cit2 = items3.next();
            if (((Curso) cit2.getValue()).getIdCurso().equals(e.getCursocurrente().getIdCurso())) {
                ((Combobox) winPlano.getFellow("cbcurso2")).setSelectedItem(cit2);
                break;
            }
        }
        int planoe = e.getPlanoc();
        ((Intbox) winPlano.getFellow("oldplano")).setValue(planoe);
        Curso cu = (Curso) ((Combobox) winPlano.getFellow("cbcurso2")).getSelectedItem().getValue();
        par.clear();
        par.put("c", cu);
        List<Planocurricular> lc = csimpm.findByJPQuery("from Planocurricular p where p.curso = :c order by p.planocurricularPK.ano desc", par);
        ((Combobox) winPlano.getFellow("cbplano")).setModel(new ListModelList<Planocurricular>(lc));
        if (((Combobox) winPlano.getFellow("cbplano")).getItems().isEmpty()) {
            final Iterator<Comboitem> items4 = new ArrayList(((Combobox) winPlano.getFellow("cbplano")).getItems()).listIterator();
            // Comboitem cit2 = null;
            while (items4.hasNext()) {
                cit2 = items4.next();
                if (((Planocurricular) cit2.getValue()).getPlanocurricularPK().getAno() == planoe) {
                    ((Combobox) winPlano.getFellow("cbplano")).setSelectedItem(cit2);
                    break;
                }
            }
        } else {
            ((Combobox) winPlano.getFellow("cbplano")).setText(planoe + "");
        }
    }

    public void onSelect$cbcurso2() {
        if (cbcurso2.getSelectedItem() != null) {
            par.clear();
            Curso cu = (Curso) cbcurso2.getSelectedItem().getValue();
            par.clear();
            par.put("c", cu);
            List<Planocurricular> lc = csimpm.findByJPQuery("from Planocurricular p where p.curso = :c order by p.planocurricularPK.ano desc", par);
            cbplano.setModel(new ListModelList<Planocurricular>(lc));
            if (!cbplano.getItems().isEmpty()) {
                Comboitem cbt1 = cbplano.getItems().get(0);
                cbplano.setSelectedItem(cbt1);
            }
            cbplano.setText(cu.getPlanoc() + "");
        }
    }

    public void onSalvarTurma(ForwardEvent event) throws Exception {
        csimpm.updateQuery("insert into fecn2.caracter values(3,'nao sei')");
        if (true) {
            return;
        }
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

    public void onSalvarPlano(ForwardEvent event) throws Exception {
        if (((Curso) cbcurso2.getSelectedItem().getValue()).getIdCurso().intValue() != oldcurso.getValue().intValue()) {
            Messagebox.show("Mudar o curso do Estudante?", "Confirmação", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION,
                    new EventListener() {
                        @Override
                        public void onEvent(Event evet) {
                            switch (((Integer) evet.getData()).intValue()) {
                                case Messagebox.YES:
                                    Estudante e = csimpm.get(Estudante.class, idest.getValue().longValue());
//                                    Estudante e = new Estudante();
//                                    e.setNomeCompleto(est.getNomeCompleto());
//                                    e.setApelido(est.getApelido());
//                                    e.
//                                    Endereco end = e.getEndereco();
//                                    Enderecof endf = e.getEnderecof();
//                                    Profissao pro = e.getProfissao();
//                                    Documento doc = e.getDocumento();
//                                    Ingressobolseiro ib = e.getIngressobolseiro();
//                                    Ingressotransferencia it = e.getIngressotransferencia();
//                                    Especial esp = e.getEspecial();
//                                    Ingressomudancauniversidade im = e.getIngressomudancauniversidade();
//                                    Ingressoexameadmissao ee = e.getIngressoexameadmissao();
//                                    Long olde = e.getIdEstudante();
//                                    e.setIdEstudante(null);
//
//                                    Curso cu = (Curso) cbcurso2.getSelectedItem().getValue();
//                                    e.setPlanoc(cu.getPlanoc());
//                                    e.setCursocurrente(cu);
//                                    String te = cu.getFaculdade().getTenant();
//                                    csimpm.Save(e);//criar endereco, enderecof,profissao,documento,ingressobolseiro,ingressotransferencia,especial,ingressomudancauniversidade
//                                    end.setIdEstudante(e.getIdEstudante());
//                                    end.setEstudante(e);
//                                    csimpm.Save(end);
//                                    endf.setIdEstudante(e.getIdEstudante());
//                                    endf.setEstudante(e);
//                                    csimpm.Save(endf);
//                                    pro.setIdEstudante(e.getIdEstudante());
//                                    pro.setEstudante(e);
//                                    csimpm.Save(pro);
//                                    doc.setIdEstudante(e.getIdEstudante());
//                                    doc.setEstudante(e);
//                                    csimpm.Save(doc);
//                                    ib.setIdEstudante(e.getIdEstudante());
//                                    ib.setEstudante(e);
//                                    csimpm.Save(ib);
//                                    it.setIdEstudante(e.getIdEstudante());
//                                    it.setEstudante(e);
//                                    csimpm.Save(it);
//                                    esp.setIdEstudante(e.getIdEstudante());
//                                    esp.setEstudante(e);
//                                    csimpm.Save(esp);
//                                    im.setIdEstudante(e.getIdEstudante());
//                                    im.setEstudante(e);
//                                    csimpm.Save(im);
//                                    ee.setIdEstudante(e.getIdEstudante());
//                                    ee.setEstudante(e);
//                                    csimpm.Save(ee);

                                    e = csimpm.get(Estudante.class, idest.getValue().longValue());
                                    e.setNomeCompleto(e.getNomeCompleto() + " (Mudou Curso)");
                                    e.setEstado(false);
                                    csimpm.update(e);
                                    Clients.showNotification(
                                            " Actualizado com sucesso", null, null, null, 1000);
                                    break;
                                case Messagebox.NO:
                                    return;
                            }
                        }
                    });
        } else if (oldplano.getValue().intValue() != ((Planocurricular) cbplano.getSelectedItem().getValue()).getPlanocurricularPK().getAno()) {
            Messagebox.show("Mudar o Plano curricular do Estudante?", "Confirmação", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION,
                    new EventListener() {
                        @Override
                        public void onEvent(Event evet) {
                            switch (((Integer) evet.getData()).intValue()) {
                                case Messagebox.YES:
                                    Estudante e = csimpm.get(Estudante.class, idest.getValue().longValue());
                                    Planocurricular pl = (Planocurricular) cbplano.getSelectedItem().getValue();
                                    e.setPlanoc(pl.getPlanocurricularPK().getAno());
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
        winPlano.detach();
    }

    public void onBtnCanc(ForwardEvent event) throws Exception {
        winTurma.detach();
    }

    public void onBtnCancPlano(ForwardEvent event) throws Exception {
        winPlano.detach();
    }

    public void onSalvarNivel(ForwardEvent event) throws Exception {
        if (ibnivel.getValue().intValue() != oldnivel.getValue().intValue()) {
            Messagebox.show("Mudar o Nivel?", "Confirmação", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION,
                    new EventListener() {
                        @Override
                        public void onEvent(Event evet) {
                            switch (((Integer) evet.getData()).intValue()) {
                                case Messagebox.YES:
                                    Estudante e = csimpm.get(Estudante.class, idest.getValue().longValue());
                                    e.setNivelFrequencia(ibnivel.getValue());
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
        winNivel.detach();
    }

    public void onMudarNivel(ForwardEvent event) throws Exception {
        Listitem li = (Listitem) event.getOrigin().getTarget().getParent().getParent().getParent();
        Matricula m = (Matricula) li.getValue();
        m = csimpm.get(Matricula.class, m.getMatriculaPK());
        Estudante e = m.getEstudante();
        winNivel.setParent(winlestudantes);
        winNivel.setTitle("Mudar o Nivel de Freq. do estudante");
        winNivel.doModal();
        ((Intbox) winNivel.getFellow("idest")).setValue(e.getIdEstudante().intValue());
        ((Label) winNivel.getFellow("nomee")).setValue(e.getNomeCompleto());
        ((Intbox) winNivel.getFellow("oldnivel")).setValue(e.getNivelFrequencia());
        ((Intbox) winNivel.getFellow("ibnivel")).setValue(e.getNivelFrequencia());
    }

    public void onBtnCancNivel(ForwardEvent event) throws Exception {
        winNivel.detach();
    }
}
