/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package esira.controller;

//import esira.controller.authentication.Grupo;
//import esira.controller.authentication.GrupoRole;
import esira.domain.Cargochefia;
import esira.domain.Estudante;
import esira.domain.Faculdade;
import esira.domain.Funcionario;
import esira.domain.Grupo;
import esira.domain.Profissao;
import esira.domain.Provincia;
import esira.domain.Roles;
import esira.domain.Tipochefia;
import esira.domain.Users;
import esira.service.CRUDService;
import esira.webapp.Item;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.zkoss.lang.Strings;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.event.InputEvent;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Constraint;
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
import org.zkoss.zul.SimpleConstraint;
import org.zkoss.zul.Tabpanel;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;
import org.zkoss.zul.event.PagingEvent;
import org.zkoss.zul.impl.InputElement;

/**
 *
 * @author user
 */
public class FuncionarioController extends GenericForwardComposer {

    @WireVariable
    private final CRUDService csimp = (CRUDService) SpringUtil.getBean("CRUDService");

    private static final long serialVersionUID = 43014628867656917L;
    List<Grupo> grupo = csimp.getAll(Grupo.class);
    ListModelList<Grupo> grupos = new ListModelList<Grupo>(grupo);
    List<Users> users = csimp.getAll(Users.class);
    private List<Funcionario> funcionarios = csimp.getAll(Funcionario.class);
    private ListModel<Funcionario> funcionarioModel = new ListModelList<Funcionario>(funcionarios);

