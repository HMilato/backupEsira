/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package esira.domain;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Han
 */
@Entity
@Table(name = "observacao")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Observacao.findAll", query = "SELECT o FROM Observacao o")
    , @NamedQuery(name = "Observacao.findByIdobservacao", query = "SELECT o FROM Observacao o WHERE o.idobservacao = :idobservacao")
    , @NamedQuery(name = "Observacao.findByDescricao", query = "SELECT o FROM Observacao o WHERE o.descricao = :descricao")
    , @NamedQuery(name = "Observacao.findByAbreviatura", query = "SELECT o FROM Observacao o WHERE o.abreviatura = :abreviatura")
    , @NamedQuery(name = "Observacao.findByCodigo", query = "SELECT o FROM Observacao o WHERE o.codigo = :codigo")})
public class Observacao implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idobservacao", nullable = false)
    private Short idobservacao;
    @Size(max = 255)
    @Column(name = "descricao", length = 255)
    private String descricao;
    @Size(max = 255)
    @Column(name = "abreviatura", length = 255)
    private String abreviatura;
    @Column(name = "codigo")
    private Short codigo;

    public Observacao() {
    }

    public Observacao(Short idobservacao) {
        this.idobservacao = idobservacao;
    }

    public Short getIdobservacao() {
        return idobservacao;
    }

    public void setIdobservacao(Short idobservacao) {
        this.idobservacao = idobservacao;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getAbreviatura() {
        return abreviatura;
    }

    public void setAbreviatura(String abreviatura) {
        this.abreviatura = abreviatura;
    }

    public Short getCodigo() {
        return codigo;
    }

    public void setCodigo(Short codigo) {
        this.codigo = codigo;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idobservacao != null ? idobservacao.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Observacao)) {
            return false;
        }
        Observacao other = (Observacao) object;
        if ((this.idobservacao == null && other.idobservacao != null) || (this.idobservacao != null && !this.idobservacao.equals(other.idobservacao))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "esira.domain.Observacao[ idobservacao=" + idobservacao + " ]";
    }
    
}
