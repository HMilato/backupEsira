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
import javax.persistence.JoinColumns;
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
@Table(name = "arquivomatricula")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Arquivomatricula.findAll", query = "SELECT a FROM Arquivomatricula a"),
    @NamedQuery(name = "Arquivomatricula.findByNomearquivo", query = "SELECT a FROM Arquivomatricula a WHERE a.nomearquivo = :nomearquivo"),
    @NamedQuery(name = "Arquivomatricula.findByNrtalao", query = "SELECT a FROM Arquivomatricula a WHERE a.nrtalao = :nrtalao"),
    @NamedQuery(name = "Arquivomatricula.findByIdarquivom", query = "SELECT a FROM Arquivomatricula a WHERE a.idarquivom = :idarquivom")})
public class Arquivomatricula implements Serializable {
    private static final long serialVersionUID = 1L;
    @Column(name = "nomearquivo", length = 255)
    private String nomearquivo;
    @Column(name = "nrtalao", length = 255)
    private String nrtalao;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idarquivom", nullable = false)
    private Long idarquivom;
    @JoinColumns({
        @JoinColumn(name = "id_estudante", referencedColumnName = "id_estudante", nullable = false),
        @JoinColumn(name = "ano", referencedColumnName = "ano", nullable = false)})
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Matricula matricula;
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
    @Column(name = "datamat")
    @Temporal(TemporalType.TIMESTAMP)
    private Date datamat;
    @Column(name = "valorcobrado", precision = 8, scale = 8)
    private Float valorcobrado;

    public Arquivomatricula() {
    }

    public Arquivomatricula(Long idarquivom) {
        this.idarquivom = idarquivom;
    }

    public String getNomearquivo() {
        return nomearquivo;
    }

    public void setNomearquivo(String nomearquivo) {
        this.nomearquivo = nomearquivo;
    }

    public String getNrtalao() {
        return nrtalao;
    }

    public void setNrtalao(String nrtalao) {
        this.nrtalao = nrtalao;
    }

    public Long getIdarquivom() {
        return idarquivom;
    }

    public void setIdarquivom(Long idarquivom) {
        this.idarquivom = idarquivom;
    }

    public Matricula getMatricula() {
        return matricula;
    }

    public void setMatricula(Matricula matricula) {
        this.matricula = matricula;
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

    public Date getDatamat() {
        return datamat;
    }

    public void setDatamat(Date datamat) {
        this.datamat = datamat;
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
        hash += (idarquivom != null ? idarquivom.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Arquivomatricula)) {
            return false;
        }
        Arquivomatricula other = (Arquivomatricula) object;
        if ((this.idarquivom == null && other.idarquivom != null) || (this.idarquivom != null && !this.idarquivom.equals(other.idarquivom))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "modelo.Arquivomatricula[ idarquivom=" + idarquivom + " ]";
    }
    
}
