package esira.pauta;

import entidade.EntPauta;
import entidade.NotaPauta;
import esira.dao.ConnectionFactory;
import esira.domain.Curso;
import esira.domain.Disciplina;
import esira.domain.Docente;
import esira.domain.Estudante;
import esira.domain.Funcionario;
import esira.domain.Inscricao;
import esira.domain.Inscricaodisciplina;
import esira.domain.InscricaodisciplinaPK;
import esira.domain.Lecciona;
import esira.domain.Notapauta;
import esira.domain.Observacao;
import esira.domain.Pauta;
import esira.domain.PautaPK;
import esira.domain.PlanificacaoAnoLectivo;
import esira.domain.Planoavaliacao;
import esira.domain.Planodisciplina;
import esira.domain.Statuspauta;
import esira.domain.Users;
import esira.hibernate.MultiTenantConnectionProviderImpl;
import esira.hibernate.TenantIdResolver;
import esira.service.CRUDService;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import javafx.scene.chart.PieChart.Data;
import javafx.scene.layout.HBox;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.hibernate.Transaction;
import org.hibernate.cfg.AnnotationConfiguration;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.zkoss.lang.Strings;
import org.zkoss.web.servlet.dsp.action.Page;
import org.zkoss.zhtml.Text;
import org.zkoss.zhtml.Textarea;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.event.InputEvent;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.Button;
import org.zkoss.zul.Caption;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Constraint;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Div;
import org.zkoss.zul.Doublebox;
import org.zkoss.zul.Doublebox;
import org.zkoss.zul.FieldComparator;
import org.zkoss.zul.Groupbox;
import org.zkoss.zul.Hbox;
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
import org.zkoss.zul.Radiogroup;
import org.zkoss.zul.Row;
import org.zkoss.zul.SimpleConstraint;
import org.zkoss.zul.Tabpanel;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;
import org.zkoss.zul.event.PagingEvent;
import org.zkoss.zul.impl.InputElement;

/**
 *
 * @author UssimaneMuieva
 */
public class PautaController extends GenericForwardComposer {

    @WireVariable
    private final CRUDService csimp = (CRUDService) SpringUtil.getBean("CRUDService");
    private Window windowPauta;
    private Paging pagPauta, paglbStudentsMarks;
    static String ord = " ";
    Map<String, Object> par = new HashMap<String, Object>();
    Map<String, Object> parcond = new HashMap<String, Object>();
    private Listbox lbpauta, lbDataavaliacao, lbStudentsMarks, lbStudentsexam, lbStudentsfreq;
    private Textbox tbdescricao, tbformula, tbobs, tbdescresumo;
    private Combobox cbdisciplina, cbdocente, cbfuncionario, cblecciona, cbSemPrec;
    private Datebox dabdatap;
    private Radio rpnpublicada, rpspublicada, rpnclassificado, rpsclassificado;
    private Radiogroup rpgpublicada, rpgclassificado;
    private Button voltar;
    private Div formpautaexam, pautadocente, formpauta, formpautafreq, formpatuaexam, listapauta, turmas;
    private Hbox botaoPautaExame, botaoPautaFreq;
    private Intbox ibano, ibpercent, ibdisc, ibturno, ibturma, ibordem, ibedicao;

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        Date dano = new Date();
        Calendar cal = new GregorianCalendar();
        cal.setTime(dano);
        int anoi = cal.get(Calendar.YEAR);
        PlanificacaoAnoLectivo pal1 = csimp.findEntByJPQuery("from PlanificacaoAnoLectivo", null);
        if (pal1 != null || pal1.getAnolectivo() == anoi) {
            Date d = new Date();
            Date di = pal1.getDatainicioInscricao2();
            if (d.compareTo(di) >= 0) {
                cbSemPrec.setSelectedItem(cbSemPrec.getItems().get(1));
            }
            if (d.compareTo(di) < 0) {
                cbSemPrec.setSelectedItem(cbSemPrec.getItems().get(0));
            }
        }
        ibano.setValue(cal.get(Calendar.YEAR));
        seleccionaTurmas();
    }

    public void onLancar(ForwardEvent event) throws SQLException {
        Menuitem m = (Menuitem) event.getOrigin().getTarget();
        Pauta p = (Pauta) ((Listitem) m.getParent().getParent().getParent()).getValue();
        if (p.getOrdem() < 0) {
            if (p.getOrdem() == -1) {
                onPautaFreq();
                return;
            } else {
                pautaExam(p.getOrdem().intValue(), p);
                return;
            }
        }
        String sql = " select  id.id_inscricao as id, e.nome_completo as nomeCompleto,e.nr_estudante as nrMecanografico "
                + "                 ,max(nota) as nota "
                + "from fecn1.estudante e,"
                + "(select i.id_inscricao as id_inscricao,i.id_estudante as id_estudante from fecn1.inscricao i where i.semestre = ? and extract(year from i.data_inscricao) = ?) as insc "
                + ",(select id.id_inscricao,id.id_disciplina from fecn1.inscricaodisciplina id where id.id_disciplina =? and id.turma = ? and id.turno =?) as id "
                + ",(select n.nota,n.id_inscricao,n.id_disciplina from fecn1.notapauta n union select '',null,null) as n "
                + "where e.id_estudante = insc.id_estudante and insc.id_inscricao = id.id_inscricao "
                + "and ((n.id_inscricao is not null and (id.id_inscricao = n.id_inscricao and id.id_disciplina=n.id_disciplina)) "
                + "or n.id_inscricao is null) group by id.id_inscricao,e.nome_completo,e.nr_estudante order by e.nome_completo";
        List<Observacao> lo = csimp.getAll(Observacao.class);
        PreparedStatement pstm = new MultiTenantConnectionProviderImpl().getConnection(TenantIdResolver.getTenant()).prepareStatement(sql);
        pstm.setShort(1, p.getPautaPK().getSemestre());
        pstm.setInt(2, p.getPautaPK().getAno());
        pstm.setLong(3, p.getPautaPK().getIddisc());
        pstm.setInt(4, p.getTurma());
        pstm.setInt(5, p.getTurno());
        // pstm.setShort(6,ib.getValue().shortValue());
        List<NotaPauta> lp = new ArrayList<>();
        String stringedicao = "+0";
        for (int i = 1; i < p.getEdicao(); i++) {
            stringedicao = stringedicao + "0";
        }
        ResultSet rs = pstm.executeQuery();
        while (rs.next()) {
            float n = 0;
            String nota = rs.getString("nota");
            if (nota != null) {

            }
            NotaPauta pa = new NotaPauta(rs.getLong("id"),
                    rs.getString("nomeCompleto"),
                    rs.getString("nrMecanografico"),
                    "");
            String[] notas = rs.getString("nota").split(":");
            String v = " ";
            if (notas.length - 1 >= p.getOrdem().intValue()) {//recupera-se o valor do vector se na posicao(ordem) existir algum valor
                v = notas[p.getOrdem().intValue()];
            }
            Float f = (float) -1;
            pa.setEditavel(true);
            if (!v.isEmpty() && !v.equals(" ")) {
                f = Float.parseFloat(v);
                if (f >= 0) {
                    pa.setNota(f + "");
                    pa.setOrdem(0);
                    if (p.getEdicao() > 0 && !v.contains(stringedicao)) {
                        pa.setEditavel(false);
                    }
                } else {
                    pa.setNota("");
                    for (Observacao o : lo) {
                        if (f.equals(o.getCodigo().floatValue())) {
                            pa.setOrdem(o.getIdobservacao());
                        }
                    }
                }
            } else {
                pa.setNota("");
                pa.setOrdem(0);
            }
            lp.add(pa);
        }
        rs.close();
        pstm.close();
        ibordem.setValue(p.getOrdem().intValue());
        ibedicao.setValue(p.getEdicao());
        listapauta.setVisible(false);
        formpauta.setVisible(true);
        lbStudentsMarks.setModel(new ListModelList<>(lp));
    }

    public void onBackPautas() {
        pautadocente.setVisible(false);
        turmas.setVisible(true);
    }

