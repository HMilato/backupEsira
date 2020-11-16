/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package esira.domain;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Ussimane
 */
@Entity
@Table(name = "utilizadorgeral", catalog = "fecn1", schema = "fecn1")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Utilizadorgeral.findAll", query = "SELECT u FROM Utilizadorgeral u"),
    @NamedQuery(name = "Utilizadorgeral.findByUtilizador", query = "SELECT u FROM Utilizadorgeral u WHERE u.utilizador = :utilizador"),
    @NamedQuery(name = "Utilizadorgeral.findByEmail", query = "SELECT u FROM Utilizadorgeral u WHERE u.email = :email"),
    @NamedQuery(name = "Utilizadorgeral.findByPasword", query = "SELECT u FROM Utilizadorgeral u WHERE u.pasword = :pasword"),
    @NamedQuery(name = "Utilizadorgeral.findByIdGrupo", query = "SELECT u FROM Utilizadorgeral u WHERE u.idGrupo = :idGrupo"),
    @NamedQuery(name = "Utilizadorgeral.findByLastAccess", query = "SELECT u FROM Utilizadorgeral u WHERE u.lastAccess = :lastAccess"),
    @NamedQuery(name = "Utilizadorgeral.findByNome", query = "SELECT u FROM Utilizadorgeral u WHERE u.nome = :nome"),
    @NamedQuery(name = "Utilizadorgeral.findByIdEstudante", query = "SELECT u FROM Utilizadorgeral u WHERE u.idEstudante = :idEstudante"),
    @NamedQuery(name = "Utilizadorgeral.findByFaculdade", query = "SELECT u FROM Utilizadorgeral u WHERE u.faculdade = :faculdade"),
    @NamedQuery(name = "Utilizadorgeral.findByUestudante", query = "SELECT u FROM Utilizadorgeral u WHERE u.uestudante = :uestudante"),
    @NamedQuery(name = "Utilizadorgeral.findByIdFuncionario", query = "SELECT u FROM Utilizadorgeral u WHERE u.idFuncionario = :idFuncionario")})
public class Utilizadorgeral implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "utilizador", length = 45)
    private String utilizador;
    @Column(name = "email", length = 45)
    private String email;
    @Column(name = "pasword", length = 45)
    private String pasword;
    @Column(name = "id_grupo", length = 45)
    private String idGrupo;
    @Column(name = "last_access")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastAccess;
    @Column(name = "nome", length = 45)
    private String nome;
    @Column(name = "id_estudante")
    private BigInteger idEstudante;
    @Column(name = "faculdade")
    private Integer faculdade;
    @Column(name = "uestudante")
    private Boolean uestudante;
    @Column(name = "id_funcionario")
    private BigInteger idFuncionario;
    @Column(name = "tenant")
    private String tenant;

    public Utilizadorgeral() {
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

    public String getPasword() {
        return pasword;
    }

    public void setPasword(String pasword) {
        this.pasword = pasword;
    }

    public String getIdGrupo() {
        return idGrupo;
    }

    public void setIdGrupo(String idGrupo) {
        this.idGrupo = idGrupo;
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

    public BigInteger getIdEstudante() {
        return idEstudante;
    }

    public void setIdEstudante(BigInteger idEstudante) {
        this.idEstudante = idEstudante;
    }

    public Integer getFaculdade() {
        return faculdade;
    }

    public void setFaculdade(Integer faculdade) {
        this.faculdade = faculdade;
    }

    public Boolean getUestudante() {
        return uestudante;
    }

    public void setUestudante(Boolean uestudante) {
        this.uestudante = uestudante;
    }

    public BigInteger getIdFuncionario() {
        return idFuncionario;
    }

    public void setIdFuncionario(BigInteger idFuncionario) {
        this.idFuncionario = idFuncionario;
    }

    public String getTenant() {
        return tenant;
    }

    public void setTenant(String tenant) {
        this.tenant = tenant;
    }
}
