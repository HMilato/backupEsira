 /*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package esira.controller;

//import esira.controller.authentication.Grupo;
//import esira.controller.authentication.GrupoRole;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.DocumentException;
import esira.domain.Arquivoestudante;
import esira.domain.Arquivoinscricao;
import esira.domain.Arquivomatricula;
import esira.domain.Cargochefia;
import esira.domain.Estudante;
import esira.domain.Funcionario;
import esira.domain.Funcionario;
import esira.domain.Grupo;
import esira.domain.Inscricao;
import esira.domain.Listaadmissao;
import esira.domain.Matricula;
import esira.domain.Operacao;
import esira.domain.Operacaopedido;
import esira.domain.Profissao;
import esira.domain.Provincia;
import esira.domain.Roles;
import esira.domain.Tipochefia;
import esira.domain.Users;
import esira.service.CRUDService;
import esira.service.Email;
import esira.service.Populardados;
import esira.service.UserAutentic;
import esira.webapp.Item;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
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
import javax.mail.MessagingException;
import net.sf.jasperreports.engine.JRException;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.mail.EmailException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.zkoss.lang.Strings;
import org.zkoss.util.media.AMedia;
import org.zkoss.util.media.Media;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Constraint;
import org.zkoss.zul.Datebox;
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
public class PermissaoController extends GenericForwardComposer {

    @WireVariable
    private final CRUDService csimp = (CRUDService) SpringUtil.getBean("CRUDService");

    private static final long serialVersionUID = 43014628867656917L;
    List<Grupo> grupo = csimp.getAll(Grupo.class);
    ListModelList<Grupo> grupos = new ListModelList<Grupo>(grupo);
    List<Users> users = csimp.getAll(Users.class);
    private List<Funcionario> funcionarios = csimp.getAll(Funcionario.class);
    private ListModel<Funcionario> funcionarioModel = new ListModelList<Funcionario>(funcionarios);

    private Listbox lbPermissao;
    private Textbox tbemail;
    private Intbox ibidpermissao;
    private Datebox dbi, dbf;
    private Combobox cbfunc, cboperacao;
    Window mDialogAddPerm, winPermissao;
    Map<String, Object> par = new HashMap<String, Object>();
    private Intbox litem;
    Users usr = (Users) Sessions.getCurrent().getAttribute("user");

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        Date d = new Date();
        par.clear();
        par.put("d", d);
        List<Operacaopedido> lop = csimp.findByJPQuery("from Operacaopedido op where op.dataf < :d", par);
        final Iterator<Operacaopedido> items = new ArrayList(lop).listIterator();
        Operacaopedido op;
        while (items.hasNext()) {
            op = items.next();
            lop.remove(op);
            csimp.delete(op);
        }
    }

    public void onClick$addPerm() {
        mDialogAddPerm.setParent(winPermissao);
        mDialogAddPerm.setTitle("Adicionar Permissao");
        ((Combobox) mDialogAddPerm.getFellow("cbfunc")).setValue("------Funcionario------");
        ((Combobox) mDialogAddPerm.getFellow("cboperacao")).setValue("------Operacao------");
        mDialogAddPerm.doModal();
    }

    public void onClick$cancelPerm(Event e) {
        limpar(mDialogAddPerm);
        mDialogAddPerm.detach();
    }

    public ListModel<Funcionario> getFuncionarioModel() {
        return new ListModelList<Funcionario>(csimp.getAll(Funcionario.class));
    }

    public ListModelList<Operacao> getOperacaoModel() {
        return new ListModelList<Operacao>(csimp.getAll(Operacao.class));
    }

    private void limpar(Component component) {
        limparComp(component);

        if (component.isVisible()) {
            List<Component> children = component.getChildren();
            for (Component each : children) {
                limpar(each);
            }
        }
    }

    public void limparComp(Component component) {
        Constraint co = null;
        if (component instanceof InputElement) {
            Constraint c = ((InputElement) component).getConstraint();
            if (c != null) {
                SimpleConstraint sc = (SimpleConstraint) c;
                String s = sc.getClientConstraint();
                ((InputElement) component).setConstraint(co);
                ((InputElement) component).setText("");
                ((InputElement) component).setConstraint(s.substring(1, s.length() - 1));
            } else {
                ((InputElement) component).setText("");
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

    public void onSalvar(ForwardEvent evt) {
        check(mDialogAddPerm);
        Operacaopedido op = new Operacaopedido();
        if (mDialogAddPerm.getTitle().charAt(0) == 'E') {
            op = csimp.get(Operacaopedido.class, ibidpermissao.getValue().longValue());
        }
        Funcionario fu = csimp.get(Users.class, usr.getUtilizador()).getIdFuncionario();
        op.setAutor(fu);
        op.setFuncionario((Funcionario) cbfunc.getSelectedItem().getValue());
        Date d = dbi.getValue();
        d.setHours(1);
        op.setDatai(d);
        d = dbf.getValue();
        d.setHours(23);
        op.setDataf(d);
        op.setOperacao((Operacao) cboperacao.getSelectedItem().getValue());
        if (mDialogAddPerm.getTitle().charAt(0) == 'E') {
            csimp.update(op);
            ((ListModelList) lbPermissao.getModel()).set(litem.getValue(), op);
            Clients.showNotification(" Actualizado com Sucesso", null, null, null, 0);
        } else {
            csimp.Save(op);
            ((ListModelList) lbPermissao.getModel()).add(op);
            Clients.showNotification(" Adicionado com Sucesso", null, null, null, 0);
        }
        limpar(mDialogAddPerm);
        mDialogAddPerm.detach();

    }

    public void onEditPerm(ForwardEvent evt) throws Exception {
        Button btn = (Button) evt.getOrigin().getTarget();
        Listitem litem = (Listitem) btn.getParent().getParent();
        Operacaopedido op = (Operacaopedido) litem.getValue();
        op = csimp.load(Operacaopedido.class, op.getIdpermissao());
        int operacao = op.getOperacao().getIdoperacao();
        Long funcionario = op.getFuncionario().getIdFuncionario().longValue();
        mDialogAddPerm.setParent(winPermissao);
        mDialogAddPerm.setTitle("Editar");
        mDialogAddPerm.doModal();
        ((Intbox) mDialogAddPerm.getFellow("litem")).setValue(litem.getIndex());
        ((Datebox) mDialogAddPerm.getFellow("dbi")).setValue(op.getDatai());
        ((Datebox) mDialogAddPerm.getFellow("dbf")).setValue(op.getDataf());
        ((Intbox) mDialogAddPerm.getFellow("ibidpermissao")).setValue(op.getIdpermissao());

        final Iterator<Comboitem> itemsfuncionario = new ArrayList(((Combobox) mDialogAddPerm.getFellow("cbfunc")).getItems()).listIterator();
        Comboitem citfuncionario;
        while (itemsfuncionario.hasNext()) {
            citfuncionario = itemsfuncionario.next();
            if (((Funcionario) citfuncionario.getValue()).getIdFuncionario() == funcionario) {
                ((Combobox) mDialogAddPerm.getFellow("cbfunc")).setSelectedItem(citfuncionario);
                break;
            }
        }

        final Iterator<Comboitem> itemsoperacao = new ArrayList(((Combobox) mDialogAddPerm.getFellow("cboperacao")).getItems()).listIterator();
        Comboitem citoperacao;
        while (itemsoperacao.hasNext()) {
            citoperacao = itemsoperacao.next();
            if (((Operacao) citoperacao.getValue()).getIdoperacao() == operacao) {
                ((Combobox) mDialogAddPerm.getFellow("cboperacao")).setSelectedItem(citoperacao);
                break;
            }
        }

    }

    public void onDeletePerm(final ForwardEvent evt) throws Exception {
        Messagebox.show("Apagar?", "Atenção", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION,
                new EventListener() {
                    @Override
                    public void onEvent(Event evet) {
                        switch (((Integer) evet.getData()).intValue()) {
                            case Messagebox.YES:
                                Button btn = (Button) evt.getOrigin().getTarget();
                                Listitem litem = (Listitem) btn.getParent().getParent();
                                Operacaopedido d = (Operacaopedido) litem.getValue();
                                ((ListModelList) lbPermissao.getModel()).remove(d);
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

    public void onTestEmail() throws IOException, MessagingException, DocumentException, BadElementException, JRException {
        new Email().SendMailSSL(UserAutentic.getEmailP("email"),
                tbemail.getText(), "Validação de Matricula", "Prezado "
                + "\nA sua matricula foi validada com Sucesso\n"
                + "" + "\n\n----------------------------------------\n\n\n"
                + "Registo Acadêmico - UniLúrio-",
                UserAutentic.getEmailP("senha"), winPermissao.getDesktop().getWebApp().getRealPath("/img"));
    }

    public void onAddEstudante() throws FileNotFoundException {
        Users u = csimp.get(Users.class, usr.getUtilizador());
        Populardados.popularEstudantes(u.getIdFuncionario());
    }

    public void onRefreshNivelEst() {
        List<Estudante> le = csimp.getAll(Estudante.class);
        Estudante e = null;
        boolean bool = false;
        final Iterator<Estudante> items = new ArrayList(le).listIterator();
        while (items.hasNext()) {
            e = items.next();
            if (e != null) {
                int nc = 1;
                int nc2 = 0;
                int c1 = 0;
                int c2 = 1;
                int co1 = 0;
                int co2 = 0;
                Date dano = new Date();
                Calendar cal = new GregorianCalendar();
                cal.setTime(dano);
                int ano = cal.get(Calendar.YEAR);
                do {
                    c1 = 0;
                    c2 = 0;
                    par.clear();
                    par.put("e", e);
                    par.put("planoc", e.getPlanoc());
                    par.put("anoi", nc);
                    //  par.put("ano", ano);
                    // par.put("niv", "%"+e.getAnoIngresso() + "%");
                    c1 = csimp.findByJPQuery("from Inscricaodisciplina id where id.inscricao.idEstudante = :e and "
                            + "id.disciplina.nivel = :anoi and id.disciplina.planoc = :planoc and id.disciplina.semestre = 1 and (id.disciplinaActiva = 3"
                            + " and id.estado is true and ((id.notaFinal is not null and id.notaFinal >= 10) ))", par).size();//or (id.notaFinal is null and extract(year from id.inscricao.dataInscricao) < :ano)
                    c2 = csimp.findByJPQuery("from Inscricaodisciplina id where id.inscricao.idEstudante = :e and "
                            + "id.disciplina.nivel = :anoi and id.disciplina.planoc = :planoc and id.disciplina.semestre = 2 and (id.disciplinaActiva = 3"
                            + " and id.estado is true and ((id.notaFinal is not null and id.notaFinal >= 10) ))", par).size();//or (id.notaFinal is null and extract(year from id.inscricao.dataInscricao) < :ano)
                    par.clear();
                    par.put("curso", e.getCursocurrente());
                    par.put("planoc", e.getPlanoc());
                    par.put("nivel", nc);
                    par.put("niv", "%" + e.getAnoIngresso() + "%");
                    co1 = csimp.findByJPQuery("from Disciplina d where d.curso=:curso and d.planoc = :planoc and d.semestre = 1 and"
                            + " d.nivel =:nivel and ((d.natraso is not null and d.natraso not like :niv) or d.natraso is null)", par).size();

                    co2 = csimp.findByJPQuery("from Disciplina d where d.curso=:curso and d.planoc = :planoc and d.semestre = 2 and"
                            + " d.nivel =:nivel and ((d.natraso is not null and d.natraso not like :niv) or d.natraso is null)", par).size();

                    //Messagebox.show("c1 e co1:" + c1 + "|" + co1 + " c2 e co2:" + c2 + "|" + co2 + " nc2:" + nc2);
                    //if(nc2==0)c2=1;
                    if (c1 != 0 || c2 != 0) {
                        nc2 = nc2 + 1;
                    }
                    if (c2 != 0) {
                       int t1 = 0, t2 = 0;
                    if (co1 >= c1) {
                        t1 = co1 - c1;
                    }
                    if (co2 >= c2) {
                        t2 = co2 - c2;
                    }
                    if (t1 + t2 <= 2) {// so no primeiro semestre
                            nc = nc + 1;
                            //  Messagebox.show("entrou");
                        } else {
                            e.setNivelFrequencia(nc2);
                            csimp.update(e);
                            bool = true;
                        }
                    }
                    if (bool) {
                        break;
                    }
                } while (c2 != 0);
                bool = false;
                e.setNivelFrequencia(nc2);
                csimp.update(e);
            }
        }
    }

    public void onRefreshNivelEst2() {
        List<Estudante> le = csimp.getAll(Estudante.class);
        Estudante e = null;
        boolean bool = false;
        final Iterator<Estudante> items = new ArrayList(le).listIterator();
        while (items.hasNext()) {
            e = items.next();
            if (e != null) {
                int nc = e.getNivelFrequencia();
                int nc2 = nc;
                int c1 = 0;
                int c2 = 0;
                par.clear();
                par.put("curso", e.getCursocurrente());
                par.put("nivel", e.getNivelFrequencia());
                par.put("planoc", e.getPlanoc());
                par.put("niv", "%" + e.getAnoIngresso() + "%");
                int co1 = csimp.findByJPQuery("from Disciplina d where d.curso=:curso and d.planoc = :planoc and d.semestre = 1 and"
                        + " d.nivel =:nivel and ((d.natraso is not null and d.natraso not like :niv) or d.natraso is null)", par).size();

                int co2 = csimp.findByJPQuery("from Disciplina d where d.curso=:curso and d.planoc = :planoc and d.semestre = 2 and"
                        + " d.nivel =:nivel and ((d.natraso is not null and d.natraso not like :niv) or d.natraso is null)", par).size();
                par.clear();
                par.put("e", e);
                par.put("planoc", e.getPlanoc());
                par.put("anoi", e.getNivelFrequencia());
                //par.put("niv", "%"+e.getAnoIngresso() + "%");
                c1 = csimp.findByJPQuery("from Inscricaodisciplina id where id.inscricao.idEstudante = :e and "
                        + "id.disciplina.nivel = :anoi and id.disciplina.planoc = :planoc and id.disciplina.semestre = 1 and (id.disciplinaActiva = 3"
                        + " and id.estado is true and ((id.notaFinal is not null and id.notaFinal >= 10)))", par).size();// or id.notaFinal is null
                c2 = csimp.findByJPQuery("from Inscricaodisciplina id where id.inscricao.idEstudante = :e and "
                        + "id.disciplina.nivel = :anoi and id.disciplina.planoc = :planoc and id.disciplina.semestre = 2 and (id.disciplinaActiva = 3"
                        + " and id.estado is true and ((id.notaFinal is not null and id.notaFinal >= 10)))", par).size();// or id.notaFinal is null
                if (c2 != 0) {
                    int t1 = 0, t2 = 0;
                    if (co1 >= c1) {
                        t1 = co1 - c1;
                    }
                    if (co2 >= c2) {
                        t2 = co2 - c2;
                    }
                    if (t1 + t2 <= 2) {// so no primeiro semestre
                        nc2 = nc2 + 1;
                    }
                }
                if (nc2 > nc) {
                    e.setNivelFrequencia(nc2);
                    csimp.updates(e);
                } else if (e.getNivelFrequencia() == 0) {
                    e.setNivelFrequencia(1);
                    csimp.updates(e);
                }
            }
        }
    }
    
    public void onRefreshNrEst() throws IOException {
        List<Estudante> le = csimp.findByJPQuery("from Estudante e where e.anoIngresso=2018", null);
        Estudante est = null;
        String oldnr = null;
        String novonr = null;
        final Iterator<Estudante> items = new ArrayList(le).listIterator();
        while (items.hasNext()) {
            est = items.next();
            par.clear();
            par.put("e", est);
            Listaadmissao la = csimp.findEntByJPQuery("from Listaadmissao l where l.idEstudante = :e", par);
            if (est != null) {
                if (la != null && la.getNumero() != null) {
                    oldnr =est.getNrEstudante();// la.getNumero();
                    novonr=la.getNumero();
                    if (!est.getNrEstudante().equals(novonr)) {
                        for (Arquivoestudante arq2 : est.getArquivoestudanteList()) {
                            String sss = UserAutentic.getPathR(arq2.getTipo().toString()) + oldnr + arq2.getNomearquivo();
                            InputStream iss = null;
                            try {
                                iss = new FileInputStream(sss);
                            } catch (java.io.FileNotFoundException ex) {

                            }
                            if (iss != null) {
                                Media pp = new AMedia(sss, FilenameUtils.getExtension(sss), URLConnection.guessContentTypeFromName(sss), iss);
                                Media mm = new AMedia(arq2.getNomearquivo(), FilenameUtils.getExtension(sss), URLConnection.guessContentTypeFromName(sss), pp.getByteData());

                                String nome = oldnr + arq2.getNomearquivo();
                                File Test = new File(UserAutentic.getPathR(arq2.getTipo().toString()) + nome);
                                Test.delete();

                                String nome2 = novonr + arq2.getNomearquivo();
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
                                String sss = UserAutentic.getPathR("3") + oldnr + mat.getMatriculaPK().getAno() + arq2.getNomearquivo();
                                InputStream iss = null;
                                try {
                                    iss = new FileInputStream(sss);
                                } catch (java.io.FileNotFoundException ex) {

                                }
                                if (iss != null) {
                                    Media pp = new AMedia(sss, FilenameUtils.getExtension(sss), URLConnection.guessContentTypeFromName(sss), iss);
                                    Media mm = new AMedia(arq2.getNomearquivo(), FilenameUtils.getExtension(sss), URLConnection.guessContentTypeFromName(sss), pp.getByteData());

                                    String nome = oldnr + arq2.getMatricula().getMatriculaPK().getAno() + arq2.getNomearquivo();
                                    File Test = new File(UserAutentic.getPathR("3") + nome);
                                    Test.delete();

                                    String nome2 = novonr + arq2.getMatricula().getMatriculaPK().getAno() + arq2.getNomearquivo();
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
                                String sss = UserAutentic.getPathR("4") + oldnr + arq.getNome() + dat;
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

                                    String sss2 = UserAutentic.getPathR("4") + novonr + arq.getNome() + dat;
                                    File fnovo = new File(sss2);
                                    try {
                                        org.zkoss.io.Files.copy(fnovo, mm.getStreamData());
                                    } catch (IOException ex) {
                                        Logger.getLogger(ProfileCreateUsersController.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                }
                            }
                        }
                        est.setNrEstudante(novonr);
                        csimp.update(est);
                        Clients.showNotification("Numero de" + est.getNomeCompleto() + " modificado com sucesso", null, null, null, 0);
                        return;
                    }
                }
            }
        }
    }
    
    
    public void onEncriptarSenha() {
        
         List<Users> us = csimp.getAll(Users.class);
         
         Users u = null;
         final Iterator<Users> items = new ArrayList(us).listIterator();
         while(items.hasNext()) {
             u = items.next();
             if(u != null){
             String password = u.getPasword();

                 PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
                 String encodedPassword = passwordEncoder.encode(password);
                 u.setPasword(encodedPassword);                
                 csimp.update(u);
             }
         }
  
        
    }
}
