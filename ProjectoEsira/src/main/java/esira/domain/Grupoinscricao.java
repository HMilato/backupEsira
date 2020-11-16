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
@Table(name = "grupoinscricao")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Grupoinscricao.findAll", query = "SELECT g FROM Grupoinscricao g")})
public class Grupoinscricao implements Serializable {

    private static final long serialVersionUID = 1L;
    @Basic(optional = false)
    @Column(name = "descricao", nullable = false, length = 2147483647)
    private String descricao;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idgrupoi", nullable = false)
    private Long idgrupoi;
    @OneToMany(mappedBy = "grupo", fetch = FetchType.LAZY)
    private List<Inscricao> inscricaoList;
    @Column(name = "niveli") //guarda o o nivel mais alto dentre as disciplinas contidas na inscricao
    private Short niveli;

    public Grupoinscricao() {
    }

    public Grupoinscricao(Long idgrupoi) {
        this.idgrupoi = idgrupoi;
    }

    public Grupoinscricao(Long idgrupoi, String descricao) {
        this.idgrupoi = idgrupoi;
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Long getIdgrupoi() {
        return idgrupoi;
    }

    public void setIdgrupoi(Long idgrupoi) {
        this.idgrupoi = idgrupoi;
    }

    public Short getNiveli() {
        return niveli;
    }

    public void setNiveli(Short niveli) {
        this.niveli = niveli;
    }

    @XmlTransient
    public List<Inscricao> getInscricaoList() {
        return inscricaoList;
    }

    public void setInscricaoList(List<Inscricao> inscricaoList) {
        this.inscricaoList = inscricaoList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idgrupoi != null ? idgrupoi.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Grupoinscricao)) {
            return false;
        }
        Grupoinscricao other = (Grupoinscricao) object;
        if ((this.idgrupoi == null && other.idgrupoi != null) || (this.idgrupoi != null && !this.idgrupoi.equals(other.idgrupoi))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "esira.domain.Grupoinscricao[ idgrupoi=" + idgrupoi + " ]";
    }

}
