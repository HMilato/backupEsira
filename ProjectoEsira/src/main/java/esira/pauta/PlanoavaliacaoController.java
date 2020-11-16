package esira.pauta;

import esira.domain.Curso;
import esira.domain.Disciplina;
import esira.domain.Funcionario;
import esira.domain.Lecciona;
import esira.domain.Pauta;
import esira.domain.PautaPK;
import esira.domain.Planoavaliacao;
import esira.domain.PlanoavaliacaoPK;
import esira.domain.Planodisciplina;
import esira.domain.Users;
import esira.service.CRUDService;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import esira.domain.Tipoavaliacao;
import java.util.Calendar;
import java.util.GregorianCalendar;
import org.zkoss.lang.Strings;
import org.zkoss.web.servlet.dsp.action.Page;
import org.zkoss.xel.VariableResolver;
import org.zkoss.zhtml.Textarea;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.ComponentNotFoundException;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.event.InputEvent;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zk.ui.util.Composer;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zk.ui.util.Template;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.Button;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Constraint;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Div;
import org.zkoss.zul.Doublebox;
import org.zkoss.zul.FieldComparator;
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
import org.zkoss.zul.Radiogroup;
import org.zkoss.zul.Row;
import org.zkoss.zul.SimpleConstraint;
import org.zkoss.zul.Tabpanel;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Vbox;
import org.zkoss.zul.Window;
import org.zkoss.zul.event.PagingEvent;
import org.zkoss.zul.impl.InputElement;

/**
 *
 * @author UssimaneMuieva
 */
public class PlanoavaliacaoController extends GenericForwardComposer {

    @WireVariable
    private final CRUDService csimp = (CRUDService) SpringUtil.getBean("CRUDService");
    private Window windowPlanoavaliacao, formPlanoavaliacao, formDataavaliacao;
    private Paging pagPlanoavaliacao, pagDataavaliacao;
    static String ord = " ";
    Map<String, Object> par = new HashMap<String, Object>();
    Map<String, Object> parcond = new HashMap<String, Object>();
    private Listbox lbPlanoavaliacao, lbDataavaliacao;
    private Combobox cblecciona, cbidtipoavaliacao, cbturma, cbturno, cbSemPrec;
    private Doublebox dbpeso, dbquantidade;
    private Datebox d;
    private Radio rpnstatus, rpsstatus;
    private Radiogroup rpgstatus;
    private Div formplano, formplano2, formplano3, listaplano, tiposavaliacao, datasavaliacao, planoavaliacao;

