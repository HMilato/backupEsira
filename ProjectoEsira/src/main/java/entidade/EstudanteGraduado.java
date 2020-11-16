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
import java.io.Serializable;
import javax.persistence.Id;
import java.util.Date;
import javax.persistence.Entity;

/**
 *
 * @author Milato
 */
@Entity
public class EstudanteGraduado implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id

    private String nrEstudante;
    private String nomecompleto;
    private String datanasc;  
    private int anoingresso;
    private String regime;
    private String paisnasc;
    private String documentotipo;
    private String documentonumero;
    private String provinciacandidatura;
    private String sexo;
    private String provincianasc;
    private int distritocampus;
    private int nivel;
    private String curso;
    private boolean bolseiro;
    private int instituicaonuit;
    private String bolsanome;
    private int nuit;
    private String delegacao;
    private String delegacaonome;
    private String endereco;
    private boolean graduado;

//    public Long getId() {
//        return id;
//    }
//
//
//    public void setId(Long id) {
//        this.id = id;
//    }

    public String getNrEstudante() {
        return nrEstudante;
    }

    public void setNrEstudante(String nrEstudante) {
        this.nrEstudante = nrEstudante;
    }

    public String getNomecompleto() {
        return nomecompleto;
    }

    public void setNomecompleto(String nomecompleto) {
        this.nomecompleto = nomecompleto;
    }

    public String getDatanasc() {
        return datanasc;
    }

    public void setDatanasc(String datanasc) {
        this.datanasc = datanasc;
    }

    public int getAnoingresso() {
        return anoingresso;
    }

    public void setAnoingresso(int anoingresso) {
        this.anoingresso = anoingresso;
    }

    public String getRegime() {
        return regime;
    }

    public void setRegime(String regime) {
        this.regime = regime;
    }

    public String getPaisnasc() {
        return paisnasc;
    }

    public void setPaisnasc(String paisnasc) {
        this.paisnasc = paisnasc;
    }

    public String getDocumentotipo() {
        return documentotipo;
    }

    public void setDocumentotipo(String documentotipo) {
        this.documentotipo = documentotipo;
    }

    public String getDocumentonumero() {
        return documentonumero;
    }

    public void setDocumentonumero(String documentonumero) {
        this.documentonumero = documentonumero;
    }

    public String getProvinciacandidatura() {
        return provinciacandidatura;
    }

    public void setProvinciacandidatura(String provinciacandidatura) {
        this.provinciacandidatura = provinciacandidatura;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getProvincianasc() {
        return provincianasc;
    }

    public void setProvincianasc(String provincianasc) {
        this.provincianasc = provincianasc;
    }

    public int getDistritocampus() {
        return distritocampus;
    }

    public void setDistritocampus(int distritocampus) {
        this.distritocampus = distritocampus;
    }

    public int getNivel() {
        return nivel;
    }

    public void setNivel(int nivel) {
        this.nivel = nivel;
    }

    public String getCurso() {
        return curso;
    }

    public void setCurso(String curso) {
        this.curso = curso;
    }

    public boolean isBolseiro() {
        return bolseiro;
    }

    public void setBolseiro(boolean bolseiro) {
        this.bolseiro = bolseiro;
    }

    public int getInstituicaonuit() {
        return instituicaonuit;
    }

    public void setInstituicaonuit(int instituicaonuit) {
        this.instituicaonuit = instituicaonuit;
    }

    public String getBolsanome() {
        return bolsanome;
    }

    public void setBolsanome(String bolsanome) {
        this.bolsanome = bolsanome;
    }

    public int getNuit() {
        return nuit;
    }

    public void setNuit(int nuit) {
        this.nuit = nuit;
    }

    public String getDelegacao() {
        return delegacao;
    }

    public void setDelegacao(String delegacao) {
        this.delegacao = delegacao;
    }

    public String getDelegacaonome() {
        return delegacaonome;
    }

    public void setDelegacaonome(String delegacaonome) {
        this.delegacaonome = delegacaonome;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }


    public boolean isGraduado() {
        return graduado;
    }

    public void setGraduado(boolean graduado) {
        this.graduado = graduado;
    }

}

