/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package esira.matricula;

import esira.domain.Bolsa;
import esira.domain.Curso;
import esira.domain.Documento;
import esira.domain.Endereco;
import esira.domain.Estadocivil;
import esira.domain.Estudante;
import esira.domain.Ingressobolseiro;
import esira.domain.Ingressomudancauniversidade;
import esira.domain.Matricula;
import esira.domain.MatriculaPK;
import esira.domain.Pais;
import esira.domain.PlanificacaoAnoLectivo;
import esira.domain.Profissao;
import esira.domain.Provincia;
import esira.domain.Tipodocumento;
import esira.domain.Viaingresso;
import esira.service.CRUDService;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import org.zkoss.image.AImage;
import org.zkoss.util.media.Media;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Image;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Radio;
import org.zkoss.zul.Radiogroup;
import org.zkoss.zul.Row;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Tabbox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

/**
 *
 * @author DataCenter
 */
public class RenovarMatriculaController extends GenericForwardComposer {

    @WireVariable
    private CRUDService csimpm = (CRUDService) SpringUtil.getBean("CRUDService");
    Matricula matricula = csimpm.GetUniqueEntityByNamedQuery("Matricula.findByIdEstudante", ((Matricula) Executions.getCurrent().getArg().get("matricula")).getMatriculaPK().getIdEstudante());
    private Label validation, errorNome, taxamultas, lbtotal;
    private Button btnAvancar1, btnAvancar2, btnAvancar3,
            btnAvancar4, btnAvancar5, btnsave, btncancelar, btnCancelar1,
            btnCancelar2, btnCancelar3, btnCancelar4, btnCancelar5;
    private Row rwUmudanca, rwoutraVia, rwPexameA, rwdataAprovacao, rwProfissao, rwPicR, rwPic,
            rwFodoMatricula, rwFodoRenovacao, rwFBIR, rwFBI, rwCHR, rwCH, rwTaxR, rwTax,
            rwPaisUmudanca, rwTaxanM, rwTaxaeM, rwTaxamulta15, rwTaxamulta30, rwTaxaMuniversidade, rwTipoU, rwViaIngresso;
    private Tab tabDadosPessoais, tabEnderecos, tabHA, tabDadosEconomicos, tabDocumentacao, tabPagamentoTaxa;
    private Tabbox tabEstudante;
//    private ListModel<Sexo> sexoModel;
    private ListModel<Pais> nacionalidadeModel;
    private ListModel<Estadocivil> estadoCivilModel;
    private ListModelList<Pais> paisModel;
    private ListModelList<Curso> cursoModel;
    private ListModelList<Viaingresso> viaIngressoModel;
    private ListModelList<Bolsa> regaliaSModel;
    private ListModelList<Tipodocumento> tipoDocModel;
    private ListModelList<Provincia> provinciaModel;
    private ListModelList<Curso> viacursoTModel;
    private List<PlanificacaoAnoLectivo> anoLectivo;
    private Label lbl, lbAtaxa, lbACH, lbBI, lbtaxaMatricula, lbtaxaMatriculaE, lbtaxaMulta15dias, lbtaxaMulta30dias, lbtaxaMuniversidade;
    Window mDialogMatricula;
    private static org.zkoss.zul.Image imag;
    private static Media media;
    private static Media mediaCA;
    private static Media mediaBI;
    private static Media mediaTaxa;
    public static Label lbImage;
    //------------COMPONENTES DE TAB DADOS PESSOAIS
    Textbox txnomeEncarregado, txtNome,
            txnomePai, txnomeMae, txconEncarregado,
            txgrauP, txnaturalidade, txlocalidade,
            txDistrito, txIdioma;
    Datebox dataNasc;
    Combobox cbsexo, cbenacionalidade, cbestadoCivil, cbcurso;
    Label lbApelido, lbNome, lbcursoid;
    Button anexarBI, btnfoto;
    Combobox cbRegaliaS;
    //------------COMPONENTES DE TAB Enderecos
    Textbox txAvenida1, txBairro1, txContacto1,
            txAvenida, txBairro, txContacto, txEmail;
    Combobox cbProvincia, cbProvincia1;
    //------------COMPONENTES DE TAB HA
    Textbox txEscola, txPuniversidade, txUnt, txOutra;
    Intbox inAno;
    Combobox cbPais, cbviaIngresso, cbProvinciaE, cbcursoT, cbPaisUmudanca, cbTipoU;
    Intbox inNotaFinal;
    //------------COMPONENTES DE TAB Dados Economicos
    Combobox cbBolsa;
    Textbox txDoador, txpr, txAvenida2, txBairro2, txContacto2;
    Combobox cbProvincia2;
    Intbox intTamFamiliar;
    Datebox dataAprovacao;
    Radiogroup rdgProfissao;
    Radio rgP, rgNP;
    //------------COMPONENTES DE TAB Documentacao
    Combobox cbtipodoc;
    Textbox txnrDoc, txLocalE;
    Datebox dataEmissao;
    //------------COMPONENTES DE TAB Pagamentos
    Textbox txnrTalao;

