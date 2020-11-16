/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package esira.domain;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Ussimane
 */
@Entity
@Table(name = "prescricao")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Prescricao.findAll", query = "SELECT p FROM Prescricao p"),
    @NamedQuery(name = "Prescricao.findByIdDisciplina", query = "SELECT p FROM Prescricao p WHERE p.inscricaodisciplinaPK.idDisciplina = :idDisciplina"),
    @NamedQuery(name = "Prescricao.findByIdInscricao", query = "SELECT p FROM Prescricao p WHERE p.inscricaodisciplinaPK.idInscricao = :idInscricao"),
    @NamedQuery(name = "Prescricao.findByEstado", query = "SELECT p FROM Prescricao p WHERE p.estado = :estado")})
public class Prescricao implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected InscricaodisciplinaPK inscricaodisciplinaPK;
    @Column(name = "estado")
    private Boolean estado;
    @JoinColumns({
        @JoinColumn(name = "id_inscricao", referencedColumnName = "id_inscricao", nullable = false, insertable = false, updatable = false),
        @JoinColumn(name = "id_disciplina", referencedColumnName = "id_disciplina", nullable = false, insertable = false, updatable = false)})
    @OneToOne(optional = false, fetch = FetchType.LAZY)
    private Inscricaodisciplina inscricaodisciplina;
    @JoinColumn(name = "operador", referencedColumnName = "id_funcionario")
    @ManyToOne(fetch = FetchType.LAZY)
    private Funcionario operador;
    @Column(name = "datai")
    @Temporal(TemporalType.TIMESTAMP)
    private Date datai;

    public Prescricao() {
    }

    public InscricaodisciplinaPK getInscricaodisciplinaPK() {
        return inscricaodisciplinaPK;
    }

    public void setInscricaodisciplinaPK(InscricaodisciplinaPK inscricaodisciplinaPK) {
        this.inscricaodisciplinaPK = inscricaodisciplinaPK;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public Inscricaodisciplina getInscricaodisciplina() {
        return inscricaodisciplina;
    }

    public void setInscricaodisciplina(Inscricaodisciplina inscricaodisciplina) {
        this.inscricaodisciplina = inscricaodisciplina;
    }

    public Funcionario getOperador() {
        return operador;
    }

    public void setOperador(Funcionario operador) {
        this.operador = operador;
    }

    public Date getDatai() {
        return datai;
    }

    public void setDatai(Date datai) {
        this.datai = datai;
    }
    
    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (inscricaodisciplinaPK != null ? inscricaodisciplinaPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Prescricao)) {
            return false;
        }
        Prescricao other = (Prescricao) object;
        if ((this.inscricaodisciplinaPK == null && other.inscricaodisciplinaPK != null) || (this.inscricaodisciplinaPK != null && !this.inscricaodisciplinaPK.equals(other.inscricaodisciplinaPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "modelo.Prescricao[ inscricaodisciplinaPK=" + inscricaodisciplinaPK + " ]";
    }
    
}
