/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package esira.domain;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Ussimane
 */
@Entity
@Table(name = "operacao")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Operacao.findAll", query = "SELECT o FROM Operacao o")})
public class Operacao implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "idoperacao", nullable = false)
    private Integer idoperacao;
    @Column(name = "descricao", length = 2147483647)
    private String descricao;
    @OneToMany(mappedBy = "operacao", fetch = FetchType.LAZY)
    private List<Operacaopedido> operacaopedidoList;

    public Operacao() {
    }

    public Operacao(Integer idoperacao) {
        this.idoperacao = idoperacao;
    }

    public Integer getIdoperacao() {
        return idoperacao;
    }

    public void setIdoperacao(Integer idoperacao) {
        this.idoperacao = idoperacao;
    }

    public String getDescricao() {
        return descricao;
    }
    
     @XmlTransient
    public List<Operacaopedido> getOperacaopedidoList() {
        return operacaopedidoList;
    }

    public void setOperacaopedidoList(List<Operacaopedido> operacaopedidoList) {
        this.operacaopedidoList = operacaopedidoList;
    }

    
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idoperacao != null ? idoperacao.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Operacao)) {
            return false;
        }
        Operacao other = (Operacao) object;
        if ((this.idoperacao == null && other.idoperacao != null) || (this.idoperacao != null && !this.idoperacao.equals(other.idoperacao))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return descricao;
    }
    
}
