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
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Ussimane
 */
@Entity
@Table(name = "equivalencia", catalog = "fecn1", schema = "fecn1")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Equivalencia.findAll", query = "SELECT e FROM Equivalencia e"),
    @NamedQuery(name = "Equivalencia.findByIdDisc", query = "SELECT e FROM Equivalencia e WHERE e.equivalenciaPK.idDisc = :idDisc"),
    @NamedQuery(name = "Equivalencia.findByEquivalencia", query = "SELECT e FROM Equivalencia e WHERE e.equivalenciaPK.equivalencia = :equivalencia"),
    @NamedQuery(name = "Equivalencia.findByDescr", query = "SELECT e FROM Equivalencia e WHERE e.descr = :descr")})
public class Equivalencia implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected EquivalenciaPK equivalenciaPK;
    @Column(name = "descr", length = 32)
    private String descr;
    @JoinColumn(name = "equivalencia", referencedColumnName = "id_disc", nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Disciplina disciplina;
    @JoinColumn(name = "id_disc", referencedColumnName = "id_disc", nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Disciplina disciplina1;

    public Equivalencia() {
    }

    public Equivalencia(EquivalenciaPK equivalenciaPK) {
        this.equivalenciaPK = equivalenciaPK;
    }

    public Equivalencia(long idDisc, long equivalencia) {
        this.equivalenciaPK = new EquivalenciaPK(idDisc, equivalencia);
    }

    public EquivalenciaPK getEquivalenciaPK() {
        return equivalenciaPK;
    }

    public void setEquivalenciaPK(EquivalenciaPK equivalenciaPK) {
        this.equivalenciaPK = equivalenciaPK;
    }

    public String getDescr() {
        return descr;
    }

    public void setDescr(String descr) {
        this.descr = descr;
    }

    public Disciplina getDisciplina() {
        return disciplina;
    }

    public void setDisciplina(Disciplina disciplina) {
        this.disciplina = disciplina;
    }

    public Disciplina getDisciplina1() {
        return disciplina1;
    }

    public void setDisciplina1(Disciplina disciplina1) {
        this.disciplina1 = disciplina1;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (equivalenciaPK != null ? equivalenciaPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Equivalencia)) {
            return false;
        }
        Equivalencia other = (Equivalencia) object;
        if ((this.equivalenciaPK == null && other.equivalenciaPK != null) || (this.equivalenciaPK != null && !this.equivalenciaPK.equals(other.equivalenciaPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "domain.Equivalencia[ equivalenciaPK=" + equivalenciaPK + " ]";
    }
    
}
