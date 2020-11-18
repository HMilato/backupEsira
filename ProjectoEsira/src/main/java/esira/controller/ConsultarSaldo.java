/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package esira.controller;

import esira.domain.Curso;
import esira.domain.Estudante;
import esira.domain.Faculdade;
import esira.domain.Funcionario;
import esira.domain.Taxa;
import esira.domain.Transacaoestudante;
import esira.domain.Users;
import esira.service.CRUDService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

/**
 *
 * @author Milato
 */
public class ConsultarSaldo extends GenericForwardComposer {

    private static final long serialVersionUID = 1L;
    @WireVariable
    private CRUDService csimpm = (CRUDService) SpringUtil.getBean("CRUDService");
    private Listbox lbtaxa;
    private Combobox cbtaxa;
    private Textbox txtRef;
    Map<String, Object> par = new HashMap<String, Object>();
    Users usr = (Users) Sessions.getCurrent().getAttribute("user");
    Window winReferencia, winConsultar;

    @Init
    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);

        par.clear();
        par.put("usr", usr.getUtilizador());

        Users us = new Users();
        us = csimpm.findEntByJPQuery(" from Users u where u.utilizador = :usr", par);

        par.clear();
        par.put("usr", us.getIdEstudante().getIdEstudante());

        List<Transacaoestudante> te = null;
        te = csimpm.findByJPQuery("from Transacaoestudante t where t.idEstudante = :usr", par);
        listarTaxasPagas(te);

    }

    public void listarTaxasPagas(List<Transacaoestudante> lp) {
        lbtaxa.setModel(new ListModelList<>(lp));
    }

    public Float getSaldoEstudante() {

        par.clear();
        par.put("usr", usr.getUtilizador());

        Users us = new Users();
        us = csimpm.findEntByJPQuery(" from Users u where u.utilizador = :usr", par);

        par.clear();
        par.put("usr", us.getIdEstudante().getIdEstudante());
        List<Transacaoestudante> t = csimpm.findByJPQuery("from Transacaoestudante t where t.idEstudante = :usr", par);

        Transacaoestudante u = null;
        float totalpago = 0;
        float valorPorPagar = 0;
        float saldoEstudante = 0;

        final Iterator<Transacaoestudante> items = new ArrayList(t).listIterator();
        while (items.hasNext()) {
            u = items.next();
            if (u != null) {
                if (u.getPrimeiroPagamento()) {
                    valorPorPagar += u.getTipoTaxa().getValor();

                }
                totalpago += u.getValor();
            }
        }

        saldoEstudante = totalpago - valorPorPagar;

        return saldoEstudante;
    }

    public void onConsultarReferencia() {

        winReferencia.setTitle("");
        winReferencia.setParent(winConsultar);

        winReferencia.doModal();

    }

    public ListModel<Taxa> getTaxaModel() {
        List<Taxa> lt = new ArrayList<Taxa>();
        Taxa t = new Taxa();
//        par.clear();
//            par.put("usr", usr.getIdEstudante().getCursocurrente());

          //  Taxa t  = csimpm.findEntByJPQuery(" from Taxa u where u.curso = :usr", par);

        t.setNomeTaxa("Selecione a Taxa da Referencia a Consultar");
        lt.add(t);
        List<Taxa> lt2 = csimpm.getAll(Taxa.class);
        
        lt.addAll(lt2);
        return new ListModelList<Taxa>(lt);
    }
    
//    public ListModel<Taxa> getTaxaModel() {
//       
//        int cur = 1;
//        par.clear();
//         par.put("usr", cur);
////        List<Taxa> lf = csimpm.findEntByJPQuery(" from Taxa u where u.curso = :usr", par);
//        List<Taxa> lf = csimpm.getAllQuery(" from Taxa u where u.curso = :usr");
//        return new ListModelList<Taxa>(lf);
//    }
//    

    public void onChange$cbtaxa() {

        if (cbtaxa.getSelectedIndex() != 0) {
            
//            par.clear();
//            par.put("usr", usr.getIdEstudante().getCursocurrente());

            Taxa tax = (Taxa) cbtaxa.getSelectedItem().getValue();
           
            String referencia = null, idEstudante = null, ref = null, codTaxa = null;
            par.clear();
            par.put("usr", usr.getUtilizador());

            Users us = new Users();
            us = csimpm.findEntByJPQuery(" from Users u where u.utilizador = :usr", par);

            idEstudante = String.valueOf(us.getIdEstudante().getIdEstudante());
            if (idEstudante.length() <= 2) {
                referencia = idEstudante.concat("00");
            } else if (idEstudante.length() == 3) {
                referencia = idEstudante.concat("0");

            } else if (idEstudante.length() > 4) {
                referencia = idEstudante.substring(0, 4);
            } else {
             referencia = idEstudante;
            }
            String idTaxa = String.valueOf(tax.getIdTaxa());
            codTaxa = idTaxa;
           
            
            if(idTaxa.length() == 1){
                codTaxa = idTaxa.concat("0");
                 Messagebox.show(""+codTaxa);
            }else if(idTaxa.length() > 2){
                codTaxa = idTaxa.substring(0, 2);
            }
            
            ref = referencia.concat(codTaxa);

            txtRef.setVisible(true);
            txtRef.setValue(ref);
        }else{
              txtRef.setVisible(false);
        }
       
    }
    
        public void onClick$fecharTela() {
   
        winReferencia.setVisible(false);
        

    }

}
