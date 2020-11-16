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
@Table(name = "ingressoexameadmissao")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Ingressoexameadmissao.findAll", query = "SELECT i FROM Ingressoexameadmissao i")})
public class Ingressoexameadmissao implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id_estudante", nullable = false)
    private Long idEstudante;
    @JoinColumn(name = "id_estudante", referencedColumnName = "id_estudante", nullable = false, insertable = false, updatable = false)
    @OneToOne(optional = false, fetch = FetchType.LAZY)
    private Estudante estudante;
    @JoinColumn(name = "provincia_admissao", referencedColumnName = "id_provincia")
    @ManyToOne(fetch = FetchType.LAZY)
    private Provincia provinciaAdmissao;

    public Ingressoexameadmissao() {
    }

    public Ingressoexameadmissao(Long idEstudante) {
        this.idEstudante = idEstudante;
    }

    public Long getIdEstudante() {
        return idEstudante;
    }

    public void setIdEstudante(Long idEstudante) {
        this.idEstudante = idEstudante;
    }

    public Estudante getEstudante() {
        return estudante;
    }

    public void setEstudante(Estudante estudante) {
        this.estudante = estudante;
    }

    public Provincia getProvinciaAdmissao() {
        return provinciaAdmissao;
    }

    public void setProvinciaAdmissao(Provincia provinciaAdmissao) {
        this.provinciaAdmissao = provinciaAdmissao;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idEstudante != null ? idEstudante.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Ingressoexameadmissao)) {
            return false;
        }
        Ingressoexameadmissao other = (Ingressoexameadmissao) object;
        if ((this.idEstudante == null && other.idEstudante != null) || (this.idEstudante != null && !this.idEstudante.equals(other.idEstudante))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "esira.domain.Ingressoexameadmissao[ idEstudante=" + idEstudante + " ]";
    }
    
}
