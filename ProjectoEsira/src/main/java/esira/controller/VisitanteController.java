/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package esira.controller;

import entidade.Arquivo;
import entidade.EstudantesMatriculado;
import esira.domain.Arquivoestudante;
import esira.domain.Arquivomatricula;
import esira.domain.Bolsa;
import esira.domain.Curso;
import esira.domain.Documento;
import esira.domain.Endereco;
import esira.domain.Enderecof;
import esira.domain.Estadocivil;
import esira.domain.Estudante;
import esira.domain.Ingressobolseiro;
import esira.domain.Ingressoexameadmissao;
import esira.domain.Ingressomudancauniversidade;
import esira.domain.Listaadmissao;
import esira.domain.Matricula;
import esira.domain.MatriculaPK;
import esira.domain.Pais;
import esira.domain.PlanificacaoAnoLectivo;
import esira.domain.Profissao;
import esira.domain.Provincia;
import esira.domain.Tipodocumento;
import esira.domain.Users;
import esira.domain.Viaingresso;
import esira.hibernate.Login;
import esira.hibernate.MultiTenantConnectionProviderImpl;
import esira.hibernate.TenantIdResolver;
import esira.service.CRUDService;
import esira.service.UserAutentic;
import esira.service.Utilizador;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.sql.PreparedStatement;
//import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AnonymousAuthenticationProvider;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.zkoss.bind.annotation.Init;
import org.zkoss.image.AImage;
import org.zkoss.image.Image;
import org.zkoss.util.media.AMedia;
import org.zkoss.util.media.Media;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.EventQueue;
import org.zkoss.zk.ui.event.EventQueues;
import org.zkoss.zk.ui.event.InputEvent;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Constraint;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Div;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Radio;
import org.zkoss.zul.Row;
import org.zkoss.zul.SimpleConstraint;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Tabpanel;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Vbox;
import org.zkoss.zul.Window;
import org.zkoss.zul.impl.InputElement;

/**
 *
 * @author Administrator
 */
public class VisitanteController extends GenericForwardComposer {

    static int c = 0;
    @WireVariable
    private CRUDService csimpm = (CRUDService) SpringUtil.getBean("CRUDService");
    private Label validation, message, lbmot, lbest, lbuser;
    private Listbox lbplm;
    Window visitante, win;
    Button addList, guardarLista;
    Button cancelarLista, btnMatric, btnInsc;
    Combobox cbCurso, cbTipoAdm, cbProcuracurso;
    Textbox txNome, txBI, txCont, txProcurar;
    Intbox idl, ide, ibidEstudante, ibano;
    private ListModel<Listaadmissao> listaAdmitModel;
    private ListModel<Curso> listaCursoModel;
    //  Login.setTenantId("fecn1");
    List listaM = csimpm.getAll(Listaadmissao.class);
    private Intbox ida;
    static Listaadmissao l;
    private Div divi;
    Label bar;
    private Vbox vb1, vb3, vb2, vb4, vb5;
    Map<String, Object> par = new HashMap<String, Object>();
    private long idcursoant;
    private boolean mudarcurso;
    private EventQueue eq;

    @Init
    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        
   
        listaM = csimpm.getAll(Listaadmissao.class);
//        Messagebox.show(Sessions.getCurrent().toString()+"     ."+TenantIdResolver.getTenant() + "  " + listaM.size());
//        (SecurityContextHolder.;

