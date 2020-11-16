/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package esira.matricula;

import com.itextpdf.text.DocumentException;
import entidade.Arquivo;
import esira.controller.ProfileCreateUsersController;
import esira.domain.Arquivoestudante;
import esira.domain.Arquivoinscricao;
import esira.domain.Arquivomatricula;
import esira.domain.Bolsa;
import esira.domain.Curso;
import esira.domain.Disciplina;
import esira.domain.Documento;
import esira.domain.Endereco;
import esira.domain.Enderecof;
import esira.domain.Estadocivil;
import esira.domain.Estudante;
import esira.domain.Faculdade;
import esira.domain.Funcionario;
import esira.domain.Grupo;
import esira.domain.Ingressobolseiro;
import esira.domain.Ingressoexameadmissao;
import esira.domain.Ingressomudancauniversidade;
import esira.domain.Inscricao;
import esira.domain.Inscricaodisciplina;
import esira.domain.Listaadmissao;
import esira.domain.Matricula;
import esira.domain.MatriculaPK;
import esira.domain.Matriculaanulada;
import esira.domain.Motivomat;
import esira.domain.Notificacao;
import esira.domain.Pais;
import esira.domain.PlanificacaoAnoLectivo;
import esira.domain.Profissao;
import esira.domain.Provincia;
import esira.domain.Tipodocumento;
import esira.domain.Users;
import esira.domain.Utilizadorgeral;
import esira.domain.Validacaopendente;
import esira.domain.ValidacaopendentePK;
import esira.domain.Viaingresso;
import esira.service.CRUDService;
import esira.service.Email;
import esira.service.UserAutentic;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLConnection;
import java.text.DecimalFormat;
import java.text.Normalizer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.mail.EmailException;
import org.springframework.transaction.annotation.Transactional;
import org.zkoss.io.Files;
import org.zkoss.util.media.AMedia;
import org.zkoss.util.media.Media;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.EventQueue;
import org.zkoss.zk.ui.event.EventQueues;
import org.zkoss.zk.ui.event.ForwardEvent;
import org.zkoss.zk.ui.event.InputEvent;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.Button;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Constraint;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Div;
import org.zkoss.zul.Doublebox;
import org.zkoss.zul.Fileupload;
import org.zkoss.zul.Iframe;
import org.zkoss.zul.Image;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Radio;
import org.zkoss.zul.Radiogroup;
import org.zkoss.zul.Row;
import org.zkoss.zul.SimpleConstraint;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Tabbox;
import org.zkoss.zul.Tabpanel;
import org.zkoss.zul.Tabpanels;
import org.zkoss.zul.Tabs;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Vbox;
import org.zkoss.zul.Vlayout;
import org.zkoss.zul.Window;
import org.zkoss.zul.impl.InputElement;

/**
 *
 * @author ESIRA
 */
public class GuardarEstudanteController2 extends GenericForwardComposer {

    @WireVariable
    private CRUDService csimpm = (CRUDService) SpringUtil.getBean("CRUDService");

    Window mDialogMatricula, winAddInscricao, winRP, winAddMotivo;
    private Row rwPuni, rwnrm, rwmulta, rwUmudanca, rwTipoU, rwPaisUmudanca, rwoutraVia, rwnaturalidade, rwPexameA, rwdataAprovacao, rwdataDoador, rwCurso, rwProfissao, rwTaxanM, rwTaxaeM, rwTaxamulta15, rwTaxamulta30, rwTaxaMuniversidade;
    private Vlayout picss;
    HashMap<String, Object> par = new HashMap<String, Object>();
    public static Label lbImage;
    private Float soma15, soma30, somat;
    private Label lbtaxaMatricula, lbtaxaMatriculaE, lbtaxaMulta15dias, lbtaxaMulta30dias,
            lbtaxaMuniversidade, lbtotal, taxamultas, lbf;
    private Button idant, idprox, btnsave;
    //Anexos Atributos
    private Textbox tbimg, tbimg2, tbimg3, tbimg4, txMotivoR, txMot, nrmeca;
    private Listitem lif;
    private Listbox lbimg, lbimg2, lbimg1, lbimgII;

    private Radio rgsexo, rgNP, rgP, rgNC, rgSC, rgNuni, rgSuni;
    private Checkbox nrm;
    private Radiogroup rdgProfissao;
    private Intbox ibidEstudante, litem, idl, tipov;

    private Intbox ibano, ibmulta, idEstuMudaC;
    private Textbox tbnomeCompleto, nrmec;
    private Textbox tbapelido;
    private Datebox dabdataNascimento;
    private Textbox tbnomePai;
    private Textbox tbnomeMae;
    private Textbox tbnaturalidade;
    private Textbox tblocalidade;
    private Textbox tbdistrito;
    private Intbox ibanoTerMedio;
    private Textbox tbescola;
    private Intbox ibnivelFrequencia;
    private Textbox tbpastaDocumento;
    private Textbox tbnomeEncarregado;
    private Textbox tbcontactoEncarregado;
    private Textbox tbgrauParentesco;
    private Intbox ibtamAgregadoFamiliar;
    private Textbox tbprimeiraUniversidade;
    private Textbox tbidioma;
    private Textbox tbemail;
    private Intbox ibnotaAdmissao;
    private Textbox tboutraViaIngresso;
    private Radio raistrabalhador;
    private Radio raisTransferencia;
    private Radio raisMudancaUn;
    private Radio raisBolseiro;

    //private Combobox cbcursoingresso;
    private Combobox cbbolsa;
    private Combobox cbcursocurrente, cbcurso;
    private Combobox cbdocumento;
    private Combobox cbendereco;
    private Combobox cbenderecoFerias;
    private Combobox cbestadoCivil;
    private Combobox cbescolaPais;
    private Combobox cbviaIngresso, cbnaturalidade;
    private Combobox cbnacionalidade;
    private Combobox cbescolaProvincia;
    private Combobox cbprovinciaAdmissao;

