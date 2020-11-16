/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package esira.service;

import esira.domain.Curso;
import esira.domain.Disciplina;
import esira.domain.Documento;
import esira.domain.Endereco;
import esira.domain.Enderecof;
import esira.domain.Estudante;
import esira.domain.Funcionario;
import esira.domain.Grupo;
import esira.domain.Inscricao;
import esira.domain.Inscricaodisciplina;
import esira.domain.InscricaodisciplinaPK;
import esira.domain.Listaadmissao;
import esira.domain.Matricula;
import esira.domain.MatriculaPK;
import esira.domain.Pais;
import esira.domain.Prescricao;
import esira.domain.Users;
import esira.domain.Utilizadorgeral;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import org.hibernate.Transaction;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Messagebox;

/**
 *
 * @author Ussimane //Ultimos Factores de Sucesso: curso misturado com numero de
 * estudante, removidos manualmente; Encontrou-se dois pontos depois do nome do
 * estudante/curso Estudantes sem numero, numero de estudante mal escrito com
 * espacos 20132004333: Numero de estudante inventado Teodoro Augusto Torres e
 * numa das suas disciplinas nao tinha ano paulino raul tambem tem anos em
 * falta; Número do estudante-20152004025
 *
 * Tenho que ter a lista de estudantes que ja mudaram de curso
 */
public class Populardados {

    static private CRUDService csimpm = (CRUDService) org.zkoss.zkplus.spring.SpringUtil.getBean("CRUDService");
    static Map<String, Object> par = new HashMap<>();

