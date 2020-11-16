/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package esira.relatorio;

import ExcelExport.BeanToExcel;
import ExcelExport.ExcelColumns;
import ExcelExport.FormatType;
import entidade.ProvinciaSexo;
import esira.domain.Arquivomatricula;
import esira.domain.Bolsa;
import esira.domain.Curso;
import esira.domain.Documento;
import esira.domain.Estadocivil;
import esira.domain.Estudante;
import esira.domain.Faculdade;
import esira.domain.Funcionario;
import esira.domain.Ingressobolseiro;
import esira.domain.Ingressoexameadmissao;
import esira.domain.Ingressomudancauniversidade;
import esira.domain.Listaadmissao;
import esira.domain.Matricula;
import esira.domain.MatriculaPK;
import esira.domain.Pais;
import esira.domain.PlanificacaoAnoLectivo;
import esira.domain.Prescricao;
import esira.domain.Profissao;
import esira.domain.Provincia;
import esira.domain.Tipodocumento;
import esira.domain.Users;
import esira.domain.Viaingresso;
import esira.service.CRUDService;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import org.zkoss.lang.Strings;
import org.zkoss.util.media.AMedia;
import org.zkoss.util.media.Media;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.event.InputEvent;
import org.zkoss.zk.ui.event.OpenEvent;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Div;
import org.zkoss.zul.Iframe;
import org.zkoss.zul.Image;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listhead;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Menuitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Paging;
import org.zkoss.zul.Radio;
import org.zkoss.zul.Radiogroup;
import org.zkoss.zul.Row;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;
import org.zkoss.zul.event.PagingEvent;

/**
 *
 * @author Tafula
 */
public class RelMatProvincia extends GenericForwardComposer {

