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
public class PrecedenciaPK implements Serializable {
    @Basic(optional = false)
    @Column(name = "id_disc", nullable = false)
    private long idDisc;
    @Basic(optional = false)
    @Column(name = "precedencia", nullable = false)
    private long precedencia;

    public PrecedenciaPK() {
    }

    public PrecedenciaPK(long idDisc, long precedencia) {
        this.idDisc = idDisc;
        this.precedencia = precedencia;
    }

    public long getIdDisc() {
        return idDisc;
    }

    public void setIdDisc(long idDisc) {
        this.idDisc = idDisc;
    }

    public long getPrecedencia() {
        return precedencia;
    }

    public void setPrecedencia(long precedencia) {
        this.precedencia = precedencia;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) idDisc;
        hash += (int) precedencia;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PrecedenciaPK)) {
            return false;
        }
        PrecedenciaPK other = (PrecedenciaPK) object;
        if (this.idDisc != other.idDisc) {
            return false;
        }
        if (this.precedencia != other.precedencia) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "esira.domain.PrecedenciaPK[ idDisc=" + idDisc + ", precedencia=" + precedencia + " ]";
    }
    
}
