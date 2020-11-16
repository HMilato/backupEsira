/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package esira.controller;

import esira.domain.Areacientifica;
import esira.domain.Caracter;
import esira.domain.Curso;
import esira.domain.Disciplina;
import esira.domain.Faculdade;
import esira.domain.Funcionario;
import esira.service.CRUDService;
import java.util.ArrayList;
import java.util.List;
import org.zkoss.util.media.Media;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.Button;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Image;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Paging;
import org.zkoss.zul.Row;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;
import org.zkoss.zul.event.ListDataListener;

/**
 *
 * @author Ussimane
 */
public class Experiencia extends GenericForwardComposer {

    @WireVariable
    private final CRUDService csimp = (CRUDService) SpringUtil.getBean("CRUDService");

    private static final long serialVersionUID = 43014628867656917L;

    List<Funcionario> funcionarios = csimp.getAll(Funcionario.class);
    private ListModel<Funcionario> funcionarioModel = new ListModelList<Funcionario>(funcionarios);
    List<Faculdade> faculdades = csimp.getAll(Faculdade.class);
    private ListModel<Faculdade> faculdadeModel = new ListModelList<Faculdade>(faculdades);
    List<Areacientifica> areascientificas = csimp.getAll(Areacientifica.class);
    private ListModel<Areacientifica> acientificaModel = new ListModelList<Areacientifica>(areascientificas);
    List<Curso> cursos = csimp.getAll(Curso.class);
    private ListModel<Curso> cursoModel = new ListModelList<Curso>(cursos);
    List<Caracter> caracteres = csimp.getAll(Caracter.class);
    private ListModel<Caracter> caracterModel = new ListModelList<Caracter>(caracteres);
    List<Disciplina> disciplinas = csimp.getAll(Disciplina.class);
    private ListModel<Disciplina> disciplinaModel = new ListModelList<Disciplina>(disciplinas);
    private Window windowFaculdade, mDialogAddFaculdade, mDialogAddACient, windowACient, mDialogAddCurso, windowCurso,
            mDialogAddDiscip, windowDiscip, winDiscDetalhe, windowPrecedencia;
    private Textbox txtDescricao, txtAbreviatura, txtAcient, txtCurso, txtAbrevCurso, txtCodCurso,
            txtAbrevDisc, txtNomeDisc, txtDpto, txtareaObjGeral;
    //private Textarea txtareaObjGeral;
    private Combobox cbdirector, cbfaculdade, cbPeriodDisc, cbcurso, cbcaracter, cbareacient, cbCursoPrec, cbdiscipPre, cbSemPrec;
    private Intbox fid, aid, cid, intSemestre, intDiscNivel, intcredito, did;
    private Listbox lbFaculdade, lbACient, lbCurso, lbDiscip, lbCursoPrec;
    private Label validationFac, validationAcient, validationCurso, validationDisc;

    private Paging pagDisc;
    private Listheader lhnome;
    static String ord = "";
    private Image img;
    // Doublebox d;
    Checkbox ch;
    Row rw;
    String cond;
    Button bv;
    Textbox tbimg;
    Label l;

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
    }

    public void imagem() {
    //   lbACient.appendChild(new Listitem(media.getName(), media));
        //  lbACient.setModel(new ListModelList(media));
        lbACient.removeChild(((Listitem) lbACient.getParent().getParent()));
       
        String[] ss = new String[]{"pdf", "doc", "docx", "xlsx", "xls"};
//        if (media instanceof org.zkoss.image.Image) {
//            s
//        } else {
//            for (String s : ss) {
//                if (s.equalsIgnoreCase(s)) {
//                    break;
//                }
//            }"style=\"border:none;background: transparent;position: absolute\""
//        }
        String s;

        List<Listitem> lit = lbACient.getItems();
        for (org.zkoss.zul.Listitem li : lit) {

//          if(((Media)li.getValue()).getName().equals(m.getName())){
//             lit.add(new Listitem(m, m.getName()))
//          } else {
//             org.zkoss.zk.ui.util.Clients.showNotification("Por favor, os nomes devem ser diferentes", "error", null, null, 0);
//             return;
//          }
        }
    }

    public void onClick$bv() {
        // Messagebox.show("dsgsfg");
        tbimg.getValue();
    }
}
