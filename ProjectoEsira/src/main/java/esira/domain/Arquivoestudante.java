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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Ussimane
 */
@Entity
@Table(name = "arquivoestudante")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Arquivoestudante.findAll", query = "SELECT a FROM Arquivoestudante a"),
    @NamedQuery(name = "Arquivoestudante.findByNomearquivo", query = "SELECT a FROM Arquivoestudante a WHERE a.nomearquivo = :nomearquivo"),
    @NamedQuery(name = "Arquivoestudante.findByTipo", query = "SELECT a FROM Arquivoestudante a WHERE a.tipo = :tipo"),
    @NamedQuery(name = "Arquivoestudante.findByIdarquivo", query = "SELECT a FROM Arquivoestudante a WHERE a.idarquivo = :idarquivo")})
public class Arquivoestudante implements Serializable {
    private static final long serialVersionUID = 1L;
    @Column(name = "nomearquivo", length = 2147483647)
    private String nomearquivo;
    @Column(name = "tipo")
    private Short tipo;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idarquivo", nullable = false)
    private Long idarquivo;
    @JoinColumn(name = "id_estudante", referencedColumnName = "id_estudante", nullable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Estudante idEstudante;

    public Arquivoestudante() {
    }

    public Arquivoestudante(Long idarquivo) {
        this.idarquivo = idarquivo;
    }

    public String getNomearquivo() {
        return nomearquivo;
    }

    public void setNomearquivo(String nomearquivo) {
        this.nomearquivo = nomearquivo;
    }

    public Short getTipo() {
        return tipo;
    }

    public void setTipo(Short tipo) {
        this.tipo = tipo;
    }

    public Long getIdarquivo() {
        return idarquivo;
    }

    public void setIdarquivo(Long idarquivo) {
        this.idarquivo = idarquivo;
    }

    public Estudante getIdEstudante() {
        return idEstudante;
    }

    public void setIdEstudante(Estudante idEstudante) {
        this.idEstudante = idEstudante;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idarquivo != null ? idarquivo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Arquivoestudante)) {
            return false;
        }
        Arquivoestudante other = (Arquivoestudante) object;
        if ((this.idarquivo == null && other.idarquivo != null) || (this.idarquivo != null && !this.idarquivo.equals(other.idarquivo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "esira.domain.Arquivoestudante[ idarquivo=" + idarquivo + " ]";
    }
    
}
