/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package esira.controller;

//import facade.UsersJpaController;
//import facade.exceptions.NonexistentEntityException;
//import facade.exceptions.RollbackFailureException;
import esira.dao.ConnectionFactory;
import esira.domain.Faculdade;
import esira.domain.Grupo;
import esira.domain.Item;
import esira.domain.Roles;
import esira.domain.Users;
import esira.service.CRUDService;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.authentication.CachingUserDetailsService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.EventQueues;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

;

/**
 *
 * @author user
 */
public class LoginController extends SelectorComposer<Component> {

    private final CRUDService csimp = (CRUDService) SpringUtil.getBean("CRUDService");

    private static final long serialVersionUID = 1L;
    //wire components
    @Wire
    Textbox account;
    @Wire
    Textbox password;
    @Wire
    Label message;
    @Wire
    Textbox senhaA;
    @Wire
    Textbox senhaN;
    @Wire
    Textbox senhaC;
    @Wire
    Label msg;
    @Wire
    Window conta;
    @Wire
    Button bfconta;
    @Wire
    Combobox cbgrupo;
    @Wire
    Label user;
    @Wire
    Combobox cfac;

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);

    }

    @Listen("onClick=#aceitar; onOK=#senhaWin")
    public void doSenha() {
        String sa = senhaA.getValue();
        String sn = senhaN.getValue();
        String sc = senhaC.getValue();
        String acc = SecurityContextHolder.getContext().getAuthentication().getName();
        Users user = new Users();
        Users u = null;

        List<Users> lu = csimp.getAll(Users.class);
        for (int i = 0; i < lu.size(); i++) {
            if (lu.get(i).getUtilizador().compareTo(acc) == 0 && lu.get(i).getPasword().compareTo(sa) == 0) {
                u = lu.get(i);
            }
        }
        if (u == null) {
            msg.setValue("Por favor, a senha anterior deve ser valida.");
            return;
        } else {
            if (sn.compareTo(sc) == 0) {
                /*  user.setPasword(u.getPassword());
                 user.setUtilizador(u.getAccount());
                 new UsersJpaController().edit(user);*/
                //  new UserDao().delete(u);
                //csimp.delete(u);
                u.setPasword(sc);
                // new UserDao().insert(u);
                csimp.update(u);
                //    new UserInfoServiceImpl().updateUser(u);
                Executions.getCurrent().getSession().invalidate();
                Executions.getCurrent().sendRedirect("/");
            } else {
                msg.setValue("Por favor, a nova senha deve ser confirmada.");
                return;
            }
        }
    }

    @Listen("onClick=#cancelar")
    public void doCancel() {
        Executions.sendRedirect("/");
    }

    @Listen("onClick=#logout")
    public void doLogout() {
        Session sess = Sessions.getCurrent();
        sess.removeAttribute("userCredential");
        Sessions.getCurrent().invalidate();
        Executions.sendRedirect("/");
    }

    @Listen("onClick=#user")
    public void doMinhaConta() {
        conta.setVisible(!conta.isVisible());
        if (conta.isVisible()) {
            conta.setTop("20px");
        }
        if (((Combobox) conta.getFellow("cbgrupo")).getItemCount() == 0) {
            Users u = csimp.get(Users.class, user.getValue());
            List<Grupo> lg = u.getGrupoList();
            String g = u.getIdGrupo().getIdGrupo();
            for (int i = 0; i < lg.size(); i++) {
                if (lg.get(i).getIdGrupo().equals(g)) {
                    lg.remove(i);
                }
            }
            lg.add(0, u.getIdGrupo());
            ((Combobox) conta.getFellow("cbgrupo")).setModel(new ListModelList(new ArrayList<Grupo>(lg)));

        }
    }

    @Listen("onClick=#bfconta")
    public void doFecharConta() {
        conta.setVisible(false);
    }

    @Listen("onSelect=#cbgrupo")
    public void doMudarPermissao() {
        //List<GrantedAuthority> authList = new ArrayList<GrantedAuthority>();
        Grupo g = (Grupo) cbgrupo.getSelectedItem().getValue();
        g = csimp.get(Grupo.class, g.getIdGrupo());
        String usr = SecurityContextHolder.getContext().getAuthentication().getName();
        Users u = csimp.get(Users.class, usr);
        u.setIdGrupo(g);
        csimp.update(u);
//        List<Roles> lr = g.getRolesList();
//        for (Roles r : lr) {
//            authList.add(new GrantedAuthorityImpl(r.getItem().getCategoria().getIdCategoria()));
//        }
//        Iterator<Roles> items = new ArrayList(g.getRolesList()).listIterator();
//        Roles r = null;
//        while (items.hasNext()) {
//            authList.add(new GrantedAuthorityImpl(r.getItem().getCategoria().getIdCategoria()));
//            Iterator<Item> items2 = new ArrayList(r.getItem().getCategoria().getItemList()).listIterator();
//            Item it;
//            while (items2.hasNext()) {
//                it = items2.next();
//                authList.add(new GrantedAuthorityImpl(it.getItemPK().getItem()));
//            }
//        }

//        Authentication a = new PreAuthenticatedAuthenticationToken(SecurityContextHolder.getContext().getAuthentication().getPrincipal(),
//                SecurityContextHolder.getContext().getAuthentication().getCredentials(),authList) {
//                    @Override
//                    public String getName() {
//                        return super.getName(); //To change body of generated methods, choose Tools | Templates.
//                    }
//
//                };
//        SecurityContextHolder.getContext().setAuthentication(a);
        Session sess = Sessions.getCurrent();
        sess.removeAttribute("userCredential");
        Sessions.getCurrent().invalidate();
        Executions.sendRedirect("/");
    }

    @Listen("onAfterRender=#cfac")
    public void onSFpermissao() {
        if (cfac.getModel() != null && cfac.getModel().getSize() > 0) {
            Object o = Sessions.getCurrent().getAttribute("indexf");
            if (o != null) {
                cfac.setSelectedIndex((int) o);
            } else {
                cfac.setSelectedIndex(0);
            }
        }
    }

    @Listen("onSelect=#cfac")
    public void onSCfac() {
        if (cfac.getSelectedItem() != null) {
            Sessions.getCurrent().setAttribute("indexf", cfac.getSelectedIndex());
            Users usr = (Users) Sessions.getCurrent().getAttribute("user");
            usr.setFaculdade((Faculdade)cfac.getSelectedItem().getValue());
           // String usr = SecurityContextHolder.getContext().getAuthentication().getName();
            user.setValue(usr.getUtilizador());// + " - " + ((Faculdade) cfac.getSelectedItem().getValue()).getAbreviatura());
            Executions.getCurrent().sendRedirect("/index.zul#" + ((Listbox) conta.getParent().getParent().getParent().getParent().getFellow("itemList")).getSelectedItem().getId());//_selected.getId());
            // Messagebox.show(((Listbox)conta.getParent().getParent().getParent().getParent().getFellow("itemList")).getSelectedItem().getId());
////////            Listbox lb = ((Listbox)conta.getParent().getParent().getParent().getParent().getFellow("itemList"));
////////            Listitem li = lb.getSelectedItem();//.getId();
////////            lb.setSelectedItem(lb.getItems().get(0));
////////            lb.invalidate();
////////            lb.setSelectedItem(li);
////           // xcontents.setSrc(((esira.webapp.Item) item.getValue()).getFile());
            // .setselec.getSelectedItem().getId());
        }
    }

}
