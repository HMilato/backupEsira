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
import org.codehaus.jackson.annotate.JsonIgnore;

/**
 *
 * @author Ussimane
 */
@Entity
@Table(name = "bolsa")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Bolsa.findAll", query = "SELECT b FROM Bolsa b")})
public class Bolsa implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_bolsa", nullable = false)
    private Long idBolsa;
    @Column(name = "descricao", length = 45)
    private String descricao;
    @OneToMany(mappedBy = "bolsa", fetch = FetchType.LAZY)
    private List<Ingressobolseiro> ingressobolseiroList;
    @OneToMany(mappedBy = "bolsa", fetch = FetchType.LAZY)
    private List<Estudante> estudanteList;

    public Bolsa() {
    }

    public Bolsa(Long idBolsa) {
        this.idBolsa = idBolsa;
    }

    public Long getIdBolsa() {
        return idBolsa;
    }

    public void setIdBolsa(Long idBolsa) {
        this.idBolsa = idBolsa;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    @XmlTransient
    @JsonIgnore
    public List<Ingressobolseiro> getIngressobolseiroList() {
        return ingressobolseiroList;
    }

    public void setIngressobolseiroList(List<Ingressobolseiro> ingressobolseiroList) {
        this.ingressobolseiroList = ingressobolseiroList;
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
        hash += (idBolsa != null ? idBolsa.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Bolsa)) {
            return false;
        }
        Bolsa other = (Bolsa) object;
        if ((this.idBolsa == null && other.idBolsa != null) || (this.idBolsa != null && !this.idBolsa.equals(other.idBolsa))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return descricao;
    }
    
}
