/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package esira.matricula;

import esira.domain.Curso;
import esira.service.CRUDService;
import java.util.Calendar;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Image;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

/**
 *
 * @author DataCenter
 */
public class EditarMatriculaController extends GenericForwardComposer {
    @WireVariable
    private CRUDService csimpm = (CRUDService) SpringUtil.getBean("CRUDService");
    Window winEditaMatricula,winComprovativo;
    //dados da winEditMatricula
    Textbox txNomec,txnrEst,txCurso,txdataMatricula,txdataConfirmacao,txConf,txfConf;
    Image imagen1;
    Button btnComprovativo,btncancelar;
    
    
    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
       
    }
    
    
   public void onClick$btnComprovativo(){
        Calendar c = Calendar.getInstance();
        String nomeFile = "/comprovativos/"+txnrEst.getValue() + "_TAXA_" + c.get(Calendar.YEAR);
        //Messagebox.show(""+matriculaPrincipal.getEstudante().getNomeCompleto());
        
        
        Image Imagen1 = (Image) winComprovativo.getFellow("imagen1");
        Imagen1.setSrc(nomeFile);
        
        winComprovativo.setParent(winEditaMatricula);
               
        
        winComprovativo.doModal();
        
    }
    
   public void onClick$btncancelar(){
       winEditaMatricula.detach();
   
   }
    
}
