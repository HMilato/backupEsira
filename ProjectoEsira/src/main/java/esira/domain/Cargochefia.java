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
 * @author Ussimane
 */
@Entity
@Table(name = "cargochefia")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Cargochefia.findAll", query = "SELECT c FROM Cargochefia c")})
public class Cargochefia implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idcargochefia", nullable = false)
    private Integer idcargochefia;
    @Column(name = "descricao", length = 2147483647)
    private String descricao;
    @OneToMany(mappedBy = "cargochefia", fetch = FetchType.LAZY)
    private List<Tipochefia> tipochefiaList;

    public Cargochefia() {
    }

    public Cargochefia(Integer idcargochefia) {
        this.idcargochefia = idcargochefia;
    }

    public Integer getIdcargochefia() {
        return idcargochefia;
    }

    public void setIdcargochefia(Integer idcargochefia) {
        this.idcargochefia = idcargochefia;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    @XmlTransient
    public List<Tipochefia> getTipochefiaList() {
        return tipochefiaList;
    }

    public void setTipochefiaList(List<Tipochefia> tipochefiaList) {
        this.tipochefiaList = tipochefiaList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idcargochefia != null ? idcargochefia.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Cargochefia)) {
            return false;
        }
        Cargochefia other = (Cargochefia) object;
        if ((this.idcargochefia == null && other.idcargochefia != null) || (this.idcargochefia != null && !this.idcargochefia.equals(other.idcargochefia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return descricao;
    }
    
}
