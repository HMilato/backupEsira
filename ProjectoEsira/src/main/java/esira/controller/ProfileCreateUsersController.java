/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package esira.controller;

//import esira.controller.authentication.Grupo;
//import esira.controller.authentication.GrupoRole;
import ExcelExport.BeanToExcel;
import com.itextpdf.text.Document;
import esira.domain.Arquivoestudante;
import esira.domain.Arquivoinscricao;
import esira.domain.Arquivomatricula;
import esira.domain.Curso;
import esira.domain.Disciplina;
import esira.domain.Docente;
import esira.domain.Documento;
import esira.domain.Endereco;
import esira.domain.Enderecof;
import esira.domain.Estudante;
import esira.domain.Faculdade;
import esira.domain.Funcionario;
import esira.domain.Grupo;
import esira.domain.Inscricao;
import esira.domain.Matricula;
import esira.domain.Pais;
import esira.domain.Planocurricular;
import esira.domain.Provincia;
import esira.domain.Users;
import esira.domain.Roles;
import esira.domain.Users;
import esira.domain.Utilizadorgeral;
import esira.matricula.GuardarEstudanteController;
import esira.service.CRUDService;
import esira.service.UserAutentic;
import esira.webapp.Item;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.FilenameUtils;
import org.zkoss.util.media.AMedia;
import org.zkoss.util.media.Media;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Sessions;
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
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Radio;
import org.zkoss.zul.Row;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

/**
 *
 * @author user
 */
public class ProfileCreateUsersController extends GenericForwardComposer {

    @WireVariable
    private final CRUDService csimp = (CRUDService) SpringUtil.getBean("CRUDService");

    private static final long serialVersionUID = 43014628867656917L;
    private Combobox cbgrupos, cbfunc, cbest, cbfac;
    private Listbox lbUser, lbgrupo;
    private Intbox tbanoing, ibturma;
    private Label validation, pwd_val;
    private Textbox txtAccount, txtEmail, txtPass, txtPassConfirm, lid, txtnome, txtapelido, txtnrmec, oldnr;
    private Radio rf, re, ne;
    Window mDialogAddUser, windowUser;
    Map<String, Object> par = new HashMap<String, Object>();
    String condfac = "", condnr = "", condgrupo = "", condnome = "", condgenero = "", condanoi = "", condano = "", condcurso = "";
    Textbox txProcurar;
    Combobox cbcurso, cbfaculdade, cbgrupop, cbcurso2, cbplanoU, cbTurnoU;
    private Button btv;
    Map<String, Object> condpar = new HashMap<String, Object>();
    Users usr = (Users) Sessions.getCurrent().getAttribute("user");
    private Intbox poscf, posce;
    private String pesqf = null, pesqe = null, condnf = "", condne = "";
    Map<String, Object> cbparf = new HashMap<String, Object>();
    Map<String, Object> cbpare = new HashMap<String, Object>();
    private int indcf = -1, indce = -1;
    // Users u = null;

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        condpar.clear();
        // condfac = " and u.faculdade = :fac ";
        condfac = " and u.faculdade is null";
        //u = csimp.get(Users.class, usr.getUtilizador());
//        Faculdade f = csimp.get(Faculdade.class, usr.getFaculdade().getIdFaculdade());
//        condpar.put("fac", f);
        setLB(0, 20);
        if (posce != null) {
            posce.setValue(0);
            poscf.setValue(0);
        }
    }

    public ListModel<Curso> getCursoModel() {
        List<Curso> lc = csimp.getAll(Curso.class);
        return new ListModelList<Curso>(lc);
    }

    public ListModel<Curso> getCursocModel() {
        List<Curso> lc = csimp.getAll(Curso.class);
        return new ListModelList<Curso>(lc);
    }

