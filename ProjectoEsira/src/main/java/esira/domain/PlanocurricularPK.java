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
 * @author Ussimane
 */
@Embeddable
public class PlanocurricularPK implements Serializable {
    @Basic(optional = false)
    @Column(name = "ano", nullable = false)
    private int ano;
    @Basic(optional = false)
    @Column(name = "idcurso", nullable = false)
    private long idcurso;

    public PlanocurricularPK() {
    }

    public PlanocurricularPK(int ano,long idcurso) {
        this.ano = ano;
        this.idcurso = idcurso;
    }

    public int getAno() {
        return ano;
    }

    public void setAno(int ano) {
        this.ano = ano;
    }

    public long getIdcurso() {
        return idcurso;
    }

    public void setIdcurso(long idcurso) {
        this.idcurso = idcurso;
    }

    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) ano;
        hash += (int) idcurso;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PlanocurricularPK)) {
            return false;
        }
        PlanocurricularPK other = (PlanocurricularPK) object;
        if (this.ano != other.ano) {
            return false;
        }
        if (this.idcurso != other.idcurso) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "esira.domain.PlanocurricularPK[ ano=" + ano + ", idcurso=" + idcurso + " ]";
    }
    
}