    public static Label getLbImage() {
        return lbImage;
    }

    public static void setLbImage(String lb) {
        lbImage.setValue(lb);
    }

    public Label getLbAtaxa() {
        return lbAtaxa;
    }

    public Label getLbACH() {
        return lbACH;
    }

    public Label getLbBI() {
        return lbBI;
    }

    public List<PlanificacaoAnoLectivo> getAnoLectivo() {
        return anoLectivo = csimpm.getAll(PlanificacaoAnoLectivo.class);
    }

    public void setAnoLectivo(List<PlanificacaoAnoLectivo> anoLectivo) {
        this.anoLectivo = anoLectivo;
    }

    public static void setMediaCA(Media mediaA) {
        RenovarMatriculaController.mediaCA = mediaA;
    }

    public static void setMediaBI(Media mediBI) {
        RenovarMatriculaController.mediaBI = mediBI;
    }

    public static void setMediaTaxa(Media mediTaxa) {
        RenovarMatriculaController.mediaTaxa = mediTaxa;
    }

    public static void caregar(Media m) throws IOException {
        media = m;
    }

    public static void pegaImage(Image img) {
        imag = img;
    }

    //TAB 1 DADOS PESSOAIS------------------------------------------------------
    public void onClick$btnAvancar1() {
        if (camposNulosDP()) {
            validation.focus();
            validation.setValue("Preencha todos campos * dos Dados Pessoais");
            tabEstudante.setSelectedTab(tabDadosPessoais);
            return;
        }
        validation.setValue("");
        tabEstudante.setSelectedTab(tabEnderecos);

    }

    public void onClick$btnCancelar1() {
        mDialogMatricula.detach();
    }

    public void onSelect$cbenacionalidade() {
        Pais p = (Pais) cbenacionalidade.getModel().getElementAt(0);
        if (p.getIdPais() != 122) {
            rwTaxanM.setVisible(false);
            rwTaxaeM.setVisible(true);

            if (taxamultas.getValue().equals("M15")) {
                lbtaxaMulta15dias.setValue("" + (anoLectivo.get(0).getPercentagemMultaMatricula15dias() * anoLectivo.get(0).getTaxaMatriculaEstrangeiro()) / 100);

            } else if (taxamultas.getValue().equals("M30")) {
                lbtaxaMulta30dias.setValue("" + (anoLectivo.get(0).getPercentagemMultaMatricula30dias() * anoLectivo.get(0).getTaxaMatriculaEstrangeiro()) / 100);

            }
        } else {
            rwTaxaeM.setVisible(false);
            rwTaxanM.setVisible(true);


            if (taxamultas.getValue().equals("M15")) {
                lbtaxaMulta15dias.setValue("" + (anoLectivo.get(0).getPercentagemMultaMatricula15dias() * anoLectivo.get(0).getTaxaMatriculaNacional() / 100));

            } else if (taxamultas.getValue().equals("M30")) {
                lbtaxaMulta30dias.setValue("" + (anoLectivo.get(0).getPercentagemMultaMatricula30dias() * anoLectivo.get(0).getTaxaMatriculaNacional() / 100));

            }


        }
    }
    //TAB 2 ENDERECOS------------------------------------------------------

