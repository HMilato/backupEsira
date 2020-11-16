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
@Table(name = "pauta")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Pauta.findAll", query = "SELECT p FROM Pauta p")})
public class Pauta implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected PautaPK pautaPK;
    @Column(name = "descricao", length = 2147483647)
    private String descricao;
    @Column(name = "publicada")
    private Boolean publicada;
    @Column(name = "turno")
    private Integer turno;
    @Column(name = "turma")
    private Integer turma;
    @Column(name = "formula", length = 255)
    private String formula;
    @Column(name = "datapublicacao")
    @Temporal(TemporalType.TIMESTAMP)
    private Date datapublicacao;
    @Column(name = "tipoavaliacao", length = 255)
    private String tipoavaliacao;
    @Column(name = "percent")
    private Integer percent;
    @Column(name = "tipo")
    private Short tipo;
    @Column(name = "edicao")
    private Integer edicao;
    @Column(name = "estado")
    private Integer estado;
    @Column(name = "ordem")
    private Short ordem;
    @JoinColumn(name = "iddisc", referencedColumnName = "id_disc", nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Disciplina disciplina;
    @JoinColumn(name = "docente", referencedColumnName = "iddocente", nullable = false, insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Docente docente;
    @JoinColumn(name = "docente", referencedColumnName = "id_funcionario")
    @ManyToOne(fetch = FetchType.LAZY)
    private Funcionario docente1;
    @JoinColumn(name = "funcionario", referencedColumnName = "id_funcionario")
    @ManyToOne(fetch = FetchType.LAZY)
    private Funcionario funcionario;
    @JoinColumn(name = "director", referencedColumnName = "id_funcionario")
    @ManyToOne(fetch = FetchType.LAZY)
    private Funcionario director;
    @JoinColumn(name = "adjunto", referencedColumnName = "id_funcionario")
    @ManyToOne(fetch = FetchType.LAZY)
    private Funcionario adjunto;
    @JoinColumn(name = "coordenador", referencedColumnName = "id_funcionario")
    @ManyToOne(fetch = FetchType.LAZY)
    private Funcionario coordenador;
    @JoinColumn(name = "docente2", referencedColumnName = "id_funcionario")
    @ManyToOne(fetch = FetchType.LAZY)
    private Funcionario docente2;
     @Column(name = "obs", length = 255)
    private String obs;

    public Pauta() {
    }

    public Pauta(PautaPK pautaPK) {
        this.pautaPK = pautaPK;
    }

    public Pauta(long iddisc, int ano, short semestre, Date datap) {
        this.pautaPK = new PautaPK(iddisc, ano, semestre, datap);
    }

    public PautaPK getPautaPK() {
        return pautaPK;
    }

    public Short getOrdem() {
        return ordem;
    }

    public void setOrdem(Short ordem) {
        this.ordem = ordem;
    }

    public Integer getEdicao() {
        return edicao;
    }

    public void setEdicao(Integer edicao) {
        this.edicao = edicao;
    }

    public Integer getEstado() {
        return estado;
    }

    public void setEstado(Integer estado) {
        this.estado = estado;
    }

    public Funcionario getDirector() {
        return director;
    }

    public void setDirector(Funcionario director) {
        this.director = director;
    }

    public Funcionario getAdjunto() {
        return adjunto;
    }

    public void setAdjunto(Funcionario adjunto) {
        this.adjunto = adjunto;
    }

    public Funcionario getCoordenador() {
        return coordenador;
    }

    public void setCoordenador(Funcionario coordenador) {
        this.coordenador = coordenador;
    }

    public Funcionario getDocente2() {
        return docente2;
    }

    public void setDocente2(Funcionario docente2) {
        this.docente2 = docente2;
    }

    public String getObs() {
        return obs;
    }

    public void setObs(String obs) {
        this.obs = obs;
    }

    public void setPautaPK(PautaPK pautaPK) {
        this.pautaPK = pautaPK;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Boolean getPublicada() {
        return publicada;
    }

    public void setPublicada(Boolean publicada) {
        this.publicada = publicada;
    }

    public Integer getTurno() {
        return turno;
    }

    public void setTurno(Integer turno) {
        this.turno = turno;
    }

    public Integer getTurma() {
        return turma;
    }

    public void setTurma(Integer turma) {
        this.turma = turma;
    }

    public String getFormula() {
        return formula;
    }

    public void setFormula(String formula) {
        this.formula = formula;
    }

    public Date getDatapublicacao() {
        return datapublicacao;
    }

    public void setDatapublicacao(Date datapublicacao) {
        this.datapublicacao = datapublicacao;
    }

    public String getTipoavaliacao() {
        return tipoavaliacao;
    }

    public void setTipoavaliacao(String tipoavaliacao) {
        this.tipoavaliacao = tipoavaliacao;
    }

    public Integer getPercent() {
        return percent;
    }

    public void setPercent(Integer percent) {
        this.percent = percent;
    }

    public Short getTipo() {
        return tipo;
    }

    public void setTipo(Short tipo) {
        this.tipo = tipo;
    }

    public Disciplina getDisciplina() {
        return disciplina;
    }

    public void setDisciplina(Disciplina disciplina) {
        this.disciplina = disciplina;
    }

    public Docente getDocente() {
        return docente;
    }

    public void setDocente(Docente docente) {
        this.docente = docente;
    }

    public Funcionario getDocente1() {
        return docente1;
    }

    public void setDocente1(Funcionario docente1) {
        this.docente1 = docente1;
    }

    public Funcionario getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(Funcionario funcionario) {
        this.funcionario = funcionario;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (pautaPK != null ? pautaPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Pauta)) {
            return false;
        }
        Pauta other = (Pauta) object;
        if ((this.pautaPK == null && other.pautaPK != null) || (this.pautaPK != null && !this.pautaPK.equals(other.pautaPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "esira.domain.Pauta[ pautaPK=" + pautaPK + " ]";
    }
    
}
