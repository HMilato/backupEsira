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
@Table(name = "mudancacurso")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Mudancacurso.findAll", query = "SELECT m FROM Mudancacurso m")})
public class Mudancacurso implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idmudanca", nullable = false)
    private Integer idmudanca;
    @Column(name = "ano")
    private Integer ano;
    @Column(name = "data")
    @Temporal(TemporalType.TIMESTAMP)
    private Date data;
    @JoinColumn(name = "curso_origem", referencedColumnName = "id_curso", nullable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Curso cursoOrigem;
    @JoinColumn(name = "curso_destino", referencedColumnName = "id_curso", nullable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Curso cursoDestino;
    @JoinColumn(name = "id_estudante", referencedColumnName = "id_estudante", nullable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Estudante idEstudante;

    public Mudancacurso() {
    }

    public Mudancacurso(Integer idmudanca) {
        this.idmudanca = idmudanca;
    }

    public Integer getIdmudanca() {
        return idmudanca;
    }

    public void setIdmudanca(Integer idmudanca) {
        this.idmudanca = idmudanca;
    }

    public Integer getAno() {
        return ano;
    }

    public void setAno(Integer ano) {
        this.ano = ano;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public Curso getCursoOrigem() {
        return cursoOrigem;
    }

    public void setCursoOrigem(Curso cursoOrigem) {
        this.cursoOrigem = cursoOrigem;
    }

    public Curso getCursoDestino() {
        return cursoDestino;
    }

    public void setCursoDestino(Curso cursoDestino) {
        this.cursoDestino = cursoDestino;
    }

    public Estudante getIdEstudante() {
        return idEstudante;
    }

    public void setIdEstudante(Estudante idEstudante) {
        this.idEstudante = idEstudante;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idmudanca != null ? idmudanca.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Mudancacurso)) {
            return false;
        }
        Mudancacurso other = (Mudancacurso) object;
        if ((this.idmudanca == null && other.idmudanca != null) || (this.idmudanca != null && !this.idmudanca.equals(other.idmudanca))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "esira.domain.Mudancacurso[ idmudanca=" + idmudanca + " ]";
    }
    
}