    private Combobox cbprcursoingresso;
    private Combobox cbprbolsa;
    private Combobox cbprcursocurrente;
    private Combobox cbprsexo;
    private Combobox cbprdocumento;
    private Combobox cbprendereco;
    private Combobox cbprenderecoFerias;
    private Combobox cbprestadoCivil;
    private Combobox cbprescolaPais;
    private Combobox cbprviaIngresso;
    //  private Combobox cbprnacionalidade;
    private Combobox cbprescolaProvincia;
    private Combobox cbprprovinciaAdmissao;
    private String condcursoingresso;
    private String condbolsa;
    private String condcursocurrente;
    private String condsexo;
    private String conddocumento;
    private String condendereco;
    private String condenderecoFerias;
    private String condestadoCivil;
    private String condescolaPais;
    private String condviaIngresso;
    private String condnacionalidade;
    private String condescolaProvincia;
    private String condprovinciaAdmissao;
    //ingressobolsa
    private Datebox dabdataAprovacao;
    private Textbox tbdoador;
    private Combobox cbbolsaI;
    //ingressoexam
    //ingressomudancau
    private Textbox tbnomeUniversidade;
    private Combobox cbpaisUniversidade;
    //profissao
    private Textbox tbdescricaopr;
    private Textbox tbavenidapr;
    private Textbox tbcontactopr;
    private Textbox tbbairropr;
    private Combobox cbprovinciapr, cbprovinciaEndAL, cbprovinciaEndPF, cbTurno;
    //Endereco1
    private Textbox tbavenidaRua, tbbairro, tbtelefone;
    //Endereco2
    private Textbox tbavenidaRua2, tbbairro2, tbtelefone2;
//Documento
    private Datebox dabdataEmissao;
    private Textbox tblocalEmissao;
    private Textbox tbnrDocumento;
    private Combobox cbtipo;
    private Tabpanels tpanel;
    private Tabs tabs;
    private Tabbox tabEstudante;
    private Tab tabEnderecos, tabDadosPessoais, tabHA, tabDadosEconomicos, tabDocumentacao, tabPagamentoTaxa;
    private Map<String, Object> mapipt = new HashMap<String, Object>();
    private Map<String, Object> mapcb = new HashMap<String, Object>();
    private Map<String, Object> maplb = new HashMap<String, Object>();
    private Map<String, Object> mapchk = new HashMap<String, Object>();
    Users usr = (Users) Sessions.getCurrent().getAttribute("user");
    private Div prazo;
    ////
    private Combobox cbtipouniversidade;
    private EventQueue eq;

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);

        // onload();
    }

    public void onBtncancelar() throws IOException {
        // Executions.getCurrent().getDesktop().getSession().removeAttribute("winb");
        //limpar(tbimg);
        limpar(tbimg2);
        limpar(tbimg3);
        limpar(tbimg4);
        mDialogMatricula.detach();
    }

    public ListModel<Bolsa> getbolsaModel() {
        return new ListModelList<Bolsa>(csimpm.getAll(Bolsa.class));
    }

    public ListModel<Curso> getCursocurrenteModel() {
        return new ListModelList<Curso>(csimpm.getAll(Curso.class));
    }

    public ListModel<Curso> getCursoingressoModel() {
        return new ListModelList<Curso>(csimpm.getAll(Curso.class));
    }

    public ListModel<Tipodocumento> getTipoDocModel() {
        return new ListModelList<Tipodocumento>(csimpm.getAll(Tipodocumento.class));
    }

    public ListModel<Estadocivil> getEstadoCivilModel() {
        return new ListModelList<Estadocivil>(csimpm.getAll(Estadocivil.class));
    }

    public ListModel<Pais> getEscolaPaisModel() {
        return new ListModelList<Pais>(csimpm.getAll(Pais.class));
    }

    public ListModel<Viaingresso> getViaIngressoModel() {
        return new ListModelList<Viaingresso>(csimpm.getAll(Viaingresso.class));
    }

    public ListModel<Pais> getPaisUniversidadeModel() {
        return new ListModelList<Pais>(csimpm.getAll(Pais.class));
    }

    public ListModel<Pais> getNacionalidadeModel() {
        return new ListModelList<Pais>(csimpm.getAll(Pais.class));
    }

    public ListModel<Provincia> getProvinciaModel() {
        return new ListModelList<Provincia>(csimpm.getAll(Provincia.class));
    }

    @SuppressWarnings("empty-statement")
    public String gerarNrMecanografico(Curso c) {
        //instancia as datas....
        Date datactual = new Date();
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(datactual);
        int anoActual = calendar.get(Calendar.YEAR);
        //format 
        DecimalFormat decimalFormat = new DecimalFormat("0,000");
        //getcourse...
        Curso curso = c;

        Map<String, Object> par = new HashMap<String, Object>();
        par.put("idCurso", curso);
        par.put("anoIngresso", anoActual);

        Map<String, Object> ent = new HashMap<String, Object>();
        ent
                .put("e", Estudante.class
                );

        //buscar estudantes deste ano no curso dado
        String nrMec = null;
        List<Estudante> estudantes = csimpm.findByJPQuery("from Estudante e "
                + "where e.anoIngresso=:anoIngresso and e.cursocurrente=:idCurso order by e.anoIngresso", par);
//List<Estudante> estudantes = csimpm.findByQuery("select e.* from fecn1.estudante e "
//                + "where e.ano_admissao=:anoAdmissao and e.cursocurrente=:idCurso", ent, par);
        if (estudantes.size()
                > 0) {
            nrMec = "" + decimalFormat.format(Integer.parseInt(estudantes.get(estudantes.size() - 1).getNrEstudante().substring(8)) + 1).substring(2);
            nrMec = "" + anoActual + curso.getCodigoCurso() + nrMec;
        } else {
            nrMec = "" + anoActual + curso.getCodigoCurso() + decimalFormat.format(1).substring(2);
        }

        return nrMec;
    }

    public void calcularTotal2(String v) {
        float soma = 0;

        if (rwTaxanM.isVisible() == true) {
            soma += Float.parseFloat(lbtaxaMatricula.getValue());
        }
        if (rwTaxaeM.isVisible() == true) {
            soma += Float.parseFloat(lbtaxaMatriculaE.getValue());
        }
        if (v.equals("0")) {
            DecimalFormat decimalFormat = new DecimalFormat("0.00");

            lbtotal.setValue(decimalFormat.format(soma));
            somat = soma;
        } else {
            onSelect$cbnacionalidade();
            soma = soma + (soma30 * (Integer.parseInt(v) / 100));
            DecimalFormat decimalFormat = new DecimalFormat("0.00");

            lbtotal.setValue(decimalFormat.format(soma));
            somat = soma;
        }

    }

    public void calcularTotal() {
        float soma = 0;

        if (rwTaxanM.isVisible() == true) {
            soma += Float.parseFloat(lbtaxaMatricula.getValue());
        }
        if (rwTaxaeM.isVisible() == true) {
            soma += Float.parseFloat(lbtaxaMatriculaE.getValue());
        }
        if (!rwmulta.isVisible()) {
            if (rwTaxamulta15.isVisible() == true) {
                soma += soma15;
            }
            if (rwTaxamulta30.isVisible() == true) {
                soma += soma30;
            }
        } else {
            if (ibmulta.getValue() != 0) {
//                soma = soma + Float.parseFloat(lbtaxaMulta30dias.getValue());
//            } else {
                soma = soma + (soma30 * (ibmulta.getValue() / 100)); //label taxa30.. leva o valor da taxa(nac ou estr)
            }
        }
//        if (rwTaxaMuniversidade.isVisible() == true) {
//            soma += Float.parseFloat(lbtaxaMuniversidade.getValue());
//        }

        DecimalFormat decimalFormat = new DecimalFormat("0.00");

        lbtotal.setValue(decimalFormat.format(soma));
        somat = soma;

    }

    public void onChanging$ibmulta(InputEvent evt) {
        if (!evt.getValue().equals("")) {
            calcularTotal2(evt.getValue());
        }
    }

    public void onError$dabdataAprovacao() {
        Clients.scrollIntoView(dabdataAprovacao);
    }

    public void onError$tbdoador() {
        Clients.scrollIntoView(tbdoador);
    }

    public void onError$cbbolsa() {
        Clients.scrollIntoView(cbbolsa);
    }

    public void onError$cbnaturalidade() {
        Clients.scrollIntoView(cbnaturalidade);
    }

    public void onError$cbprovinciaAdmissao() {
        Clients.scrollIntoView(cbprovinciaAdmissao);
    }

    public void onError$tbnomeUniversidade() {
        Clients.scrollIntoView(tbnomeUniversidade);
    }

    public void onError$cbpaisUniversidade() {
        Clients.scrollIntoView(cbpaisUniversidade);
    }

    public void onError$tbdescricaopr() {
        Clients.scrollIntoView(tbdescricaopr);
    }

    public void onError$tbavenidapr() {
        Clients.scrollIntoView(tbavenidapr);
    }

    public void onError$tbcontactopr() {
        Clients.scrollIntoView(tbcontactopr);
    }

    public void onError$tbbairropr() {
        Clients.scrollIntoView(tbbairropr);
    }

    public void onError$cbprovinciapr() {
        Clients.scrollIntoView(cbprovinciapr);
    }

    //////////////
//    public void onError$tbnrEstudante() { 
    //Clients.scrollIntoView(tbnrEstudante);
//    }
    public void onError$tbnomeCompleto() {
        Clients.scrollIntoView(tbnomeCompleto);
    }

    public void onError$tbapelido() {
        Clients.scrollIntoView(tbapelido);
    }

    public void onError$dabdataNascimento() {
        Clients.scrollIntoView(dabdataNascimento);
    }

    public void onError$tbnomePai() {
        Clients.scrollIntoView(tbnomePai);
    }

    public void onError$tbnomeMae() {
        Clients.scrollIntoView(tbnomeMae);
    }

    public void onError$tbnaturalidade() {
        Clients.scrollIntoView(tbnaturalidade);
    }

    public void onError$tblocalidade() {
        Clients.scrollIntoView(tblocalidade);
    }

    public void onError$tbdistrito() {
        Clients.scrollIntoView(tbdistrito);
    }

    public void onError$ibanoTerMedio() {
        Clients.scrollIntoView(ibanoTerMedio);
    }

    public void onError$tbescola() {
        Clients.scrollIntoView(tbescola);
    }

    public void onError$ibnivelFrequencia() {
        Clients.scrollIntoView(ibnivelFrequencia);
    }

    public void onError$tbpastaDocumento() {
        Clients.scrollIntoView(tbpastaDocumento);
    }

    public void onError$tbnomeEncarregado() {
        Clients.scrollIntoView(tbnomeEncarregado);
    }

    public void onError$tbcontactoEncarregado() {
        Clients.scrollIntoView(tbcontactoEncarregado);
    }

    public void onError$tbgrauParentesco() {
        Clients.scrollIntoView(tbgrauParentesco);
    }

    public void onError$ibtamAgregadoFamiliar() {
        Clients.scrollIntoView(ibtamAgregadoFamiliar);
    }

    public void onError$tbprimeiraUniversidade() {
        Clients.scrollIntoView(tbprimeiraUniversidade);
    }

    public void onError$tbidioma() {
        Clients.scrollIntoView(tbidioma);
    }

    public void onError$tbemail() {
        Clients.scrollIntoView(tbemail);
    }

    public void onError$ibnotaAdmissao() {
        Clients.scrollIntoView(ibnotaAdmissao);
    }

    public void onError$tboutraViaIngresso() {
        Clients.scrollIntoView(tboutraViaIngresso);
    }

    public void onError$raistrabalhador() {
        Clients.scrollIntoView(raistrabalhador);
    }

    public void onError$raisTransferencia() {
        Clients.scrollIntoView(raisTransferencia);
    }

    public void onError$raisMudancaUn() {
        Clients.scrollIntoView(raisMudancaUn);
    }

    public void onError$raisBolseiro() {
        Clients.scrollIntoView(raisBolseiro);
    }

