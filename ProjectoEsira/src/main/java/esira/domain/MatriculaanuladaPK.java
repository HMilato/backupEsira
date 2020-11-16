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
public class MatriculaanuladaPK implements Serializable {
    @Basic(optional = false)
    @Column(name = "id_estudante", nullable = false)
    private long idEstudante;
    @Basic(optional = false)
    @Column(name = "ano", nullable = false)
    private long ano;

    public MatriculaanuladaPK() {
    }

    public MatriculaanuladaPK(long idEstudante, long ano) {
        this.idEstudante = idEstudante;
        this.ano = ano;
    }

    public long getIdEstudante() {
        return idEstudante;
    }

    public void setIdEstudante(long idEstudante) {
        this.idEstudante = idEstudante;
    }

    public long getAno() {
        return ano;
    }

    public void setAno(long ano) {
        this.ano = ano;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) idEstudante;
        hash += (int) ano;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MatriculaanuladaPK)) {
            return false;
        }
        MatriculaanuladaPK other = (MatriculaanuladaPK) object;
        if (this.idEstudante != other.idEstudante) {
            return false;
        }
        if (this.ano != other.ano) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "modelo.MatriculaanuladaPK[ idEstudante=" + idEstudante + ", ano=" + ano + " ]";
    }
    
}
