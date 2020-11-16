/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package esira.matricula;

import entidade.Arquivo;
import esira.domain.Arquivoestudante;
import esira.domain.Arquivomatricula;
import esira.domain.Bolsa;
import esira.domain.Curso;
import esira.domain.Documento;
import esira.domain.Endereco;
import esira.domain.Enderecof;
import esira.domain.Estadocivil;
import esira.domain.Estudante;
import esira.domain.Faculdade;
import esira.domain.Funcionario;
import esira.domain.Ingressobolseiro;
import esira.domain.Ingressoexameadmissao;
import esira.domain.Ingressomudancauniversidade;
import esira.domain.Listaadmissao;
import esira.domain.Matricula;
import esira.domain.MatriculaPK;
import esira.domain.Pais;
import esira.domain.PlanificacaoAnoLectivo;
import esira.domain.Prescricao;
import esira.domain.Profissao;
import esira.domain.Provincia;
import esira.domain.Tipodocumento;
import esira.domain.Users;
import esira.domain.Viaingresso;
import esira.service.CRUDService;
import esira.service.UserAutentic;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.apache.commons.io.FilenameUtils;
import org.springframework.security.core.userdetails.User;
import org.zkoss.image.AImage;
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
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Hlayout;
import org.zkoss.zul.Image;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Menuitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Radio;
import org.zkoss.zul.Row;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

/**
 *
 * @author Tafula
 */
public class PedidosMatriculaController2 extends GenericForwardComposer {