//    public void onError$cbcursoingresso() {
//       tabs.getTabbox().setSelectedIndex(UserAutentic.getTab());  Clients.scrollIntoView(cbcursoingresso);
//    }
    public void onError$cbcursocurrente() {
        Clients.scrollIntoView(cbcursocurrente);
    }

    public void onError$cbdocumento() {
        Clients.scrollIntoView(cbdocumento);
    }

    public void onError$cbendereco() {
        Clients.scrollIntoView(cbendereco);
    }

    public void onError$cbenderecoFerias() {
        Clients.scrollIntoView(cbenderecoFerias);
    }

    public void onError$cbestadoCivil() {
        Clients.scrollIntoView(cbestadoCivil);
    }

    public void onError$cbescolaPais() {
        Clients.scrollIntoView(cbescolaPais);
    }

    public void onError$cbviaIngresso() {
        Clients.scrollIntoView(cbviaIngresso);
    }

    public void onError$cbnacionalidade() {
        Clients.scrollIntoView(cbnacionalidade);
    }

    public void onError$cbescolaProvincia() {
        Clients.scrollIntoView(cbescolaProvincia);
    }

    public void onError$ibidEstudante() {
        Clients.scrollIntoView(ibidEstudante);
    }

    public void onError$dabdataEmissao() {
        Clients.scrollIntoView(dabdataEmissao);
    }

    public void onError$tblocalEmissao() {
        Clients.scrollIntoView(tblocalEmissao);
    }

    public void onError$tbnrDocumento() {
        Clients.scrollIntoView(tbnrDocumento);
    }

    public void onError$cbprovinciaEndAL() {
        Clients.scrollIntoView(cbprovinciaEndAL);
    }

    public void onError$cbprovinciaEndPF() {
        Clients.scrollIntoView(cbprovinciaEndPF);
    }

    public void onError$cbtipouniversidade() {
        Clients.scrollIntoView(cbprovinciaEndPF);
    }

    public void onError$tbavenidaRua() {
        Clients.scrollIntoView(tbavenidaRua);
    }

    public void onError$tbbairro() {
        Clients.scrollIntoView(tbbairro);
    }

    public void onError$tbtelefone() {
        Clients.scrollIntoView(tbtelefone);
    }

    public void onError$tbavenidaRua2() {
        Clients.scrollIntoView(tbavenidaRua2);
    }

    public void onError$tbbairro2() {
        Clients.scrollIntoView(tbbairro2);
    }

    public void onSelect$cbnacionalidade() {
        checkIsValid(cbnacionalidade);
        if (cbnacionalidade.getSelectedItem() == null) {
            return;
        }
        Date dano = new Date();
        Calendar cal = new GregorianCalendar();
        cal.setTime(dano);
        int ano = cal.get(Calendar.YEAR);
        par.clear();
        //par.put("ano", ano);
//        PlanificacaoAnoLectivo pa = csimpm.findByJPQuery("from PlanificacaoAnoLectivo p where p.ano = :ano",par);
        PlanificacaoAnoLectivo pa = csimpm.get(PlanificacaoAnoLectivo.class, 1);
        if (cbnacionalidade.getSelectedItem().getIndex() == 121) { //122 e o indice de Mocambique na ONU
            rwTaxaeM.setVisible(false);
            rwTaxanM.setVisible(true);

            if (!rwmulta.isVisible()) {
                switch (taxamultas.getValue()) {
                    case "M15":
                        soma15 = ((pa.getPercentagemMultaMatricula15dias() / 100) * pa.getTaxaMatriculaNacional());
                        lbtaxaMulta15dias.setValue(pa.getPercentagemMultaMatricula15dias().intValue() + " %");
                        ibmulta.setValue(pa.getPercentagemMultaMatricula15dias().intValue());
                        break;
                    case "M30":
                        soma30 = ((pa.getPercentagemMultaMatricula30dias() / 100) * pa.getTaxaMatriculaNacional());
                        lbtaxaMulta30dias.setValue(pa.getPercentagemMultaMatricula30dias().intValue() + " %");
                        ibmulta.setValue(pa.getPercentagemMultaMatricula30dias().intValue());
                        break;
                }
            } else {
                soma30 = pa.getTaxaMatriculaNacional();
            }
        } else {
            rwTaxanM.setVisible(false);
            rwTaxaeM.setVisible(true);
            if (!rwmulta.isVisible()) {
                switch (taxamultas.getValue()) {
                    case "M15":
                        soma15 = ((pa.getPercentagemMultaMatricula15dias() / 100) * pa.getTaxaMatriculaEstrangeiro());
                        lbtaxaMulta15dias.setValue(pa.getPercentagemMultaMatricula15dias().intValue() + " %");
                        ibmulta.setValue(pa.getPercentagemMultaMatricula15dias().intValue());
                        break;
                    case "M30":
                        soma30 = ((pa.getPercentagemMultaMatricula30dias() / 100) * pa.getTaxaMatriculaEstrangeiro());
                        lbtaxaMulta30dias.setValue(pa.getPercentagemMultaMatricula30dias().intValue() + " %");
                        ibmulta.setValue(pa.getPercentagemMultaMatricula30dias().intValue());
                        break;
                }
            } else {
                soma30 = pa.getTaxaMatriculaEstrangeiro();
            }
        }
    }

    public void onError$tbtelefone2() {
        Clients.scrollIntoView(tbtelefone2);
    }

//    public void onClick$tabs() {
//        // event.stopPropagation();
//        int ind = tabs.getTabbox().getSelectedIndex();
//        //  Messagebox.show(ind + "");
//        operacao(ind - 1, false);
//        UserAutentic.setTab(ind);
//        if (ind > 0 && ind < 5) {
//            idant.setDisabled(false);
//            idprox.setDisabled(false);
//        } else {
//            if (ind == 5) {
//                idant.setDisabled(false);
//                idprox.setDisabled(true);
//            } else {
//                idant.setDisabled(true);
//                idprox.setDisabled(false);
//            }
//        }
//    }
    public void trocarTab(boolean proximo) throws IOException {

        int ind = tabs.getTabbox().getSelectedIndex();
        //if (mDialogMatricula.getTitle().charAt(0) == 'N') {
        //operacao(ind, false);
//        Messagebox.show("ff");
        backup(tpanel);
//        Messagebox.show("sdfdf");
        // }
        if (proximo) {
            tabs.getTabbox().setSelectedIndex(++ind);
        } else {
            tabs.getTabbox().setSelectedIndex(--ind);
        }
        if (ind > 0 && ind < 5) {
            idant.setDisabled(false);
            idprox.setDisabled(false);
        } else {
            if (ind == 5) {
                idant.setDisabled(false);
                idprox.setDisabled(true);
                btnsave.setDisabled(false);
                onSelect$cbnacionalidade();
                calcularTotal();
            } else {
                idant.setDisabled(true);
                idprox.setDisabled(false);
            }
        }
    }

    public void onProximo() throws IOException {
        check(tabs.getTabbox().getSelectedPanel());
        trocarTab(true);
    }

    public void onSave(ForwardEvent event) throws IOException {
        check(tpanel);
        operacao(0, true);
    }

    public void onSwitchTab(ForwardEvent event) throws InterruptedException, IOException {
        event.stopPropagation();
        final Tab tab = (Tab) event.getOrigin().getTarget();
        // Messagebox.show("1");
        // operacao(UserAutentic.getTab(),false);
        int tsel = tabs.getTabbox().getSelectedIndex();
        if (tab.getIndex() > tsel) {
            //j if (btnsave.isDisabled()) {
            //j     onProximo();
            //j  } else {
            // j check(tabs.getTabbox().getSelectedPanel());
//                if (mDialogMatricula.getTitle().charAt(0) == 'N') {
//                    //operacao(tabs.getTabbox().getSelectedIndex(), false);
//                }
            //   Messagebox.show("1");
            backup(tpanel);
            tab.setSelected(true);
            //   Messagebox.show("2");
            //j }
        }
        if (tab.getIndex() < tsel) {
            tab.setSelected(true);
        }
        int ind = tabs.getTabbox().getSelectedIndex();
        if (ind > 0 && ind < 5) {
            idant.setDisabled(false);
            idprox.setDisabled(false);
        } else {
            if (ind == 5) {
                idant.setDisabled(false);
                idprox.setDisabled(true);
                btnsave.setDisabled(false);
                onSelect$cbnacionalidade();
                if (!rwmulta.isVisible()) {
                    calcularTotal();
                }
            } else {
                idant.setDisabled(true);
                idprox.setDisabled(false);
            }
        }
    }

    public void onAnterior() throws IOException {
        check(tabs.getTabbox().getSelectedPanel());
        trocarTab(false);
    }