    @WireVariable
    private CRUDService csimpm = (CRUDService) SpringUtil.getBean("CRUDService");
    private Listbox lbEstudantes;
    private ListModel<Matricula> listEstudanteModel;
    private ListModel<Curso> listaCursoModel;
    Window winlestudantes, mDialogMatricula;
    private Menuitem manoi;
    private Menuitem mgenero;
    private Menuitem mmatric, minsc;
    private Menuitem mnivel;
    private Menuitem mnac;
    private Menuitem mprov;
    private Menuitem mactivo, mnactivo,mfaxa;
    private Textbox txAno, txProcurar, txPNrmec;
    private Combobox cbcurso, cbnacionalidade, cbfaculdade, cbprovincia, cbPeriodDisc;
    private Intbox ibAno, ibPAno, ibNivel,fxa1,fxa2;
    private Radiogroup pgenero, pactivo, pmatric, pminsc;
    private Radio rgsexo, rgactivo, rgsmatric, rgsminsc;
    Button btnProcurar;
    Div dfaxa;
    List listaM;
    Map<String, Object> par = new HashMap<String, Object>();
    private Paging pagEstudante;
    private Listheader lhnome;
    private Intbox litem;
    private Label lb, totale,lbAno, lbPAno;
    private Button btv;
    static String ord = "";
    private List<Estudante> estudantes = null;
    String condfac = "", condnr = "", condnome = "", condanoi = "",condfaxa = "",
            condcurso = "", condactivo = " and e.estado is true", condmatric = "", condminsc = "", condnivel = "";
    Map<String, Object> condpar = new HashMap<String, Object>();
    Users usr = (Users) Sessions.getCurrent().getAttribute("user");

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        Date dano = new Date();
        Calendar cal = new GregorianCalendar();
        cal.setTime(dano);
        cbcurso.setStyle("display:none");
        lbAno.setStyle("display:none");
        ibAno.setStyle("display:none");
        pmatric.setStyle("display:none");
        dfaxa.setStyle("display:none");
        pminsc.setStyle("display:none");
        lbPAno.setStyle("display:none");
        ibPAno.setStyle("display:none");
        cbPeriodDisc.setStyle("display:none");
        pactivo.setStyle("display:none");
        ibNivel.setStyle("display:none");
        ibAno.setValue(cal.get(Calendar.YEAR));
        ibPAno.setValue(cal.get(Calendar.YEAR));
        ibNivel.setValue(1);
        // lbEstudantes.setModel(getListEstudanteModel());
        if (lbEstudantes != null) {
            condpar.clear();
            condpar.put("p", null);
            setLB(0, 20);
            //addeventoLB("from Estudante e where 1=1" + ord, lbEstudantes, pagEstudante, Estudante.class);
        }
    }

    public ListModel<Matricula> getListEstudanteModel() {
        //retorna as matriculas que nao foram anuladas
        Calendar c = Calendar.getInstance();
        par.clear();
        par.put("user", usr.getUtilizador());
        //Users u = csimpm.findEntByJPQuery("from Users u where u.utilizador = :user", par);
        par.clear();
        Faculdade f = csimpm.get(Faculdade.class, usr.getFaculdade().getIdFaculdade());
        par.put("fac", f);
        List<Curso> lc = csimpm.findByJPQuery("from Curso c where c.faculdade =:fac", par);
        par.put("curso", lc.get(0));
        listaM = csimpm.findByJPQuery("from Matricula m where m.curso.faculdade = :fac and m.curso=:curso and m.confirmacao is not null and m.estado is true and m.matriculaPK not in (select ma.matriculaPK from "
                + "Matriculaanulada ma)", par);
        return listEstudanteModel = new ListModelList<Matricula>(listaM);
    }

    public ListModel<Faculdade> getFaculdadePModel() {
        List<Faculdade> lf = new ArrayList<Faculdade>();
        Faculdade f = new Faculdade();
        f.setDesricao("-----Todas Faculdades----");
        lf.add(f);
        lf.addAll(csimpm.getAll(Faculdade.class));
        return new ListModelList<Faculdade>(lf);
    }

    public ListModel<Curso> getListaCursoModel() {
        return listaCursoModel = new ListModelList<Curso>(csimpm.getAll(Curso.class));
    }

    public ListModel<Pais> getListaNacionalidadeModel() {
        List<Pais> lp = csimpm.getAll(Pais.class);
        return new ListModelList<Pais>(lp);
    }

    public void onRenovar(ForwardEvent evt) throws Exception {
        Button btn2 = (Button) evt.getOrigin().getTarget();
        Listitem litem2 = (Listitem) btn2.getParent().getParent();
        Matricula todo = (Matricula) litem2.getValue();
        PlanificacaoAnoLectivo pal = new PlanificacaoAnoLectivo();
        List<PlanificacaoAnoLectivo> pals = csimpm.getAll(PlanificacaoAnoLectivo.class
        );
        Date dano = new Date();
        Calendar cal = new GregorianCalendar();

        cal.setTime(dano);
        int ano = cal.get(Calendar.YEAR);
        for (PlanificacaoAnoLectivo planificacaoAnoLectivo : pals) {
            if (planificacaoAnoLectivo.getAno() == ano) {
                if (dano.before(planificacaoAnoLectivo.getDataInicioMatricula())) {
                    Clients.showNotification("Por favor aguarde o periodo de Matriculas", "warning", null, null, 0, true);
                    return;
                }
                if (dano.after(planificacaoAnoLectivo.getDataInicioMatricula())) {
                    if (dano.before(planificacaoAnoLectivo.getDataFinalMatricula())) {
                        final HashMap<String, Object> map = new HashMap<String, Object>();
                        map.put("periodo", "NORMAL");
                        Window win = (Window) Executions.createComponents("/matricula/Matricula.zul", winlestudantes, map);
                        Textbox tbnome = (Textbox) win.getFellow("tbnomeCompleto");
                        ((Label) win.getFellow("lbtaxaMatricula")).setValue(planificacaoAnoLectivo.getTaxaMatriculaNacional().toString());
                        ((Label) win.getFellow("lbtaxaMatriculaE")).setValue(planificacaoAnoLectivo.getTaxaMatriculaEstrangeiro().toString());
                        ((Row) win.getFellow("rwTaxamulta30")).setVisible(false);
                        ((Row) win.getFellow("rwTaxamulta15")).setVisible(false);
                        tbnome.setValue(todo.getEstudante().getNomeCompleto());
                        tbnome.setReadonly(true);
                        renovaMatricula(win, todo);
                    } else {
                        if (dano.after(planificacaoAnoLectivo.getDataFinalMatricula())
                                && dano.before(planificacaoAnoLectivo.getDataFimMatriculaE2())) {
                            Calendar c = Calendar.getInstance();
                            c.setTime(planificacaoAnoLectivo.getDataFinalMatricula());
                            c.add(Calendar.DAY_OF_MONTH, 15);
                            if (c.getTime().after(dano)) {
                                final HashMap<String, Object> map = new HashMap<String, Object>();
                                map.put("periodo", "M15");
                                Window win = (Window) Executions.createComponents("/matricula/Matricula.zul", winlestudantes, map);
                                Textbox tbnome = (Textbox) win.getFellow("tbnomeCompleto");
                                ((Label) win.getFellow("lbtaxaMatricula")).setValue(planificacaoAnoLectivo.getTaxaMatriculaNacional().toString());
                                ((Label) win.getFellow("lbtaxaMatriculaE")).setValue(planificacaoAnoLectivo.getTaxaMatriculaEstrangeiro().toString());
                                ((Row) win.getFellow("rwTaxamulta15")).setVisible(true);
                                ((Row) win.getFellow("rwTaxamulta30")).setVisible(false);
                                tbnome.setValue(todo.getEstudante().getNomeCompleto());
                                tbnome.setReadonly(true);
                                renovaMatricula(win, todo);
                            } else {
//                                Messagebox.show("Periodo de Multa 30 dias = " + planificacaoAnoLectivo.getPercentagemMultaMatricula30dias());
                                final HashMap<String, Object> map = new HashMap<String, Object>();
                                map.put("periodo", "M30");
                                Window win = (Window) Executions.createComponents("/matricula/Matricula.zul", winlestudantes, map);
                                Textbox tbnome = (Textbox) win.getFellow("tbnomeCompleto");
                                ((Label) win.getFellow("lbtaxaMatricula")).setValue(planificacaoAnoLectivo.getTaxaMatriculaNacional().toString());
                                ((Label) win.getFellow("lbtaxaMatriculaE")).setValue(planificacaoAnoLectivo.getTaxaMatriculaEstrangeiro().toString());
                                //    ((Label) win.getFellow("lbtaxaMuniversidade")).setValue(planificacaoAnoLectivo.getTaxaDeMudancaoTurno().toString());
                                ((Row) win.getFellow("rwTaxamulta30")).setVisible(true);
                                ((Row) win.getFellow("rwTaxamulta15")).setVisible(false);
                                tbnome.setValue(todo.getEstudante().getNomeCompleto());
                                tbnome.setReadonly(true);
                                renovaMatricula(win, todo);
                            }
                        }
                    }
                }

                if (dano.after(planificacaoAnoLectivo.getDataFimMatriculaE2())) {
                    Clients.showNotification("Periodo de Matricula encerrado."
                            + "É necessário uma permissão", "warning", null, null, 0, true);
                    return;
                }
            }
        }
    }

    public void setCombobox(Combobox cb, int id) {

    }

    public void renovaMatricula(Window win, Matricula mat) {
        Combobox cbcur = (Combobox) win.getFellow("cbcursocurrente");
        List<Curso> lc = new ArrayList<Curso>();
        lc.add(mat.getEstudante().getCursocurrente());
        cbcur.setModel(new ListModelList<Curso>(lc));
        cbcur.setReadonly(true);
        Estudante estudante = mat.getEstudante();
        ((Intbox) mDialogMatricula.getFellow("ibidEstudante")).setValue(estudante.getIdEstudante().intValue());
//((Textbox) mDialogMatricula.getFellow("tbapelido")).setValue(estudante.getApelido());
        ((Datebox) mDialogMatricula.getFellow("dabdataNascimento")).setValue(estudante.getDataNascimento());
        ((Textbox) mDialogMatricula.getFellow("tbnomePai")).setValue(estudante.getNomePai());
        ((Textbox) mDialogMatricula.getFellow("tbnomeMae")).setValue(estudante.getNomeMae());
        ((Textbox) mDialogMatricula.getFellow("tbnaturalidade")).setValue(estudante.getNaturalidade());
        ((Textbox) mDialogMatricula.getFellow("tblocalidade")).setValue(estudante.getLocalidade());
        ((Textbox) mDialogMatricula.getFellow("tbdistrito")).setValue(estudante.getDistrito());
        ((Intbox) mDialogMatricula.getFellow("ibanoTerMedio")).setValue(estudante.getAnoTerMedio());
        ((Textbox) mDialogMatricula.getFellow("tbescola")).setValue(estudante.getEscola());
        ((Intbox) mDialogMatricula.getFellow("ibanoIngresso")).setValue(estudante.getAnoIngresso());
        ((Intbox) mDialogMatricula.getFellow("ibnivelFrequencia")).setValue(estudante.getNivelFrequencia());
        ((Textbox) mDialogMatricula.getFellow("tbpastaDocumento")).setValue(estudante.getPastaDocumento());
        ((Textbox) mDialogMatricula.getFellow("tbnomeEncarregado")).setValue(estudante.getNomeEncarregado());
        ((Textbox) mDialogMatricula.getFellow("tbcontactoEncarregado")).setValue(estudante.getContactoEncarregado());
        ((Textbox) mDialogMatricula.getFellow("tbgrauParentesco")).setValue(estudante.getGrauParentesco());
        ((Intbox) mDialogMatricula.getFellow("ibtamAgregadoFamiliar")).setValue(estudante.getTamAgregadoFamiliar());
        ((Textbox) mDialogMatricula.getFellow("tbprimeiraUniversidade")).setValue(estudante.getPrimeiraUniversidade());
        ((Textbox) mDialogMatricula.getFellow("tbidioma")).setValue(estudante.getIdioma());
        ((Textbox) mDialogMatricula.getFellow("tbemail")).setValue(estudante.getEmail());
        ((Intbox) mDialogMatricula.getFellow("ibnotaAdmissao")).setValue(estudante.getNotaAdmissao());
        ((Textbox) mDialogMatricula.getFellow("tboutraViaIngresso")).setValue(estudante.getOutraViaIngresso());
//((Radio) mDialogMatricula.getFellow("raistrabalhador")).setValue(estudante.getIstrabalhador());
//((Radio) mDialogMatricula.getFellow("raisTransferencia")).setValue(estudante.getIsTransferencia());
//((Radio) mDialogMatricula.getFellow("raisMudancaUn")).setValue(estudante.getIsMudancaUn());
//((Radio) mDialogMatricula.getFellow("raisBolseiro")).setValue(estudante.getIsBolseiro());
//((Radio) mDialogMatricula.getFellow("ramasculino")).setValue(estudante.getMasculino());
//            if (!t) {
//                Sessions.getCurrent().setAttribute("estudante", estudante);
//            }
    }

    public void onEdit(ForwardEvent event) {

        Button btn = (Button) event.getOrigin().getTarget();
        Listitem litem = (Listitem) btn.getParent().getParent();
        Matricula todo = (Matricula) litem.getValue();

        final HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("matricula", todo);
        Executions.createComponents("/matricula/EditarMatricula.zul", null, map);
    }

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
        params.put("anol", ibAno.getValue());
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

