/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package esira.matricula;

import esira.domain.Faculdade;
import esira.domain.Listaadmissao;
import esira.domain.Matricula;
import esira.domain.PlanificacaoAnoLectivo;
import esira.domain.Planomatriculas;
import esira.domain.Users;
import esira.service.CRUDService;
import esira.service.Email;
import esira.service.Populardados;
import esira.service.UserAutentic;
import java.net.UnknownHostException;
import java.util.ArrayList;
import javax.mail.PasswordAuthentication;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import org.apache.commons.mail.EmailException;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
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
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Doublebox;
import org.zkoss.zul.Grid;
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
 * @
 */
public class PlanoMatriculaController extends GenericForwardComposer {

    private Listbox lbplm, lbMutas; //------LISTA DE PLANOS
    private Label validation, massage;
    private Tab tabtaxa, tabMatricula, tabInscricao;
    private Button salvarPlano, addPlano;
    private Combobox cbfaculdade;
    private Intbox ibano, litem, idfac;
    private static int c = 0;
    @WireVariable
    private CRUDService csimpm = (CRUDService) SpringUtil.getBean("CRUDService");
    HashMap<String, Object> par = new HashMap<String, Object>();

    //------------COMPONENTES DE TAB TAXAS
    private Doublebox dbTaxMatriculaN, dbuTaxMatriculaE,
            dbuTaxEExternoN, dbuTaxEExternoE, TaxMudancaEE,
            TaxMudancaEN, TaxMudancaRE, TaxMudancaRN,
            TaxEquivalenciaE, TaxEquivalenciaN, TaxPrevisaoE,
            TaxPrevisaoP, TaxPdeclaracaoD, TaxPdeclaracaoF,
            TaxPcertificadoP, TaxPcertificadoH,
            TaxPsegundaC, TaxPmudancaT,
            TaxPlsuspensao, TaxPdiploma,
            dbTaxInscricaoN, dbTaxInscricaoE, TaxRecorrencia;

    //------------COMPONENTES DE TAB MATRICULA
    private Datebox dataInicio, datafimEpoca1, datafimEpoca2, dataSem1I, dataSem1F, dataSem2I, dataSem2F, di1, dm1, dma1;//misturou-se
    private Intbox taxaMulta15, taxaMulta30;

    //------------COMPONENTES DE TAB INSCRICAO
    private Datebox dataInicioI, datafimIEpoca1, datafimIEpoca2, dataInicioI2, datafimIEpoca21, datafimIEpoca22;
    private Intbox taxaMultaI15, taxaMultaI30;
    //-----------------------------------------------------------------

//    List<PlanoMatricula> pm = new PlanoMatriculaDao().getList();
//    private ListModel<PlanoMatricula> pmModel = new ListModelList<PlanoMatricula>(pm);
    Window mDialogAddPlano, mDialogMultas, winmain;
    Button delPlano, editPlano;
    Grid formM;
    private ListModelList<PlanificacaoAnoLectivo> listaplanos;
    Users usr = (Users) Sessions.getCurrent().getAttribute("user");

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        // Users u = csimpm.get(Users.class, usr.getUtilizador());
        List<PlanificacaoAnoLectivo> pa = null;
        if (usr.getFaculdade().getLocalizacao() == null) {
            pa = csimpm.findByJPQuery("from PlanificacaoAnoLectivo p", par);
            setLB(pa);
        } else {
            Faculdade f = csimpm.get(Faculdade.class, usr.getFaculdade().getIdFaculdade());
            par.put("fac", f);
            pa = csimpm.findByJPQuery("from PlanificacaoAnoLectivo p where p.faculdade = :fac", par);
        }
        setLB(pa);
//        Faculdade f = csimpm.get(Faculdade.class, usr.getFaculdade().getIdFaculdade());
//        par.put("fac", f);
//        PlanificacaoAnoLectivo pa = csimpm.findEntByJPQuery("from PlanificacaoAnoLectivo p where p.faculdade = :fac", par);
//        if (pa != null) {
//            addPlano.setVisible(false);
//            setLB(pa);
//        }
    }

    public void setLB(List<PlanificacaoAnoLectivo> lp) {//PlanificacaoAnoLectivo pa) {
//        List<PlanificacaoAnoLectivo> lp = new ArrayList<PlanificacaoAnoLectivo>();
//        lp.add(pa);
        lbplm.setModel(new ListModelList<PlanificacaoAnoLectivo>(lp));
    }

