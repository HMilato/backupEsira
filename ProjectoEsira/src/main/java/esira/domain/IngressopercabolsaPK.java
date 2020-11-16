/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package esira.domain;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Ussimane
 */
@Embeddable
public class IngressopercabolsaPK implements Serializable {
    @Basic(optional = false)
    @Column(name = "id_estudante", nullable = false)
    private long idEstudante;
    @Basic(optional = false)
    @Column(name = "data_aprovacao", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataAprovacao;

    public IngressopercabolsaPK() {
    }

    public IngressopercabolsaPK(long idEstudante, Date dataAprovacao) {
        this.idEstudante = idEstudante;
        this.dataAprovacao = dataAprovacao;
    }

    public long getIdEstudante() {
        return idEstudante;
    }

    public void setIdEstudante(long idEstudante) {
        this.idEstudante = idEstudante;
    }

    public Date getDataAprovacao() {
        return dataAprovacao;
    }

    public void setDataAprovacao(Date dataAprovacao) {
        this.dataAprovacao = dataAprovacao;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) idEstudante;
        hash += (dataAprovacao != null ? dataAprovacao.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof IngressopercabolsaPK)) {
            return false;
        }
        IngressopercabolsaPK other = (IngressopercabolsaPK) object;
        if (this.idEstudante != other.idEstudante) {
            return false;
        }
        if ((this.dataAprovacao == null && other.dataAprovacao != null) || (this.dataAprovacao != null && !this.dataAprovacao.equals(other.dataAprovacao))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "esira.domain.IngressopercabolsaPK[ idEstudante=" + idEstudante + ", dataAprovacao=" + dataAprovacao + " ]";
    }
    
}
