/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package esira.controller;
import esira.domain.Token;
import esira.domain.Users;
import esira.domain.Utilizadorgeral;
import esira.service.CRUDService;
import static esira.service.Email.transport;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Label;

import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;
import java.util.UUID;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;
import org.apache.commons.mail.EmailException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.zkoss.zul.Messagebox;



/**
 *
 * @author Administrator
 */
public class RecuperacaoController extends GenericForwardComposer {
   
    static int c = 0;
    @WireVariable
    private final CRUDService csimp = (CRUDService) SpringUtil.getBean("CRUDService");
     private static final long serialVersionUID = 1L;
    
    private static final String SMTP_SERVER = "smtp server";
    Window visitante, win, mDialogNovaSenha, windowNovaSenha;
    Button addList, guardarLista;
    Button cancelarLista, btnMatric, btnInsc;
    Combobox cbCurso, cbTipoAdm, cbProcuracurso;
    Textbox txNome, txBI, txCont, txEmail;
    Intbox idl, ide, ibidEstudante, ibano;
    String token,tokenR, senha = "milaboss", from="helder.milato@gmail.com",  htmlText;
    String msg = "Não existe nenhum usuario com este email no Sistema", type="error", posicao="middle_center";
    String       msgErroConexao = "Não foi possível enviar o email, por favor verifique a sua Internet";
    String msgSucesso = "Enviamos para o seu email as instruções"
            + " de como recuperar a sua senha";
    int duracao = 0;
    Label bar;
    Date token_expirar, tokens_nao_usados;
    Map<String, Object> par = new HashMap<String, Object>();
  //  Users usr = (Users) Sessions.getCurrent().getAttribute("user");
   
   

    @Init
    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);

    }
    
    
     public void onObterSenha() throws EmailException, MessagingException {
         
         
        Users us = new Users();
      //  Token tk = new Token();
        par.put("usr", txEmail.getText()); 
  
       // Utilizadorgeral u = csimp.findEntByJPQuery("from Utilizadorgeral u where u.email = :usr", par);
         us  = csimp.findEntByJPQuery(" from Users u where u.email = :usr", par);
          if (us == null) {           
               
              Clients.showNotification(msg, type, null, posicao, duracao, true);
             
              }
              else {          
                   
                  //  token = Base64.getEncoder().encodeToString(txEmail.getText().getBytes());
                    UUID uuid = UUID.randomUUID();
                    tokenR = uuid.toString();
      
                    Calendar c = Calendar.getInstance();
                 //   Calendar ct = Calendar.getInstance();
                 //   ct.setTime(new Date());
                    c.setTime(new Date());
                    c.add(Calendar.MINUTE, 15);
//                    ct.add(Calendar.MINUTE, -20);
                    token_expirar = c.getTime();
//                    tokens_nao_usados = ct.getTime();
                    us.setToken(tokenR);
                   // tk.setToken(tokenR);
                    us.setValidadeToken(token_expirar);
                  //  tk.setValidadeToken(token_expirar);
                    //csimp.update(us);
                    csimp.Save(us);

                    Properties props = new Properties();
                    props.put("mail.smtp.host", "smtp.gmail.com");
                    props.put("mail.smtp.socketFactory.port", "465");
                    props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
                    props.put("mail.smtp.auth", "true");
                    props.put("mail.smtp.port", "465");
                    Session session = Session.getInstance(props);
                    if (transport != null && transport.isConnected()) {
                        System.out.println("trasport conectado");
                    }
                    else {
                          if (transport == null) {
                          System.out.println("trasport nulo");
                        //  Clients.showNotification(msgErroConexao, "error", null, posicao, duracao, true);
                          } else if (!transport.isConnected()) {
                          System.out.println("trasport desconectado");
                         // Clients.showNotification(msgErroConexao, "error", null, posicao, duracao, true);
                          }
                          transport = session.getTransport("smtp");
                          transport.connect("smtp.gmail.com", from, senha);
                          } 
        
                        try {
                           Message message = new MimeMessage(session);
                           Multipart multiPart = new MimeMultipart("alternative");

                           BodyPart messageBodyPart = new MimeBodyPart();
                           String htmlText = "<h1>Saudações "+us.getNome()+" </h1> \n" +
                           "<h2>Recebemos um pedido de recuperação de senha, se não foi você, "
                           + "por favor ignore este e-mail,</h1>\n" +
                            "<h2>mas se foi você por favor pressione no botão abaixo. Atenção: Tem no maximo 15 minutos para terminar esse processo</h2>\n" 
                            +"<a href=\"http://localhost:8082/esira/recuperacaoSenha/novaSenha.zul?tokenR="+tokenR+"\"><button style=\"background: #069cc2; border-radius: 6px; padding: 15px; cursor: pointer; color: #fff; font-size: 16px;\">Recuperar Senha</button></a>";
                           messageBodyPart.setContent(htmlText, "text/html");
                           multiPart.addBodyPart(messageBodyPart);
                           message.setContent(multiPart);


                           message.setFrom(new InternetAddress(from));
                           message.setRecipients(Message.RecipientType.TO,
                                   InternetAddress.parse(txEmail.getText()));
                           System.out.println(txEmail.getText());
                           message.setSubject("Recuperação de Senha ");
                           transport.sendMessage(message, InternetAddress.parse(txEmail.getText()));
                           System.out.println("Done");
                           Clients.showNotification(msgSucesso, "info", null, posicao, duracao, true);    
                           
                           
      
                           } catch (MessagingException e) {
                           throw new RuntimeException(e);
                           
                          
              //Clients.showNotification("Enviamos as instrucoes de recuperação de senha para o seu email"); 
        }
    }
                        
                 }  

   
    @Autowired
    @Qualifier("authenticationManager")
    protected AuthenticationManager authenticationProvider;

    
}