//    public ListModel<Users> getUserModel() {
//        return userModel;
//    }
    public ListModel<Faculdade> getFacModel() {
        List<Faculdade> lf = lf = new ArrayList<Faculdade>();
        Faculdade f = new Faculdade();
        f.setDesricao("-----Todas Faculdades----");
        lf.add(f);
        lf.addAll(csimp.getAll(Faculdade.class));
        return new ListModelList<Faculdade>(lf);
    }

    public ListModelList<Grupo> getGrupoModel() {
        return new ListModelList<Grupo>(csimp.getAll(Grupo.class));
    }

    public ListModelList<Grupo> getGrupoPModel() {
        List<Grupo> lg = csimp.getAll(Grupo.class);
        Grupo g = new Grupo();
        g.setDescricao("------ Todos Grupos ------");
        lg.add(0, g);
        return new ListModelList<Grupo>(lg);
    }

    public void onAddUser() {
        mDialogAddUser.setParent(windowUser);
        mDialogAddUser.setTitle("Adicionar Utilizador");
        ((Row) mDialogAddUser.getFellow("row1user")).setVisible(true);
        ((Row) mDialogAddUser.getFellow("f1")).setVisible(false);
        ((Row) mDialogAddUser.getFellow("f2")).setVisible(false);
        ((Row) mDialogAddUser.getFellow("f3")).setVisible(false);
        ((Row) mDialogAddUser.getFellow("f4")).setVisible(false);
        ((Row) mDialogAddUser.getFellow("f5")).setVisible(false);
        ((Row) mDialogAddUser.getFellow("f6")).setVisible(false);
        ((Row) mDialogAddUser.getFellow("f7")).setVisible(false);
        ((Row) mDialogAddUser.getFellow("f8")).setVisible(false);
        ((Row) mDialogAddUser.getFellow("f9")).setVisible(false);
        Listbox lb = (Listbox) mDialogAddUser.getFellow("lbgrupo");
        lb.setModel(new ListModelList(new ArrayList<Grupo>()));
        ((Combobox) mDialogAddUser.getFellow("cbgrupos")).setValue("------------ Grupo -----------");
        ((Combobox) mDialogAddUser.getFellow("cbfac")).setValue("------------ Faculdade -----------");
        Curso c = (Curso) ((Combobox) mDialogAddUser.getFellow("cbcurso2")).getSelectedItem().getValue();
        ((Combobox) mDialogAddUser.getFellow("cbplanoU")).setValue(c.getPlanoc() + "");
        mDialogAddUser.doModal();
    }

    public void limpaLB(Listbox lb) {
        if (lb.getItemCount() > 0) {
            final Iterator<Listitem> items = new ArrayList(lb.getItems()).listIterator();
            Listitem li;
            Listbox l = new Listbox();
            while (items.hasNext()) {
                li = items.next();
                l.appendChild(li);
            }
        }
    }

    public void onClick$cancelUser(Event e) {

        clearFormAddUser();
        addFormConstraint();
        mDialogAddUser.detach();
    }

    public void onBtnvagrup() {
        if (cbgrupos.getSelectedItem() == null) {
            return;
        }
        Grupo g = (Grupo) cbgrupos.getSelectedItem().getValue();
        if (lbgrupo.getListModel() == null) {
            lbgrupo.setModel(new ListModelList(new ArrayList<Grupo>()));
        }
        if (!((ListModelList) lbgrupo.getListModel()).contains(g)) {
            lbgrupo.setRows(lbgrupo.getItemCount() + 1);
            ((ListModelList) lbgrupo.getListModel()).add(g);
        }
    }

    public void onSaveUser() throws IOException {
        Estudante e = null;
        Endereco end1 = null;
        Enderecof end2 = null;
        Documento doc = null;
        if (validate()) {
            if (lbgrupo.getItems().size() == 0) {
                Clients.showNotification("Por favor seleccione pelo menos um Grupo de utilizador!", "warning", null, null, 3000);
                return;
            }
            Users user = new Users();
            if (!(mDialogAddUser.getTitle().charAt(0) == 'E')) {
                user.setUtilizador(txtAccount.getValue());
//    +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++        
//            Controlador deve mudar
                if (!cbfunc.isDisabled()) {
                    if (cbfunc.getSelectedItem() == null) {
                        cbfunc.setText("");
                        cbfunc.getText();
                    }
                    user.setNome(((Funcionario) cbfunc.getSelectedItem().getValue()).getNome());
                    user.setIdFuncionario((Funcionario) cbfunc.getSelectedItem().getValue());
                } else if (!cbest.isDisabled()) {
                    if (cbest.getSelectedItem() == null) {
                        cbest.setText("");
                        cbest.getText();
                    }
                    user.setNome(((Estudante) cbest.getSelectedItem().getValue()).getNomeCompleto());
                    user.setIdEstudante((Estudante) cbest.getSelectedItem().getValue());
                } else {
                    e = new Estudante();
                    e.setNrEstudante(txtnrmec.getValue());
                    e.setNomeCompleto(txtnome.getValue());
                    e.setApelido(txtapelido.getValue());
                    e.setAnoIngresso(tbanoing.getValue());
                    e.setNacionalidade(new Pais(122));
                    e.setMasculino(false);
                    e.setEstado(true);
                    e.setNivelFrequencia(1);
                    if (cbcurso.getSelectedItem() == null) {
                        cbcurso.setText("");
                        cbcurso.getText();
                    }
                    if (cbcurso2.getSelectedItem() == null) {
                        cbcurso2.setText("");
                        cbcurso2.getText();
                    }
                    e.setCursoingresso((Curso) cbcurso.getSelectedItem().getValue());
                    e.setCursocurrente((Curso) cbcurso2.getSelectedItem().getValue());
//                    if (cbplanoU.getSelectedItem() == null) {
//                        cbplanoU.setText("");
//                        cbplanoU.getText();
//                    }
                    if (cbplanoU.getSelectedItem() != null) {
                        e.setPlanoc(((Planocurricular) cbplanoU.getSelectedItem().getValue()).getPlanocurricularPK().getAno());
                    } else {
                        e.setPlanoc(((Curso) cbcurso2.getSelectedItem().getValue()).getPlanoc());
                    }
                    e.setTurno(Integer.parseInt(cbTurnoU.getSelectedItem().getValue().toString()));
                    e.setTurma(ibturma.getValue());
                }
                user.setEmail(txtEmail.getValue());
                user.setPasword(txtPassConfirm.getValue());
//                if (cbgrupos.getSelectedItem() == null) {
//                    cbgrupos.setText("");
//                    cbgrupos.getText();
//                }
//                user.setIdGrupo((Grupo) cbgrupos.getSelectedItem().getValue());
                if (cbfac.getSelectedItem() == null) {
                    cbfac.setText("");
                    cbfac.getText();
                }
                if (cbfac.getSelectedIndex() == 0) {
                    user.setFaculdade(null);
                } else {
                    user.setFaculdade((Faculdade) cbfac.getSelectedItem().getValue());
                }
                user.setTenant(usr.getTenant());
                if (rf.isChecked()) {
                    user.setUestudante(false);
                } else {
                    user.setUestudante(true);
                }
                // Messagebox.show(txtAccount.getText());
                par.clear();
                par.put("usr", txtAccount.getText());
                //Users u = csimp.findEntByJPQuery("from Users u where u.utilizador = :usr", par);
                Utilizadorgeral u = csimp.findEntByJPQuery("from Utilizadorgeral u where u.utilizador = :usr", par);
                if (u == null) {
                    if (ne.isChecked()) {
                        csimp.Save(e);
                        end1 = new Endereco(e.getIdEstudante());
                        end1.setEstudante(e);
                        end1.setProvincia(new Provincia(new Long(1)));
                        csimp.Save(end1);
                        end2 = new Enderecof(e.getIdEstudante());
                        end2.setEstudante(e);
                        end2.setProvincia(new Provincia(new Long(1)));
                        csimp.Save(end2);
                        doc = new Documento(e.getIdEstudante());
                        doc.setEstudante(e);
                        csimp.Save(doc);
                        user.setNome(e.getNomeCompleto());
                        user.setIdEstudante(e);
                    }
                    user.setIdGrupo((Grupo) lbgrupo.getItems().get(0).getValue());
//                    csimp.Save(user);
                    Grupo g;
                    if (lbgrupo.getItemCount() > 1) {
                        Iterator<Listitem> items1 = lbgrupo.getItems().iterator();
                        if (user.getGrupoList() == null) {
                            user.setGrupoList(new ArrayList<Grupo>());
                        }
                        while (items1.hasNext()) {
                            g = (Grupo) items1.next().getValue();
                            g = csimp.get(Grupo.class, g.getIdGrupo());
                            user.getGrupoList().add(g);
                        }
                    }
                    csimp.Save(user);
                    ((ListModelList) lbUser.getModel()).add(user);
                    Clients.showNotification("Uma conta para" + user.getNome() + " adicionado com sucesso", null, null, null, 2000);
                } else {
                    Clients.showNotification("Utilizador " + user.getUtilizador() + " ja se encontra cadastrado no sistema", "error", null, null, 0);
                    return;
                }
            } else {
                par.clear();
                par.put("usr", txtAccount.getText());
                //Users u = csimp.findEntByJPQuery("from Users u where u.utilizador = :usr", par);
                Utilizadorgeral uu = csimp.findEntByJPQuery("from Utilizadorgeral u where u.utilizador = :usr", par);
                if (!txtAccount.getValue().equals(lid.getValue()) && uu != null) {
                    Clients.showNotification("Utilizador " + uu.getUtilizador() + " ja se encontra cadastrado no sistema", "error", null, null, 0);
                    return;
                }
                user = csimp.GetUniqueEntityByNamedQuery("Users.findByUtilizador", lid.getValue());
                Users u = new Users();
                Estudante est = null;
                u.setNome(user.getNome());
                u.setLastAccess(user.getLastAccess());
                u.setUestudante(user.getUestudante());
                if (u.getUestudante()) {
                    est = csimp.get(Estudante.class, user.getIdEstudante().getIdEstudante());
                    if (est != null && !est.getNrEstudante().equals(oldnr.getValue())) {
                        par.clear();
                        par.put("nr", txtnrmec.getValue());
                        Estudante en = csimp.findEntByJPQuery("from Estudante e where e.estado is true and e.nrEstudante = :nr", par);
                        if (en != null) {
                            Clients.showNotification("O numero " + txtnrmec.getValue() + " ja foi registado com o estudante " + en.getNomeCompleto(), "error", null, null, 0);
                            return;
                        }
                    }
                    est.setNrEstudante(txtnrmec.getValue());
                    est.setNomeCompleto(txtnome.getValue());
                    est.setApelido(txtapelido.getValue());
                    est.setAnoIngresso(tbanoing.getValue());
                    est.setNacionalidade(new Pais(122));
                    //         est.setMasculino(false);
                    //        est.setEstado(true);
                    //        est.setNivelFrequencia(1);
                    if (cbcurso.getSelectedItem() == null) {
                        cbcurso.setText("");
                        cbcurso.getText();
                    }
                    if (cbcurso2.getSelectedItem() == null) {
                        cbcurso2.setText("");
                        cbcurso2.getText();
                    }
                    est.setCursoingresso((Curso) cbcurso.getSelectedItem().getValue());
                    est.setCursocurrente((Curso) cbcurso2.getSelectedItem().getValue());
                    u.setIdEstudante(user.getIdEstudante());
                    u.setNome(est.getNomeCompleto());
//                    if (cbplanoU.getSelectedItem() == null) {
//                        cbplanoU.setText("");
//                        cbplanoU.getText();
//                    }
                    if (cbplanoU.getSelectedItem() != null) {
                        Planocurricular p = (Planocurricular) cbplanoU.getSelectedItem().getValue();
                        int pl = p.getPlanocurricularPK().getAno();
//                        Messagebox.show(pl+"");
                        est.setPlanoc(pl);
                    } else {
                        est.setPlanoc(((Curso) cbcurso2.getSelectedItem().getValue()).getPlanoc());
                    }
                    est.setTurno(Integer.parseInt(cbTurnoU.getSelectedItem().getValue().toString()));
                } else {
                    u.setIdFuncionario(user.getIdFuncionario());
                }
                ((ListModelList) lbUser.getModel()).remove(user);

                u.setUtilizador(txtAccount.getValue());
                u.setEmail(txtEmail.getValue());
                u.setPasword(txtPassConfirm.getValue());
//                if (cbgrupos.getSelectedItem() == null) {
//                    cbgrupos.setText("");
//                    cbgrupos.getText();
//                }
                //     u.setIdGrupo((Grupo) cbgrupos.getSelectedItem().getValue());
                if (cbfac.getSelectedItem() == null) {
                    cbfac.setText("");
                    cbfac.getText();
                }
                if (cbfac.getSelectedIndex() == 0) {
                    u.setFaculdade(null);
                } else {
                    u.setFaculdade((Faculdade) cbfac.getSelectedItem().getValue());
                }
                u.setTenant(usr.getTenant());
//                if (rf.isChecked()) {
//                    u.setUestudante(false);
//                } else {
//                    u.setUestudante(true);
//                }
                u.setIdGrupo((Grupo) lbgrupo.getItems().get(0).getValue());
                if (u.getUestudante()) {
                    csimp.update(est);
                }
                if (est != null && !est.getNrEstudante().equals(oldnr.getValue())) {
//                    Clients.showNotification("Este processo podera levar muito tempo", "warning", null, null, 3000);
                    for (Arquivoestudante arq2 : est.getArquivoestudanteList()) {
                        String sss = UserAutentic.getPathR(arq2.getTipo().toString()) + oldnr.getValue() + arq2.getNomearquivo();
                        InputStream iss = null;
                        try {
                            iss = new FileInputStream(sss);
                        } catch (java.io.FileNotFoundException ex) {

                        }
                        if (iss != null) {
                            Media pp = new AMedia(sss, FilenameUtils.getExtension(sss), URLConnection.guessContentTypeFromName(sss), iss);
                            Media mm = new AMedia(arq2.getNomearquivo(), FilenameUtils.getExtension(sss), URLConnection.guessContentTypeFromName(sss), pp.getByteData());

                            String nome = oldnr.getValue() + arq2.getNomearquivo();
                            File Test = new File(UserAutentic.getPathR(arq2.getTipo().toString()) + nome);
                            Test.delete();

                            String nome2 = est.getNrEstudante() + arq2.getNomearquivo();
                            File fnovo = new File(UserAutentic.getPathR(arq2.getTipo().toString()) + nome2);
                            try {
                                org.zkoss.io.Files.copy(fnovo, mm.getStreamData());
                            } catch (IOException ex) {
                                Logger.getLogger(ProfileCreateUsersController.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    }
                    for (Matricula mat : est.getMatriculaList()) {
                        for (Arquivomatricula arq2 : mat.getArquivomatriculaList()) {
                            String sss = UserAutentic.getPathR("3") + oldnr.getValue() + mat.getMatriculaPK().getAno() + arq2.getNomearquivo();
                            InputStream iss = null;
                            try {
                                iss = new FileInputStream(sss);
                            } catch (java.io.FileNotFoundException ex) {

                            }
                            if (iss != null) {
                                Media pp = new AMedia(sss, FilenameUtils.getExtension(sss), URLConnection.guessContentTypeFromName(sss), iss);
                                Media mm = new AMedia(arq2.getNomearquivo(), FilenameUtils.getExtension(sss), URLConnection.guessContentTypeFromName(sss), pp.getByteData());

                                String nome = oldnr.getValue() + arq2.getMatricula().getMatriculaPK().getAno() + arq2.getNomearquivo();
                                File Test = new File(UserAutentic.getPathR("3") + nome);
                                Test.delete();

                                String nome2 = est.getNrEstudante() + arq2.getMatricula().getMatriculaPK().getAno() + arq2.getNomearquivo();
                                File fnovo = new File(UserAutentic.getPathR("3") + nome2);
                                try {
                                    org.zkoss.io.Files.copy(fnovo, mm.getStreamData());
                                } catch (IOException ex) {
                                    Logger.getLogger(ProfileCreateUsersController.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            }
                        }
                    }
                    for (Inscricao insc : est.getInscricaoList()) {
                        for (Arquivoinscricao arq : insc.getArquivoinscricaoList()) {
                            String dat = new SimpleDateFormat("dd_MM_yyyy_HH_mm_ss").format(insc.getDataInscricao());
                            String sss = UserAutentic.getPathR("4") + oldnr.getValue() + arq.getNome() + dat;
                            InputStream iss = null;
                            try {
                                iss = new FileInputStream(sss);
                            } catch (java.io.FileNotFoundException ex) {

                            }
                            if (iss != null) {
                                Media pp = new AMedia(sss, FilenameUtils.getExtension(sss), URLConnection.guessContentTypeFromName(sss), iss);
                                Media mm = new AMedia(arq.getNome(), FilenameUtils.getExtension(sss), URLConnection.guessContentTypeFromName(sss), pp.getByteData());

                                File test = new File(sss);
                                test.delete();

                                String sss2 = UserAutentic.getPathR("4") + est.getNrEstudante() + arq.getNome() + dat;
                                File fnovo = new File(sss2);
                                try {
                                    org.zkoss.io.Files.copy(fnovo, mm.getStreamData());
                                } catch (IOException ex) {
                                    Logger.getLogger(ProfileCreateUsersController.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            }
                        }
                    }
                }

                //csimp.Save(u);
                Grupo g;
                if (lbgrupo.getItemCount() > 1) {
                    Iterator<Listitem> items1 = lbgrupo.getItems().iterator();
                    if (u.getGrupoList() == null) {
                        u.setGrupoList(new ArrayList<Grupo>());
                    }
                    while (items1.hasNext()) {
                        g = (Grupo) items1.next().getValue();
                        g = csimp.get(Grupo.class, g.getIdGrupo());
                        user.getGrupoList().remove(g);
                        u.getGrupoList().add(g);
                    }
                }
                csimp.delete(user);
                csimp.Save(u);
                ((ListModelList) lbUser.getModel()).add(u);
                Clients.showNotification("Credenciais de" + u.getNome() + " modificado com sucesso", null, null, null, 2000);
            }
            clearFormAddUser();
            addFormConstraint();

            Component comp = this.self;

//            mDialogAddUser.setVisible(false);
            mDialogAddUser.detach();

            this.self = comp;

        } else {
            Clients.showNotification("Por favor, confirme a senha do Utilizador", "error", null, null, 0);
        }

    }

    public void onSelect$cbcurso2() {
        if (cbcurso2.getSelectedItem() != null) {
            par.clear();
            Curso cu = (Curso) cbcurso2.getSelectedItem().getValue();
            par.clear();
            par.put("c", cu);
            List<Planocurricular> lc = csimp.findByJPQuery("from Planocurricular p where p.curso = :c order by p.planocurricularPK.ano desc", par);
            cbplanoU.setModel(new ListModelList<Planocurricular>(lc));
            //  Comboitem cbt1 = cbplanoU.getItems().get(0);
            //  cbplanoU.setSelectedItem(cbt1);
        }
    }

    public void onEdit(ForwardEvent evt) throws Exception {
        Button btn = (Button) evt.getOrigin().getTarget();
        Listitem litem = (Listitem) btn.getParent().getParent();
        Users user = (Users) litem.getValue();
        user = csimp.load(Users.class, user.getUtilizador());
        mDialogAddUser.setParent(windowUser);
        mDialogAddUser.setTitle("Editar Utilizador " + user.getNome());
        mDialogAddUser.doModal();
        Faculdade f = user.getFaculdade();
        if (user.getUestudante()) {
            ((Row) mDialogAddUser.getFellow("f1")).setVisible(true);
            ((Row) mDialogAddUser.getFellow("f2")).setVisible(true);
            ((Row) mDialogAddUser.getFellow("f3")).setVisible(true);
            ((Row) mDialogAddUser.getFellow("f4")).setVisible(true);
            ((Row) mDialogAddUser.getFellow("f5")).setVisible(true);
            ((Row) mDialogAddUser.getFellow("f6")).setVisible(true);
            ((Row) mDialogAddUser.getFellow("f7")).setVisible(true);
            ((Row) mDialogAddUser.getFellow("f8")).setVisible(true);
            ((Row) mDialogAddUser.getFellow("f9")).setVisible(true);
            ((Textbox) mDialogAddUser.getFellow("txtnrmec")).setText(user.getIdEstudante().getNrEstudante());
            ((Textbox) mDialogAddUser.getFellow("oldnr")).setText(user.getIdEstudante().getNrEstudante());
            ((Textbox) mDialogAddUser.getFellow("txtnome")).setText(user.getIdEstudante().getNomeCompleto());
            ((Textbox) mDialogAddUser.getFellow("txtapelido")).setText(user.getIdEstudante().getApelido());
            ((Intbox) mDialogAddUser.getFellow("tbanoing")).setValue(user.getIdEstudante().getAnoIngresso());
            final Iterator<Comboitem> items2 = new ArrayList(((Combobox) mDialogAddUser.getFellow("cbcurso")).getItems()).listIterator();
            Comboitem cit2 = null;
            while (items2.hasNext()) {
                cit2 = items2.next();
                if (((Curso) cit2.getValue()).getIdCurso().equals(user.getIdEstudante().getCursoingresso().getIdCurso())) {
                    ((Combobox) mDialogAddUser.getFellow("cbcurso")).setSelectedItem(cit2);
                    break;
                }
            }
            final Iterator<Comboitem> items3 = new ArrayList(((Combobox) mDialogAddUser.getFellow("cbcurso2")).getItems()).listIterator();
            // Comboitem cit2 = null;
            while (items3.hasNext()) {
                cit2 = items3.next();
                if (((Curso) cit2.getValue()).getIdCurso().equals(user.getIdEstudante().getCursocurrente().getIdCurso())) {
                    ((Combobox) mDialogAddUser.getFellow("cbcurso2")).setSelectedItem(cit2);
                    break;
                }
            }
            int planoe = user.getIdEstudante().getPlanoc();
            Curso cu = (Curso) ((Combobox) mDialogAddUser.getFellow("cbcurso2")).getSelectedItem().getValue();
            par.clear();
            par.put("c", cu);
            List<Planocurricular> lc = csimp.findByJPQuery("from Planocurricular p where p.curso = :c order by p.planocurricularPK.ano desc", par);
            ((Combobox) mDialogAddUser.getFellow("cbplanoU")).setModel(new ListModelList<Planocurricular>(lc));
            if (((Combobox) mDialogAddUser.getFellow("cbplanoU")).getItems().isEmpty()) {
                final Iterator<Comboitem> items4 = new ArrayList(((Combobox) mDialogAddUser.getFellow("cbplanoU")).getItems()).listIterator();
                // Comboitem cit2 = null;
                while (items4.hasNext()) {
                    cit2 = items4.next();
                    if (((Planocurricular) cit2.getValue()).getPlanocurricularPK().getAno() == planoe) {
                        ((Combobox) mDialogAddUser.getFellow("cbplanoU")).setSelectedItem(cit2);
                        break;
                    }
                }
            } else {
                ((Combobox) mDialogAddUser.getFellow("cbplanoU")).setText(planoe + "");
            }
            Combobox cbt = (Combobox) mDialogAddUser.getFellow("cbTurnoU");
            Comboitem cbt1 = cbt.getItems().get(0);
            Comboitem cbt2 = cbt.getItems().get(1);
            if (user.getIdEstudante().getTurno() == 1) {
                cbt.setSelectedItem(cbt1);
                cbt.setText("Laboral");
            } else {
                cbt.setSelectedItem(cbt2);
                cbt.setText("Pos-Laboral");
            }
            ((Intbox) mDialogAddUser.getFellow("ibturma")).setValue(user.getIdEstudante().getTurma());
        } else {
            ((Row) mDialogAddUser.getFellow("f1")).setVisible(false);
            ((Row) mDialogAddUser.getFellow("f2")).setVisible(false);
            ((Row) mDialogAddUser.getFellow("f3")).setVisible(false);
            ((Row) mDialogAddUser.getFellow("f4")).setVisible(false);
            ((Row) mDialogAddUser.getFellow("f5")).setVisible(false);
            ((Row) mDialogAddUser.getFellow("f6")).setVisible(false);
            ((Row) mDialogAddUser.getFellow("f7")).setVisible(false);
            ((Row) mDialogAddUser.getFellow("f8")).setVisible(false);
            ((Row) mDialogAddUser.getFellow("f9")).setVisible(false);
        }
        ((Row) mDialogAddUser.getFellow("row1user")).setVisible(false);
        ((Textbox) mDialogAddUser.getFellow("txtAccount")).setText(user.getUtilizador());
        ((Textbox) mDialogAddUser.getFellow("txtEmail")).setText(user.getEmail());
        ((Textbox) mDialogAddUser.getFellow("txtPass")).setText(user.getPasword());
        ((Textbox) mDialogAddUser.getFellow("txtPassConfirm")).setText(user.getPasword());
        ((Textbox) mDialogAddUser.getFellow("lid")).setValue(user.getUtilizador());
//        if (user.getUestudante()) {
//            re.setChecked(true);
//            final Iterator<Comboitem> items = new ArrayList(((Combobox) mDialogAddUser.getFellow("cbest")).getItems()).listIterator();
//            Comboitem cit;
//            while (items.hasNext()) {
//                cit = items.next();
//                if (((Estudante) cit.getValue()).getIdEstudante().equals(user.getIdUtilizador())) {
//                    ((Combobox) mDialogAddUser.getFellow("cbest")).setSelectedItem(cit);
//                    break;
//                }
//            }
//        }else{
//          rf.setChecked(true);
//            final Iterator<Comboitem> items = new ArrayList(((Combobox) mDialogAddUser.getFellow("cbfunc")).getItems()).listIterator();
//            Comboitem cit;
//            while (items.hasNext()) {
//                cit = items.next();
//                if (((Funcionario) cit.getValue()).getIdFuncionario().equals(user.getIdUtilizador())) {
//                    ((Combobox) mDialogAddUser.getFellow("cbfunc")).setSelectedItem(cit);
//                    break;
//                }
//            }
//        }

//        final Iterator<Comboitem> items = new ArrayList(((Combobox) mDialogAddUser.getFellow("cbgrupos")).getItems()).listIterator();
//        Comboitem cit;
//        while (items.hasNext()) {
//            cit = items.next();
//            if (((Grupo) cit.getValue()).getIdGrupo().equals(user.getIdGrupo().getIdGrupo())) {
//                ((Combobox) mDialogAddUser.getFellow("cbgrupos")).setSelectedItem(cit);
//                break;
//            }
//        }
        if (f != null) {
            final Iterator<Comboitem> items2 = new ArrayList(((Combobox) mDialogAddUser.getFellow("cbfac")).getItems()).listIterator();
            Comboitem cit2 = null;
            items2.next();
            while (items2.hasNext()) {
                cit2 = items2.next();
                if (((Faculdade) cit2.getValue()).getIdFaculdade().equals(f.getIdFaculdade())) {
                    ((Combobox) mDialogAddUser.getFellow("cbfac")).setSelectedItem(cit2);
                    break;
                }
            }
        }
        Listbox grup = ((Listbox) mDialogAddUser.getFellow("lbgrupo"));
        List<Grupo> li = user.getGrupoList();
        String g = user.getIdGrupo().getIdGrupo();
        for (int i = 0; i < li.size(); i++) {
            if (li.get(i).getIdGrupo().equals(g)) {
                li.remove(i);
            }
        }
        li.add(0, user.getIdGrupo());
        if (li != null) {
            grup.setModel(new ListModelList<Grupo>(li));
        }

    }

    private void clearFormAddUser() {
        Constraint c = null;
        txtAccount.setConstraint(c);
        cbfunc.setConstraint(c);
        txtPass.setConstraint(c);
        txtPassConfirm.setConstraint(c);
        // cbgrupos.setConstraint(c);
        cbfac.setConstraint(c);
        txtAccount.setValue("");
        cbfunc.setSelectedIndex(-1);
        txtEmail.setValue("");
        txtPass.setValue("");
        txtPassConfirm.setValue("");
        validation.setValue("");
        pwd_val.setValue("");
    }

    private void addFormConstraint() {
        txtAccount.setConstraint(" no Empty: Insira o nome do utilizador!");
        cbfunc.setConstraint(" no Empty: Insira um nome ou seleccione um existente!");
        txtPass.setConstraint(" no Empty: palavra-passe!");
        txtPassConfirm.setConstraint(" no Empty: confirmar a palavra-passe!");
        //   cbgrupos.setConstraint(" no Empty:Seleccione um Grupo de utilizador!");
        cbfac.setConstraint(" no Empty:Seleccione uma Faculdade!");
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

                                Users user = (Users) litem.getValue();
                                ((ListModelList) lbUser.getModel()).remove(user);
                                new Listbox().appendChild(litem);
                                csimp.delete(user);
                                Clients.showNotification("User " + user.getUtilizador() + " apagado com sucesso", null, null, null, 2000);
                            case Messagebox.NO:
                                return;
                        }
                    }

                });
    }

    private boolean validate() {
        if (!txtPass.getValue().equals(txtPassConfirm.getValue())) {
            return false;
        }
        return true;
    }

    public ListModel<Faculdade> getListFacModel() {
        List<Faculdade> lf = new ArrayList<Faculdade>();
        Faculdade f = new Faculdade();
        f.setDesricao("-----Reitoria----");
        lf.add(f);
        lf.addAll(csimp.getAll(Faculdade.class));
        return new ListModelList<Faculdade>(lf);
    }

    ////////////////////////////////////PESQUISAS//////////////////////////////////////////////////////
    public void onChange$cbfaculdade() {
        if (cbfaculdade.getSelectedItem() != null) {
            if (cbfaculdade.getSelectedIndex() == 0) {
                condfac = " and u.faculdade is null";
                if (condpar.containsKey("fac")) {
                    condpar.remove("fac");
                }
            } else {
                condfac = " and u.faculdade = :fac";
                Faculdade f = (Faculdade) cbfaculdade.getSelectedItem().getValue();
                if (condpar.containsKey("fac")) {
                    condpar.replace("fac", f);
                } else {
                    condpar.put("fac", f);
                }
            }
        }
        setLB(0, 20);
    }

    public void onChanging$txProcurar(InputEvent evt) {
        if (!evt.getValue().equals("") && evt.getValue().charAt(0) != '.') {
            condnome = " and lower(u.nome) like :nome ";
            if (condpar.containsKey("nome")) {
                condpar.replace("nome", "%" + evt.getValue().toLowerCase() + "%");
            } else {
                condpar.put("nome", "%" + evt.getValue().toLowerCase() + "%");
            }
        } else {
            condnome = "";
            if (!condpar.containsKey("nome")) {
                return;
            }
            condpar.remove("nome");

        }
        setLB(0, 20);
    }

    public void onChange$cbgrupop() {
        if (cbgrupop.getSelectedIndex() != 0) {
            condgrupo = " and u.idGrupo = :g";
            Grupo g = (Grupo) cbgrupop.getSelectedItem().getValue();
            if (condpar.containsKey("g")) {
                condpar.replace("g", g);
            } else {
                condpar.put("g", g);
            }
        } else {
            condgrupo = "";
            condpar.remove("g");

        }
        setLB(0, 20);
    }

    public void setLB(int i, int j) {
        if (j == 20) {
            lbUser.setModel(new ListModelList<Users>());
        }
        List<Users> li = csimp.findByJPQueryFilter("from Users u where 1=1" + condfac + condgrupo + condnome + " order by u.nome", condpar, i, j);
        final Iterator<Users> items = li.iterator();
        Users e;
        lbUser.setRows(lbUser.getItemCount() + li.size());
        while (items.hasNext()) {
            e = items.next();
            ((ListModelList) lbUser.getModel()).add(e);
        }
        if (li.size() < j) {
            btv.setVisible(false);
        } else {
            btv.setVisible(true);
        }
    }

    public void onLoadi() {
        int i = lbUser.getItemCount();
        setLB(i, i + 20);
    }

    /////////////////////////////////////COMBOBOX FUNCIONARIO//////////////////////////////////////////////////////////////
    public void onSCbfunc() {
        if (cbfunc.getSelectedIndex() == poscf.getValue() - 1) {
            onLoadCombf();
            cbfunc.open();
            cbfunc.setText(pesqf);
            return;
        }
        indcf = cbfunc.getSelectedIndex();
    }

    public void onCbfunc(InputEvent evt) {
        indcf = -1;
        if (!evt.getValue().equals("") && evt.getValue().charAt(0) != '.') {
            pesqf = evt.getValue();
            condnf = " and lower(f.nome) like :nome ";
            if (cbparf.containsKey("nome")) {
                cbparf.replace("nome", "%" + evt.getValue().toLowerCase() + "%");
            } else {
                cbparf.put("nome", "%" + evt.getValue().toLowerCase() + "%");
            }
        } else {
            if (pesqf != null) {
                pesqf = "";
            }
            condnf = "";
            if (!cbparf.containsKey("nome")) {
                return;
            }
            cbparf.remove("nome");
        }
        cbfunc.getItems().clear();
        setLBCombf(0, 20);
    }

    public void onOpen$cbfunc() {
//        if (poscf.getValue() == 0) {
//            cbfunc.getItems().clear();
//            setLBCombf(0, 20);
//        }
        if (pesqf == null) {
            setLBCombf(0, 20);
            pesqf = "";
        } else {
            if (cbfunc.isOpen()) {
                cbfunc.setText(pesqf);
            } else {
                if (indcf != -1) {
                    cbfunc.getSelectedIndex();
                    cbfunc.setSelectedIndex(indcf);
                }
            }
        }
    }

    public void setLBCombf(int i, int j) {
        if (j == 20) {
            cbfunc.setModel(new ListModelList<Funcionario>());
        }
        List<Funcionario> li = null;
        Users u = csimp.get(Users.class, usr.getUtilizador());
        // par.clear();
        Date dano = new Date();
        Calendar cal = new GregorianCalendar();
        cal.setTime(dano);
        int ano = cal.get(Calendar.YEAR);
        li = csimp.findByJPQueryFilter("from Funcionario f where 1=1 " + condnf + " order by f.nome", cbparf, i, j);
        final Iterator<Funcionario> items = li.iterator();
        Funcionario e;
        //lbinscricao.setRows(lbinscricao.getItemCount() + li.size());
        if (j > 20) {
            ((ListModelList) cbfunc.getModel()).remove(new Funcionario());
            new Combobox().appendChild(cbfunc.getItemAtIndex(poscf.getValue() - 1));
        }
        while (items.hasNext()) {
            e = items.next();
            ((ListModelList) cbfunc.getModel()).add(e);
        }
        if (li.size() == j) {
            Funcionario es = new Funcionario();
            es.setNome("-------Ver Mais-------");
            ((ListModelList) cbfunc.getModel()).add(es);
            poscf.setValue(((ListModelList) cbfunc.getModel()).size());
        }
    }

    public void onLoadCombf() {
        int i = ((ListModelList) cbfunc.getModel()).size() - 1;
        setLBCombf(i, i + 20);
    }

    /////////////////////////////////////COMBOBOX ESTUDANTE//////////////////////////////////////////////////////////////
    public void onSCbest() {
        if (cbest.getSelectedIndex() == posce.getValue() - 1) {
            onLoadCombe();
            cbest.open();
            cbest.setText(pesqe);
            return;
        }
        indce = cbest.getSelectedIndex();
    }

    public void onCbest(InputEvent evt) {
        indce = -1;
        if (!evt.getValue().equals("") && evt.getValue().charAt(0) != '.') {
            pesqe = evt.getValue();
            condne = " and lower(e.nomeCompleto) like :nome ";
            if (cbpare.containsKey("nome")) {
                cbpare.replace("nome", "%" + evt.getValue().toLowerCase() + "%");
            } else {
                cbpare.put("nome", "%" + evt.getValue().toLowerCase() + "%");
            }
        } else {
            if (pesqe != null) {
                pesqe = "";
            }
            condne = "";
            if (!cbpare.containsKey("nome")) {
                return;
            }
            cbpare.remove("nome");
        }
        cbest.getItems().clear();
        setLBCombe(0, 20);
    }

    public void onOpen$cbest() {
//        if (posce.getValue() == 0) {
//            cbest.getItems().clear();
//            setLBCombe(0, 20);
//        }
        if (pesqe == null) {
            setLBCombe(0, 20);
            pesqe = "";
        } else {
            if (cbest.isOpen()) {
                cbest.setText(pesqe);
            } else {
                if (indce != -1) {
                    cbest.getSelectedIndex();
                    cbest.setSelectedIndex(indce);
                }
            }
        }
    }

    public void setLBCombe(int i, int j) {
        if (j == 20) {
            cbest.setModel(new ListModelList<Estudante>());
        }
        List<Estudante> li = null;
        li = csimp.findByJPQueryFilter("from Estudante e where 1=1 " + condne + " order by e.nomeCompleto", cbpare, i, j);
        final Iterator<Estudante> items = li.iterator();
        Estudante e;
        //lbinscricao.setRows(lbinscricao.getItemCount() + li.size());
        if (j > 20) {
            ((ListModelList) cbest.getModel()).remove(new Estudante());
            new Combobox().appendChild(cbest.getItemAtIndex(posce.getValue() - 1));
        }
        while (items.hasNext()) {
            e = items.next();
            ((ListModelList) cbest.getModel()).add(e);
        }
        if (li.size() == j) {
            Estudante es = new Estudante();
            es.setNomeCompleto("-------Ver Mais-------");
            ((ListModelList) cbest.getModel()).add(es);
            posce.setValue(((ListModelList) cbest.getModel()).size());
        }
    }

    public void onLoadCombe() {
        int i = ((ListModelList) cbest.getModel()).size() - 1;
        setLBCombe(i, i + 20);
    }

    public void onExportUsr() throws ParseException {
        if (lbUser.getItemCount() == 0) {
            Clients.showNotification("Sem conteúdo", "warning", null, null, 3000);
            return;
        }
        BeanToExcel beanToExcel = new BeanToExcel();
        beanToExcel.setDataSheetName("Lista de Utilizadores");
        beanToExcel.exportExcell(lbUser);
    }
}
