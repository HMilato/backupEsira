/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package esira.domain;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
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
 * @author Han
 */
@Entity
@Table(name = "notapauta")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Notapauta.findAll", query = "SELECT n FROM Notapauta n")})
public class Notapauta implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected InscricaodisciplinaPK inscricaodisciplinaPK;
    @JoinColumn(name = "id_disciplina", referencedColumnName = "id_disc", nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Disciplina disciplina;
    @JoinColumn(name = "id_inscricao", referencedColumnName = "id_inscricao", nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Inscricao inscricao;
    @Column(name = "estado")
    private Boolean estado;
    @Column(name = "nota", length = 2147483647)
    private String nota;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "mediafreq", precision = 8, scale = 8)
    private Float mediafreq;
    @Column(name = "notaexame", precision = 8, scale = 8)
    private Float notaexame;
    @Column(name = "notarec", precision = 8, scale = 8)
    private Float notarec;
    @Column(name = "notaextra", precision = 8, scale = 8)
    private Float notaextra;
    @Column(name = "notafinal", precision = 8, scale = 8)
    private Float notafinal;
    @Column(name = "classreq", length = 45)
    private String classreq;
    @Column(name = "classexame", length = 45)
    private String classexame;
    @Column(name = "classextra", length = 45)
    private String classextra;
    @Column(name = "classfreq", length = 45)
    private String classfreq;

    public Notapauta() {
    }

    public Notapauta(InscricaodisciplinaPK inscricaodisciplinaPK) {
        this.inscricaodisciplinaPK = inscricaodisciplinaPK;
    }

    public Notapauta(long iddisc, int idInscricao) {
        this.inscricaodisciplinaPK = new InscricaodisciplinaPK(iddisc, idInscricao);
    }

    public InscricaodisciplinaPK getInscricaodisciplinaPK() {
        return inscricaodisciplinaPK;
    }

    public void setInscricaodisciplinaPK(InscricaodisciplinaPK inscricaodisciplinaPK) {
        this.inscricaodisciplinaPK = inscricaodisciplinaPK;
    }

    public String getClassreq() {
        return classreq;
    }

    public void setClassreq(String classreq) {
        this.classreq = classreq;
    }

    public String getClassexame() {
        return classexame;
    }

    public void setClassexame(String classexame) {
        this.classexame = classexame;
    }

    public String getClassextra() {
        return classextra;
    }

    public void setClassextra(String classextra) {
        this.classextra = classextra;
    }

    public Boolean getEstado() {
        return estado;
    }

    public Float getNotafinal() {
        return notafinal;
    }

    public void setNotafinal(Float notafinal) {
        this.notafinal = notafinal;
    }

    public Disciplina getDisciplina() {
        return disciplina;
    }

    public void setDisciplina(Disciplina disciplina) {
        this.disciplina = disciplina;
    }

    public Inscricao getInscricao() {
        return inscricao;
    }

    public void setInscricao(Inscricao inscricao) {
        this.inscricao = inscricao;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public String getNota() {
        return nota;
    }

    public void setNota(String nota) {
        this.nota = nota;
    }

    public Float getMediafreq() {
        return mediafreq;
    }

    public void setMediafreq(Float mediafreq) {
        this.mediafreq = mediafreq;
    }

    public Float getNotaexame() {
        return notaexame;
    }

    public void setNotaexame(Float notaexame) {
        this.notaexame = notaexame;
    }

    public Float getNotarec() {
        return notarec;
    }

    public void setNotarec(Float notarec) {
        this.notarec = notarec;
    }

    public Float getNotaextra() {
        return notaextra;
    }

    public void setNotaextra(Float notaextra) {
        this.notaextra = notaextra;
    }

    public String getClassfreq() {
        return classfreq;
    }

    public void setClassfreq(String classfreq) {
        this.classfreq = classfreq;
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
        if (!(object instanceof Notapauta)) {
            return false;
        }
        Notapauta other = (Notapauta) object;
        if ((this.inscricaodisciplinaPK == null && other.inscricaodisciplinaPK != null) || (this.inscricaodisciplinaPK != null && !this.inscricaodisciplinaPK.equals(other.inscricaodisciplinaPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "esira.domain.Notapauta[ inscricaodisciplinaPK=" + inscricaodisciplinaPK + " ]";
    }

}
