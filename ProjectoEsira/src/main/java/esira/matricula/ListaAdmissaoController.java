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
import esira.domain.Documento;
import esira.domain.Endereco;
import esira.domain.Enderecof;
import esira.domain.Estadocivil;
import esira.domain.Estudante;
import esira.domain.Faculdade;
import esira.domain.Ingressobolseiro;
import esira.domain.Ingressoexameadmissao;
import esira.domain.Ingressomudancauniversidade;
import esira.domain.Listaadmissao;
import esira.domain.Matricula;
import esira.domain.Pais;
import esira.domain.PlanificacaoAnoLectivo;
import esira.domain.Profissao;
import esira.domain.Provincia;
import esira.domain.Tipodocumento;
import esira.domain.Users;
import esira.domain.Viaingresso;
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
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.apache.commons.io.FilenameUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
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
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Div;
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
import org.zkoss.zul.Window;
import org.zkoss.zul.event.PagingEvent;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.hibernate.Transaction;

/**
 *
 * @author Administrator
 */
public class ListaAdmissaoController extends GenericForwardComposer {

    static int c = 0;
    @WireVariable
    private CRUDService csimpm = (CRUDService) SpringUtil.getBean("CRUDService");
    private Label validation, message;
    private Listbox lbplm;
    Window mDialogAddLista, winmain, win, mDialogMatricula;
    Button addList, guardarLista;
    Button cancelarLista;
    Combobox cbCurso, cbTipoAdm, cbProcuracurso, cbTurnoA, cbfaculdade;
    Textbox txNome, txBI, txPbi, txCont, txPnome, txNumero;
    private Intbox ibidaluno, litem, ibProcAno;
    List listaM = csimpm.getAll(Listaadmissao.class);
    Map<String, Object> par = new HashMap<String, Object>();
    Users usr = (Users) Sessions.getCurrent().getAttribute("user");
    private Label lb;
    private String ord = "";
    private String sql = "";
    String condn = "", condc = "";
    Map<String, Object> condpar = new HashMap<String, Object>();
    private Paging pagListaad;
    private boolean mudarcurso;
    private long idcursoant;
    private Long idEstuMudaC;
    String condfac = "", condnr = "", condnome = "", condgenero = "", condanoi = "", condano = "", condcurso = "";
    Textbox txProcurar;
    Intbox ibanoi, anoeliminar;
    Combobox cbcurso;
    private Button btv;
    private EventQueue eq;
    private Window janelaInformacao, formApagarLista;

