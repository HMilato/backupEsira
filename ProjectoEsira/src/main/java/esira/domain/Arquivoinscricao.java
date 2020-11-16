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
 * @author Ussimane
 */
@Entity
@Table(name = "arquivoinscricao")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Arquivoinscricao.findAll", query = "SELECT a FROM Arquivoinscricao a"),
    @NamedQuery(name = "Arquivoinscricao.findByNome", query = "SELECT a FROM Arquivoinscricao a WHERE a.nome = :nome"),
    @NamedQuery(name = "Arquivoinscricao.findBySemestre", query = "SELECT a FROM Arquivoinscricao a WHERE a.semestre = :semestre"),
    @NamedQuery(name = "Arquivoinscricao.findByNrtalao", query = "SELECT a FROM Arquivoinscricao a WHERE a.nrtalao = :nrtalao"),
    @NamedQuery(name = "Arquivoinscricao.findByDataInscricao", query = "SELECT a FROM Arquivoinscricao a WHERE a.dataInscricao = :dataInscricao"),
    @NamedQuery(name = "Arquivoinscricao.findByIdarquivoi", query = "SELECT a FROM Arquivoinscricao a WHERE a.idarquivoi = :idarquivoi")})
public class Arquivoinscricao implements Serializable {
    private static final long serialVersionUID = 1L;
    @Column(name = "nome", length = 255)
    private String nome;
    @Column(name = "semestre")
    private Short semestre;
    @Column(name = "nrtalao", length = 255)
    private String nrtalao;
    @Column(name = "data_inscricao")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataInscricao;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idarquivoi", nullable = false)
    private Long idarquivoi;
    @JoinColumn(name = "id_inscricao", referencedColumnName = "id_inscricao", nullable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Inscricao idInscricao;
    @Column(name = "banco", length = 2147483647)
    private String banco;
    @Column(name = "datadeposito")
    @Temporal(TemporalType.TIMESTAMP)
    private Date datadeposito;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "valor", precision = 8, scale = 8)
    private Float valor;
    @Column(name = "estudante", length = 2147483647)
    private String estudante;
    @Column(name = "recibousado")
    private Boolean recibousado;
    @Column(name = "valorcobrado", precision = 8, scale = 8)
    private Float valorcobrado;

    public Arquivoinscricao() {
    }

    public Arquivoinscricao(Long idarquivoi) {
        this.idarquivoi = idarquivoi;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Short getSemestre() {
        return semestre;
    }

    public void setSemestre(Short semestre) {
        this.semestre = semestre;
    }

    public String getNrtalao() {
        return nrtalao;
    }

    public void setNrtalao(String nrtalao) {
        this.nrtalao = nrtalao;
    }

    public Date getDataInscricao() {
        return dataInscricao;
    }

    public void setDataInscricao(Date dataInscricao) {
        this.dataInscricao = dataInscricao;
    }

    public Long getIdarquivoi() {
        return idarquivoi;
    }

    public void setIdarquivoi(Long idarquivoi) {
        this.idarquivoi = idarquivoi;
    }

    public Inscricao getIdInscricao() {
        return idInscricao;
    }

    public void setIdInscricao(Inscricao idInscricao) {
        this.idInscricao = idInscricao;
    }

    public String getBanco() {
        return banco;
    }

    public void setBanco(String banco) {
        this.banco = banco;
    }

    public Date getDatadeposito() {
        return datadeposito;
    }

    public void setDatadeposito(Date datadeposito) {
        this.datadeposito = datadeposito;
    }

    public Float getValor() {
        return valor;
    }

    public void setValor(Float valor) {
        this.valor = valor;
    }

    public String getEstudante() {
        return estudante;
    }

    public void setEstudante(String estudante) {
        this.estudante = estudante;
    }

    public Boolean getRecibousado() {
        return recibousado;
    }

    public void setRecibousado(Boolean recibousado) {
        this.recibousado = recibousado;
    }

    public Float getValorcobrado() {
        return valorcobrado;
    }

    public void setValorcobrado(Float valorcobrado) {
        this.valorcobrado = valorcobrado;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idarquivoi != null ? idarquivoi.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Arquivoinscricao)) {
            return false;
        }
        Arquivoinscricao other = (Arquivoinscricao) object;
        if ((this.idarquivoi == null && other.idarquivoi != null) || (this.idarquivoi != null && !this.idarquivoi.equals(other.idarquivoi))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "modelo.Arquivoinscricao[ idarquivoi=" + idarquivoi + " ]";
    }
    
}