//    public void onChange$cblecciona(Event event) {
//        selCursoPrec();
//    }
    public void onChange$cbSemPrec(Event envent) {
        if (ibano.getValue() == null || cbSemPrec.getSelectedItem() == null) {
            return;
        }
        seleccionaTurmas();
        // cblecciona.setModel(this.getleccionaModel());
        //  selCursoPrec();

    }

    public void onChanging$ano(InputEvent event) {
        if (!event.getValue().equals("")) {
            ibano.setValue(Integer.parseInt(event.getValue()));
            if (ibano.getValue() == null || cbSemPrec.getSelectedItem() == null) {
                return;
            }
//            cblecciona.setModel(this.getleccionaModel());
            seleccionaTurmas();
            //selCursoPrec();
        } else {

        }
    }

    public void selCursoPrec() {
        if (cblecciona.getSelectedItem() == null || ibano.getValue() == null || cbSemPrec.getSelectedItem() == null) {
            return;
        }

        while (!pautadocente.getChildren().isEmpty()) {
            pautadocente.getChildren().get(0).detach();
        }
        Lecciona l = (Lecciona) cblecciona.getSelectedItem().getValue();
        Map<String, Object> par = new HashMap<String, Object>();
        par.put("d", l.getDisciplina());
        par.put("ano", l.getAno());
        par.put("sem", l.getSem().shortValue());
        par.put("turma", l.getLeccionaPK().getTurma());
        par.put("turno", l.getLeccionaPK().getTurno());

        String sql = "from Pauta p where p.disciplina = :d and p.pautaPK.ano = :ano and p.turma = :turma and p.turno = :turno"
                + " and p.pautaPK.semestre = :sem";
        List<Pauta> lpauta = csimp.findByJPQuery(sql, par);

        if (!lpauta.isEmpty()) {

            for (Pauta p : lpauta) {
                Hbox h = new Hbox();
                Button b = new Button();
                Intbox i = new Intbox();
                i.setValue(p.getOrdem().intValue());
                i.setVisible(false);
                Menupopup mp = new Menupopup();
                mp.setId("popup");
                Menuitem mi = new Menuitem();
                mi.addForward("onClick", "self", "onLancar");
                mi.setLabel("Editar");
                mi.setParent(mp);
                b.setPopup("popup, type=toggle, position=after_start");
                b.setImage("/icon/opcao.png");
                b.setStyle("padding: 0;");
                Div d = new Div();
                Div d1 = new Div();
                Div d2 = new Div();
                Div d3 = new Div();
                Label lb = new Label();
                lb.setStyle("font-size:18px");
                lb.setValue(p.getDescricao());
                d1.setStyle("width:50%;display: inline-block;color:blue;font-size:14px");
                //    lb.setHflex("1");
                Label lb1 = new Label();

                d2.setStyle("width:50%;display: inline-block;font-size:14px");
                //    lb1.setHflex("1");
                if (p.getDocente1() == null) {
                    lb1.setStyle("font-size:14px;color:red");
                    lb1.setValue(new java.text.SimpleDateFormat("dd/MM/yyyy").format(p.getPautaPK().getDatap()));
                    lb1.setValue("Não foi relizado");
                } else {
                    lb1.setStyle("font-size:14px;color:blue");
                    lb1.setValue(new java.text.SimpleDateFormat("dd/MM/yyyy").format(p.getPautaPK().getDatap()));
                    lb1.setValue("Realizado");
                    if (p.getDatapublicacao() == null) {
                        lb1.setStyle("font-size:14px;color:blue");
                        lb1.setValue(new java.text.SimpleDateFormat("dd/MM/yyyy").format(p.getPautaPK().getDatap()));
                        lb1.setValue("Realizado | Nao Publicado");
                    }
                }
                lb.setParent(d1);
                lb1.setParent(d2);
                d1.setParent(d);
                d2.setParent(d);
                i.setParent(h);
                d.setParent(h);
                b.setParent(h);
                mp.setParent(h);
                h.setParent(pautadocente);
            }
        }

//            btAdd.setVisible(true);
    }

    public void seleccionaTurmas() {
        formpauta.setVisible(false);
        pautadocente.setVisible(false);
        turmas.setVisible(true);
        Users usr = (Users) Sessions.getCurrent().getAttribute("user");
        Users u = csimp.load(Users.class, usr.getUtilizador());
        int ano = ibano.getValue();
        int sem = Integer.parseInt(cbSemPrec.getSelectedItem().getValue().toString());
        par.clear();
        par.put("docente", u.getIdFuncionario());//u.getIdFuncionario().getDocente()
        par.put("ano", ano);
        par.put("sem", sem);

        List<Lecciona> lecionaList = csimp.findByJPQuery("from Lecciona l where l.docente = :docente and l.sem = :sem and l.ano = :ano ", par);

        while (!turmas.getChildren().isEmpty()) {
            turmas.getChildren().get(0).detach();
        }

        if (!lecionaList.isEmpty()) {
            for (Lecciona l : lecionaList) {
                Groupbox g = new Groupbox();
                Intbox ibdisc = new Intbox();
                ibdisc.setVisible(false);
                ibdisc.setValue(l.getDisciplina().getIdDisc().intValue());
                Intbox ibturma = new Intbox();
                ibturma.setVisible(false);
                ibturma.setValue(l.getLeccionaPK().getTurma());
                Intbox ibturno = new Intbox();
                ibturno.setVisible(false);
                ibturno.setValue(l.getLeccionaPK().getTurno());
                g.getChildren().add(0, ibdisc);
                g.getChildren().add(1, ibturma);
                g.getChildren().add(2, ibturno);
                g.setMold("3d");
                // g.setWidth("200px");
                g.setStyle("overflow:auto;display: inline-block;margin:15px");
                Caption c = new Caption();
                c.addForward("onClick", "self", "onMostrarPautas");
                c.setStyle("font-size:14px;color:blue");
                String nomed = "";
                if (l.getDisciplina().getAbreviatura() != null && !l.getDisciplina().getAbreviatura().isEmpty() && l.getDisciplina().getAbreviatura().length() > 1) {
                    nomed = l.getDisciplina().getAbreviatura();
                } else {
                    nomed = l.getDisciplina().getNome();
                }
                g.setTitle(nomed);
                c.setParent(g);
                Label lc1 = new Label();
                lc1.setValue("Turma " + l.getLeccionaPK().getTurma() + " ");
                lc1.setParent(g);
                lc1 = new Label();
                if (l.getLeccionaPK().getTurno() == 1) {
                    lc1.setValue(" Laboral");
                    lc1.setParent(g);
                } else {
                    lc1.setValue(" Pos Laboral");
                    lc1.setParent(g);
                }
                g.setParent(turmas);
            }
        }
    }

    public void onBack(ForwardEvent event) {

        listapauta.setVisible(true);
        formpauta.setVisible(false);

    }

    public void onBackPautaFreq(ForwardEvent event) {

        listapauta.setVisible(true);
        formpautafreq.setVisible(false);

    }

    public void onBackPautaExam(ForwardEvent event) {

        listapauta.setVisible(true);
        formpautaexam.setVisible(false);

    }

    public ListModel<Pauta> getPautaModel() {
        return new ListModelList<>(csimp.getAll(Pauta.class));
    }

    public ListModel<Disciplina> getdisciplinaModel() {
        return new ListModelList<Disciplina>(csimp.getAll(Disciplina.class));
    }

    public ListModel<Docente> getdocenteModel() {
        return new ListModelList<Docente>(csimp.getAll(Docente.class));
    }

    public ListModel<Funcionario> getfuncionarioModel() {
        return new ListModelList<Funcionario>(csimp.getAll(Funcionario.class));
    }

    private void check(Component component) {
        checkIsValid(component);

        if (component.isVisible() || component instanceof Tabpanel) {
            List<Component> children = component.getChildren();
            for (Component each : children) {
                check(each);
            }
        }
    }

    private void checkIsValid(Component component) {
        if (component instanceof InputElement) {
            if (component instanceof Combobox || component instanceof Doublebox) {
                InputElement ie = ((InputElement) component);
                if (ie.getText().equals("")) {
                    Clients.scrollIntoView(component);
                    ie.setConstraint(" no Empty: Introduza um valor");
                    ((InputElement) component).getText();
                }

            }
        }
    }

    public void onEditar() {
        Clients.showNotification("dfgsdf");
    }

    public void onSubmeter() {

    }

    public void onSavePauta() {
        check(lbStudentsMarks);
        Iterator<Listitem> li = lbStudentsMarks.getItems().iterator();
        Listitem l;
        NotaPauta p = null;//entidade
        Notapauta np = null;//domain
        int ordem = ibordem.getValue();// = pauta.getOrdem();
        int edicao = ibedicao.getValue();
        String stringedicao = "+";
        for (int i = 0; i < edicao; i++) {
            stringedicao = stringedicao + "0";
        }
        Transaction t = csimp.getTransacao();
        try {
            t.begin();
            while (li.hasNext()) {
                l = li.next();
                p = (NotaPauta) l.getValue();
                np = csimp.get(Notapauta.class, new InscricaodisciplinaPK(p.getId(), ibdisc.getValue().longValue()));
                if (np == null) {
                    np = new Notapauta();
                    np.setInscricaodisciplinaPK(new InscricaodisciplinaPK(p.getId(), ibdisc.getValue().longValue()));
                    //  np.setIdestudante(p.);
                }
                if (np.getNota() == null) {
                    np.setNota(" :");
                }
                String[] notas = np.getNota().split(":");
                // if (np.getNota() == null || np.getNota().equals("") || np.getNota().equals(" ")) {
                int i = notas.length - 1;
                String inic = "";
                for (; i < ordem; i++) {
                    inic = inic + " :";
                }
                np.setNota(np.getNota() + inic);
                notas = np.getNota().split(":");
                if (edicao == 0) {
                    Combobox c = ((Combobox) l.getChildren().get(3).getChildren().get(0).getChildren().get(0));
                    if (c.getSelectedIndex() < 1) {
                        notas[ordem] = Float.parseFloat(c.getValue()) + "";
                    } else {
                        Observacao o = (Observacao) c.getSelectedItem().getValue();
                        notas[ordem] = o.getCodigo() + "";
                    }//////////////////////////////////////
                    String n = "";
                    for (int j = 0; j < notas.length; j++) {
                        n = n + notas[j] + ":";
                    }
                    np.setNota(n);
                } else {
                    if (p.isEditavel()) {
                        Combobox c = ((Combobox) l.getChildren().get(3).getChildren().get(0).getChildren().get(0));
                        if (c.getSelectedIndex() < 1) {
                            notas[ordem] = stringedicao + Float.parseFloat(c.getValue());
                        } else {
                            Observacao o = (Observacao) c.getSelectedItem().getValue();
                            notas[ordem] = o.getCodigo() + "";
                        }//////////////////////////////////////
                        String n = "";
                        for (int j = 0; j < notas.length; j++) {
                            n = n + notas[j] + ":";
                        }
                        np.setNota(n);
                    }
                }

                csimp.updates(np);
            }
            t.commit();
            while (!t.wasCommitted())
            ;
            Clients.showNotification("Actualizado com sucesso.", null, null, null, 2000);
        } catch (NumberFormatException | WrongValueException e) {
            Clients.showNotification("Ocorreu um erro", Clients.NOTIFICATION_TYPE_ERROR, null, null, 3000);
            t.rollback();
        } finally {

        }
    }

    public void onMostrarPautas(ForwardEvent event) {
        turmas.setVisible(false);
        pautadocente.setVisible(true);
        Groupbox g = (Groupbox) event.getOrigin().getTarget().getParent();
        Long disc = ((Intbox) g.getChildren().get(1)).getValue().longValue();
        int turma = ((Intbox) g.getChildren().get(2)).getValue();
        int turno = ((Intbox) g.getChildren().get(3)).getValue();
        ibdisc.setValue(disc.intValue());
        ibturma.setValue(turma);
        ibturno.setValue(turno);
        short s = Short.parseShort(cbSemPrec.getSelectedItem().getValue().toString());
        Map<String, Object> par = new HashMap<String, Object>();
        par.put("d", disc);
        par.put("ano", ibano.getValue());
        par.put("sem", s);
        par.put("turma", turma);
        par.put("turno", turno);

        String sql = "from Pauta p where p.disciplina.idDisc = :d and p.pautaPK.ano = :ano and p.turma = :turma and p.turno = :turno"
                + " and p.pautaPK.semestre = :sem order by p.ordem";
        List<Pauta> lpauta = csimp.findByJPQuery(sql, par);
        lbpauta.setModel(new ListModelList<>(lpauta));
    }

    public ListModel<Observacao> getObservacaoModel() {
        List<Observacao> lo1 = new ArrayList<Observacao>();
        Observacao o = new Observacao();
        o.setAbreviatura("");
        o.setDescricao("Inserir a Nota");
        lo1.add(o);
        List<Observacao> lo = csimp.getAll(Observacao.class);
        lo1.addAll(lo);
        return new ListModelList<Observacao>(lo1);
    }

    public ListModel<Statuspauta> getStatusModel() {
        List<Statuspauta> lo = csimp.getAll(Statuspauta.class);
        return new ListModelList<>(lo);
    }

    ///////////pauta freq
    public void onPautaFreq() throws SQLException {
        String sql = " select  id.id_inscricao as id, e.nome_completo as nomeCompleto,e.nr_estudante as nrMecanografico, "
                + "                 mediafreq as mediafreq,classfreq as classfreq,nota as nota "
                + "from fecn1.estudante e,"
                + "(select i.id_inscricao as id_inscricao,i.id_estudante as id_estudante from fecn1.inscricao i where i.semestre = ? and extract(year from i.data_inscricao) = ?) as insc "
                + ",(select id.id_inscricao,id.id_disciplina from fecn1.inscricaodisciplina id where id.id_disciplina =? and id.turma = ? and id.turno =?) as id "
                + ",(select n.nota,n.mediafreq,n.classfreq,n.id_inscricao,n.id_disciplina from fecn1.notapauta n) as n "
                + "where e.id_estudante = insc.id_estudante and insc.id_inscricao = id.id_inscricao "
                + "and id.id_inscricao = n.id_inscricao and id.id_disciplina=n.id_disciplina order by e.nome_completo";
        List<Observacao> lo = csimp.getAll(Observacao.class);
        //Lecciona l = (Lecciona) cblecciona.getSelectedItem().getValue();
        PreparedStatement pstm = new MultiTenantConnectionProviderImpl().getConnection(TenantIdResolver.getTenant()).prepareStatement(sql);
        pstm.setShort(1, Short.parseShort(cbSemPrec.getSelectedItem().getValue().toString()));
        pstm.setInt(2, ibano.getValue());
        pstm.setLong(3, ibdisc.getValue().longValue());
        pstm.setInt(4, ibturma.getValue());
        pstm.setInt(5, ibturno.getValue());
        List<NotaPauta> lp = new ArrayList<>();
        List<List<Integer>> lv = new ArrayList<>();
        List<Integer> ls;// = new ArrayList<>();
        List<List<String>> lvn = new ArrayList<>();
        List<String> lt = null;
        final Iterator<Listitem> items = lbpauta.getItems().listIterator();
        Pauta e = null;
        boolean tt = true;
        while (items.hasNext()) {
            e = (Pauta) items.next().getValue();
            if (e.getOrdem() >= 0) {
                int t = e.getTipo();
                for (int i = 0; i < lv.size(); i++) {
                    if (lv.get(i).get(0) == t) {
                        tt = false;
                        lv.get(i).add(e.getOrdem().intValue());
                        lvn.get(i).add(e.getDescricao());
                    }
                }
                if (tt) {
                    ls = new ArrayList<>();
                    ls.add(t);
                    ls.add(e.getPercent());
                    ls.add(e.getOrdem().intValue());
                    lv.add(ls);
                    lt = new ArrayList<>();
                    lt.add(e.getDescricao());
                    lvn.add(lt);
                }
            }
            tt = true;
        }
        ResultSet rs = pstm.executeQuery();
        while (rs.next()) {
            float m = 0;
            String snotas = "";
            NotaPauta pa = new NotaPauta(rs.getLong("id"),
                    rs.getString("nomeCompleto"),
                    rs.getString("nrMecanografico"),
                    "");
            String nota = rs.getString("nota");
            pa.setNota1(rs.getFloat("mediafreq"));
            pa.setClassfreq(rs.getString("classfreq"));
            if (nota != null) {
                String[] notas = nota.split(":");

                for (int i = 0; i < lv.size(); i++) {
                    List<Integer> tls = lv.get(i);
                    float f = -1;
                    float t = 0;
                    float sub = 0;
                    float cont = 0;
                    for (int j = 2; j < tls.size(); j++) {
                        snotas = snotas + lvn.get(i).get(j - 2) + ":";
                        //Messagebox.show(notas.length+" "+tls.size()+" "+j);
                        String v = notas[tls.get(j)];
                        cont = cont + 1;
                        if (!v.isEmpty() && !v.equals(" ")) {
                            f = Float.parseFloat(v);
                            if (f >= 0) {
                                t = t + f;
                                snotas = snotas + f + " ";
                            } else {
                                for (Observacao o : lo) {
                                    if (f == o.getCodigo().floatValue()) {
                                        snotas = snotas + o.getAbreviatura();
                                    }
                                }
                            }
                        }
                    }
                    if (cont > 0) {
                        sub = t / cont;
                    }
                    sub = sub * (tls.get(1) / 100);//percentagem
                    m = m + sub;

                }
                // if (pa.getNota1() == null) {
                float med = new Double(Math.ceil(m)).floatValue();
                if (med == 0) {
                    pa.setEditavel(false);
                }
                if (med == 0 && pa.getNota1() > 0) {

                } else {
                    pa.setNota1(med);//Messagebox.show(m+"");
                }
                // }
                pa.setNota(snotas);
            } else {
                pa.setNota(snotas);
                if (pa.getNota1() == null) {
                    pa.setNota1((float) 0);
                    pa.setEditavel(true);
                }
            }

            lp.add(pa);
        }
        rs.close();
        pstm.close();
        ibordem.setValue(-1);
        ibedicao.setValue(0);
        listapauta.setVisible(false);
        formpautafreq.setVisible(true);
        lbStudentsfreq.setModel(new ListModelList<>(lp));
    }

    public void onSavePautaFreq() {
        check(lbStudentsfreq);
        Map<String, Object> par = new HashMap<String, Object>();
        par.put("d", ibdisc.getValue().longValue());
        par.put("ano", ibano.getValue());
        par.put("sem", Short.parseShort(cbSemPrec.getSelectedItem().getValue().toString()));
        par.put("turma", ibturma.getValue());
        par.put("turno", ibturno.getValue());
        String sql = "from Pauta p where p.pautaPK.iddisc = :d and p.pautaPK.ano = :ano and p.turma = :turma and p.turno = :turno"
                + " and p.pautaPK.semestre = :sem and p.ordem = -1";
        Pauta pauta = csimp.findEntByJPQuery(sql, par);
        Iterator<Listitem> li = lbStudentsfreq.getItems().iterator();
        Listitem l;
        NotaPauta p = null;//entidade
        Notapauta np = null;//domain
        int ordem = ibordem.getValue();// = pauta.getOrdem();
//        int edicao = ibedicao.getValue();
        Transaction t = csimp.getTransacao();
        try {
            t.begin();
            if (pauta == null) {
                pauta = new Pauta(ibdisc.getValue().longValue(), ibano.getValue(), Short.parseShort(cbSemPrec.getSelectedItem().getValue().toString()), new Date());
                Users usr = (Users) Sessions.getCurrent().getAttribute("user");
                Users u = csimp.load(Users.class, usr.getUtilizador());
                pauta.setDocente(u.getIdFuncionario().getDocente());
                pauta.setDescricao("Pauta de Frequência");
                pauta.setOrdem(Short.parseShort("-1"));
                pauta.setEdicao(0);
                pauta.setTurno(ibturno.getValue());
                pauta.setTurma(ibturma.getValue());
                csimp.Saves(pauta);
            }
            while (li.hasNext()) {
                l = li.next();
                p = (NotaPauta) l.getValue();
                //se for validada nao pode ser actualizado
                Doublebox d = ((Doublebox) l.getChildren().get(3).getChildren().get(1));
                Combobox c = ((Combobox) l.getChildren().get(4).getChildren().get(0));
                np = csimp.get(Notapauta.class, new InscricaodisciplinaPK(p.getId(), ibdisc.getValue().longValue()));
                if (np == null) {
                    np = new Notapauta();
                    np.setInscricaodisciplinaPK(new InscricaodisciplinaPK(p.getId(), ibdisc.getValue().longValue()));
                }
                np.setClassfreq(c.getValue());
                np.setMediafreq(d.getValue().floatValue());
                csimp.updates(np);

            }
            t.commit();
            while (!t.wasCommitted())
            ;
            Clients.showNotification("Actualizado com sucesso.", null, null, null, 2000);
        } catch (NumberFormatException | WrongValueException e) {
            Clients.showNotification("Ocorreu um erro", Clients.NOTIFICATION_TYPE_ERROR, null, null, 3000);
            t.rollback();
        } finally {

        }
    }

    public void onNotaFreq(InputEvent event) {
        botaoPautaFreq.setVisible(false);
        Doublebox m = (Doublebox) event.getTarget();
        Listcell lc = (Listcell) m.getParent().getNextSibling();
        Combobox cb = (Combobox) lc.getFirstChild();
        limparCB(cb);
        if (event.getValue() != null && !event.getValue().isEmpty()) {
            int i = Integer.parseInt(event.getValue());
            if (i >= 10) {
                Comboitem c = new Comboitem("Admitido");
                c.setParent(cb);
                c = new Comboitem("Aprovado");
                c.setParent(cb);
                cb.setValue("Admitido");
                if (i >= 16) {
                    c = new Comboitem("Dispensado");
                    c.setParent(cb);
                    cb.setValue("Dispensado");
                }
            } else {
                Comboitem c = new Comboitem("Excluido ");
                c.setParent(cb);
                c = new Comboitem("Reprovado");
                c.setParent(cb);
                cb.setValue("Excluido");
            }
        }
        botaoPautaFreq.setVisible(true);
        // Messagebox.show(lc.getUuid() + "");
    }

    public void onSelClassif(ForwardEvent event) {
//        Doublebox m = (Doublebox) event.getTarget();
//        Listcell lc = (Listcell) m.getParent().getNextSibling();
//        Combobox cb = (Combobox) lc.getFirstChild().getFirstChild();
//        limparCB(cb);
//        if (event.getValue() != null && !event.getValue().isEmpty()) {
//            int i = Integer.parseInt(event.getValue());
//            if (i >= 10) {
//                Comboitem c = new Comboitem("Admitido");
//                c.setParent(cb);
//                c = new Comboitem("Aprovado");
//                c.setParent(cb);
//                cb.setValue("Admitido");
//                if (i >= 16) {
//                    c = new Comboitem("Dispensado");
//                    c.setParent(cb);
//                    cb.setValue("Dispensado");
//                }
//            } else {
//                Comboitem c = new Comboitem("Excluido ");
//                c.setParent(cb);
//                c = new Comboitem("Reprovado");
//                c.setParent(cb);
//                cb.setValue("Excluido");
//            }
//        }
    }

    public void limparCB(Combobox lb) {
        if (lb.getItemCount() > 0) {
            final Iterator<Comboitem> items = new ArrayList(lb.getItems()).listIterator();
            Comboitem li;
            Combobox l = new Combobox();
            while (items.hasNext()) {
                li = items.next();
                l.appendChild(li);
            }
        }
    }

    ///////////pauta exam
    public void onPautaExam() throws SQLException {
        Iterator<Listitem> li = lbpauta.getItems().listIterator();
        Pauta pa = null;
        while (li.hasNext()) {
            if (((Pauta) li.next().getValue()).getOrdem().intValue() == -2) {
                pa = (Pauta) li.next().getValue();
            }
        }
        pautaExam(-2, pa);
    }

    public void onPautaRec() throws SQLException {
        Iterator<Listitem> li = lbpauta.getItems().listIterator();
        Pauta pa = null;
        while (li.hasNext()) {
            if (((Pauta) li.next().getValue()).getOrdem().intValue() == -3) {
                pa = (Pauta) li.next().getValue();
            }
        }
        pautaExam(-3, pa);
    }

    public void onPautaExtra() throws SQLException {
        Iterator<Listitem> li = lbpauta.getItems().listIterator();
        Pauta pa = null;
        while (li.hasNext()) {
            if (((Pauta) li.next().getValue()).getOrdem().intValue() == -4) {
                pa = (Pauta) li.next().getValue();
            }
        }
        pautaExam(-4, pa);
    }

    //para nota de axame <0 colocar label
    public void pautaExam(int ordem, Pauta p) throws SQLException {
        String sql = " select  id.id_inscricao as id, e.nome_completo as nomeCompleto,e.nr_estudante as nrMecanografico, "
                + "mediafreq as mediafreq,classfreq as classfreq,notaexame as notaexame"
                + ",notarec as notarec,notaextra as notaextra,notafinal as notafinal,classexame as classexame"
                + ",classreq as classreq,classextra as classextra "
                + "from fecn1.estudante e,"
                + "(select i.id_inscricao as id_inscricao,i.id_estudante as id_estudante from fecn1.inscricao i where i.semestre = ? and extract(year from i.data_inscricao) = ?) as insc "
                + ",(select id.id_inscricao,id.id_disciplina from fecn1.inscricaodisciplina id where id.id_disciplina =? and id.turma = ? and id.turno =?) as id "
                + ",(select n.notaexame,n.notarec,n.notaextra,n.mediafreq,n.notafinal,n.classfreq,n.classexame,n.classreq,n.classextra,n.id_inscricao,n.id_disciplina from fecn1.notapauta n) as n "
                + "where e.id_estudante = insc.id_estudante and insc.id_inscricao = id.id_inscricao "
                + "and id.id_inscricao = n.id_inscricao and id.id_disciplina=n.id_disciplina order by e.nome_completo";
        List<Observacao> lo = csimp.getAll(Observacao.class);
        //Lecciona l = (Lecciona) cblecciona.getSelectedItem().getValue();
        PreparedStatement pstm = new MultiTenantConnectionProviderImpl().getConnection(TenantIdResolver.getTenant()).prepareStatement(sql);
        pstm.setShort(1, Short.parseShort(cbSemPrec.getSelectedItem().getValue().toString()));
        pstm.setInt(2, ibano.getValue());
        pstm.setLong(3, ibdisc.getValue().longValue());
        pstm.setInt(4, ibturma.getValue());
        pstm.setInt(5, ibturno.getValue());
        List<NotaPauta> lp = new ArrayList<>();
        List<List<Integer>> lv = new ArrayList<>();
        List<Integer> ls;// = new ArrayList<>();
        List<List<String>> lvn = new ArrayList<>();
        List<String> lt = null;
        final Iterator<Listitem> items = lbpauta.getItems().listIterator();
        Pauta e = null;
        boolean tt = true;

        ResultSet rs = pstm.executeQuery();
        while (rs.next()) {
            NotaPauta pa = new NotaPauta(rs.getLong("id"),
                    rs.getString("nomeCompleto"),
                    rs.getString("nrMecanografico"),
                    "");
            pa.setNota1(rs.getFloat("mediafreq"));
            pa.setNota2(rs.getFloat("notaexame"));

            pa.setOrdem(ordem);
            pa.setClassfreq(rs.getString("classfreq"));
            pa.setClassexame(rs.getString("classexame"));
            pa.setClassreq(rs.getString("classreq"));
            pa.setClassextra(rs.getString("classextra"));
            Float notaextra=rs.getFloat("notaextra");
            if (ordem == -2 && pa.getClassfreq() != null && !pa.getClassfreq().isEmpty()
                    && pa.getClassfreq().equalsIgnoreCase("Admitido")&&(notaextra==null||notaextra<10)) {
                pa.setNota("MedFreq:" + pa.getNota1() + " " + "Admitido");
                if (pa.getClassexame() != null && !pa.getClassexame().isEmpty()) {
                    pa.setNota2(rs.getFloat("notaexame"));
                } else {
                    pa.setNota2(null);
                }
                if (pa.getNota2() != null) {
                    pa.setNota3(rs.getFloat("notafinal"));
                }
                lp.add(pa);
                if (p != null && p.getEstado() != null && p.getEstado() > 0) {
                    pa.setEditavel(false);
                }
            } else if (ordem == -3 && pa.getClassexame() != null && !pa.getClassexame().isEmpty()
                    && pa.getClassexame().equalsIgnoreCase("Recorrência")&&(notaextra==null||notaextra<10)) {
                pa.setNota("MedFreq:" + pa.getNota1() + " " + "Admitido" + " NotaExam:" + pa.getNota2());
                if (pa.getClassreq() != null && !pa.getClassreq().isEmpty()) {
                    pa.setNota2(rs.getFloat("notarec"));
                } else {
                    pa.setNota2(null);
                }
                if (pa.getNota2() != null) {
                    pa.setNota3(rs.getFloat("notafinal"));
                }
                lp.add(pa);
                if (p != null && p.getEstado() != null && p.getEstado() > 0) {
                    pa.setEditavel(false);
                }
            } else if (ordem == -4) {
                Float notafinal = rs.getFloat("notafinal");
                if (notafinal != null && notafinal < 10) {
                    pa.setNota2(null);
                    if (pa.getClassextra() != null && !pa.getClassextra().isEmpty()) {
                        pa.setNota2(rs.getFloat("notaextra"));
                    } else {
                        pa.setNota2(null);
                    }
                    if (pa.getNota2() != null) {
                        pa.setNota3(rs.getFloat("notafinal"));
                    }
                    lp.add(pa);
                    if (p != null && p.getEstado() != null && p.getEstado() > 0) {
                        pa.setEditavel(false);
                    }
                }
            }
        }
        rs.close();
        pstm.close();
        ibordem.setValue(ordem);
        ibedicao.setValue(0);
        listapauta.setVisible(false);
        formpautaexam.setVisible(true);
        lbStudentsexam.setModel(new ListModelList<>(lp));
    }

    //pauta com estado validado nao deve ser salva
    public void onSavePautaExam() {
        check(lbStudentsexam);
        Map<String, Object> par = new HashMap<>();
        par.put("d", ibdisc.getValue().longValue());
        par.put("ano", ibano.getValue());
        par.put("sem", Short.parseShort(cbSemPrec.getSelectedItem().getValue().toString()));
        par.put("turma", ibturma.getValue());
        par.put("turno", ibturno.getValue());
        par.put("ordem", ibordem.getValue().shortValue());
        String sql = "from Pauta p where p.pautaPK.iddisc = :d and p.pautaPK.ano = :ano and p.turma = :turma and p.turno = :turno"
                + " and p.pautaPK.semestre = :sem and p.ordem = :ordem";
        Pauta pauta = csimp.findEntByJPQuery(sql, par);
        Iterator<Listitem> li = lbStudentsexam.getItems().iterator();
        Listitem l;
        NotaPauta p = null;//entidade
        Notapauta np = null;//domain
        int ordem = ibordem.getValue();
        Transaction t = csimp.getTransacao();
        try {
            t.begin();
            if (pauta == null) {
                pauta = new Pauta(ibdisc.getValue().longValue(), ibano.getValue(), Short.parseShort(cbSemPrec.getSelectedItem().getValue().toString()), new Date());
                Users usr = (Users) Sessions.getCurrent().getAttribute("user");
                Users u = csimp.load(Users.class, usr.getUtilizador());
                pauta.setDocente(u.getIdFuncionario().getDocente());

                if (ordem == -2) {
                    pauta.setDescricao("Pauta de Exame Normal");
                }
                if (ordem == -3) {
                    pauta.setDescricao("Pauta de Exame de Rec");
                }
                if (ordem == -4) {
                    pauta.setDescricao("Pauta de Exame Extraordinario");
                }
                pauta.setOrdem(ibordem.getValue().shortValue());
                pauta.setEdicao(0);
                pauta.setTurno(ibturno.getValue());
                pauta.setTurma(ibturma.getValue());
                csimp.Saves(pauta);
            }
            while (li.hasNext()) {
                l = li.next();
                p = (NotaPauta) l.getValue();
                Combobox c = ((Combobox) l.getChildren().get(4).getChildren().get(1));
                Label lbclass = ((Label) l.getChildren().get(5).getChildren().get(0));
                Label lbnotaf = ((Label) l.getChildren().get(6).getChildren().get(0));
                float nexame;
                if (c.getSelectedIndex() < 1) {
                    nexame = Float.parseFloat(c.getValue());
                } else {
                    Observacao o = (Observacao) c.getSelectedItem().getValue();
                    nexame = o.getCodigo().floatValue();
                }
                np = csimp.get(Notapauta.class, new InscricaodisciplinaPK(p.getId(), ibdisc.getValue().longValue()));
                if (np == null) {
                    np = new Notapauta();
                    np.setInscricaodisciplinaPK(new InscricaodisciplinaPK(p.getId(), ibdisc.getValue().longValue()));
                }
                switch (ordem) {
                    case -2:
                        np.setNotaexame(nexame);
                        if (!lbnotaf.getValue().isEmpty()) {
                            np.setNotafinal(Float.parseFloat(lbnotaf.getValue()));
                        }
                        np.setClassexame(lbclass.getValue());
                        break;
                    case -3:
                        np.setNotarec(nexame);
                        if (!lbnotaf.getValue().isEmpty()) {
                            np.setNotafinal(Float.parseFloat(lbnotaf.getValue()));
                        }
                        np.setClassreq(lbclass.getValue());
                        break;
                    case -4:
                        Doublebox mediafreq = ((Doublebox) l.getChildren().get(3).getChildren().get(1));
                        np.setMediafreq(mediafreq.getValue().floatValue());
                        np.setNotaextra(mediafreq.getValue().floatValue());
                        if (!lbnotaf.getValue().isEmpty()) {
                            np.setNotafinal(Float.parseFloat(lbnotaf.getValue()));
                        }
                        np.setClassextra(lbclass.getValue());
                        break;
                    default:
                        break;
                }
                csimp.updates(np);
//                }
            }
            t.commit();
            while (!t.wasCommitted())
            ;
            Clients.showNotification("Actualizado com sucesso.", null, null, null, 2000);
        } catch (NumberFormatException | WrongValueException e) {
            Clients.showNotification("Ocorreu um erro", Clients.NOTIFICATION_TYPE_ERROR, null, null, 3000);
            t.rollback();
        } finally {

        }
    }

    //
    public void onNotaExam(InputEvent event) {
        botaoPautaExame.setVisible(false);
        int ordem = ibordem.getValue();
        Combobox cb = (Combobox) event.getTarget();
        Listitem li = (Listitem) cb.getParent().getParent();
        NotaPauta np = (NotaPauta) li.getValue();
        float nexame = 0, nfinal = 0;
        if (event.getValue() != null && !event.getValue().isEmpty() && cb.getSelectedIndex() < 1) {
            nexame = Float.parseFloat(event.getValue());
            if (nexame >= 10) {
                switch (ordem) {
                    case -2:
                        nfinal = new Double(Math.ceil((np.getNota1() * 0.50)
                                + (nexame * 0.50))).floatValue();
                        break;
                    case -3:
                        nfinal = new Double(Math.ceil((np.getNota1() * 0.75)
                                + (nexame * 0.25))).floatValue();
                        break;
                    case -4:
                        Doublebox mediafreq = ((Doublebox) li.getChildren().get(3).getChildren().get(1));
                        if (mediafreq.getValue() != null) {
                            nfinal = new Double(Math.ceil((mediafreq.getValue() * 0.50)
                                    + (nexame * 0.50))).floatValue();
                        }
                        break;
                    default:
                        break;
                }
            }

            if (nexame >= 10) {
                ((Label) li.getLastChild().getFirstChild()).setValue(nfinal + "");
                ((Label) li.getLastChild().getLastChild()).setValue("Valores");
                ((Label) li.getLastChild().getPreviousSibling().getFirstChild()).setValue("Aprovado");
                ((Label) li.getLastChild().getPreviousSibling().getFirstChild()).setStyle("color:blue;font-size:14px");
            } else {
                if (ordem == -2) {
                    ((Label) li.getLastChild().getPreviousSibling().getFirstChild()).setValue("Recorrência");
                    ((Label) li.getLastChild().getPreviousSibling().getFirstChild()).setStyle("font-weight:italic;color:red;font-size:14px");
                } else {
                    ((Label) li.getLastChild().getPreviousSibling().getFirstChild()).setValue("Reprovado");
                    ((Label) li.getLastChild().getPreviousSibling().getFirstChild()).setStyle("color:red;font-size:14px");
                }
                ((Label) li.getLastChild().getFirstChild()).setValue("");
                ((Label) li.getLastChild().getLastChild()).setValue("");
            }
        }
        botaoPautaExame.setVisible(true);
    }

    public void onMedFreqExtra(InputEvent event) {
        botaoPautaExame.setVisible(false);
        int ordem = ibordem.getValue();
        Doublebox db = (Doublebox) event.getTarget();
        Listitem li = (Listitem) db.getParent().getParent();
        Combobox cb = ((Combobox) li.getChildren().get(4).getChildren().get(1));
        NotaPauta np = (NotaPauta) li.getValue();
        float medfreq, nexame = 0, nfinal = 0;
        if (event.getValue() != null && !event.getValue().isEmpty()) {
            if (cb.getSelectedIndex() < 1) {
                if (!cb.getValue().isEmpty()) {
                    nexame = Float.parseFloat(cb.getValue());
                    medfreq = Float.parseFloat(event.getValue());
                    if (nexame >= 10) {
                        nfinal = new Double(Math.ceil((medfreq * 0.50)
                                + (nexame * 0.50))).floatValue();
                    }

                    if (nexame >= 10) {
                        ((Label) li.getLastChild().getFirstChild()).setValue(nfinal + "");
                        ((Label) li.getLastChild().getLastChild()).setValue("Valores");
                        ((Label) li.getLastChild().getPreviousSibling().getFirstChild()).setValue("Aprovado");
                        ((Label) li.getLastChild().getPreviousSibling().getFirstChild()).setStyle("color:blue;font-size:14px");
                    } else {
                        if (ordem == -2) {
                            ((Label) li.getLastChild().getPreviousSibling().getFirstChild()).setValue("Recorrência");
                            ((Label) li.getLastChild().getPreviousSibling().getFirstChild()).setStyle("font-weight:italic;color:red;font-size:14px");
                        } else {
                            ((Label) li.getLastChild().getPreviousSibling().getFirstChild()).setValue("Reprovado");
                            ((Label) li.getLastChild().getPreviousSibling().getFirstChild()).setStyle("color:red;font-size:14px");
                        }
                        ((Label) li.getLastChild().getFirstChild()).setValue("");
                        ((Label) li.getLastChild().getLastChild()).setValue("");
                    }
                }
            } else {

            }
        }
        botaoPautaExame.setVisible(true);
    }

    public void onSelNotaExam(ForwardEvent event) {
        botaoPautaExame.setVisible(false);
        int ordem = ibordem.getValue();
        Combobox cb = (Combobox) event.getOrigin().getTarget();
        Listitem li = (Listitem) cb.getParent().getParent();
        float nexame = 0;
        if (cb.getSelectedItem() != null && cb.getSelectedIndex() > 0) {
            Observacao o = (Observacao) cb.getSelectedItem().getValue();
            nexame = o.getCodigo().floatValue();

            if (nexame == -1 && ordem == -2) {
                ((Label) li.getLastChild().getPreviousSibling().getFirstChild()).setValue("Recorrência");
                ((Label) li.getLastChild().getPreviousSibling().getFirstChild()).setStyle("font-weight:italic;color:red;font-size:14px");
            } else {
                ((Label) li.getLastChild().getPreviousSibling().getFirstChild()).setValue("Reprovado");
                ((Label) li.getLastChild().getPreviousSibling().getFirstChild()).setStyle("color:red;font-size:14px");
            }
            ((Label) li.getLastChild().getFirstChild()).setValue("");
            ((Label) li.getLastChild().getLastChild()).setValue("");
        }
        botaoPautaExame.setVisible(true);
    }

}
