/* MainLayoutComposer.java

 {{IS_NOTE
 Purpose:
		
 Description:
		
 History:
 Nov 12, 2008 3:10:06 PM , Created by jumperchen
 }}IS_NOTE 

 Copyright (C) 2008 Potix Corporation. All Rights Reserved.

 {{IS_RIGHT
 This program is distributed under GPL Version 3.0 in the hope that
 it will be useful, but WITHOUT ANY WARRANTY.
 }}IS_RIGHT
 */
package esira.webapp;

import esira.domain.Faculdade;
import esira.domain.Inscricao;
import esira.domain.Inscricaodisciplina;
import esira.domain.Users;
import esira.domain.Utilizadorgeral;
import esira.domain.Validacaopendente;
import esira.domain.ValidacaopendentePK;
import esira.hibernate.Login;
import esira.hibernate.TenantIdResolver;
import esira.service.CRUDService;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.context.SecurityContextHolder;
import org.zkoss.util.logging.Log;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.ComponentNotFoundException;
import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.BookmarkEvent;
import org.zkoss.zk.ui.event.ClientInfoEvent;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.EventQueue;
import org.zkoss.zk.ui.event.EventQueues;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.event.InputEvent;
import org.zkoss.zk.ui.event.KeyEvent;
import org.zkoss.zk.ui.event.SelectEvent;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zkplus.theme.Themes;
import org.zkoss.zul.A;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Div;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Image;
import org.zkoss.zul.Include;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.North;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.West;
import org.zkoss.zul.Window;

/**
 * @author jumperchen
 *
 */