//    public void onClick$mactivo(Event e) throws InterruptedException {
//        if (mactivo.isCheckmark()) {
//            mnactivo.setChecked(false);
//            condactivo = " and e.estado is true";
//            condpar.put("estado", true);
//        } else {
//            condactivo = "";
//            condpar.remove("estado");
//        }
//        setLB(0,20);
//    }
    public void onClick$mnactivo(Event e) throws InterruptedException {
        if (mnactivo.isChecked()) {
            mactivo.setChecked(false);
            condactivo = " and e.estado is not true";
            // condpar.put("estado", false);
        } else {
            condactivo = " and e.estado is true";
            // condpar.remove("estado");
        }
        setLB(0, 20);
    }
//    
//    public void onClick$mactivo(Event e) throws InterruptedException {
//        pactivo.setVisible(mactivo.isChecked());
//        if (mactivo.isChecked()) {
//            condactivo = " and e.estado = :estado";
//            condpar.put("estado", rgactivo.isChecked());
//        } else {
//            condactivo = "";
//            condpar.remove("estado");
//        }
//        setLB(0, 20);
//    }
    
    public void onClick$manoi(Event e) throws InterruptedException {
        ibAno.setVisible(manoi.isChecked());
        if (manoi.isChecked()) {
            condanoi = " and e.anoIngresso = :anoi";
            condpar.put("anoi", ibAno.getValue());
        } else {
            condanoi = "";
            condpar.remove("anoi");
        }
        setLB(0, 20);
    }

    public void onClick$mfaxa(Event e) throws InterruptedException {
        dfaxa.setVisible(mfaxa.isChecked());
        if (mfaxa.isChecked()) {
            condfaxa = " and extract(year from (age(current_date, e.dataNascimento))) >= :fx1 "
                    + "and extract(year from (age(current_date, e.dataNascimento))) <=:fx2 ";
            condpar.put("fx1", fxa1.getValue());
            condpar.put("fx2", fxa2.getValue());
        } else {
            condfaxa = "";
            condpar.remove("fx1");
            condpar.remove("fx2");
        }
        setLB(0, 20);
    }

    public void onClick$mnivel(Event e) throws InterruptedException {
        ibNivel.setVisible(mnivel.isChecked());
        if (mnivel.isChecked()) {
            condnivel = " and e.nivelFrequencia = :nivel";
            condpar.put("nivel", ibNivel.getValue());
        } else {
            condnivel = "";
            condpar.remove("nivel");
        }
        setLB(0, 20);
    }

    public void onClick$mmatric(Event e) throws InterruptedException {
        pmatric.setVisible(mmatric.isChecked());
        if (mmatric.isChecked()) {
            Date dano = new Date();
            Calendar cal = new GregorianCalendar();
            cal.setTime(dano);
            int ano = cal.get(Calendar.YEAR);
            ibPAno.setValue(ano);
            condmatric = " and (e in (select m.estudante from Matricula m where "
                    + "m.matriculaPK.ano = :ano and (m.estado is true or m.confirmacao is null) and m.estudante not in (select ma.matriculaPK.idEstudante from Matriculaanulada ma where ma.matriculaPK.ano = :ano)))";
            if (!pminsc.isVisible()) {
                condpar.put("ano", ano);
                ibPAno.setVisible(true);
            }
        } else {
            condmatric = "";
            //condpar.remove("buleano");
            if (!pminsc.isVisible()) {
                condpar.remove("ano");
                ibPAno.setVisible(false);
            }
        }
        setLB(0, 20);
    }

    public void onClick$minsc(Event e) throws InterruptedException {
        pminsc.setVisible(minsc.isChecked());
        if (minsc.isChecked()) {
            Date dano = new Date();
            Calendar cal = new GregorianCalendar();
            cal.setTime(dano);
            int ano = cal.get(Calendar.YEAR);
            ibPAno.setValue(ano);
            condminsc = " and (e in (select i.idEstudante from Inscricao i where "
                    + "extract(year from i.dataInscricao) = :ano and (i.estado is true or i.dataConfirmacao is null) and i.semestre = :sem))";
            if (!pmatric.isVisible()) {
                condpar.put("ano", ano);
                ibPAno.setVisible(true);
            }
            cbPeriodDisc.setVisible(true);
            cbPeriodDisc.setSelectedIndex(0);
            condpar.put("sem", Short.parseShort("1"));
        } else {
            condminsc = "";
            condpar.remove("sem");
            if (!pmatric.isVisible()) {
                condpar.remove("ano");
                ibPAno.setVisible(false);
            }
            cbPeriodDisc.setVisible(false);
        }
        setLB(0, 20);
    }

