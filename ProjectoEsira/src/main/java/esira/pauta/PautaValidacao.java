package esira.pauta;

import entidade.EntPauta;
import entidade.NotaPauta;
import esira.dao.ConnectionFactory;
import esira.domain.Curso;
import esira.domain.Disciplina;
import esira.domain.Docente;
import esira.domain.Estudante;
import esira.domain.Faculdade;
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
import org.zkoss.zul.Doublespinner;
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
public class PautaValidacao extends GenericForwardComposer {

    @WireVariable
    private final CRUDService csimp = (CRUDService) SpringUtil.getBean("CRUDService");
    private Window windowPauta;
    private Paging pagPauta, paglbStudentsMarks;
    static String ord = " ";
    Users usr = (Users) Sessions.getCurrent().getAttribute("user");
    Map<String, Object> par = new HashMap<String, Object>();
    Map<String, Object> parcond = new HashMap<String, Object>();
    private Listbox lbpauta, lbDataavaliacao, lbStudentsMarks, lbStudentsexam, lbStudentsfreq;
    private Textbox tbdescricao, tbformula, tbobs, tbdescresumo;
    private Combobox cbcurso,cbdisciplina, cbdocente, cbfuncionario, cblecciona, cbSemPrec;
    private Datebox dabdatap;
    private Radio rpnpublicada, rpspublicada, rpnclassificado, rpsclassificado;
    private Radiogroup rpgpublicada, rpgclassificado;
    private Button voltar;
    private Div pautadocente, formpauta, formpautafreq, formpatuaexam, listapauta, turmas;
    private Hbox formpautatn;
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
        selCursoPrec();
    }

    public void onChange$cbSemPrec(Event envent) {
        if (ibano.getValue() == null || cbSemPrec.getSelectedItem() == null) {
            return;
        }
          selCursoPrec();
    }

    public void onChanging$ano(InputEvent event) {
        if (!event.getValue().equals("")) {
            ibano.setValue(Integer.parseInt(event.getValue()));
            if (ibano.getValue() == null || cbSemPrec.getSelectedItem() == null) {
                return;
            }
            selCursoPrec();
        } else {

        }
    }

    public void selCursoPrec() {
        par.put("ano", ibano.getValue());
        par.put("sem",Short.parseShort(cbSemPrec.getSelectedItem().getValue().toString()));
        Curso cu=(Curso) cbcurso.getSelectedItem().getValue();//.getModel().getElementAt(0);
        par.put("cu", cu);
        String sql = "from Pauta p where p.pautaPK.ano = :ano"
                + " and p.pautaPK.semestre = :sem and p.disciplina.curso=:cu";
        List<Pauta> lpauta = csimp.findByJPQuery(sql, par);
lbpauta.setModel(new ListModelList<>(lpauta));
    }


    public void onBack(ForwardEvent event) {

        listapauta.setVisible(true);
        formpauta.setVisible(false);

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
            if (component instanceof Combobox) {
                InputElement ie = ((InputElement) component);
                if (ie.getText().equals("")) {
                    Clients.scrollIntoView(component);
                    ie.setConstraint(" no Empty: Introduza um valor");
                    ((InputElement) component).getText();
                }

            }
//            if (((component instanceof Combobox) && ((Combobox) component).getSelectedItem() == null)) {
//                Clients.scrollIntoView(component);
//                ((Combobox) component).setText("");
//                ((Combobox) component).getValue();
//            }
        }
    }

    public void onSubmeter() {

    }

    public void onMostrarPautas(ForwardEvent event) {
        turmas.setVisible(false);
        pautadocente.setVisible(true);

//        while (!pautadocente.getChildren().isEmpty()) {
//            pautadocente.getChildren().get(0).detach();
//        }
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
                + " and p.pautaPK.semestre = :sem";
        List<Pauta> lpauta = csimp.findByJPQuery(sql, par);
        lbpauta.setModel(new ListModelList<>(lpauta));
//        lbpauta.setRows(lbpauta.getItemCount() + 1);
//        ListModel lm = lbpauta.getModel();
//        if (!lpauta.isEmpty()) {
//            
//            for (Pauta p : lpauta) {
//                EntPauta ep = new EntPauta();
//                ep.setDatap(p.getPautaPK().getDatap());
//                ep.setDescricao(p.getDescricao());
//                ep.setPauta(p);
////                if (p.getDocente1() == null) {
//////                    lb1.setStyle("font-size:14px;color:red");
//////                    lb1.setValue(new java.text.SimpleDateFormat("dd/MM/yyyy").format(p.getPautaPK().getDatap()));
////                    ep.setNrealizado("Não relizado");
////                } else {
//////                    lb1.setStyle("font-size:14px;color:blue");
//////                    lb1.setValue(new java.text.SimpleDateFormat("dd/MM/yyyy").format(p.getPautaPK().getDatap()));
////                    ep.setRealizacao("Realizado");
////                    if (p.getDatapublicacao() == null) {
//////                        lb1.setStyle("font-size:14px;color:blue");
//////                        lb1.setValue(new java.text.SimpleDateFormat("dd/MM/yyyy").format(p.getPautaPK().getDatap()));
////                        ep.setPublicacao("Realizado | Nao Publicado");
////                    }
////                }
//                ((ListModelList) lm).add(p);
//                lbpauta.setRows(lbpauta.getItemCount() + 1);
////                lb.setParent(d1);
////                lb1.setParent(d2);
////                d1.setParent(d);
////                d2.setParent(d);
////                i.setParent(h);
////                d.setParent(h);
////                b.setParent(h);
////                mp.setParent(h);
////                h.setParent(pautadocente);
//            }
//        }
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
                + "                 mediafreq as mediafreq,classfreq as classfreq,max(nota) as nota "
                + "from fecn1.estudante e,"
                + "(select i.id_inscricao as id_inscricao,i.id_estudante as id_estudante from fecn1.inscricao i where i.semestre = ? and extract(year from i.data_inscricao) = ?) as insc "
                + ",(select id.id_inscricao,id.id_disciplina from fecn1.inscricaodisciplina id where id.id_disciplina =? and id.turma = ? and id.turno =?) as id "
                + ",(select n.nota,n.mediafreq,n.classfreq,n.id_inscricao,n.id_disciplina from fecn1.notapauta n union select '',null,null,null,null) as n "
                + "where e.id_estudante = insc.id_estudante and insc.id_inscricao = id.id_inscricao "
                + "and ((n.id_inscricao is not null and (id.id_inscricao = n.id_inscricao and id.id_disciplina=n.id_disciplina)) "
                + "or n.id_inscricao is null) group by id.id_inscricao,e.nome_completo,e.nr_estudante,mediafreq,classfreq";
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
                        String v = notas[tls.get(j)];
                        cont = cont + 1;
                        if (!v.isEmpty() && !v.equals(" ")) {
                            f = Float.parseFloat(v);
                            if (f >= 0) {
                                t = t + f;
                                snotas = snotas + f;
                            } else {
                                for (Observacao o : lo) {
                                    if (f == o.getCodigo().floatValue()) {
                                        snotas = snotas + o.getIdobservacao();
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
                pa.setNota1(new Double(Math.ceil(m)).floatValue());//Messagebox.show(m+"");
                // }
                pa.setNota(snotas);
            } else {
                pa.setNota(snotas);
                if (pa.getNota1() == null) {
                    pa.setNota1((float) 0);
                    pa.setEditavel(true);
                }
            }
            if (pa.getNota1() == 0) {
                pa.setEditavel(true);
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
 
    public ListModel<Curso> getListaCursoModel() {
        //Users u = csimpm.get(Users.class, usr.getUtilizador());
        List<Curso> lc = null;
        par.clear();
        if (usr.getFaculdade().getLocalizacao() == null) {
            lc = csimp.getAll(Curso.class);
        } else {
            Faculdade f = csimp.get(Faculdade.class, usr.getFaculdade().getIdFaculdade());
            par.put("fac", f);
            lc = csimp.findByJPQuery("from Curso c where c.faculdade = :fac", par);
        }
        return new ListModelList<Curso>(lc);
    }
    
    public void onSIndexCbcurso() {
        if (cbcurso.getModel() != null && cbcurso.getModel().getSize() > 0) {
            cbcurso.setSelectedIndex(0);
        }
    }
   
   public void onSelCurso() {
        if (cbcurso.getSelectedItem() != null) {
            selCursoPrec();
        }
    }
}
