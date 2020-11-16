/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidade;

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
public class Arquivo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String nrtalao;
    private Media media;
    private String nome;
    private String banco;
    private Float valor;
    private String estudante;
    private Boolean recibousado;
    private Date datamat;
    
    public Long getId() {
        return id;
    }

    public Arquivo(String nrtalao, Media media) {
        this.nrtalao = nrtalao;
        this.media = media;
    }

    public Arquivo(String nrtalao, Media media, String nome) {
        this.nrtalao = nrtalao;
        this.media = media;
        this.nome = nome;
    }

    public Arquivo(String nrtalao, Media media, String nome, String banco, Float valor, String estudante, Date datamat) {
        this.nrtalao = nrtalao;
        this.media = media;
        this.nome = nome;
        this.banco = banco;
        this.valor = valor;
        this.estudante = estudante;
        this.datamat = datamat;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Arquivo(Media media) {
        this.media = media;
    }

    public String getNrtalao() {
        return nrtalao;
    }

    public void setNrtalao(String nrtalao) {
        this.nrtalao = nrtalao;
    }

    public Media getMedia() {
        return media;
    }

    public void setMedia(Media media) {
        this.media = media;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBanco() {
        return banco;
    }

    public void setBanco(String banco) {
        this.banco = banco;
    }

    public Float getValor() {
        return valor;
    }

    public void setValor(Float valor) {
        this.valor = valor;
    }

    public String getEstudante() {
        return estudante;
    }

    public void setEstudante(String estudante) {
        this.estudante = estudante;
    }

    public Boolean getRecibousado() {
        return recibousado;
    }

    public void setRecibousado(Boolean recibousado) {
        this.recibousado = recibousado;
    }

    public Date getDatamat() {
        return datamat;
    }

    public void setDatamat(Date datamat) {
        this.datamat = datamat;
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
        if (!(object instanceof Arquivo)) {
            return false;
        }
        Arquivo other = (Arquivo) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entidade.Arquivo[ id=" + id + " ]";
    }

}
