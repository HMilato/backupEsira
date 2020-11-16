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
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "operacaopedido")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Operacaopedido.findAll", query = "SELECT o FROM Operacaopedido o")})
public class Operacaopedido implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idpermissao", nullable = false)
    private Integer idpermissao;
    @Column(name = "datai")
    @Temporal(TemporalType.TIMESTAMP)
    private Date datai;
    @Column(name = "dataf")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataf;
    @JoinColumn(name = "funcionario", referencedColumnName = "id_funcionario")
    @ManyToOne(fetch = FetchType.LAZY)
    private Funcionario funcionario;
    @JoinColumn(name = "autor", referencedColumnName = "id_funcionario")
    @ManyToOne(fetch = FetchType.LAZY)
    private Funcionario autor;
    @JoinColumn(name = "operacao", referencedColumnName = "idoperacao")
    @ManyToOne(fetch = FetchType.LAZY)
    private Operacao operacao;

    public Operacaopedido() {
    }

    public Operacaopedido(Integer idpermissao) {
        this.idpermissao = idpermissao;
    }

    public Integer getIdpermissao() {
        return idpermissao;
    }

    public void setIdpermissao(Integer idpermissao) {
        this.idpermissao = idpermissao;
    }

    public Date getDatai() {
        return datai;
    }

    public void setDatai(Date datai) {
        this.datai = datai;
    }

    public Date getDataf() {
        return dataf;
    }

    public void setDataf(Date dataf) {
        this.dataf = dataf;
    }

    public Operacao getOperacao() {
        return operacao;
    }

    public void setOperacao(Operacao operacao) {
        this.operacao = operacao;
    }

    

    public Funcionario getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(Funcionario funcionario) {
        this.funcionario = funcionario;
    }

    public Funcionario getAutor() {
        return autor;
    }

    public void setAutor(Funcionario autor) {
        this.autor = autor;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idpermissao != null ? idpermissao.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Operacaopedido)) {
            return false;
        }
        Operacaopedido other = (Operacaopedido) object;
        if ((this.idpermissao == null && other.idpermissao != null) || (this.idpermissao != null && !this.idpermissao.equals(other.idpermissao))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "esira.domain.Operacaopedido[ idpermissao=" + idpermissao + " ]";
    }
    
}
