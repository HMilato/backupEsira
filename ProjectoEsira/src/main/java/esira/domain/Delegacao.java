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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author FE-SI1
 */
@Entity
@Table(name = "delegacao")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Delegacao.findAll", query = "SELECT d FROM Delegacao d"),
    @NamedQuery(name = "Delegacao.findByIddelegacao", query = "SELECT d FROM Delegacao d WHERE d.iddelegacao = :iddelegacao"),
    @NamedQuery(name = "Delegacao.findByDelegacao", query = "SELECT d FROM Delegacao d WHERE d.delegacao = :delegacao")})
public class Delegacao implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "iddelegacao", nullable = false)
    private Integer iddelegacao;
    @Size(max = 64)
    @Column(name = "delegacao", length = 64)
    private String delegacao;

    public Delegacao() {
    }

    public Delegacao(Integer iddelegacao) {
        this.iddelegacao = iddelegacao;
    }

    public Integer getIddelegacao() {
        return iddelegacao;
    }

    public void setIddelegacao(Integer iddelegacao) {
        this.iddelegacao = iddelegacao;
    }

    public String getDelegacao() {
        return delegacao;
    }

    public void setDelegacao(String delegacao) {
        this.delegacao = delegacao;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (iddelegacao != null ? iddelegacao.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Delegacao)) {
            return false;
        }
        Delegacao other = (Delegacao) object;
        if ((this.iddelegacao == null && other.iddelegacao != null) || (this.iddelegacao != null && !this.iddelegacao.equals(other.iddelegacao))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return delegacao;
    }
    
}
