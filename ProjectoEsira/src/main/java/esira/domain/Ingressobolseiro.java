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
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Ussimane
 */
@Entity
@Table(name = "ingressobolseiro")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Ingressobolseiro.findAll", query = "SELECT i FROM Ingressobolseiro i")})
public class Ingressobolseiro implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id_estudante", nullable = false)
    private Long idEstudante;
    @Basic(optional = false)
    @Column(name = "data_aprovacao", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataAprovacao;
    @Column(name = "doador", length = 45)
    private String doador;
    @JoinColumn(name = "bolsa", referencedColumnName = "id_bolsa")
    @ManyToOne(fetch = FetchType.LAZY)
    private Bolsa bolsa;
    @JoinColumn(name = "id_estudante", referencedColumnName = "id_estudante", nullable = false, insertable = false, updatable = false)
    @OneToOne(optional = false, fetch = FetchType.LAZY)
    private Estudante estudante;

    public Ingressobolseiro() {
    }

    public Ingressobolseiro(Long idEstudante) {
        this.idEstudante = idEstudante;
    }

    public Ingressobolseiro(Long idEstudante, Date dataAprovacao) {
        this.idEstudante = idEstudante;
        this.dataAprovacao = dataAprovacao;
    }

    public Long getIdEstudante() {
        return idEstudante;
    }

    public void setIdEstudante(Long idEstudante) {
        this.idEstudante = idEstudante;
    }

    public Date getDataAprovacao() {
        return dataAprovacao;
    }

    public void setDataAprovacao(Date dataAprovacao) {
        this.dataAprovacao = dataAprovacao;
    }

    public String getDoador() {
        return doador;
    }

    public void setDoador(String doador) {
        this.doador = doador;
    }

    public Bolsa getBolsa() {
        return bolsa;
    }

    public void setBolsa(Bolsa bolsa) {
        this.bolsa = bolsa;
    }

    public Estudante getEstudante() {
        return estudante;
    }

    public void setEstudante(Estudante estudante) {
        this.estudante = estudante;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idEstudante != null ? idEstudante.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Ingressobolseiro)) {
            return false;
        }
        Ingressobolseiro other = (Ingressobolseiro) object;
        if ((this.idEstudante == null && other.idEstudante != null) || (this.idEstudante != null && !this.idEstudante.equals(other.idEstudante))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "esira.domain.Ingressobolseiro[ idEstudante=" + idEstudante + " ]";
    }
    
}
