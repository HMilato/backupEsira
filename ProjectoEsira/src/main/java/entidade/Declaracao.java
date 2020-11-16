/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidade;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 *
 * @author Meneses
 */
@Entity
public class Declaracao implements Serializable, Comparable<Declaracao> {
    
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    int ano;
    int nivel;
    String disciplina;
    String classificacao;

    public int getAno() {
        return ano;
    }

    public int getNivel() {
        return nivel;
    }

    public String getDisciplina() {
        return disciplina;
    }

    

    public void setId(Long id) {
        this.id = id;
    }

    public void setAno(int ano) {
        this.ano = ano;
    }

    public void setNivel(int nivel) {
        this.nivel = nivel;
    }

    public void setDisciplina(String disciplina) {
        this.disciplina = disciplina;
    }

    public String getClassificacao() {
        return classificacao;
    }

    public void setClassificacao(String classificacao) {
        this.classificacao = classificacao;
    }

    @Override
    public int compareTo(Declaracao d) {
       
            String ano1,ano2 = "";
            ano1 = ""+this.getAno();
            ano2 = ""+d.ano;
          return ano1.compareTo(ano2);
    }

    
    
    
}
