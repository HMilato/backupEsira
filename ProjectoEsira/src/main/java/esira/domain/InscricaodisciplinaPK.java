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
public class InscricaodisciplinaPK implements Serializable {
    @Basic(optional = false)
    @Column(name = "id_inscricao", nullable = false)
    private long idInscricao;
    @Basic(optional = false)
    @Column(name = "id_disciplina", nullable = false)
    private long idDisciplina;

    public InscricaodisciplinaPK() {
    }

    public InscricaodisciplinaPK(long idInscricao, long idDisciplina) {
        this.idInscricao = idInscricao;
        this.idDisciplina = idDisciplina;
    }

    public long getIdInscricao() {
        return idInscricao;
    }

    public void setIdInscricao(long idInscricao) {
        this.idInscricao = idInscricao;
    }

    public long getIdDisciplina() {
        return idDisciplina;
    }

    public void setIdDisciplina(long idDisciplina) {
        this.idDisciplina = idDisciplina;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) idInscricao;
        hash += (int) idDisciplina;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof InscricaodisciplinaPK)) {
            return false;
        }
        InscricaodisciplinaPK other = (InscricaodisciplinaPK) object;
        if (this.idInscricao != other.idInscricao) {
            return false;
        }
        if (this.idDisciplina != other.idDisciplina) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "modelo.InscricaodisciplinaPK[ idInscricao=" + idInscricao + ", idDisciplina=" + idDisciplina + " ]";
    }
    
}
