/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package esira.domain;

import java.io.Serializable;
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
 * @author Han
 */
@Entity
@Table(name = "planodisciplina")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Planodisciplina.findAll", query = "SELECT p FROM Planodisciplina p")})
public class Planodisciplina implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected PlanodisciplinaPK planodisciplinaPK;
    @JoinColumn(name = "iddisc", referencedColumnName = "id_disc", nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Disciplina disciplina;

    public Planodisciplina() {
    }

    public Planodisciplina(PlanodisciplinaPK planodisciplinaPK) {
        this.planodisciplinaPK = planodisciplinaPK;
    }

    public Planodisciplina(long iddisc, int nivel, int semestre, int ano) {
        this.planodisciplinaPK = new PlanodisciplinaPK(iddisc, nivel, semestre, ano);
    }

    public PlanodisciplinaPK getPlanodisciplinaPK() {
        return planodisciplinaPK;
    }

    public void setPlanodisciplinaPK(PlanodisciplinaPK planodisciplinaPK) {
        this.planodisciplinaPK = planodisciplinaPK;
    }

    public Disciplina getDisciplina() {
        return disciplina;
    }

    public void setDisciplina(Disciplina disciplina) {
        this.disciplina = disciplina;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (planodisciplinaPK != null ? planodisciplinaPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Planodisciplina)) {
            return false;
        }
        Planodisciplina other = (Planodisciplina) object;
        if ((this.planodisciplinaPK == null && other.planodisciplinaPK != null) || (this.planodisciplinaPK != null && !this.planodisciplinaPK.equals(other.planodisciplinaPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "esira.domain.Planodisciplina[ planodisciplinaPK=" + planodisciplinaPK + " ]";
    }
    
}