    @WireVariable
    private CRUDService csimpm = (CRUDService) SpringUtil.getBean("CRUDService");
    private Listbox lbEstudantes;
    private ListModel<Matricula> listEstudanteModel;
    private ListModel<Curso> listaCursoModel;
    Window win, winPMatric;
    Button btnProcurar;
    List listaM;
    Map<String, Object> par = new HashMap<String, Object>();
    Users usr = (Users) Sessions.getCurrent().getAttribute("user");
    private Hlayout ahead;
    String condfac = "", condnr = "", condnome = "", condgenero = "", condanoi = "", condano = "", condcurso = "";
    Textbox txProcurar, txProcNrmec;
    Map<String, Object> condpar = new HashMap<String, Object>();
    Combobox cbcurso;
    private Intbox ibProcAno;
    private Button btv;
    Menuitem manoi;
    private EventQueue eq;

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        win = (Window) Executions.createComponents("/matricula/Matricula.zul", winPMatric, null);
        win.setTitle("Validar Pedido de Matricula");
        win.detach();
        setLB(0, 20);
    }
    
     public void onCreatecbcurso() {
       // Clients.alert("hkjkjk");
       if(cbcurso.getModel()!=null&&cbcurso.getModel().getSize()>0)cbcurso.setSelectedIndex(0);
    }

    public void onSetQueueMat() {
      //  Users u = csimpm.get(Users.class, usr.getUtilizador());
        eq = EventQueues.lookup("pmatD" + usr.getFaculdade().getIdFaculdade(), EventQueues.APPLICATION, true);
        eq.subscribe(getEvento());
        eq = EventQueues.lookup("rmatD" + usr.getFaculdade().getIdFaculdade(), EventQueues.APPLICATION, true);
        eq.subscribe(getEvento2());
    }

    public EventListener getEvento() {
        return new EventListener() {
            @Override
            public void onEvent(Event event) throws Exception {
                Matricula m = (Matricula) event.getData();
                par.clear();
                par.put("mpk", m.getMatriculaPK());
                m = csimpm.findEntByJPQuery("from Matricula m where m.matriculaPK = :mpk", par);
                if (m.getModoMatricula() != 3) {
                    return;
                }
                // Users u = csimpm.get(Users.class, usr.getUtilizador());
                if (winPMatric.getParent() != null) {// && u.getFaculdade().getIdFaculdade() == i.getIdEstudante().getCursocurrente().getFaculdade().getIdFaculdade()) {
                    lbEstudantes.setRows(lbEstudantes.getItemCount() + 2);
                    ((ListModelList) lbEstudantes.getModel()).remove(m);
                    ((ListModelList) lbEstudantes.getModel()).add(0, m);
                    // Messagebox.show("Entrou");
                }
            }
        };
    }

    public EventListener getEvento2() {
        return new EventListener() {
            @Override
            public void onEvent(Event event) throws Exception {
                Matricula m = (Matricula) event.getData();
                par.clear();
                par.put("mpk", m.getMatriculaPK());
                m = csimpm.findEntByJPQuery("from Matricula m where m.matriculaPK = :mpk", par);
                if (m.getModoMatricula() != 3) {
                    return;
                }
                // Users u = csimpm.get(Users.class, usr.getUtilizador());
                if (winPMatric.getParent() != null) {
                    ((ListModelList) lbEstudantes.getModel()).remove(m);
                }
            }
        };
    }

    public ListModel<Matricula> getListEstudanteModel() {
        //retorna as matriculas que nao foram anuladas
        Calendar c = Calendar.getInstance();
        par.clear();
        par.put("user", usr.getUtilizador());
        //Users u = csimpm.findEntByJPQuery("from Users u where u.utilizador = :user", par);
        par.clear();
        Faculdade f = csimpm.get(Faculdade.class, usr.getFaculdade().getIdFaculdade());
        par.put("fac", f);
        listaM = csimpm.findByJPQuery("from Matricula m where m.curso.faculdade = :fac and m.confirmacao is null and m.matriculaPK not in (select ma.matriculaPK from "
                + "Matriculaanulada ma)", par);
        return listEstudanteModel = new ListModelList<Matricula>(listaM);
    }

    public void onValidar(ForwardEvent event) throws IOException {
        Button btn = (Button) event.getOrigin().getTarget();
        Listitem litem = (Listitem) btn.getParent().getParent();
        Matricula todo = (Matricula) litem.getValue();
        todo = csimpm.load(Matricula.class, todo.getMatriculaPK());
        preMatricular(todo, litem);
    }

    public void preMatricular(Matricula todo, Listitem l) throws IOException {
        Date dano = new Date();
        if (todo != null) {
            dano = todo.getDataMatricula();
        }
        Calendar c = new GregorianCalendar();
        c.setTime(dano);
        int ano = c.get(Calendar.YEAR);
        par.clear();
        // par.put("ano", ano);
        Users up = csimpm.get(Users.class, usr.getUtilizador());
         par.clear();
            par.put("fac", up.getFaculdade());
           PlanificacaoAnoLectivo planificacaoAnoLectivo = csimpm.findEntByJPQuery("from PlanificacaoAnoLectivo p where p.faculdade = :fac", par);
        Date d1, d2, d3;
        if (todo.getEstudante().getMatriculaList().size() > 1) {
            d1 = planificacaoAnoLectivo.getDataInicioMatricula();
            d2 = planificacaoAnoLectivo.getDataFinalMatricula();
            d3 = planificacaoAnoLectivo.getDataFimMatriculaE2();
        } else {
            d1 = planificacaoAnoLectivo.getDatainicioInscricao();
            d2 = planificacaoAnoLectivo.getDataFimIE1();
            d3 = planificacaoAnoLectivo.getDataFimIE2();
        }
        //for (PlanificacaoAnoLectivo planificacaoAnoLectivo : pals) {

        // Messagebox.show("Periodo de Multa 30 dias = " + planificacaoAnoLectivo.getPercentagemMultaMatricula30dias());
        final HashMap<String, Object> map = new HashMap<String, Object>();
        // map.put("matricula", todo);
        //  map.put("controler", "esira.matricula.RenovarMatriculaController");
        map.put("periodo", "M30");
        // Window win = (Window) Executions.createComponents("/matricula/Matricula.zul", null, map);

        win.setParent(winPMatric);
        win.doModal();
        ((Label) win.getFellow("taxamultas")).setValue("M30");
        ((Label) win.getFellow("lbtaxaMatricula")).setValue(planificacaoAnoLectivo.getTaxaMatriculaNacional().toString());
        ((Label) win.getFellow("lbtaxaMatriculaE")).setValue(planificacaoAnoLectivo.getTaxaMatriculaEstrangeiro().toString());
        //    ((Label) win.getFellow("lbtaxaMuniversidade")).setValue(planificacaoAnoLectivo.getTaxaDeMudancaoTurno().toString());
        ((Row) win.getFellow("rwTaxamulta15")).setVisible(false);
        ((Row) win.getFellow("rwTaxamulta30")).setVisible(false);
        ((Row) win.getFellow("rwmulta")).setVisible(true);
        ((Intbox) win.getFellow("ibmulta")).setValue(todo.getMulta().intValue());
        ((Label) win.getFellow("lbtotal")).setValue(todo.getValor() + "");
        renovar(todo, win, l);
        //}

    }

    public void renovar(Matricula mat, Window win, Listitem l) throws IOException {
        ((Intbox) win.getFellow("litem")).setValue(l.getIndex());
        ((Intbox) win.getFellow("tipov")).setValue(2);
        ((Textbox) win.getFellow("txMot")).setText(mat.getObs());
        ((Tab) win.getFellow("tabDadosPessoais")).setSelected(true);
        ((Button) win.getFellow("btnsave")).setVisible(false);
        ((Button) win.getFellow("btnCnf")).setVisible(true);
        ((Button) win.getFellow("btnRj")).setVisible(true);
        Estudante estudante = mat.getEstudante();
        estudante = csimpm.load(Estudante.class, estudante.getIdEstudante());
        Long bolsa = estudante.getBolsa().getIdBolsa();
        Long cursocurrente = estudante.getCursocurrente().getIdCurso();
        Provincia escolaprov = estudante.getEscolaprovincia();
        Long cursoingresso = estudante.getCursoingresso().getIdCurso();
        int estadoCivil = estudante.getEstadoCivil().getIdEstado();
        int escolaPais = estudante.getEscolaPais().getIdPais();
        int nacionalidade = estudante.getNacionalidade().getIdPais();
        int viaIngresso = estudante.getViaIngresso().getIdViaIngresso();
        Endereco endereco = estudante.getEndereco();
        Provincia provnat = estudante.getProvincia();
        Enderecof enderecof = estudante.getEnderecof();
        Ingressoexameadmissao iex = estudante.getIngressoexameadmissao();
        Ingressomudancauniversidade imu = estudante.getIngressomudancauniversidade();
        Ingressobolseiro ibo = estudante.getIngressobolseiro();
        Documento doc = estudante.getDocumento();
        Profissao prof = estudante.getProfissao();

        ((Intbox) win.getFellow("ibidEstudante")).setValue(estudante.getIdEstudante().intValue());
        ((Intbox) win.getFellow("ibano")).setValue(mat.getMatriculaPK().getAno());
        ((Textbox) win.getFellow("tbnomeCompleto")).setValue(estudante.getNomeCompleto());
        ((Textbox) win.getFellow("tbapelido")).setValue(estudante.getApelido());
        final Iterator<Comboitem> itemscursocurrente = new ArrayList(((Combobox) win.getFellow("cbcursocurrente")).getItems()).listIterator();
        Comboitem citcursocurrente;
        while (itemscursocurrente.hasNext()) {
            citcursocurrente = itemscursocurrente.next();
            if (Objects.equals(((Curso) citcursocurrente.getValue()).getIdCurso(), cursocurrente)) {
                ((Combobox) win.getFellow("cbcursocurrente")).setSelectedItem(citcursocurrente);
                break;
            }
        }
        Combobox cbt = (Combobox) win.getFellow("cbTurno");
        Comboitem cbt1 = cbt.getItems().get(0);
        Comboitem cbt2 = cbt.getItems().get(1);
        if (estudante.getTurno() == 1) {
            cbt.setSelectedItem(cbt1);
        } else {
            cbt.setSelectedItem(cbt2);
        }
        ((Datebox) win.getFellow("dabdataNascimento")).setValue(estudante.getDataNascimento());
        if (!estudante.getMasculino()) {
            ((Radio) win.getFellow("rgsexo")).setChecked(false);
            ((Radio) win.getFellow("rgsm")).setChecked(true);
        } else {
            ((Radio) win.getFellow("rgsexo")).setChecked(true);
            ((Radio) win.getFellow("rgsm")).setChecked(false);
        }
        ((Textbox) win.getFellow("tbnomePai")).setValue(estudante.getNomePai());
        ((Textbox) win.getFellow("tbnomeMae")).setValue(estudante.getNomeMae());
        ((Textbox) win.getFellow("tblocalidade")).setValue(estudante.getLocalidade());
        ((Textbox) win.getFellow("tbdistrito")).setValue(estudante.getDistrito());
        ((Intbox) win.getFellow("ibanoTerMedio")).setValue(estudante.getAnoTerMedio());
        ((Textbox) win.getFellow("tbescola")).setValue(estudante.getEscola());
        //  ((Intbox) win.getFellow("ibanoAdmissao")).setValue(estudante.getAnoAdmissao());
        // ((Intbox) win.getFellow("ibnivelFrequencia")).setValue(estudante.getNivelFrequencia());
        //  ((Textbox) win.getFellow("tbpastaDocumento")).setValue(estudante.getPastaDocumento());
        ((Textbox) win.getFellow("tbnomeEncarregado")).setValue(estudante.getNomeEncarregado());
        ((Textbox) win.getFellow("tbcontactoEncarregado")).setValue(estudante.getContactoEncarregado());
        ((Textbox) win.getFellow("tbgrauParentesco")).setValue(estudante.getGrauParentesco());
        final Iterator<Comboitem> itemsnacionalidade = new ArrayList(((Combobox) win.getFellow("cbnacionalidade")).getItems()).listIterator();
        Comboitem citnacionalidade;
        while (itemsnacionalidade.hasNext()) {
            citnacionalidade = itemsnacionalidade.next();
            if (((Pais) citnacionalidade.getValue()).getIdPais() == nacionalidade) {
                ((Combobox) win.getFellow("cbnacionalidade")).setSelectedItem(citnacionalidade);
                break;
            }
        }

        if (estudante.getPrimeiraUniversidade() != null) {
            ((Radio) win.getFellow("rgNuni")).setChecked(true);
            ((Radio) win.getFellow("rgSuni")).setChecked(false);
            ((Row) win.getFellow("rwPuni")).setVisible(true);
            ((Textbox) win.getFellow("tbprimeiraUniversidade")).setValue(estudante.getPrimeiraUniversidade());
        } else {
            ((Radio) win.getFellow("rgNuni")).setChecked(false);
            ((Radio) win.getFellow("rgSuni")).setChecked(true);
            ((Row) win.getFellow("rwPuni")).setVisible(false);
        }
        ((Textbox) win.getFellow("tbidioma")).setValue(estudante.getIdioma());
        final Iterator<Comboitem> itemsestadoCivil = new ArrayList(((Combobox) win.getFellow("cbestadoCivil")).getItems()).listIterator();
        Comboitem citestadoCivil;
        while (itemsestadoCivil.hasNext()) {
            citestadoCivil = itemsestadoCivil.next();
            if (((Estadocivil) citestadoCivil.getValue()).getIdEstado() == estadoCivil) {
                ((Combobox) win.getFellow("cbestadoCivil")).setSelectedItem(citestadoCivil);
                break;
            }
        }
        ((Textbox) win.getFellow("tbemail")).setValue(estudante.getEmail());

///// Endereco
        ((Textbox) win.getFellow("tbavenidaRua")).setValue(endereco.getAvenidaRua());
        ((Textbox) win.getFellow("tbbairro")).setValue(endereco.getBairro());
        ((Textbox) win.getFellow("tbtelefone")).setValue(endereco.getTelefone());
        final Iterator<Comboitem> itemsprovincia = new ArrayList(((Combobox) win.getFellow("cbprovinciaEndAL")).getItems()).listIterator();
        Comboitem citprovincia;
        while (itemsprovincia.hasNext()) {
            citprovincia = itemsprovincia.next();
            if (Objects.equals(((Provincia) citprovincia.getValue()).getIdProvincia(), endereco.getProvincia().getIdProvincia())) {
                ((Combobox) win.getFellow("cbprovinciaEndAL")).setSelectedItem(citprovincia);
                break;
            }
        }
/////Endereco Ferias
        ((Textbox) win.getFellow("tbavenidaRua2")).setValue(enderecof.getAvenidaRua());
        ((Textbox) win.getFellow("tbbairro2")).setValue(enderecof.getBairro());
        ((Textbox) win.getFellow("tbtelefone2")).setValue(enderecof.getTelefone());
        final Iterator<Comboitem> itemsprovincia2 = new ArrayList(((Combobox) win.getFellow("cbprovinciaEndPF")).getItems()).listIterator();
        Comboitem citprovincia2 = null;
        while (itemsprovincia2.hasNext()) {
            citprovincia2 = itemsprovincia2.next();
            if (Objects.equals(((Provincia) citprovincia2.getValue()).getIdProvincia(), enderecof.getProvincia().getIdProvincia())) {
                ((Combobox) win.getFellow("cbprovinciaEndPF")).setSelectedItem(citprovincia2);
                break;
            }
        }

        ((Intbox) win.getFellow("ibnotaAdmissao")).setValue(estudante.getNotaAdmissao());
        final Iterator<Comboitem> itemsescolaPais = new ArrayList(((Combobox) win.getFellow("cbescolaPais")).getItems()).listIterator();
        Comboitem citescolaPais;
        while (itemsescolaPais.hasNext()) {
            citescolaPais = itemsescolaPais.next();
            if (((Pais) citescolaPais.getValue()).getIdPais() == escolaPais) {
                ((Combobox) win.getFellow("cbescolaPais")).setSelectedItem(citescolaPais);
                break;
            }
        }
         if (escolaprov != null) {
            Long escolaprovincia = escolaprov.getIdProvincia();
            final Iterator<Comboitem> itemsescolaProv = new ArrayList(((Combobox) win.getFellow("cbescolaProvincia")).getItems()).listIterator();
            Comboitem citescolaProv;
            while (itemsescolaProv.hasNext()) {
                citescolaProv = itemsescolaProv.next();
                if (Objects.equals(((Provincia) citescolaProv.getValue()).getIdProvincia(), escolaprovincia)) {
                    ((Combobox) win.getFellow("cbescolaProvincia")).setSelectedItem(citescolaProv);
                    break;
                }
            }
        }

        
        ///Provincia naturalidade
        if(provnat != null){
           long pn = provnat.getIdProvincia();
           final Iterator<Comboitem> itemsprovnat = new ArrayList(((Combobox) win.getFellow("cbnaturalidade")).getItems()).listIterator();
            Comboitem cprov;
            while (itemsprovnat.hasNext()) {
                cprov = itemsprovnat.next();
                if (((Provincia) cprov.getValue()).getIdProvincia() == pn) {
                    ((Combobox) win.getFellow("cbnaturalidade")).setSelectedItem(cprov);
                    break;
                }
            }
            ((Combobox) win.getFellow("cbnaturalidade")).setReadonly(true);
            if(pn==12){
               ((Textbox) win.getFellow("tbnaturalidade")).setValue(estudante.getOutraViaIngresso());
               ((Row) win.getFellow("rwnaturalidade")).setVisible(true);
            }else{
               ((Row) win.getFellow("rwnaturalidade")).setVisible(false);
            }
        }
        //// VIAS DE INGRESSO
        final Iterator<Comboitem> itemsviaIngresso = new ArrayList(((Combobox) win.getFellow("cbviaIngresso")).getItems()).listIterator();
        Comboitem citviaIngresso;
        while (itemsviaIngresso.hasNext()) {
            citviaIngresso = itemsviaIngresso.next();
            if (((Viaingresso) citviaIngresso.getValue()).getIdViaIngresso() == viaIngresso) {
                ((Combobox) win.getFellow("cbviaIngresso")).setSelectedItem(citviaIngresso);
                break;
            }
        }
        if (iex != null) {
            final Iterator<Comboitem> itemsprovinciaAdmissao = new ArrayList(((Combobox) win.getFellow("cbprovinciaAdmissao")).getItems()).listIterator();
            Comboitem citprovinciaAdmissao;
            while (itemsprovinciaAdmissao.hasNext()) {
                citprovinciaAdmissao = itemsprovinciaAdmissao.next();
                if (Objects.equals(((Provincia) citprovinciaAdmissao.getValue()).getIdProvincia(), iex.getProvinciaAdmissao().getIdProvincia())) {
                    ((Combobox) win.getFellow("cbprovinciaAdmissao")).setSelectedItem(citprovinciaAdmissao);
                    break;
                }
            }
            ((Row) win.getFellow("rwPexameA")).setVisible(true);
        } else if (imu != null) {
            ((Textbox) win.getFellow("tbnomeUniversidade")).setValue(imu.getNomeUniversidade());
            final Iterator<Comboitem> itemsTipou = new ArrayList(((Combobox) win.getFellow("cbtipouniversidade")).getItems()).listIterator();
            Comboitem citTipou;
            while (itemsTipou.hasNext()) {
                citTipou = itemsTipou.next();
                if (citTipou.getLabel().equals(imu.getTipouniversidade())) {
                    ((Combobox) win.getFellow("cbtipouniversidade")).setSelectedItem(citTipou);
                    break;
                }
            }
            final Iterator<Comboitem> itemspaisUniversidade = new ArrayList(((Combobox) win.getFellow("cbpaisUniversidade")).getItems()).listIterator();
            Comboitem citpaisUniversidade;
            while (itemspaisUniversidade.hasNext()) {
                citpaisUniversidade = itemspaisUniversidade.next();
                if (Objects.equals(((Pais) citpaisUniversidade.getValue()).getIdPais(), imu.getPaisUniversidade().getIdPais())) {
                    ((Combobox) win.getFellow("cbpaisUniversidade")).setSelectedItem(citpaisUniversidade);
                    break;
                }
            }
            ((Row) win.getFellow("rwUmudanca")).setVisible(true);
            ((Row) win.getFellow("rwTipoU")).setVisible(true);
            ((Row) win.getFellow("rwPaisUmudanca")).setVisible(true);
        } else {
            // if (estudante.getOutraViaIngresso() != null) {
            ((Textbox) win.getFellow("tboutraViaIngresso")).setValue(estudante.getOutraViaIngresso());
            ((Row) win.getFellow("rwoutraVia")).setVisible(true);

            //}
        }

        ((Intbox) win.getFellow("ibtamAgregadoFamiliar")).setValue(estudante.getTamAgregadoFamiliar());
//////bolsa
        final Iterator<Comboitem> itemsbolsa = new ArrayList(((Combobox) win.getFellow("cbbolsa")).getItems()).listIterator();
        Comboitem citbolsa;
        while (itemsbolsa.hasNext()) {
            citbolsa = itemsbolsa.next();
            if (Objects.equals(((Bolsa) citbolsa.getValue()).getIdBolsa(), bolsa)) {
                ((Combobox) win.getFellow("cbbolsa")).setSelectedItem(citbolsa);
                break;
            }
        }
        if (ibo != null) {
            ((Datebox) win.getFellow("dabdataAprovacao")).setValue(ibo.getDataAprovacao());
            ((Textbox) win.getFellow("tbdoador")).setValue(ibo.getDoador());
            ((Row) win.getFellow("rwdataDoador")).setVisible(true);
            ((Row) win.getFellow("rwdataAprovacao")).setVisible(true);
        }
        // Messagebox.show(prof.getDescricaopr());
        if (prof != null) {
            ((Radio) win.getFellow("rgNP")).setChecked(false);
            ((Radio) win.getFellow("rgP")).setChecked(true);
            ((Textbox) win.getFellow("tbdescricaopr")).setValue(prof.getDescricaopr());
            ((Textbox) win.getFellow("tbavenidapr")).setValue(prof.getAvenidapr());
            ((Textbox) win.getFellow("tbcontactopr")).setValue(prof.getContactopr());
            ((Textbox) win.getFellow("tbbairropr")).setValue(prof.getBairropr());
//            ((Textbox) win.getFellow("tbdescricaopr")).setValue(prof.getDescricaopr());
            // ((Textbox) win.getFellow("tblocal")).setValue(prof.getLocal());
            final Iterator<Comboitem> itemsprovinciapr = new ArrayList(((Combobox) win.getFellow("cbprovinciapr")).getItems()).listIterator();
            Comboitem citprovinciapr;
            while (itemsprovinciapr.hasNext()) {
                citprovinciapr = itemsprovinciapr.next();
                if (Objects.equals(((Provincia) citprovinciapr.getValue()).getIdProvincia(), prof.getProvinciapr().getIdProvincia())) {
                    ((Combobox) win.getFellow("cbprovinciapr")).setSelectedItem(citprovinciapr);
                    break;
                }
            }
            ((Row) win.getFellow("rwProfissao")).setVisible(true);
        }

        par.clear();
        par.put("ide", estudante);
        Arquivoestudante am = csimpm.findEntByJPQuery("from Arquivoestudante am where"
                + " am.tipo=0 and am.idEstudante = :ide", par);
        if (am != null) {
            String ss = UserAutentic.getPathR(am.getTipo().toString()) + estudante.getNrEstudante() + am.getNomearquivo();
            //File f = new File(ss);
            InputStream is = new FileInputStream(ss);
            Media p = new AMedia(ss, FilenameUtils.getExtension(ss), URLConnection.guessContentTypeFromName(ss), is);
            Media m = new AMedia(am.getNomearquivo(), FilenameUtils.getExtension(ss), URLConnection.guessContentTypeFromName(ss), p.getByteData());
//        List<Media> lmed = new ArrayList<Media>();
//        lm.add(m);
            ((Listitem) win.getFellow("lif")).setValue(m);//.setModel(new ListModelList<Media>(lm));
//        Image i = new AImage(ss);
            org.zkoss.image.Image ii = new AImage(ss);
            ((org.zkoss.zul.Image) win.getFellow("picss").getChildren().get(0)).setContent(ii);
            ((Textbox) win.getFellow("tbimg")).setValue(ii.getName());
        }
        /////Tipo documento
        ((Datebox) win.getFellow("dabdataEmissao")).setValue(doc.getDataEmissao());
        ((Textbox) win.getFellow("tblocalEmissao")).setValue(doc.getLocalEmissao());
        ((Textbox) win.getFellow("tbnrDocumento")).setValue(doc.getNrDocumento());
        final Iterator<Comboitem> itemstipo = new ArrayList(((Combobox) win.getFellow("cbtipo")).getItems()).listIterator();
        Comboitem cittipo;
        while (itemstipo.hasNext()) {
            cittipo = itemstipo.next();
            if (Objects.equals(((Tipodocumento) cittipo.getValue()).getIdTipo(), doc.getTipo().getIdTipo())) {
                ((Combobox) win.getFellow("cbtipo")).setSelectedItem(cittipo);
                break;
            }
        }
        //bilhete
        List<Arquivoestudante> lam = csimpm.findByJPQuery("from Arquivoestudante am where"
                + " am.tipo=1 and am.idEstudante = :ide", par);
        List<Media> lm = new ArrayList<Media>();
        for (Arquivoestudante am2 : lam) {
            String sss = UserAutentic.getPathR(am2.getTipo().toString()) + estudante.getNrEstudante() + am2.getNomearquivo();
            //File f = new File(ss);
            InputStream iss = new FileInputStream(sss);
            //  ByteOutputStream by= new 
            Media pp = new AMedia(sss, FilenameUtils.getExtension(sss), URLConnection.guessContentTypeFromName(sss), iss);
            Media mm = new AMedia(am2.getNomearquivo(), FilenameUtils.getExtension(sss), URLConnection.guessContentTypeFromName(sss), pp.getByteData());
            //Media mmm = new
            //Image ii = new AImage(sss);
            //((org.zkoss.zul.Image) win.getFellow("picss").getChildren().get(0)).setContent(i);
            ((Textbox) win.getFellow("tbimg2")).setValue(am2.getNomearquivo());
            lm.add(mm);
            //  iss.close();
        }
        ((Listbox) win.getFellow("lbimg2")).setModel(new ListModelList(lm));
        //Certificado
        List<Arquivoestudante> lam2 = csimpm.findByJPQuery("from Arquivoestudante am where"
                + " am.tipo=2 and am.idEstudante = :ide", par);
        List<Media> lm2 = new ArrayList<Media>();
        for (Arquivoestudante am2 : lam2) {
            String sss = UserAutentic.getPathR(am2.getTipo().toString()) + estudante.getNrEstudante() + am2.getNomearquivo();
            //File f = new File(ss);
            InputStream iss = new FileInputStream(sss);
            Media pp = new AMedia(sss, FilenameUtils.getExtension(sss), URLConnection.guessContentTypeFromName(sss), iss);
            Media mm = new AMedia(am2.getNomearquivo(), FilenameUtils.getExtension(sss), URLConnection.guessContentTypeFromName(sss), pp.getByteData());
            //Image ii = new AImage(sss);
            //((org.zkoss.zul.Image) win.getFellow("picss").getChildren().get(0)).setContent(i);
            ((Textbox) win.getFellow("tbimg3")).setValue(am2.getNomearquivo());
            lm2.add(mm);
            //   iss.close();
        }
        ((Listbox) win.getFellow("lbimg1")).setModel(new ListModelList(lm2));
        //recibos
        ((Row) win.getFellow("rwRU")).setVisible(false);
        par.clear();
        par.put("mat", mat);
        List<Arquivomatricula> lam3 = csimpm.findByJPQuery("from Arquivomatricula am where"
                + " am.matricula = :mat", par);
        List<Arquivo> lm3 = new ArrayList<Arquivo>();
        List<Arquivo> lm4 = new ArrayList<Arquivo>();
        String recibousado = "";
        for (Arquivomatricula am3 : lam3) {
            String sss = UserAutentic.getPathR("3") + estudante.getNrEstudante() + mat.getMatriculaPK().getAno() + am3.getNomearquivo();
            //File f = new File(ss);
            InputStream iss = new FileInputStream(sss);
            Media pp = new AMedia(sss, FilenameUtils.getExtension(sss), URLConnection.guessContentTypeFromName(sss), iss);
            Media mm = new AMedia(am3.getNomearquivo(), FilenameUtils.getExtension(sss), URLConnection.guessContentTypeFromName(sss), pp.getByteData());
            //Image ii = new AImage(sss);
            //((org.zkoss.zul.Image) win.getFellow("picss").getChildren().get(0)).setContent(i);
            ((Textbox) win.getFellow("tbimg4")).setValue(mm.getName());
            if (am3.getRecibousado()!=null&&am3.getRecibousado()==true) {
                par.clear();
                par.put("nrt", am3.getNrtalao());
                List<Arquivomatricula> lamat = csimpm.findByJPQuery("from Arquivomatricula a where a.nrtalao like :nrt", par);
                String rs = "Recibo Nr.: "+am3.getNrtalao()+" foi usado por:", rs2 = "";
                for(Arquivomatricula ama : lamat){
                  rs2 = rs2 + "\nEstudante: "+ama.getEstudante()+"\n"
                     + "Taxa Matricula: "+ama.getValorcobrado()+" Data Matricula: "+ama.getDatamat();
                }
                recibousado = recibousado + "\n" + rs + "\n" + rs2;
            }
            if (!am3.getNrtalao().equals("0")) {
                lm3.add(new Arquivo(am3.getNrtalao(), mm, am3.getNomearquivo(),am3.getBanco(),am3.getValor(),am3.getEstudante(),am3.getDatadeposito()));
            } else {
                lm4.add(new Arquivo(am3.getNrtalao(), mm, am3.getNomearquivo(),am3.getBanco(),am3.getValor(),am3.getEstudante(),am3.getDatadeposito()));
            }
        }
         if (!recibousado.equals("")) {
            ((Row) win.getFellow("rwRU")).setVisible(true);
            ((Textbox) win.getFellow("txRU")).setText(recibousado);
        }
        ((Listbox) win.getFellow("lbimg")).setModel(new ListModelList(lm3));
        ((Listbox) win.getFellow("lbimgII")).setModel(new ListModelList(lm4));
    }

    //Pesquisar estudante
    public ListModel<Curso> getListaCursoModel() {
       // Users u = csimpm.get(Users.class, usr.getUtilizador());
        Faculdade f = csimpm.get(Faculdade.class, usr.getFaculdade().getIdFaculdade());
        par.clear();
         if (f != null) {
            par.put("fac", f);
            List<Curso> lc = csimpm.findByJPQuery("from Curso c where c.faculdade = :fac", par);
            return listaCursoModel = new ListModelList<Curso>(lc);
        } else{
            Clients.showNotification("O utilizador deve pertencer a uma Faculdade especifica", "warning", null, null, 0);
        }
        return null;
    }

    ////////////////////////////////////PESQUISAS//////////////////////////////////////////////////////
    public void onChanging$txProcNrmec(InputEvent evt) {
        if (!evt.getValue().equals("") && evt.getValue().charAt(0) != '.') {
            condnr = " and e.nrEstudante = :nr ";
            if (condpar.containsKey("nr")) {
                condpar.replace("nr", evt.getValue());
            } else {
                condpar.put("nr", evt.getValue());
            }
        } else {
            condnr = "";
            if (condpar.containsKey("nr")) {
                condpar.remove("nr");
            }
        }
        setLB(0, 20);
    }

    public void onClick$manoi(Event e) throws InterruptedException {
        ibProcAno.setVisible(manoi.isChecked());
        if (manoi.isChecked()) {
            Date dano = new Date();
            Calendar cal = new GregorianCalendar();
            cal.setTime(dano);
            ibProcAno.setValue(cal.get(Calendar.YEAR));
            condano = " and extract(year from i.dataInscricao) = :a ";
            condpar.put("a", ibProcAno.getValue());
        } else {
            condano = "";
            condpar.remove("a");
        }
        setLB(0, 20);
    }

    public void onChanging$txProcurar(InputEvent evt) {
        if (!evt.getValue().equals("") && evt.getValue().charAt(0) != '.') {
            condnome = " and lower(e.nomeCompleto) like :nome ";
            if (condpar.containsKey("nome")) {
                condpar.replace("nome", "%" + evt.getValue().toLowerCase() + "%");
            } else {
                condpar.put("nome", "%" + evt.getValue().toLowerCase() + "%");
            }
        } else {
            condnome = "";
            if (condpar.containsKey("nome")) {
                condpar.remove("nome");
            }
        }
        setLB(0, 20);
    }

    public void onChanging$ibProcAno(InputEvent evt) {
        if (!evt.getValue().equals("") && evt.getValue().charAt(0) != '.') {
            condano = " and extract(year from i.dataInscricao) = :a ";
            if (condpar.containsKey("a")) {
                condpar.replace("a", Integer.parseInt(evt.getValue()));
            } else {
                condpar.put("a", Integer.parseInt(evt.getValue()));
            }
        } else {
            condano = "";
            if (condpar.containsKey("a")) {
                condpar.remove("a");
            }
        }
        setLB(0, 20);
    }

    public void onChange$cbcurso() {
        // if (cbcurso.getSelectedIndex() != 0) {
        condcurso = " and e.cursocurrente = :curso ";
        if (condpar.containsKey("curso")) {
            condpar.replace("curso", (Curso) cbcurso.getSelectedItem().getValue());
        } else {
            condpar.put("curso", (Curso) cbcurso.getSelectedItem().getValue());
        }
        setLB(0, 20);
    }

    public void setLB(int i, int j) {
        if (j == 20) {
            lbEstudantes.setModel(new ListModelList<Matricula>());
        }
        Calendar c = Calendar.getInstance();
        par.clear();
        par.put("user", usr.getUtilizador());
       // Users u = csimpm.findEntByJPQuery("from Users u where u.utilizador = :user", par);
        par.clear();
        Faculdade f = csimpm.get(Faculdade.class, usr.getFaculdade().getIdFaculdade());
        par.put("fac", f);
        listaM = csimpm.findByJPQueryFilter("from Matricula m where m.curso.faculdade = :fac and m.confirmacao is null and m.modoMatricula = 3 and m.matriculaPK not in (select ma.matriculaPK from "
                + "Matriculaanulada ma)", par, i, j);

        final Iterator<Matricula> items = listaM.iterator();
        Matricula m;
        lbEstudantes.setRows(lbEstudantes.getItemCount() + listaM.size());
        while (items.hasNext()) {
            m = items.next();
            ((ListModelList) lbEstudantes.getModel()).add(m);
        }
        if (listaM.size() < j) {
            btv.setVisible(false);
        } else {
            btv.setVisible(true);
        }
    }

    public void onLoadi() {
        int i = lbEstudantes.getItemCount();
        setLB(i, i + 20);
    }
}
