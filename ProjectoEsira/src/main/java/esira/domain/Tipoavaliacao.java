/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package esira.domain;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Han
 */
@Entity
@Table(name = "tipoavaliacao")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Tipoavaliacao.findAll", query = "SELECT t FROM Tipoavaliacao t")})
public class Tipoavaliacao implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idtipoavaliacao", nullable = false)
    private Integer idtipoavaliacao;
    @Column(name = "descricao", length = 255)
    private String descricao;
    @Column(name = "pesomaximo")
    private Integer pesomaximo;
    @Column(name = "estado")
    private Boolean estado;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tipoavaliacao", fetch = FetchType.LAZY)
    private List<Planoavaliacao> planoavaliacaoList;

    public Tipoavaliacao() {
    }

    public Tipoavaliacao(Integer idtipoavaliacao) {
        this.idtipoavaliacao = idtipoavaliacao;
    }

    public Integer getIdtipoavaliacao() {
        return idtipoavaliacao;
    }

    public void setIdtipoavaliacao(Integer idtipoavaliacao) {
        this.idtipoavaliacao = idtipoavaliacao;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Integer getPesomaximo() {
        return pesomaximo;
    }

    public void setPesomaximo(Integer pesomaximo) {
        this.pesomaximo = pesomaximo;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    @XmlTransient
    public List<Planoavaliacao> getPlanoavaliacaoList() {
        return planoavaliacaoList;
    }

    public void setPlanoavaliacaoList(List<Planoavaliacao> planoavaliacaoList) {
        this.planoavaliacaoList = planoavaliacaoList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idtipoavaliacao != null ? idtipoavaliacao.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Tipoavaliacao)) {
            return false;
        }
        Tipoavaliacao other = (Tipoavaliacao) object;
        if ((this.idtipoavaliacao == null && other.idtipoavaliacao != null) || (this.idtipoavaliacao != null && !this.idtipoavaliacao.equals(other.idtipoavaliacao))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return descricao;
    }
    
}
