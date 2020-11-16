/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package esira.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Ussimane
 */
@Entity
@Table(name = "faculdade")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Faculdade.findAll", query = "SELECT f FROM Faculdade f")})
public class Faculdade implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_faculdade", nullable = false)
    private Integer idFaculdade;
    @Column(name = "desricao", length = 45)
    private String desricao;
    @Column(name = "abreviatura", length = 11)
    private String abreviatura;
    @Column(name = "endereco", length = 2147483647)
    private String endereco;
    @JsonIgnore
    @OneToMany(mappedBy = "faculdade", fetch = FetchType.LAZY)
    private List<Funcionario> funcionarioList;

    @JsonIgnore
    @OneToMany(mappedBy = "faculdade", fetch = FetchType.LAZY)
    private List<Users> usersList;

    @JsonIgnore
    @OneToMany(mappedBy = "faculdade", fetch = FetchType.LAZY)
    private List<Curso> cursoList;

    @JsonIgnore
    @JoinColumn(name = "director", referencedColumnName = "id_funcionario", nullable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Funcionario director;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "faculdade", fetch = FetchType.LAZY)
    private List<Validacaopendente> validacaopendenteList;

    @JsonIgnore
    @Column(name = "localizacao", length = 2147483647)
    private String localizacao;

    @JsonIgnore
    @OneToMany(mappedBy = "faculdade", fetch = FetchType.LAZY)
    private List<PlanificacaoAnoLectivo> planificacaoAnoLectivoList;

    @Column(name = "tenant", length = 32)
    private String tenant;
    @Column(name = "codigo", length = 32)
    private String codigo;

    @JsonIgnore
    @JoinColumn(name = "iddelegacao", referencedColumnName = "iddelegacao", nullable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Delegacao iddelegacao;

    public Faculdade() {
    }

    public Faculdade(Integer idFaculdade) {
        this.idFaculdade = idFaculdade;
    }

    public Integer getIdFaculdade() {
        return idFaculdade;
    }

    public void setIdFaculdade(Integer idFaculdade) {
        this.idFaculdade = idFaculdade;
    }

    public String getDesricao() {
        return desricao;
    }

    public void setDesricao(String desricao) {
        this.desricao = desricao;
    }

    public Delegacao getIddelegacao() {
        return iddelegacao;
    }

    public void setIddelegacao(Delegacao iddelegacao) {
        this.iddelegacao = iddelegacao;
    }

    public String getAbreviatura() {
        return abreviatura;
    }

    public void setAbreviatura(String abreviatura) {
        this.abreviatura = abreviatura;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getTenant() {
        return tenant;
    }

    public void setTenant(String tenant) {
        this.tenant = tenant;
    }

    @XmlTransient
    @JsonIgnore
    public List<Funcionario> getFuncionarioList() {
        return funcionarioList;
    }

    public void setFuncionarioList(List<Funcionario> funcionarioList) {
        this.funcionarioList = funcionarioList;
    }

    @XmlTransient
    @JsonIgnore
    public List<Users> getUsersList() {
        return usersList;
    }

    public void setUsersList(List<Users> usersList) {
        this.usersList = usersList;
    }

    public String getLocalizacao() {
        return localizacao;
    }

    public void setLocalizacao(String localizacao) {
        this.localizacao = localizacao;
    }

    @XmlTransient
    @JsonIgnore
    public List<Curso> getCursoList() {
        return cursoList;
    }

    public void setCursoList(List<Curso> cursoList) {
        this.cursoList = cursoList;
    }

    public Funcionario getDirector() {
        return director;
    }

    public void setDirector(Funcionario director) {
        this.director = director;
    }

    @XmlTransient
    public List<PlanificacaoAnoLectivo> getPlanificacaoAnoLectivoList() {
        return planificacaoAnoLectivoList;
    }

    public void setPlanificacaoAnoLectivoList(List<PlanificacaoAnoLectivo> planificacaoAnoLectivoList) {
        this.planificacaoAnoLectivoList = planificacaoAnoLectivoList;
    }

    @XmlTransient
    @JsonIgnore
    public List<Validacaopendente> getValidacaopendenteList() {
        return validacaopendenteList;
    }

    public void setValidacaopendenteList(List<Validacaopendente> validacaopendenteList) {
        this.validacaopendenteList = validacaopendenteList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idFaculdade != null ? idFaculdade.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Faculdade)) {
            return false;
        }
        Faculdade other = (Faculdade) object;
        if ((this.idFaculdade == null && other.idFaculdade != null) || (this.idFaculdade != null && !this.idFaculdade.equals(other.idFaculdade))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return desricao;
    }

}
