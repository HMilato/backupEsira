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
import org.codehaus.jackson.annotate.JsonIgnore;

/**
 *
 * @author Ussimane
 */
@Entity
@Table(name = "pais")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Pais.findAll", query = "SELECT p FROM Pais p")})
public class Pais implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_pais", nullable = false)
    private Integer idPais;
    @Column(name = "descricao", length = 45)
    private String descricao;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "paisUniversidade", fetch = FetchType.LAZY)
    private List<Ingressomudancauniversidade> ingressomudancauniversidadeList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "nacionalidade", fetch = FetchType.LAZY)
    private List<Estudante> estudanteList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "escolaPais", fetch = FetchType.LAZY)
    private List<Estudante> estudanteList1;

    public Pais() {
    }

    public Pais(Integer idPais) {
        this.idPais = idPais;
    }

    public Integer getIdPais() {
        return idPais;
    }

    public void setIdPais(Integer idPais) {
        this.idPais = idPais;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    @XmlTransient
    @JsonIgnore
    public List<Ingressomudancauniversidade> getIngressomudancauniversidadeList() {
        return ingressomudancauniversidadeList;
    }

    public void setIngressomudancauniversidadeList(List<Ingressomudancauniversidade> ingressomudancauniversidadeList) {
        this.ingressomudancauniversidadeList = ingressomudancauniversidadeList;
    }

    @XmlTransient
    @JsonIgnore
    public List<Estudante> getEstudanteList() {
        return estudanteList;
    }

    public void setEstudanteList(List<Estudante> estudanteList) {
        this.estudanteList = estudanteList;
    }

    @XmlTransient
    @JsonIgnore
    public List<Estudante> getEstudanteList1() {
        return estudanteList1;
    }

    public void setEstudanteList1(List<Estudante> estudanteList1) {
        this.estudanteList1 = estudanteList1;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idPais != null ? idPais.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Pais)) {
            return false;
        }
        Pais other = (Pais) object;
        if ((this.idPais == null && other.idPais != null) || (this.idPais != null && !this.idPais.equals(other.idPais))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return descricao;
    }
    
}