        Session sess = Sessions.getCurrent();
        if (!sess.hasAttribute("esvisi") && sess.hasAttribute("user")) {
            sess.removeAttribute("userCredential");
            //   Messagebox.show("removeu");
            Sessions.getCurrent().invalidate();
            Executions.getCurrent().sendRedirect("/visitante/visitante.zul");
        }
        Users us = new Users(null);
        us.setUestudante(true);
        Sessions.getCurrent().setAttribute("user", us);
        Sessions.getCurrent().setAttribute("esvisi", new Integer(1));
//        UserAutentic.setUestudante(true);
//        UserAutentic.setUser(null);
        win = (Window) Executions.createComponents("/matricula/Matricula.zul", visitante, null);
        win.setTitle("Nova Matricula");
        win.setVisible(false);
    }

    public void onBtnPesq() {

        vb1.setVisible(false);
        vb3.setVisible(false);
        vb2.setVisible(false);
        vb4.setVisible(false);
        vb5.setVisible(false);
        lbmot.setValue("");
        lbuser.setValue("");
        lbest.setValue("");
        check(divi);
        par.clear();
        par.put("nome", "%" + txNome.getValue().toLowerCase() + "%");
        par.put("curso", (Curso) cbCurso.getSelectedItem().getValue());
        //  par.put("nrbi", txBI.getValue());
       // par.put("contacto", txCont.getValue());
        Listaadmissao la = csimpm.findEntByJPQuery("from Listaadmissao l where lower(unaccent(l.nome)) like unaccent(:nome) and"
                + " l.curso = :curso", par);// and l.telefone = :contacto l.nrBI like :nrbi and
        if (la == null) {
            //Clients.showNotification("Candidato Econtrado com sucesso", null, null, null, 2000);
            par.clear();
            par.put("nome", "%" + txNome.getValue().toLowerCase() + "%");
            par.put("curso", (Curso) cbCurso.getSelectedItem().getValue());
            //  par.put("nrbi", txBI.getValue());
           // par.put("contacto", txCont.getValue());
            Estudante e = csimpm.findEntByJPQuery("from Estudante e where"
                    + " lower(unaccent(e.nomeCompleto)) like unaccent(:nome) and e.cursocurrente = :curso", par);// e.endereco.telefone = :contacto and  e.documento.nrDocumento = :nrbi and
            if (e != null) {
                verificarMatricula(e);
                eq = EventQueues.lookup("mat" + e.getIdEstudante(), EventQueues.APPLICATION, true);
                eq.subscribe(getEventoMat());
                eq = EventQueues.lookup("rmatD" + e.getIdEstudante(), EventQueues.APPLICATION, true);
                eq.subscribe(getEventoMat());
            } else {
                Clients.showNotification("O Candidato nao foi encontrado. Por favor corrija os dados ou dirige-se ao "
                        + " Departamento de Registo Academico da Faculdade que pretende ingressar", "warning", null, null, 0);
            }
        } else {
            idl.setValue(la.getIdaluno());
            //Clients.showNotification("Candidato Econtrado com sucesso", null, null, null, 2000);
            Estudante e = la.getIdEstudante();
            if (e != null) {
                verificarMatricula(e);
                eq = EventQueues.lookup("mat" + e.getIdEstudante(), EventQueues.APPLICATION, true);
                eq.subscribe(getEventoMat());
                eq = EventQueues.lookup("rmatD" + e.getIdEstudante(), EventQueues.APPLICATION, true);
                eq.subscribe(getEventoMat());
            } else {
                lbest.setValue(la.getNome() + "   --" + la.getCurso().getDescricao());
                vb2.setVisible(true);
                vb5.setVisible(true);
                btnMatric.setDisabled(false);
                btnInsc.setDisabled(true);
            }
        }
        limpar(divi);
    }

    public EventListener getEventoMat() { //problemas quando o estudante nao existe
        return new EventListener() {
            @Override
            public void onEvent(Event event) throws Exception {
                Matricula m = (Matricula) event.getData();
                if (visitante.getParent() != null) {
                    m = csimpm.get(Matricula.class, m.getMatriculaPK());
                    verificarMatricula2(m);
                }
            }
        };
    }

    public void verificarMatricula2(Matricula m) {
        Estudante e = m.getEstudante();
        if (m == null) {
            ide.setValue(e.getIdEstudante().intValue());
            vb2.setVisible(true);
            btnMatric.setDisabled(false);
            btnInsc.setDisabled(true);
        } else {
            if (m.getConfirmacao() == null) {
                ide.setValue(e.getIdEstudante().intValue());
                vb3.setVisible(true);
                vb5.setVisible(true);
                btnMatric.setDisabled(true);
                btnInsc.setDisabled(false);
            } else {
                if (!m.getEstado()) {
                    ide.setValue(e.getIdEstudante().intValue());
                    ((Intbox) win.getFellow("ibidEstudante")).setValue(e.getIdEstudante().intValue());
                    ((Intbox) win.getFellow("ibano")).setValue(m.getMatriculaPK().getAno());
                    vb4.setVisible(true);
                    if (m.getMotivomat() != null) {
                        lbmot.setValue(m.getMotivomat().getMotivo());
                    }
                    vb5.setVisible(true);
                    win.setTitle("Editar Matricula");
                    btnMatric.setDisabled(false);
                    btnInsc.setDisabled(true);
                } else {
                    ide.setValue(e.getIdEstudante().intValue());
                    vb1.setVisible(true);
                    vb5.setVisible(true);
                    lbuser.setValue("Nome de utilizador: " + e.getUsersList().get(0).getUtilizador() + " / Senha: " + e.getUsersList().get(0).getPasword());
                    btnMatric.setDisabled(true);
                    btnInsc.setDisabled(false);
                }
            }
        }
    }

    public void verificarMatricula(Estudante e) {
        if (e.getMatriculaList().size() > 1) {
            Clients.showNotification("O estudante deverá aceder a sua conta", null, null, null, 0);
            return;
        }
        lbest.setValue(e.getNomeCompleto() + "   --" + e.getCursocurrente().getDescricao());
        Date datactual = new Date();
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(datactual);
        int anoActual = calendar.get(Calendar.YEAR);
        MatriculaPK mpk = new MatriculaPK(e.getIdEstudante(), anoActual);
        par.clear();
        par.put("ide", mpk);
        Matricula m = (Matricula) csimpm.findEntByJPQuery("from Matricula m where m.matriculaPK=:ide", par);
        if (m == null) {
            ide.setValue(e.getIdEstudante().intValue());
            vb2.setVisible(true);
            btnMatric.setDisabled(false);
            btnInsc.setDisabled(true);
        } else {
            if (m.getConfirmacao() == null) {
                ide.setValue(e.getIdEstudante().intValue());
                vb3.setVisible(true);
                vb5.setVisible(true);
                btnMatric.setDisabled(true);
                btnInsc.setDisabled(false);
            } else {
                if (!m.getEstado()) {
                    ide.setValue(e.getIdEstudante().intValue());
                    ((Intbox) win.getFellow("ibidEstudante")).setValue(e.getIdEstudante().intValue());
                    ((Intbox) win.getFellow("ibano")).setValue(m.getMatriculaPK().getAno());
                    vb4.setVisible(true);
                    if (m.getMotivomat() != null) {
                        lbmot.setValue(m.getMotivomat().getMotivo());
                    }
                    vb5.setVisible(true);
                    win.setTitle("Editar Matricula");
                    btnMatric.setDisabled(false);
                    btnInsc.setDisabled(true);
                } else {
                    ide.setValue(e.getIdEstudante().intValue());
                    vb1.setVisible(true);
                    vb5.setVisible(true);
                    lbuser.setValue("Nome de utilizador: " + e.getUsersList().get(0).getUtilizador() + " / Senha: " + e.getUsersList().get(0).getPasword());
                    btnMatric.setDisabled(true);
                    btnInsc.setDisabled(false);
                }
            }
        }
    }

    public ListModel<Curso> getListCursoModel() {
        return listaCursoModel = new ListModelList<Curso>(csimpm.getAll(Curso.class));
    }

    public void onBtnMatric() throws Exception {
        Date dano = new Date();
        Matricula mat = null;
        Curso c=null;
        if (vb4.isVisible()) {
            // Messagebox.show("dsfd");
            MatriculaPK mpk = new MatriculaPK(((Intbox) win.getFellow("ibidEstudante")).getValue().longValue(),
                    ((Intbox) win.getFellow("ibano")).getValue());
            par.clear();
            par.put("ide", mpk);
            mat = (Matricula) csimpm.findByJPQuery("from Matricula m where m.matriculaPK=:ide", par).get(0);
            dano = mat.getDataMatricula();
             c = mat.getCurso();
        } else {
            Listaadmissao la = csimpm.get(Listaadmissao.class, idl.getValue());
            la = csimpm.get(Listaadmissao.class, la.getIdaluno());
            c=la.getCurso();
            if (la.getIdEstudante() != null) {
                Estudante e = la.getIdEstudante();
                if (e.getMatriculaList().size() == 1) {
                    mat = la.getIdEstudante().getMatriculaList().get(0);
                    mat = csimpm.load(Matricula.class, mat.getMatriculaPK());
                    dano = mat.getDataMatricula();
                }
            } else {
                par.clear();
                par.put("nome","%" + la.getNome().toLowerCase() + "%");
                // par.put("nrbi", la.getNrBI());
             //   par.put("contacto", la.getTelefone());
                Estudante e = csimpm.findEntByJPQuery("from Estudante e where"
                        + " lower(unaccent(e.nomeCompleto)) like unaccent(:nome) and e.estado is true", par);//e.endereco.telefone = :contacto and  e.documento.nrDocumento = :nrbi and
                if (e != null) {
                    if (e.getCursocurrente().getIdCurso() != la.getCurso().getIdCurso()
                            && (e.getCursocurrente().getIdCurso() != e.getCursoingresso().getIdCurso())) {
                        idcursoant = e.getCursocurrente().getIdCurso();
                        mudarcurso = true;
                    } else {
                        Clients.showNotification("O Candidato que se pretende Matricular ja existe no Sistema. "
                                + "Se ainda não mudou de curso, poderá recandidatar-se com um outro curso "
                                + "(Com a permissão da direcção!)", null, null, null, 2000);
                        return;
                    }
                } else {
                    mudarcurso = false;
                }
            }
        }
        Calendar cal = new GregorianCalendar();
        cal.setTime(dano);
        c = csimpm.get(Curso.class, c.getIdCurso());
        par.clear();
        par.put("fac", c.getFaculdade());
        String fn = c.getFaculdade().getDesricao();
        PlanificacaoAnoLectivo planificacaoAnoLectivo = csimpm.findEntByJPQuery("from PlanificacaoAnoLectivo p where p.faculdade = :fac", par);
        if(planificacaoAnoLectivo==null){
            Clients.showNotification("Não foi encontrado um plano de matriculas para "+fn, "error", null, null, 3000);
            return;
        }
        if (dano.before(planificacaoAnoLectivo.getDatainicioInscricao())) {
            Clients.showNotification("Por favor aguarde o periodo de Matriculas", "warning", null, null, 0, true);
            return;
        }
        if ((dano.after(planificacaoAnoLectivo.getDatainicioInscricao()) && dano.before(planificacaoAnoLectivo.getDataFimIE1())) || (mat != null && mat.getPeriodo() != null && mat.getPeriodo().equals("NORMAL"))) {
//                        par.clear();
//                        par.put("idl", idl.getValue());
            Listaadmissao todo = null;
            final HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("periodo", "NORMAL");
            win.setParent(visitante);
            win.doModal();
            Textbox tbnome = (Textbox) win.getFellow("tbnomeCompleto");
            Textbox tbnrmeca = (Textbox) win.getFellow("nrmeca");
            ((Label) win.getFellow("taxamultas")).setValue("NORMAL");
            ((Label) win.getFellow("lbtaxaMatricula")).setValue(planificacaoAnoLectivo.getTaxaMatriculaNacional().toString());
            ((Label) win.getFellow("lbtaxaMatriculaE")).setValue(planificacaoAnoLectivo.getTaxaMatriculaEstrangeiro().toString());
            ((Row) win.getFellow("rwTaxamulta30")).setVisible(false);
            ((Row) win.getFellow("rwTaxamulta15")).setVisible(false);
            ((Div) win.getFellow("prazo")).setVisible(false);
            ((Row) win.getFellow("rwmulta")).setVisible(false);
            ((Row) win.getFellow("prazomat")).setVisible(false);
            if (vb4.isVisible()) {
                renovar(mat, win);
            } else {
                todo = csimpm.get(Listaadmissao.class, idl.getValue());
                tbnome.setValue(todo.getNome());
                tbnrmeca.setValue(todo.getNumero());
                tbnome.setReadonly(true);
                novaMatricula(win, todo);
            }
//        } else if ((dano.after(planificacaoAnoLectivo.getDataFimIE1()) && dano.before(planificacaoAnoLectivo.getDataFimIE2())) || (mat != null && mat.getPeriodo() != null && mat.getPeriodo().equals("M15"))) {
//
//            Listaadmissao todo = null;
//            final HashMap<String, Object> map = new HashMap<String, Object>();
//            map.put("periodo", "M15");
//            win.setParent(visitante);
//            win.doModal();
//            Textbox tbnome = (Textbox) win.getFellow("tbnomeCompleto");
//            Textbox tbnrmeca = (Textbox) win.getFellow("nrmeca");
//            ((Label) win.getFellow("taxamultas")).setValue("M15");
//            ((Label) win.getFellow("lbtaxaMatricula")).setValue(planificacaoAnoLectivo.getTaxaMatriculaNacional().toString());
//            ((Label) win.getFellow("lbtaxaMatriculaE")).setValue(planificacaoAnoLectivo.getTaxaMatriculaEstrangeiro().toString());
//            ((Row) win.getFellow("rwTaxamulta15")).setVisible(true);
//            ((Row) win.getFellow("rwTaxamulta30")).setVisible(false);
//            ((Row) win.getFellow("prazomat")).setVisible(false);
//            if (vb4.isVisible()) {
//                renovar(mat, win);
//            } else {
//                todo = csimpm.get(Listaadmissao.class, idl.getValue());
//                tbnome.setValue(todo.getNome());
//                tbnrmeca.setValue(todo.getNumero());
//                tbnome.setReadonly(true);
//                novaMatricula(win, todo);
//            }
//        } else if ((dano.after(planificacaoAnoLectivo.getDataFimIE2()) && dano.before(planificacaoAnoLectivo.getDma1())) || (mat != null && mat.getPeriodo() != null && mat.getPeriodo().equals("M30"))) {
////                                Messagebox.show("Periodo de Multa 30 dias = " + planificacaoAnoLectivo.getPercentagemMultaMatricula30dias());
//            Listaadmissao todo = null;
//            final HashMap<String, Object> map = new HashMap<String, Object>();
//            map.put("periodo", "M30");
//            win.setParent(visitante);
//            win.doModal();
//            Textbox tbnome = (Textbox) win.getFellow("tbnomeCompleto");
//            Textbox tbnrmeca = (Textbox) win.getFellow("nrmeca");
//            ((Label) win.getFellow("taxamultas")).setValue("M30");
//            ((Label) win.getFellow("lbtaxaMatricula")).setValue(planificacaoAnoLectivo.getTaxaMatriculaNacional().toString());
//            ((Label) win.getFellow("lbtaxaMatriculaE")).setValue(planificacaoAnoLectivo.getTaxaMatriculaEstrangeiro().toString());
//            //    ((Label) win.getFellow("lbtaxaMuniversidade")).setValue(planificacaoAnoLectivo.getTaxaDeMudancaoTurno().toString());
//            ((Row) win.getFellow("rwTaxamulta30")).setVisible(true);
//            ((Row) win.getFellow("rwTaxamulta15")).setVisible(false);
//            ((Div) win.getFellow("prazo")).setVisible(false);
//            ((Row) win.getFellow("rwmulta")).setVisible(false);
//            ((Row) win.getFellow("prazomat")).setVisible(false);
//            if (vb4.isVisible()) {
//                renovar(mat, win);
//            } else {
//                todo = csimpm.get(Listaadmissao.class, idl.getValue());
//                tbnome.setValue(todo.getNome());
//                tbnrmeca.setValue(todo.getNumero());
//                tbnome.setReadonly(true);
//                novaMatricula(win, todo);
//            }
        } else {
            Clients.showNotification("Periodo de Matricula encerrado."
                    + "Contacte a direção da Faculdade que pretende ingressar", "warning", null, null, 0, true);
            return;
        }
    }

    public void novaMatricula(Window win, Listaadmissao t) throws IOException {
        ((Intbox) win.getFellow("idl")).setValue(idl.getValue());
        ((Intbox) win.getFellow("ibplanoc")).setValue(t.getCurso().getPlanoc());
        ((Intbox) win.getFellow("ibturno")).setValue(t.getTurno());
        ((Intbox) win.getFellow("ibano")).setValue(t.getAno());
        Combobox cbcur = (Combobox) win.getFellow("cbcursocurrente");
        if (mudarcurso) {
            Curso ca = csimpm.get(Curso.class, idcursoant);
            final Iterator<Comboitem> items = new ArrayList(cbcur.getItems()).listIterator();
            Comboitem cit;
            while (items.hasNext()) {
                cit = items.next();
                if (Objects.equals(((Curso) cit.getValue()).getIdCurso(), ca.getIdCurso())) {
                    cbcur.setSelectedItem(cit);
                    break;
                }
            }
            cbcur.setReadonly(true);
            cbcur.setButtonVisible(false);
            ((Radio) win.getFellow("rgNC")).setChecked(false);
            ((Radio) win.getFellow("rgSC")).setChecked(true);
            ((Row) win.getFellow("rcurso")).setVisible(false);
            ((Row) win.getFellow("rwCurso")).setVisible(true);
            Combobox cbcur2 = (Combobox) win.getFellow("cbcurso");
            final Iterator<Comboitem> items2 = new ArrayList(cbcur2.getItems()).listIterator();
            Comboitem cit2;
            while (items2.hasNext()) {
                cit2 = items2.next();
                if (Objects.equals(((Curso) cit2.getValue()).getIdCurso(), t.getCurso().getIdCurso())) {
                    cbcur2.setSelectedItem(cit2);
                    break;
                }
            }
            cbcur2.setReadonly(true);
            cbcur2.setButtonVisible(false);
        } else {
            final Iterator<Comboitem> items = new ArrayList(cbcur.getItems()).listIterator();
            Comboitem cit;
            while (items.hasNext()) {
                cit = items.next();
                if (Objects.equals(((Curso) cit.getValue()).getIdCurso(), t.getCurso().getIdCurso())) {
                    cbcur.setSelectedItem(cit);
                    break;
                }
            }
            cbcur.setReadonly(true);
            cbcur.setButtonVisible(false);
        }
        ((Combobox) win.getFellow("cbTurno")).setButtonVisible(false);
        if (t.getTurno() == 1) {
            ((Combobox) win.getFellow("cbTurno")).setSelectedIndex(0);
        } else {
            ((Combobox) win.getFellow("cbTurno")).setSelectedIndex(1);
        }
        ((Row) win.getFellow("rwnrm")).setVisible(false);
        ((Button) win.getFellow("btnsave")).setLabel("Enviar");
    }

    public void onBtnInsc() {
        Estudante e = csimpm.get(Estudante.class, ide.getValue().longValue());
        Window win = (Window) Executions.createComponents("/inscricao/inscricao.zul", visitante, null);
        ((Textbox) win.getFellow("txx")).setText(e.getIdEstudante() + "");
        win.doModal();
    }

    private void clearFormAddListA() {
        Constraint c = null;
        txNome.setConstraint(c);
        cbCurso.setConstraint(c);
        //  txBI.setConstraint(c);
        //txCont.setConstraint(c);
        txNome.setValue(null);
        // txBI.setValue(null);
        //txCont.setValue(null);
    }

    private void addListApConstraint() {
        txNome.setConstraint(" no Empty: Insira o nome!");
        cbCurso.setConstraint(" no Empty: Seleccione o curso!");
        // txBI.setConstraint(" no Empty: Insira o nr do BI/Doc.!");
       // txCont.setConstraint(" no Empty: Insira o numero de contacto!");
    }

    //Pesquisar estudante
