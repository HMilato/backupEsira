/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package esira.domain;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
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
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import org.codehaus.jackson.annotate.JsonIgnore;

/**
 *
 * @author Ussimane
 */
@Entity
@Table(name = "curso")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Curso.findAll", query = "SELECT c FROM Curso c")})
public class Curso implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_curso", nullable = false)
    private Long idCurso;
    @Column(name = "codigo_curso", length = 45)
    private String codigoCurso;
    @Column(name = "descricao", length = 45)
    private String descricao;
    @Column(name = "qtd_semestres")
    private Integer qtdSemestres;
    @Column(name = "abreviatura", length = 11)
    private String abreviatura;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "curso", fetch = FetchType.LAZY)
    private List<Listaadmissao> listaadmissaoList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "curso", fetch = FetchType.LAZY)
    private List<Ingressotransferencia> ingressotransferenciaList;
    @OneToMany(mappedBy = "curso", fetch = FetchType.LAZY)
    private List<Disciplina> disciplinaList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cursoOrigem", fetch = FetchType.LAZY)
    private List<Mudancacurso> mudancacursoList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cursoDestino", fetch = FetchType.LAZY)
    private List<Mudancacurso> mudancacursoList1;
    @JoinColumn(name = "faculdade", referencedColumnName = "id_faculdade")
    @ManyToOne(fetch = FetchType.LAZY)
    private Faculdade faculdade;
    @JoinColumn(name = "turno", referencedColumnName = "id_periodo")
    @ManyToOne(fetch = FetchType.LAZY)
    private Periodo turno;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "curso", fetch = FetchType.LAZY)
    private List<Matricula> matriculaList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cursocurrente", fetch = FetchType.LAZY)
    private List<Estudante> estudanteList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cursoingresso", fetch = FetchType.LAZY)
    private List<Estudante> estudanteList1;
     @OneToMany(cascade = CascadeType.ALL, mappedBy = "curso", fetch = FetchType.LAZY)
    private List<Planocurricular> planocurricularList;
     @Column(name = "planoc")
    private Integer planoc;
     @JoinColumn(name = "id_curso", referencedColumnName = "id_curso", insertable = false, updatable = false)
    @OneToOne(optional = false, fetch = FetchType.LAZY)
    private Curso curso1;

    public Curso() {
    }

    public Curso(Long idCurso) {
        this.idCurso = idCurso;
    }

    public Long getIdCurso() {
        return idCurso;
    }

    public void setIdCurso(Long idCurso) {
        this.idCurso = idCurso;
    }

    public String getCodigoCurso() {
        return codigoCurso;
    }

    public Periodo getTurno() {
        return turno;
    }

    public void setTurno(Periodo turno) {
        this.turno = turno;
    }
    
    

    public void setCodigoCurso(String codigoCurso) {
        this.codigoCurso = codigoCurso;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Integer getQtdSemestres() {
        return qtdSemestres;
    }

    public void setQtdSemestres(Integer qtdSemestres) {
        this.qtdSemestres = qtdSemestres;
    }

    public Integer getPlanoc() {
        return planoc;
    }

    public void setPlanoc(Integer planoc) {
        this.planoc = planoc;
    }
    
    

    public String getAbreviatura() {
        return abreviatura;
    }

    public void setAbreviatura(String abreviatura) {
        this.abreviatura = abreviatura;
    }

    @XmlTransient
    @JsonIgnore
    public List<Listaadmissao> getListaadmissaoList() {
        return listaadmissaoList;
    }

    public void setListaadmissaoList(List<Listaadmissao> listaadmissaoList) {
        this.listaadmissaoList = listaadmissaoList;
    }
    
     public Curso getCurso1() {
        return curso1;
    }

    public void setCurso1(Curso curso1) {
        this.curso1 = curso1;
    }

    @XmlTransient
    @JsonIgnore
    public List<Ingressotransferencia> getIngressotransferenciaList() {
        return ingressotransferenciaList;
    }

    public void setIngressotransferenciaList(List<Ingressotransferencia> ingressotransferenciaList) {
        this.ingressotransferenciaList = ingressotransferenciaList;
    }
    
     @XmlTransient
    public List<Planocurricular> getPlanocurricularList() {
        return planocurricularList;
    }

    public void setPlanocurricularList(List<Planocurricular> planocurricularList) {
        this.planocurricularList = planocurricularList;
    }

    @XmlTransient
    @JsonIgnore
    public List<Disciplina> getDisciplinaList() {
        return disciplinaList;
    }

    public void setDisciplinaList(List<Disciplina> disciplinaList) {
        this.disciplinaList = disciplinaList;
    }

    @XmlTransient
    @JsonIgnore
    public List<Mudancacurso> getMudancacursoList() {
        return mudancacursoList;
    }

    public void setMudancacursoList(List<Mudancacurso> mudancacursoList) {
        this.mudancacursoList = mudancacursoList;
    }

    @XmlTransient
    @JsonIgnore
    public List<Mudancacurso> getMudancacursoList1() {
        return mudancacursoList1;
    }

    public void setMudancacursoList1(List<Mudancacurso> mudancacursoList1) {
        this.mudancacursoList1 = mudancacursoList1;
    }

    public Faculdade getFaculdade() {
        return faculdade;
    }

    public void setFaculdade(Faculdade faculdade) {
        this.faculdade = faculdade;
    }

    @XmlTransient
    @JsonIgnore
    public List<Matricula> getMatriculaList() {
        return matriculaList;
    }

    public void setMatriculaList(List<Matricula> matriculaList) {
        this.matriculaList = matriculaList;
    }

    @XmlTransient
    @JsonIgnore
    public List<Estudante> getEstudanteList() {
        return estudanteList;
    }

    public void setEstudanteList(List<Estudante> estudanteList) {
        this.estudanteList = estudanteList;
    }

    @XmlTransient
    @JsonIgnore
    public List<Estudante> getEstudanteList1() {
        return estudanteList1;
    }

    public void setEstudanteList1(List<Estudante> estudanteList1) {
        this.estudanteList1 = estudanteList1;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idCurso != null ? idCurso.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Curso)) {
            return false;
        }
        Curso other = (Curso) object;
        if ((this.idCurso == null && other.idCurso != null) || (this.idCurso != null && !this.idCurso.equals(other.idCurso))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return descricao;
    }
    
}
