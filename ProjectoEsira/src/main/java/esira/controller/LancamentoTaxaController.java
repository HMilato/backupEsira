/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package esira.controller;

import esira.domain.Curso;
import esira.domain.Faculdade;
import esira.domain.Grupo;
import esira.domain.Taxa;
import esira.domain.Users;
import esira.service.CRUDService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.zkoss.bind.annotation.Init;
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
import org.zkoss.zul.Constraint;
import org.zkoss.zul.Doublebox;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

/**
 *
 * @author Milato
 */
public class LancamentoTaxaController extends GenericForwardComposer {

    private static final long serialVersionUID = 1L;
    private static int c = 0;
    @WireVariable
    private CRUDService csimpm = (CRUDService) SpringUtil.getBean("CRUDService");
    private Listbox lbtaxa;
    Map<String, Object> par = new HashMap<String, Object>();
    Users usr = (Users) Sessions.getCurrent().getAttribute("user");
    Window mDialogAddPlano, mDialogMultas, winmain;
    private Combobox cbfaculdade, cbcurso, ListFacModel;
    private Button addTaxa, addPlano;
    private Label validation, massage, labelcurso;
    private Intbox ibano, litem, idfac;
    Textbox txTaxa;
    Doublebox txValor;
    String condfac = "", condcurso = "";
    Map<String, Object> condpar = new HashMap<String, Object>();

