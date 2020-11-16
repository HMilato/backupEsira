/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package esira.domain;

import java.io.Serializable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "inscricaodisciplina")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Inscricaodisciplina.findAll", query = "SELECT i FROM Inscricaodisciplina i")})
public class Inscricaodisciplina implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected InscricaodisciplinaPK inscricaodisciplinaPK;
    @Column(name = "estado")
    private Boolean estado;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "nota_final", precision = 8, scale = 8)
    private Float notaFinal;
    @Column(name = "disciplina_activa")
    private Short disciplinaActiva;
    @JoinColumn(name = "id_disciplina", referencedColumnName = "id_disc", nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Disciplina disciplina;
    @JoinColumn(name = "anulacao", referencedColumnName = "idanulacao")
    @ManyToOne(fetch = FetchType.LAZY)
    private Disciplinaanulada anulacao;
    @JoinColumn(name = "id_inscricao", referencedColumnName = "id_inscricao", nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Inscricao inscricao;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "inscricaodisciplina", fetch = FetchType.LAZY)
    private Prescricao prescricao;
    @Column(name = "turma")
    private Integer turma;
    @Column(name = "turno")
    private Integer turno;

    public Inscricaodisciplina() {
    }

    public Inscricaodisciplina(InscricaodisciplinaPK inscricaodisciplinaPK) {
        this.inscricaodisciplinaPK = inscricaodisciplinaPK;
    }

    public Inscricaodisciplina(long idInscricao, long idDisciplina) {
        this.inscricaodisciplinaPK = new InscricaodisciplinaPK(idInscricao, idDisciplina);
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

    public Float getNotaFinal() {
        return notaFinal;
    }

    public Integer getTurma() {
        return turma;
    }

    public void setTurma(Integer turma) {
        this.turma = turma;
    }

    public Integer getTurno() {
        return turno;
    }

    public void setTurno(Integer turno) {
        this.turno = turno;
    }
    
    

    public void setNotaFinal(Float notaFinal) {
        this.notaFinal = notaFinal;
    }

    public Short getDisciplinaActiva() {
        return disciplinaActiva;
    }

    public void setDisciplinaActiva(Short disciplinaActiva) {
        this.disciplinaActiva = disciplinaActiva;
    }

    public Disciplina getDisciplina() {
        return disciplina;
    }

    public void setDisciplina(Disciplina disciplina) {
        this.disciplina = disciplina;
    }

    public Disciplinaanulada getAnulacao() {
        return anulacao;
    }

    public void setAnulacao(Disciplinaanulada anulacao) {
        this.anulacao = anulacao;
    }

    public Inscricao getInscricao() {
        return inscricao;
    }

    public void setInscricao(Inscricao inscricao) {
        this.inscricao = inscricao;
    }

    public Prescricao getPrescricao() {
        return prescricao;
    }

    public void setPrescricao(Prescricao prescricao) {
        this.prescricao = prescricao;
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
        if (!(object instanceof Inscricaodisciplina)) {
            return false;
        }
        Inscricaodisciplina other = (Inscricaodisciplina) object;
        if ((this.inscricaodisciplinaPK == null && other.inscricaodisciplinaPK != null) || (this.inscricaodisciplinaPK != null && !this.inscricaodisciplinaPK.equals(other.inscricaodisciplinaPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "esira.domain.Inscricaodisciplina[ inscricaodisciplinaPK=" + inscricaodisciplinaPK + " ]";
    }
    
}
