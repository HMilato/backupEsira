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
public class LeccionaPK implements Serializable {
    @Basic(optional = false)
    @Column(name = "iddocente", nullable = false)
    private long iddocente;
    @Basic(optional = false)
    @Column(name = "iddisc", nullable = false)
    private long iddisc;
    @Basic(optional = false)
    @Column(name = "turno", nullable = false)
    private int turno;
    @Basic(optional = false)
    @Column(name = "turma", nullable = false)
    private int turma;

    public LeccionaPK() {
    }

    public LeccionaPK(long iddocente, long iddisc, int turno, int turma) {
        this.iddocente = iddocente;
        this.iddisc = iddisc;
        this.turno = turno;
        this.turma = turma;
    }

    public long getIddocente() {
        return iddocente;
    }

    public void setIddocente(long iddocente) {
        this.iddocente = iddocente;
    }

    public long getIddisc() {
        return iddisc;
    }

    public void setIddisc(long iddisc) {
        this.iddisc = iddisc;
    }

    public int getTurno() {
        return turno;
    }

    public void setTurno(int turno) {
        this.turno = turno;
    }

    public int getTurma() {
        return turma;
    }

    public void setTurma(int turma) {
        this.turma = turma;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) iddocente;
        hash += (int) iddisc;
        hash += (int) turno;
        hash += (int) turma;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof LeccionaPK)) {
            return false;
        }
        LeccionaPK other = (LeccionaPK) object;
        if (this.iddocente != other.iddocente) {
            return false;
        }
        if (this.iddisc != other.iddisc) {
            return false;
        }
        if (this.turno != other.turno) {
            return false;
        }
        if (this.turma != other.turma) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "esira.domain.LeccionaPK[ iddocente=" + iddocente + ", iddisc=" + iddisc + ", turno=" + turno + ", turma=" + turma + " ]";
    }
    
}