//    public void removerArquivo(List<Arquivoestudante> la, List<Media> lm, Short tipo) {
//        for (int i = 0; i < la.size(); i++) {
//            if (la.get(i).getTipo() == tipo) {
//                la.remove(i);
//                lm.remove(i);
//            }
//        }
//    }
    //////////////////////////////////////Metodo Save/////////////////////////////////////////////////////
    @Transactional
    public void operacao(int ind, boolean t) throws IOException {
        Estudante estudante = new Estudante();
        Listaadmissao la = null;
        Endereco end1 = new Endereco();
        Enderecof end2 = new Enderecof();
        Ingressoexameadmissao iex = null;
        Ingressomudancauniversidade imu = null;
        Ingressobolseiro ibo = null;
        Profissao prof = null;
        Documento doc = null;
        Date datm = new Date();
        Date datac = datm;
        String nrm = "", oldnrm = "";
        Matricula mat = new Matricula();
        List<Arquivoestudante> larqe = new ArrayList<Arquivoestudante>();
        List<Arquivomatricula> larq = new ArrayList<Arquivomatricula>();
        List<Media> lmed = new ArrayList<Media>();
        List<Media> lmede = new ArrayList<Media>();
        ////////////////////////////////////////////////////////////////////////////////////////
        ////TAB1
        if ((mDialogMatricula.getTitle().charAt(0) != 'N')) {
            if (mDialogMatricula.getTitle().charAt(0) != 'R') {
                MatriculaPK mpk = new MatriculaPK(ibidEstudante.getValue().longValue(), ibano.getValue());
                par.clear();
                par.put("ide", mpk);
                mat = (Matricula) csimpm.findEntByJPQuery("from Matricula m where m.matriculaPK=:ide", par);
                estudante = mat.getEstudante();
                datm = mat.getDataMatricula();
            } else {//verificar se o estudante esta mudar de curso
//                if (!usr.getUestudante() && rwCurso.isVisible()) {
//                    Window win = (Window) Executions.createComponents("/inscricao/inscricaoequivalencia.zul", mDialogMatricula, null);
//                    ((Textbox) win.getFellow("est")).setText(estudante.getIdEstudante() + "");
//                    //((Textbox) win.getFellow("cur")).setText(((Curso) cbcurso.getSelectedItem().getValue()).getIdCurso() + "");
//                }
                par.clear();
                par.put("e", ibidEstudante.getValue().longValue());
                estudante = csimpm.findEntByJPQuery("from Estudante e where e.idEstudante =:e", par);

                //  Messagebox.show(estudante.getNomeCompleto());
            }
            oldnrm = nrm = estudante.getNrEstudante();
            end1 = estudante.getEndereco();
            end2 = estudante.getEnderecof();
            iex = estudante.getIngressoexameadmissao();
            imu = estudante.getIngressomudancauniversidade();
            ibo = estudante.getIngressobolseiro();
            prof = estudante.getProfissao();
            doc = estudante.getDocumento();

            //larqe.addAll(estudante.getArquivoestudanteList());
            //larq.addAll(mat.getArquivomatriculaList());
        } else {//verificar se o estudante esta mudar de curso / se esta mudar de curso na nova matricula, deve ser actualizado ao invez de salvar
            if (rwCurso.isVisible()) {
                estudante = csimpm.get(Estudante.class, idEstuMudaC.getValue().longValue());
                end1 = estudante.getEndereco();
                end2 = estudante.getEnderecof();
                iex = estudante.getIngressoexameadmissao();
                imu = estudante.getIngressomudancauniversidade();
                ibo = estudante.getIngressobolseiro();
                prof = estudante.getProfissao();
                doc = estudante.getDocumento();
            }
        }
        estudante.setNomeCompleto(tbnomeCompleto.getText());
        estudante.setUltimamatricula(datm);
        estudante.setApelido(tbapelido.getText());
        if (rwnaturalidade.isVisible()) {
            estudante.setNaturalidade(tbnaturalidade.getText());
        }
        //se for nova matricula
//            if (cbcurso.getSelectedItem() == null) {
//                cbcurso.setText("");
//                cbcurso.getText();
//            }
        if (cbcursocurrente.getSelectedItem() == null) {
            cbcursocurrente.setText("");
            cbcursocurrente.getText();
        }
        if (!rwCurso.isVisible()) {
            estudante.setMudancac(null);
            estudante.setCursocurrente((Curso) (cbcursocurrente.getSelectedItem().getValue()));
        } else {
            if (estudante.getMudancac() == null) {
                estudante.setMudancac(new Short("0"));
            }
            estudante.setCursocurrente((Curso) (cbcurso.getSelectedItem().getValue()));
        }

        if (mDialogMatricula.getTitle().charAt(0) == 'N' || estudante.getMatriculaList().size() == 1) {
            estudante.setCursoingresso((Curso) (cbcursocurrente.getSelectedItem().getValue()));
        }
        if (dabdataNascimento.getValue() == null) {
            dabdataNascimento.setText("");
            dabdataNascimento.getText();
        }
        estudante.setDataNascimento(dabdataNascimento.getValue());
        estudante.setMasculino((Boolean) rgsexo.getValue());
        estudante.setNomePai(tbnomePai.getText());
        estudante.setNomeMae(tbnomeMae.getText());
        estudante.setNomeEncarregado(tbnomeEncarregado.getText());
        estudante.setContactoEncarregado(tbcontactoEncarregado.getText());
        estudante.setNomeEncarregado(tbnomeEncarregado.getText());
        estudante.setGrauParentesco(tbgrauParentesco.getText());
        if (rgsexo.isChecked()) {
            estudante.setMasculino(true);
        } else {
            estudante.setMasculino(false);
        }
        if (cbnacionalidade.getSelectedItem() == null) {
            cbnacionalidade.setText("");
            cbnacionalidade.getText();
        }
        estudante.setNacionalidade((Pais) (cbnacionalidade.getSelectedItem().getValue()));
        estudante.setLocalidade(tblocalidade.getText());
        estudante.setDistrito(tbdistrito.getText());
        estudante.setIdioma(tbidioma.getText());
        estudante.setGrauParentesco(tbgrauParentesco.getText());
        if (cbestadoCivil.getSelectedItem() == null) {
            cbestadoCivil.setText("");
            cbestadoCivil.getText();
        }
        estudante.setEstadoCivil((Estadocivil) (cbestadoCivil.getSelectedItem().getValue()));
        //////////////////////////////////////////////////////////////////////////////
        //TAB 2

        estudante.setEmail(tbemail.getText());
        end1.setAvenidaRua(tbavenidaRua.getText());
        end1.setBairro(tbbairro.getText());
        if (cbprovinciaEndAL.getSelectedItem() == null) {
            cbprovinciaEndAL.setText("");
            cbprovinciaEndAL.getText();
        }
        end1.setProvincia((Provincia) cbprovinciaEndAL.getSelectedItem().getValue());
        end1.setTelefone(tbtelefone.getText());
        end2.setAvenidaRua(tbavenidaRua2.getText());
        end2.setBairro(tbbairro2.getText());
        if (cbprovinciaEndPF.getSelectedItem() == null) {
            cbprovinciaEndPF.setText("");
            cbprovinciaEndPF.getText();
        }
        end2.setProvincia((Provincia) cbprovinciaEndPF.getSelectedItem().getValue());
        end2.setTelefone(tbtelefone2.getText());
        estudante.setEndereco(end1);
        estudante.setEnderecof(end2);
        ///////////////////////////////////////////////////////////////////////////
        ///TAB 3
//            if ((mDialogMatricula.getTitle().charAt(0) == 'R')) {
//                par.clear();
//                par.put("ide", estudante);
//                csimpm.updateQuery("delete Ingressomudancauniversidade i where i.idEstudante =:ide", par);
//                csimpm.updateQuery("delete Ingressoexameadmissao i where i.idEstudante =:ide", par);
//                estudante.setOutraViaIngresso("");
//            }
        estudante.setAnoTerMedio(ibanoTerMedio.getValue());
        estudante.setNotaAdmissao(ibnotaAdmissao.getValue());
        estudante.setEscola(tbescola.getText());
        if (cbescolaPais.getSelectedItem() == null) {
            cbescolaPais.setText("");
            cbescolaPais.getText();
        }
        estudante.setEscolaPais((Pais) cbescolaPais.getSelectedItem().getValue());
        if (cbescolaProvincia.getSelectedItem() == null) {
            cbescolaProvincia.setText("");
            cbescolaProvincia.getText();
        }
        estudante.setEscolaprovincia((Provincia) cbescolaProvincia.getSelectedItem().getValue());
        if (rgNuni.isChecked()) {
            estudante.setPrimeiraUniversidade(tbprimeiraUniversidade.getText());
        } else {
            estudante.setPrimeiraUniversidade(null);
        }
        if (cbnaturalidade.getSelectedItem() == null) {
            cbnaturalidade.setText("");
            cbnaturalidade.getText();
        }
        estudante.setProvincia((Provincia) cbnaturalidade.getSelectedItem().getValue());
        int i = cbnaturalidade.getSelectedIndex();
        if (i == 11) {
            estudante.setNaturalidade(tbnaturalidade.getValue());
        } else {
            estudante.setNaturalidade("");
        }
        if (cbviaIngresso.getSelectedItem() == null) {
            cbviaIngresso.setText("");
            cbviaIngresso.getText();
        }
        estudante.setViaIngresso((Viaingresso) cbviaIngresso.getSelectedItem().getValue());
        i = cbviaIngresso.getSelectedIndex();
        if (i == 0) {
            if (iex == null) {
                iex = new Ingressoexameadmissao();
                iex.setIdEstudante(estudante.getIdEstudante());
                estudante.setIngressoexameadmissao(iex);
            } else {
                if (imu != null) {
                    estudante.setIngressomudancauniversidade(null);
                    estudante.setOutraViaIngresso(null);
                    csimpm.delete(imu);
                    imu = null;
                }
            }
            if (cbprovinciaAdmissao.getSelectedItem() == null) {
                cbprovinciaAdmissao.setText("");
                cbprovinciaAdmissao.getText();
            }
            iex.setProvinciaAdmissao((Provincia) cbprovinciaAdmissao.getSelectedItem().getValue());
            // estudante.setIngressoexameadmissao(iex);
        } else if (i == 1) {
            if (imu == null) {
                imu = new Ingressomudancauniversidade();
                imu.setIdEstudante(estudante.getIdEstudante());
                estudante.setIngressomudancauniversidade(imu);
            } else {
                if (iex != null) {
                    estudante.setIngressoexameadmissao(null);
                    estudante.setOutraViaIngresso("");
                    csimpm.delete(iex);
                    iex = null;
                }
            }
            imu.setNomeUniversidade(tbnomeUniversidade.getText());
            if (cbtipouniversidade.getSelectedItem() == null) {
                cbtipouniversidade.setText("");
                cbtipouniversidade.getText();
            }
            imu.setTipouniversidade(cbtipouniversidade.getSelectedItem().getLabel());
            if (cbpaisUniversidade.getSelectedItem() == null) {
                cbpaisUniversidade.setText("");
                cbpaisUniversidade.getText();
            }
            imu.setPaisUniversidade((Pais) cbpaisUniversidade.getSelectedItem().getValue());
            // estudante.setIngressomudancauniversidade(imu);
            if (estudante.getTransferido() == null) {
                estudante.setTransferido(new Short("0"));
            }
        } else if (i == 2) {
            if (imu != null) {
                estudante.setIngressomudancauniversidade(null);
                csimpm.delete(imu);
                imu = null;
            }
            if (iex != null) {
                estudante.setIngressoexameadmissao(null);
                csimpm.delete(iex);
                iex = null;
            }
            estudante.setOutraViaIngresso(tboutraViaIngresso.getText());
            estudante.setTransferido(null);
        } else if (i == 3) {
            if (imu != null) {
                estudante.setIngressomudancauniversidade(null);
                csimpm.delete(imu);
                imu = null;
            }
            if (iex != null) {
                estudante.setIngressoexameadmissao(null);
                csimpm.delete(iex);
                iex = null;
            }
            estudante.setOutraViaIngresso(tboutraViaIngresso.getText());
            estudante.setTransferido(null);
        }
////////////////////////////////////////////////////////////////////////////////////////////////
        //TAB 4
        if (cbbolsa.getSelectedItem() == null) {
            cbbolsa.setText("");
            cbbolsa.getText();
        }
        estudante.setBolsa((Bolsa) cbbolsa.getSelectedItem().getValue());
        i = cbbolsa.getSelectedIndex();
        if (i != 0) {
            if (ibo == null) {
                ibo = new Ingressobolseiro();
                ibo.setIdEstudante(estudante.getIdEstudante());
                estudante.setIngressobolseiro(ibo);
            }
            ibo.setBolsa((Bolsa) cbbolsa.getSelectedItem().getValue());
            if (dabdataAprovacao.getValue() == null) {
                dabdataAprovacao.setText("");
                dabdataAprovacao.getText();
            }
            ibo.setDataAprovacao(dabdataAprovacao.getValue());
            ibo.setDoador(tbdoador.getText());
            //estudante.setIngressobolseiro(ibo);
        } else {
            if (ibo != null) {
                estudante.setIngressobolseiro(null);
                csimpm.delete(ibo);
                ibo = null;
            }
        }
        if (rgP.isChecked()) {
            if (prof == null) {
                prof = new Profissao();
                prof.setIdEstudante(estudante.getIdEstudante());
                estudante.setProfissao(prof);
            }
            prof.setDescricaopr(tbdescricaopr.getText());
            prof.setAvenidapr(tbavenidapr.getText());
            prof.setBairropr(tbbairropr.getText());
            if (cbprovinciapr.getValue() == null) {
                cbprovinciapr.setText("");
                cbprovinciapr.getText();
            }
            prof.setProvinciapr((Provincia) cbprovinciapr.getSelectedItem().getValue());
            prof.setContactopr(tbcontactopr.getText());
            // estudante.setProfissao(prof);
        } else {
            if (prof != null) {
                estudante.setProfissao(null);
                csimpm.delete(prof);
                prof = null;
            }
        }
        estudante.setTamAgregadoFamiliar(ibtamAgregadoFamiliar.getValue());
////////////////////////////////////////////////////////////////////////////////////////////
        /////TAB 5
        tbimg.getText();
        Arquivoestudante arq = new Arquivoestudante();
        Media m = ((Media) lif.getValue());
        File f = new File("");
        //  FilenameUtils.
        //URLConnection.
        arq.setNomearquivo(m.getName());
        arq.setTipo(new Short("0"));
        larqe.add(arq);
        lmede.add(m);
        if (doc == null) {
            doc = new Documento();
        }
        if (cbtipo.getSelectedItem() == null) {
            cbtipo.setText("");
            cbtipo.getText();
        }
        doc.setTipo((Tipodocumento) cbtipo.getSelectedItem().getValue());
        if (dabdataEmissao.getValue() == null) {
            dabdataEmissao.setText("");
            dabdataEmissao.getText();
        }
        doc.setDataEmissao(dabdataEmissao.getValue());
        doc.setLocalEmissao(tblocalEmissao.getText());
        doc.setNrDocumento(tbnrDocumento.getText());
        //    estudante.setDocumento(doc);
        tbimg2.getText();
        Iterator<Listitem> items1 = new ArrayList(lbimg1.getItems()).listIterator();
        Media m2 = null;
        while (items1.hasNext()) {
            m2 = (Media) items1.next().getValue();
            Arquivoestudante arq3 = new Arquivoestudante();
            arq3.setNomearquivo(m2.getName());
            arq3.setTipo(new Short("1"));
            larqe.add(arq3);
            lmede.add(m2);
        }
        tbimg3.getText();

        final Iterator<Listitem> items2 = new ArrayList(lbimg2.getItems()).listIterator();
        Media m3 = null;
        while (items2.hasNext()) {
            m3 = (Media) items2.next().getValue();
            Arquivoestudante arq3 = new Arquivoestudante();
            arq3.setNomearquivo(m3.getName());
            arq3.setTipo(new Short("2"));
            larqe.add(arq3);
            lmede.add(m3);
        }
        //estudante.setArquivoestudanteList(larqe);
////////////////////////////////////////////////////////////////
        ///TAB 6
        onSelect$cbnacionalidade();
        calcularTotal();
        tbimg4.getText();
        items1 = new ArrayList(lbimg.getItems()).listIterator();
        m2 = null;
        boolean e = false;
        while (items1.hasNext()) {
            Listitem ii = items1.next();
            m2 = ((Arquivo) ii.getValue()).getMedia();
            Arquivomatricula arq3 = new Arquivomatricula();
            arq3.setNomearquivo(m2.getName());
            arq3.setNrtalao(((Textbox) ii.getChildren().get(0).getChildren().get(0)).getText());
            arq3.setBanco(((Textbox) ii.getChildren().get(1).getChildren().get(0)).getText());
            arq3.setValor((((Doublebox) ii.getChildren().get(2).getChildren().get(0)).getValue()).floatValue());
            arq3.setDatadeposito(((Datebox) ii.getChildren().get(3).getChildren().get(0)).getValue());
            arq3.setDatamat(datm);
            arq3.setEstudante(tbnomeCompleto.getText());
            arq3.setValorcobrado(somat);//Float.parseFloat(lbtotal.getValue()));
            if (mDialogMatricula.getTitle().charAt(0) == 'E') {
                par.clear();
                par.put("nrt", arq3.getNrtalao());
                par.put("ide", estudante.getIdEstudante());
                par.put("ano", mat.getMatriculaPK().getAno());
                if (csimpm.findByJPQuery("from Arquivomatricula a where a.matricula.matriculaPK.ano <> :ano and"
                        + " a.matricula.matriculaPK.idEstudante <> :ide and a.nrtalao like :nrt", par).size() > 0) {
                    Clients.showNotification("O nr. de recibo ja Existe", "warning", null, null, 0);
//                    ((Textbox) ii.getChildren().get(0).getChildren().get(0)).setFocus(e);
                    e = true;
//                    return;
                }
            } else {
                par.clear();
                par.put("nrt", arq3.getNrtalao());
                if (csimpm.findByJPQuery("from Arquivomatricula a where a.nrtalao like :nrt", par).size() > 0) {
                    Clients.showNotification("O nr. de recibo ja Existe", "warning", null, null, 0);
//                    ((Textbox) ii.getChildren().get(0).getChildren().get(0)).setFocus(e);
                    e = true;
//                    return;
                }
            }
            arq3.setRecibousado(e);
            larq.add(arq3);
            lmed.add(m2);
        }
        items1 = new ArrayList(lbimgII.getItems()).listIterator();
        m2 = null;
        while (items1.hasNext()) {
            Listitem ii = items1.next();
            m2 = ((Arquivo) ii.getValue()).getMedia();
            Arquivomatricula arq3 = new Arquivomatricula();
            arq3.setNomearquivo(m2.getName());
            arq3.setRecibousado(false);
            arq3.setNrtalao("0");
            larq.add(arq3);
            lmed.add(m2);
        }
//        if (e) {
//            return;
//        }
        //mat.setArquivomatriculaList(larq);
        //       mat.setDataMatricula(datm);
        if (ibmulta.getValue() != null && ibmulta.getValue() != 0) {
            mat.setMulta(ibmulta.getValue().floatValue());
            mat.setPeriodo(taxamultas.getValue());
        } else {
            mat.setMulta(new Double(0.0).floatValue());//Float.parseFloat("0"));
            mat.setPeriodo(taxamultas.getValue());
        }
        mat.setCurso((Curso) cbcursocurrente.getSelectedItem().getValue());
        mat.setEstudante(estudante);
        if (usr.getUestudante()) {
//            mat.setModoMatricula(1);
//            mat.setConfirmacao(null);
//            mat.setEstado(true);
        } else {
//            Users u = csimpm.get(Users.class, usr.getUtilizador());
//            mat.setFuncionario(u.getIdFuncionario());
//            mat.setEstado(true);
//            if (prazo.isVisible()) {
//                mat.setModoMatricula(3);
//                mat.setConfirmacao(null);
//                mat.setObs(txMot.getText());
//            } else {
//                mat.setConfirmacao(datac);
//                mat.setModoMatricula(0);
//            }
        }
        Calendar cal = new GregorianCalendar();
        cal.setTime(datm);
        int ano = cal.get(Calendar.YEAR);
        if ((mDialogMatricula.getTitle().charAt(0) == 'N')) {
            if (rwnrm.isVisible()) {
                if (nrmec.isVisible()) {
                    if (nrmec.getValue().length() < 11) {
                        tabDadosPessoais.setSelected(true);
                        Clients.scrollIntoView(nrmec);
                        Clients.showNotification("Para o Nr. Mec., introduza no mínimo 11 digitos", "error", null, null, 0);
                        return;
                    }
                    estudante.setNrEstudante(nrmec.getValue());
                } else {
                    if (nrmeca.getValue().equals("")) {
                        estudante.setNrEstudante(gerarNrMecanografico(estudante.getCursocurrente()));
                    } else {
                        estudante.setNrEstudante(nrmeca.getValue());
                    }
                }
            } else {
                if (nrmeca.getValue().equals("")) {
                    estudante.setNrEstudante(gerarNrMecanografico(estudante.getCursocurrente()));
                } else {
                    estudante.setNrEstudante(nrmeca.getValue());
                }
            }
            estudante.setAnoIngresso(ibano.getValue());

            estudante.setNivelFrequencia(1);
            //estudante.setArquivoestudanteList(null);
            //        estudante.setEstado(true);        //Se for para editar nao ha necessidade de actualizar o estado do estadudante
            if (!rwCurso.isVisible()) {
                estudante.setProfissao(null);
                estudante.setIngressobolseiro(null);
                estudante.setIngressoexameadmissao(null);
                estudante.setIngressomudancauniversidade(null);
                estudante.setDocumento(null);
                estudante.setEndereco(null);
                estudante.setEnderecof(null);
                csimpm.Save(estudante);
            } else {
                csimpm.update(estudante);
            }
            i = 0;
            for (Arquivoestudante arq2 : larqe) {
                arq2.setIdEstudante(estudante);
                csimpm.Save(arq2);
                String nome = estudante.getNrEstudante() + arq2.getNomearquivo();
                File Test = new File(UserAutentic.getPathR(arq2.getTipo().toString()) + nome);
                try {
                    org.zkoss.io.Files.copy(Test, lmede.get(i).getStreamData());
                } catch (IOException ex) {
                    Logger.getLogger(GuardarEstudanteController.class.getName()).log(Level.SEVERE, null, ex);
                }
                i++;
            }
            if (!rwCurso.isVisible()) {
                if (end1 != null) {
                    end1.setEstudante(estudante);
                    end1.setIdEstudante(estudante.getIdEstudante());
                    csimpm.Save(end1);
                }
                if (end2 != null) {
                    end2.setEstudante(estudante);
                    end2.setIdEstudante(estudante.getIdEstudante());
                    csimpm.Save(end2);
                }
                if (iex != null) {
                    iex.setEstudante(estudante);
                    iex.setIdEstudante(estudante.getIdEstudante());
                    csimpm.Save(iex);
                }
                if (imu != null) {
                    imu.setEstudante(estudante);
                    imu.setIdEstudante(estudante.getIdEstudante());
                    csimpm.Save(imu);
                }
                if (ibo != null) {
                    ibo.setEstudante(estudante);
                    ibo.setIdEstudante(estudante.getIdEstudante());
                    csimpm.Save(ibo);
                }
                if (prof != null) {
                    prof.setEstudante(estudante);
                    prof.setIdEstudante(estudante.getIdEstudante());
                    csimpm.Save(prof);
                }
                if (doc != null) {
                    doc.setEstudante(estudante);
                    doc.setIdEstudante(estudante.getIdEstudante());
                    csimpm.Save(doc);
                }
            }
            mat.setValor(somat);//Float.parseFloat(lbtotal.getValue()));

            MatriculaPK mpk = new MatriculaPK(estudante.getIdEstudante(), ano);
            mat.setMatriculaPK(mpk);
            mat.setArquivomatriculaList(null);
            mat.setAnulada(false);
            csimpm.Save(mat);
//                limpar(tbimg);
//                limpar(tbimg2);
//                limpar(tbimg3);
//                limpar(tbimg4);
            i = 0;
            for (Arquivomatricula arq2 : larq) {
                arq2.setMatricula(mat);
                csimpm.Save(arq2);
                String nome = estudante.getNrEstudante() + ano + arq2.getNomearquivo();
                File Test = new File(UserAutentic.getPathR("3") + nome);
                try {
                    org.zkoss.io.Files.copy(Test, lmed.get(i).getStreamData());
                } catch (IOException ex) {
                    Logger.getLogger(GuardarEstudanteController.class.getName()).log(Level.SEVERE, null, ex);
                }
                i++;
            }
            if (usr.getUestudante()) {
                la = csimpm.get(Listaadmissao.class, idl.getValue());
                la.setIdEstudante(estudante);
                csimpm.update(la);
                Clients.showNotification(" O seu pedido de matricula foi Enviado", null, null, null, 3000);
            } else {
                Clients.showNotification(" Matriculado com Sucesso", null, null, null, 2000);
            }
            if (!usr.getUestudante()) {
                Listbox lbox = ((Listbox) mDialogMatricula.getParent().getFellow("lbplm"));
                la = (Listaadmissao) lbox.getItemAtIndex(litem.getValue()).getValue();
                la.setIdEstudante(estudante);
                la.setMatriculado(true);
                csimpm.update(la);
                ((ListModelList) lbox.getModel()).set(litem.getValue(), la);
                Users user = new Users();
                user.setNome(estudante.getNomeCompleto());
                user.setUtilizador(estudante.getNomeCompleto().charAt(0) + estudante.getApelido());
                user.setUtilizador(semAcento(user.getUtilizador().toLowerCase()));
                user.setIdEstudante(estudante);
                user.setUestudante(true);
                user.setPasword(user.getUtilizador());
                user.setEmail(estudante.getEmail());
                user.setFaculdade(estudante.getCursocurrente().getFaculdade());
                user.setIdGrupo(csimpm.get(Grupo.class, "Estudante"));
                user.setTenant(usr.getTenant());
                int ii = 0;
                boolean b;
                do {
                    b = guardar(user, ii);
                    ii++;
                } while (!b);
            } else {
                ((Intbox) mDialogMatricula.getParent().getFellow("ide")).setValue(estudante.getIdEstudante().intValue());
                ((Button) mDialogMatricula.getParent().getFellow("btnMatric")).setDisabled(true);
                ((Vbox) mDialogMatricula.getParent().getFellow("vb1")).setVisible(false);
                ((Vbox) mDialogMatricula.getParent().getFellow("vb2")).setVisible(false);
                ((Vbox) mDialogMatricula.getParent().getFellow("vb3")).setVisible(true);
                ((Vbox) mDialogMatricula.getParent().getFellow("vb4")).setVisible(false);
                ((Button) mDialogMatricula.getParent().getFellow("btnInsc")).setDisabled(false);
            }
            final Long id = estudante.getIdEstudante();
//            if (!usr.getUestudante() && ((estudante.getTransferido() != null && estudante.getTransferido().intValue() == 0)
//                    || (estudante.getMudancac() != null && estudante.getMudancac().intValue() == 0))) {
//                Messagebox.show("Registar a Equivalência das Disciplinas", "", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION,
//                        new EventListener() {
//                            @Override
//                            public void onEvent(Event evet) throws InterruptedException {
//                                switch (((Integer) evet.getData()).intValue()) {
//                                    case Messagebox.YES:
//                                        Window win = (Window) Executions.createComponents("/inscricao/inscricaoequivalencia.zul", mDialogMatricula, null);
//                                        ((Textbox) win.getFellow("est")).setText(id + "");
//                                        ((Button) win.getFellow("btnCnf")).setVisible(false);
//                                        ((Button) win.getFellow("btnRj")).setVisible(false);
//                                        win.doModal();
//                                        break;
//                                    case Messagebox.NO:
//                                        mDialogMatricula.detach();
//                                        return;
//                                }
//                            }
//                        });
//            }
            Messagebox.show("Pretende realizar inscricao", "", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION,
                    new EventListener() {
                        @Override
                        public void onEvent(Event evet) throws InterruptedException {
                            switch (((Integer) evet.getData()).intValue()) {
                                case Messagebox.YES:
                                    Window win = (Window) Executions.createComponents("/inscricao/inscricao.zul", mDialogMatricula, null);
                                    ((Textbox) win.getFellow("txx")).setText(id + "");
                                    win.doModal();
                                    break;
                                case Messagebox.NO:
                                    mDialogMatricula.detach();
                                    return;
                            }
                        }
                    });
        } else {
            //Messagebox.show("Renovar Matricula");
            if (rwnrm.isVisible()) {
                if (nrmec.isVisible()) {
                    if (nrmec.getValue().length() < 11) {
                        tabDadosPessoais.setSelected(true);
                        Clients.scrollIntoView(nrmec);
                        Clients.showNotification("Para o Nr. Mec., introduza no mínimo 11 digitos", "error", null, null, 0);
                        return;
                    }
                    estudante.setNrEstudante(nrmec.getValue());
                }
            }
            final Iterator<Arquivoestudante> itemsa = new ArrayList(estudante.getArquivoestudanteList()).listIterator();
            Arquivoestudante arqe;
            while (itemsa.hasNext()) {
                arqe = itemsa.next();
                estudante.getArquivoestudanteList().remove(arqe);
                if (nrm.isEmpty()) {
                    nrm = estudante.getNrEstudante();
                }
                String nome = nrm + arqe.getNomearquivo();
                f = new File(UserAutentic.getPathR(arqe.getTipo().toString()) + nome);
                f.delete();
                csimpm.delete(arqe);
            }
            estudante.setMatriculaList(null);
            estudante.setArquivoestudanteList(null);
            estudante.setIngressoexameadmissao(null);
            csimpm.update(estudante);
            i = 0;
            for (Arquivoestudante arq2 : larqe) {
                arq2.setIdEstudante(estudante);
                csimpm.Save(arq2);
                String nome = estudante.getNrEstudante() + arq2.getNomearquivo();
                File Test = new File(UserAutentic.getPathR(arq2.getTipo().toString()) + nome);
                try {
                    org.zkoss.io.Files.copy(Test, lmede.get(i).getStreamData());
                } catch (IOException ex) {
                    Logger.getLogger(GuardarEstudanteController.class.getName()).log(Level.SEVERE, null, ex);
                }
                i++;
            }
            mat.setValor(somat);//Float.parseFloat(lbtotal.getValue()));
            if ((mDialogMatricula.getTitle().charAt(0) == 'R')) {
                MatriculaPK mpk = new MatriculaPK(estudante.getIdEstudante(), ano);
                mat.setEstudante(estudante);
                mat.setMatriculaPK(mpk);
                mat.setArquivomatriculaList(null);
                mat.setAnulada(false);
                csimpm.Save(mat);
            } else {
                final Iterator<Arquivomatricula> itemsm = new ArrayList(mat.getArquivomatriculaList()).listIterator();
                Arquivomatricula arqm;
                while (itemsm.hasNext()) {
                    arqm = itemsm.next();
                    mat.getArquivomatriculaList().remove(arqm);
                    if (nrm.isEmpty()) {
                        nrm = estudante.getNrEstudante();
                    }
                    String nome = nrm + arqm.getNomearquivo();
                    f = new File(UserAutentic.getPathR("3") + nome);
                    f.delete();
                    csimpm.delete(arqm);
                }
                Motivomat mm = mat.getMotivomat();
                if (mm != null) {
                    mat.setMotivomat(null);
                    csimpm.delete(mm);
                }
                mat.setObs(txMot.getValue());
                mat.setMotivomat(null);
                mat.setEstado(true);
                mat.setArquivomatriculaList(null);
                csimpm.update(mat);
                if (!usr.getUestudante()) {
//Aqui se tem em conta que a matricula pode ser editada pelo funcionario apenas apartir da lista de admissao para umma matricula
                    //rejeitada de novo ingresso
                    if (estudante.getUsersList().size() == 0) {
                        Listbox lbox = ((Listbox) mDialogMatricula.getParent().getFellow("lbplm"));
                        la = (Listaadmissao) lbox.getItemAtIndex(litem.getValue()).getValue();
                        la.setMatriculado(true);
                        csimpm.update(la);
                        //    ((ListModelList) lbox.getModel()).set(litem.getValue(), la); //substituido com eventoQuee
                        Users user = new Users();
                        user.setNome(estudante.getNomeCompleto());
                        user.setUtilizador(estudante.getNomeCompleto().charAt(0) + estudante.getApelido());
                        user.setUtilizador(semAcento(user.getUtilizador().toLowerCase()));
                        user.setIdEstudante(estudante);
                        user.setUestudante(true);
                        user.setPasword(user.getUtilizador());
                        user.setEmail(estudante.getEmail());
                        user.setFaculdade(estudante.getCursocurrente().getFaculdade());
                        user.setIdGrupo(csimpm.get(Grupo.class, "Estudante"));
                        user.setTenant(usr.getTenant());
                        int ii = 0;
                        boolean b;
                        do {
                            b = guardar(user, ii);
                            ii++;
                        } while (!b);
                    }
                }
            }
            i = 0;
            for (Arquivomatricula arq2 : larq) {
                arq2.setMatricula(mat);
                csimpm.Save(arq2);
                String nome = estudante.getNrEstudante() + ano + arq2.getNomearquivo();
                File Test = new File(UserAutentic.getPathR("3") + nome);
                try {
                    org.zkoss.io.Files.copy(Test, lmed.get(i).getStreamData());
                } catch (IOException ex) {
                    Logger.getLogger(GuardarEstudanteController.class.getName()).log(Level.SEVERE, null, ex);
                }
                i++;
            }
            //////////Verificar se mudou o nr mecanografico/////////////
            //modificar outras matriculas diferente desta e todas inscricoes////////////
            // if (btnsave.getLabel().charAt(0) == 'A') {
            if (usr.getUestudante()) {
                ((Vbox) mDialogMatricula.getParent().getFellow("vb1")).setVisible(false);
                ((Vbox) mDialogMatricula.getParent().getFellow("vb2")).setVisible(false);
                ((Vbox) mDialogMatricula.getParent().getFellow("vb3")).setVisible(true);
                ((Vbox) mDialogMatricula.getParent().getFellow("vb4")).setVisible(false);
                ((Button) mDialogMatricula.getParent().getFellow("btnMatric")).setDisabled(true);
                Clients.showNotification(" O Seu pedido foi enviado", null, null, null, 2000);
            } else {
                Listbox lbox = ((Listbox) mDialogMatricula.getParent().getFellow(lbf.getValue()));
                if (lbox.getId().equals("lbplm")) { // Se estiver a rematricular uma matricula rejeitada
                    Clients.showNotification(" Matriculado com Sucesso", null, null, null, 2000);
////                    Listaadmissao la = (Listaadmissao) lbox.getItemAtIndex(litem.getValue()).getValue();
////                    Messagebox.show(la.getNome());
////                    //la.setIdEstudante(estudante);
////                    la.setMatriculado(true);
////                    csimpm.update(la);
//                    ((ListModelList) lbox.getModel()).set(litem.getValue(), la);
                } else {
                    Clients.showNotification(" Actualizado com Sucesso", null, null, null, 2000);
                    if (lbf.getValue().equals("lbAnulcao")) {
                        Matriculaanulada manul = (Matriculaanulada) ((Listbox) mDialogMatricula.getParent().getFellow(lbf.getValue()))
                                .getItemAtIndex(litem.getValue()).getValue();
                        manul.setRenovada(true);
                        csimpm.update(manul);
                        ((ListModelList) ((Listbox) mDialogMatricula.getParent().getFellow(lbf.getValue()))
                                .getModel()).set(litem.getValue(), manul);
                    } else {
                        mDialogMatricula.detach();
//                        new Listbox().appendChild(((Listbox) mDialogMatricula.getParent().getFellow(lbf.getValue()))
//                                .getItemAtIndex(litem.getValue()));
                    }
                }
            }
            final Long id = mat.getEstudante().getIdEstudante();
//            if (!usr.getUestudante() && ((estudante.getTransferido() != null && estudante.getTransferido().intValue() == 0)
//                    || (estudante.getMudancac() != null && estudante.getMudancac().intValue() == 0))) {
//                Messagebox.show("Registar a Equivalência das Disciplinas", "", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION,
//                        new EventListener() {
//                            @Override
//                            public void onEvent(Event evet) throws InterruptedException {
//                                switch (((Integer) evet.getData()).intValue()) {
//                                    case Messagebox.YES:
//                                        Window win = (Window) Executions.createComponents("/inscricao/inscricaoequivalencia.zul", mDialogMatricula, null);
//                                        ((Textbox) win.getFellow("est")).setText(id + "");
//                                        ((Button) win.getFellow("btnCnf")).setVisible(false);
//                                        ((Button) win.getFellow("btnRj")).setVisible(false);
//                                        win.doModal();
//                                        break;
//                                    case Messagebox.NO:
//                                        mDialogMatricula.detach();
//                                        return;
//                                }
//                            }
//                        });
//            }

        }
        //limpar(tbimg);
        limpar(tbimg2);
        limpar(tbimg3);
        limpar(tbimg4);
        if (oldnrm != null && !oldnrm.isEmpty() && !estudante.getNrEstudante().equals(oldnrm)) {
            mudarFicheiroComNrmec(oldnrm, estudante);
        }
    }

    public void onSelect$cbnaturalidade() {
        checkIsValid(cbnaturalidade);
        int sel = cbnaturalidade.getSelectedIndex();
        if (sel == 11) {
            rwnaturalidade.setVisible(true);
        } else {
            rwnaturalidade.setVisible(false);
        }
    }

    public void onSelect$cbviaIngresso() {
        checkIsValid(cbviaIngresso);
        int sel = cbviaIngresso.getSelectedIndex();
        if (sel == 0) {
            rwPexameA.setVisible(true);
            rwUmudanca.setVisible(false);
            rwTipoU.setVisible(false);
            rwPaisUmudanca.setVisible(false);
            rwoutraVia.setVisible(false);
            rwTaxaMuniversidade.setVisible(false);
        } else if (sel == 1) {
            rwUmudanca.setVisible(true);
            rwTipoU.setVisible(true);
            rwPaisUmudanca.setVisible(true);
            rwPexameA.setVisible(false);
            rwoutraVia.setVisible(false);
            rwTaxaMuniversidade.setVisible(true);
        } else if (sel == 2) {
            rwoutraVia.setVisible(true);
            rwPexameA.setVisible(false);
            rwUmudanca.setVisible(false);
            rwTipoU.setVisible(false);
            rwPaisUmudanca.setVisible(false);
            rwTaxaMuniversidade.setVisible(false);
        } else if (sel == 3) {
            rwoutraVia.setVisible(true);
            rwPexameA.setVisible(false);
            rwUmudanca.setVisible(false);
            rwTipoU.setVisible(false);
            rwPaisUmudanca.setVisible(false);
            rwTaxaMuniversidade.setVisible(false);
        }
    }

    public void onSelect$cbbolsa() {
        int sel = cbbolsa.getSelectedIndex();
        if (sel != 0) {
            rwdataAprovacao.setVisible(true);
            rwdataDoador.setVisible(true);
        } else {
            rwdataAprovacao.setVisible(false);
            rwdataDoador.setVisible(false);
        }
    }

    public void onCheNrm() {
        // Messagebox.show("dfdsf");
        if (nrmec.isVisible()) {
            Constraint c = null;
            nrmec.setText("0");
            nrmec.setConstraint(c);
            nrmec.setText("");
            nrmec.setVisible(false);
        } else {
            nrmec.setVisible(true);
            Curso c;
            if (!rwCurso.isVisible()) {
                c = (Curso) cbcursocurrente.getSelectedItem().getValue();
            } else {
                c = (Curso) cbcurso.getSelectedItem().getValue();
            }
            nrmec.setValue(gerarNrMecanografico(c));
            nrmec.setConstraint(" no Empty: Introduza o nr Mecanográfico!");
        }
    }

    public void onCheck$rdgProfissao() {
        if (rgNP.isChecked()) {
            rwProfissao.setVisible(false);
        } else if (rgP.isChecked()) {
            rwProfissao.setVisible(true);
        }
    }

    public void onRdgPuni() {
        if (rgNuni.isChecked()) {
            rwPuni.setVisible(true);
        } else if (rgSuni.isChecked()) {
            rwPuni.setVisible(false);
        }
    }

    public void onCheck$rdgCurso() {
        if (rgNC.isChecked()) {
            rwCurso.setVisible(false);
        } else if (rgSC.isChecked()) {
            rwCurso.setVisible(true);
        }
    }

    public void onSelect$cbcurso() {
        if (cbcurso.getSelectedItem() != null) {
            Curso c = (Curso) cbcurso.getSelectedItem().getValue();
            if (((Curso) cbcursocurrente.getSelectedItem().getValue()).getIdCurso() == c.getIdCurso()) {
                cbcurso.setText("");
                return;
            }
            if ((mDialogMatricula.getTitle().charAt(0) == 'N')) {
                Listbox lbox = ((Listbox) mDialogMatricula.getParent().getFellow("lbplm"));
                Listaadmissao la = (Listaadmissao) lbox.getItemAtIndex(litem.getValue()).getValue();
                la = csimpm.load(Listaadmissao.class, la.getIdaluno());
                par.clear();
                par.put("nome", la.getNome());
                par.put("curso", (Curso) cbcurso.getSelectedItem().getValue());
                par.put("nrbi", la.getNrBI());
                par.put("contacto", la.getTelefone());
                Estudante e = csimpm.findEntByJPQuery("from Estudante e where e.endereco.telefone = :contacto and"
                        + " e.documento.nrDocumento like :nrbi and e.apelido like :nome and e.cursocurrente = :curso", par);
                if (e != null) {

                } else {
                    Clients.showNotification("O Candidato nao foi encontrado. Por favor corrija os dados ou dirige-se ao "
                            + " Departamento de Registo Academico da Faculdade que pretende ingressar", "warning", null, null, 0);
                }
            }
        }
    }

    private void check(Component component) {
        checkIsValid(component);

        if (component.isVisible() || component instanceof Tabpanel) {
            List<Component> children = component.getChildren();
            for (Component each : children) {
                check(each);
            }
        }
    }

    private void checkParent(Component component) {
        if (component instanceof Tabpanel) {
            ((Tabpanel) component).getTabbox().setSelectedPanel((Tabpanel) component);
            if (tabs.getTabbox().getSelectedIndex() == 0) {
                idant.setDisabled(true);
                idprox.setDisabled(false);
            }
            if (tabs.getTabbox().getSelectedIndex() == 5) {
                onSelect$cbnacionalidade();
                calcularTotal();
            }
            //Messagebox.show(((Tabpanel) component).getTabbox().getId());

        } else {
            checkParent(component.getParent());
        }
    }

    private void checkIsValid(Component component) {
        if (component instanceof InputElement) {
            if ((!((InputElement) component).isValid())) {
                // Force show errorMessage
                //if (!(((InputElement) component)).isVisible()) {
//                 tabs.getTabbox().setSelectedIndex(4);
//                Messagebox.show(component.getId());
                checkParent(component.getParent());
                // }
                Clients.scrollIntoView(component);
                ((InputElement) component).getText();

            }
            if (((component instanceof Combobox) && ((Combobox) component).getSelectedItem() == null)) {
                //if (!(((InputElement) component)).isVisible()) {
                checkParent(component.getParent());
                // }
                Clients.scrollIntoView(component);
                ((Combobox) component).setText("");
                ((Combobox) component).getValue();
            }
        }
    }

    private void limpar(Component component) {
        limparComp(component);

        if (component.isVisible()) {
            List<Component> children = component.getChildren();
            for (Component each : children) {
                limpar(each);
            }
        }
    }

    public void limparComp(Component component) {
        Constraint co = null;
        if (component instanceof InputElement) {
            Constraint c = ((InputElement) component).getConstraint();
            if (c != null) {
                SimpleConstraint sc = (SimpleConstraint) c;
                String s = sc.getClientConstraint();
                ((InputElement) component).setConstraint(co);
                ((InputElement) component).setText("");
                ((InputElement) component).setConstraint(s.substring(1, s.length() - 1));
            } else {
                ((InputElement) component).setText("");
            }
        }
    }

    public void restore(Window win, Component component) {
        mapipt = (Map<String, Object>) Sessions.getCurrent().getAttribute("mapipt");
        mapcb = (Map<String, Object>) Sessions.getCurrent().getAttribute("mapcb");
        maplb = (Map<String, Object>) Sessions.getCurrent().getAttribute("maplb");
        mapchk = (Map<String, Object>) Sessions.getCurrent().getAttribute("mapchk");
//        restoreForm(component);
        if (!mapipt.isEmpty()) {
            for (String k : mapipt.keySet()) {
                ((InputElement) win.getFellow(k)).setText((String) mapipt.get(k));
            }
        }
        if (!mapcb.isEmpty()) {
            for (String k : mapcb.keySet()) {
                ((Combobox) win.getFellow(k)).setSelectedIndex((Integer) mapcb.get(k));
            }
        }
        if (!maplb.isEmpty()) {
            for (String k : maplb.keySet()) {
                ((Combobox) win.getFellow(k)).setModel((ListModel<Object>) maplb.get(k));
            }
        }
        if (!mapchk.isEmpty()) {
            for (String k : mapchk.keySet()) {
                ((Radio) win.getFellow(k)).setChecked((Boolean) mapchk.get(k));
            }
        }
    }

