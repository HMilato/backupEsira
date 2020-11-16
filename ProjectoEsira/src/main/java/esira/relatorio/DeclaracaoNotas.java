/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package esira.relatorio;

import entidade.Declaracao;
import entidade.DisciplinaEstudante;
import esira.domain.Curso;
import esira.domain.Disciplina;
import esira.domain.Estudante;
import esira.domain.Faculdade;
import esira.domain.Funcionario;
import esira.domain.Inscricao;
import esira.domain.Inscricaodisciplina;
import esira.domain.Listaadmissao;
import esira.domain.Matricula;
import esira.domain.Provincia;
import esira.domain.Tipochefia;
import esira.domain.Users;
import esira.service.CRUDService;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JRDesignField;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import org.zkoss.util.media.AMedia;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventQueue;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.event.InputEvent;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.Button;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Hlayout;
import org.zkoss.zul.Iframe;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Radio;
import org.zkoss.zul.Radiogroup;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Vbox;
import org.zkoss.zul.Window;

/**
 *
 * @author Meneses
 */
public class DeclaracaoNotas extends GenericForwardComposer {

    private StringBuilder query;
    @WireVariable
    private CRUDService csimpm = (CRUDService) SpringUtil.getBean("CRUDService");

    Window WStudent;
    Listbox listStudents;
    @Wire
    private Textbox textboxPesquisar;
    Intbox idl, ide, ibidEstudante, ibano;
    Map<String, Object> par = new HashMap<String, Object>();
    Users usr = (Users) Sessions.getCurrent().getAttribute("user");
    private Button btv;
    private Radiogroup toRadio;
    private Radio rad1;
    private Radio rad2;
    private Intbox ate;
    private Intbox de;
    private Intbox somente;
    private Combobox cbSemestre;
    ////combobox var
    private Combobox combEst;
    private String pesq = null, condn = "";
    private Intbox posc;
    int indc = -1;
    Map<String, Object> cbpar = new HashMap<String, Object>();

