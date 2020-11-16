/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package esira.service;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.DocumentException;
import esira.domain.Inscricao;
import esira.domain.Inscricaodisciplina;
import esira.domain.Matricula;
import javax.activation.DataSource;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.MultiPartEmail;
import org.apache.commons.mail.SimpleEmail;
import org.zkoss.zul.Messagebox;
import esira.service.ReciboPDF;
import static java.awt.SystemColor.text;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletContext;
import net.sf.jasperreports.engine.JRException;

import java.util.Properties;
import javax.activation.DataHandler;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import org.apache.commons.io.IOUtils;

/**
 *
 * @author Ussimane
 */
public class Email {

    public void sendEmail(String from, String to, String titulo, String msg, String senha) throws EmailException {

        // SimpleEmail email = new SimpleEmail();
        MultiPartEmail email = new MultiPartEmail();
        //Utilize o hostname do seu provedor de email
        // System.out.println("alterando hostname...");
        email.setHostName("smtp.gmail.com");
        //Quando a porta utilizada não é a padrão (gmail = 465)
        email.setSmtpPort(465);
        //Adicione os destinatários
        // email.addTo("xxx@xxx.com", "Jose");
        email.addTo(to);
        //Configure o seu email do qual enviará
        email.setFrom(from, from.substring(from.indexOf('@') + 1));
        //Messagebox.show(from.substring(from.indexOf('@')+1));
        //Adicione um assunto
        email.setSubject(titulo);
        //Adicione a mensagem do email
        email.setMsg(msg);
//        esira.service.ReciboPDF rp = new  esira.service.ReciboPDF();
//        DataSource ds = rp.reciboMatricula(img,null);
//        email.attach(ds, "recibo", "Recibo da Matricula");
        //Para autenticar no servidor é necessário chamar os dois métodos abaixo
        //   System.out.println("autenticando...");
        email.setSSL(true);
        email.setAuthentication(from, senha);

        //  System.out.println("enviando...");
        email.send();
        //   System.out.println("Email enviado!");
    }

    public void SendMailSSL(String from, String to, String titulo, String msg, String senha, String img) throws MessagingException, IOException, DocumentException, BadElementException, JRException {
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");
        Session session = Session.getInstance(props);
//        Session session = Session.getDefaultInstance(props,
//                new javax.mail.Authenticator() {
//                    @Override
//                    protected PasswordAuthentication getPasswordAuthentication() {
//                        return new PasswordAuthentication("username", "password");
//                    }
//                });
//        Transport
        if (transport != null && transport.isConnected()) {
            System.out.println("trasport conectado");
        } else {
            if (transport == null) {
                System.out.println("trasport nulo");
            } else if (!transport.isConnected()) {
                System.out.println("trasport desconectado");
            }
            transport = session.getTransport("smtp");
            transport.connect("smtp.gmail.com", from, senha);
        }
        try {
            Message message = new MimeMessage(session);
            Multipart multiPart = new MimeMultipart();
            esira.service.ReciboPDF rp = new esira.service.ReciboPDF();
            DataSource ds = rp.reciboMatricula1(img, null);
            BodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setText(msg);
            messageBodyPart.setDataHandler(new DataHandler(ds));
            messageBodyPart.setFileName("Recibos");
            multiPart.addBodyPart(messageBodyPart);
            //multiPart.addBodyPart(textPart); // <-- first
            message.setContent(multiPart);
            // message.setText(msg);
            message.setFrom(new InternetAddress(from));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(to));
            message.setSubject(titulo);
            i++;
            //  message.setText(msg + i);
            transport.sendMessage(message, InternetAddress.parse(to));
            System.out.println("Done");

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendEmailReciboMat(ServletContext sct, CRUDService csimp, String from, String to, String titulo, String msg, String senha, String img, Matricula m) throws MessagingException, DocumentException, BadElementException, IOException, JRException {
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");
        Session session = Session.getInstance(props);
//        Session session = Session.getDefaultInstance(props,
//                new javax.mail.Authenticator() {
//                    @Override
//                    protected PasswordAuthentication getPasswordAuthentication() {
//                        return new PasswordAuthentication("username", "password");
//                    }
//                });
//        Transport
        if (transport != null && transport.isConnected()) {
            System.out.println("trasport conectado");
        } else {
            if (transport == null) {
                System.out.println("trasport nulo");
            } else if (!transport.isConnected()) {
                System.out.println("trasport desconectado");
            }
            transport = session.getTransport("smtp");
            transport.connect("smtp.gmail.com", from, senha);
        }
        try {
            Message message = new MimeMessage(session);
            Multipart multiPart = new MimeMultipart("alternative");
            esira.service.ReciboPDF rp = new esira.service.ReciboPDF();
            DataSource ds = rp.reciboMatricula(img, m, csimp, sct);
            BodyPart messageBodyPart = new MimeBodyPart();
            i++;
            messageBodyPart.setText(msg);
            multiPart.addBodyPart(messageBodyPart);
            messageBodyPart = new MimeBodyPart();
            messageBodyPart.setDataHandler(new DataHandler(ds));
            messageBodyPart.setFileName("Recibo");
            multiPart.addBodyPart(messageBodyPart);
            message.setContent(multiPart);
            message.setFrom(new InternetAddress(from));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(to));
            System.out.println(to);
            message.setSubject(titulo);
//            message.setDataHandler(new DataHandler(ds));
//            message.setFileName("Recibo")
            // message.setText(msg+i);
            transport.sendMessage(message, InternetAddress.parse(to));
            System.out.println("Done");

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
    
     public void sendEmailReciboInsc(ServletContext sct, CRUDService csimp, String from, String to, String titulo, String msg, String senha, String img, Inscricao i, List<Inscricaodisciplina> li) throws EmailException, DocumentException, BadElementException, IOException, NoSuchProviderException, MessagingException, JRException {
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");
        Session session = Session.getInstance(props);
        if (transport != null && transport.isConnected()) {
            System.out.println("trasport conectado");
        } else {
            if (transport == null) {
                System.out.println("trasport nulo");
            } else if (!transport.isConnected()) {
                System.out.println("trasport desconectado");
            }
            transport = session.getTransport("smtp");
            transport.connect("smtp.gmail.com", from, senha);
        }
        try {
            Message message = new MimeMessage(session);
            Multipart multiPart = new MimeMultipart("alternative");
            esira.service.ReciboPDF rp = new esira.service.ReciboPDF();
            DataSource ds = rp.reciboInnscricao(img, i, null, csimp, sct);
            BodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setText(msg);
            multiPart.addBodyPart(messageBodyPart);
            messageBodyPart = new MimeBodyPart();
            messageBodyPart.setDataHandler(new DataHandler(ds));
            messageBodyPart.setFileName("Recibo");
            multiPart.addBodyPart(messageBodyPart);
            message.setContent(multiPart);
            message.setFrom(new InternetAddress(from));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(to));
            System.out.println(to);
            message.setSubject(titulo);
//            message.setDataHandler(new DataHandler(ds));
//            message.setFileName("Recibo")
            // message.setText(msg+i);
            transport.sendMessage(message, InternetAddress.parse(to));
            System.out.println("Done");

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
    public static int i = 0;
    public static Transport transport = null;

}
