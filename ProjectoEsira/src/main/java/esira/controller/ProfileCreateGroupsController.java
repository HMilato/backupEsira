/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package esira.controller;

import esira.domain.Categoria;
import esira.domain.Grupo;
import esira.domain.Roles;
import esira.domain.RolesPK;
import esira.domain.Users;
import esira.service.CRUDService;
import esira.webapp.Category;
import esira.webapp.Item;
import esira.webapp.MainLayoutComposer;
import java.sql.CallableStatement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import javax.management.relation.Role;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.Button;
import org.zkoss.zul.Constraint;
import org.zkoss.zul.DefaultTreeModel;
import org.zkoss.zul.DefaultTreeNode;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Tree;
import org.zkoss.zul.TreeModel;
import org.zkoss.zul.TreeNode;
import org.zkoss.zul.Treeitem;
import org.zkoss.zul.Window;

/**
 *
 * @author user
 */
public class ProfileCreateGroupsController extends GenericForwardComposer {

    @WireVariable
    private final CRUDService csimp = (CRUDService) SpringUtil.getBean("CRUDService");

    private static final long serialVersionUID = 43014628867656917L;
    private Tree tree;
    @WireVariable
    private Textbox txtGrupo, txtDesc;
    private Listbox lbGrupo, lb;
    private Window mDialogAddGroup, windowGroup;

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
    }

    public ListModel<Grupo> getGrupoModel() {
       List<Grupo> grupos = csimp.getAll(Grupo.class);
       return new ListModelList<Grupo>(grupos);
    }

