/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package esira.controller;

import esira.domain.Curso;
import esira.domain.Estudante;
import esira.domain.Faculdade;
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
import org.zkoss.zul.Window;

/**
 *
 * @author Milato
 */

public class ConsultarSaldo  extends GenericForwardComposer {

    private static final long serialVersionUID = 1L;
    @WireVariable
    private CRUDService csimpm = (CRUDService) SpringUtil.getBean("CRUDService");
    private Listbox lbtaxa;
    private  Combobox  cbtaxa;
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
 
 public void onConsultarReferencia(){
 
    winReferencia.setTitle("");
    winReferencia.setParent(winConsultar);

    winReferencia.doModal();
 
 }
 
 
        public ListModel<Taxa> getTaxaModel(){
            List<Taxa> lt = new ArrayList<Taxa>();
            Taxa t = new Taxa();
            t.setNomeTaxa("Selecione a Taxa da Referencia a Consultar");
            lt.add(t);
            List<Taxa> lt2 =  csimpm.getAll(Taxa.class);
            lt.addAll(lt2);
            return new ListModelList<Taxa>(lt);
        }
 
 public void onChange$cbtaxa() {
     
           if(cbtaxa.getSelectedIndex() != 0){
           
                Taxa tax = (Taxa) cbtaxa.getSelectedItem().getValue();
                 Messagebox.show("A taxa escolhida eh " +tax.getNomeTaxa());
           }
//        if (cbfaculdade.getSelectedIndex() != 0) {
//            condfac = " and e.cursocurrente.faculdade = :fac";
//            Faculdade f = (Faculdade) cbfaculdade.getSelectedItem().getValue();
//            if (condpar.containsKey("fac")) {
//                condpar.replace("fac", f);
//            } else {
//                condpar.put("fac", f);
//            }
//            par.clear();
//            par.put("f", f);
//            condcurso = "";
//            if (condpar.containsKey("curso")) {
//                condpar.remove("curso");
//            }
//            Curso cu = new Curso();
//            cu.setDescricao("----------Todos Cursos---------");
//            List<Curso> lc = new ArrayList<Curso>();
//            lc.add(cu);
//            List<Curso> lc2 = csimpm.findByJPQuery("from Curso c where c.faculdade = :f", par);
//            lc.addAll(lc2);
//            cbcurso.setModel(new ListModelList<Curso>(lc));
//            cbcurso.setVisible(true);
//            labelcurso.setVisible(true);
//
//        } else {
//            condfac = "";
//            condcurso = "";
//            if (condpar.containsKey("fac")) {
//                condpar.remove("fac");
//            }
//            if (condpar.containsKey("curso")) {
//                condpar.remove("curso");
//            }
//            cbcurso.setVisible(false);
//            labelcurso.setVisible(false);
//     
//            int idf = 9;
//            par.clear();
//            par.put("fac", idf);
//
//            Faculdade us = csimpm.findEntByJPQuery("from Faculdade f where f.idFaculdade = :fac", par);
// 
//            Messagebox.show("a faculdade que sera setada eh esta" +us.getDesricao());
//        }
  
    }

    
}
