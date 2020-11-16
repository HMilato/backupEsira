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
public class ValidacaopendentePK implements Serializable {
    @Basic(optional = false)
    @Column(name = "idfaculdade", nullable = false)
    private int idfaculdade;
    @Basic(optional = false)
    @Column(name = "tipo", nullable = false, length = 32)
    private String tipo;

    public ValidacaopendentePK() {
    }

    public ValidacaopendentePK(int idfaculdade, String tipo) {
        this.idfaculdade = idfaculdade;
        this.tipo = tipo;
    }

    public int getIdfaculdade() {
        return idfaculdade;
    }

    public void setIdfaculdade(int idfaculdade) {
        this.idfaculdade = idfaculdade;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) idfaculdade;
        hash += (tipo != null ? tipo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ValidacaopendentePK)) {
            return false;
        }
        ValidacaopendentePK other = (ValidacaopendentePK) object;
        if (this.idfaculdade != other.idfaculdade) {
            return false;
        }
        if ((this.tipo == null && other.tipo != null) || (this.tipo != null && !this.tipo.equals(other.tipo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "esira.domain.ValidacaopendentePK[ idfaculdade=" + idfaculdade + ", tipo=" + tipo + " ]";
    }
    
}