    private Intbox turma, avaliacao, disciplina, turno, intbquantidade, intbavaliacao, inbdisciplina, update;
    private Intbox ano;
    private Button btCriar, btsubmeter;
    private Vbox vb3, space;

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
//        if (lbPlanoavaliacao != null) {
//            addeventoLB("from Planoavaliacao d " + ord, lbPlanoavaliacao, pagPlanoavaliacao, Planoavaliacao.class);
//        }

    }

    public void onChange$cblecciona(Event event) {
        selCursoPrec();
    }

    public void onChange$cbSemPrec(Event envent) {
        if (ano.getValue() == null || cbSemPrec.getSelectedItem() == null) {
            return;
        }
        cblecciona.setModel(this.getleccionaModelP());
        selCursoPrec();

    }

    public void onChanging$ano(InputEvent event) {
        if (!event.getValue().equals("")) {
            ano.setValue(Integer.parseInt(event.getValue()));
            if (ano.getValue() == null || cbSemPrec.getSelectedItem() == null) {
                return;
            }
            cblecciona.setModel(this.getleccionaModelP());
            selCursoPrec();
        } else {

        }
    }

    public void selCursoPrec() {
        if (cblecciona.getSelectedItem() == null || ano.getValue() == null || cbSemPrec.getSelectedItem() == null) {
            return;
        }

        while (!planoavaliacao.getChildren().isEmpty()) {
            planoavaliacao.getChildren().get(0).detach();
        }
        //  int ano = this.ano.getValue();
        Lecciona l = (Lecciona) cblecciona.getSelectedItem().getValue();
        Map<String, Object> par = new HashMap<String, Object>();
        par.put("d", l.getDisciplina());
        par.put("ano", l.getAno());
        par.put("sem", l.getSem().shortValue());
        par.put("turma", l.getLeccionaPK().getTurma());
        par.put("turno", l.getLeccionaPK().getTurno());

        String sql = "from Planoavaliacao p where p.disciplina = :d and p.planoavaliacaoPK.ano = :ano"
                + " and p.planoavaliacaoPK.turma = :turma and p.planoavaliacaoPK.turno = :turno and "
                + "p.sem = :sem";

        List<Planoavaliacao> lplano = csimp.findByJPQuery(sql, par);
        //  par.remove("turma");
        //  par.remove("turno");
        //  par.remove("sem");
        //  par.put("sem", l.getSem().shortValue());

        sql = "from Pauta p where p.disciplina = :d and p.pautaPK.ano = :ano and p.turma = :turma and p.turno = :turno"
                + " and p.pautaPK.semestre = :sem";
        List<Pauta> lpauta = csimp.findByJPQuery(sql, par);
        btsubmeter.setVisible(true);
        if (!lplano.isEmpty()) {

            btCriar.setVisible(false);
            vb3.setVisible(false);
            space.setHeight("0");
            formplano3.setVisible(true);

            check(planoavaliacao);
            Planoavaliacao pp = lplano.get(0);
            for (Planoavaliacao p : lplano) {
                pp = p;
                p.getStatus();
                Div d = new Div();
                Div d1 = new Div();
                Div d2 = new Div();
                Label lb = new Label();
                lb.setStyle("font-size:14px");
                lb.setValue(p.getTipoavaliacao().getDescricao());
                //   System.out.println(p.getTipoavaliacao().getDescricao());
                d1.setStyle("width:50%;display: inline-block;color:blue;font-size:14px");
                //   lb.setHflex("1");
                Label lb1 = new Label();
                lb1.setStyle("font-size:14px");
                d2.setStyle("width:50%;display: inline-block;font-size:14px");
                // lb1.setHflex("1");
                lb1.setValue(p.getPeso().toString() + "%");
                lb.setParent(d1);
                lb1.setParent(d2);
                d1.setParent(d);
                d2.setParent(d);
                d.setParent(planoavaliacao);
            }
            Clients.showNotification(pp.getPeso()+"");
            if (pp.getStatus()) {
                btsubmeter.setVisible(false);
            }
            Label nLabel = new Label();
            nLabel.setHflex("1");
            nLabel.setValue("--------------------------------------------------------------------------------");
            nLabel.setParent(planoavaliacao);

            if (!lpauta.isEmpty()) {

                for (Pauta p : lpauta) {

                    Div d = new Div();
                    Div d1 = new Div();
                    Div d2 = new Div();
                    Div d3 = new Div();
                    Label lb = new Label();
                    lb.setStyle("font-size:14px");
                    lb.setValue(p.getDescricao());
                    d1.setStyle("width:50%;display: inline-block;color:blue;font-size:14px");
                    //    lb.setHflex("1");
                    Label lb1 = new Label();
                    lb1.setStyle("font-size:14px");
                    d2.setStyle("width:50%;display: inline-block;font-size:14px");
                    //    lb1.setHflex("1");
                    lb1.setValue(new java.text.SimpleDateFormat("dd/MM/yyyy").format(p.getPautaPK().getDatap()));
                    lb.setParent(d1);
                    lb1.setParent(d2);
                    d1.setParent(d);
                    d2.setParent(d);
                    d.setParent(planoavaliacao);

//    nLabel = new Label(); 
//            nLabel.setValue("--------------------------------------------------------------------------------");
//            nLabel.setParent(planoavaliacao);
//                Button b = new Button("Editar o plano");
                    //  b.setStyle("display: inline-block;");
                    //   b.setHflex("1");
//        b.addForward("onClick", "self", "onEditPlano");
//        b.setParent(planoavaliacao);
                }
            }

//            btAdd.setVisible(true);
        } else {
            formplano3.setVisible(false);
            limpar(formplano3);
            space.setHeight("25px");
            btCriar.setVisible(true);
            vb3.setVisible(true);
//            btAdd.setVisible(false);

        }

    }

    public ListModel<Lecciona> getleccionaModelP() {

        Users usr = (Users) Sessions.getCurrent().getAttribute("user");
        Users u = csimp.load(Users.class, usr.getUtilizador());
        int ano = this.ano.getValue();
        int sem = Integer.parseInt(cbSemPrec.getSelectedItem().getValue().toString());
        par.clear();
        par.put("docente", u.getIdFuncionario());//u.getIdFuncionario().getDocente()
        par.put("ano", ano);
        par.put("sem", sem);

        List<Lecciona> lecionaList = csimp.findByJPQuery("from Lecciona l where l.docente = :docente and l.sem = :sem and l.ano = :ano ", par);

        return new ListModelList<Lecciona>(lecionaList);
    }

    public ListModel<Disciplina> getdisciplinaModel() {
        return new ListModelList<Disciplina>(csimp.getAll(Disciplina.class));
    }

    public ListModel<Lecciona> getleccionaModel() {
        return new ListModelList<Lecciona>(csimp.getAll(Lecciona.class));
    }

    public ListModel<Tipoavaliacao> getidtipoavaliacaoModel() {
        return new ListModelList<Tipoavaliacao>(csimp.getAll(Tipoavaliacao.class));
    }

    public void initLB(String sql, Listbox lb, Paging p, Class o) {
        p.setTotalSize(csimp.count(o));
        final int PAGE_SIZE = p.getPageSize();
        setLBModel(sql, lb, o, 0, PAGE_SIZE);
    }

    public void setLBModel(String sql, Listbox lb, Class c, int o, int p) {
        List<Planoavaliacao> planoavaliacaos = csimp.findByJPQueryFilter(sql, null, o, p);
        lbPlanoavaliacao.setModel(new ListModelList<>(planoavaliacaos));
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

    public void addeventoLB(final String sql, final Listbox lb, Paging p, final Class o) {
        addeventoOrd(sql, lb, p, o);
        addeventoPagin(sql, lb, p, o);
    }

    public void onAddPlano() {
//        formPlanoavaliacao.setParent(windowPlanoavaliacao);
//        formPlanoavaliacao.setTitle("Adicionar Planoavaliacao");
//        ((Combobox) formPlanoavaliacao.getFellow("cbdisciplina")).setValue("------------ Disciplina -----------");
//        ((Combobox) formPlanoavaliacao.getFellow("cbidtipoavaliacao")).setValue("------------ Tipoavaliacao -----------");
//        formPlanoavaliacao.doModal();
        listaplano.setVisible(false);
        formplano.setVisible(true);
    }

    public void onCancelPlano() {
        formplano.setVisible(false);
        listaplano.setVisible(true);

    }

    public void onBack() {
        formplano2.setVisible(false);
        datasavaliacao.getChildren().clear();
        formplano.setVisible(true);
        limpar(formplano2);

    }

    public void onAddpauta() {
        List<Pauta> lp = new ArrayList<>();
        Planoavaliacao planoavaliacao = new Planoavaliacao();
        Lecciona l = (Lecciona) cblecciona.getSelectedItem().getValue();
        Tipoavaliacao tipoavaliacao = (Tipoavaliacao) cbidtipoavaliacao.getSelectedItem().getValue();
        Date dano = new Date();
        Calendar c = new GregorianCalendar();
        c.setTime(dano);
        Pauta p = new Pauta();
        PautaPK ppk = new PautaPK();
        ppk.setAno(l.getAno());
        ppk.setDatap(c.getTime());
        ppk.setIddisc(l.getDisciplina().getIdDisc());
        ppk.setSemestre(l.getSem().shortValue());
        p.setPautaPK(ppk);
        p.setTurma(l.getLeccionaPK().getTurma());
        p.setTurno(l.getLeccionaPK().getTurno());

    }

    public void onAddDataavaliacao(ForwardEvent evt) {

        Button btn = (Button) evt.getOrigin().getTarget();
        Listitem litem = (Listitem) btn.getParent().getParent();
        Planoavaliacao planoavaliacao = (Planoavaliacao) litem.getValue();

        formDataavaliacao.setParent(windowPlanoavaliacao);
        formDataavaliacao.setTitle("Adicionar Data de Avaliacao");
        formDataavaliacao.doModal();

        ((Intbox) formDataavaliacao.getFellow("intbdisciplina")).setValue(planoavaliacao.getDisciplina().getIdDisc().intValue());
        ((Intbox) formDataavaliacao.getFellow("intbquantidade")).setValue(planoavaliacao.getQuantidade().intValue());

        //((Intbox) formDataavaliacao.getFellow("disciplina")).setValue(planoavaliacao.getDisciplina().getIdDisc().intValue());
        //((Intbox) formDataavaliacao.getFellow("turma")).setValue(planoavaliacao.getPlanoavaliacaoPK().getTurma());
        //((Intbox) formDataavaliacao.getFellow("turno")).setValue(planoavaliacao.getPlanoavaliacaoPK().getTurno());
        ((Intbox) formDataavaliacao.getFellow("intbtipoavaliacao")).setValue(planoavaliacao.getTipoavaliacao().getIdtipoavaliacao().intValue());

    }

    public void onClick$cancelPlanoavaliacao(Event e) {
        limpar(formPlanoavaliacao);
        update.setValue(0);
        formPlanoavaliacao.detach();
    }

    public void onCancelarDataavaliacao(ForwardEvent evt) throws Exception {
        Messagebox.show("d");
        limpar(formplano2);
        limpar(formDataavaliacao);
        formDataavaliacao.detach();
    }

    public void onEditarPlanoavaliacao(ForwardEvent evt) throws Exception {
        Button btn = (Button) evt.getOrigin().getTarget();
        Listitem litem = (Listitem) btn.getParent().getParent();
        Planoavaliacao planoavaliacao = (Planoavaliacao) litem.getValue();
        Long disciplina = planoavaliacao.getDisciplina().getIdDisc();
        Integer idtipoavaliacao = planoavaliacao.getTipoavaliacao().getIdtipoavaliacao().intValue();
        formPlanoavaliacao.setParent(windowPlanoavaliacao);
        formPlanoavaliacao.setTitle("Editar Plano de Avaliacao");
        formPlanoavaliacao.doModal();

        ((Doublebox) formPlanoavaliacao.getFellow("dbpeso")).setValue(planoavaliacao.getPeso().doubleValue());
        ((Doublebox) formPlanoavaliacao.getFellow("dbquantidade")).setValue(planoavaliacao.getQuantidade());

        ((Intbox) formPlanoavaliacao.getFellow("disciplina")).setValue(planoavaliacao.getDisciplina().getIdDisc().intValue());
        ((Intbox) formPlanoavaliacao.getFellow("turma")).setValue(planoavaliacao.getPlanoavaliacaoPK().getTurma());
        ((Intbox) formPlanoavaliacao.getFellow("turno")).setValue(planoavaliacao.getPlanoavaliacaoPK().getTurno());
        ((Intbox) formPlanoavaliacao.getFellow("avaliacao")).setValue(planoavaliacao.getTipoavaliacao().getIdtipoavaliacao().intValue());
        ((Intbox) formPlanoavaliacao.getFellow("update")).setValue(1);

        final Iterator<Comboitem> itemsdisciplina = new ArrayList(((Combobox) formPlanoavaliacao.getFellow("cbdisciplina")).getItems()).listIterator();
        Comboitem citdisciplina;
        while (itemsdisciplina.hasNext()) {
            citdisciplina = itemsdisciplina.next();
            if (((Lecciona) citdisciplina.getValue()).getDisciplina().getIdDisc() == disciplina) {
                ((Combobox) formPlanoavaliacao.getFellow("cbdisciplina")).setSelectedItem(citdisciplina);
                break;
            }
        }
        final Iterator<Comboitem> itemsidtipoavaliacao = new ArrayList(((Combobox) formPlanoavaliacao.getFellow("cbidtipoavaliacao")).getItems()).listIterator();
        Comboitem citidtipoavaliacao;
        while (itemsidtipoavaliacao.hasNext()) {
            citidtipoavaliacao = itemsidtipoavaliacao.next();
            if (((Tipoavaliacao) citidtipoavaliacao.getValue()).getIdtipoavaliacao().intValue() == idtipoavaliacao) {
                ((Combobox) formPlanoavaliacao.getFellow("cbidtipoavaliacao")).setSelectedItem(citidtipoavaliacao);
                break;
            }
        }
    }

    public void onSave() {

        check(formplano2);

        Lecciona l = (Lecciona) cblecciona.getSelectedItem().getValue();

        List<Div> div = ((Div) formplano.getFellow("tiposavaliacao")).getChildren();
//        List<Tipoavaliacao> lta = new ArrayList<Tipoavaliacao>();
        try {
            for (Div d : div) {
                Planoavaliacao planoavaliacao = new Planoavaliacao();

                Combobox cb = ((Combobox) d.getChildren().get(0));
                Tipoavaliacao t = (Tipoavaliacao) cb.getSelectedItem().getValue();
//                int qt = ((Intbox)d.getChildren().get(2)).getValue();
//                for(int q=0;q<qt;q++)
//                lta.add(t);
                PlanoavaliacaoPK planoavaliacaoPK = new PlanoavaliacaoPK(
                        l.getDisciplina().getIdDisc(), l.getLeccionaPK().getTurno(), l.getLeccionaPK().getTurma(),
                        t.getIdtipoavaliacao().intValue(), l.getAno());

                planoavaliacao.setPlanoavaliacaoPK(planoavaliacaoPK);
                planoavaliacao.setPeso(t.getPesomaximo().floatValue());
                planoavaliacao.setDisciplina(l.getDisciplina());
                planoavaliacao.setTipoavaliacao(t);
                planoavaliacao.setStatus(Boolean.FALSE);
                planoavaliacao.setSem(l.getSem().shortValue());
//        planoavaliacao.setQuantidade(dbquantidade.getValue().longValue());

                csimp.Save(planoavaliacao);
//                ((ListModelList) lbPlanoavaliacao.getModel()).add(planoavaliacao);

            }

            List<Div> divs = ((Div) formplano2.getFellow("datasavaliacao")).getChildren();
            int qp = 0;
            for (Div d : divs) {
                  Intbox ib = ((Intbox) d.getChildren().get(2));
                  Intbox ib2 = ((Intbox) d.getChildren().get(3));
                 // Tipoavaliacao t = (Tipoavaliacao) cb.getSelectedItem().getValue();
//Messagebox.show("vvv");
                Datebox i = ((Datebox) d.getChildren().get(1));

                Pauta p = new Pauta();
                PautaPK ppk = new PautaPK();
                ppk.setAno(l.getAno());
                ppk.setDatap(i.getValue());
                ppk.setIddisc(l.getDisciplina().getIdDisc());
                ppk.setSemestre(l.getSem().shortValue());
                p.setPautaPK(ppk);
                p.setTurma(l.getLeccionaPK().getTurma());
                p.setTurno(l.getLeccionaPK().getTurno());
                p.setTipoavaliacao(((Label) d.getChildren().get(0)).getValue());
                p.setTipo(ib.getValue().shortValue());
                p.setOrdem((short) qp);
                p.setPercent(ib2.getValue());
                p.setEdicao(0);
                Users usr = (Users) Sessions.getCurrent().getAttribute("user");

                Users u = csimp.load(Users.class, usr.getUtilizador());
                p.setDocente(u.getIdFuncionario().getDocente());
                p.setDescricao(((Label) d.getChildren().get(0)).getValue());
                csimp.Save(p);
                qp++;
            }
            listaplano.setVisible(true);
            formplano2.setVisible(false);
            limpar(formplano2);
            limpar(formplano);
            Clients.showNotification("Adicionado com Sucesso", "info", self, null, 3000);
            selCursoPrec();
        } catch (ComponentNotFoundException | WrongValueException e) {
            Clients.showNotification("Nao foi Possivel", "info", self, null, 3000);
        }
    }

    public void onSavePlanoavaliacao() throws ParseException {
        check(formplano);
        List<Div> divs = ((Div) formplano.getFellow("tiposavaliacao")).getChildren();
        int perc = 0;
        for (Div d : divs) {

            perc += ((Intbox) d.getChildren().get(1)).getValue();

        }
        if (perc > 100 || perc < 100) {
            Clients.showNotification("O somatorio das percentagens  deve ser igual 100!", "error", null, null, 3000);
//            Clients.showNotification("A percentagem  deve ser 100!", "warning", self, null, 3000);
            return;
        }
        formplano.setVisible(false);
        formplano2.setVisible(true);
        addata();

//        Tipoavaliacao tipoavaliacao = (Tipoavaliacao) cbidtipoavaliacao.getSelectedItem().getValue();
//
////        String tu = cbturma.getSelectedItem().getValue().toString();
////        String tun = cbturno.getSelectedItem().getValue().toString();
//        String sql = "UPDATE planoavaliacao SET turma= ?, turno = ?, iddisc=?,  idtipoavaliacao=?"
//                + " WHERE iddisc = ? AND idtipoavaliacao = ?";
//        PlanoavaliacaoPK planoavaliacaoPK = new PlanoavaliacaoPK(
//                l.getDisciplina().getIdDisc(), l.getLeccionaPK().getTurma(),
//                l.getLeccionaPK().getTurno(), tipoavaliacao.getIdtipoavaliacao().intValue(), l.getAno());
//
////        Planoavaliacao pp = csimp.load(Planoavaliacao.class,planoavaliacaoPK );
////        if (pp!= null){
////            Clients.showNotification("Este plano Ja existe", "warning", self, null, 3000);
////            limpar(formPlanoavaliacao);
////            formPlanoavaliacao.detach();
////            return ;
////        }
//        planoavaliacao.setPlanoavaliacaoPK(planoavaliacaoPK);
//        planoavaliacao.setPeso(dbpeso.getValue().floatValue());
//        planoavaliacao.setDisciplina(l.getDisciplina());
//        planoavaliacao.setTipoavaliacao(tipoavaliacao);
//        planoavaliacao.setQuantidade(dbquantidade.getValue().longValue());
//
//        if (formPlanoavaliacao.getTitle().charAt(0) == 'E') {
//            csimp.updateQuery(sql, turma.getValue(), turno.getValue(), l.getDisciplina().getIdDisc(), avaliacao.getValue(), l.getDisciplina().getIdDisc(),
//                    planoavaliacao.getTipoavaliacao().getIdtipoavaliacao());
//
//        } else {
//
//        }
//
//        if (formPlanoavaliacao.getTitle().charAt(0) == 'E') {
////            csimp.update(planoavaliacao);
//        } else {
//
//            csimp.Save(planoavaliacao);
//
//            for (int i = 0; i < dbquantidade.getValue().intValue(); i++) {
//
//                Date data = ((Datebox) formPlanoavaliacao.getFellow("d" + (i + 1))).getValue();
////                Dataavaliacao dataavaliacao = new Dataavaliacao();
////
////                dataavaliacao.setAvaliacao(tipoavaliacao.getDescricao() + "-" + (i + 1));
////                dataavaliacao.setDataavaliacao(data);
////                dataavaliacao.setDataavaliacaoPK(new DataavaliacaoPK(l.getDisciplina().getIdDisc(), tipoavaliacao.getIdtipoavaliacao(), data));
////                dataavaliacao.setStatus(Boolean.FALSE);
//
//                Pauta p = new Pauta();
//                PautaPK ppk = new PautaPK();
//                ppk.setAno(planoavaliacaoPK.getAno());
//                ppk.setDatap(data);
//                ppk.setIddisc(l.getDisciplina().getIdDisc());
//                ppk.setSemestre(l.getSem().shortValue());
//                p.setPautaPK(ppk);
//                p.setTurma(l.getLeccionaPK().getTurma());
//                p.setTurno(l.getLeccionaPK().getTurno());
//
//                Users usr = (Users) Sessions.getCurrent().getAttribute("user");
//
//                Users u = csimp.load(Users.class, usr.getUtilizador());
//                p.setDocente(u.getIdFuncionario());
//                p.setDescricao(tipoavaliacao.getDescricao() + "-" + (i + 1));
//                csimp.Save(p);
////                csimp.Save(dataavaliacao);
//
//            }
//        }
//
//        if (formPlanoavaliacao.getTitle().charAt(0) == 'E') {
//            int ind = ((ListModelList) lbPlanoavaliacao.getModel()).indexOf(planoavaliacao);
//
//            if (ind >= 0) {
//                ((ListModelList) lbPlanoavaliacao.getModel()).set(ind, planoavaliacao);
//            }
//
//            Clients.showNotification(" Actualizado com Sucesso", null, null, null, 0);
//        } else {
//            ListModelList lm = (ListModelList) lbPlanoavaliacao.getModel();
//            if (lm != null) {
//                lm.add(planoavaliacao);
//            } else {
//                lbPlanoavaliacao.setModel(new ListModelList<>());
//                ((ListModelList) lbPlanoavaliacao.getModel()).add(planoavaliacao);
//            }
//            Clients.showNotification(" Adicionado com Sucesso", null, null, null, 0);
//        }
//
//        limpar(formPlanoavaliacao);
//        formPlanoavaliacao.detach();
    }

    private void limpar(Component component) {
        limparComp(component);

        //if (component.isVisible()) {
        List<Component> children = component.getChildren();
        for (Component each : children) {
            limpar(each);
        }
        // }
    }

    public void limparComp(Component component) {
        Constraint co = null;
        if (component instanceof InputElement) {
            Constraint c = ((InputElement) component).getConstraint();
            if (!(component instanceof Combobox)) {
                if (c != null) {
                    SimpleConstraint sc = (SimpleConstraint) c;
                    String s = sc.getClientConstraint();
                    ((InputElement) component).setConstraint(co);
                    ((InputElement) component).setText("");
                    ((InputElement) component).setConstraint(c);
                } else {
                    ((InputElement) component).setText("");
                }
            }
        }
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
            if ((!((InputElement) component).isValid())) {
                Clients.scrollIntoView(component);
                ((InputElement) component).getText();
            }
            if (((component instanceof Combobox) && ((Combobox) component).getSelectedItem() == null)) {
                Clients.scrollIntoView(component);
                ((Combobox) component).setText("");
                ((Combobox) component).getValue();
            }
        }
    }

    //  public void onAddTipoA() {
    public void addata() {
        //   check(datasavaliacao);

        Combobox c = new Combobox();
        c.setStyle("display: inline-block;color:blue");
        c.setHflex("1");
        c.setReadonly(true);
//        c.addForward("onSelect", "self", "onSelectT");
        c.setValue("---Selecione o tipo de Avaliação---");

        List<Div> divs = ((Div) formplano.getFellow("tiposavaliacao")).getChildren();
        //     List<Tipoavaliacao> list = new ArrayList<>();
        for (Div di : divs) {

            Combobox c1 = ((Combobox) di.getChildren().get(0));
            Tipoavaliacao t = (Tipoavaliacao) c1.getSelectedItem().getValue();

            Intbox i = ((Intbox) di.getChildren().get(1));
            t.setPesomaximo(i.getValue().intValue());

            int nrtestes = ((Intbox) di.getChildren().get(2)).getValue();
            for (int j = 0; j < nrtestes; j++) {
                Div d = new Div();
                Label lb1 = new Label();
                lb1.setStyle("font-size:14px");
                lb1.setValue(t.getDescricao() + " " + (j + 1));
                lb1.setHflex("1");
                lb1.setParent(d);
                Datebox db = new Datebox();
                db.setStyle("display: inline-block;");
                db.setConstraint(" no Empty: Introduza a data");
                db.setHflex("1");
                db.setParent(d);
                Intbox ib = new Intbox();
                        ib.setVisible(false);
                        ib.setValue(t.getIdtipoavaliacao());
                        ib.setParent(d);
                        ib = new Intbox();
                        ib.setVisible(false);
                        ib.setValue(t.getPesomaximo());
                        ib.setParent(d);
                d.setParent(datasavaliacao);
            }
        }

    }

    public void onClick$adtipo() {
        check(tiposavaliacao);
        Div d = new Div();
        Combobox c = new Combobox();
        c.setStyle("display: inline-block;color:blue");
        c.setHflex("1");
        c.setReadonly(true);
        c.addForward("onSelect", "self", "onSelectT");
        c.setValue("---Selecione o tipo de Avaliação---");
        c.setModel(new ListModelList<>(csimp.getAll(Tipoavaliacao.class)));
        c.setParent(d);
        Intbox i = new Intbox();
        i.setStyle("display: inline-block;");
        i.setHflex("1");
        i.setParent(d);
        Intbox i2 = new Intbox();
        i2.setStyle("display: inline-block;");
        i2.setHflex("1");
        i2.setParent(d);
        Button b = new Button("Apagar");
        b.setStyle("display: inline-block;");
        b.setHflex("1");
        b.addForward("onClick", "self", "onDeletTipoA");
        b.setParent(d);
        d.setParent(tiposavaliacao);
        String script = "jq('$" + i.getUuid() + "').find('input').Watermark('--Percentagem--','gray');jq('$" + i2.getUuid() + "').find('input').Watermark('--Quantidade--','gray');";
        Clients.evalJavaScript(script);
        c.setConstraint(" no Empty: Seleccione um tipo de avalição!");
        i.setConstraint(" no Empty: Percentagem");
        i2.setConstraint(" no Empty: Quantidade");
    }

    public void onSelectT(ForwardEvent evt) {
        Combobox c = (Combobox) evt.getOrigin().getTarget();
        if (!c.getParent().getId().equals(((Tipoavaliacao) c.getSelectedItem().getValue()).getIdtipoavaliacao() + "")) {

            try {
                c.getParent().setId(((Tipoavaliacao) c.getSelectedItem().getValue()).getIdtipoavaliacao() + "");
            } catch (Exception e) {
                c.setSelectedIndex(-1);
                c.setValue("---Selecione o tipo de Avaliação---");
                Clients.showNotification("Selecione outro tipo de avaliacao", "error", null, null, 3000);
            }
        } else {
            c.setSelectedIndex(-1);
            c.setValue("---Selecione o tipo de Avaliação---");
        }
    }

    public void onDeletTipoA(ForwardEvent evt) {
        Button b = (Button) evt.getOrigin().getTarget();
        Component c = b.getParent();
        ((Component) b.getParent().getParent()).removeChild(c);
        new Div().appendChild(c);
    }

    public void onRefazer() {
        Messagebox.show("Pretende refazer o plano de avaliação?", "Prompt", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION,
                new EventListener() {
            @Override
            public void onEvent(Event evet) {
                switch (((Integer) evet.getData()).intValue()) {
                    case Messagebox.YES:
                        if (cblecciona.getSelectedItem() == null || ano.getValue() == null || cbSemPrec.getSelectedItem() == null) {
                            Clients.showNotification("Seleccione o ano, semestre e a turma");
                            return;
                        }
                        Lecciona l = (Lecciona) cblecciona.getSelectedItem().getValue();
                        Map<String, Object> par = new HashMap<String, Object>();
                        Long iddisc = l.getDisciplina().getIdDisc();
                        int ano = l.getAno();
                        Short sem = l.getSem().shortValue();
                        int turma = l.getLeccionaPK().getTurma();
                        int turno = l.getLeccionaPK().getTurno();
                        csimp.updateQuery("delete from planoavaliacao where iddisc = " + iddisc + " and turno = " + turno + " and turma = " + turma + " and ano = " + ano + " and sem = " + sem);
                        csimp.updateQuery("delete from pauta where iddisc = " + iddisc + " and turno = " + turno + " and turma = " + turma + " and ano = " + ano + " and semestre = " + sem);
                        formplano3.setVisible(false);////
                        limpar(formplano3);
                        space.setHeight("25px");
                        btCriar.setVisible(true);
                        vb3.setVisible(true);
                        listaplano.setVisible(false);
                        formplano.setVisible(true);
                        break;
                    case Messagebox.NO:
                        return;
                }
            }

        });
    }

    public void onSubmeter() {
        Messagebox.show("Apagar?", "Prompt", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION,
                new EventListener() {
            @Override
            public void onEvent(Event evet) {
                switch (((Integer) evet.getData()).intValue()) {
                    case Messagebox.YES:
                        if (cblecciona.getSelectedItem() == null || ano.getValue() == null || cbSemPrec.getSelectedItem() == null) {
                            Clients.showNotification("Seleccione o ano, semestre e a turma");
                            return;
                        }

                        //  int ano = this.ano.getValue();
                        Lecciona l = (Lecciona) cblecciona.getSelectedItem().getValue();
                        Map<String, Object> par = new HashMap<String, Object>();
                        par.put("d", l.getDisciplina());
                        par.put("ano", l.getAno());
                        par.put("sem", l.getSem().shortValue());
                        par.put("turma", l.getLeccionaPK().getTurma());
                        par.put("turno", l.getLeccionaPK().getTurno());

                        String sql = "from Planoavaliacao p where p.disciplina = :d and p.planoavaliacaoPK.ano = :ano"
                                + " and p.planoavaliacaoPK.turma = :turma and p.planoavaliacaoPK.turno = :turno and "
                                + "p.sem = :sem";

                        List<Planoavaliacao> lplano = csimp.findByJPQuery(sql, par);
                        if (!lplano.isEmpty()) {
                            for (Planoavaliacao p : lplano) {
                                p.setStatus(Boolean.TRUE);
                                csimp.update(p);
                            }
                        }
                        btsubmeter.setVisible(false);
                        break;
                    case Messagebox.NO:
                        return;
                }
            }

        });
    }
}
