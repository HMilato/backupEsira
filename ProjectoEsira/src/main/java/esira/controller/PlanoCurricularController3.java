/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package esira.controller;

import ExcelExport.BeanToExcel;
import com.mchange.v1.db.sql.CBPUtils;
import esira.domain.Areacientifica;
import esira.domain.Caracter;
import esira.domain.Curso;
import esira.domain.Disciplina;
import esira.domain.Docente;
import esira.domain.Faculdade;
import esira.domain.Funcionario;
import esira.domain.Lecciona;
import esira.domain.LeccionaPK;
import esira.domain.Matricula;
import esira.domain.Planocurricular;
import esira.domain.PlanocurricularPK;
import esira.domain.Equivalencia;
import esira.domain.EquivalenciaPK;
import esira.domain.Prescricao;
import esira.domain.Users;
import esira.service.CRUDService;
import esira.service.UserAutentic;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.zkoss.lang.Strings;
import org.zkoss.web.servlet.dsp.action.Page;
import org.zkoss.zhtml.Textarea;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.event.InputEvent;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zkplus.hibernate.HibernateUtil;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.Button;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Constraint;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Doublebox;
import org.zkoss.zul.FieldComparator;
import org.zkoss.zul.Hlayout;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Menuitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Paging;
import org.zkoss.zul.Row;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;
import org.zkoss.zul.event.PagingEvent;

/**
 *
 * @author user
 */
public class PlanoCurricularController3 extends GenericForwardComposer {

    @WireVariable
    private final CRUDService csimp = (CRUDService) SpringUtil.getBean("CRUDService");

    private static final long serialVersionUID = 43014628867656917L;

