/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidade;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author SIGP
 */
@Entity
@Table(name = "NotaPauta")
public class NotaPauta implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Long idinsc;
    private String nomeCompleto;
    private String nomeMecanografico;
    private String nota;
    private String observacoes;
    private String classfreq;
    private String classreq;
    private String classexame;
    private String classextra;
    private Float nota1;
    private Float nota2;
    private List<String> notas;
    private boolean editavel;
    private int ordem;
    private Float nota3;

    public NotaPauta(Long id, Long idinsc, String nomeCompleto, String nomeMecanografico, String nota) {
        this.id = id;
        this.idinsc = idinsc;
        this.nomeCompleto = nomeCompleto;
        this.nomeMecanografico = nomeMecanografico;
        this.nota = nota;
    }

    public NotaPauta(Long id, String nomeCompleto, String nomeMecanografico, String nota) {
        this.id = id;
        this.nomeCompleto = nomeCompleto;
        this.nomeMecanografico = nomeMecanografico;
        this.nota = nota;
    }

    public Long getIddinsc() {
        return idinsc;
    }

    public String getClassfreq() {
        return classfreq;
    }

    public void setClassfreq(String classfreq) {
        this.classfreq = classfreq;
    }

    public String getClassreq() {
        return classreq;
    }

    public void setClassreq(String classreq) {
        this.classreq = classreq;
    }

    public String getClassexame() {
        return classexame;
    }

    public void setClassexame(String classexame) {
        this.classexame = classexame;
    }

    public String getClassextra() {
        return classextra;
    }

    public void setClassextra(String classextra) {
        this.classextra = classextra;
    }

    public void setIddinsc(Long idinsc) {
        this.idinsc = idinsc;
    }

    public int getOrdem() {
        return ordem;
    }

    public void setOrdem(int ordem) {
        this.ordem = ordem;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdinsc() {
        return idinsc;
    }

    public void setIdinsc(Long idinsc) {
        this.idinsc = idinsc;
    }

    public boolean isEditavel() {
        return editavel;
    }

    public Float getNota3() {
        return nota3;
    }

    public void setNota3(Float nota3) {
        this.nota3 = nota3;
    }
    
    

    public void setEditavel(boolean editavel) {
        this.editavel = editavel;
    }

    public Float getNota1() {
        return nota1;
    }

    public void setNota1(Float nota1) {
        this.nota1 = nota1;
    }

    public Float getNota2() {
        return nota2;
    }

    public void setNota2(Float nota2) {
        this.nota2 = nota2;
    }

    public List<String> getNotas() {
        return notas;
    }

    public void setNotas(List<String> notas) {
        this.notas = notas;
    }

    public String getNomeCompleto() {
        return nomeCompleto;
    }

    public String getNomeMecanografico() {
        return nomeMecanografico;
    }

    public String getNota() {
        return nota;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setNomeCompleto(String nomeCompleto) {
        this.nomeCompleto = nomeCompleto;
    }

    public void setNomeMecanografico(String nomeMecanografico) {
        this.nomeMecanografico = nomeMecanografico;
    }

    public void setNota(String nota) {
        this.nota = nota;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

}