//Para iniciar selecionando-se o um item automaticamente ver doaftercomposer do demo
public class MainLayoutComposer extends GenericForwardComposer implements
        MainLayoutAPI {

    private static final Log log = Log.lookup(MainLayoutComposer.class);
    private EventQueue eq;
    Object o = SecurityContextHolder.getContext().getAuthentication().getAuthorities();
    GrantedAuthorityImpl[] lg = SecurityContextHolder.getContext().getAuthentication().getAuthorities().toArray(new GrantedAuthorityImpl[]{});
    @Autowired
    private HttpSession httpSession;
    @WireVariable
    private final CRUDService csimp = (CRUDService) SpringUtil.getBean("CRUDService");
    Textbox searchBox;
    Listbox itemList;
    Include xcontents;
    Map<String, Object> par = new HashMap<String, Object>();
    Div header;
    Button _selected;
    Listitem _selectedItem;
    // static boolean b = false;
    A sair;
    Label user, bar, cnome;
    Boolean inicio = true;
    Category[] catego = null;
    private boolean isMobile = false;
    Hbox hmenu, categ, autent;
    West west;
    North nort;
    Window wmenu, conta;
    Image mainpic;
    Users u = null;
    private String orientation;
    private Combobox cfac;
    Users us = null;
    //  LinkedCategory catego;
    //  LinkedItem dld;

    public MainLayoutComposer() {
        // initKey();
    }

    private Map getCategoryMap() {
        return AppInit.getCateMap();
    }

    public void onCategorySelect(ForwardEvent event) {
        Button btn = (Button) event.getOrigin().getTarget();
        Listitem item = null;
        if (_selected != btn) {
            _selected = btn;
        } else {
            item = itemList.getSelectedItem();
        }
        String href = getCategory(_selected.getId()).getHref();
        //   Messagebox.show(href);
        if (href != null) {
            Executions.getCurrent().sendRedirect(href);
        } else {
            itemList.setModel(getSelectedModel());
            itemList.renderAll();
            item = ((Listitem) itemList.getFirstChild());
            //if (b) {
            item.setSelected(true);
            //}
            itemList.invalidate();
            setSelectedCategory(item);
            xcontents.setSrc(((Item) item.getValue()).getFile());
            item.focus();
        }
    }

    public void onBookmarkChange$main(BookmarkEvent event) {
        String id = event.getBookmark();
        if (id.length() > 0) {
            final Item[] items = getItems();
            for (int i = 0; i < items.length; i++) {
                if (items[i].getId().equals(id)) {// && isUserInRole(items[i].getId())) {
                    //url                                Messagebox.show("selec");
                    _selected = (Button) self.getFellow(items[i].getCateId());
                    itemList.setModel(getSelectedModel());
                    itemList.renderAll();
                    Listitem item = ((Listitem) itemList.getFellow(id));
                    item.setSelected(true);
                    itemList.invalidate();
                    setSelectedCategory(item);
                    xcontents.setSrc(((Item) item.getValue()).getFile());
                    item.focus();
                    return;
                }
            }
        }
    }

    public void onSelect$itemList(SelectEvent event) {
        Listitem item = itemList.getSelectedItem();
        _selectedItem = item;
        if (item != null) {
            // sometimes the item is unloaded.
            if (!item.isLoaded()) {
                itemList.renderItem(item);
            }
            //itemsel    
            // Executions
            setSelectedCategory(item);
            xcontents.setSrc(((Item) item.getValue()).getFile());
        }
    }

    public void onMainCreate(Event event) {
        //    b = Executions.getCurrent().getBrowser("mobile") != null;
        final Execution exec = Executions.getCurrent();
        final String id = exec.getParameter("id");
        Listitem item = null;
        if (id != null) {
            try {
                final LinkedList list = new LinkedList();
                final Item[] items = getItems();
                for (int i = 0; i < items.length; i++) {
                    if (items[i].getId().equals(id)) {
                        list.add(items[i]);
                    }
                }
                if (!list.isEmpty()) {
                    itemList.setModel(new ListModelList(list, true));
                    itemList.renderAll();
                    item = (Listitem) self.getFellow(id);
                    //setSelectedCategory(item);
                    Listitem li = new Listitem();
                    li.setValue(items[0]);
                    setSelectedCategory(li);
//                    if (b) {
//                        item.setSelected(true);
//                    }
                    itemList.invalidate();
                }
            } catch (ComponentNotFoundException ex) { // ignore
            }
        }

        if (item == null) {
            item = (Listitem) self.getFellow(getItems()[0].getId());
            setSelectedCategory(item);
            item.setSelected(true);
            itemList.invalidate();
        }
        xcontents.setSrc(((Item) item.getValue()).getFile());

        itemList.selectItem(item);
        inicio = false;
        /// Messagebox.show((String)Executions.getCurrent().getDesktop().getSession().getAttribute("ussimane"));
    }

    private void setSelectedCategory(Listitem item) {
        Item di = (Item) item.getValue();
        _selected = (Button) self.getFellow(di.getCateId());
        String deselect = _selected != null ? "jq('#" + _selected.getUuid()
                + "').addClass('blue').siblings().removeClass('blue');" : "";
        // _selected.setClass("button-red");
        Clients.evalJavaScript(deselect);
        item.getDesktop().setBookmark(item.getId());
        if (!u.getUestudante()) {
            final Iterator<Listitem> items = new ArrayList(itemList.getItems()).listIterator();
            Listitem li;
            while (items.hasNext()) {
                li = items.next();
                if (((Item) li.getValue()).getLabel().contains("Pedido")) {
                    ValidacaopendentePK vpk = new ValidacaopendentePK(us.getFaculdade().getIdFaculdade(), ((Item) li.getValue()).getId());
                    Validacaopendente vp = csimp.get(Validacaopendente.class, vpk);
                    if (vp != null) {
                        if (vp.getQtd() > 0) {
                            Listcell lc = (Listcell) li.getChildren().get(0);
                            lc.setLabel(((Item) li.getValue()).getLabel() + " (" + vp.getQtd() + ")");
                            lc.setStyle("font-weight: bold");
                        } else {
                            Listcell lc = (Listcell) li.getChildren().get(0);
                            lc.setLabel(((Item) li.getValue()).getLabel());
                            lc.setStyle("font-weight: none");
                        }
                    }
                }
            }
        }
    }

    public void onCtrlKey$searchBox(KeyEvent event) {
        int keyCode = event.getKeyCode();
        List items = itemList.getItems();
        if (items.isEmpty()) {
            return;
        }
        Listitem item = null;
        switch (keyCode) {
            case 38: // UP
                item = itemList.getItemAtIndex(items.size() - 1);
                itemList.setSelectedItem(item);
                break;
            case 40: // DOWN
                item = itemList.getItemAtIndex(0);
                itemList.setSelectedItem(item);
                break;
        }
        if (item != null) {
            if (!item.isLoaded()) {
                itemList.renderItem(item);
            }
            setSelectedCategory(item);
            xcontents.setSrc(((Item) item.getValue()).getFile());
            item.focus();
        }
    }

    public void onChanging$searchBox(InputEvent event) {
        String key = event.getValue();
        LinkedList item = new LinkedList();
        Item[] items = getItems();

        if (key.trim().length() != 0) {
            for (int i = 0; i < items.length; i++) {
                if (items[i].getLabel().toLowerCase()
                        .indexOf(key.toLowerCase()) != -1) {
                    item.add(items[i]);
                }
            }
            itemList.setModel(new ListModelList(item, true));
        } else {
            itemList.setModel(new ListModelList(items));
        }
        _selected = null;
    }

    private Item[] getItems() {
        LinkedList items = new LinkedList();
        List<Item> litem = new ArrayList<Item>();
        Category[] categories = getCategories();
        for (int i = 0; i < categories.length; i++) {
            items.addAll(categories[i].getItems());
        }
        Item[] it = (Item[]) items.toArray(new Item[]{});
        for (int i = 0; i < it.length; i++) {
            /// if (isUserInRole(it[i].getId())) {
            litem.add(it[i]);
            /////  }
        }
        return litem.toArray(new Item[]{});
    }

    public Category[] getAllCategories() {
        //Category c[]
        if (catego == null) {
            catego = (Category[]) getCategoryMap().values()
                    .toArray(new Category[]{});
        }
        return catego;
    }

    @Override
    public Category[] getCategories() {
//        if (!inicio) {
//            return catego;
//        }
        List<Category> k = new ArrayList<Category>();
        Category c[] = getAllCategories();//(Category[]) getCategoryMap().values().toArray(new Category[]{});
        for (int i = 0; i < c.length; i++) {
            if (isUserInRole(c[i].getId())) {
                Category ca = new Category(c[i].getId(), c[i].getIcon(), c[i].getLabel(), c[i].getHref());
                k.add(ca);
                final Iterator<Object> iterat = new ArrayList(c[i].getItems()).listIterator();
                Object ite = null;
                while (iterat.hasNext()) {
                    ite = iterat.next();
                    if (isUserInRole(((Item) ite).getId())) {
                        // c[i].getItems().remove(ite);
                        ca.getItems().add(ite);
                    }
                }
            }
        }
//        catego = k.toArray(new Category[]{});
//        return catego;
        return k.toArray(new Category[]{});
    }

    @Override
    public ListitemRenderer getItemRenderer() {
        return _defRend;
    }
    private static final ListitemRenderer _defRend = new ItemRender();

    private static class ItemRender implements ListitemRenderer, java.io.Serializable {

        @Override
        public void render(Listitem item, Object data, int index) {
            Item di = (Item) data;
            Listcell lc = new Listcell();
            item.setValue(di);
            lc.setHeight("30px");
            lc.setImage(di.getIcon());
            item.setId(di.getId());
            lc.setLabel(di.getLabel());
            lc.setParent(item);
        }
    };

    private Category getCategory(String cateId) {
        //return (Category) getCategoryMap().get(cateId);
        Category[] c = getCategories();
        for (Category ca : c) {
            if (ca.getId().equals(cateId)) {
                return ca;
            }
        }
        return null;
    }

    @Override
    public ListModel getSelectedModel() {
        Category cate = _selected == null ? getCategories()[0]
                : getCategory(_selected.getId());
        List<Item> it = cate.getItems();
        List<Item> litem = new ArrayList<Item>();
        for (int i = 0; i < it.size(); i++) {
            //if (isUserInRole(it.get(i).getId())) {
            litem.add(it.get(i));
            //}
        }
        return new ListModelList(litem, true);
    }

    public boolean isUserInRole(String s) {
        for (int j = 0; j < lg.length; j++) {
            if (s.equalsIgnoreCase(lg[j].getAuthority())) {
                return true;
            }
        }
        return false;
    }

    public void toFloatingMenu() {
        // private boolean isMobile = false;
        hmenu.setVisible(true);
        categ.setVisible(false);
        autent.setVisible(false);
        west.setWidth("0px");
        wmenu.setVisible(true);
        mainpic.setHeight("10px");
        nort.setSize("20px");
    }

    public void toFixedMenu() {
//        // private boolean isMobile = false;
//        hmenu.setVisible(false);
//        categ.setVisible(true);
//        autent.setVisible(true);
//        west.setSize("200px");
//        wmenu.setVisible(false);
    }

    public void onAbrirMenu() {
        //   wmenu.setVisible(!wmenu.isVisible());
    }

//    public void onClientInfo$borderlayout(ClientInfoEvent evt) {
//        if (isMobile) {
//            if (!evt.getOrientation().equals(orientation)) {
//                orientation = evt.getOrientation();
//                if (orientation.equals("protrait") || evt.getDesktopWidth() < 800) {
//                    toFloatingMenu();
//                } else {
//                    toFixedMenu();
//                }
//            }
//        }
//    }
    // Composer Implementation
    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        org.zkoss.zul.Include i = new org.zkoss.zul.Include();
        Events.postEvent("onMainCreate", comp, null);
//        isMobile = Executions.getCurrent().getBrowser("mobile") !=null;
//                if (isMobile){
//                        toFloatingMenu();
//                }
        
        String usr = SecurityContextHolder.getContext().getAuthentication().getName();
        user.setValue(usr);
        Utilizadorgeral ut = csimp.get(Utilizadorgeral.class, usr);
        //  TenantIdResolver.setTenant(ut.getTenant());
        System.out.println("novo tenant: " + ut.getTenant());
        u = csimp.get(Users.class, usr);
        us = new Users(u.getUtilizador());
//        Login.setTenantId(u.getTenant());
//       if(httpSession!=null) httpSession.setAttribute("tenant",u.getTenant());
        ((Label) conta.getFellow("unome")).setValue(u.getNome());
        us.setUestudante(u.getUestudante());
        us.setTenant(u.getTenant());
//        us.setFaculdade(u.getFaculdade());
//        Sessions.getCurrent().setAttribute("user", us);
        if (u.getFaculdade() == null) {
            List<Faculdade> lf = csimp.getAll(Faculdade.class);
            Faculdade fac = lf.get(0);
            if (lf != null && fac != null) {
                fac.setLocalizacao(null);
             //   if (Sessions.getCurrent().getAttribute("user") == null) {
                    us.setFaculdade(fac);
                    Sessions.getCurrent().setAttribute("user", us);
//                } else {
//                    us = (Users) Sessions.getCurrent().getAttribute("user");
//                }
            }
        } else {
            us.setFaculdade(u.getFaculdade());
            Sessions.getCurrent().setAttribute("user", us);
        }

        //     Messagebox.show(u.getFaculdade().getIdFaculdade()+"");
        Date datactual = new Date();
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(datactual);
        int anoActual = calendar.get(Calendar.YEAR);
        bar.setValue("Todos Direitos Reservados © " + anoActual);
        //   Themes.setPriority("sapphire", 0);
    }

    public void onSetQueueValid() {
        if (!us.getUestudante() && us.getFaculdade() != null) {
            eq = EventQueues.lookup("valid" + us.getFaculdade().getIdFaculdade(), EventQueues.APPLICATION, true);
            eq.subscribe(getEventoValid());
        }
    }

    public EventListener getEventoValid() {
        return new EventListener() {
            @Override
            public void onEvent(Event event) throws Exception {
                Validacaopendente vp = (Validacaopendente) event.getData();
                final Iterator<Listitem> items = new ArrayList(itemList.getItems()).listIterator();
                Listitem li;
                while (items.hasNext()) {
                    li = items.next();
                    if (li.getId().equals(vp.getValidacaopendentePK().getTipo())) {
                        //   if (vp != null) {
                        if (vp.getQtd() > 0) {
                            Listcell lc = (Listcell) li.getChildren().get(0);
                            lc.setLabel(((Item) li.getValue()).getLabel() + " (" + vp.getQtd() + ")");
                            lc.setStyle("font-weight: bold");
                        } else {
                            Listcell lc = (Listcell) li.getChildren().get(0);
                            lc.setLabel(((Item) li.getValue()).getLabel());
                            lc.setStyle("font-weight: none");
                        }
                        //      }
                    }
                }
            }
        };
    }

}