    public static void popularDisciplinas(Funcionario f) throws FileNotFoundException, IOException {
        //Serao validadas pelo administrador
        //inscricao num ano, se tiver pelo menos uma nota
        //assumi-se que se escreveu em todas disciplinas do semestre
        Inscricao i = null, i2 = null;
        Disciplina d;
        Inscricaodisciplina id = null, id2 = null;
        int ano = -1;
        int ano3 = -1;
        int ano2 = 0;
        int ano4 = 0;
        // File arquivo = new File("inscricao.txt");
        Pais pa = csimpm.findEntByJPQuery("from Pais p where p.idPais=122", null);
//        RandomAccessFile raf = new RandomAccessFile(arquivo, "rw");
//        Messagebox.show(raf.getFilePointer()+"   "+raf.length());
//        while (raf.getFilePointer() != raf.length()) {
        String nr;
        String nome, nome2 = "...";
        String c;
        List<Integer> lintP = new ArrayList<Integer>();
        List<Integer> lintP2 = new ArrayList<Integer>();
        String st = null;
        String anoi;
        List<Disciplina> ldisc = new ArrayList<Disciplina>();
        List<Disciplina> ldiscf = new ArrayList<Disciplina>();
        String nrd;
        String nota;
        Date data;
        Estudante e = null;
        Users user = null;
        BufferedReader bufReader = new BufferedReader(
                new InputStreamReader(Sessions.getCurrent().getWebApp().getServletContext().getResourceAsStream("/fonteleg.txt")));//("/inscricao.txt")));
        String prop = null;

        List<Estudante> le = csimpm.findByJPQuery("from Estudante e where e.anoIngresso = 2016", null);
        final Iterator<Estudante> itemscursocurrente = le.listIterator();
        Estudante es = null;
        while (itemscursocurrente.hasNext()) {
            es = itemscursocurrente.next();
            if (es.getMatriculaList().size() == 0) {
                MatriculaPK mpk = new MatriculaPK(es.getIdEstudante(), 2016);
                Matricula mat = new Matricula(mpk);
                mat.setEstado(true);
                mat.setEstudante(es);
                mat.setAnulada(false);
                mat.setCurso(es.getCursocurrente());
                mat.setValor(Float.NaN);
                data = new Date();
                data.setYear(es.getAnoIngresso() - 1900);
                data.setMonth(1);
                mat.setModoMatricula(3);
                mat.setDataMatricula(data);
                mat.setConfirmacao(data);
                mat.setFuncionario(f);
                par.clear();
                par.put("mat", mpk);
                Matricula matt = csimpm.findEntByJPQuery("from Matricula m where m.matriculaPK = :mat", par);
                if (matt == null) {
                    csimpm.Save(mat);
                }
            }
        }

        if (false) {
            while ((prop = bufReader.readLine()) != null) {
                String s = prop;//raf.readLine();
                if (s.contains("Nome do estudante:")) {
//                Transaction t = csimpm.getTransacao();
//                try {
//                    t.begin();

                    String ss = s.substring(s.indexOf(":") + 1);
                    if (ss.charAt(0) == ' ') {
                        ss = s.substring(s.indexOf(":") + 2);
                    }
                    int ind = ss.indexOf("Número");
                    if (ind != -1) {
                        nome = ss.substring(0, ind - 1);

                        String sh = ss.substring(ind);
                        nr = sh.substring(sh.indexOf(":") + 1);
                        if (nr.charAt(0) == ' ') {
                            nr = sh.substring(sh.indexOf(":") + 2);
                        }
                    } else {
                        nome = s.substring(s.indexOf(":") + 1);
                        if (nome.charAt(0) == ' ') {
                            nome = s.substring(s.indexOf(":") + 2);
                        }
                        do {
                            s = bufReader.readLine();
                        } while (!s.contains("Número"));
                        //  Messagebox.show(s);
                        nr = s.substring(s.indexOf(":") + 1);
                        if (nr.charAt(0) == ' ') {
                            nr = s.substring(s.indexOf(":") + 2);
                        }
                    }

                    do {
                        s = bufReader.readLine();
                    } while (!s.contains("Curso"));
                    // Messagebox.show(s);
                    c = s.substring(s.indexOf(":") + 1);
                    if (c.charAt(0) == ' ') {
                        c = s.substring(s.indexOf(":") + 2);
                    }
                    do {
                        s = bufReader.readLine();
                    } while (!s.contains("Ingresso"));
                    // Messagebox.show(s);
                    anoi = s.substring(s.indexOf(":") + 1);
                    if (anoi.charAt(0) == ' ') {
                        anoi = s.substring(s.indexOf(":") + 2);
                    }
//                i = new Inscricao();
//                i2 = new Inscricao();

                    while (!s.contains("Pemba,")) {
                        s = bufReader.readLine();
                        if (s.contains("Ano da realização da disciplina")) {
                            while (!s.contains("Pemba,")) {
                                s = bufReader.readLine();
                                if (s.contains("Ano da realização da disciplina")) {
                                    s = bufReader.readLine();
                                    if (!s.contains("Introdução à Engenharia*")) {
                                        //   if(s.contains("MÉDIA FINAL")) break;
                                        par.clear();
                                        par.put("cu", "%" + c + "%");
                                        Curso cu = csimpm.findEntByJPQuery("from Curso c where c.descricao like :cu", par);
                                        if (cu == null) {
//                                    if(c.contains("Chefe"))break;
                                            Messagebox.show("Nao encontrou o curso: " + c);
                                            return;
                                        }
                                        e = new Estudante();
                                        e.setNrEstudante(nr);
                                        e.setNomeCompleto(nome);
                                        e.setApelido(obterApelido(e.getNomeCompleto()));
                                        e.setAnoIngresso(Integer.parseInt(anoi));
                                        e.setNacionalidade(pa);
                                        e.setMasculino(false);
                                        e.setEstado(true);
                                        e.setTurma(1);
                                        e.setTurno(1);
                                        //  e.setNivelFrequencia(1);
                                        e.setCursocurrente(cu);
                                        e.setCursoingresso(cu);
                                        if (!nome2.equals(nome)) {
                                            lintP.clear();
                                            for (int h = 0; h < 8; h++) {
                                                lintP.add(0);
                                            }
                                            lintP2.clear();
                                            for (int h = 0; h < 8; h++) {
                                                lintP2.add(0);
                                            }
                                            nome2 = nome;
                                            ano = -1;
                                            ano3 = -1;
                                            csimpm.Save(e);
                                            ldisc.clear();
                                            ldisc.addAll(cu.getDisciplinaList());
                                            Endereco end1 = new Endereco(e.getIdEstudante());
                                            end1.setEstudante(e);
                                            csimpm.Save(end1);
                                            Enderecof end2 = new Enderecof(e.getIdEstudante());
                                            end2.setEstudante(e);
                                            csimpm.Save(end2);
                                            Documento doc = new Documento(e.getIdEstudante());
                                            doc.setEstudante(e);
                                            csimpm.Save(doc);
                                            user = new Users();
                                            user.setNome(e.getNomeCompleto());
                                            user.setUtilizador(e.getNomeCompleto().charAt(0) + e.getApelido());
                                            user.setUtilizador(semAcento(user.getUtilizador().toLowerCase()));
                                            user.setIdEstudante(e);
                                            user.setUestudante(true);
                                            user.setPasword(user.getUtilizador());
                                            //user.setEmail(e.getEmail());
                                            user.setFaculdade(f.getFaculdade());
                                            user.setTenant(f.getFaculdade().getTenant());
                                            user.setIdGrupo(csimpm.get(Grupo.class, "Estudante"));
                                            int ii = 0;
                                            boolean b;
                                            do {
                                                b = guardar(user, ii);
                                                ii++;
                                            } while (!b);
                                        }
                                        while (!s.contains("Pemba,")) {
                                            //      System.out.println(s);
                                            List<Integer> lint = new ArrayList<Integer>();
                                            List<Integer> lint2 = new ArrayList<Integer>();
                                            List<Integer> anos1 = new ArrayList<Integer>();
                                            List<Integer> anos2 = new ArrayList<Integer>();
                                            for (int k = 0; k < s.length(); k++) {
                                                //System.out.println(s.charAt(k) + " " + ((int) s.charAt(k)));
                                                if (((int) s.charAt(k)) == 9) {
                                                    lint.add(new Integer(k));
                                                }
                                            }
                                            if (lint.size() > 0 && !s.substring(0, lint.get(0)).equals("")) {
                                                boolean bool = false;
                                                while (!bool) {
                                                    while ((lint.size() > 5 && lint.size() < 11) || (lint.size() < 5)) {
                                                        s = s + bufReader.readLine();
                                                        // System.out.println(s);
                                                        lint.clear();
                                                        for (int k = 0; k < s.length(); k++) {
                                                            //System.out.println(s.charAt(k) + " " + ((int) s.charAt(k)));
                                                            if (((int) s.charAt(k)) == 9) {
                                                                lint.add(new Integer(k));
                                                            }
                                                        }
                                                        //

                                                    }

                                                    st = bufReader.readLine();
//                                            Messagebox.show(st + " " + st.length());
//                                            Messagebox.show(s.substring(lint.get(lint.size() - 2), lint.get(lint.size() - 1)));
//                                            if (s.substring(lint.get(lint.size() - 2), lint.get(lint.size() - 1)).contains("R")) {
//                                                if (st.length() == 4) {
//                                                    Messagebox.show(st + " st lint " + lint.size());
//                                                }
//                                            }
                                                    lint2.clear();
                                                    for (int k = 0; k < st.length(); k++) {
                                                        //System.out.println(s.charAt(k) + " " + ((int) s.charAt(k)));
                                                        if (((int) st.charAt(k)) == 9) {
                                                            lint2.add(new Integer(k));
                                                        }
                                                    }
                                                    //    if(lint2.size() > 0)   Messagebox.show(e.getNomeCompleto()+" "+s+" :"+st+" "+lint2.get(0));
                                                    if (st.length() == 4 || st.length() == 8 || (lint2.size() > 0 && st.substring(0, lint2.get(0)).length() == 4)) {
                                                        if (st.length() != 8) {
                                                            //   Messagebox.show(":ant: " + s + "  cortado: " + s.substring(0, lint.get(lint.size() - 1)));
                                                            if (s.substring(lint.get(lint.size() - 2), lint.get(lint.size() - 1)).contains("R")) {
                                                                if (st.length() == 4) {
                                                                    if (lint.size() > 5) {
                                                                        String an = s.substring(lint.get(lint.size() - 1) + 1);
                                                                        if (an.charAt(0) == ' ') {
                                                                            an = an.substring(1);
                                                                        }
                                                                        anos2.add(Integer.parseInt(an));
                                                                    } else {
                                                                        String an = s.substring(lint.get(lint.size() - 1) + 1);
                                                                        if (an.charAt(0) == ' ') {
                                                                            an = an.substring(1);
                                                                        }
                                                                        anos1.add(Integer.parseInt(an));
                                                                    }
                                                                } else {
                                                                    String an = s.substring(lint.get(lint.size() - 1) + 1);
                                                                    if (an.charAt(0) == ' ') {
                                                                        an = an.substring(1);
                                                                    }
                                                                    anos1.add(Integer.parseInt(an));
                                                                }
                                                            }
                                                            s = s.substring(0, lint.get(lint.size() - 1)) + String.valueOf((char) 9) + st;
                                                            //  Messagebox.show(":dep: " + st);
                                                            // Messagebox.show(" total : " + s);
                                                        }
                                                        st = null;

                                                        lint.clear();
                                                        for (int k = 0; k < s.length(); k++) {
                                                            //System.out.println(s.charAt(k) + " " + ((int) s.charAt(k)));
                                                            if (((int) s.charAt(k)) == 9) {
                                                                lint.add(new Integer(k));
                                                            }
                                                        }
                                                        // bool = false;
                                                    } else {
                                                        bool = true;
                                                    }
                                                }
//                                        Messagebox.show(anos1.size() + "" + anos2.size());
                                                //    Messagebox.show(lint.size() + "");
                                                //Primeiro Semestre
                                                //   System.out.println(s);  uma linha completa de disciplina
                                                nrd = s.substring(0, lint.get(0));
                                                // Messagebox.show("nrd: "+nrd);
                                                par.clear();
                                                par.put("co", nrd);
                                                d = csimpm.findEntByJPQuery("from Disciplina d where d.codigo = :co", par);
                                                if (d == null) {
                                                    Messagebox.show(nrd);
                                                    System.out.println("Nao encontrou a Disciplina: " + s.substring(lint.get(0) + 1, lint.get(1)) + "///" + s + "///" + e.getNomeCompleto());
//                                            Messagebox.show("Nao encontrou a Disciplina: " + s.substring(lint.get(0) + 1, lint.get(1)) + "///" + s + "///" + e.getNomeCompleto());
//                                            return;
                                                    break;
                                                }
                                                //  Messagebox.show(s+" "+d.getNome());
                                                // nota = s.substring(lint.get(8) + 1, lint.get(9));
                                                nota = s.substring(lint.get(3) + 1, lint.get(4));
                                                //System.out.println("nota: " + nota);
                                                //  nota.matches("^[0-9]*[.]{0,1}[0-9]*$")
                                                //if(!s.contains(""))
                                                if ((lint.size() > 5) && (!espaco(s.substring(lint.get(4) + 1, lint.get(5))).equals(""))) {
                                                    if (!nota.equals("") && (nota.matches("^[0-9]*$") || nota.equals("R"))) {
                                                        if (lint.size() > 5) {
                                                            ano2 = Integer.parseInt(espaco(s.substring(lint.get(4) + 1, lint.get(5))));////////////////////////////////////////////////////////////
                                                        } else {
                                                            ano2 = Integer.parseInt(s.substring(lint.get(4) + 1));
                                                        }
                                                        if (ano != ano2) {
                                                            ano = ano2;
                                                            MatriculaPK mpk = new MatriculaPK(e.getIdEstudante(), ano);
                                                            Matricula mat = new Matricula(mpk);
                                                            mat.setEstado(true);
                                                            mat.setEstudante(e);
                                                            mat.setAnulada(false);
                                                            mat.setCurso(cu);
                                                            mat.setValor(Float.NaN);
                                                            data = new Date();
                                                            data.setYear(ano2 - 1900);
                                                            data.setMonth(1);
                                                            mat.setModoMatricula(3);
                                                            mat.setDataMatricula(data);
                                                            mat.setConfirmacao(data);
                                                            mat.setFuncionario(f);
                                                            par.clear();
                                                            par.put("mat", mpk);
                                                            Matricula matt = csimpm.findEntByJPQuery("from Matricula m where m.matriculaPK = :mat", par);
                                                            if (matt == null) {
                                                                csimpm.Save(mat);
                                                            }
                                                            par.clear();
                                                            par.put("e", e);
                                                            par.put("ano", ano);
                                                            par.put("sem", new Short("1"));
                                                            i = csimpm.findEntByJPQuery("from Inscricao i where i.idEstudante = :e and extract(year from i.dataInscricao)"
                                                                    + " = :ano and i.semestre = :sem", par);
                                                            if (i == null) {
                                                                i = new Inscricao();
                                                                i.setSemestre(new Short("1"));
                                                                i.setModoInscricao(new Short("3"));
                                                                i.setDataInscricao(data);
                                                                i.setIdEstudante(e);
                                                                i.setDataConfirmacao(data);
                                                                i.setTaxaInscricao(Float.NaN);
                                                                i.setFuncionario(f);
                                                                i.setEstado(true);
                                                                i.setIdEstudante(e);
                                                                csimpm.Save(i);
                                                            }
                                                        }
                                                        id = new Inscricaodisciplina(new InscricaodisciplinaPK(i.getIdInscricao(), d.getIdDisc()));
                                                        id.setEstado(true);
                                                        id.setDisciplinaActiva(new Short("3"));
                                                        id.setInscricao(i);
                                                        id.setDisciplina(d);
                                                        if (!nota.equals("R")) {
                                                            // int indice = ano - e.getAnoIngresso();//houve uma mudanca no ano das disciplinas porque posso reprovar em 2013 e faze-las em 2014 porem devo levar o nivel original da disciplina
                                                            //  indice = indice + indice;
                                                            int indice = d.getNivel();
                                                            //if(indice==1)indice = indice-1;
                                                            lintP.set(indice - 1, lintP.get(indice - 1) + 1);
                                                            //Messagebox.show(lintP.size()+" 1o "+(indice)+" "+(lintP.get(indice)));
                                                            id.setNotaFinal(Float.parseFloat(nota));
                                                        } else {
                                                            id.setNotaFinal(Float.parseFloat("8"));
                                                        }
                                                        par.clear();
                                                        par.put("pk", id.getInscricaodisciplinaPK());
                                                        Inscricaodisciplina idi = csimpm.findEntByJPQuery("from Inscricaodisciplina id where id.inscricaodisciplinaPK = :pk", par);
                                                        if (idi == null) {
                                                            csimpm.Save(id);
                                                        }
                                                        if (nota.equals("R")) {
                                                            if (anos1.size() == 1) {
                                                                Prescricao p = new Prescricao();
                                                                p.setEstado(false);
                                                                p.setInscricaodisciplina(id2);
                                                                p.setInscricaodisciplinaPK(id.getInscricaodisciplinaPK());
                                                                p.setOperador(f);
                                                                csimpm.Save(p);
                                                            }
                                                        }
                                                        if (anos1.size() > 0) {
                                                            for (int h = 0; h < anos1.size(); h++) {
                                                                par.clear();  ///DISCIPLINAS REPROVADAS
                                                                par.put("e", e);
                                                                par.put("ano", anos1.get(h));
                                                                par.put("sem", d.getSemestre().shortValue());
                                                                data = new Date();
                                                                data.setYear(anos1.get(h) - 1900);
                                                                data.setMonth(1);
                                                                Inscricao ij = csimpm.findEntByJPQuery("from Inscricao i where i.idEstudante = :e and extract(year from i.dataInscricao)"
                                                                        + " = :ano and i.semestre = :sem", par);
                                                                if (ij == null) {
                                                                    ij = new Inscricao();
                                                                    ij.setSemestre(new Short("1"));
                                                                    ij.setModoInscricao(new Short("3"));
                                                                    ij.setDataInscricao(data);
                                                                    ij.setIdEstudante(e);
                                                                    ij.setDataConfirmacao(data);
                                                                    ij.setTaxaInscricao(Float.NaN);
                                                                    ij.setFuncionario(f);
                                                                    ij.setEstado(true);
                                                                    ij.setIdEstudante(e);
                                                                    csimpm.Save(ij);
                                                                }
                                                                id = new Inscricaodisciplina(new InscricaodisciplinaPK(ij.getIdInscricao(), d.getIdDisc()));
                                                                id.setEstado(true);
                                                                id.setDisciplinaActiva(new Short("2"));
                                                                id.setInscricao(ij);
                                                                id.setDisciplina(d);
                                                                id.setNotaFinal(Float.parseFloat("8"));
                                                                csimpm.Save(id);
                                                            }
                                                        }
//                                        }
                                                        // System.out.println("ano: " + ano2);
                                                    } else {
                                                    }
                                                }
                                                //Segundo Semestre
                                                if (lint.size() > 7) {
                                                    //          Messagebox.show(s);
                                                    nota = s.substring(lint.get(9) + 1, lint.get(10));
                                                    if (!nota.equals("")) {
                                                        nrd = s.substring(lint.get(5) + 1, lint.get(6));
                                                        par.clear();
                                                        par.put("co", nrd);
                                                        d = csimpm.findEntByJPQuery("from Disciplina d where d.codigo = :co", par);
                                                        if (d == null) {
                                                            System.out.println("Nao encontrou a Disciplina: " + s.substring(lint.get(6) + 1, lint.get(7)) + "///" + s + "///" + e.getNomeCompleto());
//                                                    Messagebox.show("Nao encontrou a Disciplina: " + s.substring(lint.get(6) + 1, lint.get(7)) + "///" + s + "///" + e.getNomeCompleto());
//                                                    return;
                                                            break;
                                                        }
                                                        if (!nota.equals("") && (nota.matches("^[0-9]*$") || nota.equals("R"))) {
                                                            System.out.println("nota: " + nota);
                                                            ano4 = Integer.parseInt(espaco(s.substring(lint.get(10) + 1)));
                                                            if (ano3 != ano4) {
                                                                ano3 = ano4;
                                                                data = new Date();
                                                                data.setYear(ano3 - 1900);
                                                                data.setMonth(7);
                                                                par.clear();
                                                                par.put("e", e);
                                                                par.put("ano", ano3);
                                                                par.put("sem", new Short("2"));
                                                                i2 = csimpm.findEntByJPQuery("from Inscricao i where i.idEstudante = :e and extract(year from i.dataInscricao)"
                                                                        + " = :ano and i.semestre = :sem", par);
                                                                if (i2 == null) {
                                                                    i2 = new Inscricao();
                                                                    i2.setDataInscricao(data);
                                                                    i2.setIdEstudante(e);
                                                                    i2.setDataConfirmacao(data);
                                                                    i2.setTaxaInscricao(Float.NaN);
                                                                    i2.setSemestre(new Short("2"));
                                                                    i2.setModoInscricao(new Short("3"));
                                                                    i2.setFuncionario(f);
                                                                    i2.setEstado(true);
                                                                    i2.setIdEstudante(e);
                                                                    csimpm.Save(i2);
                                                                }
                                                            }
                                                            id = new Inscricaodisciplina(new InscricaodisciplinaPK(i2.getIdInscricao(), d.getIdDisc()));
                                                            id.setEstado(true);
                                                            id.setDisciplinaActiva(new Short("3"));
                                                            id.setInscricao(i2);
                                                            id.setDisciplina(d);
                                                            if (!nota.equals("R")) {
//                                                        int indice = (ano3 - e.getAnoIngresso());  //houve uma mudanca no ano das disciplinas porque posso reprovar em 2013 e faze-las em 2014 porem devo levar o nivel original da disciplina
//                                                        Messagebox.show(" ind "+indice+" ano: " +ano3);
//                                                        indice = indice + indice;
                                                                // lintP.set(indice+1, lintP.get(indice+1) + 1);
                                                                int indice = d.getNivel();
                                                                //if(indice!=1)indice = indice+1;
                                                                lintP2.set(indice - 1, lintP2.get(indice - 1) + 1);
//                                                        Messagebox.show(lintP.size()+" 2o "+(indice+1)+" "+(lintP.get(indice+1)));
                                                                id.setNotaFinal(Float.parseFloat(nota));
                                                            } else {
                                                                id.setNotaFinal(Float.parseFloat("8"));
                                                            }
//                                                    par.clear();
//                                                    par.put("pk",id.getInscricaodisciplinaPK());
//                                                    Inscricaodisciplina idi = csimpm.findEntByJPQuery("from Inscricaodisciplina id where id.inscricaodisciplinaPK = :pk", par);
//                                                    if(idi==null){
                                                            csimpm.Save(id);
                                                            if (nota.equals("R")) {
                                                                if (anos2.size() == 1) {
                                                                    Prescricao p = new Prescricao();
                                                                    p.setEstado(false);
                                                                    p.setInscricaodisciplina(id2);
                                                                    p.setInscricaodisciplinaPK(id.getInscricaodisciplinaPK());
                                                                    p.setOperador(f);
                                                                    csimpm.Save(p);
                                                                }
                                                            }
                                                            if (anos2.size() > 0) {
                                                                for (int h = 0; h < anos2.size(); h++) {
                                                                    par.clear();  ///DISCIPLINAS REPROVADAS
                                                                    par.put("e", e);
                                                                    par.put("ano", anos2.get(h));
                                                                    par.put("sem", d.getSemestre().shortValue());
                                                                    data = new Date();
                                                                    data.setYear(anos2.get(h) - 1900);
                                                                    data.setMonth(1);
                                                                    Inscricao ij = csimpm.findEntByJPQuery("from Inscricao i where i.idEstudante = :e and extract(year from i.dataInscricao)"
                                                                            + " = :ano and i.semestre = :sem", par);
                                                                    if (ij == null) {
                                                                        ij = new Inscricao();
                                                                        ij.setSemestre(new Short("1"));
                                                                        ij.setModoInscricao(new Short("3"));
                                                                        ij.setDataInscricao(data);
                                                                        ij.setIdEstudante(e);
                                                                        ij.setDataConfirmacao(data);
                                                                        ij.setTaxaInscricao(Float.NaN);
                                                                        ij.setFuncionario(f);
                                                                        ij.setEstado(true);
                                                                        ij.setIdEstudante(e);
                                                                        csimpm.Save(ij);
                                                                    }
                                                                    id = new Inscricaodisciplina(new InscricaodisciplinaPK(ij.getIdInscricao(), d.getIdDisc()));
                                                                    id.setEstado(true);
                                                                    id.setDisciplinaActiva(new Short("2"));
                                                                    id.setInscricao(ij);
                                                                    id.setDisciplina(d);
                                                                    id.setNotaFinal(Float.parseFloat("8"));
                                                                    csimpm.Save(id);
                                                                }
                                                            }
//                                                }
                                                        }
                                                    }
                                                }
                                            } else {
                                                st = null;
                                            }
                                            if (st != null) {
                                                s = st;
                                            } else {
                                                s = bufReader.readLine();//raf.readLine();
                                            }
                                        }
                                        int an = 0, x = 0, sem = 1, cont = 0, anoing = 1;
                                        boolean sair = false;
//                                for (int g = 0; (e.getAnoIngresso() + an) < 2016 && g < lintP.size() && !sair; g++) {
//                                    if (x == 2) {
//                                        x=0;
//                                        an = an + 1;
//                                        sem = 1;
//                                    }
//                                    for (Disciplina dis : ldisc) {
//                                        if (dis.getSemestre() == sem && dis.getNivel() == an + 1) {
//                                            cont = cont + 1;
//                                        }
//                                    }
//                                    //           Messagebox.show(cont + "");
////                                      Messagebox.show(lintP.get(g)+"");
////                                        Messagebox.show("Total: "+(cont - lintP.get(g))+"  dados:"+" "+cont+" "+lintP.get(g));
//                                    if (cont - lintP.get(g) > 2) {
//                                        sair = true;
//                                    } else {
//                                       // Messagebox.show((e.getAnoIngresso() + an)+"   < 2016 "+g+"  <"+lintP.size());
//                                        if (sem == 2) {
//                                           if(an>0) anoing = anoing + 1;
//                                      //      Messagebox.show(anoing+"");
//                                        }
//                                    }
//                                    if (x!=2){
//                                        x = x + 1;
//                                        sem = 2;
//                                    }
//                                    cont = 0;
//                                }
                                        int cont2 = 0;
                                        for (int g = 0; g < 8 && sair == false; g++) {

                                            for (Disciplina dis : ldisc) {
                                                if (dis.getSemestre() == 1 && dis.getNivel() == g + 1) {
                                                    cont = cont + 1;
                                                }
                                            }
                                            for (Disciplina dis : ldisc) {
                                                if (dis.getSemestre() == 2 && dis.getNivel() == g + 1) {
                                                    cont2 = cont2 + 1;
                                                }
                                            }
                                            //           Messagebox.show(cont + "");
//                                      Messagebox.show(lintP.get(g)+"");
//                                        Messagebox.show("Total: "+(cont - lintP.get(g))+"  dados:"+" "+cont+" "+lintP.get(g));
                                            if (cont - lintP.get(g) > 2 || cont2 - lintP2.get(g) > 2) {
                                                if (g > 0 && lintP.size() > 0 && lintP2.size() > 0) {
                                                    anoing = anoing + 1;
                                                }
                                                sair = true;
                                            } else {
                                                if (g > 0) {
                                                    anoing = anoing + 1;
                                                }
                                            }
                                            cont = 0;
                                            cont2 = 0;
                                        }
                                        e.setNivelFrequencia(anoing);
//                             //   Messagebox.show(anoing+"");
                                        csimpm.update(e);
                                    }
                                }
                            }
                        }
                    }
//                    t.commit();
//                } catch (Exception ex) {
//                    t.rollback();
//                    Clients.showNotification("Erro", "error", null, null, 2000);
//                }
                }

            }
        }

    }

