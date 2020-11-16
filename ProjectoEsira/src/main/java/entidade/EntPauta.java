/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidade;

import esira.domain.Pauta;
import esira.domain.PautaPK;
import java.io.Serializable;
import java.util.Date;
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
@Table(name = "EntPauta")
public class EntPauta implements Serializable {
    
    private static final long serialVersionUID = 1L;
    @Id
   // @GeneratedValue(strategy = GenerationType.AUTO)
    private Date datap;
    private String descricao;
    private String realizacao;
    private String nrealizado;
    private String validacao;
    private String rejeitado;
    private String publicacao;
    private Pauta pauta;

    public EntPauta(Date datap, String descricao, String realizacao, String validacao, String publicacao, Pauta pauta) {
        this.datap = datap;
        this.descricao = descricao;
        this.realizacao = realizacao;
        this.validacao = validacao;
        this.publicacao = publicacao;
        this.pauta = pauta;
    }

    public EntPauta() {
    }

    public String getNrealizado() {
        return nrealizado;
    }

    public void setNrealizado(String nrealizado) {
        this.nrealizado = nrealizado;
    }

    public String getRejeitado() {
        return rejeitado;
    }

    public void setRejeitado(String rejeitado) {
        this.rejeitado = rejeitado;
    }

    public Date getDatap() {
        return datap;
    }

    public void setDatap(Date datap) {
        this.datap = datap;
    }

     

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getRealizacao() {
        return realizacao;
    }

    public void setRealizacao(String realizacao) {
        this.realizacao = realizacao;
    }

    public String getValidacao() {
        return validacao;
    }

    public void setValidacao(String validacao) {
        this.validacao = validacao;
    }

    public String getPublicacao() {
        return publicacao;
    }

    public void setPublicacao(String publicacao) {
        this.publicacao = publicacao;
    }

    public Pauta getPauta() {
        return pauta;
    }

    public void setPauta(Pauta pauta) {
        this.pauta = pauta;
    }

    
 
    
}
