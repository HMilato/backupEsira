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
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Ussimane
 */
@Entity
@Table(name = "tipochefia")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Tipochefia.findAll", query = "SELECT t FROM Tipochefia t")})
public class Tipochefia implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "idfuncionario", nullable = false)
    private Long idfuncionario;
    @Column(name = "descricao", length = 2147483647)
    private String descricao;
    @JoinColumn(name = "cargochefia", referencedColumnName = "idcargochefia")
    @ManyToOne(fetch = FetchType.LAZY)
    private Cargochefia cargochefia;
    @JoinColumn(name = "idfuncionario", referencedColumnName = "id_funcionario", nullable = false, insertable = false, updatable = false)
    @OneToOne(optional = false, fetch = FetchType.LAZY)
    private Funcionario funcionario;

    public Tipochefia() {
    }

    public Tipochefia(Long idfuncionario) {
        this.idfuncionario = idfuncionario;
    }

    public Long getIdfuncionario() {
        return idfuncionario;
    }

    public void setIdfuncionario(Long idfuncionario) {
        this.idfuncionario = idfuncionario;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Cargochefia getCargochefia() {
        return cargochefia;
    }

    public void setCargochefia(Cargochefia cargochefia) {
        this.cargochefia = cargochefia;
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
        hash += (idfuncionario != null ? idfuncionario.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Tipochefia)) {
            return false;
        }
        Tipochefia other = (Tipochefia) object;
        if ((this.idfuncionario == null && other.idfuncionario != null) || (this.idfuncionario != null && !this.idfuncionario.equals(other.idfuncionario))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "esira.domain.Tipochefia[ idfuncionario=" + idfuncionario + " ]";
    }
    
}
