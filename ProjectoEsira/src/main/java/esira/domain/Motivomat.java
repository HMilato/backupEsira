/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package esira.domain;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Ussimane
 */
@Entity
@Table(name = "motivomat")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Motivomat.findAll", query = "SELECT m FROM Motivomat m")})
public class Motivomat implements Serializable {
    private static final long serialVersionUID = 1L;
   @EmbeddedId
    protected MatriculaPK matriculaPK;
    @Column(name = "motivo", length = 2147483647)
    private String motivo;
    @JoinColumns({
        @JoinColumn(name = "id_estudante", referencedColumnName = "id_estudante", nullable = false, insertable = false, updatable = false),
        @JoinColumn(name = "ano", referencedColumnName = "ano", nullable = false, insertable = false, updatable = false)})
    @OneToOne(optional = false, fetch = FetchType.LAZY)
    private Matricula matricula;

    public Motivomat() {
    }

    public Motivomat(MatriculaPK matriculaPK) {
        this.matriculaPK = matriculaPK;
    }

    public Motivomat(long idEstudante, int ano) {
        this.matriculaPK = new MatriculaPK(idEstudante, ano);
    }

    public MatriculaPK getMatriculaPK() {
        return matriculaPK;
    }

    public void setMatriculaPK(MatriculaPK matriculaPK) {
        this.matriculaPK = matriculaPK;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public Matricula getMatricula() {
        return matricula;
    }

    public void setMatricula(Matricula matricula) {
        this.matricula = matricula;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (matriculaPK != null ? matriculaPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Motivomat)) {
            return false;
        }
        Motivomat other = (Motivomat) object;
        if ((this.matriculaPK == null && other.matriculaPK != null) || (this.matriculaPK != null && !this.matriculaPK.equals(other.matriculaPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "esira.domain.Motivomat[ matriculaPK=" + matriculaPK + " ]";
    }
    
}
