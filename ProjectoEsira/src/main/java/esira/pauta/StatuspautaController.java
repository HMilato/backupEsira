package esira.pauta;

import esira.domain.Statuspauta;
import esira.service.CRUDService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.zkoss.lang.Strings;
import org.zkoss.web.servlet.dsp.action.Page;
import org.zkoss.zhtml.Textarea;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.Button;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Constraint;
import org.zkoss.zul.Datebox;
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
import org.zkoss.zul.Window;
import org.zkoss.zul.event.PagingEvent;
import org.zkoss.zul.impl.InputElement;

/**
 *
 * @author UssimaneMuieva
 */
public class StatuspautaController extends GenericForwardComposer {

    @WireVariable
    private final CRUDService csimp = (CRUDService) SpringUtil.getBean("CRUDService");
    private Window windowStatuspauta, formStatuspauta;
    private Paging pagStatuspauta;
    static String ord = " ";
    Map<String, Object> par = new HashMap<String, Object>();
    Map<String, Object> parcond = new HashMap<String, Object>();
    private Listbox lbStatuspauta;
    private Textbox tbdescricao;
    private Intbox ibidstatus;

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        if (lbStatuspauta != null) {
            addeventoLB("from Statuspauta d " + ord, lbStatuspauta, pagStatuspauta, Statuspauta.class);
        }
    }

    public void initLB(String sql, Listbox lb, Paging p, Class o) {
        p.setTotalSize(csimp.count(o));
        final int PAGE_SIZE = p.getPageSize();
        setLBModel(sql, lb, o, 0, PAGE_SIZE);
    }
//

    public void setLBModel(String sql, Listbox lb, Class c, int o, int p) {
        List<Statuspauta> statuspautas = csimp.findByJPQueryFilter(sql, null, o, p);
        lbStatuspauta.setModel(new ListModelList<>(statuspautas));
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

    public void onClick$addStatuspauta() {
        formStatuspauta.setParent(windowStatuspauta);
        formStatuspauta.setTitle("Adicionar Statuspauta");
        formStatuspauta.doModal();
    }

    public void onClick$cancelStatuspauta(Event e) {
        limpar(formStatuspauta);
        formStatuspauta.detach();
    }

    public void onEditarStatuspauta(ForwardEvent evt) throws Exception {
        Button btn = (Button) evt.getOrigin().getTarget();
        Listitem litem = (Listitem) btn.getParent().getParent();
        Statuspauta statuspauta = (Statuspauta) litem.getValue();
        formStatuspauta.setParent(windowStatuspauta);
        formStatuspauta.setTitle("Editar Statuspauta");
        formStatuspauta.doModal();
        ((Intbox) formStatuspauta.getFellow("ibidstatus")).setValue(statuspauta.getIdstatus());
        ((Textbox) formStatuspauta.getFellow("tbdescricao")).setText(statuspauta.getDescricao());
    }

    public void onRemoverStatuspauta(final ForwardEvent evt) throws Exception {

        Messagebox.show("Apagar?", "Prompt", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION,
                new EventListener() {
                    @Override
                    public void onEvent(Event evet) {
                        switch (((Integer) evet.getData()).intValue()) {
                            case Messagebox.YES:
                                Button btn = (Button) evt.getOrigin().getTarget();
                                Listitem litem = (Listitem) btn.getParent().getParent();
                                Statuspauta statuspauta = (Statuspauta) litem.getValue();
                                int ind = ((ListModelList) lbStatuspauta.getModel()).indexOf(statuspauta);
                                if (ind >= 0) {
                                    ((ListModelList) lbStatuspauta.getModel()).remove(ind);
                                }
                                csimp.delete(statuspauta);
                                Clients.showNotification(" apagado com sucesso", null, null, null, 2000);
                                break;
                            case Messagebox.NO:
                                return;
                        }
                    }

                });
    }

    public void onSaveStatuspauta() {
        check(formStatuspauta);
        Statuspauta statuspauta = new Statuspauta();
        if (formStatuspauta.getTitle().charAt(0) == 'E') {
            statuspauta = csimp.get(Statuspauta.class, ibidstatus.getValue().intValue());
        }
        statuspauta.setDescricao(tbdescricao.getValue());
        if (formStatuspauta.getTitle().charAt(0) == 'E') {
            csimp.update(statuspauta);
        } else {
            csimp.Save(statuspauta);
        }
        if (formStatuspauta.getTitle().charAt(0) == 'E') {
            int ind = ((ListModelList) lbStatuspauta.getModel()).indexOf(statuspauta);
            if (ind >= 0) {
                ((ListModelList) lbStatuspauta.getModel()).set(ind, statuspauta);
            }
            Clients.showNotification(" Actualizado com Sucesso", null, null, null, 0);
        } else {
            ListModelList lm = (ListModelList) lbStatuspauta.getModel();
            if (lm != null) {
                lm.add(statuspauta);
            } else {
                lbStatuspauta.setModel(new ListModelList<>());
                ((ListModelList) lbStatuspauta.getModel()).add(statuspauta);
            }
            Clients.showNotification(" Adicionado com Sucesso", null, null, null, 0);
        }
        limpar(formStatuspauta);
        formStatuspauta.detach();
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
}
