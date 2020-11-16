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
@Table(name = "caracter")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Caracter.findAll", query = "SELECT c FROM Caracter c")})
public class Caracter implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_caracter", nullable = false)
    private Long idCaracter;
    @Column(name = "descricao", length = 11)
    private String descricao;
    @OneToMany(mappedBy = "caracter", fetch = FetchType.LAZY)
    private List<Disciplina> disciplinaList;

    public Caracter() {
    }

    public Caracter(Long idCaracter) {
        this.idCaracter = idCaracter;
    }

    public Long getIdCaracter() {
        return idCaracter;
    }

    public void setIdCaracter(Long idCaracter) {
        this.idCaracter = idCaracter;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    @XmlTransient
    @JsonIgnore
    public List<Disciplina> getDisciplinaList() {
        return disciplinaList;
    }

    public void setDisciplinaList(List<Disciplina> disciplinaList) {
        this.disciplinaList = disciplinaList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idCaracter != null ? idCaracter.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Caracter)) {
            return false;
        }
        Caracter other = (Caracter) object;
        if ((this.idCaracter == null && other.idCaracter != null) || (this.idCaracter != null && !this.idCaracter.equals(other.idCaracter))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return descricao;
    }
    
}
