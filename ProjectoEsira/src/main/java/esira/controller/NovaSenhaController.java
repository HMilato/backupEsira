/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package esira.controller;


import esira.domain.Users;
import esira.service.CRUDService;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.Label;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

;

/**
 *
 * @author user
 */
public class NovaSenhaController extends SelectorComposer<Component> {

    private final CRUDService csimp = (CRUDService) SpringUtil.getBean("CRUDService");

    private static final long serialVersionUID = 1L;
    Window visitante, win, mDialogNovaSenha, windowNovaSenha;
    //wire components
    @Wire
    Label msg;
    @Wire
    Textbox txNovaSenha;
    @Wire
    Textbox txConfirmarSenha;
    @Wire
    String token;
    @Wire
    String tokenR;
    @Wire
    String emailRecuperado;
    @Wire
    Date token_expirar;
    Map<String, Object> par = new HashMap<String, Object>();
    String msgErro = "Desculpa, mas este link ja nao é valido, tente novamente!";
    String msgSucesso = "Parabens! Senha Recuperada Com Sucesso! Clique em Voltar para logar com a nova senha";

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        
        Execution exc = Executions.getCurrent();
     
       // token = exc.getParameter("token");
        tokenR = exc.getParameter("tokenR");
    }
    
 
    @Listen("onClick=#guardar; onOK=#senhaWin")
     public void onNovaSenha() throws NoSuchAlgorithmException {
   
     String senhaNova = txNovaSenha.getText();
     String confirmarSenha = txConfirmarSenha.getText();
     
    // emailRecuperado = new String(Base64.getDecoder().decode(token));
     Calendar c = Calendar.getInstance();
     token_expirar = c.getTime();
    
     par.clear();
     par.put("tkn",tokenR);
     par.put("validade", token_expirar);

    Users us = csimp.findEntByJPQuery("from Users us where us.token = :tkn and us.validadeToken > :validade", par);
            if (us == null) {
                 
                Clients.showNotification(msgErro, "error", null, "middle_center", 0, true); 
                 }
            else{

                     
                     if(senhaNova.compareTo(confirmarSenha) == 0) {
//                        us  = csimp.findEntByJPQuery(" from Users u where u.email = :usr", par);
//                        if (us != null){
                                           
                        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
                        String encodedPassword = passwordEncoder.encode(senhaNova);
                            
                            
                           us.setPasword(encodedPassword);
                           csimp.update(us);
                           par.clear();
                           par.put("tkn",tokenR);
                           us = csimp.findEntByJPQuery("from Users us where us.token = :tkn", par);
                           us.setToken(null);
                           us.setValidadeToken(null);
                           csimp.update(us);
                            Clients.showNotification(msgSucesso, "info", null, "middle_center", 0, true); 

                         // Executions.sendRedirect("/");
                       // }
                     
                    } else{
                            msg.setValue("As senhas devem ser as mesmas");
                        }

             }
     }
    

    @Listen("onClick=#cancelar")
    public void doCancel() {
        Executions.sendRedirect("/");
}

 

    }