    public void onClick$btnAvancar2() {
        if (camposNulosE()) {
            validation.setValue("Preencha todos campos * dos Enderecos");
        } else {
            validation.setValue("");
            tabEstudante.setSelectedTab(tabHA);
        }
    }

    public void onClick$tabEnderecos() {
        if (camposNulosDP()) {
            validation.setValue("Preencha todos campos * dos Dados Pessoais");
            tabEstudante.setSelectedTab(tabDadosPessoais);
            return;
        }
        validation.setValue("");
    }

    public void onClick$btnCancelar2() {
        mDialogMatricula.detach();
    }

    //TAB 3 HISTORIAL ACADEMICO------------------------------------------------
    public void onClick$btnAvancar3() {
        if (camposNulosHA()) {
            validation.setValue("Preencha todos campos * do Historial Académico");
        } else {
            validation.setValue("");
            tabEstudante.setSelectedTab(tabDadosEconomicos);
        }
    }

    public void onClick$tabHA() {
        if (camposNulosDP()) {
            validation.setValue("Preencha todos campos * do Historial Académico");
            tabEstudante.setSelectedTab(tabDadosPessoais);
            return;
        }
        if (camposNulosE()) {
            validation.setValue("Preencha todos campos * dos Enderecos");
            tabEstudante.setSelectedTab(tabEnderecos);
            return;
        }
        validation.setValue("");
    }

    public void onClick$btnCancelar3() {
        mDialogMatricula.detach();
    }

    //TAB 4 DADOS ECONOMICOS------------------------------------------------
    public void onClick$btnAvancar4() {
        if (camposNulosDE()) {
            validation.setValue("Preencha todos campos * dos Dados Económicos");
        } else {
            validation.setValue("");
            tabEstudante.setSelectedTab(tabDocumentacao);
        }
    }

    public void onClick$tabDadosEconomicos() {
        if (camposNulosDP()) {
            validation.setValue("Preencha todos campos * dos Dados Pessoais");
            tabEstudante.setSelectedTab(tabDadosPessoais);
            return;
        }
        if (camposNulosE()) {
            validation.setValue("Preencha todos campos * dos Enderecos");
            tabEstudante.setSelectedTab(tabEnderecos);
            return;
        }
        if (camposNulosHA()) {
            validation.setValue("Preencha todos campos * do Historial Académico");
            tabEstudante.setSelectedTab(tabHA);
            return;
        }
        validation.setValue("");
    }

    public void onCheck$rdgProfissao() {
        if (rgNP.isChecked()) {
            rwProfissao.setVisible(false);
        } else {
            if (rgP.isChecked()) {
                rwProfissao.setVisible(true);
            }
        }


    }

    public void onClick$btnCancelar4() {
        mDialogMatricula.detach();
    }

    public void onSelect$cbBolsa() {
        if (cbBolsa.getSelectedItem().getIndex() == 0) {
            rwdataAprovacao.setVisible(false);

        } else {
            rwdataAprovacao.setVisible(true);
        }


        if (matricula.getEstudante().getIsBolseiro()) {

            Ingressobolseiro i = csimpm.GetUniqueEntityByNamedQuery("Ingressobolseiro.findByIdEstudante", matricula.getEstudante().getIdEstudante());
            if (cbBolsa.getSelectedItem().getIndex() == i.getBolsa().getIdBolsa()) {
                dataAprovacao.setValue(i.getDataAprovacao());
            } else {
                dataAprovacao.setValue(null);
            }
        }
    }

    //TAB 5 DOCUMENTACAO------------------------------------------------
    public void onClick$tabDocumentacao() {
        if (camposNulosDP()) {
            validation.setValue("Preencha todos campos * dos Dados Pessoais");
            tabEstudante.setSelectedTab(tabDadosPessoais);
            return;
        }
        if (camposNulosE()) {
            validation.setValue("Preencha todos campos * dos Enderecos");
            tabEstudante.setSelectedTab(tabEnderecos);
            return;
        }
        if (camposNulosHA()) {
            validation.setValue("Preencha todos campos * do Historial Académico");
            tabEstudante.setSelectedTab(tabHA);
            return;
        }
        if (camposNulosDE()) {
            validation.setValue("Preencha todos campos * dos Dados Económicos");
            tabEstudante.setSelectedTab(tabDadosEconomicos);
            return;
        }
        validation.setValue("");
    }

