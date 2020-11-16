/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package esira.matricula;

import esira.domain.Disciplinaanulada;
import esira.domain.Inscricao;
import esira.domain.Inscricaodisciplina;
import esira.domain.Listaadmissao;
import esira.domain.Matricula;
import esira.domain.MatriculaPK;
import esira.domain.Matriculaanulada;
import esira.domain.MatriculaanuladaPK;
import esira.domain.Users;
import esira.service.CRUDService;
import esira.service.UserAutentic;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.zkoss.util.media.Media;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.Button;
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
 * @author Alcidio
 */
public class AnularMatriculaController extends GenericForwardComposer {

    @WireVariable
    private Label lbCurso;
    private Label erroValidacao;
    private CRUDService csimpm = (CRUDService) SpringUtil.getBean("CRUDService");
    private Label lbNumero;
    private Label lbNome, lba;
    private Textbox txMotivo;
    private Intbox txAno, idlitem, lbano, lbestuid;
    private static org.zkoss.zul.Image imag;
    private static Media media;
    private ListModel listaMatriculados;
    private Button btnRegistar, btnCancelar, lbc;
    private Window win;
    private Listbox lbimg1;
    Map<String, Object> par = new HashMap<String, Object>();
    Users usr = (Users) Sessions.getCurrent().getAttribute("user");

    public AnularMatriculaController(Label lbCurso, Label erroValidacao, Label lbNumero, Label lbNome, Textbox txMotivo, Intbox txAno) {
        this.lbCurso = lbCurso;
        this.erroValidacao = erroValidacao;
        this.lbNumero = lbNumero;
        this.lbNome = lbNome;
        this.txMotivo = txMotivo;
        this.txAno = txAno;
    }

