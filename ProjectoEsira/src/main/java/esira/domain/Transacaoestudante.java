/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package esira.domain;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Milato
 */
@Entity
@Table(catalog = "fecn1", schema = "fecn1")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Transacaoestudante.findAll", query = "SELECT t FROM Transacaoestudante t"),
    @NamedQuery(name = "Transacaoestudante.findByData", query = "SELECT t FROM Transacaoestudante t WHERE t.data = :data"),
    @NamedQuery(name = "Transacaoestudante.findByValor", query = "SELECT t FROM Transacaoestudante t WHERE t.valor = :valor"),
    @NamedQuery(name = "Transacaoestudante.findByIdEstudante", query = "SELECT t FROM Transacaoestudante t WHERE t.idEstudante = :idEstudante"),
    @NamedQuery(name = "Transacaoestudante.findByTipoTaxa", query = "SELECT t FROM Transacaoestudante t WHERE t.tipoTaxa = :tipoTaxa"),
    @NamedQuery(name = "Transacaoestudante.findByIdTaxa", query = "SELECT t FROM Transacaoestudante t WHERE t.idTaxa = :idTaxa")})
public class Transacaoestudante implements Serializable {
    private static final long serialVersionUID = 1L;
    @Temporal(TemporalType.TIMESTAMP)
    private Date data;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(precision = 8, scale = 8)
    private Float valor;
    @Column(name = "id_estudante")
    private Long idEstudante;
//    @Column(name = "tipo_taxa")
//    private Integer tipoTaxa;
    
    @JoinColumn(name = "tipo_taxa", referencedColumnName = "id_taxa")
    @ManyToOne(fetch = FetchType.LAZY)
    private Taxa tipoTaxa;
    @Column(name = "primeiro_pagamento")
    private Boolean primeiroPagamento;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_taxa", nullable = false)
    private Integer idTaxa;

    public Transacaoestudante() {
    }

    public Transacaoestudante(Integer idTaxa) {
        this.idTaxa = idTaxa;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public Float getValor() {
        return valor;
    }

    public void setValor(Float valor) {
        this.valor = valor;
    }

    public Long getIdEstudante() {
        return idEstudante;
    }

    public void setIdEstudante(Long idEstudante) {
        this.idEstudante = idEstudante;
    }

    public Taxa getTipoTaxa() {
        return tipoTaxa;
    }

    public void setTipoTaxa(Taxa tipoTaxa) {
        this.tipoTaxa = tipoTaxa;
    }
    
//     public Integer getTipoTaxa() {
//        return tipoTaxa;
//    }
//
//    public void setTipoTaxa(Integer tipoTaxa) {
//        this.tipoTaxa = tipoTaxa;
//    }

    public Integer getIdTaxa() {
        return idTaxa;
    }

    public void setIdTaxa(Integer idTaxa) {
        this.idTaxa = idTaxa;
    }

    public Boolean getPrimeiroPagamento() {
        return primeiroPagamento;
    }

    public void setPrimeiroPagamento(Boolean primeiroPagamento) {
        this.primeiroPagamento = primeiroPagamento;
    }
    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idTaxa != null ? idTaxa.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Transacaoestudante)) {
            return false;
        }
        Transacaoestudante other = (Transacaoestudante) object;
        if ((this.idTaxa == null && other.idTaxa != null) || (this.idTaxa != null && !this.idTaxa.equals(other.idTaxa))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "esira.domain.Transacaoestudante[ idTaxa=" + idTaxa + " ]";
    }
    
}