    public void onClick$btnCancelar5() {
        mDialogMatricula.detach();
    }

    public void onClick$btnAvancar5() {
        if (camposNulosDoc()) {
            validation.setValue("Preencha todos campos * da Documenta?ão");
        } else {
            validation.setValue("");
            calcularTotal();
            tabEstudante.setSelectedTab(tabPagamentoTaxa);

        }


    }

    //TAB 6 PAGAMENTO............................................................
    public void onClick$tabPagamentoTaxa() {
        if (camposNulosDP()) {
            validation.setValue("Preencha todos campos * dos Dados Pessoais");
            tabEstudante.setSelectedTab(tabDadosPessoais);
            return;
        }
        if (camposNulosE()) {
            validation.setValue("Preencha todos campos * dos Enderecos");
            tabEstudante.setSelectedTab(tabEnderecos);
            return;
        }
        if (camposNulosHA()) {
            validation.setValue("Preencha todos campos * do Historial Académico");
            tabEstudante.setSelectedTab(tabHA);
            return;
        }
        if (camposNulosDE()) {
            validation.setValue("Preencha todos campos * dos Dados Económicos");
            tabEstudante.setSelectedTab(tabDadosEconomicos);
            return;
        }
        if (camposNulosDoc()) {
            validation.setValue("Preencha todos campos * da Documenta?ão");
            tabEstudante.setSelectedTab(tabDocumentacao);
            return;
        }
        validation.setValue("");
        calcularTotal();

    }

    public void onClick$btnsave() throws IOException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        String dataNome = sdf.format(c.getTime());


        Estudante e = new Estudante();
        Endereco e1 = new Endereco();
        Endereco e2 = new Endereco();

        e.setIdEstudante(matricula.getEstudante().getIdEstudante());
        e.setNrEstudante(matricula.getEstudante().getNrEstudante());
        e.setNomeCompleto(txtNome.getValue());
        e.setCursoingresso(matricula.getEstudante().getCursoingresso());
        e.setCursocurrente(matricula.getEstudante().getCursocurrente());
        e.setDataNascimento(dataNasc.getValue());
//        e.setSexo(matricula.getEstudante().getSexo());
        e.setNomePai(txnomePai.getValue());
        e.setNomeMae(txnomeMae.getValue());
        e.setNomeEncarregado(txnomeEncarregado.getValue());
        e.setContactoEncarregado(txconEncarregado.getValue());
        e.setGrauParentesco(txgrauP.getValue());
        e.setNacionalidade(matricula.getEstudante().getNacionalidade());
        e.setNaturalidade(txnaturalidade.getValue());
        e.setLocalidade(txlocalidade.getValue());
        e.setDistrito(txDistrito.getValue());
        e.setIdioma(txIdioma.getValue());
        e.setEstadoCivil((Estadocivil) cbestadoCivil.getSelectedItem().getValue());
        //enderecos
        e.setEmail(txEmail.getValue());
      //  e1.setIdEndereco(matricula.getEstudante().getEnderecoFerias().getIdEndereco());
        e1.setAvenidaRua(txAvenida.getValue());
        e1.setBairro(txBairro.getValue());
        e1.setProvincia((Provincia) cbProvincia.getSelectedItem().getValue());
        e1.setTelefone(txContacto.getValue());
        csimpm.update(e1);

      //  e.setEnderecoFerias(e1);

     //   e2.setIdEndereco(matricula.getEstudante().getEndereco().getIdEndereco());
        e2.setAvenidaRua(txAvenida1.getValue());
        e2.setBairro(txBairro1.getValue());
        e2.setProvincia((Provincia) cbProvincia1.getSelectedItem().getValue());
        e2.setTelefone(txContacto1.getValue());
        csimpm.update(e2);

        e.setEndereco(e2);

