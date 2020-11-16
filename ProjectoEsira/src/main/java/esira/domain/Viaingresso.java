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
import org.codehaus.jackson.annotate.JsonIgnore;

/**
 *
 * @author Ussimane
 */
@Entity
@Table(name = "viaingresso")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Viaingresso.findAll", query = "SELECT v FROM Viaingresso v")})
public class Viaingresso implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id_via_ingresso", nullable = false)
    private Integer idViaIngresso;
    @Column(name = "descricao", length = 45)
    private String descricao;
    @OneToMany(mappedBy = "viaIngresso", fetch = FetchType.LAZY)
    private List<Estudante> estudanteList;

    public Viaingresso() {
    }

    public Viaingresso(Integer idViaIngresso) {
        this.idViaIngresso = idViaIngresso;
    }

    public Integer getIdViaIngresso() {
        return idViaIngresso;
    }

    public void setIdViaIngresso(Integer idViaIngresso) {
        this.idViaIngresso = idViaIngresso;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    @XmlTransient
    @JsonIgnore
    public List<Estudante> getEstudanteList() {
        return estudanteList;
    }

    public void setEstudanteList(List<Estudante> estudanteList) {
        this.estudanteList = estudanteList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idViaIngresso != null ? idViaIngresso.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Viaingresso)) {
            return false;
        }
        Viaingresso other = (Viaingresso) object;
        if ((this.idViaIngresso == null && other.idViaIngresso != null) || (this.idViaIngresso != null && !this.idViaIngresso.equals(other.idViaIngresso))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return descricao;
    }
    
}