    @Init
    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);

        List<Taxa> pa = null;
        pa = csimpm.getAllQuery("SELECT t FROM Taxa t");
        setLB(pa);
    }

    public void setLB(List<Taxa> lp) {
        lbtaxa.setModel(new ListModelList<>(lp));
    }

    public void onNovaTaxa() {

        mDialogAddPlano.setTitle("");
        mDialogAddPlano.setParent(winmain);
        c = 0;
        mDialogAddPlano.doModal();

    }

    public ListModel<Faculdade> getFaculdadeModel() {
        List<Faculdade> lf = new ArrayList<Faculdade>();
        Faculdade f = new Faculdade();
        f.setDesricao("------- Todas Faculdades-------");
        lf.add(f);
        List<Faculdade> lf2 = csimpm.getAll(Faculdade.class);
        lf.addAll(lf2);
        return new ListModelList<Faculdade>(lf);
    }

    public void onChange$cbfaculdade() {
        if (cbfaculdade.getSelectedIndex() != 0) {
            condfac = " and e.cursocurrente.faculdade = :fac";
            Faculdade f = (Faculdade) cbfaculdade.getSelectedItem().getValue();
            if (condpar.containsKey("fac")) {
                condpar.replace("fac", f);
            } else {
                condpar.put("fac", f);
            }
            par.clear();
            par.put("f", f);
            condcurso = "";
            if (condpar.containsKey("curso")) {
                condpar.remove("curso");
            }
            Curso cu = new Curso();
            cu.setDescricao("----------Todos Cursos---------");
            List<Curso> lc = new ArrayList<Curso>();
            lc.add(cu);
            List<Curso> lc2 = csimpm.findByJPQuery("from Curso c where c.faculdade = :f", par);
            lc.addAll(lc2);
            cbcurso.setModel(new ListModelList<Curso>(lc));
            cbcurso.setVisible(true);
            labelcurso.setVisible(true);

        } else {
            condfac = "";
            condcurso = "";
            if (condpar.containsKey("fac")) {
                condpar.remove("fac");
            }
            if (condpar.containsKey("curso")) {
                condpar.remove("curso");
            }
            cbcurso.setVisible(false);
            labelcurso.setVisible(false);
     
//            int idf = 9;
//            par.clear();
//            par.put("fac", idf);
//
//            Faculdade us = csimpm.findEntByJPQuery("from Faculdade f where f.idFaculdade = :fac", par);
 
          //  Messagebox.show("a faculdade que sera setada eh esta" +us.getDesricao());
        }
        // setLB(0, 20);
    }

    public void onSalvarTaxa() {

        Taxa tax = getTaxa();
        Faculdade fa = null;
        Curso curso = null;
        
        if (c == 0) {
            
            
            if (usr.getFaculdade().getLocalizacao() == null) {

                if (cbfaculdade.getSelectedItem() == null) {

                    Clients.showNotification(" Selecione a faculdade", "error", null, null, 3000);
                    return;
                } else {
                     if (cbfaculdade.getSelectedIndex() != 0) {
                    fa = csimpm.get(Faculdade.class, ((Faculdade) cbfaculdade.getSelectedItem().getValue()).getIdFaculdade());
                    tax.setFaculdade(fa);
                     }
                }
            } else {
                fa = csimpm.get(Faculdade.class, idfac.getValue());
            }

            if (cbcurso.getSelectedItem() == null) {
                Clients.showNotification(" Selecione o curso", "error", null, null, 3000);
                return;
            } //else {
         //        if (cbfaculdade.getSelectedIndex() != 0) {
//                curso = csimpm.get(Curso.class, ((Curso) cbcurso.getSelectedItem().getValue()).getIdCurso());
//                tax.setCurso(curso); 
         
                 
           // }
            
           // curso = 1;
        //   int co = 1;
            par.clear();
            par.put("f", fa);
//            par.put("c", curso);
           // par.put("c", co);
            par.put("nt", txTaxa.getText());

            Taxa ta = csimpm.findEntByJPQuery("from Taxa t where t.faculdade = :f and t.nomeTaxa = :nt ", par);
         //    Taxa ta = csimpm.findEntByJPQuery("from Taxa t where t.faculdade = :f and t.curso = :c and t.nomeTaxa = :nt ", par);
            if (ta != null) {
                Clients.showNotification(" Ja se encontra cadastrada essa taxa", "error", null, null, 3000);
                return;
            }


            tax.setNomeTaxa(txTaxa.getText());
            tax.setValor(Float.parseFloat(String.valueOf(txValor.getValue())));
     
            csimpm.Save(tax);

            ((ListModelList) lbtaxa.getModel()).add(tax);
            Clients.showNotification(" Adicionado com Sucesso", null, null, null, 0);
            mDialogAddPlano.detach();


        } else {
            csimpm.update(tax);
            ((ListModelList) lbtaxa.getModel()).set(litem.getValue(), tax);
            Clients.showNotification(" Actualizado com Sucesso", null, null, null, 0);
            mDialogAddPlano.detach();

        }

        limparCampos();

    }

    public void onEdit(ForwardEvent evt) throws Exception {

        Button btn = (Button) evt.getOrigin().getTarget();
        Listitem litem = (Listitem) btn.getParent().getParent();
        Taxa todo = (Taxa) litem.getValue();
        mDialogAddPlano.setParent(winmain);
        c = 1;
        mDialogAddPlano.doModal();
        // mDialogAddPlano.setTitle(todo.getFaculdade().getDesricao());
        // ((Combobox) mDialogAddPlano.getFellow("cbfaculdade")).setVisible(true);
        ((Intbox) mDialogAddPlano.getFellow("ibano")).setValue(todo.getIdTaxa());
        ((Intbox) mDialogAddPlano.getFellow("litem")).setValue(litem.getIndex());
        ((Intbox) mDialogAddPlano.getFellow("idfac")).setValue(todo.getFaculdade().getIdFaculdade());
        ((Combobox) mDialogAddPlano.getFellow("cbcurso")).setVisible(true);
        ((Label) mDialogAddPlano.getFellow("labelcurso")).setVisible(true);

        setTaxa(todo);

    }

    public void setTaxa(Taxa t) {
          Textbox NomeTaxa = (Textbox) mDialogAddPlano.getFellow("txTaxa");
          NomeTaxa.setValue(t.getNomeTaxa());         
          Doublebox TxValor = (Doublebox) mDialogAddPlano.getFellow("txValor");
          TxValor.setValue(t.getValor());
          Combobox faculdade = (Combobox) mDialogAddPlano.getFellow("cbfaculdade");
          faculdade.setValue(t.getFaculdade().getDesricao());
          Combobox curso = (Combobox) mDialogAddPlano.getFellow("cbcurso");
          curso.setValue(t.getCurso().getDescricao());

    }

    public Taxa getTaxa() {

        Taxa p = new Taxa();
         Faculdade fa = null;
         Curso curso = null;
        if (c == 0) {
            //p.setAno(ano);
        } else {

            p = csimpm.get(Taxa.class, ibano.getValue());

        }
        p.setNomeTaxa(txTaxa.getValue());
        p.setValor(Float.parseFloat(String.valueOf(txValor.getValue())));
         if (cbfaculdade.getSelectedIndex() != 0) {
           fa = csimpm.get(Faculdade.class, ((Faculdade) cbfaculdade.getSelectedItem().getValue()).getIdFaculdade());
           p.setFaculdade(fa);
           curso = csimpm.get(Curso.class, ((Curso) cbcurso.getSelectedItem().getValue()).getIdCurso());
           p.setCurso(curso); 

         } 
            int idf = 10;
            par.clear();
            par.put("fac", idf);

            Faculdade us = csimpm.findEntByJPQuery("from Faculdade f where f.idFaculdade = :fac", par);
            p.setFaculdade(us);
 
        
         
         
     
        return p;
    }

    public void onClick$cancelarTaxa() {
        validation.setValue("");
   
         limparCampos();
        mDialogAddPlano.setVisible(false);
        

    }

    public void onDelete(final ForwardEvent evt) throws Exception {
        Messagebox.show("Tens a certeza que desejas apagar?", "Atencao", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION,
                new EventListener() {
            @Override
            public void onEvent(Event evet) {
                switch (((Integer) evet.getData()).intValue()) {
                    case Messagebox.YES:
                        Button btn = (Button) evt.getOrigin().getTarget();
                        Listitem litem = (Listitem) btn.getParent().getParent();

                        Taxa taxa = (Taxa) litem.getValue();
                        ((ListModelList) lbtaxa.getModel()).remove(taxa);
                        new Listbox().appendChild(litem);
                        csimpm.delete(taxa);

                        Clients.showNotification("Taxa " + taxa.getNomeTaxa() + " apagada com sucesso", null, null, null, 2000);
                    case Messagebox.NO:
                        return;
                }
            }

        });
    }

    private void limparCampos() {
        Constraint c = null;
        txValor.setConstraint(c);
        txValor.setValue(null);

        txTaxa.setConstraint(c);
        txTaxa.setValue(null);

        cbfaculdade.setSelectedIndex(0);
        condpar.remove("curso");

        cbcurso.setVisible(false);
        labelcurso.setVisible(false);

    }

}
