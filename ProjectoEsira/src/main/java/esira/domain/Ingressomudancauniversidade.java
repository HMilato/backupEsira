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
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Ussimane
 */
@Entity
@Table(name = "ingressomudancauniversidade")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Ingressomudancauniversidade.findAll", query = "SELECT i FROM Ingressomudancauniversidade i")})
public class Ingressomudancauniversidade implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id_estudante", nullable = false)
    private Long idEstudante;
    @Column(name = "nome_universidade", length = 45)
    private String nomeUniversidade;
    @Column(name = "tipouniversidade", length = 255)
    private String tipouniversidade;
    @JoinColumn(name = "id_estudante", referencedColumnName = "id_estudante", nullable = false, insertable = false, updatable = false)
    @OneToOne(optional = false, fetch = FetchType.LAZY)
    private Estudante estudante;
    @JoinColumn(name = "pais_universidade", referencedColumnName = "id_pais", nullable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Pais paisUniversidade;

    public Ingressomudancauniversidade() {
    }

    public Ingressomudancauniversidade(Long idEstudante) {
        this.idEstudante = idEstudante;
    }

    public Long getIdEstudante() {
        return idEstudante;
    }

    public void setIdEstudante(Long idEstudante) {
        this.idEstudante = idEstudante;
    }

    public String getNomeUniversidade() {
        return nomeUniversidade;
    }

    public void setNomeUniversidade(String nomeUniversidade) {
        this.nomeUniversidade = nomeUniversidade;
    }

    public String getTipouniversidade() {
        return tipouniversidade;
    }

    public void setTipouniversidade(String tipouniversidade) {
        this.tipouniversidade = tipouniversidade;
    }

    public Estudante getEstudante() {
        return estudante;
    }

    public void setEstudante(Estudante estudante) {
        this.estudante = estudante;
    }

    public Pais getPaisUniversidade() {
        return paisUniversidade;
    }

    public void setPaisUniversidade(Pais paisUniversidade) {
        this.paisUniversidade = paisUniversidade;
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
        if (!(object instanceof Ingressomudancauniversidade)) {
            return false;
        }
        Ingressomudancauniversidade other = (Ingressomudancauniversidade) object;
        if ((this.idEstudante == null && other.idEstudante != null) || (this.idEstudante != null && !this.idEstudante.equals(other.idEstudante))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "esira.domain.Ingressomudancauniversidade[ idEstudante=" + idEstudante + " ]";
    }
    
}
