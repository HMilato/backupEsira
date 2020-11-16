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
@Table(name = "lecciona")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Lecciona.findAll", query = "SELECT l FROM Lecciona l")})
public class Lecciona implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected LeccionaPK leccionaPK;
    @Column(name = "sem")
    private Integer sem;
    @Column(name = "ano")
    private Integer ano;
    @JoinColumn(name = "iddisc", referencedColumnName = "id_disc", nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Disciplina disciplina;
    @JoinColumn(name = "iddocente", referencedColumnName = "id_funcionario", nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Funcionario docente;
    @Column(name = "nivel")
    private Integer nivel;

    public Lecciona() {
    }

    public Lecciona(LeccionaPK leccionaPK) {
        this.leccionaPK = leccionaPK;
    }

    public Lecciona(long iddocente, long iddisc, int turno, int turma) {
        this.leccionaPK = new LeccionaPK(iddocente, iddisc, turno, turma);
    }

    public LeccionaPK getLeccionaPK() {
        return leccionaPK;
    }

    public void setLeccionaPK(LeccionaPK leccionaPK) {
        this.leccionaPK = leccionaPK;
    }

    public Integer getSem() {
        return sem;
    }

    public void setSem(Integer sem) {
        this.sem = sem;
    }

    public Integer getAno() {
        return ano;
    }

    public void setAno(Integer ano) {
        this.ano = ano;
    }

    public Integer getNivel() {
        return nivel;
    }

    public void setNivel(Integer nivel) {
        this.nivel = nivel;
    }
    
    

    public Disciplina getDisciplina() {
        return disciplina;
    }

    public void setDisciplina(Disciplina disciplina) {
        this.disciplina = disciplina;
    }

    public Funcionario getDocente() {
        return docente;
    }

    public void setDocente(Funcionario docente) {
        this.docente = docente;
    }

   

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (leccionaPK != null ? leccionaPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Lecciona)) {
            return false;
        }
        Lecciona other = (Lecciona) object;
        if ((this.leccionaPK == null && other.leccionaPK != null) || (this.leccionaPK != null && !this.leccionaPK.equals(other.leccionaPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "esira.domain.Lecciona[ leccionaPK=" + leccionaPK + " ]";
    }
    
}