    private ListModelList<Estudante> estudanteListModelList;

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        rad1.setChecked(true);
        somente.setDisabled(true);

    }

    public void onCheckRadio() {

        String temp = toRadio.getSelectedItem().getId();
        if (temp == rad1.getId()) {
            ate.setDisabled(false);
            de.setDisabled(false);
            somente.setDisabled(true);
        } else {
            somente.setDisabled(false);
            ate.setDisabled(true);
            de.setDisabled(true);

        }

    }

    /////////////////////////////////////COMBOBOX PESQUISA//////////////////////////////////////////////////////////////
    public void onCombEst(InputEvent evt) {
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
        combEst.getItems().clear();
        setLBComb(0, 20);
    }

    public void onOpen$combEst() {

        if (pesq == null) {
            //   Messagebox.show("esta");
            //  combEst.getItems().clear();
            setLBComb(0, 20);
            pesq = "";
        } else //  Messagebox.show("nao esta");
        if (combEst.isOpen()) {
            combEst.setText(pesq);
        } else if (indc != -1) {
            combEst.getSelectedIndex();
            combEst.setSelectedIndex(indc);
        }
    }

    public void setLBComb(int i, int j) {
        if (j == 20) {
            combEst.setModel(new ListModelList<Estudante>());
        }
        List<Estudante> li = null;
        // Users u = csimpm.get(Users.class, usr.getUtilizador());
        Faculdade f = csimpm.get(Faculdade.class, usr.getFaculdade().getIdFaculdade());
        // par.clear();
        // Date dano = new Date();
        //Calendar cal = new GregorianCalendar();
        // cal.setTime(dano);
        // int ano = cal.get(Calendar.YEAR);
        // cbpar.put("ano", ano);
        cbpar.put("fac", f);
        li = csimpm.findByJPQueryFilter("from Estudante e where e.cursocurrente.faculdade = :fac " + condn + " order by e.nomeCompleto", cbpar, i, j);
        final Iterator<Estudante> items = li.iterator();
        Estudante e;
        //lbinscricao.setRows(lbinscricao.getItemCount() + li.size());
        if (j > 20) {
            ((ListModelList) combEst.getModel()).remove(new Estudante());
            new Combobox().appendChild(combEst.getItemAtIndex(posc.getValue() - 1));
        }
        while (items.hasNext()) {
            e = items.next();
            ((ListModelList) combEst.getModel()).add(e);
        }
        if (li.size() == j) {
            Estudante es = new Estudante();
            es.setNomeCompleto("-------Ver Mais-------");
            ((ListModelList) combEst.getModel()).add(es);
            posc.setValue(((ListModelList) combEst.getModel()).size());
        }
    }

    public void onLoadComb() {
        int i = ((ListModelList) combEst.getModel()).size() - 1;
        setLBComb(i, i + 20);
    }

    public void onSCombEst(Event event) throws IOException {
        if (combEst.getSelectedIndex() == posc.getValue() - 1) {
            onLoadComb();
            combEst.open();
            combEst.setText(pesq);
            return;
        }
    }

    public void onRelatorio() throws JRException, IOException {

        int nivel, inivel, i;
        int semestre;
        List<Disciplina> ld = new ArrayList<>();
        if (combEst.getSelectedItem() == null) {
            return;
        }

        JRDesignField field2 = new JRDesignField();

        List<Declaracao> lde = new ArrayList<Declaracao>();
        Estudante e = (Estudante) combEst.getSelectedItem().getValue();
        e = csimpm.load(Estudante.class, e.getIdEstudante());
        Curso c = e.getCursocurrente();
        par.clear();
        par.put("cu", c);
        par.put("planoc", e.getPlanoc());

        if (toRadio.getSelectedItem().getId().equals(rad2.getId())) {

            nivel = somente.getValue();
            if (cbSemestre.getSelectedItem() == null) {
                Clients.showNotification("Por favor selecione o semestre", "warning", null, null, 0, true);
                return;
            }
            semestre = Integer.parseInt((String) cbSemestre.getSelectedItem().getValue());
            par.put("nivel", nivel);
            if (semestre == 3) {

                ld = csimpm.findByJPQuery("from Disciplina d where d.curso = :cu and d.planoc = "
                        + ":planoc and d.nivel = :nivel order by d.nivel, d.semestre", par);//filtrar
            } else {
                par.put("semestre", semestre);
                ld = csimpm.findByJPQuery("from Disciplina d where d.curso = :cu and d.planoc = "
                        + ":planoc and d.semestre = :semestre and d.nivel = :nivel order by d.nivel, d.semestre", par);//filtrar
            }

        } else {
            nivel = ate.getValue();

            inivel = de.getValue() == null ? 0 : de.getValue();

            par.put("nivel", nivel);
            par.put("inivel", inivel);
            if (cbSemestre.getSelectedItem() == null) {
                Clients.showNotification("Por favor selecione o semestre", "warning", null, null, 0, true);
                return;
            }
            semestre = Integer.parseInt((String) cbSemestre.getSelectedItem().getValue());
            ld = csimpm.findByJPQuery("from Disciplina d where d.curso = :cu and d.planoc = "
                    + ":planoc and d.nivel <= :nivel and d.nivel>= :inivel  order by d.nivel, d.semestre", par);//filtrar

            for (Iterator<Disciplina> iterator = ld.iterator(); iterator.hasNext();) {
                Disciplina d = iterator.next();
                if ((d.getNivel() == nivel) && (d.getSemestre() > semestre)) {
                    iterator.remove();
                }
            }

        }
        final Iterator<Disciplina> items2 = new ArrayList(ld).listIterator();
        List<String> notas = new Notas().getNotasEx();
        par.clear();
        par.put("e", e);
        par.put("d", null);
        Disciplina d;
        while (items2.hasNext()) {
            d = items2.next();
            Declaracao de;
            par.replace("d", d);
            List<Inscricaodisciplina> lid = csimpm.findByJPQuery("from Inscricaodisciplina id where id.inscricao.idEstudante = :e"
                    + " and id.disciplinaActiva = 3 and id.estado is true and id.notaFinal is not null and id.disciplina = :d ", par);
            for (Inscricaodisciplina id : lid) {
                de = new Declaracao();
                de.setDisciplina(d.getNome());
                int nota = Math.round(id.getNotaFinal());

                Calendar cal = new GregorianCalendar();
                cal.setTime(id.getInscricao().getDataInscricao());
                int ano = cal.get(Calendar.YEAR);
                de.setNivel(id.getDisciplina().getNivel());
                if (id.getNotaFinal() >= 10) {
                    de.setClassificacao(new DecimalFormat("#").format(id.getNotaFinal()) + " " + notas.get(nota));
                } else {
                    de.setClassificacao("Reprovado");
                }
                de.setAno(ano);
                lde.add(de);

            }
        }

        Collections.sort(lde);

        Provincia p = e.getProvincia();
        Curso cr = e.getCursocurrente();
        String path = WStudent.getDesktop().getWebApp().getRealPath("/relatorio");
        String path2 = WStudent.getDesktop().getWebApp().getRealPath("/img");

        Window win = (Window) Executions.createComponents("/report.zul", null, null);
        win.setTitle("Declaracao");

        JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(lde);//((ListModelList) lbinscdisc.getListModel()).getInnerList());
//
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("img", path2 + "/icoul.png");
        String nomeCompleto = e.getNomeCompleto();
        String nomePai = e.getNomePai();
        String nomeMai = e.getNomeMae();
        String naturalidade = e.getLocalidade();
        String distrito = e.getDistrito();
        String faculdade = e.getCursocurrente().getFaculdade().getDesricao();
//        List<Tipochefia> tp = new ArrayList<>();
//        tp = csimpm.findByJPQuery("from Tipochefia tp where tp.cargochefia = 3", null);//filtrar
        Users user = csimpm.get(Users.class, usr.getUtilizador());
        Funcionario f = user.getFaculdade().getDirector();
        String cidade = user.getFaculdade().getIddelegacao().getDelegacao();
        String director = f.getNome();
        String grau = f.getGrau();
        String grauchefe = user.getIdFuncionario().getGrau();
        String localizacao = user.getFaculdade().getLocalizacao();
//        tp = csimpm.findByJPQuery("from Tipochefia tp where tp.cargochefia = 4", null);//filtrar
//        String dirAcademico = tp.get(0).getFuncionario().getNome();
        String dirAcademico = user.getNome();
        Calendar cal = new GregorianCalendar();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMMMM yyyy", Locale.forLanguageTag("Pt"));
        String data = "";
        String genero = e.getMasculino() ? "o" : "a";
        String temp = dateFormat.format(cal.getTime());
        i = 0;
        for (String st : temp.split(" ", 3)) {
            data += st;
            if (i == 2) {
                break;
            }
            data += " de ";
            i++;
        }
        params.put("genero", genero);
        params.put("data", data);
        params.put("dirAcademico", dirAcademico);
        params.put("director", director);
        params.put("grauChefeRegisto", grauchefe);
        params.put("grauDirector", grau);
        params.put("nomeCompleto", nomeCompleto);
        params.put("faculdade", faculdade);
        params.put("localiz", localizacao);
        params.put("curso", cr.getDescricao());
        params.put("nomePai", nomePai);
        params.put("nomeMae", nomeMai);
        params.put("naturalidade", naturalidade);
        params.put("distrito", distrito);
        if (p != null) {
            params.put("provincia", p.getDescricao());
        } else {
            params.put("provincia", null);
        }
        params.put("cidade", cidade);

        String jrxmlFile = path + "/declaracao.jrxml";
        String jasperDestination = path + "/declaracao.jasper";
        JasperCompileManager.compileReportToFile(jrxmlFile, jasperDestination);
        JasperPrint jasperPrint = JasperFillManager.fillReport(path + "/declaracao.jasper", params, ds);
        JRPdfExporter exporter = new JRPdfExporter();
//
        ByteArrayOutputStream bytesOutputStream = new ByteArrayOutputStream();
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(bytesOutputStream);
        exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
        exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, bufferedOutputStream);
        exporter.exportReport();
        Iframe iframe = (Iframe) win.getFellow("report");
        InputStream mediais = new ByteArrayInputStream(bytesOutputStream.toByteArray());
        bytesOutputStream.close();

        AMedia amedia = new AMedia("declaracao.pdf", "pdf", "application/pdf", mediais);

        iframe.setContent(amedia);

    }

}
