/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package esira.domain;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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
@Table(name = "validacaopendente")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Validacaopendente.findAll", query = "SELECT v FROM Validacaopendente v")})
public class Validacaopendente implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected ValidacaopendentePK validacaopendentePK;
    @Column(name = "qtd")
    private Integer qtd;
    @JoinColumn(name = "idfaculdade", referencedColumnName = "id_faculdade", nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Faculdade faculdade;

    public Validacaopendente() {
    }

    public Validacaopendente(ValidacaopendentePK validacaopendentePK) {
        this.validacaopendentePK = validacaopendentePK;
    }

    public Validacaopendente(int idfaculdade, String tipo) {
        this.validacaopendentePK = new ValidacaopendentePK(idfaculdade, tipo);
    }

    public ValidacaopendentePK getValidacaopendentePK() {
        return validacaopendentePK;
    }

    public void setValidacaopendentePK(ValidacaopendentePK validacaopendentePK) {
        this.validacaopendentePK = validacaopendentePK;
    }

    public Integer getQtd() {
        return qtd;
    }

    public void setQtd(Integer qtd) {
        this.qtd = qtd;
    }

    public Faculdade getFaculdade() {
        return faculdade;
    }

    public void setFaculdade(Faculdade faculdade) {
        this.faculdade = faculdade;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (validacaopendentePK != null ? validacaopendentePK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Validacaopendente)) {
            return false;
        }
        Validacaopendente other = (Validacaopendente) object;
        if ((this.validacaopendentePK == null && other.validacaopendentePK != null) || (this.validacaopendentePK != null && !this.validacaopendentePK.equals(other.validacaopendentePK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "esira.domain.Validacaopendente[ validacaopendentePK=" + validacaopendentePK + " ]";
    }
    
}
