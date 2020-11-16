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
public class EquivalenciaPK implements Serializable {
    @Basic(optional = false)
    @Column(name = "id_disc", nullable = false)
    private long idDisc;
    @Basic(optional = false)
    @Column(name = "equivalencia", nullable = false)
    private long equivalencia;

    public EquivalenciaPK() {
    }

    public EquivalenciaPK(long idDisc, long equivalencia) {
        this.idDisc = idDisc;
        this.equivalencia = equivalencia;
    }

    public long getIdDisc() {
        return idDisc;
    }

    public void setIdDisc(long idDisc) {
        this.idDisc = idDisc;
    }

    public long getEquivalencia() {
        return equivalencia;
    }

    public void setEquivalencia(long equivalencia) {
        this.equivalencia = equivalencia;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) idDisc;
        hash += (int) equivalencia;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EquivalenciaPK)) {
            return false;
        }
        EquivalenciaPK other = (EquivalenciaPK) object;
        if (this.idDisc != other.idDisc) {
            return false;
        }
        if (this.equivalencia != other.equivalencia) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "domain.EquivalenciaPK[ idDisc=" + idDisc + ", equivalencia=" + equivalencia + " ]";
    }
    
}