    //  @Init
    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        win = (Window) Executions.createComponents("/matricula/Matricula.zul", null, null);
        win.setTitle("Editar Matricula");
        win.detach();
//        if (lbplm != null) {
//            addeventoLB("from Listaadmissao l where 1=1" + ord, lbplm, pagListaad, Listaadmissao.class);
//        }
        condpar.clear();
        condcurso = " and la.curso.faculdade = :fac ";
        //Users u = csimpm.get(Users.class, usr.getUtilizador());
        if (usr.getFaculdade().getLocalizacao() == null) {
            cbfaculdade.setVisible(true);
            cbfaculdade.setModel(getFaculdadeModel());
        }
        Faculdade f = csimpm.get(Faculdade.class, usr.getFaculdade().getIdFaculdade());
        condpar.put("fac", f);
        Date dano = new Date();
        Calendar cal = new GregorianCalendar();
        cal.setTime(dano);
        int ano = cal.get(Calendar.YEAR);
        ibProcAno.setValue(ano);
        condpar.put("a", ano);
        condano = " and la.ano = :a ";
        setLB(0, 20);

    }

    public void onChanging$ibProcAno(InputEvent evt) {
        if (!evt.getValue().equals("") && evt.getValue().charAt(0) != '.') {
            condano = " and la.ano = :a ";
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

    public ListModel<Faculdade> getFaculdadeModel() {
        List<Faculdade> faculdades = csimpm.getAll(Faculdade.class);
        return new ListModelList<Faculdade>(faculdades);
    }

    public void onSelectcbfaculdadeCurso() {
        if (cbfaculdade.getSelectedItem() != null) {
            Faculdade f = cbfaculdade.getSelectedItem().getValue();
            f = csimpm.get(Faculdade.class, f.getIdFaculdade());
            Curso c = new Curso();
            c.setDescricao("-----Todos cursos-----");
            List<Curso> lc = new ArrayList<Curso>();
            lc.add(c);
            lc.addAll(f.getCursoList());
            cbcurso.setModel(new ListModelList<Curso>(lc));
            condcurso = " and la.curso.faculdade = :fac ";
            if (condpar.containsKey("curso")) {
                condpar.remove("curso");
            }
            if (condpar.containsKey("fac")) {
                condpar.replace("fac", f);
            } else {
                condpar.put("fac", f);
            }
            setLB(0, 20);
        }
    }

    public void onSetQueueLadm() {
        //Users u = csimpm.get(Users.class, usr.getUtilizador());
        eq = EventQueues.lookup("ladm" + usr.getFaculdade().getIdFaculdade(), EventQueues.APPLICATION, true);
        eq.subscribe(getEvento());
        eq = EventQueues.lookup("uladm" + usr.getFaculdade().getIdFaculdade(), EventQueues.APPLICATION, true);
        eq.subscribe(getEvento2());
        eq = EventQueues.lookup("rladm" + usr.getFaculdade().getIdFaculdade(), EventQueues.APPLICATION, true);
        eq.subscribe(getEvento3());
    }

    public EventListener getEvento() {
        return new EventListener() {
            @Override
            public void onEvent(Event event) throws Exception {
                Listaadmissao la = (Listaadmissao) event.getData();
                la = csimpm.get(Listaadmissao.class, la.getIdaluno());
                if (winmain.getParent() != null) {// && u.getFaculdade().getIdFaculdade() == i.getIdEstudante().getCursocurrente().getFaculdade().getIdFaculdade()) {
                    lbplm.setRows(lbplm.getItemCount() + 2);
                    ((ListModelList) lbplm.getModel()).remove(la);
                    ((ListModelList) lbplm.getModel()).add(0, la);
                    // Messagebox.show("Entrou");
                }
            }
        };
    }

    public EventListener getEvento2() {
        return new EventListener() {
            @Override
            public void onEvent(Event event) throws Exception {
                Listaadmissao la = (Listaadmissao) event.getData();
                la = csimpm.get(Listaadmissao.class, la.getIdaluno());
                if (winmain.getParent() != null) {
                    int ind = ((ListModelList) lbplm.getModel()).indexOf(la);
                    if (ind >= 0) {
                        ((ListModelList) lbplm.getModel()).set(ind, la);
                    }
                }
            }
        };
    }

    public EventListener getEvento3() {
        return new EventListener() {
            @Override
            public void onEvent(Event event) throws Exception {
                Listaadmissao la = (Listaadmissao) event.getData();
                //    la = csimpm.get(Listaadmissao.class, la.getIdaluno());
                if (winmain.getParent() != null) {
                    int ind = ((ListModelList) lbplm.getModel()).indexOf(la);
                    if (ind >= 0) {
                        ((ListModelList) lbplm.getModel()).remove(ind);
                    }
                }
            }
        };
    }

//    public void onChange$cbPfac() {
//        if (cbPfac.getSelectedItem() != null && !cbPfac.getText().equals("")) {
//            condfac = " and f.faculdade = :fac";
//            condpar.put("fac", (Faculdade) cbPfac.getSelectedItem().getValue());
//        } else {
//            condfac = "";
//            condpar.remove("fac");
//        }
//        addeventoLB("from Funcionario f where 1=1" + condfac + condnr + condnome + ord, lbFuncionario, pagFuncionario, Funcionario.class);
//    }
//
//    public void onChanging$txPnr(InputEvent evt) {
//        // evt.stopPropagation();
//        if (!evt.getValue().equals("") && evt.getValue().charAt(0) != '.') {
//            condnr = " and lower(f.nrfuncionario) like :nr";
//            condpar.put("nr", "%" + evt.getValue().toLowerCase() + "%");
//        } else {
//            condnr = "";
//            condpar.remove("nr");
//        }
//        addeventoLB("from Funcionario f where 1=1" + condfac + condnr + condnome + ord, lbFuncionario, pagFuncionario, Funcionario.class);
//    }
    public void onAddList() {
        mDialogAddLista.setParent(winmain);
        c = 0;
        mDialogAddLista.doModal();
    }

    public void onGuardarLista() {
        if (!camposnulo()) {

            validation.setValue("Todos os campos São de preenchimento Obrigatório");
            return;
        }
        try {
        } catch (Exception e) {
        }
        Users us = csimpm.get(Users.class, usr.getUtilizador());
        if (c == 0) {
            Listaadmissao l = new Listaadmissao();
            l.setAno(ibanoi.getValue());
            l.setCurso((Curso) cbCurso.getSelectedItem().getValue());
            l.setNumero(txNumero.getValue());
            l.setNome(txNome.getValue());
            l.setNrBI(txBI.getValue());
            l.setContacto(txCont.getValue());
            l.setTelefone(txCont.getValue());
            l.setMatriculado(false);
            l.setTurno(Integer.parseInt(cbTurnoA.getSelectedItem().getValue().toString()));
            l.setTipoad(cbTipoAdm.getSelectedItem().getLabel());
            csimpm.Save(l);
            lbplm.setRows(lbplm.getItemCount() + 1);
            // ((ListModelList) lbplm.getModel()).add(0, l);//.add(l);
            Clients.showNotification("Inserido com sucessos", null, null, null, 2000);
//            lbplm
//                    .setModel(new ListModelList<Listaadmissao>(csimpm.getAll(Listaadmissao.class
//                                    )));
            mDialogAddLista.detach();

            c = 0;
            eq = EventQueues.lookup("ladm" + us.getIdFuncionario().getFaculdade().getIdFaculdade(), EventQueues.APPLICATION, true);
            eq.publish(new Event("onPedidoMat", null, l));
        } else {
            Messagebox.show("Pretende modificar os dados?", "?", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION,
                    new EventListener() {
                @Override
                public void onEvent(Event evet) {
                    switch (((Integer) evet.getData()).intValue()) {
                        case Messagebox.YES:
                            Listaadmissao l = csimpm.get(Listaadmissao.class, ibidaluno.getValue());//csimpm.GetUniqueEntityByNamedQuery("Listaadmissao.findByIdaluno", Integer.parseInt(txidaluno.getValue()));          
                            //l.setAno(cbTipoAdm.getSelectedIndex());                                   
                            l.setCurso((Curso) cbCurso.getSelectedItem().getValue());
                            l.setNome(txNome.getValue());
                            l.setAno(ibanoi.getValue());
                            l.setNumero(txNumero.getValue());
                            l.setNrBI(txBI.getValue());
                            l.setContacto(txCont.getValue());
                            l.setTelefone(txCont.getValue());
                            l.setTipoad(cbTipoAdm.getSelectedItem().getLabel());
                            l.setTurno(Integer.parseInt(cbTurnoA.getSelectedItem().getValue().toString()));
                            csimpm.update(l);
                            Clients.showNotification("Actualizado com sucessos", null, null, null, 2000);
                            // ((ListModelList) lbplm.getModel()).set(litem.getValue(), l);
                            mDialogAddLista.detach();

                            c = 1;
                            Users us = csimpm.get(Users.class, usr.getUtilizador());
                            eq = EventQueues.lookup("uladm" + us.getIdFuncionario().getFaculdade().getIdFaculdade(), EventQueues.APPLICATION, true);
                            eq.publish(new Event("onPedidoMat", null, l));
                            break;
                        case Messagebox.NO:
                            return;
                    }
                }
            });
        }

    }

    public void onCancelarLista() {

        mDialogAddLista.detach();

    }

    public ListModel<Listaadmissao> getListAmitModel() {
        par.clear();
        par.put("user", usr.getUtilizador());
        // Users u = csimpm.findEntByJPQuery("from Users u where u.utilizador = :user", par);
        par.clear();
        Faculdade f = csimpm.get(Faculdade.class, usr.getFaculdade().getIdFaculdade());
        par.put("fac", f);
        List<Listaadmissao> la = csimpm.findByJPQuery("from Listaadmissao la where"
                + " la.curso.faculdade = :fac", par);
        return new ListModelList<Listaadmissao>(la);
    }

    public ListModel<Curso> getListCursoPModel() {
        //Users u = csimpm.get(Users.class, usr.getUtilizador());
        par.clear();
        Faculdade f = csimpm.get(Faculdade.class, usr.getFaculdade().getIdFaculdade());
        par.put("fac", f);
        Curso c = new Curso();
        c.setDescricao("-----Todos cursos-----");
        List<Curso> lc = new ArrayList<Curso>();
        lc.add(c);
        List<Curso> lc2 = csimpm.findByJPQuery("from Curso c where c.faculdade = :fac", par);
        lc.addAll(lc2);
        return new ListModelList<Curso>(lc);
    }

    public ListModel<Curso> getListCursoModel() {
        //Users u = csimpm.get(Users.class, usr.getUtilizador());
        par.clear();
        List<Curso> lc = null;
        if (usr.getFaculdade().getLocalizacao() != null) {
            Faculdade f = csimpm.get(Faculdade.class, usr.getFaculdade().getIdFaculdade());
            par.put("fac", f);
            lc = csimpm.findByJPQuery("from Curso c where c.faculdade = :fac", par);
        } else {
            lc = csimpm.getAll(Curso.class);
        }
        return new ListModelList<Curso>(lc);
    }

    public void onMatricular(ForwardEvent evt) throws Exception {
        idEstuMudaC = null;
        mudarcurso = false;
//        Executions.getCurrent().getSession().setAttribute("ussimane", "Maria");
//        Messagebox.show((String) Executions.getCurrent().getSession().getAttribute("ussimane"));
        Date dano = new Date();
        Matricula mat = null;
        Listaadmissao la = (Listaadmissao) ((Listitem) evt.getOrigin().getTarget().getParent().getParent()).getValue();
        la = csimpm.get(Listaadmissao.class, la.getIdaluno());
        if (la.getIdEstudante() != null) {
            Estudante e = la.getIdEstudante();
            if (e.getMatriculaList().size() == 1) {
                mat = la.getIdEstudante().getMatriculaList().get(0);
                mat = csimpm.load(Matricula.class, mat.getMatriculaPK());
                if (mat.getConfirmacao() == null) {
                    Clients.showNotification("Este candidato tem uma matricula não Confirmada", "warning", null, null, 0);
                    return;
                }
                dano = mat.getDataMatricula();
            }
        } else {
            par.clear();
            par.put("nome", "%" + la.getNome() + "%");
            par.put("nrbi", la.getNrBI());
            Estudante e = csimpm.findEntByJPQuery("from Estudante e where"
                    + " e.documento.nrDocumento = :nrbi and e.nomeCompleto like :nome", par);

            if (e != null) {
                if (e.getCursoingresso().getIdCurso() == e.getCursocurrente().getIdCurso() && e.getCursocurrente().getIdCurso() != la.getCurso().getIdCurso()) {
                    idcursoant = e.getCursocurrente().getIdCurso();
                    idEstuMudaC = e.getIdEstudante();
                    mudarcurso = true;
                } else {
                    Clients.showNotification("O Candidato que se pretende Matricular ja existe no Sistema. "
                            + "Se ainda não mudou de curso, poderá recandidatar-se com um outro curso "
                            + "(Com a permissão da direcção!)", "warning", null, null, 0);
                    return;
                }
            }
        }
        Calendar c = new GregorianCalendar();
        c.setTime(dano);
        Users up = csimpm.get(Users.class, usr.getUtilizador());
        par.clear();
        par.put("fac", la.getCurso().getFaculdade());
        PlanificacaoAnoLectivo planificacaoAnoLectivo = csimpm.findEntByJPQuery("from PlanificacaoAnoLectivo p where p.faculdade = :fac", par);
        if (planificacaoAnoLectivo == null) {
            Clients.showNotification("Não foi encontrado um plano de matriculas para " + la.getCurso().getFaculdade(), "error", null, null, 3000);
            return;
        }
        if (planificacaoAnoLectivo != null) {
            if (dano.before(planificacaoAnoLectivo.getDatainicioInscricao())) {
                Clients.showNotification("Por favor aguarde o periodo de Matriculas", "warning", null, null, 0, true);
                return;
            }
            if ((dano.after(planificacaoAnoLectivo.getDatainicioInscricao()) && dano.before(planificacaoAnoLectivo.getDataFimIE1())) || (mat != null && mat.getPeriodo() != null && mat.getPeriodo().equals("NORMAL"))) {
                Button btn = (Button) evt.getOrigin().getTarget();
                Listitem litem = (Listitem) btn.getParent().getParent();

                final HashMap<String, Object> map = new HashMap<String, Object>();
                map.put("periodo", "NORMAL");
                if (mat == null) {
                    win = (Window) Executions.createComponents("/matricula/Matricula.zul", winmain, map);
                } else {
                    win.setParent(winmain);
                    win.doModal();
                }
                Textbox tbnome = (Textbox) win.getFellow("tbnomeCompleto");
                Textbox tbnrmeca = (Textbox) win.getFellow("nrmeca");
                ((Label) win.getFellow("taxamultas")).setValue("NORMAL");
                ((Label) win.getFellow("lbtaxaMatricula")).setValue(planificacaoAnoLectivo.getTaxaMatriculaNacional().toString());
                ((Label) win.getFellow("lbtaxaMatriculaE")).setValue(planificacaoAnoLectivo.getTaxaMatriculaEstrangeiro().toString());
                ((Row) win.getFellow("rwTaxamulta30")).setVisible(false);
                ((Row) win.getFellow("rwTaxamulta15")).setVisible(false);
                ((Row) win.getFellow("rwmulta")).setVisible(false);
                ((Div) win.getFellow("prazo")).setVisible(false);
                ((Row) win.getFellow("prazomat")).setVisible(false);
                Listaadmissao todo = null;
                if (mat != null) {
                    renovar(mat, win, litem.getIndex());
                } else {
                    todo = (Listaadmissao) litem.getValue();
                    tbnrmeca.setValue(todo.getNumero());
                    tbnome.setValue(todo.getNome());
                    tbnome.setReadonly(true);
                    novaMatricula(win, todo, litem.getIndex());
                }

//            } else if ((dano.after(planificacaoAnoLectivo.getDataFimIE1()) && dano.before(planificacaoAnoLectivo.getDataFimIE2())) || (mat != null && mat.getPeriodo() != null && mat.getPeriodo().equals("M15"))) {
//                Button btn = (Button) evt.getOrigin().getTarget();
//                Listitem litem = (Listitem) btn.getParent().getParent();
//                final HashMap<String, Object> map = new HashMap<String, Object>();
//                map.put("periodo", "M15");
//                if (mat == null) {
//                    win = (Window) Executions.createComponents("/matricula/Matricula.zul", winmain, map);
//                } else {
//                    win.setParent(winmain);
//                    win.doModal();
//                }
//                Textbox tbnome = (Textbox) win.getFellow("tbnomeCompleto");
//                Textbox tbnrmeca = (Textbox) win.getFellow("nrmeca");
//                ((Label) win.getFellow("taxamultas")).setValue("M15");//Conhecer o periodo para calcular multa
//                ((Label) win.getFellow("lbtaxaMatricula")).setValue(planificacaoAnoLectivo.getTaxaMatriculaNacional().toString());
//                ((Label) win.getFellow("lbtaxaMatriculaE")).setValue(planificacaoAnoLectivo.getTaxaMatriculaEstrangeiro().toString());
//                ((Row) win.getFellow("rwTaxamulta15")).setVisible(true);
//                ((Row) win.getFellow("rwTaxamulta30")).setVisible(false);
//                ((Row) win.getFellow("rwmulta")).setVisible(false);
//                ((Div) win.getFellow("prazo")).setVisible(false);
//                ((Row) win.getFellow("prazomat")).setVisible(false);
//                Listaadmissao todo = null;
//                if (mat != null) {
//                    renovar(mat, win, litem.getIndex());
//                } else {
//                    todo = (Listaadmissao) litem.getValue();
//                    tbnrmeca.setValue(todo.getNumero());
//                    tbnome.setValue(todo.getNome());
//                    tbnome.setReadonly(true);
//                    novaMatricula(win, todo, litem.getIndex());
//                }
//            } else if ((dano.after(planificacaoAnoLectivo.getDataFimIE2()) && dano.before(planificacaoAnoLectivo.getDma1())) || (mat != null && mat.getPeriodo() != null && mat.getPeriodo().equals("M30"))) {
////                                Messagebox.show("Periodo de Multa 30 dias = " + planificacaoAnoLectivo.getPercentagemMultaMatricula30dias());
////                                
//                Button btn = (Button) evt.getOrigin().getTarget();
//                Listitem litem = (Listitem) btn.getParent().getParent();
//                final HashMap<String, Object> map = new HashMap<String, Object>();
//                map.put("periodo", "M30");
//                if (mat == null) {
//                    win = (Window) Executions.createComponents("/matricula/Matricula.zul", winmain, map);
//                } else {
//                    win.setParent(winmain);
//                    win.doModal();
//                }
//                Textbox tbnome = (Textbox) win.getFellow("tbnomeCompleto");
//                Textbox tbnrmeca = (Textbox) win.getFellow("nrmeca");
//                ((Label) win.getFellow("taxamultas")).setValue("M30");
//                ((Label) win.getFellow("lbtaxaMatricula")).setValue(planificacaoAnoLectivo.getTaxaMatriculaNacional().toString());
//                ((Label) win.getFellow("lbtaxaMatriculaE")).setValue(planificacaoAnoLectivo.getTaxaMatriculaEstrangeiro().toString());
//                //    ((Label) win.getFellow("lbtaxaMuniversidade")).setValue(planificacaoAnoLectivo.getTaxaDeMudancaoTurno().toString());
//                ((Row) win.getFellow("rwTaxamulta30")).setVisible(true);
//                ((Row) win.getFellow("rwTaxamulta15")).setVisible(false);
//                ((Row) win.getFellow("rwmulta")).setVisible(false);
//                ((Div) win.getFellow("prazo")).setVisible(false);
//                ((Row) win.getFellow("prazomat")).setVisible(false);
//                Listaadmissao todo = null;
//                if (mat != null) {
//                    renovar(mat, win, litem.getIndex());
//                } else {
//                    todo = (Listaadmissao) litem.getValue();
//                    tbnrmeca.setValue(todo.getNumero());
//                    tbnome.setValue(todo.getNome());
//                    tbnome.setReadonly(true);
//                    novaMatricula(win, todo, litem.getIndex());
//                }
            } else {
                //Messagebox.show("Periodo de Multa 30 dias = " + planificacaoAnoLectivo.getPercentagemMultaMatricula30dias());    
                Button btn = (Button) evt.getOrigin().getTarget();
                Listitem litem = (Listitem) btn.getParent().getParent();
                final HashMap<String, Object> map = new HashMap<String, Object>();
                map.put("periodo", "M30");
                if (mat == null) {
                    win = (Window) Executions.createComponents("/matricula/Matricula.zul", winmain, map);
                } else {
                    win.setParent(winmain);
                    win.doModal();
                }
                Textbox tbnome = (Textbox) win.getFellow("tbnomeCompleto");
                Textbox tbnrmeca = (Textbox) win.getFellow("nrmeca");
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
                Listaadmissao todo = null;
                if (mat != null) {
                    renovar(mat, win, litem.getIndex());
                } else {
                    todo = (Listaadmissao) litem.getValue();
                    tbnrmeca.setValue(todo.getNumero());
                    tbnome.setValue(todo.getNome());
                    tbnome.setReadonly(true);
                    novaMatricula(win, todo, litem.getIndex());
                }
            }
        }
    }

//    if (dano.after (planificacaoAnoLectivo.getDataFimIE2 
//        ())) {
//                Boolean b = true;
//        if (!usr.getUestudante()) {
//            Users u = csimpm.get(Users.class, usr.getUtilizador());
//            if (u.getIdFuncionario().getTipochefia() != null) {
//                String ca = u.getIdFuncionario().getTipochefia().getCargochefia().getDescricao();
//                if (ca.equals("Director Adj. Pedagogico") || ca.equals("Director da Faculdade")) {
//                    b = false;
//                }
//            }
//            if (b) {
//                par.clear();
//                par.put("f", u.getIdFuncionario());
//                par.put("d", new Date());
//                par.put("ido", 3);
//                Operacaopedido op = csimpm.findEntByJPQuery("from Operacaopedido op where op.funcionario = :f and "
//                        + "op.datai >= :d and op.dataf <= :d and op.operacao.idoperacao = :ido", par);
//                if (op != null) {
//                    b = false;
//                }
//            }
//        }
//        if (!b) {
//            Button btn = (Button) evt.getOrigin().getTarget();
//            Listitem litem = (Listitem) btn.getParent().getParent();
//            final HashMap<String, Object> map = new HashMap<String, Object>();
//            map.put("periodo", "M30");
//            Window win = (Window) Executions.createComponents("/matricula/Matricula.zul", winmain, map);
//            Textbox tbnome = (Textbox) win.getFellow("tbnomeCompleto");
//            ((Label) win.getFellow("lbtaxaMatricula")).setValue(planificacaoAnoLectivo.getTaxaMatriculaNacional().toString());
//            ((Label) win.getFellow("lbtaxaMatriculaE")).setValue(planificacaoAnoLectivo.getTaxaMatriculaEstrangeiro().toString());
//            //    ((Label) win.getFellow("lbtaxaMuniversidade")).setValue(planificacaoAnoLectivo.getTaxaDeMudancaoTurno().toString());
//            ((Row) win.getFellow("rwTaxamulta30")).setVisible(true);
//            ((Row) win.getFellow("rwTaxamulta15")).setVisible(false);
//            Listaadmissao todo = null;
//            if (mat != null) {
//                renovar(mat, win, litem.getIndex());
//            } else {
//                todo = (Listaadmissao) litem.getValue();
//                tbnome.setValue(todo.getNome());
//                tbnome.setReadonly(true);
//                novaMatricula(win, todo, litem.getIndex());
//            }
//        }
//        if (b) {
//            Clients.showNotification("Periodo de Matricula encerrado", "warning", null, null, 0);
//            return;
//        }
//    }
//}
//}
    public void novaMatricula(Window win, Listaadmissao t, int index) {
        win.setTitle("Nova Matricula");
        ((Intbox) win.getFellow("litem")).setValue(index);
        Combobox cbcur = (Combobox) win.getFellow("cbcursocurrente");
        ((Intbox) win.getFellow("ibplanoc")).setValue(t.getCurso().getPlanoc());
        ((Intbox) win.getFellow("ibturno")).setValue(t.getTurno());
        ((Intbox) win.getFellow("ibano")).setValue(t.getAno());
        ((Combobox) win.getFellow("cbTurno")).setButtonVisible(false);
        if (t.getTurno() == 1) {
            ((Combobox) win.getFellow("cbTurno")).setSelectedIndex(0);
        } else {
            ((Combobox) win.getFellow("cbTurno")).setSelectedIndex(1);
        }
        if (mudarcurso) {
            ((Intbox) win.getFellow("idEstuMudaC")).setValue(idEstuMudaC.intValue());
            Curso ca = csimpm.get(Curso.class, idcursoant);
            List<Curso> lc = new ArrayList<Curso>();

            lc.add(ca);

            cbcur.setModel(
                    new ListModelList<Curso>(lc));
            cbcur.setReadonly(
                    true);
            ((Radio) win.getFellow("rgNC")).setChecked(false);
            ((Radio) win.getFellow("rgSC")).setChecked(true);
            ((Row) win.getFellow("rcurso")).setVisible(false);
            ((Row) win.getFellow("rwCurso")).setVisible(true);
            Combobox cbcur2 = (Combobox) win.getFellow("cbcurso");
            lc = new ArrayList<Curso>();

            lc.add(t.getCurso());
            cbcur2.setModel(
                    new ListModelList<Curso>(lc));
            cbcur2.setReadonly(
                    true);

        } else {
            List<Curso> lc = new ArrayList<Curso>();
            lc.add(t.getCurso());
            cbcur.setModel(new ListModelList<Curso>(lc));
            cbcur.setReadonly(true);
        }
        ((Row) win.getFellow("rwnrm")).setVisible(true);
        ((Row) win.getFellow("rwRU")).setVisible(false);
//        Window wi = (Window) win.clone();
//        wi.getFellow("mDialogMatricula").detach();
//        wi.removeAttribute("apply");
//        wi.removeAttribute("id");
//        wi.invalidate();
//        wi.setClosable(true);
//           wi.getDesktop().invalidate();
//        wi.setParent(null);
//        Sessions.getCurrent().setAttribute("winb", wi);
    }

    public void onEdit(ForwardEvent evt) throws Exception {
        Button btn = (Button) evt.getOrigin().getTarget();
        Listitem litem = (Listitem) btn.getParent().getParent();
        Listaadmissao todo = (Listaadmissao) litem.getValue();
        todo
                = csimpm.load(Listaadmissao.class, todo.getIdaluno());
        c = 1;

        ((Intbox) mDialogAddLista.getFellow("litem")).setValue(litem.getIndex());
        Intbox idaluno = (Intbox) mDialogAddLista.getFellow("ibidaluno");
        Textbox nome = (Textbox) mDialogAddLista.getFellow("txNome");
        Textbox numero = (Textbox) mDialogAddLista.getFellow("txNumero");
        Intbox ano = (Intbox) mDialogAddLista.getFellow("ibanoi");
        Textbox bi = (Textbox) mDialogAddLista.getFellow("txBI");
        Textbox tel = (Textbox) mDialogAddLista.getFellow("txCont");

        mDialogAddLista.setParent(winmain);

        mDialogAddLista.doModal();

        nome.setValue(todo.getNome());
        bi.setValue(todo.getNrBI());
        tel.setValue(todo.getContacto());
        ano.setValue(todo.getAno());
        numero.setValue(todo.getNumero());
        idaluno.setValue(todo.getIdaluno());
        final Iterator<Comboitem> items1 = new ArrayList(((Combobox) mDialogAddLista.getFellow("cbTipoAdm")).getItems()).listIterator();
        Comboitem cit1;

        while (items1.hasNext()) {
            cit1 = items1.next();
            if (cit1.getLabel().equals(todo.getTipoad())) {
                ((Combobox) mDialogAddLista.getFellow("cbTipoAdm")).setSelectedItem(cit1);
                break;
            }
        }
        final Iterator<Comboitem> items = new ArrayList(((Combobox) mDialogAddLista.getFellow("cbCurso")).getItems()).listIterator();
        Comboitem cit;

        while (items.hasNext()) {
            cit = items.next();
            if (((Curso) cit.getValue()).getIdCurso().equals(todo.getCurso().getIdCurso())) {
                ((Combobox) mDialogAddLista.getFellow("cbCurso")).setSelectedItem(cit);
                break;
            }
        }
        Combobox cbt = (Combobox) mDialogAddLista.getFellow("cbTurnoA");
        Comboitem cbt1 = cbt.getItems().get(0);
        Comboitem cbt2 = cbt.getItems().get(1);
//        Messagebox.show(f.getTurno()+"");
        //   Messagebox.show(cbt1.getValue().toString());
        if (todo.getTurno() == 1) {
            cbt.setSelectedItem(cbt1);
            cbt.setText("Laboral");
        } else {
            cbt.setSelectedItem(cbt2);
            cbt.setText("Pos-Laboral");
        }

    }

    public void onEliminarLista() {
        formApagarLista.setParent(winmain);
        formApagarLista.doModal();;
    }

    public void onBtnCanc() {
        formApagarLista.detach();
    }

    public void onBtnApagar() {
        int r = csimpm.updateQuery("delete from listaadmissao where ano =?", anoeliminar.getValue());
        formApagarLista.detach();
        if (r == 1) {
            Clients.showNotification(r + " Registo foi eliminado", null, null, null, 3000);
            setLB(0, 20);
        } else if (r > 1) {
            Clients.showNotification(r + " Registos foram eliminados", null, null, null, 3000);
            setLB(0, 20);
        } else {
            Clients.showNotification("Nenhum registo foi eliminado", "warning", null, null, 0);
        }

    }

    public void onEliminar(final ForwardEvent evt) throws Exception {
        Messagebox.show("Apagar?", "Confirmação", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION,
                new EventListener() {
            @Override
            public void onEvent(Event evet) {
                switch (((Integer) evet.getData()).intValue()) {
                    case Messagebox.YES:
                        Button btn = (Button) evt.getOrigin().getTarget();
                        Listitem litem = (Listitem) btn.getParent().getParent();
                        Listaadmissao todo = (Listaadmissao) litem.getValue();
                        todo = csimpm.load(Listaadmissao.class, todo.getIdaluno());
                        Users us = csimpm.get(Users.class, usr.getUtilizador());
                        if (!todo.getMatriculado()
                                && todo.getIdEstudante() != null) {
                            if (todo.getIdEstudante().getMatriculaList().size() > 0) {
                                Clients.showNotification("Este candidato tem uma matricula pendente", "warning", null, null, 0);
                                return;
                            }
                        }
//                                ((ListModelList) lbplm.getModel()).remove(todo);
//                                new Listbox()
//                                .appendChild(litem);
                        eq = EventQueues.lookup("rladm" + us.getIdFuncionario().getFaculdade().getIdFaculdade(), EventQueues.APPLICATION, true);
                        eq.publish(new Event("onPedidoMat", null, todo));
                        csimpm.delete(todo);

                        Clients.showNotification(
                                " Removido da lista", null, null, null, 2000);
                        break;
                    case Messagebox.NO:
                        return;
                }
            }
        });
    }

    public void limparcampos() {
        txNome.setText("");
        txBI.setText("");
        txCont.setText("");
    }

    public boolean camposnulo() {
        if (txNome.getValue().equals("")
                //|| txBI.getValue().equals("")
                || txCont.getValue().equals("")
                || cbCurso.getSelectedIndex() < 0
                || cbTipoAdm.getSelectedIndex() < 0) {
            return false;
        }

        return true;
    }

    //Pesquisar estudante
    public void onChanging$txPnome(InputEvent event) throws Exception {

        List<Listaadmissao> userList = new ArrayList<Listaadmissao>(listaM);
        List<Listaadmissao> tempList = new ArrayList<Listaadmissao>();

        if (cbProcuracurso.getSelectedItem() == null) {
            for (Listaadmissao p : userList) {

                if (p.getNome().regionMatches(true, 0, event.getValue(), 0, event.getValue().length())) {
                    tempList.add(p);
                }
            }
        } else {

            for (Listaadmissao m : userList) {
                if (m.getNome().regionMatches(true, 0, event.getValue(), 0, event.getValue().length())
                        && m.getCurso() == cbProcuracurso.getSelectedItem().getValue()) {
                    tempList.add(m);
                }
            }
        }

        ListModelList<Listaadmissao> model = new ListModelList<Listaadmissao>(tempList);
        lbplm.setModel(model);

    }

    public void onSelect$cbProcuracurso() throws Exception {
        List<Listaadmissao> l = new ArrayList<Listaadmissao>(listaM);

        List<Listaadmissao> tempList = new ArrayList<Listaadmissao>();

        if (txPnome.getValue().isEmpty()) {
            for (Listaadmissao m : l) {
                if (m.getCurso() == cbProcuracurso.getSelectedItem().getValue()) {
                    tempList.add(m);
                }
            }

        } else {

            for (Listaadmissao m : l) {
                if (m.getNome().regionMatches(true, 0, txPnome.getValue(), 0, txPnome.getValue().length())
                        && m.getCurso() == cbProcuracurso.getSelectedItem().getValue()) {
                    tempList.add(m);
                }
            }

        }

        ListModelList<Listaadmissao> model = new ListModelList<Listaadmissao>(tempList);
        lbplm.setModel(model);

    }

    public void renovar(Matricula mat, Window win, int index) throws IOException {
        win.setTitle("Editar Matricula");
        ((Intbox) win.getFellow("litem")).setValue(index);
        ((Tab) win.getFellow("tabDadosPessoais")).setSelected(true);
        ((Row) win.getFellow("rwnrm")).setVisible(true);
        ((Button) win.getFellow("btnsave")).setLabel("Aceitar");
        ((Button) win.getFellow("btnsave")).setDisabled(false);
        ((Intbox) win.getFellow("litem")).setValue(index);
        ((Label) win.getFellow("lbf")).setValue(lbplm.getId());
        Estudante estudante = mat.getEstudante();
        estudante
                = csimpm.load(Estudante.class, estudante.getIdEstudante());
        ((Combobox) win.getFellow("cbturno")).setButtonVisible(false);
        if (estudante.getTurno() == 1) {
            ((Combobox) win.getFellow("cbTurno")).setSelectedIndex(0);
        } else {
            ((Combobox) win.getFellow("cbTurno")).setSelectedIndex(1);
        }
        Combobox cbcur = (Combobox) win.getFellow("cbcursocurrente");
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

        if (cursocurrente != cursoingresso) {
//            List<Curso> lc = new ArrayList<Curso>();
//            lc.add(estudante.getCursoingresso());
//            cbcur.setModel(new ListModelList<Curso>(lc));
            ((ListModelList) cbcur.getModel()).clear();
            ((ListModelList) cbcur.getModel()).add(estudante.getCursoingresso());
//            final Iterator<Comboitem> itemscursoi = new ArrayList(cbcur.getItems()).listIterator();
//            Comboitem citcursoi;
//            while (itemscursoi.hasNext()) {
//                citcursoi = itemscursoi.next();
//                if (((Curso) citcursoi.getValue()).getIdCurso() == estudante.getCursoingresso().getIdCurso()) {
//                    cbcur.setSelectedItem(citcursoi);
//                    break;
//                }
//            }
            cbcur.setReadonly(true);
            ((Radio) win.getFellow("rgNC")).setChecked(false);
            ((Radio) win.getFellow("rgSC")).setChecked(true);
            ((Row) win.getFellow("rcurso")).setVisible(false);
            ((Row) win.getFellow("rwCurso")).setVisible(true);
            Combobox cbcur2 = (Combobox) win.getFellow("cbcurso");
//            List<Curso>lc = new ArrayList<Curso>();
//            lc.add(estudante.getCursocurrente());
            // cbcur2.setModel(new ListModelList<Curso>(lc));
            ((ListModelList) cbcur2.getModel()).clear();
            ((ListModelList) cbcur2.getModel()).add(estudante.getCursocurrente());
//            final Iterator<Comboitem> itemscursocurrente = new ArrayList(cbcur2.getItems()).listIterator();
//            Comboitem citcursocurrente;
//            while (itemscursocurrente.hasNext()) {
//                citcursocurrente = itemscursocurrente.next();
//                if (((Curso) citcursocurrente.getValue()).getIdCurso() == cursocurrente) {
//                    cbcur2.setSelectedItem(citcursocurrente);
//                    break;
//                }
//            }
            cbcur2.setReadonly(true);

        } else {
//            List<Curso> lc = new ArrayList<Curso>();
//            lc.add(estudante.getCursocurrente());
//            cbcur.setModel(new ListModelList<Curso>(lc));
            ((ListModelList) cbcur.getModel()).clear();
            ((ListModelList) cbcur.getModel()).clearSelection();
            ((ListModelList) cbcur.getModel()).add(estudante.getCursocurrente());
//            final Iterator<Comboitem> itemscursocurrente = new ArrayList(cbcur.getItems()).listIterator();
//            Comboitem citcursocurrente;
//            while (itemscursocurrente.hasNext()) {
//                citcursocurrente = itemscursocurrente.next();
//                if (((Curso) citcursocurrente.getValue()).getIdCurso() == cursocurrente) {
//                    cbcur.setSelectedItem(citcursocurrente);
//                    break;
//                }
//            }
            cbcur.setReadonly(true);
        }

        ((Intbox) win.getFellow("ibidEstudante")).setValue(estudante.getIdEstudante().intValue());
        ((Intbox) win.getFellow("ibano")).setValue(mat.getMatriculaPK().getAno());
        ((Textbox) win.getFellow("tbnomeCompleto")).setValue(estudante.getNomeCompleto());
        ((Textbox) win.getFellow("tbapelido")).setValue(estudante.getApelido());
        final Iterator<Comboitem> itemscursocurrente = new ArrayList(((Combobox) win.getFellow("cbcursocurrente")).getItems()).listIterator();
        Comboitem citcursocurrente;
        while (itemscursocurrente.hasNext()) {
            citcursocurrente = itemscursocurrente.next();
            if (((Curso) citcursocurrente.getValue()).getIdCurso() == cursocurrente) {
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

        if (!mat.getEstado()) {
            //recibos
            ((Row) win.getFellow("rwRU")).setVisible(false);
            par.clear();
            par.put("mat", mat);
            List<Arquivomatricula> lam3 = csimpm.findByJPQuery("from Arquivomatricula am where"
                    + " am.matricula = :mat", par);
            List<Arquivo> lm3 = new ArrayList<Arquivo>();
            List<Arquivo> lm4 = new ArrayList<Arquivo>();
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

    public void onExcelExport() throws ParseException {
        if (lbplm.getItemCount() == 0) {
            Clients.showNotification("Sem conteúdo", "warning", null, null, 3000);
            return;
        }
        BeanToExcel beanToExcel = new BeanToExcel();
        beanToExcel.setDataSheetName("Grade de Notas");
        beanToExcel.exportExcell(lbplm);
    }

    public void initLB(String sql, Listbox lb, Paging p, Class o) {
        p.setTotalSize(csimpm.count(o));
        final int PAGE_SIZE = p.getPageSize();
        setLBModel(sql, lb, o, 0, PAGE_SIZE);
    }

    public void setLBModel(String sql, Listbox lb, Class c, int o, int p) {
        lbplm.setModel(new ListModelList<>(csimpm.findByJPQueryFilter(sql, condpar, o, p)));
    }

    public void onSortEventListener(Event event, String sql, final Listbox lb, Paging p, final Class o) {
        final Listheader lh = (Listheader) event.getTarget();
        final String sortDirection = lh.getSortDirection();

        if ("ascending".equals(sortDirection)) {
            ord = "order by d." + lh.getId() + " asc";
            initLB(sql + "order by d." + lh.getId() + " asc", lb, p, o);
            p.setActivePage(0);
            addeventoPagin(sql + ord, lb, p, o);
        } else if ("descending".equals(sortDirection) || "natural".equals(sortDirection) || Strings.isBlank(sortDirection)) {
            ord = "order by d." + lh.getId() + " desc";
            initLB(sql + "order by d." + lh.getId() + " desc", lb, p, o);
            p.setActivePage(0);
            addeventoPagin(sql + ord, lb, p, o);
        }
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

    public void addeventoLB(final String sql, final Listbox lb, Paging p, final Class o) {
        addeventoOrd(sql, lb, p, o);
        addeventoPagin(sql, lb, p, o);
    }

    public void addeventoPagin(final String sql, final Listbox lb, Paging p, final Class o) {
        final int PAGE_SIZE = p.getPageSize();
        initLB(sql, lb, p, o);
        p.addEventListener("onPaging", new EventListener() {
            public void onEvent(Event event) {
                PagingEvent pe = (PagingEvent) event;
                int pgno = pe.getActivePage();
                int ofs = pgno * PAGE_SIZE;
                // Redraw current paging
                setLBModel(sql, lb, o, ofs, PAGE_SIZE);
            }
        });
    }
//
//    public void editCond(){
//       
//    }
//    
//    public void addeventoPInput(Component c, final String sq,final String cond) {
//        c.addEventListener("onChanging", new EventListener() {
//            @Override
//            public void onEvent(Event event) {
//                InputEvent evt = (InputEvent) event;
//                if (!evt.getValue().equals("") && evt.getValue().charAt(0) != '.') {
//                    sql=sql+sq;
//                    condpar.put("nr", "%" + evt.getValue().toLowerCase() + "%");
//                } else {
//                    cond = "";
//                    condpar.remove("nr");
//                }
//                addeventoLB("from Funcionario f where 1=1" + );
//            }
//        });
//    }
//
//    public void addeventoPSel(Component c, final String sql, Component c2, final String sql2) {
//        //final int PAGE_SIZE = p.getPageSize();
//        // initLB(sql, lb, p, o);
//        c.addEventListener("onChange", new EventListener() {
//            public void onEvent(Event event) {
//
//            }
//        });
//    }

    ////////////////////////////////////PESQUISAS//////////////////////////////////////////////////////
    public void onChanging$txPbi(InputEvent evt) {
        if (!evt.getValue().equals("") && evt.getValue().charAt(0) != '.') {
            condnr = " and la.nrBI = :nr ";
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
            condnome = " and lower(la.nome) like :nome ";
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

//    public void onChanging$ibProcAno(InputEvent evt) {
//        if (!evt.getValue().equals("") && evt.getValue().charAt(0) != '.') {
//            condano = " and extract(year from i.dataInscricao) = :a ";
//            if (condpar.containsKey("a")) {
//                condpar.replace("a", Integer.parseInt(evt.getValue()));
//            } else {
//                condpar.put("a", Integer.parseInt(evt.getValue()));
//            }
//        } else {
//            condano = "";
//            if (condpar.containsKey("a")) {
//                condpar.remove("a");
//            }
//        }
//        setLB(0, 20);
//    }
    public void onSelect$cbcurso() {
        if (cbcurso.getSelectedIndex() != 0) {
            condcurso = " and la.curso = :curso ";
            condpar.remove("fac");
            if (condpar.containsKey("curso")) {
                condpar.replace("curso", (Curso) cbcurso.getSelectedItem().getValue());
            } else {
                condpar.put("curso", (Curso) cbcurso.getSelectedItem().getValue());
            }
        } else {
            condpar.remove("curso");
            condcurso = " and la.curso.faculdade = :fac ";
            Faculdade f;
            if (cbfaculdade.isVisible()) {
                f = csimpm.get(Faculdade.class, ((Faculdade) cbfaculdade.getSelectedItem().getValue()).getIdFaculdade());
            } else {
                f = csimpm.get(Faculdade.class, usr.getFaculdade().getIdFaculdade());
            }
            condpar.put("fac", f);
        }
        setLB(0, 20);
    }

    public void setLB(int i, int j) {
        if (j == 20) {
            lbplm.setModel(new ListModelList<Listaadmissao>());
        }
        List<Listaadmissao> li = csimpm.findByJPQueryFilter("from Listaadmissao la where 1=1" + condcurso + condnome + condano + condnr + " order by la.numero", condpar, i, j);
        final Iterator<Listaadmissao> items = li.iterator();
        Listaadmissao e;
        lbplm.setRows(lbplm.getItemCount() + li.size());
        while (items.hasNext()) {
            e = items.next();
            ((ListModelList) lbplm.getModel()).add(e);
        }
        if (li.size() < j) {
            btv.setVisible(false);
        } else {
            btv.setVisible(true);
        }
    }

    public void onLoadi() {
        int i = lbplm.getItemCount();
        setLB(i, i + 20);
    }

    public void onUploadFile(ForwardEvent evt) {
        Button button = (Button) evt.getOrigin().getTarget();
        button.addEventListener("onUpload", new EventListener<UploadEvent>() {
            @Override
            @SuppressWarnings("empty-statement")
            public void onEvent(UploadEvent event) throws Exception {
                String alert = "";
                Media media = event.getMedia();
                janelaInformacao.setVisible(false);
                boolean t, j = false, k = false;

                Hashtable<String, Integer> cursosCounter = getLast();
                //  Hashtable<String, Integer> cursosCounter = new Hashtable<>();

//                List<Curso> cursos = csimpm.getAll(Curso.class);
//
//                for (Curso curso : cursos) {
//                    if (!cursosCounter.containsKey(curso.getAbreviatura())) {
//                        cursosCounter.put(curso.getAbreviatura(), 1);
//                    }
//                }
                Transaction transacao = csimpm.getTransacao();

                try {

                    Date dano = new Date();
                    Calendar cal = new GregorianCalendar();
                    cal.setTime(dano);
                    int ano = cal.get(Calendar.YEAR);
                    Workbook wb = WorkbookFactory.create(media.getStreamData());
                    Sheet sheet = wb.getSheetAt(0);

                    double nota = 0;
                    // double nrTelefone = 0;
                    // double telefone = 0;
                    int val = 0;
                    // double nrTelefoneAlternativo = 0;

                    //  String dataNasc = "";
                    String cidade = "";
                    String nome = "";
                    String curso = "";
                    //   String nrBI = "";
                    //   String apelido = "";

                    t = false;
                    j = false;

                    transacao.begin();

                    for (org.apache.poi.ss.usermodel.Row row : sheet) {
                        val = 0;
                        if (t) {
                            for (Cell cell : row) {
                                switch (val) {
                                    case 0:
                                        //  Messagebox.show(cell.getStringCellValue());
                                        // ce ll.setCellType(Cell.CELL_TYPE_NUMERIC);
//                                        nota = cell.getNumericCellValue();
                                        cidade = cell.getStringCellValue();
                                        break;
//                                        nota = new Double(cell.getStringCellValue());
//                                        break;
                                    case 1:
                                        nome = cell.getStringCellValue();
                                        break;
//                                        apelido = cell.getStringCellValue();
//                                        break;
                                    case 2:
                                        curso = cell.getStringCellValue();
                                        break;
//                                        nome = cell.getStringCellValue();
//                                        break;
                                    case 3:
                                        nota = new Double(cell.getNumericCellValue());
                                        break;
//                                        curso = cell.getStringCellValue();
//                                        break;
//                                    case 4:
//                                        dataNasc = cell.getStringCellValue();
//                                        break;
//                                    case 5:
//                                        cidade = cell.getStringCellValue();
//                                        break;
//                                    case 6:
//                                        nrTelefone = Integer.parseInt(cell.getStringCellValue());
//                                        break;
//                                    case 7:
//                                        nrTelefoneAlternativo = Integer.parseInt(cell.getStringCellValue());
//                                        break;
                                }

                                if (val == 3 && !curso.isEmpty()) {

                                    //  String tel = (int) nrTelefone + "";
                                    String cursoabrev = curso;
                                    // Messagebox.show(cursoabrev+" "+row.getRowNum());
                                    if (curso.contains(" (Pos-Laboral)")) {
                                        cursoabrev = cursoabrev.replace(" (Pos-Laboral)", "");
                                    }
                                    String sql = "from Curso c where lower(c.abreviatura) =  \'" + cursoabrev.toLowerCase() + "\'";
                                    Curso c = (Curso) csimpm.findEntByJPQuery(sql, null);

                                    if (c != null) {
                                        // long i = (long) telefone;
                                        String nmec = "";
                                        int anoIngresso = Calendar.getInstance().get(Calendar.YEAR);
                                        String faculdade = c.getFaculdade().getCodigo();
                                        String codigoCurso = c.getCodigoCurso();

                                        Integer cont = cursosCounter.get(c.getAbreviatura());
                                        cont++;
                                        if (cont.toString().length() == 1) {
                                            nmec = anoIngresso + faculdade + codigoCurso + "00" + cont;
                                        } else if (cont.toString().length() == 2) {
                                            nmec = anoIngresso + faculdade + codigoCurso + "0" + cont;
                                        }

                                        cursosCounter.put(c.getAbreviatura(), cont);
                                        //    String date[] = dataNasc.split("/");
                                        //   Calendar d = new GregorianCalendar(Integer.parseInt(date[2]), Integer.parseInt(date[1]), Integer.parseInt(date[0]));

                                        Listaadmissao novoIngresso = new Listaadmissao();

                                        /*Dados do novo ingresso*/
                                        novoIngresso.setNome(nome);
                                        //    novoIngresso.setApelido(apelido);
                                        //    novoIngresso.setTelefone("" + nrTelefone);
                                        novoIngresso.setNotaAdmissao(nota);
                                        novoIngresso.setCidade(cidade);
                                        //    novoIngresso.setContacto(tel + "");
                                        //   novoIngresso.setContactoAlternativo(nrTelefoneAlternativo + "");
                                        //    novoIngresso.setDataNascimento(d.getTime());
                                        novoIngresso.setCurso(c);
                                        novoIngresso.setNumero(nmec);
                                        novoIngresso.setAno(ano);
                                        //validar se na base de dados nao existir turno 1 e 2
                                        if (c.getTurno().getIdPeriodo() == 2 || curso.contains("(Pos-Laboral)")) {
                                            novoIngresso.setTurno(2);
                                        } else {
                                            novoIngresso.setTurno(1);
                                        }

                                        /*Por padrao o estudante novo Inresso nao esta matriculado*/
                                        novoIngresso.setMatriculado(false);
//Messagebox.show(novoIngresso.getNumero());
                                        csimpm.Saves(novoIngresso);
                                        val = -1;
                                    } else {
                                        j = true;
                                        Clients.showNotification("Corrija o Curso: " + curso + " do estudante:\n" + "[" + nome + "] ", Clients.NOTIFICATION_TYPE_WARNING, null, null, 10000);
                                        break;
                                    }
                                }
                                val++;
                            }
                        } else {
                            t = true;
                        }

                        if (j) {
                            break;
                        }

                    }

                    if (j) {
                        transacao.rollback();
                    } else {
                        Clients.showNotification("O ficheiro " + media.getName() + " foi importado com sucesso.", null, null, null, 2000);
                        transacao.commit();
                    }
                } catch (IOException | InvalidFormatException | NumberFormatException e) {
                    Clients.showNotification("Ocorreu uma falha na Importacao do ficheiro " + media.getName(), Clients.NOTIFICATION_TYPE_ERROR, null, null, 3000);
                    transacao.rollback();
                    j = true;
                } finally {
                    janelaInformacao.detach();
                }
                setLB(0, 20);
                janelaInformacao.detach();
            }

        });

    }

    public void onClickApearInfo() {
//        janelaInformacao.setVisible(true);
//        janelaInformacao.doModal();
        Executions.createComponents("matricula/importingresso.zul", winmain, null);
    }

    public void onCanlar() {
        janelaInformacao.detach();
    }

    public void onSetFalseVisibility(ForwardEvent evt) {
        janelaInformacao.setVisible(false);
    }

    private Hashtable<String, Integer> getLast() {

        List<Listaadmissao> la = csimpm.getAll(Listaadmissao.class);
        List<Curso> cursos = csimpm.getAll(Curso.class);
        Hashtable<String, Integer> map = new Hashtable<>();

        // copia todos os cursos existentes para um dicionario e os inicia a zero
        for (Curso c : cursos) {
            map.put(c.getAbreviatura(), 0);
        }
        String n,n1 = null;
        try {
            for (Listaadmissao l : la) {

                if (l.getAno() == 2020) {
//               Messagebox.show(l.getNumero()+"");
//               Messagebox.show(l.getNome());
                    String abreviatura = l.getCurso().getAbreviatura();
                    Integer mapValue = map.get(abreviatura);

                    Integer numero = null;
                    n1=l.getNome()+l.getCurso();
                    n = getLastThree(l.getNumero());
                    numero = Integer.parseInt(n);
                    //} catch(Exception e) {

                    if (numero > mapValue) {
                        map.replace(abreviatura, numero);
                    }
                }

            }
        } finally {
            Messagebox.show(n1 + "");
        }
        return map;
    }

    private String getLastThree(String n) {
//        Messagebox.show(n + " " + n.length() + "");
//        Messagebox.show((n.length() - 3) + "");
//        Messagebox.show((n.length() - 3) + "");
//        Messagebox.show((n.length() - 3) + "");
        return n.charAt((n.length() - 3)) + "" + n.charAt(n.length() - 2) + "" + n.charAt(n.length() - 1) + "";
    }
}