    public static void popularEstudantes(Funcionario f) throws FileNotFoundException {
//        File arquivo = new File("src/evento/Evento.java");
//        RandomAccessFile raf = new RandomAccessFile(arquivo, "rw");
        Estudante e = null;
        Users user = null;
        List<Listaadmissao> la = csimpm.getAll(Listaadmissao.class);
        for (Listaadmissao l : la) {
            if (l.getIdEstudante() == null) {
                e = new Estudante();
                e.setNrEstudante(semEspacoInicial(l.getNumero()));
                e.setNomeCompleto(semEspacoInicial(l.getNome()));
                e.setApelido(obterApelido(e.getNomeCompleto()));
                e.setAnoIngresso(l.getAno());
                e.setNacionalidade(new Pais(122));
                e.setMasculino(false);
                e.setEstado(true);
                e.setTurma(l.getTurno());
                e.setTurno(1);
                e.setNivelFrequencia(1);
                e.setCursocurrente(l.getCurso());
                e.setCursoingresso(l.getCurso());
                e.setPlanoc(l.getCurso().getPlanoc());
                csimpm.Save(e);
                Endereco end1 = new Endereco(e.getIdEstudante());
                end1.setEstudante(e);
                csimpm.Save(end1);
                Enderecof end2 = new Enderecof(e.getIdEstudante());
                end2.setEstudante(e);
                csimpm.Save(end2);
                Documento doc = new Documento(e.getIdEstudante());
                doc.setEstudante(e);
                csimpm.Save(doc);
                user = new Users();
                user.setNome(e.getNomeCompleto());
                user.setUtilizador(e.getNomeCompleto().charAt(0) + e.getApelido());
                user.setUtilizador(semAcento(user.getUtilizador().toLowerCase()));
                user.setIdEstudante(e);
                user.setUestudante(true);
                user.setPasword(user.getUtilizador());
                //user.setEmail(e.getEmail());
                user.setFaculdade(f.getFaculdade());
                user.setTenant(f.getFaculdade().getTenant());
                user.setIdGrupo(csimpm.get(Grupo.class, "Estudante"));
                int ii = 0;
                boolean b;
                do {
                    b = guardar(user, ii);
                    ii++;
                } while (!b);
                csimpm.get(Listaadmissao.class, l.getIdaluno());
                l.setIdEstudante(e);
                csimpm.update(l);
            }
        }
    }

