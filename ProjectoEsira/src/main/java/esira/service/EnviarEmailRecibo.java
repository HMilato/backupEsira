/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package esira.service;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.DocumentException;
import entidade.Parametro;
import esira.domain.Funcionario;
import esira.domain.Inscricao;
import esira.domain.Inscricaodisciplina;
import esira.domain.Matricula;
import esira.domain.Estudante;
import esira.domain.Faculdade;
import esira.domain.MatriculaPK;
import esira.domain.Users;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.MessagingException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JRException;
import org.apache.commons.mail.EmailException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.zkoss.zk.ui.util.Clients;

/**
 *
 * @author Ussimane
 */
@WebServlet(name = "EnviarEmailRecibo", urlPatterns = {"/EnviarEmailRecibo"})
public class EnviarEmailRecibo extends HttpServlet {
//     @Autowired
//  private MyService myService;
//
//  public void init(ServletConfig config) throws ServletException {
//    super.init(config);
//    SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this,
//      config.getServletContext());
//  }

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    private AtomicLong counter = new AtomicLong();
    private boolean running;
    private BlockingQueue<Parametro> messageQueue = null;
//    private Thread notifier = new Thread(new Runnable() {
//
//        @Override
//        public void run() {
//           
//        }
//    });

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, MessagingException, BadElementException, JRException, InterruptedException {
        response.setContentType("text/html;charset=UTF-8");
        // Gets message from request
        String tipo = request.getParameter("tipo");
        String id1 = request.getParameter("id1");
        String id2 = request.getParameter("id2");
        String idf = request.getParameter("idf");
        String ido = request.getParameter("ido");
        Parametro par = new Parametro();
        par.setTipo(Integer.parseInt(tipo));
        par.setId1(Integer.parseInt(id1));
        par.setId2(Integer.parseInt(id2));
        par.setIdf(Integer.parseInt(idf));
        par.setIdo(Integer.parseInt(ido));
//        par.setMs(ms);
        // Do some verification and save message into DB, etc.
//        if ((message != null) && !message.trim().isEmpty()) {

        if (messageQueue != null && (running == true)) {
            try {
//                Mensagem msg = new Mensagem();
//                msg.setText(message.trim());
                // Put message into messageQueue
//                ApplicationContext ac = WebApplicationContextUtils.getWebApplicationContext(getServletContext());
//                CRUDService csimp = (CRUDService) ac.getBean("CRUDService");
//                Estudante estudante = csimp.load(Estudante.class, new Long(par.getId1()));
//                System.out.println(estudante.getEmail());
                messageQueue.put(par);
            } catch (InterruptedException e) {
                throw new IOException(e);
            }
        } else {
            try {
                if (messageQueue == null) {
                    messageQueue = new LinkedBlockingQueue<Parametro>();
                }
//                Mensagem msg = new Mensagem();
//                msg.setText(message.trim());
                // Put message into messageQueue
                messageQueue.put(par);
            } catch (InterruptedException e) {
                throw new IOException(e);
            }
            Map<String, Object> cpar = new HashMap<String, Object>();
            while (running) {
                System.out.println("espera");
                try {
                    par = messageQueue.take();
                } catch (InterruptedException e) {
                    running=false;
                    Thread.currentThread().interrupt();
                }
                running = true;
                try {
                    String path = getServletContext().getRealPath("/img");
                    ApplicationContext ac = WebApplicationContextUtils.getWebApplicationContext(getServletContext());
                    CRUDService csimp = (CRUDService) ac.getBean("CRUDService");
                    if (par.getTipo() == 1) {
                        Matricula mat = csimp.get(Matricula.class, new MatriculaPK(new Long(par.getId1()), par.getId2()));
                        csimp.refresh(mat);
                        Long idest = mat.getMatriculaPK().getIdEstudante();
                        Funcionario f = csimp.get(Funcionario.class, new Long(par.getIdf()));
                        csimp.refresh(f);
                        cpar.clear();
                        cpar.put("f", f.getFaculdade());
                        Faculdade fa = csimp.findEntByJPQuery("from Faculdade f where f = :f", cpar);
                        csimp.refresh(fa);
                        Estudante estudante = csimp.get(Estudante.class, idest);
                        csimp.refresh(estudante);
                        String s = "";
                        Date dano = new Date();
                        Calendar cal = new GregorianCalendar();
                        cal.setTime(dano);
                        int ano = cal.get(Calendar.YEAR);
//                        if (par.getIdo() == 1) {
                        if (estudante.getAnoIngresso() == ano) {
                            cpar.clear();
                            cpar.put("e", estudante);
                            Users user = csimp.findEntByJPQuery("from Users u where u.idEstudante = :e", cpar);
                            s = "Para aceder a sua conta no Sistema eSira, entre com " + "Utilizador: "
                                    + user.getUtilizador() + " & Senha: " + user.getPasword();
                        }
//                        }
                        try {
                            try {
                                new Email().sendEmailReciboMat(getServletContext(), csimp, UserAutentic.getEmailP2("email", getServletContext()),
                                        estudante.getEmail(), "Validação de Matricula", "Caro Estudante " + estudante.getNomeCompleto() + ""
                                        + "\nA sua matricula foi validada com Sucesso\n"
                                        + s + "\n\n------------------------------------------\n\n\n"
                                        + "Registo Acadêmico - UniLúrio-" + fa.getDesricao(),
                                        UserAutentic.getEmailP2("senha", getServletContext()), path + "/icoul.png", mat);
                                mat = csimp.get(Matricula.class, new MatriculaPK(par.getId1(), par.getId2()));
                                mat.setEmailenviado(true);
                                csimp.update(mat);
                            } catch (DocumentException ex) {
                                Logger.getLogger(EnviarEmailRecibo.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            System.out.println(estudante.getEmail());
                        } catch (IOException ex) {
                            Logger.getLogger(EnviarEmailRecibo.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    } else if (par.getTipo() == 2) {
                        Matricula mat = csimp.get(Matricula.class, new MatriculaPK(new Long(par.getId1()), par.getId2()));
                        csimp.refresh(mat);
                        Long idest = mat.getMatriculaPK().getIdEstudante();
                        Funcionario f = csimp.get(Funcionario.class, new Long(par.getIdf()));
                        csimp.refresh(f);
                        cpar.clear();
                        cpar.put("f", f.getFaculdade());
                        Faculdade fa = csimp.findEntByJPQuery("from Faculdade f where f = :f", cpar);
                        csimp.refresh(fa);
                        Estudante estudante = csimp.get(Estudante.class, idest);
                        csimp.refresh(estudante);
                        String mot = "";
                        if (mat.getMotivomat() != null) {
                            mot = mat.getMotivomat().getMotivo();
                        }
                        try {
                            new Email().sendEmail(UserAutentic.getEmailP2("email", getServletContext()),
                                    estudante.getEmail(), "Matricula Rejeitada", "Caro Estudante " + mat.getEstudante().getNomeCompleto() + ""
                                    + "\nA sua matricula foi rejeitada!"
                                    + "\n" + mot + "\n\n\n----------------------------------------\n\n\n"
                                    + "Registo Acadêmico - UniLúrio-" + fa.getDesricao(),
                                    UserAutentic.getEmailP2("senha", getServletContext()));
                        } catch (Exception ex) {
                            Logger.getLogger(EnviarEmailRecibo.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    } else if (par.getTipo() == 3) {
                        Inscricao i = csimp.get(Inscricao.class, new Long(par.getId1()));
                        csimp.refresh(i);
                        Long idest = i.getIdEstudante().getIdEstudante();
                        Funcionario f = csimp.get(Funcionario.class, new Long(par.getIdf()));
                        csimp.refresh(f);
                        Estudante estudante = csimp.get(Estudante.class, idest);
                        csimp.refresh(estudante);
                        cpar.clear();
                        cpar.put("i", i);
                        List<Inscricaodisciplina> lid = csimp.findByJPQuery("from Inscricaodisciplina id where id.inscricao = :i and id.estado is true", cpar);
                        try {
                            new Email().sendEmailReciboInsc(getServletContext(), csimp, UserAutentic.getEmailP2("email", getServletContext()),
                                    i.getIdEstudante().getEmail(), "Validação de Inscrição", "Caro Estudante " + estudante.getNomeCompleto() + ""
                                    + "\nA sua Inscrição foi validada com Sucesso\n\n----------------------------------------\n\n\n"
                                    + "Registo Acadêmico - UniLúrio-" + f.getFaculdade().getDesricao(),
                                    UserAutentic.getEmailP2("senha", getServletContext()), path + "/icoul.png", i, lid);
                            i = csimp.get(Inscricao.class, new Long(par.getId1()));
                            i.setEmailenviado(true);
                            csimp.update(i);
                        } catch (IOException ex) {
                            Logger.getLogger(EnviarEmailRecibo.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (EmailException ex) {
                            Logger.getLogger(EnviarEmailRecibo.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (DocumentException ex) {
                            Logger.getLogger(EnviarEmailRecibo.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    } else if (par.getTipo() == 4) {
                        Inscricao i = csimp.get(Inscricao.class, new Long(par.getId1()));
                        csimp.refresh(i);
                        Long idest = i.getIdEstudante().getIdEstudante();
                        Funcionario f = csimp.get(Funcionario.class, new Long(par.getIdf()));
                        csimp.refresh(f);
                        Estudante estudante = csimp.get(Estudante.class, idest);
                        csimp.refresh(estudante);
                        String mot = "";
                        if (i.getMotivoinsc() != null) {
                            mot = i.getMotivoinsc().getMotivo();
                        }
                        try {
                            new Email().sendEmail(UserAutentic.getEmailP2("email", getServletContext()),
                                    i.getIdEstudante().getEmail(), "Inscrição Rejeitada!", "Caro Estudante " + estudante.getNomeCompleto() + ""
                                    + "\nA sua Inscricao foi rejeitada\n" + mot + "\n"
                                    + "\n\n----------------------------------------\n\n\n"
                                    + "Registo Acadêmico - UniLúrio-" + f.getFaculdade().getDesricao(),
                                    UserAutentic.getEmailP2("senha", getServletContext()));
                        } catch (EmailException ex) {
                            Logger.getLogger(EnviarEmailRecibo.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
//                    for(int i=0;i<2000;i++){
//                    
//                    }
                } catch (Exception ex) {
                    Logger.getLogger(EnviarEmailRecibo.class.getName()).log(Level.SEVERE, null, ex);
                }
                System.out.println("passou");
            }
//        }
        }
        System.out.println("fim");
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (MessagingException ex) {
            Logger.getLogger(EnviarEmailRecibo.class.getName()).log(Level.SEVERE, null, ex);
        } catch (BadElementException ex) {
            Logger.getLogger(EnviarEmailRecibo.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JRException ex) {
            Logger.getLogger(EnviarEmailRecibo.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(EnviarEmailRecibo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (MessagingException ex) {
            Logger.getLogger(EnviarEmailRecibo.class.getName()).log(Level.SEVERE, null, ex);
        } catch (BadElementException ex) {
            Logger.getLogger(EnviarEmailRecibo.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JRException ex) {
            Logger.getLogger(EnviarEmailRecibo.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(EnviarEmailRecibo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);

        // Load previous messages from DB into messageStore
        // messageStore.addAll(db.loadMessages(100));
        // Start thread
        running = true;
//        notifier.start();
    }

    @Override
    public void destroy() {
        // Stops thread and clears queue and stores
        running = false;
        //asyncContexts.clear();
        messageQueue.clear();
        messageQueue = null;
        //messageStore.clear();
    }

}