        //historial academico
        e.setAnoIngresso(matricula.getEstudante().getAnoIngresso());
        e.setNivelFrequencia(matricula.getEstudante().getNivelFrequencia() + 1);        //Primeiro Ano = Primeiro nivel de frequencia...
        e.setAnoTerMedio(inAno.getValue());
        e.setNotaAdmissao(inNotaFinal.getValue());
        e.setEscola(txEscola.getValue());
        e.setPrimeiraUniversidade(txPuniversidade.getValue());
        e.setEscolaPais((Pais) cbPais.getSelectedItem().getValue());
        e.setViaIngresso(matricula.getEstudante().getViaIngresso());

        //....A via de ingresso sera mantida...

        e.setViaIngresso(matricula.getEstudante().getViaIngresso());
//        e.setIsMudancaUn(matricula.getEstudante().getIsMudancaUn());
//        e.setIsTransferencia(matricula.getEstudante().getIsTransferencia());
        e.setOutraViaIngresso(matricula.getEstudante().getOutraViaIngresso());


        //dados economicos

        if (cbBolsa.getSelectedItem().getIndex() == 0) {
            e.setIsBolseiro(false);
        } else {
            e.setIsBolseiro(true);


            if (csimpm.GetUniqueEntityByNamedQuery("Ingressobolseiro.findByIdEstudante", matricula.getEstudante().getIdEstudante()) != null) {
                Ingressobolseiro i = csimpm.GetUniqueEntityByNamedQuery("Ingressobolseiro.findByIdEstudante", matricula.getEstudante().getIdEstudante());
                csimpm.delete(i);

            }
            Ingressobolseiro i = new Ingressobolseiro();
            i.setDataAprovacao(dataAprovacao.getValue());
            i.setIdEstudante(matricula.getEstudante().getIdEstudante());

            i.setEstudante(matricula.getEstudante());
            i.setBolsa((Bolsa) cbBolsa.getSelectedItem().getValue());
            csimpm.Save(i);


        }



        if (rgP.isChecked()) {
            Endereco e3 = new Endereco();
//            e3.setIdEndereco(matricula.getEstudante().getProfissao().getEndereco().getIdEndereco());
            e3.setAvenidaRua(txAvenida2.getValue());
            e3.setBairro(txBairro2.getValue());
            e3.setProvincia((Provincia) cbProvincia2.getSelectedItem().getValue());
            e3.setTelefone(txContacto2.getValue());
            csimpm.update(e3);
            Profissao p = new Profissao();
            p.setIdEstudante(matricula.getEstudante().getProfissao().getIdEstudante());
          //  p.setEndereco(e3);
          //  p.setLocal(txpr.getValue());
            csimpm.update(p);
            e.setProfissao(p);
        } else {
            e.setProfissao(null);
        }




        e.setTamAgregadoFamiliar(intTamFamiliar.getValue());

        //documentacao
        Documento d = new Documento();
    // n    d.setIdDocumento(matricula.getEstudante().getDocumento().getIdDocumento());
        d.setDataEmissao(dataEmissao.getValue());
        d.setLocalEmissao(txLocalE.getValue());
        d.setNrDocumento(txnrDoc.getValue());
        d.setTipo(matricula.getEstudante().getDocumento().getTipo());
        csimpm.update(d);

        e.setDocumento(d);

        //comprovativo



        csimpm.update(e);

        if (rgNP.isChecked()) {
            if (matricula.getEstudante().getProfissao() != null) {
                csimpm.delete(matricula.getEstudante().getProfissao());
            }
        }


        //====================UPLOAD DE DOCUMENTOS======================================

        //======================================================================

        String nomeCtaxa = matricula.getEstudante().getNrEstudante() + "_TAXA_" + c.get(Calendar.YEAR) + "." + mediaTaxa.getFormat();
        File Test = new File("C:\\Users\\Administrator.DATACETER1-PC\\Documents\\NetBeansProjects\\eSira\\src\\main\\webapp\\comprovativos\\" + nomeCtaxa);
        org.zkoss.io.Files.copy(Test, mediaTaxa.getStreamData());

        //======================================================================

        Matricula m = new Matricula();
        m.setEstudante(matricula.getEstudante());
        m.setCurso(matricula.getCurso());
        m.setDataMatricula(c.getTime());
        m.setMatriculaPK(new MatriculaPK(matricula.getEstudante().getIdEstudante(), c.get(Calendar.YEAR)));

