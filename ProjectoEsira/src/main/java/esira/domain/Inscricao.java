/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package esira.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Ussimane
 */
@Entity
@Table(name = "inscricao")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Inscricao.findAll", query = "SELECT i FROM Inscricao i")})
public class Inscricao implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_inscricao", nullable = false)
    private Long idInscricao;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "taxa_inscricao", precision = 8, scale = 8)
    private Float taxaInscricao;
    @Column(name = "semestre")
    private Short semestre;
    @Column(name = "data_confirmacao")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataConfirmacao;
    @Column(name = "data_inscricao")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataInscricao;
    @Column(name = "modo_inscricao")
    private Short modoInscricao;
    @Column(name = "estado")
    private Boolean estado;
    @JoinColumn(name = "id_estudante", referencedColumnName = "id_estudante", nullable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Estudante idEstudante;
    @JsonIgnore
    @JoinColumn(name = "funcionario", referencedColumnName = "id_funcionario")
    @ManyToOne(fetch = FetchType.LAZY)
    private Funcionario funcionario;
    @JsonIgnore
    @JoinColumn(name = "grupo", referencedColumnName = "idgrupoi")
    @ManyToOne(fetch = FetchType.LAZY)
    private Grupoinscricao grupo;
    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idInscricao", fetch = FetchType.LAZY)
    private List<Arquivoinscricao> arquivoinscricaoList;
    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "inscricao", fetch = FetchType.LAZY)
    private List<Inscricaodisciplina> inscricaodisciplinaList;
    @JsonIgnore
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "inscricao", fetch = FetchType.LAZY)
    private Motivoinsc motivoinsc;
    @Column(name = "multa", precision = 8, scale = 8)
    private Float multa;
    @Column(name = "obs", length = 255)
    private String obs;
    @Column(name = "version")
    private Short version;
    @Column(name = "emailenviado")
    private Boolean emailenviado;

    public Inscricao() {
    }

    public Inscricao(Long idInscricao) {
        this.idInscricao = idInscricao;
    }

    public Long getIdInscricao() {
        return idInscricao;
    }

    public void setIdInscricao(Long idInscricao) {
        this.idInscricao = idInscricao;
    }

    public Float getTaxaInscricao() {
        return taxaInscricao;
    }

    public void setTaxaInscricao(Float taxaInscricao) {
        this.taxaInscricao = taxaInscricao;
    }

    public Short getSemestre() {
        return semestre;
    }

    public void setSemestre(Short semestre) {
        this.semestre = semestre;
    }

    public Date getDataConfirmacao() {
        return dataConfirmacao;
    }

    public void setDataConfirmacao(Date dataConfirmacao) {
        this.dataConfirmacao = dataConfirmacao;
    }

    public Date getDataInscricao() {
        return dataInscricao;
    }

    public String getObs() {
        return obs;
    }

    public void setObs(String obs) {
        this.obs = obs;
    }

    public void setDataInscricao(Date dataInscricao) {
        this.dataInscricao = dataInscricao;
    }

    public Short getModoInscricao() {
        return modoInscricao;
    }

    public void setModoInscricao(Short modoInscricao) {
        this.modoInscricao = modoInscricao;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public Grupoinscricao getGrupo() {
        return grupo;
    }

    public void setGrupo(Grupoinscricao grupo) {
        this.grupo = grupo;
    }

    public Estudante getIdEstudante() {
        return idEstudante;
    }

    public void setIdEstudante(Estudante idEstudante) {
        this.idEstudante = idEstudante;
    }

    public Funcionario getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(Funcionario funcionario) {
        this.funcionario = funcionario;
    }

    public Short getVersion() {
        return version;
    }

    public void setVersion(Short version) {
        this.version = version;
    }

    public Boolean getEmailenviado() {
        return emailenviado;
    }

    public void setEmailenviado(Boolean emailenviado) {
        this.emailenviado = emailenviado;
    }
    
    
    

    @XmlTransient
    public List<Arquivoinscricao> getArquivoinscricaoList() {
        return arquivoinscricaoList;
    }

    public void setArquivoinscricaoList(List<Arquivoinscricao> arquivoinscricaoList) {
        this.arquivoinscricaoList = arquivoinscricaoList;
    }

    @XmlTransient
    public List<Inscricaodisciplina> getInscricaodisciplinaList() {
        return inscricaodisciplinaList;
    }

    public void setInscricaodisciplinaList(List<Inscricaodisciplina> inscricaodisciplinaList) {
        this.inscricaodisciplinaList = inscricaodisciplinaList;
    }

    public Motivoinsc getMotivoinsc() {
        return motivoinsc;
    }

    public void setMotivoinsc(Motivoinsc motivoinsc) {
        this.motivoinsc = motivoinsc;
    }

    public Float getMulta() {
        return multa;
    }

    public void setMulta(Float multa) {
        this.multa = multa;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idInscricao != null ? idInscricao.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Inscricao)) {
            return false;
        }
        Inscricao other = (Inscricao) object;
        if ((this.idInscricao == null && other.idInscricao != null) || (this.idInscricao != null && !this.idInscricao.equals(other.idInscricao))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "esira.domain.Inscricao[ idInscricao=" + idInscricao + " ]";
    }

}
