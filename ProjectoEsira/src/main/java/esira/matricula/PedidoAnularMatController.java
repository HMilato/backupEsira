/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package esira.matricula;

import esira.domain.Curso;
import esira.domain.Disciplinaanulada;
import esira.domain.Estudante;
import esira.domain.Faculdade;
import esira.domain.Inscricao;
import esira.domain.Inscricaodisciplina;
import esira.domain.Matricula;
import esira.domain.MatriculaPK;
import esira.domain.Matriculaanulada;
import esira.domain.Users;
import esira.service.CRUDService;
import esira.service.UserAutentic;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.mail.EmailException;
import org.zkoss.util.media.AMedia;
import org.zkoss.util.media.Media;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.EventQueue;
import org.zkoss.zk.ui.event.EventQueues;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Iframe;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

/**
 *
 * @author Tafula
 */
public class PedidoAnularMatController extends GenericForwardComposer {

    @WireVariable
    private CRUDService csimpm = (CRUDService) SpringUtil.getBean("CRUDService");
    private Listbox lbPAnulMat;
    private ListModel<Matricula> listEstudanteModel;
    private ListModel<Curso> listaCursoModel;
    Window win, winPAnularMat, winAnularM, winAddMotivoPM;
    Combobox cbcurso;
    Textbox txMotivoR;
    Intbox ide, idlitem, ano;
    Button btnProcurar;
    List listaM;
    Map<String, Object> par = new HashMap<String, Object>();
    Users usr = (Users) Sessions.getCurrent().getAttribute("user");
    private EventQueue eq;

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
    }

    public ListModel<Matriculaanulada> getPedidoModel() {
       // Users u = csimpm.get(Users.class, usr.getUtilizador());
        par.clear();
        Faculdade f = csimpm.get(Faculdade.class, usr.getFaculdade().getIdFaculdade());
        par.put("fac", f);
        List<Matriculaanulada> lda = csimpm.findByJPQuery("from Matriculaanulada ma where ma.dataconfirmacao is null and ma.matricula.curso.faculdade = :fac", par);
        return new ListModelList<Matriculaanulada>(lda);
    }

    public void onValidar(ForwardEvent evt) {
        Button btn = (Button) evt.getOrigin().getTarget();
        Listitem litem = (Listitem) btn.getParent().getParent();
        Matriculaanulada ma = (Matriculaanulada) litem.getValue();
        MatriculaPK mpk = new MatriculaPK(ma.getMatriculaPK().getIdEstudante(),
                ma.getMatriculaPK().getAno());
        par.clear();
        par.put("ide", mpk);
        ma = csimpm.findEntByJPQuery("from Matriculaanulada ma where ma.matriculaPK=:ide", par);
        Estudante e = ma.getMatricula().getEstudante();
        winAnularM.setParent(winPAnularMat);
        winAnularM.doModal();
        ((Label) winAnularM.getFellow("lbNome")).setValue(e.getNomeCompleto());
        ((Label) winAnularM.getFellow("lbCurso")).setValue(e.getCursocurrente().getDescricao());
        ((Intbox) winAnularM.getFellow("idlitem")).setValue(litem.getIndex());
        ((Intbox) winAnularM.getFellow("ide")).setValue(e.getIdEstudante().intValue());
        ((Intbox) winAnularM.getFellow("ano")).setValue(ma.getMatriculaPK().getAno());
        ((Label) winAnularM.getFellow("lbNumero")).setValue(e.getNrEstudante());
        ((Intbox) winAnularM.getFellow("txAno")).setValue(ma.getAnoVolta());
        ((Textbox) winAnularM.getFellow("txMotivo")).setValue(ma.getMotivo());
    }

    public void onVerRequerimento(Event evt) throws FileNotFoundException, IOException {
        MatriculaPK mpk = new MatriculaPK(ide.getValue().longValue(),
                ano.getValue());
        par.clear();
        par.put("ide", mpk);
        Matriculaanulada ma = csimpm.findEntByJPQuery("from Matriculaanulada ma where ma.matriculaPK=:ide", par);
        if (ma.getRequerimento() == null) {
            Clients.showNotification("Não existe documento", "warning", null, null, 2000);
            return;
        }
        String nre = ma.getMatricula().getEstudante().getNrEstudante();
        int ano = ma.getMatriculaPK().getAno();
        String nome = nre + ano;
        String sss = UserAutentic.getPathR("5") + nome;
        //File f = new File(ss);
        InputStream iss = new FileInputStream(sss);
        Media pp = new AMedia(sss, FilenameUtils.getExtension(sss), URLConnection.guessContentTypeFromName(sss), iss);
        Media mm = new AMedia(sss, FilenameUtils.getExtension(sss), URLConnection.guessContentTypeFromName(sss), pp.getByteData());
        Window win = (Window) Executions.createComponents("/report.zul", null, null);
        win.setTitle("Requerimento/Anulação de Matricula");
        Iframe iframe = (Iframe) win.getFellow("report");
        iframe.setContent(mm);
    }

    public void onClick$btnCnf() {
        Messagebox.show("Pretende validar este Pedido de Anulação de Matricula?", "", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION,
                new EventListener() {
                    @Override
                    public void onEvent(Event evet) throws InterruptedException {
                        switch (((Integer) evet.getData()).intValue()) {
                            case Messagebox.YES:
                                MatriculaPK mpk = new MatriculaPK(ide.getValue().longValue(),
                                        ano.getValue());
                                par.clear();
                                par.put("ide", mpk);
                                final Matriculaanulada ma = csimpm.findEntByJPQuery("from Matriculaanulada ma where ma.matriculaPK=:ide", par);
                                ma.setEstado(true);
                                Date dano = new Date();
                                Calendar cal = new GregorianCalendar();
                                cal.setTime(dano);
                                int ano = cal.get(Calendar.YEAR);
                                par.clear();
                                par.put("e", ma.getMatricula().getEstudante());
                                par.put("a", ano);
                                Inscricao i = csimpm.findEntByJPQuery("from Inscricao i where i.idEstudante = :e and"
                                        + " extract(year from i.dataInscricao) = :a and i.semestre = 2", par);
                                par.clear();
                                par.put("a", ano);
                                par.put("ide", ma.getMatricula().getEstudante());
                                if (i != null) {
                                    final List<Inscricaodisciplina> lid = csimpm.findByJPQuery("from Inscricaodisciplina id where"
                                            + " id.estado is true and id.inscricao.semestre = 2 and id.inscricao.idEstudante = :ide and "
                                            + "extract(year from id.inscricao.dataInscricao) = :a and (id.notaFinal is null or id.notaFinal < 10) and "
                                            + "id.disciplinaActiva = 3", par);
                                    if (!lid.isEmpty()) {
                                        Messagebox.show("As Inscrições feitas no 2o Semestre serão anuladas.\n"
                                                + "Para não anular as Inscrições feitas no 2o Semestre, Por favor introduza as notas Finais,"
                                                + "se estiverem disponiveis\n\n"
                                                + "CONTINUAR?", "Atenção", Messagebox.YES | Messagebox.NO, Messagebox.EXCLAMATION,
                                                new EventListener() {
                                                    @Override
                                                    public void onEvent(Event evet) throws IOException {
                                                        switch (((Integer) evet.getData()).intValue()) {
                                                            case Messagebox.YES:
                                                                anular(ma, lid);
                                                                break;
                                                            case Messagebox.NO:
                                                                //win.detach();
                                                                return;
                                                        }
                                                    }
                                                });
                                    } else {
                                        anular(ma, lid);
                                    }
                                } else {

                                    final List<Inscricaodisciplina> lid = csimpm.findByJPQuery("from Inscricaodisciplina id where"
                                            + " id.estado is true and id.inscricao.semestre = 1 and id.inscricao.idEstudante = :ide and "
                                            + "extract(year from id.inscricao.dataInscricao) = :a and (id.notaFinal is null or id.notaFinal < 10) and "
                                            + "id.disciplinaActiva = 3", par);
                                    if (!lid.isEmpty()) {
                                        Messagebox.show("As Inscrições feitas no 1o Semestre serão anuladas.\n"
                                                + "Para não anular as Inscrições feitas no 1o Semestre, Por favor introduza as notas Finais,"
                                                + "se estiverem disponiveis\n\n"
                                                + "CONTINUAR?", "Atenção", Messagebox.YES | Messagebox.NO, Messagebox.EXCLAMATION,
                                                new EventListener() {
                                                    @Override
                                                    public void onEvent(Event evet) throws IOException {
                                                        switch (((Integer) evet.getData()).intValue()) {
                                                            case Messagebox.YES:
                                                                anular(ma, lid);
                                                                break;
                                                            case Messagebox.NO:
                                                                //win.detach();
                                                                return;
                                                        }
                                                    }
                                                });
                                    } else {
                                        anular(ma, lid);
                                    }
                                }

                                break;
                            case Messagebox.NO:
                                return;
                        }
                    }
                });

    }

    public void anular(Matriculaanulada ma, List<Inscricaodisciplina> lid) {
        ma.setDataconfirmacao(new Date());
        Users u = csimpm.get(Users.class, usr.getUtilizador());
        Faculdade f = csimpm.get(Faculdade.class, usr.getFaculdade().getIdFaculdade());
        ma.setFuncionario(u.getIdFuncionario());
        Matricula man = ma.getMatricula();
        man.setAnulada(true);
        csimpm.update(man);
        csimpm.update(ma);
        Disciplinaanulada da = new Disciplinaanulada();
        Date d = new Date();
        da.setDataanulacao(d);
        da.setDataconfirmacao(d);
        da.setEstado(true);
        da.setIdEstudante(ma.getMatricula().getEstudante());
        da.setFuncionario(u.getIdFuncionario());
        da.setMotivo("Anulou a Matricula");
        csimpm.Save(da);
        if (!lid.isEmpty()) {
            final Iterator<Inscricaodisciplina> items2 = lid.listIterator();
            Inscricaodisciplina idis = null;
            while (items2.hasNext()) {
                idis = (Inscricaodisciplina) items2.next();
                idis.setEstado(false);
                idis.setAnulacao(da);
                csimpm.update(idis);
            }
            eq = EventQueues.lookup("anula" + f.getIdFaculdade(), EventQueues.APPLICATION, true);
            eq.publish(new Event("onPedidoMat", null, lid.get(0).getInscricao()));
            eq = EventQueues.lookup("anula" + da.getIdEstudante().getIdEstudante(), EventQueues.APPLICATION, true);
            eq.publish(new Event("onPedidoMat", null, lid.get(0).getInscricao()));
        }
        Clients.showNotification("A Anulação de Matricula foi validada com Sucesso", null, null, null, 2000);
//        new Listbox().appendChild(((Listbox) winAnularM.getParent().getFellow("lbPAnulMat"))
//                .getItemAtIndex(idlitem.getValue()));
        eq = EventQueues.lookup("rmatA" + f.getIdFaculdade(), EventQueues.APPLICATION, true);
        eq.publish(new Event("onPedidoMatD", null, da));
        eq = EventQueues.lookup("rmatA" + da.getIdEstudante().getIdEstudante(), EventQueues.APPLICATION, true);
        eq.publish(new Event("onPedidoMatD", null, da));
        winAnularM.detach();
    }

    public void onRejeitar(ForwardEvent event) throws IOException {
        winAddMotivoPM.setParent(winAnularM);
        winAddMotivoPM.doModal();
        ((Intbox) winAddMotivoPM.getFellow("idest")).setValue(ide.getValue());
        ((Textbox) winAddMotivoPM.getFellow("txMotivoR")).setText("O Seu Pedido de Anulação de Matricula foi rejeitado.\n Os seus dados estão incorrectos");

    }

    public void onBtnC() {
        winAddMotivoPM.detach();
    }

    public void onBtncancelar() {
        winAnularM.detach();
    }

    public void onBtnRjt() {
        Messagebox.show("Pretende Rejeitar este Pedido de Anulação de Matricula?", "", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION,
                new EventListener() {
                    @Override
                    public void onEvent(Event evet) throws InterruptedException, IOException, EmailException {
                        switch (((Integer) evet.getData()).intValue()) {
                            case Messagebox.YES:
                                MatriculaPK mpk = new MatriculaPK(((Intbox) winAddMotivoPM.getParent().getFellow("ide")).getValue().longValue(),
                                        ((Intbox) winAddMotivoPM.getParent().getFellow("ano")).getValue());
                                par.clear();
                                par.put("ide", mpk);
                                Matriculaanulada ma = csimpm.findEntByJPQuery("from Matriculaanulada ma where ma.matriculaPK=:ide", par);
                                ma.setEstado(false);
                                ma.setMotivo(txMotivoR.getValue());
                                ma.setDataconfirmacao(new Date());
                                Users u = csimpm.get(Users.class, usr.getUtilizador());
                                Faculdade f = csimpm.get(Faculdade.class, usr.getFaculdade().getIdFaculdade());
                                ma.setFuncionario(u.getIdFuncionario());
                                csimpm.update(ma);
                                Clients.showNotification("Pedido de Anulação de Matricula Rejeitado com Sucesso", null, null, null, 2000);
//                                new Listbox().appendChild(((Listbox) winAddMotivoPM.getParent().getParent().getFellow("lbPAnulMat"))
//                                        .getItemAtIndex(((Intbox) winAddMotivoPM.getParent().getFellow("idlitem")).getValue()));
                                eq = EventQueues.lookup("rmatA" + f.getIdFaculdade(), EventQueues.APPLICATION, true);
                                eq.publish(new Event("onPedidoMatD", null, ma));
                                eq = EventQueues.lookup("rmatA" + ma.getMatricula().getEstudante().getIdEstudante(), EventQueues.APPLICATION, true);
                                eq.publish(new Event("onPedidoMatD", null, ma));
                                winAddMotivoPM.getParent().detach();
                                break;
                            case Messagebox.NO:
                                return;
                        }
                    }
                });
    }
}
