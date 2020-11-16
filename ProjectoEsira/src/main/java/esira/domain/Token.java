/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package esira.domain;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Milato
 */
@Entity
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Token.findAll", query = "SELECT t FROM Token t"),
    @NamedQuery(name = "Token.findByIdToken", query = "SELECT t FROM Token t WHERE t.idToken = :idToken"),
    @NamedQuery(name = "Token.findByToken", query = "SELECT t FROM Token t WHERE t.token = :token"),
    @NamedQuery(name = "Token.findByValidadeToken", query = "SELECT t FROM Token t WHERE t.validadeToken = :validadeToken")})
public class Token implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_token", nullable = false)
    private Long idToken;
    @Size(max = 255)
    @Column(length = 255)
    private String token;
    @Column(name = "validade_token")
    @Temporal(TemporalType.TIMESTAMP)
    private Date validadeToken;

    public Token() {
    }

    public Token(Long idToken) {
        this.idToken = idToken;
    }

    public Long getIdToken() {
        return idToken;
    }

    public void setIdToken(Long idToken) {
        this.idToken = idToken;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Date getValidadeToken() {
        return validadeToken;
    }

    public void setValidadeToken(Date validadeToken) {
        this.validadeToken = validadeToken;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idToken != null ? idToken.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Token)) {
            return false;
        }
        Token other = (Token) object;
        if ((this.idToken == null && other.idToken != null) || (this.idToken != null && !this.idToken.equals(other.idToken))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "esira.domain.Token[ idToken=" + idToken + " ]";
    }
    
}
