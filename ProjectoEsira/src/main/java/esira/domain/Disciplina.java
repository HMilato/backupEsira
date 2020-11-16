/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package esira.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Ussimane
 */
@Entity
@Table(name = "disciplina")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Disciplina.findAll", query = "SELECT d FROM Disciplina d"),
    @NamedQuery(name = "Disciplina.findByIdDisc", query = "SELECT d FROM Disciplina d WHERE d.idDisc = :idDisc"),
    @NamedQuery(name = "Disciplina.findByCodigo", query = "SELECT d FROM Disciplina d WHERE d.codigo = :codigo"),
    @NamedQuery(name = "Disciplina.findByNome", query = "SELECT d FROM Disciplina d WHERE d.nome = :nome"),
    @NamedQuery(name = "Disciplina.findByDepartamento", query = "SELECT d FROM Disciplina d WHERE d.departamento = :departamento"),
    @NamedQuery(name = "Disciplina.findByObjcetivoGeral", query = "SELECT d FROM Disciplina d WHERE d.objcetivoGeral = :objcetivoGeral"),
    @NamedQuery(name = "Disciplina.findByNivel", query = "SELECT d FROM Disciplina d WHERE d.nivel = :nivel"),
    @NamedQuery(name = "Disciplina.findBySemestre", query = "SELECT d FROM Disciplina d WHERE d.semestre = :semestre"),
    @NamedQuery(name = "Disciplina.findByCredito", query = "SELECT d FROM Disciplina d WHERE d.credito = :credito"),
    @NamedQuery(name = "Disciplina.findByAbreviatura", query = "SELECT d FROM Disciplina d WHERE d.abreviatura = :abreviatura"),
    @NamedQuery(name = "Disciplina.findByHorast", query = "SELECT d FROM Disciplina d WHERE d.horast = :horast"),
    @NamedQuery(name = "Disciplina.findByHorasp", query = "SELECT d FROM Disciplina d WHERE d.horasp = :horasp"),
    @NamedQuery(name = "Disciplina.findByNatraso", query = "SELECT d FROM Disciplina d WHERE d.natraso = :natraso")})