    public AnularMatriculaController() {
    }

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        //Users usr = (Users) Sessions.getCurrent().getAttribute("user");
    }

    public void onRegAnulacao() throws IOException {

        if (validar()) {
            erroValidacao.setValue("Alguns Campos estão vazios. Preencha Todos os campos");
        } else {
            if (validarAnoVolta()) {
                erroValidacao.setValue("O ano de Volta está fora dos parámetros");
            } else {
                Messagebox.show("Pretende anular esta Matricula?", "Atenção", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION,
                        new EventListener() {
                            @Override
                            public void onEvent(Event evet) throws IOException {
                                switch (((Integer) evet.getData()).intValue()) {
                                    case Messagebox.YES:
                                        // evet.stopPropagation();

                                        final Matriculaanulada mat = new Matriculaanulada();
                                        MatriculaPK matpk = new MatriculaPK();
                                        matpk.setIdEstudante(lbestuid.getValue().longValue());
                                        matpk.setAno(lbano.getValue());
                                        par.clear();
                                        par.put("matpk", matpk);
                                        final Matricula ma = csimpm.findEntByJPQuery("from Matricula m where m.matriculaPK =:matpk", par);
                                        mat.setMatriculaPK(matpk);
                                        mat.setMotivo(txMotivo.getValue());
                                        mat.setAnoVolta(txAno.getValue());
                                        mat.setDataAnulacao(new Date());

                                        if (idlitem.getValue() != -1) {
                                            mat.setEstado(true);
                                            mat.setDataconfirmacao(new Date());
                                            Users u = csimpm.get(Users.class, usr.getUtilizador());
                                            mat.setFuncionario(u.getIdFuncionario());
                                            Date dano = new Date();
                                            Calendar cal = new GregorianCalendar();
                                            cal.setTime(dano);
                                            int ano = cal.get(Calendar.YEAR);
                                            par.clear();

                                            par.put("e", ma.getEstudante());
                                            par.put("a", ano);
                                            Inscricao i = csimpm.findEntByJPQuery("from Inscricao i where i.idEstudante = :e and"
                                                    + " extract(year from i.dataInscricao) = :a and i.semestre = 2", par);
                                            par.clear();
                                            par.put("a", ano);
                                            par.put("e", ma.getEstudante());
                                            if (i != null) {
                                                final List<Inscricaodisciplina> lid = csimpm.findByJPQuery("from Inscricaodisciplina id where"
                                                        + " id.estado is true and id.inscricao.semestre = 2 and id.inscricao.idEstudante = :e and "
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
                                                                            anular(mat, ma, lid);
                                                                            break;
                                                                        case Messagebox.NO:
                                                                            win.detach();
                                                                            return;
                                                                    }
                                                                }
                                                            });
                                                } else {
                                                    anular(mat, ma, lid);
                                                }
                                            } else {
                                                final List<Inscricaodisciplina> lid = csimpm.findByJPQuery("from Inscricaodisciplina id where"
                                                        + " id.estado is true and id.inscricao.semestre = 1 and id.inscricao.idEstudante = :e and "
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
                                                                            anular(mat, ma, lid);
                                                                            break;
                                                                        case Messagebox.NO:
                                                                            win.detach();
                                                                            return;
                                                                    }
                                                                }
                                                            });
                                                } else {
                                                    anular(mat, ma, lid);
                                                }
                                            }
                                        } else {
                                            anular(mat, ma, new ArrayList<Inscricaodisciplina>());
                                        }
                                        break;
                                    case Messagebox.NO:
                                        return;
                                }
                            }
                        });
            }
        }
    }

    public void anular(Matriculaanulada mat, Matricula ma, List<Inscricaodisciplina> lid) throws IOException {
        final Iterator<Listitem> items1 = new ArrayList(lbimg1.getItems()).listIterator();
        Media m2 = null;
        while (items1.hasNext()) {
            m2 = (Media) items1.next().getValue();
            mat.setRequerimento(m2.getName());
        }
        if (usr.getUestudante() && !lbc.isDisabled()) {
            Clients.showNotification("Por favor anexar o seu Requerimento", "error", null, null, 0);
            return;
        }
        if (idlitem.getValue() != -1) {
            ma.setAnulada(true);
            csimpm.update(ma);
        }
        mat.setMatricula(ma);
        mat.setRenovada(false);
        if (ma.getMatriculaanulada() == null) {
            csimpm.Save(mat);
        } else {
            csimpm.update(mat);
        }
        if (!usr.getUestudante()) {
            Disciplinaanulada da = new Disciplinaanulada();
            Date d = new Date();
            da.setDataanulacao(d);
            da.setDataconfirmacao(d);
            da.setEstado(true);
            da.setIdEstudante(mat.getMatricula().getEstudante());
            Users u = csimpm.get(Users.class, usr.getUtilizador());
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
            }
        }
        par.clear();
        //List<Inscricaodisciplina> = csimpm.findEntByJPQuery("from Inscricaodisciplina where i.idEstudante = :e and extract(year from i.dataInscricao) = :ano and"
        //    + "i.inscricao", par);
        if (m2 != null) {
            String nome = lbNumero.getValue() + mat.getMatriculaPK().getAno();
            File Test = new File(UserAutentic.getPathR("5") + nome);
            try {
                org.zkoss.io.Files.copy(Test, m2.getStreamData());
            } catch (IOException ex) {
                Logger.getLogger(GuardarEstudanteController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        limpar();

        if (idlitem.getValue() != -1) {
            Clients.showNotification("Matricula Anulada com sucesso", null, null, null, 2000);
            Listitem li = ((Listbox) win.getParent().getFellow("lbEstudantes")).getItemAtIndex(idlitem.getValue());
            new Listbox().appendChild(li);
        } else {
            Clients.showNotification("Pedido de Anulação de Matricula Enviado com sucesso", null, null, null, 2000);
            lba.setValue("O Seu pedido para anular a Matricula foi enviado. \n Por favor aguarde a confirmação...");
            ((Button) win.getParent().getFellow("btnAnular")).setDisabled(true);
        }
        win.detach();
    }

    public void onBtnCancelar() {
        win.detach();
    }

    private boolean validar() {
        boolean teste = false;
        if (txAno.getText().isEmpty() || txMotivo.getValue().isEmpty()) {
            teste = true;
        }
        return teste;
    }

    private boolean validarAnoVolta() {
        boolean teste = false;
        Calendar c = new GregorianCalendar();
        Date d = new Date();
        c.setTime(d);
        if (txAno.getValue() < c.get(Calendar.YEAR)) {
            teste = true;
        }
        return teste;
    }

    private void limpar() {
        txAno.setText("");
        txMotivo.setText("");

    }

    public ListModel<Matricula> getListAmitModel() {
        return listaMatriculados = new ListModelList<Matricula>(csimpm.getAll(Matricula.class));
    }

}
