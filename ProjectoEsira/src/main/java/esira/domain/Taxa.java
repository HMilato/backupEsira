/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package esira.domain;

import java.io.Serializable;
import java.math.BigInteger;
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
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Milato
 */
@Entity
@Table(catalog = "fecn1", schema = "fecn1")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Taxa.findAll", query = "SELECT t FROM Taxa t"),
    @NamedQuery(name = "Taxa.findByNomeTaxa", query = "SELECT t FROM Taxa t WHERE t.nomeTaxa = :nomeTaxa"),
    @NamedQuery(name = "Taxa.findByFaculdade", query = "SELECT t FROM Taxa t WHERE t.faculdade = :faculdade"),
    @NamedQuery(name = "Taxa.findByCurso", query = "SELECT t FROM Taxa t WHERE t.curso = :curso"),
    @NamedQuery(name = "Taxa.findByValor", query = "SELECT t FROM Taxa t WHERE t.valor = :valor"),
    @NamedQuery(name = "Taxa.findByIdTaxa", query = "SELECT t FROM Taxa t WHERE t.idTaxa = :idTaxa")})
public class Taxa implements Serializable {
    private static final long serialVersionUID = 1L;
    @Size(max = 255)
    @Column(name = "nome_taxa", length = 255)
    private String nomeTaxa;
    
    @JoinColumn(name = "faculdade", referencedColumnName = "id_faculdade")
    @ManyToOne(fetch = FetchType.LAZY)
    private Faculdade faculdade;
   // private Integer faculdade;
    
    @JoinColumn(name = "curso", referencedColumnName = "id_curso")
    @ManyToOne(fetch = FetchType.LAZY)
    private Curso curso;
    //private Long curso;
    
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(precision = 8, scale = 8)
    private Float valor;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_taxa", nullable = false)
    private Integer idTaxa;
  

    public Taxa() {
    }

    public Taxa(Integer idTaxa) {
        this.idTaxa = idTaxa;
    }

    public String getNomeTaxa() {
        return nomeTaxa;
    }

    public void setNomeTaxa(String nomeTaxa) {
        this.nomeTaxa = nomeTaxa;
    }

//    public Integer getFaculdade() {
//        return faculdade;
//    }
//
//    public void setFaculdade(Integer faculdade) {
//        this.faculdade = faculdade;
//    }
    
     public Faculdade getFaculdade() {
        return faculdade;
    }

    public void setFaculdade(Faculdade faculdade) {
        this.faculdade = faculdade;
    }


//    public Long getCurso() {
//        return curso;
//    }
//
//    public void setCurso(Long curso) {
//        this.curso = curso;
//    }
    
      public Curso getCurso() {
        return curso;
    }

    public void setCurso(Curso curso) {
        this.curso = curso;
    }

    public Float getValor() {
        return valor;
    }

    public void setValor(Float valor) {
        this.valor = valor;
    }

    public Integer getIdTaxa() {
        return idTaxa;
    }

    public void setIdTaxa(Integer idTaxa) {
        this.idTaxa = idTaxa;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idTaxa != null ? idTaxa.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Taxa)) {
            return false;
        }
        Taxa other = (Taxa) object;
        if ((this.idTaxa == null && other.idTaxa != null) || (this.idTaxa != null && !this.idTaxa.equals(other.idTaxa))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "esira.domain.Taxa[ idTaxa=" + idTaxa + " ]";
    }
    
}
