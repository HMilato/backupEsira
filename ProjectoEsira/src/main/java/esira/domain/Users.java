/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package esira.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Ussimane
 */
@Entity
@Table(name = "users")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Users.findAll", query = "SELECT u FROM Users u"),
    @NamedQuery(name = "Users.findByUtilizador", query = "SELECT u FROM Users u WHERE u.utilizador = ?")})
public class Users implements Serializable {
     private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "utilizador", nullable = false, length = 45)
    private String utilizador;
    @Column(name = "email", length = 45)
    private String email;
    @Column(name = "pasword", length = 45)
    private String pasword;
    @JsonIgnore
    @Column(name = "last_access")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastAccess;
    @Column(name = "nome", length = 255)
    private String nome;
    @Column(name = "uestudante")
    private Boolean uestudante;
    @JoinColumn(name = "id_estudante", referencedColumnName = "id_estudante")
    @ManyToOne(fetch = FetchType.LAZY)
    private Estudante idEstudante;
    @JsonIgnore
    @JoinColumn(name = "faculdade", referencedColumnName = "id_faculdade")
    @ManyToOne(fetch = FetchType.LAZY)
    private Faculdade faculdade;
    @JsonIgnore
    @JoinColumn(name = "id_funcionario", referencedColumnName = "id_funcionario")
    @ManyToOne(fetch = FetchType.LAZY)
    private Funcionario idFuncionario;
    @JsonIgnore
    @JoinColumn(name = "id_grupo", referencedColumnName = "id_grupo")
    @ManyToOne(fetch = FetchType.LAZY)
    private Grupo idGrupo;
    @JsonIgnore
    @JoinTable(name = "usergrupo", joinColumns = {
        @JoinColumn(name = "utilizador", referencedColumnName = "utilizador", nullable = false)}, inverseJoinColumns = {
        @JoinColumn(name = "id_grupo", referencedColumnName = "id_grupo", nullable = false)})
    @ManyToMany(fetch = FetchType.LAZY)
    private List<Grupo> grupoList;
    @JsonIgnore
    @Column(name = "tenant", length = 45)
    private String tenant;
    @Column(length = 255)
    private String token;
    @Column(name = "validade_token")
    @Temporal(TemporalType.TIMESTAMP)
    private Date validadeToken;

    public Users() {
    }

    public Users(String utilizador) {
        this.utilizador = utilizador;
    }



    public String getUtilizador() {
        return utilizador;
    }

    public void setUtilizador(String utilizador) {
        this.utilizador = utilizador;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    
    @XmlTransient
    public List<Grupo> getGrupoList() {
        return grupoList;
    }

    public void setGrupoList(List<Grupo> grupoList) {
        this.grupoList = grupoList;
    }

    public String getPasword() {
        return pasword;
    }

    public void setPasword(String pasword) {
        this.pasword = pasword;
    }

    public Date getLastAccess() {
        return lastAccess;
    }

    public void setLastAccess(Date lastAccess) {
        this.lastAccess = lastAccess;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Estudante getIdEstudante() {
        return idEstudante;
    }

    public void setIdEstudante(Estudante idEstudante) {
        this.idEstudante = idEstudante;
    }

    public Funcionario getIdFuncionario() {
        return idFuncionario;
    }

    public void setIdFuncionario(Funcionario idFuncionario) {
        this.idFuncionario = idFuncionario;
    }


    public Boolean getUestudante() {
        return uestudante;
    }

    public void setUestudante(Boolean uestudante) {
        this.uestudante = uestudante;
    }

    public Faculdade getFaculdade() {
        return faculdade;
    }

    public void setFaculdade(Faculdade faculdade) {
        this.faculdade = faculdade;
    }

    public Grupo getIdGrupo() {
        return idGrupo;
    }

    public void setIdGrupo(Grupo idGrupo) {
        this.idGrupo = idGrupo;
    }

    public String getTenant() {
        return tenant;
    }

    public void setTenant(String tenant) {
        this.tenant = tenant;
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
        hash += (utilizador != null ? utilizador.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Users)) {
            return false;
        }
        Users other = (Users) object;
        if ((this.utilizador == null && other.utilizador != null) || (this.utilizador != null && !this.utilizador.equals(other.utilizador))) {
            return false;
        }
        return true;
    }

       @Override
    public String toString() {
        return nome;
    }
    public static Users clone(Users user) {
        try {
            return (Users) user.clone();
        } catch (CloneNotSupportedException e) {
            //not possible
        }
        return null;
    }

    public boolean isAdmin() {
        if (utilizador.equals("admin")) {
            return true;
        }

        return false;
    }
    
}