    private Window windowPlano, mDialogAddPlano;
    private Textbox txtNome;
    //private Textarea txtareaObjGeral;
    private Combobox cbcurso, cbcursop;
    private Intbox ano, idcurso;
    private Listbox lbPlano;
    private Datebox dataI, dataF;
    private Label validationFac, validationAcient, validationCurso, validationDisc;
    private Paging pagDisc;
    private Listheader lhnome;
    private String ord = "";
    private String sql = "";
    // Doublebox d;
    Checkbox ch;
    Row rw;
    String cond;
    Users usr = (Users) Sessions.getCurrent().getAttribute("user");
    private Hlayout ahead;
    String condfac = "", condnr = "", condnome = "", condgenero = "", condanoi = "", condano = "", condcurso = "";
    Textbox txProcurar, txProcNrmec;
    Map<String, Object> par = new HashMap<String, Object>();
    Map<String, Object> condpar = new HashMap<String, Object>();
    private Intbox ibProcAno, iddisc, ibitem;
    private Button btv;
    Menuitem manoi;

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        condpar.clear();
        condcurso = " and p.curso = :curso ";
        ListModel lm = cbcursop.getModel();
        if (lm != null && lm.getSize() > 0) {
            condpar.put("curso", (Curso) lm.getElementAt(0));
            setLB(0, 20);
        }
    }

    public ListModel<Curso> getCursoModel() {
        if (usr.getFaculdade().getLocalizacao() != null) {
            Faculdade f = csimp.get(Faculdade.class, usr.getFaculdade().getIdFaculdade());
            par.clear();
            par.put("fac", f);
            List<Curso> lp = csimp.findByJPQuery("from Curso c where c.faculdade = :fac", par);
            return new ListModelList<Curso>(lp);
        } else {
            List<Curso> lp = csimp.getAll(Curso.class);
            return new ListModelList<Curso>(lp);
        }
    }

    public void setLB(int i, int j) {
        if (j == 20) {
            lbPlano.setModel(new ListModelList<Planocurricular>());
        }
        List<Planocurricular> li = csimp.findByJPQueryFilter("from Planocurricular p where 1=1" + condnome + condcurso + ord, condpar, i, j);
        final Iterator<Planocurricular> items = li.iterator();
        Planocurricular e;
        lbPlano.setRows(lbPlano.getItemCount() + li.size());
        while (items.hasNext()) {
            e = items.next();
            ((ListModelList) lbPlano.getModel()).add(e);
        }
        if (li.size() < j) {
            btv.setVisible(false);
        } else {
            btv.setVisible(true);
        }
    }

    public void onSelectcbcurso() {
        if (cbcursop.getSelectedItem() != null) {
            Curso c = (Curso) cbcursop.getSelectedItem().getValue();
            condpar.replace("curso", c);
            setLB(0, 20);
        }
    }

    public void onAddPlano() {
        mDialogAddPlano.setParent(windowPlano);
        mDialogAddPlano.setTitle("Adicionar Plano");
        ((Combobox) mDialogAddPlano.getFellow("cbcurso")).setValue("------Curso------");
        mDialogAddPlano.doModal();
    }

    public void onCancelPlano() {
        clearFormAddPlano();
        mDialogAddPlano.detach();
        addPlanoConstraint();
    }

    private void clearFormAddPlano() {
        Constraint c = null;
        txtNome.setConstraint(c);
        dataI.setConstraint(c);
        dataF.setConstraint(c);
        cbcurso.setConstraint(c);
        txtNome.setValue("");
        dataI.setValue(null);
        dataF.setValue(null);
        cbcurso.setValue("------Curso------");
    }

    private void addPlanoConstraint() {
        txtNome.setConstraint(" no Empty: Insira o nome do Plano! Ex: Plano de 2013");
        dataI.setConstraint(" no Empty: Selecione uma data!");
        dataF.setConstraint(" no Empty: Selecione uma data!");
        cbcurso.setConstraint(" no Empty: Seleccione um item!");
    }

    public void onSavePlano() {
        if (!(mDialogAddPlano.getTitle().charAt(0) == 'E')) {
            PlanocurricularPK pk = new PlanocurricularPK();
            Calendar cal = new GregorianCalendar();
            cal.setTime(dataI.getValue());
            int ano = cal.get(Calendar.YEAR);
            pk.setAno(ano);
             if (cbcurso.getSelectedItem() == null) {
                cbcurso.setText("");
                cbcurso.getText();
                return;
            }
             Curso cu = (Curso) cbcurso.getSelectedItem().getValue();
             pk.setIdcurso(cu.getIdCurso());
             Planocurricular p = csimp.get(Planocurricular.class, pk);
            if(p==null){
              p = new Planocurricular();
              p.setPlanocurricularPK(pk);
            }
            
           // Planocurricular p = new Planocurricular();
            
            p.setNome(txtNome.getValue());
            p.setDatainicio(dataI.getValue());
            p.setDatafinal(dataF.getValue());
            p.setCurso(cu);
            if (!csimp.exist(p)) {
                csimp.Save(p);
                Clients.showNotification(" adicionado com sucesso", null, null, null, 2000);
            } else {
               // Clients.showNotification(p.getNome() + " Ja se encontra cadastrado", Messagebox.EXCLAMATION, null, null, 2000);
               Clients.alert("Ja se encontra cadastrado");
                return;
            }
        } else {
            Planocurricular p = csimp.get(Planocurricular.class, new PlanocurricularPK(ano.getValue(), idcurso.getValue().longValue()));
            p.setNome(txtNome.getValue());
            p.setDatainicio(dataI.getValue());
            p.setDatafinal(dataF.getValue());
//             if (cbcurso.getSelectedItem() == null) {
//                cbcurso.setText("");
//                cbcurso.getText();
//            }
//            Curso cu = (Curso) cbcurso.getSelectedItem().getValue();
//            p.setCurso(cu);
            Calendar cal = new GregorianCalendar();
            cal.setTime(dataI.getValue());
//            int ano = cal.get(Calendar.YEAR);
//            if (ano != this.ano.getValue().intValue()) {
//                csimp.delete(p);
//                p.setPlanocurricularPK(new PlanocurricularPK(ano, idcurso.getValue().longValue()));
//                csimp.Save(p);
//            } else {
                csimp.update(p);
//            }
            Clients.showNotification(" Moddificado com sucesso", null, null, null, 2000);
        }
        clearFormAddPlano();
        mDialogAddPlano.detach();
    }

    public void onEditPlano(ForwardEvent evt) throws Exception {
        Button btn = (Button) evt.getOrigin().getTarget();
        Listitem litem = (Listitem) btn.getParent().getParent();
        Planocurricular p = (Planocurricular) litem.getValue();
        Curso c = p.getCurso();
        long idc = 0;
        if (p.getCurso() != null) {
            idc = p.getCurso().getIdCurso();
        } // se aceita campos nulos
        mDialogAddPlano.setParent(windowPlano);
        mDialogAddPlano.setTitle("Editar Plano");
        mDialogAddPlano.doModal();
        ((Intbox) mDialogAddPlano.getFellow("ano")).setValue(p.getPlanocurricularPK().getAno());
        ((Intbox) mDialogAddPlano.getFellow("idcurso")).setValue(p.getCurso().getIdCurso().intValue());
        ((Textbox) mDialogAddPlano.getFellow("txtNome")).setValue(p.getNome());
        ((Datebox) mDialogAddPlano.getFellow("dataI")).setValue(p.getDatafinal());
        ((Datebox) mDialogAddPlano.getFellow("dataF")).setValue(p.getDatafinal());
       ((Combobox) mDialogAddPlano.getFellow("cbcurso")).setText(p.getCurso().getDescricao());
       // final Iterator<Comboitem> items = new ArrayList(((Combobox) mDialogAddPlano.getFellow("cbcurso")).getItems()).listIterator();
//        Comboitem cit;
//        while (items.hasNext()) {
//            cit = items.next();
//            if (((Curso) cit.getValue()).getIdCurso() == idc) {
//                ((Combobox) mDialogAddPlano.getFellow("cbcurso")).setSelectedItem(cit);
//             //   Messagebox.show("jh"+idc+" "+((Curso) cit.getValue()).getIdCurso());
//                break;
//            }
//        }
    }

    public void onDeletePlano(final ForwardEvent evt) throws Exception {
        Messagebox.show("Apagar?", "Prompt", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION,
                new EventListener() {
                    @Override
                    public void onEvent(Event evet) {
                        switch (((Integer) evet.getData()).intValue()) {
                            case Messagebox.YES:

                                Button btn = (Button) evt.getOrigin().getTarget();
                                Listitem litem = (Listitem) btn.getParent().getParent();

                                Planocurricular p = (Planocurricular) litem.getValue();
                                ((ListModelList) lbPlano.getModel()).remove(p);
                                new Listbox().appendChild(litem);
                                csimp.delete(p);
                                Clients.showNotification(" apagado com sucesso", null, null, null, 2000);
                                break;
                            case Messagebox.NO:
                                return;
                        }
                    }

                });
    }

}
