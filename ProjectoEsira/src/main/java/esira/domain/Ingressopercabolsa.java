/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package esira.domain;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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
 * @author Ussimane
 */
@Entity
@Table(name = "ingressopercabolsa")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Ingressopercabolsa.findAll", query = "SELECT i FROM Ingressopercabolsa i")})
public class Ingressopercabolsa implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected IngressopercabolsaPK ingressopercabolsaPK;
    @Column(name = "data_perca")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataPerca;
    @Column(name = "motivo", length = 255)
    private String motivo;
    @JoinColumn(name = "id_estudante", referencedColumnName = "id_estudante", nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Estudante estudante;

    public Ingressopercabolsa() {
    }

    public Ingressopercabolsa(IngressopercabolsaPK ingressopercabolsaPK) {
        this.ingressopercabolsaPK = ingressopercabolsaPK;
    }

    public Ingressopercabolsa(long idEstudante, Date dataAprovacao) {
        this.ingressopercabolsaPK = new IngressopercabolsaPK(idEstudante, dataAprovacao);
    }

    public IngressopercabolsaPK getIngressopercabolsaPK() {
        return ingressopercabolsaPK;
    }

    public void setIngressopercabolsaPK(IngressopercabolsaPK ingressopercabolsaPK) {
        this.ingressopercabolsaPK = ingressopercabolsaPK;
    }

    public Date getDataPerca() {
        return dataPerca;
    }

    public void setDataPerca(Date dataPerca) {
        this.dataPerca = dataPerca;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public Estudante getEstudante() {
        return estudante;
    }

    public void setEstudante(Estudante estudante) {
        this.estudante = estudante;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ingressopercabolsaPK != null ? ingressopercabolsaPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Ingressopercabolsa)) {
            return false;
        }
        Ingressopercabolsa other = (Ingressopercabolsa) object;
        if ((this.ingressopercabolsaPK == null && other.ingressopercabolsaPK != null) || (this.ingressopercabolsaPK != null && !this.ingressopercabolsaPK.equals(other.ingressopercabolsaPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "esira.domain.Ingressopercabolsa[ ingressopercabolsaPK=" + ingressopercabolsaPK + " ]";
    }
    
}
