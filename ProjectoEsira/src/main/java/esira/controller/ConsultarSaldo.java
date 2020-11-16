/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package esira.controller;

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
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;

/**
 *
 * @author Milato
 */

public class ConsultarSaldo  extends GenericForwardComposer {

    private static final long serialVersionUID = 1L;
    @WireVariable
    private CRUDService csimpm = (CRUDService) SpringUtil.getBean("CRUDService");
    private Listbox lbtaxa;
    Map<String, Object> par = new HashMap<String, Object>();
    Users usr = (Users) Sessions.getCurrent().getAttribute("user");  

    @Init
    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
  
        par.clear();
        par.put("usr", usr.getUtilizador());
      
        Users us = new Users();
        us  = csimpm.findEntByJPQuery(" from Users u where u.utilizador = :usr", par);

        par.clear();
        par.put("usr", us.getIdEstudante().getIdEstudante());
         
        List<Transacaoestudante> te =null;
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
        us  = csimpm.findEntByJPQuery(" from Users u where u.utilizador = :usr", par);
   
        par.clear();
        par.put("usr", us.getIdEstudante().getIdEstudante());  
        List<Transacaoestudante> t = csimpm.findByJPQuery("from Transacaoestudante t where t.idEstudante = :usr", par);
             
        Transacaoestudante u = null;
        float totalpago = 0; 
        float valorPorPagar = 0;
        float saldoEstudante = 0;
         
        final Iterator<Transacaoestudante> items = new ArrayList(t).listIterator();
         while(items.hasNext()) {
             u = items.next();
             if(u != null){
                 if(u.getPrimeiroPagamento()){
                    valorPorPagar += u.getTipoTaxa().getValor();
                  // valorPorPagar +=  u.getTipoTaxa().getValor();
                  // u.getTipoTaxa().getValor();
                }
                totalpago += u.getValor(); 
             }
         }     
          
        saldoEstudante = totalpago - valorPorPagar;
      //   Messagebox.show("Saldos do estudante " +u.getTipoTaxa().getValor()); 

            return  saldoEstudante;
    }
    
}
