/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidade;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 *
 * @author Milato
 */
@Entity
public class Planoscurriculares implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
  
    private String plano;
    private String planonome;
    private boolean ativo;
    private String curso;
    private String cursonome;
    private String cursograu;
    private String cursoareaformacao;   
    private String areacientificanome;
    private String disciplina;
    private String disciplinanome;   
    private int nivel;
    private int semestre;
    private int credito;

    public String getPlano() {
        return plano;
    }

    public void setPlano(String plano) {
        this.plano = plano;
    }

    public String getPlanonome() {
        return planonome;
    }

    public void setPlanonome(String planonome) {
        this.planonome = planonome;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public String getCurso() {
        return curso;
    }

    public void setCurso(String curso) {
        this.curso = curso;
    }

    public String getCursonome() {
        return cursonome;
    }

    public void setCursonome(String cursonome) {
        this.cursonome = cursonome;
    }

    public String getCursograu() {
        return cursograu;
    }

    public void setCursograu(String cursograu) {
        this.cursograu = cursograu;
    }

    public String getCursoareaformacao() {
        return cursoareaformacao;
    }

    public void setCursoareaformacao(String cursoareaformacao) {
        this.cursoareaformacao = cursoareaformacao;
    }

    public String getAreacientificanome() {
        return areacientificanome;
    }

    public void setAreacientificanome(String areacientificanome) {
        this.areacientificanome = areacientificanome;
    }

    public String getDisciplina() {
        return disciplina;
    }

    public void setDisciplina(String disciplina) {
        this.disciplina = disciplina;
    }

    public String getDisciplinanome() {
        return disciplinanome;
    }

    public void setDisciplinanome(String disciplinanome) {
        this.disciplinanome = disciplinanome;
    }

    public int getNivel() {
        return nivel;
    }

    public void setNivel(int nivel) {
        this.nivel = nivel;
    }

    public int getSemestre() {
        return semestre;
    }

    public void setSemestre(int semestre) {
        this.semestre = semestre;
    }

    public int getCredito() {
        return credito;
    }

    public void setCredito(int credito) {
        this.credito = credito;
    }
    
  
    
}
