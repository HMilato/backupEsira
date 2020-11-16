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
@Table(name = "planomatriculas")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Planomatriculas.findAll", query = "SELECT p FROM Planomatriculas p")})
public class Planomatriculas implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ano_lec", nullable = false)
    private Integer anoLec;
    @Column(name = "datainicio")
    @Temporal(TemporalType.TIMESTAMP)
    private Date datainicio;
    @Column(name = "datafim")
    @Temporal(TemporalType.TIMESTAMP)
    private Date datafim;
    @Column(name = "data_fim_tolera")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataFimTolera;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "tax_nac_normal", precision = 17, scale = 17)
    private Double taxNacNormal;
    @Column(name = "tax_estr_normal", precision = 17, scale = 17)
    private Double taxEstrNormal;
    @Column(name = "multa_percentual", precision = 17, scale = 17)
    private Double multaPercentual;

    public Planomatriculas() {
    }

    public Planomatriculas(Integer anoLec) {
        this.anoLec = anoLec;
    }

    public Integer getAnoLec() {
        return anoLec;
    }

    public void setAnoLec(Integer anoLec) {
        this.anoLec = anoLec;
    }

    public Date getDatainicio() {
        return datainicio;
    }

    public void setDatainicio(Date datainicio) {
        this.datainicio = datainicio;
    }

    public Date getDatafim() {
        return datafim;
    }

    public void setDatafim(Date datafim) {
        this.datafim = datafim;
    }

    public Date getDataFimTolera() {
        return dataFimTolera;
    }

    public void setDataFimTolera(Date dataFimTolera) {
        this.dataFimTolera = dataFimTolera;
    }

    public Double getTaxNacNormal() {
        return taxNacNormal;
    }

    public void setTaxNacNormal(Double taxNacNormal) {
        this.taxNacNormal = taxNacNormal;
    }

    public Double getTaxEstrNormal() {
        return taxEstrNormal;
    }

    public void setTaxEstrNormal(Double taxEstrNormal) {
        this.taxEstrNormal = taxEstrNormal;
    }

    public Double getMultaPercentual() {
        return multaPercentual;
    }

    public void setMultaPercentual(Double multaPercentual) {
        this.multaPercentual = multaPercentual;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (anoLec != null ? anoLec.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Planomatriculas)) {
            return false;
        }
        Planomatriculas other = (Planomatriculas) object;
        if ((this.anoLec == null && other.anoLec != null) || (this.anoLec != null && !this.anoLec.equals(other.anoLec))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "esira.domain.Planomatriculas[ anoLec=" + anoLec + " ]";
    }
    
}
