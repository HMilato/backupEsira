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
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Han
 */
@Entity
@Table(name = "planoavaliacao")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Planoavaliacao.findAll", query = "SELECT p FROM Planoavaliacao p")})
public class Planoavaliacao implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected PlanoavaliacaoPK planoavaliacaoPK;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "peso", precision = 8, scale = 8)
    private Float peso;
    @Column(name = "status")
    private Boolean status;
    @Column(name = "quantidade")
    private Integer quantidade;
    @Column(name = "dataavaliacao")
    @Temporal(TemporalType.DATE)
    private Date dataavaliacao;
    @Column(name = "dataregisto")
    @Temporal(TemporalType.DATE)
    private Date dataregisto;
    @Column(name = "sem")
    private Short sem;
    @JoinColumn(name = "iddisc", referencedColumnName = "id_disc", nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Disciplina disciplina;
    @JoinColumn(name = "idtipoavaliacao", referencedColumnName = "idtipoavaliacao", nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Tipoavaliacao tipoavaliacao;

    public Planoavaliacao() {
    }

    public Planoavaliacao(PlanoavaliacaoPK planoavaliacaoPK) {
        this.planoavaliacaoPK = planoavaliacaoPK;
    }

    public Planoavaliacao(long iddisc, int turno, int turma, int idtipoavaliacao, int ano) {
        this.planoavaliacaoPK = new PlanoavaliacaoPK(iddisc, turno, turma, idtipoavaliacao, ano);
    }

    public PlanoavaliacaoPK getPlanoavaliacaoPK() {
        return planoavaliacaoPK;
    }

    public void setPlanoavaliacaoPK(PlanoavaliacaoPK planoavaliacaoPK) {
        this.planoavaliacaoPK = planoavaliacaoPK;
    }

    public Float getPeso() {
        return peso;
    }

    public void setPeso(Float peso) {
        this.peso = peso;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public Date getDataavaliacao() {
        return dataavaliacao;
    }

    public void setDataavaliacao(Date dataavaliacao) {
        this.dataavaliacao = dataavaliacao;
    }

    public Date getDataregisto() {
        return dataregisto;
    }

    public void setDataregisto(Date dataregisto) {
        this.dataregisto = dataregisto;
    }

    public Short getSem() {
        return sem;
    }

    public void setSem(Short sem) {
        this.sem = sem;
    }

    public Disciplina getDisciplina() {
        return disciplina;
    }

    public void setDisciplina(Disciplina disciplina) {
        this.disciplina = disciplina;
    }

    public Tipoavaliacao getTipoavaliacao() {
        return tipoavaliacao;
    }

    public void setTipoavaliacao(Tipoavaliacao tipoavaliacao) {
        this.tipoavaliacao = tipoavaliacao;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (planoavaliacaoPK != null ? planoavaliacaoPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Planoavaliacao)) {
            return false;
        }
        Planoavaliacao other = (Planoavaliacao) object;
        if ((this.planoavaliacaoPK == null && other.planoavaliacaoPK != null) || (this.planoavaliacaoPK != null && !this.planoavaliacaoPK.equals(other.planoavaliacaoPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "esira.domain.Planoavaliacao[ planoavaliacaoPK=" + planoavaliacaoPK + " ]";
    }
    
}