//    public void initLB(String sql, Listbox lb, Paging p, Class o) {
//        p.setTotalSize(csimpm.count(o));
//        final int PAGE_SIZE = p.getPageSize();
//        setLBModel(sql, lb, o, 0, PAGE_SIZE);
//    }
//
//    public void setLBModel(String sql, Listbox lb, Class c, int o, int p) {
//        List<Estudante> estudantes = csimpm.findByJPQueryFilter(sql, condpar, o, p);
//        // funcionarioModel = new ListModelList<Funcionario>(funcionarios);
//        lbEstudantes.setModel(new ListModelList<>(estudantes));
//    }
//
//    public void onSortEventListener(Event event, String sql, final Listbox lb, Paging p, final Class o) {
//        final Listheader lh = (Listheader) event.getTarget();
//        final String sortDirection = lh.getSortDirection();
//
//        if ("ascending".equals(sortDirection)) {
//            ord = "order by d." + lh.getId() + " asc";
//            initLB(sql + "order by d." + lh.getId() + " asc", lb, p, o);
//            p.setActivePage(0);
//            addeventoPagin(sql + ord, lb, p, o);
//        } else if ("descending".equals(sortDirection) || "natural".equals(sortDirection) || Strings.isBlank(sortDirection)) {
//            ord = "order by d." + lh.getId() + " desc";
//            initLB(sql + "order by d." + lh.getId() + " desc", lb, p, o);
//            p.setActivePage(0);
//            addeventoPagin(sql + ord, lb, p, o);
//        }
//    }
//
//    public void addeventoOrd(final String sql, final Listbox lb, final Paging p, final Class o) {
//        List<Listheader> list = lb.getListhead().getChildren();
//        for (Object object : list) {
//            if (object instanceof Listheader) {
//                Listheader lheader = (Listheader) object;
//
//                if (lheader.getSortAscending() != null || lheader.getSortDescending() != null) {
//
//                    lheader.addEventListener("onSort", new EventListener() {
//                        public void onEvent(Event event) {
//                            onSortEventListener(event, sql, lb, p, o);
//                        }
//                    });
//                }
//            }
//        }
//    }
//
//    public void addeventoLB(final String sql, final Listbox lb, Paging p, final Class o) {
//        addeventoOrd(sql, lb, p, o);
//        addeventoPagin(sql, lb, p, o);
//    }
//
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
    public ListModel<Faculdade> getListFacModel() {
        List<Faculdade> lf = new ArrayList<Faculdade>();
        Faculdade f = new Faculdade();
        f.setDesricao("------- Todas Faculdades-------");
        lf.add(f);
        List<Faculdade> lf2 = csimpm.getAll(Faculdade.class);
        lf.addAll(lf2);
        return new ListModelList<Faculdade>(lf);
    }

    public void onChange$cbfaculdade() {
        if (cbfaculdade.getSelectedIndex() != 0) {
            condfac = " and e.cursocurrente.faculdade = :fac";
            Faculdade f = (Faculdade) cbfaculdade.getSelectedItem().getValue();
            if (condpar.containsKey("fac")) {
                condpar.replace("fac", f);
            } else {
                condpar.put("fac", f);
            }
            par.clear();
            par.put("f", f);
            condcurso = "";
            if (condpar.containsKey("curso")) {
                condpar.remove("curso");
            }
            Curso cu = new Curso();
            cu.setDescricao("----------Todos Cursos---------");
            List<Curso> lc = new ArrayList<Curso>();
            lc.add(cu);
            List<Curso> lc2 = csimpm.findByJPQuery("from Curso c where c.faculdade = :f", par);
            lc.addAll(lc2);
            cbcurso.setModel(new ListModelList<Curso>(lc));
            cbcurso.setVisible(true);
        } else {
            condfac = "";
            condcurso = "";
            if (condpar.containsKey("fac")) {
                condpar.remove("fac");
            }
            if (condpar.containsKey("curso")) {
                condpar.remove("curso");
            }
            cbcurso.setVisible(false);
        }
        setLB(0, 20);
    }

    public void onChange$cbcurso() {
        if (cbcurso.getSelectedIndex() != 0) {
            condcurso = " and e.cursocurrente = :curso";
            condpar.put("curso", (Curso) cbcurso.getSelectedItem().getValue());
            condfac = "";
            condpar.remove("fac");
        } else {
            condcurso = "";
            condpar.remove("curso");
            //cbcurso.setVisible(false);
        }
        setLB(0, 20);
    }

    public void onCheck$pmatric() {
//        Date dano = new Date();
//        Calendar cal = new GregorianCalendar();
//        cal.setTime(dano);
//        int ano = cal.get(Calendar.YEAR);
        if (rgsmatric.isChecked()) {
            condmatric = " and (e in (select m.estudante from Matricula m where "
                    + "m.matriculaPK.ano = :ano and (m.estado is true or m.confirmacao is null) and m.estudante not in (select ma.matriculaPK.idEstudante from Matriculaanulada ma where ma.matriculaPK.ano = :ano)))";
        } else {
            condmatric = " and (e not in (select m.estudante from Matricula m where "
                    + "m.matriculaPK.ano = :ano and e.anoIngresso >= :ano and (m.estado is true or m.confirmacao is null) and m.estudante not in (select ma.matriculaPK.idEstudante from Matriculaanulada ma where ma.matriculaPK.ano = :ano)))";
        }
        //   condpar.put("ano", ano);  and e.anoIngresso >= :ano 
        setLB(0, 20);
    }

    public void onCheck$pminsc() {
//        Date dano = new Date();
//        Calendar cal = new GregorianCalendar();
//        cal.setTime(dano);
//        int ano = cal.get(Calendar.YEAR);
        if (rgsminsc.isChecked()) {
            condminsc = " and (e in (select i.idEstudante from Inscricao i where "
                    + "extract(year from i.dataInscricao) = :ano and (i.estado is true or i.dataConfirmacao is null) and i.semestre = :sem))";
        } else {
            condminsc = " and (e not in (select i.idEstudante from Inscricao i where "
                    + "extract(year from i.dataInscricao) = :ano and (i.estado is true or i.dataConfirmacao is null) and i.semestre = :sem))";
        }
        // condpar.put("ano", ano);
        setLB(0, 20);
    }

    public void onSelect$cbPeriodDisc() {
        if (cbPeriodDisc.getSelectedItem() != null) {
            //    check(hlayout);
            condpar.replace("sem", Short.parseShort(cbPeriodDisc.getSelectedItem().getValue().toString()));
            setLB(0, 20);
        }
    }

    public void onCheck$pactivo() {
        condactivo = " and e.estado = :estado";
        condpar.put("estado", rgactivo.isChecked());
        setLB(0, 20);
    }

    public void onChanging$txPNrmec(InputEvent evt) {
        if (!evt.getValue().equals("") && evt.getValue().charAt(0) != '.') {
            condnr = " and e.nrEstudante = :nr";
            condpar.put("nr", evt.getValue());
        } else {
            condnr = "";
            condpar.remove("nr");
        }
        setLB(0, 20);
    }

    public void onChanging$txProcurar(InputEvent evt) {
        if (!evt.getValue().equals("") && evt.getValue().charAt(0) != '.') {
            condnome = " and e.nomeCompleto = :nome";
            condpar.put("nome", "%" + evt.getValue().toLowerCase() + "%");
        } else {
            condnome = "";
            condpar.remove("nome");
        }
        setLB(0, 20);
    }

    public void onChanging$ibAno(InputEvent evt) {
        if (!evt.getValue().equals("") && evt.getValue().charAt(0) != '.') {
            condanoi = " and e.anoIngresso = :anoi";
            condpar.put("anoi", Integer.parseInt(evt.getValue()));
        } else {
            condanoi = "";
            condpar.remove("anoi");
        }
        setLB(0, 20);
    }

    public void onChanging$ibPAno(InputEvent evt) {
        if (!evt.getValue().equals("") && evt.getValue().charAt(0) != '.') {
//            condanoi = " and e.anoIngresso = :anoi";
            condpar.replace("ano", Integer.parseInt(evt.getValue()));
            setLB(0, 20);
        }
//        else {
////            condanoi = "";
////            condpar.remove("anoi");
//        }

    }

    public void onChanging$ibNivel(InputEvent evt) {
        if (!evt.getValue().equals("") && evt.getValue().charAt(0) != '.') {
            condnivel = " and e.nivelFrequencia = :nivel";
            condpar.put("nivel", Integer.parseInt(evt.getValue()));
        } else {
            condnivel = "";
            condpar.remove("nivel");
        }
        setLB(0, 20);
    }
    public void onChanging$fxa1(InputEvent evt) {
        if (!evt.getValue().equals("") && evt.getValue().charAt(0) != '.' && fxa2.getValue()!=null) {
            condfaxa = " and extract(year from (age(current_date, e.dataNascimento))) >= :fx1 "
                    + "and extract(year from (age(current_date, e.dataNascimento))) <=:fx2 ";
            condpar.put("fx1", Integer.parseInt(evt.getValue()));
            condpar.put("fx2", fxa2.getValue());
        } else {
            condfaxa = "";
            condpar.remove("fx1");
            condpar.remove("fx2");
        }
        setLB(0, 20);
    }
    public void onChanging$fxa2(InputEvent evt) {
        if (!evt.getValue().equals("") && evt.getValue().charAt(0) != '.' && fxa1.getValue()!=null) {
            condfaxa = " and extract(year from (age(current_date, e.dataNascimento))) >= :fx1 "
                    + "and extract(year from (age(current_date, e.dataNascimento))) <=:fx2 ";
            condpar.put("fx1", fxa1.getValue());
            condpar.put("fx2", Integer.parseInt(evt.getValue()));
        } else {
            condfaxa = "";
            condpar.remove("fx1");
            condpar.remove("fx2");
        }
        setLB(0, 20);
    }

    public void onExcelExport() throws ParseException {
        BeanToExcel beanToExcel = new BeanToExcel();
        beanToExcel.setDataSheetName("Lista de Estudantes");
        beanToExcel.exportExcell(lbEstudantes);
    }
