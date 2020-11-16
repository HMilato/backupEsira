/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package esira.domain;

import java.io.Serializable;
import java.util.Date;
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
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Ussimane
 */
@Entity
@Table(name = "planificacao_ano_lectivo")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PlanificacaoAnoLectivo.findAll", query = "SELECT p FROM PlanificacaoAnoLectivo p")})
public class PlanificacaoAnoLectivo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ano", nullable = false)
    private Integer ano;
    @Column(name = "data_inicio_matricula")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataInicioMatricula;
    @Column(name = "data_final_matricula")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataFinalMatricula;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "taxa_matricula_nacional", precision = 8, scale = 8)
    private Float taxaMatriculaNacional;
    @Column(name = "taxa_matricula_estrangeiro", precision = 8, scale = 8)
    private Float taxaMatriculaEstrangeiro;
    @Column(name = "taxa_exame_externo_nacional", precision = 8, scale = 8)
    private Float taxaExameExternoNacional;
    @Column(name = "taxa_exame_externo_estrangeiro", precision = 8, scale = 8)
    private Float taxaExameExternoEstrangeiro;
    @Column(name = "taxa_recorencia", precision = 8, scale = 8)
    private Float taxaRecorencia;
    @Column(name = "taxa_mudanca_curso_via_exameadmissao_nacional", precision = 8, scale = 8)
    private Float taxaMudancaCursoViaExameadmissaoNacional;
    @Column(name = "taxa_mudanca_curso_exameadmissao_via_estrangeiro", precision = 8, scale = 8)
    private Float taxaMudancaCursoExameadmissaoViaEstrangeiro;
    @Column(name = "taxa_mudanca_curso_exameadmissao_via_requerimento_nacional", precision = 8, scale = 8)
    private Float taxaMudancaCursoExameadmissaoViaRequerimentoNacional;
    @Column(name = "taxa_mudanca_curso_exameadmissao_via_requerimento_estrangeiro", precision = 8, scale = 8)
    private Float taxaMudancaCursoExameadmissaoViaRequerimentoEstrangeiro;
    @Column(name = "taxa_equivalencia_internos", precision = 8, scale = 8)
    private Float taxaEquivalenciaInternos;
    @Column(name = "taxa_equivalencia", precision = 8, scale = 8)
    private Float taxaEquivalencia;
    @Column(name = "taxa_de_pedido_revisao_exame_escrito", precision = 8, scale = 8)
    private Float taxaDePedidoRevisaoExameEscrito;
    @Column(name = "taxa_de_pedido_revisao_provas_trabalhos_frequencia", precision = 8, scale = 8)
    private Float taxaDePedidoRevisaoProvasTrabalhosFrequencia;
    @Column(name = "taxa_de_pedido_segunda_chamada_prova_avaliacao_frequencia", precision = 8, scale = 8)
    private Float taxaDePedidoSegundaChamadaProvaAvaliacaoFrequencia;
    @Column(name = "taxa_de_pedido_declaracao_frequencia", precision = 8, scale = 8)
    private Float taxaDePedidoDeclaracaoFrequencia;
    @Column(name = "taxa_de_pedido_declaracao_cadeiras_feitas", precision = 8, scale = 8)
    private Float taxaDePedidoDeclaracaoCadeirasFeitas;
    @Column(name = "taxa_de_pedido_certificado_plano_tematico", precision = 8, scale = 8)
    private Float taxaDePedidoCertificadoPlanoTematico;
    @Column(name = "taxa_de_pedido_certificado_habilitacoes", precision = 8, scale = 8)
    private Float taxaDePedidoCertificadoHabilitacoes;
    @Column(name = "taxa_de_pedido_diploma", precision = 8, scale = 8)
    private Float taxaDePedidoDiploma;
    @Column(name = "taxa_de_pedido_levantamento_suspensao", precision = 8, scale = 8)
    private Float taxaDePedidoLevantamentoSuspensao;
    @Column(name = "taxa_de_mudancao_turno", precision = 8, scale = 8)
    private Float taxaDeMudancaoTurno;
    @Column(name = "percentagem_multa_matricula_15dias", precision = 8, scale = 8)
    private Float percentagemMultaMatricula15dias;
    @Column(name = "percentagem_multa_matricula_30dias", precision = 8, scale = 8)
    private Float percentagemMultaMatricula30dias;
    @Column(name = "datainicio_inscricao")
    @Temporal(TemporalType.TIMESTAMP)
    private Date datainicioInscricao;
    @Column(name = "data_fim_i_e1")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataFimIE1;
    @Column(name = "data_fim_i_e2")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataFimIE2;
    @Column(name = "data_fim_matricula_e2")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataFimMatriculaE2;
    @Column(name = "percentagem_multa_inscricao_15dias", precision = 8, scale = 8)
    private Float percentagemMultaInscricao15dias;
    @Column(name = "percentagem_multa_inscricao_30dias", precision = 8, scale = 8)
    private Float percentagemMultaInscricao30dias;
    @Column(name = "taxa_inscricao_nacional", precision = 8, scale = 8)
    private Float taxaInscricaoNacional;
    @Column(name = "taxa_inscricao_estrangeiro", precision = 8, scale = 8)
    private Float taxaInscricaoEstrangeiro;
    @Column(name = "datainicio_inscricao2")
    @Temporal(TemporalType.TIMESTAMP)
    private Date datainicioInscricao2;
    @Column(name = "data_fim_e1")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataFimE1;
    @Column(name = "data_fim_e2")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataFimE2;
    @Column(name = "sem1i")
    @Temporal(TemporalType.TIMESTAMP)
    private Date sem1i;
    @Column(name = "sem1f")
    @Temporal(TemporalType.TIMESTAMP)
    private Date sem1f;
    @Column(name = "sem2i")
    @Temporal(TemporalType.TIMESTAMP)
    private Date sem2i;
    @Column(name = "sem2f")
    @Temporal(TemporalType.TIMESTAMP)
    private Date sem2f;
    @Column(name = "dm1")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dm1;
    @Column(name = "dma1")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dma1;
    @Column(name = "di1")
    @Temporal(TemporalType.TIMESTAMP)
    private Date di1;
    @Column(name = "anolectivo")
    private Integer anolectivo;
    @JoinColumn(name = "faculdade", referencedColumnName = "id_faculdade")
    @ManyToOne(fetch = FetchType.LAZY)
    private Faculdade faculdade;

    public PlanificacaoAnoLectivo() {
    }

    public PlanificacaoAnoLectivo(Integer ano) {
        this.ano = ano;
    }

    public Integer getAno() {
        return ano;
    }

    public void setAno(Integer ano) {
        this.ano = ano;
    }

    public Date getDataInicioMatricula() {
        return dataInicioMatricula;
    }

    public void setDataInicioMatricula(Date dataInicioMatricula) {
        this.dataInicioMatricula = dataInicioMatricula;
    }

    public Date getDataFinalMatricula() {
        return dataFinalMatricula;
    }

    public void setDataFinalMatricula(Date dataFinalMatricula) {
        this.dataFinalMatricula = dataFinalMatricula;
    }

    public Float getTaxaMatriculaNacional() {
        return taxaMatriculaNacional;
    }

    public void setTaxaMatriculaNacional(Float taxaMatriculaNacional) {
        this.taxaMatriculaNacional = taxaMatriculaNacional;
    }

    public Float getTaxaMatriculaEstrangeiro() {
        return taxaMatriculaEstrangeiro;
    }

    public void setTaxaMatriculaEstrangeiro(Float taxaMatriculaEstrangeiro) {
        this.taxaMatriculaEstrangeiro = taxaMatriculaEstrangeiro;
    }

    public Float getTaxaExameExternoNacional() {
        return taxaExameExternoNacional;
    }

    public void setTaxaExameExternoNacional(Float taxaExameExternoNacional) {
        this.taxaExameExternoNacional = taxaExameExternoNacional;
    }

    public Float getTaxaExameExternoEstrangeiro() {
        return taxaExameExternoEstrangeiro;
    }

    public void setTaxaExameExternoEstrangeiro(Float taxaExameExternoEstrangeiro) {
        this.taxaExameExternoEstrangeiro = taxaExameExternoEstrangeiro;
    }

    public Float getTaxaRecorencia() {
        return taxaRecorencia;
    }

    public void setTaxaRecorencia(Float taxaRecorencia) {
        this.taxaRecorencia = taxaRecorencia;
    }

    public Float getTaxaMudancaCursoViaExameadmissaoNacional() {
        return taxaMudancaCursoViaExameadmissaoNacional;
    }

    public void setTaxaMudancaCursoViaExameadmissaoNacional(Float taxaMudancaCursoViaExameadmissaoNacional) {
        this.taxaMudancaCursoViaExameadmissaoNacional = taxaMudancaCursoViaExameadmissaoNacional;
    }

    public Float getTaxaMudancaCursoExameadmissaoViaEstrangeiro() {
        return taxaMudancaCursoExameadmissaoViaEstrangeiro;
    }

    public void setTaxaMudancaCursoExameadmissaoViaEstrangeiro(Float taxaMudancaCursoExameadmissaoViaEstrangeiro) {
        this.taxaMudancaCursoExameadmissaoViaEstrangeiro = taxaMudancaCursoExameadmissaoViaEstrangeiro;
    }

    public Float getTaxaMudancaCursoExameadmissaoViaRequerimentoNacional() {
        return taxaMudancaCursoExameadmissaoViaRequerimentoNacional;
    }

    public void setTaxaMudancaCursoExameadmissaoViaRequerimentoNacional(Float taxaMudancaCursoExameadmissaoViaRequerimentoNacional) {
        this.taxaMudancaCursoExameadmissaoViaRequerimentoNacional = taxaMudancaCursoExameadmissaoViaRequerimentoNacional;
    }

    public Float getTaxaMudancaCursoExameadmissaoViaRequerimentoEstrangeiro() {
        return taxaMudancaCursoExameadmissaoViaRequerimentoEstrangeiro;
    }

    public void setTaxaMudancaCursoExameadmissaoViaRequerimentoEstrangeiro(Float taxaMudancaCursoExameadmissaoViaRequerimentoEstrangeiro) {
        this.taxaMudancaCursoExameadmissaoViaRequerimentoEstrangeiro = taxaMudancaCursoExameadmissaoViaRequerimentoEstrangeiro;
    }

    public Float getTaxaEquivalenciaInternos() {
        return taxaEquivalenciaInternos;
    }

    public void setTaxaEquivalenciaInternos(Float taxaEquivalenciaInternos) {
        this.taxaEquivalenciaInternos = taxaEquivalenciaInternos;
    }

    public Float getTaxaEquivalencia() {
        return taxaEquivalencia;
    }

    public void setTaxaEquivalencia(Float taxaEquivalencia) {
        this.taxaEquivalencia = taxaEquivalencia;
    }

    public Float getTaxaDePedidoRevisaoExameEscrito() {
        return taxaDePedidoRevisaoExameEscrito;
    }

    public void setTaxaDePedidoRevisaoExameEscrito(Float taxaDePedidoRevisaoExameEscrito) {
        this.taxaDePedidoRevisaoExameEscrito = taxaDePedidoRevisaoExameEscrito;
    }

    public Float getTaxaDePedidoRevisaoProvasTrabalhosFrequencia() {
        return taxaDePedidoRevisaoProvasTrabalhosFrequencia;
    }

    public Faculdade getFaculdade() {
        return faculdade;
    }

    public void setFaculdade(Faculdade faculdade) {
        this.faculdade = faculdade;
    }

    public void setTaxaDePedidoRevisaoProvasTrabalhosFrequencia(Float taxaDePedidoRevisaoProvasTrabalhosFrequencia) {
        this.taxaDePedidoRevisaoProvasTrabalhosFrequencia = taxaDePedidoRevisaoProvasTrabalhosFrequencia;
    }

    public Float getTaxaDePedidoSegundaChamadaProvaAvaliacaoFrequencia() {
        return taxaDePedidoSegundaChamadaProvaAvaliacaoFrequencia;
    }

    public void setTaxaDePedidoSegundaChamadaProvaAvaliacaoFrequencia(Float taxaDePedidoSegundaChamadaProvaAvaliacaoFrequencia) {
        this.taxaDePedidoSegundaChamadaProvaAvaliacaoFrequencia = taxaDePedidoSegundaChamadaProvaAvaliacaoFrequencia;
    }

    public Float getTaxaDePedidoDeclaracaoFrequencia() {
        return taxaDePedidoDeclaracaoFrequencia;
    }

    public void setTaxaDePedidoDeclaracaoFrequencia(Float taxaDePedidoDeclaracaoFrequencia) {
        this.taxaDePedidoDeclaracaoFrequencia = taxaDePedidoDeclaracaoFrequencia;
    }

    public Float getTaxaDePedidoDeclaracaoCadeirasFeitas() {
        return taxaDePedidoDeclaracaoCadeirasFeitas;
    }

    public void setTaxaDePedidoDeclaracaoCadeirasFeitas(Float taxaDePedidoDeclaracaoCadeirasFeitas) {
        this.taxaDePedidoDeclaracaoCadeirasFeitas = taxaDePedidoDeclaracaoCadeirasFeitas;
    }

    public Float getTaxaDePedidoCertificadoPlanoTematico() {
        return taxaDePedidoCertificadoPlanoTematico;
    }

    public void setTaxaDePedidoCertificadoPlanoTematico(Float taxaDePedidoCertificadoPlanoTematico) {
        this.taxaDePedidoCertificadoPlanoTematico = taxaDePedidoCertificadoPlanoTematico;
    }

    public Float getTaxaDePedidoCertificadoHabilitacoes() {
        return taxaDePedidoCertificadoHabilitacoes;
    }

    public void setTaxaDePedidoCertificadoHabilitacoes(Float taxaDePedidoCertificadoHabilitacoes) {
        this.taxaDePedidoCertificadoHabilitacoes = taxaDePedidoCertificadoHabilitacoes;
    }

    public Float getTaxaDePedidoDiploma() {
        return taxaDePedidoDiploma;
    }

    public void setTaxaDePedidoDiploma(Float taxaDePedidoDiploma) {
        this.taxaDePedidoDiploma = taxaDePedidoDiploma;
    }

    public Float getTaxaDePedidoLevantamentoSuspensao() {
        return taxaDePedidoLevantamentoSuspensao;
    }

    public void setTaxaDePedidoLevantamentoSuspensao(Float taxaDePedidoLevantamentoSuspensao) {
        this.taxaDePedidoLevantamentoSuspensao = taxaDePedidoLevantamentoSuspensao;
    }

    public Float getTaxaDeMudancaoTurno() {
        return taxaDeMudancaoTurno;
    }

    public void setTaxaDeMudancaoTurno(Float taxaDeMudancaoTurno) {
        this.taxaDeMudancaoTurno = taxaDeMudancaoTurno;
    }

    public Float getPercentagemMultaMatricula15dias() {
        return percentagemMultaMatricula15dias;
    }

    public void setPercentagemMultaMatricula15dias(Float percentagemMultaMatricula15dias) {
        this.percentagemMultaMatricula15dias = percentagemMultaMatricula15dias;
    }

    public Float getPercentagemMultaMatricula30dias() {
        return percentagemMultaMatricula30dias;
    }

    public void setPercentagemMultaMatricula30dias(Float percentagemMultaMatricula30dias) {
        this.percentagemMultaMatricula30dias = percentagemMultaMatricula30dias;
    }

    public Date getDatainicioInscricao() {
        return datainicioInscricao;
    }

    public void setDatainicioInscricao(Date datainicioInscricao) {
        this.datainicioInscricao = datainicioInscricao;
    }

    public Date getDataFimIE1() {
        return dataFimIE1;
    }

    public void setDataFimIE1(Date dataFimIE1) {
        this.dataFimIE1 = dataFimIE1;
    }

    public Date getDataFimIE2() {
        return dataFimIE2;
    }

    public void setDataFimIE2(Date dataFimIE2) {
        this.dataFimIE2 = dataFimIE2;
    }

    public Date getDataFimMatriculaE2() {
        return dataFimMatriculaE2;
    }

    public void setDataFimMatriculaE2(Date dataFimMatriculaE2) {
        this.dataFimMatriculaE2 = dataFimMatriculaE2;
    }

    public Float getPercentagemMultaInscricao15dias() {
        return percentagemMultaInscricao15dias;
    }

    public void setPercentagemMultaInscricao15dias(Float percentagemMultaInscricao15dias) {
        this.percentagemMultaInscricao15dias = percentagemMultaInscricao15dias;
    }

    public Float getPercentagemMultaInscricao30dias() {
        return percentagemMultaInscricao30dias;
    }

    public void setPercentagemMultaInscricao30dias(Float percentagemMultaInscricao30dias) {
        this.percentagemMultaInscricao30dias = percentagemMultaInscricao30dias;
    }

    public Float getTaxaInscricaoNacional() {
        return taxaInscricaoNacional;
    }

    public void setTaxaInscricaoNacional(Float taxaInscricaoNacional) {
        this.taxaInscricaoNacional = taxaInscricaoNacional;
    }

    public Float getTaxaInscricaoEstrangeiro() {
        return taxaInscricaoEstrangeiro;
    }

    public void setTaxaInscricaoEstrangeiro(Float taxaInscricaoEstrangeiro) {
        this.taxaInscricaoEstrangeiro = taxaInscricaoEstrangeiro;
    }

    public Date getDatainicioInscricao2() {
        return datainicioInscricao2;
    }

    public void setDatainicioInscricao2(Date datainicioInscricao2) {
        this.datainicioInscricao2 = datainicioInscricao2;
    }

    public Date getDataFimE1() {
        return dataFimE1;
    }

    public void setDataFimE1(Date dataFimE1) {
        this.dataFimE1 = dataFimE1;
    }

    public Date getDataFimE2() {
        return dataFimE2;
    }

    public void setDataFimE2(Date dataFimE2) {
        this.dataFimE2 = dataFimE2;
    }

    public Date getSem1i() {
        return sem1i;
    }

    public void setSem1i(Date sem1i) {
        this.sem1i = sem1i;
    }

    public Date getSem1f() {
        return sem1f;
    }

    public void setSem1f(Date sem1f) {
        this.sem1f = sem1f;
    }

    public Date getSem2i() {
        return sem2i;
    }

    public void setSem2i(Date sem2i) {
        this.sem2i = sem2i;
    }

    public Date getSem2f() {
        return sem2f;
    }

    public void setSem2f(Date sem2f) {
        this.sem2f = sem2f;
    }

    public Date getDm1() {
        return dm1;
    }

    public void setDm1(Date dm1) {
        this.dm1 = dm1;
    }

    public Date getDma1() {
        return dma1;
    }

    public void setDma1(Date dma1) {
        this.dma1 = dma1;
    }

    public Date getDi1() {
        return di1;
    }

    public void setDi1(Date di1) {
        this.di1 = di1;
    }

    public Integer getAnolectivo() {
        return anolectivo;
    }

    public void setAnolectivo(Integer anolectivo) {
        this.anolectivo = anolectivo;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ano != null ? ano.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PlanificacaoAnoLectivo)) {
            return false;
        }
        PlanificacaoAnoLectivo other = (PlanificacaoAnoLectivo) object;
        if ((this.ano == null && other.ano != null) || (this.ano != null && !this.ano.equals(other.ano))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "esira.domain.PlanificacaoAnoLectivo[ ano=" + ano + " ]";
    }

}
