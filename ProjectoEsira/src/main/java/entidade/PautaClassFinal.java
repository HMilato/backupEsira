/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidade;

import esira.domain.Estudante;
import java.io.Serializable;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 *
 * @author Ussimane
 */
@Entity
public class PautaClassFinal implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Estudante estudante;
    private float mediafreq;
    private String rqfreq;
    private float mediaexame;
    private float mediarec;
    private float mediafinal;
    private String rqfinal;            

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Estudante getEstudante() {
        return estudante;
    }

    public void setEstudante(Estudante estudante) {
        this.estudante = estudante;
    }

    public float getMediafreq() {
        return mediafreq;
    }

    public void setMediafreq(float mediafreq) {
        this.mediafreq = mediafreq;
    }

    public String getRqfreq() {
        return rqfreq;
    }

    public void setRqfreq(String rqfreq) {
        this.rqfreq = rqfreq;
    }

    public float getMediaexame() {
        return mediaexame;
    }

    public void setMediaexame(float mediaexame) {
        this.mediaexame = mediaexame;
    }

    public float getMediarec() {
        return mediarec;
    }

    public void setMediarec(float mediarec) {
        this.mediarec = mediarec;
    }

    public float getMediafinal() {
        return mediafinal;
    }

    public void setMediafinal(float mediafinal) {
        this.mediafinal = mediafinal;
    }

    public String getRqfinal() {
        return rqfinal;
    }

    public void setRqfinal(String rqfinal) {
        this.rqfinal = rqfinal;
    }
    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PautaClassFinal)) {
            return false;
        }
        PautaClassFinal other = (PautaClassFinal) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidade.PautaClassFreq[ id=" + id + " ]";
    }
    
}