//    public void onChanging$txProcurar(InputEvent event) throws Exception {
//
//        List<Listaadmissao> userList = new ArrayList<Listaadmissao>(listaM);
//
//        List<Listaadmissao> tempList = new ArrayList<Listaadmissao>();
//
//        if (cbProcuracurso.getSelectedItem() == null) {
//            for (Listaadmissao m : userList) {
//                if (m.getNome().regionMatches(true, 0, event.getValue(), 0, event.getValue().length())) {
//                    tempList.add(m);
//                }
//            }
//        } else {
//
//            for (Listaadmissao m : userList) {
//                if (m.getNome().regionMatches(true, 0, event.getValue(), 0, event.getValue().length())
//                        && m.getCurso() == cbProcuracurso.getSelectedItem().getValue()) {
//                    tempList.add(m);
//                }
//            }
//        }
//
//        ListModelList<Listaadmissao> model = new ListModelList<Listaadmissao>(tempList);
//        lbplm.setModel(model);
//
//    }
//    public void onSelect$cbProcuracurso() throws Exception {
//        List<Listaadmissao> l = new ArrayList<Listaadmissao>(listaM);
//
//        List<Listaadmissao> tempList = new ArrayList<Listaadmissao>();
//
//        if (txProcurar.getValue().isEmpty()) {
//            for (Listaadmissao m : l) {
//                if (m.getCurso() == cbProcuracurso.getSelectedItem().getValue()) {
//                    tempList.add(m);
//                }
//            }
//
//        } else {
//
//            for (Listaadmissao m : l) {
//                if (m.getNome().regionMatches(true, 0, txProcurar.getValue(), 0, txProcurar.getValue().length())
//                        && m.getCurso() == cbProcuracurso.getSelectedItem().getValue()) {
//                    tempList.add(m);
//                }
//            }
//
//        }
//
//        ListModelList<Listaadmissao> model = new ListModelList<Listaadmissao>(tempList);
//        lbplm.setModel(model);
//
//    }
    private void check(Component component) {
        checkIsValid(component);

        if (component.isVisible()) {
            List<Component> children = component.getChildren();
            for (Component each : children) {
                check(each);
            }
        }
    }

    private void checkParent(Component component) {
        if (component instanceof Tabpanel) {
            ((Tabpanel) component).getTabbox().setSelectedPanel((Tabpanel) component);
        } else {
            checkParent(component.getParent());
        }
    }

    private void checkIsValid(Component component) {
        if (component instanceof InputElement) {
            if ((!((InputElement) component).isValid())) {
                // Force show errorMessage
                if (!(((InputElement) component)).isVisible()) {
                    checkParent(component.getParent());
                }
                Clients.scrollIntoView(component);
                ((InputElement) component).getText();
            }
            if (((component instanceof Combobox) && ((Combobox) component).getSelectedItem() == null)) {
                if (!(((InputElement) component)).isVisible()) {
                    checkParent(component.getParent());
                }
                Clients.scrollIntoView(component);
                ((Combobox) component).setText("");
                ((Combobox) component).getValue();
            }
        }
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

    public void renovar(Matricula mat, Window win) throws IOException {
        win.setTitle("Editar Matricula");
        ((Tab) win.getFellow("tabDadosPessoais")).setSelected(true);
        //if (UserAutentic.isUestudante()) {
        ((Row) win.getFellow("rwnrm")).setVisible(false);
        ((Button) win.getFellow("btnsave")).setLabel("Enviar");
        ((Button) win.getFellow("btnsave")).setDisabled(false);
        //}
        Estudante estudante = mat.getEstudante();
        estudante = csimpm.load(Estudante.class, estudante.getIdEstudante());
        Long bolsa = estudante.getBolsa().getIdBolsa();
        Long cursocurrente = estudante.getCursocurrente().getIdCurso();
        Long cursoingresso = estudante.getCursoingresso().getIdCurso();
        Combobox cbcur = (Combobox) win.getFellow("cbcursocurrente");
        if (!Objects.equals(cursocurrente, cursoingresso)) {
            final Iterator<Comboitem> items = new ArrayList(cbcur.getItems()).listIterator();
            Comboitem cit;
            while (items.hasNext()) {
                cit = items.next();
                if (Objects.equals(((Curso) cit.getValue()).getIdCurso(), estudante.getCursoingresso().getIdCurso())) {
                    cbcur.setSelectedItem(cit);
                    break;
                }
            }
            cbcur.setReadonly(true);
            cbcur.setButtonVisible(false);
            ((Radio) win.getFellow("rgNC")).setChecked(false);
            ((Radio) win.getFellow("rgSC")).setChecked(true);
            ((Row) win.getFellow("rcurso")).setVisible(false);
            ((Row) win.getFellow("rwCurso")).setVisible(true);
            Combobox cbcur2 = (Combobox) win.getFellow("cbcurso");
            final Iterator<Comboitem> items2 = new ArrayList(cbcur2.getItems()).listIterator();
            Comboitem cit2;
            while (items2.hasNext()) {
                cit2 = items2.next();
                if (Objects.equals(((Curso) cit2.getValue()).getIdCurso(), estudante.getCursocurrente().getIdCurso())) {
                    cbcur2.setSelectedItem(cit2);
                    break;
                }
            }
            cbcur2.setReadonly(true);
            cbcur2.setButtonVisible(false);
        } else {
            final Iterator<Comboitem> items = new ArrayList(cbcur.getItems()).listIterator();
            Comboitem cit;
            while (items.hasNext()) {
                cit = items.next();
                if (Objects.equals(((Curso) cit.getValue()).getIdCurso(), estudante.getCursocurrente().getIdCurso())) {
                    cbcur.setSelectedItem(cit);
                    break;
                }
            }
            cbcur.setReadonly(true);
            cbcur.setButtonVisible(false);
        }
        int estadoCivil = estudante.getEstadoCivil().getIdEstado();
        int escolaPais = estudante.getEscolaPais().getIdPais();
        Provincia escolaprov = estudante.getEscolaprovincia();
        int nacionalidade = estudante.getNacionalidade().getIdPais();
        int viaIngresso = estudante.getViaIngresso().getIdViaIngresso();
        Endereco endereco = estudante.getEndereco();
        Provincia provnat = estudante.getProvincia();
        Enderecof enderecof = estudante.getEnderecof();
        Ingressoexameadmissao iex = estudante.getIngressoexameadmissao();
        Ingressomudancauniversidade imu = estudante.getIngressomudancauniversidade();
        Ingressobolseiro ibo = estudante.getIngressobolseiro();
        Documento doc = estudante.getDocumento();
        Profissao prof = estudante.getProfissao();

        ((Intbox) win.getFellow("ibidEstudante")).setValue(estudante.getIdEstudante().intValue());
        ((Intbox) win.getFellow("ibano")).setValue(mat.getMatriculaPK().getAno());
        ((Textbox) win.getFellow("tbnomeCompleto")).setValue(estudante.getNomeCompleto());
        ((Textbox) win.getFellow("tbapelido")).setValue(estudante.getApelido());
        ((Combobox) win.getFellow("cbTurno")).setButtonVisible(false);
        if (estudante.getTurno() == 1) {
            ((Combobox) win.getFellow("cbTurno")).setSelectedIndex(0);
        } else {
            ((Combobox) win.getFellow("cbTurno")).setSelectedIndex(1);
        }
        final Iterator<Comboitem> itemscursocurrente = new ArrayList(((Combobox) win.getFellow("cbcursocurrente")).getItems()).listIterator();
        Comboitem citcursocurrente;
        while (itemscursocurrente.hasNext()) {
            citcursocurrente = itemscursocurrente.next();
            if (Objects.equals(((Curso) citcursocurrente.getValue()).getIdCurso(), cursocurrente)) {
                ((Combobox) win.getFellow("cbcursocurrente")).setSelectedItem(citcursocurrente);
                break;
            }
        }
        ((Datebox) win.getFellow("dabdataNascimento")).setValue(estudante.getDataNascimento());
        if (!estudante.getMasculino()) {
            ((Radio) win.getFellow("rgsexo")).setChecked(false);
            ((Radio) win.getFellow("rgsm")).setChecked(true);
        } else {
            ((Radio) win.getFellow("rgsexo")).setChecked(true);
            ((Radio) win.getFellow("rgsm")).setChecked(false);
        }
        ((Textbox) win.getFellow("tbnomePai")).setValue(estudante.getNomePai());
        ((Textbox) win.getFellow("tbnomeMae")).setValue(estudante.getNomeMae());
        ((Textbox) win.getFellow("tblocalidade")).setValue(estudante.getLocalidade());
        ((Textbox) win.getFellow("tbdistrito")).setValue(estudante.getDistrito());
        ((Intbox) win.getFellow("ibanoTerMedio")).setValue(estudante.getAnoTerMedio());
        ((Textbox) win.getFellow("tbescola")).setValue(estudante.getEscola());
        //  ((Intbox) win.getFellow("ibanoAdmissao")).setValue(estudante.getAnoAdmissao());
        // ((Intbox) win.getFellow("ibnivelFrequencia")).setValue(estudante.getNivelFrequencia());
        //  ((Textbox) win.getFellow("tbpastaDocumento")).setValue(estudante.getPastaDocumento());
        ((Textbox) win.getFellow("tbnomeEncarregado")).setValue(estudante.getNomeEncarregado());
        ((Textbox) win.getFellow("tbcontactoEncarregado")).setValue(estudante.getContactoEncarregado());
        ((Textbox) win.getFellow("tbgrauParentesco")).setValue(estudante.getGrauParentesco());
        final Iterator<Comboitem> itemsnacionalidade = new ArrayList(((Combobox) win.getFellow("cbnacionalidade")).getItems()).listIterator();
        Comboitem citnacionalidade;
        while (itemsnacionalidade.hasNext()) {
            citnacionalidade = itemsnacionalidade.next();
            if (((Pais) citnacionalidade.getValue()).getIdPais() == nacionalidade) {
                ((Combobox) win.getFellow("cbnacionalidade")).setSelectedItem(citnacionalidade);
                break;
            }
        }

        if (estudante.getPrimeiraUniversidade() != null) {
            ((Radio) win.getFellow("rgNuni")).setChecked(true);
            ((Radio) win.getFellow("rgSuni")).setChecked(false);
            ((Row) win.getFellow("rwPuni")).setVisible(true);
            ((Textbox) win.getFellow("tbprimeiraUniversidade")).setValue(estudante.getPrimeiraUniversidade());
        } else {
            ((Radio) win.getFellow("rgNuni")).setChecked(false);
            ((Row) win.getFellow("rwPuni")).setVisible(false);
            ((Radio) win.getFellow("rgSuni")).setChecked(true);
        }
        ((Textbox) win.getFellow("tbidioma")).setValue(estudante.getIdioma());
        final Iterator<Comboitem> itemsestadoCivil = new ArrayList(((Combobox) win.getFellow("cbestadoCivil")).getItems()).listIterator();
        Comboitem citestadoCivil;
        while (itemsestadoCivil.hasNext()) {
            citestadoCivil = itemsestadoCivil.next();
            if (((Estadocivil) citestadoCivil.getValue()).getIdEstado() == estadoCivil) {
                ((Combobox) win.getFellow("cbestadoCivil")).setSelectedItem(citestadoCivil);
                break;
            }
        }
        ((Textbox) win.getFellow("tbemail")).setValue(estudante.getEmail());

///// Endereco
        ((Textbox) win.getFellow("tbavenidaRua")).setValue(endereco.getAvenidaRua());
        ((Textbox) win.getFellow("tbbairro")).setValue(endereco.getBairro());
        ((Textbox) win.getFellow("tbtelefone")).setValue(endereco.getTelefone());
        final Iterator<Comboitem> itemsprovincia = new ArrayList(((Combobox) win.getFellow("cbprovinciaEndAL")).getItems()).listIterator();
        Comboitem citprovincia;
        while (itemsprovincia.hasNext()) {
            citprovincia = itemsprovincia.next();
            if (Objects.equals(((Provincia) citprovincia.getValue()).getIdProvincia(), endereco.getProvincia().getIdProvincia())) {
                ((Combobox) win.getFellow("cbprovinciaEndAL")).setSelectedItem(citprovincia);
                break;
            }
        }
/////Endereco Ferias
        ((Textbox) win.getFellow("tbavenidaRua2")).setValue(enderecof.getAvenidaRua());
        ((Textbox) win.getFellow("tbbairro2")).setValue(enderecof.getBairro());
        ((Textbox) win.getFellow("tbtelefone2")).setValue(enderecof.getTelefone());
        final Iterator<Comboitem> itemsprovincia2 = new ArrayList(((Combobox) win.getFellow("cbprovinciaEndPF")).getItems()).listIterator();
        Comboitem citprovincia2 = null;
        while (itemsprovincia2.hasNext()) {
            citprovincia2 = itemsprovincia2.next();
            if (Objects.equals(((Provincia) citprovincia2.getValue()).getIdProvincia(), enderecof.getProvincia().getIdProvincia())) {
                ((Combobox) win.getFellow("cbprovinciaEndPF")).setSelectedItem(citprovincia2);
                break;
            }
        }

        ((Intbox) win.getFellow("ibnotaAdmissao")).setValue(estudante.getNotaAdmissao());
        final Iterator<Comboitem> itemsescolaPais = new ArrayList(((Combobox) win.getFellow("cbescolaPais")).getItems()).listIterator();
        Comboitem citescolaPais;
        while (itemsescolaPais.hasNext()) {
            citescolaPais = itemsescolaPais.next();
            if (((Pais) citescolaPais.getValue()).getIdPais() == escolaPais) {
                ((Combobox) win.getFellow("cbescolaPais")).setSelectedItem(citescolaPais);
                break;
            }
        }
        if (escolaprov != null) {
            Long escolaprovincia = escolaprov.getIdProvincia();
            final Iterator<Comboitem> itemsescolaProv = new ArrayList(((Combobox) win.getFellow("cbescolaProvincia")).getItems()).listIterator();
            Comboitem citescolaProv;
            while (itemsescolaProv.hasNext()) {
                citescolaProv = itemsescolaProv.next();
                if (Objects.equals(((Provincia) citescolaProv.getValue()).getIdProvincia(), escolaprovincia)) {
                    ((Combobox) win.getFellow("cbescolaProvincia")).setSelectedItem(citescolaProv);
                    break;
                }
            }
        }

        ///Provincia naturalidade
        if (provnat != null) {
            long pn = provnat.getIdProvincia();
            final Iterator<Comboitem> itemsprovnat = new ArrayList(((Combobox) win.getFellow("cbnaturalidade")).getItems()).listIterator();
            Comboitem cprov;
            while (itemsprovnat.hasNext()) {
                cprov = itemsprovnat.next();
                if (((Provincia) cprov.getValue()).getIdProvincia() == pn) {
                    ((Combobox) win.getFellow("cbnaturalidade")).setSelectedItem(cprov);
                    break;
                }
            }
            ((Combobox) win.getFellow("cbnaturalidade")).setReadonly(true);
            if (pn == 12) {
                ((Textbox) win.getFellow("tbnaturalidade")).setValue(estudante.getNaturalidade());
                ((Row) win.getFellow("rwnaturalidade")).setVisible(true);
            } else {
                ((Row) win.getFellow("rwnaturalidade")).setVisible(false);
            }
        }

        //// VIAS DE INGRESSO
        final Iterator<Comboitem> itemsviaIngresso = new ArrayList(((Combobox) win.getFellow("cbviaIngresso")).getItems()).listIterator();
        Comboitem citviaIngresso;
        while (itemsviaIngresso.hasNext()) {
            citviaIngresso = itemsviaIngresso.next();
            if (((Viaingresso) citviaIngresso.getValue()).getIdViaIngresso() == viaIngresso) {
                ((Combobox) win.getFellow("cbviaIngresso")).setSelectedItem(citviaIngresso);
                break;
            }
        }
        if (iex != null) {
            final Iterator<Comboitem> itemsprovinciaAdmissao = new ArrayList(((Combobox) win.getFellow("cbprovinciaAdmissao")).getItems()).listIterator();
            Comboitem citprovinciaAdmissao;
            while (itemsprovinciaAdmissao.hasNext()) {
                citprovinciaAdmissao = itemsprovinciaAdmissao.next();
                if (Objects.equals(((Provincia) citprovinciaAdmissao.getValue()).getIdProvincia(), iex.getProvinciaAdmissao().getIdProvincia())) {
                    ((Combobox) win.getFellow("cbprovinciaAdmissao")).setSelectedItem(citprovinciaAdmissao);
                    break;
                }
            }
            ((Row) win.getFellow("rwPexameA")).setVisible(true);
        } else if (imu != null) {
            ((Textbox) win.getFellow("tbnomeUniversidade")).setValue(imu.getNomeUniversidade());
            final Iterator<Comboitem> itemsTipou = new ArrayList(((Combobox) win.getFellow("cbtipouniversidade")).getItems()).listIterator();
            Comboitem citTipou;
            while (itemsTipou.hasNext()) {
                citTipou = itemsTipou.next();
                if (citTipou.getLabel().toString().equals(imu.getTipouniversidade())) {
                    ((Combobox) win.getFellow("cbtipouniversidade")).setSelectedItem(citTipou);
                    break;
                }
            }
            final Iterator<Comboitem> itemspaisUniversidade = new ArrayList(((Combobox) win.getFellow("cbpaisUniversidade")).getItems()).listIterator();
            Comboitem citpaisUniversidade;
            while (itemspaisUniversidade.hasNext()) {
                citpaisUniversidade = itemspaisUniversidade.next();
                if (Objects.equals(((Pais) citpaisUniversidade.getValue()).getIdPais(), imu.getPaisUniversidade().getIdPais())) {
                    ((Combobox) win.getFellow("cbpaisUniversidade")).setSelectedItem(citpaisUniversidade);
                    break;
                }
            }
            ((Row) win.getFellow("rwUmudanca")).setVisible(true);
            ((Row) win.getFellow("rwTipoU")).setVisible(true);
            ((Row) win.getFellow("rwPaisUmudanca")).setVisible(true);
        } else {
            // if (estudante.getOutraViaIngresso() != null) {
            ((Textbox) win.getFellow("tboutraViaIngresso")).setValue(estudante.getOutraViaIngresso());
            ((Row) win.getFellow("rwoutraVia")).setVisible(true);

            //}
        }

        ((Intbox) win.getFellow("ibtamAgregadoFamiliar")).setValue(estudante.getTamAgregadoFamiliar());
//////bolsa
        final Iterator<Comboitem> itemsbolsa = new ArrayList(((Combobox) win.getFellow("cbbolsa")).getItems()).listIterator();
        Comboitem citbolsa;
        while (itemsbolsa.hasNext()) {
            citbolsa = itemsbolsa.next();
            if (Objects.equals(((Bolsa) citbolsa.getValue()).getIdBolsa(), bolsa)) {
                ((Combobox) win.getFellow("cbbolsa")).setSelectedItem(citbolsa);
                break;
            }
        }
        if (ibo != null) {
            ((Datebox) win.getFellow("dabdataAprovacao")).setValue(ibo.getDataAprovacao());
            ((Textbox) win.getFellow("tbdoador")).setValue(ibo.getDoador());
            ((Row) win.getFellow("rwdataDoador")).setVisible(true);
            ((Row) win.getFellow("rwdataAprovacao")).setVisible(true);
        }
        // Messagebox.show(prof.getDescricaopr());
        if (prof != null) {
            ((Radio) win.getFellow("rgNP")).setChecked(false);
            ((Radio) win.getFellow("rgP")).setChecked(true);
            ((Textbox) win.getFellow("tbdescricaopr")).setValue(prof.getDescricaopr());
            ((Textbox) win.getFellow("tbavenidapr")).setValue(prof.getAvenidapr());
            ((Textbox) win.getFellow("tbcontactopr")).setValue(prof.getContactopr());
            ((Textbox) win.getFellow("tbbairropr")).setValue(prof.getBairropr());
//            ((Textbox) win.getFellow("tbdescricaopr")).setValue(prof.getDescricaopr());
            // ((Textbox) win.getFellow("tblocal")).setValue(prof.getLocal());
            final Iterator<Comboitem> itemsprovinciapr = new ArrayList(((Combobox) win.getFellow("cbprovinciapr")).getItems()).listIterator();
            Comboitem citprovinciapr;
            while (itemsprovinciapr.hasNext()) {
                citprovinciapr = itemsprovinciapr.next();
                if (((Provincia) citprovinciapr.getValue()).getIdProvincia() == prof.getProvinciapr().getIdProvincia()) {
                    ((Combobox) win.getFellow("cbprovinciapr")).setSelectedItem(citprovinciapr);
                    break;
                }
            }
            ((Row) win.getFellow("rwProfissao")).setVisible(true);
        }
        par.clear();
        par.put("ide", estudante);
        Arquivoestudante am = csimpm.findEntByJPQuery("from Arquivoestudante am where"
                + " am.tipo=0 and am.idEstudante = :ide", par);
        if (am != null) {
            String ss = UserAutentic.getPathR(am.getTipo().toString()) + estudante.getNrEstudante() + am.getNomearquivo();
            //File f = new File(ss);
            InputStream is = new FileInputStream(ss);
            Media p = new AMedia(ss, FilenameUtils.getExtension(ss), URLConnection.guessContentTypeFromName(ss), is);
            Media m = new AMedia(am.getNomearquivo(), FilenameUtils.getExtension(ss), URLConnection.guessContentTypeFromName(ss), p.getByteData());
//        List<Media> lmed = new ArrayList<Media>();
//        lm.add(m);
            ((Listitem) win.getFellow("lif")).setValue(m);//.setModel(new ListModelList<Media>(lm));
//        Image i = new AImage(ss);
            Image ii = new AImage(ss);
            ((org.zkoss.zul.Image) win.getFellow("picss").getChildren().get(0)).setContent(ii);
            ((Textbox) win.getFellow("tbimg")).setValue(ii.getName());
        }
        /////Tipo documento
        ((Datebox) win.getFellow("dabdataEmissao")).setValue(doc.getDataEmissao());
        ((Textbox) win.getFellow("tblocalEmissao")).setValue(doc.getLocalEmissao());
        ((Textbox) win.getFellow("tbnrDocumento")).setValue(doc.getNrDocumento());
        final Iterator<Comboitem> itemstipo = new ArrayList(((Combobox) win.getFellow("cbtipo")).getItems()).listIterator();
        Comboitem cittipo;
        while (itemstipo.hasNext()) {
            cittipo = itemstipo.next();
            if (Objects.equals(((Tipodocumento) cittipo.getValue()).getIdTipo(), doc.getTipo().getIdTipo())) {
                ((Combobox) win.getFellow("cbtipo")).setSelectedItem(cittipo);
                break;
            }
        }
        //bilhete
        List<Arquivoestudante> lam = csimpm.findByJPQuery("from Arquivoestudante am where"
                + " am.tipo=1 and am.idEstudante = :ide", par);
        List<Media> lm = new ArrayList<Media>();
        for (Arquivoestudante am2 : lam) {
            String sss = UserAutentic.getPathR(am2.getTipo().toString()) + estudante.getNrEstudante() + am2.getNomearquivo();
            //File f = new File(ss);
            InputStream iss = new FileInputStream(sss);
            //  ByteOutputStream by= new 
            Media pp = new AMedia(sss, FilenameUtils.getExtension(sss), URLConnection.guessContentTypeFromName(sss), iss);
            Media mm = new AMedia(am2.getNomearquivo(), FilenameUtils.getExtension(sss), URLConnection.guessContentTypeFromName(sss), pp.getByteData());
            //Media mmm = new
            //Image ii = new AImage(sss);
            //((org.zkoss.zul.Image) win.getFellow("picss").getChildren().get(0)).setContent(i);
            ((Textbox) win.getFellow("tbimg2")).setValue(am2.getNomearquivo());
            lm.add(mm);
            //  iss.close();
        }
        ((Listbox) win.getFellow("lbimg2")).setModel(new ListModelList(lm));
        //Certificado
        List<Arquivoestudante> lam2 = csimpm.findByJPQuery("from Arquivoestudante am where"
                + " am.tipo=2 and am.idEstudante = :ide", par);
        List<Media> lm2 = new ArrayList<Media>();
        for (Arquivoestudante am2 : lam2) {
            String sss = UserAutentic.getPathR(am2.getTipo().toString()) + estudante.getNrEstudante() + am2.getNomearquivo();
            //File f = new File(ss);
            InputStream iss = new FileInputStream(sss);
            Media pp = new AMedia(sss, FilenameUtils.getExtension(sss), URLConnection.guessContentTypeFromName(sss), iss);
            Media mm = new AMedia(am2.getNomearquivo(), FilenameUtils.getExtension(sss), URLConnection.guessContentTypeFromName(sss), pp.getByteData());
            //Image ii = new AImage(sss);
            //((org.zkoss.zul.Image) win.getFellow("picss").getChildren().get(0)).setContent(i);
            ((Textbox) win.getFellow("tbimg3")).setValue(am2.getNomearquivo());
            lm2.add(mm);
            //   iss.close();
        }
        ((Listbox) win.getFellow("lbimg1")).setModel(new ListModelList(lm2));
        //recibos
//        par.clear();
//        par.put("mat", mat);
//        List<Arquivomatricula> lam3 = csimpm.findByJPQuery("from Arquivomatricula am where"
//                + " am.matricula = :mat", par);
//        List<Arquivo> lm3 = new ArrayList<Arquivo>();
//        for (Arquivomatricula am3 : lam3) {
//            String sss = UserAutentic.getPathR("3") + estudante.getNrEstudante() + mat.getMatriculaPK().getAno() + am3.getNomearquivo();
//            //File f = new File(ss);
//            InputStream iss = new FileInputStream(sss);
//            Media pp = new AMedia(sss, FilenameUtils.getExtension(sss), URLConnection.guessContentTypeFromName(sss), iss);
//            Media mm = new AMedia(am3.getNomearquivo(), FilenameUtils.getExtension(sss), URLConnection.guessContentTypeFromName(sss), pp.getByteData());
//            //Image ii = new AImage(sss);
//            //((org.zkoss.zul.Image) win.getFellow("picss").getChildren().get(0)).setContent(i);
//            ((Textbox) win.getFellow("tbimg4")).setValue(mm.getName());
//            lm3.add(new Arquivo(am3.getNrtalao(), mm, am3.getNomearquivo()));
//        }
//        ((Listbox) win.getFellow("lbimg")).setModel(new ListModelList(lm3));
         par.clear();
        par.put("mat", mat);
        List<Arquivomatricula> lam3 = csimpm.findByJPQuery("from Arquivomatricula am where"
                + " am.matricula = :mat", par);
        List<Arquivo> lm3 = new ArrayList<Arquivo>();
        List<Arquivo> lm4 = new ArrayList<Arquivo>();
        String recibousado = "";
        for (Arquivomatricula am3 : lam3) {
            String sss = UserAutentic.getPathR("3") + estudante.getNrEstudante() + mat.getMatriculaPK().getAno() + am3.getNomearquivo();
            //File f = new File(ss);
            InputStream iss = new FileInputStream(sss);
            Media pp = new AMedia(sss, FilenameUtils.getExtension(sss), URLConnection.guessContentTypeFromName(sss), iss);
            Media mm = new AMedia(am3.getNomearquivo(), FilenameUtils.getExtension(sss), URLConnection.guessContentTypeFromName(sss), pp.getByteData());
            //Image ii = new AImage(sss);
            //((org.zkoss.zul.Image) win.getFellow("picss").getChildren().get(0)).setContent(i);
            ((Textbox) win.getFellow("tbimg4")).setValue(mm.getName());
            if (am3.getRecibousado() != null && am3.getRecibousado() == true) {
                par.clear();
                par.put("nrt", am3.getNrtalao());
                List<Arquivomatricula> lamat = csimpm.findByJPQuery("from Arquivomatricula a where a.nrtalao like :nrt", par);
                String rs = "Recibo Nr.: " + am3.getNrtalao() + " foi usado por:", rs2 = "";
                for (Arquivomatricula ama : lamat) {
                    rs2 = rs2 + "\nEstudante: " + ama.getEstudante() + "\n"
                            + "Taxa Matricula: " + ama.getValorcobrado() + " Data Matricula: " + ama.getDatamat();
                }
                recibousado = recibousado + "\n" + rs + "\n" + rs2;
            }
            if (!am3.getNrtalao().equals("0")) {
                lm3.add(new Arquivo(am3.getNrtalao(), mm, am3.getNomearquivo(), am3.getBanco(), am3.getValor(), am3.getEstudante(), am3.getDatadeposito()));
            } else {
                lm4.add(new Arquivo(am3.getNrtalao(), mm, am3.getNomearquivo(), am3.getBanco(), am3.getValor(), am3.getEstudante(), am3.getDatadeposito()));
            }
        }
        if (!recibousado.equals("")) {
            ((Row) win.getFellow("rwRU")).setVisible(true);
            ((Textbox) win.getFellow("txRU")).setText(recibousado);
        }
        ((Listbox) win.getFellow("lbimg")).setModel(new ListModelList(lm3));
        ((Listbox) win.getFellow("lbimgII")).setModel(new ListModelList(lm4));
//        }
    }

    @Autowired
    @Qualifier("authenticationManager")
    protected AuthenticationManager authenticationProvider;

    public void onSelectCurso() {
        //   Messagebox.show("sdfsdf1");
        if (cbCurso.getSelectedItem() != null) {
            //       Messagebox.show("sdfsdf2");
            Curso c = (Curso) cbCurso.getSelectedItem().getValue();
            c = csimpm.get(Curso.class, c.getIdCurso());
            String t = "fecn1";
            System.out.println("Fac: " + c.getFaculdade().getTenant() + " " + TenantIdResolver.getTenant());
            if (SecurityContextHolder.getContext() != null && SecurityContextHolder.getContext().getAuthentication() != null
                    && SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof Utilizador) {
                Utilizador usr = (Utilizador) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                if (usr != null) {
                    //  System.out.println("utiliza o esquema de utilizador"+usr.getEsquema());
                    t = usr.getEsquema();
                    //    Messagebox.show("t=="+usr.getEsquema());
                }
            }
            //   Messagebox.show(c.getFaculdade().getTenant());
            if (!c.getFaculdade().getTenant().equals(t)) {//TenantIdResolver.getTenant())) {
//                TenantIdResolver.setTenant(c.getFaculdade().getTenant());
                Session sess = Sessions.getCurrent();
                sess.setAttribute("tenant", c.getFaculdade().getTenant());
                String ten = c.getFaculdade().getTenant();
                ;
                List<GrantedAuthority> authList = new ArrayList<GrantedAuthority>();
                authList.add(new GrantedAuthorityImpl("IS_AUTHENTICATED_ANONYMOUSLY"));
                UserDetails user = new Utilizador(sess.toString(), sess.toString(), authList, c.getFaculdade().getTenant());
                UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(user, null, authList);
                token.setAuthenticated(false);
               // Authentication a=this.authenticationProvider.authenticate(token.);
                SecurityContextHolder.getContext().setAuthentication(token);
//                SecurityContext s = SecurityContextHolder.getContext();
//                List<GrantedAuthority> authList = new ArrayList<GrantedAuthority>();
//                authList.add(new GrantedAuthorityImpl("IS_AUTHENTICATED_ANONYMOUSLY"));
//                UserDetails user = new Utilizador(ten, ten, authList, c.getFaculdade().getTenant());
//                Authentication a = new AnonymousAuthenticationToken(sess.toString(), user, authList);
//                a.setAuthenticated(true);
//                AuthenticationProvider ap = new AnonymousAuthenticationProvider(sess.toString());
//                ap.authenticate(a);
//                s.setAuthentication(a);
                Executions.getCurrent().sendRedirect("/visitante/visitante.zul");
            }
        }
    }

}
