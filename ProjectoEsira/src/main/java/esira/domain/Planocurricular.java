/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package esira.domain;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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
@Table(name = "planocurricular")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Planocurricular.findAll", query = "SELECT p FROM Planocurricular p")})
public class Planocurricular implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected PlanocurricularPK planocurricularPK;
    @JoinColumn(name = "idcurso", referencedColumnName = "id_curso", nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Curso curso;
    @Column(name = "datainicio")
    @Temporal(TemporalType.TIMESTAMP)
    private Date datainicio;
    @Column(name = "datafinal")
    @Temporal(TemporalType.TIMESTAMP)
    private Date datafinal;
    @Column(name = "nome", nullable = false, length = 64)
    private String nome;

    public Planocurricular() {
    }

    public Planocurricular(PlanocurricularPK planocurricularPK) {
        this.planocurricularPK = planocurricularPK;
    }

    public Planocurricular(int ano, int idcurso) {
        this.planocurricularPK = new PlanocurricularPK(ano, idcurso);
    }

    public PlanocurricularPK getPlanocurricularPK() {
        return planocurricularPK;
    }

    public void setPlanocurricularPK(PlanocurricularPK planocurricularPK) {
        this.planocurricularPK = planocurricularPK;
    }

    public Curso getCurso() {
        return curso;
    }

    public void setCurso(Curso curso) {
        this.curso = curso;
    }

    public Date getDatainicio() {
        return datainicio;
    }

    public void setDatainicio(Date datainicio) {
        this.datainicio = datainicio;
    }

    public Date getDatafinal() {
        return datafinal;
    }

    public void setDatafinal(Date datafinal) {
        this.datafinal = datafinal;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
    
    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (planocurricularPK != null ? planocurricularPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Planocurricular)) {
            return false;
        }
        Planocurricular other = (Planocurricular) object;
        if ((this.planocurricularPK == null && other.planocurricularPK != null) || (this.planocurricularPK != null && !this.planocurricularPK.equals(other.planocurricularPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return planocurricularPK.getAno() + "";
    }

}