//-------------Salvar novo plano
    public void onSalvarPlano() {
        if (!camposNnuloInscricao()) {
            validation.setValue("Preencha todos campos de Inscrição");
            return;
        } else {
            if (datafimEpoca1.getValue().compareTo(dataInicio.getValue()) < 0
                    || datafimEpoca2.getValue().compareTo(datafimEpoca1.getValue()) < 0
                    || datafimIEpoca1.getValue().compareTo(dataInicioI.getValue()) < 0
            //        || datafimIEpoca2.getValue().compareTo(datafimIEpoca1.getValue()) < 0
                    // || dataInicioI2.getValue().compareTo(datafimIEpoca2.getValue()) < 0
                    || datafimIEpoca21.getValue().compareTo(dataInicioI2.getValue()) < 0
                    || datafimIEpoca22.getValue().compareTo(datafimIEpoca21.getValue()) < 0
                    || di1.getValue().compareTo(datafimIEpoca22.getValue()) < 0
                  //  || dma1.getValue().compareTo(datafimIEpoca2.getValue()) < 0
                    || dm1.getValue().compareTo(datafimEpoca2.getValue()) < 0) {
                Clients.showNotification("As datas finais devem ser Superiores que as datas iniciais", "error", null, null, 0);
                return;
            }
            if (dataSem1F.getValue().compareTo(dataSem1I.getValue()) < 0
                    || dataSem2F.getValue().compareTo(dataSem2I.getValue()) < 0) {
                Clients.showNotification("As datas finais devem ser Superiores que as datas iniciais", "error", null, null, 0);
                return;
            }
        }
        PlanificacaoAnoLectivo pla = getPlano();
        Date dano = new Date();
        Calendar cal = new GregorianCalendar();
        cal.setTime(dano);
        int ano = cal.get(Calendar.YEAR);
        pla.setAnolectivo(ano);
        Faculdade fa = null;
        if (c == 0) {
            if (usr.getFaculdade().getLocalizacao() == null) {
                if (cbfaculdade.getSelectedItem() == null) {
                    Clients.showNotification(" Selecione a faculdade", "error", null, null, 3000);
                    return;
                } else {
                    fa = csimpm.get(Faculdade.class, ((Faculdade) cbfaculdade.getSelectedItem().getValue()).getIdFaculdade());
                }
            } else {
                fa = csimpm.get(Faculdade.class, idfac.getValue());
            }
            par.clear();
            par.put("f", fa);
            PlanificacaoAnoLectivo pa = csimpm.findEntByJPQuery("from PlanificacaoAnoLectivo p where p.faculdade = :f", par);
            if (pa != null) {
                Clients.showNotification(" Ja se encontra cadastrado um plano para esta Faculdade", "error", null, null, 3000);
                return;
            }
            pla.setFaculdade(fa);
            csimpm.Save(pla);
            ((ListModelList) lbplm.getModel()).add(pla);
            Clients.showNotification(" Adicionado com Sucesso", null, null, null, 0);
            mDialogAddPlano.detach();
        } else {
            csimpm.update(pla);
            ((ListModelList) lbplm.getModel()).set(litem.getValue(), pla);
            Clients.showNotification(" Actualizado com Sucesso", null, null, null, 0);
            mDialogAddPlano.detach();
        }

        // Messagebox.show(""+getPlano());
    }

    public void onEdit(ForwardEvent evt) throws Exception {
//       Users u = csimpm.get(Users.class, usr.getUtilizador()); 
//        Populardados.popularDisciplinas(u.getIdFuncionario());
//        // Messagebox.show(u.getIdEstudante().getInscricaoList().get(0).getInscricaodisciplinaList().size()+"");
////        try {
//        Matricula mat = csimpm.getAll(Matricula.class).get(0);
//        String path = winmain.getDesktop().getWebApp().getRealPath("/img");
//                                try {
//                                    new Email().sendEmailReciboMat(UserAutentic.getEmailP("email"),
//                                            "ussimaria@gmail.com", "Validação de Matricula", "Prezado " + mat.getEstudante().getNomeCompleto() + ""
//                                            + "\nA sua matricula foi validada com Sucesso\n"
//                                             + "\n\n----------------------------------------\n\n\n"
//                                            + "Registo Acadêmico - UniLúrio-" + u.getFaculdade().getDesricao(),
//                                            UserAutentic.getEmailP("senha"), path + "/icoul.png", mat);
//                                } catch (EmailException uh) {
//                                    Clients.showNotification("O Envio de e-mail Falhou!", "warning", null, null, 0, true);
//                                }

        Button btn = (Button) evt.getOrigin().getTarget();
        Listitem litem = (Listitem) btn.getParent().getParent();
        PlanificacaoAnoLectivo todo = (PlanificacaoAnoLectivo) litem.getValue();
        mDialogAddPlano.setParent(winmain);
        c = 1;
        mDialogAddPlano.doModal();
        mDialogAddPlano.setTitle(todo.getFaculdade().getDesricao());
         ((Combobox) mDialogAddPlano.getFellow("cbfaculdade")).setVisible(false);
        ((Intbox) mDialogAddPlano.getFellow("ibano")).setValue(todo.getAno());
        ((Intbox) mDialogAddPlano.getFellow("litem")).setValue(litem.getIndex());
        ((Intbox) mDialogAddPlano.getFellow("idfac")).setValue(todo.getFaculdade().getIdFaculdade());
        setPlano(todo);
    }