//    A simpler answer:
//    public void expandTree(Boolean expander) {
//        Collection itm = tree.getItems();
//        if (itm != null) {
//            for (Object item  : itm) {
//                if (item instanceof Treeitem) {
//                    Treeitem treeitem = (Treeitem) item;
//                    treeitem.setOpen(expander);
//                }
//            }
//        }
//    }
    @Listen("onSelect=#tree")
    public void displayCars() {
//        Treeitem item = tree.getSelectedItem();
//        
//        item.isCheckable();
//        
//        Treeitem selectedTreeItem = tree.getSelectedItem();
////        
//        DefaultTreeNode selectedTreeNode = (DefaultTreeNode) selectedTreeItem.getValue();
////        
//        if(((PackageDataItem)selectedTreeNode.getData()).getId().equals("-1")){
////            Set<Treeitem> items = tree.getItems();
//        
//        }
////        selectedTreeNode.getData()
//        Clients.showNotification(((PackageDataItem)selectedTreeNode.getData()).getId() + ((PackageDataItem)selectedTreeNode.getData()).getIdItem());
//        treeModel.isMultiple();
    }

    public void onClick$addGroup() {
        mDialogAddGroup.setParent(windowGroup);
        mDialogAddGroup.setTitle("Adicionar Grupo");
        mDialogAddGroup.doModal();
        ((Textbox) mDialogAddGroup.getFellow("txtGrupo")).setReadonly(false);
    }

    public void onClick$cancelGroup() {
//        mDialogAddGroup.setVisible(false);
        clearFormAddGroup();
        addGroupConstraint();
        mDialogAddGroup.detach();
        ((Listbox) mDialogAddGroup.getFellow("lb")).clearSelection();
    }

    public void onClick$saveGroup() {
//         Retorna os items seleccionados na arvore
        //  Set<Treeitem> item = tree.getSelectedItems(); 
        Set<Listitem> l = lb.getSelectedItems();

        if (lb.getSelectedItems().isEmpty()) {
            Clients.showNotification("Por favor seleccione pelo menos uma ac√ß√£o!", "warning", null, null, 3000);
            return;
        }

        List<Roles> li = new ArrayList<Roles>();
        esira.domain.Item it = new esira.domain.Item();
        String id = "";
        for (Listitem litem : l) {
//            if (id.compareToIgnoreCase(((Item) litem.getValue()).getCateId()) != 0) {
//                id = ((Item) litem.getValue()).getCateId();
//                Nodo nodo = new Nodo(((Item) litem.getValue()).getCateId(), "-1");
//                itens.add(nodo);
//                nodo = new Nodo(((Item) litem.getValue()).getCateId(), ((Item) litem.getValue()).getId());
//                itens.add(nodo);
//            } else {
//                Nodo nodo = new Nodo(((Item) litem.getValue()).getCateId(), ((Item) litem.getValue()).getId());
//                itens.add(nodo);
//            }
            li.add(new Roles(txtGrupo.getValue(), ((Item) litem.getValue()).getCateId(), ((Item) litem.getValue()).getId()));
        }

//
//        boolean ja = false;
//        String id = "";
//        for (Treeitem treeitem : item) {
//
//            DefaultTreeNode selectedTreeNode = (DefaultTreeNode) treeitem.getValue();
//            if (id.compareTo(((PackageData) selectedTreeNode.getData()).getId()) != 0) {
//                ja = false;
//            }
////            descobre se o item eh uma categoria ou nao e guarda no lista de nodo
//            if (selectedTreeNode.getData() instanceof PackageDataItem) {
//                if (ja = false) {
//                    Messagebox.show(((PackageData) selectedTreeNode.getData()).getId() + "  Segundo");
//                    Nodo nodo = new Nodo(((PackageData) selectedTreeNode.getData()).getId(), "-1");
//                    itens.add(nodo);
//                    ja = true;
//                }
//                Messagebox.show(((PackageData) selectedTreeNode.getData()).getId() + "  Primeiro");
//                Nodo nodo = new Nodo(((PackageData) selectedTreeNode.getData()).getId(), ((PackageDataItem) selectedTreeNode.getData()).getIdItem());
//                itens.add(nodo);
//
//            } else {
//                ja = true;
//                Messagebox.show(((PackageData) selectedTreeNode.getData()).getId() + "  Segundo");
//                Nodo nodo = new Nodo(((PackageData) selectedTreeNode.getData()).getId(), "-1");
//                itens.add(nodo);
//            }
//            id = ((PackageData) selectedTreeNode.getData()).getId();
//
//        }
        if (mDialogAddGroup.getTitle().compareTo("Adicionar Grupo") == 0) {
            Grupo g = new Grupo();
            g.setIdGrupo(txtGrupo.getValue());
            g.setDescricao(txtDesc.getValue());
            g.setRolesList(li);
            if (!csimp.exist(g)) {
                csimp.Save(g);
                ((ListModelList) lbGrupo.getModel()).add(g);
            }
            Clients.showNotification("Grupo " + g.getDescricao() + " Salvado com sucesso", null, null, null, 2000);
            clearFormAddGroup();
            lb.clearSelection();
            mDialogAddGroup.setVisible(false);
            txtGrupo.setConstraint(" no Empty: Insira o Nome do grupo!");
            txtDesc.setConstraint(" no Empty: Insira a Descri√ß√£o do grupo!");
        } else {
            final List<Roles> li2 = new ArrayList<Roles>();
            li2.addAll(li);
            Messagebox.show("Modificar?", "ConfirmaÁ„o", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION,
                    new EventListener() {
                        @Override
                        public void onEvent(Event evet) {
                            switch (((Integer) evet.getData()).intValue()) {
                                case Messagebox.YES:
                                    Grupo g = csimp.GetUniqueEntityByNamedQuery("Grupo.findByIdGrupo", txtGrupo.getValue());
                                    int index = ((ListModelList) lbGrupo.getModel()).indexOf(g);
                                    //g.setIdGrupo(txtGrupo.getValue());
                                    g.setDescricao(txtDesc.getValue());
                                    Iterator<Roles> items = new ArrayList(g.getRolesList()).listIterator();
                                    Roles r;
                                    while (items.hasNext()) {
                                        r = items.next();
                                        g.getRolesList().remove(r);
                                        csimp.delete(csimp.get(Roles.class, r.getRolesPK()));
                                    }
                                    g.setRolesList(li2);
                                    csimp.update(g);
                                    ((ListModelList) lbGrupo.getModel()).set(index, g);
                                    Clients.showNotification("Grupo " + g.getDescricao() + " Modificado com sucesso", null, null, null, 2000);
                                    clearFormAddGroup();
                                    lb.clearSelection();
                                    mDialogAddGroup.setVisible(false);
                                    txtGrupo.setConstraint(" no Empty: Insira o Nome do grupo!");
                                    txtDesc.setConstraint(" no Empty: Insira a Descri√ß√£o do grupo!");
                                case Messagebox.NO:
                                    return;
                            }
                        }

                    });
        }
    }

    public void onEdit(ForwardEvent evt) throws Exception {
        Button btn = (Button) evt.getOrigin().getTarget();
        Listitem litem = (Listitem) btn.getParent().getParent();
        Grupo grupo = (Grupo) litem.getValue();
        grupo = csimp.load(Grupo.class, grupo.getIdGrupo());
        mDialogAddGroup.setParent(windowGroup);
        mDialogAddGroup.setTitle("Editar Grupo");
        mDialogAddGroup.doModal();
        ((Textbox) mDialogAddGroup.getFellow("txtGrupo")).setText(grupo.getIdGrupo());
        ((Textbox) mDialogAddGroup.getFellow("txtGrupo")).setReadonly(true);
        ((Textbox) mDialogAddGroup.getFellow("txtDesc")).setText(grupo.getDescricao());
        Listbox lbo = (Listbox) mDialogAddGroup.getFellow("lb");
        int size = lbo.getItems().size();
        Set<Listitem> sli = new HashSet<Listitem>();
        List<Roles> r = csimp.getAll(Roles.class);
        for (int i = 0; i < size; i++) {
            Listitem lit = (Listitem) lbo.getItems().get(i);
            for (Roles it : grupo.getRolesList()) {
                if (((Item) lit.getValue()).getId().compareTo(it.getRolesPK().getIdItem()) == 0) {
                    sli.add(lit);
                }
            }
        }
        lbo.setSelectedItems(sli);
    }

    public void onDelete(final ForwardEvent evt) throws Exception {
        Messagebox.show("Apagar?", "Prompt", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION,
                new EventListener() {
                    @Override
                    public void onEvent(Event evet) {
                        switch (((Integer) evet.getData()).intValue()) {
                            case Messagebox.YES:
                                Button btn = (Button) evt.getOrigin().getTarget();
                                Listitem litem = (Listitem) btn.getParent().getParent();

                                Grupo grupo = (Grupo) litem.getValue();
                                ((ListModelList) lbGrupo.getModel()).remove(grupo);
                                new Listbox().appendChild(litem);
                                csimp.delete(grupo);
                                Clients.showNotification("Grupo " + grupo.getDescricao() + " apagado com sucesso", null, null, null, 2000);
                            case Messagebox.NO:
                                return;
                        }
                    }

                });
    }

    public ListModelList getListaitemModel() {
        Category[] categories = new MainLayoutComposer().getAllCategories();
        LinkedList items = new LinkedList();
        for (int i = 0; i < categories.length; i++) {
            items.addAll(categories[i].getItems());
        }
        Item[] it;
        it = (Item[]) items.toArray(new Item[]{});
        List<Item> listadeitem = new ArrayList<Item>();
        for (int i = 0; i < it.length; i++) {
            listadeitem.add(new Item(it[i].getId(), it[i].getCateId(), it[i].getFile(), it[i].getIcon(), it[i].getLabel()));
        }
        ListModelList<Item> ite;
        ite = new ListModelList<Item>(listadeitem);
        return ite;
    }

    private void clearFormAddGroup() {
        Constraint c = null;
        txtGrupo.setConstraint(c);
        txtDesc.setConstraint(c);
        txtGrupo.setValue("");
        txtDesc.setValue("");
    }

    private void addGroupConstraint() {
        txtGrupo.setConstraint(" no Empty: Insira o Nome do grupo!");
        txtDesc.setConstraint(" no Empty: Insira a DescriÁ„o do grupo!");
    }

    public void onError$txtDesc() {
        Clients.scrollIntoView(txtDesc);
    }

}
