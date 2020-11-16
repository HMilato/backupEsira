/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package esira.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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
@Table(name = "matricula")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Matricula.findAll", query = "SELECT m FROM Matricula m")})
public class Matricula implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected MatriculaPK matriculaPK;
    @Column(name = "data_matricula")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataMatricula;
    @Column(name = "confirmacao")
    @Temporal(TemporalType.TIMESTAMP)
    private Date confirmacao;
    @Column(name = "modo_matricula")
    private Integer modoMatricula;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "valor", precision = 8, scale = 8)
    private Float valor;
    @Column(name = "estado")
    private Boolean estado;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "matricula", fetch = FetchType.LAZY)
    private Motivomat motivomat;
    @JoinColumn(name = "curso", referencedColumnName = "id_curso", nullable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Curso curso;
    @JoinColumn(name = "id_estudante", referencedColumnName = "id_estudante", nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Estudante estudante;
    @JoinColumn(name = "funcionario", referencedColumnName = "id_funcionario")
    @ManyToOne(fetch = FetchType.LAZY)
    private Funcionario funcionario;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "matricula", fetch = FetchType.LAZY)
    private List<Arquivomatricula> arquivomatriculaList;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "matricula", fetch = FetchType.LAZY)
    private Matriculaanulada matriculaanulada;
    @Column(name = "anulada")
    private Boolean anulada;
    @Column(name = "multa", precision = 8, scale = 8)
    private Float multa;
    @Column(name = "obs", length = 255)
    private String obs;
    @Column(name = "periodo", length = 255)
    private String periodo;
    @Column(name = "emailenviado")
    private Boolean emailenviado;
    @Column(name = "version")
    private Short version;

    public Matricula() {
    }

    public Matricula(MatriculaPK matriculaPK) {
        this.matriculaPK = matriculaPK;
    }

    public Matricula(long idEstudante, int ano) {
        this.matriculaPK = new MatriculaPK(idEstudante, ano);
    }

    public String getPeriodo() {
        return periodo;
    }

    public void setPeriodo(String periodo) {
        this.periodo = periodo;
    }

    public Boolean getEmailenviado() {
        return emailenviado;
    }

    public void setEmailenviado(Boolean emailenviado) {
        this.emailenviado = emailenviado;
    }
    
    

    public MatriculaPK getMatriculaPK() {
        return matriculaPK;
    }

    public void setMatriculaPK(MatriculaPK matriculaPK) {
        this.matriculaPK = matriculaPK;
    }

    public Date getDataMatricula() {
        return dataMatricula;
    }

    public void setDataMatricula(Date dataMatricula) {
        this.dataMatricula = dataMatricula;
    }

    public Date getConfirmacao() {
        return confirmacao;
    }

    public void setConfirmacao(Date confirmacao) {
        this.confirmacao = confirmacao;
    }

    public Integer getModoMatricula() {
        return modoMatricula;
    }

    public void setModoMatricula(Integer modoMatricula) {
        this.modoMatricula = modoMatricula;
    }

    public Float getValor() {
        return valor;
    }

    public void setValor(Float valor) {
        this.valor = valor;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public Boolean getAnulada() {
        return anulada;
    }

    public void setAnulada(Boolean anulada) {
        this.anulada = anulada;
    }

    public Motivomat getMotivomat() {
        return motivomat;
    }

    public void setMotivomat(Motivomat motivomat) {
        this.motivomat = motivomat;
    }

    public Curso getCurso() {
        return curso;
    }

    public void setCurso(Curso curso) {
        this.curso = curso;
    }

    public Estudante getEstudante() {
        return estudante;
    }

    public void setEstudante(Estudante estudante) {
        this.estudante = estudante;
    }

    public String getObs() {
        return obs;
    }

    public void setObs(String obs) {
        this.obs = obs;
    }

    public Float getMulta() {
        return multa;
    }

    public void setMulta(Float multa) {
        this.multa = multa;
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
    
    

    @XmlTransient
    public List<Arquivomatricula> getArquivomatriculaList() {
        return arquivomatriculaList;
    }

    public void setArquivomatriculaList(List<Arquivomatricula> arquivomatriculaList) {
        this.arquivomatriculaList = arquivomatriculaList;
    }

    public Matriculaanulada getMatriculaanulada() {
        return matriculaanulada;
    }

    public void setMatriculaanulada(Matriculaanulada matriculaanulada) {
        this.matriculaanulada = matriculaanulada;
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
        if (!(object instanceof Matricula)) {
            return false;
        }
        Matricula other = (Matricula) object;
        if ((this.matriculaPK == null && other.matriculaPK != null) || (this.matriculaPK != null && !this.matriculaPK.equals(other.matriculaPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "esira.domain.Matricula[ matriculaPK=" + matriculaPK + " ]";
    }

}