    public void popularInscricao() throws FileNotFoundException {
        File arquivo = new File("src/evento/Evento.java");
        RandomAccessFile raf = new RandomAccessFile(arquivo, "rw");
    }

    public static String obterApelido(String n) {
        int k = 0;
        char c = ' ';
        for (int i = n.length() - 1; i > 0 && (i + 1) != k; i--) {

            if (n.charAt(i) == ' ' || n.charAt(i) == '.') {
                if (c != ' ' || c != '.') {
                    k = i;
                    return n.substring(k + 1);
                }
            } else {
                if (c == ' ' || c == '.') {
                    c = n.charAt(i);
                }
            }
        }
        return n.substring(k + 1);
    }

    public static boolean guardar(Users user, int i) {
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

    public static String semAcento(String str) {
        String normal = Normalizer.normalize(str, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(normal).replaceAll("");
    }

    public static String semEspacoInicial(String s) {
        while (s.length() > 1 && s.charAt(0) == ' ') {
            s = s.substring(1);
        }
        while (s.length() > 1 && s.charAt(s.length()-1) == ' ') {
            s = s.substring(0, s.length()-2);
        }
        return s;
    }

    public static String espaco(String s) {
        String k = s;
        String j = "";
        if (s.length() > 1 && s.charAt(0) == ' ') {
            k = s.substring(1);
        }
        if (s.length() > 21 && s.charAt(1) == ' ') {
            k = s.substring(2);
        }
        if (s.length() > 3 && s.charAt(2) == ' ') {
            k = s.substring(3);
        }
        if (s.length() > 4 && s.charAt(3) == ' ') {
            k = s.substring(4);
        }
        j = k;
        if (k.length() > 1 && k.charAt(1) == ' ') {
            j = k.substring(0, 1);
        }
        if (k.length() > 2 && k.charAt(2) == ' ') {
            j = k.substring(0, 2);
        }
        if (k.length() > 3 && k.charAt(3) == ' ') {
            j = k.substring(0, 3);
        }
        if (k.length() > 4 && k.charAt(4) == ' ') {
            j = k.substring(0, 4);
        }
        if (k.length() > 5 && k.charAt(5) == ' ') {
            j = k.substring(0, 5);
        }

        return j;

    }
}