    private Textbox tbnrfuncionario;
    private Textbox tbnome;
    private Textbox tbapelido;
    private Textbox tbcontacto;
    private Textbox tbemail, txPnr, txPnome, tbdescricao;
    private Listbox lbFuncionario;
    private Intbox ibidFuncionario;
    private Row rwchefia;
    private Radio rgsexo, rgNP, rgP;
    private Combobox cbfaculdade, cbPfac, cbcargochefia;
    Window mDialogAddFuncionario, windowFuncionario;
    Map<String, Object> par = new HashMap<String, Object>();
    private Paging pagFuncionario;
    private Listheader lhnome;
    private Intbox litem;
    private Label lb;
    static String ord = "";
    String condfac = "", condnr = "", condnome = "";
    Map<String, Object> condpar = new HashMap<String, Object>();
    private Window windowCargo, mDialogAddCargo;
    private Textbox txtproccargo, txtCargo;
    private Intbox caid;
    private Listbox lbCargo;
    
    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        if (lbFuncionario != null) {
            addeventoLB("from Funcionario f where 1=1" + ord, lbFuncionario, pagFuncionario, Funcionario.class);
        }
    }

    public void onChange$cbPfac() {
        if (cbPfac.getSelectedItem() != null && !cbPfac.getText().equals("")) {
            condfac = " and f.faculdade = :fac";
            condpar.put("fac", (Faculdade) cbPfac.getSelectedItem().getValue());
        } else {
            condfac = "";
            condpar.remove("fac");
        }
        addeventoLB("from Funcionario f where 1=1" + condfac + condnr + condnome + ord, lbFuncionario, pagFuncionario, Funcionario.class);
    }

    public void onChanging$txPnr(InputEvent evt) {
        // evt.stopPropagation();
        if (!evt.getValue().equals("") && evt.getValue().charAt(0) != '.') {
            condnr = " and lower(f.nrfuncionario) like :nr";
            condpar.put("nr", "%" + evt.getValue().toLowerCase() + "%");
        } else {
            condnr = "";
            condpar.remove("nr");
        }
        addeventoLB("from Funcionario f where 1=1" + condfac + condnr + condnome + ord, lbFuncionario, pagFuncionario, Funcionario.class);
    }

    public void onCreateWnd(Event evt) {

        Window window = (Window) evt.getTarget();

        if (window.getId().equals("windowFuncionario")) {
            window.addEventListener("onNotifyServer", this);
        } else {
            //for others
        }
    }

    public void onChanging$txPnome(InputEvent evt) {
        if (!evt.getValue().equals("") && evt.getValue().charAt(0) != '.') {
            condnome = " and lower(f.nome) like :nome";
            condpar.put("nome", "%" + evt.getValue().toLowerCase() + "%");
        } else {
            condnome = "";
            condpar.remove("nome");
        }
        addeventoLB("from Funcionario f where 1=1" + condfac + condnr + condnome + ord, lbFuncionario, pagFuncionario, Funcionario.class);
    }

    public void onClick$addFunc() {
        mDialogAddFuncionario.setParent(windowFuncionario);
        mDialogAddFuncionario.setTitle("Adicionar Funcionario");
        ((Combobox) mDialogAddFuncionario.getFellow("cbfaculdade")).setValue("------faculdade------");
        ((Combobox) mDialogAddFuncionario.getFellow("cbcargochefia")).setValue("------cargochefia------");
        limpar(mDialogAddFuncionario);
        mDialogAddFuncionario.doModal();
    }

    public void onClick$cancelFuncionario(Event e) {
        limpar(mDialogAddFuncionario);
        mDialogAddFuncionario.detach();
    }

    public ListModel<Faculdade> getFaculdadePModel() {
        List<Faculdade> lf = new ArrayList<Faculdade>();
        lf.add(new Faculdade(-1));
        lf.addAll(csimp.getAll(Faculdade.class));
        return new ListModelList<Faculdade>(lf);
    }

    public ListModel<Faculdade> getFaculdadeModel() {
        List<Faculdade> lf = csimp.getAll(Faculdade.class);
        return new ListModelList<Faculdade>(lf);
    }

    public ListModel<Funcionario> getFuncionarioModel() {
        return funcionarioModel;
    }

    public ListModel<Cargochefia> getCargochefiaModel() {
        List<Cargochefia> lc = csimp.getAll(Cargochefia.class);
        Cargochefia c = new Cargochefia();
        c.setDescricao("Adicionar Novo Cargo........");
        lc.add(c);
        return new ListModelList<Cargochefia>(lc);
    }

    public void initLB(String sql, Listbox lb, Paging p, Class o) {
        p.setTotalSize(csimp.count(o));
        final int PAGE_SIZE = p.getPageSize();
        setLBModel(sql, lb, o, 0, PAGE_SIZE);
    }

    public void setLBModel(String sql, Listbox lb, Class c, int o, int p) {
        funcionarios = csimp.findByJPQueryFilter(sql, condpar, o, p);
        // funcionarioModel = new ListModelList<Funcionario>(funcionarios);
        lbFuncionario.setModel(new ListModelList<>(funcionarios));
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

    public void onCheck$rdchefia() {
        if (rgNP.isChecked()) {
            rwchefia.setVisible(false);
        } else if (rgP.isChecked()) {
            rwchefia.setVisible(true);
        }
    }

    public void onSalvar(ForwardEvent evt) {
        check(mDialogAddFuncionario);
        Funcionario func = new Funcionario();
        Tipochefia tc = null;
        if (mDialogAddFuncionario.getTitle().charAt(0) == 'E') {
            func = csimp.get(Funcionario.class, ibidFuncionario.getValue().longValue());
            tc = func.getTipochefia();
        }
        func.setNrfuncionario(tbnrfuncionario.getValue());
        func.setNome(tbnome.getValue());
        func.setApelido(tbapelido.getValue());
        func.setContacto(tbcontacto.getValue());
        func.setEmail(tbemail.getValue());
        func.setFaculdade((Faculdade) cbfaculdade.getSelectedItem().getValue());
        if (rgsexo.isChecked()) {
            func.setMasculino(true);
        } else {
            func.setMasculino(false);
        }
        if (mDialogAddFuncionario.getTitle().charAt(0) == 'E') {
            csimp.update(func);
        } else {
            csimp.Save(func);
        }
        if (rgP.isChecked()) {
            if (tc == null) {
                tc = new Tipochefia();
                tc.setDescricao(tbdescricao.getValue());
                tc.setCargochefia((Cargochefia) cbcargochefia.getSelectedItem().getValue());
                tc.setFuncionario(func);
                tc.setIdfuncionario(func.getIdFuncionario());
                csimp.Save(tc);
            } else {
                tc.setDescricao(tbdescricao.getValue());
                tc.setCargochefia((Cargochefia) cbcargochefia.getSelectedItem().getValue());
                csimp.update(tc);
            }
        } else {
            if (tc != null) {
                func.setTipochefia(null);
                csimp.delete(tc);
            }
        }
        if (mDialogAddFuncionario.getTitle().charAt(0) == 'E') {
            ((ListModelList) lbFuncionario.getModel()).set(litem.getValue(), func);
            Clients.showNotification(" Actualizado com Sucesso", null, null, null, 0);
        } else {
            ((ListModelList) lbFuncionario.getModel()).add(func);
            Clients.showNotification(" Adicionado com Sucesso", null, null, null, 0);
        }
        limpar(mDialogAddFuncionario);
        mDialogAddFuncionario.detach();

    }

    public void onEditFuncionario(ForwardEvent evt) throws Exception {
        Button btn = (Button) evt.getOrigin().getTarget();
        Listitem litem = (Listitem) btn.getParent().getParent();
        Funcionario funcionario = (Funcionario) litem.getValue();
        funcionario = csimp.load(Funcionario.class, funcionario.getIdFuncionario());
        Tipochefia tc = funcionario.getTipochefia();
        int faculdade = funcionario.getFaculdade().getIdFaculdade();

        mDialogAddFuncionario.setParent(windowFuncionario);
        mDialogAddFuncionario.setTitle("Editar Funcionario");
        mDialogAddFuncionario.doModal();
        ((Radio) mDialogAddFuncionario.getFellow("rgNP")).setChecked(true);
        ((Radio) mDialogAddFuncionario.getFellow("rgP")).setChecked(false);
        ((Row) mDialogAddFuncionario.getFellow("rwchefia")).setVisible(false);
        ((Intbox) mDialogAddFuncionario.getFellow("litem")).setValue(litem.getIndex());
        ((Textbox) mDialogAddFuncionario.getFellow("tbnrfuncionario")).setValue(funcionario.getNrfuncionario());
        ((Textbox) mDialogAddFuncionario.getFellow("tbnome")).setValue(funcionario.getNome());
        ((Textbox) mDialogAddFuncionario.getFellow("tbapelido")).setValue(funcionario.getApelido());
        ((Textbox) mDialogAddFuncionario.getFellow("tbcontacto")).setValue(funcionario.getContacto());
        ((Textbox) mDialogAddFuncionario.getFellow("tbemail")).setValue(funcionario.getEmail());
        if (funcionario.getMasculino()) {
            ((Radio) mDialogAddFuncionario.getFellow("rgsexo")).setChecked(true);
        } else {
            ((Radio) mDialogAddFuncionario.getFellow("rgsm")).setChecked(true);
        }

        ((Intbox) mDialogAddFuncionario.getFellow("ibidFuncionario")).setValue(funcionario.getIdFuncionario().intValue());

        final Iterator<Comboitem> itemsfaculdade = new ArrayList(((Combobox) mDialogAddFuncionario.getFellow("cbfaculdade")).getItems()).listIterator();
        Comboitem citfaculdade;
        while (itemsfaculdade.hasNext()) {
            citfaculdade = itemsfaculdade.next();
            if (((Faculdade) citfaculdade.getValue()).getIdFaculdade() == faculdade) {
                ((Combobox) mDialogAddFuncionario.getFellow("cbfaculdade")).setSelectedItem(citfaculdade);
                break;
            }
        }

        if (tc != null) {
            ((Radio) mDialogAddFuncionario.getFellow("rgNP")).setChecked(false);
            ((Radio) mDialogAddFuncionario.getFellow("rgP")).setChecked(true);
            int cargochefia = tc.getCargochefia().getIdcargochefia();
            ((Textbox) mDialogAddFuncionario.getFellow("tbdescricao")).setValue(tc.getDescricao());
            final Iterator<Comboitem> itemscargochefia = new ArrayList(((Combobox) mDialogAddFuncionario.getFellow("cbcargochefia")).getItems()).listIterator();
            Comboitem citcargochefia;
            while (itemscargochefia.hasNext()) {
                citcargochefia = itemscargochefia.next();
                if (((Cargochefia) citcargochefia.getValue()).getIdcargochefia() == cargochefia) {   // deve-se conhecer o id do objecto embutido
                    ((Combobox) mDialogAddFuncionario.getFellow("cbcargochefia")).setSelectedItem(citcargochefia);
                    break;
                }
            }
            ((Row) mDialogAddFuncionario.getFellow("rwchefia")).setVisible(true);
        }
    }

    public void onDeleteFuncionario(final ForwardEvent evt) throws Exception {
        Messagebox.show("Apagar?", "Prompt", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION,
                new EventListener() {
                    @Override
                    public void onEvent(Event evet) {
                        switch (((Integer) evet.getData()).intValue()) {
                            case Messagebox.YES:
                                Button btn = (Button) evt.getOrigin().getTarget();
                                Listitem litem = (Listitem) btn.getParent().getParent();
                                Funcionario d = (Funcionario) litem.getValue();
                                ((ListModelList) lbFuncionario.getModel()).remove(d);
                                new Listbox().appendChild(litem);
                                csimp.delete(d);
                                Clients.showNotification(" apagado com sucesso", null, null, null, 2000);
                                break;
                            case Messagebox.NO:
                                return;
                        }
                    }

                });
    }
    ///////////////////////////////// CargoChefia //////////////////////////
    public void onSaveCargo() {
      Cargochefia c = new Cargochefia();
        if (!(mDialogAddCargo.getTitle().charAt(0) == 'E')) {
            c.setDescricao(txtCargo.getValue());

            if (!csimp.exist(c)) {
                csimp.Save(c);
                Clients.showNotification(" adicionado com sucesso", null, null, null, 2000);
            } else {
                Clients.showNotification(c.getDescricao() + " Ja se encontra cadastrado", Messagebox.EXCLAMATION, null, null, 2000);
                return;
            }
        } else {
            c = csimp.get(Cargochefia.class, caid.getValue());//Depende do tamanho (Se for long convertemos)
            c.setDescricao(txtCargo.getValue());
           // if (!csimp.exist(c)) {
                csimp.update(c);
           //     Clients.showNotification(" adicionado com sucesso", null, null, null, 2000);
//            } else {
//                Clients.alert("Ja se encontra cadastrado");
//
//                return;
//            }
            Clients.showNotification(c.getDescricao() + " modificado com sucesso", null, null, null, 2000);
        }
        lbCargo.setModel(getCargoModel());
        clearFormAddCargo();
        AddCargoConstraint();

        mDialogAddCargo.detach();
    }

    public void onEditCargo(ForwardEvent evt) {
       Button btn = (Button) evt.getOrigin().getTarget();
        Listitem litem = (Listitem) btn.getParent().getParent();//depende da posicao?
        Cargochefia c = (Cargochefia) litem.getValue();
        mDialogAddCargo.setParent(windowCargo);
        mDialogAddCargo.setTitle("Editar Cargo");
        mDialogAddCargo.doModal();
        ((Intbox) mDialogAddCargo.getFellow("caid")).setValue(c.getIdcargochefia().intValue());//(convertemos sempre para int)
        ((Textbox) mDialogAddCargo.getFellow("txtCargo")).setText(c.getDescricao());
    }

    public void onDeleteCargo(ForwardEvent evt) {
        Button btn = (Button) evt.getOrigin().getTarget();
        Listitem litem = (Listitem) btn.getParent().getParent();
        Cargochefia c = (Cargochefia) litem.getValue();
         c = csimp.get(Cargochefia.class, c.getIdcargochefia());
        csimp.delete(c);
        lbCargo.setModel(getCargoModel());
        Clients.showNotification(" apagado com sucesso", null, null, null, 2000);
    }

    public void onCancelCargo() {
        clearFormAddCargo();
        AddCargoConstraint();
        mDialogAddCargo.detach();
    }

    public ListModel<Cargochefia> getCargoModel() {
        List<Cargochefia> lc = csimp.getAll(Cargochefia.class);
        return new ListModelList<Cargochefia>(lc);
    }

    public void onClick$addCargo() {
        mDialogAddCargo.setParent(windowCargo);
        mDialogAddCargo.setTitle("Adicionar Cargo");
        mDialogAddCargo.doModal();
    }

    private void clearFormAddCargo() {
        Constraint c = null;
        txtCargo.setConstraint(c);
        txtCargo.setValue("");
    }

    private void AddCargoConstraint() {
        txtCargo.setConstraint(" no Empty: Insira um Cargo!");
    }
    
//    public void onSComboCargo(){
//       if(cbcargochefia.getSelectedIndex()==cbcargochefia.getModel().getSize()-1){
//           Executions.getCurrent().getServerName();
//          Executions.getCurrent().sendRedirect("/index.zul#h5");
//       }
//    }

}