//   public void onOpen$mtt(OpenEvent evt){
//      if( evt.isOpen()){
//         Listheader lh = (Listheader) evt.getReference();
//         Messagebox.show(lh.getId());
//      }
//   }

    public void onClickpl(ForwardEvent evt) {
        Menuitem mi = (Menuitem) evt.getOrigin().getTarget();
        Listheader lh = (Listheader) lbEstudantes.getListhead().getFellow(mi.getId().substring(1));
        lh.setVisible(!lh.isVisible());
    }

//    public ListModelList<Integer> getPageModel(){
//      return new ListModelList(new ArrayList<Integer>(Arrays.asList(5, 10, 20, 25, 50)));
//    }
    public void setLB(int i, int j) {
        int th = 0;
        int tm = 0;
        List<Provincia> ld = csimpm.findByJPQuery("from Provincia p order by p.ordregiao", null);
        final Iterator<Provincia> items2 = new ArrayList(ld).listIterator();
        Provincia p;
        List<ProvinciaSexo> lps = new ArrayList<ProvinciaSexo>();
        while (items2.hasNext()) {
            p = items2.next();
            if (p.getIdProvincia() != 12) {
                ProvinciaSexo ps = new ProvinciaSexo();
                ps.setNome(p.getDescricao());

                condpar.replace("p", p); //new Provincia(new Long("1")));
                //  condpar.remove("p");
                Long l = csimpm.countJPQuery("select count(e.idEstudante) from Estudante e where e.provincia =:p and e.masculino is true" + condfac + condanoi + condnivel +condfaxa+ condmatric
                        + condminsc + condactivo + condcurso + ord, condpar);
                ps.setHomem(l.intValue());
                th = th + l.intValue();
                l = new Long("0");
                l = csimpm.countJPQuery("select count(e.idEstudante) from Estudante e where e.provincia=:p and e.masculino is false" + condfac + condanoi + condnivel +condfaxa+ condmatric
                        + condminsc + condactivo + condcurso + ord, condpar);
                ps.setMulher(l.intValue());
                tm = tm + l.intValue();
                ps.setTotal(ps.getHomem() + ps.getMulher());
                lps.add(ps);
            }
        }
        condpar.remove("p");
        ProvinciaSexo ps = new ProvinciaSexo();
        ps.setNome("Estrangeiro");
        Long l = csimpm.countJPQuery("select count(e.idEstudante) from Estudante e where e.masculino is true and e.nacionalidade.idPais <> 122" + condfac + condanoi + condnivel +condfaxa+ condmatric
                + condminsc + condactivo + condcurso + ord, condpar);
        ps.setHomem(l.intValue());
        th = th + l.intValue();
        l = csimpm.countJPQuery("select count(e.idEstudante) from Estudante e where e.masculino is false and e.nacionalidade.idPais <> 122" + condfac + condanoi + condnivel +condfaxa+ condmatric
                + condminsc + condactivo + condcurso + ord, condpar);
        ps.setMulher(l.intValue());
        tm = tm + l.intValue();
        ps.setTotal(ps.getHomem() + ps.getMulher());
        lps.add(ps);
        ps = new ProvinciaSexo();
        ps.setNome("Total");
        ps.setHomem(th);
        ps.setMulher(tm);
        ps.setTotal(th + tm);
        lps.add(ps);

        Listbox li = lbEstudantes;
        li.setModel(new ListModelList<ProvinciaSexo>());
        condpar.put("p", null);
        //int k = ((ListModelList) li.getListModel()).getSize() + 1;
        // li.setRows(lps.size() + k);
        int ind = -1;
        for (ProvinciaSexo de : lps) {
            ++ind;
            de.setId(new Long(ind + 1));
            li.setMultiple(false);
            // l.setSizedByContent(false);
            //  l.getListhead().setSizable(false);
            ((ListModelList) li.getListModel()).add(ind, de);
            // l.setSizedByContent(true);
            //   l.getListhead().setSizable(true);
            li.setMultiple(true);
        }
//        if (li.size() < j) {
//            btv.setVisible(false);
//        } else {
//            btv.setVisible(true);
//        }
    }

    public void onLoadi() {
        int i = lbEstudantes.getItemCount();
        setLB(i, i + 20);
    }

}
