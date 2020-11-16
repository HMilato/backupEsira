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
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Ussimane
 */
@Entity
@Table(name = "matriculaanulada", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"id_estudante", "ano"})})
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Matriculaanulada.findAll", query = "SELECT m FROM Matriculaanulada m")})
public class Matriculaanulada implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected MatriculaPK matriculaPK;
    @Column(name = "data_anulacao")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataAnulacao;
    @Column(name = "dataconfirmacao")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataconfirmacao;
    @Column(name = "motivo", length = 255)
    private String motivo;
    @Column(name = "ano_volta")
    private Integer anoVolta;
    @Basic(optional = false)
    @Column(name = "requerimento", length = 255)
    private String requerimento;
    @JoinColumns({
        @JoinColumn(name = "id_estudante", referencedColumnName = "id_estudante", nullable = false, insertable = false, updatable = false),
        @JoinColumn(name = "ano", referencedColumnName = "ano", nullable = false, insertable = false, updatable = false)})
    @OneToOne(optional = false, fetch = FetchType.LAZY)
    private Matricula matricula;
    @Column(name = "estado")
    private Boolean estado;
    @Column(name = "renovada")
    private Boolean renovada;
    @JoinColumn(name = "funcionario", referencedColumnName = "id_funcionario")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Funcionario funcionario;

    public Matriculaanulada() {
    }

    public MatriculaPK getMatriculaPK() {
        return matriculaPK;
    }

    public void setMatriculaPK(MatriculaPK matriculaPK) {
        this.matriculaPK = matriculaPK;
    }

     

    public Date getDataAnulacao() {
        return dataAnulacao;
    }

    public void setDataAnulacao(Date dataAnulacao) {
        this.dataAnulacao = dataAnulacao;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public Integer getAnoVolta() {
        return anoVolta;
    }

    public void setAnoVolta(Integer anoVolta) {
        this.anoVolta = anoVolta;
    }

    public Funcionario getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(Funcionario funcionario) {
        this.funcionario = funcionario;
    }

    public String getRequerimento() {
        return requerimento;
    }

    public void setRequerimento(String requerimento) {
        this.requerimento = requerimento;
    }

    public Matricula getMatricula() {
        return matricula;
    }

    public void setMatricula(Matricula matricula) {
        this.matricula = matricula;
    }

    public Date getDataconfirmacao() {
        return dataconfirmacao;
    }

    public void setDataconfirmacao(Date dataconfirmacao) {
        this.dataconfirmacao = dataconfirmacao;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }
    
    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (matriculaPK != null ? matriculaPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Matriculaanulada)) {
            return false;
        }
        Matriculaanulada other = (Matriculaanulada) object;
        if ((this.matriculaPK == null && other.matriculaPK != null) || (this.matriculaPK != null && !this.matriculaPK.equals(other.matriculaPK))) {
            return false;
        }
        return true;
    }

    public Boolean getRenovada() {
        return renovada;
    }

    public void setRenovada(Boolean renovada) {
        this.renovada = renovada;
    }
    
    

    @Override
    public String toString() {
        return "modelo.Matriculaanulada[ matriculaPK=" + matriculaPK + " ]";
    }
    
}
