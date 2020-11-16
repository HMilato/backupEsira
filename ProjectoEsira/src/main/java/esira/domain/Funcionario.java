/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package esira.domain;

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
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Ussimane
 */
@Entity
@Table(name = "funcionario")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Funcionario.findAll", query = "SELECT f FROM Funcionario f")})
public class Funcionario implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_funcionario", nullable = false)
    private Long idFuncionario;
    @Column(name = "nrfuncionario", length = 45)
    private String nrfuncionario;
    @Column(name = "nome", length = 45)
    private String nome;
    @Column(name = "apelido", length = 45)
    private String apelido;
    @Column(name = "masculino")
    private Boolean masculino;
    @Column(name = "contacto", length = 2147483647)
    private String contacto;
    @Column(name = "email", length = 2147483647)
    private String email;
    @OneToMany(mappedBy = "funcionario", fetch = FetchType.LAZY)
    private List<Inscricao> inscricaoList;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "funcionario", fetch = FetchType.LAZY)
    private Tipochefia tipochefia;
    @JoinColumn(name = "faculdade", referencedColumnName = "id_faculdade")
    @ManyToOne(fetch = FetchType.LAZY)
    private Faculdade faculdade;
    @OneToMany(mappedBy = "idFuncionario", fetch = FetchType.LAZY)
    private List<Users> usersList;
    @OneToMany(mappedBy = "funcionario", fetch = FetchType.LAZY)
    private List<Matricula> matriculaList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "director", fetch = FetchType.LAZY)
    private List<Faculdade> faculdadeList;
    @OneToMany(mappedBy = "funcionario", fetch = FetchType.LAZY)
    private List<Disciplinaanulada> disciplinaanuladaList;
    @OneToMany(mappedBy = "funcionario", fetch = FetchType.LAZY)
    private List<Operacaopedido> funcionarioList;
    @OneToMany(mappedBy = "autor", fetch = FetchType.LAZY)
    private List<Operacaopedido> autorList;
    @OneToMany(mappedBy = "operador", fetch = FetchType.LAZY)
    private List<Prescricao> operadorList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "funcionario", fetch = FetchType.LAZY)
    private List<Matriculaanulada> matriculaanuladaList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idFuncionario", fetch = FetchType.LAZY)
    private List<Notificacao> notificacaoList;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "funcionario", fetch = FetchType.LAZY)
    private Docente docente;
    @Column(name = "grau", length = 45)
    private String grau;

    public Funcionario() {
    }

    public Funcionario(Long idFuncionario) {
        this.idFuncionario = idFuncionario;
    }

    public Long getIdFuncionario() {
        return idFuncionario;
    }

    public void setIdFuncionario(Long idFuncionario) {
        this.idFuncionario = idFuncionario;
    }

    public String getNome() {
        return nome;
    }

    public String getGrau() {
        return grau;
    }

    public void setGrau(String grau) {
        this.grau = grau;
    }
    
    

    public String getContacto() {
        return contacto;
    }

    public void setContacto(String contacto) {
        this.contacto = contacto;
    }

    public Faculdade getFaculdade() {
        return faculdade;
    }

    public Boolean getMasculino() {
        return masculino;
    }

    public void setMasculino(Boolean masculino) {
        this.masculino = masculino;
    }

    public void setFaculdade(Faculdade faculdade) {
        this.faculdade = faculdade;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNrfuncionario() {
        return nrfuncionario;
    }

    public void setNrfuncionario(String nrfuncionario) {
        this.nrfuncionario = nrfuncionario;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Tipochefia getTipochefia() {
        return tipochefia;
    }

    public void setTipochefia(Tipochefia tipochefia) {
        this.tipochefia = tipochefia;
    }

    @XmlTransient
    public List<Operacaopedido> getFuncionarioList() {
        return funcionarioList;
    }

    public void setFuncionarioList(List<Operacaopedido> funcionarioList) {
        this.funcionarioList = funcionarioList;
    }

    @XmlTransient
    public List<Operacaopedido> getAutorList() {
        return autorList;
    }

    public void setAutorList(List<Operacaopedido> autorList) {
        this.autorList = autorList;
    }

    @XmlTransient
    public List<Prescricao> getOperadorList() {
        return operadorList;
    }

    public void setOperadorList(List<Prescricao> operadorList) {
        this.operadorList = operadorList;
    }

    @XmlTransient
    public List<Disciplinaanulada> getDisciplinaanuladaList() {
        return disciplinaanuladaList;
    }

    public void setDisciplinaanuladaList(List<Disciplinaanulada> disciplinaanuladaList) {
        this.disciplinaanuladaList = disciplinaanuladaList;
    }

    public String getApelido() {
        return apelido;
    }

    public void setApelido(String apelido) {
        this.apelido = apelido;
    }

    public Docente getDocente() {
        return docente;
    }

    public void setDocente(Docente docente) {
        this.docente = docente;
    }

    @XmlTransient
    public List<Inscricao> getInscricaoList() {
        return inscricaoList;
    }

    public void setInscricaoList(List<Inscricao> inscricaoList) {
        this.inscricaoList = inscricaoList;
    }

    @XmlTransient
    public List<Users> getUsersList() {
        return usersList;
    }

    public void setUsersList(List<Users> usersList) {
        this.usersList = usersList;
    }

    @XmlTransient
    public List<Matricula> getMatriculaList() {
        return matriculaList;
    }

    public void setMatriculaList(List<Matricula> matriculaList) {
        this.matriculaList = matriculaList;
    }

    @XmlTransient
    public List<Matriculaanulada> getMatriculaanuladaList() {
        return matriculaanuladaList;
    }

    public void setMatriculaanuladaList(List<Matriculaanulada> matriculaanuladaList) {
        this.matriculaanuladaList = matriculaanuladaList;
    }

    @XmlTransient
    public List<Faculdade> getFaculdadeList() {
        return faculdadeList;
    }

    public void setFaculdadeList(List<Faculdade> faculdadeList) {
        this.faculdadeList = faculdadeList;
    }

    @XmlTransient
    public List<Notificacao> getNotificacaoList() {
        return notificacaoList;
    }

    public void setNotificacaoList(List<Notificacao> notificacaoList) {
        this.notificacaoList = notificacaoList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idFuncionario != null ? idFuncionario.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Funcionario)) {
            return false;
        }
        Funcionario other = (Funcionario) object;
        if ((this.idFuncionario == null && other.idFuncionario != null) || (this.idFuncionario != null && !this.idFuncionario.equals(other.idFuncionario))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return nome;
    }

}
