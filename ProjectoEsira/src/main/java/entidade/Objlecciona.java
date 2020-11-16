/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidade;

import esira.domain.Lecciona;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import org.zkoss.util.media.Media;

/**
 *
 * @author Ussimane
 */
@Entity
public class Objlecciona implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Lecciona le;
    
    public Long getId() {
        return id;
    }

    public Lecciona getLe() {
        return le;
    }

    public void setLe(Lecciona le) {
        this.le = le;
    }

    public Objlecciona(Lecciona le) {
        this.le = le;
    }

    
    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

   

    @Override
    public String toString() {
        return "entidade.Arquivo[ id=" + id + " ]";
    }

}