//    public void restoreForm(Component component) {
//        copiarDados(component);
//
//        if (component.isVisible()) {
//            List<Component> children = component.getChildren();
//            for (Component each : children) {
//                backupForm(each);
//            }
//        }
//    }
    public void backup(Component component) {
        mapipt.clear();
        mapcb.clear();
        maplb.clear();
        mapchk.clear();
        backupForm(component);
        Sessions.getCurrent().setAttribute("mapipt", mapipt);
        Sessions.getCurrent().setAttribute("mapcb", mapcb);
        Sessions.getCurrent().setAttribute("maplb", maplb);
        Sessions.getCurrent().setAttribute("mapchk", mapchk);
    }

    public void backupForm(Component component) {
        copiarDados(component);

        if (component.isVisible() || component instanceof Tabpanel) {
            List<Component> children = component.getChildren();
            for (Component each : children) {
                backupForm(each);
            }
        }
    }

    private void copiarDados(Component component) {
        if (component instanceof InputElement) {
            InputElement i = (InputElement) component;
            if (i.isValid()) {
                if (!(component instanceof Combobox)) {
                    mapipt.put(i.getId(), i.getText());
                } else if (((Combobox) component).getSelectedItem() == null) {
                    Combobox c = ((Combobox) component);
                    mapcb.replace(c.getId(), c.getSelectedIndex());
                }
            }
        } else if (component instanceof Listbox) {
            Listbox l = (Listbox) component;
            if (l.getItemCount() > 1) {
                maplb.put(l.getId(), l.getModel());
            }
        } else if (component instanceof Radio) {
            Radio r = (Radio) component;
            mapchk.put(r.getId(), r.isChecked());
        }
    }

    public String semAcento(String str) {
        String normal = Normalizer.normalize(str, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(normal).replaceAll("");
    }

    public boolean guardar(Users user, int i) {
        par.clear();
        String us = user.getUtilizador();
        if (i > 0) {
            us = us + i;
        }
        par.put("ut", us);
//        Users u = csimpm.findEntByJPQuery("from Users u where u.utilizador = :ut", par);
        Utilizadorgeral u = csimpm.findEntByJPQuery("from Utilizadorgeral u where u.utilizador = :ut", par);
        if (u != null) {
            return false;
        } else {
            user.setUtilizador(us);
            user.setPasword(us);
            csimpm.Save(user);
            return true;
        }
    }

    public void mudarFicheiroComNrmec(String oldnrm, Estudante est) throws IOException {
//                    Clients.showNotification("Este processo podera levar muito tempo", "warning", null, null, 3000);
        par.clear();
        par.put("e", est);
        List<Matricula> lm = csimpm.findByJPQuery("from Matricula m where m.estudante = :e", par);
        List<Inscricao> li = csimpm.findByJPQuery("from Inscricao i where i.idEstudante = :e", par);
        for (Matricula mat : lm) {
            par.clear();
            par.put("m", mat);
            List<Arquivomatricula> lam = csimpm.findByJPQuery("from Arquivomatricula i where i.matricula = :m", par);
            for (Arquivomatricula arq2 : lam) {
                try {
                    String sss = UserAutentic.getPathR("3") + oldnrm + mat.getMatriculaPK().getAno() + arq2.getNomearquivo();
                    InputStream iss = null;
                    try {
                        iss = new FileInputStream(sss);
                    } catch (java.io.FileNotFoundException ex) {

                    }
                    if (iss != null) {
                        Media pp = new AMedia(sss, FilenameUtils.getExtension(sss), URLConnection.guessContentTypeFromName(sss), iss);
                        Media mm = new AMedia(arq2.getNomearquivo(), FilenameUtils.getExtension(sss), URLConnection.guessContentTypeFromName(sss), pp.getByteData());

                        String nome = oldnrm + arq2.getMatricula().getMatriculaPK().getAno() + arq2.getNomearquivo();
                        File Test = new File(UserAutentic.getPathR("3") + nome);
                        Test.delete();

                        String nome2 = est.getNrEstudante() + arq2.getMatricula().getMatriculaPK().getAno() + arq2.getNomearquivo();
                        File fnovo = new File(UserAutentic.getPathR("3") + nome2);
                        try {
                            org.zkoss.io.Files.copy(fnovo, mm.getStreamData());
                        } catch (IOException ex) {
                            Logger.getLogger(ProfileCreateUsersController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }catch(Exception ex)  {
                }
            }
        }
        for (Inscricao insc : li) {
            par.clear();
            par.put("i", insc);
            List<Arquivoinscricao> lam = csimpm.findByJPQuery("from Arquivoinscricao i where i.idInscricao = :i                      ", par);
            for (Arquivoinscricao arq : insc.getArquivoinscricaoList()) {
                String dat = new SimpleDateFormat("dd_MM_yyyy_HH_mm_ss").format(insc.getDataInscricao());
                String sss = UserAutentic.getPathR("4") + oldnrm + arq.getNome() + dat;
                InputStream iss = null;
                try {
                    iss = new FileInputStream(sss);
                } catch (java.io.FileNotFoundException ex) {

                }
                if (iss != null) {
                    Media pp = new AMedia(sss, FilenameUtils.getExtension(sss), URLConnection.guessContentTypeFromName(sss), iss);
                    Media mm = new AMedia(arq.getNome(), FilenameUtils.getExtension(sss), URLConnection.guessContentTypeFromName(sss), pp.getByteData());

                    File test = new File(sss);
                    test.delete();

                    String sss2 = UserAutentic.getPathR("4") + est.getNrEstudante() + arq.getNome() + dat;
                    File fnovo = new File(sss2);
                    try {
                        org.zkoss.io.Files.copy(fnovo, mm.getStreamData());
                    } catch (IOException ex) {
                        Logger.getLogger(ProfileCreateUsersController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
    }

}
