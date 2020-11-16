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
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
 * @author FE-SI1
 */
@Entity
@Table(name = "listaadmissao")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Listaadmissao.findAll", query = "SELECT l FROM Listaadmissao l"),
    @NamedQuery(name = "Listaadmissao.findByIdaluno", query = "SELECT l FROM Listaadmissao l WHERE l.idaluno = :idaluno"),
    @NamedQuery(name = "Listaadmissao.findByNome", query = "SELECT l FROM Listaadmissao l WHERE l.nome = :nome"),
    @NamedQuery(name = "Listaadmissao.findByNrBI", query = "SELECT l FROM Listaadmissao l WHERE l.nrBI = :nrBI"),
    @NamedQuery(name = "Listaadmissao.findByTelefone", query = "SELECT l FROM Listaadmissao l WHERE l.telefone = :telefone"),
    @NamedQuery(name = "Listaadmissao.findByAno", query = "SELECT l FROM Listaadmissao l WHERE l.ano = :ano"),
    @NamedQuery(name = "Listaadmissao.findByTipoad", query = "SELECT l FROM Listaadmissao l WHERE l.tipoad = :tipoad"),
    @NamedQuery(name = "Listaadmissao.findByMatriculado", query = "SELECT l FROM Listaadmissao l WHERE l.matriculado = :matriculado"),
    @NamedQuery(name = "Listaadmissao.findByEstado", query = "SELECT l FROM Listaadmissao l WHERE l.estado = :estado"),
    @NamedQuery(name = "Listaadmissao.findByNumero", query = "SELECT l FROM Listaadmissao l WHERE l.numero = :numero"),
    @NamedQuery(name = "Listaadmissao.findByTurno", query = "SELECT l FROM Listaadmissao l WHERE l.turno = :turno"),
    @NamedQuery(name = "Listaadmissao.findByNotaAdmissao", query = "SELECT l FROM Listaadmissao l WHERE l.notaAdmissao = :notaAdmissao"),
    @NamedQuery(name = "Listaadmissao.findByDataNascimento", query = "SELECT l FROM Listaadmissao l WHERE l.dataNascimento = :dataNascimento"),
    @NamedQuery(name = "Listaadmissao.findByCidade", query = "SELECT l FROM Listaadmissao l WHERE l.cidade = :cidade"),
    @NamedQuery(name = "Listaadmissao.findByContacto", query = "SELECT l FROM Listaadmissao l WHERE l.contacto = :contacto"),
    @NamedQuery(name = "Listaadmissao.findByContactoAlternativo", query = "SELECT l FROM Listaadmissao l WHERE l.contactoAlternativo = :contactoAlternativo"),
    @NamedQuery(name = "Listaadmissao.findByApelido", query = "SELECT l FROM Listaadmissao l WHERE l.apelido = :apelido")})
public class Listaadmissao implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idaluno", nullable = false)
    private Integer idaluno;
    @Column(name = "nome", length = 45)
    private String nome;
    @Column(name = "nr_b_i", length = 45)
    private String nrBI;
    @Column(name = "telefone", length = 45)
    private String telefone;
    @Column(name = "ano")
    private Integer ano;
    @Column(name = "tipoad", length = 2147483647)
    private String tipoad;
    @Column(name = "matriculado")
    private Boolean matriculado;
    @Column(name = "estado")
    private Boolean estado;
    @Column(name = "numero", length = 45)
    private String numero;
    @Column(name = "turno")
    private Integer turno;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "nota_admissao", precision = 17, scale = 17)
    private Double notaAdmissao;
    @Column(name = "data_nascimento")
    @Temporal(TemporalType.DATE)
    private Date dataNascimento;
    @Column(name = "cidade", length = 255)
    private String cidade;
    @Column(name = "contacto", length = 255)
    private String contacto;
    @Column(name = "contacto_alternativo", length = 255)
    private String contactoAlternativo;
    @Column(name = "apelido", length = 45)
    private String apelido;
    @JoinColumn(name = "curso", referencedColumnName = "id_curso", nullable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Curso curso;
    @JoinColumn(name = "id_estudante", referencedColumnName = "id_estudante")
    @ManyToOne(fetch = FetchType.LAZY)
    private Estudante idEstudante;

    public Listaadmissao() {
    }

    public Listaadmissao(Integer idaluno) {
        this.idaluno = idaluno;
    }

    public Integer getIdaluno() {
        return idaluno;
    }

    public void setIdaluno(Integer idaluno) {
        this.idaluno = idaluno;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getNrBI() {
        return nrBI;
    }

    public void setNrBI(String nrBI) {
        this.nrBI = nrBI;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public Integer getAno() {
        return ano;
    }

    public void setAno(Integer ano) {
        this.ano = ano;
    }

    public String getTipoad() {
        return tipoad;
    }

    public void setTipoad(String tipoad) {
        this.tipoad = tipoad;
    }

    public Boolean getMatriculado() {
        return matriculado;
    }

    public void setMatriculado(Boolean matriculado) {
        this.matriculado = matriculado;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public Integer getTurno() {
        return turno;
    }

    public void setTurno(Integer turno) {
        this.turno = turno;
    }

    public Double getNotaAdmissao() {
        return notaAdmissao;
    }

    public void setNotaAdmissao(Double notaAdmissao) {
        this.notaAdmissao = notaAdmissao;
    }

    public Date getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(Date dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getContacto() {
        return contacto;
    }

    public void setContacto(String contacto) {
        this.contacto = contacto;
    }

    public String getContactoAlternativo() {
        return contactoAlternativo;
    }

    public void setContactoAlternativo(String contactoAlternativo) {
        this.contactoAlternativo = contactoAlternativo;
    }

    public String getApelido() {
        return apelido;
    }

    public void setApelido(String apelido) {
        this.apelido = apelido;
    }

    public Curso getCurso() {
        return curso;
    }

    public void setCurso(Curso curso) {
        this.curso = curso;
    }

    public Estudante getIdEstudante() {
        return idEstudante;
    }

    public void setIdEstudante(Estudante idEstudante) {
        this.idEstudante = idEstudante;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idaluno != null ? idaluno.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Listaadmissao)) {
            return false;
        }
        Listaadmissao other = (Listaadmissao) object;
        if ((this.idaluno == null && other.idaluno != null) || (this.idaluno != null && !this.idaluno.equals(other.idaluno))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "esira.domain.Listaadmissao[ idaluno=" + idaluno + " ]";
    }
    
}
