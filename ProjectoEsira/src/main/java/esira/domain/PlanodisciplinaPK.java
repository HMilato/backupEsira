/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package esira.domain;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *
 * @author Han
 */
@Embeddable
public class PlanodisciplinaPK implements Serializable {

    @Basic(optional = false)
    @Column(name = "iddisc", nullable = false)
    private long iddisc;
    @Basic(optional = false)
    @Column(name = "nivel", nullable = false)
    private int nivel;
    @Basic(optional = false)
    @Column(name = "semestre", nullable = false)
    private int semestre;
    @Basic(optional = false)
    @Column(name = "ano", nullable = false)
    private int ano;

    public PlanodisciplinaPK() {
    }

    public PlanodisciplinaPK(long iddisc, int nivel, int semestre, int ano) {
        this.iddisc = iddisc;
        this.nivel = nivel;
        this.semestre = semestre;
        this.ano = ano;
    }

    public long getIddisc() {
        return iddisc;
    }

    public void setIddisc(long iddisc) {
        this.iddisc = iddisc;
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

    public int getAno() {
        return ano;
    }

    public void setAno(int ano) {
        this.ano = ano;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) iddisc;
        hash += (int) nivel;
        hash += (int) semestre;
        hash += (int) ano;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PlanodisciplinaPK)) {
            return false;
        }
        PlanodisciplinaPK other = (PlanodisciplinaPK) object;
        if (this.iddisc != other.iddisc) {
            return false;
        }
        if (this.nivel != other.nivel) {
            return false;
        }
        if (this.semestre != other.semestre) {
            return false;
        }
        if (this.ano != other.ano) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "esira.domain.PlanodisciplinaPK[ iddisc=" + iddisc + ", nivel=" + nivel + ", semestre=" + semestre + ", ano=" + ano + " ]";
    }
    
}