        csimpm.Save(m);
        Messagebox.show("Matricula Renovada com Sucesso! ");
        mDialogMatricula.detach();
    }

    public void onClick$btncancelar() {
        mDialogMatricula.detach();
    }

//    public ListModel<Sexo> getSexoModel() {
//        List<Sexo> p = new ArrayList<Sexo>();
//        p.add(matricula.getEstudante().getSexo());
//
//        return sexoModel = new ListModelList<Sexo>(p);
//    }

    public ListModel<Pais> getNacionalidadeModel() {
        List<Pais> p = new ArrayList<Pais>();
        p.add(matricula.getEstudante().getNacionalidade());
        return nacionalidadeModel = new ListModelList<Pais>(p);
    }

    public ListModel<Estadocivil> getEstadoCivilModel() {
        return estadoCivilModel = new ListModelList<Estadocivil>(csimpm.getAll(Estadocivil.class));
    }

    public ListModelList<Pais> getPaisModel() {
        return paisModel = new ListModelList<Pais>(csimpm.getAll(Pais.class));
    }

    public ListModelList<Viaingresso> getViaIngressoModel() {
        return viaIngressoModel;
    }

    public ListModelList<Bolsa> getRegaliaSModel() {
        return regaliaSModel = new ListModelList<Bolsa>(csimpm.getAll(Bolsa.class));
    }

    public ListModelList<Tipodocumento> getTipoDocModel() {
        List<Tipodocumento> l = new ArrayList<Tipodocumento>();
        l.add(matricula.getEstudante().getDocumento().getTipo());
        return tipoDocModel = new ListModelList<Tipodocumento>(l);
    }

    public ListModelList<Provincia> getProvinciaModel() {
        return provinciaModel = new ListModelList<Provincia>(csimpm.getAll(Provincia.class));
    }

    public ListModelList<Curso> getViacursoTModel() {
        return viacursoTModel;
    }

    public ListModelList<Curso> getCursoModel() {
        List<Curso> c = new ArrayList<Curso>();
        c.add(matricula.getEstudante().getCursocurrente());
        return cursoModel = new ListModelList<Curso>(c);

    }

    @Override
    public void doAfterCompose(Component comp) throws Exception {

        super.doAfterCompose(comp);
        //matricula=(Matricula)Executions.getCurrent().getArg().get("matricula"); 
        onload();
        setestudante();
    }

    public Boolean camposNulosDP() {
        if (txDistrito.getValue().isEmpty()
                || txIdioma.getValue().isEmpty()
                || txnaturalidade.getValue().isEmpty()
                || txlocalidade.getValue().isEmpty()
                || txnomeMae.getValue().isEmpty()
                || txnomePai.getValue().isEmpty()
                || txnomeMae.getValue().isEmpty()
                || txtNome.getValue().isEmpty()
                || cbcurso.getValue().isEmpty()
                || cbsexo.getValue().isEmpty()
                || cbenacionalidade.getValue().isEmpty()
                || cbestadoCivil.getValue().isEmpty()) {
            return true;
        }


        return false;

    }

    public Boolean camposNulosE() {
        //somente e verificado o contacto e a provincia...
        if (txContacto.getValue().isEmpty()
                || txContacto1.getValue().isEmpty()
                || cbProvincia.getValue().isEmpty()
                || cbProvincia1.getValue().isEmpty()
                || txEmail.getValue().isEmpty()) {
            return true;
        }
        return false;

    }

    public Boolean camposNulosHA() {
        if (inAno.getValue() == null
                || inNotaFinal.getValue() == null
                || txEscola.getValue().isEmpty()
                || cbPais.getValue().isEmpty()
                || txPuniversidade.getValue().isEmpty()) {
            return true;
        }
        return false;
    }

    public Boolean camposNulosDE() {
        if (intTamFamiliar.getValue() == null
                || cbBolsa.getValue().isEmpty()) {
            return true;
        }
        return false;
    }

    public Boolean camposNulosDoc() {
        if (txnrDoc.getValue().isEmpty()
                || txLocalE.getValue().isEmpty()
                || cbtipodoc.getValue().isEmpty()
                || dataEmissao.getValue() == null) {
            return true;
        }
        return false;

    }

    public Boolean camposNulosPT() {
        if (txnrTalao.getValue().isEmpty()
                || lbAtaxa.getValue().equals("")) {
            return true;
        }
        return false;

    }

    public void onload() {
        rwFodoMatricula.setVisible(false);
        rwFBI.setVisible(false);
        rwCH.setVisible(false);
        rwFBIR.setVisible(false);
        rwCHR.setVisible(false);
        rwTax.setVisible(false);
        rwPic.setVisible(false);
        rwTaxaMuniversidade.setVisible(false);
        setAnoLectivo(csimpm.getAll(PlanificacaoAnoLectivo.class));

        lbtaxaMatricula.setValue("" + anoLectivo.get(0).getTaxaMatriculaNacional());
        lbtaxaMatriculaE.setValue("" + anoLectivo.get(0).getTaxaMatriculaEstrangeiro());
        //as multas de 15 e 30 dias estao no botao avancar5


        if (taxamultas.getValue().equals("NORMAL")) {
            rwTaxamulta15.setVisible(false);
            rwTaxamulta30.setVisible(false);
        } else if (taxamultas.getValue().equals("M15")) {
            rwTaxamulta30.setVisible(false);
        } else if (taxamultas.getValue().equals("M30")) {
            rwTaxamulta15.setVisible(false);
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
        if (rwTaxamulta15.isVisible() == true) {
            soma += Float.parseFloat(lbtaxaMulta15dias.getValue());
        }
        if (rwTaxamulta30.isVisible() == true) {
            soma += Float.parseFloat(lbtaxaMulta30dias.getValue());
        }


        DecimalFormat decimalFormat = new DecimalFormat("0.00");


        lbtotal.setValue(decimalFormat.format(soma));


    }

    public void setestudante() throws IOException {

        //Dados Pessoais........................................................

        Textbox TxtNome = (Textbox) mDialogMatricula.getFellow("txtNome");
        TxtNome.setValue(matricula.getEstudante().getNomeCompleto());
        Datebox DataNasc = (Datebox) mDialogMatricula.getFellow("dataNasc");
        DataNasc.setValue(matricula.getEstudante().getDataNascimento());
        Textbox TxnomePai = (Textbox) mDialogMatricula.getFellow("txnomePai");
        TxnomePai.setValue(matricula.getEstudante().getNomePai());
        Textbox TxnomeMae = (Textbox) mDialogMatricula.getFellow("txnomeMae");
        TxnomeMae.setValue(matricula.getEstudante().getNomeMae());
        Textbox TxnomeEncarregado = (Textbox) mDialogMatricula.getFellow("txnomeEncarregado");
        TxnomeEncarregado.setValue(matricula.getEstudante().getNomeEncarregado());
        Textbox TxconEncarregado = (Textbox) mDialogMatricula.getFellow("txconEncarregado");
        TxconEncarregado.setValue(matricula.getEstudante().getContactoEncarregado());
        Textbox TxgrauP = (Textbox) mDialogMatricula.getFellow("txgrauP");
        TxgrauP.setValue(matricula.getEstudante().getGrauParentesco());
        Textbox Txnaturalidade = (Textbox) mDialogMatricula.getFellow("txnaturalidade");
        Txnaturalidade.setValue(matricula.getEstudante().getNaturalidade());
        Textbox Txlocalidade = (Textbox) mDialogMatricula.getFellow("txlocalidade");
        Txlocalidade.setValue(matricula.getEstudante().getLocalidade());
        Textbox TxIdioma = (Textbox) mDialogMatricula.getFellow("txIdioma");
        TxIdioma.setValue(matricula.getEstudante().getIdioma());
        Textbox TxDistrito = (Textbox) mDialogMatricula.getFellow("txDistrito");
        TxDistrito.setValue(matricula.getEstudante().getDistrito());

        //Dados enderecos........................................................
        Textbox TxEmail = (Textbox) mDialogMatricula.getFellow("txEmail");
        TxEmail.setValue(matricula.getEstudante().getEmail());
        Textbox TxAvenida1 = (Textbox) mDialogMatricula.getFellow("txAvenida1");
        TxAvenida1.setValue(matricula.getEstudante().getEndereco().getAvenidaRua());

        Textbox TxBairro1 = (Textbox) mDialogMatricula.getFellow("txBairro1");
        TxBairro1.setValue(matricula.getEstudante().getEndereco().getBairro());

        Textbox TxContacto1 = (Textbox) mDialogMatricula.getFellow("txContacto1");
        TxContacto1.setValue(matricula.getEstudante().getEndereco().getTelefone());

        Textbox TxAvenida = (Textbox) mDialogMatricula.getFellow("txAvenida");
//        TxAvenida.setValue(matricula.getEstudante().getEnderecoFerias().getAvenidaRua());
//        Textbox TxBairro = (Textbox) mDialogMatricula.getFellow("txBairro");
//        TxBairro.setValue(matricula.getEstudante().getEnderecoFerias().getBairro());
//        Textbox TxContacto = (Textbox) mDialogMatricula.getFellow("txContacto");
//        TxContacto.setValue(matricula.getEstudante().getEnderecoFerias().getTelefone());


        //historial academico...................................................
        Intbox InAno = (Intbox) mDialogMatricula.getFellow("inAno");
        InAno.setValue(matricula.getEstudante().getAnoTerMedio());
        Intbox InNotaFinal = (Intbox) mDialogMatricula.getFellow("inNotaFinal");
        InNotaFinal.setValue(matricula.getEstudante().getNotaAdmissao());
        Textbox TtxEscola = (Textbox) mDialogMatricula.getFellow("txEscola");
        TtxEscola.setValue(matricula.getEstudante().getEscola());
        Textbox TtxPuniversidade = (Textbox) mDialogMatricula.getFellow("txPuniversidade");
        TtxPuniversidade.setValue(matricula.getEstudante().getPrimeiraUniversidade());
        rwViaIngresso.setVisible(false);

        //dados economicos......................................................
        if (matricula.getEstudante().getProfissao() == null) {
            rgNP.setChecked(true);
        } else {
            rgP.setChecked(true);
            Textbox Ttxpr = (Textbox) mDialogMatricula.getFellow("txpr");
//            Ttxpr.setValue(matricula.getEstudante().getProfissao().getLocal());
            Textbox TtxAvenida2 = (Textbox) mDialogMatricula.getFellow("txAvenida2");
          //  TtxAvenida2.setValue(matricula.getEstudante().getProfissao().getEndereco().getAvenidaRua());
            Textbox TtxBairro2 = (Textbox) mDialogMatricula.getFellow("txBairro2");
         //   TtxBairro2.setValue(matricula.getEstudante().getProfissao().getEndereco().getBairro());
            Textbox TtxContacto2 = (Textbox) mDialogMatricula.getFellow("txContacto2");
       //     TtxContacto2.setValue(matricula.getEstudante().getProfissao().getEndereco().getTelefone());

        }
        Intbox IintTamFamiliar = (Intbox) mDialogMatricula.getFellow("intTamFamiliar");
        IintTamFamiliar.setValue(matricula.getEstudante().getTamAgregadoFamiliar());

        //documentacao..........................................................


        Image Ipicss = (Image) mDialogMatricula.getFellow("pics");
        String nomeFile = "/fotografias/" + matricula.getEstudante().getNrEstudante() + "_FOTO_" + matricula.getEstudante().getAnoIngresso() + ".jpg";
        Ipicss.setSrc(nomeFile);

        Button b = (Button) mDialogMatricula.getFellow("btnfotoR");
        b.setDisabled(true);

        Textbox TtxnrDoc = (Textbox) mDialogMatricula.getFellow("txnrDoc");
        TtxnrDoc.setValue(matricula.getEstudante().getDocumento().getNrDocumento());
        Textbox TtxLocalE = (Textbox) mDialogMatricula.getFellow("txLocalE");
        TtxLocalE.setValue(matricula.getEstudante().getDocumento().getLocalEmissao());
        Datebox TdataEmissao = (Datebox) mDialogMatricula.getFellow("dataEmissao");
        TdataEmissao.setValue(matricula.getEstudante().getDocumento().getDataEmissao());





    }
}
