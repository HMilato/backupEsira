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
import java.util.Date;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Milato
 */
@Entity
public class Funcionariows implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id

    private String nomeCompleto;
    private int nuit;
    private String sexo;
    private String docidentnum;
    private String contrato;
     private String habilitacao;
    private String habilitacaoescola;
    private String areaformacao;
    private String paisorigem;
    private String datanasc;


    public String getNomeCompleto() {
        return nomeCompleto;
    }

    public void setNomeCompleto(String nomeCompleto) {
        this.nomeCompleto = nomeCompleto;
    }

    public int getNuit() {
        return nuit;
    }

    public void setNuit(int nuit) {
        this.nuit = nuit;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

 

    public String getDocidentnum() {
        return docidentnum;
    }

    public void setDocidentnum(String docidentnum) {
        this.docidentnum = docidentnum;
    }

    public String getContrato() {
        return contrato;
    }

    public void setContrato(String contrato) {
        this.contrato = contrato;
    }

    public String getHabilitacao() {
        return habilitacao;
    }

    public void setHabilitacao(String habilitacao) {
        this.habilitacao = habilitacao;
    }

    public String getHabilitacaoescola() {
        return habilitacaoescola;
    }

    public void setHabilitacaoescola(String habilitacaoescola) {
        this.habilitacaoescola = habilitacaoescola;
    }

    public String getAreaformacao() {
        return areaformacao;
    }

    public void setAreaformacao(String areaformacao) {
        this.areaformacao = areaformacao;
    }

    public String getPaisorigem() {
        return paisorigem;
    }

    public void setPaisorigem(String paisorigem) {
        this.paisorigem = paisorigem;
    }

    public String getDatanasc() {
        return datanasc;
    }

    public void setDatanasc(String datanasc) {
        this.datanasc = datanasc;
    }

   


}