public class Disciplina implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_disc", nullable = false)
    private Long idDisc;
    @Column(name = "codigo", length = 32)
    private String codigo;
    @Column(name = "nome", length = 200)
    private String nome;
    @Column(name = "departamento", length = 45)
    private String departamento;
    @Column(name = "objcetivo_geral", length = 200)
    private String objcetivoGeral;
    @Column(name = "nivel")
    private Integer nivel;
    @Column(name = "semestre")
    private Integer semestre;
    @Column(name = "credito")
    private Integer credito;
    @Column(name = "abreviatura", length = 11)
    private String abreviatura;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "horast", precision = 8, scale = 8)
    private Float horast;
    @Column(name = "horasp", precision = 8, scale = 8)
    private Float horasp;
    @Column(name = "natraso", length = 64)
    private String natraso;
    
    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "disciplina", fetch = FetchType.LAZY)
    private List<Precedencia> precedenciaList;
    
    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "disciplina1", fetch = FetchType.LAZY)
    private List<Precedencia> precedenciaList1;
    
    @JsonIgnore
    @JoinColumn(name = "area_cientifica", referencedColumnName = "idarea")
    @ManyToOne(fetch = FetchType.LAZY)
    private Areacientifica areaCientifica;
    
    @JsonIgnore
    @JoinColumn(name = "caracter", referencedColumnName = "id_caracter")
    @ManyToOne(fetch = FetchType.LAZY)
    private Caracter caracter;
    
    @JsonIgnore
    @JoinColumn(name = "curso", referencedColumnName = "id_curso")
    @ManyToOne(fetch = FetchType.LAZY)
    private Curso curso;
    
    @JsonIgnore
    @JoinColumn(name = "id_periodo", referencedColumnName = "id_periodo")
    @ManyToOne(fetch = FetchType.LAZY)
    private Periodo idPeriodo;
    
    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "disciplina", fetch = FetchType.LAZY)
    private List<Lecciona> leccionaList;
    
    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "disciplina", fetch = FetchType.LAZY)
    private List<Inscricaodisciplina> inscricaodisciplinaList;
    
    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "disciplina", fetch = FetchType.LAZY)
    private List<Pauta> pautaList;
    
    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "disciplina", fetch = FetchType.LAZY)
    private List<Planoavaliacao> planoavaliacaoList;
    
    @JsonIgnore
    @Column(name = "planoc")
    private Integer planoc;
    
    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "disciplina", fetch = FetchType.LAZY)
    private List<Equivalencia> equivalenciaList;
    
    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "disciplina1", fetch = FetchType.LAZY)
    private List<Equivalencia> equivalenciaList1;

    public Disciplina() {
    }

    public Disciplina(Long idDisc) {
        this.idDisc = idDisc;
    }

    public Long getIdDisc() {
        return idDisc;
    }

    public void setIdDisc(Long idDisc) {
        this.idDisc = idDisc;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDepartamento() {
        return departamento;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }

    public Integer getPlanoc() {
        return planoc;
    }

    public void setPlanoc(Integer planoc) {
        this.planoc = planoc;
    }

    public String getObjcetivoGeral() {
        return objcetivoGeral;
    }

    public void setObjcetivoGeral(String objcetivoGeral) {
        this.objcetivoGeral = objcetivoGeral;
    }

    public Integer getNivel() {
        return nivel;
    }

    public void setNivel(Integer nivel) {
        this.nivel = nivel;
    }

    public Integer getSemestre() {
        return semestre;
    }

    public void setSemestre(Integer semestre) {
        this.semestre = semestre;
    }

    public Integer getCredito() {
        return credito;
    }

    public void setCredito(Integer credito) {
        this.credito = credito;
    }

    public String getAbreviatura() {
        return abreviatura;
    }

    public void setAbreviatura(String abreviatura) {
        this.abreviatura = abreviatura;
    }

    public Float getHorast() {
        return horast;
    }

    public void setHorast(Float horast) {
        this.horast = horast;
    }

    public Float getHorasp() {
        return horasp;
    }

    public void setHorasp(Float horasp) {
        this.horasp = horasp;
    }

    public String getNatraso() {
        return natraso;
    }

    public void setNatraso(String natraso) {
        this.natraso = natraso;
    }

    @XmlTransient
    public List<Precedencia> getPrecedenciaList() {
        return precedenciaList;
    }

    public void setPrecedenciaList(List<Precedencia> precedenciaList) {
        this.precedenciaList = precedenciaList;
    }

    @XmlTransient
    public List<Precedencia> getPrecedenciaList1() {
        return precedenciaList1;
    }

    public void setPrecedenciaList1(List<Precedencia> precedenciaList1) {
        this.precedenciaList1 = precedenciaList1;
    }
    
    @XmlTransient
    public List<Planoavaliacao> getPlanoavaliacaoList() {
        return planoavaliacaoList;
    }

    public void setPlanoavaliacaoList(List<Planoavaliacao> planoavaliacaoList) {
        this.planoavaliacaoList = planoavaliacaoList;
    }

    public Areacientifica getAreaCientifica() {
        return areaCientifica;
    }

    public void setAreaCientifica(Areacientifica areaCientifica) {
        this.areaCientifica = areaCientifica;
    }

    public Caracter getCaracter() {
        return caracter;
    }

    public void setCaracter(Caracter caracter) {
        this.caracter = caracter;
    }

    public Curso getCurso() {
        return curso;
    }

    public void setCurso(Curso curso) {
        this.curso = curso;
    }

    public Periodo getIdPeriodo() {
        return idPeriodo;
    }

    public void setIdPeriodo(Periodo idPeriodo) {
        this.idPeriodo = idPeriodo;
    }

    @XmlTransient
    public List<Lecciona> getLeccionaList() {
        return leccionaList;
    }

    public void setLeccionaList(List<Lecciona> leccionaList) {
        this.leccionaList = leccionaList;
    }

    @XmlTransient
    public List<Inscricaodisciplina> getInscricaodisciplinaList() {
        return inscricaodisciplinaList;
    }

    public void setInscricaodisciplinaList(List<Inscricaodisciplina> inscricaodisciplinaList) {
        this.inscricaodisciplinaList = inscricaodisciplinaList;
    }

    @XmlTransient
    public List<Pauta> getPautaList() {
        return pautaList;
    }

    public void setPautaList(List<Pauta> pautaList) {
        this.pautaList = pautaList;
    }
    
    @XmlTransient
    public List<Equivalencia> getEquivalenciaList() {
        return equivalenciaList;
    }

    public void setEquivalenciaList(List<Equivalencia> equivalenciaList) {
        this.equivalenciaList = equivalenciaList;
    }

    @XmlTransient
    public List<Equivalencia> getEquivalenciaList1() {
        return equivalenciaList1;
    }

    public void setEquivalenciaList1(List<Equivalencia> equivalenciaList1) {
        this.equivalenciaList1 = equivalenciaList1;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idDisc != null ? idDisc.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Disciplina)) {
            return false;
        }
        Disciplina other = (Disciplina) object;
        if ((this.idDisc == null && other.idDisc != null) || (this.idDisc != null && !this.idDisc.equals(other.idDisc))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return nome;
    }
    
}