//-----------Abrir formulario de plano

    public void onAddPlano() {
        if (usr.getFaculdade().getLocalizacao() == null) {
            ((Combobox) mDialogAddPlano.getFellow("cbfaculdade")).setVisible(true);
        } else {
            ((Combobox) mDialogAddPlano.getFellow("cbfaculdade")).setVisible(false);
            ((Intbox) mDialogAddPlano.getFellow("idfac")).setValue(usr.getFaculdade().getIdFaculdade());
        }
        Tab tab2 = (Tab) mDialogAddPlano.getFellow("tabtaxa");
        mDialogAddPlano.setTitle("");
        mDialogAddPlano.setParent(winmain);
        c = 0;
        mDialogAddPlano.doModal();
        tab2.setSelected(true);
        PlanificacaoAnoLectivo pa = csimpm.findEntByJPQuery("from PlanificacaoAnoLectivo", null);
        if (pa != null) {
            setPlano(pa);
        }
    }
    //---------------------------------------------------

    public ListModel<Faculdade> getFaculdadeModel() {
        List<Faculdade> faculdades = csimpm.getAll(Faculdade.class);
        return new ListModelList<Faculdade>(faculdades);
    }

    public void onSwitchTab$tabMatricula(ForwardEvent evt) {
        if (!camposNnuloTaxas()) {
            //   tabtaxa.setSelected(true);
            validation.setValue("Preencha todos campos de taxa");
            return;
        }
        validation.setValue("");
        final Tab tab = (Tab) evt.getOrigin().getTarget();
        tab.setSelected(true);
    }

    public void onSwitchTab$tabtaxa(ForwardEvent evt) {
        final Tab tab = (Tab) evt.getOrigin().getTarget();
        tab.setSelected(true);
    }
    //------------------------------------------------------

