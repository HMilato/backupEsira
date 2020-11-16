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
 * @author Han
 */
@Embeddable
public class PautaPK implements Serializable {

    @Basic(optional = false)
    @Column(name = "iddisc", nullable = false)
    private long iddisc;
    @Basic(optional = false)
    @Column(name = "ano", nullable = false)
    private int ano;
    @Basic(optional = false)
    @Column(name = "semestre", nullable = false)
    private short semestre;
    @Basic(optional = false)
    @Column(name = "datap", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date datap;

    public PautaPK() {
    }

    public PautaPK(long iddisc, int ano, short semestre, Date datap) {
        this.iddisc = iddisc;
        this.ano = ano;
        this.semestre = semestre;
        this.datap = datap;
    }

    public long getIddisc() {
        return iddisc;
    }

    public void setIddisc(long iddisc) {
        this.iddisc = iddisc;
    }

    public int getAno() {
        return ano;
    }

    public void setAno(int ano) {
        this.ano = ano;
    }

    public short getSemestre() {
        return semestre;
    }

    public void setSemestre(short semestre) {
        this.semestre = semestre;
    }

    public Date getDatap() {
        return datap;
    }

    public void setDatap(Date datap) {
        this.datap = datap;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) iddisc;
        hash += (int) ano;
        hash += (int) semestre;
        hash += (datap != null ? datap.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PautaPK)) {
            return false;
        }
        PautaPK other = (PautaPK) object;
        if (this.iddisc != other.iddisc) {
            return false;
        }
        if (this.ano != other.ano) {
            return false;
        }
        if (this.semestre != other.semestre) {
            return false;
        }
        if ((this.datap == null && other.datap != null) || (this.datap != null && !this.datap.equals(other.datap))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "esira.domain.PautaPK[ iddisc=" + iddisc + ", ano=" + ano + ", semestre=" + semestre + ", datap=" + datap + " ]";
    }
    
}
