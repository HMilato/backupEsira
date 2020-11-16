/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidade;

import esira.domain.Arquivoinscricao;
import esira.domain.Arquivomatricula;
import esira.domain.Disciplina;
import esira.domain.Estudante;
import esira.domain.Inscricaodisciplina;
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
public class EstudanteMatricula implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Estudante estudante;
    private List<Arquivomatricula> ai;

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

    public List<Arquivomatricula> getAi() {
        return ai;
    }

    public void setAi(List<Arquivomatricula> ai) {
        this.ai = ai;
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
        if (!(object instanceof EstudanteMatricula)) {
            return false;
        }
        EstudanteMatricula other = (EstudanteMatricula) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidade.EstudanteMatricula[ id=" + id + " ]";
    }

}