//    public void onClick$tabInscricao() {
//        if (!camposNnuloTaxas()) {
//            tabtaxa.setSelected(true);
//            validation.setValue("Preencha todos campos de taxa");
//            return;
//        }
//        if (!camposNnuloMatricula()) {
//            tabMatricula.setSelected(true);
//            validation.setValue("Preencha todos campos de taxa de Matricula");
//            return;
//        }
//        validation.setValue("");
//        salvarPlano.setDisabled(false);
//    }
    //-------Fechar a janela plano de matricula
    public void onClick$cancelarPlano() {
        validation.setValue("");
        limparcampos();
        mDialogAddPlano.setVisible(false);

    }

    public ListModel<PlanificacaoAnoLectivo> getPlanosmatricula() {
        // Users u = csimpm.get(Users.class, usr.getUtilizador());
        par.clear();
        Faculdade f = csimpm.get(Faculdade.class, usr.getFaculdade().getIdFaculdade());
        par.put("fac", f);
        PlanificacaoAnoLectivo p = csimpm.findEntByJPQuery("from PlanificacaoAnoLectivo p where p.faculdade = :fac", par);
        List<PlanificacaoAnoLectivo> pa = new ArrayList<>();
        pa.add(p);
        return listaplanos = new ListModelList<>(pa);
    }

    public boolean camposNnuloTaxas() {
        if (dbTaxMatriculaN.getValue() == null
                || dbuTaxMatriculaE.getValue() == null
                || dbuTaxEExternoN.getValue() == null
                || dbuTaxEExternoE.getValue() == null
                || TaxMudancaEE.getValue() == null
                || TaxMudancaEN.getValue() == null
                || TaxMudancaRE.getValue() == null
                || TaxMudancaRN.getValue() == null
                || TaxEquivalenciaE.getValue() == null
                || TaxEquivalenciaN.getValue() == null
                || TaxPrevisaoE.getValue() == null
                || TaxPrevisaoP.getValue() == null
                || TaxPdeclaracaoD.getValue() == null
                || TaxPdeclaracaoF.getValue() == null
                || TaxPcertificadoP.getValue() == null
                || TaxPcertificadoH.getValue() == null
                || TaxPsegundaC.getValue() == null
                || TaxPmudancaT.getValue() == null
                || TaxPlsuspensao.getValue() == null
                || TaxPdiploma.getValue() == null
                || dbTaxInscricaoN.getValue() == null
                || dbTaxInscricaoE.getValue() == null
                || TaxRecorrencia.getValue() == null) {
            return false;
        }

        return true;
    }

    public boolean camposNnuloMatricula() {
        if (dataInicio.getValue() == null
                || datafimEpoca1.getValue() == null
                || datafimEpoca2.getValue() == null
                || taxaMulta15.getValue() == null
                || taxaMulta30.getValue() == null
               // || dma1.getValue() == null
                ) 
        {
            return false;
        }

        return true;
    }

    public boolean camposNnuloInscricao() {
        if (dataInicioI.getValue() == null
                || datafimIEpoca1.getValue() == null
              //  || datafimIEpoca2.getValue() == null
                || taxaMultaI15.getValue() == null
                || taxaMultaI30.getValue() == null
                || datafimIEpoca21.getValue() == null
                || datafimIEpoca22.getValue() == null
                || dataInicioI2.getValue() == null
                || di1.getValue() == null
                || dataSem1I.getValue() == null
                || dataSem1F.getValue() == null
                || dataSem2I.getValue() == null
                || dataSem2F.getValue() == null) {
            return false;
        }

        return true;
    }

    public void limparcampos() {
        //dataInicioI.setValue(null);
        dbTaxMatriculaN.setValue(null);
        dbuTaxMatriculaE.setValue(null);
        dbuTaxEExternoN.setValue(null);
        dbuTaxEExternoE.setValue(null);
        TaxMudancaEE.setValue(null);
        TaxMudancaEN.setValue(null);
        TaxMudancaRE.setValue(null);
        TaxMudancaRN.setValue(null);
        TaxEquivalenciaE.setValue(null);
        TaxEquivalenciaN.setValue(null);
        TaxPrevisaoE.setValue(null);
        TaxPrevisaoP.setValue(null);
        TaxPdeclaracaoD.setValue(null);
        TaxPdeclaracaoF.setValue(null);
        TaxPcertificadoP.setValue(null);
        TaxPcertificadoH.setValue(null);
        TaxPsegundaC.setValue(null);
        TaxPmudancaT.setValue(null);
        TaxPlsuspensao.setValue(null);
        TaxPdiploma.setValue(null);
        dbTaxInscricaoN.setValue(null);
        dbTaxInscricaoE.setValue(null);
        TaxRecorrencia.setValue(null);
        //---------------------------
        dataInicio.setValue(null);
        datafimEpoca1.setValue(null);
        datafimEpoca2.setValue(null);
        taxaMulta15.setValue(null);
        taxaMulta30.setValue(null);
        //------------------------------
        dataInicioI.setValue(null);
        datafimIEpoca1.setValue(null);
        datafimIEpoca2.setValue(null);
        dataInicioI2.setValue(null);
        datafimIEpoca21.setValue(null);
        datafimIEpoca22.setValue(null);
        taxaMultaI15.setValue(null);
        taxaMultaI30.setValue(null);
    }

    public PlanificacaoAnoLectivo getPlano() {
//        Date dano = new Date();
//        Calendar cal = new GregorianCalendar();
//        cal.setTime(dano);
//        int ano = cal.get(Calendar.YEAR);
        //-----------------------------------------------
        PlanificacaoAnoLectivo p = new PlanificacaoAnoLectivo();
        if (c == 0) {
            //p.setAno(ano);
        } else {
            p = csimpm.get(PlanificacaoAnoLectivo.class, ibano.getValue());
            //p.setAno(ibano.getValue());
        }
        p.setDataFinalMatricula(datafimEpoca1.getValue());
        p.getDataFinalMatricula().setHours(23);
        p.setDataInicioMatricula(dataInicio.getValue());
        p.getDataInicioMatricula().setHours(1);
        p.setDataFimMatriculaE2(datafimEpoca2.getValue());
        p.getDataFimMatriculaE2().setHours(23);
        p.setPercentagemMultaMatricula15dias(taxaMulta15.getValue().floatValue());
        p.setPercentagemMultaMatricula30dias(taxaMulta30.getValue().floatValue());
        p.setDatainicioInscricao(dataInicioI.getValue());
        p.getDatainicioInscricao().setHours(1);
        p.setDataFimIE1(datafimIEpoca1.getValue());
        p.getDataFimIE1().setHours(23);
       // p.setDataFimIE2(datafimIEpoca2.getValue());
       // p.getDataFimIE2().setHours(23);
        p.setDatainicioInscricao2(dataInicioI2.getValue());
        p.getDatainicioInscricao2().setHours(1);
        p.setDataFimE1(datafimIEpoca21.getValue());
        p.getDataFimE1().setHours(23);
        p.setDataFimE2(datafimIEpoca22.getValue());
        p.getDataFimE2().setHours(23);
        p.setTaxaInscricaoNacional(dbTaxInscricaoN.getValue().floatValue());
        p.setTaxaInscricaoEstrangeiro(dbTaxInscricaoE.getValue().floatValue());
        p.setPercentagemMultaInscricao15dias(taxaMultaI15.getValue().floatValue());
        p.setPercentagemMultaInscricao30dias(taxaMultaI30.getValue().floatValue());
        //  p.setPlanificacaoAnoLectivoSemestre(null);
        //------------------------------------------------------------------
        p.setTaxaDeMudancaoTurno(TaxPmudancaT.getValue().floatValue());
        p.setTaxaDePedidoCertificadoHabilitacoes(TaxPcertificadoH.getValue().floatValue());
        p.setTaxaDePedidoCertificadoPlanoTematico(TaxPcertificadoP.getValue().floatValue());
        p.setTaxaDePedidoDeclaracaoCadeirasFeitas(TaxPdeclaracaoD.getValue().floatValue());
        p.setTaxaDePedidoDeclaracaoFrequencia(TaxPdeclaracaoF.getValue().floatValue());
        p.setTaxaDePedidoDiploma(TaxPdiploma.getValue().floatValue());
        p.setTaxaDePedidoLevantamentoSuspensao(TaxPlsuspensao.getValue().floatValue());
        p.setTaxaDePedidoRevisaoExameEscrito(TaxPrevisaoE.getValue().floatValue());
        p.setTaxaDePedidoRevisaoProvasTrabalhosFrequencia(TaxPrevisaoP.getValue().floatValue());
        p.setTaxaDePedidoSegundaChamadaProvaAvaliacaoFrequencia(TaxPsegundaC.getValue().floatValue());
        p.setTaxaEquivalencia(TaxEquivalenciaE.getValue().floatValue());
        p.setTaxaEquivalenciaInternos(TaxEquivalenciaN.getValue().floatValue());
        p.setTaxaExameExternoEstrangeiro(dbuTaxEExternoE.getValue().floatValue());
        p.setTaxaExameExternoNacional(dbuTaxEExternoN.getValue().floatValue());
        p.setTaxaMatriculaEstrangeiro(dbuTaxMatriculaE.getValue().floatValue());
        p.setTaxaMatriculaNacional(dbTaxMatriculaN.getValue().floatValue());
        p.setTaxaMudancaCursoExameadmissaoViaEstrangeiro(TaxMudancaEE.getValue().floatValue());
        p.setTaxaMudancaCursoExameadmissaoViaRequerimentoEstrangeiro(TaxMudancaRE.getValue().floatValue());
        p.setTaxaMudancaCursoExameadmissaoViaRequerimentoNacional(TaxMudancaRN.getValue().floatValue());
        p.setTaxaMudancaCursoViaExameadmissaoNacional(TaxMudancaEN.getValue().floatValue());
        p.setTaxaRecorencia(TaxRecorrencia.getValue().floatValue());
        p.setSem1i(dataSem1I.getValue());
        p.getSem1i().setHours(1);
        p.setSem1f(dataSem1F.getValue());
        p.getSem1f().setHours(23);
        p.setSem2i(dataSem2I.getValue());
        p.getSem2i().setHours(1);
        p.setSem2f(dataSem2F.getValue());
        p.getSem2f().setHours(23);

        p.setDi1(di1.getValue());
        p.getDi1().setHours(23);
       // p.setDma1(dma1.getValue());
      //  p.getDma1().setHours(23);
        p.setDm1(dm1.getValue());
        p.getDm1().setHours(23);

        return p;
    }

    public void setPlano(PlanificacaoAnoLectivo p) {
        Doublebox TaxMatriculaN = (Doublebox) mDialogAddPlano.getFellow("dbTaxMatriculaN");
        TaxMatriculaN.setValue(p.getTaxaMatriculaNacional());
        Doublebox TaxMatriculaE = (Doublebox) mDialogAddPlano.getFellow("dbuTaxMatriculaE");
        TaxMatriculaE.setValue(p.getTaxaMatriculaEstrangeiro());
        Doublebox TaxEExternoN = (Doublebox) mDialogAddPlano.getFellow("dbuTaxEExternoN");
        TaxEExternoN.setValue(p.getTaxaExameExternoNacional());
        Doublebox TaxEExternoE = (Doublebox) mDialogAddPlano.getFellow("dbuTaxEExternoE");
        TaxEExternoE.setValue(p.getTaxaExameExternoEstrangeiro());
        Doublebox taxMudancaEE = (Doublebox) mDialogAddPlano.getFellow("TaxMudancaEE");
        taxMudancaEE.setValue(p.getTaxaMudancaCursoExameadmissaoViaEstrangeiro());
        Doublebox taxMudancaEN = (Doublebox) mDialogAddPlano.getFellow("TaxMudancaEN");
        taxMudancaEN.setValue(p.getTaxaMudancaCursoViaExameadmissaoNacional());
        Doublebox taxMudancaRE = (Doublebox) mDialogAddPlano.getFellow("TaxMudancaRE");
        taxMudancaRE.setValue(p.getTaxaMudancaCursoExameadmissaoViaRequerimentoEstrangeiro());
        Doublebox taxMudancaRN = (Doublebox) mDialogAddPlano.getFellow("TaxMudancaRN");
        taxMudancaRN.setValue(p.getTaxaMudancaCursoExameadmissaoViaRequerimentoNacional());
        Doublebox taxEquivalenciaE = (Doublebox) mDialogAddPlano.getFellow("TaxEquivalenciaE");
        taxEquivalenciaE.setValue(p.getTaxaEquivalencia());
        Doublebox taxEquivalenciaN = (Doublebox) mDialogAddPlano.getFellow("TaxEquivalenciaN");
        taxEquivalenciaN.setValue(p.getTaxaEquivalenciaInternos());
        Doublebox taxPrevisaoE = (Doublebox) mDialogAddPlano.getFellow("TaxPrevisaoE");
        taxPrevisaoE.setValue(p.getTaxaDePedidoRevisaoExameEscrito());
        Doublebox taxPrevisaoP = (Doublebox) mDialogAddPlano.getFellow("TaxPrevisaoP");
        taxPrevisaoP.setValue(p.getTaxaDePedidoRevisaoProvasTrabalhosFrequencia());
        Doublebox taxPdeclaracaoD = (Doublebox) mDialogAddPlano.getFellow("TaxPdeclaracaoD");
        taxPdeclaracaoD.setValue(p.getTaxaDePedidoDeclaracaoCadeirasFeitas());
        Doublebox taxPdeclaracaoF = (Doublebox) mDialogAddPlano.getFellow("TaxPdeclaracaoF");
        taxPdeclaracaoF.setValue(p.getTaxaDePedidoDeclaracaoFrequencia());
        Doublebox taxPcertificadoP = (Doublebox) mDialogAddPlano.getFellow("TaxPcertificadoP");
        taxPcertificadoP.setValue(p.getTaxaDePedidoCertificadoPlanoTematico());
        Doublebox taxPcertificadoH = (Doublebox) mDialogAddPlano.getFellow("TaxPcertificadoH");
        taxPcertificadoH.setValue(p.getTaxaDePedidoCertificadoHabilitacoes());
        Doublebox taxPsegundaC = (Doublebox) mDialogAddPlano.getFellow("TaxPsegundaC");
        taxPsegundaC.setValue(p.getTaxaDePedidoSegundaChamadaProvaAvaliacaoFrequencia());
        Doublebox taxPmudancaT = (Doublebox) mDialogAddPlano.getFellow("TaxPmudancaT");
        taxPmudancaT.setValue(p.getTaxaDeMudancaoTurno());
        Doublebox taxPlsuspensao = (Doublebox) mDialogAddPlano.getFellow("TaxPlsuspensao");
        taxPlsuspensao.setValue(p.getTaxaDePedidoLevantamentoSuspensao());
        Doublebox taxPdiploma = (Doublebox) mDialogAddPlano.getFellow("TaxPdiploma");
        taxPdiploma.setValue(p.getTaxaDePedidoDiploma());
        Doublebox TaxInscricaoN = (Doublebox) mDialogAddPlano.getFellow("dbTaxInscricaoN");
        TaxInscricaoN.setValue(p.getTaxaInscricaoNacional());
        Doublebox TaxInscricaoE = (Doublebox) mDialogAddPlano.getFellow("dbTaxInscricaoE");
        TaxInscricaoE.setValue(p.getTaxaInscricaoEstrangeiro());
        Doublebox taxRecorrencia = (Doublebox) mDialogAddPlano.getFellow("TaxRecorrencia");
        taxRecorrencia.setValue(p.getTaxaRecorencia());

        Datebox dSem1I = (Datebox) mDialogAddPlano.getFellow("dataSem1I");
        dSem1I.setValue(p.getSem1i());
        Datebox dSem1F = (Datebox) mDialogAddPlano.getFellow("dataSem1F");
        dSem1F.setValue(p.getSem1f());
        Datebox dSem2I = (Datebox) mDialogAddPlano.getFellow("dataSem2I");
        dSem2I.setValue(p.getSem2i());
        Datebox dSem2F = (Datebox) mDialogAddPlano.getFellow("dataSem2F");
        dSem2F.setValue(p.getSem2f());

        ((Datebox) mDialogAddPlano.getFellow("di1")).setValue(p.getDi1());
        ((Datebox) mDialogAddPlano.getFellow("dma1")).setValue(p.getDma1());
        ((Datebox) mDialogAddPlano.getFellow("dm1")).setValue(p.getDm1());
        //---------------------------
        //  if (c == 1) {
        Datebox DataInicio = (Datebox) mDialogAddPlano.getFellow("dataInicio");
        DataInicio.setValue(p.getDataInicioMatricula());
        Datebox DatafimEpoca1 = (Datebox) mDialogAddPlano.getFellow("datafimEpoca1");
        DatafimEpoca1.setValue(p.getDataFinalMatricula());
        Datebox DatafimEpoca2 = (Datebox) mDialogAddPlano.getFellow("datafimEpoca2");
        DatafimEpoca2.setValue(p.getDataFimMatriculaE2());
        //  }

        Intbox TaxaMulta15 = (Intbox) mDialogAddPlano.getFellow("taxaMulta15");
        TaxaMulta15.setValue(p.getPercentagemMultaMatricula15dias().intValue());
        Intbox TaxaMulta30 = (Intbox) mDialogAddPlano.getFellow("taxaMulta30");
        TaxaMulta30.setValue(p.getPercentagemMultaMatricula30dias().intValue());
        //------------------------------
        //    if (c == 1) {
        Datebox DataInicioI = (Datebox) mDialogAddPlano.getFellow("dataInicioI");
        DataInicioI.setValue(p.getDatainicioInscricao());
        Datebox DatafimIEpoca1 = (Datebox) mDialogAddPlano.getFellow("datafimIEpoca1");
        DatafimIEpoca1.setValue(p.getDataFimIE1());
        Datebox DatafimIEpoca2 = (Datebox) mDialogAddPlano.getFellow("datafimIEpoca2");
        DatafimIEpoca2.setValue(p.getDataFimIE2());
        Datebox DataInicioI2 = (Datebox) mDialogAddPlano.getFellow("dataInicioI2");
        DataInicioI2.setValue(p.getDatainicioInscricao2());
        Datebox DatafimIEpoca21 = (Datebox) mDialogAddPlano.getFellow("datafimIEpoca21");
        DatafimIEpoca21.setValue(p.getDataFimE1());
        Datebox DatafimIEpoca22 = (Datebox) mDialogAddPlano.getFellow("datafimIEpoca22");
        DatafimIEpoca22.setValue(p.getDataFimE2());
        // }
        Intbox TaxaMultaI15 = (Intbox) mDialogAddPlano.getFellow("taxaMultaI15");
        TaxaMultaI15.setValue(p.getPercentagemMultaInscricao15dias().intValue());
        Intbox TaxaMultaI30 = (Intbox) mDialogAddPlano.getFellow("taxaMultaI30");
        TaxaMultaI30.setValue(p.getPercentagemMultaInscricao30dias().intValue());
    }
}
